package com.bizeng.lh.base;

import com.bizeng.lh.utils.HttpsUtils;
import com.bizeng.lh.utils.MMKVUtils;

public class Content {
    public static final String PARAMS_TITLE = "title";
    public static final String PARAMS_URL = "url";
    public static final String KEY = "key";
    public static final String TYPE = "type";
    public static final String REN_MIN_BI = "¥";
    public static final int ACTIVITY_REQUEST_CODE = 1000;
    //默认等待超时时间
    public static final int DEFAULT_WAITING_TIMEOUT = HttpsUtils.DEFAULT_TIMEOUT;

    /**
     * 分页默认参数
     */
    public static final int PAGE_NUM = 1;
    public static final int PAGE_SIZE = 20;
    public static final int PAGE_SIZE_MAX = 1000;

    /**
     * 币种
     */
    public static final String API_TAG_HB = "hb";
    public static final String API_TAG_BAN = "biance";
    public static final String API_TAG_OKEX = "okex";

    /**
     * 教程title
     */
    public static final String QUANTITATIVE_INTRODUCTION = "量化介绍";
    //    public static final String DEPOSIT_TUTORIAL_GUIDE = "充币教程指南";
    public static final String BUY_COINS_GUIDE = "买币指导";
    //    public static final String BUY_COINS_GUIDE = "兑换点卡教程指南";
    public static final String SECURITY = "安全保障";
    public static final String HUO_BI_API_SETTING_TUTORIAL = "火币API设置教程";
    public static final String BIN_AN_API_SETUP_TUTORIAL = "币安API设置教程";
    public static final String OKEX_API_SETUP_TUTORIAL = "OKEXAPI设置教程";
    public static final String HOW_TO_BECOME_A_GOLD_CARD = "如何成为金卡";
    public static final String CONTACT_CUSTOMER_SERVICE = "联系客服";
    public static final String DISCLAIMER = "免责申明";
    public static final String USER_AGREEMENT = "用户协议";
    public static final String PRIVACY_POLICY = "隐私政策";

    /**
     * 默认弹框宽度，屏幕百分比  0-1
     */
    public static final Double WIDTH_RATIO = 0.84;

    /**
     * u->cny转化比例 从行情列表遍历拿USDT的price_cny
     */
    public static Double U2CNY = MMKVUtils.getInstance().getU2cny();

    /**
     * 链类型
     */
    public static String TRC20 = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";
}
