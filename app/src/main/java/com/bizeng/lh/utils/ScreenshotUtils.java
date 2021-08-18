package com.bizeng.lh.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.wzq.mvvmsmart.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * @Desc: 截屏保存工具类
 * @author: admin wsj
 * @Date: 2021/5/14 9:14 AM
 */
public class ScreenshotUtils {
    private ScreenshotUtils() {
    }

    private volatile static ScreenshotUtils instance;

    public static ScreenshotUtils getInstance() {
        if (instance == null) {
            synchronized (ScreenshotUtils.class) {
                if (instance == null) {
                    instance = new ScreenshotUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 截全屏，并保存
     *
     * @param activity
     */
    public void screenshot(Activity activity, String filename) {
        // 获取屏幕
        View dView = activity.getWindow().getDecorView();
//        View dView = binding.viewScreenshot;
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        saveImg(bmp, dView, filename);
    }

    /**
     * 对View进行截图
     *
     * @param view
     * @return 返回保存的文件链接
     */
    public Uri viewSnapshot(View view, String fileName) {
        //使控件可以进行缓存
        view.setDrawingCacheEnabled(true);
        //获取缓存的 Bitmap
        Bitmap drawingCache = view.getDrawingCache();
        //复制获取的 Bitmap
        drawingCache = Bitmap.createBitmap(drawingCache);
        //关闭视图的缓存
        view.setDrawingCacheEnabled(false);
        if (drawingCache != null) {
            return saveImg(drawingCache, view, fileName);
        } else {
            ToastUtils.showShort("失败");
        }
        return null;
    }


    /**
     * 保存图片
     *
     * @param bmp
     */
    public Uri saveImg(Bitmap bmp, View view, String fileName) {
        if (bmp != null) {
            try {
                // 获取内置SD卡路径
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径
                String filePath;
                if (TextUtils.isEmpty(fileName)) {
                    filePath = sdCardPath + File.separator + new Date().getTime() + ".png";
                } else {
                    filePath = sdCardPath + File.separator + fileName + ".png";
                }
                File file = new File(filePath);
                FileOutputStream os = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
//                Uri uri = Uri.parse(file.toString());
                intent.setData(uri);
                view.getContext().sendBroadcast(intent);
//                ToastUtils.showShort("已保存至相册");
                return getImageContentUri(view.getContext(), file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Gets the content:// URI from the given corresponding path to a file
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, java.io.File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * Gets the corresponding path to a file from the given content:// URI
     *
     * @param selectedVideoUri The content:// URI to find the file path from
     * @param contentResolver  The content resolver to use to perform the query.
     * @return the file path as a string
     */
    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }


}
