package com.dream.rxjava.rxjavaessentials.chapter7;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dream.rxjava.R;
import com.dream.rxjava.XLog;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.rey.material.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/24 下午4:56
 * Description: RxJavaDemo
 */
public class NetworkTaskFragment extends Fragment {

    @Bind(R.id.arc_progress)
    ArcProgress mArcProgress;
    @Bind(R.id.button_download)
    Button mButton;

    private PublishSubject<Integer> mDownloadProgress = PublishSubject.create();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.button_download)
    public void onClick() {
        mButton.setText(getString(R.string.downloading));
        mButton.setClickable(false);

        mDownloadProgress.distinct()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        XLog.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        XLog.e("onError");
                        XLog.e(e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        XLog.d("onNext : " + integer);
                        mArcProgress.setProgress(integer);
                    }
                });


        String destination = "/sdcard/softboy.avi";
        String source = "http://archive.blender.org/fileadmin/movies/softboy.avi";

        observableDownload(source, destination)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        NetworkTaskFragment.this.resetDownloadButton();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        File file = new File(destination);
                        intent.setDataAndType(Uri.fromFile(file), "video/avi");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        NetworkTaskFragment.this.startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(NetworkTaskFragment.this.getActivity(), "Something went south", Toast.LENGTH_SHORT).show();
                        resetDownloadButton();
                    }
                });
    }


    private void resetDownloadButton() {
        mButton.setText(getString(R.string.download));
        mButton.setClickable(true);
        mArcProgress.setProgress(0);
    }

    private Observable<Boolean> observableDownload(String source, String destination) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean result = downloadFile(source, destination);
                if (result) {
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable("Download failed."));
                }
            }
        });
    }

    private boolean downloadFile(String source, String destination) {
        boolean result = false;
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(source);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }

            int fileLength = connection.getContentLength();

            input = connection.getInputStream();
            output = new FileOutputStream(destination);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                if (fileLength > 0) {
                    int percentage = (int) (total * 100 / fileLength);
                    mDownloadProgress.onNext(percentage);
                }
                output.write(data, 0, count);
            }
            mDownloadProgress.onCompleted();
            result = true;
        } catch (Exception e) {
            mDownloadProgress.onError(e);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                mDownloadProgress.onError(e);
            }

            if (connection != null) {
                connection.disconnect();
                mDownloadProgress.onCompleted();
            }
        }
        return result;
    }
}
