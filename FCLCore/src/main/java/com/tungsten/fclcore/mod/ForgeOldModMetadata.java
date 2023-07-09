package com.tungsten.fclcore.mod;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ForgeOldModMetadata {

    @SerializedName("modid")
    private final String modId;
    private final String name;
    private final String description;
    private final String author;
    private final String version;
    private final String logoFile;
    private final String mcversion;
    private final String url;
    private final String updateUrl;
    private final String credits;
    private final String[] authorList;
    private final String[] authors;

    public ForgeOldModMetadata() {
        this("", "", "", "", "", "", "", "", "", "", new String[0], new String[0]);
    }

    public ForgeOldModMetadata(String modId, String name, String description, String author, String version, String logoFile, String mcversion, String url, String updateUrl, String credits, String[] authorList, String[] authors) {
        this.modId = modId;
        this.name = name;
        this.description = description;
        this.author = author;
        this.version = version;
        this.logoFile = logoFile;
        this.mcversion = mcversion;
        this.url = url;
        this.updateUrl = updateUrl;
        this.credits = credits;
        this.authorList = authorList;
        this.authors = authors;
    }

    public String getModId() {
        return modId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }

    public String getLogoFile() {
        return logoFile;
    }

    public String getGameVersion() {
        return mcversion;
    }

    public String getUrl() {
        return url;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public String getCredits() {
        return credits;
    }

    public String[] getAuthorList() {
        return authorList;
    }

    public String[] getAuthors() {
        return authors;
    }

    public static LocalModFile fromFile(ModManager modManager, Path modFile, FileSystem fs) throws IOException, JsonParseException {
        Path mcmod = fs.getPath("mcmod.info");
        if (Files.notExists(mcmod))
            throw new IOException("File " + modFile + " is not a Forge mod.");
        List<ForgeOldModMetadata> modList = JsonUtils.GSON.fromJson(FileUtils.readText(mcmod),
                new TypeToken<List<ForgeOldModMetadata>>() {
                }.getType());
        if (modList == null || modList.isEmpty())
            throw new IOException("Mod " + modFile + " `mcmod.info` is malformed..");
        ForgeOldModMetadata metadata = modList.get(0);
        String authors = metadata.getAuthor();
        if (StringUtils.isBlank(authors) && metadata.getAuthors().length > 0)
            authors = String.join(", ", metadata.getAuthors());
        if (StringUtils.isBlank(authors) && metadata.getAuthorList().length > 0)
            authors = String.join(", ", metadata.getAuthorList());
        if (StringUtils.isBlank(authors))
            authors = metadata.getCredits();
        return new LocalModFile(modManager, modManager.getLocalMod(metadata.getModId(), ModLoaderType.FORGE), modFile, metadata.getName(), new LocalModFile.Description(metadata.getDescription()),
                authors, metadata.getVersion(), metadata.getGameVersion(),
                StringUtils.isBlank(metadata.getUrl()) ? metadata.getUpdateUrl() : metadata.url,
                metadata.getLogoFile());
    }
}
