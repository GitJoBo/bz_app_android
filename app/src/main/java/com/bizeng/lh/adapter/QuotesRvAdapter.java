package com.bizeng.lh.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

import com.bizeng.lh.R;
import com.bizeng.lh.bean.MainstreamCurrencyMarketBean;
import com.bizeng.lh.utils.BigDecimalUtils;
import com.bizeng.lh.utils.TextParserUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wzq.mvvmsmart.utils.ConvertUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Desc: 行情
 * @author: admin wsj
 * @Date: 2021/4/24 10:14 PM
 */
public class QuotesRvAdapter extends BaseQuickAdapter<MainstreamCurrencyMarketBean, BaseViewHolder> {
    public QuotesRvAdapter(@Nullable List<MainstreamCurrencyMarketBean> data) {
        super(R.layout.item_quotes, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, MainstreamCurrencyMarketBean item) {
        if (item == null) {
            return;
        }
        int color = Color.parseColor("#BC3932");
        TextView tv_percent_change_24h = holder.getView(R.id.tv_percent_change_24h);
        TextView tv_price_usd = holder.getView(R.id.tv_price_usd);
        holder.setText(R.id.tv_quotes_title, item.symbol);
//        holder.setText(R.id.tv_percent_change_24h, String.format("%s%%", item.percent_change_24h));
        if (item.percent_change_24h.contains("-")) {
            color = Color.parseColor("#BC3932");
            tv_percent_change_24h.setText(String.format("%s%%", item.percent_change_24h));
            tv_percent_change_24h.setTextColor(color);
            tv_price_usd.setTextColor(color);
        } else {
            tv_percent_change_24h.setText(String.format("+%s%%", item.percent_change_24h));
            color = Color.parseColor("#37761F");
            tv_percent_change_24h.setTextColor(color);
            tv_price_usd.setTextColor(color);
        }
        BigDecimal bigDecimal = BigDecimalUtils.getInstance().getBigDecimal(new BigDecimal(item.price_usd));
//        tv_price_usd.setText(bigDecimal.toString());
        TextParserUtils textParser = new TextParserUtils();
        textParser.append(bigDecimal.toString(), ConvertUtils.dp2px(20), color, Typeface.BOLD);
        textParser.append(" U", ConvertUtils.dp2px(14), color, Typeface.BOLD);
        textParser.parse(tv_price_usd);
        BigDecimal bigDecimal2 = BigDecimalUtils.getInstance().getBigDecimal(new BigDecimal(item.price_cny));
//        holder.setText(R.id.tv_price_cny, Content.REN_MIN_BI + bigDecimal2);
        holder.setText(R.id.tv_price_cny, String.format(tv_price_usd.getContext().getString(R.string.cny), bigDecimal2));
    }


}
