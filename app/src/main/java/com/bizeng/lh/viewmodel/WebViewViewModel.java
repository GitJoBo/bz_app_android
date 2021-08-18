package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.ApiBean;
import com.bizeng.lh.bean.PageList;
import com.bizeng.lh.bean.StrategyBean;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.http.Api;
import com.bizeng.lh.model.AppModel;
import com.bizeng.lh.utils.MMKVUtils;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

public class WebViewViewModel extends BaseViewModel {
    AppModel mAppModel;
    public WebViewViewModel(@NonNull Application application) {
        super(application);
        mAppModel = new AppModel();
    }

    //获取策略信息 strategyId 不分页
    private SingleSourceLiveData<List<StrategyBean>> strategyInfo = new SingleSourceLiveData<>();
    //已帮策略apiId
    private SingleSourceLiveData<List<String>> apiIds = new SingleSourceLiveData<>();
    //添加策略
    private SingleSourceLiveData<String> addStrategy = new SingleSourceLiveData<>();
    //获取绑定的apis
    private SingleSourceLiveData<Map<String, ApiBean>> apiBeans = new SingleSourceLiveData<>();
    //教程专区
    public SingleSourceLiveData<TutorialAreaBean> tutorialArea = new SingleSourceLiveData<>();

    /**
     * 获取vip积分值 > 100是金卡会员
     * @return
     */
    public int getVipIntegral() {
        return MMKVUtils.getInstance().getVipIntegral();
    }

    public LiveData<List<StrategyBean>> getStrategyInfo() {
        return strategyInfo;
    }

    public LiveData<List<String>> getApiIds() {
        return apiIds;
    }

    public LiveData<String> getAddStrategy() {
        return addStrategy;
    }

    public LiveData<Map<String, ApiBean>> getApiBeans() {
        return apiBeans;
    }

    public void requestStrategyInfo(String strategyId) {
        Observable<PageList<StrategyBean>> observable = RxHttp.get(Api.STRATEGY_CENTER_LIST).add("strategyId", strategyId).asResponsePageList(StrategyBean.class);
        loadingDisposable(observable, s -> {
            strategyInfo.postValue(s.getRows());
            List<StrategyBean> rows = s.getRows();
            if (rows != null) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    list.add(rows.get(i).apiId);
                }
                apiIds.postValue(list);
            }

        }, error -> {
            //此接口不提示错误信息，用户无感知
        }, true);
    }

    /**
     * 添加策略
     * @param strategyId
     * @param apiIds
     */
    public void requestAddStrategy(String strategyId, String[] apiIds) {
//        Observable<String> observable = RxHttp.postJson(String.format("%s%s/%s", Api.ADD_STRATEGY, strategyId, apiId))
        Observable<String> observable = RxHttp.postJson(Api.ADD_STRATEGY)
                .add("apiIds",apiIds)
                .add("strategyId",strategyId)
                .asResponse(String.class);
        loadingDisposable(observable, s -> addStrategy.postValue(s), true);
    }

    public void requestApiBeans() {
        List<ApiBean> apisBe = MMKVUtils.getInstance().getApisBe();
        if (apisBe != null) {
            Map<String, ApiBean> map = new HashMap<>();
            for (int i = 0; i < apisBe.size(); i++) {
                ApiBean apiBean = apisBe.get(i);
                map.put(apiBean.apiTag, apiBean);
            }
            apiBeans.postValue(map);
        }
    }

    /**
     * 教程专区
     *
     * @param titles "量化介绍,买币指导,安全保障,火币API设置教程,币安API设置教程,如何成为金卡"
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
