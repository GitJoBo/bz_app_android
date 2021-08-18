package com.bizeng.lh.base;

import android.os.Bundle;

import androidx.databinding.ViewDataBinding;

import com.bizeng.lh.R;
import com.liys.dialoglib.UniversalDialog;
import com.wzq.mvvmsmart.base.BaseActivityMVVM;
import com.wzq.mvvmsmart.utils.KLog;
import com.wzq.mvvmsmart.utils.resource.Status;

import org.greenrobot.eventbus.EventBus;


/**
 * @Desc:
 * @author: admin wsj
 * @Date: 2021/4/20 10:14 AM
 */
public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivityMVVM<V, VM> {
    public final String TAG = getClass().getSimpleName();
    private UniversalDialog mLoadingDialog;
    //记录等待框次数，用于关闭等待时判断
    private volatile int mLoadingNum = 0;
//    private boolean mHasEventBus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isHasEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getStateLiveData().observe(this, resource -> {
            if (resource.status == Status.LOADING) {
                BaseActivity.this.showLoading();
            } else if (resource.status == Status.SUCCESS || resource.status == Status.ERROR) {
                BaseActivity.this.closeLoading();
            }
        });
//        requestCameraPermissions();
    }

    public UniversalDialog getLoadingDialog() {
        return mLoadingDialog;
    }

    public void showLoading() {
        mLoadingNum++;
        KLog.d(KLog.COMMON, "showLoading：mLoadingNum=" + mLoadingNum);
        if (mLoadingDialog == null) {
            mLoadingDialog = UniversalDialog.newInstance(this, R.layout.dialog_loading2, R.style.MyDialogStyle);
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

//    /**
//     * 请求相机权限
//     */
//    @SuppressLint("CheckResult")
//    public void requestCameraPermissions() {
//        RxPermissions rxPermissions = new RxPermissions(this);
//        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .subscribe(aBoolean -> {
//                    if (aBoolean) {
//
//                    } else {
//                        requestCameraPermissions();
//                    }
//                });
//    }

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
