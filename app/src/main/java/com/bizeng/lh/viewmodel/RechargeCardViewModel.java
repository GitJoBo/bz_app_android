package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.bean.WalletInformationBean;
import com.bizeng.lh.model.AppModel;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class RechargeCardViewModel extends BaseViewModel {
    AppModel mAppModel;

    public RechargeCardViewModel(@NonNull Application application) {
        super(application);
        mAppModel = new AppModel();
    }

    //充卡地址
    private SingleSourceLiveData<WalletInformationBean> cardRechargeAddress = new SingleSourceLiveData<>();
    //教程专区
    public SingleSourceLiveData<TutorialAreaBean> tutorialArea = new SingleSourceLiveData<>();
    //说明
    public String[] mStrInfo = new String[]{
            "请勿向上述地址支付任何非 TRC20，否则将无法正确兑换点卡。且可能导致您的意外损失且不可追回。务必确认自身的网络安全。" +
                    " <br /><br />" +
                    "提交兑换后，区块链网络节点认证后即完成兑换，需要一段时间等待。兑换最小单位是1USDT，小于最小单位的无法兑换。" +
                    "<br /><br />" +
                    "支付兑换点卡的USDT不予退款。" +
                    "<br /><br />" +
                    "点卡是币增平台的商品，兑换后不可兑现，不可退款，转移等。",
            "请勿向上述地址支付任何非 ERC20，否则将无法正确兑换点卡。且可能导致您的意外损失且不可追回。务必确认自身的网络安全。" +
                    " <br /><br />" +
                    "提交兑换后，区块链网络节点认证后即完成兑换，需要一段时间等待。兑换最小单位是1USDT，小于最小单位的无法兑换。" +
                    "<br /><br />" +
                    "支付兑换点卡的USDT不予退款。" +
                    "<br /><br />" +
                    "点卡是币增平台的商品，兑换后不可兑现，不可退款，转移等。"
    };

    public SingleSourceLiveData<WalletInformationBean> getCardRechargeAddress() {
        return cardRechargeAddress;
    }

    public void requestCardRechargeAddress() {
        Observable<WalletInformationBean> walletInformationBeanObservable = mAppModel.requestUserWallet();
        loadingDisposable(walletInformationBeanObservable, s -> {
            cardRechargeAddress.postValue(s);
        });
    }

    /**
     * 教程专区
     *
     * @param titles "量化介绍,买币指导,安全保障,火币API设置教程,币安API设置教程"
     */
    public void requestTutorialArea(String titles) {
        Observable<List<TutorialAreaBean>> observable1 = mAppModel.requestTutorialArea(titles);
        loadingDisposable(observable1, s -> {
            if (s != null && s.size() > 0) {
                tutorialArea.postValue(s.get(0));
            }
        }, false);
    }
}
