package com.bizeng.lh.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.bizeng.lh.R;
import com.bizeng.lh.base.Content;
import com.liys.dialoglib.UniversalDialog;

/**
 * @Desc: dialog统一管理
 * @author: admin wsj
 * @Date: 2021/4/21 2:16 PM
 */
public class DialogUtils {
    private volatile static DialogUtils instance = null;

    private DialogUtils() {

    }

    public static DialogUtils getInstance() {
        if (instance == null) {
            synchronized (DialogUtils.class) {
                if (instance == null) {
                    instance = new DialogUtils();
                }
            }
        }
        return instance;
    }

    public UniversalDialog showTip(final Context context, String content,
                                   UniversalDialog.DialogOnClickListener negativeClickListener, UniversalDialog.DialogOnClickListener positiveClickListener) {
        return showTip(context, null, content, null, null, negativeClickListener, positiveClickListener);
    }

    public UniversalDialog showTip(final Context context, int content,
                                   UniversalDialog.DialogOnClickListener negativeClickListener, UniversalDialog.DialogOnClickListener positiveClickListener) {
        return showTip(context, 0, content, 0, 0, negativeClickListener, positiveClickListener);
    }

    public UniversalDialog showTip(final Context context, String title, String content,
                                   UniversalDialog.DialogOnClickListener negativeClickListener, UniversalDialog.DialogOnClickListener positiveClickListener) {
        return showTip(context, title, content, null, null, negativeClickListener, positiveClickListener);
    }

    public UniversalDialog showTip(final Context context, int title, int content,
                                   UniversalDialog.DialogOnClickListener negativeClickListener, UniversalDialog.DialogOnClickListener positiveClickListener) {
        return showTip(context, title, content, 0, 0, negativeClickListener, positiveClickListener);
    }


    /**
     * 普通通知 弹框【标题居中，居中内容，左右底部按钮】
     */
    public UniversalDialog showTip(final Context context, String title, String content, String btnLef, String btnRight,
                                   UniversalDialog.DialogOnClickListener negativeClickListener, UniversalDialog.DialogOnClickListener positiveClickListener) {
        UniversalDialog dialog = UniversalDialog.newInstance(context, R.layout.commom_dialog_tip);
        dialog.setWidthRatio(Content.WIDTH_RATIO);
        if (!TextUtils.isEmpty(title)) {
            dialog.setText(R.id.dialog_tv_title, title);
            dialog.setVisible(R.id.dialog_tv_title, true);
        } else {
            dialog.setVisible(R.id.dialog_tv_title, false);
        }
        if (!TextUtils.isEmpty(content)) {
            dialog.setText(R.id.dialog_tv_content, content);
        }
        if (!TextUtils.isEmpty(btnLef)) {
            dialog.setText(R.id.dialog_btn_negative, btnLef);
        }
        if (!TextUtils.isEmpty(btnRight)) {
            dialog.setText(R.id.dialog_btn_positive, btnRight);
        }
        showAndClick(negativeClickListener, positiveClickListener, dialog);
        return dialog;
    }

    /**
     * 普通通知 弹框【标题居中，居中内容，左右底部按钮】
     */
    public UniversalDialog showTip(final Context context, int title, int content, int btnLef, int btnRight,
                                   UniversalDialog.DialogOnClickListener negativeClickListener, UniversalDialog.DialogOnClickListener positiveClickListener) {
        UniversalDialog dialog = UniversalDialog.newInstance(context, R.layout.commom_dialog_tip);
        dialog.setWidthRatio(Content.WIDTH_RATIO);
        if (title != 0) {
            dialog.setText(R.id.dialog_tv_title, title);
        }
        if (content != 0) {
            dialog.setText(R.id.dialog_tv_content, content);
        }
        if (btnLef != 0) {
            dialog.setText(R.id.dialog_btn_negative, btnLef);
        }
        if (btnRight != 0) {
            dialog.setText(R.id.dialog_btn_positive, btnRight);
        }
        showAndClick(negativeClickListener, positiveClickListener, dialog);

        return dialog;
    }

    private void showAndClick(UniversalDialog.DialogOnClickListener negativeClickListener, UniversalDialog.DialogOnClickListener positiveClickListener, UniversalDialog dialog) {
        dialog.show();

        dialog.getView(R.id.dialog_btn_negative).setOnClickListener(v -> {
            dialog.dismiss();
            if (negativeClickListener != null) {
                negativeClickListener.onClick(v, dialog);
            }
        });
        dialog.getView(R.id.dialog_btn_positive).setOnClickListener(v -> {
            dialog.dismiss();
            if (positiveClickListener != null) {
                positiveClickListener.onClick(v, dialog);
            }
        });
    }

    /**
     * 普通通知 弹框【标题居中，居中内容，底部一个按钮】
     */
    public UniversalDialog showTip(final Context context, String title, String content, String btnStr, UniversalDialog.DialogOnClickListener positiveClickListener) {
        UniversalDialog dialog = UniversalDialog.newInstance(context, R.layout.commom_dialog_tip2);
        dialog.setWidthRatio(Content.WIDTH_RATIO);
        if (!TextUtils.isEmpty(title)) {
            dialog.setText(R.id.dialog_tv_title, title);
            dialog.setVisible(R.id.dialog_tv_title, true);
        } else {
            dialog.setVisible(R.id.dialog_tv_title, View.GONE);
        }
        if (!TextUtils.isEmpty(content)) {
            dialog.setText(R.id.dialog_tv_content, content);
        }
        if (!TextUtils.isEmpty(btnStr)) {
            dialog.setText(R.id.dialog_btn_positive, btnStr);
        }
        dialog.show();
        dialog.getView(R.id.dialog_btn_positive).setOnClickListener(v -> {
            dialog.dismiss();
            if (positiveClickListener != null) {
                positiveClickListener.onClick(v, dialog);
            }
        });
        return dialog;
    }
}
