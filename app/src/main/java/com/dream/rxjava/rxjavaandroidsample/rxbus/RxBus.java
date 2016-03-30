package com.dream.rxjava.rxjavaandroidsample.rxbus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/30 上午10:52
 * Description: RxJavaDemo
 * courtesy: https://gist.github.com/benjchristensen/04eef9ca0851f3a5d7bf
 * https://drakeet.me/rxbus
 */
public class RxBus {

    //    private final PublishSubject<Object> mBus = PublishSubject.create();
    // If multiple threads are going to emit events to this
    // then it must be made thread-safe like this instead
    private final Subject<Object, Object> mBus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        mBus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }
}
