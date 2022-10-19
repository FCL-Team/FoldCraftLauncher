package com.tungsten.fclcore.game;

public class JavaVersion {

    public static final int JAVA_8 = 8;

    public static final int JAVA_17 = 17;

    private final int version;
    private final String versionName;
    private final int architecture;

    public JavaVersion(int version, String versionName, int architecture) {
        this.version = version;
        this.versionName = versionName;
        this.architecture = architecture;
    }

    public int getVersion() {
        return version;
    }

    public String getVersionName() {
        return versionName;
    }

    public int getArchitecture() {
        return architecture;
    }

    public static int getSuitableJavaVersion(Version version) {
        if (version.getJavaVersion() == null || version.getJavaVersion().getMajorVersion() == 8) {
            return JAVA_8;
        }
        return JAVA_17;
    }

    public static boolean checkJavaVersion(Version version, JavaVersion javaVersion) {
        if (version.getJavaVersion() == null || version.getJavaVersion().getMajorVersion() == 8) {
            return javaVersion.getVersion() == JAVA_8;
        }
        return javaVersion.getVersion() == JAVA_17;
    }
}
