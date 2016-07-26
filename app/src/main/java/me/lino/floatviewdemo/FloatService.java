package me.lino.floatviewdemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


public class FloatService extends Service {

    WindowManager mWindowManager;
    WindowManager.LayoutParams mParams;

    private ICamara camara;
    ViewGroup mFloatView;
    private AutoFitTextureView mTextureView;
    private boolean mFloating;

    public FloatService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mWindowManager == null) {
            mFloatView = (ViewGroup) View.inflate(this, R.layout.video_view_float, null);
            mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            mTextureView = (AutoFitTextureView) mFloatView.findViewById(R.id.vv);
            mFloatView.setOnTouchListener(touchListener);

            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            mParams.gravity = Gravity.LEFT | Gravity.TOP;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                // your code using Camera API here - is between 1-20
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // your code using Camera2 API here - is api 21 or higher
                camara = new Camera2Impl(getApplication(), mTextureView);
            }
        }

        addView();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 拖拽Video更换位置
     */
    private final View.OnTouchListener touchListener = new View.OnTouchListener() {
        int lastX, lastY, x, y;

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            switch (MotionEvent.ACTION_MASK & e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = (int) e.getRawX();
                    lastY = (int) e.getRawY();
                    x = mParams.x;
                    y = mParams.y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dx = (int) e.getRawX() - lastX;
                    int dy = (int) e.getRawY() - lastY;
                    mParams.x = x + dx;
                    mParams.y = y + dy;
                    mWindowManager.updateViewLayout(mFloatView, mParams);
                    break;
            }
            return true;
        }
    };

    private void addView() {
        if (mFloating) return;
        mParams.width = 400;//VideoView不能获取Video的宽高度
        mParams.height = 600;
        mWindowManager.addView(mFloatView, mParams);
        mFloating = true;

        camara.startBackgroundThread();

        if (mTextureView.isAvailable()) {
            camara.openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    private void removeView() {
        if (mFloating)
            mWindowManager.removeView(mFloatView);
        camara.closeCamera();
        camara.stopBackgroundThread();
        mFloating = false;
    }

    @Override
    public void onDestroy() {
        removeView();
        super.onDestroy();
    }


    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
     * {@link TextureView}.
     */
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            camara.openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mTextureView.setTransform(camara.configureTransform(400, 600));
        super.onConfigurationChanged(newConfig);
    }

}
