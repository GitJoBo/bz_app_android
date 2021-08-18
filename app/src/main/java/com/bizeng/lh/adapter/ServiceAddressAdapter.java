package com.bizeng.lh.adapter;

import android.text.TextUtils;
import android.view.View;

import com.bizeng.lh.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Desc: 服务器ip
 * @author: admin wsj
 * @Date: 2021/5/9 1:45 PM
 */
public class ServiceAddressAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ServiceAddressAdapter(@Nullable List<String> data) {
        super(R.layout.item_service_address, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String s) {
        if (TextUtils.isEmpty(s)) {
            return;
        }
        int itemPosition = getItemPosition(s);
        int size = getData().size()-1;
        View view = holder.getView(R.id.view_diver);
        if (itemPosition == size) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        holder.setText(R.id.tv_current_server_ip_address, s);
    }
}
