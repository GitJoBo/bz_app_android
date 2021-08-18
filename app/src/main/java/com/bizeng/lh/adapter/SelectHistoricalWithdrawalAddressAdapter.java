package com.bizeng.lh.adapter;

import android.widget.TextView;

import com.bizeng.lh.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SelectHistoricalWithdrawalAddressAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SelectHistoricalWithdrawalAddressAdapter(@Nullable List<String> data) {
        super(R.layout.item_withdrawal_address, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String item) {
        if (item == null) return;
//        CheckBox checkBox = holder.getView(R.id.cb_rv);
        TextView tvAddress = holder.getView(R.id.tv_address);
        tvAddress.setText(item);
//        checkBox.setChecked(item.isSelected());
    }

//    /**
//     * 单选
//     */
//    public void singleChoice(int position) {
//        List<SelectBean<String>> data = getData();
//        if (position < data.size()) {
//            toBeUnselected(data);
//            data.get(position).setSelected(true);
//            notifyDataSetChanged();
//        }
//    }
//
//    /**
//     * 至为未选
//     */
//    public void toBeUnselected(List<SelectBean<String>> data) {
//        if (data != null) {
//            for (SelectBean<String> selected : data) {
//                selected.setSelected(false);
//            }
//        }
//    }
//
//    /**
//     * 获取选择的值
//     */
//    public String getSelectedValue() {
//        List<SelectBean<String>> data = getData();
//        for (int i = 0; i < data.size(); i++) {
//            SelectBean<String> selectBean = data.get(i);
//            if (selectBean.isSelected()) {
//                if (selectBean.getType() == 9) {
//                    if (Integer.parseInt(selectBean.getExtra()) < 1) {
//                        return "-1";
//                    }
//                }
//                return selectBean.getExtra();
//            }
//        }
//        return "";
//    }
}
