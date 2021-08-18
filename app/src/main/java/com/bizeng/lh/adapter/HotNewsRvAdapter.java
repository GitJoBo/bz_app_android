package com.bizeng.lh.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.utils.GlideUtils;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Desc: 热门资讯 行业焦点
 * @author: admin wsj
 * @Date: 2021/4/22 3:11 PM
 */
public class HotNewsRvAdapter extends BaseMultiItemQuickAdapter<TutorialAreaBean, BaseViewHolder> {
    public HotNewsRvAdapter(@Nullable List<TutorialAreaBean> data) {
        super(data);
        addItemType(TutorialAreaBean.TYPE, R.layout.item_hot_news);
        addItemType(TutorialAreaBean.TYPE_IMG, R.layout.item_hot_news_imge);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TutorialAreaBean item) {
        if (item == null) {
            return;
        }
        TextView tv_popular_inquiries_title = holder.getView(R.id.tv_popular_inquiries_title);
        TextView tv_popular_inquiries_content = holder.getView(R.id.tv_popular_inquiries_content);
        TextView tv_popular_inquiries_time = holder.getView(R.id.tv_popular_inquiries_time);
        tv_popular_inquiries_title.setText(item.title);
        tv_popular_inquiries_content.setText(item.informationAbstract);
        tv_popular_inquiries_time.setText(item.createTime);
        switch (holder.getItemViewType()) {
            case TutorialAreaBean.TYPE:
                break;
            case TutorialAreaBean.TYPE_IMG:
                ImageView iv_popular_inquiries = holder.getView(R.id.iv_popular_inquiries);
                GlideUtils.getLoad(item.coverSrc, iv_popular_inquiries)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(15)))//通过此方式不会闪烁
                        .into(iv_popular_inquiries);
                break;
            default:
                break;
        }
    }
}
