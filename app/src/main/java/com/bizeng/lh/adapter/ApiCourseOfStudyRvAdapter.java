package com.bizeng.lh.adapter;

import android.view.View;

import com.bizeng.lh.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class ApiCourseOfStudyRvAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ApiCourseOfStudyRvAdapter(List<String> data) {
        super(R.layout.item_api_course_of_study, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        View view = holder.getView(R.id.view_1);
        holder.setText(R.id.tv_api_title, s);
        if (getItemPosition(s) % 2 == 0) {
            view.setBackgroundResource(R.drawable.shape_c89a9f0_r5);
        } else {
            view.setBackgroundResource(R.drawable.shape_cec8e70_r5);
        }
    }
}
