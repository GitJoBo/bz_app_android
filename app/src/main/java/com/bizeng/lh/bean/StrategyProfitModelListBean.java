package com.bizeng.lh.bean;

import com.bizeng.lh.utils.BigDecimalUtils;

import java.math.BigDecimal;

public class StrategyProfitModelListBean {
    /**
     * strategyName : HT低买高卖策略
     * total : 1101.0604
     * endTime : 2021-05-11
     */

    public String userStrategyId;
    public String strategyName;
    public BigDecimal total;
    public BigDecimal today;
    public String endTime;

    public BigDecimal getTotal() {
        return BigDecimalUtils.getInstance().getBigDecimal(total);
    }
    public BigDecimal getToday() {
        return BigDecimalUtils.getInstance().getBigDecimal(today);
    }
}
