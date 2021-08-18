package com.bizeng.lh.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.PhoneAdapter;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.UserBean;
import com.bizeng.lh.databinding.ActivityMyGoodFriendBinding;
import com.bizeng.lh.viewmodel.MyGoodFriendViewModel;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 我的好友 我的团队 该用MyTeamActivity
 * @author: admin wsj
 * @Date: 2021/4/30 1:18 PM
 */
@Deprecated
public class MyGoodFriendActivity extends BaseActivity<ActivityMyGoodFriendBinding, MyGoodFriendViewModel> implements View.OnClickListener {
    private List<UserBean> mUserBeans = new ArrayList<>();
    private TextView mTvPhone;
    private PhoneAdapter mPhoneAdapter;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_my_good_friend;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mTvPhone = binding.includeRecommendedMePerson.findViewById(R.id.tv_phone);
//        binding.includeRecommendedMePerson.findViewById(R.id.tv_usdt);
        binding.recyclerViewMeRecommendedPerson.setAdapter(mPhoneAdapter = new PhoneAdapter(mUserBeans));
        binding.recyclerViewMeRecommendedPerson.setLayoutManager(new LinearLayoutManager(this));
        initTitle();
    }

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getUserTeam().observe(this, s -> {
            mUserBeans.clear();
            if (s != null) {
                if (s.childList != null) {
                    mUserBeans.addAll(s.childList);
                    mPhoneAdapter.setEmptyView(R.layout.item_empty);
                    mPhoneAdapter.notifyDataSetChanged();
                }
            }
            if (mUserBeans.size() > 0) {
                binding.tvMeRecommendedPersonValue.setVisibility(View.VISIBLE);
                binding.tvMeRecommendedPersonValue.setText(mUserBeans.size() + "人");
            } else {
                binding.tvMeRecommendedPersonValue.setVisibility(View.GONE);
            }
            if (s.parent != null) {
                if (!TextUtils.isEmpty(s.parent.phone)) {
                    mTvPhone.setText(s.parent.phone);
                }
            } else {
                mTvPhone.setText("填写邀请码");
                mTvPhone.setOnClickListener(this);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.requestUserTeam();
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
    public String setTitleBar() {
        return "我的好友";
    }

    @Override
    protected void initTitle() {
        mToolbar = findViewById(com.wzq.mvvmsmart.R.id.main_bar);
        mTvTitle = findViewById(com.wzq.mvvmsmart.R.id.tv_title);
        if (mToolbar != null) {
            mToolbar.setNavigationOnClickListener(v -> finish());
        }
        if (mTvTitle != null) {
            mTvTitle.setText(setTitleBar());
            mTvTitle.setTextColor(getResources().getColor(com.wzq.mvvmsmart.R.color.white));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_phone:
                Intent intent = new Intent(this, InvitationCodeActivity.class);
                intent.putExtra(Content.KEY, 1);
                startActivity(intent);
//                startActivity(InvitationCodeActivity.class);
                break;
        }
    }
}
