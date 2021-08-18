package com.bizeng.lh.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bizeng.lh.R;
import com.bizeng.lh.base.BaseActivity;
import com.bizeng.lh.base.BaseViewModel;
import com.bizeng.lh.bean.event.ScanEvent;
import com.bizeng.lh.databinding.ZxingCaptureBinding;
import com.bizeng.lh.utils.PhotoUtils;
import com.bizeng.lh.utils.qrcode.QRCodeUtils;
import com.bizeng.lh.utils.qrcode.barcodescanner.BarcodeResult;
import com.bizeng.lh.utils.qrcode.barcodescanner.CaptureManager;
import com.bizeng.lh.utils.qrcode.barcodescanner.DecoratedBarcodeView;
import com.chad.library.BR;
import com.wzq.mvvmsmart.utils.KLog;

import org.greenrobot.eventbus.EventBus;

/**
 * @Desc: 扫一扫
 * @author: admin wsj
 * @Date: 2021/6/25 10:31 AM
 */
public class ScanActivity extends BaseActivity<ZxingCaptureBinding, BaseViewModel> implements View.OnClickListener {
    private CaptureManager capture;
    private TextView lightControlTv;
    private TextView selectPicTv;
    private TextView tipsTv;
    private PhotoUtils photoUtils;
    private boolean isCameraLightOn = false;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.zxing_capture;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public String setTitleBar() {
        return "扫一扫";
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                String result = QRCodeUtils.analyzeImage(uri.getPath());
//                String result = QRCodeUtils.analyzeImage(PhotoUtils.getRealPathFromURI(ScanActivity.this, uri));
                handleQrCode(result);
            }

            @Override
            public void onPhotoCancel() {
            }
        });
        lightControlTv = findViewById(R.id.zxing_open_light);
        lightControlTv.setOnClickListener(this);
        selectPicTv = findViewById(R.id.zxing_select_pic);
        selectPicTv.setOnClickListener(this);
        tipsTv = findViewById(R.id.zxing_user_tips);
        capture = new CaptureManager(this, binding.zxingBarcodeScanner);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.setOnCaptureResultListener(new CaptureManager.OnCaptureResultListener() {
            @Override
            public void onCaptureResult(BarcodeResult result) {
                handleQrCode(result.toString());
            }
        });
        capture.decode();
//        binding.zxingBarcodeScanner.getViewFinder().networkChange(!NetworkUtils.isNetWorkAvailable(this));
//        if (!NetworkUtils.isNetWorkAvailable(this)) {
//            capture.stopDecode();
//        } else {
//            capture.decode();
//        }
        binding.zxingBarcodeScanner.setTorchListener(new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                lightControlTv.setText(R.string.zxing_close_light);
                isCameraLightOn = true;
            }

            @Override
            public void onTorchOff() {
                lightControlTv.setText(R.string.zxing_open_light);
                isCameraLightOn = false;
            }
        });
    }

    /**
     * 从相册中选中
     */
    public void scanFromAlbum() {
        photoUtils.selectPicture(this);
    }

    /**
     * 切换摄像头照明
     */
    private void switchCameraLight() {
        if (isCameraLightOn) {
            binding.zxingBarcodeScanner.setTorchOff();
        } else {
            binding.zxingBarcodeScanner.setTorchOn();
        }
    }

    /**
     * 处理二维码结果，并跳转到相应界面
     *
     * @param qrCodeText
     */
    private void handleQrCode(String qrCodeText) {
        KLog.d(TAG, qrCodeText);
        ScanEvent<String> event = new ScanEvent<>();
        event.code = 0;
        event.data = qrCodeText;
        EventBus.getDefault().post(event);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zxing_open_light:
                switchCameraLight();
                break;
            case R.id.zxing_select_pic:
                scanFromAlbum();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return binding.zxingBarcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        photoUtils.onActivityResult(this, requestCode, resultCode, data);
    }
}
