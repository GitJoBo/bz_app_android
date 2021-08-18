package com.bizeng.lh.bean;

import android.text.TextUtils;

import com.bizeng.lh.http.Api;

import rxhttp.wrapper.utils.GsonUtil;

public class BannerBean {
    public static String STRATEGY = "strategy";


    /**
     * searchValue :
     * createBy :
     * createTime : 2021-05-07 11:26:51
     * updateBy :
     * updateTime :
     * remark :
     * id : 2
     * imgSrc : /file/statics/2021/05/07/14c3ff6b-f68e-4e25-91c0-883d1ae20c07.png
     * conHref : https://baidu.com
     * orderNum : 1
     * expand : {"type":"strategy","strategyId":1}
     */

    public String searchValue;
    public String createBy;
    public String createTime;
    public String updateBy;
    public String updateTime;
    public String remark;
    public int id;
    public String imgSrc;
    public String conHref;
    public int orderNum;
    public String expand;//扩展字段

    public BannerBean(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getConHref() {
        if (!TextUtils.isEmpty(conHref)) {
            if (!conHref.startsWith("http")) {
                while (conHref.startsWith("/")) {
                    conHref = conHref.substring(1);
                }
                return String.format("%s%s", Api.DOMAIN, conHref);
            }
        }
        return conHref;
    }

    public BannerExpand getExpand() {
        BannerExpand bannerExpand = GsonUtil.fromJson(expand, BannerExpand.class);
        return bannerExpand;
    }
}
