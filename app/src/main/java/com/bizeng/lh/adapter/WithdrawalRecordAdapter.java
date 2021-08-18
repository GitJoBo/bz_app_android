package com.bizeng.lh.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.bizeng.lh.R;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.WithdrawalRecordBean;
import com.bizeng.lh.utils.BigDecimalUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Desc: 兑换记录
 * @author: admin wsj
 * @Date: 2021/7/1 1:38 PM
 */
public class WithdrawalRecordAdapter extends BaseQuickAdapter<WithdrawalRecordBean, BaseViewHolder> {

    public WithdrawalRecordAdapter(@Nullable List<WithdrawalRecordBean> data) {
        super(R.layout.item_withdrawal_record, data);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, WithdrawalRecordBean item) {
        if (item == null) {
            return;
        }
        holder.setText(R.id.tv_type_value, item.getWithdrawalTypeValue());
        TextView tv_states = holder.getView(R.id.tv_states);
        TextView tv_chain_type = holder.getView(R.id.tv_chain_type);
        TextView tv_chain_type_value = holder.getView(R.id.tv_chain_type_value);
        TextView tv_reason_value = holder.getView(R.id.tv_reason_value);
        TextView tv_source = holder.getView(R.id.tv_source);
        TextView tv_source_value = holder.getView(R.id.tv_source_value);
        TextView tv_handling_fee = holder.getView(R.id.tv_handling_fee);
        TextView tv_handling_fee_value = holder.getView(R.id.tv_handling_fee_value);
        TextView tv_reason = holder.getView(R.id.tv_reason);
        tv_reason_value.setVisibility(View.GONE);
        tv_reason.setVisibility(View.GONE);
        if ("1".equals(item.withdrawalType)) {
            tv_source.setVisibility(View.GONE);
            tv_source_value.setVisibility(View.GONE);
            tv_handling_fee.setVisibility(View.GONE);
            tv_handling_fee_value.setVisibility(View.GONE);
            tv_chain_type.setVisibility(View.GONE);
            tv_chain_type_value.setVisibility(View.GONE);
            tv_handling_fee.setText("兑换点数：");
            tv_handling_fee_value.setText(BigDecimalUtils.getInstance().getBigDecimal(item.proceMoney).toString() + "U");
        } else {
            tv_source.setVisibility(View.VISIBLE);
            tv_source_value.setVisibility(View.VISIBLE);
            tv_handling_fee.setVisibility(View.VISIBLE);
            tv_handling_fee_value.setVisibility(View.VISIBLE);
            tv_chain_type.setVisibility(View.VISIBLE);
            tv_chain_type_value.setVisibility(View.VISIBLE);
            tv_source_value.setText(item.address);
            if (Content.TRC20.equals(item.coinType)) {
                tv_chain_type_value.setText("TRC20");
            } else {
                tv_chain_type_value.setText("ERC20");
            }
            tv_handling_fee.setText("手续费：");
            tv_handling_fee_value.setText(BigDecimalUtils.getInstance().getBigDecimal(item.proceMoney).toString() + "U");
        }
        switch (item.status) {
            case 2:
                tv_states.setBackgroundResource(R.mipmap.ic_mention_money_audit_failed);
                tv_states.setText("审核不通过");
                tv_states.setTextColor(tv_states.getResources().getColor(R.color.white));
                tv_reason_value.setVisibility(View.VISIBLE);
                tv_reason.setVisibility(View.VISIBLE);
                break;
            case 3:
                tv_states.setBackgroundResource(R.mipmap.ic_mention_money_audit_finish);
                tv_states.setText("已完成");
                tv_states.setTextColor(tv_states.getResources().getColor(R.color.color_999));
                break;
            default:
                tv_states.setBackgroundResource(R.mipmap.ic_mention_money_audit_pending);
                tv_states.setText("待审核");
                tv_states.setTextColor(tv_states.getResources().getColor(R.color.white));
                break;
        }
        holder.setText(R.id.tv_num_value, BigDecimalUtils.getInstance().getBigDecimal(item.money).toString() + "U");
        holder.setText(R.id.tv_end_time_value, item.creatTime);
        tv_reason_value.setText(item.errorMsg);
    }
}
