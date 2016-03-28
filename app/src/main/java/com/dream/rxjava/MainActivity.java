package com.dream.rxjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dream.rxjava.rxjavaandroidsample.RxJavaAndroidSampleActivity;
import com.dream.rxjava.rxjavaessentials.RxJavaEssentialsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/25 下午7:16
 * Description: RxJavaDemo
 */
public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_rxjava_essentials)
    Button mBtnRxjavaEssentials;
    @Bind(R.id.btn_rxjava_android_sample)
    Button mBtnRxjavaAndroidSample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({
            R.id.btn_rxjava_essentials,
            R.id.btn_rxjava_android_sample
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_rxjava_essentials:
                startActivity(new Intent(this, RxJavaEssentialsActivity.class));
                break;
            case R.id.btn_rxjava_android_sample:
                startActivity(new Intent(this, RxJavaAndroidSampleActivity.class));
                break;
        }
    }
}
