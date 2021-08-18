package com.bizeng.lh.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.SelectBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wzq.mvvmsmart.utils.ConvertUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

public class ReportCenterTimeSelectAdapter extends BaseQuickAdapter<SelectBean<String>, BaseViewHolder> {
    //时间选择
    public boolean isShowSelectTime = false;
    //时间选择 时间
    public long oldTime = 0;

    public ReportCenterTimeSelectAdapter(@Nullable List<SelectBean<String>> data) {
        super(R.layout.item_report_center_time_select, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, SelectBean<String> item) {
        if (item == null) {
            return;
        }
        TextView tvTime = holder.getView(R.id.tv_title);
        LinearLayout llItem = holder.getView(R.id.ll_item);
//        ImageView ivDropdown = holder.getView(R.id.iv_drop_down);
        int itemPosition = getItemPosition(item);
        int size = getData().size() - 1;
//        if (itemPosition == 2) {
//            ivDropdown.setVisibility(View.VISIBLE);
//        }
        tvTime.setText(item.getName());
        tvTime.setPadding(0, 0, 0, 0);
//        GradientDrawable gd = new GradientDrawable();
//        gd.setColor(Color.parseColor("#ECEFF9"));
        if (item.isSelected()) {
            tvTime.setTextColor(Color.parseColor("#000000"));
            if (itemPosition == 0) {
                llItem.setBackgroundResource(R.mipmap.ic_tab_one);
                tvTime.setPadding(0, 0, ConvertUtils.dp2px(20), 0);
            } else if (itemPosition == size) {
                llItem.setBackgroundResource(R.mipmap.ic_tab_three);
                tvTime.setPadding(ConvertUtils.dp2px(20), 0, 0, 0);
            } else {
                llItem.setBackgroundResource(R.mipmap.ic_tab_two);
                tvTime.setPadding(0, 0, 0, 0);
            }
        } else {
            tvTime.setTextColor(Color.parseColor("#3C5B75"));
            if (itemPosition == 0) {
                llItem.setBackgroundResource(R.drawable.shape_cec_left_r6);
                tvTime.setPadding(0, 0, ConvertUtils.dp2px(20), 0);
//                gd.setCornerRadii(new float[]{ConvertUtils.dp2px(6), ConvertUtils.dp2px(6), 0, 0, 0, 0, 0, 0});
            } else if (itemPosition == size) {
                llItem.setBackgroundResource(R.drawable.shape_cec_right_r6);
                tvTime.setPadding(ConvertUtils.dp2px(20), 0, 0, 0);
//                gd.setCornerRadii(new float[]{0, 0, ConvertUtils.dp2px(6), ConvertUtils.dp2px(6), 0, 0, 0, 0});
            } else {
                llItem.setBackgroundResource(R.color.color_cec);
                tvTime.setPadding(0, 0, 0, 0);
//                gd.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
            }
//            llItem.setBackground(gd);
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

    public void rotate(View view, SelectBean selectBean) {
        long millis = new Date().getTime();
        if (millis - oldTime > 500) {
            oldTime = millis;
        } else {
            return;
        }
        if (!selectBean.isSelected()) {
            return;
        }
        float toDegrees = -180f;
        float fromDegrees = 0f;
        if (!isShowSelectTime) {
            toDegrees = 0f;
            fromDegrees = -180f;
        }
        Animation anim = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(500); // 设置动画时间
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i("lgq", "re==logtest===onAnimationStart==");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i("lgq", "re==logtest===onAnimationEnd==");
                isShowSelectTime = !isShowSelectTime;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.i("lgq", "re==logtest===onAnimationRepeat==");
            }
        });
        view.startAnimation(anim);
    }

}
