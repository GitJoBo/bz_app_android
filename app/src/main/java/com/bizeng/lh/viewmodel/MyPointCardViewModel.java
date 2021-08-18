package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.PageList;
import com.bizeng.lh.bean.PointCardRecordBean;
import com.bizeng.lh.bean.SelectBean;
import com.bizeng.lh.bean.WalletInformationBean;
import com.bizeng.lh.http.Api;
import com.bizeng.lh.model.AppModel;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;
import com.wzq.mvvmsmart.utils.KLog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;
import rxhttp.RxHttpNoBodyParam;

/**
 * @Desc: 我的点卡
 * @author: admin wsj
 * @Date: 2021/5/12 2:44 PM
 */
public class MyPointCardViewModel extends BaseViewModel {
    public int mPageNum = Content.PAGE_NUM;
    public int mSmartType = 0;//0刷新，1加载更多
    public int mRecordType = -1;

    AppModel mAppModel;
    private List<SelectBean<String>> mSelectBeans = new ArrayList<>();

    //记录
    private SingleSourceLiveData<List<PointCardRecordBean>> mPointCardLiveData = new SingleSourceLiveData<>();
    //获取钱包信息
    private SingleSourceLiveData<WalletInformationBean> mWalletInformation = new SingleSourceLiveData<>();
    //获取收支明细列表
    public SingleSourceLiveData<PageList<PointCardRecordBean>> listOfIncomeAndExpenditureDetails = new SingleSourceLiveData<>();

    public MyPointCardViewModel(@NonNull Application application) {
        super(application);
        mAppModel = new AppModel();
    }

    public LiveData<List<PointCardRecordBean>> getPointCardLiveData() {
        return mPointCardLiveData;
    }

    public LiveData<WalletInformationBean> getWalletInformation() {
        return mWalletInformation;
    }

    /**
     * 获取点卡记录
     */
    public void requestPointCards() {

    }

    /**
     * 获取钱包信息
     */
    public void requestWalletInformation() {
        Observable<WalletInformationBean> observable = mAppModel.requestUserWallet();
        loadingDisposable(observable, s -> {
            mWalletInformation.postValue(s);
        });
    }

    /**
     * 获取收支明细列表
     *
     * @return
     */
    public void requestListOfIncomeAndExpenditureDetails(int pageNum, int pageSize, int recordType) {
        KLog.d("aaaaaa requestListOfIncomeAndExpenditureDetails:pageNum" + pageNum);
        RxHttpNoBodyParam add = RxHttp.get(Api.LIST_OF_INCOME_AND_EXPENDITURE_DETAILS)
                .add("pageNum", pageNum)
                .add("pageSize", pageSize);
        if (recordType != -1) {
            add.add("recordType", recordType);
        }
        Observable<PageList<PointCardRecordBean>> observable = add.asResponsePageList(PointCardRecordBean.class);
        loadingDisposable(observable, s -> {
//            listOfIncomeAndExpenditureDetails.postValue(s);
            listOfIncomeAndExpenditureDetails.setValue(s);
        });
    }

    public List<SelectBean<String>> getSelected() {
        if (mSelectBeans.size() < 1) {
            SelectBean<String> all = new SelectBean<>("全部");
            all.setExtra(null);
            SelectBean<String> recharge = new SelectBean<>("兑点");
            recharge.setExtra("0");
            SelectBean<String> presentation = new SelectBean<>("赠送");
            presentation.setExtra("1");
            SelectBean<String> deduction = new SelectBean<>("扣点");
            deduction.setExtra("2");
            all.setSelected(true);
            mSelectBeans.add(all);
            mSelectBeans.add(recharge);
            mSelectBeans.add(presentation);
            mSelectBeans.add(deduction);
        }
        return mSelectBeans;
    }
}
