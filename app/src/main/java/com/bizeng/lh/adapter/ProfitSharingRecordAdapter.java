package com.bizeng.lh.adapter;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.ProfitSharingRecordBean;
import com.bizeng.lh.utils.BigDecimalUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Desc: 返利记录
 * @author: admin wsj
 * @Date: 2021/6/28 9:31 AM
 */
public class ProfitSharingRecordAdapter extends BaseQuickAdapter<ProfitSharingRecordBean, BaseViewHolder> {

    public ProfitSharingRecordAdapter(@Nullable List<ProfitSharingRecordBean> data) {
        super(R.layout.item_profit_sharing_record, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ProfitSharingRecordBean item) {
        if (item == null) {
            return;
        }
        holder.setText(R.id.tv_type_value, item.getPropertyValue());
        holder.setText(R.id.tv_source_value, item.fromNickName);
        holder.setText(R.id.tv_num_value, BigDecimalUtils.getInstance().getBigDecimal(item.money).toString());
        holder.setText(R.id.tv_end_time_value, item.createTime);
    }
}
