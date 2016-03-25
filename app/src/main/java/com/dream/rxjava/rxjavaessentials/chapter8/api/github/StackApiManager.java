package com.dream.rxjava.rxjavaessentials.chapter8.api.github;

import com.dream.rxjava.rxjavaessentials.chapter8.api.github.models.User;
import com.dream.rxjava.rxjavaessentials.chapter8.api.github.models.UsersResponse;

import java.util.List;

import lombok.experimental.Accessors;
import retrofit.RestAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/25 上午11:24
 * Description: RxJavaDemo
 */
@Accessors(prefix = "m")
public class StackApiManager {

    private final StackService mStackService;

    public StackApiManager() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://api.stackexchange.com")
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();
        mStackService = restAdapter.create(StackService.class);
    }

    public Observable<List<User>> getTenMostPopularSOusers() {
        return mStackService.getTenMostPopularSOusers()
                .map(new Func1<UsersResponse, List<User>>() {
                    @Override
                    public List<User> call(UsersResponse usersResponse) {
                        return usersResponse.getUsers();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<User>> getMostPopularSOusers(int howmany) {
        return mStackService.getMostPopularSOusers(howmany)
                .map(new Func1<UsersResponse, List<User>>() {
                    @Override
                    public List<User> call(UsersResponse usersResponse) {
                        return usersResponse.getUsers();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
