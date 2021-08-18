package com.liys.dialoglib;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

/**
 * @Desc: 背景属性
 * @author: admin wsj
 * @Date: 2021/4/21 2:08 PM
 */
public class BgBean {

    public int color; //背景颜色
    public float left_top_radius; //
    public float right_top_radius; //
    public float right_bottom_radius; //
    public float left_bottom_radius; //

    //渐变方向
    public GradientDrawable.Orientation gradientsOrientation = GradientDrawable.Orientation.TOP_BOTTOM;
    public int[] gradientsColors; //渐变颜色

    public GradientDrawable getRoundRectDrawable(){
        //1. 圆角
        float[] radiuss = new float[]{ //左上、右上、右下、左下的圆角半径
                left_top_radius, left_top_radius,
                right_top_radius, right_top_radius,
                right_bottom_radius, right_bottom_radius,
                left_bottom_radius, left_bottom_radius};;
        //2.渐变
        GradientDrawable drawable;
        if(gradientsColors==null){
            drawable = new GradientDrawable();
            drawable.setColor(color!=0 ? color : Color.TRANSPARENT);
        }else{
            drawable = new GradientDrawable(gradientsOrientation, gradientsColors);
        }
        drawable.setCornerRadii(radiuss);
        return drawable;
    }
}
