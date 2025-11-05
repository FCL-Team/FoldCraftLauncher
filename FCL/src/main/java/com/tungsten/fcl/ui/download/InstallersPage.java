package com.tungsten.fcl.ui.download;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.FCLGameRepository;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.ui.InstallerItem;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclcore.download.ArtifactMalformedException;
import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.GameBuilder;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.download.RemoteVersion;
import com.tungsten.fclcore.download.UnsupportedInstallationException;
import com.tungsten.fclcore.download.VersionMismatchException;
import com.tungsten.fclcore.download.game.GameAssetIndexDownloadTask;
import com.tungsten.fclcore.download.game.LibraryDownloadException;
import com.tungsten.fclcore.task.DownloadException;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.task.TaskListener;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.ResponseCodeException;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.File;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.zip.ZipException;

public class InstallersPage extends FCLTempPage implements View.OnClickListener {

    private final String gameVersion;
    private InstallerItem.InstallerItemGroup group;
    private final Map<String, RemoteVersion> map = new HashMap<>();

    private LinearLayoutCompat nameBar;

    private FCLEditText editText;
    private FCLImageButton install;
    private boolean nameManuallyModified = false;

    public InstallersPage(Context context, int id, FCLUILayout parent, int resId, final String gameVersion) {
        super(context, id, parent, resId);
        this.gameVersion = gameVersion;
        onCreate(gameVersion);
    }

    public void onCreate(String gameVersion) {
        group = new InstallerItem.InstallerItemGroup(getContext(), gameVersion);
        nameBar = findViewById(R.id.name_bar);

        ColorStateList colorStateList = new ColorStateList(new int[][]{{}}, new int[]{ThemeEngine.getInstance().getTheme().getLtColor()});
        ThemeEngine.getInstance().registerEvent(nameBar, () -> nameBar.setBackgroundTintList(colorStateList));

        editText = findViewById(R.id.edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String autoGenName = generateVersionName();
                if (!s.toString().equals(autoGenName)) {
                    nameManuallyModified = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        install = findViewById(R.id.install);
        editText.setText(gameVersion);
        install.setOnClickListener(this);

        ScrollView scrollView = findViewById(R.id.scroll);
        scrollView.addView(group.getView());
        ViewGroup.LayoutParams layoutParams = scrollView.getChildAt(0).getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        scrollView.getChildAt(0).setLayoutParams(layoutParams);

        for (InstallerItem library : group.getLibraries()) {
            String libraryId = library.getLibraryId();
            if (libraryId.equals("game")) continue;
            library.action.set(() -> {
                if (LibraryAnalyzer.LibraryType.FABRIC_API.getPatchId().equals(libraryId)) {
                    FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                    builder.setMessage(getContext().getString(R.string.install_installer_fabric_api_warning));
                    builder.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                    builder.create().show();
                }

                if (library.incompatibleLibraryName.get() == null) {
                    InstallerVersionPage page = new InstallerVersionPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_install_version, gameVersion, libraryId, remoteVersion -> {
                        map.put(libraryId, remoteVersion);
                        refreshVersionName();
                        DownloadPageManager.getInstance().dismissCurrentTempPage();
                    });
                    DownloadPageManager.getInstance().showTempPage(page);
                }
            });
            library.removeAction.set(() -> {
                map.remove(libraryId);
                refreshVersionName();
                reload();
            });
        }
    }

    private String generateVersionName() {
        StringBuilder nameBuilder = new StringBuilder(gameVersion);
        Arrays.stream(LibraryAnalyzer.LibraryType.values())
                .filter(libraryType -> map.containsKey(libraryType.getPatchId()))
                .map(this::getLoaderName)
                .filter(name -> !Objects.isNull(name))
                .forEach(name -> nameBuilder.append("-").append(name));
        return nameBuilder.toString();
    }

    private void refreshVersionName() {
        if (nameManuallyModified) {
            return;
        }
        editText.setText(generateVersionName());
    }

    private String getLoaderName(LibraryAnalyzer.LibraryType libraryType) {
        switch (libraryType) {
            case FORGE:
                return getContext().getString(R.string.install_installer_forge);
            case NEO_FORGE:
                return getContext().getString(R.string.install_installer_neoforge);
            case CLEANROOM:
                return getContext().getString(R.string.install_installer_cleanroom);
            case FABRIC:
                return getContext().getString(R.string.install_installer_fabric);
            case LITELOADER:
                return getContext().getString(R.string.install_installer_liteloader);
            case QUILT:
                return getContext().getString(R.string.install_installer_quilt);
            case OPTIFINE:
                return getContext().getString(R.string.install_installer_optifine);
            default:
                return null;
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
            if (StringUtils.isBlank(Objects.requireNonNull(editText.getText()).toString())) {
                Toast.makeText(getContext(), getContext().getString(R.string.input_not_empty), Toast.LENGTH_SHORT).show();
            } else if (Profiles.getSelectedProfile().getRepository().versionIdConflicts(editText.getText().toString())) {
                Toast.makeText(getContext(), getContext().getString(R.string.install_new_game_already_exists), Toast.LENGTH_SHORT).show();
            } else if (!FCLGameRepository.isValidVersionId(editText.getText().toString())) {
                Toast.makeText(getContext(), getContext().getString(R.string.install_new_game_malformed), Toast.LENGTH_SHORT).show();
            } else {
                GameBuilder builder = Profiles.getSelectedProfile().getDependency().gameBuilder();

                String name = editText.getText().toString();
                builder.name(name);
                builder.gameVersion(gameVersion);

                for (Map.Entry<String, RemoteVersion> entry : map.entrySet()) {
                    if (!LibraryAnalyzer.LibraryType.MINECRAFT.getPatchId().equals(entry.getKey())) {
                        builder.version(entry.getValue());
                    }
                }

                Task<Void> task = builder.buildAsync().whenComplete(any -> Profiles.getSelectedProfile().getRepository().refreshVersions())
                        .thenRunAsync(Schedulers.androidUIThread(), () -> {
                            Profile profile = Profiles.getSelectedProfile();
                            profile.setSelectedVersion(name);
                            if (!map.isEmpty()) {
                                if (map.containsKey(LibraryAnalyzer.LibraryType.OPTIFINE.getPatchId()) && map.size() == 1) {
                                    return;
                                }
                                new File(profile.getRepository().getRunDirectory(profile.getSelectedVersion()), "mods").mkdirs();
                            }
                        });

                TaskDialog pane = new TaskDialog(getContext(), new TaskCancellationAction(AppCompatDialog::dismiss));
                pane.setTitle(getContext().getString(R.string.install_new_game));
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
                                    builder1.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), () -> DownloadPageManager.getInstance().dismissCurrentTempPage());
                                    builder1.create().show();
                                } else {
                                    if (executor.getException() == null)
                                        return;
                                    alertFailureMessage(getContext(), executor.getException(), () -> {
                                    });
                                }

                            });
                        }
                    });
                    pane.setExecutor(executor);
                    pane.show();
                    executor.start();
                });
            }
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

    public static void alertFailureMessage(Context context, Exception exception, Runnable next) {
        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
        builder.setNegativeButton(context.getString(com.tungsten.fcllibrary.R.string.dialog_positive), next::run);
        String title;
        String msg;
        if (exception instanceof LibraryDownloadException) {
            String message = AndroidUtils.getLocalizedText(context, "launch_failed_download_library", ((LibraryDownloadException) exception).getLibrary().getName()) + "\n";
            if (exception.getCause() instanceof ResponseCodeException) {
                ResponseCodeException rce = (ResponseCodeException) exception.getCause();
                int responseCode = rce.getResponseCode();
                URL url = rce.getUrl();
                if (responseCode == 404)
                    message += AndroidUtils.getLocalizedText(context, "download_code_404", url);
                else
                    message += AndroidUtils.getLocalizedText(context, "download_failed", url, responseCode);
            } else {
                message += StringUtils.getStackTrace(exception.getCause());
            }
            title = context.getString(R.string.install_failed_downloading);
            msg = message;
        } else if (exception instanceof DownloadException) {
            URL url = ((DownloadException) exception).getUrl();
            if (exception.getCause() instanceof SocketTimeoutException) {
                title = context.getString(R.string.install_failed_downloading);
                msg = AndroidUtils.getLocalizedText(context, "install_failed_downloading_timeout", url);
            } else if (exception.getCause() instanceof ResponseCodeException) {
                ResponseCodeException responseCodeException = (ResponseCodeException) exception.getCause();
                if (AndroidUtils.hasStringId(context, "download_code_" + responseCodeException.getResponseCode())) {
                    title = context.getString(R.string.install_failed_downloading);
                    msg = AndroidUtils.getLocalizedText(context, "download_code_" + responseCodeException.getResponseCode(), url);
                } else {
                    title = context.getString(R.string.install_failed_downloading);
                    msg = AndroidUtils.getLocalizedText(context, "install_failed_downloading_detail", url);
                }
            } else {
                title = context.getString(R.string.install_failed_downloading);
                msg = AndroidUtils.getLocalizedText(context, "install_failed_downloading_detail", url) + "\n" + StringUtils.getStackTrace(exception.getCause());
            }
        } else if (exception instanceof UnsupportedInstallationException) {
            if (((UnsupportedInstallationException) exception).getReason() == UnsupportedInstallationException.FORGE_1_17_OPTIFINE_H1_PRE2) {
                title = context.getString(R.string.install_failed);
                msg = context.getString(R.string.install_failed_optifine_forge_1_17);
            } else {
                title = context.getString(R.string.install_failed);
                msg = context.getString(R.string.install_failed_optifine_conflict);
            }
        } else if (exception instanceof DefaultDependencyManager.UnsupportedLibraryInstallerException) {
            title = context.getString(R.string.install_failed);
            msg = context.getString(R.string.install_failed_install_online);
        } else if (exception instanceof ArtifactMalformedException || exception instanceof ZipException) {
            title = context.getString(R.string.install_failed);
            msg = context.getString(R.string.install_failed_malformed);
        } else if (exception instanceof GameAssetIndexDownloadTask.GameAssetIndexMalformedException) {
            title = context.getString(R.string.install_failed);
            msg = context.getString(R.string.assets_index_malformed);
        } else if (exception instanceof VersionMismatchException) {
            VersionMismatchException e = ((VersionMismatchException) exception);
            title = context.getString(R.string.install_failed);
            msg = AndroidUtils.getLocalizedText(context, "install_failed_version_mismatch", e.getExpect(), e.getActual());
        } else if (exception instanceof CancellationException) {
            // Ignore cancel
            title = "";
            msg = "";
        } else {
            title = context.getString(R.string.install_failed);
            msg = StringUtils.getStackTrace(exception);
        }
        builder.setTitle(title);
        builder.setMessage(msg);
        if (!(exception instanceof CancellationException)) {
            builder.create().show();
        }
    }
}
