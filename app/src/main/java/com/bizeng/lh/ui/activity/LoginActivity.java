package com.bizeng.lh.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.databinding.ActivityLoginBinding;
import com.bizeng.lh.viewmodel.LoginViewModel;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.wzq.mvvmsmart.utils.LiveEventBusKeys;
import com.wzq.mvvmsmart.utils.RegexUtils;
import com.wzq.mvvmsmart.utils.ToastUtils;
import com.wzq.mvvmsmart.utils.resource.Resource;
import com.wzq.mvvmsmart.utils.resource.Status;
@Deprecated
public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements View.OnClickListener {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @Override
    public String setTitleBar() {
//        return "登录";
        return "";
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        binding.btnLogin.setOnClickListener(this);
        binding.btnLoginSendCode.setOnClickListener(this);

    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        // 等待接受验证码倒计时， 并刷新及时按钮的刷新
        viewModel.getCodeCountDown().observe(this, integer -> {
            if (integer > 0) {
                binding.btnLoginSendCode.setText(integer + "s");
                binding.btnLoginSendCode.setEnabled(false);
                binding.btnLoginSendCode.setClickable(false);
            } else {
                // 当计时结束时， 恢复按钮的状态
                binding.btnLoginSendCode.setEnabled(true);
                binding.btnLoginSendCode.setClickable(true);
                //phoneEdit.setEnabled(true);
                binding.btnLoginSendCode.setText(R.string.login_send_code);
            }
        });
        viewModel.getLogin().observe(this, userResource -> {
            if (userResource.status == Status.SUCCESS) {
                LiveEventBus.get(LiveEventBusKeys.LOGIN_REFRESH).post(Resource.success(null));
                if (userResource.data != null && userResource.data.getIsFirst()) {
                    startActivity(InvitationCodeActivity.class);
                }
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Editable phoneText = binding.etLoginPhone.getText();
        Editable codeText = binding.etLoginVerificationCode.getText();
        switch (v.getId()) {
            case R.id.btn_login:
                if (phoneText == null) {
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                if (!RegexUtils.isMobileSimple(phoneText)) {
                    ToastUtils.showShort("手机号格式有误");
                    return;
                }
                if (codeText == null) {
                    ToastUtils.showShort("请确认验证码");
                    return;
                }
                viewModel.requestLogin(phoneText.toString(), codeText.toString());
                break;
            case R.id.btn_login_send_code:
                if (phoneText == null) {
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                if (!RegexUtils.isMobileSimple(phoneText)) {
                    ToastUtils.showShort("手机号格式有误");
                    return;
                }
                binding.btnLoginSendCode.setEnabled(false);
                binding.btnLoginSendCode.setClickable(false);
                viewModel.requestSendVerificationCode(binding.etLoginPhone.getText().toString());
                break;
        }
    }
}
