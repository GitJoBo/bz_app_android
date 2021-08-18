package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.LimitedTimeOfferBean;
import com.bizeng.lh.http.Api;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

public class LimitedTimeOfferViewModel extends BaseViewModel {
    public LimitedTimeOfferViewModel(@NonNull Application application) {
        super(application);
    }

    //获取赠点列表
    private SingleSourceLiveData<List<LimitedTimeOfferBean>> listOfBonusPoints = new SingleSourceLiveData<>();

    public SingleSourceLiveData<List<LimitedTimeOfferBean>> getListOfBonusPoints() {
        return listOfBonusPoints;
    }

    /**
     * 获取赠点列表
     */
    public void requestListOfBonusPoints() {
        Observable<List<LimitedTimeOfferBean>> observable = RxHttp.get(Api.ADDED_POINT_LIST).asResponseList(LimitedTimeOfferBean.class);
        loadingDisposable(observable, s -> {
            listOfBonusPoints.postValue(s);
        });
    }
}
