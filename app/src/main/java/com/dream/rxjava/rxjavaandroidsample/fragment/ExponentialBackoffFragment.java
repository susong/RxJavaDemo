package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dream.rxjava.R;
import com.dream.rxjava.rxjavaandroidsample.RxUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.observables.MathObservable;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/31 上午10:26
 * Description: RxJavaDemo
 */
public class ExponentialBackoffFragment extends BaseFragment {
    @Bind(R.id.btn_eb_retry) Button mBtnEbRetry;
    @Bind(R.id.btn_eb_delay) Button mBtnEbDelay;

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_exponential_backoff, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCompositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(mCompositeSubscription);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(mCompositeSubscription);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_eb_retry, R.id.btn_eb_delay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_eb_retry:
                mLogList = new ArrayList<>();
                mLogAdapter.clear();
                mCompositeSubscription.add(
                        Observable.error(new RuntimeException("testing"))
                                .retryWhen(new RetryWithDelay(5, 1000))
                                .doOnSubscribe(new Action0() {
                                    @Override
                                    public void call() {
                                        log("Attempting the impossible 5 times in intervals of 1s");
                                    }
                                })
                                .subscribe(new Subscriber<Object>() {
                                    @Override
                                    public void onCompleted() {
                                        Timber.d("on Completed");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        log("Error: I give up!");
                                    }

                                    @Override
                                    public void onNext(Object o) {
                                        Timber.d("on Next");
                                    }
                                })
                );
                break;
            case R.id.btn_eb_delay:
                mLogList = new ArrayList<>();
                mLogAdapter.clear();
                mCompositeSubscription.add(
                        Observable.range(1, 4)
                                .delay(new Func1<Integer, Observable<Integer>>() {
                                    @Override
                                    public Observable<Integer> call(Integer integer) {
                                        // Rx-y way of doing the Fibonnaci :P
                                        return MathObservable.sumInteger(Observable.range(1, integer))
                                                .flatMap(new Func1<Integer, Observable<Integer>>() {
                                                    @Override
                                                    public Observable<Integer> call(Integer targetSecondDelay) {
                                                        return Observable.just(integer)
                                                                .delay(targetSecondDelay, TimeUnit.SECONDS);
                                                    }
                                                });
                                    }
                                })
                                .doOnSubscribe(new Action0() {
                                    @Override
                                    public void call() {
                                        log(String.format("Execute 4 tasks with delay - time now: [xx:%02d]",
                                                getSecondHand()));
                                    }
                                })//
                                .subscribe(new Observer<Integer>() {
                                    @Override
                                    public void onCompleted() {
                                        Timber.d("onCompleted");
                                        log("Completed");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Timber.d(e, "arrrr. Error");
                                        log("Error");
                                    }

                                    @Override
                                    public void onNext(Integer integer) {
                                        Timber.d("executing Task %d [xx:%02d]", integer, getSecondHand());
                                        log(String.format("executing Task %d  [xx:%02d]",
                                                integer,
                                                getSecondHand()));

                                    }
                                })
                );
                break;
        }
    }

    private int getSecondHand() {
        long millis = System.currentTimeMillis();
        return (int) (TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    // -----------------------------------------------------------------------------------

    // CAUTION:
    // --------------------------------------
    // THIS notificationHandler class HAS NO BUSINESS BEING non-static
    // I ONLY did this cause i wanted access to the `_log` method from inside here
    // for the purpose of demonstration. In the real world, make it static and LET IT BE!!

    // It's 12am in the morning and i feel lazy dammit !!!

    //public static class RetryWithDelay
    public class RetryWithDelay
            implements Func1<Observable<? extends Throwable>, Observable<?>> {

        private int mMaxRetries;
        private int mRetryDelayMillis;
        private int mRetryCount;

        public RetryWithDelay(int maxRetries, int retryDelayMillis) {
            mMaxRetries = maxRetries;
            mRetryDelayMillis = retryDelayMillis;
            mRetryCount = 0;
        }

        // this is a notificationhandler, all that is cared about here
        // is the emission "type" not emission "content"
        // only onNext triggers a re-subscription (onError + onComplete kills it)
        @Override
        public Observable<?> call(Observable<? extends Throwable> observable) {
            // it is critical to use inputObservable in the chain for the result
            // ignoring it and doing your own thing will break the sequence
            return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                @Override
                public Observable<?> call(Throwable throwable) {
                    if (++mRetryCount < mMaxRetries) {
                        // When this Observable calls onNext, the original
                        // Observable will be retried (i.e. re-subscribed)
                        Timber.d("Retrying in %d ms", mRetryCount * mRetryDelayMillis);
                        log(String.format("Retrying in %d ms", mRetryCount * mRetryDelayMillis));
                        return Observable.timer(mRetryCount * mRetryDelayMillis, TimeUnit.MILLISECONDS);
                    }
                    Timber.d("Argh! i give up");
                    // Max retries hit. Pass an error so the chain is forcibly completed
                    // only onNext triggers a re-subscription (onError + onComplete kills it)
                    return Observable.error(throwable);
                }
            });
        }
    }
}
