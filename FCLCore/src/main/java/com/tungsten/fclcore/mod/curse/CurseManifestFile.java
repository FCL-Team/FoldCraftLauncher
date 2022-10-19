package com.tungsten.fclcore.mod.curse;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.tungsten.fclcore.util.gson.Validation;
import com.tungsten.fclcore.util.io.NetworkUtils;

import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.Objects;

public final class CurseManifestFile implements Validation {

    @SerializedName("projectID")
    private final int projectID;
    
    @SerializedName("fileID")
    private final int fileID;
    
    @SerializedName("fileName")
    private final String fileName;

    @SerializedName("url")
    private final String url;
    
    @SerializedName("required")
    private final boolean required;

    public CurseManifestFile() {
        this(0, 0, null, null, true);
    }

    public CurseManifestFile(int projectID, int fileID, String fileName, String url, boolean required) {
        this.projectID = projectID;
        this.fileID = fileID;
        this.fileName = fileName;
        this.url = url;
        this.required = required;
    }

    public int getProjectID() {
        return projectID;
    }

    public int getFileID() {
        return fileID;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isRequired() {
        return required;
    }

    @Override
    public void validate() throws JsonParseException {
        if (projectID == 0 || fileID == 0)
            throw new JsonParseException("Missing Project ID or File ID.");
    }

    @Nullable
    public URL getUrl() {
        if (url == null) {
            if (fileName != null) {
                return NetworkUtils.toURL(NetworkUtils.encodeLocation(String.format("https://edge.forgecdn.net/files/%d/%d/%s", fileID / 1000, fileID % 1000, fileName)));
            } else {
                return null;
            }
        } else {
            return NetworkUtils.toURL(NetworkUtils.encodeLocation(url));
        }
    }

    public CurseManifestFile withFileName(String fileName) {
        return new CurseManifestFile(projectID, fileID, fileName, url, required);
    }

    public CurseManifestFile withURL(String url) {
        return new CurseManifestFile(projectID, fileID, fileName, url, required);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurseManifestFile that = (CurseManifestFile) o;
        return projectID == that.projectID &&
                fileID == that.fileID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectID, fileID);
    }
}
