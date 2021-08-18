package com.bizeng.lh.adapter;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NoticeListAdapter extends BaseQuickAdapter<TutorialAreaBean, BaseViewHolder> {


    public NoticeListAdapter(@Nullable List<TutorialAreaBean> data) {
        super(R.layout.item_notice_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TutorialAreaBean item) {
        if (item == null) {
            return;
        }
        holder.setText(R.id.tv_notice_title, item.title);
        holder.setText(R.id.tv_notice_time, item.createTime);
    }
}
