package com.bizeng.lh.bean;

import java.math.BigDecimal;

public class LimitedTimeOfferBean {

    /**
     * searchValue : null
     * createBy : null
     * createTime : 2021-05-13 16:54:45
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * rechargeGiveId : 1
     * minMoney : 300
     * giveProportion : 0.1000
     */

    public String searchValue;
    public String createBy;
    public String createTime;
    public String updateBy;
    public String updateTime;
    public String remark;
    public String rechargeGiveId;
    public String minMoney;
    public String giveProportion;

    public int getGiveProportion() {
        try {
            return (int) (Double.parseDouble(giveProportion) * 100);
        } catch (Exception e) {
            return 0;
        }
    }

    public BigDecimal getMinMoneyBigDecimal() {
        return new BigDecimal(minMoney);
    }

    public BigDecimal getGiveProportionBigDecimal() {
        return new BigDecimal(giveProportion);
    }

}
