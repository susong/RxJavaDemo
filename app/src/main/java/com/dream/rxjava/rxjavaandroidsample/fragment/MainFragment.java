package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dream.rxjava.R;
import com.dream.rxjava.rxjavaandroidsample.rxbus.RxBusDemoFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/27 下午2:13
 * Description: RxJavaDemo
 */
public class MainFragment extends BaseFragment {

    @Bind(R.id.btn_demo_schedulers)
    Button mBtnDemoSchedulers;
    @Bind(R.id.btn_demo_buffer)
    Button mBtnDemoBuffer;
    @Bind(R.id.btn_demo_debounce)
    Button mBtnDemoDebounce;
    @Bind(R.id.btn_demo_retrofit)
    Button mBtnDemoRetrofit;
    @Bind(R.id.btn_demo_double_binding_textview)
    Button mBtnDemoDoubleBindingTextview;
    @Bind(R.id.btn_demo_polling)
    Button mBtnDemoPolling;
    @Bind(R.id.btn_demo_rxbus)
    Button mBtnDemoRxbus;
    @Bind(R.id.btn_demo_form_validation_combinel)
    Button mBtnDemoFormValidationCombinel;
    @Bind(R.id.btn_demo_pseudo_cache)
    Button mBtnDemoPseudoCache;
    @Bind(R.id.btn_demo_timing)
    Button mBtnDemoTiming;
    @Bind(R.id.btn_demo_exponential_backoff)
    Button mBtnDemoExponentialBackoff;
    @Bind(R.id.btn_demo_rotation_persist)
    Button mBtnDemoRotationPersist;
    @Bind(R.id.btn_demo_volley)
    Button mBtnDemoVolley;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_demo_schedulers, R.id.btn_demo_buffer, R.id.btn_demo_debounce, R.id.btn_demo_retrofit, R.id.btn_demo_double_binding_textview, R.id.btn_demo_polling, R.id.btn_demo_rxbus, R.id.btn_demo_form_validation_combinel, R.id.btn_demo_pseudo_cache, R.id.btn_demo_timing, R.id.btn_demo_exponential_backoff, R.id.btn_demo_rotation_persist, R.id.btn_demo_volley})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_demo_schedulers:
                clickedOn(new ConcurrencyWithSchedulersDemoFragment());
                break;
            case R.id.btn_demo_buffer:
                clickedOn(new BufferDemoFragment());
                break;
            case R.id.btn_demo_debounce:
                clickedOn(new DebounceSearchEmitterFragment());
                break;
            case R.id.btn_demo_retrofit:
                clickedOn(new RetrofitFragment());
                break;
            case R.id.btn_demo_double_binding_textview:
                clickedOn(new DoubleBindingTextViewFragment());
                break;
            case R.id.btn_demo_polling:
                clickedOn(new PollingFragment());
                break;
            case R.id.btn_demo_rxbus:
                clickedOn(new RxBusDemoFragment());
                break;
            case R.id.btn_demo_form_validation_combinel:
                clickedOn(new FormValidationCombineLatestFragment());
                break;
            case R.id.btn_demo_pseudo_cache:
                clickedOn(new PseudoCacheMergeFragment());
                break;
            case R.id.btn_demo_timing:
                break;
            case R.id.btn_demo_exponential_backoff:
                break;
            case R.id.btn_demo_rotation_persist:
                break;
            case R.id.btn_demo_volley:
                break;
        }
    }

    private void clickedOn(@NonNull Fragment fragment) {
        final String tag = fragment.getClass().toString();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(android.R.id.content, fragment, tag)
                .commit();
    }
}
