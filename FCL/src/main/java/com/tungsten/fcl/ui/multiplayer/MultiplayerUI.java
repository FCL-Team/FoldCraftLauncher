package com.tungsten.fcl.ui.multiplayer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.terracotta.Terracotta;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLSwitch;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.File;
import java.util.Map;

public class MultiplayerUI extends FCLCommonUI implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private LinearLayoutCompat btnMain;
    private LinearLayoutCompat btnHost;
    private LinearLayoutCompat btnGuest;
    private LinearLayoutCompat btnFeedback;
    private LinearLayoutCompat btnEasytier;

    private ScrollView mainLayout;
    private ScrollView hostLayout;
    private ScrollView guestLayout;

    private FCLSwitch switchMultiplayer;
    private LinearLayoutCompat extraLayout;
    private FCLButton shareLog;

    public MultiplayerUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        btnMain = findViewById(R.id.about_terracotta);
        btnHost = findViewById(R.id.host_tutorial);
        btnGuest = findViewById(R.id.guest_tutorial);
        btnFeedback = findViewById(R.id.terracotta_feedback);
        btnEasytier = findViewById(R.id.about_easytier);

        btnMain.setOnClickListener(this);
        btnHost.setOnClickListener(this);
        btnGuest.setOnClickListener(this);
        btnFeedback.setOnClickListener(this);
        btnEasytier.setOnClickListener(this);

        // Todo: feedback link not available now, delete this shit later
        btnFeedback.setVisibility(View.GONE);

        mainLayout = findViewById(R.id.multiplayer_layout_settings);
        hostLayout = findViewById(R.id.tutorial_text_host);
        guestLayout = findViewById(R.id.tutorial_text_guest);

        mainLayout.setVisibility(View.VISIBLE);
        hostLayout.setVisibility(View.GONE);
        guestLayout.setVisibility(View.GONE);

        switchMultiplayer = findViewById(R.id.enable_multiplayer);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("third_party", Context.MODE_PRIVATE);
        switchMultiplayer.setChecked(sharedPreferences.getBoolean("terracotta", false));
        switchMultiplayer.setOnCheckedChangeListener(this);

        extraLayout = findViewById(R.id.extra_layout);
        shareLog = findViewById(R.id.export_log);
        extraLayout.setVisibility(switchMultiplayer.isChecked() ? View.VISIBLE : View.GONE);
        shareLog.setOnClickListener(this);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == btnMain) {
            mainLayout.setVisibility(View.VISIBLE);
            hostLayout.setVisibility(View.GONE);
            guestLayout.setVisibility(View.GONE);
        }
        if (v == btnHost) {
            mainLayout.setVisibility(View.GONE);
            hostLayout.setVisibility(View.VISIBLE);
            guestLayout.setVisibility(View.GONE);
        }
        if (v == btnGuest) {
            mainLayout.setVisibility(View.GONE);
            hostLayout.setVisibility(View.GONE);
            guestLayout.setVisibility(View.VISIBLE);
        }
        if (v == btnFeedback) {
            // Todo: feedback link not available now, rewrite this shit later
            PackageManager pm = getContext().getPackageManager();
            String versionName;
            try {
                PackageInfo packageInfo = pm.getPackageInfo(getContext().getPackageName(), 0);
                versionName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                versionName = "Unknown";
            }
            if (versionName == null)
                versionName = "Unknown";
            String link = NetworkUtils.withQuery("https://docs.hmcl.net/multiplayer/feedback.html", Map.of(
                    "v", "v1",
                    "launcher_version", versionName
            ));
            AndroidUtils.openLink(getContext(), link);
        }
        if (v == btnEasytier) {
            AndroidUtils.openLink(getContext(), "https://easytier.cn/");
        }

        if (v == shareLog) {
            if (new File(FCLPath.LOG_DIR, "terracotta.log").exists()) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                Uri uri = FileProvider.getUriForFile(getContext(), getContext().getString(com.tungsten.fcllibrary.R.string.file_browser_provider), new File(FCLPath.LOG_DIR, "terracotta.log"));
                intent.setType(AndroidUtils.getMimeType(FCLPath.LOG_DIR + "/" + "terracotta.log"));
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                getActivity().startActivity(Intent.createChooser(intent, getContext().getString(R.string.terracotta_export_log_share)));
            } else {
                Toast.makeText(getContext(), getContext().getString(R.string.terracotta_export_log_share_null), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == switchMultiplayer) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("third_party", Context.MODE_PRIVATE);
            int version = sharedPreferences.getInt("terracotta_user_notice", 0);
            if (version >= Terracotta.getUserNoticeVersion() || !isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("terracotta", isChecked);
                editor.apply();
                if (!((MainActivity) getActivity()).checkNotificationPermission() && isChecked) {
                    ((MainActivity) getActivity()).requestNotificationPermission();
                }
                extraLayout.setVisibility(switchMultiplayer.isChecked() ? View.VISIBLE : View.GONE);
            } else {
                FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
                builder.setMessage(getContext().getString(R.string.terracotta_status_uninitialized_desc));
                builder.setPositiveButton(() -> {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("terracotta_user_notice", Terracotta.getUserNoticeVersion());
                    editor.apply();
                    switchMultiplayer.setChecked(true);
                });
                builder.setNegativeButton(null);
                builder.create().show();
                switchMultiplayer.setChecked(false);
            }
        }
    }
}
