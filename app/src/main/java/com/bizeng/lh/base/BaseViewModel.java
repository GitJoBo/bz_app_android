package com.bizeng.lh.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bizeng.lh.app.App;
import com.bizeng.lh.bean.ErrorInfo;
import com.bizeng.lh.http.OnError;
import com.bizeng.lh.utils.SysUtils;
import com.wzq.mvvmsmart.base.BaseViewModelMVVM;
import com.wzq.mvvmsmart.event.StateLiveData;
import com.wzq.mvvmsmart.utils.ToastUtils;
import com.wzq.mvvmsmart.utils.resource.Resource;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * @Desc:
 * @author: admin wsj
 * @Date: 2021/4/20 1:04 PM
 */
public class BaseViewModel extends BaseViewModelMVVM {
    public StateLiveData<Resource> stateLiveData;

    public MutableLiveData<Resource> getStateLiveData() {
        return stateLiveData.stateEnumMutableLiveData;
    }

    public BaseViewModel(@NonNull Application application) {
        super(application);
        stateLiveData = new StateLiveData<>();
    }

    /**
     * 有失败原因toast
     * 没有请求loading动画
     *
     * @param observable
     * @param onNext     成功回调
     * @param <T>
     * @return
     */
    public <T> Disposable loadingDisposable(Observable<T> observable, @NonNull Consumer<? super T> onNext) {
        return loadingDisposable(observable, onNext, false);
    }

    /**
     * 网络请求执行
     * loading动画
     *
     * @param observable
     * @param anim       true 显示请求动画
     * @param <T>
     * @return
     */
    public <T> Observable<T> loadingObservable(Observable<T> observable, boolean anim) {
        //若请求失败，则1重试3次
//        observable.retry(3);
        return observable.observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //判断网络状况
                    if (!SysUtils.isThereANet(App.getInstance())) {
                        ToastUtils.showShort("网络不给力，请稍后重试");
                    }
                    if (anim) {
                        //请求开始，当前在主线程回调
                        stateLiveData.postLoading();
                    }
                })
                .doFinally(() -> {
                    if (anim) {
                        //请求结束，当前在主线程回调
                        stateLiveData.postSuccess();
                    }
                });
    }

    /**
     * 网络请求执行 打印错误日志
     *
     * @param observable
     * @param onNext     成功回调
     * @param anim       true loading动画
     * @param <T>
     * @return
     */
    public <T> Disposable loadingDisposable(Observable<T> observable, @NonNull Consumer<? super T> onNext, boolean anim) {
        return loadingDisposable(observable, onNext, null, anim);
    }

    /**
     * 网络请求执行
     *
     * @param observable
     * @param onNext     成功回调
     * @param onError    错误回调 null有toast
     * @param anim       true loading动画
     * @param <T>
     * @return
     */
    public <T> Disposable loadingDisposable(Observable<T> observable, @NonNull Consumer<? super T> onNext, OnError onError, boolean anim) {
        return loadingObservable(observable, anim)
                .subscribe(onNext, onError == null ? (OnError) ErrorInfo::show : onError);
    }

}

