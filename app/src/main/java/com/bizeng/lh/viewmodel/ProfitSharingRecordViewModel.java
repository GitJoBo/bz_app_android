package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.PageList;
import com.bizeng.lh.bean.ProfitSharingRecordBean;
import com.bizeng.lh.bean.SelectBean;
import com.bizeng.lh.http.Api;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;
import rxhttp.RxHttpNoBodyParam;

public class ProfitSharingRecordViewModel extends BaseViewModel {
    public ProfitSharingRecordViewModel(@NonNull Application application) {
        super(application);
    }

    private List<SelectBean<String>> mSelectBeans = new ArrayList<>();
    /**
     * 返利记录列表
     */
    public SingleSourceLiveData<List<ProfitSharingRecordBean>> profitSharingRecord = new SingleSourceLiveData<>();

    /**
     * 获取选项数据
     *
     * @return
     */
    public List<SelectBean<String>> getSelected() {
        if (mSelectBeans.size() < 1) {
            SelectBean<String> all = new SelectBean<>("全部");
            all.setExtra(null);
            SelectBean<String> recharge = new SelectBean<>("预估返利");
            recharge.setExtra("0");
            SelectBean<String> presentation = new SelectBean<>("到账返利");
            presentation.setExtra("1");
            all.setSelected(true);
            mSelectBeans.add(all);
            mSelectBeans.add(recharge);
            mSelectBeans.add(presentation);
        }
        return mSelectBeans;
    }

    public void requestProfitSharingRecord(int pageNum, int pageSize, int profitType) {
        RxHttpNoBodyParam add = RxHttp.get(Api.USER_PROFIT_LIST)
                .add("pageNum", pageNum)
                .add("pageSize", pageSize);
        if (profitType != -1) {
            add.add("profitType", profitType);
        }
        Observable<PageList<ProfitSharingRecordBean>> observable = add.asResponsePageList(ProfitSharingRecordBean.class);
        loadingDisposable(observable, s -> {
            profitSharingRecord.postValue(s.getRows());
        });
    }
}
