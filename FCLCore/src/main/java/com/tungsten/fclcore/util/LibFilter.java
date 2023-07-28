package com.tungsten.fclcore.util;

import static com.tungsten.fclcore.util.gson.JsonUtils.GSON;

import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.Version;

import java.util.ArrayList;
import java.util.List;

public class LibFilter {

    private static final String ASM_ALL_5_2_STRING =
            "{\n" +
            "  \"name\": \"org.ow2.asm:asm-all:5.2\"\n" +
            "}";

    private static final Library ASM_ALL_5_2 = GSON.fromJson(ASM_ALL_5_2_STRING, Library.class);

    public static Version filter(Version version) {
        return version.setLibraries(filterLibs(version.getLibraries()));
    }

    public static List<Library> filterLibs(List<Library> libraries) {
        ArrayList<Library> newLibraries = new ArrayList<>();
        for (Library library : libraries) {
            if (!library.isNative() && !library.getName().contains("org.lwjgl") && (library.getName().contains("jna") || !library.getName().contains("platform"))) {
                if (library.getArtifactId().equals("asm-all") && library.getVersion().equals("4.1")) {
                    newLibraries.add(ASM_ALL_5_2);
                } else {
                    newLibraries.add(library);
                }
            }
        }
        return newLibraries;
    }

}
