package com.bizeng.lh.utils;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * 原生与js调用
 * 使用
 * 1、方法上开启注解
 *
 * @SuppressLint("SetJavaScriptEnabled") 2、开启webview调用js权限
 * mWebview.getSettings().setJavaScriptEnabled(true);
 * 3、增加js
 * mWebview.addJavascriptInterface(new BaseAndroidJs(this, mWebview), "app");
 */
public class AndroidToJs {

    public Activity mActivity;
    public WebView mWebView;

    public AndroidToJs(Activity activity, WebView webView) {
        mActivity = activity;
        mWebView = webView;
    }

    /**
     * js调用原生 关闭
     */
    @JavascriptInterface
    public synchronized void back() {
        if (mActivity == null){
            return;
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //按返回键操作并且能回退网页
                if (mWebView != null && mWebView.canGoBack()) {
                    //后退
                    mWebView.goBack();
                } else {
                    mActivity.finish();
                }
            }
        });

    }


}
