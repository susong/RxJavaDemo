package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.dream.rxjava.rxjavaandroidsample.RxJavaAndroidSampleActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/4/1 下午6:19
 * Description: RxJavaDemo
 */
public class RotationPersist1WorkerFragment extends Fragment {

    private IAmYourMaster mMasterFrag;
    private Subscription mStoredIntsSubscription;
    private ConnectableObservable<Integer> mStoredIntsObservable;

    /**
     * Hold a reference to the activity -> caller fragment
     * this way when the worker frag kicks off
     * we can talk back to the master and send results
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        List<Fragment> fragments = ((RxJavaAndroidSampleActivity) context).getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f instanceof IAmYourMaster) {
                mMasterFrag = (IAmYourMaster) f;
            }
        }
        if (mMasterFrag == null) {
            throw new ClassCastException("We did not find a master who can understand us :(");
        }
    }

    /**
     * This method will only be called once when the retained Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
        if (mStoredIntsObservable != null) {
            return;
        }
        Observable<Integer> intsObservable = Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long aLong) {
                        return aLong.intValue();
                    }
                })
                .take(20);

        // -----------------------------------------------------------------------------------
        // Making our observable "HOT" for the purpose of the demo.

        //_intsObservable = _intsObservable.share();
        mStoredIntsObservable = intsObservable.replay();

        mStoredIntsSubscription = mStoredIntsObservable.connect();

        // Do not do this in production!
        // `.share` is "warm" not "hot"
        // the below forceful subscription fakes the heat
        //_intsObservable.subscribe();
    }

    /**
     * The Worker fragment has started doing it's thing
     */
    @Override
    public void onResume() {
        super.onResume();
        mMasterFrag.observeResults(mStoredIntsObservable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStoredIntsSubscription.unsubscribe();
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mMasterFrag = null;
    }


    public interface IAmYourMaster {
        void observeResults(ConnectableObservable<Integer> intsObservable);
    }
}
