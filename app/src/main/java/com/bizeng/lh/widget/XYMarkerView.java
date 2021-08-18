package com.bizeng.lh.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.bizeng.lh.R;
import com.bizeng.lh.utils.BigDecimalUtils;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.math.BigDecimal;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
public class XYMarkerView extends MarkerView {

    private final TextView tvContent;
    private final ValueFormatter valueFormatter;

//    private final DecimalFormat format;

    public XYMarkerView(Context context, ValueFormatter valueFormatter) {
        super(context, R.layout.custom_marker_view);

        this.valueFormatter = valueFormatter;
        tvContent = findViewById(R.id.tvContent);
//        format = new DecimalFormat("##0.00");
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
//        tvContent.setText(String.format("时间: %s, \r\n币增量: %s", valueFormatter.getAxisLabel(e.getX(), null), format.format(e.getY())));
//        tvContent.setText(String.format("币增量: %s \r\n时间: %s", format.format(e.getY()), valueFormatter.getAxisLabel(e.getX(), null)));
        tvContent.setText(String.format("币增量: %s \r\n时间: %s", BigDecimalUtils.getInstance().getBigDecimal(BigDecimal.valueOf(e.getY()))
                , valueFormatter.getAxisLabel(e.getX(), null)));

        super.refreshContent(e, highlight);
    }

    /**
     * 居中显示
     * @return
     */
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
