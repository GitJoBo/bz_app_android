package com.bizeng.lh.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.ApiCourseOfStudyRvAdapter;
import com.bizeng.lh.adapter.ApiRvAdapter;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.ApiBean;
import com.bizeng.lh.databinding.ActivityApiManagerBinding;
import com.bizeng.lh.utils.MMKVUtils;
import com.bizeng.lh.viewmodel.ApiManagerViewModel;
import com.wzq.mvvmsmart.utils.resource.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 交易所api管理
 * @author: admin wsj
 * @Date: 2021/4/22 8:57 AM
 */
public class ApiManagerActivity extends BaseActivity<ActivityApiManagerBinding, ApiManagerViewModel> implements View.OnClickListener {
    private List<String> apiCourseOfStudy = new ArrayList<>();
    private List<ApiBean> mApiBeans = new ArrayList<>();//全部币种
    private List<ApiBean> mSaveApis = new ArrayList<>();//已设置api币种
    private ApiCourseOfStudyRvAdapter mApiCourseOfStudyRvAdapter;
    private ApiRvAdapter mApiRvAdapter;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_api_manager;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mApiBeans.add(new ApiBean(Content.API_TAG_HB, "火币API"));
        mApiBeans.add(new ApiBean(Content.API_TAG_BAN, "币安API"));
        mApiBeans.add(new ApiBean(Content.API_TAG_OKEX, "OKEx"));
        binding.ivApiTitleHb.setOnClickListener(this);
        binding.ivApiTitleBa.setOnClickListener(this);
        binding.ivApiTitleOkex.setOnClickListener(this);
        binding.llApiBa.setOnClickListener(this);
        binding.llApiHb.setOnClickListener(this);
        binding.llApiOkex.setOnClickListener(this);

    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getApiList().observe(this, s -> {
            if (s.status == Status.SUCCESS) {
                mSaveApis.clear();
                mSaveApis.addAll(s.data);
                for (int i = 0; i < mSaveApis.size(); i++) {
                    ApiBean apiBean = mSaveApis.get(i);
                    setTextState(apiBean);
//                    setTextState(apiBean, Content.API_TAG_BAN, binding.tvApiStatesBa);
//                    setTextState(apiBean, Content.API_TAG_HB, binding.tvApiStatesHb);
//                    setTextState(apiBean, Content.API_TAG_OKEX, binding.tvApiStatesOkex);
                }
            }
        });
        viewModel.getTransactionServiceIp().observe(this, s -> {
            if (s.status == Status.SUCCESS) {
                MMKVUtils.getInstance().setServiceIp(s.data);
            }
        });
        viewModel.getTutorialArea().observe(this, s -> {
            WebViewActivity.newInstance(ApiManagerActivity.this, "", s.getUrl(), 1);
        });

    }

    private void setTextState(ApiBean apiBean) {
        if (apiBean == null) return;
        String str = null;
        int intColor;
        if (!apiBean.apiContentIsNull()) {
            str = "已添加";
            intColor = getResources().getColor(R.color.color_919);
        } else {
            str = "去添加";
            intColor = getResources().getColor(R.color.text_245);
        }
        switch (apiBean.apiTag) {
            case Content.API_TAG_HB:
                binding.tvApiStatesHb.setText(str);
                binding.tvApiStatesHb.setTextColor(intColor);
                break;
            case Content.API_TAG_BAN:
                binding.tvApiStatesBa.setText(str);
                binding.tvApiStatesBa.setTextColor(intColor);
                break;
            case Content.API_TAG_OKEX:
                binding.tvApiStatesOkex.setText(str);
                binding.tvApiStatesOkex.setTextColor(intColor);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        viewModel.requestApiList();
        viewModel.requestTransactionServiceIp();
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    public String setTitleBar() {
        return "交易所API管理";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_api_title_hb:
                viewModel.requestTutorialArea(Content.HUO_BI_API_SETTING_TUTORIAL);
                break;
            case R.id.ll_api_hb:
                Bundle bundle = new Bundle();
                bundle.putParcelable(Content.KEY, mApiBeans.get(0));
                startActivity(ApiSettingActivity.class, bundle);
                break;
            case R.id.iv_api_title_ba:
                viewModel.requestTutorialArea(Content.BIN_AN_API_SETUP_TUTORIAL);
                break;
            case R.id.ll_api_ba:
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable(Content.KEY, mApiBeans.get(1));
                startActivity(ApiSettingActivity.class, bundle2);
                break;
            case R.id.iv_api_title_okex:
                viewModel.requestTutorialArea(Content.OKEX_API_SETUP_TUTORIAL);
                break;
            case R.id.ll_api_okex:
                Bundle bundle3 = new Bundle();
                bundle3.putParcelable(Content.KEY, mApiBeans.get(2));
                startActivity(ApiSettingActivity.class, bundle3);
                break;
        }
    }
}
