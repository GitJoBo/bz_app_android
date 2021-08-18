package com.bizeng.lh.adapter;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.UserBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PhoneAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {
    public PhoneAdapter(@Nullable List<UserBean> data) {
        super(R.layout.item_recommend, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, UserBean userBean) {
        if (userBean == null) {
            return;
        }
        holder.setText(R.id.tv_phone, userBean.phone);
    }
}
