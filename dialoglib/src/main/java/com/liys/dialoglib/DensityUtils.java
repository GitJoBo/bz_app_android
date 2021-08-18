//package com.liys.dialoglib;
//
//import android.content.Context;
//import android.util.TypedValue;
//
///**
// * @Desc: 单位转换
// * @author: admin wsj
// * @Date: 2021/4/21 2:08 PM
// */
//class DensityUtils {
//    /** dp转px */
//    public static int dp2px(Context context, float dpVal){
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal, context.getResources().getDisplayMetrics());
//    }
//    public static int sp2px(Context context, float spVal){
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spVal, context.getResources().getDisplayMetrics());
//    }
//    public static float px2dp(Context context, float pxVal){
//        return (pxVal/context.getResources().getDisplayMetrics().density);
//    }
//    public static float px2sp(Context context, float pxVal){
//        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
//    }
//}
