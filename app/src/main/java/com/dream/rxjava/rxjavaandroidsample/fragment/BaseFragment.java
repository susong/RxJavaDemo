package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.dream.rxjava.App;
import com.dream.rxjava.R;
import com.dream.rxjava.rxjavaandroidsample.wiring.LogAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/27 下午2:13
 * Description: RxJavaDemo
 */
public class BaseFragment extends Fragment {

    @Nullable
    @Bind(R.id.list_threading_log)
    ListView mListThreadingLog;

    protected LogAdapter mLogAdapter;
    protected List<String> mLogList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mListThreadingLog != null) {
            setupLogger();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getRefWatcher().watch(this);
    }

    protected boolean isCurrentlyOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    protected void setupLogger() {
        mLogList = new ArrayList<>();
        mLogAdapter = new LogAdapter(getActivity(), new ArrayList<>());
        mListThreadingLog.setAdapter(mLogAdapter);
    }

    protected void log(String logMsg) {
        if (isCurrentlyOnMainThread()) {
            mLogList.add(0, logMsg + " (main thread)");
            mLogAdapter.clear();
            mLogAdapter.addAll(mLogList);
        } else {
            mLogList.add(0, logMsg + " (NOT main thread)");
            // You can only do below stuff on main thread.
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mLogAdapter.clear();
                    mLogAdapter.addAll(mLogList);
                }
            });
        }
    }
}
