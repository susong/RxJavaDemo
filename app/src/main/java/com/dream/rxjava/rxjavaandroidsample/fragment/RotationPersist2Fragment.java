package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dream.rxjava.R;
import com.dream.rxjava.rxjavaandroidsample.RxUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/4/1 下午6:19
 * Description: RxJavaDemo
 */
public class RotationPersist2Fragment
        extends BaseFragment
        implements RotationPersist2WorkerFragment.IAmYourMaster {

    public static final String FRAG_TAG = RotationPersist1WorkerFragment.class.getName();

    @Bind(R.id.btn_rotate_persist) Button mBtnRotatePersist;

    private CompositeSubscription mSubscription = new CompositeSubscription();

    @OnClick(R.id.btn_rotate_persist)
    public void onClick() {
        mLogList = new ArrayList<>();
        mLogAdapter.clear();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        RotationPersist2WorkerFragment frag = (RotationPersist2WorkerFragment) fm.findFragmentByTag(FRAG_TAG);

        if (frag == null) {
            frag = new RotationPersist2WorkerFragment();
            fm.beginTransaction().add(frag, FRAG_TAG).commit();
        } else {
            Timber.d("Worker frag already spawned");
        }
    }

    @Override
    public void setStream(Observable<Integer> intStream) {
        mSubscription.add(
                intStream.doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        log("Subscribing to intsObservable");
                    }
                }).subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        log("Observable is complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error in worker demo frag observable");
                        log("Dang! something went wrong.");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        log(String.format("Worker frag spits out - %d", integer));
                    }
                })
        );
    }

    // -----------------------------------------------------------------------------------
    // Boilerplate样板文件
    // -----------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_rotation_persist, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(mSubscription);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(mSubscription);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
