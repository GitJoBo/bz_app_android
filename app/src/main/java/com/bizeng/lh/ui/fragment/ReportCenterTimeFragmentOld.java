package com.bizeng.lh.ui.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseFragment;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.LineChartBean;
import com.bizeng.lh.databinding.FragmentReportCenterTimeOldBinding;
import com.bizeng.lh.utils.DateUtils;
import com.bizeng.lh.viewmodel.ReportCenterViewModel;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.wzq.mvvmsmart.utils.KLog;

import java.util.ArrayList;

/**
 * @Desc: 币增报表 量化统计报表 时间折线图
 * @author: admin wsj
 * @Date: 2021/5/9 6:13 PM
 */
@Deprecated
public class ReportCenterTimeFragmentOld extends BaseFragment<FragmentReportCenterTimeOldBinding, ReportCenterViewModel> implements OnChartValueSelectedListener, View.OnClickListener {
    private int mType = 7;//7天，30天，99自定义
    private String mApiId = null;
    private String mStartTime = null;
    private String mEndTime = null;
    private int mStartYear, mStartMonth, mStartDay;
    private int mEndYear, mEndMonth, mEndDay;
    //
    ArrayList<Entry> mValues = new ArrayList<>();
    ArrayList<Entry> mValues2 = new ArrayList<>();

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_report_center_time_old;
    }

    @Override
    public void initParam() {
        super.initParam();
        if (getArguments() != null) {
            mType = getArguments().getInt(Content.TYPE);
            mApiId = getArguments().getString(Content.KEY);
        }
//        if (TextUtils.isEmpty(mApiId)) {
//            ToastUtils.showShort("数据有误");
//        }
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
                mStartTime = mStartYear + "-" + mStartMonth + "-" + mStartDay;
            }
        }
        String[] splitEnd = mEndTime.split("-");
        if (splitEnd != null) {
            mEndYear = Integer.parseInt(splitEnd[0]);
            mEndMonth = Integer.parseInt(splitEnd[1]);
            mEndDay = Integer.parseInt(splitEnd[2]);
        }
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
            mValues.clear();
            if (s != null) {
                for (int i = 0; i < s.size(); i++) {
                    LineChartBean lineChartBean = s.get(i);
                    mValues.add(new Entry(i, lineChartBean.money));
                }
                LineDataSet set1 = new LineDataSet(mValues, "DataSet 1");
                initLineDataSet(set1, Color.BLUE);
                LineData data = new LineData();
                data.addDataSet(set1);
                binding.lineChart.setData(data);
                binding.lineChart.invalidate();
            }
        });

        if (!TextUtils.isEmpty(mApiId)){
            viewModel.requestEarningsTrend(mApiId, mStartTime, mEndTime,null);
            viewModel.requestIncomeStatistics(mApiId);
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (mType == 99) {
            binding.tvReportStartTime.setOnClickListener(this);
            binding.tvReportEndTime.setOnClickListener(this);
        }
        // 背景色
        binding.lineChart.setBackgroundColor(Color.WHITE);
        // 图表的文本描述
        binding.lineChart.getDescription().setEnabled(false);
        // 启用/禁用绘制图表边框（图表周围的线）。
        binding.lineChart.setDrawBorders(false);
        // 手势设置
        binding.lineChart.setTouchEnabled(true);
        // 添加监听器
        binding.lineChart.setOnChartValueSelectedListener(this);
        binding.lineChart.setDrawGridBackground(false);
//        // 自定义 MarkView，当数据被选择时会展示
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//        mv.setChartView(chart);
//        binding.lineChart.setMarker(mv);
        // 设置拖拽、缩放等
        binding.lineChart.setDragEnabled(false);
        binding.lineChart.setScaleEnabled(false);
        binding.lineChart.setScaleXEnabled(false);
        binding.lineChart.setScaleYEnabled(false);
        // 设置双指缩放
        binding.lineChart.setPinchZoom(false);
//        binding.lineChart.setDragDecelerationFrictionCoef(0.9f);
        // 获取图例（仅在设置数据后才可能）
        Legend l = binding.lineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(tfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);
        // 获取 X 轴
        XAxis xAxis = binding.lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(false);
        //设置x轴值在下方
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //x轴不显示文本
        xAxis.setDrawLabels(false);
        // 允许显示 X 轴的垂直网格线
//        xAxis.enableGridDashedLine(10f, 10f, 0f);
        // 获取 Y 轴
        YAxis yAxis = binding.lineChart.getAxisLeft();
        yAxis.setDrawGridLines(true);
        yAxis.setDrawZeroLine(true);
        yAxis.setGranularityEnabled(true);
        // 禁止右轴
        binding.lineChart.getAxisRight().setEnabled(false);
        // Y 轴的水平网格线
//        yAxis.enableGridDashedLine(10f, 10f, 0f);
        // 设置 Y 轴的数值范围
//        yAxis.setAxisMaximum(200f);
//        yAxis.setAxisMinimum(-50f);

//        for (int i = 0; i < 10; i++) {
//            float val = (float) (Math.random() * 2) - 30;
//            mValues.add(new Entry(i, val));
//        }
//
//        for (int i = 0; i < 10; i++) {
//            float val = (float) (Math.random() * 10) - 10;
//            mValues2.add(new Entry(i, val));
//        }
        // 2. 创建一个数据集 DataSet ，用来添加 Entry。一个图中可以包含多个数据集
//        LineDataSet set1 = new LineDataSet(mValues, "DataSet 1");
        // 3. 通过数据集设置数据的样式，如字体颜色、线型、线型颜色、填充色、线宽等属性
//        set1.enableDashedLine(10f, 5f, 0f);
//        initLineDataSet(set1,Color.BLUE);
//        LineDataSet set2 = new LineDataSet(mValues2, "DataSet 2");
//        // 3. 通过数据集设置数据的样式，如字体颜色、线型、线型颜色、填充色、线宽等属性
//        // 画虚线
////        set2.enableDashedLine(10f, 5f, 0f);
//        // 线和黑颜色
//        set2.setColor(Color.YELLOW);
//        set2.setCircleColor(Color.YELLOW);
//        // 线的粗细和点的大小
//        set2.setLineWidth(1f);
//        set2.setCircleRadius(3f);
//        // 画点为实心圆
//        set2.setDrawCircleHole(false);
        // 4.将数据集添加到数据 ChartData 中
//        LineData data = new LineData();
//        data.addDataSet(set1);
//        data.addDataSet(set2);
        // 5. 将数据添加到图表中
//        binding.lineChart.setData(data);

        binding.tvReportStartTime.setText(mStartTime);
        binding.tvReportEndTime.setText(mEndTime);

    }

    private void initLineDataSet(LineDataSet set1, int color) {
        set1.setColor(color);
        set1.setCircleColor(color);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
    }

    public static ReportCenterTimeFragmentOld newInstance(int type, String apiId) {

        Bundle args = new Bundle();
        args.putInt(Content.TYPE, type);
        args.putString(Content.KEY, apiId);
        ReportCenterTimeFragmentOld fragment = new ReportCenterTimeFragmentOld();
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
        new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    // 此处得到选择的时间，可以进行你想要的操作
                    KLog.d("您选择了：" + year + "年" + monthOfYear
                            + "月" + dayOfMonth + "日");
                    mStartYear = year;
                    mStartMonth = monthOfYear + 1;
                    mStartDay = dayOfMonth;
                    mStartTime = year + "-" + DateUtils.getInstance().getMakeUp0(mStartMonth) + "-" + DateUtils.getInstance().getMakeUp0(dayOfMonth);
                    binding.tvReportStartTime.setText(mStartTime);
                    viewModel.requestEarningsTrend(mApiId, mStartTime, mEndTime,null);
                }
                // 设置初始日期
                , mStartYear
                , mStartMonth - 1
                , mStartDay).show();
    }


    /**
     * 结束时间选择
     */
    private void showSelectEndTime() {
        new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    // 此处得到选择的时间，可以进行你想要的操作
                    KLog.d("您选择了：" + year + "年" + monthOfYear
                            + "月" + dayOfMonth + "日");
                    mEndYear = year;
                    mEndMonth = monthOfYear + 1;
                    mEndDay = dayOfMonth;
                    mEndTime = year + "-" + DateUtils.getInstance().getMakeUp0(mStartMonth) + "-" + DateUtils.getInstance().getMakeUp0(dayOfMonth);
                    binding.tvReportEndTime.setText(mEndTime);
                    viewModel.requestEarningsTrend(mApiId, mStartTime, mEndTime,null);
                }
                // 设置初始日期
                , mEndYear
                , mEndMonth - 1
                , mEndDay).show();
    }

}
