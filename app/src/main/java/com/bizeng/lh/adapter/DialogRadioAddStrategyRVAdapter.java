package com.bizeng.lh.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.ApiBean;
import com.bizeng.lh.bean.SelectBean;
import com.bizeng.lh.utils.MapRemoveNullUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;
import java.util.Map;

/**
 * @Desc: 通知弹框 选择适配器 添加策略
 * @author: admin wsj
 * @Date: 2021/4/21 2:39 PM
 */
public class DialogRadioAddStrategyRVAdapter extends BaseQuickAdapter<SelectBean<ApiBean>, BaseViewHolder> {
    public DialogRadioAddStrategyRVAdapter(@Nullable List<SelectBean<ApiBean>> data) {
        super(R.layout.item_dialog_radio_add_strategy, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(@NonNull BaseViewHolder helper, SelectBean<ApiBean> item) {
        if (item == null) return;
//        helper.setText(R.id.tv_name, item.getName());
        Map map = null;
        CheckBox checkBox = helper.getView(R.id.cb_rv);
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_exp_api = helper.getView(R.id.tv_exp_api);
        TextView tv_click_api = helper.getView(R.id.tv_click_api);
        ImageView iv_name = helper.getView(R.id.iv_name);
        tv_name.setTextColor(tv_name.getResources().getColor(R.color.color_999));
        switch (item.getName()) {
            case "火币":
                iv_name.setBackgroundResource(R.mipmap.ic_huobi);
                break;
            case "OKEx":
                iv_name.setBackgroundResource(R.mipmap.ic_okex);
                break;
            default:
                iv_name.setBackgroundResource(R.mipmap.ic_bian);
                break;
        }
        if (item.getExtra() == null) {
            tv_name.setText(String.format("%s(暂未绑定)", item.getName()));
            tv_name.setTextColor(tv_name.getResources().getColor(R.color.color_999));
            tv_click_api.setVisibility(View.VISIBLE);
            checkBox.setEnabled(false);
        } else {
            if (MapRemoveNullUtil.mapValueIsAllNull(item.getExtra().apiContent)) {
                tv_name.setText(String.format("%s(暂未绑定)", item.getName()));
                tv_name.setTextColor(tv_name.getResources().getColor(R.color.color_999));
                tv_click_api.setVisibility(View.VISIBLE);
                checkBox.setEnabled(false);
            } else {
                tv_exp_api.setVisibility(View.GONE);
                tv_click_api.setVisibility(View.GONE);
                tv_name.setText(item.getName());
                tv_name.setTextColor(tv_name.getResources().getColor(R.color.text_black));
            }
        }
        if (item.isSelected()) {
            checkBox.setChecked(true);
            tv_name.setTextColor(tv_name.getResources().getColor(R.color.text_245));
        } else {
            checkBox.setChecked(false);
            tv_name.setTextColor(tv_name.getResources().getColor(R.color.text_black));
        }
        if (item.getType() == 3) {
            checkBox.setEnabled(false);
            checkBox.setButtonDrawable(R.drawable.checkbox_style3);
            if (!MapRemoveNullUtil.mapValueIsAllNull(map)) {
                tv_exp_api.setVisibility(View.VISIBLE);
                tv_exp_api.setText("已添加策略");
                tv_name.setText(item.getName());
                tv_name.setTextColor(tv_name.getResources().getColor(R.color.color_999));
            }
        } else if (item.getType() == 1) {
            if (item.getExtra() != null && !MapRemoveNullUtil.mapValueIsAllNull(item.getExtra().apiContent)) {
                checkBox.setEnabled(true);
            }
            tv_exp_api.setVisibility(View.GONE);
            checkBox.setButtonDrawable(R.drawable.checkbox_style2);
        } else if (item.getType() == 2) {
            checkBox.setEnabled(false);
            checkBox.setButtonDrawable(R.drawable.checkbox_style2);
        } else {
            checkBox.setEnabled(false);
            checkBox.setButtonDrawable(R.drawable.checkbox_style2);
            tv_exp_api.setVisibility(View.GONE);
            tv_name.setText(String.format("%s(暂未绑定)", item.getName()));
            tv_name.setTextColor(tv_name.getResources().getColor(R.color.color_999));
        }
    }

    /**
     * 返回选中的apiId ,风格
     *
     * @return
     */
    public String getChecks() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getData().size(); i++) {
            SelectBean<ApiBean> selectBean = getData().get(i);
            if (selectBean.isSelected()) {
                if (selectBean.getExtra() != null && selectBean.getType() != 3) {
                    sb.append(selectBean.getExtra().apiId + ",");
                }
            }
        }
        return sb.toString();
    }
}
