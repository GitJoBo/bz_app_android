package com.bizeng.lh.ui.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.MyTeamAdapter;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.UserBean;
import com.bizeng.lh.databinding.ActivityMyTeamBinding;
import com.bizeng.lh.viewmodel.MyGoodFriendViewModel;
import com.bizeng.lh.widget.view.GridItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 我的好友_新版
 * @author: admin wsj
 * @Date: 2021/7/1 11:36 AM
 */
public class MyTeamActivity extends BaseActivity<ActivityMyTeamBinding, MyGoodFriendViewModel> {
    private MyTeamAdapter mMyTeamAdapter;
    private List<UserBean> mList = new ArrayList<>();

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_my_team;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public String setTitleBar() {
        return "我的好友";
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mMyTeamAdapter = new MyTeamAdapter(mList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(mMyTeamAdapter);
        binding.recyclerView.addItemDecoration(new GridItemDecoration(this, RecyclerView.VERTICAL));
        binding.smartRefresh.setEnableLoadMore(false);
        binding.smartRefresh.setOnRefreshListener(refreshLayout -> {
            onRefresh();
            binding.smartRefresh.finishRefresh(Content.DEFAULT_WAITING_TIMEOUT);
        });
    }

    private void onRefresh() {
        viewModel.requestUserTeam();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getUserTeam().observe(this, s -> {
            mList.clear();
            mList.addAll(s.childList);
            mMyTeamAdapter.notifyDataSetChanged();
            binding.smartRefresh.finishRefresh();
            mMyTeamAdapter.setEmptyView(R.layout.item_empty);
        });
        viewModel.requestUserTeam();
    }
}
