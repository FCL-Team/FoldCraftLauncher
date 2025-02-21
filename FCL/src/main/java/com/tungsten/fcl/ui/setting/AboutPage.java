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

    private FCLLinearLayout launcherFork;
    private FCLLinearLayout launcher;
    private FCLLinearLayout developer;
    private FCLLinearLayout sponsorFork;
    private FCLLinearLayout sponsor;
    private FCLLinearLayout source;

    public AboutPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        launcherFork = findViewById(R.id.launcher_fork);
        launcher = findViewById(R.id.launcher);
        developer = findViewById(R.id.developer);
        sponsorFork = findViewById(R.id.sponsor_fork);
        sponsor = findViewById(R.id.sponsor);
        source = findViewById(R.id.source);
        launcherFork.setOnClickListener(this);
        launcher.setOnClickListener(this);
        developer.setOnClickListener(this);
        sponsorFork.setOnClickListener(this);
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

        if (v == launcherFork) {
            uri = Uri.parse("https://github.com/MrXiaoM/FoldCraftLauncher");
        }
        if (v == launcher) {
            uri = Uri.parse("https://fcl-team.github.io/");
        }
        if (v == developer) {
            uri = Uri.parse("https://github.com/FCL-Team");
        }
        if (v == sponsorFork) {
            uri = Uri.parse("https://afdian.net/@mrxiaom");
        }
        if (v == sponsor) {
            uri = Uri.parse("https://afdian.com/@tungs");
        }
        if (v == source) {
            uri = Uri.parse("https://github.com/MrXiaoM/FoldCraftLauncher");
        }

        if (uri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getContext().startActivity(intent);
        }
    }
}
