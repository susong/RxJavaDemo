package com.dream.rxjava.chapter8;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dream.rxjava.R;
import com.dream.rxjava.chapter8.api.github.StackApiManager;
import com.dream.rxjava.chapter8.api.github.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/25 上午10:51
 * Description: RxJavaDemo
 */
public class SoActivity extends AppCompatActivity {

    @Bind(R.id.so_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.so_swipe)
    SwipeRefreshLayout mSwipe;
    private StackApiManager mStackApiManager;
    private SoAdapter mSoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_so);
        ButterKnife.bind(this);

        mSoAdapter = new SoAdapter(new ArrayList<>());
        mStackApiManager = new StackApiManager();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mSoAdapter);

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

        refreshList();
    }

    private void refreshList() {
        showRefresh(true);
        mStackApiManager.getMostPopularSOusers(10)
                .subscribe(new Action1<List<User>>() {
                    @Override
                    public void call(List<User> users) {
                        showRefresh(false);
                        mSoAdapter.updateUsers(users);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        showRefresh(false);
                    }
                });
    }

    private void showRefresh(boolean show) {
        mSwipe.setRefreshing(show);
        int visibility = show ? View.GONE : View.VISIBLE;
        mRecyclerView.setVisibility(visibility);
    }
}
