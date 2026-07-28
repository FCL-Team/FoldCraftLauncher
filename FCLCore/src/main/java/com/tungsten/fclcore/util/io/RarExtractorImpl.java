package com.tungsten.fclcore.util.io;

import com.tungsten.fclcore.util.Logging;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Locale;
import java.util.logging.Level;
import java.util.regex.Pattern;

import me.zhanghai.android.libarchive.Archive;
import me.zhanghai.android.libarchive.ArchiveEntry;
import me.zhanghai.android.libarchive.ArchiveException;

/**
 * Uses Android libarchive to extract RAR4 and RAR5 without exposing native handles to callers.
 */
final class RarExtractorImpl implements RarExtractor {

    /** Matches the naming convention used by modern multi-volume RAR archives. */
    private static final Pattern PART_VOLUME_PATTERN = Pattern.compile(".*\\.part\\d+\\.rar");

    /** Matches secondary volumes in the legacy RAR naming convention. */
    private static final Pattern LEGACY_VOLUME_PATTERN = Pattern.compile(".*\\.r\\d{2,3}");

    /** Native read block size selected to match libarchive's documented examples. */
    private static final int ARCHIVE_BLOCK_SIZE = 10_240;

    /** Reusable per-entry buffer size that avoids retaining large native or Java buffers. */
    private static final int ENTRY_BUFFER_SIZE = 8_192;

    @Override
    public void extract(File archive, File destination) throws IOException {
        try {
            validateSource(archive);
            rejectKnownMultiVolumeArchive(archive);
            Path root = ArchiveExtractionPath.prepareDestination(destination.toPath());
            extractEntries(archive, root);
        } catch (ArchiveException e) {
            IOException failure = new IOException("Unable to extract RAR archive: " + archive, e);
            Logging.LOG.log(Level.WARNING, failure.getMessage(), failure);
            throw failure;
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, "Unable to extract RAR archive: " + archive, e);
            throw e;
        }
    }

    /** Rejects missing paths and directories before opening a native archive handle. */
    private static void validateSource(File archive) throws IOException {
        if (!archive.isFile()) {
            throw new IOException("RAR archive is not a regular file: " + archive);
        }
    }

    /** Rejects known multi-volume naming patterns because the picker supplies one file only. */
    private static void rejectKnownMultiVolumeArchive(File archive) throws IOException {
        String lowerName = archive.getName().toLowerCase(Locale.ROOT);
        if (PART_VOLUME_PATTERN.matcher(lowerName).matches()
                || LEGACY_VOLUME_PATTERN.matcher(lowerName).matches()) {
            throw new IOException("Multi-volume RAR archives are not supported: " + archive);
        }

        if (lowerName.endsWith(".rar")) {
            String baseName = archive.getName().substring(0, archive.getName().length() - 4);
            File firstLegacyContinuation = new File(archive.getParentFile(), baseName + ".r00");
            if (firstLegacyContinuation.exists()) {
                throw new IOException("Multi-volume RAR archives are not supported: " + archive);
            }
        }
    }

    /** Iterates validated archive entries and writes them below the trusted extraction root. */
    private static void extractEntries(File archive, Path root) throws IOException {
        try (NativeRarArchive nativeArchive = NativeRarArchive.open(archive)) {
            long entry;
            while ((entry = Archive.readNextHeader(nativeArchive.handle())) != 0) {
                validateArchiveFormat(nativeArchive.handle());
                validateEntryType(entry);
                Path output = ArchiveExtractionPath.resolve(root, readEntryName(entry));
                if (ArchiveEntry.filetype(entry) == ArchiveEntry.AE_IFDIR) {
                    ArchiveExtractionPath.createDirectories(root, output);
                    Archive.readDataSkip(nativeArchive.handle());
                } else {
                    extractRegularFile(nativeArchive.handle(), root, output);
                }
            }
        }
    }

    /** Ensures libarchive did not auto-detect a non-RAR format from a renamed file. */
    private static void validateArchiveFormat(long archive) throws IOException {
        int format = Archive.format(archive);
        if (format != Archive.FORMAT_RAR && format != Archive.FORMAT_RAR_V5) {
            throw new IOException("Unsupported archive format reported by libarchive: " + format);
        }
    }

    /** Rejects encryption, links, devices, and any entry other than a regular file or directory. */
    private static void validateEntryType(long entry) throws IOException {
        if (ArchiveEntry.isEncrypted(entry)) {
            throw new IOException("Encrypted RAR entries are not supported: " + readEntryName(entry));
        }
        if (ArchiveEntry.hardlinkIsSet(entry) || ArchiveEntry.symlink(entry) != null
                || ArchiveEntry.filetype(entry) == ArchiveEntry.AE_IFLNK) {
            throw new IOException("RAR symbolic and hard links are not supported: " + readEntryName(entry));
        }

        int fileType = ArchiveEntry.filetype(entry);
        if (fileType != ArchiveEntry.AE_IFREG && fileType != ArchiveEntry.AE_IFDIR) {
            throw new IOException("Unsupported RAR entry type " + fileType + ": " + readEntryName(entry));
        }
    }

    /** Reads an entry path as strict UTF-8 so malformed names cannot be normalized ambiguously. */
    private static String readEntryName(long entry) throws IOException {
        String utf8Name = ArchiveEntry.pathnameUtf8(entry);
        if (utf8Name != null) {
            return utf8Name;
        }

        byte[] rawName = ArchiveEntry.pathname(entry);
        if (rawName == null) {
            throw new IOException("RAR entry has no path");
        }
        try {
            return StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(rawName))
                    .toString();
        } catch (CharacterCodingException e) {
            throw new IOException("RAR entry path is not valid UTF-8", e);
        }
    }

    /** Streams one regular file without retaining the complete entry in memory. */
    private static void extractRegularFile(long archive, Path root, Path output) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(ENTRY_BUFFER_SIZE);
        try (OutputStream stream = ArchiveExtractionPath.newOutputStream(root, output)) {
            while (true) {
                buffer.clear();
                Archive.readData(archive, buffer);
                int bytesRead = buffer.position();
                if (bytesRead == 0) {
                    return;
                }
                stream.write(buffer.array(), 0, bytesRead);
            }
        }
    }

    /** Owns one native archive handle and always releases it via try-with-resources. */
    private static final class NativeRarArchive implements AutoCloseable {

        /** Native handle returned by libarchive, or zero after release. */
        private long handle;

        /** Wraps a newly allocated native handle for deterministic release. */
        private NativeRarArchive(long handle) {
            this.handle = handle;
        }

        /** Opens a native reader with only RAR4/RAR5 formats enabled. */
        private static NativeRarArchive open(File archive) throws ArchiveException {
            NativeRarArchive nativeArchive = new NativeRarArchive(Archive.readNew());
            try {
                Archive.setCharset(nativeArchive.handle, StandardCharsets.UTF_8.name()
                        .getBytes(StandardCharsets.UTF_8));
                Archive.readSupportFilterAll(nativeArchive.handle);
                Archive.readSupportFormatRar(nativeArchive.handle);
                Archive.readSupportFormatRar5(nativeArchive.handle);
                byte[] archivePath = archive.toPath().toAbsolutePath().normalize().toString()
                        .getBytes(StandardCharsets.UTF_8);
                Archive.readOpenFileName(nativeArchive.handle, archivePath, ARCHIVE_BLOCK_SIZE);
                return nativeArchive;
            } catch (ArchiveException e) {
                try {
                    nativeArchive.close();
                } catch (ArchiveException closeException) {
                    e.addSuppressed(closeException);
                }
                throw e;
            }
        }

        /** Returns the live native handle used by libarchive calls. */
        private long handle() {
            return handle;
        }

        @Override
        public void close() throws ArchiveException {
            long archive = handle;
            if (archive != 0) {
                handle = 0;
                Archive.readFree(archive);
            }
        }
    }
}
