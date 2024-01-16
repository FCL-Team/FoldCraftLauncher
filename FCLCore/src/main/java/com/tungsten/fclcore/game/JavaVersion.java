package com.tungsten.fclcore.game;

import com.tungsten.fclauncher.utils.FCLPath;

public class JavaVersion {

    public static final int JAVA_VERSION_8 = 8;
    public static final int JAVA_VERSION_17 = 17;
    public static final int JAVA_VERSION_21 = 21;

    private final int id;
    private final boolean auto;
    private final int version;
    private final String versionName;

    public static final JavaVersion JAVA_AUTO = new JavaVersion(0, true, JAVA_VERSION_8, "Auto");
    public static final JavaVersion JAVA_8 = new JavaVersion(1, false, JAVA_VERSION_8, "1.8");
    public static final JavaVersion JAVA_17 = new JavaVersion(2, false, JAVA_VERSION_17, "17");
    public static final JavaVersion JAVA_21 = new JavaVersion(3, false, JAVA_VERSION_21, "21");

    public JavaVersion(int id, boolean auto, int version, String versionName) {
        this.id = id;
        this.auto = auto;
        this.version = version;
        this.versionName = versionName;
    }

    public int getId() {
        return id;
    }

    public boolean isAuto() {
        return auto;
    }

    public int getVersion() {
        return version;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getJavaPath(Version version) {
        JavaVersion javaVersion = auto ? getSuitableJavaVersion(version) : this;
        if (javaVersion.getVersion() == JAVA_VERSION_8) {
            return FCLPath.JAVA_8_PATH;
        } else if (javaVersion.getVersion() == JAVA_VERSION_17) {
            return FCLPath.JAVA_17_PATH;
        }
        return FCLPath.JAVA_21_PATH;
    }

    public static JavaVersion getJavaFromId(int id) {
        if (id == 0) {
            return JAVA_AUTO;
        } else if (id == 1) {
            return JAVA_8;
        } else if (id == 2) {
            return JAVA_17;
        }
        return JAVA_21;
    }

    public static JavaVersion getSuitableJavaVersion(Version version) {
        if (version.getJavaVersion() == null || version.getJavaVersion().getMajorVersion() == 8) {
            return JAVA_8;
        } else if (version.getJavaVersion().getMajorVersion() == 21) {
            return JAVA_21;
        }
        return JAVA_17;
    }

    public static boolean checkJavaVersion(Version version, JavaVersion javaVersion) {
        if (version.getJavaVersion() == null || version.getJavaVersion().getMajorVersion() == 8) {
            return javaVersion.getVersion() == JAVA_VERSION_8;
        } else if (version.getJavaVersion().getMajorVersion() == 21) {
            return javaVersion.getVersion() == JAVA_VERSION_21;
        }
        return javaVersion.getVersion() == JAVA_VERSION_17;
    }

}
