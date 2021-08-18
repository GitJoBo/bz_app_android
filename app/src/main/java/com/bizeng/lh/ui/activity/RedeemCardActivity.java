package com.bizeng.lh.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.LimitedTimeOfferBean;
import com.bizeng.lh.databinding.ActivityRedeemCardBinding;
import com.bizeng.lh.utils.BigDecimalUtils;
import com.bizeng.lh.utils.MMKVUtils;
import com.bizeng.lh.utils.listener.MoneyValueFilter;
import com.bizeng.lh.viewmodel.RedeemCardViewModel;
import com.liys.dialoglib.AnimationsType;
import com.liys.dialoglib.UniversalDialog;
import com.wzq.mvvmsmart.utils.ToastUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 兑换点卡
 * @author: admin wsj
 * @Date: 2021/6/30 4:52 PM
 */
public class RedeemCardActivity extends BaseActivity<ActivityRedeemCardBinding, RedeemCardViewModel> implements View.OnClickListener {
    private TextView mTvSendCode;
    private UniversalDialog mUniversalDialog;
    List<LimitedTimeOfferBean> mList = new ArrayList<>();
    private BigDecimal mOver;
    private BigDecimal mGiveProportion;
    //增点 + 增上加赠
    private BigDecimal mGiveProportionValue;
    private BigDecimal mAddBonus;
    //增上加赠
    private BigDecimal mAddBonusValue;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_redeem_card;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public String setTitleBar() {
        return "兑换点卡";
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mOver = (BigDecimal) getIntent().getSerializableExtra(Content.KEY);
        if (mOver == null) {
            mOver = new BigDecimal(0);
        }
        binding.tvOver.setText(String.format(getString(R.string.tv_over), mOver));
        binding.tvAll.setOnClickListener(this);
        binding.tvSubmit.setOnClickListener(this);
        binding.cwetNum.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(2), new InputFilter.LengthFilter(9)});
        binding.cwetNum.addCommonTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    BigDecimal bigDecimal = new BigDecimal(s.toString());
                    //能输入的最大值是当前最大余额mOver 2021-8-4按文档要求 去除验证，可输入超过余额
//                    if (bigDecimal.compareTo(mOver) > 0) {
//                        bigDecimal = mOver;
//                        String s1 = bigDecimal.toString();
//                        binding.cwetNum.setText(bigDecimal.toString());
//                        binding.cwetNum.setSelection(s1.length());
//                    }
                    // 根据规则计算赠点  实际数量 = 兑换数量 + 赠点（赠点=兑换数量*相应的区间比例）
                    if (mList.size() > 0) {
                        for (int i = 0; i < mList.size(); i++) {
                            LimitedTimeOfferBean limitedTimeOfferBean = mList.get(i);
                            int compareTo = bigDecimal.compareTo(limitedTimeOfferBean.getMinMoneyBigDecimal());
                            if (compareTo == 0) {
                                //相等，直接用此比例
                                mGiveProportion = limitedTimeOfferBean.getGiveProportionBigDecimal();
                                break;
                            } else if (compareTo == -1) {
                                //bigDecimal小于limitedTimeOfferBean.getMinMoneyBigDecimal() 若i>0取i-1的比例，若i = 0，则没有增点
                                if (i > 0) {
                                    limitedTimeOfferBean = mList.get(i - 1);
                                    mGiveProportion = limitedTimeOfferBean.getGiveProportionBigDecimal();
                                } else {
                                    mGiveProportion = new BigDecimal(0);
                                }
                                break;
                            }
                        }
                    }
                    if (new BigDecimal(0).compareTo(mGiveProportion) < 0) {
                        mAddBonusValue = bigDecimal.multiply(mAddBonus);
                        mGiveProportionValue = bigDecimal.multiply(mGiveProportion).add(mAddBonusValue);
                        BigDecimal handlingFee = bigDecimal.add(mGiveProportionValue);
                        binding.tvHandlingFee.setText(BigDecimalUtils.getInstance().getBigDecimal(handlingFee).toString());
                        binding.tvAddPoint.setText("含赠送点数  " + BigDecimalUtils.getInstance().getBigDecimal(mGiveProportionValue).toString() + "点");
                        binding.tvAddPoint2.setText("加赠点数  " + BigDecimalUtils.getInstance().getBigDecimal(mAddBonusValue).toString() + "点");
                    } else {
                        extracted();
                    }
                } catch (Exception e) {
                    extracted();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void extracted() {
        String s = binding.cwetNum.getText().toString();
        if (TextUtils.isEmpty(s)) {
            binding.tvHandlingFee.setText("0.00");
        } else {
            binding.tvHandlingFee.setText(BigDecimalUtils.getInstance().getBigDecimal(s).toString());
        }
        binding.tvAddPoint.setText("含赠送点数  0.00点");
        binding.tvAddPoint2.setText("加赠点数  0.00点");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                String money = binding.cwetNum.getText().toString();
                if (TextUtils.isEmpty(money) || "0.00".equals(money)) {
                    ToastUtils.showShort("请填写兑换金额");
                    return;
                }
                dialogMore(money);
                break;
            case R.id.tv_all:
                String s = BigDecimalUtils.getInstance().getBigDecimal(mOver).toString();
                binding.cwetNum.setText(s);
                binding.cwetNum.setSelection(s.length());
                break;
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getCodeCountDown().observe(this, integer -> {
            if (mTvSendCode == null) {
                return;
            }
            if (integer > 0) {
                mTvSendCode.setText(integer + "s");
                mTvSendCode.setEnabled(false);
                mTvSendCode.setClickable(false);
            } else {
                // 当计时结束时， 恢复按钮的状态
                mTvSendCode.setEnabled(true);
                mTvSendCode.setClickable(true);
                //phoneEdit.setEnabled(true);
                mTvSendCode.setText(R.string.login_send_code);
            }
        });
        viewModel.redeemCard.observe(this, s -> {
            ToastUtils.showShort("兑换成功");
            finish();
        });
        viewModel.listOfBonusPoints.observe(this, s -> {
            mList.clear();
            mList.addAll(s);
        });
        viewModel.addPointAddBonus.observe(this, s -> {
            mAddBonus = s;
        });
        viewModel.requestRequestAddPointAddBonus();
        viewModel.requestListOfBonusPoints();
    }

    private void dialogMore(String money) {
        if (mUniversalDialog == null) {
            mUniversalDialog = UniversalDialog.newInstance(this, R.layout.commom_dialog_verification_code);
            mUniversalDialog.setBgRadius(12, 12, 0, 0);
            mUniversalDialog.setGravity(Gravity.BOTTOM)
                    .setAnimations(AnimationsType.BOTTOM)
                    .setWidthRatio(1).show();
            mUniversalDialog.setCancelable(false);
            //宽全屏
            mUniversalDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        TextView tv_title = mUniversalDialog.getView(R.id.tv_title);
        TextView tv_phone = mUniversalDialog.getView(R.id.tv_phone);
        EditText et_code = mUniversalDialog.getView(R.id.et_code);
        tv_title.setText("兑换");
        mTvSendCode = mUniversalDialog.getView(R.id.tv_send_code);
        tv_phone.setText(MMKVUtils.getInstance().getUserPhoneHide());
        mUniversalDialog.setOnClickListener((v, universalDialog) -> {
            switch (v.getId()) {
                case R.id.iv_close:
                    mUniversalDialog.dismiss();
                    break;
                case R.id.tv_send_code:
                    viewModel.sendCode();
                    break;
                case R.id.tv_withdraw:
                    String code = et_code.getText().toString();
                    if (TextUtils.isEmpty(code)) {
                        ToastUtils.showShort("请填写验证码");
                        return;
                    }
                    viewModel.requestRedeemCard(code, money);
                    break;
            }
        }, R.id.iv_close, R.id.tv_send_code, R.id.tv_withdraw);
        mUniversalDialog.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUniversalDialog != null) {
            if (mUniversalDialog.isShowing()) {
                mUniversalDialog.dismiss();
            }
            mUniversalDialog = null;
        }
    }
}
