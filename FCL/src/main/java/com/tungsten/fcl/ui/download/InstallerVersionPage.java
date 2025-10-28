package com.tungsten.fcl.ui.download;

import static com.tungsten.fclcore.util.Logging.LOG;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.DownloadProviders;
import com.tungsten.fclcore.download.RemoteVersion;
import com.tungsten.fclcore.download.VersionList;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class InstallerVersionPage extends FCLTempPage implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private final String gameVersion;
    private final String libraryId;
    private final Callback callback;
    private RemoteVersionListAdapter.OnRemoteVersionSelectListener listener;

    private FCLCheckBox checkRelease;
    private FCLCheckBox checkSnapShot;
    private FCLCheckBox checkOld;
    private FCLImageButton refresh;
    private FCLImageButton failedRefresh;
    private FCLProgressBar progressBar;
    private ListView listView;

    public InstallerVersionPage(Context context, int id, FCLUILayout parent, int resId, String gameVersion, String libraryId, Callback callback) {
        super(context, id, parent, resId);
        this.gameVersion = gameVersion;
        this.libraryId = libraryId;
        this.callback = callback;
        create();
    }

    public void create() {
        FCLLinearLayout checkBar = findViewById(R.id.bar);
        checkBar.setVisibility(DownloadProviders.getDownloadProvider().getVersionListById(libraryId).hasType() ? View.VISIBLE : View.GONE);

        checkRelease = findViewById(R.id.release);
        checkSnapShot = findViewById(R.id.snapshot);
        checkOld = findViewById(R.id.old);
        refresh = findViewById(R.id.refresh);
        failedRefresh = findViewById(R.id.failed_refresh);
        progressBar = findViewById(R.id.progress);
        listView = findViewById(R.id.list);

        checkRelease.setChecked(true);

        checkRelease.setOnCheckedChangeListener(this);
        checkSnapShot.setOnCheckedChangeListener(this);
        checkOld.setOnCheckedChangeListener(this);
        refresh.setOnClickListener(this);
        failedRefresh.setOnClickListener(this);

        listener = callback::onSelect;

        findViewById(R.id.april_fools).setVisibility(View.GONE);
        refreshList();
    }

    private List<RemoteVersion> loadVersions() {
        return DownloadProviders.getDownloadProvider().getVersionListById(libraryId).getVersions(gameVersion).stream()
                .filter(it -> {
                    switch (it.getVersionType()) {
                        case RELEASE:
                            return checkRelease.isChecked();
                        case SNAPSHOT:
                            return checkSnapShot.isChecked();
                        case OLD:
                            return checkOld.isChecked();
                        default:
                            return true;
                    }
                })
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
        VersionList<?> currentVersionList = DownloadProviders.getDownloadProvider().getVersionListById(libraryId);
        currentVersionList.refreshAsync(gameVersion).whenComplete((result, exception) -> {
            if (isShowing()) {
                if (exception == null) {
                    List<RemoteVersion> items = loadVersions();

                    Schedulers.androidUIThread().execute(() -> {
                        if (currentVersionList.getVersions(gameVersion).isEmpty()) {
                            Toast.makeText(getContext(), getContext().getString(R.string.download_failed_empty), Toast.LENGTH_SHORT).show();
                            listView.setVisibility(View.GONE);
                            failedRefresh.setVisibility(View.VISIBLE);
                        } else {
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
                        }
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
    public void onRestart() {

    }

    @Override
    public void onClick(View view) {
        if (view == refresh || view == failedRefresh) {
            refreshList();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == checkRelease || compoundButton == checkSnapShot || compoundButton == checkOld) {
            refreshDisplayVersions();
        }
    }

    public interface Callback {
        void onSelect(RemoteVersion remoteVersion);
    }
}
