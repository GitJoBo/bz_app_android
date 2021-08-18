package com.wzq.mvvmsmart.utils;

/**
 * @Desc: 统一管理LiveEventBus的key
 * @author: admin wsj
 * @Date: 2021/4/25 10:41 AM
 */
public interface LiveEventBusKeys {
    /**
     * 刷新登录信息
     * //接受
     * LiveEventBus.get(LiveEventBusKeys.LOGIN_REFRESH).observe(this, new Observer<Object>() {
     *
     * @Override public void onChanged(Object o) {
     * //收到这个通知需要做的事情，注意，只有活着的页面才能走此回调
     * viewModel.requestLoginUser();
     * }
     * });
     * //发送
     * LiveEventBus.get(LiveEventBusKeys.LOGIN_REFRESH).post(Resource.success(null));
     */
    String LOGIN_REFRESH = "login_refresh";
}
