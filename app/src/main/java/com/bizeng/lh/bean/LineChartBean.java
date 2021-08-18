package com.bizeng.lh.bean;

/**
 * @Desc: 折线图bean
 * @author: admin wsj
 * @Date: 2021/5/12 9:45 AM
 */
public class LineChartBean {

    /**
     * searchValue : null
     * createBy : null
     * createTime : 2021-05-10 00:00:00
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * statisticsId : 1
     * apiTag : hb
     * userId : 4
     * apiId : 5
     * userStrategyId : 79
     * money : 66.6666
     * strategyId : 20
     */

    public String searchValue;
    public String createBy;
    public String createTime;
    public String updateBy;
    public String updateTime;
    public String remark;
    public String statisticsId;
    public String apiTag;
    public String userId;
    public String apiId;
    public String userStrategyId;
    public float money;
    public String strategyId;

    public String getCreateTime() {
        try {
            return createTime.substring(0, 10).replace("-", ".");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
