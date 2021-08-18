package com.bizeng.lh.utils;

import java.math.BigDecimal;

public class ViewPager2Utils {
    private volatile static ViewPager2Utils instance = null;

    private ViewPager2Utils() {
    }

    public static ViewPager2Utils getInstance() {
        if (instance == null) {
            synchronized (ViewPager2Utils.class) {
                if (instance == null) {
                    instance = new ViewPager2Utils();
                }
            }
        }
        return instance;
    }

    /**
     * viewPager是否执行动画
     * @param currentItem 当前页面
     * @param item 切换的页面
     * @return false不执行动画，true执行动画
     */
    public boolean isAnimation(int currentItem,int item) {
        BigDecimal currentValue = new BigDecimal(currentItem);
        BigDecimal itemValue = new BigDecimal(item);
        BigDecimal subtract = currentValue.subtract(itemValue);
        if (subtract.abs().intValue() != 1) {
            //去除页面切换时的滑动翻页效果
           return false;
        } else {
            return true;
        }
    }
}
