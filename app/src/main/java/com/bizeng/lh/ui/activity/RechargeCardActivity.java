package com.bizeng.lh.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.WalletInformationBean;
import com.bizeng.lh.databinding.ActivityRechargeCardBinding;
import com.bizeng.lh.utils.DialogUtils;
import com.bizeng.lh.utils.ScreenshotUtils;
import com.bizeng.lh.utils.qrcode.QRCodeUtils;
import com.bizeng.lh.viewmodel.RechargeCardViewModel;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.wzq.mvvmsmart.utils.ConvertUtils;
import com.wzq.mvvmsmart.utils.KLog;
import com.wzq.mvvmsmart.utils.ToastUtils;

/**
 * @Desc: 充点卡 兑换点卡
 * @author: admin wsj
 * @Date: 2021/5/12 3:42 PM
 */
public class RechargeCardActivity extends BaseActivity<ActivityRechargeCardBinding, RechargeCardViewModel> implements View.OnClickListener {
    private String mDepositAddress = "充币地址";
    private WalletInformationBean mWalletInformationBean;


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_recharge_card;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        binding.tvSaveQr.setOnClickListener(this);
        binding.tvCopyAddress.setOnClickListener(this);
        binding.btnShowTip.setOnClickListener(this);
        binding.ivDepositGuide.setOnClickListener(this);
        binding.rgName.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_erc20:
                    KLog.d("erc20");
                    binding.tvExplanation.setText(Html.fromHtml(viewModel.mStrInfo[1]));
                    getQr(mWalletInformationBean.ethAddress);
                    break;
                case R.id.rb_trc20:
                    KLog.d("trc20");
                    binding.tvExplanation.setText(Html.fromHtml(viewModel.mStrInfo[0]));
                    getQr(mWalletInformationBean.trxAddress);
                    break;
            }
        });
    }

    private void initQr() {
        getQr(mWalletInformationBean.trxAddress);
        binding.tvExplanation.setText(Html.fromHtml(viewModel.mStrInfo[0]));
    }

    private void getQr(String str) {
        int px = ConvertUtils.dp2px(118);
        Bitmap bitmap = QRCodeUtils.generateImage(str, px, px, null);
        binding.ivQr.setImageBitmap(bitmap);
        binding.tvAddressValue.setText(str);
        mDepositAddress = str;
    }

    @Override
    public String setTitleBar() {
        return getString(R.string.redeem_card);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getCardRechargeAddress().observe(this, s -> {
            mWalletInformationBean = s;
            initQr();
        });
        viewModel.tutorialArea.observe(this, s -> {
            WebViewActivity.newInstance(RechargeCardActivity.this, "", s.getUrl(), 1);
        });
        viewModel.requestCardRechargeAddress();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save_qr:
                requestCameraPermissions();
                break;
            case R.id.tv_copy_address:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", mDepositAddress);
                cm.setPrimaryClip(mClipData);
                ToastUtils.showShort(R.string.copy_successfully);
                break;
            case R.id.btn_show_tip:
                showTip();
                break;
            case R.id.iv_deposit_guide:
                viewModel.requestTutorialArea(Content.BUY_COINS_GUIDE);
                break;
        }
    }

    private void showTip() {
        DialogUtils.getInstance().showTip(this, getString(R.string.tips),
                getString(R.string.tips_content),
                getString(R.string.continue_to_pay), getString(R.string.view_records),
                null, (v1, universalDialog) -> {
//                    setResult(Activity.RESULT_OK);
                    startActivity(MyPointCardActivity.class);
                    finish();
                }).show();
    }

    /**
     * 请求相机权限
     */
    @SuppressLint("CheckResult")
    public void requestCameraPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        ScreenshotUtils.getInstance().viewSnapshot(binding.ivQr, getString(R.string.coin_recharge_point_card));
                    } else {
                        ToastUtils.showShort(R.string.permission_denied);
                    }
                });
    }
}
