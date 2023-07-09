package com.tungsten.fclcore.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class DigestUtils {

    private DigestUtils() {
    }

    private static final int STREAM_BUFFER_LENGTH = 1024;

    public static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static byte[] digest(String algorithm, byte[] data) {
        return getDigest(algorithm).digest(data);
    }

    public static byte[] digest(String algorithm, Path path) throws IOException {
        try (InputStream is = Files.newInputStream(path)) {
            return digest(algorithm, is);
        }
    }

    public static byte[] digest(String algorithm, InputStream data) throws IOException {
        return digest(getDigest(algorithm), data);
    }

    public static byte[] digest(MessageDigest digest, InputStream data) throws IOException {
        return updateDigest(digest, data).digest();
    }

    public static String digestToString(String algorithm, byte[] data) throws IOException {
        return Hex.encodeHex(digest(algorithm, data));
    }

    public static String digestToString(String algorithm, Path path) throws IOException {
        return Hex.encodeHex(digest(algorithm, path));
    }

    public static String digestToString(String algorithm, InputStream data) throws IOException {
        return Hex.encodeHex(digest(algorithm, data));
    }

    private static final ThreadLocal<byte[]> threadLocalBuffer = ThreadLocal.withInitial(() -> new byte[STREAM_BUFFER_LENGTH]);

    public static MessageDigest updateDigest(MessageDigest digest, InputStream data) throws IOException {
        byte[] buffer = threadLocalBuffer.get();
        int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);

        while (read > -1) {
            digest.update(buffer, 0, read);
            read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        }

        return digest;
    }

}
