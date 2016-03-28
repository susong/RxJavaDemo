package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dream.rxjava.R;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/27 下午3:17
 * Description: RxJavaDemo
 * This is a demonstration of the `buffer` Observable.
 * <p>
 * The buffer observable allows taps to be collected only within a time span. So taps outside the
 * 2s limit imposed by buffer will get accumulated in the next log statement.
 * <p>
 * If you're looking for a more foolproof solution that accumulates "continuous" taps vs
 * a more dumb solution as show below (i.e. number of taps within a timespan)
 * look at {@link com.dream.rxjava.rxjavaandroidsample.rxbus.RxBusDemo_Bottom3Fragment} where a combo
 * of `publish` and `buffer` is used.
 * <p>
 * Also http://nerds.weddingpartyapp.com/tech/2015/01/05/debouncedbuffer-used-in-rxbus-example/
 * if you're looking for words instead of code
 */
public class BufferDemoFragment extends BaseFragment {

    @Bind(R.id.btn_start_operation)
    Button mBtnStartOperation;
    private Subscription mBufferedSubscription;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_buffer, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mBufferedSubscription = getBufferedSubscription();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBufferedSubscription.unsubscribe();
    }

    private Subscription getBufferedSubscription() {
        return RxView.clicks(mBtnStartOperation)
                .map(new Func1<Void, Void>() {
                    @Override
                    public Void call(Void aVoid) {
                        Timber.d("--------- GOT A TAP");
                        log("GOT A TAP");
                        return aVoid;
                    }
                })
                .buffer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Void>>() {
                    @Override
                    public void onCompleted() {
                        // fyi: you'll never reach here
                        Timber.d("----- onCompleted");
                        log("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "--------- Woops on error!");
                        log("Dang error! check your logs");
                    }

                    @Override
                    public void onNext(List<Void> voids) {
                        Timber.d("--------- onNext");
                        if (voids.size() > 0) {
                            log(String.format("%d taps", voids.size()));
                        } else {
                            Timber.d("--------- No taps received ");
                        }
                    }
                });
    }
}
