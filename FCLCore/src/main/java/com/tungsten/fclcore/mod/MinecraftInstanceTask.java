package com.tungsten.fclcore.mod;

import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.DigestUtils;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.CompressingUtils;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class MinecraftInstanceTask<T> extends Task<ModpackConfiguration<T>> {

    private final File zipFile;
    private final Charset encoding;
    private final List<String> subDirectories;
    private final File jsonFile;
    private final T manifest;
    private final String type;
    private final String name;
    private final String version;

    public MinecraftInstanceTask(File zipFile, Charset encoding, List<String> subDirectories, T manifest, ModpackProvider modpackProvider, String name, String version, File jsonFile) {
        this.zipFile = zipFile;
        this.encoding = encoding;
        this.subDirectories = subDirectories.stream().map(FileUtils::normalizePath).collect(Collectors.toList());
        this.manifest = manifest;
        this.jsonFile = jsonFile;
        this.type = modpackProvider.getName();
        this.name = name;
        this.version = version;
    }

    @Override
    public void execute() throws Exception {
        List<ModpackConfiguration.FileInformation> overrides = new ArrayList<>();

        try (FileSystem fs = CompressingUtils.readonly(zipFile.toPath()).setEncoding(encoding).build()) {
            for (String subDirectory : subDirectories) {
                Path root = fs.getPath(subDirectory);

                if (Files.exists(root))
                    Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            String relativePath = root.relativize(file).normalize().toString().replace(File.separatorChar, '/');
                            overrides.add(new ModpackConfiguration.FileInformation(relativePath, DigestUtils.digestToString("SHA-1", file)));
                            return FileVisitResult.CONTINUE;
                        }
                    });
            }
        }

        ModpackConfiguration<T> configuration = new ModpackConfiguration<>(manifest, type, name, version, overrides);
        FileUtils.writeText(jsonFile, JsonUtils.GSON.toJson(configuration));
        setResult(configuration);
    }
}
