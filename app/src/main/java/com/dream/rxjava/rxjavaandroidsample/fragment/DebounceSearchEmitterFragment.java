package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dream.rxjava.R;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import timber.log.Timber;

import static java.lang.String.format;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/28 上午10:28
 * Description: RxJavaDemo
 */
public class DebounceSearchEmitterFragment extends BaseFragment {


    @Bind(R.id.input_txt_debounce)
    EditText mInputTxtDebounce;
    @Bind(R.id.clr_debounce)
    ImageButton mClrDebounce;
    private Subscription mSubscribe;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_debounce, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSubscribe = RxTextView.textChangeEvents(mInputTxtDebounce)
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<TextViewTextChangeEvent>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("--------- onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "--------- Woops on error!");
                        log("Dang error. check your logs");
                    }

                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        log(format("Searching for %s", textViewTextChangeEvent.text().toString()));
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.clr_debounce)
    public void onClick() {
        mLogList = new ArrayList<>();
        mLogAdapter.clear();
    }
}
