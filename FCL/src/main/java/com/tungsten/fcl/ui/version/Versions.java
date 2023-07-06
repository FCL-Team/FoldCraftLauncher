package com.tungsten.fcl.ui.version;

import android.app.Activity;
import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.game.LauncherHelper;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.ui.account.CreateAccountDialog;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.auth.AccountFactory;
import com.tungsten.fclcore.download.game.GameAssetDownloadTask;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.platform.OperatingSystem;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;

public class Versions {

    public static void importModpack() {
        Profile profile = Profiles.getSelectedProfile();
        if (profile.getRepository().isLoaded()) {
            //Controllers.getDecorator().startWizard(new ModpackInstallWizardProvider(profile), i18n("install.modpack"));
        }
    }

    public static void downloadModpackImpl(Context context, Profile profile, String version, RemoteMod.Version file) {
        /*
        Path modpack;
        URL downloadURL;
        try {
            modpack = Files.createTempFile("modpack", ".zip");
            downloadURL = new URL(file.getFile().getUrl());
        } catch (IOException e) {
            Controllers.dialog(
                    i18n("install_failed_downloading_detail", file.getFile().getUrl()) + "\n" + StringUtils.getStackTrace(e),
                    i18n("download_failed"), MessageDialogPane.MessageType.ERROR);
            return;
        }
        Controllers.taskDialog(
                new FileDownloadTask(downloadURL, modpack.toFile())
                        .whenComplete(Schedulers.javafx(), e -> {
                            if (e == null) {
                                Controllers.getDecorator().startWizard(new ModpackInstallWizardProvider(profile, modpack.toFile()));
                            } else if (e instanceof CancellationException) {
                                Controllers.showToast(i18n("message.cancelled"));
                            } else {
                                Controllers.dialog(
                                        i18n("install.failed.downloading.detail", file.getFile().getUrl()) + "\n" + StringUtils.getStackTrace(e),
                                        i18n("download.failed"), MessageDialogPane.MessageType.ERROR);
                            }
                        }).executor(true),
                i18n("message.downloading"),
                TaskCancellationAction.NORMAL
        );

         */
    }

    public static void deleteVersion(Context context, Profile profile, String version) {
        boolean isIndependent = profile.getVersionSetting(version).isIsolateGameDir();
        String message = isIndependent ? String.format(context.getString(R.string.version_manage_remove_confirm_independent), version) : String.format(context.getString(R.string.version_manage_remove_confirm), version);

        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
        builder.setMessage(message);
        builder.setPositiveButton(() -> profile.getRepository().removeVersionFromDisk(version));
        builder.setNegativeButton(null);
        builder.create().show();
    }

    public static CompletableFuture<String> renameVersion(Context context, Profile profile, String version) {
        RenameVersionDialog dialog = new RenameVersionDialog(context, version, (newName, resolve, reject) -> {
            if (!OperatingSystem.isNameValid(newName)) {
                reject.accept(context.getString(R.string.install_new_game_malformed));
                return;
            }
            if (profile.getRepository().renameVersion(version, newName)) {
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
        });
        dialog.show();
        return dialog.getFuture();
    }

    public static void exportVersion(Profile profile, String version) {
        //Controllers.getDecorator().startWizard(new ExportWizardProvider(profile, version), i18n("modpack.wizard"));
    }

    public static void openFolder(Activity context, Profile profile, String version) {
        FileBrowser.Builder builder = new FileBrowser.Builder(context);
        builder.setLibMode(LibMode.FILE_BROWSER);
        builder.setInitDir(profile.getRepository().getRunDirectory(version).getAbsolutePath());
        builder.create().browse(context, RequestCodes.BROWSE_DIR_CODE, null);
    }

    public static void duplicateVersion(Context context, Profile profile, String version) {
        DuplicateVersionDialog dialog = new DuplicateVersionDialog(context, profile, version, (res, resolve, reject) -> {
            String newVersionName = (String) res.get(0);
            boolean copySaves = (boolean) res.get(1);
            Task.runAsync(() -> profile.getRepository().duplicateVersion(version, newVersionName, copySaves))
                    .thenComposeAsync(profile.getRepository().refreshVersionsAsync())
                    .whenComplete(Schedulers.androidUIThread(), (result, exception) -> {
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

    public static void updateVersion(Profile profile, String version) {
        //Controllers.getDecorator().startWizard(new ModpackInstallWizardProvider(profile, version));
    }

    public static void updateGameAssets(Context context, Profile profile, String version) {
        TaskExecutor executor = new GameAssetDownloadTask(profile.getDependency(), profile.getRepository().getVersion(version), GameAssetDownloadTask.DOWNLOAD_INDEX_FORCIBLY, true)
                .executor();
        TaskDialog dialog = new TaskDialog(context, TaskCancellationAction.NO_CANCEL);
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
            builder.setNegativeButton(context.getString(com.tungsten.fcllibrary.R.string.dialog_positive), () -> {
                MainActivity.getInstance().refreshMenuView(null);
                MainActivity.getInstance().download.setSelected(true);
            });
            builder.create().show();
            return false;
        } else {
            return true;
        }
    }

    private static void ensureSelectedAccount(Context context, Consumer<Account> action) {
        Account account = Accounts.getSelectedAccount();
        if (account == null) {
            CreateAccountDialog dialog = new CreateAccountDialog(context, (AccountFactory<?>) null);
            dialog.setOnDismissListener(dialogInterface -> {
                Account newAccount = Accounts.getSelectedAccount();
                if (newAccount == null) {
                    // user cancelled operation
                } else {
                    action.accept(newAccount);
                }
            });
            dialog.show();
        } else {
            action.accept(account);
        }
    }

}
