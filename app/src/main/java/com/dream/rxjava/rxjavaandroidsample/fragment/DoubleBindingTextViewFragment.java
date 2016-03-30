package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dream.rxjava.R;
import com.dream.rxjava.rxjavaandroidsample.RxUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

import static android.text.TextUtils.isEmpty;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/29 下午8:17
 * Description: RxJavaDemo
 */
public class DoubleBindingTextViewFragment extends BaseFragment {

    @Bind(R.id.double_binding_num1) EditText mNum1;
    @Bind(R.id.double_binding_num2) EditText mNum2;
    @Bind(R.id.double_binding_result) TextView mResult;
    private PublishSubject<Float> mPublishSubject;
    private Subscription mSubscribe;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_double_binding_textview, container, false);
        ButterKnife.bind(this, layout);

        mPublishSubject = PublishSubject.create();

        mSubscribe = mPublishSubject
                .asObservable()
                .subscribe(new Action1<Float>() {
                    @Override
                    public void call(Float f) {
                        mResult.setText(String.valueOf(f));
                    }
                });

        onNumberChanged();
        mNum2.requestFocus();
        return layout;
    }

    @OnTextChanged({R.id.double_binding_num1, R.id.double_binding_num2})
    public void onNumberChanged() {
        float num1 = 0;
        float num2 = 0;

        if (!isEmpty(mNum1.getText().toString())) {
            num1 = Float.parseFloat(mNum1.getText().toString());
        }

        if (!isEmpty(mNum2.getText().toString())) {
            num2 = Float.parseFloat(mNum2.getText().toString());
        }

        mPublishSubject.onNext(num1 + num2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        RxUtils.unsubscribeIfNotNull(mSubscribe);
    }
}
