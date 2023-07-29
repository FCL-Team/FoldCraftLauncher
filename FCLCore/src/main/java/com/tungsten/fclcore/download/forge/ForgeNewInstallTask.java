package com.tungsten.fclcore.download.forge;

import static com.tungsten.fclcore.util.Logging.LOG;
import static com.tungsten.fclcore.util.gson.JsonUtils.fromNonNullJson;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.download.ArtifactMalformedException;
import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.download.ProcessService;
import com.tungsten.fclcore.download.game.GameLibrariesTask;
import com.tungsten.fclcore.download.game.VersionJsonDownloadTask;
import com.tungsten.fclcore.game.Artifact;
import com.tungsten.fclcore.game.DefaultGameRepository;
import com.tungsten.fclcore.game.DownloadInfo;
import com.tungsten.fclcore.game.DownloadType;
import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.DigestUtils;
import com.tungsten.fclcore.util.SocketServer;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.function.ExceptionalFunction;
import com.tungsten.fclcore.util.io.ChecksumMismatchException;
import com.tungsten.fclcore.util.io.CompressingUtils;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.platform.CommandBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

public class ForgeNewInstallTask extends Task<Version> {

    private class ProcessorTask extends Task<Void> {

        private ForgeNewInstallProfile.Processor processor;
        private Map<String, String> vars;

        public ProcessorTask(@NotNull ForgeNewInstallProfile.Processor processor, @NotNull Map<String, String> vars) {
            this.processor = processor;
            this.vars = vars;
            setSignificance(TaskSignificance.MODERATE);
        }

        @Override
        public void execute() throws Exception {
            Map<String, String> outputs = new HashMap<>();
            boolean miss = false;

            for (Map.Entry<String, String> entry : processor.getOutputs().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                key = parseLiteral(key, vars);
                value = parseLiteral(value, vars);

                if (key == null || value == null) {
                    throw new ArtifactMalformedException("Invalid forge installation configuration");
                }

                outputs.put(key, value);

                Path artifact = Paths.get(key);
                if (Files.exists(artifact)) {
                    String code;
                    try (InputStream stream = Files.newInputStream(artifact)) {
                        code = (DigestUtils.digestToString("SHA-1", stream));
                    }

                    if (!Objects.equals(code, value)) {
                        Files.delete(artifact);
                        LOG.info("Found existing file is not valid: " + artifact);

                        miss = true;
                    }
                } else {
                    miss = true;
                }
            }

            if (!processor.getOutputs().isEmpty() && !miss) {
                return;
            }

            Path jar = gameRepository.getArtifactFile(version, processor.getJar());
            if (!Files.isRegularFile(jar))
                throw new FileNotFoundException("Game processor file not found, should be downloaded in preprocess");

            String mainClass;
            try (JarFile jarFile = new JarFile(jar.toFile())) {
                mainClass = jarFile.getManifest().getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
            }

            if (StringUtils.isBlank(mainClass))
                throw new Exception("Game processor jar does not have main class " + jar);

            List<String> command = new ArrayList<>();
            command.add("-cp");

            List<String> classpath = new ArrayList<>(processor.getClasspath().size() + 1);
            for (Artifact artifact : processor.getClasspath()) {
                Path file = gameRepository.getArtifactFile(version, artifact);
                if (!Files.isRegularFile(file))
                    throw new Exception("Game processor dependency missing");
                classpath.add(file.toString());
            }
            classpath.add(jar.toString());
            command.add(String.join(File.pathSeparator, classpath));

            command.add(mainClass);

            List<String> args = new ArrayList<>(processor.getArgs().size());
            for (String arg : processor.getArgs()) {
                String parsed = parseLiteral(arg, vars);
                if (parsed == null)
                    throw new ArtifactMalformedException("Invalid forge installation configuration");
                args.add(parsed);
            }

            command.addAll(args);

            LOG.info("Executing external processor " + processor.getJar().toString() + ", command line: " + new CommandBuilder().addAll(command).toString());
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
            bundle.putStringArray("command", command.toArray(new String[0]));
            service.putExtras(bundle);
            FCLPath.CONTEXT.startService(service);
            server.start();
            latch.await();
            exitCode = Integer.parseInt((String) server.getResult());
            if (exitCode != 0)
                throw new IOException("Game processor exited abnormally with code " + exitCode);

            for (Map.Entry<String, String> entry : outputs.entrySet()) {
                Path artifact = Paths.get(entry.getKey());
                if (!Files.isRegularFile(artifact))
                    throw new FileNotFoundException("File missing: " + artifact);

                String code;
                try (InputStream stream = Files.newInputStream(artifact)) {
                    code = DigestUtils.digestToString("SHA-1", stream);
                }

                if (!Objects.equals(code, entry.getValue())) {
                    Files.delete(artifact);
                    throw new ChecksumMismatchException("SHA-1", entry.getValue(), code);
                }
            }
        }
    }

    private final DefaultDependencyManager dependencyManager;
    private final DefaultGameRepository gameRepository;
    private final Version version;
    private final Path installer;
    private final List<Task<?>> dependents = new ArrayList<>(1);
    private final List<Task<?>> dependencies = new ArrayList<>(1);

    private ForgeNewInstallProfile profile;
    private List<ForgeNewInstallProfile.Processor> processors;
    private Version forgeVersion;
    private final String selfVersion;

    private Path tempDir;
    private AtomicInteger processorDoneCount = new AtomicInteger(0);

    ForgeNewInstallTask(DefaultDependencyManager dependencyManager, Version version, String selfVersion, Path installer) {
        this.dependencyManager = dependencyManager;
        this.gameRepository = dependencyManager.getGameRepository();
        this.version = version;
        this.installer = installer;
        this.selfVersion = selfVersion;

        setSignificance(TaskSignificance.MAJOR);
    }

    private static String replaceTokens(Map<String, String> tokens, String value) {
        StringBuilder buf = new StringBuilder();
        for (int x = 0; x < value.length(); x++) {
            char c = value.charAt(x);
            if (c == '\\') {
                if (x == value.length() - 1)
                    throw new IllegalArgumentException("Illegal pattern (Bad escape): " + value);
                buf.append(value.charAt(++x));
            } else if (c == '{' || c == '\'') {
                StringBuilder key = new StringBuilder();
                for (int y = x + 1; y <= value.length(); y++) {
                    if (y == value.length())
                        throw new IllegalArgumentException("Illegal pattern (Unclosed " + c + "): " + value);
                    char d = value.charAt(y);
                    if (d == '\\') {
                        if (y == value.length() - 1)
                            throw new IllegalArgumentException("Illegal pattern (Bad escape): " + value);
                        key.append(value.charAt(++y));
                    } else {
                        if (c == '{' && d == '}') {
                            x = y;
                            break;
                        }
                        if (c == '\'' && d == '\'') {
                            x = y;
                            break;
                        }
                        key.append(d);
                    }
                }
                if (c == '\'') {
                    buf.append(key);
                } else {
                    if (!tokens.containsKey(key.toString()))
                        throw new IllegalArgumentException("Illegal pattern: " + value + " Missing Key: " + key);
                    buf.append(tokens.get(key.toString()));
                }
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    private <E extends Exception> String parseLiteral(String literal, Map<String, String> var, ExceptionalFunction<String, String, E> plainConverter) throws E {
        if (StringUtils.isSurrounded(literal, "{", "}"))
            return var.get(StringUtils.removeSurrounding(literal, "{", "}"));
        else if (StringUtils.isSurrounded(literal, "'", "'"))
            return StringUtils.removeSurrounding(literal, "'");
        else if (StringUtils.isSurrounded(literal, "[", "]"))
            return gameRepository.getArtifactFile(version, Artifact.fromDescriptor(StringUtils.removeSurrounding(literal, "[", "]"))).toString();
        else
            return plainConverter.apply(replaceTokens(var, literal));
    }

    private String parseLiteral(String literal, Map<String, String> var) {
        return parseLiteral(literal, var, ExceptionalFunction.identity());
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
    public boolean doPreExecute() {
        return true;
    }

    @Override
    public void preExecute() throws Exception {
        try (FileSystem fs = CompressingUtils.createReadOnlyZipFileSystem(installer)) {
            profile = fromNonNullJson(FileUtils.readText(fs.getPath("install_profile.json")), ForgeNewInstallProfile.class);
            processors = profile.getProcessors();
            forgeVersion = fromNonNullJson(FileUtils.readText(fs.getPath(profile.getJson())), Version.class);

            for (Library library : profile.getLibraries()) {
                Path file = fs.getPath("maven").resolve(library.getPath());
                if (Files.exists(file)) {
                    Path dest = gameRepository.getLibraryFile(version, library).toPath();
                    FileUtils.copyFile(file, dest);
                }
            }

            if (profile.getPath().isPresent()) {
                Path mainJar = profile.getPath().get().getPath(fs.getPath("maven"));
                if (Files.exists(mainJar)) {
                    Path dest = gameRepository.getArtifactFile(version, profile.getPath().get());
                    FileUtils.copyFile(mainJar, dest);
                }
            }
        } catch (ZipException ex) {
            throw new ArtifactMalformedException("Malformed forge installer file", ex);
        }

        dependents.add(new GameLibrariesTask(dependencyManager, version, true, profile.getLibraries()));
    }

    private Map<String, String> parseOptions(List<String> args, Map<String, String> vars) {
        Map<String, String> options = new LinkedHashMap<>();
        String optionName = null;
        for (String arg : args) {
            if (arg.startsWith("--")) {
                if (optionName != null) {
                    options.put(optionName, "");
                }
                optionName = arg.substring(2);
            } else {
                if (optionName == null) {
                    // ignore
                } else {
                    options.put(optionName, parseLiteral(arg, vars));
                    optionName = null;
                }
            }
        }
        if (optionName != null) {
            options.put(optionName, "");
        }
        return options;
    }

    private Task<?> patchDownloadMojangMappingsTask(ForgeNewInstallProfile.Processor processor, Map<String, String> vars) {
        Map<String, String> options = parseOptions(processor.getArgs(), vars);
        if (!"DOWNLOAD_MOJMAPS".equals(options.get("task")) || !"client".equals(options.get("side")))
            return null;
        String version = options.get("version");
        String output = options.get("output");
        if (version == null || output == null)
            return null;

        LOG.info("Patching DOWNLOAD_MOJMAPS task");
        return new VersionJsonDownloadTask(version, dependencyManager)
                .thenComposeAsync(json -> {
                    DownloadInfo mappings = fromNonNullJson(json, Version.class)
                            .getDownloads().get(DownloadType.CLIENT_MAPPINGS);
                    if (mappings == null) {
                        throw new Exception("client_mappings download info not found");
                    }

                    List<URL> mappingsUrl = dependencyManager.getDownloadProvider()
                            .injectURLWithCandidates(mappings.getUrl());
                    FileDownloadTask mappingsTask = new FileDownloadTask(
                            mappingsUrl,
                            new File(output),
                            FileDownloadTask.IntegrityCheck.of("SHA-1", mappings.getSha1()));
                    mappingsTask.setCaching(true);
                    mappingsTask.setCacheRepository(dependencyManager.getCacheRepository());
                    return mappingsTask;
                });
    }

    private Task<?> createProcessorTask(ForgeNewInstallProfile.Processor processor, Map<String, String> vars) {
        Task<?> task = patchDownloadMojangMappingsTask(processor, vars);
        if (task == null) {
            task = new ProcessorTask(processor, vars);
        }
        task.onDone().register(
                () -> updateProgress(processorDoneCount.incrementAndGet(), processors.size()));
        return task;
    }

    @Override
    public void execute() throws Exception {
        tempDir = Files.createTempDirectory("forge_installer");

        Map<String, String> vars = new HashMap<>();

        try (FileSystem fs = CompressingUtils.createReadOnlyZipFileSystem(installer)) {
            for (Map.Entry<String, String> entry : profile.getData().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                vars.put(key, parseLiteral(value,
                        Collections.emptyMap(),
                        str -> {
                            Path dest = Files.createTempFile(tempDir, null, null);
                            FileUtils.copyFile(fs.getPath(str), dest);
                            return dest.toString();
                        }));
            }
        } catch (ZipException ex) {
            throw new ArtifactMalformedException("Malformed forge installer file", ex);
        }

        vars.put("SIDE", "client");
        vars.put("MINECRAFT_JAR", gameRepository.getVersionJar(version).getAbsolutePath());
        vars.put("MINECRAFT_VERSION", gameRepository.getVersionJar(version).getAbsolutePath());
        vars.put("ROOT", gameRepository.getBaseDirectory().getAbsolutePath());
        vars.put("INSTALLER", installer.toAbsolutePath().toString());
        vars.put("LIBRARY_DIR", gameRepository.getLibrariesDirectory(version).getAbsolutePath());

        updateProgress(0, processors.size());

        Task<?> processorsTask = Task.runSequentially(
                processors.stream()
                        .map(processor -> createProcessorTask(processor, vars))
                        .toArray(Task<?>[]::new));

        dependencies.add(
                processorsTask.thenComposeAsync(
                        dependencyManager.checkLibraryCompletionAsync(forgeVersion, true)));

        setResult(forgeVersion
                .setPriority(30000)
                .setId(LibraryAnalyzer.LibraryType.FORGE.getPatchId())
                .setVersion(selfVersion));
    }

    @Override
    public boolean doPostExecute() {
        return true;
    }

    @Override
    public void postExecute() throws Exception {
        FileUtils.deleteDirectory(tempDir.toFile());
    }
}
