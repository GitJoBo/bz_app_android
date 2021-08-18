package com.bizeng.lh.utils;

public class StringUtils {

    /**
     * @param str      目标字符串
     * @param indexStr 要查询的字符串
     * @param num      第几次
     * @return
     */
    public static int getIndex(String str, String indexStr, int num) {
        int rtn = -1;
        String[] temp = str.split(indexStr);
        if (temp.length > num) {
            for (int i = 0; i < num; i++) {
                rtn += temp[i].length() + 1;
            }
        }
        return rtn;
    }
}