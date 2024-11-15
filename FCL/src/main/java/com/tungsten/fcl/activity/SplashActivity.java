package com.tungsten.fcl.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.tungsten.fcl.R;
import com.tungsten.fcl.fragment.EulaFragment;
import com.tungsten.fcl.fragment.RuntimeFragment;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclauncher.plugins.RendererPlugin;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.ResultListener;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends FCLActivity {

    public ConstraintLayout background;

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
            if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                    // 若用户第一次拒绝了授予，那么将会弹窗提醒用户为什么需要该权限
                    enableAlertDialog(() -> {
                        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, RequestCodes.PERMISSION_REQUEST_CODE);
                    }, getString(R.string.splash_permission_title), getString(R.string.splash_permission_msg), getString(R.string.splash_permission_grant), getString(R.string.splash_permission_close));
                } else {
                    // 没有勾选始终拒绝的化则继续请求权限
                    ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, RequestCodes.PERMISSION_REQUEST_CODE);
                }
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
        transFile();
        Logging.start(Paths.get(FCLPath.LOG_DIR));
        start();
    }

    public void start() {
        SharedPreferences sharedPreferences = getSharedPreferences("launcher", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isFirstLaunch", true)) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.frag_start_anim, R.anim.frag_stop_anim).replace(R.id.fragment, EulaFragment.class, null).commit();
        } else {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.frag_start_anim, R.anim.frag_stop_anim).replace(R.id.fragment, RuntimeFragment.class, null).commit();
        }
    }

    private void transFile() {
        try {
            Path controlDir = Paths.get(FCLPath.FILES_DIR + "/control");
            if (controlDir.toFile().exists()) {
                FileUtils.copyDirectory(controlDir, Paths.get(FCLPath.CONTROLLER_DIR));
                FileUtils.deleteDirectory(controlDir.toFile());
            }
        } catch (IOException ignore) {
        }
    }

    public void enterLauncher() {
        RendererPlugin.init(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCodes.PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || !ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                    // 用户勾选了“始终拒绝”
                    enableAlertDialog(() -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        ResultListener.startActivityForResult(this, intent, RequestCodes.PERMISSION_REQUEST_CODE, (requestCode1, resultCode, data) -> {
                            if (requestCode1 == RequestCodes.PERMISSION_REQUEST_CODE) {
                                if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    init();
                                } else {
                                    onRequestPermissionsResult(requestCode1, permissions, grantResults);
                                }
                            }
                        });
                    }, getString(R.string.splash_permission_title), getString(R.string.splash_permission_settings_msg), getString(R.string.splash_permission_settings), getString(R.string.splash_permission_close));
                } else {
                    // 用户只是拒绝了权限
                    checkPermission();
                }
            }
        }
    }

    private void enableAlertDialog(FCLAlertDialog.ButtonListener positiveButtonEvent, String... strings) {
        new FCLAlertDialog.Builder(this)
                .setTitle(String.valueOf(strings[0]))
                .setMessage(String.valueOf(strings[1]))
                .setPositiveButton(String.valueOf(strings[2]), positiveButtonEvent)
                .setNegativeButton(String.valueOf(strings[3]), () -> System.exit(0))
                .setCancelable(false)
                .create()
                .show();
    }
}
