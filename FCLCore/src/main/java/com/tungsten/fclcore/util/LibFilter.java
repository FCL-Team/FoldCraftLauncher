package com.tungsten.fclcore.util;

import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.Version;

import java.util.ArrayList;

public class LibFilter {

    public static Version filter(Version version) {
        ArrayList<Library> newLibraries = new ArrayList<>();
        for (Library library : version.getLibraries()) {
            if (!library.isNative() && !library.getName().contains("net.java.jinput") && !library.getName().contains("org.lwjgl") && !library.getName().contains("platform")) {
                newLibraries.add(library);
            }
        }
        return version.setLibraries(newLibraries);
    }

}
