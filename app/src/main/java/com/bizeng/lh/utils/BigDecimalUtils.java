package com.bizeng.lh.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BigDecimalUtils {
    private static volatile BigDecimalUtils instance = null;

    private BigDecimalUtils() {
    }

    public static BigDecimalUtils getInstance() {
        if (instance == null) {
            synchronized (BigDecimalUtils.class) {
                if (instance == null) {
                    instance = new BigDecimalUtils();
                }
            }
        }
        return instance;
    }

    public static final int MONEY_POINT = 2; // 货币保留两位小数

    /**
     * 格式化精度
     *
     * @param v
     * @param point 小数位数
     * @return double
     */
    public Double format(double v, int point) {
        BigDecimal b = new BigDecimal(v);
        return b.setScale(point, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * @param v
     * @param point
     * @return
     */
    public Double formatRoundUp(double v, int point) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setRoundingMode(RoundingMode.HALF_UP);//设置四舍五入
        nf.setMinimumFractionDigits(point);//设置最小保留几位小数
        nf.setMaximumFractionDigits(point);//设置最大保留几位小数
        return Double.valueOf(nf.format(v));
    }

    /**
     * @param v
     * @param point
     * @return
     */
    public Double formatRoundUp(String v, int point) {
        double v1 = Double.parseDouble(v);
        return formatRoundUp(v1, point);
    }

    /**
     * 保留两位数
     *
     * @param v
     * @return
     */
    public Double formatRoundUp(String v) {
        double v1 = Double.parseDouble(v);
        return formatRoundUp(v1, MONEY_POINT);
    }

    /**
     * 格式化金额。带千位符
     *
     * @param v
     * @return
     */
    public String moneyFormat(Double v) {
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(2);
        formater.setGroupingSize(3);
        formater.setRoundingMode(RoundingMode.FLOOR);
        return formater.format(v.doubleValue());
    }

    /**
     * 格式化金额。带千位符
     *
     * @param v
     * @return
     */
    public String moneyFormat(String v) {
        double v1 = Double.parseDouble(v);
        return moneyFormat(v1);
    }

    /**
     * 带小数的显示小数。不带小数的显示整数
     *
     * @param d
     * @return
     */
    public String doubleTrans(Double d) {
        if (Math.round(d) - d == 0) {
            return String.valueOf((long) d.doubleValue());
        }
        return String.valueOf(d);
    }

    /**
     * BigDecimal 相加
     *
     * @param v1
     * @param v2
     * @return double
     */
    public Double add(double v1, double v2) {
        return add(Double.toString(v1), Double.toString(v2));
    }

    /**
     * BigDecimal 相加
     *
     * @param v1
     * @param v2
     * @return double
     */
    public Double add(String v1, String v2) {
        BigDecimal n1 = new BigDecimal(v1);
        BigDecimal n2 = new BigDecimal(v2);
        return n1.add(n2).doubleValue();
    }

    /**
     * BigDecimal 相减
     *
     * @param v1
     * @param v2
     * @return double
     */
    public Double subtract(double v1, double v2) {
        return subtract(Double.toString(v1), Double.toString(v2));
    }

    /**
     * BigDecimal 相减
     *
     * @param v1
     * @param v2
     * @return double
     */
    public Double subtract(String v1, String v2) {
        BigDecimal n1 = new BigDecimal(v1);
        BigDecimal n2 = new BigDecimal(v1);
        return n1.subtract(n2).doubleValue();
    }

    /**
     * BigDecimal 相乘
     *
     * @param v1
     * @param v2
     * @return double
     */
    public Double multiply(double v1, double v2) {
        return multiply(Double.toString(v1), Double.toString(v2));
    }

    /**
     * BigDecimal 相乘
     *
     * @param v1
     * @param v2
     * @return double
     */
    public Double multiply(String v1, String v2) {
        BigDecimal n1 = new BigDecimal(v1);
        BigDecimal n2 = new BigDecimal(v2);
        return n1.multiply(n2).doubleValue();
    }

    /**
     * BigDecimal 相除
     *
     * @param v1
     * @param v2
     * @return double
     */
    public Double divide(double v1, double v2) {
        return divide(Double.toString(v1), Double.toString(v2));
    }

    /**
     * BigDecimal 相除
     *
     * @param v1
     * @param v2
     * @return double
     */
    public Double divide(String v1, String v2) {
        BigDecimal n1 = new BigDecimal(v1);
        BigDecimal n2 = new BigDecimal(v2);
        return n1.divide(n2, 10, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 比较大小 小于0：v1 < v2 大于0：v1 > v2 等于0：v1 = v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public int compare(double v1, double v2) {
        return compare(Double.toString(v1), Double.toString(v2));
    }

    /**
     * 比较大小 小于0：v1 < v2 大于0：v1 > v2 等于0：v1 = v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public int compare(String v1, String v2) {
        BigDecimal n1 = new BigDecimal(v1);
        BigDecimal n2 = new BigDecimal(v2);
        return n1.compareTo(n2);
    }

    /**
     * 返回两位，null返回0.00
     *
     * @param bigDecimal
     * @return
     */
    public BigDecimal getBigDecimal(BigDecimal bigDecimal) {
        try {
            return bigDecimal.setScale(2, RoundingMode.DOWN);
        } catch (Exception e) {
            return new BigDecimal("0.00").setScale(2, RoundingMode.DOWN);
        }
    }/**
     * 返回两位，null返回0.00
     *
     * @param str
     * @return
     */
    public BigDecimal getBigDecimal(String str) {
        try {
            BigDecimal bigDecimal = new BigDecimal(str);
            return bigDecimal.setScale(2, RoundingMode.DOWN);
        } catch (Exception e) {
            return new BigDecimal("0.00").setScale(2, RoundingMode.DOWN);
        }
    }

//    /**
//     * @param args
//     */
//    public static void main(String[] args) {
//
//        //前提为a、b均不能为null
//        if (a.compareTo(b) == -1) {
//            System.out.println("a小于b");
//        }
//
//        if (a.compareTo(b) == 0) {
//            System.out.println("a等于b");
//        }
//
//        if (a.compareTo(b) == 1) {
//            System.out.println("a大于b");
//        }
//
//        if (a.compareTo(b) > -1) {
//            System.out.println("a大于等于b");
//        }
//
//        if (a.compareTo(b) < 1) {
//            System.out.println("a小于等于b");
//        }
//        System.out.println(BigDecimalUtils.getInstance().divide(1, 8));
//        System.out.println(BigDecimalUtils.getInstance().format(multiply(3.55, 2.44), 2));
//        System.out.println(BigDecimalUtils.getInstance().divide(1.0, 3.0));
//        System.out.println(BigDecimalUtils.getInstance().add(2.79, -3.0));
//        System.out.println(BigDecimalUtils.getInstance().doubleTrans(10000.0));
//    }
}
