package com.bigbang.doubanfilm.view;

import com.bigbang.doubanfilm.bean.response.SearchResponseBean;

/**
 * Created by Administrator on 2018/2/7.
 */

public interface MainView {
    void onSearchResponse(SearchResponseBean searchResponseBean);

    void onSearchError(String error);
}
