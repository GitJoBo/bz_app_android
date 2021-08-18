package com.bizeng.lh.interceptor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bizeng.lh.ui.activity.LoginNewActivity;
import com.bizeng.lh.utils.MMKVUtils;
import com.wzq.mvvmsmart.utils.ToastUtils;

/**
 * @Desc: 登录拦截器
 * @author: admin wsj
 * @Date: 2021/7/20 4:53 下午
 */
public class LoginInterceptor {
    public static final String mINVOKER = "INTERCEPTOR_INVOKER";

    /**
     * 判断处理
     *
     * @param ctx
     *            当前activity的上下文
     * @param target
     *            目标activity的target
     * @param bundle
     *            目标activity所需要的参数
     * @param intent
     *            目标activity
     */
    public static void interceptor(Context ctx, String target, Bundle bundle, Intent intent) {
        if (target != null && target.length() > 0) {
            LoginCarrier invoker = new LoginCarrier(target, bundle);
            if (MMKVUtils.getInstance().isLogin()) {
                invoker.invoke(ctx);
            } else {
                if (intent == null) {
                    intent = new Intent(ctx, LoginNewActivity.class);
                }
                login(ctx, invoker, intent);
            }
        } else {
            ToastUtils.showShort("没有activity可以跳转");
        }
    }

    /**
     * 登录判断
     *
     * @param ctx
     *            当前activity的上下文
     * @param target
     *            目标activity的target xml的action
     * @param bundle
     *            目标activity所需要的参数
     */
    public static void interceptor(Context ctx, String target, Bundle bundle) {
        interceptor(ctx, target, bundle, null);
    }


    private static void login(Context context, LoginCarrier invoker, Intent intent) {
        intent.putExtra(mINVOKER, invoker);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
