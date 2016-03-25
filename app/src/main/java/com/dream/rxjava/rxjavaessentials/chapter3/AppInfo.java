package com.dream.rxjava.rxjavaessentials.chapter3;

import android.support.annotation.NonNull;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/18 下午10:53
 * Description: RxJavaDemo
 */
@Data
@Accessors(prefix = "m")
public class AppInfo implements Comparable<AppInfo> {

    long mLastUpdateTime;
    String mName;
    String mIcon;

    public AppInfo(long lastUpdateTime, String name, String icon) {
        this.mLastUpdateTime = lastUpdateTime;
        this.mName = name;
        this.mIcon = icon;
    }

    @Override
    public int compareTo(@NonNull AppInfo another) {
        return getName().compareTo(another.getName());
    }
}
