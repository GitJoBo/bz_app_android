package com.bizeng.lh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.LeaderBoardBean;
import com.bizeng.lh.model.AppModel;
import com.wzq.mvvmsmart.event.SingleSourceLiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class LeaderBoardViewModel extends BaseViewModel {
    AppModel mAppModel;

    public LeaderBoardViewModel(@NonNull Application application) {
        super(application);
        mAppModel = new AppModel();
    }

    //排行榜
    private SingleSourceLiveData<List<LeaderBoardBean>> leaderBoards = new SingleSourceLiveData<>();

    /**
     * 排行榜
     *
     * @return
     */
    public SingleSourceLiveData<List<LeaderBoardBean>> getLeaderBoards() {
        return leaderBoards;
    }

    /**
     * 请求排行榜
     */
    public void requestLeaderBoards(int num, int type) {
        Observable<List<LeaderBoardBean>> listObservable = mAppModel.requestLeaderBoard(num, type);
        loadingDisposable(listObservable, s -> {
            leaderBoards.postValue(s);
        });
    }
}
