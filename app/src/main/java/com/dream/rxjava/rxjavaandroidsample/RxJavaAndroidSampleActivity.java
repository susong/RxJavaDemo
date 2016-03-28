package com.dream.rxjava.rxjavaandroidsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dream.rxjava.rxjavaandroidsample.fragment.MainFragment;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/25 下午7:50
 * Description: RxJavaDemo
 */
public class RxJavaAndroidSampleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new MainFragment(), this.toString())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
