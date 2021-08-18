package com.bizeng.lh.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.ServiceAddressAdapter;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.ApiBean;
import com.bizeng.lh.databinding.ActivityApiSettingBinding;
import com.bizeng.lh.utils.MMKVUtils;
import com.bizeng.lh.viewmodel.ApiManagerViewModel;
import com.wzq.mvvmsmart.utils.ToastUtils;
import com.wzq.mvvmsmart.utils.resource.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import rxhttp.wrapper.utils.GsonUtil;

/**
 * @Desc: Api设置
 * @author: admin wsj
 * @Date: 2021/4/22 9:51 AM
 */
public class ApiSettingActivity extends BaseActivity<ActivityApiSettingBinding, ApiManagerViewModel> implements View.OnClickListener {
    private ApiBean mApiBean = null;
    private ServiceAddressAdapter mServiceAddressAdapter;
    private List<String> mList = new ArrayList<>();

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_api_setting;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public String setTitleBar() {
        return mApiBean.title + "设置";
    }

    @Override
    public void initParam() {
        mApiBean = getIntent().getParcelableExtra(Content.KEY);
        if (mApiBean == null) {
            ToastUtils.showShort("数据异常");
            finish();
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (mApiBean.apiTag.equals(Content.API_TAG_OKEX)) {
            binding.tvPassphase.setVisibility(View.VISIBLE);
            binding.etPassphase.setVisibility(View.VISIBLE);
        } else {
            binding.tvPassphase.setVisibility(View.GONE);
            binding.etPassphase.setVisibility(View.GONE);
        }
        binding.btnSubmit.setOnClickListener(this);
        binding.ivApiCourseOfStudy.setOnClickListener(this);
        binding.recyclerViewServiceAddress.setLayoutManager(new LinearLayoutManager(this));
        mServiceAddressAdapter = new ServiceAddressAdapter(mList);
        String serviceIp = MMKVUtils.getInstance().getServiceIp();
        if (Content.API_TAG_BAN.equals(mApiBean.apiTag)) {
            binding.ivApiCourseOfStudy.setBackgroundResource(R.mipmap.bg_bian_title);
            String[] split = serviceIp.split(",");
            if (split != null && split.length > 0) {
                mList.addAll(Arrays.asList(split));
            }
        } else if (Content.API_TAG_OKEX.equals(mApiBean.apiTag)) {
            binding.ivApiCourseOfStudy.setBackgroundResource(R.mipmap.bg_okex_title);
            mList.add(serviceIp);
        } else {
            binding.ivApiCourseOfStudy.setBackgroundResource(R.mipmap.bg_huobi_title);
            mList.add(serviceIp);
        }
        mServiceAddressAdapter.addChildClickViewIds(R.id.tv_copy);
        mServiceAddressAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", mList.get(position));
            cm.setPrimaryClip(mClipData);
            ToastUtils.showShort("复制成功");
        });
        binding.recyclerViewServiceAddress.setAdapter(mServiceAddressAdapter);
        List<ApiBean> apisBe = MMKVUtils.getInstance().getApisBe();
        if (apisBe != null) {
            int size = apisBe.size();
            for (int i = 0; i < size; i++) {
                ApiBean apiBean = apisBe.get(i);
                if (mApiBean.apiTag.equals(apiBean.apiTag)) {
                    String apiContent = apiBean.apiContent;
                    Map apiContentMap = GsonUtil.getObject(apiContent, Map.class);
                    if (apiContentMap.get("access_key") != null) {
                        binding.etApiKey.setText(apiContentMap.get("access_key").toString());
                    }
                    if (apiContentMap.get("secret_key") != null) {
                        binding.etSecretKey.setText(apiContentMap.get("secret_key").toString());
                    }
                    if (apiContentMap.get("pass_key") != null) {
                        binding.etPassphase.setText(apiContentMap.get("pass_key").toString());
                    }
                }
            }
        }

        //输入框弹起时重置布局 有黑边bug
//        View decorView = this.getWindow().getDecorView();
//        View contentView = this.findViewById(Window.ID_ANDROID_CONTENT);
//        decorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, contentView));
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getSaveUserApi().observe(this, s -> {
            if (s.status == Status.SUCCESS) {
                ApiSettingActivity.this.finish();
            }
        });
        viewModel.getTutorialArea().observe(this, s -> {
//            Intent intent = new Intent(ApiSettingActivity.this, WebViewActivity.class);
//            intent.putExtra(Content.PARAMS_TITLE, "");
//            intent.putExtra(Content.PARAMS_URL, s.getUrl());
//            intent.putExtra(Content.TYPE, 1);
//            startActivity(intent);
            WebViewActivity.newInstance(ApiSettingActivity.this, "", s.getUrl(), 1);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                ApiBean apiBean = new ApiBean();
                apiBean.access_key = binding.etApiKey.getText().toString();
                apiBean.secret_key = binding.etSecretKey.getText().toString();
                if (mApiBean.apiTag.equals(Content.API_TAG_OKEX)) {
                    apiBean.pass_key = binding.etPassphase.getText().toString();
                    if (TextUtils.isEmpty(apiBean.pass_key)) {
                        ToastUtils.showShort(binding.etPassphase.getHint());
                        return;
                    }
                }
                if (TextUtils.isEmpty(apiBean.access_key)) {
                    ToastUtils.showShort(binding.etApiKey.getHint());
                    return;
                }
                if (TextUtils.isEmpty(apiBean.secret_key)) {
                    ToastUtils.showShort(binding.etSecretKey.getHint());
                    return;
                }

                viewModel.requestSaveUserApi(GsonUtil.toJson(apiBean), mApiBean.apiTag);
                break;
            case R.id.iv_api_course_of_study:
                if (Content.API_TAG_BAN.equals(mApiBean.apiTag)) {
                    viewModel.requestTutorialArea(Content.BIN_AN_API_SETUP_TUTORIAL);
                } else if (Content.API_TAG_OKEX.equals(mApiBean.apiTag)) {
                    viewModel.requestTutorialArea(Content.OKEX_API_SETUP_TUTORIAL);
                } else {
                    viewModel.requestTutorialArea(Content.HUO_BI_API_SETTING_TUTORIAL);
                }
                break;
//            case R.id.btn_copy:
//                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData mClipData = ClipData.newPlainText("Label", binding.etCurrentServerIpAddress.getText().toString());
//                cm.setPrimaryClip(mClipData);
//                ToastUtils.showShort("复制成功");
//                break;
        }
    }

//    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
//        return new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Rect r = new Rect();
//                decorView.getWindowVisibleDisplayFrame(r);
//
//                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
//                int diff = height - r.bottom;
//
//                if (diff != 0) {
//                    if (contentView.getPaddingBottom() != diff) {
//                        contentView.setPadding(0, 0, 0, diff + 120);
//                    }
//                } else {
//                    if (contentView.getPaddingBottom() != 0) {
//                        contentView.setPadding(0, 0, 0, 0);
//                    }
//                }
//            }
//        };
//    }
}
