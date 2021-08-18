package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.UserTeamBean;
import com.bizeng.lh.bean.UserWalletBean;
import com.bizeng.lh.http.Api;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

public class MyEarningsViewModel extends BaseViewModel {
    public MyEarningsViewModel(@NonNull Application application) {
        super(application);
    }

    //收益信息
    public SingleSourceLiveData<UserWalletBean> userWalletBean = new SingleSourceLiveData<>();
    //团队信息
    public SingleSourceLiveData<UserTeamBean> userTeam = new SingleSourceLiveData<>();

    public void requestUserWalletBean() {
        Observable<UserWalletBean> userWalletBeanObservable = RxHttp.get(Api.USER_WALLET).asResponse(UserWalletBean.class);
        loadingDisposable(userWalletBeanObservable, s -> {
            userWalletBean.postValue(s);
        });
    }

    public void requestUserTeam() {
        Observable<UserTeamBean> observable = RxHttp.get(Api.USER_TEAM).asResponse(UserTeamBean.class);
        loadingDisposable(observable, s -> {
            userTeam.postValue(s);
        });
    }
}
