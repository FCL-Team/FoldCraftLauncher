package com.tungsten.fcl.game;

import static com.tungsten.fcl.util.AndroidUtils.getLocalizedText;
import static com.tungsten.fcl.util.AndroidUtils.hasStringId;
import static com.tungsten.fclcore.util.Logging.LOG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.JVMActivity;
import com.tungsten.fcl.control.MenuType;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.VersionSetting;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.ui.account.AccountListItem;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.auth.AuthenticationException;
import com.tungsten.fclcore.auth.CharacterDeletedException;
import com.tungsten.fclcore.auth.CredentialExpiredException;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorDownloadException;
import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.MaintainTask;
import com.tungsten.fclcore.download.game.GameAssetIndexDownloadTask;
import com.tungsten.fclcore.download.game.GameVerificationFixTask;
import com.tungsten.fclcore.download.game.LibraryDownloadException;
import com.tungsten.fclcore.game.JavaVersion;
import com.tungsten.fclcore.game.LaunchOptions;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.mod.ModpackCompletionException;
import com.tungsten.fclcore.mod.ModpackConfiguration;
import com.tungsten.fclcore.mod.ModpackProvider;
import com.tungsten.fclcore.task.DownloadException;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.task.TaskListener;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.LibFilter;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.ResponseCodeException;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public final class LauncherHelper {

    private final Context context;
    private final Profile profile;
    private final Account account;
    private final String selectedVersion;
    private final VersionSetting setting;
    private final TaskDialog launchingStepsPane;

    public LauncherHelper(Context context, Profile profile, Account account, String selectedVersion) {
        this.context = Objects.requireNonNull(context);
        this.profile = Objects.requireNonNull(profile);
        this.account = Objects.requireNonNull(account);
        this.selectedVersion = Objects.requireNonNull(selectedVersion);
        this.setting = profile.getVersionSetting(selectedVersion);
        this.launchingStepsPane = new TaskDialog(context, TaskCancellationAction.NORMAL);
        this.launchingStepsPane.setTitle(context.getString(R.string.version_launch));
    }

    public void launch() {
        LOG.info("Launching game version: " + selectedVersion);

        launchingStepsPane.show();
        launch0();
    }

    private void launch0() {
        FCLGameRepository repository = profile.getRepository();
        DefaultDependencyManager dependencyManager = profile.getDependency();
        AtomicReference<Version> version = new AtomicReference<>(MaintainTask.maintain(repository, repository.getResolvedVersion(selectedVersion)));
        Optional<String> gameVersion = repository.getGameVersion(version.get());
        boolean integrityCheck = repository.unmarkVersionLaunchedAbnormally(selectedVersion);
        List<String> javaAgents = new ArrayList<>(0);

        AtomicReference<JavaVersion> javaVersionRef = new AtomicReference<>();

        TaskExecutor executor = checkGameState(context, setting, version.get())
                .thenComposeAsync(javaVersion -> {
                    javaVersionRef.set(Objects.requireNonNull(javaVersion));
                    version.set(LibFilter.filter(version.get()));
                    if (setting.isNotCheckGame())
                        return null;
                    return Task.allOf(
                            dependencyManager.checkGameCompletionAsync(version.get(), integrityCheck),
                            Task.composeAsync(() -> {
                                try {
                                    ModpackConfiguration<?> configuration = ModpackHelper.readModpackConfiguration(repository.getModpackConfiguration(selectedVersion));
                                    ModpackProvider provider = ModpackHelper.getProviderByType(configuration.getType());
                                    if (provider == null) return null;
                                    else return provider.createCompletionTask(dependencyManager, selectedVersion);
                                } catch (IOException e) {
                                    return null;
                                }
                            }),
                            Task.composeAsync(() -> null)
                    );
                }).withStage("launch.state.dependencies")
                .thenComposeAsync(() -> {
                    if (!new File(FCLPath.MULTIPLAYER_FIX_PATH).exists()) {
                        try (InputStream input = LauncherHelper.class.getResourceAsStream("/assets/game/MultiplayerFix.jar")) {
                            Files.copy(input, new File(FCLPath.MULTIPLAYER_FIX_PATH).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            Logging.LOG.log(Level.WARNING, "Unable to unpack MultiplayerFix.jar", e);
                        }
                    }
                    return null;
                })
                .thenComposeAsync(() -> gameVersion.map(s -> new GameVerificationFixTask(dependencyManager, s, version.get())).orElse(null))
                .thenComposeAsync(() -> logIn(context, account).withStage("launch.state.logging_in"))
                .thenComposeAsync(authInfo -> Task.supplyAsync(() -> {
                    LaunchOptions launchOptions = repository.getLaunchOptions(selectedVersion, javaVersionRef.get(), profile.getGameDir(), javaAgents);
                    return new FCLGameLauncher(
                            context,
                            repository,
                            version.get(),
                            authInfo,
                            launchOptions
                    );
                }).thenComposeAsync(launcher -> { // launcher is prev task's result
                    return Task.supplyAsync(launcher::launch);
                }).thenAcceptAsync(fclBridge -> Schedulers.androidUIThread().execute(() -> {
                    Intent intent = new Intent(context, JVMActivity.class);
                    fclBridge.setScaleFactor(repository.getVersionSetting(selectedVersion).getScaleFactor());
                    fclBridge.setController(repository.getVersionSetting(selectedVersion).getController());
                    fclBridge.setGameDir(repository.getRunDirectory(selectedVersion).getAbsolutePath());
                    fclBridge.setRenderer(repository.getVersionSetting(selectedVersion).getRenderer().toString());
                    fclBridge.setJava(Integer.toString(javaVersionRef.get().getVersion()));
                    JVMActivity.setFCLBridge(fclBridge, MenuType.GAME);
                    Bundle bundle = new Bundle();
                    bundle.putString("controller", repository.getVersionSetting(selectedVersion).getController());
                    intent.putExtras(bundle);
                    LOG.log(Level.INFO, "Start JVMActivity!");
                    context.startActivity(intent);
                })).withStage("launch.state.waiting_launching"))
                .withStagesHint(Lang.immutableListOf(
                        "launch.state.java",
                        "launch.state.dependencies",
                        "launch.state.logging_in",
                        "launch.state.waiting_launching"))
                .executor();
        launchingStepsPane.setExecutor(executor, false);
        executor.addTaskListener(new TaskListener() {

            @Override
            public void onStop(boolean success, TaskExecutor executor) {
                launchingStepsPane.dismiss();
                if (!success) {
                    Exception ex = executor.getException();
                    if (!(ex instanceof CancellationException)) {
                        Schedulers.androidUIThread().execute(() -> {
                            String message;
                            if (ex instanceof ModpackCompletionException) {
                                if (ex.getCause() instanceof FileNotFoundException)
                                    message = getLocalizedText(context, "modpack_type_curse_not_found");
                                else
                                    message = getLocalizedText(context, "modpack_type_curse_error");
                            } else if (ex instanceof LibraryDownloadException) {
                                message = getLocalizedText(context, "launch_failed_download_library", ((LibraryDownloadException) ex).getLibrary().getName()) + "\n";
                                if (ex.getCause() instanceof ResponseCodeException) {
                                    ResponseCodeException rce = (ResponseCodeException) ex.getCause();
                                    int responseCode = rce.getResponseCode();
                                    URL url = rce.getUrl();
                                    if (responseCode == 404)
                                        message += getLocalizedText(context, "download_code_404", url);
                                    else
                                        message += getLocalizedText(context, "download_failed", url, responseCode);
                                } else {
                                    message += StringUtils.getStackTrace(ex.getCause());
                                }
                            } else if (ex instanceof DownloadException) {
                                URL url = ((DownloadException) ex).getUrl();
                                if (ex.getCause() instanceof SocketTimeoutException) {
                                    message = getLocalizedText(context, "install_failed_downloading_timeout", url);
                                } else if (ex.getCause() instanceof ResponseCodeException) {
                                    ResponseCodeException responseCodeException = (ResponseCodeException) ex.getCause();
                                    if (hasStringId(context, "download_code_" + responseCodeException.getResponseCode())) {
                                        message = getLocalizedText(context, "download_code_" + responseCodeException.getResponseCode(), url);
                                    } else {
                                        message = getLocalizedText(context, "install_failed_downloading_detail", url) + "\n" + StringUtils.getStackTrace(ex.getCause());
                                    }
                                } else {
                                    message = getLocalizedText(context, "install_failed_downloading_detail", url) + "\n" + StringUtils.getStackTrace(ex.getCause());
                                }
                            } else if (ex instanceof GameAssetIndexDownloadTask.GameAssetIndexMalformedException) {
                                message = getLocalizedText(context, "assets_index_malformed");
                            } else if (ex instanceof AuthlibInjectorDownloadException) {
                                message = getLocalizedText(context, "account_failed_injector_download_failure");
                            } else if (ex instanceof CharacterDeletedException) {
                                message = getLocalizedText(context, "account_failed_character_deleted");
                            } else if (ex instanceof ResponseCodeException) {
                                ResponseCodeException rce = (ResponseCodeException) ex;
                                int responseCode = rce.getResponseCode();
                                URL url = rce.getUrl();
                                if (responseCode == 404)
                                    message = getLocalizedText(context, "download_code_404", url);
                                else
                                    message = getLocalizedText(context, "download_failed", url, responseCode);
                            } else if (ex instanceof AccessDeniedException) {
                                message = getLocalizedText(context, "exception_access_denied", ((AccessDeniedException) ex).getFile());
                            } else {
                                if (ex == null) {
                                    message = "Task failed without exception!";
                                } else {
                                    message = StringUtils.getStackTrace(ex);
                                }
                            }

                            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
                            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                            builder.setCancelable(false);
                            builder.setTitle(context.getString(R.string.launch_failed));
                            builder.setMessage(message);
                            builder.setNegativeButton(context.getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                            builder.create().show();
                        });
                    }
                }
            }
        });

        executor.start();
    }

    private static Task<JavaVersion> checkGameState(Context context, VersionSetting setting, Version version) {
        if (setting.isNotCheckJVM()) {
            return Task.composeAsync(() -> setting.getJavaVersion(version))
                    .withStage("launch.state.java");
        }

        return Task.composeAsync(() -> setting.getJavaVersion(version))
                .thenComposeAsync(javaVersion -> Task.allOf(Task.completed(javaVersion), Task.supplyAsync(() -> JavaVersion.getSuitableJavaVersion(version))))
                .thenComposeAsync(Schedulers.androidUIThread(), javaVersions -> {
            JavaVersion javaVersion = (JavaVersion) javaVersions.get(0);
            JavaVersion suggestedJavaVersion = (JavaVersion) javaVersions.get(1);
            if (setting.getJava() == 0 || javaVersion.getVersion() == suggestedJavaVersion.getVersion()) {
                return Task.completed(suggestedJavaVersion);
            }

            CompletableFuture<JavaVersion> future = new CompletableFuture<>();
            Runnable continueAction = () -> future.complete(javaVersion);
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage(context.getString(R.string.launch_error_java));
            builder.setPositiveButton(context.getString(R.string.launch_error_java_auto), () -> {
                setting.setJava(0);
                future.complete(suggestedJavaVersion);
            });
            builder.setPositiveButton(context.getString(R.string.launch_error_java_continue), continueAction::run);
            builder.create().show();
            return Task.fromCompletableFuture(future);
        }).withStage("launch.state.java");
    }

    private static Task<AuthInfo> logIn(Context context, Account account) {
        return Task.composeAsync(() -> {
            try {
                return Task.completed(account.logIn());
            } catch (CredentialExpiredException e) {
                LOG.log(Level.INFO, "Credential has expired", e);

                return Task.completed(AccountListItem.logIn(account));
            } catch (AuthenticationException e) {
                LOG.log(Level.WARNING, "Authentication failed, try skipping refresh", e);

                CompletableFuture<Task<AuthInfo>> future = new CompletableFuture<>();
                Schedulers.androidUIThread().execute(() -> {
                    SkipLoginDialog dialog = new SkipLoginDialog(context, account, future);
                    dialog.show();
                });
                return Task.fromCompletableFuture(future).thenComposeAsync(task -> task);
            }
        });
    }

    static class SkipLoginDialog extends FCLDialog implements View.OnClickListener {

        private final Account account;
        private final CompletableFuture<Task<AuthInfo>> future;

        private FCLButton retry;
        private FCLButton skip;
        private FCLButton cancel;

        public SkipLoginDialog(@NonNull Context context, Account account, CompletableFuture<Task<AuthInfo>> future) {
            super(context);
            this.account = account;
            this.future = future;
            setContentView(R.layout.dialog_skip_login);
            setCancelable(false);

            retry = findViewById(R.id.retry);
            skip = findViewById(R.id.skip);
            cancel = findViewById(R.id.cancel);
            retry.setOnClickListener(this);
            skip.setOnClickListener(this);
            cancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == retry) {
                future.complete(logIn(getContext(), account));
            }
            if (view == skip) {
                try {
                    future.complete(Task.completed(account.playOffline()));
                } catch (AuthenticationException e2) {
                    future.completeExceptionally(e2);
                }
            }
            if (view == cancel) {
                future.completeExceptionally(new CancellationException());
            }
            dismiss();
        }
    }

}
