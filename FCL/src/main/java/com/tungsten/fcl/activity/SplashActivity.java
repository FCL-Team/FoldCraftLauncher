package com.tungsten.fcl.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tungsten.fcl.R;
import com.tungsten.fcl.fragment.EulaFragment;
import com.tungsten.fcl.fragment.RuntimeFragment;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.ResultListener;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

import java.io.File;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends FCLActivity {

    public ConstraintLayout background;

    private EulaFragment eulaFragment;
    private RuntimeFragment runtimeFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        background = findViewById(R.id.background);
        background.setBackground(ThemeEngine.getInstance().getTheme().getBackground(this));

        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                init();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                ResultListener.startActivityForResult(this, intent, RequestCodes.PERMISSION_REQUEST_CODE, (requestCode, resultCode, data) -> {
                    if (requestCode == RequestCodes.PERMISSION_REQUEST_CODE) {
                        if (Environment.isExternalStorageManager()) {
                            init();
                        } else {
                            recheckPermission();
                        }
                    }
                });
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestCodes.PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void recheckPermission() {
        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
        builder.setMessage(getString(R.string.splash_permission_msg));
        builder.setPositiveButton(this::checkPermission);
        builder.setNegativeButton(this::finish);
        builder.create().show();
    }

    private void init() {
        FCLPath.loadPaths(this);
        Logging.start(new File(FCLPath.FILES_DIR, "logs").toPath());
        initFragments();
        start();
    }

    private void initFragments() {
        eulaFragment = new EulaFragment();
        runtimeFragment = new RuntimeFragment();
    }

    public void start() {
        SharedPreferences sharedPreferences = getSharedPreferences("launcher", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isFirstLaunch", true)) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.frag_start_anim, R.anim.frag_stop_anim).replace(R.id.fragment, eulaFragment).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.frag_start_anim, R.anim.frag_stop_anim).replace(R.id.fragment, runtimeFragment).commit();
        }
    }

    public void enterLauncher() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCodes.PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    recheckPermission();
                }
            }
        }
    }
}
