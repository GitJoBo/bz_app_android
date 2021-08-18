package com.bizeng.lh.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.FeeConfiguration;
import com.bizeng.lh.bean.event.ScanEvent;
import com.bizeng.lh.databinding.ActivityWithdrawBinding;
import com.bizeng.lh.utils.BigDecimalUtils;
import com.bizeng.lh.utils.MMKVUtils;
import com.bizeng.lh.utils.listener.MoneyValueFilter;
import com.bizeng.lh.viewmodel.WithdrawViewModel;
import com.liys.dialoglib.AnimationsType;
import com.liys.dialoglib.UniversalDialog;
import com.wzq.mvvmsmart.utils.KLog;
import com.wzq.mvvmsmart.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;

/**
 * @Desc: 提币
 * @author: admin wsj
 * @Date: 2021/6/25 2:12 PM
 */
public class WithdrawActivity extends BaseActivity<ActivityWithdrawBinding, WithdrawViewModel> implements View.OnClickListener {
    private UniversalDialog mUniversalDialog;
    private TextView mTvSendCode;
    private int mType = 1;//币种类型:erc20-0;trc20-1
    private BigDecimal mOver;
    private BigDecimal mSmallestSingle = new BigDecimal(0);//最小兑换数量
    private BigDecimal mScale = new BigDecimal(0.002);//兑换手续费比例
    private BigDecimal mArrivalQuantityValue = new BigDecimal(0);//实际到账数量
    private FeeConfiguration mFeeConfiguration = null;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_withdraw;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public String setTitleBar() {
        return "提币";
    }

    @Override
    public boolean isHasEventBus() {
        return true;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
//        mOver = new BigDecimal(getIntent().getStringExtra(Content.KEY));
        mOver = (BigDecimal) getIntent().getSerializableExtra(Content.KEY);
        initView();
    }

    private void initView() {
//        DialogUtils.getInstance().showTip(this, null,
//                "为防止黑客风险或钓鱼攻击，确保您的财产安全，务必使用运营商数据流量网络，或者确认可信的自家WIFI，不能使用公共场所WIFI进行兑换操作！",
//                "我已知晓，继续兑换",
//                (v1, universalDialog) -> {
//                    universalDialog.dismiss();
//                });
        UniversalDialog dialog = UniversalDialog.newInstance(this, R.layout.commom_dialog_tip2);
        dialog.setCancelable(false);
        dialog.setWidthRatio(Content.WIDTH_RATIO);
//        dialog.setWidthRatio(0.5);
        dialog.setVisible(R.id.dialog_tv_title, View.GONE);
        TextView dialog_tv_content = dialog.getView(R.id.dialog_tv_content);
        dialog_tv_content.setText(Html.fromHtml("为防止黑客风险或钓鱼攻击，确保您的财产安全，务必使用运营商<font color=\"#ff0000\">数据流量网络</font>，或者确认可信的<font color=\"#ff0000\">自家WIFI</font>，不能使用公共场所WIFI进行提币操作！"));
        dialog.setText(R.id.dialog_btn_positive, "我已知晓，继续提币");
        dialog.show();
        dialog.getView(R.id.dialog_btn_positive).setOnClickListener(v -> {
            dialog.dismiss();
        });
        binding.tvOver.setText(String.format(getString(R.string.tv_over), mOver));
        binding.ivScan.setOnClickListener(this);
        binding.ivCopy.setOnClickListener(this);
        binding.tvWithdraw.setOnClickListener(this);
        binding.tvAll.setOnClickListener(this);
        binding.rgName.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_erc20:
                    KLog.d("erc20");
                    mType = 0;
                    initConfig();
                    break;
                case R.id.rb_trc20:
                    KLog.d("trc20");
                    mType = 1;
                    initConfig();
                    break;
            }
            calculateEstimatedHandlingFee(binding.cwetNum.getText().toString());
        });
        binding.cwetNum.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(2),new InputFilter.LengthFilter(9)});
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
                calculateEstimatedHandlingFee(s.toString());
            }
        });
    }

    /**
     * 计算预估手续费
     *
     * @param s
     */
    private void calculateEstimatedHandlingFee(String s) {
        try {
            BigDecimal bigDecimal = new BigDecimal(s);
            //能输入的最大值是当前最大余额mOver  2021-8-4按文档要求 去除验证，可输入超过余额
//            if (bigDecimal.compareTo(mOver) > 0) {
//                bigDecimal = mOver;
//                String s1 = bigDecimal.toString();
//                binding.cwetNum.setText(s1);
//                binding.cwetNum.setSelection(s1.length());
//            }
            /**
             * 手续预估 = 兑换数量 * 手续费比例
             * 注：（兑换数量 * 手续费比例）< 最小兑换数量时，手续费 = 单笔最小值
             */
            BigDecimal handlingFee = bigDecimal.multiply(mScale);
            if (handlingFee.compareTo(mSmallestSingle) < 0) {
                handlingFee = mSmallestSingle;
            }
            mArrivalQuantityValue = bigDecimal.subtract(handlingFee);
            binding.tvHandlingFee.setText(BigDecimalUtils.getInstance().getBigDecimal(handlingFee).toString());
            binding.tvArrivalQuantityValue.setText(BigDecimalUtils.getInstance().getBigDecimal(mArrivalQuantityValue).toString() + " USDT");
        } catch (Exception e) {
            binding.tvHandlingFee.setText("0.00");
            binding.tvArrivalQuantityValue.setText("0.00 USDT");
        }
    }

    private void initConfig() {
        if (mFeeConfiguration == null) {
            return;
        }
        if (mType == 0) {
            mSmallestSingle = mFeeConfiguration.singleMinERCAmount;
            mScale = mFeeConfiguration.chargeERCProportion;
        } else {
            mSmallestSingle = mFeeConfiguration.singleMinTRCAmount;
            mScale = mFeeConfiguration.chargeTRCProportion;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_scan:
                startActivity(ScanActivity.class);
                break;
            case R.id.iv_copy:
//                String clipboardContent = Utils.getClipboardContent(this);
//                if (TextUtils.isEmpty(clipboardContent)){
//                    ToastUtils.showShort("剪贴板为空");
//                    return;
//                }
//                binding.cwetAddress.setText(clipboardContent);
                Bundle bundle = new Bundle();
                bundle.putInt(Content.KEY, mType);
                startActivity(HistoricalWithdrawalAddressActivity.class, bundle);
                break;
            case R.id.tv_withdraw:
                String address = binding.cwetAddress.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    ToastUtils.showShort("请填写提币地址");
                    return;
                }
                String money = binding.cwetNum.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    ToastUtils.showShort("请填写提币数量");
                    return;
                }
                if (mArrivalQuantityValue.compareTo(new BigDecimal(0)) < 1) {
                    ToastUtils.showShort("提币数量太少");
                    return;
                }
                dialogMore(address, money);
                break;
            case R.id.tv_all:
                String s = BigDecimalUtils.getInstance().getBigDecimal(mOver).toString();
                binding.cwetNum.setText(s);
                binding.cwetNum.setSelection(s.length());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScanEvent(ScanEvent<String> event) {
        if (event != null && event.code == 0) {
            binding.cwetAddress.setText(event.data);
        }
    }

    private void dialogMore(String address, String money) {
        if (mUniversalDialog == null) {
            mUniversalDialog = UniversalDialog.newInstance(this, R.layout.commom_dialog_verification_code);
            mUniversalDialog.setBgRadius(12, 12, 0, 0);
            mUniversalDialog.setGravity(Gravity.BOTTOM)
                    .setAnimations(AnimationsType.BOTTOM)
                    .setWidthRatio(1).show();
            mUniversalDialog.setCancelable(false);
            //宽全屏
            mUniversalDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            ImageView iv_close = mUniversalDialog.getView(R.id.iv_close);
            TextView tv_title = mUniversalDialog.getView(R.id.tv_title);
            TextView tv_phone = mUniversalDialog.getView(R.id.tv_phone);
            EditText et_code = mUniversalDialog.getView(R.id.et_code);
            TextView tv_withdraw = mUniversalDialog.getView(R.id.tv_withdraw);
            tv_title.setText("提币");
            tv_withdraw.setText("确认提币");
            mTvSendCode = mUniversalDialog.getView(R.id.tv_send_code);
            tv_phone.setText(MMKVUtils.getInstance().getUserPhoneHide());
            mUniversalDialog.setOnClickListener((v, universalDialog) -> {
                switch (v.getId()) {
                    case R.id.iv_close:
                        mUniversalDialog.dismiss();
                        break;
                    case R.id.tv_send_code:
//                        ToastUtils.showShort("发送验证码接口");
                        viewModel.requestSendVerificationCodeFromCurrentMobilePhone();
                        break;
                    case R.id.tv_withdraw:
                        String code = et_code.getText().toString();
                        if (TextUtils.isEmpty(code)) {
                            ToastUtils.showShort("请填写验证码");
                            return;
                        }
                        viewModel.requestWithdraw(address, code, mType, money);
                        break;
                }
            }, R.id.iv_close, R.id.tv_send_code, R.id.tv_withdraw);
        } else {
            mUniversalDialog.show();
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
        viewModel.feeConfiguration.observe(this, s -> {
            mFeeConfiguration = s;
            initConfig();
        });
        viewModel.withdraw.observe(this, s -> {
            ToastUtils.showShort("提币成功");
            finish();
        });
        viewModel.requestFeeConfiguration();
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
