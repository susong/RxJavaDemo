package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dream.rxjava.R;
import com.dream.rxjava.rxjavaandroidsample.RxUtils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/29 下午8:06
 * Description: RxJavaDemo
 */
public class PollingFragment extends BaseFragment {

    @Bind(R.id.btn_start_simple_polling) Button mBtnStartSimplePolling;
    @Bind(R.id.btn_start_increasingly_delayed_polling) Button mBtnStartIncreasinglyDelayedPolling;

    private CompositeSubscription mCompositeSubscription;
    private static final int INITIAL_DELAY = 0;
    private static final int POLLING_INTERVAL = 1000;
    private static final int POLL_COUNT = 8;
    private int mCounter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_polling, container, false);
        ButterKnife.bind(this, layout);
        mCompositeSubscription = new CompositeSubscription();
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxUtils.unsubscribeIfNotNull(mCompositeSubscription);
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_start_simple_polling, R.id.btn_start_increasingly_delayed_polling})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_simple_polling:
                mCompositeSubscription.add(
                        Observable.interval(INITIAL_DELAY, POLLING_INTERVAL, TimeUnit.MILLISECONDS)
                                .map(new Func1<Long, String>() {
                                    @Override
                                    public String call(Long aLong) {
                                        return doNetworkCallAndGetStringResult(aLong);
                                    }
                                })
                                .take(POLL_COUNT)
                                .doOnSubscribe(new Action0() {
                                    @Override
                                    public void call() {
                                        log(String.format("Start simple polling - %s", mCounter));
                                    }
                                })
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        log(String.format(Locale.US, "Executing polled task [%s] now time : [xx:%02d]",
                                                s, getSecondHand()));
                                    }
                                })
                );
                break;
            case R.id.btn_start_increasingly_delayed_polling:
                final int pollingInterval = POLLING_INTERVAL;
                final int pollCount = POLL_COUNT;

                log(String.format(Locale.US, "Start increasingly delayed polling now time: [xx:%02d]",
                        getSecondHand()));
                mCompositeSubscription.add(
                        Observable.just(1)
                                .repeatWhen(new RepeatWithDelay(pollCount, pollingInterval))
                                .subscribe(new Action1<Object>() {
                                    @Override
                                    public void call(Object o) {
                                        log(String.format(Locale.US, "Executing polled task now time : [xx:%02d]",
                                                getSecondHand()));
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable e) {
                                        Timber.d(e, "Error");
                                    }
                                })
                );
                break;
        }
    }
// -----------------------------------------------------------------------------------

    // CAUTION:
    // --------------------------------------
    // THIS notificationHandler class HAS NO BUSINESS BEING non-static
    // I ONLY did this cause i wanted access to the `_log` method from inside here
    // for the purpose of demonstration. In the real world, make it static and LET IT BE!!

    // It's 12am in the morning and i feel lazy dammit !!!

    //public static class RepeatWithDelay
    public class RepeatWithDelay
            implements Func1<Observable<? extends Void>, Observable<?>> {

        private final int mRepeatLimit;
        private final int mPollingInterval;
        private int mRepeatCount = 1;

        RepeatWithDelay(int repeatLimit, int pollingInterval) {
            mPollingInterval = pollingInterval;
            mRepeatLimit = repeatLimit;
        }

        // this is a notificationhandler, all we care about is
        // the emission "type" not emission "content"
        // only onNext triggers a re-subscription

        @Override
        public Observable<?> call(Observable<? extends Void> inputObservable) {

            // it is critical to use inputObservable in the chain for the result
            // ignoring it and doing your own thing will break the sequence

            return inputObservable.flatMap(new Func1<Void, Observable<?>>() {
                @Override
                public Observable<?> call(Void blah) {


                    if (mRepeatCount >= mRepeatLimit) {
                        // terminate the sequence cause we reached the limit
                        log("Completing sequence");
                        return Observable.empty();
                    }

                    // since we don't get an input
                    // we store state in this handler to tell us the point of time we're firing
                    mRepeatCount++;

                    return Observable.timer(mRepeatCount * mPollingInterval,
                            TimeUnit.MILLISECONDS);
                }
            });
        }
    }


    // -----------------------------------------------------------------------------------
    // Method that help wiring up the example (irrelevant to RxJava)

    private String doNetworkCallAndGetStringResult(long attempt) {
        Timber.d("atttempt : " + attempt);
        try {
            if (attempt == 4) {
                // randomly make one event super long so we test that the repeat logic waits
                // and accounts for this.
                Thread.sleep(9000);
            } else {
                Thread.sleep(3000);
            }

        } catch (InterruptedException e) {
            Timber.d("Operation was interrupted");
        }
        mCounter++;

        return String.valueOf(mCounter);
    }

    private int getSecondHand() {
        long millis = System.currentTimeMillis();
        return (int) (TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
