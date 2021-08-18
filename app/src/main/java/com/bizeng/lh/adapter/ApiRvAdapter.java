package com.bizeng.lh.adapter;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.ApiBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class ApiRvAdapter extends BaseQuickAdapter<ApiBean, BaseViewHolder> {
    public ApiRvAdapter(List<ApiBean> data) {
        super(R.layout.item_api, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ApiBean item) {
        holder.setText(R.id.tv_api_title, item.title);
    }
}
