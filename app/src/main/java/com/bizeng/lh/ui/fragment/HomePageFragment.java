package com.bizeng.lh.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.HomePloyRVAdapter;
import com.bizeng.lh.adapter.HotNewsRvAdapter;
import com.bizeng.lh.adapter.ImageAdapter;
import com.bizeng.lh.base.BaseFragment;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.BannerBean;
import com.bizeng.lh.bean.StrategyBean;
import com.bizeng.lh.bean.TutorialAreaBean;
import com.bizeng.lh.databinding.FragmentHomePageBinding;
import com.bizeng.lh.interfaces.MainRefreshInterface;
import com.bizeng.lh.ui.activity.MainActivity;
import com.bizeng.lh.ui.activity.NoticeListActivity;
import com.bizeng.lh.ui.activity.WebViewActivity;
import com.bizeng.lh.viewmodel.HomePageViewModel;
import com.wzq.mvvmsmart.utils.ToastUtils;
import com.youth.banner.indicator.RectangleIndicator;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 首页
 * @author: admin wsj
 * @Date: 2021/4/18 12:22 AM
 */
public class HomePageFragment extends BaseFragment<FragmentHomePageBinding, HomePageViewModel> implements MainRefreshInterface, View.OnClickListener {
    //    private static final float MAX_SCALE = 1f;
//    private static final float MIN_SCALE = 0.8f;
    private List<BannerBean> mBannerBeans = new ArrayList<>();
    private HomePloyRVAdapter mHomePloyRVAdapter;
    private HotNewsRvAdapter mHotNewsRvAdapter;
    private ImageAdapter mImageAdapter;
    private TutorialAreaBean mTutorialAreaBean;
    private List<StrategyBean> mStrategies = new ArrayList<>();
    private List<TutorialAreaBean> mHotNews = new ArrayList<>();
    private List<String> mAnnouncements = new ArrayList<>();

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_home_page;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    /**
     * ViewPager中的Fragment使用navigation,需要创建View的时候,从之前的父类中remove掉
     * 不remove掉的话,从其他页面返回含有ViewPager页面的话会报错"The specified child already has a parent"
     */
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
    public void refreshMain() {
        viewModel.requestHomePageBean();
        viewModel.requestU2CNY();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initData(Bundle savedInstanceState) {
        initBanner();
//        initAnnouncement();
        initStrategyRecommendation();
        initHotNews();
        initView();
    }

    private void initView() {
        UltimateBarX.addStatusBarTopPadding(binding.viewTitle);
        binding.tvHomeStrategyRecommendationMore.setOnClickListener(this);
        binding.tvHomeHotNewMore.setOnClickListener(this);
        binding.smartRefresh.setEnableLoadMore(false);
        binding.smartRefresh.setOnRefreshListener(refreshLayout -> {
            binding.smartRefresh.finishRefresh(Content.DEFAULT_WAITING_TIMEOUT);
            refreshMain();
        });
        binding.ivHomeQuantitativeIntroduction.setOnClickListener(this);
        binding.tvHomeBuyCoinsGuide.setOnClickListener(this);
        binding.tvHomeSecurity.setOnClickListener(this);
        binding.tvHomeAnnouncementMore.setOnClickListener(this);
    }

    /**
     * 新闻
     */
    private void initHotNews() {
        mHotNewsRvAdapter = new HotNewsRvAdapter(mHotNews);
        mHotNewsRvAdapter.setOnItemClickListener((adapter, view, position) -> {
            WebViewActivity.newInstance(getActivity(), "", mHotNews.get(position).getUrl(), 1);
            //TODO 调试WebView缓存https
//            WebViewActivity.newInstance(getActivity(), "","https://www.open-open.com/lib/view/open1392188052301.html", 1);
        });
        binding.recyclerViewHomeHotNew.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewHomeHotNew.setAdapter(mHotNewsRvAdapter);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getTutorialArea().observe(this, s -> {
            WebViewActivity.newInstance(getActivity(), "", s.getUrl(), 1);
        });
        viewModel.getHomePageBean().observe(this, s -> {
            if (s != null) {
                if (s.banners != null) {
                    mBannerBeans.clear();
                    mBannerBeans.addAll(s.banners);
                    mImageAdapter.notifyDataSetChanged();
                }
                if (s.strategies != null) {
                    mStrategies.clear();
                    mStrategies.addAll(s.strategies);
                    mHomePloyRVAdapter.notifyDataSetChanged();
//                    scrollRefresh(binding.recyclerViewHomeStrategyRecommendation);
                }
                if (s.informationList != null) {
                    mHotNews.clear();
                    mHotNews.addAll(s.informationList);
                    mHotNewsRvAdapter.notifyDataSetChanged();
                }
                if (s.notice != null) {
                    mAnnouncements.clear();
                    mAnnouncements.add(s.notice.title);
                    mTutorialAreaBean = s.notice;
                    initAnnouncement();
                }
                binding.smartRefresh.finishRefresh();
            }

        });
        refreshMain();
    }

    /**
     * 策略
     */
    private void initStrategyRecommendation() {
        mHomePloyRVAdapter = new HomePloyRVAdapter(mStrategies);
        mHomePloyRVAdapter.addChildClickViewIds(R.id.item_home_ploy, R.id.shadow_layout, R.id.tv_ploy_annualized_rate);
        mHomePloyRVAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            WebViewActivity.newInstance(getActivity(), "", mStrategies.get(position).getUrl(), 0, mStrategies.get(position).strategyId);
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        binding.recyclerViewHomeStrategyRecommendation.setLayoutManager(linearLayoutManager);
        binding.recyclerViewHomeStrategyRecommendation.setAdapter(mHomePloyRVAdapter);
        binding.recyclerViewHomeStrategyRecommendation.getRecycledViewPool().setMaxRecycledViews(0,0);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.recyclerViewHomeStrategyRecommendation);
        //滑动缩放
//        binding.recyclerViewHomeStrategyRecommendation.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                scrollRefresh(recyclerView);
//            }
//        });

    }

    /**
     * @Desc: 滑动缩放，类似画廊效果
     * @author: admin wsj
     * @Date: 2021/6/11 1:55 PM
     */
//    private void scrollRefresh(@NonNull RecyclerView recyclerView) {
//        int childCount = recyclerView.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = recyclerView.getChildAt(i);
//            int left = child.getLeft();
//            int paddingStart = recyclerView.getPaddingStart();
//            // 遍历recyclerView子项，以中间项左侧偏移量为基准进行缩放
//            float bl = Math.min(1, Math.abs(left - paddingStart) * 1f / child.getWidth());
//            float scale = MAX_SCALE - bl * (MAX_SCALE - MIN_SCALE);
//            child.setScaleY(scale);
//        }
//    }

    /**
     * 公告
     */
    private void initAnnouncement() {
        binding.stvHomeAnnouncement.setList(mAnnouncements);
        binding.stvHomeAnnouncement.setOnClickListener(this);
        binding.stvHomeAnnouncement.startScroll();
    }

    private void initBanner() {
        //        指示器
        RectangleIndicator rectangleIndicator = new RectangleIndicator(getContext());
        binding.bannerHome.setIndicator(rectangleIndicator);
        mImageAdapter = new ImageAdapter(mBannerBeans);
        mImageAdapter.setOnBannerListener((data, position) -> {
            BannerBean bannerBean = (BannerBean) data;
//            if (TextUtils.isEmpty(bannerBean.getConHref())) {
//                ToastUtils.showShort("没有跳转链接");
//                return;
//            }
            if (bannerBean.getExpand() == null || TextUtils.isEmpty(bannerBean.getExpand().type)) {
                WebViewActivity.newInstance(getActivity(), "", bannerBean.getConHref(), 1);
            } else if (BannerBean.STRATEGY.equals(bannerBean.getExpand().type)) {
                if (TextUtils.isEmpty(bannerBean.getExpand().strategyId)) {
                    ToastUtils.showShort("缺少必备参数strategyId");
                    return;
                }
                WebViewActivity.newInstance(getActivity(), "", bannerBean.getConHref(), 0, bannerBean.getExpand().strategyId);
            }
        });
        binding.bannerHome.setAdapter(mImageAdapter);
        //添加透明效果(画廊配合透明效果更棒)
//        binding.bannerHome.addPageTransformer(new AlphaPageTransformer());
        //添加画廊效果
//        binding.bannerHome.setBannerGalleryEffect(0, 0);
        //设置轮播间隔时间
//        binding.bannerHome.setLoopTime(3000);
        binding.bannerHome.start();
        binding.bannerHome.addBannerLifecycleObserver(this);
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    public String setTitleBar() {
        return "币增AI平台";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_home_strategy_recommendation_more:
                ((MainActivity) getActivity()).setSelected(1);
                break;
            case R.id.tv_home_hot_new_more:
                ((MainActivity) getActivity()).setSelected(2);
                break;
            case R.id.iv_home_quantitative_introduction:
                viewModel.requestTutorialArea(Content.QUANTITATIVE_INTRODUCTION);
                break;
            case R.id.tv_home_buy_coins_guide:
                viewModel.requestTutorialArea(Content.BUY_COINS_GUIDE);
                break;
            case R.id.tv_home_security:
                viewModel.requestTutorialArea(Content.SECURITY);
                break;
            case R.id.stv_home_announcement:
//                Intent intent = new Intent(getActivity(), WebViewActivity.class);
//                intent.putExtra(Content.PARAMS_TITLE, "");
//                intent.putExtra(Content.PARAMS_URL, mTutorialAreaBean.getUrl());
//                intent.putExtra(Content.TYPE, 1);
//                startActivity(intent);
                WebViewActivity.newInstance(getActivity(), "", mTutorialAreaBean.getUrl(), 1);
                break;
            case R.id.tv_home_announcement_more:
                startActivity(NoticeListActivity.class);
                break;
        }
    }
}
