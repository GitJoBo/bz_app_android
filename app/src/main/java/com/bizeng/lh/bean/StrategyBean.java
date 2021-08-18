package com.bizeng.lh.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.bizeng.lh.http.Api;
import com.bizeng.lh.utils.BigDecimalUtils;

import java.math.BigDecimal;

public class StrategyBean implements Parcelable {

    /**
     * searchValue : null
     * createBy : null
     * createTime : 2021-04-26 14:46:50
     * updateBy : null
     * updateTime : null
     * remark : 投资复合策略
     * params : {}
     * strategyId : 8
     * title : 策略1
     * strategyConfig : {}
     * proposalCapacity : full
     * userGroups : 1,2
     * tag : ["稳健型","收益高"]
     * annualized : 103%
     * url :
     * isHot : 1
     * isNew : 1
     * isRecommend : 0 //是否推荐
     * useNumber : 0 //使用人数
     */

    public String searchValue;
    public String createBy;
    public String createTime;
    public String updateBy;
    public String updateTime;
    public String remark;
    public String strategyId;
    public String title;
    public String strategyConfig;
    public String proposalCapacity;//建议仓库容量
    public String userGroups;
    public String tag;
    public String annualized;
    private String url;
    private String isHot;
    private String isNew;
    private String isRecommend;
    private String useNumber;

    //策略详情
    public String userStrategyId;
    public String userId;
    public String strategyTitle;
    public String apiId;
    public String capacity;//仓位容量
    public String status;//状态：0-未启动；1-启动；2-暂停
    public String stopDesc;
    public String delFlag;
    public String startTime;
    public String stopTime;
    public String resetTime;
    public BigDecimal profit;//今日收益
    /**
     * 停止状态：
     * 0-手动暂停；
     * 1-api错误；
     * 2-欠费；
     * 3-未知错误
     */
    public Integer stopStatus;

    public BigDecimal getProfit() {
        return BigDecimalUtils.getInstance().getBigDecimal(profit);
    }

    public int getStopStatusValue() {
        if (stopStatus == null) {
            return 3;
        }
        return stopStatus;
    }


    public boolean getIsHot() {
        return "1".equals(isHot);
    }

    public boolean getIsNew() {
        return "1".equals(isNew);
    }

    public boolean getIsRecommend() {
        return "1".equals(isRecommend);
    }

    public String getUseNumber() {
        if (TextUtils.isEmpty(useNumber)) {
            return "0";
        }
        return useNumber;
    }

    public void setUseNumber(String useNumber) {
        this.useNumber = useNumber;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public void setIsRecommend(String isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getUrl() {
        if (!TextUtils.isEmpty(url)) {
            if (!url.startsWith("http")) {
                while (url.startsWith("/")) {
                    url = url.substring(1);
                }
                return String.format("%s%s", Api.DOMAIN, url);
            }
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCapacityValue() {
        if ("full".equals(capacity)) {
            return "全仓仓位";
        } else if ("default".equals(capacity)) {
            return "智能仓位";
        } else if (TextUtils.isEmpty(capacity)) {
            return "0.00U";
        } else {
            return BigDecimalUtils.getInstance().getBigDecimal(new BigDecimal(capacity)) + "U";
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.searchValue);
        dest.writeString(this.createBy);
        dest.writeString(this.createTime);
        dest.writeString(this.updateBy);
        dest.writeString(this.updateTime);
        dest.writeString(this.remark);
        dest.writeString(this.strategyId);
        dest.writeString(this.title);
        dest.writeString(this.strategyConfig);
        dest.writeString(this.proposalCapacity);
        dest.writeString(this.userGroups);
        dest.writeString(this.tag);
        dest.writeString(this.annualized);
        dest.writeString(this.url);
        dest.writeString(this.isHot);
        dest.writeString(this.isNew);
        dest.writeString(this.isRecommend);
        dest.writeString(this.useNumber);
    }

    public void readFromParcel(Parcel source) {
        this.searchValue = source.readString();
        this.createBy = source.readString();
        this.createTime = source.readString();
        this.updateBy = source.readString();
        this.updateTime = source.readString();
        this.remark = source.readString();
        this.strategyId = source.readString();
        this.title = source.readString();
        this.strategyConfig = source.readString();
        this.proposalCapacity = source.readString();
        this.userGroups = source.readString();
        this.tag = source.readString();
        this.annualized = source.readString();
        this.url = source.readString();
        this.isHot = source.readString();
        this.isNew = source.readString();
        this.isRecommend = source.readString();
        this.useNumber = source.readString();
    }

    public StrategyBean() {
    }

    protected StrategyBean(Parcel in) {
        this.searchValue = in.readString();
        this.createBy = in.readString();
        this.createTime = in.readString();
        this.updateBy = in.readString();
        this.updateTime = in.readString();
        this.remark = in.readString();
        this.strategyId = in.readString();
        this.title = in.readString();
        this.strategyConfig = in.readString();
        this.proposalCapacity = in.readString();
        this.userGroups = in.readString();
        this.tag = in.readString();
        this.annualized = in.readString();
        this.url = in.readString();
        this.isHot = in.readString();
        this.isNew = in.readString();
        this.isRecommend = in.readString();
        this.useNumber = in.readString();
    }

    public static final Parcelable.Creator<StrategyBean> CREATOR = new Parcelable.Creator<StrategyBean>() {
        @Override
        public StrategyBean createFromParcel(Parcel source) {
            return new StrategyBean(source);
        }

        @Override
        public StrategyBean[] newArray(int size) {
            return new StrategyBean[size];
        }
    };
}
