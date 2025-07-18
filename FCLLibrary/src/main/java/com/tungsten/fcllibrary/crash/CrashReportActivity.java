package com.tungsten.fcllibrary.crash;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class CrashReportActivity extends FCLActivity implements View.OnClickListener {

    private FCLButton restart;
    private FCLButton close;
    private FCLButton copy;
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
            // This should never happen - Just finish the activity to avoid a recursive crash.
            finish();
        }

        restart = findViewById(R.id.restart);
        close = findViewById(R.id.close);
        copy = findViewById(R.id.copy);
        share = findViewById(R.id.share);

        restart.setOnClickListener(this);
        close.setOnClickListener(this);
        copy.setOnClickListener(this);
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
        if (view == copy) {
            copyErrorToClipboard();
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

    private void copyErrorToClipboard() {
        String errorInformation = CrashReporter.getAllErrorDetailsFromIntent(this, getIntent());
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        // Are there any devices without clipboard...?
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(null, errorInformation);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, R.string.crash_reporter_toast, Toast.LENGTH_SHORT).show();
        }
    }

}
