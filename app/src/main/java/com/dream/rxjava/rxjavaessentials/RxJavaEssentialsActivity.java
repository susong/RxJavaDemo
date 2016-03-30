package com.dream.rxjava.rxjavaessentials;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dream.rxjava.BuildConfig;
import com.dream.rxjava.R;
import com.dream.rxjava.rxjavaessentials.chapter2.ObservableActivity;
import com.dream.rxjava.rxjavaessentials.chapter3.AppInfoFragment;
import com.dream.rxjava.rxjavaessentials.chapter3.AppInfoFragment2;
import com.dream.rxjava.rxjavaessentials.chapter3.AppInfoFragment3;
import com.dream.rxjava.rxjavaessentials.chapter4.DistinctExampleFragment;
import com.dream.rxjava.rxjavaessentials.chapter4.FilterExampleFragment;
import com.dream.rxjava.rxjavaessentials.chapter4.TakeExampleFragment;
import com.dream.rxjava.rxjavaessentials.chapter5.FlatMapExampleFragment;
import com.dream.rxjava.rxjavaessentials.chapter5.GroupByExampleFragment;
import com.dream.rxjava.rxjavaessentials.chapter5.ScanExampleFragment;
import com.dream.rxjava.rxjavaessentials.chapter6.AndThenWhenExampleFragment;
import com.dream.rxjava.rxjavaessentials.chapter6.CombineLatestExampleFragment;
import com.dream.rxjava.rxjavaessentials.chapter6.JoinExampleFragment;
import com.dream.rxjava.rxjavaessentials.chapter6.MergeExampleFragment;
import com.dream.rxjava.rxjavaessentials.chapter6.ZipExampleFragment;
import com.dream.rxjava.rxjavaessentials.chapter7.LongTaskFragment;
import com.dream.rxjava.rxjavaessentials.chapter7.NetworkTaskFragment;
import com.dream.rxjava.rxjavaessentials.chapter7.SharedPreferencesListFragment;
import com.dream.rxjava.rxjavaessentials.chapter8.SoActivity;
import com.dream.rxjava.rxjavaessentials.navigation_drawer.NavigationDrawerCallbacks;
import com.dream.rxjava.rxjavaessentials.navigation_drawer.NavigationDrawerFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/18 下午11:14
 * Description: RxJavaDemo
 */
public class RxJavaEssentialsActivity extends AppCompatActivity implements NavigationDrawerCallbacks {

    @Bind(R.id.toolbar_actionbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer)
    DrawerLayout mDrawer;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_essentials);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNavigationDrawerFragment =
                (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, mDrawer, mToolbar);

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new AppInfoFragment())
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new AppInfoFragment2())
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new AppInfoFragment3())
                        .commit();
                break;
            case 3:
                startActivity(new Intent(this, ObservableActivity.class));
                break;
            case 4:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FilterExampleFragment())
                        .commit();
                break;
            case 5:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new TakeExampleFragment())
                        .commit();
                break;
            case 6:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new DistinctExampleFragment())
//                        .replace(R.id.container, new FirstLastExampleFragment())
//                        .replace(R.id.container, new SkipExampleFragment())
//                        .replace(R.id.container, new ElementAtExampleFragment())
//                        .replace(R.id.container, new SamplingThrottleExampleFragment())
//                        .replace(R.id.container, new TimeoutExampleFragment())
//                        .replace(R.id.container, new DebounceExampleFragment())
                        .commit();
                break;
            case 7:
                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new MapExampleFragment())
                        .replace(R.id.container, new FlatMapExampleFragment())
//                        .replace(R.id.container, new ConcatMapExampleFragment())
                        .commit();
                break;
            case 8:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ScanExampleFragment())
                        .commit();
                break;
            case 9:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new GroupByExampleFragment())
                        .commit();
                break;
            case 10:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new MergeExampleFragment())
//                        .replace(R.id.container, new ConcatExampleFragment())
                        .commit();
                break;
            case 11:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ZipExampleFragment())
                        .commit();
                break;
            case 12:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new JoinExampleFragment())
                        .commit();
                break;
            case 13:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new CombineLatestExampleFragment())
                        .commit();
                break;
            case 14:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new AndThenWhenExampleFragment())
                        .commit();
                break;
            case 15:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new SharedPreferencesListFragment())
                        .commit();
                break;
            case 16:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new LongTaskFragment())
                        .commit();
                break;
            case 17:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new NetworkTaskFragment())
                        .commit();
                break;
            case 18:
                startActivity(new Intent(this, SoActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
