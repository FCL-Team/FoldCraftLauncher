package com.tungsten.fcl.ui.version;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;

import com.mio.util.ParseUtil;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.game.LauncherHelper;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcllibrary.ui.ProgressDialog;
import com.tungsten.fcl.ui.compose.FCLDialogs;
import com.tungsten.fcl.ui.compose.MiuixTaskDialog;
import com.tungsten.fcl.ui.compose.dialog.MiuixCreateAccountDialog;
import com.tungsten.fcl.ui.compose.dialog.MiuixDuplicateVersionDialog;
import com.tungsten.fcl.ui.compose.dialog.MiuixRenameVersionDialog;
import com.tungsten.fcl.ui.download.DownloadPageManager;
import com.tungsten.fcl.ui.download.modpack.compose.ComposeLocalModpackPage;
import com.tungsten.fcl.ui.download.modpack.compose.ComposeModpackSelectionPage;
import com.tungsten.fcl.ui.manage.ManagePageManager;
import com.tungsten.fcl.ui.manage.ModpackTypeSelectionPage;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.NavigationBus;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.auth.AccountFactory;
import com.tungsten.fclcore.download.game.GameAssetDownloadTask;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.util.FutureCallback;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.platform.OperatingSystem;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;

public class Versions {

    public static void importModpack(Context context, FCLUILayout parent) {
        Profile profile = Profiles.getSelectedProfile();
        if (profile.getRepository().isLoaded()) {
            ComposeModpackSelectionPage page = new ComposeModpackSelectionPage(context, PageManager.PAGE_ID_TEMP, parent, profile, null);
            DownloadPageManager.getInstance().showTempPage(page);
        }
    }

    /** 5.1 遗留 L3：下载失败提示（AlertLevel.ALERT 等价于 showAlert 的标题/文案，按钮仅 dismiss）。 */
    private static void showModpackDownloadFailed(Context context, String url, Exception e) {
        String title = context.getString(R.string.download_failed);
        String message = AndroidUtils.getLocalizedText(context, "install_failed_downloading_detail", url) + "\n" + StringUtils.getStackTrace(e);
        FCLDialogs.showAlert(context, title, message, null, null, null, false);
    }

    public static void downloadModpackImpl(Context context, FCLUILayout parent, Profile profile, RemoteMod.Version file) {
        Path modpack;
        URL downloadURL;
        try {
            modpack = Files.createTempFile("modpack", ".zip");
            downloadURL = new URL(file.getFile().getUrl());
        } catch (IOException e) {
            showModpackDownloadFailed(context, file.getFile().getUrl(), e);
            return;
        }

        TaskExecutor executor = new FileDownloadTask(downloadURL, modpack.toFile())
                .whenComplete(Schedulers.androidUIThread(), e -> {
                    if (e == null) {
                        ComposeLocalModpackPage page = new ComposeLocalModpackPage(context, PageManager.PAGE_ID_TEMP, parent, profile, null, modpack.toFile());
                        DownloadPageManager.getInstance().showTempPage(page);
                    } else if (e instanceof CancellationException) {
                        Toast.makeText(context, context.getString(R.string.message_cancelled), Toast.LENGTH_SHORT).show();
                    } else {
                        showModpackDownloadFailed(context, file.getFile().getUrl(), e);
                    }
                }).executor();
        MiuixTaskDialog taskDialog = new MiuixTaskDialog(context);
        taskDialog.setTitle(context.getString(R.string.message_downloading));
        taskDialog.setExecutor(executor);
        taskDialog.show();
        executor.start();
    }

    public static void deleteVersion(Context context, Profile profile, String version) {
        boolean isIndependent = profile.getVersionSetting(version).isIsolateGameDir();
        String message = isIndependent ? String.format(context.getString(R.string.version_manage_remove_confirm_independent), version) : String.format(context.getString(R.string.version_manage_remove_confirm), version);

        Runnable deleteAction = () -> {
            AppCompatDialog progress = FCLDialogs.showProgress(context);
            Task.runAsync(() -> {
                profile.getRepository().removeVersionFromDisk(version);
            }).whenComplete(Schedulers.androidUIThread(), (e) -> {
                progress.dismiss();
            }).start();
        };
        FCLDialogs.showAlert(context, null, message, null,
                context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
                result -> { if (result) deleteAction.run(); });
    }

    public static CompletableFuture<String> renameVersion(Context context, Profile profile, String version) {
        FutureCallback<String> callback = (newName, resolve, reject) -> {
            if (!OperatingSystem.isNameValid(newName) || !ParseUtil.isValidCharacters(newName)) {
                reject.accept(context.getString(R.string.install_new_game_malformed));
                return;
            }
            ProgressDialog progress = new ProgressDialog(context);
            Task.supplyAsync(() -> profile.getRepository().renameVersion(version, newName))
                    .thenComposeAsync(Schedulers.androidUIThread(), result -> {
                        progress.dismiss();
                        if (result) {
                            resolve.run();
                            profile.getRepository().refreshVersionsAsync()
                                    .thenRunAsync(Schedulers.androidUIThread(), () -> {
                                        if (profile.getRepository().hasVersion(newName)) {
                                            profile.setSelectedVersion(newName);
                                        }
                                    }).start();
                        } else {
                            reject.accept(context.getString(R.string.version_manage_rename_fail));
                        }
                        return null;
                    }).start();
        };
        MiuixRenameVersionDialog dialog = new MiuixRenameVersionDialog(context, version, callback);
        dialog.show();
        return dialog.getFuture();
    }

    public static void exportVersion(Context context, FCLUILayout parent, Profile profile, String version) {
        ModpackTypeSelectionPage page = new ModpackTypeSelectionPage(context, PageManager.PAGE_ID_TEMP, parent, R.layout.page_modpack_type, profile, version);
        ManagePageManager.getInstance().showTempPage(page);
    }

    public static void duplicateVersion(Context context, Profile profile, String version) {
        FutureCallback<ArrayList<Object>> callback = (res, resolve, reject) -> {
            String newVersionName = (String) res.get(0);
            if (!OperatingSystem.isNameValid(newVersionName) || !ParseUtil.isValidCharacters(newVersionName)) {
                reject.accept(context.getString(R.string.install_new_game_malformed));
                return;
            }
            boolean copySaves = (boolean) res.get(1);
            ProgressDialog progress = new ProgressDialog(context);
            Task.runAsync(() -> profile.getRepository().duplicateVersion(version, newVersionName, copySaves))
                    .thenComposeAsync(profile.getRepository().refreshVersionsAsync())
                    .whenComplete(Schedulers.androidUIThread(), (result, exception) -> {
                        progress.dismiss();
                        if (exception == null) {
                            resolve.run();
                        } else {
                            reject.accept(StringUtils.getStackTrace(exception));
                            profile.getRepository().removeVersionFromDisk(newVersionName);
                        }
                    }).start();
        };
        new MiuixDuplicateVersionDialog(context, profile, version, callback).show();
    }

    public static void updateVersion(Context context, FCLUILayout parent, Profile profile, String version) {
        ComposeModpackSelectionPage page = new ComposeModpackSelectionPage(context, PageManager.PAGE_ID_TEMP, parent, profile, version);
        ManagePageManager.getInstance().showTempPage(page);
    }

    public static void updateGameAssets(Context context, Profile profile, String version) {
        TaskExecutor executor = new GameAssetDownloadTask(profile.getDependency(), profile.getRepository().getVersion(version), GameAssetDownloadTask.DOWNLOAD_INDEX_FORCIBLY, true)
                .executor();
        MiuixTaskDialog dialog = new MiuixTaskDialog(context);
        dialog.setExecutor(executor);
        dialog.setTitle(context.getString(R.string.version_manage_redownload_assets_index));
        dialog.show();
        executor.start();
    }

    public static void cleanVersion(Profile profile, String id) {
        try {
            profile.getRepository().clean(id);
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, "Unable to clean game directory", e);
        }
    }

    public static void launch(Context context, Profile profile) {
        launch(context, profile, profile.getSelectedVersion());
    }

    public static void launch(Context context, Profile profile, String id) {
        launch(context, profile, id, null);
    }

    public static void launch(Context context, Profile profile, String id, Consumer<LauncherHelper> injector) {
        if (!checkVersionForLaunching(context, profile, id))
            return;
        ensureSelectedAccount(context, account -> {
            LauncherHelper launcherHelper = new LauncherHelper(context, profile, account, id);
            if (injector != null)
                injector.accept(launcherHelper);
            launcherHelper.launch();
        });
    }

    private static boolean checkVersionForLaunching(Context context, Profile profile, String id) {
        if (id == null || !profile.getRepository().isLoaded() || !profile.getRepository().hasVersion(id)) {
            Runnable jumpToDownload = () -> NavigationBus.select(NavigationBus.Menu.DOWNLOAD);
            FCLDialogs.showAlert(context, context.getString(R.string.launch_failed),
                    context.getString(R.string.version_empty_launch),
                    null, null,
                    result -> { if (result) jumpToDownload.run(); }, false);
            return false;
        } else {
            return true;
        }
    }

    private static void ensureSelectedAccount(Context context, Consumer<Account> action) {
        Account account = Accounts.getSelectedAccount();
        if (account == null) {
            android.content.DialogInterface.OnDismissListener dismissListener = dialogInterface -> {
                Account newAccount = Accounts.getSelectedAccount();
                if (newAccount == null) {
                    // user cancelled operation
                } else {
                    action.accept(newAccount);
                }
            };
            MiuixCreateAccountDialog dialog = new MiuixCreateAccountDialog(context, (AccountFactory<?>) null);
            dialog.setOnDismissListener(dismissListener);
            dialog.show();
        } else {
            action.accept(account);
        }
    }

}
