package com.bigbang.doubanfilm.common;

import android.app.Application;
import android.content.Context;


/**
 * Created by Administrator on 2017/11/7.
 */

public class DoubanApplication extends Application {

    private static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = getApplicationContext();
    }

    public static Context getDoubanApplicationContext() {
        return mApplicationContext;
    }

}
