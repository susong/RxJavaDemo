package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dream.rxjava.R;
import com.dream.rxjava.rxjavaandroidsample.RxUtils;
import com.dream.rxjava.rxjavaandroidsample.retrofit.Contributor;
import com.dream.rxjava.rxjavaandroidsample.retrofit.GithubApi;
import com.dream.rxjava.rxjavaandroidsample.retrofit.GithubService;
import com.dream.rxjava.rxjavaandroidsample.retrofit.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static java.lang.String.format;


/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/28 下午10:34
 * Description: RxJavaDemo
 */
public class RetrofitFragment extends BaseFragment {


    @Bind(R.id.btn_demo_retrofit_contributors)
    Button mBtnDemoRetrofitContributors;
    @Bind(R.id.demo_retrofit_contributors_username)
    EditText mDemoRetrofitContributorsUsername;
    @Bind(R.id.demo_retrofit_contributors_repository)
    EditText mDemoRetrofitContributorsRepository;
    @Bind(R.id.btn_demo_retrofit_contributors_with_user_info)
    Button mBtnDemoRetrofitContributorsWithUserInfo;
    @Bind(R.id.demo_retrofit_contributors_with_user_info_username)
    EditText mDemoRetrofitContributorsWithUserInfoUsername;
    @Bind(R.id.demo_retrofit_contributors_with_user_info_repository)
    EditText mDemoRetrofitContributorsWithUserInfoRepository;
    private GithubApi mGithubService;

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String githubToken = "";
        mGithubService = GithubService.createGithubService(githubToken);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_retrofit, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCompositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(mCompositeSubscription);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(mCompositeSubscription);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_demo_retrofit_contributors, R.id.btn_demo_retrofit_contributors_with_user_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_demo_retrofit_contributors:
                mLogAdapter.clear();
                mCompositeSubscription.add(
                        mGithubService.contributors(mDemoRetrofitContributorsUsername.getText().toString(),
                                mDemoRetrofitContributorsRepository.getText().toString())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<List<Contributor>>() {
                                    @Override
                                    public void onCompleted() {
                                        Timber.d("Retrofit call 1 completed");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Timber.e(e, "woops we got an error while getting the list of contributors");
                                    }

                                    @Override
                                    public void onNext(List<Contributor> contributors) {
                                        for (Contributor c : contributors) {
                                            mLogAdapter.add(String.format("%s has made %d contributions to %s",
                                                    c.getLogin(),
                                                    c.getContributions(),
                                                    mDemoRetrofitContributorsRepository.getText().toString()));

                                            Timber.d("%s has made %d contributions to %s",
                                                    c.getLogin(),
                                                    c.getContributions(),
                                                    mDemoRetrofitContributorsRepository.getText().toString());
                                        }
                                    }
                                })
                );
                break;
            case R.id.btn_demo_retrofit_contributors_with_user_info:
                mLogAdapter.clear();
                mCompositeSubscription.add(
                        mGithubService.contributors(mDemoRetrofitContributorsUsername.getText().toString(),
                                mDemoRetrofitContributorsRepository.getText().toString())
                                .flatMap(new Func1<List<Contributor>, Observable<Contributor>>() {
                                    @Override
                                    public Observable<Contributor> call(List<Contributor> contributors) {
                                        return Observable.from(contributors);
                                    }
                                })
                                .flatMap(new Func1<Contributor, Observable<Pair<User, Contributor>>>() {
                                    @Override
                                    public Observable<Pair<User, Contributor>> call(Contributor contributor) {

                                        Observable<User> userObservable = mGithubService.user(contributor.getLogin())
                                                .filter(new Func1<User, Boolean>() {
                                                    @Override
                                                    public Boolean call(User user) {
                                                        return !TextUtils.isEmpty(user.getName()) && !TextUtils.isEmpty(user.getEmail());
                                                    }
                                                });

                                        return Observable.zip(userObservable, Observable.just(contributor),
                                                new Func2<User, Contributor, Pair<User, Contributor>>() {
                                                    @Override
                                                    public Pair<User, Contributor> call(User user, Contributor contributor) {
                                                        return new Pair<User, Contributor>(user, contributor);
                                                    }
                                                });
                                    }
                                })
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Pair<User, Contributor>>() {
                                    @Override
                                    public void onCompleted() {
                                        Timber.d("Retrofit call 2 completed ");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Timber.e(e, "error while getting the list of contributors along with full " + "names");
                                    }

                                    @Override
                                    public void onNext(Pair<User, Contributor> pair) {
                                        User user = pair.first;
                                        Contributor contributor = pair.second;


                                        mLogAdapter.add(format("%s(%s) has made %d contributions to %s",
                                                user.getName(),
                                                user.getEmail(),
                                                contributor.getContributions(),
                                                mDemoRetrofitContributorsRepository.getText().toString()));

                                        mLogAdapter.notifyDataSetChanged();

                                        Timber.d("%s(%s) has made %d contributions to %s",
                                                user.getName(),
                                                user.getEmail(),
                                                contributor.getContributions(),
                                                mDemoRetrofitContributorsRepository.getText().toString());
                                    }
                                })
                );
                break;
        }
    }
}
