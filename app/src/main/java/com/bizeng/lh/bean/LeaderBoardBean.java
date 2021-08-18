package com.bizeng.lh.bean;

import android.text.TextUtils;

public class LeaderBoardBean {

    public String nickName;
    public double money;

    public String getNickName() {
        if (TextUtils.isEmpty(nickName)) {
            return "****";
        } else {
            return nickName;
        }
    }
}
