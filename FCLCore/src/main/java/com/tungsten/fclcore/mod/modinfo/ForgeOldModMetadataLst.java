package com.tungsten.fclcore.mod.modinfo;

import java.util.List;

/// @author Glavo
public class ForgeOldModMetadataLst {
    private int modListVersion;
    private List<ForgeOldModMetadata> modList;

    public int getModListVersion() {
        return modListVersion;
    }

    public void setModListVersion(int modListVersion) {
        this.modListVersion = modListVersion;
    }

    public List<ForgeOldModMetadata> getModList() {
        return modList;
    }

    public void setModList(List<ForgeOldModMetadata> modList) {
        this.modList = modList;
    }
}
