package com.bizeng.lh.bean;

import com.bizeng.lh.utils.BigDecimalUtils;
import com.bizeng.lh.utils.DateUtils;

import java.math.BigDecimal;
import java.util.List;

public class UserStrategyBean {

    public int userStrategyId;
    public int status;//状态：0-未启动；1-启动；2-暂停
    public String startTime;
    public String strategyTitle;//策略标题
    public String stopTime;//暂停时间
    public String resetTime;
    public BigDecimal total;//累计币增
    public BigDecimal today;//今日币增
    public List<UserStrategyPositionBean> positions;

    public BigDecimal getTotal() {
        return BigDecimalUtils.getInstance().getBigDecimal(total);
    }

    public BigDecimal getToDay() {
        return BigDecimalUtils.getInstance().getBigDecimal(today);
    }

    public String getStartTime() {
        try {
            return startTime.substring(0, 11);
        } catch (Exception e) {
//            return "一";
            return "-";
        }
    }

    /**
     * 获取运行的时间
     *
     * @return
     */
    public String getRunDate() {
        try {
            if (status == 2) {
//            return "已暂停";
                return DateUtils.getInstance().getTimeDifferenceNo0(startTime, stopTime, 0) + "天";
            } else if (status == 1) {
                return DateUtils.getInstance().getTimeDifferenceNo0(startTime, DateUtils.getInstance().getNowTime(), 0) + "天";
            } else {
                return "-";
            }
        } catch (Exception e) {
            return "-";
        }
    }
}
