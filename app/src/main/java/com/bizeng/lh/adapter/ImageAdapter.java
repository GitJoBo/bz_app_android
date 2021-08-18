package com.bizeng.lh.adapter;

import android.os.Build;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bizeng.lh.R;
import com.bizeng.lh.adapter.holder.ImageHolder;
import com.bizeng.lh.bean.BannerBean;
import com.bizeng.lh.utils.GlideUtils;
import com.wzq.mvvmsmart.utils.ConvertUtils;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;

/**
 * @Desc: 自定义布局，图片
 * @author: admin wsj
 * @Date: 2021/5/6 4:54 PM
 */
public class ImageAdapter extends BannerAdapter<BannerBean, ImageHolder> {

    public ImageAdapter(List<BannerBean> mDatas) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
    }

    //更新数据
    public void updateData(List<BannerBean> data) {
        //这里的代码自己发挥，比如如下的写法等等
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }


    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public ImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = (ImageView) BannerUtils.getView(parent, R.layout.item_banner);
        //通过裁剪实现圆角
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BannerUtils.setBannerRound(imageView, ConvertUtils.dp2px(6));
        }
        return new ImageHolder(imageView);
    }

    @Override
    public void onBindView(ImageHolder holder, BannerBean data, int position, int size) {
        GlideUtils.getLoad(data.imgSrc, holder.imageView)
//                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))//通过此方式不会闪烁
                .into(holder.imageView);
    }

}
