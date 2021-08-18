package com.bizeng.lh.ui.activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.CurrentPositionRVAdapter;
import com.bizeng.lh.adapter.FragmentViewPager;
import com.bizeng.lh.adapter.ReportCenterTimeSelectAdapter;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.CurrentPositionBean;
import com.bizeng.lh.bean.SelectBean;
import com.bizeng.lh.bean.UserStrategyPositionBean;
import com.bizeng.lh.databinding.ActivityStrategyStatisticsBinding;
import com.bizeng.lh.ui.fragment.ReportCenterCustomizeTimeFragment;
import com.bizeng.lh.ui.fragment.ReportCenterTimeFragment;
import com.bizeng.lh.utils.BigDecimalUtils;
import com.bizeng.lh.utils.DateUtils;
import com.bizeng.lh.utils.TextParserUtils;
import com.bizeng.lh.utils.ViewPager2Utils;
import com.bizeng.lh.viewmodel.StrategyStatisticsViewModel;
import com.bizeng.lh.widget.PieChartMarkerView;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.wzq.mvvmsmart.utils.ConvertUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 统计策略
 * @author: admin wsj
 * @Date: 2021/5/20 7:12 PM
 */

public class StrategyStatisticsActivity extends BaseActivity<ActivityStrategyStatisticsBinding, StrategyStatisticsViewModel> {
    List<Fragment> mFragments = new ArrayList<>();
    List<SelectBean<String>> mSelectBeans = new ArrayList<>();
    private String mApiTag = null;
    private String mApiId = null;
    private String mUserStrategyId = null;
    ReportCenterTimeSelectAdapter mReportCenterTimeSelectAdapter;
    CurrentPositionRVAdapter mCurrentPositionRVAdapter = null;
    int mTitleHeight = ConvertUtils.dp2px(80);
    float mTotalPositionValue = 0.0f;
    ArrayList<Integer> mColors = new ArrayList<Integer>();
    List<CurrentPositionBean> mBeans = new ArrayList<>();


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_strategy_statistics;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public String setTitleBar() {
        return "";
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    public void initParam() {
        mApiId = getIntent().getStringExtra(Content.KEY);
        mApiTag = getIntent().getStringExtra(Content.TYPE);
        mUserStrategyId = getIntent().getStringExtra(Content.PARAMS_TITLE);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            initFragment();
        } else {
            mFragments.addAll(getSupportFragmentManager().getFragments());
        }
        binding.smartRefresh.setOnRefreshListener(refreshLayout -> {
            viewModel.requestUserPolicyInformation(mUserStrategyId);
            binding.smartRefresh.finishRefresh(Content.DEFAULT_WAITING_TIMEOUT);
        });
        binding.smartRefresh.setEnableLoadMore(false);
    }

    private void initPieChart(List<UserStrategyPositionBean> list) {
        if (list == null) {
            return;
        }
        mBeans.clear();
        mTotalPositionValue = 0.0f;
        List<PieEntry> strings = new ArrayList<>();
        initColors();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            UserStrategyPositionBean bean = list.get(i);
            mTotalPositionValue += bean.getPositionUsdt().floatValue();
            if (size <= mColors.size()) {
                mBeans.add(new CurrentPositionBean(bean.symbolName, mColors.get(i)));
            } else {
                mBeans.add(new CurrentPositionBean(bean.symbolName, mColors.get(i % size)));
            }
            strings.add(new PieEntry(bean.getPositionUsdt().floatValue(), bean.symbolName, null, bean));
        }

        PieDataSet dataSet = new PieDataSet(strings, "");
        dataSet.setColors(mColors);
        PieData pieData = new PieData(dataSet);
        //true 显示数值
        pieData.setDrawValues(false);
        pieData.setValueTextSize(12f);
        Description description = new Description();
        description.setText("");
        //隐藏description
        binding.pieChart.setDescription(description);
        binding.pieChart.setDrawEntryLabels(false);
        Legend legend = binding.pieChart.getLegend();
        //实例，不能换行
        legend.setEnabled(false);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        //先设成半径为零
//        binding.pieChart.setHoleRadius(0f);
        //发现内圈还有个半透明的，再设置半透明的半径为0
//        binding.pieChart.setTransparentCircleRadius(0f);
        //显示百分比
        binding.pieChart.setUsePercentValues(false);
        if (list.size() < 1) {
            binding.pieChart.setData(null);
        } else {
            binding.pieChart.setData(pieData);
        }
        binding.pieChart.invalidate();
        //设置选中弹框
        float finalAll = mTotalPositionValue;
        PieChartMarkerView mv = new PieChartMarkerView(this, new ValueFormatter() {
            @Override
            public String getPieLabel(float value, PieEntry pieEntry) {
                if (pieEntry != null) {
                    UserStrategyPositionBean bean = (UserStrategyPositionBean) pieEntry.getData();
                    if (finalAll == 0) {
                        return bean.symbolName + ":0" + "%";
                    }
                    float v = (bean.getPositionUsdt().floatValue() / finalAll) * 100;
//                    float v = bean.getPositionUsdt().divide(new BigDecimal(finalAll)).multiply(new BigDecimal(100)).floatValue();
                    BigDecimal bigDecimal = new BigDecimal(v).setScale(2, RoundingMode.HALF_UP);
                    return bean.symbolName + ":" + bigDecimal.toString() + "%";
                }
                return String.valueOf(value);
            }
        });
        mv.setChartView(binding.pieChart);
        binding.pieChart.setMarker(mv);
        mCurrentPositionRVAdapter = new CurrentPositionRVAdapter(mBeans);
        binding.strategyRecyclerView.setAdapter(mCurrentPositionRVAdapter);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        GridLayoutManager layoutManager;
        if (mBeans.size() > 0 && mBeans.size() < 5) {
            layoutManager = new GridLayoutManager(this, mBeans.size());
        } else {
            layoutManager = new GridLayoutManager(this, 5);
        }
        binding.strategyRecyclerView.setLayoutManager(layoutManager);
//        mCurrentPositionRVAdapter.setEmptyView(R.layout.item_empty);
    }

    private void initColors() {
        if (mColors == null || mColors.size() < 1) {
            mColors.add(Color.parseColor("#2457BF"));
            mColors.add(Color.parseColor("#C26017"));
            mColors.add(Color.parseColor("#1BADBF"));
            mColors.add(Color.parseColor("#BFAF11"));
            mColors.add(Color.parseColor("#7090FF"));
            mColors.add(Color.parseColor("#AFCAFF"));
            mColors.add(Color.parseColor("#BD00E5"));
            mColors.add(Color.parseColor("#FF5A5A"));
            mColors.add(Color.parseColor("#00FFBC"));
            mColors.add(Color.parseColor("#FF2E87"));
//            mColors.add(Color.BLUE);
//            mColors.add(Color.GREEN);
//            mColors.add(Color.CYAN);
//            mColors.add(Color.MAGENTA);
//            mColors.add(Color.GRAY);
//            mColors.add(Color.YELLOW);
//            mColors.add(Color.CYAN);
//            mColors.add(Color.RED);
        }
    }

    private void initFragment() {
        mFragments.add(ReportCenterTimeFragment.newInstance(7, mApiId, mUserStrategyId));
        mFragments.add(ReportCenterTimeFragment.newInstance(30, mApiId, mUserStrategyId));
        mFragments.add(ReportCenterCustomizeTimeFragment.newInstance(99, mApiId, mUserStrategyId));
        binding.viewPager2.setAdapter(new FragmentViewPager(getSupportFragmentManager(), getLifecycle(), mFragments));
        SelectBean<String> selectBean = new SelectBean<>("近7天");
        selectBean.setSelected(true);
        SelectBean<String> selectBean2 = new SelectBean<>("近30天");
        SelectBean<String> selectBean3 = new SelectBean<>("自定义");
        mSelectBeans.add(selectBean);
        mSelectBeans.add(selectBean2);
        mSelectBeans.add(selectBean3);
        mReportCenterTimeSelectAdapter = new ReportCenterTimeSelectAdapter(mSelectBeans);
        binding.recyclerViewTime.setAdapter(mReportCenterTimeSelectAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 10);
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
                    mReportCenterTimeSelectAdapter.rotate(view, mSelectBeans.get(position));
                    break;
                default:
                    mReportCenterTimeSelectAdapter.setSelectItem(position);
                    mReportCenterTimeSelectAdapter.notifyDataSetChanged();
//                    binding.viewPager2.setCurrentItem(position);
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
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        if (mTvTitle != null) {
            mTvTitle.setText(setTitleBar());
            mTvTitle.setTextColor(getResources().getColor(com.wzq.mvvmsmart.R.color.black));
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.userPolicyInformation.observe(this, s -> {
            binding.smartRefresh.finishRefresh();
            binding.tvCurrentPositionTime.setText(DateUtils.getInstance().getNowTime());
//            binding.tvName.setText(s.strategyTitle);
            mTvTitle.setText(s.strategyTitle);
            binding.tvStartTimeValue.setText(s.getStartTime());
            binding.tvEndTimeValue.setText(s.getRunDate());
            binding.tvU.setText(String.valueOf(s.getTotal()));
            BigDecimal bigDecimal = s.getTotal().multiply(new BigDecimal(Content.U2CNY)).setScale(2, RoundingMode.HALF_UP);
            binding.tvCny.setText(String.format(getString(R.string.cny), bigDecimal));
            binding.tvTodayU.setText(String.valueOf(s.getToDay()));
            BigDecimal bigDecimalToDay = s.getToDay().multiply(new BigDecimal(Content.U2CNY)).setScale(2, RoundingMode.HALF_UP);
            binding.tvTodayCny.setText(String.format(getString(R.string.cny), bigDecimalToDay));
            if (s.positions != null) {
                initPieChart(s.positions);
                if (s.positions.size() < 1) {
                    showEmptyLayout(binding.pieChart, "暂无数据", R.mipmap.ic_null, false, false, null);
                } else {
                    showNormalLayout(binding.pieChart);
                }
            } else {
//                initPieChart(new ArrayList<>());
                showEmptyLayout(binding.pieChart, "暂无数据", R.mipmap.ic_null, false, false, null);
            }
            BigDecimal bigDecimalTotalPositionValue = new BigDecimal(mTotalPositionValue).setScale(2, RoundingMode.HALF_UP);
            TextParserUtils textParser = new TextParserUtils();
            textParser.append(bigDecimalTotalPositionValue.toString(), ConvertUtils.dp2px(16), 0, 0);
            textParser.append(" U", ConvertUtils.dp2px(12), 0, 0);
            textParser.append(" ≈ ", ConvertUtils.dp2px(12), 0);
            textParser.append(BigDecimalUtils.getInstance().getBigDecimal(bigDecimalTotalPositionValue.multiply(new BigDecimal(Content.U2CNY))).toString(), ConvertUtils.dp2px(12), 0);
            textParser.append(" CNY", ConvertUtils.dp2px(12), 0);
            textParser.parse(binding.tvTotalPositionValue);
        });
        viewModel.requestUserPolicyInformation(mUserStrategyId);
    }

}
