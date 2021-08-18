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
import com.bizeng.lh.adapter.MyPointCardValueAdapter;
import com.bizeng.lh.base.BaseFragment;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.PointCardRecordBean;
import com.bizeng.lh.databinding.LayoutSmartRecyclerviewBinding;
import com.bizeng.lh.viewmodel.MyPointCardViewModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wzq.mvvmsmart.base.ViewModelFactory;
import com.wzq.mvvmsmart.utils.KLog;
import com.wzq.mvvmsmart.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 我的点卡
 * @author: admin wsj
 * @Date: 2021/7/14 4:55 下午
 */
public class MyPointCardFragment extends BaseFragment<LayoutSmartRecyclerviewBinding, MyPointCardViewModel> {
    private MyPointCardValueAdapter mMyPointCardValueAdapter;
    List<PointCardRecordBean> mList = new ArrayList<>();
    private int mRecordType = -1;


    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.layout_smart_recyclerview;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MyPointCardViewModel initViewModel() {
        mRecordType = getArguments().getInt(Content.KEY, -1);
        return new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(Utils.getApplication())).get(String.valueOf(mRecordType), MyPointCardViewModel.class);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        viewModel.mRecordType = mRecordType;
        mMyPointCardValueAdapter = new MyPointCardValueAdapter(mList);
        binding.recyclerView.setAdapter(mMyPointCardValueAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.mSmartType = 1;
                viewModel.mPageNum++;
                loadDate();
                binding.smartRefresh.finishLoadMore(Content.DEFAULT_WAITING_TIMEOUT);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.mSmartType = 0;
                viewModel.mPageNum = Content.PAGE_NUM;
                loadDate();
                binding.smartRefresh.finishRefresh(Content.DEFAULT_WAITING_TIMEOUT);
            }
        });
    }

    private void loadDate() {
        viewModel.requestListOfIncomeAndExpenditureDetails(viewModel.mPageNum, Content.PAGE_SIZE, viewModel.mRecordType);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.listOfIncomeAndExpenditureDetails.observe(this, s -> {
            KLog.d("aaaaaa listOfIncomeAndExpenditureDetails:viewModel.mSmartType" + viewModel.mSmartType);
            if (viewModel.mSmartType == 0) {
                mList.clear();
                mList.addAll(s.getRows());
                binding.smartRefresh.finishRefresh();
                binding.smartRefresh.setEnableLoadMore(true);
            } else {
                binding.smartRefresh.finishLoadMore();
                if (s != null && s.getRows().size() > 0) {
                    mList.addAll(s.getRows());
                } else {
                    viewModel.mPageNum--;
                    binding.smartRefresh.finishLoadMoreWithNoMoreData();
                }
            }
            mMyPointCardValueAdapter.setEmptyView(R.layout.item_empty);
            mMyPointCardValueAdapter.notifyDataSetChanged();
        });
        loadDate();
    }

    public static MyPointCardFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(Content.KEY, type);
        MyPointCardFragment fragment = new MyPointCardFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
