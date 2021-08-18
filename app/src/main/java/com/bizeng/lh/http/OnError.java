package com.bizeng.lh.http;


import com.bizeng.lh.bean.ErrorInfo;

import io.reactivex.rxjava3.functions.Consumer;


/**
 * RxJava 错误回调 ,加入网络异常处理
 */
public interface OnError extends Consumer<Throwable> {

    @Override
    default void accept(Throwable throwable) throws Exception {
        onError(new ErrorInfo(throwable));
    }

    void onError(ErrorInfo error) throws Exception;
}
