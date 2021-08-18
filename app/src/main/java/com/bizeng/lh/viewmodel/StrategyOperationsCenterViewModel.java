package com.bizeng.lh.viewmodel;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.PageList;
import com.bizeng.lh.bean.StrategyBean;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.http.Api;
import com.bizeng.lh.model.AppModel;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;
import com.wzq.mvvmsmart.utils.resource.Resource;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

public class StrategyOperationsCenterViewModel extends BaseViewModel {
    public StrategyOperationsCenterViewModel(@NonNull Application application) {
        super(application);
        mAppModel = new AppModel();
    }

    private AppModel mAppModel;

    //删除策略
    private SingleSourceLiveData<String> delStrategy = new SingleSourceLiveData<>();
    //重置策略
    private SingleSourceLiveData<String> resetStrategy = new SingleSourceLiveData<>();
    //暂停策略
    private SingleSourceLiveData<String> stopStrategy = new SingleSourceLiveData<>();
    //开启策略
    private SingleSourceLiveData<Resource<String>> startStrategy = new SingleSourceLiveData<>();
    //修改仓库容量
    private SingleSourceLiveData<String> modifyWarehouseCapacity = new SingleSourceLiveData<>();
    //获取策略列表 apiId
    private SingleSourceLiveData<PageList<StrategyBean>> strategyInfoList = new SingleSourceLiveData<>();
    //教程专区
    public SingleSourceLiveData<TutorialAreaBean> tutorialArea = new SingleSourceLiveData<>();

    //倒计时
    private MutableLiveData<Integer> codeCountDown = new MutableLiveData<>();

    /**
     * 倒计时器
     */
    private CountDownTimer countDownTimer = new CountDownTimer(6 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            int count = Math.round(millisUntilFinished / 1000);
            codeCountDown.postValue(count);
        }

        @Override
        public void onFinish() {
            codeCountDown.postValue(-1);
        }
    };

    /**
     * 倒计时
     *
     * @return
     */
    public LiveData<Integer> getCodeCountDown() {
        return codeCountDown;
    }

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
        countDownTimer.cancel();
    }

    public LiveData<String> getDelStrategy() {
        return delStrategy;
    }

    public LiveData<String> getResetStrategy() {
        return resetStrategy;
    }

    public LiveData<String> getStopStrategy() {
        return stopStrategy;
    }

    public LiveData<Resource<String>> getStartStrategy() {
        return startStrategy;
    }

    public LiveData<String> getModifyWarehouseCapacity() {
        return modifyWarehouseCapacity;
    }

    public LiveData<PageList<StrategyBean>> getStrategyInfoList() {
        return strategyInfoList;
    }

    /**
     * 删除策略
     *
     * @param userStrategyId
     */
    public void requestDelStrategy(String userStrategyId) {
        Observable<String> observable = RxHttp.deleteJson(Api.DEL_STRATEGY + userStrategyId).asResponse(String.class);
        loadingDisposable(observable, s -> {
            delStrategy.postValue(s);
        }, true);
    }

    /**
     * 重置策略
     *
     * @param userStrategyId
     */
    public void requestResetStrategy(String userStrategyId) {
        Observable<String> observable = RxHttp.putJson(Api.RESET_STRATEGY + userStrategyId).asResponse(String.class);
        loadingDisposable(observable, s -> {
            resetStrategy.postValue(s);
        }, true);
    }

    /**
     * 停止策略
     *
     * @param userStrategyId
     */
    public void requestStopStrategy(String userStrategyId) {
        Observable<String> observable = RxHttp.putJson(Api.STOP_STRATEGY + userStrategyId).asResponse(String.class);
        loadingDisposable(observable, s -> {
            stopStrategy.postValue(s);
        }, true);
    }

    /**
     * 开启策略
     *
     * @param userStrategyId
     */
    public void requestStartStrategy(String userStrategyId, String capacity) {
        Observable<String> observable = RxHttp.postJson(Api.START_STRATEGY)
                .add("userStrategyId", userStrategyId)
                .add("capacity", capacity)
                .asResponse(String.class);
        loadingDisposable(observable, s -> {
            startStrategy.postValue(Resource.success(s));
        }, error -> {
            startStrategy.postValue(Resource.error(error.getErrorCode(), null,error.getErrorMsg()));
        }, true);
    }

    /**
     * 修改仓库容量
     *
     * @param userStrategyId
     * @param capacity
     */
    public void requestModifyWarehouseCapacity(String userStrategyId, String capacity) {
        Observable<String> observable = RxHttp.putJson(Api.MODIFY_WAREHOUSE_CAPACITY)
                .add("userStrategyId", userStrategyId)
                .add("capacity", capacity)
                .asResponse(String.class);
        loadingDisposable(observable, s -> {
            modifyWarehouseCapacity.postValue(s);
        }, true);
    }

    /**
     * 获取策略列表 apiId
     *
     * @param apiId
     * @param page
     * @param pageNum
     */
    public void requestStrategyInfoList(String apiId, int page, int pageNum) {
        Observable<PageList<StrategyBean>> observable = RxHttp.get(Api.STRATEGY_CENTER_LIST).add("apiId", apiId).asResponsePageList(StrategyBean.class);
        loadingDisposable(observable, s -> {
            strategyInfoList.postValue(s);
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
