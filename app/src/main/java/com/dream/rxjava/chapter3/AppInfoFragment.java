package com.dream.rxjava.chapter3;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dream.rxjava.App;
import com.dream.rxjava.R;
import com.dream.rxjava.Utils;
import com.dream.rxjava.XLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/18 下午10:56
 * Description: RxJavaDemo
 */
public class AppInfoFragment extends Fragment {


    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private File mFilesDir;
    private AppInfoAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View view = inflater.inflate(R.layout.fragment_example, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mAdapter = new AppInfoAdapter(new ArrayList<>(), R.layout.appinfo_list_item);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.myPrimaryColor));
        mSwipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                        getResources().getDisplayMetrics()));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTheList();
            }
        });

        mRecyclerView.setVisibility(View.GONE);

        getFileDir().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        XLog.d("getFilesDir subscribe : " + file.getAbsolutePath());
                        mFilesDir = file;
                        refreshTheList();
                    }
                });
    }

    private Observable<File> getFileDir() {
        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                XLog.d("getFilesDir Observable : " + App.instance.getFilesDir());
                subscriber.onNext(App.instance.getFilesDir());
                subscriber.onCompleted();
            }
        });
    }

    private void refreshTheList() {
        // toSortedList 变成列表
        getApps().toSortedList().subscribe(new Subscriber<List<AppInfo>>() {
            @Override
            public void onCompleted() {
                XLog.d("getApps subscribe onCompleted");
                Toast.makeText(getActivity(), "Here is the list!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {
                XLog.e(e);
                XLog.d("getApps subscribe onError");
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(List<AppInfo> appInfoList) {
                XLog.d("getApps subscribe : " + appInfoList.toString());
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.addAppInfoList(appInfoList);
                mSwipeRefreshLayout.setRefreshing(false);
                storeList(appInfoList);
            }
        });
    }

    private void storeList(List<AppInfo> appInfoList) {
        AppInfoList.getInstance().setList(appInfoList);

        Schedulers.io().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                XLog.d("storeList schedule : " + appInfoList.toString());
                SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
                Type appInfoType = new TypeToken<List<AppInfo>>() {
                }.getType();
                sp.edit().putString("APPS", new Gson().toJson(appInfoList, appInfoType)).apply();
            }
        });
    }

    private Observable<AppInfo> getApps() {
        return Observable.create(subscriber -> {
            List<AppInfoRich> appInfoRichList = new ArrayList<>();
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> resolveInfoList = getActivity().getPackageManager().queryIntentActivities(mainIntent, 0);
            for (ResolveInfo info : resolveInfoList) {
                appInfoRichList.add(new AppInfoRich(getActivity(), info));
            }

            for (AppInfoRich appInfoRich : appInfoRichList) {
                Bitmap icon = Utils.drawableToBitmap(appInfoRich.getIcon());
                String name = appInfoRich.getName();
                String iconPath = mFilesDir + "/" + name;
                Utils.storeBitmap(getActivity(), icon, name);
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                XLog.d("getApps Observable : " + name);
                subscriber.onNext(new AppInfo(appInfoRich.getLastUpdateTime(), name, iconPath));
            }
            if (!subscriber.isUnsubscribed()) {
                XLog.d("getApps Observable onCompleted");
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
