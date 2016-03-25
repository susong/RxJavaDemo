package com.dream.rxjava.rxjavaessentials.chapter3;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dream.rxjava.R;
import com.dream.rxjava.XLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/18 下午10:56
 * Description: RxJavaDemo
 */
public class AppInfoFragment3 extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private AppInfoAdapter mAdapter;
    private List<AppInfo> mAppInfoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View view = inflater.inflate(R.layout.fragment_example, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new AppInfoAdapter(new ArrayList<>(), R.layout.appinfo_list_item);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.GONE);

        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.myPrimaryColor));
        mSwipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                        getResources().getDisplayMetrics()));

        List<AppInfo> appInfoList = AppInfoList.getInstance().getList();

        AppInfo appOne = appInfoList.get(0);

        AppInfo appTwo = appInfoList.get(10);

        AppInfo appThree = appInfoList.get(24);

        just(appOne, appTwo, appThree);
//        repeat(appOne, appTwo, appThree);
//        defer();
//        range();
//        interval();
//        timer();
    }

    private void just(AppInfo appOne, AppInfo appTwo, AppInfo appThree) {
        mRecyclerView.setVisibility(View.VISIBLE);
        Observable.just(appOne, appTwo, appThree)
                .subscribe(new Subscriber<AppInfo>() {
                    @Override
                    public void onCompleted() {
                        XLog.d("loadList subscribe onCompleted");
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "Here is the list!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        XLog.d("loadList subscribe onError");
                        XLog.e(e);
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AppInfo appInfo) {
                        XLog.d("loadList subscribe onNext : " + appInfo.getName());
                        mAppInfoList.add(appInfo);
                        mAdapter.addAppInfo(mAppInfoList.size() - 1, appInfo);
                    }
                });
    }

    private void repeat(AppInfo appOne, AppInfo appTwo, AppInfo appThree) {
        mRecyclerView.setVisibility(View.VISIBLE);
        Observable.just(appOne, appTwo, appThree)
                .repeat(3)
                .subscribe(new Subscriber<AppInfo>() {
                    @Override
                    public void onCompleted() {
                        XLog.d("loadList subscribe onCompleted");
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "Here is the list!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        XLog.d("loadList subscribe onError");
                        XLog.e(e);
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AppInfo appInfo) {
                        XLog.d("loadList subscribe onNext : " + appInfo.getName());
                        mAppInfoList.add(appInfo);
                        mAdapter.addAppInfo(mAppInfoList.size() - 1, appInfo);
                    }
                });
    }

    private void defer() {
        getInt().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                XLog.d("subscribe call : " + integer);
            }
        });

        Observable<Integer> defer = Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return getInt();
            }
        });


        defer.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                XLog.d("subscribe call : " + integer);
            }
        });
    }

    private Observable<Integer> getInt() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    XLog.d("isUnsubscribed");
                    return;
                }
                XLog.d("getInt");
                subscriber.onNext(42);
                subscriber.onCompleted();
            }
        });
    }

    private void range() {
        Observable.range(10, 3)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        XLog.d("onCompleted");
                        Toast.makeText(getActivity(), "Yeaaah!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        XLog.d("onError");
                        XLog.e(e);
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Integer number) {
                        XLog.d("onNext : " + number);
                        Toast.makeText(getActivity(), "I say " + number, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void interval() {
        XLog.d("interval");
        Observable.interval(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//不要这句就会在子线程中调用onNext
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        XLog.d("onCompleted");
//                        Toast.makeText(getActivity(), "Yeaaah!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        XLog.d("onError");
                        XLog.e(e);
//                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Long number) {
                        XLog.d("onNext : " + number);
//                        Toast.makeText(getActivity(), "I say " + number, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void timer() {
        XLog.d("timer");
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        XLog.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        XLog.d("onError");
                        XLog.e(e);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        XLog.d("onNext : " + aLong);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
