package com.dream.rxjava.chapter3;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/18 下午11:21
 * Description: RxJavaDemo
 */
public class AppInfoRich implements Comparable<AppInfoRich> {

    String name;
    private Context context;
    private ResolveInfo resolveInfo;
    private ComponentName componentName;
    private PackageInfo packageInfo;
    private Drawable drawable;

    public AppInfoRich(Context context, ResolveInfo resolveInfo) {
        this.context = context;
        this.resolveInfo = resolveInfo;
    }

    @Override
    public int compareTo(AppInfoRich another) {
        return 0;
    }
}
