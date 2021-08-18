package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.http.Api;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

public class HistoricalWithdrawalAddressViewModel extends BaseViewModel {
    public HistoricalWithdrawalAddressViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 兑换地址列表
     */
    public SingleSourceLiveData<List<String>> withdrawalAddressList = new SingleSourceLiveData();

    /**
     * 获取兑换地址列表
     *
     * @param type 0-ERC20;1-TRC20
     */
    public void requestWithdrawalAddressList(int type) {
        Observable<List<String>> observable = RxHttp.get(Api.WITHDRAWAL_ADDRESS_LIST + type).asResponseList(String.class);
        loadingDisposable(observable, s -> {
            withdrawalAddressList.postValue(s);
        });
    }
}
