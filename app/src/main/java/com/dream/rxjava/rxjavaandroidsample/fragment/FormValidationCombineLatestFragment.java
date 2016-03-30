package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dream.rxjava.R;
import com.dream.rxjava.rxjavaandroidsample.RxUtils;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func3;
import timber.log.Timber;


/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/30 上午11:36
 * Description: RxJavaDemo
 */
public class FormValidationCombineLatestFragment extends BaseFragment {
    @Bind(R.id.demo_combl_email) EditText mDemoComblEmail;
    @Bind(R.id.demo_combl_password) EditText mDemoComblPassword;
    @Bind(R.id.demo_combl_num) EditText mDemoComblNum;
    @Bind(R.id.btn_demo_form_valid) TextView mBtnDemoFormValid;


    private Observable<CharSequence> emailChangeObservable;
    private Observable<CharSequence> passwordChangeObservable;
    private Observable<CharSequence> numberChangeObservable;
    private Subscription mSubscription;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_form_validation_comb_latest,
                container,
                false);
        ButterKnife.bind(this, layout);

        emailChangeObservable = RxTextView.textChanges(mDemoComblEmail).skip(1);
        passwordChangeObservable = RxTextView.textChanges(mDemoComblPassword).skip(1);
        numberChangeObservable = RxTextView.textChanges(mDemoComblNum).skip(1);
        combineLatestEvents();
        return layout;
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

    private void combineLatestEvents() {
        mSubscription = Observable.combineLatest(
                emailChangeObservable,
                passwordChangeObservable,
                numberChangeObservable,
                new Func3<CharSequence, CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean call(
                            CharSequence newEmail,
                            CharSequence newPassword,
                            CharSequence newNumber) {

                        boolean emailValid = !TextUtils.isEmpty(newEmail) &&
                                Patterns.EMAIL_ADDRESS.matcher(newEmail).matches();
                        if (!emailValid) {
                            mDemoComblEmail.setError("Invalid Email!");
                        }

                        boolean passValid = !TextUtils.isEmpty(newPassword) && newPassword.length() > 8;
                        if (!passValid) {
                            mDemoComblPassword.setError("Invalid Password!");
                        }

                        boolean numValid = !TextUtils.isEmpty(newNumber);
                        if (numValid) {
                            int num = Integer.parseInt(newNumber.toString());
                            numValid = num > 0 && num <= 100;
                        }
                        if (!numValid) {
                            mDemoComblNum.setError("Invalid Number!");
                        }

                        return emailValid && passValid && numValid;
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "there was an error");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mBtnDemoFormValid.setBackgroundColor(getResources().getColor(R.color.blue));
                        } else {
                            mBtnDemoFormValid.setBackgroundColor(getResources().getColor(R.color.gray));
                        }
                    }
                });
    }
}
