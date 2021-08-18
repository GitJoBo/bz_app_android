package com.bizeng.lh.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.http.Api;
import com.bizeng.lh.model.AppModel;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;
import com.wzq.mvvmsmart.utils.ToastUtils;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

public class ChangePhoneViewModel extends BaseViewModel {
    public ChangePhoneViewModel(@NonNull Application application) {
        super(application);
        mAppModel = new AppModel();
    }

    AppModel mAppModel;

    /**
     * 当前手机发送验证码
     */
//    public SingleSourceLiveData<String> sendVerificationCodeFromCurrentMobilePhone = new SingleSourceLiveData<>();

    /**
     * 下一步验证
     */
    public SingleSourceLiveData<String> nextChangePhone = new SingleSourceLiveData<>();

    /**
     * 新手机发送验证码
     */
//    public SingleSourceLiveData<String> sendVerificationCodeOnNewPhone = new SingleSourceLiveData<>();

    /**
     * 更换手机号
     */
    public SingleSourceLiveData<String> changeMobilePhoneNumber = new SingleSourceLiveData<>();

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
        Observable<String> observable = RxHttp.postJson(Api.SEND_VERIFICATION_CURRENT_PHONE).asResponse(String.class);
        loadingDisposable(observable, s -> {
            mAppModel.startCodeCountDown();
        });
    }

    /**
     * 下一步验证
     */
    public void requestNextChangPhone(String code) {
        Observable<String> observable = RxHttp.postJson(Api.VERIFY_OLD_PHONE_VERIFICATION_CODE + code).asResponse(String.class);
        loadingDisposable(observable, s -> {
            mAppModel.stopCodeCountDown();
            nextChangePhone.postValue(s);
        });
    }

    /**
     * 新手机发送验证码
     */
    public void requestSendVerificationCodeOnNewPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("请输入手机号");
            return;
        }
        Observable<String> observable = RxHttp.postJson(Api.SEND_REPLACEMENT_PHONE_VERIFICATION_CODE + phone).asResponse(String.class);
        loadingDisposable(observable, s -> {
            mAppModel.startCodeCountDown();
        });
    }

    /**
     * 更换手机
     */
    public void requestChangeMobilePhoneNumber(String code, String phone) {
        Observable<String> observable = RxHttp.postJson(Api.CHANGE_PHONE)
                .add("code", code)
                .add("phone", phone).asResponse(String.class);
        loadingDisposable(observable, s -> {
            mAppModel.stopCodeCountDown();
            changeMobilePhoneNumber.postValue(s);
        });
    }
}
