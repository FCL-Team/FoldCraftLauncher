package com.tungsten.fcl.ui.manage;

import static com.tungsten.fcl.ui.download.InstallersPage.alertFailureMessage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.DownloadProviders;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.ui.InstallerItem;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.ui.download.InstallerVersionPage;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.download.RemoteVersion;
import com.tungsten.fclcore.event.Event;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.task.TaskListener;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class InstallerListPage extends FCLCommonPage implements ManageUI.VersionLoadable, View.OnClickListener {

    private Profile profile;
    private String versionId;
    private Version version;
    private String gameVersion;

    private ScrollView scrollView;
    private LinearLayoutCompat parent;

    private FCLButton installOfflineButton;

    public InstallerListPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
        create();
    }

    private void create() {
        scrollView = findViewById(R.id.scroll);
        installOfflineButton = findViewById(R.id.install_offline);
        installOfflineButton.setOnClickListener(this);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void loadVersion(Profile profile, String versionId) {
        this.profile = profile;
        this.versionId = versionId;
        this.version = profile.getRepository().getVersion(versionId);
        this.gameVersion = null;

        CompletableFuture.supplyAsync(() -> {
            gameVersion = profile.getRepository().getGameVersion(version).orElse(null);

            return LibraryAnalyzer.analyze(profile.getRepository().getResolvedPreservingPatchesVersion(versionId));
        }).thenAcceptAsync(analyzer -> {
            Function<String, Runnable> removeAction = libraryId -> () -> profile.getDependency().removeLibraryAsync(version, libraryId)
                    .thenComposeAsync(profile.getRepository()::saveAsync)
                    .withComposeAsync(profile.getRepository().refreshVersionsAsync())
                    .withRunAsync(Schedulers.androidUIThread(), () -> {
                        loadVersion(this.profile, this.versionId);
                        profile.getRepository().onVersionIconChanged.fireEvent(new Event(this));
                    })
                    .start();

            clear();

            InstallerItem.InstallerItemGroup group = new InstallerItem.InstallerItemGroup(getContext(), gameVersion);

            // Conventional libraries: game, fabric, quilt, forge, neoforge, liteloader, optifine
            for (InstallerItem installerItem : group.getLibraries()) {
                String libraryId = installerItem.getLibraryId();

                // Skip fabric-api and quilt-api
                if (libraryId.contains("fabric-api") || libraryId.contains("quilt-api")) {
                    continue;
                }

                String libraryVersion = analyzer.getVersion(libraryId).orElse(null);
                installerItem.libraryVersion.set(libraryVersion);
                installerItem.upgradable.set(libraryVersion != null);
                installerItem.installable.set(true);
                installerItem.action.set(() -> {
                    InstallerVersionPage page = new InstallerVersionPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_install_version, gameVersion, libraryId, remoteVersion -> {
                        if (libraryVersion == null) {
                            finish(profile, remoteVersion);
                        } else {
                            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                            builder.setCancelable(false);
                            builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
                            builder.setTitle(getContext().getString(R.string.install_change_version));
                            builder.setMessage(AndroidUtils.getLocalizedText(getContext(), "install_change_version_confirm", AndroidUtils.getLocalizedText(getContext(), "install_installer_" + libraryId), libraryVersion, remoteVersion.getSelfVersion()));
                            builder.setPositiveButton(() -> finish(profile, remoteVersion));
                            builder.setNegativeButton(null);
                            builder.create().show();
                        }
                    });
                    ManagePageManager.getInstance().showTempPage(page);
                });
                boolean removable = !"game".equals(libraryId) && libraryVersion != null;
                installerItem.removable.set(removable);
                if (removable) {
                    Runnable action = removeAction.apply(libraryId);
                    installerItem.removeAction.set(action);
                }
                addView(installerItem);
            }
        }, Schedulers.androidUIThread());
    }

    public void installOffline() {
        ArrayList<String> suffix = new ArrayList<>();
        suffix.add(".jar");
        FileBrowser.Builder builder = new FileBrowser.Builder(getContext());
        builder.setTitle(getContext().getString(R.string.install_installer_install_offline_extension));
        builder.setLibMode(LibMode.FILE_CHOOSER);
        builder.setSelectionMode(SelectionMode.SINGLE_SELECTION);
        builder.setSuffix(suffix);
        builder.create().browse(getActivity(), RequestCodes.SELECT_AUTO_INSTALLER_CODE, (requestCode, resultCode, data) -> {
            if (requestCode == RequestCodes.SELECT_AUTO_INSTALLER_CODE && resultCode == Activity.RESULT_OK && data != null) {
                String path = FileBrowser.getSelectedFiles(data).get(0);
                Uri uri = Uri.parse(path);
                if (AndroidUtils.isDocUri(uri)) {
                    path = AndroidUtils.copyFileToDir(getActivity(), uri, new File(FCLPath.CACHE_DIR));
                }
                if (new File(path).exists()) {
                    doInstallOffline(new File(path));
                }
            }
        });
    }

    private void doInstallOffline(File file) {
        Task<?> task = profile.getDependency().installLibraryAsync(version, file.toPath())
                .thenComposeAsync(profile.getRepository()::saveAsync)
                .thenComposeAsync(profile.getRepository().refreshVersionsAsync());
        task.setName(getContext().getString(R.string.install_installer_install_offline));
        TaskExecutor executor = task.executor(new TaskListener() {
            @Override
            public void onStop(boolean success, TaskExecutor executor) {
                Schedulers.androidUIThread().execute(() -> {
                    if (success) {
                        loadVersion(profile, versionId);
                        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                        builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
                        builder.setCancelable(false);
                        builder.setMessage(getContext().getString(R.string.install_success));
                        builder.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), () -> profile.getRepository().onVersionIconChanged.fireEvent(new Event(this)));
                        builder.create().show();
                    } else {
                        if (executor.getException() == null)
                            return;
                        alertFailureMessage(getContext(), executor.getException(), () -> {});
                    }
                    loadVersion(InstallerListPage.this.profile, InstallerListPage.this.versionId);
                });
            }
        });
        TaskDialog dialog = new TaskDialog(getContext(), TaskCancellationAction.NORMAL);
        dialog.setTitle(getContext().getString(R.string.install_installer_install_offline));
        dialog.setExecutor(executor);
        dialog.show();
        executor.start();
    }

    private void clear() {
        if (parent == null) {
            parent = new LinearLayoutCompat(getContext());
            parent.setOrientation(LinearLayoutCompat.VERTICAL);
            scrollView.addView(parent);
            ViewGroup.LayoutParams layoutParams = scrollView.getChildAt(0).getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            scrollView.getChildAt(0).setLayoutParams(layoutParams);
        }
        parent.removeAllViews();
    }

    private void addView(InstallerItem installerItem) {
        if (parent == null) {
            parent = new LinearLayoutCompat(getContext());
            parent.setOrientation(LinearLayoutCompat.VERTICAL);
            scrollView.addView(parent);
            ViewGroup.LayoutParams layoutParams = scrollView.getChildAt(0).getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            scrollView.getChildAt(0).setLayoutParams(layoutParams);
        }
        View view = installerItem.createView();
        if (parent.getChildCount() > 0) {
            view.setPadding(0, ConvertUtils.dip2px(getContext(), 10), 0, 0);
        }
        parent.addView(view);
    }

    private void finish(Profile profile, RemoteVersion remoteVersion) {
        // We remove library but not save it,
        // so if installation failed will not break down current version.
        Task<Version> ret = Task.supplyAsync(() -> version);
        List<String> stages = new ArrayList<>();
        ret = ret.thenComposeAsync(version -> profile.getDependency(DownloadProviders.getDownloadProvider()).installLibraryAsync(version, remoteVersion));
        stages.add(String.format("fcl.install.%s:%s", remoteVersion.getLibraryId(), remoteVersion.getSelfVersion()));

        Task<?> task = ret.thenComposeAsync(profile.getRepository()::saveAsync).thenComposeAsync(profile.getRepository().refreshVersionsAsync()).withStagesHint(stages);

        TaskDialog pane = new TaskDialog(getContext(), new TaskCancellationAction(AppCompatDialog::dismiss));

        pane.setTitle(getContext().getString(R.string.install_change_version));

        Schedulers.androidUIThread().execute(() -> {
            TaskExecutor executor = task.executor(new TaskListener() {
                @Override
                public void onStop(boolean success, TaskExecutor executor) {
                    Schedulers.androidUIThread().execute(() -> {
                        if (success) {
                            FCLAlertDialog.Builder builder1 = new FCLAlertDialog.Builder(getContext());
                            builder1.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
                            builder1.setCancelable(false);
                            builder1.setMessage(getContext().getString(R.string.install_success));
                            builder1.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), () -> {
                                ManagePageManager.getInstance().dismissCurrentTempPage();
                                profile.getRepository().onVersionIconChanged.fireEvent(new Event(this));
                            });
                            builder1.create().show();
                        } else {
                            if (executor.getException() == null)
                                return;
                            alertFailureMessage(getContext(), executor.getException(), () -> {});
                        }
                        loadVersion(InstallerListPage.this.profile, InstallerListPage.this.versionId);
                    });
                }
            });
            pane.setExecutor(executor);
            pane.show();
            executor.start();
        });
    }

    @Override
    public void onClick(View view) {
        if (view == installOfflineButton) {
            installOffline();
        }
    }
}
