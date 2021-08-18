package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.bean.UserBean;
import com.bizeng.lh.http.Api;
import com.bizeng.lh.model.AppModel;
import com.bizeng.lh.utils.MMKVUtils;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;
import com.wzq.mvvmsmart.utils.ToastUtils;
import com.wzq.mvvmsmart.utils.resource.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;
import rxhttp.wrapper.cahce.CacheMode;
import rxhttp.wrapper.utils.GsonUtil;

public class LoginViewModel extends BaseViewModel {
    private AppModel mAppModel = null;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        mAppModel = new AppModel();
    }

    //倒计时
//    private MutableLiveData<Integer> codeCountDown = new MutableLiveData<>();
    //发送验证码
//    private SingleSourceLiveData<Object> sendVerificationCode = new SingleSourceLiveData<>();
    //登录
    private SingleSourceLiveData<Resource<UserBean>> login = new SingleSourceLiveData<>();
    //教程专区
    public SingleSourceLiveData<TutorialAreaBean> tutorialArea = new SingleSourceLiveData<>();


    /**
     * 开始倒计时
     */
    private void startCodeCountDown() {
        mAppModel.startCodeCountDown();
    }

    /**
     * 取消倒计时
     */
    private void stopCodeCountDown() {
        mAppModel.stopCodeCountDown();
    }

    /**
     * 验证码接受倒计时
     *
     * @return
     */
    public LiveData<Integer> getCodeCountDown() {
        return AppModel.codeCountDown;
    }

    /**
     * 发送验证码
     */
    public void requestSendVerificationCode(String phone) {
        Observable<String> observable = RxHttp.postJson(Api.LOGIN_CODE + phone).setCacheMode(CacheMode.ONLY_NETWORK).asResponse(String.class);
        loadingDisposable(observable, s -> {
            //请求成功
            startCodeCountDown();
            ToastUtils.showShort("验证码发送成功");
        }, true);
    }

    /**
     * 登录
     */
    public void requestLogin(String phone, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        Observable<UserBean> userObservable = RxHttp.postJson(Api.LOGIN)
                .setCacheMode(CacheMode.ONLY_NETWORK)
                .addAll(map)
                .asResponse(UserBean.class);
        loadingDisposable(userObservable, user -> {
            stopCodeCountDown();
            login.postValue(Resource.success(user));
            MMKVUtils.getInstance().setUser(user);
            MMKVUtils.getInstance().setToken(user.token);
            MMKVUtils.getInstance().setApis(GsonUtil.toJson(user.apis));
            MMKVUtils.getInstance().setVipIntegral(user.vipIntegral);
        }, error -> {
            login.postValue(Resource.error(error.getErrorCode(), null));
            ToastUtils.showShort(error.getErrorMsg());
        }, true);
    }

    public LiveData<Resource<UserBean>> getLogin() {
        return login;
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
