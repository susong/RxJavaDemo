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

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/30 上午10:56
 * Description: RxJavaDemo
 */
public class RxBusDemo_Bottom1Fragment extends BaseFragment {

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
        mCompositeSubscription.add(
                mRxBus.toObserverable()
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object o) {
                                if (o instanceof RxBusDemoFragment.TapEvent) {
                                    showTapText();
                                }
                            }
                        })
        );
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


}
