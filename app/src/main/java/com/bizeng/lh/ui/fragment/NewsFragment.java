package com.bizeng.lh.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.FragmentViewPager;
import com.bizeng.lh.adapter.HotNewsRvAdapter;
import com.bizeng.lh.adapter.QuotesRvAdapter;
import com.bizeng.lh.base.BaseFragment;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.MainstreamCurrencyMarketBean;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.databinding.FragmentNewsBinding;
import com.bizeng.lh.interfaces.MainRefreshInterface;
import com.bizeng.lh.ui.activity.WebViewActivity;
import com.bizeng.lh.utils.DateUtils;
import com.bizeng.lh.viewmodel.NewsViewModel;
import com.bizeng.lh.widget.ViewPager2Delegate;
import com.liys.dialoglib.UniversalDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wzq.mvvmsmart.utils.KLog;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 资讯
 * @author: admin wsj
 * @Date: 2021/4/18 12:23 AM
 */
public class NewsFragment extends BaseFragment<FragmentNewsBinding, NewsViewModel> implements MainRefreshInterface, View.OnClickListener {
    int mPageNum = Content.PAGE_NUM;
    private QuotesRvAdapter mQuotesRvAdapter;
    private HotNewsRvAdapter mHotNewsRvAdapter;
    private long mCurrentMarketUpdate = 0l;
    //主流币行情
    private List<MainstreamCurrencyMarketBean> mList = new ArrayList<>();
    //排行榜
    private List<Fragment> mFragments = new ArrayList<>();
    //新闻资讯
    private List<TutorialAreaBean> mHotNews = new ArrayList<>();
    Animation mAnimation;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_news;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (lastView != null) {
            ViewGroup parent = (ViewGroup) lastView.getParent();
            if (parent != null) {
                parent.removeView(lastView);
            }
        }
        super.onCreateView(inflater, container, savedInstanceState);
        return lastView;

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        UltimateBarX.addStatusBarTopPadding(binding.viewTitle);
        binding.ivWhy.setOnClickListener(this);
        binding.ivRefresh.setOnClickListener(this);
        binding.tvNewsTime.setOnClickListener(this);
//        binding.smartRefresh.setEnableLoadMore(false);
//        binding.smartRefresh.setOnRefreshListener(refreshLayout -> {
//            binding.smartRefresh.finishRefresh(800);
//            refreshMain();
//        });
        binding.smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageNum++;
                refreshMain();
                binding.smartRefresh.finishLoadMore(Content.DEFAULT_WAITING_TIMEOUT);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageNum = Content.PAGE_NUM;
                refreshMain();
                binding.smartRefresh.finishRefresh(Content.DEFAULT_WAITING_TIMEOUT);
            }
        });
        mQuotesRvAdapter = new QuotesRvAdapter(mList);
        binding.progressBar.setMax(mList.size());
        binding.recyclerViewPrice.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.recyclerViewPrice.setAdapter(mQuotesRvAdapter);
        //行情滑动进度控制
//        binding.recyclerViewPrice.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                if (layoutManager instanceof LinearLayoutManager) {
//                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
//                    //获取第一个可见view的位置
//                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
//                    //获取最后一个可见view的位置
//                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
//                    binding.progressBar.setMax(mList.size());
//                    binding.progressBar.setProgress(lastItemPosition);
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
        //排行榜  LeaderBoard
        if (savedInstanceState == null) {
            mFragments.add(LeaderBoardFragment.newInstance(0, 0));
            mFragments.add(LeaderBoardFragment.newInstance(0, 1));
            mFragments.add(LeaderBoardFragment.newInstance(0, 2));
        } else {
            mFragments.addAll(getChildFragmentManager().getFragments());
        }
        binding.viewPager2.setAdapter(new FragmentViewPager(getChildFragmentManager(), getLifecycle(), mFragments));
        ViewPager2Delegate.Companion.install(binding.viewPager2, binding.dslTabLayout);
        //新闻
        initHotNews();
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        mAnimation.setInterpolator(lin);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getNewsList().observe(this, this::initListRefresh);
        viewModel.getMainstreamCurrencyMarketBean().observe(this, s -> {
            binding.ivRefresh.clearAnimation();
            if (s != null && s.size() > 0) {
                MainstreamCurrencyMarketBean mainstreamCurrencyMarketBean = s.get(0);
                KLog.d("time:::last_updated=" + mainstreamCurrencyMarketBean.last_updated);
                KLog.d("time:::YYYY_MM_DD_HH_MM_SS=" + DateUtils.getInstance().getDateToString(mainstreamCurrencyMarketBean.last_updated, DateUtils.MM_DD_HH_MM_SS));
                if (mainstreamCurrencyMarketBean.last_updated > mCurrentMarketUpdate) {
                    mCurrentMarketUpdate = mainstreamCurrencyMarketBean.last_updated;
                    mList.clear();
                    mList.addAll(s);
                    binding.progressBar.setMax(mList.size());
                    mQuotesRvAdapter.notifyDataSetChanged();
                    mQuotesRvAdapter.setEmptyView(R.layout.item_empty);
                    binding.tvNewsTime.setText(String.format("更新时间：%s", DateUtils.getInstance().getDateToString(mainstreamCurrencyMarketBean.last_updated, DateUtils.MM_DD_HH_MM2)));
                }
            }
        });
        requestMainstreamCurrency(binding.ivRefresh, true);
        viewModel.requestNewsList(Content.PAGE_SIZE, mPageNum);
    }

    @Override
    public void refreshMain() {
        requestMainstreamCurrency(binding.ivRefresh, false);
        viewModel.requestNewsList(Content.PAGE_SIZE, mPageNum);
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    public String setTitleBar() {
        return "资讯";
    }

    /**
     * 新闻
     */
    private void initHotNews() {
        mHotNewsRvAdapter = new HotNewsRvAdapter(mHotNews);
        mHotNewsRvAdapter.setOnItemClickListener((adapter, view, position) -> {
            WebViewActivity.newInstance(getActivity(), "", mHotNews.get(position).getUrl(), 1);
        });
        binding.recyclerViewNewsHotNew.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewNewsHotNew.setAdapter(mHotNewsRvAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_refresh:
            case R.id.tv_news_time:
                requestMainstreamCurrency(binding.ivRefresh, false);
                break;
            case R.id.iv_why:
                UniversalDialog dialog = UniversalDialog.newInstance(getActivity(), R.layout.commom_dialog_tip2);
                TextView dialog_tv_content = dialog.getView(R.id.dialog_tv_content);
                Button dialog_btn_positive = dialog.getView(R.id.dialog_btn_positive);
                dialog_tv_content.setText("数据来源于非小号，仅供参考");
                dialog_btn_positive.setOnClickListener(v12 -> {
                    dialog.dismiss();
                });
                dialog.show();
                break;
        }
    }

    private void requestMainstreamCurrency(View v, boolean whetherToUseTheCacheDirectly) {
        Animation animation = v.getAnimation();
        if (animation == null) {
            v.startAnimation(mAnimation);
            viewModel.requestMainstreamCurrency(whetherToUseTheCacheDirectly);
        } else {
            v.clearAnimation();
        }
    }

    private void initListRefresh(List<TutorialAreaBean> s) {
        if (mPageNum == Content.PAGE_NUM) {
            binding.smartRefresh.finishRefresh();
            mHotNews.clear();
        } else if (s.size() < 1) {
            mPageNum--;
            binding.smartRefresh.finishLoadMoreWithNoMoreData();
        } else {
            binding.smartRefresh.finishLoadMore();
        }
        mHotNews.addAll(s);
        if (mHotNewsRvAdapter != null) {
            mHotNewsRvAdapter.notifyDataSetChanged();
        }
        mHotNewsRvAdapter.setEmptyView(R.layout.item_empty);
    }
}
