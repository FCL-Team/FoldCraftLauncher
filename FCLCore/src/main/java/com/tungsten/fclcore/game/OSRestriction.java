package com.tungsten.fclcore.game;

import com.tungsten.fclcore.util.platform.OperatingSystem;

public final class OSRestriction {

    private final OperatingSystem name;
    private final String version;
    private final String arch;

    public OSRestriction() {
        this(OperatingSystem.UNKNOWN);
    }

    public OSRestriction(OperatingSystem name) {
        this(name, null);
    }

    public OSRestriction(OperatingSystem name, String version) {
        this(name, version, null);
    }

    public OSRestriction(OperatingSystem name, String version, String arch) {
        this.name = name;
        this.version = version;
        this.arch = arch;
    }

    public OperatingSystem getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getArch() {
        return arch;
    }

    public boolean allow() {
        return false;
    }

}
