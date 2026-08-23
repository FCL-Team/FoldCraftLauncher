package com.tungsten.fclcore.mod.modinfo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fclcore.mod.ModLoaderType;
import com.tungsten.fclcore.mod.ModManager;
import com.tungsten.fclcore.util.gson.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

public final class QuiltModMetadata {
    private record QuiltLoader(String id, String version, Metadata metadata) {
        private record Metadata(String name, String description, JsonObject contributors,
                                String icon, JsonObject contact) {
        }

    }

    private final int schema_version;
    private final QuiltLoader quilt_loader;

    public QuiltModMetadata(int schemaVersion, QuiltLoader quiltLoader) {
        this.schema_version = schemaVersion;
        this.quilt_loader = quiltLoader;
    }

    public static LocalModFile fromFile(ModManager modManager, Path modFile, FileSystem fs) throws IOException, JsonParseException {
        Path path = fs.getPath("quilt.mod.json");
        if (Files.notExists(path)) {
            throw new IOException("File " + modFile + " is not a Quilt mod.");
        }

        QuiltModMetadata root;
        try (InputStream is = Files.newInputStream(path)) {
            root = JsonUtils.fromNonNullJsonFully(is, QuiltModMetadata.class);
        }
        if (root.schema_version != 1) {
            throw new IOException("File " + modFile + " is not a supported Quilt mod.");
        }

        return new LocalModFile(
                modManager,
                modManager.getLocalMod(root.quilt_loader.id, ModLoaderType.QUILT),
                modFile,
                root.quilt_loader.metadata.name,
                new LocalModFile.Description(root.quilt_loader.metadata.description),
                root.quilt_loader.metadata.contributors.entrySet().stream().map(entry -> String.format("%s (%s)", entry.getKey(), entry.getValue().getAsJsonPrimitive().getAsString())).collect(Collectors.joining(", ")),
                root.quilt_loader.version,
                "",
                Optional.ofNullable(root.quilt_loader.metadata.contact.get("homepage")).map(jsonElement -> jsonElement.getAsJsonPrimitive().getAsString()).orElse(""),
                root.quilt_loader.metadata.icon
        );
    }
}