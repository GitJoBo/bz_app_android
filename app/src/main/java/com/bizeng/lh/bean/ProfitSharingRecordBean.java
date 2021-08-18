package com.bizeng.lh.bean;

import java.math.BigDecimal;

public class ProfitSharingRecordBean {

    public String searchValue;
    public String createBy;
    public String createTime;
    public String updateBy;
    public String updateTime;
    public String remark;
    public String id;
    public String userId;
    public String fromId;
    public String fromNickName;
    public BigDecimal money;
    public String gradations;
    public int profitType;//类型：0-预估返利；1-到账返利

    public String getPropertyValue() {
        if (profitType == 0) {
            return "预估返利";
        } else {
            return "到账返利";
        }
    }
}
