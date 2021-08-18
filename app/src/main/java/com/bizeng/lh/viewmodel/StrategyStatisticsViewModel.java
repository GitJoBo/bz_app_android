package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.UserStrategyBean;
import com.bizeng.lh.http.Api;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

public class StrategyStatisticsViewModel extends BaseViewModel {
    public StrategyStatisticsViewModel(@NonNull Application application) {
        super(application);
    }

    //用户策略信息
    public SingleSourceLiveData<UserStrategyBean> userPolicyInformation = new SingleSourceLiveData<>();

    /**
     * 用户策略信息
     */
    public void requestUserPolicyInformation(String userStrategyId) {
        Observable<UserStrategyBean> observable = RxHttp.get(Api.USER_POLICY_INFORMATION + userStrategyId).asResponse(UserStrategyBean.class);
        loadingDisposable(observable, s -> {
            userPolicyInformation.postValue(s);
        });
    }
}
