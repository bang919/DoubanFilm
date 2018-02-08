package com.bigbang.doubanfilm.presenter;

import android.content.Context;
import android.util.Log;

import com.bigbang.doubanfilm.bean.request.SearchRequestBean;
import com.bigbang.doubanfilm.bean.response.SearchResponseBean;
import com.bigbang.doubanfilm.common.BasePresenter;
import com.bigbang.doubanfilm.model.MainModel;
import com.bigbang.doubanfilm.view.MainView;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/2/7.
 */

public class MainPresenter extends BasePresenter<MainView> {

    private MainModel mMainModel;

    public MainPresenter(Context context, MainView view) {
        super(context, view);
        mMainModel = new MainModel();
    }

    public void search(SearchRequestBean searchRequestBean, int yearAfter, int yearBefore, int scoreAfter, int scoreBefore) {
        mMainModel.search(searchRequestBean, new Observer<SearchResponseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(SearchResponseBean searchResponseBean) {
                Log.d("bigbangc", "onNext() called with: searchResponseBean = [" + searchResponseBean + "]");
                mView.onSearchResponse(searchResponseBean);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("bigbangc", "onError() called with: e = [" + e + "]");
            }

            @Override
            public void onComplete() {

            }
        }, yearAfter, yearBefore, scoreAfter, scoreBefore);
    }
}
