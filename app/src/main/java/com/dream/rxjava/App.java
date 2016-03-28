package com.dream.rxjava;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/18 下午11:14
 * Description: RxJavaDemo
 */
public class App extends Application {

    private static Context mContext;
    private RefWatcher mRefWatcher;

    public static App getApp() {
        return (App) mContext;
    }

    public static RefWatcher getRefWatcher() {
        return App.getApp().mRefWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mRefWatcher = LeakCanary.install(this);

        Timber.plant(new Timber.DebugTree());

        DisplayImageOptions defaultOptions =
                new DisplayImageOptions.Builder().showImageOnFail(R.drawable.ic_launcher)
                        .showImageOnLoading(R.drawable.ic_launcher)
                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(
                        defaultOptions).build();
        ImageLoader.getInstance().init(config);
    }
}
