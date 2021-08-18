package com.bizeng.lh.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: banner 实体
 * @author: admin wsj
 * @Date: 2021/5/6 5:44 PM
 */
public class ImageBean {
    public Integer imageRes;
    public String imageUrl;
    public String title;
    public int viewType;

    public ImageBean(Integer imageRes, String title, int viewType) {
        this.imageRes = imageRes;
        this.title = title;
        this.viewType = viewType;
    }

    public ImageBean(String imageUrl, String title, int viewType) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.viewType = viewType;
    }

    /**
     * 仿淘宝商品详情第一个是视频
     * @return
     */
    public static List<ImageBean> getTestDataVideo() {
        List<ImageBean> list = new ArrayList<>();
//        list.add(new ImageBean("http://vfx.mtime.cn/Video/2019/03/09/mp4/190309153658147087.mp4", "第一个放视频", 2));
        list.add(new ImageBean("https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg", null, 1));
        list.add(new ImageBean("https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg", null, 1));
        list.add(new ImageBean("https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg", null, 1));
        list.add(new ImageBean("https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg", null, 1));
        list.add(new ImageBean("https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg", null, 1));
        return list;
    }

}
