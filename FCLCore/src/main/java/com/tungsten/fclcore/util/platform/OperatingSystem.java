package com.tungsten.fclcore.util.platform;

public enum OperatingSystem {
    /**
     * Microsoft Windows.
     */
    WINDOWS("windows"),
    /**
     * Linux and Unix like OS, including Solaris.
     */
    LINUX("linux"),
    /**
     * Mac OS X.
     */
    OSX("osx"),
    /**
     * Unknown operating system.
     */
    UNKNOWN("universal");

    private final String checkedName;

    OperatingSystem(String checkedName) {
        this.checkedName = checkedName;
    }

    public String getCheckedName() {
        return checkedName;
    }

}