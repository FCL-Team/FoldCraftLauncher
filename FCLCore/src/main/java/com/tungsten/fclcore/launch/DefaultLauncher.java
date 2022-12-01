package com.tungsten.fclcore.launch;

import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Pair.pair;

import android.content.Context;

import com.tungsten.fclauncher.FCLConfig;
import com.tungsten.fclauncher.FCLauncher;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.utils.Architecture;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclauncher.FCLPath;
import com.tungsten.fclcore.game.Argument;
import com.tungsten.fclcore.game.Arguments;
import com.tungsten.fclcore.game.GameRepository;
import com.tungsten.fclcore.game.LaunchOptions;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.UUIDTypeAdapter;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.IOUtils;
import com.tungsten.fclcore.util.platform.CommandBuilder;
import com.tungsten.fclcore.util.versioning.VersionNumber;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

public class DefaultLauncher extends Launcher {

    public DefaultLauncher(Context context, GameRepository repository, Version version, AuthInfo authInfo, LaunchOptions options) {
        super(context, repository, version, authInfo, options);
    }

    private CommandBuilder generateCommandLine() throws IOException {
        CommandBuilder res = new CommandBuilder();

        switch (options.getProcessPriority()) {
            case HIGH:
                res.add("nice", "-n", "-5");
                break;
            case NORMAL:
                // do nothing
                break;
            case LOW:
                res.add("nice", "-n", "5");
                break;
        }

        res.addAllWithoutParsing(options.getJavaArguments());

        // JVM Args
        if (!options.isNoGeneratedJVMArgs()) {
            appendJvmArgs(res);

            res.addDefault("-Dminecraft.client.jar=", repository.getVersionJar(version).toString());

            // Using G1GC with its settings by default
            if (options.getJava().getVersion() >= 8) {
                boolean addG1Args = true;
                for (String javaArg : options.getJavaArguments()) {
                    if ("-XX:-UseG1GC".equals(javaArg) || (javaArg.startsWith("-XX:+Use") && javaArg.endsWith("GC"))) {
                        addG1Args = false;
                        break;
                    }
                }
                if (addG1Args) {
                    res.addUnstableDefault("UnlockExperimentalVMOptions", true);
                    res.addUnstableDefault("UseG1GC", true);
                    res.addUnstableDefault("G1NewSizePercent", "20");
                    res.addUnstableDefault("G1ReservePercent", "20");
                    res.addUnstableDefault("MaxGCPauseMillis", "50");
                    res.addUnstableDefault("G1HeapRegionSize", "16m");
                }
            }

            if (options.getMetaspace() != null && options.getMetaspace() > 0)
                res.addDefault("-XX:MetaspaceSize=", options.getMetaspace() + "m");

            res.addUnstableDefault("UseAdaptiveSizePolicy", false);
            res.addUnstableDefault("OmitStackTraceInFastThrow", false);
            res.addUnstableDefault("DontCompileHugeMethods", false);
            res.addDefault("-Xmn", "128m");

            // As 32-bit JVM allocate 320KB for stack by default rather than 64-bit version allocating 1MB,
            // causing Minecraft 1.13 crashed accounting for java.lang.StackOverflowError.
            if (Architecture.is32BitsDevice()) {
                res.addDefault("-Xss", "1m");
            }

            if (options.getMaxMemory() != null && options.getMaxMemory() > 0)
                res.addDefault("-Xmx", options.getMaxMemory() + "m");

            if (options.getMinMemory() != null && options.getMinMemory() > 0)
                res.addDefault("-Xms", options.getMinMemory() + "m");

            res.addDefault("-Dfml.ignoreInvalidMinecraftCertificates=", "true");
            res.addDefault("-Dfml.ignorePatchDiscrepancies=", "true");
        }

        // Fix RCE vulnerability of log4j2
        res.addDefault("-Djava.rmi.server.useCodebaseOnly=", "true");
        res.addDefault("-Dcom.sun.jndi.rmi.object.trustURLCodebase=", "false");
        res.addDefault("-Dcom.sun.jndi.cosnaming.object.trustURLCodebase=", "false");

        String formatMsgNoLookups = res.addDefault("-Dlog4j2.formatMsgNoLookups=", "true");
        if (!"-Dlog4j2.formatMsgNoLookups=false".equals(formatMsgNoLookups) && isUsingLog4j()) {
            res.addDefault("-Dlog4j.configurationFile=", getLog4jConfigurationFile().getAbsolutePath());
        }

        res.addDefault("-Dos.name=", "Linux");
        res.addDefault("-Dlwjgl.platform=", "FCL");
        res.addDefault("-Dorg.lwjgl.opengl.libname=", "${gl_lib_name}");

        List<String> classpath = repository.getClasspath(version);

        File jar = repository.getVersionJar(version);
        if (!jar.exists() || !jar.isFile())
            throw new IOException("Minecraft jar does not exist");
        classpath.add(jar.getAbsolutePath());

        // Provided Minecraft arguments
        Path gameAssets = repository.getActualAssetDirectory(version.getId(), version.getAssetIndex().getId());
        Map<String, String> configuration = getConfigurations();
        configuration.put("${classpath}", String.join(File.pathSeparator, classpath));
        configuration.put("${game_assets}", gameAssets.toAbsolutePath().toString());
        configuration.put("${assets_root}", gameAssets.toAbsolutePath().toString());
        configuration.put("${natives_directory}", "${natives_directory}");

        res.addAll(Arguments.parseArguments(version.getArguments().map(Arguments::getJvm).orElseGet(this::getDefaultJVMArguments), configuration));
        Arguments argumentsFromAuthInfo = authInfo.getLaunchArguments(options);
        if (argumentsFromAuthInfo != null && argumentsFromAuthInfo.getJvm() != null && !argumentsFromAuthInfo.getJvm().isEmpty())
            res.addAll(Arguments.parseArguments(argumentsFromAuthInfo.getJvm(), configuration));

        for (String javaAgent : options.getJavaAgents()) {
            res.add("-javaagent:" + javaAgent);
        }

        res.add(version.getMainClass());

        res.addAll(Arguments.parseStringArguments(version.getMinecraftArguments().map(StringUtils::tokenize).orElseGet(ArrayList::new), configuration));

        Map<String, Boolean> features = getFeatures();
        version.getArguments().map(Arguments::getGame).ifPresent(arguments -> res.addAll(Arguments.parseArguments(arguments, configuration, features)));
        if (version.getMinecraftArguments().isPresent()) {
            res.addAll(Arguments.parseArguments(this.getDefaultGameArguments(), configuration, features));
        }
        if (argumentsFromAuthInfo != null && argumentsFromAuthInfo.getGame() != null && !argumentsFromAuthInfo.getGame().isEmpty())
            res.addAll(Arguments.parseArguments(argumentsFromAuthInfo.getGame(), configuration, features));

        if (StringUtils.isNotBlank(options.getServerIp())) {
            String[] args = options.getServerIp().split(":");
            res.add("--server");
            res.add(args[0]);
            res.add("--port");
            res.add(args.length > 1 ? args[1] : "25565");
        }

        if (options.isFullscreen())
            res.add("--fullscreen");

        res.addAllWithoutParsing(Arguments.parseStringArguments(options.getGameArguments(), configuration));

        res.removeIf(it -> getForbiddens().containsKey(it) && getForbiddens().get(it).get());
        return res;
    }

    public Map<String, Boolean> getFeatures() {
        return Collections.singletonMap(
                "has_custom_resolution",
                options.getHeight() != null && options.getHeight() != 0 && options.getWidth() != null && options.getWidth() != 0
        );
    }

    private final Map<String, Supplier<Boolean>> forbiddens = mapOf(
            pair("-Xincgc", () -> options.getJava().getVersion() >= 9)
    );

    protected Map<String, Supplier<Boolean>> getForbiddens() {
        return forbiddens;
    }

    protected List<Argument> getDefaultJVMArguments() {
        return Arguments.DEFAULT_JVM_ARGUMENTS;
    }

    protected List<Argument> getDefaultGameArguments() {
        return Arguments.DEFAULT_GAME_ARGUMENTS;
    }

    /**
     * Do something here.
     * i.e.
     * -Dminecraft.launcher.version=&lt;Your launcher name&gt;
     * -Dminecraft.launcher.brand=&lt;Your launcher version&gt;
     * -Dlog4j.configurationFile=&lt;Your custom log4j configuration&gt;
     */
    protected void appendJvmArgs(CommandBuilder result) {
    }

    private boolean isUsingLog4j() {
        return VersionNumber.VERSION_COMPARATOR.compare(repository.getGameVersion(version).orElse("1.7"), "1.7") >= 0;
    }

    public File getLog4jConfigurationFile() {
        return new File(repository.getVersionRoot(version.getId()), "log4j2.xml");
    }

    public void extractLog4jConfigurationFile() throws IOException {
        File targetFile = getLog4jConfigurationFile();
        InputStream source;
        if (VersionNumber.VERSION_COMPARATOR.compare(repository.getGameVersion(version).orElse("0.0"), "1.12") < 0) {
            source = DefaultLauncher.class.getResourceAsStream("/assets/game/log4j2-1.7.xml");
        } else {
            source = DefaultLauncher.class.getResourceAsStream("/assets/game/log4j2-1.12.xml");
        }

        try (InputStream input = source; OutputStream output = new FileOutputStream(targetFile)) {
            IOUtils.copyTo(input, output);
        }
    }

    protected Map<String, String> getConfigurations() {
        return mapOf(
                // defined by Minecraft official launcher
                pair("${auth_player_name}", authInfo.getUsername()),
                pair("${auth_session}", authInfo.getAccessToken()),
                pair("${auth_access_token}", authInfo.getAccessToken()),
                pair("${auth_uuid}", UUIDTypeAdapter.fromUUID(authInfo.getUUID())),
                pair("${version_name}", Optional.ofNullable(options.getVersionName()).orElse(version.getId())),
                pair("${profile_name}", Optional.ofNullable(options.getProfileName()).orElse("Minecraft")),
                pair("${version_type}", Optional.ofNullable(options.getVersionType()).orElse(version.getType().getId())),
                pair("${game_directory}", repository.getRunDirectory(version.getId()).getAbsolutePath()),
                pair("${user_type}", "mojang"),
                pair("${assets_index_name}", version.getAssetIndex().getId()),
                pair("${user_properties}", authInfo.getUserProperties()),
                pair("${resolution_width}", options.getWidth().toString()),
                pair("${resolution_height}", options.getHeight().toString()),
                pair("${library_directory}", repository.getLibrariesDirectory(version).getAbsolutePath()),
                pair("${classpath_separator}", File.pathSeparator),
                pair("${primary_jar}", repository.getVersionJar(version).getAbsolutePath()),
                pair("${language}", Locale.getDefault().toString()),

                // file_separator is used in -DignoreList
                pair("${file_separator}", File.pathSeparator),
                pair("${primary_jar_name}", FileUtils.getName(repository.getVersionJar(version).toPath()))
        );
    }

    @Override
    public FCLBridge launch() throws IOException, InterruptedException {
        final CommandBuilder command = generateCommandLine();

        // To guarantee that when failed to generate launch command line, we will not call pre-launch command
        List<String> rawCommandLine = command.asList();

        if (rawCommandLine.stream().anyMatch(StringUtils::isBlank)) {
            throw new IllegalStateException("Illegal command line " + rawCommandLine);
        }

        if (isUsingLog4j()) {
            extractLog4jConfigurationFile();
        }

        String[] finalArgs = rawCommandLine.toArray(new String[0]);

        FCLConfig config = new FCLConfig(context,
                FCLPath.LOG_DIR,
                options.getJava().getVersion() == 8 ? FCLPath.JAVA_8_PATH : FCLPath.JAVA_17_PATH,
                repository.getRunDirectory(version.getId()).getAbsolutePath(),
                options.getRenderer(),
                finalArgs);
        return FCLauncher.launchMinecraft(config);
    }
}
