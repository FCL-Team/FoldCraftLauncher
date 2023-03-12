package com.tungsten.fclcore.launch;

import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.game.Version;

import java.util.ArrayList;

public class InjectorMap {

    private final ArrayList<MapInfo> maps;

    public InjectorMap(ArrayList<MapInfo> maps) {
        this.maps = maps;
    }

    public ArrayList<MapInfo> getMaps() {
        return maps;
    }

    public static class MapInfo {
        private final String id;
        private final Argument argument;

        public MapInfo(String id, Argument argument) {
            this.id = id;
            this.argument = argument;
        }

        public String getId() {
            return id;
        }

        public Argument getArgument() {
            return argument;
        }
    }

    public static class Argument {
        private final String vanilla;
        private final String forge;
        private final String fabric;

        public Argument(String vanilla, String forge, String fabric) {
            this.vanilla = vanilla;
            this.forge = forge;
            this.fabric = fabric;
        }

        public String getVanilla() {
            return vanilla;
        }

        public String getForge() {
            return forge;
        }

        public String getFabric() {
            return fabric;
        }

        public String getArgument(Version version) {
            if (LibraryAnalyzer.analyze(version).has(LibraryAnalyzer.LibraryType.FORGE) || LibraryAnalyzer.analyze(version).has(LibraryAnalyzer.LibraryType.BOOTSTRAP_LAUNCHER))
                return getForge();
            else if (LibraryAnalyzer.analyze(version).has(LibraryAnalyzer.LibraryType.FABRIC) || LibraryAnalyzer.analyze(version).has(LibraryAnalyzer.LibraryType.QUILT))
                return getFabric();
            else
                return getVanilla();
        }
    }

}
