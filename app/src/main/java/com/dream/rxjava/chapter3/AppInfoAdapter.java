package com.dream.rxjava.chapter3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dream.rxjava.R;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/19 上午12:34
 * Description: RxJavaDemo
 */
public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.ViewHolder> {

    private List<AppInfo> mAppInfoList;
    private int mRowLayout;

    public AppInfoAdapter(List<AppInfo> appInfoList, int rowLayout) {
        mAppInfoList = appInfoList;
        mRowLayout = rowLayout;
    }

    public void addAppInfoList(List<AppInfo> appInfoList) {
        mAppInfoList.clear();
        mAppInfoList.addAll(appInfoList);
        notifyDataSetChanged();
    }

    public void addAppInfo(int position, AppInfo appInfo) {
        if (position < 0) {
            position = 0;
        }
        mAppInfoList.add(position, appInfo);
        notifyItemInserted(position);
    }

    @Override
    public AppInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mRowLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppInfoAdapter.ViewHolder holder, int position) {
        AppInfo appInfo = mAppInfoList.get(position);
        holder.mName.setText(appInfo.getName());
        getBitmap(appInfo.getIcon())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
//                        XLog.d("setBitmap call : " + appInfo.getIcon());
                        holder.mImage.setImageBitmap(bitmap);
                    }
                });
//        holder.mImage.setImageBitmap(BitmapFactory.decodeFile(appInfo.getIcon()));
    }

    private Observable<Bitmap> getBitmap(String icon) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
//                XLog.d("getBitmap call : " + icon);
                subscriber.onNext(BitmapFactory.decodeFile(icon));
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAppInfoList == null ? 0 : mAppInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;

        public ImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mImage = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
