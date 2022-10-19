package com.tungsten.fclcore.game;

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
