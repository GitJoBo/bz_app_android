package com.bizeng.lh.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bizeng.lh.R;
import com.bizeng.lh.adapter.LimitedTimeOfferAdapter;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.LimitedTimeOfferBean;
import com.bizeng.lh.databinding.ActivityLimitedTimeOfferBinding;
import com.bizeng.lh.viewmodel.LimitedTimeOfferViewModel;
import com.chad.library.BR;
import com.wzq.mvvmsmart.utils.ConvertUtils;
import com.wzq.mvvmsmart.utils.KLog;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 限时优惠
 * @author: admin wsj
 * @Date: 2021/5/13 5:30 PM
 */
public class LimitedTimeOfferActivity extends BaseActivity<ActivityLimitedTimeOfferBinding, LimitedTimeOfferViewModel> implements View.OnClickListener {

    private LimitedTimeOfferAdapter mLimitedTimeOfferAdapter;
    List<LimitedTimeOfferBean> mList = new ArrayList<>();
    int mTitleHeight = ConvertUtils.dp2px(200);

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_limited_time_offer;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getListOfBonusPoints().observe(this, s -> {
            mList.clear();
            mList.addAll(s);
            mLimitedTimeOfferAdapter.notifyDataSetChanged();
        });
        viewModel.requestListOfBonusPoints();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initTitle();
        mLimitedTimeOfferAdapter = new LimitedTimeOfferAdapter(mList);
        binding.recyclerViewLimitedTimeOffer.setAdapter(mLimitedTimeOfferAdapter);
        binding.recyclerViewLimitedTimeOffer.setLayoutManager(new LinearLayoutManager(this));
        mLimitedTimeOfferAdapter.addChildClickViewIds(R.id.tv_recharge_now);
        mLimitedTimeOfferAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Intent intent = new Intent(this, RechargeCardActivity.class);
            startActivityForResult(intent, Content.ACTIVITY_REQUEST_CODE);
        });
    }

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_recharge:
//                Intent intent = new Intent(this, RechargeCardActivity.class);
//                startActivityForResult(intent, Content.ACTIVITY_REQUEST_CODE);
//                break;
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Content.ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                finish();
            }
        }
    }

    @Override
    public String setTitleBar() {
        return "限时优惠";
    }

    @Override
    public void initImmersive() {
        UltimateBarX.with(this)                   // 在当前 Activity/Fragment 生效
//                .transparent()
                .fitWindow(false)                      // 是否侵入状态栏 （true: 不侵入）
//                .color(Color.WHITE)                   // 状态栏颜色（色值）
//                .colorRes(R.color.deepSkyBlue)        // 状态栏颜色（资源 id）
//                .drawableRes(R.drawable.bg_gradient)  // 状态栏背景（drawable 资源）
                .light(false)                         // light 模式（true: 字体变灰）
                .applyStatusBar();                   // 应用到状态栏
//        UltimateBarX.addStatusBarTopPadding(targetView);
//        UltimateBarX.addNavigationBarBottomPadding(targetView);
    }

    @Override
    protected void initTitle() {
        mToolbar = findViewById(com.wzq.mvvmsmart.R.id.main_bar);
        mTvTitle = findViewById(com.wzq.mvvmsmart.R.id.tv_title);
        UltimateBarX.addStatusBarTopPadding(mToolbar);
        if (mToolbar != null) {
            mToolbar.setNavigationOnClickListener(v -> finish());
        }
        if (mTvTitle != null) {
            mTvTitle.setText(setTitleBar());
            mTvTitle.setTextColor(getResources().getColor(com.wzq.mvvmsmart.R.color.white));
        }
        binding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY <= 0) {
                binding.mainBarBg.setImageAlpha(0);
                binding.mainBarBg.setVisibility(View.INVISIBLE);
            } else if (scrollY > 0 && scrollY < mTitleHeight) {
                binding.mainBarBg.setVisibility(View.VISIBLE);
                //获取渐变率
                float scale = (float) scrollY / mTitleHeight;
                //获取渐变数值
                float alpha = (1.0f * scale);
                binding.mainBarBg.setAlpha(alpha);
            } else {
                binding.mainBarBg.setAlpha(1f);
                binding.mainBarBg.setVisibility(View.VISIBLE);
            }

            if (scrollY > oldScrollY) {
                KLog.d("=====", "下滑");
            }

            if (scrollY < oldScrollY) {
                KLog.d("=====", "上滑");
            }

            if (scrollY == 0) {
                KLog.d("=====", "滑倒顶部");
                binding.mainBarBg.setAlpha(0);
                binding.mainBarBg.setVisibility(View.INVISIBLE);
            }

            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                KLog.d("=====", "滑倒底部");
            }
        });
    }
}
