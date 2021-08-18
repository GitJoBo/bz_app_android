package com.bizeng.lh.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.PloyRVAdapter;
import com.bizeng.lh.base.BaseFragment;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.StrategyBean;
import com.bizeng.lh.databinding.FragmentStrategyBinding;
import com.bizeng.lh.interfaces.MainRefreshInterface;
import com.bizeng.lh.ui.activity.ApiManagerActivity;
import com.bizeng.lh.ui.activity.LoginNewActivity;
import com.bizeng.lh.ui.activity.ReportCenterActivity;
import com.bizeng.lh.ui.activity.StrategyOperationsCenterActivity;
import com.bizeng.lh.ui.activity.WebViewActivity;
import com.bizeng.lh.viewmodel.StrategyViewModel;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.wzq.mvvmsmart.utils.LiveEventBusKeys;
import com.wzq.mvvmsmart.utils.resource.Status;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import java.util.ArrayList;
import java.util.List;


/**
 * @Desc: 策略专区 AI量化
 * @author: admin wsj
 * @Date: 2021/4/18 12:23 AM
 */
public class StrategyFragment extends BaseFragment<FragmentStrategyBinding, StrategyViewModel> implements MainRefreshInterface, View.OnClickListener {
    List<StrategyBean> mList = new ArrayList<>();
    private PloyRVAdapter mPloyRVAdapter;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_strategy;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        UltimateBarX.addStatusBarTopPadding(binding.viewTitle);
        binding.smartRefresh.setEnableLoadMore(false);
        binding.smartRefresh.setOnRefreshListener(refreshLayout -> {
            binding.smartRefresh.finishRefresh(Content.DEFAULT_WAITING_TIMEOUT);
            refreshMain();
        });
        binding.ivBgPloyStrategyOperationsCenter.setOnClickListener(this);
        binding.tvStrategyOperationsCenter.setOnClickListener(this);
        binding.tvStrategyOperationsCenter2.setOnClickListener(this);
        binding.ivBgPloyReportForm.setOnClickListener(this);
        binding.tvPloyReportForm.setOnClickListener(this);
        binding.tvPloyReportForm2.setOnClickListener(this);
        binding.ivBgPloyExchangeApiManagement.setOnClickListener(this);
        binding.tvPloyExchangeApiManagement.setOnClickListener(this);
        binding.tvPloyExchangeApiManagement2.setOnClickListener(this);
        binding.recyclerviewPloy.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerviewPloy.getRecycledViewPool().setMaxRecycledViews(0, 0);
//        binding.recyclerviewPloy.setItemViewCacheSize(0);
        mPloyRVAdapter = new PloyRVAdapter(mList);
        binding.recyclerviewPloy.setAdapter(mPloyRVAdapter);
        mPloyRVAdapter.addChildClickViewIds(R.id.item_ploy, R.id.shadow_layout, R.id.tv_ploy_annualized_rate);
        mPloyRVAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            WebViewActivity.newInstance(getActivity(), "", mList.get(position).getUrl(), 0, mList.get(position).strategyId);
        });
        binding.btnPloyLogin.setOnClickListener(this);
        binding.tvPloyReportForm.setOnClickListener(this);
        LiveEventBus.get(LiveEventBusKeys.LOGIN_REFRESH).observe(this, o -> {
            refresh();
        });
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getLoginUer().observe(this, user -> {
            if (user == null) {
                binding.groupLogin.setVisibility(View.GONE);
                binding.groupNotLogin.setVisibility(View.VISIBLE);
            } else {
                binding.groupLogin.setVisibility(View.VISIBLE);
                binding.groupNotLogin.setVisibility(View.GONE);
            }
            binding.smartRefresh.finishRefresh();
        });
        viewModel.getFeaturedStrategyList().observe(this, s -> {
            if (s.status == Status.SUCCESS) {
                mList.clear();
                mList.addAll(s.data.getRows());
                //移到此处设置是为了解决首次加载会显示空布局
                mPloyRVAdapter.setEmptyView(R.layout.item_empty);
                mPloyRVAdapter.notifyDataSetChanged();
                binding.smartRefresh.finishRefresh();
            }
        });
        viewModel.getLoginCheck().observe(this, o -> {
            switch (o) {
                case StrategyViewModel.API:
                    startActivity(ApiManagerActivity.class);
                    break;
                case StrategyViewModel.CENTER:
                    startActivity(StrategyOperationsCenterActivity.class);
                    break;
            }
        });
        refresh();
    }

    private void refresh() {
        viewModel.requestLoginUser();
        viewModel.requestFeaturedStrategyList();
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    public String setTitleBar() {
        return "AI量化";
    }

    @Override
    public void refreshMain() {
        viewModel.requestFeaturedStrategyList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_bg_ploy_exchange_api_management:
            case R.id.tv_ploy_exchange_api_management:
            case R.id.tv_ploy_exchange_api_management2:
                viewModel.requestLoginCheck(StrategyViewModel.API);
                break;
            case R.id.iv_bg_ploy_strategy_operations_center:
            case R.id.tv_strategy_operations_center:
            case R.id.tv_strategy_operations_center2:
                viewModel.requestLoginCheck(StrategyViewModel.CENTER);
                break;
            case R.id.btn_ploy_login:
//                startActivity(LoginActivity.class);
                startActivity(LoginNewActivity.class);
                break;
            case R.id.iv_bg_ploy_report_form:
            case R.id.tv_ploy_report_form2:
            case R.id.tv_ploy_report_form:
                startActivity(ReportCenterActivity.class);
                break;
        }
    }
}
