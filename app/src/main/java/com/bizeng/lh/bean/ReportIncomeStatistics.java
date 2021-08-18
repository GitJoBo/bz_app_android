package com.bizeng.lh.bean;

import com.bizeng.lh.utils.BigDecimalUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Desc: 收益统计
 * @author: admin wsj
 * @Date: 2021/5/11 6:20 PM
 */
public class ReportIncomeStatistics {

    /**
     * total : 1101.0604
     * today : 78.7898
     * strategyProfitModelList : [{"strategyName":"HT低买高卖策略","total":1101.0604,"endTime":"2021-05-11"}]
     */

    public BigDecimal total;
    public BigDecimal today;
    public List<StrategyProfitModelListBean> strategyProfitModelList;

    public BigDecimal getTotal() {
        return BigDecimalUtils.getInstance().getBigDecimal(total);
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getToday() {
        return BigDecimalUtils.getInstance().getBigDecimal(today);
    }

    public void setToday(BigDecimal today) {
        this.today = today;
    }
}
