package com.tungsten.fclcore.fakefx;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

public class PlatformUtil {

    // NOTE: since this class can be initialized by application code in some
    // cases, we must encapsulate all calls to System.getProperty("...") in
    // a doPrivileged block except for standard JVM properties such as
    // os.name, os.version, os.arch, java.vm.name, etc.

    private static final String os = System.getProperty("os.name");
    private static final String version = System.getProperty("os.version");
    private static final boolean embedded;
    private static final String embeddedType;
    private static final boolean useEGL;
    private static final boolean doEGLCompositing;
    // a property used to denote a non-default impl for this host
    private static String javafxPlatform;

    static {
        @SuppressWarnings("removal")
        String str1 = AccessController.doPrivileged((PrivilegedAction<String>) () -> System.getProperty("javafx.platform"));
        javafxPlatform = str1;

        loadProperties();

        @SuppressWarnings("removal")
        boolean bool1 = AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> Boolean.getBoolean("com.sun.javafx.isEmbedded"));
        embedded = bool1;

        @SuppressWarnings("removal")
        String str2 = AccessController.doPrivileged((PrivilegedAction<String>) () -> System.getProperty("glass.platform", "").toLowerCase(Locale.ROOT));
        embeddedType = str2;

        @SuppressWarnings("removal")
        boolean bool2 = AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> Boolean.getBoolean("use.egl"));
        useEGL = bool2;

        if (useEGL) {
            @SuppressWarnings("removal")
            boolean bool3 = AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> Boolean.getBoolean("doNativeComposite"));
            doEGLCompositing = bool3;
        } else
            doEGLCompositing = false;
    }

    private static final boolean ANDROID = "android".equals(javafxPlatform) || "Dalvik".equals(System.getProperty("java.vm.name"));
    private static final boolean WINDOWS = os.startsWith("Windows");
    private static final boolean WINDOWS_VISTA_OR_LATER = WINDOWS && versionNumberGreaterThanOrEqualTo(6.0f);
    private static final boolean WINDOWS_7_OR_LATER = WINDOWS && versionNumberGreaterThanOrEqualTo(6.1f);
    private static final boolean MAC = os.startsWith("Mac");
    private static final boolean LINUX = os.startsWith("Linux") && !ANDROID;
    private static final boolean SOLARIS = os.startsWith("SunOS");
    private static final boolean IOS = os.startsWith("iOS");
    private static final boolean STATIC_BUILD = "Substrate VM".equals(System.getProperty("java.vm.name"));

    /**
     * Utility method used to determine whether the version number as
     * reported by system properties is greater than or equal to a given
     * value.
     *
     * @param value The value to test against.
     * @return false if the version number cannot be parsed as a float,
     *         otherwise the comparison against value.
     */
    private static boolean versionNumberGreaterThanOrEqualTo(float value) {
        try {
            return Float.parseFloat(version) >= value;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the operating system is a form of Windows.
     */
    public static boolean isWindows(){
        return WINDOWS;
    }

    /**
     * Returns true if the operating system is at least Windows Vista(v6.0).
     */
    public static boolean isWinVistaOrLater(){
        return WINDOWS_VISTA_OR_LATER;
    }

    /**
     * Returns true if the operating system is at least Windows 7(v6.1).
     */
    public static boolean isWin7OrLater(){
        return WINDOWS_7_OR_LATER;
    }

    /**
     * Returns true if the operating system is a form of Mac OS.
     */
    public static boolean isMac(){
        return MAC;
    }

    /**
     * Returns true if the operating system is a form of Linux.
     */
    public static boolean isLinux(){
        return LINUX;
    }

    public static boolean useEGL() {
        return useEGL;
    }

    public static boolean useEGLWindowComposition() {
        return doEGLCompositing;
    }

    public static boolean useGLES2() {
        @SuppressWarnings("removal")
        String useGles2 =
                AccessController.doPrivileged((PrivilegedAction<String>) () -> System.getProperty("use.gles2"));
        if ("true".equals(useGles2))
            return true;
        else
            return false;
    }

    /**
     * Returns true if the operating system is a form of Unix, including Linux.
     */
    public static boolean isSolaris(){
        return SOLARIS;
    }

    /**
     * Returns true if the operating system is a form of Linux or Solaris
     */
    public static boolean isUnix(){
        return LINUX || SOLARIS;
    }

    /**
     * Returns true if the platform is embedded.
     */
    public static boolean isEmbedded() {
        return embedded;
    }

    /**
     * Returns a string with the embedded type - ie eglx11, eglfb, dfb or null.
     */
    public static String getEmbeddedType() {
        return embeddedType;
    }

    /**
     * Returns true if the operating system is iOS
     */
    public static boolean isIOS(){
        return IOS;
    }

    /**
     * Returns true if the current runtime is a statically linked image
     */
    public static boolean isStaticBuild(){
        return STATIC_BUILD;
    }

    private static void loadPropertiesFromFile(final File file) {
        Properties p = new Properties();
        try {
            InputStream in = new FileInputStream(file);
            p.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (javafxPlatform == null) {
            javafxPlatform = p.getProperty("javafx.platform");
        }
        String prefix = javafxPlatform + ".";
        int prefixLength = prefix.length();
        boolean foundPlatform = false;
        for (Object o : p.keySet()) {
            String key = (String) o;
            if (key.startsWith(prefix)) {
                foundPlatform = true;
                String systemKey = key.substring(prefixLength);
                if (System.getProperty(systemKey) == null) {
                    String value = p.getProperty(key);
                    System.setProperty(systemKey, value);
                }
            }
        }
        if (!foundPlatform) {
            System.err.println(
                    "Warning: No settings found for javafx.platform='"
                    + javafxPlatform + "'");
        }
    }

    /** Returns the directory containing the JavaFX runtime, or null
     * if the directory cannot be located
     */
    private static File getRTDir() {
        try {
            String theClassFile = "PlatformUtil.class";
            Class theClass = PlatformUtil.class;
            URL url = theClass.getResource(theClassFile);
            if (url == null) return null;
            String classUrlString = url.toString();
            if (!classUrlString.startsWith("jar:file:")
                    || classUrlString.indexOf('!') == -1) {
                return null;
            }
            // Strip out the "jar:" and everything after and including the "!"
            String s = classUrlString.substring(4,
                    classUrlString.lastIndexOf('!'));
            // Strip everything after the last "/" or "\" to get rid of the jar filename
            int lastIndexOfSlash = Math.max(
                    s.lastIndexOf('/'), s.lastIndexOf('\\'));
            return new File(new URL(s.substring(0, lastIndexOfSlash + 1)).getPath());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @SuppressWarnings("removal")
    private static void loadProperties() {
        final String vmname = System.getProperty("java.vm.name");
        final String arch = System.getProperty("os.arch");

        if (! (javafxPlatform != null ||
                (arch != null && arch.equals("arm")) ||
                (vmname != null && vmname.indexOf("Embedded") > 0))) {
            return;
        }
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            final File rtDir = getRTDir();
            final String propertyFilename = "javafx.platform.properties";
            File rtProperties = new File(rtDir, propertyFilename);
            // First look for javafx.platform.properties in the JavaFX runtime
            // Then in the installation directory of the JRE
            if (rtProperties.exists()) {
                loadPropertiesFromFile(rtProperties);
                return null;
            }
            String javaHome = System.getProperty("java.home");
            File javaHomeProperties = new File(javaHome,
                                               "lib" + File.separator
                                               + propertyFilename);
            if (javaHomeProperties.exists()) {
                loadPropertiesFromFile(javaHomeProperties);
                return null;
            }

            String javafxRuntimePath = System.getProperty("javafx.runtime.path");
            File javafxRuntimePathProperties = new File(javafxRuntimePath,
                                                     File.separator + propertyFilename);
            if (javafxRuntimePathProperties.exists()) {
               loadPropertiesFromFile(javafxRuntimePathProperties);
               return null;
            }
            return null;
        });
    }

    public static boolean isAndroid() {
       return ANDROID;
    }
}
