package com.dream.rxjava.rxjavaandroidsample.asynchttp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dream.rxjava.R;
import com.dream.rxjava.XLog;
import com.dream.rxjava.rxjavaandroidsample.fragment.BaseFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/4/3 上午1:33
 * Description: RxJavaDemo
 */
public class AsyncHttpFragment extends BaseFragment {
    @Bind(R.id.btn_start_operation) Button mBtnStartOperation;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_async_http, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_start_operation)
    public void onClick() {
        startAsyncHttpRequest();
    }

    private void startAsyncHttpRequest() {
        mCompositeSubscription.add(
                getData().subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        XLog.d("onCompleted");
                        log("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        XLog.e(e);
                        log("onError");
                    }

                    @Override
                    public void onNext(String s) {
                        XLog.d("onNext " + s);
                        log("onNext " + s);
                    }
                })
        );
    }

    private Observable<String> getData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://www.weather.com.cn/adat/sk/101010100.html";
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                client.get(url, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        subscriber.onError(throwable);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        subscriber.onNext(responseString);
                    }
                });
            }
        });

    }
}
