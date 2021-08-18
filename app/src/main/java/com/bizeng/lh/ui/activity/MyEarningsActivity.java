package com.bizeng.lh.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.UserWalletBean;
import com.bizeng.lh.databinding.ActivityMyEarningsBinding;
import com.bizeng.lh.viewmodel.MyEarningsViewModel;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

/**
 * @Desc: 我的收益 好友返利
 * @author: admin wsj
 * @Date: 2021/6/30 10:47 AM
 */
public class MyEarningsActivity extends BaseActivity<ActivityMyEarningsBinding, MyEarningsViewModel> implements View.OnClickListener {
    private UserWalletBean mUserWalletBean;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_my_earnings;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    public String setTitleBar() {
        return getString(R.string.friends_rebate);
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
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        initTitle();
        binding.smartRefresh.setEnableLoadMore(false);
        binding.smartRefresh.setOnRefreshListener(refreshLayout -> {
            onRefresh();
            binding.smartRefresh.finishRefresh(Content.DEFAULT_WAITING_TIMEOUT);
        });
        binding.tvWithdraw.setOnClickListener(this);
        binding.tvRedeemCard.setOnClickListener(this);
        binding.sivProfitSharingRecord.setOnClickListener(this);
        binding.sivMentionMoneyRecord.setOnClickListener(this);
        binding.sivMyTeam.setOnClickListener(this);
        binding.sivRecommendedMe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_withdraw:
//                if (mUserWalletBean == null || TextUtils.isEmpty(mUserWalletBean.getProfitMoney())) {
//                    ToastUtils.showShort("余额有误");
//                    return;
//                }
                Bundle bundle = new Bundle();
                if (mUserWalletBean != null) {
                    bundle.putSerializable(Content.KEY, mUserWalletBean.getProfitMoneyBigDecimal());
                }
                startActivity(WithdrawActivity.class, bundle);
                break;
            case R.id.tv_redeem_card:
//                if (mUserWalletBean == null || TextUtils.isEmpty(mUserWalletBean.getProfitMoney())) {
//                    ToastUtils.showShort("余额有误");
//                    return;
//                }
                Bundle bundle2 = new Bundle();
                if (mUserWalletBean != null) {
                    bundle2.putSerializable(Content.KEY, mUserWalletBean.getProfitMoneyBigDecimal());
                }
                startActivity(RedeemCardActivity.class, bundle2);
                break;
            case R.id.siv_profit_sharing_record:
                startActivity(ProfitSharingRecordActivity.class);
                break;
            case R.id.siv_mention_money_record:
                startActivity(WithdrawalRecordActivity.class);
                break;
            case R.id.siv_my_team:
                startActivity(MyTeamActivity.class);
                break;
            case R.id.siv_recommended_me:
                Intent intent = new Intent(this, InvitationCodeActivity.class);
                intent.putExtra(Content.KEY, 1);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.userWalletBean.observe(this, s -> {
            mUserWalletBean = s;
            setText(binding.tvProfitMoney, String.format("当前余额：%s U", s.getProfitMoney()), 12);
            setText(binding.tvCumulativeAdvanceReceiptValue, String.format("%s U", s.getBeforeProfitMoney()), 16);
            setText(binding.tvAccumulativeValue, String.format("%s U", s.getActualProfitMoney()), 16);
            binding.smartRefresh.finishRefresh();
        });
        viewModel.userTeam.observe(this, s -> {
            if (s.parent != null) {
                if ("0".equals(s.parent.recommendStatus)) {
                    goToSet();
                } else {
                    binding.sivRecommendedMe.setValue(s.parent.phone);
                    if ("1".equals(s.parent.recommendStatus)) {
                        binding.sivRecommendedMe.setVisibility(View.GONE);
                    } else {
                        binding.sivRecommendedMe.setVisibility(View.VISIBLE);
                        binding.sivRecommendedMe.setEnabled(false);
                        binding.sivRecommendedMe.setClickable(false);
                        binding.sivRecommendedMe.getRightImageView().setVisibility(View.GONE);
                    }
                }
            } else {
                goToSet();
            }
        });
    }

    private void goToSet() {
        binding.sivRecommendedMe.setValue("去设置");
        binding.sivRecommendedMe.setVisibility(View.VISIBLE);
        binding.sivRecommendedMe.setEnabled(true);
        binding.sivRecommendedMe.setClickable(true);
        binding.sivRecommendedMe.getRightImageView().setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    private void onRefresh() {
        viewModel.requestUserWalletBean();
        viewModel.requestUserTeam();
    }

    public void setText(TextView tv, String str, int size) {
        SpannableString spanString = new SpannableString(str);
        //构造一个改变字体颜色的Span
//        ForegroundColorSpan span = new ForegroundColorSpan(Color.YELLOW);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(size, true);
        //将这个Span应用于指定范围的字体
        spanString.setSpan(absoluteSizeSpan, str.length() - 1, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置给TextView显示出来
        tv.setText(spanString);
    }
}
