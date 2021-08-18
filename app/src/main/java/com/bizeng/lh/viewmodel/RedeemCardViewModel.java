package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.LimitedTimeOfferBean;
import com.bizeng.lh.http.Api;
import com.bizeng.lh.model.AppModel;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;
import rxhttp.wrapper.cahce.CacheMode;

public class RedeemCardViewModel extends BaseViewModel {
    private AppModel mAppModel;

    public RedeemCardViewModel(@NonNull Application application) {
        super(application);
        mAppModel = new AppModel();
    }

    /**
     * 兑换点卡
     */
    public SingleSourceLiveData<String> redeemCard = new SingleSourceLiveData<>();
    /**
     * 增点规则
     */
    public SingleSourceLiveData<List<LimitedTimeOfferBean>> listOfBonusPoints = new SingleSourceLiveData<>();

    public SingleSourceLiveData<BigDecimal> addPointAddBonus = new SingleSourceLiveData<>();

    /**
     * 获取增点规则
     */
    public void requestListOfBonusPoints() {
        Observable<List<LimitedTimeOfferBean>> observable = RxHttp.get(Api.ADDED_POINT_LIST)
                .asResponseList(LimitedTimeOfferBean.class);
        loadingDisposable(observable, s -> {
            listOfBonusPoints.postValue(s);
        });
    }

    /**
     * 获取增上加赠比例
     */
    public void requestRequestAddPointAddBonus() {
        Observable<BigDecimal> observable = RxHttp.get(Api.ADDED_POINT_ADD_BONUS).asResponse(BigDecimal.class);
        loadingDisposable(observable, s -> {
            addPointAddBonus.postValue(s);
        });
    }

    /**
     * 倒计时
     *
     * @return
     */
    public MutableLiveData<Integer> getCodeCountDown() {
        return mAppModel.codeCountDown;
    }

    /**
     * 申请兑换
     */
    public void requestRedeemCard(String code, String money) {
        Observable<String> observable = RxHttp.postJson(Api.REDEEM_CARD)
                .setCacheMode(CacheMode.ONLY_NETWORK)
                .add("code", code)
                .add("money", money)
                .asResponse(String.class);
        loadingDisposable(observable, s -> {
            mAppModel.stopCodeCountDown();
            redeemCard.postValue(s);
        });
    }

    /**
     * 发验证码
     */
    public void sendCode() {
        Observable<String> observable = RxHttp.postJson(Api.REDEEM_CARD_CODE).asResponse(String.class);
        loadingDisposable(observable, s -> {
            mAppModel.startCodeCountDown();
        });
    }
}
