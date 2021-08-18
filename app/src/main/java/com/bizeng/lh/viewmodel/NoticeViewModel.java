package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.PageList;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.http.Api;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import rxhttp.RxHttp;

public class NoticeViewModel extends BaseViewModel {
    public NoticeViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 公告列表，分页
     */
    public SingleSourceLiveData<List<TutorialAreaBean>> noticeList = new SingleSourceLiveData<>();

    /**
     * 请求获取公告列表数据，分页
     */
    public void requestNoticeList(int pageNum, int pageSize) {
        Observable<PageList<TutorialAreaBean>> pageListObservable = RxHttp.get(Api.NOTICE_LIST)
                .add("pageNum", pageNum)
                .add("pageSize", pageSize)
                .asResponsePageList(TutorialAreaBean.class);
        loadingDisposable(pageListObservable, s -> {
            noticeList.postValue(s.getRows());
        });
    }
}
