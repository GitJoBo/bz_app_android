package com.bizeng.lh.base;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.bizeng.lh.R;
import com.liys.dialoglib.UniversalDialog;
import com.wzq.mvvmsmart.base.BaseFragmentMVVM;
import com.wzq.mvvmsmart.utils.KLog;
import com.wzq.mvvmsmart.utils.resource.Status;

import org.greenrobot.eventbus.EventBus;

/**
 * @Desc:
 * @author: admin wsj
 * @Date: 2021/4/20 1:04 PM
 */
public abstract class BaseFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragmentMVVM<V, VM> {
    public final String TAG = getClass().getSimpleName();
    private UniversalDialog mLoadingDialog;
    //记录等待框次数，用于关闭等待时判断
    private volatile int mLoadingNum = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isHasEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLoadingNum = -99;
        closeLoading();
        mLoadingDialog = null;
        if (isHasEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getStateLiveData().observe(this, resource -> {
            if (resource.status == Status.LOADING) {
                showLoading();
            } else if (resource.status == Status.SUCCESS || resource.status == Status.ERROR) {
                closeLoading();
            }
        });
    }

    public UniversalDialog getLoadingDialog() {
        return mLoadingDialog;
    }

    /**
     * loading 动画
     */
    public void showLoading() {
        mLoadingNum++;
        KLog.d(KLog.COMMON, "showLoading：mLoadingNum=" + mLoadingNum);
        if (mLoadingDialog == null) {
            mLoadingDialog = UniversalDialog.newInstance(getContext(), R.layout.dialog_loading2, R.style.MyDialogStyle);
            mLoadingDialog.setCancelable(false);
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    /**
     * 关闭loading 条件是mLoadingNum小于等于0，即调用几次showLoading
     */
    public void closeLoading() {
        mLoadingNum--;
        KLog.d(KLog.COMMON, "closeLoading：mLoadingNum=" + mLoadingNum);
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            if (mLoadingNum <= 0) {
                mLoadingDialog.dismiss();
            }
        }
    }

    @Override
    public void onDestroy() {
        mLoadingNum = -99;
        closeLoading();
        if (isHasEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    public boolean isHasEventBus() {
        return false;
    }
}

