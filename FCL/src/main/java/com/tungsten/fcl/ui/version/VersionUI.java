package com.tungsten.fcl.ui.version;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLMultiPageUI;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class VersionUI extends FCLMultiPageUI {

    public static final int PAGE_ID_VERSION_LIST = 15020;

    private FCLUILayout container;

    public VersionUI(Context context, int id) {
        super(context, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        container = findViewById(R.id.container);
        setupPages(container, null);
    }

    @Override
    public int getPageCount() {
        return 1;
    }

    @Override
    public FCLPage createPage(int position) {
        return new VersionListPage(getContext(), PAGE_ID_VERSION_LIST);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return Task.runAsync(() -> {

        });
    }
}
