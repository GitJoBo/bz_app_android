package com.bizeng.lh.bean;

public class VipBean {

    /**
     * searchValue : null
     * createBy : admin
     * createTime : 2021-05-11 11:58:23
     * updateBy : admin
     * updateTime : 2021-05-14 09:51:56
     * remark : null
     * params : {}
     * levelId : 1
     * levelNumber : 1
     * levelName : GV1
     * minIntegral : 100
     * deductionProportion : 0.3000
     */

    public String searchValue;
    public String createBy;
    public String createTime;
    public String updateBy;
    public String updateTime;
    public String remark;
    public String levelId;
    public String levelNumber;
    public String levelName;
    public String minIntegral;
    public String deductionProportion;

    public int getMinIntegral() {
        try {
            return Integer.parseInt(minIntegral);
        } catch (Exception e) {
            return 0;
        }

    }
}
