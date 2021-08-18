package com.bizeng.lh.model;

import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;

import com.bizeng.lh.bean.LeaderBoardBean;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.bean.U2CNYBean;
import com.bizeng.lh.bean.UserBean;
import com.bizeng.lh.bean.VipBean;
import com.bizeng.lh.bean.WalletInformationBean;
import com.bizeng.lh.http.Api;
import com.wzq.mvvmsmart.base.BaseModelMVVM;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

/**
 * @Desc: 公用数据提供类
 * @author: admin wsj
 * @Date: 2021/5/6 3:37 PM
 */
public class AppModel extends BaseModelMVVM {

    /**
     * 登录校验
     *
     * @return
     */
    public Observable<String> loginCheck() {
        return RxHttp.postJson(Api.LOGIN_CHECK).asResponse(String.class);
    }

    /**
     * 教程专区
     *
     * @param titles "量化介绍,买币指导,安全保障,火币API设置教程,币安API设置教程"
     */
    public Observable<List<TutorialAreaBean>> requestTutorialArea(String titles) {
        return RxHttp.get(Api.INFORMATION_HELP + titles).asResponseList(TutorialAreaBean.class);
    }

    /**
     * 获取vip等级
     *
     * @return
     */
    public Observable<List<VipBean>> requestVip() {
        return RxHttp.get(Api.VIP).asResponseList(VipBean.class);
    }

    /**
     * 获取用户信息
     */
    public Observable<UserBean> requestUserInfo() {
        return RxHttp.get(Api.USER_INFO).asResponse(UserBean.class);
    }

    /**
     * 钱包信息
     */
    public Observable<WalletInformationBean> requestUserWallet() {
        return RxHttp.get(Api.WALLET_INFORMATION).asResponse(WalletInformationBean.class);
    }

    /**
     * 获取cny u转换比例 usdt_price_cny
     */
    public Observable<U2CNYBean> requestU2CNY() {
        return RxHttp.get(Api.USDT_PRICE_CNY).asResponse(U2CNYBean.class);
    }

    /**
     * 排行榜
     */
    public Observable<List<LeaderBoardBean>> requestLeaderBoard(int num, int type) {
        return RxHttp.get(Api.LEADER_BOARD).add("num", num).add("type", type).asResponseList(LeaderBoardBean.class);
    }

    /**
     * 倒计时
     * 静态是为了实现全局倒计时
     */
    public static MutableLiveData<Integer> codeCountDown = new MutableLiveData<>();
    /**
     * 倒计时器
     */
    private CountDownTimer countDownTimer = new CountDownTimer(40 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            int count = Math.round(millisUntilFinished / 1000);
            codeCountDown.postValue(count);
        }

        @Override
        public void onFinish() {
            codeCountDown.postValue(0);
        }
    };

    /**
     * 开始倒计时
     */
    public void startCodeCountDown() {
        countDownTimer.cancel();
        countDownTimer.start();
    }

    /**
     * 取消倒计时
     */
    public void stopCodeCountDown() {
        codeCountDown.postValue(-1);
        countDownTimer.cancel();
    }

}
