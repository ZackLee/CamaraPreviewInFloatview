package me.lino.floatviewdemo;

import android.graphics.Matrix;
import android.view.TextureView;

/**
 * Created by Macx on 7/26/16.
 */

public interface ICamara {
    public void openCamera(int width, int height);

    public void closeCamera();

    public Matrix configureTransform(int width, int height);

    public void startBackgroundThread();

    public void stopBackgroundThread();


}
