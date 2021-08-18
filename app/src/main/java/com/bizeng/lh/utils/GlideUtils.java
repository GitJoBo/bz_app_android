package com.bizeng.lh.utils;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bizeng.lh.http.Api;
import com.bumptech.glide.RequestBuilder;

import org.jetbrains.annotations.NotNull;

public class GlideUtils {

    @NotNull
    public static RequestBuilder<Drawable> getLoad(@NonNull String url, @NonNull ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("/")){
                url = url.substring(1);
            }
            if (!url.contains("http") && !url.contains("file:") && !url.contains("resource://com.jidu.webar/")) {
                url = Api.DOMAIN + url;
            }
        }
        return GlideApp.with(imageView).load(url);
    }
}
