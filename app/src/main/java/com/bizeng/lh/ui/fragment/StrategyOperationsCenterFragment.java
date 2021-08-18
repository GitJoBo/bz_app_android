package com.bizeng.lh.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.DialogRadioRVAdapter;
import com.bizeng.lh.adapter.StrategyOperationsCenterRVAdapter;
import com.bizeng.lh.base.BaseFragment;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.ApiBean;
import com.bizeng.lh.bean.SelectBean;
import com.bizeng.lh.bean.StrategyBean;
import com.bizeng.lh.databinding.FragmentStrategyOperationsCenterBinding;
import com.bizeng.lh.ui.activity.ApiSettingActivity;
import com.bizeng.lh.ui.activity.RechargeCardActivity;
import com.bizeng.lh.ui.activity.StrategyOperationsCenterActivity;
import com.bizeng.lh.ui.activity.StrategyStatisticsActivity;
import com.bizeng.lh.ui.activity.WebViewActivity;
import com.bizeng.lh.utils.BigDecimalUtils;
import com.bizeng.lh.utils.MMKVUtils;
import com.bizeng.lh.viewmodel.StrategyOperationsCenterViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.liys.dialoglib.AnimationsType;
import com.liys.dialoglib.UniversalDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wzq.mvvmsmart.utils.ToastUtils;
import com.wzq.mvvmsmart.utils.resource.Status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 策略中心 AI量化操作中心
 * @author: admin wsj
 * @Date: 2021/5/13 5:29 PM
 */
public class StrategyOperationsCenterFragment extends BaseFragment<FragmentStrategyOperationsCenterBinding, StrategyOperationsCenterViewModel> implements OnItemChildClickListener {
    private ApiBean mApiBean = null;
    private String mApiId = null;
    private List<StrategyBean> mList = new ArrayList<>();
    private StrategyOperationsCenterRVAdapter mStrategyOperationsCenterRVAdapter;
    private UniversalDialog mUniversalDialog;
    private UniversalDialog mUniversalDialogOperate;
    private UniversalDialog mUniversalDialogRecoveryStrategy;
    private UniversalDialog mDialog;
    private TextView mTvCountdown = null;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_strategy_operations_center;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        if (getArguments() != null) {
            mApiBean = getArguments().getParcelable(Content.KEY);
        }
        if (mApiBean == null) {
            ToastUtils.showShort("数据有误");
            return;
        }
        List<ApiBean> apisBe = MMKVUtils.getInstance().getApisBe();
        if (apisBe != null) {
            for (int i = 0; i < apisBe.size(); i++) {
                ApiBean apiBean = apisBe.get(i);
                if (mApiBean.apiTag.equals(apiBean.apiTag)) {
                    mApiId = apiBean.apiId;
                }
            }
        }

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        binding.recyclerviewStrategyCenter.setAdapter(mStrategyOperationsCenterRVAdapter =
                new StrategyOperationsCenterRVAdapter(mList));
        binding.recyclerviewStrategyCenter.setLayoutManager(new LinearLayoutManager(getContext()));
        mStrategyOperationsCenterRVAdapter.addChildClickViewIds(R.id.tv_more, R.id.iv_more, R.id.tv_start_strategy_now, R.id.tv_recover, R.id.tv_history);
        mStrategyOperationsCenterRVAdapter.setOnItemChildClickListener(this);
        mStrategyOperationsCenterRVAdapter.setEmptyView(R.layout.item_empty);
        binding.smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refresh();
                binding.smartRefresh.finishLoadMore(Content.DEFAULT_WAITING_TIMEOUT);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
                binding.smartRefresh.finishRefresh(Content.DEFAULT_WAITING_TIMEOUT);
            }
        });

    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getStrategyInfoList().observe(this, o -> {
            mList.clear();
            mList.addAll(o.getRows());
            mStrategyOperationsCenterRVAdapter.notifyDataSetChanged();
            binding.smartRefresh.finishLoadMoreWithNoMoreData();
        });
        refresh();

        viewModel.getDelStrategy().observe(this, o -> {
            ToastUtils.showShort("删除成功");
            refresh();
        });
        viewModel.getResetStrategy().observe(this, o -> {
            ToastUtils.showShort("重置成功");
            refresh();
        });
        viewModel.getStopStrategy().observe(this, o -> {
            ToastUtils.showShort("暂停成功");
            refresh();
        });
        viewModel.getStartStrategy().observe(this, o -> {
            if (o.status == Status.SUCCESS) {
                ToastUtils.showShort("开启成功");
                refresh();
            } else if (o.status == Status.ERROR) {
                if (o.code == 201) {
                    showDeCharge();
                } else {
//                    ToastUtils.showShort(o.message);
                    o.show();
                }
            }
        });
        viewModel.getModifyWarehouseCapacity().observe(this, o -> {
            ToastUtils.showShort("修改成功");
            refresh();
        });
        viewModel.getCodeCountDown().observe(this, s -> {
            if (mUniversalDialogOperate != null && !mUniversalDialogOperate.isShowing()) {
                viewModel.stopCodeCountDown();
                return;
            }
            if (mTvCountdown != null) {
                String s1 = mTvCountdown.getText().toString();
                String s2 = s1.substring(0, s1.length() - 4);
                if (s != -1) {
                    mTvCountdown.setText(String.format("%s(%dS)", s2, s));
                    mTvCountdown.setClickable(false);
                    mTvCountdown.setEnabled(false);
                } else {
                    mTvCountdown.setText(s2);
                    mTvCountdown.setClickable(true);
                    mTvCountdown.setEnabled(true);
                    mTvCountdown.setBackgroundResource(R.drawable.shape_dialog_text_cred_r10);
                }
            }
        });
        viewModel.tutorialArea.observe(this, s -> {
            WebViewActivity.newInstance(getActivity(), "", s.getUrl(), 1);
        });
    }

    public void refresh() {
        //关闭所有弹框
        if (mUniversalDialogRecoveryStrategy != null) {
            mUniversalDialogRecoveryStrategy.dismiss();
        }
        if (mUniversalDialog != null) {
            mUniversalDialog.dismiss();
        }
        if (mUniversalDialogOperate != null) {
            mUniversalDialogOperate.dismiss();
        }
        if (!TextUtils.isEmpty(mApiId)) {
            viewModel.requestStrategyInfoList(mApiId, 1, 10000);
        }
    }

    private void delStrategy(String userStrategyId) {
        viewModel.requestDelStrategy(userStrategyId);
    }

    private void resetStrategy(String userStrategyId) {
        viewModel.requestResetStrategy(userStrategyId);
    }

    private void stopStrategy(String userStrategyId) {
        viewModel.requestStopStrategy(userStrategyId);
    }

    private void startStrategy(String userStrategyId, String capacity) {
        if (TextUtils.isEmpty(capacity)) {
            capacity = "full";
        }
        viewModel.requestStartStrategy(userStrategyId, capacity);
    }

    private void modifyWarehouseCapacity(String userStrategyId, String capacity) {
        viewModel.requestModifyWarehouseCapacity(userStrategyId, capacity);
    }

    public static StrategyOperationsCenterFragment newInstance(ApiBean apiBean) {
        Bundle args = new Bundle();
        args.putParcelable(Content.KEY, apiBean);
        StrategyOperationsCenterFragment fragment = new StrategyOperationsCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        StrategyBean s = mList.get(position);
        switch (view.getId()) {
            case R.id.tv_more:
            case R.id.iv_more:
                dialogMore(s);
                break;
            case R.id.tv_start_strategy_now:
                fillInTheWarehouseCapacityBulletBox(s, true);
                break;
            case R.id.tv_recover:
                showDialogRecoveryStrategy(s);
                break;
            default:
                Intent intent = new Intent(getContext(), StrategyStatisticsActivity.class);
                intent.putExtra(Content.KEY, mApiId);
                intent.putExtra(Content.TYPE, mApiBean.apiTag);
                intent.putExtra(Content.PARAMS_TITLE, mList.get(position).userStrategyId);
                startActivity(intent);
        }
    }

    private void dialogMore(StrategyBean s) {
        mUniversalDialog = UniversalDialog.newInstance(getContext(), R.layout.commom_dialog_more);
        mUniversalDialog.setBgRadius(12, 12, 0, 0);
        mUniversalDialog.setGravity(Gravity.BOTTOM)
                .setAnimations(AnimationsType.BOTTOM)
                .setWidthRatio(1).show();
        //宽全屏
        mUniversalDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView tv_dialog_more_modify_warehouse_capacity = mUniversalDialog.getView(R.id.tv_dialog_more_modify_warehouse_capacity);
        TextView tv_dialog_more_pause_strategy = mUniversalDialog.getView(R.id.tv_dialog_more_pause_strategy);
        TextView tv_dialog_more_reset_strategy = mUniversalDialog.getView(R.id.tv_dialog_more_reset_strategy);
        TextView tv_dialog_more_delete_strategy = mUniversalDialog.getView(R.id.tv_dialog_more_delete_strategy);
        tv_dialog_more_pause_strategy.setText("暂停策略");
        switch (s.status) {
            case "0":
                //只有删除
                tv_dialog_more_modify_warehouse_capacity.setVisibility(View.GONE);
                tv_dialog_more_pause_strategy.setVisibility(View.GONE);
                tv_dialog_more_reset_strategy.setVisibility(View.GONE);
                tv_dialog_more_delete_strategy.setVisibility(View.VISIBLE);
                break;
            case "1":
                //修改、暂停、重置
                tv_dialog_more_modify_warehouse_capacity.setVisibility(View.VISIBLE);
                tv_dialog_more_pause_strategy.setVisibility(View.VISIBLE);
                tv_dialog_more_reset_strategy.setVisibility(View.VISIBLE);
                tv_dialog_more_delete_strategy.setVisibility(View.GONE);
                break;
            case "2":
                //修改、恢复、重置、删除
                tv_dialog_more_modify_warehouse_capacity.setVisibility(View.VISIBLE);
                tv_dialog_more_pause_strategy.setVisibility(View.VISIBLE);
                tv_dialog_more_pause_strategy.setText("恢复策略");
                tv_dialog_more_reset_strategy.setVisibility(View.VISIBLE);
                tv_dialog_more_delete_strategy.setVisibility(View.VISIBLE);
                break;
        }
        mUniversalDialog.setOnClickListener((v, universalDialog) -> {
                    mUniversalDialog.dismiss();
                    mUniversalDialogOperate = UniversalDialog.newInstance(getContext(), R.layout.commom_dialog_tip);
                    mTvCountdown = mUniversalDialogOperate.getView(R.id.dialog_btn_positive);
                    switch (v.getId()) {
                        case R.id.tv_dialog_more_modify_warehouse_capacity:
                            fillInTheWarehouseCapacityBulletBox(s, false);
                            break;
                        case R.id.tv_dialog_more_pause_strategy:
                            //暂停策略 tip
                            if ("2".equals(s.status)) {
                                dialogTip(R.string.dialog_recovery_strategy, R.string.dialog_recovery_strategy_info, R.string.dialog_confirm_recovery, 4, s);
                            } else {
                                dialogTip(R.string.dialog_pause_strategy, R.string.dialog_pause_strategy_info, R.string.dialog_confirm_suspension, 1, s);
                            }
                            viewModel.startCodeCountDown();
                            break;
                        case R.id.tv_dialog_more_reset_strategy:
                            //重置策略
                            dialogTip(R.string.dialog_reset_strategy, R.string.dialog_reset_strategy_info, R.string.dialog_confirm_reset, 2, s);
                            viewModel.startCodeCountDown();
                            break;
                        case R.id.tv_dialog_more_delete_strategy:
                            //删除策略
                            dialogTip(R.string.dialog_delete_strategy, R.string.dialog_delete_strategy_info, R.string.dialog_confirm_deletion, 3, s);
                            viewModel.startCodeCountDown();
                            break;
                        case R.id.tv_dialog_more_cancel:
                            mUniversalDialogOperate.dismiss();
                            viewModel.startCodeCountDown();
                            break;
                    }
                }, R.id.tv_dialog_more_modify_warehouse_capacity, R.id.tv_dialog_more_pause_strategy,
                R.id.tv_dialog_more_reset_strategy, R.id.tv_dialog_more_delete_strategy,
                R.id.tv_dialog_more_cancel);
    }

    /**
     * @param titleId
     * @param contentId
     * @param okId
     * @param type      0,仓位容量，1暂停策略，2重置策略，3删除策略，4恢复策略
     */
    @SuppressLint("ResourceType")
    private void dialogTip(@StringRes int titleId, @StringRes int contentId, @StringRes int okId, int type, StrategyBean s) {
        if (type == 4) {
            showDialogRecoveryStrategy(s);
            return;
        }
        if (mUniversalDialogOperate == null) {
            return;
        }
        mUniversalDialogOperate.setGravity(Gravity.CENTER)
                .setAnimations(AnimationsType.SCALE)
                .setWidthRatio(Content.WIDTH_RATIO)
                .setText(R.id.dialog_tv_content, contentId)
                .setText(R.id.dialog_btn_negative, R.string.common_cancel)
                .setText(R.id.dialog_btn_positive, okId);
        TextView dialog_tv_title = mUniversalDialogOperate.getView(R.id.dialog_tv_title);
        TextView dialog_tv_content = mUniversalDialogOperate.getView(R.id.dialog_tv_content);
        TextView dialog_btn_negative = mUniversalDialogOperate.getView(R.id.dialog_btn_negative);
        TextView dialog_btn_positive = mUniversalDialogOperate.getView(R.id.dialog_btn_positive);
        dialog_tv_content.setGravity(Gravity.LEFT);
        dialog_btn_positive.setTextColor(Color.WHITE);
        dialog_btn_positive.setBackgroundResource(R.drawable.shape_dialog_text_cddd_r10);
        dialog_btn_negative.setOnClickListener(v -> mUniversalDialogOperate.dismiss());
        dialog_btn_positive.setOnClickListener(v -> {
            mUniversalDialogOperate.dismiss();
            StrategyOperationsCenterActivity activity = (StrategyOperationsCenterActivity) getActivity();
            if (activity == null || s == null) {
                ToastUtils.showShort("数据异常");
                return;
            }
            switch (type) {
                case 1:
                    stopStrategy(s.userStrategyId);
                    break;
                case 2:
                    resetStrategy(s.userStrategyId);
                    break;
                case 3:
                    delStrategy(s.userStrategyId);
                    break;
                case 4:
                    startStrategy(s.userStrategyId, s.capacity);
                    break;
            }
        });
        dialog_tv_title.setText(titleId);
//        dialog_tv_title.setGravity(Gravity.LEFT);
        dialog_tv_title.setVisibility(View.VISIBLE);
        mUniversalDialogOperate.show();
    }

    /**
     * 填仓位容量弹框
     *
     * @param s       选择的策略
     * @param isStart true开启按钮，false不是开启按钮
     */
    private void fillInTheWarehouseCapacityBulletBox(StrategyBean s, boolean isStart) {
        List<SelectBean<String>> list = new ArrayList<>();
        SelectBean<String> sbIntelligent = new SelectBean<>("智能仓位（推荐）", "量化策略按照行情自动进行仓位管理，收益稳健");
//        sbIntelligent.setSelected(true);
        sbIntelligent.setExtra(s.proposalCapacity);
        list.add(sbIntelligent);
        SelectBean<String> sbFullPosition = new SelectBean<>("全仓仓位", "使用账户下全部仓位进行交易，收益最高");
        sbFullPosition.setExtra("full");
        list.add(sbFullPosition);
        SelectBean<String> sbCustomPosition = new SelectBean<>("自定义仓位", "用户设置策略使用的仓位，建议不小于500，适合有经验用户");
        sbCustomPosition.setType(9);
        list.add(sbCustomPosition);
        UniversalDialog dialog = UniversalDialog.newInstance(getContext(), R.layout.commom_dialog_list);
        dialog.setGravity(Gravity.CENTER)
                .setAnimations(AnimationsType.SCALE)
                .setWidthRatio(Content.WIDTH_RATIO)
                .setText(R.id.dialog_tv_title, R.string.dialog_set_position_capacity)
                .setText(R.id.dialog_btn_negative, R.string.common_cancel)
                .setText(R.id.dialog_btn_positive, R.string.common_confirm)
                .show();
        RecyclerView recyclerView = dialog.getView(R.id.dialog_rv_content);
        TextView dialog_tv_title = dialog.getView(R.id.dialog_tv_title);
        //2021-8-4新增弹框回显已经设置的仓位容量
        if ("full".equals(s.capacity)) {
            sbFullPosition.setSelected(true);
        } else if ("default".equals(s.capacity)) {
            sbIntelligent.setSelected(true);
        } else if (TextUtils.isEmpty(s.capacity)) {
            sbIntelligent.setSelected(true);
        } else {
            sbCustomPosition.setSelected(true);
            sbCustomPosition.setExtra(BigDecimalUtils.getInstance().getBigDecimal(new BigDecimal(s.capacity)).toString());
        }
        DialogRadioRVAdapter dialogRadioRVAdapter = new DialogRadioRVAdapter(list);
        dialog.getView(R.id.dialog_btn_negative).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getView(R.id.dialog_btn_positive).setOnClickListener(v -> {
            String selectedValue = dialogRadioRVAdapter.getSelectedValue();
            if ("-1".equals(selectedValue)) {
                ToastUtils.showShort("仓位容量需要大于0U");
                return;
            }
            if (isStart) {
                startStrategy(s.userStrategyId, selectedValue);
            } else {
                modifyWarehouseCapacity(s.userStrategyId, selectedValue);
            }
            dialog.dismiss();
        });
//        dialog_tv_title.setGravity(Gravity.LEFT);
        dialog_tv_title.setVisibility(View.VISIBLE);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            recyclerView.setAdapter(dialogRadioRVAdapter);
        }
        dialogRadioRVAdapter.addChildClickViewIds(R.id.cb_rv);
        dialogRadioRVAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.cb_rv:
                    dialogRadioRVAdapter.singleChoice(position);
                    break;
            }
        });
    }

    private void showDeCharge() {
        mDialog = UniversalDialog.newInstance(getContext(), R.layout.commom_dialog_tip);
        mDialog.setGravity(Gravity.CENTER)
                .setAnimations(AnimationsType.SCALE)
                .setWidthRatio(0.7)
                .setText(R.id.dialog_tv_content, R.string.dialog_insufficient_points)
                .setText(R.id.dialog_btn_negative, R.string.common_cancel)
                .setText(R.id.dialog_btn_positive, R.string.dialog_recharge)
                .show();
        mDialog.getView(R.id.dialog_btn_negative).setOnClickListener(v1 -> mDialog.dismiss());
        mDialog.getView(R.id.dialog_btn_positive).setOnClickListener(v13 -> {
            startActivity(RechargeCardActivity.class);
            mDialog.dismiss();
        });
    }

    /**
     * 恢复策略
     */
    private void showDialogRecoveryStrategy(StrategyBean strategyBean) {
        String contentStr = null;
        String cancelStr = null;
        int stopStatusValue = strategyBean.getStopStatusValue();

        switch (stopStatusValue) {
            case 0:
//                startStrategy(strategyBean.userStrategyId, strategyBean.capacity);
                contentStr = "确认量化策略运行请点击继续恢复";
                cancelStr = "取消";
                break;
            case 1:
                contentStr = "策略因交易所API错误已处于暂停状态，若您已重新设置，请点击继续恢复";
                cancelStr = "设置API";
                break;
            case 2:
                contentStr = "策略因点卡欠费已处于暂停状态，若您已完成兑换，请点击继续恢复";
                cancelStr = "点卡兑换";
                break;
            default:
                contentStr = "策略因未知I错误(" + strategyBean.stopDesc + ")已处于暂停状态，若已经处理，请点击继续恢复";
                cancelStr = "联系客服";
                break;
        }
        if (mUniversalDialogRecoveryStrategy == null) {
            mUniversalDialogRecoveryStrategy = UniversalDialog.newInstance(requireActivity(), R.layout.commom_dialog_recovery_strategy);
        }
        mUniversalDialogRecoveryStrategy.setGravity(Gravity.CENTER)
                .setAnimations(AnimationsType.SCALE)
                .setWidthRatio(Content.WIDTH_RATIO)
                .setText(R.id.dialog_tv_content, contentStr)
                .setText(R.id.dialog_btn_negative, cancelStr)
                .setText(R.id.dialog_btn_positive, "继续恢复");
        mUniversalDialogRecoveryStrategy.setOnClickListener(new UniversalDialog.DialogOnClickListener() {
            @Override
            public void onClick(View v, UniversalDialog universalDialog) {
                switch (v.getId()) {
                    case R.id.dialog_btn_negative:
                        if (stopStatusValue == 0) {
                            mUniversalDialogRecoveryStrategy.dismiss();
                        } else if (stopStatusValue == 1) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(Content.KEY, mApiBean);
                            startActivity(ApiSettingActivity.class, bundle);
                        } else if (stopStatusValue == 2) {
                            startActivity(RechargeCardActivity.class);
                            mUniversalDialogRecoveryStrategy.dismiss();
                        } else {
//                            ToastUtils.showShort("客服请假了");
                            viewModel.requestTutorialArea(Content.CONTACT_CUSTOMER_SERVICE);
                            mUniversalDialogRecoveryStrategy.dismiss();
                        }
                        break;
                    case R.id.dialog_btn_positive:
                        startStrategy(strategyBean.userStrategyId, strategyBean.capacity);
                        break;
                    case R.id.iv_close:
                        mUniversalDialogRecoveryStrategy.dismiss();
                        break;
                }
            }
        }, R.id.dialog_btn_negative, R.id.dialog_btn_positive, R.id.iv_close);
        mUniversalDialogRecoveryStrategy.show();
    }
}
