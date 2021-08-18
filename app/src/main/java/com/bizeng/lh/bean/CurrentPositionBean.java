package com.bizeng.lh.bean;

import android.text.TextUtils;

public class CurrentPositionBean {
    public String name;
    public int Color;

    public CurrentPositionBean(String name, int color) {
        this.name = name;
        Color = color;
    }

    public String getName(){
        if (TextUtils.isEmpty(name)){
            return "***";
        }else {
            return name;
        }
    }
}
