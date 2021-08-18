package com.bizeng.lh.adapter;

import android.text.Html;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.LimitedTimeOfferBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wzq.mvvmsmart.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LimitedTimeOfferAdapter extends BaseQuickAdapter<LimitedTimeOfferBean, BaseViewHolder> {
    public LimitedTimeOfferAdapter(@Nullable List<LimitedTimeOfferBean> data) {
        super(R.layout.item_limited_time_offer, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, LimitedTimeOfferBean item) {
        try {
            int itemPosition = getItemPosition(item);
            int size = getData().size() - 1;
            //单次充币 100~299  USDT
            if (itemPosition < size) {
                LimitedTimeOfferBean item2 = getData().get(itemPosition + 1);
                int i = Integer.parseInt(item2.minMoney) - 1;
                holder.setText(R.id.tv_title, Html.fromHtml("单次兑换满足 <font color=\"#2457BF\">" + item.minMoney + "-" + i + "</font> USDT"));
            } else {
                holder.setText(R.id.tv_title, Html.fromHtml("单次兑换满足 <font color=\"#2457BF\">" + item.minMoney + "</font> USDT"));
            }
            holder.setText(R.id.tv_presentation, "赠送" + item.getGiveProportion() + "%点");
        }catch (Exception e){
            ToastUtils.showShort("优惠政策数据有误！");
        }
    }
}
