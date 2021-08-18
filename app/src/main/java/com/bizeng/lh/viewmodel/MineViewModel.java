package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.bean.UserBean;
import com.bizeng.lh.bean.VipBean;
import com.bizeng.lh.model.AppModel;
import com.bizeng.lh.utils.MMKVUtils;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public class MineViewModel extends BaseViewModel {
    private AppModel mAppModel = null;

    public MineViewModel(@NonNull Application application) {
        super(application);
        mAppModel = new AppModel();
    }

    //获取vip等级
    private SingleSourceLiveData<List<VipBean>> vip = new SingleSourceLiveData<>();
    //用户信息
    private SingleSourceLiveData<UserBean> userInfo = new SingleSourceLiveData<>();
    //vip 显示对应
    private Map<String, String> mCardNameMap = new HashMap<>();

    //教程专区
    public SingleSourceLiveData<TutorialAreaBean> tutorialArea = new SingleSourceLiveData<>();

    public SingleSourceLiveData<List<VipBean>> getVip() {
        return vip;
    }

    public SingleSourceLiveData<UserBean> getUserInfo() {
        return userInfo;
    }

//    public Map<String, String> getCardNameMap() {
//        if (mCardNameMap.size() < 1) {
//            mCardNameMap.put("PT", "普通会员");
//            mCardNameMap.put("GV", "金卡会员");
//            mCardNameMap.put("PV", "铂金会员");
//            mCardNameMap.put("DV", "钻石会员");
//            mCardNameMap.put("MAX", "百夫长会员");
//        }
//        return mCardNameMap;
//    }

    /**
     * 获取vip
     *
     * @return
     */
    public Map<String, VipBean> getLocationVipMap() {
        return MMKVUtils.getInstance().getVipsMap();
    }

    public List<VipBean> getLocationVipList() {
        return MMKVUtils.getInstance().getVips();
    }

    public void requestVip() {
        Observable<List<VipBean>> observable = mAppModel.requestVip();
        loadingDisposable(observable, s -> {
            vip.postValue(s);
        }, error -> {
        }, false);
    }

    public void requestUserInfo() {
        Observable<UserBean> observable = mAppModel.requestUserInfo();
        loadingDisposable(observable, s -> {
            userInfo.postValue(s);
            MMKVUtils.getInstance().setVipIntegral(s.vipIntegral);
        }, error -> {
            userInfo.postValue(null);
        }, false);
    }

    /**
     * 教程专区
     *
     * @param titles "量化介绍,买币指导,安全保障,火币API设置教程,币安API设置教程,联系客服"
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
