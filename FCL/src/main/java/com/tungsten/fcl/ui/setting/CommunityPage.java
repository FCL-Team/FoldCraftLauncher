package com.tungsten.fcl.ui.setting;

import android.content.Context;
import android.view.View;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class CommunityPage extends FCLCommonPage implements View.OnClickListener {

    private FCLLinearLayout discord;
    private FCLLinearLayout qq;

    public CommunityPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        discord = findViewById(R.id.discord);
        qq = findViewById(R.id.qq);
        discord.setOnClickListener(this);
        qq.setOnClickListener(this);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == discord) {

        }
        if (v == qq) {
            
        }
    }
}
