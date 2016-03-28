package com.dream.rxjava.rxjavaandroidsample.retrofit;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/28 下午10:37
 * Description: RxJavaDemo
 */
public interface GithubApi {
    /**
     * See https://developer.github.com/v3/repos/#list-contributors
     */
    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> contributors(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    /**
     * See https://developer.github.com/v3/users/
     */
    @GET("/users/{user}")
    Observable<User> user(
            @Path("user") String user
    );

    /**
     * See https://developer.github.com/v3/users/
     */
    @GET("/users/{user}")
    User getUser(
            @Path("user") String user
    );
}
