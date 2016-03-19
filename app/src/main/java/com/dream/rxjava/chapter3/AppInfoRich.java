package com.dream.rxjava.chapter3;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.dream.rxjava.XLog;

import java.util.Locale;

import lombok.experimental.Accessors;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/18 下午11:21
 * Description: RxJavaDemo
 */
@Accessors(prefix = "m")
public class AppInfoRich implements Comparable<AppInfoRich> {

    private String mName;
    private Context mContext;
    private ResolveInfo mResolveInfo;
    private ComponentName mComponentName;
    private PackageInfo mPackageInfo;
    private Drawable mIcon;

    public AppInfoRich(Context context, ResolveInfo resolveInfo) {
        this.mContext = context;
        this.mResolveInfo = resolveInfo;
        mComponentName = new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName,
                resolveInfo.activityInfo.name);
        try {
            context.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            XLog.e(e);
        }
    }

    public String getName() {
        if (mName != null) {
            return mName;
        } else {
            try {
                return getNameFromResolveInfo(mResolveInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                XLog.e(e);
                return getPackageName();
            }
        }
    }

    public String getActivityName() {
        return mResolveInfo.activityInfo.name;
    }

    public String getPackageName() {
        return mResolveInfo.activityInfo.packageName;
    }

    public ComponentName getComponentName() {
        return mComponentName;
    }

    public String getComponentInfo() {
        if (getComponentName() != null) {
            return getComponentName().toString();
        } else {
            return "";
        }
    }

    public ResolveInfo getResolveInfo() {
        return mResolveInfo;
    }

    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }

    public String getVersionName() {
        PackageInfo packageInfo = getPackageInfo();
        if (packageInfo != null) {
            return packageInfo.versionName;
        } else {
            return "";
        }
    }

    public int getVersionCode() {
        PackageInfo packageInfo = getPackageInfo();
        if (packageInfo != null) {
            return packageInfo.versionCode;
        } else {
            return 0;
        }
    }

    public Drawable getIcon() {
        if (mIcon == null) {
            mIcon = getResolveInfo().loadIcon(mContext.getPackageManager());
        }
        return mIcon;
    }

    public long getFirstInstallTime() {
        PackageInfo packageInfo = getPackageInfo();
        if (packageInfo != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            return packageInfo.firstInstallTime;
        } else {
            return 0;
        }
    }

    public long getLastUpdateTime() {
        PackageInfo packageInfo = getPackageInfo();
        if (packageInfo != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            return packageInfo.lastUpdateTime;
        } else {
            return 0;
        }
    }

    public String getNameFromResolveInfo(ResolveInfo resolveInfo) throws PackageManager.NameNotFoundException {
        String name = resolveInfo.resolvePackageName;
        if (resolveInfo.activityInfo != null) {
            Resources resources = mContext.getPackageManager().getResourcesForApplication(resolveInfo.activityInfo.applicationInfo);
            Resources chinaResources = getChinaResources(resources);
            if (resolveInfo.activityInfo.labelRes != 0) {
                name = chinaResources.getString(resolveInfo.activityInfo.labelRes);
                if (TextUtils.isEmpty(name)) {
                    name = resources.getString(resolveInfo.activityInfo.labelRes);
                }
            } else {
                name = resolveInfo.activityInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
            }
        }
        return name;
    }

    public Resources getChinaResources(Resources resources) {
        AssetManager assets = resources.getAssets();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.locale = Locale.CHINA;
        return new Resources(assets, metrics, configuration);
    }

    @Override
    public int compareTo(@NonNull AppInfoRich another) {
        return getName().compareTo(another.getName());
    }

    @Override
    public String toString() {
        return getName();
    }
}
