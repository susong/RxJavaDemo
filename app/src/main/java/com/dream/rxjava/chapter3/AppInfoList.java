package com.dream.rxjava.chapter3;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/18 下午11:25
 * Description: RxJavaDemo
 */
@Accessors(prefix = "m")
public class AppInfoList {

    private static AppInfoList mInstance = new AppInfoList();

    private AppInfoList() {
    }

    public static AppInfoList getInstance() {
        return mInstance;
    }

    @Getter
    @Setter
    private List<AppInfo> mList;

}
