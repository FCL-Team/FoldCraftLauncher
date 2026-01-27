package com.tungsten.fcllibrary.crash;

import static com.tungsten.fclcore.util.Pair.pair;

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

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.io.HttpRequest;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.LogSharingUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrashReportActivity extends FCLActivity implements View.OnClickListener {

    private FCLButton restart;
    private FCLButton close;
    private FCLButton copy;
    private FCLButton upload;
    private FCLButton share;

    private FCLTextView error;
    private FCLProgressBar progressBar;

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
        upload = findViewById(R.id.upload);
        share = findViewById(R.id.share);

        restart.setOnClickListener(this);
        close.setOnClickListener(this);
        copy.setOnClickListener(this);
        upload.setOnClickListener(this);
        share.setOnClickListener(this);

        error = findViewById(R.id.error);
        progressBar = findViewById(R.id.progress);
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
        if (view == upload) {
            uploadLog();
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

    private void setLoading(boolean loading) {
        Schedulers.androidUIThread().execute(() -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        });
    }

    private void uploadLog() {
        setLoading(true);
        CompletableFuture.runAsync(() -> {
            try {
                String logContent = error.getText().toString();
                String apiUrl = LogSharingUtils.getLogUploadApiUrl(this);
                String response = HttpRequest.POST(apiUrl)
                        .form(pair("content", logContent))
                        .getString();

                // Response format: {"success":true,"url":"https://mclo.gs/XXXXX"}
                Pattern pattern = Pattern.compile("\"url\":\"(.*?)\"");
                Matcher matcher = pattern.matcher(response);
                if (matcher.find()) {
                    String url = matcher.group(1).replace("\\/", "/");
                    Schedulers.androidUIThread().execute(() -> {
                        setLoading(false);
                        LogSharingUtils.showLogUploadSuccessDialog(this, url);
                    });
                } else {
                    throw new IOException("Failed to parse response: " + response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Schedulers.androidUIThread().execute(() -> {
                    setLoading(false);
                    Toast.makeText(this, getString(R.string.upload_failed, e.getMessage()), Toast.LENGTH_LONG).show();
                });
            }
        });
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
