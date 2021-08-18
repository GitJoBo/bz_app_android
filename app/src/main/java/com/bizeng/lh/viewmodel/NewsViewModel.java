package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.MainstreamCurrencyMarketBean;
import com.bizeng.lh.bean.PageList;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.http.Api;
import com.bizeng.lh.http.RxHttpManager;
import com.bizeng.lh.utils.MMKVUtils;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;
import com.wzq.mvvmsmart.utils.KLog;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;
import rxhttp.wrapper.cahce.CacheMode;

public class NewsViewModel extends BaseViewModel {
    public NewsViewModel(@NonNull Application application) {
        super(application);
    }

    //资讯列表
    private SingleSourceLiveData<List<TutorialAreaBean>> newsList = new SingleSourceLiveData<>();
    //主流币行情 MainstreamCurrencyMarket
    private SingleSourceLiveData<List<MainstreamCurrencyMarketBean>> mainstreamCurrencyMarketBean = new SingleSourceLiveData<>();
    //主流币行情 缓存获取
    public SingleSourceLiveData<List<MainstreamCurrencyMarketBean>> mainstreamCurrencyMarketBeanCache = new SingleSourceLiveData<>();

    //倒计时
//    public MutableLiveData<Integer> codeCountDown = new MutableLiveData<>();
//    long millisInFuture = 15 * 60 * 1000;
//    long countDownInterval = 60 * 1000;
    /**
     * 15分 15 * 60 * 1000钟倒计时器
     */
//    private CountDownTimer countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
//        @Override
//        public void onTick(long millisUntilFinished) {
//            int count = Math.round(millisUntilFinished / countDownInterval);
//            codeCountDown.postValue(count);
//            KLog.d("行情数据倒计时：" + count);
//        }
//
//        @Override
//        public void onFinish() {
//            codeCountDown.postValue(0);
//            requestMainstreamCurrency();
//        }
//    };

//    /**
//     * 开始倒计时
//     */
//    public void startCodeCountDown() {
//        countDownTimer.cancel();
//        countDownTimer.start();
//    }
//
//    /**
//     * 取消倒计时
//     */
//    public void stopCodeCountDown() {
//        countDownTimer.cancel();
//    }

    /**
     * 资讯列表
     *
     * @return
     */
    public SingleSourceLiveData<List<TutorialAreaBean>> getNewsList() {
        return newsList;
    }

    /**
     * 主流币行情
     *
     * @return
     */
    public SingleSourceLiveData<List<MainstreamCurrencyMarketBean>> getMainstreamCurrencyMarketBean() {
        return mainstreamCurrencyMarketBean;
    }

    /**
     * 请求资讯列表
     */
    public void requestNewsList(int pageSize, int pageNum) {
        Observable<PageList<TutorialAreaBean>> pageListObservable = RxHttp.get(Api.NEWS_LIST)
                .add("pageNum", pageNum)
                .add("pageSize", pageSize)
                .asResponsePageList(TutorialAreaBean.class);
        loadingDisposable(pageListObservable, s -> {
            newsList.postValue(s.getRows());
        });
    }

    /**
     * 获取主流币行情代理地址
     */
    public void requestMainstreamCurrency(boolean whetherToUseTheCacheDirectly) {
        Observable<String> observable = RxHttp.get(Api.GET_FEI_XIAO_HAO).setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE)
                .asResponse(String.class);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                            MMKVUtils.getInstance().setFeiXiaoHaoProxy(s);
                            requestMainstreamCurrencyCache(whetherToUseTheCacheDirectly);
                        }
                        , onError -> {
                            KLog.i("获取代理失败");
                        });
    }

    /**
     * 获取主流币行情
     *
     * @param whetherToUseTheCacheDirectly
     */
    private synchronized void requestMainstreamCurrencyCache(boolean whetherToUseTheCacheDirectly) {
        CacheMode cacheMode = CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE;
        if (whetherToUseTheCacheDirectly) {
            cacheMode = CacheMode.READ_CACHE_FAILED_REQUEST_NETWORK;
        }
        Observable<List<MainstreamCurrencyMarketBean>> listObservable;
        String[] feiXiaoHaoProxy = MMKVUtils.getInstance().getFeiXiaoHaoProxy();
        if (feiXiaoHaoProxy != null && feiXiaoHaoProxy.length > 1) {
            listObservable = RxHttp.get(Api.MAINSTREAM_CURRENCY_MARKET)
                    .setOkClient(RxHttpManager.getInstance().getFeiXiaoHaoOkHttpClient())
                    .setCacheMode(cacheMode)
                    .asList(MainstreamCurrencyMarketBean.class);
        } else {
            listObservable = RxHttp.get(Api.MAINSTREAM_CURRENCY_MARKET)
                    .setCacheMode(cacheMode)
                    .asList(MainstreamCurrencyMarketBean.class);
        }
        loadingDisposable(listObservable, success -> {
            mainstreamCurrencyMarketBean.postValue(success);
            if (whetherToUseTheCacheDirectly) {
                requestMainstreamCurrencyCache(false);
            }
        }, err -> {
            if (whetherToUseTheCacheDirectly) {
                requestMainstreamCurrencyCache(false);
            }
        }, false);
    }
}
