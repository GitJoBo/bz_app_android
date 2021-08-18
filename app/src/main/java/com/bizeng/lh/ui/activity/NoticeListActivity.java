package com.bizeng.lh.ui.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.NoticeListAdapter;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.databinding.ActivityNoticeListBinding;
import com.bizeng.lh.viewmodel.NoticeViewModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 公告列表
 * @author: admin wsj
 * @Date: 2021/5/25 9:11 AM
 */
public class NoticeListActivity extends BaseActivity<ActivityNoticeListBinding, NoticeViewModel> {
    int mPageNum = Content.PAGE_NUM;
    private List<TutorialAreaBean> mList = new ArrayList<>();
    private NoticeListAdapter mAdapter = null;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_notice_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public String setTitleBar() {
        return "公告";
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.noticeList.observe(this, this::initListRefresh);
        request();
    }

    private void initListRefresh(List<TutorialAreaBean> s) {
        if (mPageNum == Content.PAGE_NUM) {
            binding.includeRv.smartRefresh.finishRefresh();
            mList.clear();
        } else if (s.size() < 1) {
            mPageNum--;
            binding.includeRv.smartRefresh.finishLoadMoreWithNoMoreData();
        } else {
            binding.includeRv.smartRefresh.finishLoadMore();
        }
        mList.addAll(s);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.setEmptyView(R.layout.item_empty);
    }

    private void request() {
        viewModel.requestNoticeList(mPageNum, Content.PAGE_SIZE);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mAdapter = new NoticeListAdapter(mList);
        binding.includeRv.recyclerview.setAdapter(mAdapter);
        binding.includeRv.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.addChildClickViewIds(R.id.tv_notice_more, R.id.sl_bg);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            TutorialAreaBean tutorialAreaBean = mList.get(position);
            WebViewActivity.newInstance(this, "", tutorialAreaBean.getUrl(), 1);
        });
        binding.includeRv.smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageNum++;
                request();
                binding.includeRv.smartRefresh.finishLoadMore(Content.DEFAULT_WAITING_TIMEOUT);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageNum = Content.PAGE_NUM;
                request();
                binding.includeRv.smartRefresh.finishRefresh(Content.DEFAULT_WAITING_TIMEOUT);
            }
        });

    }
}
