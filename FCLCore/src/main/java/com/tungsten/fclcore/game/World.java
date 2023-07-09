package com.tungsten.fclcore.game;

import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.LongTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.CompressingUtils;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.Unzipper;
import com.tungsten.fclcore.util.io.Zipper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class World {

    private final Path file;
    private String fileName;
    private String worldName;
    private String gameVersion;
    private long lastPlayed;

    public World(Path file) throws IOException {
        this.file = file;

        if (Files.isDirectory(file))
            loadFromDirectory();
        else if (Files.isRegularFile(file))
            loadFromZip();
        else
            throw new IOException("Path " + file + " cannot be recognized as a Minecraft world");
    }

    private void loadFromDirectory() throws IOException {
        fileName = FileUtils.getName(file);
        Path levelDat = file.resolve("level.dat");
        getWorldName(levelDat);
    }

    public Path getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public String getWorldName() {
        return worldName;
    }

    public Path getLevelDatFile() {
        return file.resolve("level.dat");
    }

    public long getLastPlayed() {
        return lastPlayed;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    private void loadFromZipImpl(Path root) throws IOException {
        Path levelDat = root.resolve("level.dat");
        if (!Files.exists(levelDat))
            throw new IOException("Not a valid world zip file since level.dat cannot be found.");

        getWorldName(levelDat);
    }

    private void loadFromZip() throws IOException {
        try (FileSystem fs = CompressingUtils.readonly(file).setAutoDetectEncoding(true).build()) {
            Path cur = fs.getPath("/level.dat");
            if (Files.isRegularFile(cur)) {
                fileName = FileUtils.getName(file);
                loadFromZipImpl(fs.getPath("/"));
                return;
            }

            try (Stream<Path> stream = Files.list(fs.getPath("/"))) {
                Path root = stream.filter(Files::isDirectory).findAny()
                        .orElseThrow(() -> new IOException("Not a valid world zip file"));
                fileName = FileUtils.getName(root);
                loadFromZipImpl(root);
            }
        }
    }

    private void getWorldName(Path levelDat) throws IOException {
        CompoundTag nbt = parseLevelDat(levelDat);

        CompoundTag data = nbt.get("Data");
        if (data == null)
            throw new IOException("level.dat missing Data");

        if (data.get("LevelName") instanceof StringTag)
            worldName = data.<StringTag>get("LevelName").getValue();
        else
            throw new IOException("level.dat missing LevelName");

        if (data.get("LastPlayed") instanceof LongTag)
            lastPlayed = data.<LongTag>get("LastPlayed").getValue();
        else
            throw new IOException("level.dat missing LastPlayed");

        gameVersion = null;
        if (data.get("Version") instanceof CompoundTag) {
            CompoundTag version = data.get("Version");

            if (version.get("Name") instanceof StringTag)
                gameVersion = version.<StringTag>get("Name").getValue();
        }
    }

    public void rename(String newName) throws IOException {
        if (!Files.isDirectory(file))
            throw new IOException("Not a valid world directory");

        // Change the name recorded in level.dat
        CompoundTag nbt = readLevelDat();
        CompoundTag data = nbt.get("Data");
        data.put(new StringTag("LevelName", newName));
        writeLevelDat(nbt);

        // then change the folder's name
        Files.move(file, file.resolveSibling(newName));
    }

    public void install(Path savesDir, String name) throws IOException {
        Path worldDir;
        try {
            worldDir = savesDir.resolve(name);
        } catch (InvalidPathException e) {
            throw new IOException(e);
        }

        if (Files.isDirectory(worldDir)) {
            throw new FileAlreadyExistsException("World already exists");
        }

        if (Files.isRegularFile(file)) {
            try (FileSystem fs = CompressingUtils.readonly(file).setAutoDetectEncoding(true).build()) {
                Path cur = fs.getPath("/level.dat");
                if (Files.isRegularFile(cur)) {
                    fileName = FileUtils.getName(file);

                    new Unzipper(file, worldDir).unzip();
                } else {
                    try (Stream<Path> stream = Files.list(fs.getPath("/"))) {
                        List<Path> subDirs = stream.collect(Collectors.toList());
                        if (subDirs.size() != 1) {
                            throw new IOException("World zip malformed");
                        }
                        String subDirectoryName = FileUtils.getName(subDirs.get(0));
                        new Unzipper(file, worldDir)
                                .setSubDirectory("/" + subDirectoryName + "/")
                                .unzip();
                    }
                }

            }
            new World(worldDir).rename(name);
        } else if (Files.isDirectory(file)) {
            FileUtils.copyDirectory(file, worldDir);
        }
    }

    public void export(Path zip, String worldName) throws IOException {
        if (!Files.isDirectory(file))
            throw new IOException();

        try (Zipper zipper = new Zipper(zip)) {
            zipper.putDirectory(file, worldName);
        }
    }

    public CompoundTag readLevelDat() throws IOException {
        if (!Files.isDirectory(file))
            throw new IOException("Not a valid world directory");

        return parseLevelDat(getLevelDatFile());
    }

    public void writeLevelDat(CompoundTag nbt) throws IOException {
        if (!Files.isDirectory(file))
            throw new IOException("Not a valid world directory");

        FileUtils.saveSafely(getLevelDatFile(), os -> {
            try (OutputStream gos = new GZIPOutputStream(os)) {
                NBTIO.writeTag(gos, nbt);
            }
        });
    }

    private static CompoundTag parseLevelDat(Path path) throws IOException {
        try (InputStream is = new GZIPInputStream(Files.newInputStream(path))) {
            Tag nbt = NBTIO.readTag(is);
            if (nbt instanceof CompoundTag)
                return (CompoundTag) nbt;
            else
                throw new IOException("level.dat malformed");
        }
    }

    public static Stream<World> getWorlds(Path savesDir) {
        try {
            if (Files.exists(savesDir)) {
                return Files.list(savesDir).flatMap(world -> {
                    try {
                        return Stream.of(new World(world));
                    } catch (IOException e) {
                        Logging.LOG.log(Level.WARNING, "Failed to read world " + world, e);
                        return Stream.empty();
                    }
                });
            }
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, "Failed to read saves", e);
        }
        return Stream.empty();
    }
}
