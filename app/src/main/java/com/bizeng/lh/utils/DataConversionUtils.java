package com.bizeng.lh.utils;

import com.wzq.mvvmsmart.utils.KLog;

import java.math.BigDecimal;

/**
 * @Desc:  数字转换 千、万、k、w、9999w+
 * @author: admin wsj
 * @Date: 2021/4/29 11:29 AM
 */
public class DataConversionUtils {
    /**
     * 中文显示
     */
    public static final String CH = "CH";
    /**
     * 拼音显示
     */
    public static final String PY = "PY";

    private static final Long THOUSAND = 1000L;
    private static final Long TEN_THOUSAND = 10000L;
    private static final Long ONE_HUNDRED_MILLION = 100000000L;

    /**
     * 数据转换
     *
     * @param temp
     *            需要转换的数据 支持Long、BigDecimal、Integer、String、int、long类型
     * @param type
     *            需要转换的方式 RelativeNumberFormatTool.CH:中文显示 RelativeNumberFormatTool.PY:拼音显示
     * @return
     */
    public static String dataConversion(Object temp, String type) {
        Long num = numberFormat(temp);
        if (null == num) {
            return temp + "";
        }
        if (type.equals(PY)) {
            if (num.compareTo(ONE_HUNDRED_MILLION) == 1 || num.compareTo(ONE_HUNDRED_MILLION) == 0) {
                return "9999w+";
            }
            if (num.compareTo(TEN_THOUSAND) == 1 || num.compareTo(TEN_THOUSAND) == 0) {
                return num / TEN_THOUSAND + "w+";
            }
            if (num.compareTo(THOUSAND) == 1 || num.compareTo(THOUSAND) == 0) {
                return num / THOUSAND + "k+";
            }
        } else if (type.equals(CH)) {
            if (num.compareTo(ONE_HUNDRED_MILLION) == 1 || num.compareTo(ONE_HUNDRED_MILLION) == 0) {
                return "9999万+";
            }
            if (num.compareTo(TEN_THOUSAND) == 1 || num.compareTo(TEN_THOUSAND) == 0) {
                return num / TEN_THOUSAND + "万+";
            }
            if (num.compareTo(THOUSAND) == 1 || num.compareTo(THOUSAND) == 0) {
                return num / THOUSAND + "千+";
            }
        }
        return num + "";
    }

    /**
     * 格式化数据为Long类型
     */
    public static Long numberFormat(Object number) {
        if (number != null && !"".equals(number)) {
            if (number instanceof BigDecimal) {
                return ((BigDecimal)number).longValue();
            }
            if (number instanceof Integer) {
                return ((Integer)number).longValue();
            }
            if (number instanceof Long) {
                return (Long)number;
            }
            if (number instanceof String) {
                try {
                    return Long.valueOf(number + "");
                } catch (Exception e) {
                    KLog.e("字符串数字转换失败，请检查！" + e.getMessage(), e);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal(103);
        int c = 6666;
        Integer c1 = 6000000;
        String str = "6000";
        long bb = 16544l;
        Long bb1 = 9924l;

        System.out.println(dataConversion(a, CH));
        System.out.println(dataConversion(c, CH));
        System.out.println(dataConversion(c1, CH));
        System.out.println(dataConversion(str, CH));
        System.out.println(dataConversion(bb, CH));
        System.out.println(dataConversion(bb1, CH));
        System.out.println(dataConversion(a, PY));
        System.out.println(dataConversion(c, PY));
        System.out.println(dataConversion(c1, PY));
        System.out.println(dataConversion(str, PY));
        System.out.println(dataConversion(bb, PY));
        System.out.println(dataConversion(bb1, PY));
    }
}
