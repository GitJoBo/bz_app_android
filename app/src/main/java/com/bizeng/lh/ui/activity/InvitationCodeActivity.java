package com.bizeng.lh.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.databinding.ActivityInvitationCodeBinding;
import com.bizeng.lh.viewmodel.InvitationCodeViewModel;
import com.wzq.mvvmsmart.utils.ToastUtils;

/**
 * @Desc: 邀请码
 * @author: admin wsj
 * @Date: 2021/4/25 11:12 AM
 */
public class InvitationCodeActivity extends BaseActivity<ActivityInvitationCodeBinding, InvitationCodeViewModel> implements View.OnClickListener {
    private int mSource;//0登录返回去主页，1其他页面返回直接关闭

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_invitation_code;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        mSource = getIntent().getIntExtra(Content.KEY, 0);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        binding.ivClose.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
    }

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    public void initViewObservable() {
        viewModel.getSubmitInvitationCode().observe(this, s -> {
            if (mSource == 0) {
                startActivity(MainActivity.class);
            }
            finish();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                if (mSource == 0) {
                    startActivity(MainActivity.class);
                } else {
                    finish();
                }
                break;
            case R.id.btn_submit:
                String s = binding.etInvitation.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToastUtils.showShort("请填写邀请码再提交");
                    return;
                }
                viewModel.requestSubmitInvitationCode(s);
                break;
        }
    }
}
