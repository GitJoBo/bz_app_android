package com.bizeng.lh.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.FragmentViewPager;
import com.bizeng.lh.adapter.MyPointCardAdapter;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.databinding.ActivityMyPointCardBinding;
import com.bizeng.lh.ui.fragment.MyPointCardFragment;
import com.bizeng.lh.viewmodel.MyPointCardViewModel;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 我的点卡
 * @author: admin wsj
 * @Date: 2021/4/30 1:18 PM
 */
public class MyPointCardActivity extends BaseActivity<ActivityMyPointCardBinding, MyPointCardViewModel> implements View.OnClickListener {
    private MyPointCardAdapter mMyPointCardAdapter;
    private final List<Fragment> mFragments = new ArrayList<>();
    private int mCurrentlySelected = 0;


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_my_point_card;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        binding.tvRecharge.setOnClickListener(this);
        mMyPointCardAdapter = new MyPointCardAdapter(viewModel.getSelected());
        binding.recyclerViewPointCard.setAdapter(mMyPointCardAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        binding.recyclerViewPointCard.setLayoutManager(layoutManager);
        if (savedInstanceState == null) {
            mFragments.add(MyPointCardFragment.newInstance(-1));
            mFragments.add(MyPointCardFragment.newInstance(0));
            mFragments.add(MyPointCardFragment.newInstance(1));
            mFragments.add(MyPointCardFragment.newInstance(2));
        } else {
            mFragments.addAll(getSupportFragmentManager().getFragments());
        }

        mMyPointCardAdapter.setOnItemClickListener((adapter, view, position) -> {
            mMyPointCardAdapter.setSelectItem(position);
            mMyPointCardAdapter.notifyDataSetChanged();
            binding.viewPager2.setCurrentItem(position, false);
        });


        binding.viewPager2.setUserInputEnabled(false);
        binding.viewPager2.setOffscreenPageLimit(4);
        binding.viewPager2.setAdapter(new FragmentViewPager(getSupportFragmentManager(), getLifecycle(), mFragments));
        binding.viewPager2.setCurrentItem(0, false);
    }

    @Override
    public String setTitleBar() {
        return "我的点卡";
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getWalletInformation().observe(this, s -> {
            binding.tvPointCardValue.setText(s.getAllMoney().toString());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.requestWalletInformation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_recharge:
                startActivity(LimitedTimeOfferActivity.class);
                break;
        }
    }
}
