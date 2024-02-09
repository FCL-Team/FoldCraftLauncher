package com.tungsten.fcl.ui.version;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class VersionListPage extends FCLCommonPage implements View.OnClickListener {

    private FCLButton refresh;
    private FCLButton newProfile;
    private ListView profileListView;

    private VersionList versionList;
    private FCLTextView dirPath;

    public VersionListPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        refresh = findViewById(R.id.refresh);
        newProfile = findViewById(R.id.new_profile);
        profileListView = findViewById(R.id.profile_list);
        dirPath = findViewById(R.id.dir_path);
        FCLProgressBar progressBar = findViewById(R.id.progress);
        ListView versionListView = findViewById(R.id.version_list);

        refresh.setOnClickListener(this);
        newProfile.setOnClickListener(this);

        refreshProfile();
        versionList = new VersionList(getContext(), versionListView, refresh, progressBar, dirPath);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return Task.runAsync(() -> {

        });
    }

    public void refreshProfile() {
        ProfileListAdapter adapter = new ProfileListAdapter(getContext(), Profiles.getProfiles());
        profileListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view == refresh) {
            versionList.refreshList();
        }
        if (view == newProfile) {
            AddProfileDialog dialog = new AddProfileDialog(getContext());
            dialog.show();
        }
    }
}
