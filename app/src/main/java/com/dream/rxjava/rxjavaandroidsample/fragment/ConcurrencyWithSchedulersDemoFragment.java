package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.dream.rxjava.R;
import com.dream.rxjava.rxjavaandroidsample.RxUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/27 下午2:26
 * Description: RxJavaDemo
 */
public class ConcurrencyWithSchedulersDemoFragment extends BaseFragment {

    @Bind(R.id.btn_start_operation)
    Button mBtnStartOperation;
    @Bind(R.id.progress_operation_running)
    ProgressBar mProgressOperationRunning;
    private Subscription mSubscribe;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_concurrency_schedulers, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        RxUtils.unsubscribeIfNotNull(mSubscribe);
    }

    @OnClick(R.id.btn_start_operation)
    public void onClick() {
        mProgressOperationRunning.setVisibility(View.VISIBLE);
        log("Button Clicked");

        mSubscribe = getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriber());
    }

    private Observable<Boolean> getObservable() {
        return Observable.just(true)
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        log("Within Observable");
                        doSomeLongOperationThatBlocksCurrentThread();
                        return aBoolean;
                    }
                });
    }

    private void doSomeLongOperationThatBlocksCurrentThread() {
        log("performing long operation");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Timber.d("Operation was interrupted");
        }
    }

    private Subscriber<Boolean> getSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                log("On complete");
                mProgressOperationRunning.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "Error in RxJava Demo concurrency");
                log(String.format("Boo! Error %s", e.getMessage()));
                mProgressOperationRunning.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                log(String.format("onNext with return value \"%b\"", aBoolean));
            }
        };
    }
}
