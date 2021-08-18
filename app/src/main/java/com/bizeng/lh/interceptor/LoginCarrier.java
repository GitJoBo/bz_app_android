package com.bizeng.lh.interceptor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Desc: 如果没有登录，则把这个LoginCarrier传入到登录界面。登录成功后，触发invoke()方法
 * @author: admin wsj
 * @Date: 2021/7/20 4:55 下午
 */
public class LoginCarrier implements Parcelable {
    public String mTargetAction;
    public Bundle mBundle;

    public LoginCarrier(String target, Bundle bundle) {
        mTargetAction = target;
        mBundle = bundle;
    }

    /**
     * 目标activity
     *
     * @param ctx
     */
    public void invoke(Context ctx) {
        Intent intent = new Intent(mTargetAction);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(intent);
    }

    public LoginCarrier(Parcel parcel) {
        // 按变量定义的顺序读取
        mTargetAction = parcel.readString();
        mBundle = parcel.readParcelable(Bundle.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        // 按变量定义的顺序写入
        parcel.writeString(mTargetAction);
        parcel.writeParcelable(mBundle, flags);
    }

    public static final Parcelable.Creator<LoginCarrier> CREATOR = new Parcelable.Creator<LoginCarrier>() {

        @Override
        public LoginCarrier createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new LoginCarrier(source);
        }

        @Override
        public LoginCarrier[] newArray(int arg0) {
            // TODO Auto-generated method stub
            return new LoginCarrier[arg0];
        }
    };
}