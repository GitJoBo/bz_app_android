package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.HomePageBean;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.bean.U2CNYBean;
import com.bizeng.lh.http.Api;
import com.bizeng.lh.model.AppModel;
import com.bizeng.lh.utils.MMKVUtils;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

public class HomePageViewModel extends BaseViewModel {
    AppModel mAppModel;

    public HomePageViewModel(@NonNull Application application) {
        super(application);
        mAppModel = new AppModel();
    }

    //教程专区
    private SingleSourceLiveData<TutorialAreaBean> tutorialArea = new SingleSourceLiveData<>();
    //首页数据
    private SingleSourceLiveData<HomePageBean> homePageBean = new SingleSourceLiveData<>();

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

    public SingleSourceLiveData<HomePageBean> getHomePageBean() {
        return homePageBean;
    }

    /**
     * 首页数据
     */
    public void requestHomePageBean() {
        Observable<HomePageBean> observable1 = RxHttp.get(Api.HOME_PAGE).asResponse(HomePageBean.class);
        loadingDisposable(observable1, s -> {
            homePageBean.postValue(s);
        });
    }

    /**
     * 获取cny u转换比例 usdt_price_cny
     */
    public void requestU2CNY() {
        Observable<U2CNYBean> u2CNYBeanObservable = mAppModel.requestU2CNY();
        loadingDisposable(u2CNYBeanObservable, s -> {
            Content.U2CNY = s.usdt_price_cny;
            MMKVUtils.getInstance().setU2cny(s.usdt_price_cny);
        });
    }


}
