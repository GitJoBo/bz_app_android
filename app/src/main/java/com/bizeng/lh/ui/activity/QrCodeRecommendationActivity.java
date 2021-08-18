package com.bizeng.lh.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.databinding.ActivityQrCodeRecommendationBinding;
import com.bizeng.lh.utils.MMKVUtils;
import com.bizeng.lh.utils.ScreenshotUtils;
import com.bizeng.lh.utils.qrcode.QRCodeConstant;
import com.bizeng.lh.utils.qrcode.QRCodeUtils;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.wzq.mvvmsmart.utils.ConvertUtils;
import com.wzq.mvvmsmart.utils.ToastUtils;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

/**
 * @Desc: 二维码推荐 邀请
 * @author: admin wsj
 * @Date: 2021/4/30 1:18 PM
 */
public class QrCodeRecommendationActivity extends BaseActivity<ActivityQrCodeRecommendationBinding, BaseViewModel> implements View.OnClickListener {
    private String mInvitationCode = null;
    private String mQrCodeContent = null;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_qr_code_recommendation;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initTitle();
        mInvitationCode = MMKVUtils.getInstance().getInvitationCode();
        mQrCodeContent = QRCodeConstant.QR_SHARE + "?code=" + mInvitationCode + "&tel=" + MMKVUtils.getInstance().getUserPhoneLastFour();
        int px = ConvertUtils.dp2px(118);
        Bitmap bitmap = QRCodeUtils.generateImage(mQrCodeContent, px, px, null);
        binding.ivQr.setImageBitmap(bitmap);
        binding.tvCopyInvitationCode.setOnClickListener(this);
        binding.tvDownloadPoster.setOnClickListener(this);
        binding.tvShare.setOnClickListener(this);
        binding.tvInvitationCode.setText(String.format("邀请码：%s", mInvitationCode));
    }

    @Override
    public String setTitleBar() {
        return "二维码推荐";
    }

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    public void initImmersive() {
        UltimateBarX.with(this)                   // 在当前 Activity/Fragment 生效
//                .transparent()
                .fitWindow(false)                      // 是否侵入状态栏 （true: 不侵入）
//                .color(Color.WHITE)                   // 状态栏颜色（色值）
//                .colorRes(R.color.deepSkyBlue)        // 状态栏颜色（资源 id）
//                .drawableRes(R.drawable.bg_gradient)  // 状态栏背景（drawable 资源）
                .light(false)                         // light 模式（true: 字体变灰）
                .applyStatusBar();                   // 应用到状态栏
    }

    @Override
    protected void initTitle() {
        mToolbar = findViewById(com.wzq.mvvmsmart.R.id.main_bar);
        mTvTitle = findViewById(com.wzq.mvvmsmart.R.id.tv_title);
        UltimateBarX.addStatusBarTopPadding(mToolbar);
        if (mToolbar != null) {
            mToolbar.setNavigationOnClickListener(v -> finish());
        }
        if (mTvTitle != null) {
            mTvTitle.setText(setTitleBar());
            mTvTitle.setTextColor(getResources().getColor(com.wzq.mvvmsmart.R.color.white));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_copy_invitation_code:
//                String str = "我在币增AI平台，低风险量化交易策略等你来用，点击链接进入：" + mQrCodeContent + " 邀请码:" + mInvitationCode;
//                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData mClipData = ClipData.newPlainText("Label", str);
//                cm.setPrimaryClip(mClipData);
//                ToastUtils.showShort("复制成功");
                String strShare = "我在币增AI平台，低风险量化交易策略等你来用，点击链接进入：" + mQrCodeContent + " 邀请码:" + mInvitationCode;
                new Share2.Builder(this)
                        .setContentType(ShareContentType.TEXT)
                        .setTextContent(strShare)
                        .forcedUseSystemChooser(true)
                        .setTitle("币增分享")
                        // .forcedUseSystemChooser(false)
                        .build()
                        .shareBySystem();
                break;
            case R.id.tv_download_poster:
//                binding.llBottom.setVisibility(View.GONE);
//                ScreenshotUtils.getInstance().screenshot(this);
//                binding.llBottom.setVisibility(View.VISIBLE);
//                requestCameraPermissions();
                break;
            case R.id.tv_share:
                requestCameraPermissions();
                break;
        }
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
                        Uri uri = ScreenshotUtils.getInstance().viewSnapshot(binding.clScreenshot, "币增海报");
                        if (uri == null) {
                            ToastUtils.showShort("分享失败");
                            return;
                        }
                        new Share2.Builder(this)
                                .setContentType(ShareContentType.IMAGE)
                                .setShareFileUri(uri)
                                .forcedUseSystemChooser(true)
                                //.setShareToComponent("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI")
                                .setTitle("币增分享")
                                .build()
                                .shareBySystem();
                    } else {
                        ToastUtils.showShort("没有权限，操作失败");
                    }
                });
    }

}
