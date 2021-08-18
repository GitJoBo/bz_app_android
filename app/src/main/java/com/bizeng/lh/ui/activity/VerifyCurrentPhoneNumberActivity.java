package com.bizeng.lh.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.databinding.ActivityVerifyCurrentPhoneNumberBinding;
import com.bizeng.lh.utils.MMKVUtils;
import com.bizeng.lh.viewmodel.ChangePhoneViewModel;
import com.wzq.mvvmsmart.utils.ToastUtils;

/**
 * @Desc: 验证当前手机号
 * @author: admin wsj
 * @Date: 2021/5/20 10:48 AM
 */
public class VerifyCurrentPhoneNumberActivity extends BaseActivity<ActivityVerifyCurrentPhoneNumberBinding, ChangePhoneViewModel> implements View.OnClickListener {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_verify_current_phone_number;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        binding.btnNext.setOnClickListener(this);
        binding.tvSendCode.setOnClickListener(this);
        binding.tvCurrentPhoneValue.setText(MMKVUtils.getInstance().getUserPhoneHide());
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getCodeCountDown().observe(this, integer -> {
            if (integer > 0) {
                // 当计时结束时， 恢复按钮的状态
                binding.tvSendCode.setEnabled(false);
                binding.tvSendCode.setClickable(false);
                binding.tvSendCode.setText(integer + "s");
            } else {
                // 当计时结束时， 恢复按钮的状态
                binding.tvSendCode.setEnabled(true);
                binding.tvSendCode.setClickable(true);
                //phoneEdit.setEnabled(true);
                binding.tvSendCode.setText(R.string.login_send_code);
            }
        });
        viewModel.nextChangePhone.observe(this, s -> {
            startActivity(ChangePhoneActivity.class);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (binding.etCode.getText() == null || TextUtils.isEmpty(binding.etCode.getText().toString())) {
                    ToastUtils.showShort("请输入验证码");
                    return;
                }
                viewModel.requestNextChangPhone(binding.etCode.getText().toString());
                break;
            case R.id.tv_send_code:
                viewModel.requestSendVerificationCodeFromCurrentMobilePhone();
                break;
        }
    }

    @Override
    public String setTitleBar() {
        return "更换手机号";
    }
}
