package com.bizeng.lh.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseFragment;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.VipBean;
import com.bizeng.lh.bean.event.LoginEvent;
import com.bizeng.lh.databinding.FragmentMineBinding;
import com.bizeng.lh.interceptor.LoginInterceptor;
import com.bizeng.lh.interfaces.MainRefreshInterface;
import com.bizeng.lh.ui.activity.LoginNewActivity;
import com.bizeng.lh.ui.activity.MyPointCardActivity;
import com.bizeng.lh.ui.activity.SettingActivity;
import com.bizeng.lh.ui.activity.WebViewActivity;
import com.bizeng.lh.utils.GlideUtils;
import com.bizeng.lh.utils.MMKVUtils;
import com.bizeng.lh.viewmodel.MineViewModel;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.wzq.mvvmsmart.utils.ToastUtils;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * @Desc: 我的
 * @author: admin wsj
 * @Date: 2021/4/18 12:23 AM
 */
public class MineFragment extends BaseFragment<FragmentMineBinding, MineViewModel> implements MainRefreshInterface, View.OnClickListener {
    TextView mFirstSendToMembers;
    List<VipBean> mLocationVipList;
    private String mLevelName = "PT";
    private String mLevelNameExp = "普通会员";

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_mine;
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
    public void refreshMain() {
//        ToastUtils.showShort(TAG);
        refresh();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        UltimateBarX.addStatusBarTopPadding(binding.viewMineLogo);
        initOnClick();
//        mFirstSendToMembers = binding.sivMineMyPointCard.getValueView();
//        mFirstSendToMembers.setText("首充送会员");
//        mFirstSendToMembers.setTextColor(Color.parseColor("#EBC17D"));
//        @SuppressLint("UseCompatLoadingForDrawables") Drawable img = getResources().getDrawable(R.mipmap.ic_vip);
//        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
//        mFirstSendToMembers.setCompoundDrawables(img, null, null, null);
//        mFirstSendToMembers.setCompoundDrawablePadding(15);
    }

    private void initOnClick() {
        binding.tvGoLogin.setOnClickListener(this);
        binding.ivMineLogo.setOnClickListener(this);
        binding.sivMineBzReportCenter.setOnClickListener(this);
        binding.sivMineMyPointCard.setOnClickListener(this);
        binding.sivMineMyGoodFriend.setOnClickListener(this);
        binding.sivMineQrCodeRecommendation.setOnClickListener(this);
        binding.sivMineApiManagement.setOnClickListener(this);
        binding.sivMineChangePhone.setOnClickListener(this);
        binding.sivMineContactCustomerService.setOnClickListener(this);
        binding.sivMineSetting.setOnClickListener(this);
        binding.smartRefresh.setEnableLoadMore(false);
        binding.smartRefresh.setOnRefreshListener(refreshLayout -> {
            binding.smartRefresh.finishRefresh(Content.DEFAULT_WAITING_TIMEOUT);
            refreshMain();
        });
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        mLocationVipList = viewModel.getLocationVipList();
        viewModel.getVip().observe(this, s -> {
            MMKVUtils.getInstance().setVips(s);
            mLocationVipList = s;
            binding.smartRefresh.finishRefresh();
        });
        viewModel.getUserInfo().observe(this, s -> {
            if (s != null) {
                setVipCardBg(s.vipIntegral);
                binding.tvMineNick.setText(s.nickName);
                binding.tvMineNick.setVisibility(View.VISIBLE);
                binding.tvMinePhone.setVisibility(View.VISIBLE);
                binding.tvGoLogin.setVisibility(View.GONE);
                binding.tvMinePhone.setText(String.format("手机号:%s", s.phone));
                binding.tvMinePhone.setVisibility(View.VISIBLE);
                GlideUtils.getLoad(s.headImg, binding.ivMineLogo)
                        .placeholder(R.mipmap.ic_mine_logo)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(180)))
                        .into(binding.ivMineLogo);
            }
            binding.smartRefresh.finishRefresh();
        });
        viewModel.tutorialArea.observe(this, s -> {
            WebViewActivity.newInstance(getActivity(), "", s.getUrl(), 1);
            binding.smartRefresh.finishRefresh();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        viewModel.requestVip();
        viewModel.requestUserInfo();
    }

    /**
     * 根据积分显示卡片
     *
     * @param vipIntegral 当前积分
     */
    public void setVipCardBg(String vipIntegral) {
        //距离下级还需要积分数
        int nextLevel = 0;
        if (TextUtils.isEmpty(vipIntegral)) {
            vipIntegral = "0";
        }
        int intVipIntegral = Integer.parseInt(vipIntegral);
        if (mLocationVipList != null && mLocationVipList.size() > 0) {
            int size = mLocationVipList.size();
            VipBean vipBeanMax = mLocationVipList.get(size - 1);
            if (vipBeanMax.getMinIntegral() <= intVipIntegral) {
                mLevelName = vipBeanMax.levelName;
                mLevelNameExp = vipBeanMax.levelName;
            }
            for (int i = 0; i < size; i++) {
                VipBean vipBean = mLocationVipList.get(i);
                int minIntegral = vipBean.getMinIntegral();
                if (minIntegral > intVipIntegral) {
                    nextLevel = minIntegral - intVipIntegral;
                    if (i != 0) {
                        VipBean vipBean2 = mLocationVipList.get(i - 1);
                        mLevelName = vipBean2.levelName;
                        mLevelNameExp = vipBean2.levelName;
                    } else {
                        mLevelName = "";
                        mLevelNameExp = "";
                    }
                    break;
                }
            }
        } else {
            mLevelName = "";
            mLevelNameExp = "";
            nextLevel = 100;
        }

        int backgroundResourceId = 0;
        if (mLevelName.contains("GV")) {
            binding.groupMemberDisplay.setVisibility(View.VISIBLE);
            backgroundResourceId = R.mipmap.bg_vip_2;
            setTexts("金卡会员", "距升级下一级还需" + nextLevel + "积分", Color.parseColor("#8F2900"));
        } else if (mLevelName.contains("PV")) {
            binding.groupMemberDisplay.setVisibility(View.VISIBLE);
            backgroundResourceId = R.mipmap.bg_vip_3;
            setTexts("铂金会员", "距升级下一级还需" + nextLevel + "积分", Color.parseColor("#8F1C7B"));
        } else if (mLevelName.contains("DV")) {
            binding.groupMemberDisplay.setVisibility(View.VISIBLE);
            backgroundResourceId = R.mipmap.bg_vip_4;
            setTexts("钻石会员", "距升级下一级还需" + nextLevel + "积分", Color.parseColor("#432290"));
        } else if (mLevelName.contains("MAX")) {
            binding.groupMemberDisplay.setVisibility(View.VISIBLE);
            backgroundResourceId = R.mipmap.bg_vip_5;
            setTexts("百夫长会员", "恭喜您，您的会员已升级至最高等级！", Color.parseColor("#AD9E7C"));
        } else {
            binding.groupMemberDisplay.setVisibility(View.VISIBLE);
            backgroundResourceId = R.mipmap.bg_vip_1;
            binding.tvVipName.setVisibility(View.GONE);
            binding.tvNextLevel.setVisibility(View.GONE);
            binding.tvVipExp.setVisibility(View.GONE);
            binding.tvNextLevel.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mLevelNameExp)) {
            binding.tvRank.setVisibility(View.VISIBLE);
            binding.tvRank.setText(mLevelNameExp);
        } else {
            binding.tvRank.setVisibility(View.GONE);
        }
        binding.tvVipExp.setText("当前积分：" + vipIntegral);
        binding.ivVipBg.setBackgroundResource(backgroundResourceId);
        if (backgroundResourceId == R.mipmap.bg_vip_1) {
            binding.ivVipBg.setOnClickListener(v -> {
//                startActivity(MyPointCardActivity.class);
//                LoginInterceptor.interceptor(getActivity(), "com.bizeng.lh.ui.activity.ui.activity.MyPointCardActivity", null);
                if (!MMKVUtils.getInstance().isLogin()) {
                    startActivity(LoginNewActivity.class);
                    return;
                }
                startActivity(MyPointCardActivity.class);
            });
        } else {
            binding.ivVipBg.setOnClickListener(v -> {

            });
        }
    }

    private void setTexts(String vipName, String nextLevel, int color) {
        binding.tvVipName.setText(vipName);
        binding.tvNextLevel.setText(nextLevel);
        binding.tvVipName.setTextColor(color);
        binding.tvVipExp.setTextColor(color);
        binding.tvNextLevel.setTextColor(color);
        binding.tvRank.setTextColor(color);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.siv_mine_bz_report_center:
//                if (!MMKVUtils.getInstance().isLogin()) {
//                    startActivity(LoginNewActivity.class);
//                    return;
//                }
//                startActivity(ReportCenterActivity.class);
                LoginInterceptor.interceptor(getActivity(), "com.bizeng.lh.ui.activity.ui.activity.ReportCenterActivity", null);
                break;
            case R.id.siv_mine_my_point_card:
//                if (!MMKVUtils.getInstance().isLogin()) {
//                    startActivity(LoginNewActivity.class);
//                    return;
//                }
//                startActivity(MyPointCardActivity.class);
                LoginInterceptor.interceptor(getActivity(), "com.bizeng.lh.ui.activity.ui.activity.MyPointCardActivity", null);
                break;
            case R.id.siv_mine_my_good_friend:
//                if (!MMKVUtils.getInstance().isLogin()) {
//                    startActivity(LoginNewActivity.class);
//                    return;
//                }
////                startActivity(MyGoodFriendActivity.class);
//                startActivity(MyEarningsActivity.class);
                LoginInterceptor.interceptor(getActivity(), "com.bizeng.lh.ui.activity.ui.activity.MyEarningsActivity", null);
                break;
            case R.id.siv_mine_qr_code_recommendation:
//                if (!MMKVUtils.getInstance().isLogin()) {
//                    startActivity(LoginNewActivity.class);
//                    return;
//                }
//                startActivity(QrCodeRecommendationActivity.class);
                LoginInterceptor.interceptor(getActivity(), "com.bizeng.lh.ui.activity.ui.activity.QrCodeRecommendationActivity", null);
                break;
            case R.id.siv_mine_api_management:
//                if (!MMKVUtils.getInstance().isLogin()) {
//                    startActivity(LoginNewActivity.class);
//                    return;
//                }
//                startActivity(ApiManagerActivity.class);
                LoginInterceptor.interceptor(getActivity(), "com.bizeng.lh.ui.activity.ui.activity.ApiManagerActivity", null);
                break;
            case R.id.siv_mine_change_phone:
//                if (!MMKVUtils.getInstance().isLogin()) {
//                    startActivity(LoginNewActivity.class);
//                    return;
//                }
////                startActivity(ChangePhoneActivity.class);
//                startActivity(VerifyCurrentPhoneNumberActivity.class);
                LoginInterceptor.interceptor(getActivity(), "com.bizeng.lh.ui.activity.ui.activity.VerifyCurrentPhoneNumberActivity", null);
                break;
            case R.id.siv_mine_contact_customer_service:
//                if (!MMKVUtils.getInstance().isLogin()) {
//                    startActivity(LoginNewActivity.class);
//                    return;
//                }
//                startActivity(ContactCustomerServiceActivity.class);
                viewModel.requestTutorialArea(Content.CONTACT_CUSTOMER_SERVICE);
                break;
            case R.id.siv_mine_setting:
                startActivity(SettingActivity.class);
                break;
            case R.id.tv_go_login:
            case R.id.iv_mine_logo:
                if (!MMKVUtils.getInstance().isLogin()) {
                    startActivity(LoginNewActivity.class);
                    return;
                }
                break;
        }
    }

    @Override
    public boolean isHasEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventLogin(LoginEvent loginEvent) {
        if (loginEvent.state != 1) {
            setVipCardBg("0");
            binding.ivMineLogo.setBackgroundResource(R.mipmap.ic_mine_logo2);
            binding.tvMineNick.setVisibility(View.GONE);
            binding.tvMinePhone.setVisibility(View.GONE);
            binding.tvGoLogin.setVisibility(View.VISIBLE);
            if (loginEvent.state == 3) {
                ToastUtils.showLong("您的登录信息已过期，请重新登录");
            }
        }
    }
}
