package com.bizeng.lh.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.ProfitSharingRecordAdapter;
import com.bizeng.lh.base.BaseFragment;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.ProfitSharingRecordBean;
import com.bizeng.lh.databinding.FragmentProfitSharingRecordBinding;
import com.bizeng.lh.viewmodel.ProfitSharingRecordViewModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wzq.mvvmsmart.base.ViewModelFactory;
import com.wzq.mvvmsmart.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 返利记录
 * @author: admin wsj
 * @Date: 2021/6/28 9:47 AM
 */
public class ProfitSharingRecordFragment extends BaseFragment<FragmentProfitSharingRecordBinding, ProfitSharingRecordViewModel> {
    private ProfitSharingRecordAdapter mProfitSharingRecordAdapter;
    private List<ProfitSharingRecordBean> mList = new ArrayList<>();
    private int mType;//-1全部；0预估返利；1到账返利
    private int mPageNum = Content.PAGE_NUM;
    private int mSmartType = 0;//0刷新，1加载更多

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_profit_sharing_record;
    }

    @Override
    public ProfitSharingRecordViewModel initViewModel() {
        mType = getArguments().getInt(Content.TYPE, -1);
        return new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(Utils.getApplication())).get(String.valueOf(mType), ProfitSharingRecordViewModel.class);
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mProfitSharingRecordAdapter = new ProfitSharingRecordAdapter(mList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(mProfitSharingRecordAdapter);
        binding.smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mSmartType = 1;
                mPageNum++;
                loadDate();
                binding.smartRefresh.finishLoadMore(Content.DEFAULT_WAITING_TIMEOUT);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSmartType = 0;
                mPageNum = Content.PAGE_NUM;
                loadDate();
                binding.smartRefresh.finishRefresh(Content.DEFAULT_WAITING_TIMEOUT);
            }
        });
    }

    private void loadDate() {
        viewModel.requestProfitSharingRecord(mPageNum, Content.PAGE_SIZE, mType);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.profitSharingRecord.observe(this, s -> {
            if (mSmartType == 0) {
                binding.smartRefresh.finishRefresh();
                mList.clear();
                mList.addAll(s);
                binding.smartRefresh.setEnableLoadMore(true);
            } else {
                binding.smartRefresh.finishLoadMore();
                if (s != null && s.size() > 0) {
                    mList.addAll(s);
                } else {
                    mPageNum--;
                    binding.smartRefresh.finishLoadMoreWithNoMoreData();
                }
            }
            mProfitSharingRecordAdapter.setEmptyView(R.layout.item_empty);
            mProfitSharingRecordAdapter.notifyDataSetChanged();
        });
        loadDate();
    }

    public static ProfitSharingRecordFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(Content.TYPE, type);
        ProfitSharingRecordFragment fragment = new ProfitSharingRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
