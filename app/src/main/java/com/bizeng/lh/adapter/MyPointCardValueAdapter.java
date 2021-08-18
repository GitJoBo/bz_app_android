package com.bizeng.lh.adapter;

import android.view.View;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.PointCardRecordBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MyPointCardValueAdapter extends BaseQuickAdapter<PointCardRecordBean, BaseViewHolder> {
    public MyPointCardValueAdapter(@Nullable List<PointCardRecordBean> data) {
        super(R.layout.item_potnt_card_rv, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, PointCardRecordBean pointCardBean) {
        if (pointCardBean == null) {
            return;
        }
        int itemPosition = getItemPosition(pointCardBean);
        int size = getData().size() - 1;
        View view = holder.getView(R.id.item_rv);
        if (itemPosition == 0) {
            holder.getView(R.id.view_1).setVisibility(View.VISIBLE);
//            view.setBackgroundResource(R.drawable.shape_white_r6600);
        } else if (itemPosition == size) {
            holder.getView(R.id.view_1).setVisibility(View.INVISIBLE);
//            view.setBackgroundResource(R.drawable.shape_white_r0066);
        } else {
            holder.getView(R.id.view_1).setVisibility(View.VISIBLE);
//            view.setBackgroundResource(R.color.white);
        }
        holder.setText(R.id.tv_type, pointCardBean.getRecordType());
        holder.setText(R.id.tv_gift_instructions, "[" + pointCardBean.recordTag + "]" + pointCardBean.description);
        if ("2".equals(pointCardBean.recordType)) {
            holder.setText(R.id.tv_point, "-" + pointCardBean.getMoney().toString());
        } else {
            holder.setText(R.id.tv_point, "+" + pointCardBean.getMoney().toString());
        }
        holder.setText(R.id.tv_point_time, pointCardBean.createTime);

    }
}
