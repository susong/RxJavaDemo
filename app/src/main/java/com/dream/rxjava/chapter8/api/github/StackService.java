package com.dream.rxjava.chapter8.api.github;

import com.dream.rxjava.chapter8.api.github.models.UsersResponse;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/25 上午11:24
 * Description: RxJavaDemo
 */
public interface StackService {

    @GET("/2.2/users?order=desc&pagesize=10&sort=reputation&site=stackoverflow")
    Observable<UsersResponse> getTenMostPopularSOusers();

    @GET("/2.2/users?order=desc&sort=reputation&site=stackoverflow")
    Observable<UsersResponse> getMostPopularSOusers(@Query("pagesize") int howmany);
}
