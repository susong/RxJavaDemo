package com.dream.rxjava.rxjavaessentials.chapter7;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.dream.rxjava.rxjavaessentials.chapter3.AppInfo;
import com.dream.rxjava.rxjavaessentials.chapter3.AppInfoAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/18 下午10:56
 * Description: RxJavaDemo
 */
public class SharedPreferencesListFragment extends Fragment {

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

        loadList();
    }

    private void loadList() {
        mRecyclerView.setVisibility(View.VISIBLE);
        getApps()
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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

    private Observable<AppInfo> getApps() {
        return Observable.create(new Observable.OnSubscribe<AppInfo>() {
            @Override
            public void call(Subscriber<? super AppInfo> subscriber) {
                XLog.d("call");
                List<AppInfo> apps = new ArrayList<>();
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                Type appInfoType = new TypeToken<List<AppInfo>>() {
                }.getType();
                String serializedApps = sharedPref.getString("APPS", "");
                if (!"".equals(serializedApps)) {
                    apps = new Gson().fromJson(serializedApps, appInfoType);
                }
                for (AppInfo app : apps) {
                    subscriber.onNext(app);
                }
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
