package com.bizeng.lh.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.bizeng.lh.app.App;
import com.wzq.mvvmsmart.utils.KLog;

import java.io.File;

public class SysUtils {
    private SysUtils() {
    }

    public static volatile SysUtils instance = null;

    public static SysUtils getInstance() {
        if (instance == null) {
            synchronized (SysUtils.class) {
                if (instance == null) {
                    instance = new SysUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 安装app
     *
     * @param filePath
     */
    public void isAPK(String filePath) {
        File file = new File(filePath);
        if (file.getName().endsWith(".apk")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 适配Android 7系统版本
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    uri = FileProvider.getUriForFile(App.getInstance(), App.getInstance().getPackageName() + ".fileprovider", file);//通过FileProvider创建一个content类型的Uri
                } else {
                    uri = Uri.fromFile(file);
                }
                intent.setDataAndType(uri, "application/vnd.android.package-archive"); // 对应apk类型
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getInstance().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前的网络状态  -1：没有网络  1：WIFI网络2：wap网络3：net网络
     *
     * @param context
     * @return
     */
    public static int getAPNType(Context context) {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            KLog.e("networkInfo.getExtraInfo()", "networkInfo.getExtraInfo() is " + networkInfo.getExtraInfo());
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = 3;
            } else {
                netType = 2;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }
        return netType;
    }

    /**
     * 是否有网
     * @param context
     * @return true 有网，
     */
    public static boolean isThereANet(Context context) {
        return getAPNType(context) != -1;
    }

}
