package com.bizeng.lh.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.FragmentViewPager;
import com.bizeng.lh.adapter.MyPointCardAdapter;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.databinding.LayoutRecyclerviewViewpager2Binding;
import com.bizeng.lh.ui.fragment.ProfitSharingRecordFragment;
import com.bizeng.lh.viewmodel.ProfitSharingRecordViewModel;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 返利记录
 * @author: admin wsj
 * @Date: 2021/6/25 2:12 PM
 */
public class ProfitSharingRecordActivity extends BaseActivity<LayoutRecyclerviewViewpager2Binding, ProfitSharingRecordViewModel> {
    //    private final List<String> mTitles = new ArrayList<>();
    private final List<Fragment> mFragments = new ArrayList<>();
    private MyPointCardAdapter mMyPointCardAdapter;

    @Override
    public String setTitleBar() {
        return "返利记录";
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
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
//        binding.recyclerViewTitle.setLayoutManager(staggeredGridLayoutManager);
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
            mFragments.add(ProfitSharingRecordFragment.newInstance(-1));
            mFragments.add(ProfitSharingRecordFragment.newInstance(0));
            mFragments.add(ProfitSharingRecordFragment.newInstance(1));
        } else {
            mFragments.addAll(getSupportFragmentManager().getFragments());
        }
        binding.viewPager2.setUserInputEnabled(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.setAdapter(new FragmentViewPager(getSupportFragmentManager(), getLifecycle(), mFragments));
        binding.viewPager2.setCurrentItem(0);
//        TextView tvAll = new TextView(this);
//        tvAll.setText("全部");
//        tvAll.setGravity(Gravity.CENTER);
//        TextView tvPreReceiptAndProfitSharing = new TextView(this);
//        tvPreReceiptAndProfitSharing.setText("预估返利");
//        tvPreReceiptAndProfitSharing.setGravity(Gravity.CENTER);
//        TextView tvReceiptOfProfit = new TextView(this);
//        tvReceiptOfProfit.setText("到账返利");
//        tvReceiptOfProfit.setGravity(Gravity.CENTER);
//        binding.dslTabLayout.addView(tvAll);
//        binding.dslTabLayout.addView(tvPreReceiptAndProfitSharing);
//        binding.dslTabLayout.addView(tvReceiptOfProfit);
//        if (savedInstanceState == null) {
//            mFragments.add(ProfitSharingRecordFragment.newInstance());
//            mFragments.add(ProfitSharingRecordFragment.newInstance());
//            mFragments.add(ProfitSharingRecordFragment.newInstance());
//        } else {
//            mFragments.addAll(getSupportFragmentManager().getFragments());
//        }
//        binding.viewPager2.setAdapter(new FragmentViewPager(getSupportFragmentManager(), getLifecycle(), mFragments));
//        ViewPager2Delegate.Companion.install(binding.viewPager2, binding.dslTabLayout);

    }
}
