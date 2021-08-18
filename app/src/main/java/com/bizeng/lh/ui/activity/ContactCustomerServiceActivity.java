package com.bizeng.lh.ui.activity;

import android.os.Bundle;

import com.bizeng.lh.BR;
import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.databinding.ActivityContactCustomerServiceBinding;

/**
 * @Desc: 联系客服
 * @author: admin wsj
 * @Date: 2021/4/30 1:18 PM
 */
public class ContactCustomerServiceActivity extends BaseActivity<ActivityContactCustomerServiceBinding, BaseViewModel> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_contact_customer_service;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public String setTitleBar() {
        return "联系客服";
    }
}
