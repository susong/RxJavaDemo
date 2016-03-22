package com.dream.rxjava.chapter4;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dream.rxjava.R;
import com.dream.rxjava.XLog;
import com.dream.rxjava.chapter3.AppInfo;
import com.dream.rxjava.chapter3.AppInfoAdapter;
import com.dream.rxjava.chapter3.AppInfoList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/18 下午10:56
 * Description: RxJavaDemo
 */
public class SamplingThrottleExampleFragment extends Fragment {

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
        sampling();
    }


    private void sampling() {
        Observable.interval(1, TimeUnit.SECONDS)
//                .sample(2, TimeUnit.SECONDS)
//                .throttleFirst(2, TimeUnit.SECONDS)
                .throttleLast(2, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        XLog.d("loadList subscribe onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        XLog.d("loadList subscribe onError");
                        XLog.e(e);
                    }

                    @Override
                    public void onNext(Long l) {
                        XLog.d("loadList subscribe onNext : " + l);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
