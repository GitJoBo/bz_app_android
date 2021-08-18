package com.wzq.mvvmsmart.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.wzq.mvvmsmart.utils.KLog;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

public class BaseViewModelMVVM extends AndroidViewModel implements IBaseViewModelMVVM, Consumer<Disposable> {
    public final String TAG = getClass().getSimpleName();

    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    protected CompositeDisposable mCompositeDisposable;

    public BaseViewModelMVVM(@NonNull Application application) {
        super(application);
        mCompositeDisposable = new CompositeDisposable();
    }

    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }


    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
//        KLog.d(TAG, "onAny");
    }

    @Override
    public void onCreate() {
        KLog.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        KLog.d(TAG, "onDestroy");
    }

    @Override
    public void onStart() {
        KLog.d(TAG, "onStart");
    }

    @Override
    public void onStop() {
        KLog.d(TAG, "onStop");
    }

    @Override
    public void onResume() {
        KLog.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        KLog.d(TAG, "onPause");
    }


    @Override
    protected void onCleared() {
        KLog.d(TAG, "onCleared");
        super.onCleared();
        //ViewModel销毁时会执行，同时取消所有异步任务
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void accept(Disposable disposable) throws Exception {
        KLog.d(TAG, "accept");
        addSubscribe(disposable);
    }

}
