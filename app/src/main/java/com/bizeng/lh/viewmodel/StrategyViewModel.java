package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.PageList;
import com.bizeng.lh.bean.StrategyBean;
import com.bizeng.lh.bean.UserBean;
import com.bizeng.lh.http.Api;
import com.bizeng.lh.model.AppModel;
import com.bizeng.lh.utils.MMKVUtils;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;
import com.wzq.mvvmsmart.utils.resource.Resource;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

public class StrategyViewModel extends BaseViewModel {
    public static final String API = "api";
    public static final String CENTER = "center";
    AppModel mAppModel;

    public StrategyViewModel(@NonNull Application application) {
        super(application);
        mAppModel = new AppModel();
    }

    //获取登录信息
    private SingleSourceLiveData<UserBean> loginUser = new SingleSourceLiveData<>();
    //精选策略列表 AI量化策略
    private SingleSourceLiveData<Resource<PageList<StrategyBean>>> featuredStrategyList = new SingleSourceLiveData<>();
    private SingleSourceLiveData<String> loginCheck = new SingleSourceLiveData<>();


    public LiveData<UserBean> getLoginUer() {
        return loginUser;
    }

    public LiveData<Resource<PageList<StrategyBean>>> getFeaturedStrategyList() {
        return featuredStrategyList;
    }


    public LiveData<String> getLoginCheck() {
        return loginCheck;
    }

    public void requestLoginUser() {
        loginUser.postValue(MMKVUtils.getInstance().getUser());
    }

    /**
     * 登录校验
     *
     * @param type api API管理，center 策略操作中心
     */
    public void requestLoginCheck(String type) {
        Observable<String> observable = mAppModel.loginCheck();
        loadingDisposable(observable, s -> {
            loginCheck.postValue(type);
        });
    }

    /**
     * 精选策略列表 AI量化策略
     */
    public void requestFeaturedStrategyList() {
        Observable<PageList<StrategyBean>> observable = RxHttp.get(Api.STRATEGY_LIST).asResponsePageList(StrategyBean.class);
        loadingDisposable(observable,
                s -> featuredStrategyList.postValue(Resource.success(s)));
    }


}
