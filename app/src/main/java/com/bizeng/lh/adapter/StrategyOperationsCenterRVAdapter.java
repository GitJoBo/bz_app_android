package com.bizeng.lh.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.StrategyBean;
import com.bizeng.lh.utils.DateUtils;
import com.bizeng.lh.utils.UIUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class StrategyOperationsCenterRVAdapter extends BaseQuickAdapter<StrategyBean, BaseViewHolder> {

    public StrategyOperationsCenterRVAdapter(List<StrategyBean> data) {
        super(R.layout.item_strategy_operations_center, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, StrategyBean s) {
        holder.setText(R.id.tv_name, s.strategyTitle);
        TextView tvU = holder.getView(R.id.tv_u);
        holder.setText(R.id.tv_warehouse_capacity, String.format("仓位容量：%s", s.getCapacityValue()));
        UIUtils.getInstance().setU2CNY(s.getProfit(), tvU);
        TextView tv_start_strategy_now = holder.getView(R.id.tv_start_strategy_now);
        LinearLayout ll_state = holder.getView(R.id.ll_state);
        TextView tv_strategy_state = holder.getView(R.id.tv_strategy_state);
        TextView tv_strategy_reason = holder.getView(R.id.tv_strategy_reason);
        TextView tv_strategy_run_day = holder.getView(R.id.tv_strategy_run_day);
        TextView tv_recover = holder.getView(R.id.tv_recover);
        TextView tv_history = holder.getView(R.id.tv_history);
        ll_state.setVisibility(View.GONE);
        tv_start_strategy_now.setVisibility(View.GONE);
        tv_strategy_reason.setVisibility(View.GONE);
        tv_strategy_run_day.setVisibility(View.GONE);
        tv_recover.setVisibility(View.GONE);
        tv_history.setVisibility(View.VISIBLE);
        tv_strategy_state.setCompoundDrawables(null, null, null, null);
        switch (s.status) {
            //状态：0-未启动；1-启动；2-暂停
            case "0":
                tv_start_strategy_now.setVisibility(View.VISIBLE);
                tv_start_strategy_now.setText("立即开启策略");
                tv_history.setVisibility(View.GONE);
                tv_start_strategy_now.setEnabled(true);
                tv_start_strategy_now.setClickable(true);
                tv_start_strategy_now.setTextColor(ContextCompat.getColor(tv_start_strategy_now.getContext(), R.color.text_245));
                break;
            case "1":
                ll_state.setVisibility(View.VISIBLE);
                tv_strategy_state.setText("运行中");
                setStateDrawableLeft(tv_strategy_state, R.mipmap.ic_runed);
                tv_strategy_state.setTextColor(tv_strategy_state.getContext().getResources().getColor(R.color.color_449));
                //已运行  xxx天
//                String timeDifference = DateUtils.getInstance().getTimeDifference(s.startTime, DateUtils.getInstance().getNowTime(), 0);
                String timeDifference = DateUtils.getInstance().getTimeDifferenceNo0(s.startTime, DateUtils.getInstance().getNowTime(), 0);
                if ("0".equals(timeDifference)) {
                    tv_strategy_run_day.setVisibility(View.GONE);
                } else {
                    tv_strategy_run_day.setVisibility(View.VISIBLE);
                    tv_strategy_run_day.setTextColor(tv_strategy_run_day.getContext().getResources().getColor(R.color.color_449));
                    tv_strategy_run_day.setText(String.format("已运行  %s天", timeDifference));
                }
                break;
            case "2":
                tv_recover.setVisibility(View.VISIBLE);
                ll_state.setVisibility(View.VISIBLE);
                if (s.getStopStatusValue() == 0) {
                    //手动暂停，不显示原因
                    tv_strategy_state.setText("已暂停");
                    setStateDrawableLeft(tv_strategy_state, R.mipmap.ic_stoped);
                    tv_strategy_state.setTextColor(tv_strategy_state.getContext().getResources().getColor(R.color.color_df8));
                } /*else if (s.stopStatus == 1) {
                    //api错误
                } else if (s.stopStatus == 2) {
                    //欠费
                } */ else {
                    //未知错误
                    tv_strategy_state.setText("已停止");
                    setStateDrawableLeft(tv_strategy_state, R.mipmap.ic_stoped_red);
                    tv_strategy_state.setTextColor(tv_strategy_state.getContext().getResources().getColor(R.color.red_ff44));
                    //暂停原因：1、xxxx 2、xxxx
                    tv_strategy_reason.setVisibility(View.VISIBLE);
                    tv_strategy_reason.setTextColor(tv_strategy_run_day.getContext().getResources().getColor(R.color.red_ff44));
                    tv_strategy_reason.setText(String.format("暂停原因：%s", s.stopDesc));
                }
                break;
        }
    }

    private void setStateDrawableLeft(TextView tv_strategy_state, int drawableId) {
        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = tv_strategy_state.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_strategy_state.setCompoundDrawables(drawable, null, null, null);
    }


}
