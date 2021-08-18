package com.bizeng.lh.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.DialogRadioAddStrategyRVAdapter;
import com.bizeng.lh.app.App;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.ApiBean;
import com.bizeng.lh.bean.SelectBean;
import com.bizeng.lh.bean.StrategyBean;
import com.bizeng.lh.databinding.ActivityWebviewBinding;
import com.bizeng.lh.utils.AndroidToJs;
import com.bizeng.lh.utils.MMKVUtils;
import com.bizeng.lh.utils.SysUtils;
import com.bizeng.lh.viewmodel.WebViewViewModel;
import com.liys.dialoglib.AnimationsType;
import com.liys.dialoglib.UniversalDialog;
import com.wzq.mvvmsmart.utils.KLog;
import com.wzq.mvvmsmart.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rxhttp.wrapper.utils.GsonUtil;


/**
 * @Desc: webView 加载界面
 * 1、策略详情 显示功能按钮
 * <p>
 * //TODO 2021-7-16 跟换https后导致webview缓存失效，用https://www.baidu.com/测试app缓存正常
 * @author: admin wsj
 * @Date: 2021/4/21 10:19 AM
 */
public class WebViewActivity extends BaseActivity<ActivityWebviewBinding, WebViewViewModel> implements View.OnClickListener {
    //    private int mLoginRequestCode = 0012;
    private String mTitle;
    private String mUrl;
    private int mType = 0;//0策略详情显示添加策略按钮，1新闻资讯
    private String mStrategyId = null;
    private boolean mHasToolBar = false;
    private Map<String, ApiBean> mApiBeanMap;
    UniversalDialog mDialog;
    private List<StrategyBean> mStrategyBeanList = null;
    //已绑定的apiId
    private List<String> mApiIds = null;
    private boolean mIsLoadSuccess = true;//true加载成功

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_webview;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.webView.onResume();
        requestStrategyInfo();
//        binding.webView.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.webView.onPause();
//        binding.webView.pauseTimers();
    }

    @Override
    public void initParam() {
        mTitle = getIntent().getStringExtra(Content.PARAMS_TITLE);
        mUrl = getIntent().getStringExtra(Content.PARAMS_URL);
        mStrategyId = getIntent().getStringExtra(Content.KEY);
        if (TextUtils.isEmpty(mTitle)) {
            mHasToolBar = false;
        } else {
            mHasToolBar = true;
        }
        mType = getIntent().getIntExtra(Content.TYPE, 0);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initData(Bundle savedInstanceState) {
        if (mType == 0) {
            binding.btnWebView.setVisibility(View.VISIBLE);
        } else {
            binding.btnWebView.setVisibility(View.GONE);
        }
        binding.btnWebView.setOnClickListener(this);
        if (getTitleBar() != null) {
            getTitleBar().setNavigationOnClickListener(v -> back());
        }
        binding.webView.addJavascriptInterface(new AndroidToJs(WebViewActivity.this, binding.webView), "app");
        WebSettings settings = binding.webView.getSettings();
//        String cacheDirPath1 = this.getFilesDir().getAbsolutePath() + "cache/";// /data/user/0/com.bizeng.lh/filescache/
        String cacheDirPath = App.getInstance().getExternalCacheDir() + "/cacheWebView/";// /storage/emulated/0/Android/data/com.bizeng.lh/cache/cacheWebView/
        settings.setAppCachePath(cacheDirPath);
        settings.setUserAgentString("User-Agent:Android");
//        settings.setAppCacheMaxSize(20 * 1024 * 1024);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setDomStorageEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        if (SysUtils.isThereANet(this)) {
            //当前有可用网络
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式( 根据cache-control决定是否从网络上取数据。)
        } else {
            //当前没有可用网络
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式(只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。)
        }

        MyWebViewClient mMyWebViewClient = new MyWebViewClient();
        binding.webView.setWebViewClient(mMyWebViewClient);
        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    binding.pbWebView.setVisibility(View.INVISIBLE);
                } else {
                    binding.pbWebView.setProgress(newProgress);
                    if (binding.pbWebView.getVisibility() != View.VISIBLE) {
                        binding.pbWebView.setVisibility(View.VISIBLE);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    setTvTitle(title);
                } else {
                    setTvTitle("帮助");
                }
            }
        });
        if (TextUtils.isEmpty(mUrl)) {
//            ToastUtils.showShort("数据有误");
            mIsLoadSuccess = false;
            showEmptyLayout(binding.webView, "抱歉，您访问的页面不存在", R.mipmap.bg_404, false, true, v -> {
                finish();
            });
            return;
        }
        loadUrl();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadUrl() {
        binding.webView.resumeTimers();
        binding.webView.loadUrl(mUrl);
//        binding.webView.loadUrl("https://www.baidu.com/");
//        binding.webView.loadUrl("https://www.open-open.com/lib/view/open1392188052301.html");
    }

    @Override
    public void initViewObservable() {
//        super.initViewObservable();
        viewModel.getAddStrategy().observe(this, s -> {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            ToastUtils.showShort("添加成功");
            requestStrategyInfo();
            startActivity(StrategyOperationsCenterActivity.class);
        });
        viewModel.getStrategyInfo().observe(this, s -> {
            mStrategyBeanList = s;
        });
        viewModel.getApiIds().observe(this, s -> {
            mApiIds = s;
        });
        viewModel.getApiBeans().observe(this, s -> {
            mApiBeanMap = s;
            if (mApiIds == null) {
                requestStrategyInfo();
            }
            listSelectionPopup();
        });
        viewModel.tutorialArea.observe(this, s -> {
            WebViewActivity.newInstance(WebViewActivity.this, "", s.getUrl(), 1);
            if (mDialog != null) {
                mDialog.dismiss();
            }
        });
    }

    private void requestStrategyInfo() {
        if (!TextUtils.isEmpty(mStrategyId)) {
            viewModel.requestStrategyInfo(mStrategyId);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_webView:
                if (!MMKVUtils.getInstance().getIsLogined()) {
//                    startActivity(LoginActivity.class);
                    startActivity(LoginNewActivity.class);
                    return;
                }
                if (viewModel.getVipIntegral() < 100) {
                    becomeAGoldMemberTip();
                    return;
                }
                viewModel.requestApiBeans();
                break;
        }
    }

    private void becomeAGoldMemberTip() {
        mDialog = UniversalDialog.newInstance(this, R.layout.commom_dialog_tip);
        mDialog.setGravity(Gravity.CENTER)
                .setAnimations(AnimationsType.SCALE)
                .setWidthRatio(0.7)
                .setText(R.id.dialog_tv_content, R.string.dialog_not_a_gold_member)
                .setText(R.id.dialog_btn_negative, R.string.common_cancel)
                .setText(R.id.dialog_btn_positive, R.string.dialog_how_to_become_a_gold_member)
                .show();
        mDialog.getView(R.id.dialog_btn_negative).setOnClickListener(v1 -> mDialog.dismiss());
        mDialog.getView(R.id.dialog_btn_positive).setOnClickListener(v13 -> {
//            startActivity(RechargeCardActivity.class);
            viewModel.requestTutorialArea(Content.HOW_TO_BECOME_A_GOLD_CARD);
        });
    }

    /**
     * 列表选择弹框
     */
    private void listSelectionPopup() {
        List<SelectBean<ApiBean>> list = new ArrayList<>();
        if (initListNew(list)) return;
        mDialog = UniversalDialog.newInstance(this, R.layout.commom_dialog_list);
        mDialog.setGravity(Gravity.CENTER)
                .setAnimations(AnimationsType.SCALE)
                .setWidthRatio(0.84)
                .setText(R.id.dialog_tv_title, R.string.dialog_choose_an_exchange)
                .setText(R.id.dialog_btn_negative, R.string.common_cancel)
                .setText(R.id.dialog_btn_positive, R.string.common_confirm)
                .show();
        RecyclerView recyclerView = mDialog.getView(R.id.dialog_rv_content);
        TextView dialog_tv_title = mDialog.getView(R.id.dialog_tv_title);
//        dialog_tv_title.setGravity(Gravity.LEFT);
        dialog_tv_title.setVisibility(View.VISIBLE);
        DialogRadioAddStrategyRVAdapter dialogRadioAddStrategyRVAdapter = new DialogRadioAddStrategyRVAdapter(list);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            recyclerView.setAdapter(dialogRadioAddStrategyRVAdapter);
        }
        mDialog.getView(R.id.dialog_btn_negative).setOnClickListener(v1 -> mDialog.dismiss());
        mDialog.getView(R.id.dialog_btn_positive).setOnClickListener(v13 -> {
            String checks = dialogRadioAddStrategyRVAdapter.getChecks();
            if (!TextUtils.isEmpty(checks)) {
                String[] split = checks.split(",");
                if (split != null) {
                    addStrategy(mStrategyId, split);
                }
            } else {
                ToastUtils.showShort("请选择添加的api");
            }
        });
        dialogRadioAddStrategyRVAdapter.addChildClickViewIds(R.id.tv_click_api, R.id.cb_rv);
        dialogRadioAddStrategyRVAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.tv_click_api:
                    mDialog.dismiss();
                    startActivity(ApiManagerActivity.class);
                    break;
                case R.id.cb_rv:
                    list.get(position).setSelected();
                    dialogRadioAddStrategyRVAdapter.notifyItemChanged(position);
                    break;
            }
        });
    }

    private boolean initListNew(List<SelectBean<ApiBean>> list) {
        SelectBean<ApiBean> selectBeanHB = new SelectBean<>("火币");
        SelectBean<ApiBean> selectBeanBan = new SelectBean<>("币安");
        SelectBean<ApiBean> selectBeanOKEX = new SelectBean<>("OKEx");
        //有已绑定的api
        if (mApiBeanMap != null) {
            //已绑定api数量
            int apiBeanMapSize = 0;
            ApiBean apiBeanHB = mApiBeanMap.get(Content.API_TAG_HB);
            ApiBean apiBeanBan = mApiBeanMap.get(Content.API_TAG_BAN);
            ApiBean apiBeanOKEX = mApiBeanMap.get(Content.API_TAG_OKEX);
            selectBeanHB.setExtra(apiBeanHB);
            selectBeanBan.setExtra(apiBeanBan);
            selectBeanOKEX.setExtra(apiBeanOKEX);
            if (apiBeanHB != null && !apiBeanHB.apiContentIsNull()) {
                apiBeanMapSize++;
            }
            if (apiBeanBan != null && !apiBeanBan.apiContentIsNull()) {
                apiBeanMapSize++;
            }
            if (apiBeanOKEX != null && !apiBeanOKEX.apiContentIsNull()) {
                apiBeanMapSize++;
            }
            //有已添加的策略
            if (mApiIds != null) {
                int size = mApiIds.size();
                if (size == 0 && apiBeanMapSize == 1) {
                    //绑定api只有一个，且没有添加策略，此时自动添加策略
                    if (apiBeanHB != null && !apiBeanHB.apiContentIsNull()) {
                        addStrategy(mStrategyId, new String[]{apiBeanHB.apiId});
                    } else if (apiBeanBan != null && !apiBeanBan.apiContentIsNull()) {
                        addStrategy(mStrategyId, new String[]{apiBeanBan.apiId});
                    } else if (apiBeanOKEX != null && !apiBeanOKEX.apiContentIsNull()) {
                        addStrategy(mStrategyId, new String[]{apiBeanOKEX.apiId});
                    }
                    return true;
                }
                if (size == apiBeanMapSize && apiBeanMapSize > 0) {
                    //绑定api与已添加策略数相等，提示已添加此策略
                    dialogYouHaveAddedThisStrategy();
                    return true;
                }
                if (apiBeanHB != null && mApiIds.contains(apiBeanHB.apiId)) {
                    selectBeanHB.setType(3);
                    selectBeanHB.setSelected(true);
                } else {
                    selectBeanHB.setType(1);
                    selectBeanHB.setSelected(false);
                }
                if (apiBeanBan != null && mApiIds.contains(apiBeanBan.apiId)) {
                    selectBeanBan.setType(3);
                    selectBeanBan.setSelected(true);
                } else {
                    selectBeanBan.setType(1);
                    selectBeanBan.setSelected(false);
                }
                if (apiBeanOKEX != null && mApiIds.contains(apiBeanOKEX.apiId)) {
                    selectBeanOKEX.setType(3);
                    selectBeanOKEX.setSelected(true);
                } else {
                    selectBeanOKEX.setType(1);
                    selectBeanOKEX.setSelected(false);
                }
            } else {
                selectBeanHB.setType(1);
                selectBeanHB.setSelected(false);
                selectBeanBan.setType(1);
                selectBeanBan.setSelected(false);
                selectBeanOKEX.setType(1);
                selectBeanOKEX.setSelected(false);
            }
        } else {
            selectBeanHB.setType(1);
            selectBeanHB.setSelected(false);
            selectBeanBan.setType(1);
            selectBeanBan.setSelected(false);
            selectBeanOKEX.setType(1);
            selectBeanOKEX.setSelected(false);
        }

        list.add(selectBeanHB);
        list.add(selectBeanBan);
        list.add(selectBeanOKEX);
        return false;
    }

    /**
     * 您已添加此策略 tip
     */
    private void dialogYouHaveAddedThisStrategy() {
        UniversalDialog dialog = UniversalDialog.newInstance(this, R.layout.commom_dialog_tip);
        dialog.setGravity(Gravity.CENTER)
                .setAnimations(AnimationsType.SCALE)
                .setWidthRatio(Content.WIDTH_RATIO)
                .setText(R.id.dialog_tv_content, R.string.dialog_you_have_already_added_this_strategy)
                .setText(R.id.dialog_btn_negative, R.string.common_cancel)
                .setText(R.id.dialog_btn_positive, R.string.dialog_strategy_operations_center)
                .show();
        dialog.getView(R.id.dialog_btn_negative).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getView(R.id.dialog_btn_positive).setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(StrategyOperationsCenterActivity.class);
        });
    }

    @Override
    protected boolean hasToolBar() {
        return mHasToolBar;
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        //按返回键操作并且能回退网页
        if (binding.webView.canGoBack()) {
            //后退
            binding.webView.goBack();
        } else {
            WebViewActivity.this.finish();
        }
    }

    /**
     * 添加策略
     */
    private void addStrategy(String strategyId, String[] apiIds) {
        viewModel.requestAddStrategy(strategyId, apiIds);
    }

    @Override
    public void onContentReload() {
        mIsLoadSuccess = true;
        requestStrategyInfo();
        binding.webView.reload();
    }

    /**
     * 启动webview
     *
     * @param content
     * @param title   标题，若没有择没有返回键
     * @param url     链接
     * @param type    0策略详情显示添加策略按钮，1新闻资讯
     */
    public static void newInstance(Activity content, String title, String url, int type, String strategyId) {
        Intent intent = new Intent(content, WebViewActivity.class);
        intent.putExtra(Content.PARAMS_TITLE, title);
        intent.putExtra(Content.PARAMS_URL, url);
        intent.putExtra(Content.TYPE, type);
        if (!TextUtils.isEmpty(strategyId)) {
            intent.putExtra(Content.KEY, strategyId);
        }
        content.startActivity(intent);
    }

    public static void newInstance(Activity content, String title, String url, int type) {
        newInstance(content, title, url, type, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.webView.removeAllViews();
        binding.webView.destroy();
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if ("about:blank".equals(url)) {
                mIsLoadSuccess = false;
                showEmptyLayout(binding.webView, "抱歉，您访问的页面不存在", R.mipmap.bg_404, false, true, v -> {
                    finish();
                });
                return;
            }
            if (mIsLoadSuccess) {
                showNormalLayout(binding.webView);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { //网页加载时的连接的网址
            if (TextUtils.isEmpty(url)) {
                return false;
            }
            WebView.HitTestResult hit = view.getHitTestResult();
            //hit.getExtra()为null或者hit.getType() == 0都表示即将加载的URL会发生重定向，需要做拦截处理
            if (TextUtils.isEmpty(hit.getExtra()) || hit.getType() == 0) {
                //通过判断开头协议就可解决大部分重定向问题了，有另外的需求可以在此判断下操作
                KLog.d("重定向", "重定向: " + hit.getType() + " && EXTRA（）" + hit.getExtra() + "------");
                KLog.d("重定向", "GetURL: " + view.getUrl() + "\n" + "getOriginalUrl()" + view.getOriginalUrl());
                KLog.d("重定向", "URL: " + url);
            }

            if (url.startsWith("http://") || url.startsWith("https://")) { //加载的url是http/https协议地址
                view.loadUrl(url);
            } else { //加载的url是自定义协议地址
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true; //返回false表示此url默认由系统处理,url未加载完成，会继续往下走
        }

        /**
         * 这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
         * 旧版本，会在新版本中也可能被调用，所以加上一个判断，防止重复显示
         *
         * @param view
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            //Log.e(TAG, "onReceivedError: ----url:" + error.getDescription());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return;
            }
            showEmpty();
        }

        // 新版本，只会在Android6及以上调用
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (request.isForMainFrame()) { // 或者： if(request.getUrl().toString() .equals(getUrl()))
                showEmpty();
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            super.onReceivedSslError(view, handler, error);
            //handler.cancel(); // Android默认的处理方式
            handler.proceed();  // 接受所有网站的证书
//            handler.handleMessage(Message msg); // 进行其他处理
            KLog.d("onReceivedSslError()::error:" + GsonUtil.toJson(error));

//            SslCertificate certificate = error.getCertificate();
//            String cName = certificate.getIssuedBy().getCName();
//            String uName = certificate.getIssuedTo().getOName();
//            if(HttpsUtils.checkSslCertificate(certificate)&&
//                    cName.equals(getResources().getString(R.string.ssl_issueby_name))&&
//                    uName.equals(getResources().getString(R.string.company_name))){
//                handler.proceed();
//            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            int statusCode = errorResponse.getStatusCode();
            System.out.println("onReceivedHttpError code = " + statusCode);
            if (404 == statusCode || 500 == statusCode) {
                mIsLoadSuccess = false;
                showEmptyLayout(binding.webView, "抱歉，您访问的页面不存在", R.mipmap.bg_404, false, true, v -> {
                    finish();
                });
            }
        }

//        @Nullable
//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
////            return super.shouldInterceptRequest(view, request);
//            return WebViewCacheInterceptorImpl.getInstance().intercept(request);
//        }
//
//        @Nullable
//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//            return super.shouldInterceptRequest(view, url);
//        }

        private void showEmpty() {
            mIsLoadSuccess = false;
            showEmptyLayout(binding.webView, "请检查网络连接后重试", R.mipmap.ic_not_wifi, true, true, v -> {
                finish();
            });
        }
    }
}
