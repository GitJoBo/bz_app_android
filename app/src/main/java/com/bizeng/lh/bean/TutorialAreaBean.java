package com.bizeng.lh.bean;

import android.text.TextUtils;

import com.bizeng.lh.http.Api;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 资讯 教程
 */
public class TutorialAreaBean implements MultiItemEntity {
    public static final int TYPE = 0;//资讯无图
    public static final int TYPE_IMG = 1;//资讯有图

    /**
     * searchValue :
     * createBy : admin
     * createTime : 2021-05-07 15:16:57
     * updateBy :
     * updateTime :
     * remark :
     * informationId : 5
     * informationType : 0
     * isRecommend : 0
     * title : 安全保障
     * coverSrc :
     * informationAbstract : 安全保障
     * conHref : /file/statics/2021/05/07/3ca6b029-aa74-4dd5-bc03-ecde7b44eb93/index.html
     * userGroups : 1
     */

    public String searchValue;
    public String createBy;
    public String createTime;
    public String updateBy;
    public String updateTime;
    public String remark;
    public int informationId;
    public int informationType;
    public int isRecommend;
    public String title;
    public String coverSrc;
    public String informationAbstract;
    public String conHref;
    public String userGroups;

    public String getUrl() {
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

    @Override
    public int getItemType() {
        return TextUtils.isEmpty(coverSrc) ? 0 : 1;
    }
}
