package com.tungsten.fcl.ui.version;

import android.app.Activity;
import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.game.LauncherHelper;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.ui.account.CreateAccountDialog;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.game.GameDirectoryType;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.platform.OperatingSystem;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;

public class Versions {

    public static void deleteVersion(Context context, Profile profile, String version) {
        boolean isIndependent = profile.getVersionSetting(version).getGameDirType() == GameDirectoryType.VERSION_FOLDER;
        String message = isIndependent ? String.format(context.getString(R.string.version_manage_remove_confirm_independent), version) : String.format(context.getString(R.string.version_manage_remove_confirm), version);

        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
        builder.setMessage(message);
        builder.setPositiveButton(() -> {
            profile.getRepository().removeVersionFromDisk(version);
            UIManager.getInstance().getVersionUI().refresh().start();
        });
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
                            UIManager.getInstance().getVersionUI().refresh().start();
                        }).start();
            } else {
                reject.accept(context.getString(R.string.version_manage_rename_fail));
            }
        });
        dialog.show();
        return dialog.getFuture();
    }

    /*
    public static void exportVersion(Profile profile, String version) {
        Controllers.getDecorator().startWizard(new ExportWizardProvider(profile, version), i18n("modpack.wizard"));
    }

     */

    public static void openFolder(Activity context, Profile profile, String version) {
        FileBrowser.Builder builder = new FileBrowser.Builder(context);
        builder.setLibMode(LibMode.FILE_BROWSER);
        builder.setInitDir(profile.getRepository().getRunDirectory(version).getAbsolutePath());
        builder.create().browse(context, RequestCodes.BROWSE_VERSION_DIR_CODE, null);
    }

    /*
    public static void duplicateVersion(Profile profile, String version) {
        Controllers.prompt(
                new PromptDialogPane.Builder(i18n("version.manage.duplicate.prompt"), (res, resolve, reject) -> {
                    String newVersionName = ((PromptDialogPane.Builder.StringQuestion) res.get(1)).getValue();
                    boolean copySaves = ((PromptDialogPane.Builder.BooleanQuestion) res.get(2)).getValue();
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
                })
                        .addQuestion(new PromptDialogPane.Builder.HintQuestion(i18n("version.manage.duplicate.confirm")))
                        .addQuestion(new PromptDialogPane.Builder.StringQuestion(null, version,
                                new Validator(i18n("install.new_game.already_exists"), newVersionName -> !profile.getRepository().hasVersion(newVersionName))))
                        .addQuestion(new PromptDialogPane.Builder.BooleanQuestion(i18n("version.manage.duplicate.duplicate_save"), false)));
    }

    public static void updateVersion(Profile profile, String version) {
        Controllers.getDecorator().startWizard(new ModpackInstallWizardProvider(profile, version));
    }

    public static void updateGameAssets(Profile profile, String version) {
        TaskExecutor executor = new GameAssetDownloadTask(profile.getDependency(), profile.getRepository().getVersion(version), GameAssetDownloadTask.DOWNLOAD_INDEX_FORCIBLY, true)
                .executor();
        Controllers.taskDialog(executor, i18n("version.manage.redownload_assets_index"), TaskCancellationAction.NO_CANCEL);
        executor.start();
    }

     */

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
            CreateAccountDialog dialog = new CreateAccountDialog(context, null);
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
