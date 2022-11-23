package com.tungsten.fcl.ui.version;

import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.MINECRAFT;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class VersionUI extends FCLCommonUI implements View.OnClickListener {

    private FCLButton refresh;
    private FCLButton newProfile;
    private FCLProgressBar progressBar;
    private ListView profileListView;
    private ListView versionListView;

    public VersionUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        refresh = findViewById(R.id.refresh);
        newProfile = findViewById(R.id.new_profile);
        progressBar = findViewById(R.id.progress);
        profileListView = findViewById(R.id.profile_list);
        versionListView = findViewById(R.id.version_list);

        refresh.setOnClickListener(this);
        newProfile.setOnClickListener(this);

        refresh();
    }

    @Override
    public void refresh() {
        refresh.setEnabled(false);
        versionListView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        refreshProfile();
        Profiles.getSelectedProfile().getRepository().refreshVersionsAsync().whenComplete(Schedulers.androidUIThread(), exception -> {
            ArrayList<VersionListItem> children = (ArrayList<VersionListItem>) Profiles.getSelectedProfile().getRepository().getDisplayVersions()
                    .map(version -> {
                        String game = Profiles.getSelectedProfile().getRepository().getGameVersion(version.getId()).orElse(getContext().getString(R.string.message_unknown));
                        StringBuilder libraries = new StringBuilder(game);
                        LibraryAnalyzer analyzer = LibraryAnalyzer.analyze(Profiles.getSelectedProfile().getRepository().getResolvedPreservingPatchesVersion(version.getId()));
                        for (LibraryAnalyzer.LibraryMark mark : analyzer) {
                            String libraryId = mark.getLibraryId();
                            String libraryVersion = mark.getLibraryVersion();
                            if (libraryId.equals(MINECRAFT.getPatchId())) continue;
                            int resId = getContext().getResources().getIdentifier("install_installer_" + libraryId.replace("-", "_"), "string", getContext().getPackageName());
                            if (resId != 0 && getContext().getString(resId) != null) {
                                libraries.append(", ").append(getContext().getString(resId));
                                if (libraryVersion != null)
                                    libraries.append(": ").append(libraryVersion.replaceAll("(?i)" + libraryId, ""));
                            }
                        }
                        return new VersionListItem(Profiles.getSelectedProfile(), version.getId(), libraries.toString(), Profiles.getSelectedProfile().getRepository().getVersionIconImage(version.getId()));
                    })
                    .collect(Collectors.toList());
            VersionListAdapter adapter = new VersionListAdapter(getContext(), children);
            versionListView.setAdapter(adapter);
            refresh.setEnabled(true);
            versionListView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            MainActivity.getInstance().refresh();
        }).start();
    }

    public void refreshProfile() {
        ProfileListAdapter adapter = new ProfileListAdapter(getContext(), Profiles.getProfiles());
        profileListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view == refresh) {
            refresh();
        }
        if (view == newProfile) {
            AddProfileDialog dialog = new AddProfileDialog(getContext());
            dialog.show();
        }
    }
}
