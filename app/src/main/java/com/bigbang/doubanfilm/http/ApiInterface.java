package com.bigbang.doubanfilm.http;


import com.bigbang.doubanfilm.bean.response.SearchResponseBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/8/17.
 */

public interface ApiInterface {

    @Streaming
    @GET
        //下载文件
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    @GET("v2/movie/search")
    Observable<SearchResponseBean> search(@Query("q") String query, @Query("tag") String tag, @Query("start") Integer start, @Query("count") Integer count);

    @GET("v2/movie/top250")
    Observable<SearchResponseBean> getTop250(@Query("start") Integer start, @Query("count") Integer count);
}
