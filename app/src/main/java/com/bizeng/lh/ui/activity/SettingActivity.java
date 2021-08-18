package com.bizeng.lh.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.event.LoginEvent;
import com.bizeng.lh.databinding.ActivitySettingBinding;
import com.bizeng.lh.utils.DialogUtils;
import com.bizeng.lh.utils.MMKVUtils;
import com.bizeng.lh.utils.SysUtils;
import com.bizeng.lh.viewmodel.SettingViewModel;
import com.liys.dialoglib.UniversalDialog;
import com.tencent.bugly.beta.Beta;
import com.wzq.mvvmsmart.utils.APKVersionCodeUtils;
import com.wzq.mvvmsmart.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import rxhttp.RxHttp;

/**
 * @Desc: 设置
 * @author: admin wsj
 * @Date: 2021/4/30 9:20 AM
 */
public class SettingActivity extends BaseActivity<ActivitySettingBinding, SettingViewModel> implements View.OnClickListener {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_setting;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public String setTitleBar() {
        return "设置";
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        binding.tvLogout.setOnClickListener(this);
        binding.sivSettingSoftwareUpgrade.setOnClickListener(this);
        binding.sivSettingDisclaimer.setOnClickListener(this);
        binding.sivSettingUserAgreement.setOnClickListener(this);
        binding.sivSettingPrivacyPolicy.setOnClickListener(this);
        String v = "当前版本：v " + APKVersionCodeUtils.getInstance().getVerName(this) + "  ";
        binding.sivSettingSoftwareUpgrade.setValue(v);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.appBean.observe(this, s -> {
            if (s == null) {
                ToastUtils.showShort("已是最新版");
                return;
            }
            if (APKVersionCodeUtils.getInstance().getVersionCode(this) < s.getVersion()) {
                if (TextUtils.isEmpty(s.url)) {
                    ToastUtils.showShort("下载地址为空");
                    return;
                }
                up(s.url);
            } else {
                ToastUtils.showShort("已是最新版");
            }
        });

        viewModel.tutorialArea.observe(this, s -> {
            WebViewActivity.newInstance(this, "", s.getUrl(), 1);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_logout:
                if (MMKVUtils.getInstance().isLogin()) {
                    DialogUtils.getInstance().showTip(this, R.string.logout, R.string.are_you_sure_to_log_out,
                            null, (v1, universalDialog) -> {
                                MMKVUtils.getInstance().clearUserAndToken();
                                EventBus.getDefault().post(new LoginEvent());
                                ToastUtils.showShort("成功退出");
                                universalDialog.dismiss();
                                finish();
                            });
                } else {
                    ToastUtils.showShort(R.string.not_login);
                }

                break;
            case R.id.siv_setting_software_upgrade:
//                ToastUtils.showShort("已最新版");
//                up();
//                viewModel.requestAppBean();

                /***** 检查更新 *****/
                Beta.checkUpgrade();
                break;
            case R.id.siv_setting_disclaimer:
                viewModel.requestTutorialArea(Content.DISCLAIMER);
                break;
            case R.id.siv_setting_user_agreement:
                viewModel.requestTutorialArea(Content.USER_AGREEMENT);
                break;
            case R.id.siv_setting_privacy_policy:
                viewModel.requestTutorialArea(Content.PRIVACY_POLICY);
                break;
        }
    }

    private void up(String url) {
        String destPath = getExternalCacheDir() + "/" + "bz.apk";
        UniversalDialog dialog = UniversalDialog.newInstance(this, R.layout.commom_dialog_download);
        TextView dialog_tv_title = dialog.getView(R.id.dialog_tv_title);
        TextView tv_progress = dialog.getView(R.id.tv_progress);
        ProgressBar dialog_progress_bar = dialog.getView(R.id.dialog_progress_bar);
        Button dialog_btn_negative = dialog.getView(R.id.dialog_btn_negative);
        Button dialog_btn_positive = dialog.getView(R.id.dialog_btn_positive);
        dialog_tv_title.setText("版本更新");
        dialog_tv_title.setVisibility(View.VISIBLE);
        dialog_btn_negative.setOnClickListener(v12 -> dialog.dismiss());
        dialog_btn_positive.setOnClickListener(v12 -> {
            if ("下载".equals(dialog_btn_positive.getText())) {
                dialog_btn_positive.setText("下载中");
                dialog_btn_positive.setEnabled(false);
                dialog_btn_positive.setClickable(false);
                long length = new File(destPath).length();
                //断点下载
//                        RxHttp.get("https://www.pgyer.com/dK2R")
//                                .asAppendDownload(destPath, AndroidSchedulers.mainThread(), progress -> { //
//                                    //下载进度回调,0-100，仅在进度有更新时才会回调
//                                    int currentProgress = progress.getProgress(); //当前进度 0-100
//                                    long currentSize = progress.getCurrentSize(); //当前已下载的字节大小
//                                    long totalSize = progress.getTotalSize();     //要下载的总字节大小
//                                    dialog_progress_bar.setProgress(currentProgress);
//                                    tv_progress.setText(String.format("%d/%d", currentSize, totalSize));
//                                }) //指定回调(进度/成功/失败)线程,不指定,默认在请求所在线程回调
//                                .subscribe(s -> { //s为String类型
//                                    //下载成功，处理相关逻辑
//                                }, throwable -> {
//                                    //下载失败，处理相关逻辑
//                                });
                //下载
                RxHttp.get(url)
                        .asDownload(destPath, AndroidSchedulers.mainThread(), progress -> {
                            //下载进度回调,0-100，仅在进度有更新时才会回调，最多回调101次，最后一次回调文件存储路径
                            int currentProgress = progress.getProgress(); //当前进度 0-100
                            long currentSize = progress.getCurrentSize(); //当前已下载的字节大小
                            long totalSize = progress.getTotalSize();     //要下载的总字节大小
                            dialog_progress_bar.setProgress(currentProgress);
                            tv_progress.setText(String.format("%d/%d", currentSize, totalSize));
                            dialog_btn_positive.setText("下载中" + currentProgress + "%");
                        }) //指定回调(进度/成功/失败)线程,不指定,默认在请求所在线程回调
                        .subscribe(s -> {                  //s为String类型，这里为文件存储路径
                            //下载完成
                            dialog_btn_positive.setText("安装");
                            dialog_btn_positive.setEnabled(true);
                            dialog_btn_positive.setClickable(true);
                        }, throwable -> {
                            //下载失败
                            ToastUtils.showShort("下载失败");
                        });
            } else if ("安装".equals(dialog_btn_positive.getText())) {
                SysUtils.getInstance().isAPK(destPath);
            }
        });
        dialog.show();
    }
}
