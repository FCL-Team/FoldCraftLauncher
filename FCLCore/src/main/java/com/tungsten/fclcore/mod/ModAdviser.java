package com.tungsten.fclcore.mod;

import com.tungsten.fclcore.util.Lang;

import java.io.File;
import java.util.List;

public interface ModAdviser {

    /**
     * Suggests the file should be displayed, hidden, or included by default.
     * @param fileName full path of fileName
     * @param isDirectory whether the path is directory
     * @return the suggestion to the file
     */
    ModSuggestion advise(String fileName, boolean isDirectory);

    enum ModSuggestion {
        SUGGESTED,
        NORMAL,
        HIDDEN
    }

    List<String> MODPACK_BLACK_LIST = Lang.immutableListOf(
            "regex:(.*?)\\.log",
            "usernamecache.json", "usercache.json", // Minecraft
            "launcher_profiles.json", "launcher.pack.lzma", // Old Minecraft Launcher
            "launcher_accounts.json", "launcher_cef_log.txt", "launcher_log.txt", "launcher_msa_credentials.bin", "launcher_settings.json", "launcher_ui_state.json", "realms_persistence.json", "webcache2", "treatment_tags.json", // New Minecraft Launcher
            "clientId.txt", "PCL.ini", // Plain Craft Launcher
            "backup", "pack.json", "launcher.jar", "cache", "modpack.cfg", // HMCL
            "manifest.json", "minecraftinstance.json", ".curseclient", // Curse
            ".fabric", ".mixin.out", // Fabric
            "jars", "logs", "versions", "assets", "libraries", "crash-reports", "NVIDIA", "AMD", "screenshots", "natives", "native", "$native", "server-resource-packs", // Minecraft
            "downloads", // Curse
            "asm", "backups", "TCNodeTracker", "CustomDISkins", "data", "CustomSkinLoader/caches" // Mods
    );

    List<String> MODPACK_SUGGESTED_BLACK_LIST = Lang.immutableListOf(
            "fonts", // BetterFonts
            "saves", "servers.dat", "options.txt", // Minecraft
            "blueprints" /* BuildCraft */,
            "optionsof.txt" /* OptiFine */,
            "journeymap" /* JourneyMap */,
            "optionsshaders.txt",
            "mods" + File.separator + "VoxelMods");

    static ModAdviser.ModSuggestion suggestMod(String fileName, boolean isDirectory) {
        if (match(MODPACK_BLACK_LIST, fileName, isDirectory))
            return ModAdviser.ModSuggestion.HIDDEN;
        if (match(MODPACK_SUGGESTED_BLACK_LIST, fileName, isDirectory))
            return ModAdviser.ModSuggestion.NORMAL;
        else
            return ModAdviser.ModSuggestion.SUGGESTED;
    }

    static boolean match(List<String> l, String fileName, boolean isDirectory) {
        for (String s : l)
            if (isDirectory) {
                if (fileName.startsWith(s + File.separator))
                    return true;
            } else {
                if (s.startsWith("regex:")) {
                    if (fileName.matches(s.substring("regex:".length())))
                        return true;
                } else {
                    if (fileName.equals(s))
                        return true;
                }
            }
        return false;
    }
}
