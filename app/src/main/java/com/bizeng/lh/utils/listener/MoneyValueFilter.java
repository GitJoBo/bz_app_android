package com.bizeng.lh.utils.listener;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.util.Log;

/**
 * 小数点前自动补0，首位为0后面必须是.
 * <p>
 * 使用：
 * editText.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(2)});
 */
public class MoneyValueFilter extends DigitsKeyListener {
    private static final String TAG = "MoneyValueFilter";

    public MoneyValueFilter() {
        super(false, true);
    }

    private int digits = 2;

    /**
     * 设置小数点后位数
     *
     * @param d
     * @return
     */
    public MoneyValueFilter setDigits(int d) {
        digits = d;
        return this;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        CharSequence out = super.filter(source, start, end, dest, dstart, dend);
        // 如果更改，请替换源
        if (out != null) {
            source = out;
            start = 0;
            end = out.length();
        }

        int len = end - start;

//        如果删除，则源为空，并且删除不会破坏任何内容
        if (len == 0) {
            return source;
        }

        //以点开始的时候，自动在前面添加0
        if (source.toString().equals(".") && dstart == 0) {
            return "0.";
        }
        //如果起始位置为0,且第二位跟的不是".",则无法后续输入
        if (!source.toString().equals(".") && dest.toString().equals("0")) {
            return "." + source;
        }
        int dlen = dest.length();
        // 找到小数的位置。
        for (int i = 0; i < dstart; i++) {
            if (dest.charAt(i) == '.') {
                //在这里意味着一个数字
                // 点后插入
                // 检查位数是否正确
                return (dlen - (i + 1) + len > digits) ?
                        "" :
                        new SpannableStringBuilder(source, start, end);
            }
        }

        for (int i = start; i < end; ++i) {
            if (source.charAt(i) == '.') {
                // 在这里意味着已经插入了点
                // 检查位数是否正确
                if ((dlen - dend) + (end - (i + 1)) > digits)
                    return source.subSequence(0, source.length() - 1);
                else
                    break;  // return new SpannableStringBuilder(source, start, end);
            }
        }
        Log.d(TAG, "-------return:" + new SpannableStringBuilder(source, start, end).toString());
        Log.d(TAG, "-----------------------");

        // 如果点在插入的部分之后，
        return new SpannableStringBuilder(source, start, end);
    }
}