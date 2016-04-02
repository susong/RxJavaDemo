package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.dream.rxjava.R;
import com.dream.rxjava.XLog;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.jakewharton.rxbinding.widget.RxCompoundButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/4/2 下午11:23
 * Description: RxJavaDemo
 */
public class RxSharedPreferencesAndRxBindingFragment extends BaseFragment {
    @Bind(R.id.checkBox) CheckBox mCheckBox;
    private Subscription mRxCompoundButtonSubscription;
    private Subscription mRxSharedPreferencesSubscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        XLog.d("onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rx_ui, container, false);
        ButterKnife.bind(this, view);
        initCheckBox();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mRxCompoundButtonSubscription.unsubscribe();
        mRxSharedPreferencesSubscription.unsubscribe();
    }

    private void initCheckBox() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        Preference<Boolean> checkBox = rxPreferences.getBoolean("checkBox", false);

        // Update the checkbox when the preference changes.
        mRxSharedPreferencesSubscription = checkBox.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(RxCompoundButton.checked(mCheckBox));//这种方式不会出现mCheckBox为空的情况。
                .subscribe(new Action1<Boolean>() {//这种方式在call方法中会出现mCheckBox为空的情况，解决方法是xxxSubscription.unsubscribe()
                    @Override
                    public void call(Boolean aBoolean) {
                        XLog.d("checked:" + aBoolean);
                        Toast.makeText(getContext(), "checked:" + aBoolean, Toast.LENGTH_SHORT).show();
                        mCheckBox.setChecked(aBoolean);
                    }
                });

        // Update preference when the checkbox state changes.
        mRxCompoundButtonSubscription = RxCompoundButton.checkedChanges(mCheckBox)
                .skip(1)// Skip the initial value.
                .subscribe(checkBox.asAction());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
