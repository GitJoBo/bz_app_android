package com.bizeng.lh.ui.fragment;

import static androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.FragmentViewPager;
import com.bizeng.lh.adapter.ReportCenterAdapter;
import com.bizeng.lh.adapter.ReportCenterTimeSelectAdapter;
import com.bizeng.lh.base.BaseFragment;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.ApiBean;
import com.bizeng.lh.bean.SelectBean;
import com.bizeng.lh.bean.StrategyProfitModelListBean;
import com.bizeng.lh.databinding.FragmentReportCenterBinding;
import com.bizeng.lh.ui.activity.StrategyStatisticsActivity;
import com.bizeng.lh.utils.MMKVUtils;
import com.bizeng.lh.utils.ViewPager2Utils;
import com.bizeng.lh.viewmodel.ReportCenterViewModel;
import com.wzq.mvvmsmart.utils.ToastUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 币增报表 量化统计报表
 * @author: admin wsj
 * @Date: 2021/5/9 6:13 PM
 */
public class ReportCenterFragment extends BaseFragment<FragmentReportCenterBinding, ReportCenterViewModel> {
    private String mApiTag = null;
    private String mApiId = null;
    List<Fragment> mFragments = new ArrayList<>();
    List<StrategyProfitModelListBean> mStrategyProfitModelListBeans = new ArrayList<>();
    List<SelectBean<String>> mSelectBeans = new ArrayList<>();
    ReportCenterAdapter mReportCenterAdapter;
    ReportCenterTimeSelectAdapter mReportCenterTimeSelectAdapter;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_report_center;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getIncomeStatistics().observe(this, s -> {
            binding.smartRefresh.finishRefresh();
            if (s != null) {
                binding.tvAccumulativeU.setText(String.valueOf(s.getTotal()));
                BigDecimal bigDecimal = s.getTotal().multiply(new BigDecimal(Content.U2CNY)).setScale(2, RoundingMode.HALF_UP);
                binding.tvAccumulativeCny.setText(String.format(getString(R.string.cny), bigDecimal.toString()));
                binding.tvTodayU.setText(String.valueOf(s.getToday()));
                BigDecimal bigDecimalToDay = s.getToday().multiply(new BigDecimal(Content.U2CNY)).setScale(2, RoundingMode.HALF_UP);
                binding.tvTodayCny.setText(String.format(getString(R.string.cny), bigDecimalToDay.toString()));

                if (s.strategyProfitModelList != null) {
                    mStrategyProfitModelListBeans.clear();
                    mStrategyProfitModelListBeans.addAll(s.strategyProfitModelList);
                    mReportCenterAdapter.setEmptyView(R.layout.item_empty_cwhite_r6);
                    mReportCenterAdapter.notifyDataSetChanged();
                }
            }
        });
        refresh();
    }

    private void refresh() {
        if (!TextUtils.isEmpty(mApiId)) {
            viewModel.requestIncomeStatistics(mApiId);
        } else {
            mReportCenterAdapter.setEmptyView(R.layout.item_empty_cwhite_r6);
        }
    }

    @Override
    public void initParam() {
        super.initParam();
        if (getArguments() != null) {
            mApiTag = getArguments().getString(Content.KEY);
        }
        if (TextUtils.isEmpty(mApiTag)) {
            ToastUtils.showShort("数据有误");
            return;
        }
        List<ApiBean> apisBe = MMKVUtils.getInstance().getApisBe();
        if (apisBe != null) {
            for (int i = 0; i < apisBe.size(); i++) {
                ApiBean apiBean = apisBe.get(i);
                if (mApiTag.equals(apiBean.apiTag)) {
                    mApiId = apiBean.apiId;
                }
            }
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mFragments.add(ReportCenterTimeFragment.newInstance(7, mApiId, null));
            mFragments.add(ReportCenterTimeFragment.newInstance(30, mApiId, null));
            mFragments.add(ReportCenterCustomizeTimeFragment.newInstance(99, mApiId, null));
        } else {
            mFragments.addAll(getChildFragmentManager().getFragments());
        }
        binding.viewPager2.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT_DEFAULT);
        binding.viewPager2.setAdapter(new FragmentViewPager(getChildFragmentManager(), getLifecycle(), mFragments));
        SelectBean<String> selectBean = new SelectBean<>("近7天");
        selectBean.setSelected(true);
        SelectBean<String> selectBean2 = new SelectBean<>("近30天");
        SelectBean<String> selectBean3 = new SelectBean<>("自定义");
        mSelectBeans.add(selectBean);
        mSelectBeans.add(selectBean2);
        mSelectBeans.add(selectBean3);
        mReportCenterTimeSelectAdapter = new ReportCenterTimeSelectAdapter(mSelectBeans);
        binding.recyclerViewTime.setAdapter(mReportCenterTimeSelectAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 10);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                } else if (position == 2) {
                    return 3;
                } else {
                    return 4;
                }
            }
        });
        binding.recyclerViewTime.setLayoutManager(gridLayoutManager);
        mReportCenterTimeSelectAdapter.addChildClickViewIds(R.id.ll_item, R.id.tv_title, R.id.iv_drop_down);
        mReportCenterTimeSelectAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.iv_drop_down:
//                    if (mReportCenterTimeSelectAdapter.isShowSelectTime) {
                    mReportCenterTimeSelectAdapter.rotate(view, mSelectBeans.get(position));
//                    }
                    break;
                default:
                    mReportCenterTimeSelectAdapter.setSelectItem(position);
                    mReportCenterTimeSelectAdapter.notifyDataSetChanged();
                    binding.viewPager2.setCurrentItem(position, ViewPager2Utils.getInstance().isAnimation(binding.viewPager2.getCurrentItem(), position));
                    break;
            }
        });
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.recyclerViewTime.smoothScrollToPosition(position);
                mReportCenterTimeSelectAdapter.setSelectItem(position);
                mReportCenterTimeSelectAdapter.notifyDataSetChanged();
            }
        });

        binding.recyclerviewReportCenter.setLayoutManager(new LinearLayoutManager(getContext()));
        mReportCenterAdapter = new ReportCenterAdapter(mStrategyProfitModelListBeans);
        mReportCenterAdapter.addChildClickViewIds(R.id.iv_report_bg);
        mReportCenterAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getContext(), StrategyStatisticsActivity.class);
            intent.putExtra(Content.KEY, mApiId);
            intent.putExtra(Content.TYPE, mApiTag);
            intent.putExtra(Content.PARAMS_TITLE, mStrategyProfitModelListBeans.get(position).userStrategyId);
            startActivity(intent);
        });
        binding.recyclerviewReportCenter.setAdapter(mReportCenterAdapter);
        binding.smartRefresh.setOnRefreshListener(refreshLayout -> {
            binding.smartRefresh.finishRefresh(Content.DEFAULT_WAITING_TIMEOUT);
            refresh();
        });
        binding.smartRefresh.setEnableLoadMore(false);
    }

    public static ReportCenterFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(Content.KEY, type);
        ReportCenterFragment fragment = new ReportCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
