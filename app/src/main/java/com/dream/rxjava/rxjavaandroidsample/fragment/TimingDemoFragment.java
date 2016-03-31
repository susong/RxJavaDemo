package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.dream.rxjava.R;
import com.dream.rxjava.XLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import timber.log.Timber;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/31 上午9:44
 * Description: RxJavaDemo
 */
public class TimingDemoFragment extends BaseFragment {
    @Bind(R.id.btn_demo_timing_1) Button mBtnDemoTiming1;
    @Bind(R.id.btn_demo_timing_2) Button mBtnDemoTiming2;
    @Bind(R.id.btn_demo_timing_3) Button mBtnDemoTiming3;
    @Bind(R.id.btn_demo_timing_4) Button mBtnDemoTiming4;
    @Bind(R.id.btn_clr) ImageButton mBtnClr;
    private Subscription mSubscription1;
    private Subscription mSubscription2;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_demo_timing, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSubscription1 != null && !mSubscription1.isUnsubscribed()) {
            mSubscription1.unsubscribe();
        }
        if (mSubscription2 != null && !mSubscription2.isUnsubscribed()) {
            mSubscription2.unsubscribe();
        }
    }

    @OnClick({R.id.btn_demo_timing_1, R.id.btn_demo_timing_2, R.id.btn_demo_timing_3, R.id.btn_demo_timing_4, R.id.btn_clr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_demo_timing_1:
                log(String.format("A1 [%s] --- BTN click", getCurrentTimestamp()));
                Observable.timer(2, TimeUnit.SECONDS)
                        .subscribe(new Subscriber<Long>() {
                            @Override
                            public void onCompleted() {
                                log(String.format("A1 [%s] XXX COMPLETE", getCurrentTimestamp()));
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e, "something went wrong in TimingDemoFragment example");
                            }

                            @Override
                            public void onNext(Long aLong) {
                                log(String.format("A1 [%s]     NEXT", getCurrentTimestamp()));
                            }
                        });
                break;
            case R.id.btn_demo_timing_2:
                if (mSubscription1 != null && !mSubscription1.isUnsubscribed()) {
                    mSubscription1.unsubscribe();
                    log(String.format("B2 [%s] XXX BTN KILLED", getCurrentTimestamp()));
                    return;
                }
                log(String.format("B2 [%s] --- BTN click", getCurrentTimestamp()));
                mSubscription1 = Observable.interval(1, TimeUnit.SECONDS)
                        .subscribe(new Subscriber<Long>() {
                            @Override
                            public void onCompleted() {
                                log(String.format("B2 [%s] XXX COMPLETE", getCurrentTimestamp()));

                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e, "something went wrong in TimingDemoFragment example");

                            }

                            @Override
                            public void onNext(Long aLong) {
                                XLog.d("onNext : " + aLong);
                                log(String.format("B2 [%s]     NEXT", getCurrentTimestamp()));

                            }
                        });
                break;
            case R.id.btn_demo_timing_3:
                if (mSubscription2 != null && !mSubscription2.isUnsubscribed()) {
                    mSubscription2.unsubscribe();
                    log(String.format("C3 [%s] XXX BTN KILLED", getCurrentTimestamp()));
                    return;
                }
                log(String.format("C3 [%s] --- BTN click", getCurrentTimestamp()));
                mSubscription2 = Observable.interval(0, 1, TimeUnit.SECONDS)
                        .subscribe(new Subscriber<Long>() {
                            @Override
                            public void onCompleted() {
                                log(String.format("C3 [%s] XXX COMPLETE", getCurrentTimestamp()));
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e, "something went wrong in TimingDemoFragment example");
                            }

                            @Override
                            public void onNext(Long aLong) {
                                log(String.format("C3 [%s]     NEXT", getCurrentTimestamp()));
                            }
                        });
                break;
            case R.id.btn_demo_timing_4:
                log(String.format("D4 [%s] --- BTN click", getCurrentTimestamp()));
                Observable.interval(3, TimeUnit.SECONDS)
                        .take(5)
                        .subscribe(new Subscriber<Long>() {
                            @Override
                            public void onCompleted() {
                                log(String.format("D4 [%s] XXX COMPLETE", getCurrentTimestamp()));
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e, "something went wrong in TimingDemoFragment example");
                            }

                            @Override
                            public void onNext(Long aLong) {
                                log(String.format("D4 [%s]     NEXT", getCurrentTimestamp()));
                            }
                        });
                break;
            case R.id.btn_clr:
                mLogList = new ArrayList<>();
                mLogAdapter.clear();
                break;
        }
    }

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("k:m:s:S a").format(new Date());
    }
}
