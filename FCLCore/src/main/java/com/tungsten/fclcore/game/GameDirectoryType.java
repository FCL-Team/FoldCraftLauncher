package com.tungsten.fclcore.game;

/**
 * Determines where game runs in and game files such as mods.
 */
public enum GameDirectoryType {
    /**
     * .minecraft
     */
    ROOT_FOLDER,
    /**
     * .minecraft/versions/&lt;version name&gt;
     */
    VERSION_FOLDER,
    /**
     * user customized directory.
     */
    CUSTOM
}
