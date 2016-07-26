package me.lino.floatviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//        checkPermission();
//        requestMultiplePermissions();
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    private void checkPermission() {
//        if (!Settings.canDrawOverlays(MainActivity.this)) {//判断android 6.0中是否开启权限
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + getPackageName()));
//            //跳转到权限设置页面
//            startActivityForResult(intent, 110);
//        }
//        int permissionCheck = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.CAMERA);
//    }


//    @TargetApi(Build.VERSION_CODES.M)
//    private void requestMultiplePermissions() {
//        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE};
//        requestPermissions(permissions, 000);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(MainActivity.this, FloatService.class);
        startService(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
