package com.bizeng.lh.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.SelectBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Desc: 我的点卡 全部 充币 赠送 扣点
 * @author: admin wsj
 * @Date: 2021/5/12 2:35 PM
 */
public class MyPointCardAdapter extends BaseQuickAdapter<SelectBean<String>, BaseViewHolder> {
    public MyPointCardAdapter(@Nullable List<SelectBean<String>> data) {
        super(R.layout.item_point_card_select, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, SelectBean<String> item) {
        if (item == null) {
            return;
        }
        TextView tv = holder.getView(R.id.tv_item_point_card_value);
        tv.setText(item.getName());
        if (item.isSelected()) {
            tv.setBackgroundResource(R.drawable.shape_c315_r20);
            tv.setTextColor(Color.WHITE);
        } else {
            tv.setBackgroundResource(R.drawable.shape_stroke_cddd_r20);
            tv.setTextColor(tv.getResources().getColor(R.color.color_666));
        }
    }

    public void setSelectItem(int position) {
        initItem();
        SelectBean<String> selectBean = getData().get(position);
        selectBean.setSelected(!selectBean.isSelected());
    }

    public void initItem() {
        for (SelectBean selectBean : getData()) {
            selectBean.setSelected(false);
        }
    }
}
