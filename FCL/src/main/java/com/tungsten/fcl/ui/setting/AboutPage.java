package com.tungsten.fcl.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.tungsten.fcl.R;
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
        Uri uri = null;

        if (v == launcher) {
            uri = Uri.parse("https://fcl-team.github.io/");
        }
        if (v == developer) {
            uri = Uri.parse("https://github.com/FCL-Team");
        }
        if (v == sponsor) {
            uri = Uri.parse("https://afdian.net/@tungs");
        }
        if (v == source) {
            uri = Uri.parse("https://github.com/FCL-Team/FoldCraftLauncher");
        }

        if (uri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getContext().startActivity(intent);
        }
    }
}
