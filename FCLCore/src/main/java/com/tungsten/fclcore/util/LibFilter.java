package com.tungsten.fclcore.util;

import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.Version;

import java.util.ArrayList;
import java.util.List;

public class LibFilter {

    public static Version filter(Version version) {
        return version.setLibraries(filterLibs(version.getLibraries()));
    }

    public static List<Library> filterLibs(List<Library> libraries) {
        ArrayList<Library> newLibraries = new ArrayList<>();
        for (Library library : libraries) {
            if (!library.isNative() && !library.getName().contains("org.lwjgl") && !library.getName().contains("platform")) {
                if (library.getArtifactId().equals("asm-all") && library.getVersion().equals("4.1")) {
                    newLibraries.add(library.setVersion("5.2"));
                } else {
                    newLibraries.add(library);
                }
            }
        }
        return newLibraries;
    }

}
