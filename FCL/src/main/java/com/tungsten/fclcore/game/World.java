/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fclcore.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.IntArrayTag;
import com.github.steveice10.opennbt.tag.builtin.ByteTag;
import com.github.steveice10.opennbt.tag.builtin.LongTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.CompressingUtils;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.IOUtils;
import com.tungsten.fclcore.util.io.Unzipper;
import com.tungsten.fclcore.util.io.Zipper;
import com.tungsten.fclcore.util.versioning.GameVersionNumber;
import net.jpountz.lz4.LZ4BlockInputStream;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.tungsten.fclcore.util.Logging.LOG;

public final class World {

    private static final byte[] LZ4_BLOCK_MAGIC = "LZ4Block".getBytes(StandardCharsets.US_ASCII);

    private final Path file;
    private String fileName;
    private Bitmap icon;

    private CompoundTag levelData;
    private CompoundTag dataTag;
    private Path levelDataPath;

    private CompoundTag worldGenSettingsDataBackingTag; // Use for writing back to the file
    private CompoundTag normalizedWorldGenSettingsData; // Use for reading/modification
    private Path worldGenSettingsDataPath;

    private CompoundTag playerData; // Use for both reading/modification and writing back to the file
    private Path playerDataPath;

    public World(Path file) throws IOException {
        this.file = file;

        if (Files.isDirectory(file)) {
            fileName = FileUtils.getName(this.file);
            Path levelDatPath = this.file.resolve("level.dat");
            if (!Files.exists(levelDatPath)) { // version 20w14infinite
                levelDatPath = this.file.resolve("special_level.dat");
            }
            if (!Files.exists(levelDatPath)) {
                throw new IOException("Not a valid world directory since level.dat or special_level.dat cannot be found.");
            }
            this.levelDataPath = levelDatPath;
            loadAndCheckWorldData();

            Path iconFile = this.file.resolve("icon.png");
            if (Files.isRegularFile(iconFile)) {
                try (InputStream inputStream = Files.newInputStream(iconFile)) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inScaled = false;
                    icon = BitmapFactory.decodeStream(inputStream, null, options);
                } catch (Exception e) {
                    LOG.warning("Failed to load world icon");
                }
            }
        } else if (Files.isRegularFile(file))
            try (FileSystem fs = CompressingUtils.readonly(this.file).setAutoDetectEncoding(true).build()) {
                Path root;
                if (Files.isRegularFile(fs.getPath("/level.dat"))) {
                    root = fs.getPath("/");
                    fileName = FileUtils.getName(this.file);
                } else {
                    try (Stream<Path> filesStream = Files.list(fs.getPath("/"))) {
                        List<Path> files = filesStream.toList();
                        if (files.size() != 1 || !Files.isDirectory(files.get(0))) {
                            throw new IOException("Not a valid world zip file");
                        }

                        root = files.get(0);
                        fileName = FileUtils.getName(root);
                    }
                }

                Path levelDat = root.resolve("level.dat");
                if (!Files.exists(levelDat)) { //version 20w14infinite
                    levelDat = root.resolve("special_level.dat");
                }
                if (!Files.exists(levelDat)) {
                    throw new IOException("Not a valid world zip file since level.dat or special_level.dat cannot be found.");
                }
                loadAndCheckLevelData(levelDat);

                Path iconFile = root.resolve("icon.png");
                if (Files.isRegularFile(iconFile)) {
                    try (InputStream inputStream = Files.newInputStream(iconFile)) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inScaled = false;
                        icon = BitmapFactory.decodeStream(inputStream, null, options);
                    } catch (Exception e) {
                        LOG.warning("Failed to load world icon");
                    }
                }
            }
        else
            throw new IOException("Path " + file + " cannot be recognized as a Minecraft world");
    }

    public Path getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public String getWorldName() {
        if (levelData.get("Data") instanceof CompoundTag data
                && data.get("LevelName") instanceof StringTag levelNameTag)
            return levelNameTag.getValue();
        else
            return "";
    }

    public void setWorldName(String worldName) throws IOException {
        if (levelData.get("Data") instanceof CompoundTag data && data.get("LevelName") instanceof StringTag levelNameTag) {
            levelNameTag.setValue(worldName);
            writeLevelData();
        }
    }

    public Path getSessionLockFile() {
        return file.resolve("session.lock");
    }

    public CompoundTag getLevelData() {
        return levelData;
    }

    public @Nullable CompoundTag getNormalizedWorldGenSettingsData() {
        return normalizedWorldGenSettingsData;
    }

    public @Nullable CompoundTag getPlayerData() {
        return playerData;
    }

    public long getLastPlayed() {
        if (dataTag.get("LastPlayed") instanceof LongTag lastPlayedTag) {
            return lastPlayedTag.getValue();
        } else {
            return 0L;
        }
    }

    public @Nullable GameVersionNumber getGameVersion() {
        if (levelData.get("Data") instanceof CompoundTag data &&
                data.get("Version") instanceof CompoundTag versionTag &&
                versionTag.get("Name") instanceof StringTag nameTag) {
            return GameVersionNumber.asGameVersion(nameTag.getValue());
        }
        return null;
    }

    public @Nullable Long getSeed() {
        // Valid after 1.16(20w20a)
        if (normalizedWorldGenSettingsData != null
                && normalizedWorldGenSettingsData.get("seed") instanceof LongTag seedTag) {
            return seedTag.getValue();
        }
        // Valid before 1.16(20w20a)
        if (dataTag.get("RandomSeed") instanceof LongTag seedTag) {
            return seedTag.getValue();
        }
        return null;
    }

    public boolean isLargeBiomes() {
        // Valid before 1.16(20w20a)
        if (dataTag.get("generatorName") instanceof StringTag generatorNameTag) {
            return "largeBiomes".equals(generatorNameTag.getValue());
        } else if (normalizedWorldGenSettingsData != null
                && normalizedWorldGenSettingsData.get("dimensions") instanceof CompoundTag dimensionsTag) {
            // Unified handling of logic after version 1.16
            if (dimensionsTag.get("minecraft:overworld") instanceof CompoundTag overworldTag
                    && overworldTag.get("generator") instanceof CompoundTag generatorTag) {
                // Valid between 1.16(20w20a) and 1.18(21w37a)
                if (generatorTag.get("biome_source") instanceof CompoundTag biomeSourceTag
                        && biomeSourceTag.get("large_biomes") instanceof ByteTag largeBiomesTag) {
                    return largeBiomesTag.getValue() == (byte) 1;
                } else if (generatorTag.get("settings") instanceof StringTag settingsTag) {
                    // Valid after 1.18(21w37a)
                    return "minecraft:large_biomes".equals(settingsTag.getValue());
                }
            }
        }
        return false;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public boolean isLocked() {
        return isLocked(getSessionLockFile());
    }

    public boolean supportDataPacks() {
        return getGameVersion() != null && getGameVersion().isAtLeast("1.13", "17w43a");
    }

    public boolean supportQuickPlay() {
        return getGameVersion() != null && getGameVersion().isAtLeast("1.20", "23w14a");
    }

    public static boolean supportQuickPlay(GameVersionNumber gameVersionNumber) {
        return gameVersionNumber != null && gameVersionNumber.isAtLeast("1.20", "23w14a");
    }

    private void loadAndCheckWorldData() throws IOException {
        loadAndCheckLevelData(levelDataPath);
        loadOtherData();
    }

    private void loadAndCheckLevelData(Path levelDat) throws IOException {
        this.levelData = readNBTFile(levelDat);
        if (!(levelData.get("Data") instanceof CompoundTag data))
            throw new IOException("level.dat missing Data");

        if (!(data.get("LevelName") instanceof StringTag))
            throw new IOException("level.dat missing LevelName");

        if (!(data.get("LastPlayed") instanceof LongTag))
            throw new IOException("level.dat missing LastPlayed");
        this.dataTag = data;
    }

    private void loadOtherData() throws IOException {
        if (!(levelData.get("Data") instanceof CompoundTag data)) return;

        Path worldGenSettingsDatPath = file.resolve("data/minecraft/world_gen_settings.dat");
        if (data.get("WorldGenSettings") instanceof CompoundTag worldGenSettingsTag) {
            setWorldGenSettingsData(null, worldGenSettingsTag, worldGenSettingsTag);
        } else if (Files.isRegularFile(worldGenSettingsDatPath)) {
            CompoundTag raw = readNBTFile(worldGenSettingsDatPath);
            if (raw.get("data") instanceof CompoundTag compoundTag) {
                setWorldGenSettingsData(worldGenSettingsDatPath, raw, compoundTag);
            } else {
                setWorldGenSettingsData(null, null, null);
            }
        } else {
            setWorldGenSettingsData(null, null, null);
        }

        if (data.get("Player") instanceof CompoundTag playerTag) {
            setPlayerData(null, playerTag);
        } else if (data.get("singleplayer_uuid") instanceof IntArrayTag uuidTag && uuidTag.getValue().length == 4) {
            int[] uuidValue = uuidTag.getValue();
            UUID playerUUID = new UUID(
                    ((long) uuidValue[0] << 32) | (uuidValue[1] & 0xFFFFFFFFL),
                    ((long) uuidValue[2] << 32) | (uuidValue[3] & 0xFFFFFFFFL));
            Path playerDatPath = file.resolve("players/data/" + playerUUID + ".dat");
            if (Files.exists(playerDatPath)) {
                setPlayerData(playerDatPath, readNBTFile(playerDatPath));
            } else {
                setPlayerData(null, null);
            }
        } else {
            setPlayerData(null, null);
        }
    }

    private void setWorldGenSettingsData(Path worldGenSettingsDataPath, CompoundTag worldGenSettingsDataBackingTag, CompoundTag unifiedWorldGenSettingsData) {
        this.worldGenSettingsDataPath = worldGenSettingsDataPath;
        this.worldGenSettingsDataBackingTag = worldGenSettingsDataBackingTag;
        this.normalizedWorldGenSettingsData = unifiedWorldGenSettingsData;
    }

    private void setPlayerData(Path playerDataPath, CompoundTag playerData) {
        this.playerDataPath = playerDataPath;
        this.playerData = playerData;
    }

    public void reloadWorldData() throws IOException {
        loadAndCheckWorldData();
    }

    // The rename method is used to rename temporary world object during installation and copying,
    // so there is no need to modify the `file` field.
    public void rename(String newName) throws IOException {
        if (!Files.isDirectory(file))
            throw new IOException("Not a valid world directory");

        // Change the name recorded in level.dat
        dataTag.put(new StringTag("LevelName", newName));
        writeLevelData();

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
                Path levelDatPath = fs.getPath("/level.dat");
                if (Files.isRegularFile(levelDatPath)) {
                    fileName = FileUtils.getName(file);

                    new Unzipper(file, worldDir).unzip();
                } else {
                    try (Stream<Path> stream = Files.list(fs.getPath("/"))) {
                        List<Path> subDirs = stream.toList();
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

    public void delete() throws IOException {
        if (isLocked()) {
            throw new WorldLockedException("The world " + getFile() + " has been locked");
        }
        FileUtils.forceDelete(file.toFile());
    }

    public void copy(String newName) throws IOException {
        if (!Files.isDirectory(file)) {
            throw new IOException("Not a valid world directory");
        }

        if (isLocked()) {
            throw new WorldLockedException("The world " + getFile() + " has been locked");
        }

        Path newPath = file.resolveSibling(newName);
        FileUtils.copyDirectory(file, newPath, path -> !path.contains("session.lock"));
        World newWorld = new World(newPath);
        newWorld.rename(newName);
    }

    public FileChannel lock() throws WorldLockedException {
        Path lockFile = getSessionLockFile();
        FileChannel channel = null;
        try {
            channel = FileChannel.open(lockFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            channel.write(ByteBuffer.wrap("\u2603".getBytes(StandardCharsets.UTF_8)));
            channel.force(true);
            FileLock fileLock = channel.tryLock();
            if (fileLock != null) {
                return channel;
            } else {
                IOUtils.closeQuietly(channel);
                throw new WorldLockedException("The world " + getFile() + " has been locked");
            }
        } catch (IOException e) {
            IOUtils.closeQuietly(channel);
            throw new WorldLockedException(e);
        }
    }

    public void writeWorldData() throws IOException {
        if (!Files.isDirectory(file)) throw new IOException("Not a valid world directory");

        writeLevelData();

        if (worldGenSettingsDataPath != null && worldGenSettingsDataBackingTag != null) {
            writeTag(worldGenSettingsDataBackingTag, worldGenSettingsDataPath);
        }

        if (playerDataPath != null && playerData != null) {
            writeTag(playerData, playerDataPath);
        }
    }

    public void writeLevelData() throws IOException {
        writeTag(levelData, levelDataPath);
    }

    private void writeTag(CompoundTag nbt, Path path) throws IOException {
        if (!Files.isDirectory(file)) throw new IOException("Not a valid world directory");
        FileUtils.saveSafely(path, os -> {
            try (OutputStream gos = new GZIPOutputStream(os)) {
                NBTIO.writeTag(gos, nbt);
            }
        });
    }

    /**
     * 按文件头识别 NBT 容器的压缩方式并包装为解压后的流，支持 GZip、LZ4 Block 与未压缩格式
     */
    private static InputStream openNBTInputStream(InputStream is) throws IOException {
        PushbackInputStream pis = new PushbackInputStream(is, 8);
        byte[] header = new byte[8];
        int n = 0;
        while (n < header.length) {
            int count = pis.read(header, n, header.length - n);
            if (count < 0) break;
            n += count;
        }
        pis.unread(header, 0, n);
        if (n >= 2 && (header[0] & 0xff) == 0x1f && (header[1] & 0xff) == 0x8b) {
            return new GZIPInputStream(pis);
        }
        if (n >= 8 && Arrays.equals(header, 0, 8, LZ4_BLOCK_MAGIC, 0, 8)) {
            return new LZ4BlockInputStream(pis);
        }
        return pis;
    }

    private static CompoundTag readNBTFile(Path path) throws IOException {
        try (InputStream is = openNBTInputStream(Files.newInputStream(path))) {
            Tag tag = NBTIO.readTag(is);
            if (tag instanceof CompoundTag compoundTag)
                return compoundTag;
            throw new IOException("NBT file malformed");
        }
    }

    private static boolean isLocked(Path sessionLockFile) {
        try (FileChannel fileChannel = FileChannel.open(sessionLockFile, StandardOpenOption.WRITE)) {
            return fileChannel.tryLock() == null;
        } catch (AccessDeniedException | OverlappingFileLockException accessDeniedException) {
            return true;
        } catch (NoSuchFileException noSuchFileException) {
            return false;
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Failed to open the lock file " + sessionLockFile, e);
            return false;
        }
    }

    public static List<World> getWorlds(Path savesDir) {
        if (Files.exists(savesDir)) {
            try (Stream<Path> stream = Files.list(savesDir)) {
                return stream
                        .filter(Files::isDirectory)
                        .flatMap(world -> {
                            try {
                                return Stream.of(new World(world.toAbsolutePath().normalize()));
                            } catch (IOException e) {
                                LOG.warning("Failed to read world " + world);
                                return Stream.empty();
                            }
                        })
                        .toList();
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Failed to read saves", e);
            }
        }
        return List.of();
    }
}
