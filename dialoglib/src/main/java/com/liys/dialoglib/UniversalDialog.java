package com.liys.dialoglib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 万能Dialog
 * @author: admin wsj
 * @Date: 2021/4/21 2:06 PM
 */
public class UniversalDialog extends AppCompatDialog {

    protected Context context;
    protected SparseArray<View> views = new SparseArray<>();
    protected List<Integer> cancelIds = new ArrayList<>();
    protected DialogRootView controlView;
    protected int layoutId = 0;
    protected int width = 0;
    protected int height = 0;

    protected BgBean bgBean = new BgBean(); //背景属性对象

    private UniversalDialog(@NonNull Context context) {
        this(context, R.layout.dialog_confirm);
    }

    private UniversalDialog(@NonNull Context context, int layoutId) {
        this(context, layoutId, R.style.LDialog);
    }

    private UniversalDialog(@NonNull Context context, int layoutId, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.layoutId = layoutId;
    }

    /**
     * 获取对象
     *
     * @param context
     * @return
     */
    public static UniversalDialog newInstance(@NonNull Context context) {
        return new UniversalDialog(context).with();
    }

    public static UniversalDialog newInstance(@NonNull Context context, int layoutId) {
        return new UniversalDialog(context, layoutId).with();
    }

    public static UniversalDialog newInstance(@NonNull Context context, int layoutId, int themeResId) {
        return new UniversalDialog(context, layoutId, themeResId).with();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controlView = new DialogRootView(context);
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        controlView.addView(view);
        setContentView(controlView);
//        //宽全屏
//        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        init();
    }

    protected void init() {
        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(getRoundRectDrawable(dp2px(context, 0), Color.TRANSPARENT));

        width = (int) (ScreenUtils.getWidthPixels(context) * 0.8);
        height = WindowManager.LayoutParams.WRAP_CONTENT;
        setWidthHeight();
        getWindow().setWindowAnimations(R.style.li_dialog_default);
//        getWindow().setWindowAnimations(R.style.dialog_translate);
    }

    protected UniversalDialog with() {
        show();
        dismiss();
        return this;
    }

//    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置动画>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 自定义动画
     *
     * @param style
     * @return
     */
    public UniversalDialog setAnimationsStyle(int style) {
        getWindow().setWindowAnimations(style);
        return this;
    }

    /**
     * 内置动画
     *
     * @param styleType 类型
     * @return
     */
    public UniversalDialog setAnimations(@AnimationsType.AAnimationsType String styleType) {
        int style = -1;
        switch (styleType) {
            case AnimationsType.DEFAULT: //默认
                style = R.style.li_dialog_default;
                break;
            case AnimationsType.SCALE:
                style = R.style.li_dialog_scale;
                break;
            case AnimationsType.LEFT:
                style = R.style.li_dialog_translate_left;
                break;
            case AnimationsType.TOP:
                style = R.style.li_dialog_translate_top;
                break;
            case AnimationsType.RIGHT:
                style = R.style.li_dialog_translate_right;
                break;
            case AnimationsType.BOTTOM:
                style = R.style.li_dialog_translate_bottom;
                break;
        }
        if (style != -1) {
            setAnimationsStyle(style);
        }
        return this;
    }

//    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置位置>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 设置位置
     *
     * @param gravity
     * @param offX
     * @param offY
     */
    public UniversalDialog setGravity(int gravity, int offX, int offY) {
        setGravity(gravity);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.x = offX;
        layoutParams.y = offY;
        getWindow().setAttributes(layoutParams);
        return this;
    }

    public UniversalDialog setGravity(int gravity) {
        getWindow().setGravity(gravity);
        return this;
    }


    /**
     * 遮罩透明度
     *
     * @param value 0-1f
     * @return
     */
    public UniversalDialog setMaskValue(float value) {
        getWindow().setDimAmount(value);
        return this;
    }

//    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置背景>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 刷新背景
     *
     * @return
     */
    protected UniversalDialog refreshBg() {
//        getWindow().setBackgroundDrawable(getRoundRectDrawable(bgRadius, bgColor));
        controlView.setBackground(bgBean.getRoundRectDrawable());
        controlView.setBgRadius(
                bgBean.left_top_radius,
                bgBean.right_top_radius,
                bgBean.right_bottom_radius,
                bgBean.left_bottom_radius);
        return this;
    }

    /**
     * 设置背景颜色
     *
     * @return
     */
    public UniversalDialog setBgColor(@ColorInt int color) {
        bgBean.color = color;
        return refreshBg();
    }

    public UniversalDialog setBgColor(@Size(min = 1) String colorString) {
        bgBean.color = Color.parseColor(colorString);
        return refreshBg();
    }

    /**
     * 渐变背景
     *
     * @param orientation
     * @param colors
     * @return
     */
    public UniversalDialog setBgColor(GradientDrawable.Orientation orientation, @ColorInt int... colors) {
        bgBean.gradientsOrientation = orientation;
        bgBean.gradientsColors = colors;
        return refreshBg();
    }

    public UniversalDialog setBgColor(GradientDrawable.Orientation orientation, @Size(min = 1) String... colorStrings) {
        bgBean.gradientsOrientation = orientation;
        if (colorStrings == null) {
            return this;
        }
        bgBean.gradientsColors = new int[colorStrings.length];
        for (int i = 0; i < bgBean.gradientsColors.length; i++) {
            bgBean.gradientsColors[i] = Color.parseColor(colorStrings[i]);
        }
        return refreshBg();
    }

    public UniversalDialog setBgColorRes(@ColorRes int colorRes) {
        bgBean.color = context.getResources().getColor(colorRes);
        return refreshBg();
    }

    public UniversalDialog setBgColorRes(GradientDrawable.Orientation orientation, @Size(min = 1) String... colorRes) {
        bgBean.gradientsOrientation = orientation;
        bgBean.gradientsColors = new int[colorRes.length];
        for (int i = 0; i < colorRes.length; i++) {
            bgBean.gradientsColors[i] = Color.parseColor(colorRes[i]);
        }
        return refreshBg();
    }

    public UniversalDialog setBgColorRes(GradientDrawable.Orientation orientation, @ColorRes int... colorRes) {
        bgBean.gradientsOrientation = orientation;
        bgBean.gradientsColors = new int[colorRes.length];
        for (int i = 0; i < colorRes.length; i++) {
            bgBean.gradientsColors[i] = getColor(colorRes[i]);
        }
        return refreshBg();
    }

    /**
     * 设置背景圆角
     *
     * @param bgRadius
     */
    public UniversalDialog setBgRadius(float bgRadius) {
        setBgRadius(bgRadius, bgRadius, bgRadius, bgRadius);
        return refreshBg();
    }

    public UniversalDialog setBgRadius(float left_top_radius, float right_top_radius, float right_bottom_radius, float left_bottom_radius) {
        bgBean.left_top_radius = dp2px(context, left_top_radius);
        bgBean.right_top_radius = dp2px(context, right_top_radius);
        bgBean.right_bottom_radius = dp2px(context, right_bottom_radius);
        bgBean.left_bottom_radius = dp2px(context, left_bottom_radius);
        return refreshBg();
    }

    /**
     * 设置背景圆角
     *
     * @param bgRadius
     */
    public UniversalDialog setBgRadiusPX(int bgRadius) {
        setBgRadiusPX(bgRadius, bgRadius, bgRadius, bgRadius);
        return refreshBg();
    }

    public UniversalDialog setBgRadiusPX(float left_top_radius, float right_top_radius, float right_bottom_radius, float left_bottom_radius) {
        bgBean.left_top_radius = left_top_radius;
        bgBean.right_top_radius = right_top_radius;
        bgBean.right_bottom_radius = right_bottom_radius;
        bgBean.left_bottom_radius = left_bottom_radius;
        return refreshBg();
    }


//    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置宽高>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 设置宽高(精确)
     */
    private UniversalDialog setWidthHeight() {
//        Window dialogWindow = getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = width;
//        lp.height = height;
//        dialogWindow.setAttributes(lp);
        ViewGroup.LayoutParams layoutParams = controlView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        controlView.setLayoutParams(layoutParams);
        return this;
    }

    /**
     * 精确宽
     *
     * @param width
     * @return
     */
    public UniversalDialog setWidth(int width) {
        this.width = dp2px(context, width);
        return setWidthHeight();
    }

    public UniversalDialog setWidthPX(int width) {
        this.width = width;
        return setWidthHeight();
    }

    /**
     * 最大宽
     *
     * @param width
     * @return
     */
    public UniversalDialog setMaxWidth(int width) {
        setMaxWidthPX(dp2px(context, width));
        return this;
    }

    public UniversalDialog setMaxWidthPX(int width) {
        controlView.setMaxWidth(width);
        return this;
    }

    /**
     * 最小宽
     *
     * @param width
     * @return
     */
    public UniversalDialog setMinWidth(int width) {
        setMinWidthPX(dp2px(context, width));
        return this;
    }

    public UniversalDialog setMinWidthPX(int width) {
        controlView.setMinimumWidth(width);
        return this;
    }


    /**
     * 精确高度
     *
     * @param height
     * @return
     */
    public UniversalDialog setHeight(int height) {
        this.height = dp2px(context, height);
        return setWidthHeight();
    }

    public UniversalDialog setHeightPX(int height) {
        this.height = height;
        return setWidthHeight();
    }

    /**
     * 最大高度
     *
     * @param height
     * @return
     */
    public UniversalDialog setMaxHeight(int height) {
        setMaxHeightPX(dp2px(context, height));
        return this;
    }

    public UniversalDialog setMaxHeightPX(int height) {
        controlView.setMaxHeight(height);
        return this;
    }

    /**
     * 最小高度
     *
     * @param height
     * @return
     */
    public UniversalDialog setMinHeight(int height) {
        setMinHeightPX(dp2px(context, height));
        return this;
    }

    public UniversalDialog setMinHeightPX(int height) {
        controlView.setMinimumHeight(height);
        return this;
    }


    /**
     * 设置宽占屏幕的比例
     *
     * @param widthRatio
     */
    public UniversalDialog setWidthRatio(double widthRatio) {
        width = (int) (ScreenUtils.getWidthPixels(context) * widthRatio);
        setWidthHeight();
        return this;
    }

    public UniversalDialog setMaxWidthRatio(double widthRatio) {
        return setMaxWidthPX((int) (ScreenUtils.getWidthPixels(context) * widthRatio));
    }

    public UniversalDialog setMinWidthRatio(double widthRatio) {
        return setMinWidthPX((int) (ScreenUtils.getWidthPixels(context) * widthRatio));
    }

    /**
     * 设置高占屏幕的比例
     *
     * @param heightRatio
     */
    public UniversalDialog setHeightRatio(double heightRatio) {
        height = (int) (ScreenUtils.getHeightPixels(context) * heightRatio);
        setWidthHeight();
        return this;
    }

    public UniversalDialog setMaxHeightRatio(double heightRatio) {
        return setMaxHeightPX((int) (ScreenUtils.getWidthPixels(context) * heightRatio));
    }

    public UniversalDialog setMinHeightRatio(double heightRatio) {
        return setMinHeightPX((int) (ScreenUtils.getWidthPixels(context) * heightRatio));
    }

//    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 设置监听
     *
     * @param onClickListener
     * @param viewIds
     */
    public UniversalDialog setOnClickListener(final DialogOnClickListener onClickListener, int... viewIds) {
        final UniversalDialog universalDialog = this;
        for (int i = 0; i < viewIds.length; i++) {
            if (cancelIds.contains(viewIds[i])) {
                getView(viewIds[i]).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClick(v, universalDialog);
                        universalDialog.dismiss();
                    }
                });
            } else {
                getView(viewIds[i]).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClick(v, universalDialog);
                    }
                });
            }

        }
        return this;
    }

    /**
     * 设置 关闭dialog的按钮
     *
     * @param viewIds
     * @return
     */
    public UniversalDialog setCancelBtn(int... viewIds) {
        for (int i = 0; i < viewIds.length; i++) {
            if (cancelIds.contains(viewIds[i])) {
                continue;
            }
            cancelIds.add(viewIds[i]);
            getView(viewIds[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        return this;
    }

    //    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置常见属性>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The BaseViewHolder for chaining.
     */
    public UniversalDialog setText(@IdRes int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public UniversalDialog setText(@IdRes int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        view.setText(strId);
        return this;
    }

    /**
     * 设置文字大小
     *
     * @param viewId
     * @param size   (单位：SP)
     * @return
     */
    public UniversalDialog setTextSize(@IdRes int viewId, float size) {
        setTextSizePX(viewId, sp2px(context, size));
        return this;
    }

    public UniversalDialog setTextSizePX(@IdRes int viewId, float size) {
        TextView view = getView(viewId);
        view.setTextSize(sp2px(context, size));
        return this;
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param viewId     The view id.
     * @param imageResId The image resource id.
     * @return The BaseViewHolder for chaining.
     */
    public UniversalDialog setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    /**
     * Will set background color of a view.
     *
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The BaseViewHolder for chaining.
     */
    public UniversalDialog setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * Will set background of a view.
     *
     * @param viewId        The view id.
     * @param backgroundRes A resource to use as a background.
     * @return The BaseViewHolder for chaining.
     */
    public UniversalDialog setBackgroundRes(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @return The BaseViewHolder for chaining.
     */
    public UniversalDialog setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }


    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The BaseViewHolder for chaining.
     */
    public UniversalDialog setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * Add an action to set the image of an image view. Can be called multiple times.
     */
    public UniversalDialog setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    public UniversalDialog setAlpha(@IdRes int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for GONE.
     * @return The BaseViewHolder for chaining.
     */
    public UniversalDialog setGone(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * Set a view visibility to VISIBLE (true) or INVISIBLE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for INVISIBLE.
     * @return The BaseViewHolder for chaining.
     */
    public UniversalDialog setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    public UniversalDialog setVisible(@IdRes int viewId, int visible) {
        View view = getView(viewId);
        view.setVisibility(visible);
        return this;
    }

    public static GradientDrawable getRoundRectDrawable(int radius, int color) {
        //左上、右上、右下、左下的圆角半径
        float[] radiuss = {radius, radius, radius, radius, radius, radius, radius, radius};
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(radiuss);
        drawable.setColor(color != 0 ? color : Color.TRANSPARENT);
        return drawable;
    }
//    public static GradientDrawable getRoundRectDrawable(int radius, int color){
//        //左上、右上、右下、左下的圆角半径
//        float[] radiuss = {radius, radius, radius, radius, radius, radius, radius, radius};
//        GradientDrawable drawable = new GradientDrawable();
//        drawable.setCornerRadii(radiuss);
//        drawable.setColor(color!=0 ? color : Color.TRANSPARENT);
//        return drawable;
//    }
//    public static GradientDrawable getRoundRectDrawable(int radius, int color, boolean isFill, int strokeWidth){
//        //左上、右上、右下、左下的圆角半径
//        float[] radiuss = {radius, radius, radius, radius, radius, radius, radius, radius};
//        GradientDrawable drawable = new GradientDrawable();
//        drawable.setCornerRadii(radiuss);
//        drawable.setColor(isFill ? color : Color.TRANSPARENT);
//        drawable.setStroke(isFill ? 0 : strokeWidth, color);
//        return drawable;
//    }

    public int getColor(int colorResId) {
        return context.getResources().getColor(colorResId);
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    public interface DialogOnClickListener {
        void onClick(View v, UniversalDialog universalDialog);
    }
}
