package com.bizeng.lh.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.databinding.ActivityLoginNewBinding;
import com.bizeng.lh.interceptor.LoginCarrier;
import com.bizeng.lh.interceptor.LoginInterceptor;
import com.bizeng.lh.utils.MMKVUtils;
import com.bizeng.lh.utils.StringUtils;
import com.bizeng.lh.viewmodel.LoginViewModel;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.tencent.mmkv.MMKV;
import com.wzq.mvvmsmart.utils.LiveEventBusKeys;
import com.wzq.mvvmsmart.utils.ToastUtils;
import com.wzq.mvvmsmart.utils.resource.Resource;
import com.wzq.mvvmsmart.utils.resource.Status;

public class LoginNewActivity extends BaseActivity<ActivityLoginNewBinding, LoginViewModel> implements View.OnClickListener {
    private Boolean mIsRequestVerifyCode = false;
    private LoginCarrier mLoginCarrier;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login_new;
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
        binding.cb.setChecked(MMKV.defaultMMKV().decodeBool(MMKVUtils.AGREEMENT_HAS_BEEN_READ, false));
        binding.btnLogin.setOnClickListener(this);
        binding.btnLoginSendCode.setOnClickListener(this);
        binding.cb.setOnClickListener(this);
        String str = "我已阅读并同意《免责声明》，《用户协议》和《隐私政策》";
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                MMKV.defaultMMKV().encode(MMKVUtils.AGREEMENT_HAS_BEEN_READ, binding.cb.isChecked());
                binding.cb.setChecked(!binding.cb.isChecked());
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                //设置文件颜色
                ds.setColor(getResources().getColor(R.color.color_999));
                // 去掉下划线
                ds.setUnderlineText(false);
            }

        }, 0, 7, 0);
        final String tag = "《";
        //第一个出现的位置
        final int start = str.indexOf(tag);
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                viewModel.requestTutorialArea(Content.DISCLAIMER);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                //设置文件颜色
                ds.setColor(getResources().getColor(R.color.text_245));
                // 去掉下划线
                ds.setUnderlineText(false);
            }

        }, start, start + 6, 0);

        //第二个《
        final int two = StringUtils.getIndex(str, tag, 2);
        ssb.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                viewModel.requestTutorialArea(Content.USER_AGREEMENT);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                //设置文件颜色
                ds.setColor(getResources().getColor(R.color.text_245));
                // 去掉下划线
                ds.setUnderlineText(false);
            }

        }, two, two + 6, 0);
        //最后一个出现的位置
        final int end = str.lastIndexOf(tag);
        ssb.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                viewModel.requestTutorialArea(Content.PRIVACY_POLICY);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                //设置文件颜色
                ds.setColor(getResources().getColor(R.color.text_245));
                // 去掉下划线
                ds.setUnderlineText(false);
            }

        }, end, end + 6, 0);
        binding.tvAgree.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvAgree.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        // 等待接受验证码倒计时， 并刷新及时按钮的刷新
        viewModel.getCodeCountDown().observe(this, integer -> {
            if (integer > 0) {
                binding.btnLoginSendCode.setEnabled(false);
                binding.btnLoginSendCode.setClickable(false);
                binding.btnLoginSendCode.setText(integer + "s");
                mIsRequestVerifyCode = true;
            } else {
                // 当计时结束时， 恢复按钮的状态
                binding.btnLoginSendCode.setEnabled(true);
                binding.btnLoginSendCode.setClickable(true);
                //phoneEdit.setEnabled(true);
                binding.btnLoginSendCode.setText(R.string.login_send_code);
                mIsRequestVerifyCode = false;

            }
        });
        viewModel.getLogin().observe(this, userResource -> {
            binding.btnLogin.setEnabled(true);
            if (userResource.status == Status.SUCCESS) {
                LiveEventBus.get(LiveEventBusKeys.LOGIN_REFRESH).post(Resource.success(null));
                if (userResource.data.getIsFirst()) {
                    startActivity(InvitationCodeActivity.class);
                } else {
//                    startActivity(MainActivity.class);
                }
                setResult(Activity.RESULT_OK);
                mLoginCarrier = (LoginCarrier) getIntent().getParcelableExtra(LoginInterceptor.mINVOKER);
                if (mLoginCarrier != null) {
                    mLoginCarrier.invoke(LoginNewActivity.this);
                }
                finish();
            }
        });
        viewModel.tutorialArea.observe(this, s -> {
            WebViewActivity.newInstance(this, "", s.getUrl(), 1);
        });
//        viewModel.getLogin().observe(this, userResource -> {
//            switch (userResource.status) {
//                case ERROR:
//                    closeLoading();
//                    break;
//                case SUCCESS:
//                    closeLoading();
//                    LiveEventBus.get(LiveEventBusKeys.LOGIN_REFRESH).post(Resource.success(null));
//                    if (userResource.data.getIsFirst()) {
//                        startActivity(InvitationCodeActivity.class);
//                    } else {
//                        startActivity(MainActivity.class);
//                    }
//                    break;
//                case LOADING:
//                    showLoading();
//                    break;
//                default:
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        Editable phoneText = binding.etLoginPhone.getText();
        Editable codeText = binding.etLoginVerificationCode.getText();
        switch (v.getId()) {
            case R.id.btn_login:
                if (verification(phoneText)) return;
                if (codeText == null) {
                    ToastUtils.showShort(R.string.please_confirm_the_verification_code);
                    return;
                }
                viewModel.requestLogin(phoneText.toString(), codeText.toString());
                binding.btnLogin.setEnabled(false);
                break;
            case R.id.btn_login_send_code:
                if (verification(phoneText)) return;
                viewModel.requestSendVerificationCode(binding.etLoginPhone.getText().toString());
                break;
            case R.id.cb_:
                MMKV.defaultMMKV().encode(MMKVUtils.AGREEMENT_HAS_BEEN_READ, binding.cb.isChecked());
                break;
        }
    }

    private boolean verification(Editable phoneText) {
        if (!binding.cb.isChecked()) {
            ToastUtils.showShort(R.string.please_agree_to_the_agreement);
            return true;
        }
        if (TextUtils.isEmpty(phoneText)) {
            ToastUtils.showShort(R.string.please_enter_phone_number);
            return true;
        }
//        if (!RegexUtils.isMobileSimple(phoneText)) {
//            ToastUtils.showShort(R.string.the_phone_number_format_is_wrong);
//            return true;
//        }
        return false;
    }
}
