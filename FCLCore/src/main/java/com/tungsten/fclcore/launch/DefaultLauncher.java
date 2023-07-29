package com.tungsten.fclcore.launch;

import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Logging.LOG;
import static com.tungsten.fclcore.util.Pair.pair;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.tungsten.fclauncher.FCLConfig;
import com.tungsten.fclauncher.FCLauncher;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.utils.Architecture;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.game.Argument;
import com.tungsten.fclcore.game.Arguments;
import com.tungsten.fclcore.game.GameRepository;
import com.tungsten.fclcore.game.JavaVersion;
import com.tungsten.fclcore.game.LaunchOptions;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.UUIDTypeAdapter;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.IOUtils;
import com.tungsten.fclcore.util.platform.CommandBuilder;
import com.tungsten.fclcore.util.platform.OperatingSystem;
import com.tungsten.fclcore.util.versioning.VersionNumber;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Level;

public class DefaultLauncher extends Launcher {

    public DefaultLauncher(Context context, GameRepository repository, Version version, AuthInfo authInfo, LaunchOptions options) {
        super(context, repository, version, authInfo, options);
    }

    private CommandBuilder generateCommandLine() throws IOException {
        CommandBuilder res = new CommandBuilder();

        getCacioJavaArgs(res, version, options);

        res.addAllWithoutParsing(options.getOverrideJavaArguments());

        if (options.getMaxMemory() != null && options.getMaxMemory() > 0)
            res.addDefault("-Xmx", options.getMaxMemory() + "m");

        if (options.getMinMemory() != null && options.getMinMemory() > 0
                && (options.getMaxMemory() == null || options.getMinMemory() <= options.getMaxMemory()))
            res.addDefault("-Xms", options.getMinMemory() + "m");

        if (options.getMetaspace() != null && options.getMetaspace() > 0)
            res.addDefault("-XX:MetaspaceSize=", options.getMetaspace() + "m");

        res.addAllDefaultWithoutParsing(options.getJavaArguments());

        Charset encoding = OperatingSystem.NATIVE_CHARSET;
        String fileEncoding = res.addDefault("-Dfile.encoding=", encoding.name());
        if (fileEncoding != null && !"-Dfile.encoding=COMPAT".equals(fileEncoding)) {
            try {
                encoding = Charset.forName(fileEncoding.substring("-Dfile.encoding=".length()));
            } catch (Throwable ex) {
                LOG.log(Level.WARNING, "Bad file encoding", ex);
            }
        }
        res.addDefault("-Dsun.stdout.encoding=", encoding.name());
        res.addDefault("-Dsun.stderr.encoding=", encoding.name());

        // Fix RCE vulnerability of log4j2
        res.addDefault("-Djava.rmi.server.useCodebaseOnly=", "true");
        res.addDefault("-Dcom.sun.jndi.rmi.object.trustURLCodebase=", "false");
        res.addDefault("-Dcom.sun.jndi.cosnaming.object.trustURLCodebase=", "false");

        String formatMsgNoLookups = res.addDefault("-Dlog4j2.formatMsgNoLookups=", "true");
        if (!"-Dlog4j2.formatMsgNoLookups=false".equals(formatMsgNoLookups) && isUsingLog4j()) {
            res.addDefault("-Dlog4j.configurationFile=", getLog4jConfigurationFile().getAbsolutePath());
        }

        // Default JVM Args
        appendJvmArgs(res);

        res.addDefault("-Dminecraft.client.jar=", repository.getVersionJar(version).toString());

        // Using G1GC with its settings by default
        if (options.getJava().getVersion() >= 8
                && res.noneMatch(arg -> "-XX:-UseG1GC".equals(arg) || (arg.startsWith("-XX:+Use") && arg.endsWith("GC")))) {
            res.addUnstableDefault("UnlockExperimentalVMOptions", true);
            res.addUnstableDefault("UseG1GC", true);
            res.addUnstableDefault("G1NewSizePercent", "20");
            res.addUnstableDefault("G1ReservePercent", "20");
            res.addUnstableDefault("MaxGCPauseMillis", "50");
            res.addUnstableDefault("G1HeapRegionSize", "32m");
        }

        res.addUnstableDefault("UseAdaptiveSizePolicy", false);
        res.addUnstableDefault("OmitStackTraceInFastThrow", false);
        res.addUnstableDefault("DontCompileHugeMethods", false);

        // As 32-bit JVM allocate 320KB for stack by default rather than 64-bit version allocating 1MB,
        // causing Minecraft 1.13 crashed accounting for java.lang.StackOverflowError.
        if (Architecture.is32BitsDevice()) {
            res.addDefault("-Xss", "1m");
        }

        res.addDefault("-Dfml.ignoreInvalidMinecraftCertificates=", "true");
        res.addDefault("-Dfml.ignorePatchDiscrepancies=", "true");

        // LWJGL debug mode
        // res.addDefault("-Dorg.lwjgl.util.Debug=", "true");
        // res.addDefault("-Dorg.lwjgl.util.DebugLoader=", "true");
        // res.addDefault("-Dorg.lwjgl.util.DebugFunctions=", "true");

        // FCL specific args
        JavaVersion javaVersion = options.getJava().getId() == 0 ? JavaVersion.getSuitableJavaVersion(version) : options.getJava();
        if (javaVersion.getVersion() == JavaVersion.JAVA_VERSION_17) {
            res.addDefault("-Dext.net.resolvPath=", FCLPath.JAVA_17_PATH + "/resolv.conf");
        }

        res.addDefault("-Djava.io.tmpdir=", FCLPath.CACHE_DIR);
        res.addDefault("-Dos.name=", "Linux");
        res.addDefault("-Dlwjgl.platform=", "FCL");
        res.addDefault("-Dorg.lwjgl.opengl.libname=", "${gl_lib_name}");
        res.addDefault("-Dfml.earlyprogresswindow=", "false");
        res.addDefault("-Dwindow.width=", options.getWidth() + "");
        res.addDefault("-Dwindow.height=", options.getHeight() + "");
        res.addDefault("-Duser.home=", options.getGameDir().getAbsolutePath());
        res.addDefault("-Duser.language=", System.getProperty("user.language"));
        res.addDefault("-Duser.timezone=", TimeZone.getDefault().getID());

        if (getInjectorArg() != null && options.isBeGesture()) {
            res.addDefault("-Dfcl.injector=", getInjectorArg());
        }

        // Fix 1.7.2 Forge
        if (repository.getGameVersion(version).isPresent() && repository.getGameVersion(version).get().equals("1.7.2")) {
            res.addDefault("-Dsort.patch=", "true");
        }

        // Fix 1.16.x multiplayer
        if (repository.getGameVersion(version).isPresent() && repository.getGameVersion(version).get().startsWith("1.16")) {
            res.add("-javaagent:" + FCLPath.MULTIPLAYER_FIX_PATH);
        }

        Set<String> classpath = repository.getClasspath(version);

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

        res.addAllWithoutParsing(Arguments.parseStringArguments(options.getGameArguments(), configuration));

        res.removeIf(it -> getForbiddens().containsKey(it) && getForbiddens().get(it).get());
        return res;
    }

    public static void getCacioJavaArgs(CommandBuilder res, Version version, LaunchOptions options) {
        JavaVersion javaVersion;
        if (options.getJava().getId() == 0) {
            javaVersion = JavaVersion.getSuitableJavaVersion(version);
        } else {
            javaVersion = options.getJava();
        }
        boolean isJava8 = javaVersion.getVersion() == JavaVersion.JAVA_VERSION_8;

        res.addDefault("-Djava.awt.headless=", "false");
        res.addDefault("-Dcacio.managed.screensize=", options.getWidth() + "x" + options.getHeight());
        res.addDefault("-Dcacio.font.fontmanager=", "sun.awt.X11FontManager");
        res.addDefault("-Dcacio.font.fontscaler=", "sun.font.FreetypeFontScaler");
        res.addDefault("-Dswing.defaultlaf=", "javax.swing.plaf.metal.MetalLookAndFeel");
        if (isJava8) {
            res.addDefault("-Dawt.toolkit=", "net.java.openjdk.cacio.ctc.CTCToolkit");
            res.addDefault("-Djava.awt.graphicsenv=", "net.java.openjdk.cacio.ctc.CTCGraphicsEnvironment");
        } else {
            res.addDefault("-Dawt.toolkit=", "com.github.caciocavallosilano.cacio.ctc.CTCToolkit");
            res.addDefault("-Djava.awt.graphicsenv=", "com.github.caciocavallosilano.cacio.ctc.CTCGraphicsEnvironment");
            res.addDefault("-Djava.system.class.loader=", "com.github.caciocavallosilano.cacio.ctc.CTCPreloadClassLoader");

            res.add("--add-exports=java.desktop/java.awt=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/java.awt.peer=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/sun.awt.image=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/sun.java2d=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/java.awt.dnd.peer=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/sun.awt=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/sun.awt.event=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/sun.awt.datatransfer=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/sun.font=ALL-UNNAMED");
            res.add("--add-exports=java.base/sun.security.action=ALL-UNNAMED");
            res.add("--add-opens=java.base/java.util=ALL-UNNAMED");
            res.add("--add-opens=java.desktop/java.awt=ALL-UNNAMED");
            res.add("--add-opens=java.desktop/sun.font=ALL-UNNAMED");
            res.add("--add-opens=java.desktop/sun.java2d=ALL-UNNAMED");
            res.add("--add-opens=java.base/java.lang.reflect=ALL-UNNAMED");
            res.add("--add-opens=java.base/java.net=ALL-UNNAMED");
        }

        StringBuilder cacioClasspath = new StringBuilder();
        cacioClasspath.append("-Xbootclasspath/").append(isJava8 ? "p" : "a");
        File cacioDir = new File(isJava8 ? FCLPath.CACIOCAVALLO_8_DIR : FCLPath.CACIOCAVALLO_17_DIR);
        if (cacioDir.exists() && cacioDir.isDirectory()) {
            for (File file : Objects.requireNonNull(cacioDir.listFiles())) {
                if (file.getName().endsWith(".jar")) {
                    cacioClasspath.append(":").append(file.getAbsolutePath());
                }
            }
        }
        res.add(cacioClasspath.toString());
    }

    public String getInjectorArg() {
        try {
            String map = IOUtils.readFullyAsString(DefaultLauncher.class.getResourceAsStream("/assets/map.json"));
            InjectorMap injectorMap = new GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .fromJson(map, InjectorMap.class);
            Optional<InjectorMap.MapInfo> mapInfo = injectorMap.getMaps().stream()
                    .filter(it -> {
                        String versionTypeId = version.getAssetIndex().getId();
                        if (versionTypeId.equals("legacy") || versionTypeId.equals("pre-1.6")) {
                            versionTypeId = repository.getGameVersion(version).orElse("");
                        }
                        if (versionTypeId.equals("1.8")
                                && repository.getGameVersion(version).isPresent()
                                && (repository.getGameVersion(version).get().equals("1.8.8")
                                || repository.getGameVersion(version).get().equals("1.8.9"))) {
                            versionTypeId = "1.8.8";
                        }
                        if (versionTypeId.equals("1.9")
                                && repository.getGameVersion(version).isPresent()
                                && repository.getGameVersion(version).get().equals("1.9.4")) {
                            versionTypeId = "1.9.4";
                        }
                        return it.getId().equals(versionTypeId);
                    })
                    .findFirst();
            return mapInfo.map(it -> it.getArgument().getArgument(version)).orElse(null);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Failed to get game map", e);
            return null;
        }
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

        FCLConfig.Renderer renderer = options.getRenderer();
        FCLConfig config = new FCLConfig(context,
                FCLPath.LOG_DIR,
                options.getJava().getVersion() == 8 ? FCLPath.JAVA_8_PATH : FCLPath.JAVA_17_PATH,
                repository.getRunDirectory(version.getId()).getAbsolutePath(),
                renderer,
                finalArgs);
        return FCLauncher.launchMinecraft(config);
    }
}
