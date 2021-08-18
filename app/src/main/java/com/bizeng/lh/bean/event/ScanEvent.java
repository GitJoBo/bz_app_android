package com.bizeng.lh.bean.event;

/**
 * @Desc: 扫码结果通知
 * @author: admin wsj
 * @Date: 2021/6/30 3:56 PM
 */
public class ScanEvent<T> {
    public T data;
    public int code;//0 兑换二维码扫描
}
