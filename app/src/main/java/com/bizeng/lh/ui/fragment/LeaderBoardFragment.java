package com.bizeng.lh.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.LeaderBoardAdapter;
import com.bizeng.lh.base.BaseFragment;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.LeaderBoardBean;
import com.bizeng.lh.databinding.FragmentLeaderBoardBinding;
import com.bizeng.lh.http.Api;
import com.bizeng.lh.ui.activity.WebViewActivity;
import com.bizeng.lh.viewmodel.LeaderBoardViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 排行榜
 * @author: admin wsj
 * @Date: 2021/4/30 5:55 PM
 */
public class LeaderBoardFragment extends BaseFragment<FragmentLeaderBoardBinding, LeaderBoardViewModel> implements View.OnClickListener {
    private int mKey = 0; //0recyclerview显示前三
    private int mType = 0; //0-日；1-月；2-年
    private LeaderBoardAdapter mLeaderBoardAdapter = null;
    List<LeaderBoardBean> mList = new ArrayList<>();

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_leader_board;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getLeaderBoards().observe(this, s -> {
            mList.clear();
            mList.addAll(s);
            while (mList.size() < 3) {
                mList.add(new LeaderBoardBean());
            }
            mLeaderBoardAdapter.notifyDataSetChanged();
            mLeaderBoardAdapter.setEmptyView(R.layout.item_empty);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mKey == 0) {
            binding.tvLoadMore.setVisibility(View.VISIBLE);
            binding.tvLoadMore.setOnClickListener(this);
            viewModel.requestLeaderBoards(3, mType);
        } else {
            binding.tvLoadMore.setVisibility(View.GONE);
            viewModel.requestLeaderBoards(10, mType);
        }
    }

    @Override
    public void initParam() {
        mKey = getArguments().getInt(Content.KEY);
        mType = getArguments().getInt(Content.TYPE);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mLeaderBoardAdapter = new LeaderBoardAdapter(mList, mType);
        binding.recyclerviewLeaderBoard.setAdapter(mLeaderBoardAdapter);
        binding.recyclerviewLeaderBoard.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /**
     * @param key  0显示前三
     * @param type 0-日；1-月；2-年
     * @return
     */
    public static LeaderBoardFragment newInstance(int key, int type) {

        Bundle args = new Bundle();
        args.putInt(Content.KEY, key);
        args.putInt(Content.TYPE, type);
        LeaderBoardFragment fragment = new LeaderBoardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_load_more:
//                ToastUtils.showShort("排行详情页web");
//                Intent intent2 = new Intent(getActivity(), WebViewActivity.class);
//                intent2.putExtra(Content.PARAMS_TITLE, "");
//                intent2.putExtra(Content.PARAMS_URL, Api.COIN_INCREASE_LEADERBOARD_ADDRESS);
//                intent2.putExtra(Content.TYPE, 1);
//                startActivity(intent2);
                WebViewActivity.newInstance(getActivity(),"",Api.COIN_INCREASE_LEADERBOARD_ADDRESS,1);
                break;
        }
    }
}
