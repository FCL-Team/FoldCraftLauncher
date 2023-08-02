package com.tungsten.fcl.upgrade;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.FileProvider;

import com.tungsten.fcl.R;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.io.File;
import java.util.concurrent.CancellationException;

public class UpdateDialog extends FCLDialog implements View.OnClickListener {

    private final RemoteVersion version;

    private View parent;
    private ScrollView scrollView;
    private FCLLinearLayout layout;

    private FCLTextView versionName;
    private FCLTextView date;
    private FCLTextView type;
    private FCLTextView description;

    private FCLButton ignore;
    private FCLButton positive;
    private FCLButton negative;
    private FCLButton netdisk;

    public UpdateDialog(@NonNull Context context, RemoteVersion version) {
        super(context);
        this.version = version;
        setCancelable(false);
        setContentView(R.layout.dialog_update);

        init();
    }

    private void init() {
        parent = findViewById(R.id.parent);
        scrollView = findViewById(R.id.text_scroll);
        layout = findViewById(R.id.layout);

        versionName = findViewById(R.id.version);
        date = findViewById(R.id.date);
        type = findViewById(R.id.type);
        description = findViewById(R.id.description);

        versionName.setText(String.format(getContext().getString(R.string.update_version), version.getVersionName()));
        date.setText(String.format(getContext().getString(R.string.update_date), version.getDate()));
        type.setText(String.format(getContext().getString(R.string.update_type), version.getDisplayType(getContext())));
        description.setText(String.format(getContext().getString(R.string.update_description), version.getDisplayDescription(getContext())));

        ignore = findViewById(R.id.ignore);
        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        netdisk = findViewById(R.id.netdisk);
        ignore.setOnClickListener(this);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
        netdisk.setOnClickListener(this);

        checkHeight();
    }

    private void checkHeight() {
        parent.post(() -> layout.post(() -> {
            WindowManager wm = getWindow().getWindowManager();
            Point point = new Point();
            wm.getDefaultDisplay().getSize(point);
            int maxHeight = point.y - ConvertUtils.dip2px(getContext(), 30);
            if (parent.getMeasuredHeight() < maxHeight) {
                ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
                layoutParams.height = layout.getMeasuredHeight();
                scrollView.setLayoutParams(layoutParams);
                getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            } else {
                getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, maxHeight);
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v == ignore) {
            UpdateChecker.setIgnore(getContext(), version.getVersionCode());
            dismiss();
        }
        if (v == positive) {
            TaskDialog dialog = new TaskDialog(getContext(), new TaskCancellationAction(AppCompatDialog::dismiss));
            dialog.setTitle(getContext().getString(R.string.update_launcher));
            Schedulers.androidUIThread().execute(() -> {
                TaskExecutor executor = Task.composeAsync(() -> {
                    FileDownloadTask task = new FileDownloadTask(NetworkUtils.toURL(version.getUrl()), new File(FCLPath.CACHE_DIR, "FoldCraftLauncher.apk"));
                    task.setName("FoldCraftLauncher");
                    return task.whenComplete(Schedulers.androidUIThread(), exception -> {
                        if (exception == null) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Uri apkUri = FileProvider.getUriForFile(getContext(), getContext().getString(com.tungsten.fcllibrary.R.string.file_browser_provider), new File(FCLPath.CACHE_DIR, "FoldCraftLauncher.apk"));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                            getContext().startActivity(intent);
                        } else if (!(exception instanceof CancellationException)) {
                            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                            builder.setCancelable(false);
                            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                            builder.setMessage(getContext().getString(R.string.update_failed) + "\n" + exception.getMessage());
                            builder.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                            builder.create().show();
                        }
                    });
                }).executor();
                dialog.setExecutor(executor);
                dialog.show();
                executor.start();
            });
            dismiss();
        }
        if (v == negative) {
            dismiss();
        }
        if (v == netdisk) {
            FCLBridge.openLink(version.getNetdiskUrl());
            ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("FCL Clipboard", "1145");
            clipboard.setPrimaryClip(clip);
            dismiss();
        }
    }
}
