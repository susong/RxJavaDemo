package com.dream.rxjava.rxjavaandroidsample.rxbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dream.rxjava.R;
import com.dream.rxjava.rxjavaandroidsample.RxJavaAndroidSampleActivity;
import com.dream.rxjava.rxjavaandroidsample.fragment.BaseFragment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.subscriptions.CompositeSubscription;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/30 上午10:56
 * Description: RxJavaDemo
 */
public class RxBusDemo_Bottom3Fragment extends BaseFragment {
    @Bind(R.id.demo_rxbus_tap_count) TextView mDemoRxbusTapCount;
    @Bind(R.id.demo_rxbus_tap_txt) TextView mDemoRxbusTapTxt;
    private RxBus mRxBus;
    private CompositeSubscription mCompositeSubscription;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_rxbus_bottom, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRxBus = ((RxJavaAndroidSampleActivity) getActivity()).getRxBusSingleton();
    }

    @Override
    public void onStart() {
        super.onStart();

        mCompositeSubscription = new CompositeSubscription();

        ConnectableObservable<Object> tapEventEmitter = mRxBus.toObserverable().publish();

        mCompositeSubscription.add(
                tapEventEmitter.subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (o instanceof RxBusDemoFragment.TapEvent) {
                            showTapText();
                        }
                    }
                })
        );

        mCompositeSubscription.add(
                tapEventEmitter.publish(new Func1<Observable<Object>, Observable<List<Object>>>() {
                    @Override
                    public Observable<List<Object>> call(Observable<Object> objectObservable) {
                        return objectObservable.buffer(objectObservable.debounce(1, TimeUnit.SECONDS));
                    }
                })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<Object>>() {
                            @Override
                            public void call(List<Object> objects) {
                                showTapCount(objects.size());
                            }
                        })
        );
        mCompositeSubscription.add(tapEventEmitter.connect());
    }

    @Override
    public void onStop() {
        super.onStop();
        mCompositeSubscription.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void showTapText() {
        mDemoRxbusTapTxt.setVisibility(View.VISIBLE);
        mDemoRxbusTapTxt.setAlpha(1f);
        ViewCompat.animate(mDemoRxbusTapTxt).alphaBy(-1f).setDuration(400);
    }

    private void showTapCount(int size) {
        mDemoRxbusTapCount.setText(String.valueOf(size));
        mDemoRxbusTapCount.setVisibility(View.VISIBLE);
        mDemoRxbusTapCount.setScaleX(1f);
        mDemoRxbusTapCount.setScaleY(1f);
        ViewCompat.animate(mDemoRxbusTapCount)
                .scaleXBy(-1f)
                .scaleYBy(-1f)
                .setDuration(800)
                .setStartDelay(100);
    }
}
