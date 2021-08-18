package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.PageList;
import com.bizeng.lh.bean.SelectBean;
import com.bizeng.lh.bean.WithdrawalRecordBean;
import com.bizeng.lh.http.Api;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;
import rxhttp.RxHttpNoBodyParam;

public class WithdrawalRecordViewModel extends BaseViewModel {
    public WithdrawalRecordViewModel(@NonNull Application application) {
        super(application);
    }

    private List<SelectBean<String>> mSelectBeans = new ArrayList<>();
    public SingleSourceLiveData<List<WithdrawalRecordBean>> withdrawalRecordList = new SingleSourceLiveData<>();

    /**
     * 获取选项数据
     *
     * @return
     */
    public List<SelectBean<String>> getSelected() {
        if (mSelectBeans.size() < 1) {
            SelectBean<String> all = new SelectBean<>("全部");
            all.setExtra(null);
            SelectBean<String> recharge = new SelectBean<>("提币");
            recharge.setExtra("0");
            SelectBean<String> presentation = new SelectBean<>("兑换点卡");
            presentation.setExtra("1");
            all.setSelected(true);
            mSelectBeans.add(all);
            mSelectBeans.add(recharge);
            mSelectBeans.add(presentation);
        }
        return mSelectBeans;
    }

    /**
     * 获取兑换记录列表
     */
    public void requestWithdrawalRecordList(int pageNum, int pageSize, int withdrawalType) {
        RxHttpNoBodyParam add = RxHttp.get(Api.WITHDRAWAL_RECORD_LIST)
                .add("pageNum", pageNum)
                .add("pageSize", pageSize);
        if (withdrawalType != -1) {
            add.add("withdrawalType", withdrawalType);
        }
        Observable<PageList<WithdrawalRecordBean>> observable = add.asResponsePageList(WithdrawalRecordBean.class);
        loadingDisposable(observable, s -> {
            withdrawalRecordList.postValue(s.getRows());
        });
    }


}
