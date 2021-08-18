package com.bizeng.lh.bean;

import com.bizeng.lh.utils.BigDecimalUtils;

import java.math.BigDecimal;

public class UserWalletBean {

    public int userId;
    public String ethAddress;
    public String trxAddress;
    public BigDecimal pointMoney;
    public BigDecimal giveMoney;
    public BigDecimal profitMoney;
    public BigDecimal beforeProfitMoney;
    public BigDecimal actualProfitMoney;
    public String updateTime;

    public String getProfitMoney() {
        return getProfitMoneyBigDecimal().toString();
    }

    public BigDecimal getProfitMoneyBigDecimal() {
        return BigDecimalUtils.getInstance().getBigDecimal(profitMoney);
//        return profitMoney.setScale(2, RoundingMode.DOWN);
    }

    public String getBeforeProfitMoney() {
        return BigDecimalUtils.getInstance().getBigDecimal(beforeProfitMoney).toString();
    }

    public String getActualProfitMoney() {
        return BigDecimalUtils.getInstance().getBigDecimal(actualProfitMoney).toString();
    }
}
