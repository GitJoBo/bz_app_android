package com.bizeng.lh.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.adapter.ApiCourseOfStudyRvAdapter;
import com.bizeng.lh.adapter.ApiRvAdapter;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.ApiBean;
import com.bizeng.lh.databinding.ActivityApiManagerOldBinding;
import com.bizeng.lh.utils.MMKVUtils;
import com.bizeng.lh.viewmodel.ApiManagerViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wzq.mvvmsmart.utils.resource.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 交易所api管理 列表展示
 * @author: admin wsj
 * @Date: 2021/4/22 8:57 AM
 */
public class ApiManagerActivity_old extends BaseActivity<ActivityApiManagerOldBinding, ApiManagerViewModel> {
    private List<String> apiCourseOfStudy = new ArrayList<>();
    private List<ApiBean> apis = new ArrayList<>();
    private ApiCourseOfStudyRvAdapter mApiCourseOfStudyRvAdapter;
    private ApiRvAdapter mApiRvAdapter;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_api_manager_old;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        apiCourseOfStudy.add("火币API设置教程");
        apiCourseOfStudy.add("币安API设置教程");
        mApiCourseOfStudyRvAdapter = new ApiCourseOfStudyRvAdapter(apiCourseOfStudy);
        mApiCourseOfStudyRvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String s = apiCourseOfStudy.get(position);
                viewModel.requestTutorialArea(s);
            }
        });
        binding.recyclerviewApiCourseOfStudy.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, true));
        binding.recyclerviewApiCourseOfStudy.setAdapter(mApiCourseOfStudyRvAdapter);
        apis.add(new ApiBean(Content.API_TAG_HB, "火币API"));
        apis.add(new ApiBean(Content.API_TAG_BAN, "币安API"));
        mApiRvAdapter = new ApiRvAdapter(apis);
        binding.recyclerviewApi.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewApi.setAdapter(mApiRvAdapter);
        mApiRvAdapter.addChildClickViewIds(R.id.tv_api_title);
        mApiRvAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Content.KEY, apis.get(position));
                startActivity(ApiSettingActivity.class, bundle);
            }
        });

    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getApiList().observe(this, s -> {
            if (s.status == Status.SUCCESS) {

            }
        });
        viewModel.getTransactionServiceIp().observe(this, s -> {
            if (s.status == Status.SUCCESS) {
                MMKVUtils.getInstance().setServiceIp(s.data);
            }
        });
        viewModel.getTutorialArea().observe(this, s -> {
//            Intent intent = new Intent(ApiManagerActivity_old.this, WebViewActivity.class);
//            intent.putExtra(Content.PARAMS_TITLE, "");
//            intent.putExtra(Content.PARAMS_URL, s.getUrl());
//            intent.putExtra(Content.TYPE, 1);
//            startActivity(intent);
            WebViewActivity.newInstance(ApiManagerActivity_old.this, "", s.getUrl(), 1);
        });

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
}
