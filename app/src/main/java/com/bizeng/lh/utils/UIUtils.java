package com.bizeng.lh.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import com.angcyo.tablayout.DslTabLayout;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.ApiBean;
import com.wzq.mvvmsmart.utils.ConvertUtils;

import java.math.BigDecimal;
import java.util.List;

public class UIUtils {
    private static volatile UIUtils instance = null;

    private UIUtils() {
    }

    public static UIUtils getInstance() {
        if (instance == null) {
            synchronized (UIUtils.class) {
                instance = new UIUtils();
            }
        }
        return instance;
    }

    /**
     * 初始化tab 已设定api的在前
     * 2021-7-22新增apiContent设置空字符串视为未添加
     *
     * @param context
     * @param apiBeans
     * @param dslTabLayout
     */
    public void initTabs(Context context, List<ApiBean> apiBeans, DslTabLayout dslTabLayout) {
        TextView tvHb = null;
        TextView tvBan = null;
        TextView tvOKEX = null;
        List<ApiBean> apisBe = MMKVUtils.getInstance().getApisBe();
        if (apisBe != null) {
            int size = apisBe.size();
            for (int i = 0; i < size; i++) {
                ApiBean apiBean = apisBe.get(i);
                if (!TextUtils.isEmpty(apiBean.apiContent)) {
                    if (!MapRemoveNullUtil.mapValueIsAllNull(apiBean.apiContent)) {
                        if (Content.API_TAG_HB.equals(apiBean.apiTag)) {
                            tvHb = new TextView(context);
                            addTvHb(apiBeans, dslTabLayout, tvHb, "火币", Content.API_TAG_HB);
                        } else if (Content.API_TAG_BAN.equals(apiBean.apiTag)) {
                            tvBan = new TextView(context);
                            addTvHb(apiBeans, dslTabLayout, tvBan, "币安", Content.API_TAG_BAN);
                        } else if (Content.API_TAG_OKEX.equals(apiBean.apiTag)) {
                            tvOKEX = new TextView(context);
                            addTvHb(apiBeans, dslTabLayout, tvOKEX, "OKEx", Content.API_TAG_OKEX);
                        }
                    }
                }
            }
        }
        addDefApis(context, apiBeans, dslTabLayout, tvHb, tvBan, tvOKEX);
    }

    private void addDefApis(Context context, List<ApiBean> apiBeans, DslTabLayout dslTabLayout, TextView tvHb, TextView tvBan, TextView tvOKEX) {
        if (tvHb == null) {
            tvHb = new TextView(context);
            addTvHb(apiBeans, dslTabLayout, tvHb, "火币", Content.API_TAG_HB);
        }
        if (tvBan == null) {
            tvBan = new TextView(context);
            addTvHb(apiBeans, dslTabLayout, tvBan, "币安", Content.API_TAG_BAN);
        }
        if (tvOKEX == null) {
            tvOKEX = new TextView(context);
            addTvHb(apiBeans, dslTabLayout, tvOKEX, "OKEx", Content.API_TAG_OKEX);
        }
    }

    private void addTvHb(List<ApiBean> apiBeans, DslTabLayout dslTabLayout, TextView tvHb, String 火币, String apiTagHb) {
        tvHb.setGravity(Gravity.CENTER);
        tvHb.setText(火币);
        dslTabLayout.addView(tvHb);
        apiBeans.add(new ApiBean(apiTagHb, 火币));
    }

    public void setU2CNY(BigDecimal sU, TextView tvU) {
        int color = Color.parseColor("#37761F");
        int color2 = Color.parseColor("#333333");
        TextParserUtils textParser = new TextParserUtils();
        textParser.append(sU.toString(), ConvertUtils.dp2px(21), color, Typeface.BOLD);
        textParser.append(" U", ConvertUtils.dp2px(14), color, Typeface.BOLD);
        textParser.append(" ≈ ", ConvertUtils.dp2px(14), color2);
        textParser.append(BigDecimalUtils.getInstance().getBigDecimal(sU.multiply(new BigDecimal(Content.U2CNY))).toString(), ConvertUtils.dp2px(14), color2);
        textParser.append(" CNY", ConvertUtils.dp2px(14), color2);
        textParser.parse(tvU);
    }
}
