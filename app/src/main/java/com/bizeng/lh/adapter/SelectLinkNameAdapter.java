package com.bizeng.lh.adapter;

import android.widget.TextView;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.SelectBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Desc: ERC20 TRC20
 * @author: admin wsj
 * @Date: 2021/5/12 2:35 PM
 */
public class SelectLinkNameAdapter extends BaseQuickAdapter<SelectBean<String>, BaseViewHolder> {
    public SelectLinkNameAdapter(@Nullable List<SelectBean<String>> data) {
        super(R.layout.item_link_name, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, SelectBean<String> item) {
        if (item == null) {
            return;
        }
        TextView tv = holder.getView(R.id.tv_item_point_card_value);
        tv.setText(item.getName());
        if (item.isSelected()) {
            tv.setBackgroundResource(R.drawable.shape_storke_c315_r2);
            tv.setTextColor(tv.getContext().getResources().getColor(R.color.color_315));
        } else {
            tv.setBackgroundResource(R.drawable.shape_storke_cccc_r2);
            tv.setTextColor(tv.getResources().getColor(R.color.text_black));
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
