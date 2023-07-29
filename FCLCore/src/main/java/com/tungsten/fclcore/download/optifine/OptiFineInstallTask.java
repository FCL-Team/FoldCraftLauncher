package com.tungsten.fclcore.download.optifine;

import static com.tungsten.fclcore.util.Lang.getOrDefault;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.download.ProcessService;
import com.tungsten.fclcore.download.UnsupportedInstallationException;
import com.tungsten.fclcore.download.VersionMismatchException;
import com.tungsten.fclcore.game.Arguments;
import com.tungsten.fclcore.game.Artifact;
import com.tungsten.fclcore.game.DefaultGameRepository;
import com.tungsten.fclcore.game.LibrariesDownloadInfo;
import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.LibraryDownloadInfo;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.SocketServer;
import com.tungsten.fclcore.util.io.CompressingUtils;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.platform.CommandBuilder;
import com.tungsten.fclcore.util.versioning.VersionNumber;

import org.jenkinsci.constant_pool_scanner.ConstantPool;
import org.jenkinsci.constant_pool_scanner.ConstantPoolScanner;
import org.jenkinsci.constant_pool_scanner.ConstantType;
import org.jenkinsci.constant_pool_scanner.Utf8Constant;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * <b>Note</b>: OptiFine should be installed in the end.
 */
public final class OptiFineInstallTask extends Task<Version> {

    private final DefaultGameRepository gameRepository;
    private final DefaultDependencyManager dependencyManager;
    private final Version version;
    private final OptiFineRemoteVersion remote;
    private final Path installer;
    private final List<Task<?>> dependents = new ArrayList<>(0);
    private final List<Task<?>> dependencies = new ArrayList<>(1);
    private Path dest;

    private final Library optiFineLibrary;
    private final Library optiFineInstallerLibrary;

    public OptiFineInstallTask(DefaultDependencyManager dependencyManager, Version version, OptiFineRemoteVersion remoteVersion) {
        this(dependencyManager, version, remoteVersion, null);
    }

    public OptiFineInstallTask(DefaultDependencyManager dependencyManager, Version version, OptiFineRemoteVersion remoteVersion, Path installer) {
        this.dependencyManager = dependencyManager;
        this.gameRepository = dependencyManager.getGameRepository();
        this.version = version;
        this.remote = remoteVersion;
        this.installer = installer;

        String mavenVersion = remote.getGameVersion() + "_" + remote.getSelfVersion();

        optiFineLibrary = new Library(new Artifact("optifine", "OptiFine", mavenVersion));

        optiFineInstallerLibrary = new Library(
                new Artifact("optifine", "OptiFine", mavenVersion, "installer"), null,
                new LibrariesDownloadInfo(new LibraryDownloadInfo(
                        "optifine/OptiFine/" + mavenVersion + "/OptiFine-" + mavenVersion + "-installer.jar",
                        remote.getUrls().get(0).toString()))
        );
    }

    @Override
    public boolean doPreExecute() {
        return true;
    }

    @Override
    public void preExecute() throws Exception {
        dest = Files.createTempFile("optifine-installer", ".jar");

        if (installer == null) {
            FileDownloadTask task = new FileDownloadTask(
                    dependencyManager.getDownloadProvider().injectURLsWithCandidates(remote.getUrls()),
                    dest.toFile(), null);
            task.setCacheRepository(dependencyManager.getCacheRepository());
            task.setCaching(true);
            dependents.add(task);
        } else {
            FileUtils.copyFile(installer, dest);
        }
    }

    @Override
    public Collection<Task<?>> getDependents() {
        return dependents;
    }

    @Override
    public Collection<Task<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public boolean isRelyingOnDependencies() {
        return false;
    }

    @Override
    public void execute() throws Exception {
        String originalMainClass = version.resolve(dependencyManager.getGameRepository()).getMainClass();
        if (!LibraryAnalyzer.VANILLA_MAIN.equals(originalMainClass) &&
                !LibraryAnalyzer.LAUNCH_WRAPPER_MAIN.equals(originalMainClass) &&
                !LibraryAnalyzer.MOD_LAUNCHER_MAIN.equals(originalMainClass) &&
                !LibraryAnalyzer.BOOTSTRAP_LAUNCHER_MAIN.equals(originalMainClass))
            throw new UnsupportedInstallationException(UnsupportedInstallationException.UNSUPPORTED_LAUNCH_WRAPPER);

        List<Library> libraries = new ArrayList<>(4);
        libraries.add(optiFineLibrary);

        FileUtils.copyFile(dest, gameRepository.getLibraryFile(version, optiFineInstallerLibrary).toPath());

        // Install launch wrapper modified by OptiFine
        boolean hasLaunchWrapper = false;
        try (FileSystem fs = CompressingUtils.createReadOnlyZipFileSystem(dest)) {
            if (Files.exists(fs.getPath("optifine/Patcher.class"))) {
                String[] command = {
                        "-cp",
                        dest.toString(),
                        "optifine.Patcher",
                        gameRepository.getVersionJar(version).getAbsolutePath(),
                        dest.toString(),
                        gameRepository.getLibraryFile(version, optiFineLibrary).toString()
                };
                int exitCode;
                boolean listen = true;
                while (listen) {
                    if (((ActivityManager) FCLPath.CONTEXT.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses().size() == 1) {
                        listen = false;
                    }
                }
                CountDownLatch latch = new CountDownLatch(1);
                SocketServer server = new SocketServer("127.0.0.1", ProcessService.PROCESS_SERVICE_PORT, (server1, msg) -> {
                    server1.setResult(msg);
                    server1.stop();
                    latch.countDown();
                });
                Intent service = new Intent(FCLPath.CONTEXT, ProcessService.class);
                Bundle bundle = new Bundle();
                bundle.putStringArray("command", command);
                service.putExtras(bundle);
                FCLPath.CONTEXT.startService(service);
                server.start();
                latch.await();
                exitCode = Integer.parseInt((String) server.getResult());
                if (exitCode != 0)
                    throw new IOException("OptiFine patcher failed, command: " + new CommandBuilder().addAll(Arrays.asList(command)));
            } else {
                FileUtils.copyFile(dest, gameRepository.getLibraryFile(version, optiFineLibrary).toPath());
            }

            Path launchWrapper2 = fs.getPath("launchwrapper-2.0.jar");
            if (Files.exists(launchWrapper2)) {
                Library launchWrapper = new Library(new Artifact("optifine", "launchwrapper", "2.0"));
                File launchWrapperFile = gameRepository.getLibraryFile(version, launchWrapper);
                FileUtils.makeDirectory(launchWrapperFile.getAbsoluteFile().getParentFile());
                FileUtils.copyFile(launchWrapper2, launchWrapperFile.toPath());
                hasLaunchWrapper = true;
                libraries.add(launchWrapper);
            }

            Path launchWrapperVersionText = fs.getPath("launchwrapper-of.txt");
            if (Files.exists(launchWrapperVersionText)) {
                String launchWrapperVersion = FileUtils.readText(launchWrapperVersionText).trim();
                Path launchWrapperJar = fs.getPath("launchwrapper-of-" + launchWrapperVersion + ".jar");

                Library launchWrapper = new Library(new Artifact("optifine", "launchwrapper-of", launchWrapperVersion));

                if (Files.exists(launchWrapperJar)) {
                    File launchWrapperFile = gameRepository.getLibraryFile(version, launchWrapper);
                    FileUtils.makeDirectory(launchWrapperFile.getAbsoluteFile().getParentFile());
                    FileUtils.copyFile(launchWrapperJar, launchWrapperFile.toPath());

                    hasLaunchWrapper = true;
                    libraries.add(launchWrapper);
                }
            }

            Path buildofText = fs.getPath("buildof.txt");
            if (Files.exists(buildofText)) {
                String buildof = FileUtils.readText(buildofText).trim();
                VersionNumber buildofVer = VersionNumber.asVersion(buildof);

                if (LibraryAnalyzer.BOOTSTRAP_LAUNCHER_MAIN.equals(originalMainClass)) {
                    // OptiFine H1 Pre2+ is compatible with Forge 1.17
                    if (buildofVer.compareTo(VersionNumber.asVersion("20210924-190833")) < 0) {
                        throw new UnsupportedInstallationException(UnsupportedInstallationException.FORGE_1_17_OPTIFINE_H1_PRE2);
                    }
                }
            }
        }

        if (!hasLaunchWrapper) {
            libraries.add(new Library(new Artifact("net.minecraft", "launchwrapper", "1.12")));
        }

        setResult(new Version(
                LibraryAnalyzer.LibraryType.OPTIFINE.getPatchId(),
                remote.getSelfVersion(),
                10000,
                new Arguments().addGameArguments("--tweakClass", "optifine.OptiFineTweaker"),
                LibraryAnalyzer.LAUNCH_WRAPPER_MAIN,
                libraries
        ));

        dependencies.add(dependencyManager.checkLibraryCompletionAsync(getResult(), true));
    }

    /**
     * Install OptiFine library from existing local file.
     *
     * @param dependencyManager game repository
     * @param version           version.json
     * @param installer         the OptiFine installer
     * @return the task to install library
     * @throws IOException              if unable to read compressed content of installer file, or installer file is corrupted, or the installer is not the one we want.
     * @throws VersionMismatchException if required game version of installer does not match the actual one.
     */
    public static Task<Version> install(DefaultDependencyManager dependencyManager, Version version, Path installer) throws IOException, VersionMismatchException {
        Optional<String> gameVersion = dependencyManager.getGameRepository().getGameVersion(version);
        if (!gameVersion.isPresent()) throw new IOException();
        try (FileSystem fs = CompressingUtils.createReadOnlyZipFileSystem(installer)) {
            Path configClass = fs.getPath("Config.class");
            if (!Files.exists(configClass)) configClass = fs.getPath("net/optifine/Config.class");
            if (!Files.exists(configClass)) configClass = fs.getPath("notch/net/optifine/Config.class");
            if (!Files.exists(configClass)) throw new IOException("Unrecognized installer");
            ConstantPool pool = ConstantPoolScanner.parse(Files.readAllBytes(configClass), ConstantType.UTF8);
            List<String> constants = new ArrayList<>();
            pool.list(Utf8Constant.class).forEach(utf8 -> constants.add(utf8.get()));
            String mcVersion = getOrDefault(constants, constants.indexOf("MC_VERSION") + 1, null);
            String ofEdition = getOrDefault(constants, constants.indexOf("OF_EDITION") + 1, null);
            String ofRelease = getOrDefault(constants, constants.indexOf("OF_RELEASE") + 1, null);

            if (mcVersion == null || ofEdition == null || ofRelease == null)
                throw new IOException("Unrecognized OptiFine installer");

            if (!mcVersion.equals(gameVersion.get()))
                throw new VersionMismatchException(mcVersion, gameVersion.get());

            return new OptiFineInstallTask(dependencyManager, version,
                    new OptiFineRemoteVersion(mcVersion, ofEdition + "_" + ofRelease, Collections.singletonList(""), false), installer);
        }
    }
}
