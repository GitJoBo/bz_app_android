//package com.bizeng.lh.widget.view;
//
//import android.content.Context;
//import android.util.AttributeSet;
//
//import androidx.viewpager2.widget.ViewPager2;
//
//import java.math.BigDecimal;
//
///**
// * @Desc: 非相邻的取消切换动画
// * @author: admin wsj
// * @Date: 2021/5/27 10:21 AM
// */
//public class NoAnimationViewPager2 extends ViewPager2 {
//    public NoAnimationViewPager2(Context context) {
//        super(context);
//    }
//
//    public NoAnimationViewPager2(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    public void setCurrentItem(int item, boolean smoothScroll) {
//        super.setCurrentItem(item, smoothScroll);
//    }
//
//    @Override
//    public void setCurrentItem(int item) {
//        int currentItem = getCurrentItem();
//        BigDecimal currentValue = new BigDecimal(currentItem);
//        BigDecimal itemValue = new BigDecimal(item);
//        BigDecimal subtract = currentValue.subtract(itemValue);
//        if (subtract.abs().intValue() != 1) {
//            //去除页面切换时的滑动翻页效果
//            super.setCurrentItem(item, false);
//        } else {
//            super.setCurrentItem(item, true);
//        }
//
//    }
//
//}
