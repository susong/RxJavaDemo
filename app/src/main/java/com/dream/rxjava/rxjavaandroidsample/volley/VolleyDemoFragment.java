package com.dream.rxjava.rxjavaandroidsample.volley;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.dream.rxjava.R;
import com.dream.rxjava.XLog;
import com.dream.rxjava.rxjavaandroidsample.fragment.BaseFragment;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/4/2 下午10:37
 * Description: RxJavaDemo
 */
public class VolleyDemoFragment extends BaseFragment {
    @Bind(R.id.btn_start_operation) Button mBtnStartOperation;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_volley, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onPause() {
        super.onPause();
        mCompositeSubscription.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_start_operation)
    public void onClick() {
        startVolleyRequest();
    }

    private void startVolleyRequest() {
        mCompositeSubscription.add(
                newGetRouteData()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<JSONObject>() {
                            @Override
                            public void onCompleted() {
                                XLog.d("onCompleted");
                                log("onCompleted ");
                            }

                            @Override
                            public void onError(Throwable e) {
                                VolleyError cause = (VolleyError) e.getCause();
                                String s = new String(cause.networkResponse.data, Charset.forName("UTF-8"));
                                XLog.e(s);
                                XLog.e(cause.toString());
                            }

                            @Override
                            public void onNext(JSONObject jsonObject) {
                                XLog.d("onNext " + jsonObject.toString());
                                log("onNext " + jsonObject.toString());
                            }
                        })
        );
    }

    public Observable<JSONObject> newGetRouteData() {
        return Observable.defer(new Func0<Observable<JSONObject>>() {
            @Override
            public Observable<JSONObject> call() {
                try {
                    return Observable.just(getRouteData());
                } catch (ExecutionException | InterruptedException e) {
                    XLog.e(e.getMessage());
                    return Observable.error(e);
                }
            }
        });
    }

    private JSONObject getRouteData() throws ExecutionException, InterruptedException {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        String url = "http://www.weather.com.cn/adat/sk/101010100.html";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, future, future);
        MyVolley.getRequestQueue().add(req);
        return future.get();
    }
}
