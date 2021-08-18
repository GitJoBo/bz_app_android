package com.wzq.mvvmsmart.event;

/**
 * <p>作者：<p>
 * <p>创建时间：2019/12/21<p>
 * <p>文件描述：带状态的livedata<p>
 */

import androidx.lifecycle.MutableLiveData;

import com.wzq.mvvmsmart.utils.resource.Resource;
import com.wzq.mvvmsmart.utils.resource.Status;

public class StateLiveData<T> extends MutableLiveData<T> {
    // 封装的枚举,用MutableLiveData可以被view观察;
    public MutableLiveData<Resource> stateEnumMutableLiveData = new MutableLiveData<>();

    public final void postValueAndSuccess(T t) {
        super.postValue(t);
        this.postSuccess();
    }

    public void postLoading() {
        stateEnumMutableLiveData.postValue(Resource.loading(null));
    }

    public void postLoading(String tip) {
        stateEnumMutableLiveData.postValue(Resource.loading(tip));
    }

    public void postNoData() {
        stateEnumMutableLiveData.postValue(new Resource(Status.NO_DATA, null, 1));
    }

    public void postNoMoreData() {
        stateEnumMutableLiveData.postValue(new Resource(Status.NO_MORE_DATA, null, 2));
    }

    public void postNoNet() {
        stateEnumMutableLiveData.postValue(new Resource(Status.NO_NET, null, 3));
    }

    public void postSuccess() {
        stateEnumMutableLiveData.postValue(new Resource(Status.SUCCESS, null, 200));
    }

    public void postError() {
        stateEnumMutableLiveData.postValue(new Resource(Status.ERROR, null, 999));
    }

    public void changeState(Resource resource) {
        stateEnumMutableLiveData.postValue(resource);
    }


}
