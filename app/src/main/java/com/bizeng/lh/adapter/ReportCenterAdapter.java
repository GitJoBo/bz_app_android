package com.bizeng.lh.adapter;

import android.widget.TextView;

import com.bizeng.lh.R;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.StrategyProfitModelListBean;
import com.bizeng.lh.utils.TextParserUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wzq.mvvmsmart.utils.ConvertUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @Desc: 策略币增统计表
 * @author: admin wsj
 * @Date: 2021/5/11 6:54 PM
 */
public class ReportCenterAdapter extends BaseQuickAdapter<StrategyProfitModelListBean, BaseViewHolder> {
    public ReportCenterAdapter(@Nullable List<StrategyProfitModelListBean> data) {
        super(R.layout.item_report_center, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, StrategyProfitModelListBean strategyProfitModelListBean) {
        if (strategyProfitModelListBean == null) {
            return;
        }
        TextView tv_cumulative_currency_increase_u = holder.getView(R.id.tv_cumulative_currency_increase_u);
        TextView tv_today_increase_u = holder.getView(R.id.tv_today_increase_u);
        holder.setText(R.id.tv_report_strategy_name, strategyProfitModelListBean.strategyName);
//        holder.setText(R.id.tv_cumulative_currency_increase_u, String.format("%sU", strategyProfitModelListBean.getTotal()));
//        holder.setText(R.id.tv_today_increase_u, String.format("%sU", strategyProfitModelListBean.getToday()));
        textParse(strategyProfitModelListBean.getTotal().toString(), tv_cumulative_currency_increase_u);
        textParse(strategyProfitModelListBean.getToday().toString(), tv_today_increase_u);
        BigDecimal bigDecimal = strategyProfitModelListBean.getTotal().multiply(new BigDecimal(Content.U2CNY)).setScale(2, RoundingMode.HALF_UP);
        holder.setText(R.id.tv_cumulative_currency_increase_cny, String.format("≈%s CNY", bigDecimal));
        BigDecimal bigDecimalToday = strategyProfitModelListBean.getToday().multiply(new BigDecimal(Content.U2CNY)).setScale(2, RoundingMode.HALF_UP);
        holder.setText(R.id.tv_today_increase_cny, String.format("≈%s CNY", bigDecimalToday));

//        holder.setText(R.id.tv_end_time, strategyProfitModelListBean.endTime);

    }

    private void textParse(String sU, TextView tv_cumulative_currency_increase_u) {
        TextParserUtils textParserUtils = new TextParserUtils();
        textParserUtils.append(sU, ConvertUtils.dp2px(18), 0);
        textParserUtils.append(" U", ConvertUtils.dp2px(12), 0);
        textParserUtils.parse(tv_cumulative_currency_increase_u);
    }
}
