package com.dream.rxjava.rxjavaandroidsample.rxbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dream.rxjava.R;
import com.dream.rxjava.rxjavaandroidsample.RxJavaAndroidSampleActivity;
import com.dream.rxjava.rxjavaandroidsample.fragment.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/30 上午10:56
 * Description: RxJavaDemo
 */
public class RxBusDemo_TopFragment extends BaseFragment {

    @Bind(R.id.btn_demo_rxbus_tap) Button mBtnDemoRxbusTap;
    private RxBus mRxBus;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_rxbus_top, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRxBus = ((RxJavaAndroidSampleActivity) getActivity()).getRxBusSingleton();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_demo_rxbus_tap)
    public void onClick() {
        if (mRxBus.hasObservers()) {
            mRxBus.send(new RxBusDemoFragment.TapEvent());
        }
    }
}
