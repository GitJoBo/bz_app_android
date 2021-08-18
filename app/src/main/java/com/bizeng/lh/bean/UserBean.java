package com.bizeng.lh.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.bizeng.lh.utils.BigDecimalUtils;

import java.math.BigDecimal;
import java.util.List;

public class UserBean implements Parcelable {

    /**
     * userId : 4
     * phone : 13349169496
     * token : 79c18106-dcc6-46e5-ab21-eed9c8ad4e35
     * first : 0 //首次
     * loginTime : 2021-04-23
     * apis : null
     * userGroups : null
     */

    public String userId;
    public String headImg;
    public String phone;
    public String recommendStatus;//0可设置，1过期，2已设置
    public String token;
    public String first;
    public String loginTime;
    public List<ApiBean> apis;
    public String userGroups;
    public String invitationCode;//邀请码
    public String vipIntegral;//vip积分
    public String vipLevel;//vip等级
    public String nickName;
    public boolean firstRechargePoint;//首充送点数
    public boolean firstRechargeIntegral;//首充满100送积分
    public BigDecimal actualProfitMoney;//实际到账返利
    public BigDecimal beforeProfitMoney;//预估返利


    public boolean getIsFirst() {
        return "1".equals(first);
    }

    /**
     * 手机后四位
     *
     * @return
     */
    public String getPhoneX() {
        try {
            return phone.substring(phone.length() - 4);
        } catch (Exception e) {
            return "xxxx";
        }
    }

    /**
     * 到账率
     *
     * @return
     */
    public String getArrivalRate() {
        try {
            return actualProfitMoney.multiply(BigDecimal.valueOf(100)).divide(beforeProfitMoney, 0, BigDecimal.ROUND_HALF_UP).toString() + "%";
        } catch (Exception e) {
            return "0%";
        }
    }

    public String getActualProfitMoney() {
        return BigDecimalUtils.getInstance().getBigDecimal(actualProfitMoney).toString();
    }

    public String getBeforeProfitMoney() {
        return BigDecimalUtils.getInstance().getBigDecimal(beforeProfitMoney).toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.headImg);
        dest.writeString(this.phone);
        dest.writeString(this.token);
        dest.writeString(this.first);
        dest.writeString(this.loginTime);
        dest.writeTypedList(this.apis);
        dest.writeString(this.userGroups);
        dest.writeString(this.invitationCode);
        dest.writeString(this.vipIntegral);
        dest.writeString(this.vipLevel);
        dest.writeString(this.nickName);
        dest.writeByte(this.firstRechargePoint ? (byte) 1 : (byte) 0);
        dest.writeByte(this.firstRechargeIntegral ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.actualProfitMoney);
        dest.writeSerializable(this.beforeProfitMoney);
    }

    public void readFromParcel(Parcel source) {
        this.userId = source.readString();
        this.headImg = source.readString();
        this.phone = source.readString();
        this.token = source.readString();
        this.first = source.readString();
        this.loginTime = source.readString();
        this.apis = source.createTypedArrayList(ApiBean.CREATOR);
        this.userGroups = source.readString();
        this.invitationCode = source.readString();
        this.vipIntegral = source.readString();
        this.vipLevel = source.readString();
        this.nickName = source.readString();
        this.firstRechargePoint = source.readByte() != 0;
        this.firstRechargeIntegral = source.readByte() != 0;
        this.actualProfitMoney = (BigDecimal) source.readSerializable();
        this.beforeProfitMoney = (BigDecimal) source.readSerializable();
    }

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        this.userId = in.readString();
        this.headImg = in.readString();
        this.phone = in.readString();
        this.token = in.readString();
        this.first = in.readString();
        this.loginTime = in.readString();
        this.apis = in.createTypedArrayList(ApiBean.CREATOR);
        this.userGroups = in.readString();
        this.invitationCode = in.readString();
        this.vipIntegral = in.readString();
        this.vipLevel = in.readString();
        this.nickName = in.readString();
        this.firstRechargePoint = in.readByte() != 0;
        this.firstRechargeIntegral = in.readByte() != 0;
        this.actualProfitMoney = (BigDecimal) in.readSerializable();
        this.beforeProfitMoney = (BigDecimal) in.readSerializable();
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}
