package com.bigbang.doubanfilm.presenter;

import android.content.Context;

import com.bigbang.doubanfilm.bean.request.SearchRequestBean;
import com.bigbang.doubanfilm.bean.response.SearchResponseBean;
import com.bigbang.doubanfilm.common.BasePresenter;
import com.bigbang.doubanfilm.model.MainModel;
import com.bigbang.doubanfilm.utils.ExceptionUtil;
import com.bigbang.doubanfilm.view.MainView;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/2/7.
 */

public class MainPresenter extends BasePresenter<MainView> {

    private MainModel mMainModel;

    public MainPresenter(Context context, MainView view, LifecycleProvider<ActivityEvent> activityLifecycleProvider) {
        super(context, view, activityLifecycleProvider);
        mMainModel = new MainModel(activityLifecycleProvider);
    }

    public void search(SearchRequestBean searchRequestBean, int yearAfter, int yearBefore, int scoreAfter, int scoreBefore, float hot) {
        mMainModel.search(searchRequestBean, new Observer<SearchResponseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(SearchResponseBean searchResponseBean) {
                mView.onSearchResponse(searchResponseBean);
            }

            @Override
            public void onError(Throwable e) {
                mView.onSearchError(ExceptionUtil.getHttpExceptionMessage(e));
            }

            @Override
            public void onComplete() {

            }
        }, yearAfter, yearBefore, scoreAfter, scoreBefore, hot);
    }
}
