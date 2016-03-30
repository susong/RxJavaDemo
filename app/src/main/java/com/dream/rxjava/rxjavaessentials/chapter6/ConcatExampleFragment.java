package com.dream.rxjava.rxjavaessentials.chapter6;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dream.rxjava.XLog;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/31 上午1:17
 * Description: RxJavaDemo
 */
public class ConcatExampleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Observable<String> observable1 = Observable.interval(1, TimeUnit.SECONDS)
                .take(10)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return aLong + " a";
                    }
                });
        Observable<String> observable2 = Observable.interval(1, TimeUnit.SECONDS)
                .take(10)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return aLong + " b";
                    }
                });
        Observable
                .merge(observable1, observable2)
//                .concat(observable1, observable2)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        XLog.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        XLog.d("onError");
                    }

                    @Override
                    public void onNext(String s) {
                        XLog.d("onNext : " + s);
                    }
                });
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
