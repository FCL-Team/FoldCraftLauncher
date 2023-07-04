package com.tungsten.fclcore.game;

import com.google.gson.JsonParseException;
import com.tungsten.fclcore.util.DigestUtils;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.Validation;

import java.io.IOException;
import java.nio.file.Path;

public final class AssetObject implements Validation {

    private final String hash;
    private final long size;

    public AssetObject() {
        this("", 0);
    }

    public AssetObject(String hash, long size) {
        this.hash = hash;
        this.size = size;
    }

    public String getHash() {
        return hash;
    }

    public long getSize() {
        return size;
    }

    public String getLocation() {
        return hash.substring(0, 2) + "/" + hash;
    }

    @Override
    public void validate() throws JsonParseException {
        if (StringUtils.isBlank(hash) || hash.length() < 2)
            throw new JsonParseException("AssetObject hash cannot be blank.");
    }

    public boolean validateChecksum(Path file, boolean defaultValue) throws IOException {
        if (hash == null) return defaultValue;
        return DigestUtils.digestToString("SHA-1", file).equalsIgnoreCase(hash);
    }
}
