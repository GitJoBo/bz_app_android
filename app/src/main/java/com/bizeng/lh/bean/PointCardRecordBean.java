package com.bizeng.lh.bean;

import android.text.TextUtils;

import com.bizeng.lh.utils.BigDecimalUtils;

import java.math.BigDecimal;

public class PointCardRecordBean {

    /**
     * searchValue : null
     * createBy : null
     * createTime : 2021-05-14 15:03:53
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * recordId : 9
     * recordTag : 充点
     * recordType : 0  //0充币 1 增送 2 扣点
     * userId : 3
     * money : 11.0000
     * afterMoney : 33.0000
     * expenditureType : 1
     * description : 充币
     * objectId : 8
     */

    public String searchValue;
    public String createBy;
    public String createTime;
    public String updateBy;
    public String updateTime;
    public String remark;
    public String recordId;
    public String recordTag;
    public String recordType;
    public String userId;
    public BigDecimal money;
    public String afterMoney;
    public String expenditureType;
    public String description;
    public String objectId;

    public String getRecordType() {
        if (TextUtils.isEmpty(recordType)) {
            return "充点";
        }
        switch (recordType) {
//            case "0":
//                return "充币";
            case "1":
                return "增送";
            case "2":
                return "扣点";
            default:
                return "充点";
        }
    }

    public BigDecimal getMoney() {
        return BigDecimalUtils.getInstance().getBigDecimal(money);
    }
}
