package com.bizeng.lh.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.bizeng.lh.utils.MapRemoveNullUtil;

/**
 * @Desc: 交易所api管理实体类
 * @author: admin wsj
 * @Date: 2021/4/25 11:58 AM
 */
public class ApiBean implements Parcelable {
    public String apiId;
    public String apiTag;
    //火币,币安
    public String access_key;
    public String secret_key;
    //欧易OKEX专用
    public String pass_key;
    //
    public String title;
    public String apiContent;//用于接收后台数据"{"access_key":"apikey","secret_key":"secretkey"}"

    public ApiBean(String apiTag, String title) {
        this.apiTag = apiTag;
        this.title = title;
    }

    public ApiBean() {

    }

    /**
     * apiContent下所有key都是空
     *
     * @return true是空，提现未添加
     */
    public boolean apiContentIsNull() {
        if (TextUtils.isEmpty(apiContent)) {
            return true;
        }
        return MapRemoveNullUtil.mapValueIsAllNull(apiContent);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.apiTag);
        dest.writeString(this.access_key);
        dest.writeString(this.secret_key);
        dest.writeString(this.pass_key);
        dest.writeString(this.title);
        dest.writeString(this.apiContent);
    }

    public void readFromParcel(Parcel source) {
        this.apiTag = source.readString();
        this.access_key = source.readString();
        this.secret_key = source.readString();
        this.pass_key = source.readString();
        this.title = source.readString();
        this.apiContent = source.readString();
    }

    protected ApiBean(Parcel in) {
        this.apiTag = in.readString();
        this.access_key = in.readString();
        this.secret_key = in.readString();
        this.pass_key = in.readString();
        this.title = in.readString();
        this.apiContent = in.readString();
    }

    public static final Creator<ApiBean> CREATOR = new Creator<ApiBean>() {
        @Override
        public ApiBean createFromParcel(Parcel source) {
            return new ApiBean(source);
        }

        @Override
        public ApiBean[] newArray(int size) {
            return new ApiBean[size];
        }
    };
}
