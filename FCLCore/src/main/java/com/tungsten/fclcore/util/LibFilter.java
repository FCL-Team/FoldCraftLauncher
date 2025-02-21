package com.tungsten.fclcore.util;

import static com.tungsten.fclcore.util.gson.JsonUtils.GSON;

import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.Version;

import java.util.ArrayList;
import java.util.List;

public class LibFilter {

    private static final String ASM_ALL_5_0_4_STRING = "{\n" +
            "      \"name\": \"org.ow2.asm:asm-all:5.0.4\",\n" +
            "      \"downloads\": {\n" +
            "        \"artifact\": {\n" +
            "          \"path\": \"org/ow2/asm/asm-all/5.0.4/asm-all-5.0.4.jar\",\n" +
            "          \"sha1\": \"e6244859997b3d4237a552669279780876228909\",\n" +
            "          \"url\": \"https://repo1.maven.org/maven2/org/ow2/asm/asm-all/5.0.4/asm-all-5.0.4.jar\"\n" +
            "        }\n" +
            "      }\n" +
            "    }";
    private static final String JNA_5_13_STRING = "{\n" +
            "      \"name\": \"net.java.dev.jna:jna:5.13.0\",\n" +
            "      \"downloads\": {\n" +
            "        \"artifact\": {\n" +
            "          \"path\": \"net/java/dev/jna/jna/5.13.0/jna-5.13.0.jar\",\n" +
            "          \"sha1\": \"1200e7ebeedbe0d10062093f32925a912020e747\",\n" +
            "          \"url\": \"https://repo1.maven.org/maven2/net/java/dev/jna/jna/5.13.0/jna-5.13.0.jar\"\n" +
            "        }\n" +
            "      }\n" +
            "    }";
    private static final String OSHI_6_3_STRING = "{\n" +
            "      \"name\": \"com.github.oshi:oshi-core:6.3.0\",\n" +
            "      \"downloads\": {\n" +
            "        \"artifact\": {\n" +
            "          \"path\": \"com/github/oshi/oshi-core/6.3.0/oshi-core-6.3.0.jar\",\n" +
            "          \"sha1\": \"9e98cf55be371cafdb9c70c35d04ec2a8c2b42ac\",\n" +
            "          \"url\": \"https://repo1.maven.org/maven2/com/github/oshi/oshi-core/6.3.0/oshi-core-6.3.0.jar\"\n" +
            "        }\n" +
            "      }\n" +
            "    }";

    private static final Library ASM_ALL_5_0_4 = GSON.fromJson(ASM_ALL_5_0_4_STRING, Library.class);
    private static final Library JNA_5_13 = GSON.fromJson(JNA_5_13_STRING, Library.class);
    private static final Library OSHI_6_3 = GSON.fromJson(OSHI_6_3_STRING, Library.class);

    public static Version filter(Version version) {
        return version.setLibraries(filterLibs(version.getLibraries()));
    }

    public static List<Library> filterLibs(List<Library> libraries) {
        ArrayList<Library> newLibraries = new ArrayList<>();
        for (Library library : libraries) {
            if (!library.getName().contains("org.lwjgl") && !library.getName().contains("jinput-platform") && !library.getName().contains("twitch-platform")) {
                String[] version = library.getName().split(":")[2].split("\\.");
                if (library.getArtifactId().equals("asm-all") && Integer.parseInt(version[0]) < 5) {
                    newLibraries.add(ASM_ALL_5_0_4);
                } else if (library.getName().startsWith("net.java.dev.jna:jna:")) {
                    if (Integer.parseInt(version[0]) >= 5 && Integer.parseInt(version[1]) >= 13) {
                        newLibraries.add(library);
                    } else {
                        newLibraries.add(JNA_5_13);
                    }
                } else if (library.getName().startsWith("com.github.oshi:oshi-core:")) {
                    if (Integer.parseInt(version[0]) != 6 || Integer.parseInt(version[1]) != 2) {
                        newLibraries.add(library);
                    } else {
                        newLibraries.add(OSHI_6_3);
                    }
                } else {
                    newLibraries.add(library);
                }
            }
        }
        return newLibraries;
    }

}