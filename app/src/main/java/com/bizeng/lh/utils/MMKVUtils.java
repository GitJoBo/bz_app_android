package com.bizeng.lh.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.bizeng.lh.bean.ApiBean;
import com.bizeng.lh.bean.UserBean;
import com.bizeng.lh.bean.VipBean;
import com.google.gson.reflect.TypeToken;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.tencent.mmkv.MMKV;
import com.wzq.mvvmsmart.utils.LiveEventBusKeys;
import com.wzq.mvvmsmart.utils.resource.Resource;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rxhttp.wrapper.utils.GsonUtil;

public class MMKVUtils {
    public static final String KEY_USER = "user";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_SERVICE_IP = "service_ip";
    public static final String KEY_APIS = "apis";
    public static final String KEY_VIP = "vip";
    public static final String KEY_VIP_LIST = "vip_list";
    public static final String KEY_FEI_XIAO_HAO = "fei_xiao_hao";
    public static final String U2CNY = "u2cny";
    public static final String VIP_INTEGRAL = "VipIntegral";
    public static final String AGREEMENT_HAS_BEEN_READ = "agreement_has_been_read";
    public static String[] FEI_XIAO_HAO_PROXY_VALUE = null;
    private String token = null;

    private MMKVUtils() {
    }

    private static volatile MMKVUtils instance = null;

    public static MMKVUtils getInstance() {
        if (instance == null) {
            synchronized (MMKVUtils.class) {
                if (instance == null) {
                    instance = new MMKVUtils();
                }
            }
        }
        return instance;
    }

    public MMKV getUserMMkv() {
        return MMKV.mmkvWithID(KEY_USER);
    }

    public MMKV getVipMapMMkv() {
        return MMKV.mmkvWithID(KEY_VIP);
    }

    public MMKV getVipListMMkv() {
        return MMKV.mmkvWithID(KEY_VIP_LIST);
    }

    /**
     * 缓存登录用户
     *
     * @param userBean
     */
    public void setUser(UserBean userBean) {
        getUserMMkv().encode(KEY_USER, userBean);
    }

    public void setToken(String token) {
        getUserMMkv().encode(KEY_TOKEN, token);
    }

    /**
     * 删除登录用户
     */
    public void clearUserAndToken() {
        token = null;
        getUserMMkv().remove(KEY_USER);
        getUserMMkv().remove(KEY_TOKEN);
        LiveEventBus.get(LiveEventBusKeys.LOGIN_REFRESH).post(Resource.success(null));
    }

    /**
     * 获取登录用户
     */
    public UserBean getUser() {
        return getUserMMkv().decodeParcelable(KEY_USER, UserBean.class);
    }

    /**
     * 获取自己的邀请码
     */
    public String getInvitationCode() {
        UserBean user = getUser();
        if (user != null) {
            return user.invitationCode;
        }
        return "";
    }

    /**
     * 获取手机号后四位
     *
     * @return
     */
    public String getUserPhoneLastFour() {
        String userPhone = getUserPhone();
        if (!TextUtils.isEmpty(userPhone)) {
            return userPhone.substring(userPhone.length() - 4);
        }
        return "";
    }

    /**
     * 获取手机号中间四位****
     *
     * @return
     */
    public String getUserPhoneHide() {
        String userPhone = getUserPhone();
        if (!TextUtils.isEmpty(userPhone)) {
            return userPhone.substring(0, 3) + "****" + userPhone.substring(userPhone.length() - 4);
        }
        return "";
    }

    /**
     * 获取手机号
     *
     * @return
     */
    public String getUserPhone() {
        UserBean user = getUser();
        if (user != null) {
            return user.phone;
        }
        return "";
    }

    /**
     * 本地判断是否登录，并不准
     *
     * @return
     */
    public boolean isLogin() {
        if (TextUtils.isEmpty(getToken())) {
            return false;
        } else {
            return true;
        }
    }

    public String getToken() {
        if (TextUtils.isEmpty(token)) {
            token = getUserMMkv().decodeString(KEY_TOKEN);
        }
        return token;
    }

    /**
     * 是否已登录
     *
     * @return true已登录
     */
    public boolean getIsLogined() {
        return !TextUtils.isEmpty(getToken());
    }

    /**
     * 存储服务器ip
     */
    public void setServiceIp(String serviceIp) {
        getUserMMkv().encode(KEY_SERVICE_IP, serviceIp);
    }

    /**
     * 获取存储的服务器ip
     *
     * @return
     */
    public String getServiceIp() {
        return getUserMMkv().decodeString(KEY_SERVICE_IP);
    }

    /**
     * 设置api
     *
     * @param apis
     */
    public void setApis(String apis) {
        getUserMMkv().encode(KEY_APIS, apis);
    }

    public List<ApiBean> getApisBe() {
        Type type = new TypeToken<List<ApiBean>>() {
        }.getType();
        return GsonUtil.fromJson(getUserMMkv().decodeString(KEY_APIS), type);
    }

    public void setVips(@NonNull List<VipBean> vips) {
        getVipListMMkv().encode(KEY_VIP_LIST, GsonUtil.toJson(vips));
    }

    public List<VipBean> getVips() {
        String s = getVipListMMkv().decodeString(KEY_VIP_LIST);
        if (TextUtils.isEmpty(s)) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<VipBean>>() {
        }.getType();
        return GsonUtil.fromJson(s, type);
    }

    public void setVipsMap(@NonNull List<VipBean> vips) {
        Map<String, VipBean> map = new HashMap<>();
        for (VipBean vip : vips) {
            map.put(vip.minIntegral, vip);
        }
        getVipMapMMkv().encode(KEY_VIP, GsonUtil.toJson(map));
    }

    public Map<String, VipBean> getVipsMap() {
        String s = getVipMapMMkv().decodeString(KEY_VIP);
        if (TextUtils.isEmpty(s)) {
            return new HashMap<>();
        }
        Type type = new TypeToken<Map<String, VipBean>>() {
        }.getType();
        return GsonUtil.fromJson(s, type);
    }

    public void setU2cny(double u2CNY) {
        MMKV.defaultMMKV().encode(U2CNY, u2CNY);
    }

    public double getU2cny() {
        double v = MMKV.defaultMMKV().decodeDouble(U2CNY);
        if (v == 0) {
            v = 6.5;
        }
        return v;
    }

    public void setVipIntegral(String vipIntegral) {
        MMKV.defaultMMKV().encode(VIP_INTEGRAL, vipIntegral);
    }

    public int getVipIntegral() {
        String s = MMKV.defaultMMKV().decodeString(VIP_INTEGRAL);
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 设置非小号代理地址
     *
     * @param str
     */
    public void setFeiXiaoHaoProxy(String str) {
        MMKV.defaultMMKV().encode(KEY_FEI_XIAO_HAO, str);
    }

    public String[] getFeiXiaoHaoProxy() {
        if (FEI_XIAO_HAO_PROXY_VALUE == null) {
            String s = MMKV.defaultMMKV().decodeString(KEY_FEI_XIAO_HAO);
            if (!TextUtils.isEmpty(s)) {
                String[] split = s.split(":");
                FEI_XIAO_HAO_PROXY_VALUE = split;
            }
        }
        return FEI_XIAO_HAO_PROXY_VALUE;
    }
}
