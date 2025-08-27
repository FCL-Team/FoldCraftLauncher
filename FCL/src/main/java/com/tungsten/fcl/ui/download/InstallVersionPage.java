package com.tungsten.fcl.ui.download;

import static com.tungsten.fclcore.util.Logging.LOG;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.DownloadProviders;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fclcore.download.RemoteVersion;
import com.tungsten.fclcore.download.VersionList;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.versioning.GameVersionNumber;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class InstallVersionPage extends FCLCommonPage implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private FCLCheckBox checkRelease;
    private FCLCheckBox checkSnapShot;
    private FCLCheckBox checkOld;
    private FCLCheckBox checkAprilFools;
    private FCLImageButton refresh;
    private FCLImageButton failedRefresh;
    private FCLProgressBar progressBar;
    private ListView listView;
    private FCLEditText search;

    private RemoteVersionListAdapter.OnRemoteVersionSelectListener listener;

    public InstallVersionPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        checkRelease = findViewById(R.id.release);
        checkSnapShot = findViewById(R.id.snapshot);
        checkOld = findViewById(R.id.old);
        checkAprilFools = findViewById(R.id.april_fools);
        refresh = findViewById(R.id.refresh);
        failedRefresh = findViewById(R.id.failed_refresh);
        progressBar = findViewById(R.id.progress);
        listView = findViewById(R.id.list);
        search = findViewById(R.id.search);

        checkRelease.setChecked(true);

        checkRelease.setOnCheckedChangeListener(this);
        checkSnapShot.setOnCheckedChangeListener(this);
        checkOld.setOnCheckedChangeListener(this);
        checkAprilFools.setOnCheckedChangeListener(this);
        refresh.setOnClickListener(this);
        failedRefresh.setOnClickListener(this);

        listener = remoteVersion -> {
            InstallersPage page = new InstallersPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_installer, remoteVersion.getGameVersion());
            DownloadPageManager.getInstance().showTempPage(page);
        };

        search.stringProperty().addListener(observable -> refreshDisplayVersions());

        refreshList();
    }

    private List<RemoteVersion> loadVersions() {
        return DownloadProviders.getDownloadProvider().getVersionListById("game").getVersions("").stream()
                .filter(it -> {
                    switch (it.getVersionType()) {
                        case RELEASE:
                            return checkRelease.isChecked();
                        case SNAPSHOT:
                            if (checkSnapShot.isChecked()) return true;
                            else if (checkAprilFools.isChecked())
                                return GameVersionNumber.asGameVersion(it.getGameVersion()).isAprilFools();
                            return false;
                        case OLD:
                            if (checkOld.isChecked()) return true;
                            else if (checkAprilFools.isChecked())
                                return GameVersionNumber.asGameVersion(it.getGameVersion()).isAprilFools();
                            return false;
                        default:
                            return true;
                    }
                })
                .filter(it -> it.getGameVersion().contains(search.getStringValue()))
                .sorted().collect(Collectors.toList());
    }

    public void refreshDisplayVersions() {
        List<RemoteVersion> items = loadVersions();
        RemoteVersionListAdapter adapter = new RemoteVersionListAdapter(getContext(), (ArrayList<RemoteVersion>) items, listener);
        listView.setAdapter(adapter);
    }

    public void refreshList() {
        listView.setVisibility(View.GONE);
        failedRefresh.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        refresh.setEnabled(false);
        search.setText("");
        VersionList<?> currentVersionList = DownloadProviders.getDownloadProvider().getVersionListById("game");
        currentVersionList.refreshAsync("").whenComplete((result, exception) -> {
            if (exception == null) {
                List<RemoteVersion> items = loadVersions();

                Schedulers.androidUIThread().execute(() -> {
                    if (items.isEmpty()) {
                        checkRelease.setChecked(true);
                        checkSnapShot.setChecked(true);
                        checkOld.setChecked(true);
                    } else {
                        RemoteVersionListAdapter adapter = new RemoteVersionListAdapter(getContext(), (ArrayList<RemoteVersion>) items, listener);
                        listView.setAdapter(adapter);
                    }
                    listView.setVisibility(View.VISIBLE);
                    failedRefresh.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    refresh.setEnabled(true);
                });
            } else {
                LOG.log(Level.WARNING, "Failed to fetch versions list", exception);
                Schedulers.androidUIThread().execute(() -> {
                    listView.setVisibility(View.GONE);
                    failedRefresh.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    refresh.setEnabled(true);
                });
            }

            System.gc();
        });
    }

    @Override
    public Task<?> refresh(Object... param) {
        return Task.runAsync(() -> {

        });
    }

    @Override
    public void onClick(View view) {
        if (view == refresh || view == failedRefresh) {
            refreshList();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == checkRelease || compoundButton == checkSnapShot || compoundButton == checkOld || compoundButton == checkAprilFools) {
            refreshDisplayVersions();
        }
    }
}
