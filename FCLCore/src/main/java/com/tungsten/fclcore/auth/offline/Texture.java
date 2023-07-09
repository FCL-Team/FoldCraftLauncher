package com.tungsten.fclcore.auth.offline;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tungsten.fclcore.util.Hex;

public final class Texture {
    private final String hash;
    private final Bitmap image;

    public Texture(String hash, Bitmap image) {
        this.hash = requireNonNull(hash);
        this.image = requireNonNull(image);
    }

    public String getHash() {
        return hash;
    }

    public Bitmap getImage() {
        return image;
    }

    private static final Map<String, Texture> textures = new HashMap<>();

    public static boolean hasTexture(String hash) {
        return textures.containsKey(hash);
    }

    public static Texture getTexture(String hash) {
        return textures.get(hash);
    }

    private static String computeTextureHash(Bitmap img) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        int width = (int) img.getWidth();
        int height = (int) img.getHeight();
        byte[] buf = new byte[4096];

        putInt(buf, 0, width);
        putInt(buf, 4, height);
        int pos = 8;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                putInt(buf, pos, img.getPixel(x, y));
                if (buf[pos + 0] == 0) {
                    buf[pos + 1] = buf[pos + 2] = buf[pos + 3] = 0;
                }
                pos += 4;
                if (pos == buf.length) {
                    pos = 0;
                    digest.update(buf, 0, buf.length);
                }
            }
        }
        if (pos > 0) {
            digest.update(buf, 0, pos);
        }

        return Hex.encodeHex(digest.digest());
    }

    private static void putInt(byte[] array, int offset, int x) {
        array[offset + 0] = (byte) (x >> 24 & 0xff);
        array[offset + 1] = (byte) (x >> 16 & 0xff);
        array[offset + 2] = (byte) (x >> 8 & 0xff);
        array[offset + 3] = (byte) (x >> 0 & 0xff);
    }

    public static Texture loadTexture(InputStream in) throws IOException {
        if (in == null) return null;
        Bitmap img;
        try (InputStream is = in) {
            img = BitmapFactory.decodeStream(is);
        }

        return loadTexture(img);
    }

    public static Texture loadTexture(Bitmap image) {
        if (image == null) return null;

        String hash = computeTextureHash(image);

        Texture existent = textures.get(hash);
        if (existent != null) {
            return existent;
        }

        Texture texture = new Texture(hash, image);
        existent = textures.putIfAbsent(hash, texture);

        if (existent != null) {
            return existent;
        }
        return texture;
    }

}
