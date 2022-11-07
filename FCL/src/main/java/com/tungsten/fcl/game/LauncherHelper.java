package com.tungsten.fcl.game;

import static com.tungsten.fclcore.util.Logging.LOG;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.VersionSetting;
import com.tungsten.fclauncher.FCLPath;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
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
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.ResponseCodeException;
import com.tungsten.fclcore.util.platform.CommandBuilder;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public final class LauncherHelper {

    /*

    private final Profile profile;
    private final Account account;
    private final String selectedVersion;
    private final VersionSetting setting;
    private boolean showLogs;

    public LauncherHelper(Profile profile, Account account, String selectedVersion) {
        this.profile = Objects.requireNonNull(profile);
        this.account = Objects.requireNonNull(account);
        this.selectedVersion = Objects.requireNonNull(selectedVersion);
        this.setting = profile.getVersionSetting(selectedVersion);
        this.launchingStepsPane.setTitle(i18n("version.launch"));
    }

    private final TaskExecutorDialogPane launchingStepsPane = new TaskExecutorDialogPane(TaskCancellationAction.NORMAL);

    public void launch() {
        LOG.info("Launching game version: " + selectedVersion);

        Controllers.dialog(launchingStepsPane);
        launch0();
    }

    private void launch0() {
        FCLGameRepository repository = profile.getRepository();
        DefaultDependencyManager dependencyManager = profile.getDependency();
        AtomicReference<Version> version = new AtomicReference<>(MaintainTask.maintain(repository, repository.getResolvedVersion(selectedVersion)));
        Optional<String> gameVersion = repository.getGameVersion(version.get());
        boolean integrityCheck = repository.unmarkVersionLaunchedAbnormally(selectedVersion);
        CountDownLatch launchingLatch = new CountDownLatch(1);
        List<String> javaAgents = new ArrayList<>(0);

        AtomicReference<JavaVersion> javaVersionRef = new AtomicReference<>();

        TaskExecutor executor = checkGameState(profile, setting, version.get())
                .thenComposeAsync(javaVersion -> {
                    javaVersionRef.set(Objects.requireNonNull(javaVersion));
                    version.set(version.get());
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
                    return gameVersion.map(s -> new GameVerificationFixTask(dependencyManager, s, version.get())).orElse(null);
                })
                .thenComposeAsync(() -> logIn(account).withStage("launch.state.logging_in"))
                .thenComposeAsync(authInfo -> Task.supplyAsync(() -> {
                    LaunchOptions launchOptions = repository.getLaunchOptions(selectedVersion, javaVersionRef.get(), profile.getGameDir(), javaAgents, scriptFile != null);
                    return new FCLGameLauncher(
                            FCLPath.CONTEXT,
                            repository,
                            version.get(),
                            authInfo,
                            launchOptions,
                            new FCLProcessListener()
                    );
                }).thenComposeAsync(launcher -> { // launcher is prev task's result
                    return Task.supplyAsync(launcher::launch);
                }).thenAcceptAsync(fclBridge -> { // process is LaunchTask's result
                    if (scriptFile == null) {
                        PROCESSES.add(process);
                        if (launcherVisibility == LauncherVisibility.CLOSE)
                            Launcher.stopApplication();
                        else
                            launchingStepsPane.setCancel(new TaskCancellationAction(it -> {
                                process.stop();
                                it.fireEvent(new DialogCloseEvent());
                            }));
                    } else {
                        Platform.runLater(() -> {
                            launchingStepsPane.fireEvent(new DialogCloseEvent());
                            Controllers.dialog(i18n("version.launch_script.success", scriptFile.getAbsolutePath()));
                        });
                    }
                }).thenRunAsync(() -> {
                    launchingLatch.await();
                }).withStage("launch.state.waiting_launching"))
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
                launchingStepsPane.fireEvent(new DialogCloseEvent());
                if (!success) {
                    Exception ex = executor.getException();
                    if (!(ex instanceof CancellationException)) {
                        String message;
                        if (ex instanceof ModpackCompletionException) {
                            if (ex.getCause() instanceof FileNotFoundException)
                                message = i18n("modpack.type.curse.not_found");
                            else
                                message = i18n("modpack.type.curse.error");
                        } else if (ex instanceof PermissionException) {
                            message = i18n("launch.failed.executable_permission");
                        } else if (ex instanceof ProcessCreationException) {
                            message = i18n("launch.failed.creating_process") + ex.getLocalizedMessage();
                        } else if (ex instanceof NotDecompressingNativesException) {
                            message = i18n("launch.failed.decompressing_natives") + ex.getLocalizedMessage();
                        } else if (ex instanceof LibraryDownloadException) {
                            message = i18n("launch.failed.download_library", ((LibraryDownloadException) ex).getLibrary().getName()) + "\n";
                            if (ex.getCause() instanceof ResponseCodeException) {
                                ResponseCodeException rce = (ResponseCodeException) ex.getCause();
                                int responseCode = rce.getResponseCode();
                                URL url = rce.getUrl();
                                if (responseCode == 404)
                                    message += i18n("download.code.404", url);
                                else
                                    message += i18n("download.failed", url, responseCode);
                            } else {
                                message += StringUtils.getStackTrace(ex.getCause());
                            }
                        } else if (ex instanceof DownloadException) {
                            URL url = ((DownloadException) ex).getUrl();
                            if (ex.getCause() instanceof SocketTimeoutException) {
                                message = i18n("install.failed.downloading.timeout", url);
                            } else if (ex.getCause() instanceof ResponseCodeException) {
                                ResponseCodeException responseCodeException = (ResponseCodeException) ex.getCause();
                                if (I18n.hasKey("download.code." + responseCodeException.getResponseCode())) {
                                    message = i18n("download.code." + responseCodeException.getResponseCode(), url);
                                } else {
                                    message = i18n("install.failed.downloading.detail", url) + "\n" + StringUtils.getStackTrace(ex.getCause());
                                }
                            } else {
                                message = i18n("install.failed.downloading.detail", url) + "\n" + StringUtils.getStackTrace(ex.getCause());
                            }
                        } else if (ex instanceof GameAssetIndexDownloadTask.GameAssetIndexMalformedException) {
                            message = i18n("assets.index.malformed");
                        } else if (ex instanceof AuthlibInjectorDownloadException) {
                            message = i18n("account.failed.injector_download_failure");
                        } else if (ex instanceof CharacterDeletedException) {
                            message = i18n("account.failed.character_deleted");
                        } else if (ex instanceof ResponseCodeException) {
                            ResponseCodeException rce = (ResponseCodeException) ex;
                            int responseCode = rce.getResponseCode();
                            URL url = rce.getUrl();
                            if (responseCode == 404)
                                message = i18n("download.code.404", url);
                            else
                                message = i18n("download.failed", url, responseCode);
                        } else if (ex instanceof CommandTooLongException) {
                            message = i18n("launch.failed.command_too_long");
                        } else if (ex instanceof ExecutionPolicyLimitException) {
                            Controllers.prompt(new PromptDialogPane.Builder(i18n("launch.failed.execution_policy"),
                                    (result, resolve, reject) -> {
                                        if (CommandBuilder.setExecutionPolicy()) {
                                            LOG.info("Set the ExecutionPolicy for the scope 'CurrentUser' to 'RemoteSigned'");
                                            resolve.run();
                                        } else {
                                            LOG.warning("Failed to set ExecutionPolicy");
                                            reject.accept(i18n("launch.failed.execution_policy.failed_to_set"));
                                        }
                                    })
                                    .addQuestion(new PromptDialogPane.Builder.HintQuestion(i18n("launch.failed.execution_policy.hint")))
                            );

                            return;
                        } else if (ex instanceof AccessDeniedException) {
                            message = i18n("exception.access_denied", ((AccessDeniedException) ex).getFile());
                        } else {
                            message = StringUtils.getStackTrace(ex);
                        }
                        Controllers.dialog(message,
                                scriptFile == null ? i18n("launch.failed") : i18n("version.launch_script.failed"),
                                MessageType.ERROR);
                    }
                }
            }
        });

        executor.start();
    }

    private static Task<JavaVersion> checkGameState(Profile profile, VersionSetting setting, Version version) {
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
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(FCLPath.CONTEXT);
            builder.setCancelable(false);
            builder.setMessage(FCLPath.CONTEXT.getString(R.string.launch_error_java));
            builder.setPositiveButton(FCLPath.CONTEXT.getString(R.string.launch_error_java_auto), () -> {
                setting.setJava(0);
                future.complete(suggestedJavaVersion);
            });
            builder.setPositiveButton(FCLPath.CONTEXT.getString(R.string.launch_error_java_continue), continueAction::run);
            builder.create().show();
            return Task.fromCompletableFuture(future);
        }).withStage("launch.state.java");
    }

    private static Task<AuthInfo> logIn(Account account) {
        return Task.composeAsync(() -> {
            try {
                return Task.completed(account.logIn());
            } catch (CredentialExpiredException e) {
                LOG.log(Level.INFO, "Credential has expired", e);

                return Task.completed(DialogController.logIn(account));
            } catch (AuthenticationException e) {
                LOG.log(Level.WARNING, "Authentication failed, try skipping refresh", e);

                CompletableFuture<Task<AuthInfo>> future = new CompletableFuture<>();
                runInFX(() -> {
                    JFXButton loginOfflineButton = new JFXButton(i18n("account.login.skip"));
                    loginOfflineButton.setOnAction(event -> {
                        try {
                            future.complete(Task.completed(account.playOffline()));
                        } catch (AuthenticationException e2) {
                            future.completeExceptionally(e2);
                        }
                    });
                    JFXButton retryButton = new JFXButton(i18n("account.login.retry"));
                    retryButton.setOnAction(event -> {
                        future.complete(logIn(account));
                    });
                    Controllers.dialog(new MessageDialogPane.Builder(i18n("account.failed.server_disconnected"), i18n("account.failed"), MessageType.ERROR)
                            .addAction(loginOfflineButton)
                            .addAction(retryButton)
                            .addCancel(() ->
                                    future.completeExceptionally(new CancellationException()))
                            .build());
                });
                return Task.fromCompletableFuture(future).thenComposeAsync(task -> task);
            }
        });
    }

    class FCLProcessListener implements FCLBridgeCallback {

        @Override
        public void onCursorModeChange(int mode) {
            // TODO: Handle mouse event
        }

        @Override
        public void onExit(int code) {
            if (code != 0) {
                // TODO: Show GameCrashWindow here
            }
        }
    }

    public static void stopManagedProcesses() {
        while (!PROCESSES.isEmpty())
            Optional.ofNullable(PROCESSES.poll()).ifPresent(ManagedProcess::stop);
    }

     */
}
