package com.bizeng.lh.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.FragmentViewPager;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.bean.ApiBean;
import com.bizeng.lh.databinding.ActivityReportCenterBinding;
import com.bizeng.lh.ui.fragment.ReportCenterFragment;
import com.bizeng.lh.utils.UIUtils;
import com.bizeng.lh.viewmodel.ReportCenterViewModel;
import com.bizeng.lh.widget.ViewPager2Delegate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 币增报表 量化统计报表
 * @author: admin wsj
 * @Date: 2021/4/30 1:18 PM
 */
public class ReportCenterActivity extends BaseActivity<ActivityReportCenterBinding, ReportCenterViewModel> {
    private List<Fragment> mFragments = new ArrayList<>();
    //tab选项
    private List<ApiBean> mApiBeans = new ArrayList<>();

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_report_center;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public String setTitleBar() {
        return "量化统计报表";
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        UIUtils.getInstance().initTabs(this, mApiBeans, binding.dslTabLayout);
        if (savedInstanceState == null){
            mFragments.add(ReportCenterFragment.newInstance(mApiBeans.get(0).apiTag));
            mFragments.add(ReportCenterFragment.newInstance(mApiBeans.get(1).apiTag));
            mFragments.add(ReportCenterFragment.newInstance(mApiBeans.get(2).apiTag));
        }else {
            mFragments.addAll(getSupportFragmentManager().getFragments());
        }
        binding.viewPager2.setAdapter(new FragmentViewPager(getSupportFragmentManager(), getLifecycle(), mFragments));
        ViewPager2Delegate.Companion.install(binding.viewPager2, binding.dslTabLayout);
    }


}
