package com.bizeng.lh.http;

import rxhttp.wrapper.annotation.DefaultDomain;

public interface Api {

    @DefaultDomain //设置为默认域名
            String baseUrl = "https://47.119.173.204/";
    String LOGIN = "app/api/login";
    //登录校验
    String LOGIN_CHECK = "app/api/login/check";
    //设置邀请码 invitationCode
    String SET_INVITATION_CODE = "app/api/login/";
    String LOGIN_CODE = "app/api/code/";
    String DOMAIN = baseUrl;
    //用户api列表
    String USER_API_LIST = "app/api/userApi/list";
    //保存api信息结果
    String SAVE_USER_API = "app/api/userApi";
    //服务ip
    String GET_SERVICE_IP = "app/api/userApi/getIp";
    String GET_FEI_XIAO_HAO = "app/api/userStrategy/agentGetFeiXiaoHao";
    //精选策略列表 AI量化策略
    String STRATEGY_LIST = "app/api/strategy/list";

    //首页接口
    String HOME = "";
    //添加策略
    String ADD_STRATEGY = "app/api/strategy/add";
    //{userStrategyId} 删除策略
    String DEL_STRATEGY = "app/api/userStrategy/del/";
    //{userStrategyId} 重置策略
    String RESET_STRATEGY = "app/api/userStrategy/reset/";
    ///开启策略
    String START_STRATEGY = "app/api/userStrategy/start";
    //修改策略容量
    String MODIFY_WAREHOUSE_CAPACITY = "app/api/userStrategy/update";

    //{userStrategyId}    暂停策略
    String STOP_STRATEGY = "app/api/userStrategy/stop/";
    //策略中心列表
    String STRATEGY_CENTER_LIST = "app/api/userStrategy/list";

    //首页
    String HOME_PAGE = "app/api/index";

    //资讯列表
    String NEWS_LIST = "app/api/information";
    //资讯帮助
    String INFORMATION_HELP = "app/api/information/";

    // /api/userStatisticsProfit/{apiId} 收益统计
    String INCOME_STATISTICS = "app/api/userStatisticsProfit/";
    //排行榜
    String LEADER_BOARD = "app/api/userStatisticsProfit/rankingList";
    // /api/userStatisticsProfit/tables 收益趋势
    String EARNINGS_TREND = "app/api/userStatisticsProfit/tables";

    //团队信息 /api/userTeam
    String USER_TEAM = "app/api/userTeam";

    //获取vip等级
    String VIP = "app/api/vip";
    //用户信息
    String USER_INFO = "app/api/userInfo";

    //获取钱包信息
    String WALLET_INFORMATION = "app/api/userWallet";
    //获取赠点列表
    String ADDED_POINT_LIST = "app//api/userWallet/selectGiveList";
    //增上加赠 比例
    String ADDED_POINT_ADD_BONUS = "app/api/userWallet/selectExchangeProportion";
    //收支明细列表
    String LIST_OF_INCOME_AND_EXPENDITURE_DETAILS = "app//api/userWallet/selectRecordList";

    //获取主流币行情
    String MAINSTREAM_CURRENCY_MARKET = "https://fxhapi.feixiaohao.com/public/v1/ticker?limit=10&convert=CNY";
    //获取cny u转换比例 usdt_price_cny
    String USDT_PRICE_CNY = "https://dncapi.bqrank.net/api/home/global?webp=1";
    //币增排行榜地址
    String COIN_INCREASE_LEADERBOARD_ADDRESS = baseUrl + "file/statics/register/index.html#/pages/ranking/ranking";

    //获取客户端版本号
    String APP_INFO = "app//api/appInfo/";

    //发送当前登陆手机验证码
    String SEND_VERIFICATION_CURRENT_PHONE = "app/api/code";
    //发送更换手机验证码 app/api/code/changeCode/{phone}
    String SEND_REPLACEMENT_PHONE_VERIFICATION_CODE = "app/api/code/changeCode/";
    //校验旧手机验证码 app/api/login/checkOldCode/{code}
    String VERIFY_OLD_PHONE_VERIFICATION_CODE = "app/api/login/checkOldCode/";
    //更换手机
    String CHANGE_PHONE = "app/api/login/change";
    //data null测试
    String CHECK_NULL = "app/api/login/checkNull";
    //用户策略信息
    String USER_POLICY_INFORMATION = "app/api/userStrategy/";

    //公告列表
    String NOTICE_LIST = "app/api/information/noticeList";

    //收益
    //获取钱包信息
    String USER_WALLET = "app/api/userWallet";
    //兑换
    String WITHDRAW = "app/api/withdrawal";
    //兑换手续费
    String WITHDRAWAL_CONFIG = "app/api/withdrawal/config";
    //发送兑换验证码
    String WITHDRAWAL_CODE = "app/api/code/withdrawalCode";
    //历史兑换地址
    String WITHDRAWAL_ADDRESS_LIST = "app/api/withdrawal/selectAddress/";
    //兑换记录
    String WITHDRAWAL_RECORD_LIST = "app/api/withdrawal/list";
    //返利记录
    String USER_PROFIT_LIST = "app/api/userProfit/list";
    //兑换点卡
    String REDEEM_CARD = "app/api/withdrawal/exchange";
    //兑换点卡验证码
    String REDEEM_CARD_CODE = "app/api/code/exchangeCode";


}
