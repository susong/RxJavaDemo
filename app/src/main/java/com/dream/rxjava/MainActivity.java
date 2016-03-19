package com.dream.rxjava;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dream.rxjava.chapter3.AppInfoFragment;
import com.dream.rxjava.chapter3.AppInfoFragment2;
import com.dream.rxjava.navigation_drawer.NavigationDrawerCallbacks;
import com.dream.rxjava.navigation_drawer.NavigationDrawerFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/18 下午11:14
 * Description: RxJavaDemo
 */
public class MainActivity extends AppCompatActivity implements NavigationDrawerCallbacks {

    @Bind(R.id.toolbar_actionbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer)
    DrawerLayout mDrawer;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNavigationDrawerFragment =
                (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, mDrawer, mToolbar);
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
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new ThirdExampleFragment())
//                        .commit();
                break;
            case 3:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new FilterExampleFragment())
//                        .commit();
                break;
            case 4:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new TakeExampleFragment())
//                        .commit();
                break;
            case 5:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new DistinctExampleFragment())
//                        .commit();
                break;
            case 6:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new MapExampleFragment())
//                        .commit();
                break;
            case 7:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new ScanExampleFragment())
//                        .commit();
                break;
            case 8:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new GroupByExampleFragment())
//                        .commit();
                break;
            case 9:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new MergeExampleFragment())
//                        .commit();
                break;
            case 10:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new ZipExampleFragment())
//                        .commit();
                break;
            case 11:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new JoinExampleFragment())
//                        .commit();
                break;
            case 12:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new CombineLatestExampleFragment())
//                        .commit();
                break;
            case 13:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new AndThenWhenExampleFragment())
//                        .commit();
                break;
            case 14:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new SharedPreferencesListFragment())
//                        .commit();
                break;
            case 15:
//                fragmentManager.beginTransaction().replace(R.id.container, new LongTaskFragment()).commit();
                break;
            case 16:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new NetworkTaskFragment())
//                        .commit();
                break;
            case 17:
//                startActivity(new Intent(this, SoActivity.class));
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
