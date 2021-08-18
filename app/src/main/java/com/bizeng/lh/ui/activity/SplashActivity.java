package com.bizeng.lh.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.databinding.ActivitySplashBinding;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, BaseViewModel> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersive();
        // 处理小米手机按 home 键重新进入会重新打开初始化的页面
        if (!this.isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
    }

    @Override
    public void initImmersive() {
        UltimateBarX.with(this)                   // 在当前 Activity/Fragment 生效
                .transparent()
                .fitWindow(false)                      // 是否侵入状态栏 （true: 不侵入）
//                .color(Color.WHITE)                   // 状态栏颜色（色值）
//                .colorRes(R.color.deepSkyBlue)        // 状态栏颜色（资源 id）
//                .drawableRes(R.drawable.bg_gradient)  // 状态栏背景（drawable 资源）
                .light(false)                         // light 模式（true: 字体变灰）
                .applyStatusBar();                   // 应用到状态栏
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        startActivity(MainActivity.class);
        finish();
    }
}
