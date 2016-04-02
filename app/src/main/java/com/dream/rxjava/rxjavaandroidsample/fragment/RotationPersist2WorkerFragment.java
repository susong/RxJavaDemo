package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.dream.rxjava.rxjavaandroidsample.RxJavaAndroidSampleActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/4/1 下午6:19
 * Description: RxJavaDemo
 */
public class RotationPersist2WorkerFragment extends Fragment {

    private IAmYourMaster mMasterFrag;
    private Subscription mStoredIntsSubscription;
    private Subject<Integer, Integer> mIntStream = PublishSubject.create();

    /**
     * Since we're holding a reference to the Master a.k.a Activity/Master Frag
     * remember to explicitly remove the worker fragment or you'll have a mem leak in your hands.
     * <p>
     * See {@link RxJavaAndroidSampleActivity#onBackPressed()}
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        List<Fragment> fragments = ((RxJavaAndroidSampleActivity) activity).getSupportFragmentManager().getFragments();
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

        mStoredIntsSubscription = Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long aLong) {
                        return aLong.intValue();
                    }
                })
                .take(20)
                .subscribe(mIntStream);

    }

    /**
     * The Worker fragment has started doing it's thing
     */
    @Override
    public void onResume() {
        super.onResume();
        mMasterFrag.setStream(mIntStream.asObservable());
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
        void setStream(Observable<Integer> intStream);
    }
}
