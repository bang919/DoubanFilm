package com.bigbang.doubanfilm.model;

import com.bigbang.doubanfilm.bean.request.SearchRequestBean;
import com.bigbang.doubanfilm.bean.response.SearchResponseBean;
import com.bigbang.doubanfilm.http.HttpClient;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.bigbang.doubanfilm.common.Constants.BASIC_THERMOMETER;

/**
 * Created by Administrator on 2018/2/7.
 */

public class MainModel {

    private LifecycleProvider<ActivityEvent> activityLifecycleProvider;

    public MainModel(LifecycleProvider<ActivityEvent> activityLifecycleProvider) {
        this.activityLifecycleProvider = activityLifecycleProvider;
    }

    public void search(SearchRequestBean bean, Observer<? super SearchResponseBean> observer, final int yearAfter, final int yearBefore,
                       final float scoreAfter, final float scoreBefore, final float hot) {
        Observable<SearchResponseBean> observable = HttpClient.getApiInterface().search(bean.getQ(), bean.getTag(), bean.getStart(), 20);
        if (bean.getStart() == null) {
            bean.setStart(0);
        }
        if (bean.getCount() == null) {
            bean.setCount(0);
        }
        for (int i = bean.getStart() + 20; i < bean.getCount(); i = i + 20) {
            int count = i + 20 <= bean.getCount() ? 20 : bean.getCount() - i;
            observable = zipTwoSearchRequest(observable, HttpClient.getApiInterface().search(bean.getQ(), bean.getTag(), i, count));
        }
        observable.flatMap(new Function<SearchResponseBean, Observable<SearchResponseBean>>() {
            @Override
            public Observable<SearchResponseBean> apply(final SearchResponseBean bean) throws Exception {
                return Observable.create(new ObservableOnSubscribe<SearchResponseBean>() {
                    @Override
                    public void subscribe(ObservableEmitter<SearchResponseBean> emitter) throws Exception {
                        ArrayList<String> tempStrings = new ArrayList<>();
                        ArrayList<SearchResponseBean.SubjectsBean> subjectsReturn = new ArrayList<>();
                        ArrayList<SearchResponseBean.SubjectsBean> subjects = bean.getSubjects();
                        for (SearchResponseBean.SubjectsBean subjectsBean : subjects) {
                            int year = Integer.valueOf(subjectsBean.getYear());
                            double average = subjectsBean.getRating().getAverage();
                            float subjectHot = subjectsBean.getCollect_count() / BASIC_THERMOMETER;
                            if (year >= yearAfter && year <= yearBefore && average >= scoreAfter && average <= scoreBefore
                                    && subjectHot > hot && tempStrings.indexOf(subjectsBean.getId()) == -1) {
                                tempStrings.add(subjectsBean.getId());
                                subjectsReturn.add(subjectsBean);
                            }
                        }
                        bean.setSubjects(subjectsReturn);
                        emitter.onNext(bean);
                        emitter.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activityLifecycleProvider.<SearchResponseBean>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getTop250(Observer<? super SearchResponseBean> observer) {
        Observable<SearchResponseBean> observable = HttpClient.getApiInterface().getTop250(0,100);

        for (int i = 100; i <= 250; i = i + 100) {
            int count = i + 100 <= 250 ? 100 : 50;
            observable = zipTwoSearchRequest(observable, HttpClient.getApiInterface().getTop250(i,count));
        }
        observable.flatMap(new Function<SearchResponseBean, ObservableSource<SearchResponseBean>>() {
            @Override
            public ObservableSource<SearchResponseBean> apply(final SearchResponseBean searchResponseBean) throws Exception {

                return Observable.create(new ObservableOnSubscribe<SearchResponseBean>() {
                    @Override
                    public void subscribe(ObservableEmitter<SearchResponseBean> e) throws Exception {
                        ArrayList<SearchResponseBean.SubjectsBean> subjects = searchResponseBean.getSubjects();
                        Collections.sort(subjects, new Comparator<SearchResponseBean.SubjectsBean>() {
                            @Override
                            public int compare(SearchResponseBean.SubjectsBean o1, SearchResponseBean.SubjectsBean o2) {
                                return Integer.valueOf(o2.getYear())-Integer.valueOf(o1.getYear());
                            }
                        });
                        searchResponseBean.setSubjects(subjects);
                        e.onNext(searchResponseBean);
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activityLifecycleProvider.<SearchResponseBean>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);

    }

    private Observable<SearchResponseBean> zipTwoSearchRequest(Observable<SearchResponseBean> observable, Observable<SearchResponseBean> observable2) {
        return Observable.zip(observable, observable2,
                new BiFunction<SearchResponseBean, SearchResponseBean, SearchResponseBean>() {
                    @Override
                    public SearchResponseBean apply(SearchResponseBean searchResponseBean, SearchResponseBean searchResponseBean2) throws Exception {
                        ArrayList<SearchResponseBean.SubjectsBean> subjects1 = searchResponseBean.getSubjects();
                        ArrayList<SearchResponseBean.SubjectsBean> subjects2 = searchResponseBean2.getSubjects();
                        if (subjects1.size() > 0 && subjects2.size() > 0 && !subjects1.get(0).getId().equals(subjects2.get(0).getId())) {
                            subjects1.addAll(searchResponseBean2.getSubjects());
                            searchResponseBean.setSubjects(subjects1);
                            searchResponseBean.setCount(searchResponseBean.getCount() + searchResponseBean2.getCount());
                        }
                        return searchResponseBean;
                    }
                });
    }
}
