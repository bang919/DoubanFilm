package com.bigbang.doubanfilm.common;

import android.content.Context;

/**
 * Created by Leo on 2017/5/18.
 * presenter 基类
 */

public class BasePresenter<V> {

    protected V mView;
    protected Context mContext;

    public BasePresenter(Context context, V view) {
        mView = view;
        mContext = context;
    }


    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }
}
