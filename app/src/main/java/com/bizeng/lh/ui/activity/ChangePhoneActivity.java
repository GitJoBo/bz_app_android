package com.bizeng.lh.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.databinding.ActivityChangePhoneBinding;
import com.bizeng.lh.viewmodel.ChangePhoneViewModel;
import com.wzq.mvvmsmart.utils.RegexUtils;
import com.wzq.mvvmsmart.utils.ToastUtils;

/**
 * @Desc: 更换手机号
 * @author: admin wsj
 * @Date: 2021/4/30 1:18 PM
 */
public class ChangePhoneActivity extends BaseActivity<ActivityChangePhoneBinding, ChangePhoneViewModel> implements View.OnClickListener {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_change_phone;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getCodeCountDown().observe(this, integer -> {
            if (integer > 0) {
                binding.tvSendCode.setText(integer + "s");
                binding.tvSendCode.setEnabled(false);
                binding.tvSendCode.setClickable(false);
            } else {
                // 当计时结束时， 恢复按钮的状态
                binding.tvSendCode.setEnabled(true);
                binding.tvSendCode.setClickable(true);
                //phoneEdit.setEnabled(true);
                binding.tvSendCode.setText(R.string.login_send_code);
            }
        });
        viewModel.changeMobilePhoneNumber.observe(this, s -> {
            startActivity(MainActivity.class);
            finish();
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        binding.btnOk.setOnClickListener(this);
        binding.tvSendCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_ok:
                String etCurrentPhoneValue = binding.etCurrentPhoneValue.getText().toString();
                if (TextUtils.isEmpty(etCurrentPhoneValue)) {
                    ToastUtils.showShort("请输入新手机号");
                    return;
                }
                String etCode = binding.etCode.getText().toString();
                if (TextUtils.isEmpty(etCode)) {
                    ToastUtils.showShort("请输入验证码");
                    return;
                }
                viewModel.requestChangeMobilePhoneNumber(etCode, etCurrentPhoneValue);
                break;
            case R.id.tv_send_code:
                String etCurrentPhoneValue2 = binding.etCurrentPhoneValue.getText().toString();
                if (TextUtils.isEmpty(etCurrentPhoneValue2)) {
                    ToastUtils.showShort("请输入新手机号");
                    return;
                }
                if (!RegexUtils.isMobileSimple(etCurrentPhoneValue2)) {
                    ToastUtils.showShort("新手机号格式有误");
                    return;
                }
                viewModel.requestSendVerificationCodeOnNewPhone(etCurrentPhoneValue2);
                break;
        }
    }

    @Override
    public String setTitleBar() {
        return "更换手机号";
    }
}
