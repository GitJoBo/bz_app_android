package com.bizeng.lh.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.FragmentViewPager;
import com.bizeng.lh.adapter.MyPointCardAdapter;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.databinding.LayoutRecyclerviewViewpager2Binding;
import com.bizeng.lh.ui.fragment.WithdrawalRecordFragment;
import com.bizeng.lh.viewmodel.WithdrawalRecordViewModel;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 提币记录
 * @author: admin wsj
 * @Date: 2021/7/1 10:55 AM
 */
public class WithdrawalRecordActivity extends BaseActivity<LayoutRecyclerviewViewpager2Binding, WithdrawalRecordViewModel> {
    private final List<Fragment> mFragments = new ArrayList<>();
    private MyPointCardAdapter mMyPointCardAdapter;

    @Override
    public String setTitleBar() {
        return "提币记录";
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.layout_recyclerview_viewpager2;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mMyPointCardAdapter = new MyPointCardAdapter(viewModel.getSelected());
        binding.recyclerViewTitle.setAdapter(mMyPointCardAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        binding.recyclerViewTitle.setLayoutManager(layoutManager);
        mMyPointCardAdapter.setOnItemClickListener((adapter, view, position) -> {
            mMyPointCardAdapter.setSelectItem(position);
            mMyPointCardAdapter.notifyDataSetChanged();
            binding.viewPager2.setCurrentItem(position, false);
        });
        if (savedInstanceState == null) {
            mFragments.add(WithdrawalRecordFragment.newInstance(-1));
            mFragments.add(WithdrawalRecordFragment.newInstance(0));
            mFragments.add(WithdrawalRecordFragment.newInstance(1));
        } else {
            mFragments.addAll(getSupportFragmentManager().getFragments());
        }
        binding.viewPager2.setUserInputEnabled(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.setAdapter(new FragmentViewPager(getSupportFragmentManager(), getLifecycle(), mFragments));
        binding.viewPager2.setCurrentItem(0,false);
    }
}
