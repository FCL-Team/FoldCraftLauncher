package com.tungsten.fcllibrary.crash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.FCLActivity;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

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
