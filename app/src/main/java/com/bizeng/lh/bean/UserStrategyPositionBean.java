package com.bizeng.lh.bean;

import com.bizeng.lh.utils.BigDecimalUtils;

import java.math.BigDecimal;

public class UserStrategyPositionBean {
    public int userPositionId;
    public int userId;
    public int userStrategyId;
    public String symbolName;
    private BigDecimal positionUsdt;
    public String createTime;
    public Object updateTime;

    public BigDecimal getPositionUsdt(){
        return BigDecimalUtils.getInstance().getBigDecimal(positionUsdt);
    }

    public void setPositionUsdt(BigDecimal positionUsdt) {
        this.positionUsdt = positionUsdt;
    }
}
