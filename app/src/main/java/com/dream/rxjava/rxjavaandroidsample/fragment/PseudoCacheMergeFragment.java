package com.dream.rxjava.rxjavaandroidsample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dream.rxjava.R;
import com.dream.rxjava.XLog;
import com.dream.rxjava.rxjavaandroidsample.retrofit.Contributor;
import com.dream.rxjava.rxjavaandroidsample.retrofit.GithubApi;
import com.dream.rxjava.rxjavaandroidsample.retrofit.GithubService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/3/30 下午8:25
 * Description: RxJavaDemo
 */
public class PseudoCacheMergeFragment extends BaseFragment {
    @Bind(R.id.btn_start_pseudo_cache) Button mBtnStartPseudoCache;


    private HashMap<String, Long> mContributionMap = null;
    private HashMap<Contributor, Long> mResultAgeMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_pseudo_cache_concat, container, false);
        ButterKnife.bind(this, layout);
        initializeCache();
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_start_pseudo_cache)
    public void onClick() {
        Observable
                .merge(getCachedData(), getFreshData())
//                .concat(getCachedData(), getFreshData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Pair<Contributor, Long>>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("done loading all data");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "arr something went wrong");
                    }

                    @Override
                    public void onNext(Pair<Contributor, Long> contributorLongPair) {
                        XLog.d("onNext : " + contributorLongPair.first.getLogin());
                        Contributor contributor = contributorLongPair.first;

                        if (mResultAgeMap.containsKey(contributor) &&
                                mResultAgeMap.get(contributor) > contributorLongPair.second) {
                            return;
                        }

                        mContributionMap.put(contributor.getLogin(), contributor.getContributions());
                        mResultAgeMap.put(contributor, contributorLongPair.second);

                        mLogAdapter.clear();
                        mLogAdapter.addAll(getListStringFromMap());
                    }
                });
    }

    private List<String> getListStringFromMap() {
        List<String> list = new ArrayList<>();

        for (String username : mContributionMap.keySet()) {
            String rowLog = String.format("%s [%d]", username, mContributionMap.get(username));
            list.add(rowLog);
        }

        return list;
    }


    private void initializeCache() {
        mContributionMap = new HashMap<>();
        mContributionMap.put("JakeWharton", 0l);
        mContributionMap.put("pforhan", 0l);
        mContributionMap.put("edenman", 0l);
        mContributionMap.put("swankjesse", 0l);
        mContributionMap.put("bruceLee", 0l);
    }

    private Observable<Pair<Contributor, Long>> getCachedData() {
        XLog.d("getCachedData");

        List<Pair<Contributor, Long>> list = new ArrayList<>();
        for (String username : mContributionMap.keySet()) {
            Contributor c = new Contributor();
            c.setLogin(username);
            c.setContributions(mContributionMap.get(username));

            Pair<Contributor, Long> dataWithAgePair = new Pair<>(c, System.currentTimeMillis());
            list.add(dataWithAgePair);
        }
        return Observable.from(list)
                .map(new Func1<Pair<Contributor, Long>, Pair<Contributor, Long>>() {
                    @Override
                    public Pair<Contributor, Long> call(Pair<Contributor, Long> contributorLongPair) {
                        XLog.d("map call");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return contributorLongPair;
                    }
                });
    }

    private Observable<Pair<Contributor, Long>> getFreshData() {
        XLog.d("getFreshData");
        String githubToken = getResources().getString(R.string.github_oauth_token);
        GithubApi githubService = GithubService.createGithubService(githubToken);
        return githubService.contributors("square", "retrofit")
                .flatMap(new Func1<List<Contributor>, Observable<Contributor>>() {
                    @Override
                    public Observable<Contributor> call(List<Contributor> contributors) {
                        return Observable.from(contributors);
                    }
                })
                .map(new Func1<Contributor, Pair<Contributor, Long>>() {
                    @Override
                    public Pair<Contributor, Long> call(Contributor contributor) {
                        return new Pair<>(contributor, System.currentTimeMillis());
                    }
                });
    }
}
