package com.bizeng.lh.ui.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.Nullable;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseFragment;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.LineChartBean;
import com.bizeng.lh.databinding.FragmentReportCenterTime2Binding;
import com.bizeng.lh.utils.BarChartUtils;
import com.bizeng.lh.utils.DateUtils;
import com.bizeng.lh.viewmodel.ReportCenterViewModel;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.wzq.mvvmsmart.utils.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 币增报表 量化统计报表 时间折线图 自定义时间单独拿出来
 * 2021/5/17 换成柱状图
 * @author: admin wsj
 * @Date: 2021/5/9 6:13 PM
 */
public class ReportCenterCustomizeTimeFragment extends BaseFragment<FragmentReportCenterTime2Binding, ReportCenterViewModel> implements OnChartValueSelectedListener, View.OnClickListener {
    private int mType = 7;//7天，30天，99自定义
    private String mApiId = null;
    private String mUserStrategyId = null;
    private String mStartTime = null;
    private String mEndTime = null;
    private int mStartYear, mStartMonth, mStartDay;
    private int mEndYear, mEndMonth, mEndDay;
    private boolean mIsSlither = true;//true可左右滑动
    //正值 绿色
    ArrayList<BarEntry> mValues = new ArrayList<>();
    //负值 红色
    ArrayList<BarEntry> mValuesRed = new ArrayList<>();
    List<LineChartBean> mLineChartBeans = new ArrayList<>();
    DatePickerDialog mDatePickerDialog = null;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_report_center_time2;
    }

    @Override
    public void initParam() {
        super.initParam();
        if (getArguments() != null) {
            mType = getArguments().getInt(Content.TYPE);
            mApiId = getArguments().getString(Content.KEY);
            mUserStrategyId = getArguments().getString(Content.PARAMS_TITLE);
        }
        switch (mType) {
            case 7:
                mStartTime = DateUtils.getInstance().getOldDate(-7);
                mEndTime = DateUtils.getInstance().getOldDate(0);
                break;
            case 30:
                mStartTime = DateUtils.getInstance().getOldDate(-30);
                mEndTime = DateUtils.getInstance().getOldDate(0);
                break;
            case 99:
                mStartTime = DateUtils.getInstance().getOldDate(0);
                mEndTime = DateUtils.getInstance().getOldDate(0);
                break;
        }
        String[] splitStart = mStartTime.split("-");
        if (splitStart != null) {
            String startMonth;
            String startDay;
            mStartYear = Integer.parseInt(splitStart[0]);
            mStartMonth = Integer.parseInt(splitStart[1]);
            mStartDay = Integer.parseInt(splitStart[2]);
            if (mType == 99) {
                if (mStartMonth > 6) {
                    mStartMonth -= 6;
                } else {
                    mStartYear -= 1;
                    mStartMonth = mStartMonth + 6;
                }
                startMonth = get2String(mStartMonth);
                startDay = get2String(mStartDay);
                mStartTime = mStartYear + "-" + startMonth + "-" + startDay;
            }
        }
        String[] splitEnd = mEndTime.split("-");
        if (splitEnd != null) {
            mEndYear = Integer.parseInt(splitEnd[0]);
            mEndMonth = Integer.parseInt(splitEnd[1]);
            mEndDay = Integer.parseInt(splitEnd[2]);
        }
    }

    private String get2String(int i) {
        String startMonth;
        if (i < 10) {
            startMonth = "0" + i;
        } else {
            startMonth = String.valueOf(i);
        }
        return startMonth;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getIncomeStatistics().observe(this, s -> {

        });
        viewModel.getEarningsTrend().observe(this, s -> {
            mLineChartBeans = s;
            initBarChart();
            mValues.clear();
            mValuesRed.clear();
            binding.barChart.setVisibility(View.VISIBLE);
            if (s != null) {
                if (s.size() < 1) {
                    // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
//                    binding.barChart.setNoDataText("暂无数据");
//                    binding.barChart.setNoDataTextColor(Color.parseColor("#CCCCCC"));
                    showEmptyLayout(binding.barChart, "暂无数据", R.mipmap.ic_null, false, false, null);
                } else {
                    showNormalLayout(binding.barChart);
                    for (int i = 0; i < s.size(); i++) {
                        LineChartBean lineChartBean = s.get(i);
                        if (lineChartBean.money > 0) {
                            mValues.add(new BarEntry(i, lineChartBean.money));
                        } else {
                            mValuesRed.add(new BarEntry(i, lineChartBean.money));
                        }
                    }
                    BarDataSet set1;
                    BarDataSet set2;
                    BarData data = binding.barChart.getData();
                    int size = s.size();
                    //bug 会出现数据起点极端问题
//                    if (data != null &&
//                            data.getDataSetCount() > 0) {
//                        data = binding.barChart.getData();
//                        set1 = (BarDataSet) data.getDataSetByIndex(0);
//                        set1.setValues(mValues);
//                        set2 = (BarDataSet) data.getDataSetByIndex(1);
//                        set2.setValues(mValuesRed);
//                        binding.barChart.getData().notifyDataChanged();
//                        binding.barChart.notifyDataSetChanged();
//                    } else {
                    set1 = new BarDataSet(mValues, null);
                    set2 = new BarDataSet(mValuesRed, null);
//                        initLineDataSet(set1, Color.GREEN);
//                    initLineDataSet(set1, Color.parseColor("#37761F"));
                    initLineDataSet(set1, Color.parseColor("#599EF8"));
                    initLineDataSet(set2, Color.RED);
                    data = new BarData();
                    data.addDataSet(set1);
                    data.addDataSet(set2);
                    binding.barChart.setData(data);
                    binding.barChart.animateY(1000);
//                    }
                    switch (size) {
                        case 1:
                            data.setBarWidth(0.03f);
                            break;
                        case 2:
                            data.setBarWidth(0.05f);
                            break;
                        case 3:
                            data.setBarWidth(0.07f);
                            break;
                        case 4:
                            data.setBarWidth(0.1f);
                            break;
                        case 5:
                            data.setBarWidth(0.13f);
                            break;
                        case 6:
                            data.setBarWidth(0.16f);
                            break;
                        case 7:
                            data.setBarWidth(0.19f);
                            break;
                        case 8:
                            data.setBarWidth(0.22f);
                            break;
                        case 9:
                            data.setBarWidth(0.25f);
                            break;
                        case 10:
                            data.setBarWidth(0.3f);
                            break;
                        default:
                            data.setBarWidth(0.85f);
                    }
//                    if (size >= 10) {
//                        BarChartUtils.getInstance().setChartData(binding.barChart, s.size());
//                    }
                }
                binding.barChart.invalidate();
            } else {
                showEmptyLayout(binding.barChart, "暂无数据", R.mipmap.ic_null, false, false, null);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        if (!TextUtils.isEmpty(mApiId)) {
            viewModel.requestEarningsTrend(mApiId, mStartTime, mEndTime, mUserStrategyId);
            viewModel.requestIncomeStatistics(mApiId);
        } else {
            binding.barChart.setVisibility(View.VISIBLE);
            showEmptyLayout(binding.barChart, "暂无数据", R.mipmap.ic_null, false, false, null);
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (mType == 99) {
            binding.tvReportStartTime.setOnClickListener(this);
            binding.tvReportEndTime.setOnClickListener(this);
        }
        binding.tvReportStartTime.setText(mStartTime);
        binding.tvReportEndTime.setText(mEndTime);

    }

    private void initBarChart() {
        BarChartUtils.getInstance().mStartTime = mStartTime;
        BarChartUtils.getInstance().mEndTime = mEndTime;
        BarChartUtils.getInstance().initView(getContext(), this, binding.barChart,mLineChartBeans);
    }

    private void initLineDataSet(BarDataSet set1, int color) {
        set1.setColor(color);
//        set1.setFormLineWidth(20);

    }

    public static ReportCenterCustomizeTimeFragment newInstance(int type, String apiId, String userStrategyId) {

        Bundle args = new Bundle();
        args.putInt(Content.TYPE, type);
        args.putString(Content.KEY, apiId);
        args.putString(Content.PARAMS_TITLE, userStrategyId);
        ReportCenterCustomizeTimeFragment fragment = new ReportCenterCustomizeTimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_report_start_time:
                showSelectStartTime();
                break;
            case R.id.tv_report_end_time:
                showSelectEndTime();
                break;
        }
    }

    /**
     * 开始时间选择
     */
    private void showSelectStartTime() {
        mDatePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    mStartYear = year;
                    mStartMonth = monthOfYear + 1;
                    mStartDay = dayOfMonth;
                    mStartTime = year + "-" + DateUtils.getInstance().getMakeUp0(mStartMonth) + "-" + DateUtils.getInstance().getMakeUp0(dayOfMonth);
                    binding.tvReportStartTime.setText(mStartTime);
                    viewModel.requestEarningsTrend(mApiId, mStartTime, mEndTime, mUserStrategyId);
                    BarChartUtils.getInstance().mStartTime = mStartTime;
                    // 此处得到选择的时间，可以进行你想要的操作
                    KLog.d("您选择了：" + year + "年" + mStartMonth
                            + "月" + dayOfMonth + "日");
                }
                // 设置初始日期
                , mStartYear
                , mStartMonth - 1
                , mStartDay);
        DatePicker datePicker = mDatePickerDialog.getDatePicker();
        datePicker.setMaxDate(DateUtils.getInstance().str2Timestamp(mEndTime, DateUtils.YYYY_MM_DD));
        mDatePickerDialog.show();
    }


    /**
     * 结束时间选择
     */
    private void showSelectEndTime() {
        mDatePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    mEndYear = year;
                    mEndMonth = monthOfYear + 1;
                    mEndDay = dayOfMonth;
                    mEndTime = year + "-" + DateUtils.getInstance().getMakeUp0(mEndMonth) + "-" + DateUtils.getInstance().getMakeUp0(dayOfMonth);
                    binding.tvReportEndTime.setText(mEndTime);
                    viewModel.requestEarningsTrend(mApiId, mStartTime, mEndTime, mUserStrategyId);
                    BarChartUtils.getInstance().mEndTime = mEndTime;
                    // 此处得到选择的时间，可以进行你想要的操作
                    KLog.d("您选择了：" + year + "年" + mEndMonth
                            + "月" + dayOfMonth + "日");
                }
                // 设置初始日期
                , mEndYear
                , mEndMonth - 1
                , mEndDay);
        DatePicker datePicker = mDatePickerDialog.getDatePicker();
        datePicker.setMaxDate(DateUtils.getInstance().getNowTimeLong());
        datePicker.setMinDate(DateUtils.getInstance().str2Timestamp(mStartTime, DateUtils.YYYY_MM_DD));
        mDatePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDatePickerDialog != null) {
            if (mDatePickerDialog.isShowing()) {
                mDatePickerDialog.dismiss();
            }
            mDatePickerDialog = null;
        }
    }
}
