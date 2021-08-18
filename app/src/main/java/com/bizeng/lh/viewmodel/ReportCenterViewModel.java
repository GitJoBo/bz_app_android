package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.LineChartBean;
import com.bizeng.lh.bean.ReportIncomeStatistics;
import com.bizeng.lh.http.Api;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

public class ReportCenterViewModel extends BaseViewModel {
    public ReportCenterViewModel(@NonNull Application application) {
        super(application);
    }

    //收益统计
    private SingleSourceLiveData<ReportIncomeStatistics> incomeStatistics = new SingleSourceLiveData<>();

    //收益趋势
    private SingleSourceLiveData<List<LineChartBean>> earningsTrend = new SingleSourceLiveData<>();

    /**
     * 收益趋势
     *
     * @return
     */
    public LiveData<List<LineChartBean>> getEarningsTrend() {
        return earningsTrend;
    }

    /**
     * 收益统计
     *
     * @return
     */
    public LiveData<ReportIncomeStatistics> getIncomeStatistics() {
        return incomeStatistics;
    }

    public void requestIncomeStatistics(String apiId) {
        Observable<ReportIncomeStatistics> observable = RxHttp.get(Api.INCOME_STATISTICS + apiId).asResponse(ReportIncomeStatistics.class);
        loadingDisposable(observable, s -> {
            incomeStatistics.postValue(s);
        });
    }

    public void requestEarningsTrend(String apiId, String startTime, String endTime,String userStrategyId) {
        Observable<List<LineChartBean>> observable = RxHttp.get(Api.EARNINGS_TREND)
                .add("apiId", apiId)
                .add("beginCreateTime", startTime)
                .add("endCreateTime", endTime)
                .add("userStrategyId", userStrategyId)
                .asResponseList(LineChartBean.class);
        loadingDisposable(observable, s -> {
            earningsTrend.postValue(s);
        });
    }
}
