package com.tungsten.fcl.ui.version;

import android.content.Context;
import android.widget.Toast;

import com.mio.download.DownloadManager;
import com.mio.util.ParseUtil;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.game.LauncherHelper;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.ui.download.modpack.LocalModpackPage;
import com.tungsten.fcl.ui.download.modpack.ModpackSelectionPage;
import com.tungsten.fcl.ui.manage.ModpackTypeSelectionPage;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.download.game.GameAssetDownloadTask;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.platform.OperatingSystem;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.ui.ProgressDialog;

import java.io.IOException;
import java.net.URL;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;

public class Versions {

    public static void importModpack(Context context) {
        Profile profile = Profiles.getSelectedProfile();
        if (profile.getRepository().isLoaded()) {
            ModpackSelectionPage page = new ModpackSelectionPage(context, FCLPage.PAGE_ID_TEMP, profile, null);
            UIManager.getInstance().getDownloadUI().showTempPage(page);
        }
    }

    public static void downloadModpackImpl(Context context, Profile profile, RemoteMod.Version file) {
        Path modpack;
        URL downloadURL;
        try {
            modpack = Files.createTempFile("modpack", ".zip");
            downloadURL = new URL(file.file().url());
        } catch (IOException e) {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
            builder.setCancelable(false);
            builder.setTitle(context.getString(R.string.download_failed));
            builder.setMessage(context.getString(R.string.install_failed_downloading_detail, file.file().url()) + "\n" + StringUtils.getStackTrace(e));
            builder.setNegativeButton(context.getString(com.tungsten.fcl.R.string.dialog_positive), null);
            builder.create().show();
            return;
        }

        FileDownloadTask downloadTask = new FileDownloadTask(downloadURL, modpack.toFile());
        TaskExecutor executor = downloadTask.whenComplete(Schedulers.androidUIThread(), e -> {
                    if (e instanceof CancellationException) {
                        modpack.toFile().delete();
                        Toast.makeText(context, context.getString(R.string.message_cancelled), Toast.LENGTH_SHORT).show();
                    } else if (e != null) {
                        modpack.toFile().delete();
                        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
                        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                        builder.setCancelable(false);
                        builder.setTitle(context.getString(R.string.install_failed_downloading));
                        builder.setMessage(context.getString(R.string.install_failed_downloading_detail, file.file().url()) + "\n" + StringUtils.getStackTrace(e));
                        builder.setNegativeButton(context.getString(com.tungsten.fcl.R.string.dialog_positive), null);
                        builder.create().show();
                    } else {
                        // 下载完成：保留在下载面板，由用户手动点击安装
                        Toast.makeText(context, context.getString(R.string.download_ready_to_install), Toast.LENGTH_LONG).show();
                    }
                }).executor();
        DownloadManager.submit(file.file().filename(), downloadTask, executor,
                () -> installDownloadedModpack(context, profile, modpack.toFile()),
                () -> modpack.toFile().delete());
        executor.start();
    }

    /** 打开已下载整合包的安装页 */
    private static void installDownloadedModpack(Context context, Profile profile, File modpack) {
        LocalModpackPage page = new LocalModpackPage(context, FCLPage.PAGE_ID_TEMP, profile, null, modpack);
        // 切换到下载 UI，让安装页显示在前台
        UIManager.getInstance().switchUI(UIManager.getInstance().getDownloadUI());
        UIManager.getInstance().getDownloadUI().showTempPage(page);
    }

    public static void deleteVersion(Context context, Profile profile, String version) {
        boolean isIndependent = profile.getVersionSetting(version).isIsolateGameDir();
        String message = isIndependent ? String.format(context.getString(R.string.version_manage_remove_confirm_independent), version) : String.format(context.getString(R.string.version_manage_remove_confirm), version);

        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
        builder.setMessage(message);
        builder.setPositiveButton(() -> {
            ProgressDialog progress = new ProgressDialog(context);
            Task.runAsync(() -> profile.getRepository().removeVersionFromDisk(version)).whenComplete(Schedulers.androidUIThread(), (e) -> progress.dismiss()).start();
        });
        builder.setNegativeButton(null);
        builder.create().show();
    }

    public static CompletableFuture<String> renameVersion(Context context, Profile profile, String version) {
        RenameVersionDialog dialog = new RenameVersionDialog(context, version, (newName, resolve, reject) -> {
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
        });
        dialog.show();
        return dialog.getFuture();
    }

    public static void exportVersion(Context context, Profile profile, String version) {
        ModpackTypeSelectionPage page = new ModpackTypeSelectionPage(context, FCLPage.PAGE_ID_TEMP, profile, version);
        UIManager.getInstance().getManageUI().showTempPage(page);
    }

    public static void duplicateVersion(Context context, Profile profile, String version) {
        DuplicateVersionDialog dialog = new DuplicateVersionDialog(context, profile, version, (res, resolve, reject) -> {
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
        });
        dialog.show();
    }

    public static void updateVersion(Context context, Profile profile, String version) {
        ModpackSelectionPage page = new ModpackSelectionPage(context, FCLPage.PAGE_ID_TEMP, profile, version);
        UIManager.getInstance().getManageUI().showTempPage(page);
    }

    public static void updateGameAssets(Context context, Profile profile, String version) {
        TaskExecutor executor = new GameAssetDownloadTask(profile.getDependency(), profile.getRepository().getVersion(version), GameAssetDownloadTask.DOWNLOAD_INDEX_FORCIBLY, true)
                .executor();
        TaskDialog dialog = new TaskDialog(context, TaskCancellationAction.NORMAL);
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
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
            builder.setTitle(context.getString(R.string.launch_failed));
            builder.setMessage(context.getString(R.string.version_empty_launch));
            builder.setNegativeButton(context.getString(com.tungsten.fcl.R.string.dialog_positive), () -> {
                MainActivity.getInstance().refreshMenuView(null);
                MainActivity.getInstance().binding.download.setSelected(true);
            });
            builder.create().show();
            return false;
        } else {
            return true;
        }
    }

    private static void ensureSelectedAccount(Context context, Consumer<Account> action) {
        Account account = Accounts.getSelectedAccount();
        if (account != null) {
            action.accept(account);
            return;
        }
        // 未创建账户：提示后跳转账户管理页，中止本次启动
        Toast.makeText(context, R.string.create_account_first, Toast.LENGTH_SHORT).show();
        UIManager uiManager = UIManager.getInstance();
        uiManager.switchUI(uiManager.getAccountUI());
    }

}
