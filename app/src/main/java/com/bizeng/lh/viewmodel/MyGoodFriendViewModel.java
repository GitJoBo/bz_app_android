package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.UserTeamBean;
import com.bizeng.lh.http.Api;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

public class MyGoodFriendViewModel extends BaseViewModel {
    public MyGoodFriendViewModel(@NonNull Application application) {
        super(application);
    }

    //团队信息
    private SingleSourceLiveData<UserTeamBean> userTeam = new SingleSourceLiveData<>();

    public LiveData<UserTeamBean> getUserTeam() {
        return userTeam;
    }

    public void requestUserTeam() {
        Observable<UserTeamBean> observable = RxHttp.get(Api.USER_TEAM).asResponse(UserTeamBean.class);
        loadingDisposable(observable, s -> {
            userTeam.postValue(s);
        });
    }
}
