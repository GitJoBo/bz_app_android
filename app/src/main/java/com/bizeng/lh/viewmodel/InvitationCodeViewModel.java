package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.http.Api;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

public class InvitationCodeViewModel extends BaseViewModel {
    public InvitationCodeViewModel(@NonNull Application application) {
        super(application);
    }

    //提交邀请码
    private SingleSourceLiveData<String> submitInvitationCode = new SingleSourceLiveData<>();

    public LiveData<String> getSubmitInvitationCode() {
        return submitInvitationCode;
    }

    public void requestSubmitInvitationCode(String code) {
        Observable<String> observable = RxHttp.postJson(Api.SET_INVITATION_CODE + code).asResponse(String.class);
        loadingDisposable(observable, s -> {
            submitInvitationCode.postValue(s);
        });
    }
}
