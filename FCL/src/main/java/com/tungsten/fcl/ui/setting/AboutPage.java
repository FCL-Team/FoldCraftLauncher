package com.tungsten.fcl.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.tungsten.fcl.R;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class AboutPage extends FCLCommonPage implements View.OnClickListener {

    private FCLLinearLayout launcher;
    private FCLLinearLayout developer;
    private FCLLinearLayout sponsor;
    private FCLLinearLayout source;

    public AboutPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        launcher = findViewById(R.id.launcher);
        developer = findViewById(R.id.developer);
        sponsor = findViewById(R.id.sponsor);
        source = findViewById(R.id.source);
        launcher.setOnClickListener(this);
        developer.setOnClickListener(this);
        sponsor.setOnClickListener(this);
        source.setOnClickListener(this);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onClick(View v) {
        String url = null;

        if (v == launcher) {
            url = "https://fcl-team.github.io/";
        }
        if (v == developer) {
            url = "https://github.com/FCL-Team";
        }
        if (v == sponsor) {
            url = "https://afdian.com/@tungs";
        }
        if (v == source) {
            url = "https://github.com/FCL-Team/FoldCraftLauncher";
        }

        if (url != null) {
            AndroidUtils.openLink(getContext(), url);
        }
    }
}
