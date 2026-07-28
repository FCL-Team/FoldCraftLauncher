package com.tungsten.fclcore.util.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Resolves archive entries without allowing them to escape their extraction directory.
 */
final class ArchiveExtractionPath {

    /** Prevents construction of this path-validation utility. */
    private ArchiveExtractionPath() {
    }

    /**
     * Creates the destination and returns its real path as the trusted extraction root.
     *
     * @param destination requested extraction directory
     * @return real path of the extraction root
     * @throws IOException if the destination cannot be created or is not a directory
     */
    static Path prepareDestination(Path destination) throws IOException {
        Files.createDirectories(destination);
        Path root = destination.toRealPath();
        if (!Files.isDirectory(root, LinkOption.NOFOLLOW_LINKS)) {
            throw new IOException("Archive destination is not a directory: " + destination);
        }
        return root;
    }

    /**
     * Resolves a portable archive entry name below the trusted extraction root.
     *
     * @param root trusted extraction root returned by {@link #prepareDestination(Path)}
     * @param entryName raw path stored in the archive
     * @return normalized output path below the root
     * @throws IOException if the entry is absolute, traverses upward, or has an invalid path
     */
    static Path resolve(Path root, String entryName) throws IOException {
        if (entryName == null || entryName.isEmpty()) {
            throw new IOException("Archive entry has an empty path");
        }

        String portableName = entryName.replace('\\', '/');
        if (portableName.startsWith("/") || hasWindowsDrivePrefix(portableName)) {
            throw new IOException("Archive entry uses an absolute path: " + entryName);
        }

        String[] segments = portableName.split("/", -1);
        for (String segment : segments) {
            if ("..".equals(segment)) {
                throw new IOException("Archive entry traverses outside the destination: " + entryName);
            }
        }

        Path relative;
        try {
            relative = Paths.get(portableName).normalize();
        } catch (InvalidPathException e) {
            throw new IOException("Archive entry has an invalid path: " + entryName, e);
        }
        if (relative.isAbsolute() || relative.getNameCount() == 0 || ".".equals(relative.toString())) {
            throw new IOException("Archive entry has an invalid path: " + entryName);
        }

        Path output = root.resolve(relative).normalize();
        if (!output.startsWith(root)) {
            throw new IOException("Archive entry escapes the destination: " + entryName);
        }
        return output;
    }

    /**
     * Creates an output directory one component at a time so existing links cannot redirect extraction.
     *
     * @param root trusted extraction root
     * @param directory directory below the root
     * @throws IOException if a component is a link, a non-directory, or cannot be created
     */
    static void createDirectories(Path root, Path directory) throws IOException {
        Path normalizedDirectory = directory.normalize();
        if (!normalizedDirectory.startsWith(root)) {
            throw new IOException("Archive output directory escapes the destination: " + directory);
        }

        Path current = root;
        for (Path segment : root.relativize(normalizedDirectory)) {
            current = current.resolve(segment);
            ensureDirectory(current);
        }
    }

    /**
     * Opens a regular output file without following a pre-existing symbolic link.
     *
     * @param root trusted extraction root
     * @param file output file below the root
     * @return stream that truncates or creates the output file
     * @throws IOException if the parent is unsafe or the output is not a regular file
     */
    static OutputStream newOutputStream(Path root, Path file) throws IOException {
        Path parent = file.getParent();
        if (parent == null) {
            throw new IOException("Archive output file has no parent: " + file);
        }
        createDirectories(root, parent);
        if (Files.exists(file, LinkOption.NOFOLLOW_LINKS)
                && (!Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS) || Files.isSymbolicLink(file))) {
            throw new IOException("Archive output is not a regular file: " + file);
        }

        OpenOption[] options = {
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE,
                LinkOption.NOFOLLOW_LINKS
        };
        return Files.newOutputStream(file, options);
    }

    /** Detects drive-letter absolute paths even when running on a non-Windows file system. */
    private static boolean hasWindowsDrivePrefix(String path) {
        return path.length() >= 2 && Character.isLetter(path.charAt(0)) && path.charAt(1) == ':';
    }

    /** Creates one directory component without accepting a link or regular file in its place. */
    private static void ensureDirectory(Path directory) throws IOException {
        if (!Files.exists(directory, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectory(directory);
            } catch (FileAlreadyExistsException e) {
                validateExistingDirectory(directory, e);
            }
        }
        validateExistingDirectory(directory, null);
    }

    /** Validates an existing component and attaches the creation race as its cause when present. */
    private static void validateExistingDirectory(Path directory, IOException cause) throws IOException {
        if (Files.isSymbolicLink(directory)) {
            throw new IOException("Archive output directory is a symbolic link: " + directory, cause);
        }
        if (!Files.isDirectory(directory, LinkOption.NOFOLLOW_LINKS)) {
            throw new IOException("Archive output directory is not a directory: " + directory, cause);
        }
    }
}
