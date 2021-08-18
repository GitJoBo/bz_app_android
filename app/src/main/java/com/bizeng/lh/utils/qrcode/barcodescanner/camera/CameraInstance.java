package com.bizeng.lh.utils.qrcode.barcodescanner.camera;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import com.bizeng.lh.R;
import com.bizeng.lh.utils.qrcode.barcodescanner.Size;
import com.bizeng.lh.utils.qrcode.barcodescanner.Util;


/**
 * Manage a camera instance using a background thread.
 * 使用后台线程管理相机实例。
 *
 * All methods must be called from the main thread.
 * 所有方法都必须从主线程调用。
 */
public class CameraInstance {
    private static final String TAG = CameraInstance.class.getSimpleName();

    private CameraThread cameraThread;
    private CameraSurface surface;

    private CameraManager cameraManager;
    private Handler readyHandler;
    private DisplayConfiguration displayConfiguration;
    private boolean open = false;
    private CameraSettings cameraSettings = new CameraSettings();

    /**
     * Construct a new CameraInstance.
     * 构造一个新的 CameraInstance。
     *
     * A new CameraManager is created.
     * 创建了一个新的 CameraManager。
     *
     * @param context the Android Context
     */
    public CameraInstance(Context context) {
        Util.validateMainThread();

        this.cameraThread = CameraThread.getInstance();
        this.cameraManager = new CameraManager(context);
        this.cameraManager.setCameraSettings(cameraSettings);
    }

    /**
     * Construct a new CameraInstance with a specific CameraManager.
     * 使用特定的 CameraManager 构造一个新的 CameraInstance。
     *
     * @param cameraManager the CameraManager to use
     */
    public CameraInstance(CameraManager cameraManager) {
        Util.validateMainThread();

        this.cameraManager = cameraManager;
    }

    public void setDisplayConfiguration(DisplayConfiguration configuration) {
        this.displayConfiguration = configuration;
        cameraManager.setDisplayConfiguration(configuration);
    }

    public DisplayConfiguration getDisplayConfiguration() {
        return displayConfiguration;
    }

    public void setReadyHandler(Handler readyHandler) {
        this.readyHandler = readyHandler;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        setSurface(new CameraSurface(surfaceHolder));
    }

    public void setSurface(CameraSurface surface) {
        this.surface = surface;
    }

    public CameraSettings getCameraSettings() {
        return cameraSettings;
    }

    /**
     * This only has an effect if the camera is not opened yet.
     * 这仅在相机尚未打开时有效。
     *
     * @param cameraSettings the new camera settings
     */
    public void setCameraSettings(CameraSettings cameraSettings) {
        if (!open) {
            this.cameraSettings = cameraSettings;
            this.cameraManager.setCameraSettings(cameraSettings);
        }
    }

    /**
     * Actual preview size in current rotation. null if not determined yet.
     * 当前旋转中的实际预览大小。如果尚未确定，则为 null。
     *
     * @return preview size
     */
    private Size getPreviewSize() {
        return cameraManager.getPreviewSize();
    }

    /**
     *
     * @return the camera rotation relative to display rotation, in degrees. Typically 0 if the
     *    display is in landscape orientation.
     *    相对于显示旋转的相机旋转，以度为单位。如果显示为横向，通常为 0。
     */
    public int getCameraRotation() {
        return cameraManager.getCameraRotation();
    }

    public void open() {
        Util.validateMainThread();

        open = true;

        cameraThread.incrementAndEnqueue(opener);
    }

    public void configureCamera() {
        Util.validateMainThread();
        validateOpen();

        cameraThread.enqueue(configure);
    }

    public void startPreview() {
        Util.validateMainThread();
        validateOpen();

        cameraThread.enqueue(previewStarter);
    }

    public void setTorch(final boolean on) {
        Util.validateMainThread();

        if (open) {
            cameraThread.enqueue(new Runnable() {
                @Override
                public void run() {
                    cameraManager.setTorch(on);
                }
            });
        }
    }

    public void close() {
        Util.validateMainThread();

        if (open) {
            cameraThread.enqueue(closer);
        }

        open = false;
    }

    public boolean isOpen() {
        return open;
    }

    public void requestPreview(final PreviewCallback callback) {
        validateOpen();

        cameraThread.enqueue(new Runnable() {
            @Override
            public void run() {
                cameraManager.requestPreviewFrame(callback);
            }
        });
    }

    private void validateOpen() {
        if (!open) {
            throw new IllegalStateException("CameraInstance is not open");
        }
    }

    private Runnable opener = new Runnable() {
        @Override
        public void run() {
            try {
                Log.d(TAG, "Opening camera");
                cameraManager.open();
            } catch (Exception e) {
                notifyError(e);
                Log.e(TAG, "Failed to open camera", e);
            }
        }
    };

    private Runnable configure = new Runnable() {
        @Override
        public void run() {
            try {
                Log.d(TAG, "Configuring camera");
                cameraManager.configure();
                if (readyHandler != null) {
                    readyHandler.obtainMessage(R.id.zxing_prewiew_size_ready, getPreviewSize()).sendToTarget();
                }
            } catch (Exception e) {
                notifyError(e);
                Log.e(TAG, "Failed to configure camera", e);
            }
        }
    };

    private Runnable previewStarter = new Runnable() {
        @Override
        public void run() {
            try {
                Log.d(TAG, "Starting preview");
                cameraManager.setPreviewDisplay(surface);
                cameraManager.startPreview();
            } catch (Exception e) {
                notifyError(e);
                Log.e(TAG, "Failed to start preview", e);
            }
        }
    };

    private Runnable closer = new Runnable() {
        @Override
        public void run() {
            try {
                Log.d(TAG, "Closing camera");
                cameraManager.stopPreview();
                cameraManager.close();
            } catch (Exception e) {
                Log.e(TAG, "Failed to close camera", e);
            }

            cameraThread.decrementInstances();
        }
    };

    private void notifyError(Exception error) {
        if (readyHandler != null) {
            readyHandler.obtainMessage(R.id.zxing_camera_error, error).sendToTarget();
        }
    }

    /**
     * Returns the CameraManager used to control the camera.
     * 返回用于控制相机的 CameraManager。
     *
     * The CameraManager is not thread-safe, and must only be used from the CameraThread.
     * CameraManager 不是线程安全的，只能从 CameraThread 使用。
     *
     * @return the CameraManager used
     */
    protected CameraManager getCameraManager() {
        return cameraManager;
    }

    /**
     *
     * @return the CameraThread used to manage the camera
     * @return 用于管理相机的 CameraThread
     */
    protected CameraThread getCameraThread() {
        return cameraThread;
    }

    /**
     *
     * @return the surface om which the preview is displayed
     * @return 显示预览的表面
     */
    protected CameraSurface getSurface() {
        return surface;
    }
}
