package com.bizeng.lh.adapter;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.SelectBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @Desc: 通知弹框 单选适配器 开启策略
 * @author: admin wsj
 * @Date: 2021/4/21 2:39 PM
 */
public class DialogRadioRVAdapter extends BaseQuickAdapter<SelectBean<String>, BaseViewHolder> {
    public DialogRadioRVAdapter(@Nullable List<SelectBean<String>> data) {
        super(R.layout.item_radio_et, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SelectBean<String> item) {
        if (item == null) return;
        TextView tvName = helper.getView(R.id.tv_name);
        Group group = helper.getView(R.id.cl_group_capacity);
        CheckBox checkBox = helper.getView(R.id.cb_rv);
        TextView tvLegend = helper.getView(R.id.tv_legend);
        EditText etWarehouseCapacity = helper.getView(R.id.et_warehouse_capacity);
        tvName.setText(item.getName());
        if (item.getType() == 9) {
            //显示自定义仓位输入框
            if (item.isSelected()) {
                group.setVisibility(View.VISIBLE);
                etWarehouseCapacity.setText(item.getExtra());
            } else {
                group.setVisibility(View.GONE);
            }
        } else {
            group.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(item.getLegend())) {
            tvLegend.setText(item.getLegend());
            tvLegend.setVisibility(View.VISIBLE);
        } else {
            tvLegend.setVisibility(View.GONE);
        }
        etWarehouseCapacity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setExtra(s.toString());
            }
        });
        checkBox.setChecked(item.isSelected());
        if (item.isSelected()) {
            tvName.setTextColor(Color.parseColor("#2457BF"));
        } else {
            tvName.setTextColor(Color.parseColor("#333333"));
        }
    }

    /**
     * 单选
     */
    public void singleChoice(int position) {
        List<SelectBean<String>> data = getData();
        if (position < data.size()) {
            toBeUnselected(data);
            data.get(position).setSelected(true);
            notifyDataSetChanged();
        }
    }

    /**
     * 至为未选
     */
    public void toBeUnselected(List<SelectBean<String>> data) {
        if (data != null) {
            for (SelectBean<String> selected : data) {
                selected.setSelected(false);
            }
        }
    }

    /**
     * 获取选择的值
     */
    public String getSelectedValue() {
        try {
            List<SelectBean<String>> data = getData();
            for (int i = 0; i < data.size(); i++) {
                SelectBean<String> selectBean = data.get(i);
                if (selectBean.isSelected()) {
                    if (selectBean.getType() == 9) {
                        if (Integer.parseInt(selectBean.getExtra()) < 1) {
                            return "-1";
                        }
                    }
                    return selectBean.getExtra();
                }
            }
        }catch (Exception e){

        }
        return "-1";
    }
}
