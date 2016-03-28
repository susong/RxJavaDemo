package com.dream.rxjava.rxjavaandroidsample.wiring;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.dream.rxjava.R;

import java.util.List;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/27 下午2:32
 * Description: RxJavaDemo
 */
public class LogAdapter extends ArrayAdapter<String> {
    public LogAdapter(Context context, List<String> objects) {
        super(context, R.layout.item_log, R.id.item_log, objects);
    }
}
