package com.tungsten.fcl.game;

import com.tungsten.fclcore.mod.ModpackManifest;
import com.tungsten.fclcore.mod.ModpackProvider;

public final class HMCLModpackManifest implements ModpackManifest {
    public static final HMCLModpackManifest INSTANCE = new HMCLModpackManifest();

    private HMCLModpackManifest() {}

    @Override
    public ModpackProvider getProvider() {
        return HMCLModpackProvider.INSTANCE;
    }
}
