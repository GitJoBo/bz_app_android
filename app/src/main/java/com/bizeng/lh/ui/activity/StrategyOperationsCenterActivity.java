package com.bizeng.lh.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.FragmentViewPager;
import com.bizeng.lh.app.App;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.bean.ApiBean;
import com.bizeng.lh.databinding.ActivityStrategyOperationsCenterBinding;
import com.bizeng.lh.ui.fragment.StrategyOperationsCenterFragment;
import com.bizeng.lh.utils.UIUtils;
import com.bizeng.lh.viewmodel.StrategyOperationsCenterViewModel;
import com.bizeng.lh.widget.ViewPager2Delegate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 策略中心
 * @author: admin wsj
 * @Date: 2021/7/22 2:06 下午
 */
public class StrategyOperationsCenterActivity extends BaseActivity<ActivityStrategyOperationsCenterBinding, StrategyOperationsCenterViewModel> {
    private List<Fragment> mFragments = new ArrayList<>();
    //tab选项
    private List<ApiBean> mApiBeans = new ArrayList<>();

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_strategy_operations_center;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        UIUtils.getInstance().initTabs(this, mApiBeans, binding.dslTabLayout);
        if (savedInstanceState == null) {
            mFragments.add(StrategyOperationsCenterFragment.newInstance(mApiBeans.get(0)));
            mFragments.add(StrategyOperationsCenterFragment.newInstance(mApiBeans.get(1)));
            mFragments.add(StrategyOperationsCenterFragment.newInstance(mApiBeans.get(2)));
        } else {
            mFragments.addAll(getSupportFragmentManager().getFragments());
        }
        binding.viewPager2.setAdapter(new FragmentViewPager(getSupportFragmentManager(), getLifecycle(), mFragments));
        ViewPager2Delegate.Companion.install(binding.viewPager2, binding.dslTabLayout);
    }


    @Override
    public String setTitleBar() {
        return App.getInstance().getString(R.string.dialog_strategy_operations_center);
    }
}
