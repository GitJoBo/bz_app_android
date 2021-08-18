package com.bizeng.lh.ui.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.SelectHistoricalWithdrawalAddressAdapter;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.event.ScanEvent;
import com.bizeng.lh.databinding.ActivityHistoricalWithdrawalAddressBinding;
import com.bizeng.lh.viewmodel.HistoricalWithdrawalAddressViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 历史兑换地址选择
 * @author: admin wsj
 * @Date: 2021/7/1 11:59 AM
 */
public class HistoricalWithdrawalAddressActivity extends BaseActivity<ActivityHistoricalWithdrawalAddressBinding, HistoricalWithdrawalAddressViewModel> {
    private SelectHistoricalWithdrawalAddressAdapter mAdapter;
    private List<String> mSelectBeanList = new ArrayList<>();
    private int mType;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_historical_withdrawal_address;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public String setTitleBar() {
        return "历史兑换地址";
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mType = getIntent().getIntExtra(Content.KEY, 1);
        mAdapter = new SelectHistoricalWithdrawalAddressAdapter(mSelectBeanList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(mAdapter);
        mAdapter.addChildClickViewIds(R.id.tv_address);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
//                case R.id.cb_rv:
//                    mAdapter.singleChoice(position);
//                    ScanEvent<String> event = new ScanEvent<>();
//                    event.code = 0;
//                    event.data = mSelectBeanList.get(position).getName();
//                    EventBus.getDefault().post(event);
//                    finish();
//                    break;
                case R.id.tv_address:
                    ScanEvent<String> event = new ScanEvent<>();
                    event.code = 0;
                    event.data = mSelectBeanList.get(position);
                    EventBus.getDefault().post(event);
                    finish();
                    break;
            }
        });
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.withdrawalAddressList.observe(this, s -> {
            mSelectBeanList.clear();
            mSelectBeanList.addAll(s);
            mAdapter.notifyDataSetChanged();
        });
        viewModel.requestWithdrawalAddressList(mType);
    }
}
