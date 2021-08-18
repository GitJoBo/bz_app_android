package com.bizeng.lh.ui.activity;

import android.os.Bundle;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.databinding.ActivityLeaderBoardBinding;
import com.bizeng.lh.viewmodel.LeaderBoardViewModel;

/**
 * @Desc: 币增排行榜详情
 * @author: admin wsj
 * @Date: 2021/5/19 1:53 PM
 */
public class LeaderBoardActivity extends BaseActivity<ActivityLeaderBoardBinding, LeaderBoardViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_leader_board;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

}
