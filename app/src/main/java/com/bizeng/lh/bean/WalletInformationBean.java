package com.bizeng.lh.bean;

import com.bizeng.lh.utils.BigDecimalUtils;

import java.math.BigDecimal;

/**
 * @Desc: 钱包信息
 * @author: admin wsj
 * @Date: 2021/5/14 2:39 PM
 */
public class WalletInformationBean {

    /**
     * ethAddress	string
     * 以太坊地址
     * pointMoney	number
     * 点数
     * profitMoney	number
     * 返利
     * trxAddress	string
     * 波场币地址
     * updateTime	string($date-time)
     * 更新时间
     */

    public String userId;
    public String ethAddress;
    public String trxAddress;
    public BigDecimal pointMoney;
    public BigDecimal giveMoney;
    public BigDecimal profitMoney;
    public String updateTime;

    public BigDecimal getPointMoney() {
        return BigDecimalUtils.getInstance().getBigDecimal(pointMoney);
    }

    public BigDecimal getAllMoney() {
        return BigDecimalUtils.getInstance().getBigDecimal(pointMoney.add(giveMoney));
    }
}
