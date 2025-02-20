package com.tungsten.fcl.ui.download;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ScrollView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public abstract class ModpackPage extends FCLTempPage implements View.OnClickListener {

    protected final Profile profile;

    protected FCLProgressBar progressBar;
    protected FCLLinearLayout layout;
    protected ScrollView infoLayout;

    protected FCLEditText editText;
    protected FCLTextView name;
    protected FCLTextView version;
    protected FCLTextView author;

    protected FCLButton install;
    protected FCLButton describe;

    public ModpackPage(Context context, int id, FCLUILayout parent, int resId, Profile profile) {
        super(context, id, parent, resId);
        this.profile = profile;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        progressBar = findViewById(R.id.progress);
        layout = findViewById(R.id.layout);
        infoLayout = findViewById(R.id.info_layout);
        editText = findViewById(R.id.name);
        name = findViewById(R.id.modpack_name);
        version = findViewById(R.id.version);
        author = findViewById(R.id.author);
        install = findViewById(R.id.install);
        describe = findViewById(R.id.describe);
        install.setOnClickListener(this);
        describe.setOnClickListener(this);
        ThemeEngine.getInstance().registerEvent(infoLayout, () -> infoLayout.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { ThemeEngine.getInstance().getTheme().getLtColor() })));
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onRestart() {

    }

    protected abstract void onInstall();

    protected abstract void onDescribe();

    @Override
    public void onClick(View v) {
        if (v == install) {
            FCLAlertDialog dialog = new FCLAlertDialog(getContext());
            dialog.setTitle(R.string.modpack_download_warn_title);
            dialog.setMessage(getContext().getString(R.string.modpack_download_warn_msg));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setPositiveButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), this::onInstall);
            dialog.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_negative), null);
            dialog.show();
        }
        if (v == describe) {
            onDescribe();
        }
    }
}
