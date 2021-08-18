package com.bizeng.lh.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.bizeng.lh.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
public class PieChartMarkerView extends MarkerView {

    private final TextView tvContent;
    private final ValueFormatter valueFormatter;
    private final DecimalFormat format;

    public PieChartMarkerView(Context context, ValueFormatter valueFormatter) {
        super(context, R.layout.custom_marker_view2);

        this.valueFormatter = valueFormatter;
        tvContent = findViewById(R.id.tvContent);
        format = new DecimalFormat("##0.00");
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String axisLabel = valueFormatter.getPieLabel(e.getX(), (PieEntry) e);
        tvContent.setText(String.format("%s, \r\n持仓: %s", axisLabel, format.format(e.getY())));

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
