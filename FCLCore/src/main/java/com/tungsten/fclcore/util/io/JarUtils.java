package com.tungsten.fclcore.util.io;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public final class JarUtils {
    private JarUtils() {
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static final Optional<Path> THIS_JAR =
            Optional.ofNullable(JarUtils.class.getProtectionDomain().getCodeSource())
                    .map(CodeSource::getLocation)
                    .map(url -> {
                        try {
                            return Paths.get(url.toURI());
                        } catch (FileSystemNotFoundException | IllegalArgumentException | URISyntaxException e) {
                            return null;
                        }
                    })
                    .filter(Files::isRegularFile);

    public static Optional<Path> thisJar() {
        return THIS_JAR;
    }

    public static Optional<Manifest> getManifest(Path jar) {
        try (JarFile file = new JarFile(jar.toFile())) {
            return Optional.ofNullable(file.getManifest());
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> getImplementationVersion(Path jar) {
        return Optional.of(jar).flatMap(JarUtils::getManifest)
                .flatMap(manifest -> Optional.ofNullable(manifest.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION)));
    }
}
