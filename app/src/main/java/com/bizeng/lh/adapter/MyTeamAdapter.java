package com.bizeng.lh.adapter;

import android.view.View;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.UserBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MyTeamAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {
    public MyTeamAdapter(@Nullable List<UserBean> data) {
        super(R.layout.item_my_team, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, UserBean item) {
        if (item == null) return;
        int itemPosition = getItemPosition(item);
        int size = getData().size() - 1;
        View view = holder.getView(R.id.item_rv);
        if (itemPosition == 0) {
            view.setBackgroundResource(R.drawable.shape_white_r6600);
        } else if (itemPosition < size) {
            view.setBackgroundResource(R.color.white);
        } else {
            view.setBackgroundResource(R.drawable.shape_white_r0066);
        }
        holder.setText(R.id.tv_member, item.getPhoneX());
        holder.setText(R.id.tv_cumulative_advance_receipt, item.getBeforeProfitMoney() + " U");
        holder.setText(R.id.tv_accumulative, item.getActualProfitMoney() + " U");
        holder.setText(R.id.tv_arrival_rate, item.getArrivalRate());
    }
}
