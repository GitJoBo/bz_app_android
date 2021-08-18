package com.bizeng.lh.adapter;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.CurrentPositionBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Desc: 当前持仓饼状图
 * @author: admin wsj
 * @Date: 2021/6/10 4:52 PM
 */
public class CurrentPositionRVAdapter extends BaseQuickAdapter<CurrentPositionBean, BaseViewHolder> {

    public CurrentPositionRVAdapter(@Nullable List<CurrentPositionBean> data) {
        super(R.layout.item_current_position, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, CurrentPositionBean item) {
        if (item == null) {
            return;
        }
        holder.setText(R.id.tv_name, item.getName());
        holder.setBackgroundColor(R.id.iv_color, item.Color);

    }
}
