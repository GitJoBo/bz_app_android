package com.bizeng.lh.bean;

public class AppBean {

    public String searchValue;
    public String createBy;
    public String createTime;
    public String updateBy;
    public String updateTime;
    public String remark;
    public String appId;
    public String key;
    public String version;
    public String url;

    public int getVersion() {
        try {
            return Integer.parseInt(version);
        } catch (Exception e) {
            return 1;
        }

    }
}
