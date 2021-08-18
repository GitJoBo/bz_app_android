package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.AppBean;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.http.Api;
import com.bizeng.lh.model.AppModel;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

public class SettingViewModel extends BaseViewModel {
    private AppModel mAppModel = null;

    public SettingViewModel(@NonNull Application application) {
        super(application);
        mAppModel = new AppModel();
    }

    //获取app版本信息
    public SingleSourceLiveData<AppBean> appBean = new SingleSourceLiveData<>();
    //教程专区
    public SingleSourceLiveData<TutorialAreaBean> tutorialArea = new SingleSourceLiveData<>();

    /**
     * 获取app版本信息
     */
    public void requestAppBean() {
        Observable<AppBean> appBeanObservable = RxHttp.get(Api.APP_INFO + "android").asResponse(AppBean.class);
        loadingDisposable(appBeanObservable, s -> {
            appBean.postValue(s);
        });
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
