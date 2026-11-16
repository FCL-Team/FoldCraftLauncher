package com.tungsten.fcl.ui.download.modpack;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Html;
import android.view.View;
import android.widget.ScrollView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.util.regex.Pattern;

public abstract class ModpackPage extends FCLPage implements View.OnClickListener {

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

    public ModpackPage(Context context, int id, int resId, Profile profile) {
        super(context, id, resId);
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
        ThemeEngine.getInstance().registerEvent(infoLayout, () -> infoLayout.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{ThemeEngine.getInstance().getTheme().getLtColor()})));
    }



    protected abstract void onInstall();

    protected abstract void onDescribe();

    private static final Pattern HTML_TAG = Pattern.compile("<\\/?[a-zA-Z][a-zA-Z0-9]*(\\s[^>]*)?\\/?>");

    /**
     * 将整合包描述转换为富文本：纯文本中的换行符转为换行标签，HTML 描述按原样解析。
     */
    protected CharSequence formatDescription(String description) {
        if (description == null) {
            return "";
        }
        String text = description.replace("\r\n", "\n").replace('\r', '\n');
        if (HTML_TAG.matcher(text).find()) {
            return Html.fromHtml(text, 0);
        }
        return Html.fromHtml(text.replace("\n", "<br>"), 0);
    }

    @Override
    public void onClick(View v) {
        if (v == install) {
            FCLAlertDialog dialog = new FCLAlertDialog(getContext());
            dialog.setTitle(R.string.modpack_download_warn_title);
            dialog.setMessage(getContext().getString(R.string.modpack_download_warn_msg));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setPositiveButton(getContext().getString(com.tungsten.fcl.R.string.dialog_positive), this::onInstall);
            dialog.setNegativeButton(getContext().getString(com.tungsten.fcl.R.string.dialog_negative), null);
            dialog.show();
        }
        if (v == describe) {
            onDescribe();
        }
    }
}
