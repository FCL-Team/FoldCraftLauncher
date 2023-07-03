package com.tungsten.fclcore.util.io;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public final class JarUtils {
    private JarUtils() {
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static final Optional<Path> THIS_JAR;

    private static final Manifest manifest;

    static {
        THIS_JAR = Optional.ofNullable(JarUtils.class.getProtectionDomain().getCodeSource())
                .map(codeSource -> {
                    try {
                        return Paths.get(codeSource.getLocation().toURI());
                    } catch (FileSystemNotFoundException | IllegalArgumentException | URISyntaxException e) {
                        return null;
                    }
                })
                .filter(Files::isRegularFile);

        manifest = THIS_JAR.flatMap(JarUtils::getManifest).orElseGet(Manifest::new);
    }

    public static Optional<Path> thisJar() {
        return THIS_JAR;
    }

    public static String getManifestAttribute(String name, String defaultValue) {
        String value = manifest.getMainAttributes().getValue(name);
        return value != null ? value : defaultValue;
    }

    public static Optional<Manifest> getManifest(Path jar) {
        try (JarFile file = new JarFile(jar.toFile())) {
            return Optional.ofNullable(file.getManifest());
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
