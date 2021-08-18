package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.ApiBean;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.http.Api;
import com.bizeng.lh.model.AppModel;
import com.bizeng.lh.utils.MMKVUtils;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;
import com.wzq.mvvmsmart.utils.ToastUtils;
import com.wzq.mvvmsmart.utils.resource.Resource;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;
import rxhttp.wrapper.cahce.CacheMode;
import rxhttp.wrapper.utils.GsonUtil;

public class ApiManagerViewModel extends BaseViewModel {
    AppModel mAppModel;
    public ApiManagerViewModel(@NonNull Application application) {
        super(application);
        mAppModel = new AppModel();
    }

    //用户api列表
    private SingleSourceLiveData<Resource<List<ApiBean>>> apiList = new SingleSourceLiveData<>();
    //api/userApi 保存api信息结果
    private SingleSourceLiveData<Resource<String>> saveUserApi = new SingleSourceLiveData<>();
    ///api/userApi/getIp 交易服务器ip
    private SingleSourceLiveData<Resource<String>> transactionServerIp = new SingleSourceLiveData<>();
    //教程专区
    private SingleSourceLiveData<TutorialAreaBean> tutorialArea = new SingleSourceLiveData<>();


    public LiveData<Resource<List<ApiBean>>> getApiList() {
        return apiList;
    }

    public void requestApiList() {
        Observable<List<ApiBean>> listObservable = RxHttp.get(Api.USER_API_LIST)
                .asResponseList(ApiBean.class);
        loadingDisposable(listObservable, s -> {
            apiList.postValue(Resource.success(s));
            MMKVUtils.getInstance().setApis(GsonUtil.toJson(s));
        }, err -> {
        }, false);
    }

    public LiveData<Resource<String>> getSaveUserApi() {
        return saveUserApi;
    }

    public void requestSaveUserApi(String apiContent, String apiTag) {
        Observable<String> observable = RxHttp.putJson(Api.SAVE_USER_API)
                .setCacheMode(CacheMode.ONLY_NETWORK)
                .add("apiContent", apiContent)
                .add("apiTag", apiTag)
                .asResponse(String.class);
        loadingDisposable(observable,
                s -> {
                    ToastUtils.showShort(s);
                    saveUserApi.postValue(Resource.success(s));
                },
                true);
    }

    public LiveData<Resource<String>> getTransactionServiceIp() {
        return transactionServerIp;
    }

    public void requestTransactionServiceIp() {
        Observable<String> observable = RxHttp.get(Api.GET_SERVICE_IP)
                .asResponse(String.class);
        loadingDisposable(observable, s -> {
            transactionServerIp.postValue(Resource.success(s));
        });
    }

    public SingleSourceLiveData<TutorialAreaBean> getTutorialArea() {
        return tutorialArea;
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










