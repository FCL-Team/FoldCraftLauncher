package com.tungsten.fcllibrary.crash;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.LogSharingUtilsKt;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class CrashReportActivity extends FCLActivity implements View.OnClickListener {

    private FCLButton restart;
    private FCLButton close;
    private FCLButton upload;
    private FCLButton share;

    private FCLTextView error;
    private CrashReporterConfig config;
    private View root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        // 崩溃页背景跟随 FCL 亮暗设置：windowBackground 只在创建时按进程初始模式解析，
        // 亮暗切换（configChanges=uiMode）时不会自动更新，需显式设置并随主题刷新
        root = findViewById(R.id.root);
        ThemeEngine.getInstance().registerEvent(root, this::applyWindowBackground);
        if (!getSharedPreferences("launcher", MODE_PRIVATE).getBoolean("allowScreenshots", false)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }

        config = CrashReporter.getConfigFromIntent(getIntent());

        if (config == null) {
            finish();
        }

        restart = findViewById(R.id.restart);
        close = findViewById(R.id.close);
        upload = findViewById(R.id.upload);
        share = findViewById(R.id.share);

        restart.setOnClickListener(this);
        close.setOnClickListener(this);
        upload.setOnClickListener(this);
        share.setOnClickListener(this);

        error = findViewById(R.id.error);
        error.setText(CrashReporter.getAllErrorDetailsFromIntent(this, getIntent()));
    }

    /** 解析当前生效主题的窗口背景色并设置到根布局，与主界面背景保持一致 */
    private void applyWindowBackground() {
        TypedValue outValue = new TypedValue();
        int color;
        if (getTheme().resolveAttribute(android.R.attr.windowBackground, outValue, true)
                && outValue.type >= TypedValue.TYPE_FIRST_COLOR_INT
                && outValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            color = outValue.data;
        } else {
            // windowBackground 非纯色时退回按亮暗取黑白
            color = ThemeEngine.getInstance().isNightMode(this) ? Color.BLACK : Color.WHITE;
        }
        root.setBackgroundColor(color);
    }

    @Override
    public void onClick(View view) {
        if (view == restart) {
            CrashReporter.restartApplication(this, config);
        }
        if (view == close) {
            CrashReporter.closeApplication(this, config);
        }
        if (view == upload) {
            LogSharingUtilsKt.uploadLog(this, error.getText().toString());
        }
        if (view == share) {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                File file = File.createTempFile("crash_report", ".txt");
                Files.write(file.toPath(), CrashReporter.getAllErrorDetailsFromIntent(this, getIntent()).getBytes(StandardCharsets.UTF_8));
                Uri uri = FileProvider.getUriForFile(this, getApplication().getPackageName() + ".provider", file);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, getString(R.string.crash_reporter_share)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
