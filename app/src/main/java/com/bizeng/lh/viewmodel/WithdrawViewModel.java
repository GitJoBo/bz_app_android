package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.FeeConfiguration;
import com.bizeng.lh.http.Api;
import com.bizeng.lh.model.AppModel;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;
import rxhttp.wrapper.cahce.CacheMode;

public class WithdrawViewModel extends BaseViewModel {
    public WithdrawViewModel(@NonNull Application application) {
        super(application);
        mAppModel = new AppModel();
    }

    private AppModel mAppModel;

    /**
     * 兑换
     */
    public SingleSourceLiveData<String> withdraw = new SingleSourceLiveData<>();
    /**
     * 手续费配置
     */
    public SingleSourceLiveData<FeeConfiguration> feeConfiguration = new SingleSourceLiveData<>();

    /**
     * 倒计时
     *
     * @return
     */
    public MutableLiveData<Integer> getCodeCountDown() {
        return mAppModel.codeCountDown;
    }

    /**
     * 当前手机发送验证码
     */
    public void requestSendVerificationCodeFromCurrentMobilePhone() {
        Observable<String> observable = RxHttp.postJson(Api.WITHDRAWAL_CODE).asResponse(String.class);
        loadingDisposable(observable, s -> {
            mAppModel.startCodeCountDown();
        });
    }

    /**
     * 兑换
     *
     * @param address
     * @param code     验证码
     * @param coinType 币种类型:erc20-0;trc20-1
     * @param money
     */
    public void requestWithdraw(String address, String code, int coinType, String money) {
        Observable<String> observable = RxHttp.postJson(Api.WITHDRAW)
                .setCacheMode(CacheMode.ONLY_NETWORK)
                .add("address", address)
                .add("code", code)
                .add("coinType", coinType)
                .add("money", money)
                .asResponse(String.class);
        loadingDisposable(observable, s -> {
            mAppModel.stopCodeCountDown();
            withdraw.postValue(s);
        }, true);
    }

    /**
     * 获取手续费配置
     */
    public void requestFeeConfiguration() {
        Observable<FeeConfiguration> observable = RxHttp.get(Api.WITHDRAWAL_CONFIG).asResponse(FeeConfiguration.class);
        loadingDisposable(observable, s -> {
            feeConfiguration.postValue(s);
        });
    }


}
