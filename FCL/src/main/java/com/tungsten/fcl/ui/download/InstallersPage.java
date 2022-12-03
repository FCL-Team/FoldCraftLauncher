package com.tungsten.fcl.ui.download;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.tungsten.fcl.R;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fclcore.download.RemoteVersion;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InstallersPage extends FCLTempPage implements View.OnClickListener {

    private InstallerItem.InstallerItemGroup group;
    private final Map<String, RemoteVersion> map = new HashMap<>();

    private LinearLayoutCompat nameBar;
    private ScrollView scrollView;

    private FCLEditText editText;
    private FCLImageButton install;

    public InstallersPage(Context context, int id, FCLUILayout parent, int resId, final String gameVersion) {
        super(context, id, parent, resId);
        onCreate(gameVersion);
    }

    public void onCreate(String gameVersion) {
        group = new InstallerItem.InstallerItemGroup(getContext());
        nameBar = findViewById(R.id.name_bar);
        scrollView = findViewById(R.id.scroll);

        ColorStateList colorStateList = new ColorStateList(new int[][]{ { } }, new int[]{ ThemeEngine.getInstance().getTheme().getLtColor() });
        ThemeEngine.getInstance().registerEvent(nameBar, () -> nameBar.setBackgroundTintList(colorStateList));

        editText = findViewById(R.id.edit);
        install = findViewById(R.id.install);
        editText.setText(gameVersion);
        install.setOnClickListener(this);

        scrollView.addView(group.getView());
        ViewGroup.LayoutParams layoutParams = scrollView.getChildAt(0).getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        scrollView.getChildAt(0).setLayoutParams(layoutParams);

        for (InstallerItem library : group.getLibraries()) {
            String libraryId = library.getLibraryId();
            if (libraryId.equals("game")) continue;
            library.action.set(() -> {
                if ("fabric-api".equals(libraryId)) {
                    FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                    builder.setMessage(getContext().getString(R.string.install_installer_fabric_api_warning));
                    builder.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_negative), null);
                    builder.create().show();
                }

                if (library.incompatibleLibraryName.get() == null) {
                    InstallerVersionPage page = new InstallerVersionPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_install_version, gameVersion, libraryId, remoteVersion -> {
                        map.put(libraryId, remoteVersion);
                        DownloadPageManager.getInstance().dismissCurrentTempPage();
                    });
                    DownloadPageManager.getInstance().showTempPage(page);
                }
            });
            library.removeAction.set(() -> {
                map.remove(libraryId);
                reload();
            });
        }
    }

    @Override
    public Task<?> refresh(Object... param) {
        return Task.runAsync(() -> {

        });
    }

    @Override
    public void onRestart() {
        reload();
    }

    @Override
    public void onClick(View view) {
        if (view == install) {

        }
    }

    private String getVersion(String id) {
        return Objects.requireNonNull(map.get(id)).getSelfVersion();
    }

    protected void reload() {
        for (InstallerItem library : group.getLibraries()) {
            String libraryId = library.getLibraryId();
            if (map.containsKey(libraryId)) {
                library.libraryVersion.set(getVersion(libraryId));
                library.removable.set(true);
            } else {
                library.libraryVersion.set(null);
                library.removable.set(false);
            }
        }
    }
}
