package com.bizeng.lh.ui.activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.app.App;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.databinding.ActivityMainBinding;
import com.bizeng.lh.ui.fragment.HomePageFragment;
import com.bizeng.lh.ui.fragment.MineFragment;
import com.bizeng.lh.ui.fragment.NewsFragment;
import com.bizeng.lh.ui.fragment.StrategyFragment;
import com.liys.dialoglib.ScreenUtils;
import com.wzq.mvvmsmart.utils.KLog;

import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

public class MainActivity extends BaseActivity<ActivityMainBinding, BaseViewModel> {
    private List<Fragment> mFragments = new ArrayList<>();
    private int mCurrentlySelected = 0;
    NavigationController mNavigationController;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //初始化底部Button
        initBottomTab();
        //初始化Fragment
        if (savedInstanceState == null) {
            initFragment();
        } else {
            mFragments.addAll(getSupportFragmentManager().getFragments());
        }

        int heightPixels = ScreenUtils.getHeightPixels(App.getInstance());
        int statusBarHeight = ScreenUtils.getStatusBarHeight(App.getInstance());
        KLog.d("heightPixels:" + heightPixels + ";statusBarHeight:" + statusBarHeight);
    }


    private void initFragment() {
        mFragments.add(new HomePageFragment());
        mFragments.add(new StrategyFragment());
        mFragments.add(new NewsFragment());
        mFragments.add(new MineFragment());
        //默认选中第一个
        commitAllowingStateLoss(mCurrentlySelected);
    }

    private void initBottomTab() {
        //去除动画
        mNavigationController = binding.pagerBottomTab.custom()
                .addItem(newItem(R.mipmap.ic_home_page, R.mipmap.ic_home_page_checked, "首页"))
//                .addItem(newItem(R.mipmap.ic_main_strategy, R.mipmap.ic_main_strategy_checked, "策略"))
                .addItem(newItem(R.mipmap.ic_main_strategy, R.mipmap.ic_main_strategy_checked, "量化"))
                .addItem(newItem(R.mipmap.ic_main_news, R.mipmap.ic_main_news_checked, "资讯"))
                .addItem(newItem(R.mipmap.ic_main_mine, R.mipmap.ic_main_mine_checked, "我的"))
                .build();

//        mNavigationController = binding.pagerBottomTab.material()
//                .addItem(R.mipmap.ic_home_page, R.mipmap.ic_home_page_checked, "首页")//HomePage
//                .addItem(R.mipmap.ic_main_strategy, R.mipmap.ic_main_strategy_checked, "策略")//Ploy
//                .addItem(R.mipmap.ic_main_news, R.mipmap.ic_main_news_checked, "资讯")//News
//                .addItem(R.mipmap.ic_main_mine, R.mipmap.ic_main_mine_checked, "我的")//Mine
//                .setDefaultColor(ContextCompat.getColor(this, R.color.textColorVice))
//                .build();
        //底部按钮的点击事件监听
        mNavigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                commitAllowingStateLoss(mCurrentlySelected = index);
            }

            @Override
            public void onRepeat(int index) {
                mCurrentlySelected = index;
            }
        });
    }

    private void commitAllowingStateLoss(int position) {
        hideAllFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(position + "");
        if (currentFragment != null) {
            transaction.show(currentFragment);
        } else {
            currentFragment = mFragments.get(position);
            transaction.add(R.id.frameLayout, currentFragment, position + "");
        }
        transaction.commitAllowingStateLoss();
    }

    public void setSelected(int position) {
        if (mNavigationController != null && mNavigationController.getSelected() != position && mNavigationController.getItemCount() > position) {
            mNavigationController.setSelect(position);
        }
    }

    //隐藏所有Fragment
    private void hideAllFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(i + "");
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    //创建一个Item
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        NormalItemView normalItemView = new NormalItemView(this);
        normalItemView.initialize(drawable, checkedDrawable, text);
        normalItemView.setTextDefaultColor(Color.GRAY);
        normalItemView.setTextCheckedColor(ContextCompat.getColor(this, R.color.color_2f5));
        return normalItemView;
    }

}