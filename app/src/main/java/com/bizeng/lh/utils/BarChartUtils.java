package com.bizeng.lh.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.LineChartBean;
import com.bizeng.lh.widget.XYMarkerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.List;

public class BarChartUtils {

    private static volatile BarChartUtils instance = null;

    private BarChartUtils() {

    }

    public static BarChartUtils getInstance() {
        if (instance == null) {
            synchronized (BarChartUtils.class) {
                instance = new BarChartUtils();
            }
        }
        return instance;
    }

    public String mStartTime, mEndTime;
    private List<LineChartBean> mLineChartBeans;

    public void initView(Context context, OnChartValueSelectedListener onChartValueSelectedListener, BarChart barChart, List<LineChartBean> lineChartBeans) {
        barChart.removeAllViews();
        barChart.clear();
        mLineChartBeans = lineChartBeans;
        barChart.setFitBars(true);//使两侧的柱图完全显示
        barChart.setExtraRightOffset(30);
        // 背景色
        barChart.setBackgroundColor(Color.WHITE);
        // 图表的文本描述
        barChart.getDescription().setEnabled(false);
        // 启用/禁用绘制图表边框（图表周围的线）。
        barChart.setDrawBorders(false);
        // 手势设置
        barChart.setTouchEnabled(true);
        // 添加监听器
        barChart.setOnChartValueSelectedListener(onChartValueSelectedListener);
        barChart.setDrawGridBackground(false);
        // 设置拖拽、缩放等
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setDragXEnabled(true);
        barChart.setDragYEnabled(false);
        barChart.setScaleXEnabled(true);
        barChart.setScaleYEnabled(false);
        barChart.setAutoScaleMinMaxEnabled(true);
        // 设置双指缩放
        barChart.setPinchZoom(true);
//        binding.lineChart.setDragDecelerationFrictionCoef(0.9f);
        barChart.setBorderColor(R.color.eee);
        barChart.setMaxVisibleValueCount(0);
        // 获取图例（仅在设置数据后才可能）
        Legend l = barChart.getLegend();
        l.setEnabled(false);
//        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(tfLight);
//        l.setTextSize(11f);
//        l.setTextColor(Color.WHITE);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setYOffset(11f);

        // 获取 X 轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(false);
        xAxis.setTextColor(R.color.text_c5c);
        //设置x轴值在下方
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //x轴显示文本
        xAxis.setDrawLabels(true);
        if (lineChartBeans != null && lineChartBeans.size() > 1) {
            xAxis.setLabelCount(2, false);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    //显示时间
                    if (mLineChartBeans != null) {
                        if (value >= 0 && mLineChartBeans.size() > value) {
                            if (mLineChartBeans.size() > value) {
                                LineChartBean lineChartBean = mLineChartBeans.get((int) value);
                                if (lineChartBean != null) {
                                    return lineChartBean.getCreateTime();
                                }
                            }
                        }
                    }
                    return "";
                }
            });
        } else {
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    //显示时间
                    if (mLineChartBeans != null) {
                        if (value == 0 && mLineChartBeans.size() > value) {
                            if (mLineChartBeans.size() > value) {
                                LineChartBean lineChartBean = mLineChartBeans.get((int) value);
                                if (lineChartBean != null) {
                                    return lineChartBean.getCreateTime();
                                }
                            }
                        }
                    }
                    return "";
                }
            });
        }

        // 允许显示 X 轴的垂直网格线
//        xAxis.enableGridDashedLine(10f, 10f, 0f);
        // 获取 Y 轴
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setDrawGridLines(true);
        yAxis.setDrawZeroLine(true);
        yAxis.setGranularityEnabled(false);
        yAxis.setTextColor(R.color.text_c5c);
//        yAxis.setLabelCount(5,false);
        //设置最小/最大宽度
//        yAxis.setMinWidth(10);
//        yAxis.setMaxWidth(20);
        // 禁止右轴
        barChart.getAxisRight().setEnabled(false);
//        barChart.getAxisLeft().setEnabled(false);
        // 设置硬件加速功能
        barChart.setHardwareAccelerationEnabled(true);
        //设置选中弹框
        XYMarkerView mv = new XYMarkerView(context, new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if (mLineChartBeans != null && mLineChartBeans.size() > value) {
                    LineChartBean lineChartBean = mLineChartBeans.get((int) value);
                    if (lineChartBean != null) {
                        return lineChartBean.getCreateTime();
                    }
                }
                return String.valueOf(value);
            }
        });
        mv.setChartView(barChart);
        barChart.setMarker(mv);
    }

    public void setChartData(BarChart barChart, int count) {
        Matrix m = new Matrix();
        float v = scaleNum(count);
        m.postScale(v, 1f);//两个参数分别是x,y轴的缩放比例。例如：将x轴的数据放大为之前的1.5倍
        barChart.getViewPortHandler().refresh(m, barChart, false);//将图表动画显示之前进行缩放
    }

    //30个横坐标时，缩放4f是正好的。
    private float scalePercent = 10f / 30f;

    private float scaleNum(int xCount) {
        return xCount * scalePercent;
    }
}
