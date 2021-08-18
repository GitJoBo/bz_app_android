package com.bizeng.lh.adapter;

import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.bizeng.lh.R;
import com.bizeng.lh.app.App;
import com.bizeng.lh.bean.StrategyBean;
import com.bizeng.lh.utils.TextParserUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wzq.mvvmsmart.utils.ConvertUtils;

import java.util.List;

import rxhttp.wrapper.utils.GsonUtil;

/**
 * 精选策略 AI量化策略 R.layout.item_ploy
 */
public class PloyRVAdapter extends BaseQuickAdapter<StrategyBean, BaseViewHolder> {
    public PloyRVAdapter(@Nullable List<StrategyBean> data) {
        super(R.layout.item_ploy, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, StrategyBean item) {
        if (item == null) {
            return;
        }
        TextView tv_ploy_annualized_rate = helper.getView(R.id.tv_ploy_annualized_rate);
        TextView tv_ploy_label_three = helper.getView(R.id.tv_ploy_label_three);
        TextView tv_ploy_label_four = helper.getView(R.id.tv_ploy_label_four);
        LinearLayoutCompat ll_ploy_ = helper.getView(R.id.ll_ploy_);
        List<String> list = GsonUtil.fromJson(item.tag, List.class);
        helper.setText(R.id.tv_ploy_rv_name, item.title);
        helper.setText(R.id.tv_ploy_type, item.remark);
        //改为动态添加，不考虑显示不下
        ll_ploy_.removeAllViews();
        if (list != null) {
            int size1 = list.size();
            int liftPadding = ConvertUtils.dp2px(6);
            int topPadding = ConvertUtils.dp2px(4);
            for (int i = 0; i < size1; i++) {
                TextView textView = new TextView(ll_ploy_.getContext());
                textView.setText(list.get(i));
                textView.setPadding(liftPadding, topPadding, liftPadding, topPadding);
                textView.setMaxLines(1);
                textView.setTextSize(9);
                textView.setTextColor(App.getInstance().getColor(R.color.text_245));
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setBackgroundResource(R.drawable.shape_e9f_solid);
                ll_ploy_.addView(textView);
                if (i != 0) {
                    LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams) textView.getLayoutParams();
                    layoutParams.leftMargin = ConvertUtils.dp2px(10);
                }
            }
        }
        if (item.getIsHot()) {
            tv_ploy_label_three.setVisibility(View.VISIBLE);
        } else {
            tv_ploy_label_three.setVisibility(View.GONE);
        }
        if (item.getIsNew()) {
            tv_ploy_label_four.setVisibility(View.VISIBLE);
        } else {
            tv_ploy_label_four.setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_ploy_rv_name, item.title);
        if (!TextUtils.isEmpty(item.annualized)) {
            tv_ploy_annualized_rate.setVisibility(View.VISIBLE);
            item.annualized = item.annualized.replace("%", "");
            if (item.annualized.contains("-")) {
//                helper.setText(R.id.tv_ploy_annualized_rate, item.annualized);
                item.annualized = item.annualized.replace("-", "");
                TextParserUtils textParser = new TextParserUtils();
                textParser.append("-", ConvertUtils.dp2px(14), 0, Typeface.NORMAL);
                textParser.append(item.annualized, ConvertUtils.dp2px(21), 0, Typeface.NORMAL);
                textParser.append("%", ConvertUtils.dp2px(14), 0, Typeface.NORMAL);
                textParser.parse(tv_ploy_annualized_rate);
            } else {
//            helper.setText(R.id.tv_ploy_annualized_rate, "+" + item.annualized);
                TextParserUtils textParser = new TextParserUtils();
                textParser.append("+", ConvertUtils.dp2px(14), 0, Typeface.NORMAL);
                textParser.append(item.annualized, ConvertUtils.dp2px(21), 0, Typeface.NORMAL);
                textParser.append("%", ConvertUtils.dp2px(14), 0, Typeface.NORMAL);
                textParser.parse(tv_ploy_annualized_rate);
            }
        }else {
            tv_ploy_annualized_rate.setVisibility(View.GONE);
        }

//        helper.setText(R.id.tv_number_of_users, DataConversionUtils.dataConversion(item.getUseNumber(), DataConversionUtils.CH));
        helper.setText(R.id.tv_number_of_users, item.getUseNumber());
    }

}
