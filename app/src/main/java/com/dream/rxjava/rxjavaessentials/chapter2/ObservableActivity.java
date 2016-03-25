package com.dream.rxjava.rxjavaessentials.chapter2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dream.rxjava.R;
import com.dream.rxjava.XLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subjects.AsyncSubject;
import rx.subjects.PublishSubject;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/18 下午11:14
 * Description: RxJavaDemo
 */
public class ObservableActivity extends AppCompatActivity {

    private Subscriber<String> mSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observable);

        mSubscriber = new Subscriber<String>() {

            @Override
            public void onCompleted() {
                XLog.d("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                XLog.d("onError");
                XLog.e(e);
            }

            @Override
            public void onNext(String s) {
                XLog.d("onNext " + s);
            }
        };

        demo8();
    }

    private void demo9() {
//        final PublishSubject<String> subject = PublishSubject.create();
//        final BehaviorSubject<String> subject = BehaviorSubject.create("H");
//        final ReplaySubject<String> subject = ReplaySubject.create();
        final AsyncSubject<String> subject = AsyncSubject.create();
        subject.subscribe(mSubscriber);

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 5; i++) {
                    subscriber.onNext(i + "");
                }
                subscriber.onCompleted();
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {
                subject.onNext("hello");
            }
        }).subscribe();
    }

    private void demo8() {
        // 要先订阅后发送消息，才能收到，否则收不到
        PublishSubject<String> subject = PublishSubject.create();
        subject.subscribe(mSubscriber);
        subject.onNext("Hello");
    }


    private void demo7() {
        Observable.empty().subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                XLog.d("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                XLog.d("onError");
                XLog.e(e);
            }

            @Override
            public void onNext(Object o) {
                XLog.d("onNext " + o);
            }
        });

        Observable.never().subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                XLog.d("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                XLog.d("onError");
                XLog.e(e);
            }

            @Override
            public void onNext(Object o) {
                XLog.d("onNext " + o);
            }
        });

        Observable.error(new RuntimeException("有错误")).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                XLog.d("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                XLog.d("onError");
                XLog.e(e);
            }

            @Override
            public void onNext(Object o) {
                XLog.d("onNext " + o);
            }
        });
    }

    private String helloWorld() {
        return "HelloWorld";
    }

    private void demo6() {

        Observable.just(helloWorld())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        XLog.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        XLog.d("onError");
                        XLog.e(e);
                    }

                    @Override
                    public void onNext(String s) {
                        XLog.d("onNext " + s);
                    }
                });
    }

    private void demo5() {
        Observable<Integer> observable = Observable.from(new Future<Integer>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                XLog.d("cancel");
                return true;
            }

            @Override
            public boolean isCancelled() {
                XLog.d("isCancelled");
                return false;
            }

            @Override
            public boolean isDone() {
                XLog.d("isDone");
                return false;
            }

            @Override
            public Integer get() throws InterruptedException, ExecutionException {
                XLog.d("get");
                return null;
            }

            @Override
            public Integer get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                XLog.d("get");
                return null;
            }
        });

        observable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                XLog.d("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                XLog.d("onError");
                XLog.e(e);
            }

            @Override
            public void onNext(Integer integer) {
                XLog.d("onNext " + integer);
            }
        });
    }

    private void demo4() {
        List<Integer> items = new ArrayList<>();
        items.add(1);
        items.add(11);
        items.add(111);
        items.add(1111);

        Observable<Integer> observable = Observable.from(items);
        observable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                XLog.d("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                XLog.d("onError");
            }

            @Override
            public void onNext(Integer integer) {
                XLog.d("onNext " + integer);
            }
        });


    }

    private void demo3() {
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 5; i++) {
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        });

        Subscription subscription = observable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                XLog.d("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                XLog.d("onError");
            }

            @Override
            public void onNext(Integer integer) {
                XLog.d("onNext " + integer);
            }
        });

    }

    private void demo2() {
        Subscriber<String> subscriber = new Subscriber<String>() {

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
                XLog.d("onNext " + s);
            }
        };
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onCompleted();
            }
        });

        observable.subscribe(subscriber);
    }

    private void demo1() {
        Observer<String> observer = new Observer<String>() {
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
                XLog.d("onNext " + s);
            }
        };

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onCompleted();
            }
        });

        observable.subscribe(observer);
    }
}
