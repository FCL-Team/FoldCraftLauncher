package com.tungsten.fclcore.util.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Builds a ZIP-equivalent modpack from safely extracted RAR entries.
 */
final class RarToZipConverterImpl implements RarToZipConverter {

    /** Extractor shared with direct RAR extraction to keep validation rules identical. */
    private final RarExtractor extractor;

    /** Creates a converter backed by the validated RAR extraction interface. */
    RarToZipConverterImpl(RarExtractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public File convert(File archive) throws IOException {
        Path extractionDirectory = Files.createTempDirectory("fcl-rar-modpack-");
        Path zipFile = null;
        IOException failure = null;
        try {
            extractor.extract(archive, extractionDirectory.toFile());
            zipFile = Files.createTempFile("fcl-rar-modpack-", ".zip");
            writeZip(extractionDirectory, zipFile);
            return zipFile.toFile();
        } catch (IOException e) {
            failure = e;
            deleteOutput(zipFile, e);
            throw e;
        } finally {
            try {
                FileUtils.deleteDirectory(extractionDirectory.toFile());
            } catch (IOException cleanupException) {
                if (failure != null) {
                    failure.addSuppressed(cleanupException);
                } else {
                    deleteOutput(zipFile, cleanupException);
                    throw cleanupException;
                }
            }
        }
    }

    /** Writes extracted top-level entries while preserving their archive-relative paths. */
    private static void writeZip(Path source, Path destination) throws IOException {
        try (Zipper zipper = new Zipper(destination);
             Stream<Path> children = Files.list(source)) {
            List<Path> entries = children.sorted(Comparator.comparing(Path::toString))
                    .collect(Collectors.toList());
            for (Path entry : entries) {
                String name = entry.getFileName().toString();
                if (Files.isDirectory(entry)) {
                    zipper.putDirectory(entry, name);
                } else {
                    zipper.putFile(entry, name);
                }
            }
        }
    }

    /** Removes a partial ZIP and preserves cleanup errors on the primary conversion failure. */
    private static void deleteOutput(Path output, IOException failure) {
        if (output == null) {
            return;
        }
        try {
            Files.deleteIfExists(output);
        } catch (IOException cleanupException) {
            failure.addSuppressed(cleanupException);
        }
    }
}
