package com.dream.rxjava.chapter3;

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
public class AppInfo implements Comparable<AppInfo> {

    long lastUpdateTime;
    String name;
    String icon;

    public AppInfo(long lastUpdateTime, String name, String icon) {
        this.lastUpdateTime = lastUpdateTime;
        this.name = name;
        this.icon = icon;
    }

    @Override
    public int compareTo(@NonNull AppInfo another) {
        return getName().compareTo(another.getName());
    }
}
