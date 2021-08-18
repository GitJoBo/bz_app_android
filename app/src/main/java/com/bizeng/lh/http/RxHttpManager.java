package com.bizeng.lh.http;

import android.text.TextUtils;

import com.bizeng.lh.BuildConfig;
import com.bizeng.lh.app.App;
import com.bizeng.lh.utils.HttpsUtils;
import com.bizeng.lh.utils.MMKVUtils;
import com.wzq.mvvmsmart.utils.KLog;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import rxhttp.RxHttp;
import rxhttp.RxHttpPlugins;
import rxhttp.wrapper.cahce.CacheMode;
import rxhttp.wrapper.param.Method;

public class RxHttpManager {
    private volatile static RxHttpManager instance = null;

    private RxHttpManager() {

    }

    public static RxHttpManager getInstance() {
        if (instance == null) {
            synchronized (RxHttpManager.class) {
                if (instance == null) {
                    instance = new RxHttpManager();
                }
            }
        }
        return instance;
    }

    private OkHttpClient mOkHttpClient = null;

//    @OkClient(name = "FeiXiaoHaoClient", className = "FeiXiaoHao")
//    public static OkHttpClient feiXiaoHaoClient = getFeiXiaoHaoOkHttpClient();

    public void initHttp() {
        //设置debug模式，默认为false，设置为true后，发请求，过滤"RxHttp"能看到请求日志
        RxHttp.setDebug(BuildConfig.DEBUG, true);
        //设置缓存目录为：Android/data/{app包名目录}/cache/RxHttpCache
        File cacheDir = new File(App.getInstance().getExternalCacheDir(), "RxHttpCache");
        //设置最大缓存为100M，缓存有效时长为12小时
//        RxHttpPlugins.setCache(cacheDir, 100 * 1024 * 1024, CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE, 12 * 60 * 60 * 1000);
        RxHttpPlugins.setCache(cacheDir, 100 * 1024 * 1024, CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE, HttpsUtils.CACHE_TIMEOUT);
        //或者，调试模式下会有日志输出
//        RxHttp.init(OkHttpClient okHttpClient, boolean debug);
        RxHttp.setOnParamAssembly(param -> {
            Method method = param.getMethod();
            if (method.isGet()) {     //可根据请求类型添加不同的参数
            } else if (method.isPost()) {
            }
            param.add("versionName", BuildConfig.VERSION_NAME)//添加公共参数
                    .addHeader("deviceType", "android");//添加公共请求头
            if (!TextUtils.isEmpty(MMKVUtils.getInstance().getToken())) {
                param.addHeader("token", MMKVUtils.getInstance().getToken());
            }
            return param;
        });
        //非必须,只能初始化一次，第二次将抛出异常
        RxHttp.init(getDefaultOkHttpClient());
    }

    public OkHttpClient getDefaultOkHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = getDefaultOkHttpBuilder()
                    .build();
        }
        return mOkHttpClient;
    }

    /**
     * 设置代理
     *
     * @return
     */
    public OkHttpClient getFeiXiaoHaoOkHttpClient() {
        OkHttpClient.Builder builder = getDefaultOkHttpBuilder();
        //设置代理,需要替换
        String[] feiXiaoHaoProxy = MMKVUtils.getInstance().getFeiXiaoHaoProxy();
        if (feiXiaoHaoProxy != null && feiXiaoHaoProxy.length > 1) {
            KLog.d("feiXiaoHaoProxy" + feiXiaoHaoProxy.length);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(feiXiaoHaoProxy[0], Integer.parseInt(feiXiaoHaoProxy[1])));
            builder.proxy(proxy);
        } else {
            KLog.d("feiXiaoHaoProxy 0");
        }
        return builder.build();
    }

    /**
     * okhtttp默认参数配置
     *
     * @return
     */
    public static OkHttpClient.Builder getDefaultOkHttpBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();  // https证书认证,封装了认证方法,可根据自己公司进行调整;
        return builder.connectTimeout(HttpsUtils.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(HttpsUtils.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(HttpsUtils.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) //添加信任证书
                .hostnameVerifier((hostname, session) -> true); //忽略host验证
    }
}
