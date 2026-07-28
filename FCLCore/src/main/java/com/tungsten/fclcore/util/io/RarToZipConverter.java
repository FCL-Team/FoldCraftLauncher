package com.tungsten.fclcore.util.io;

import java.io.File;
import java.io.IOException;

/**
 * Converts a RAR modpack into a temporary ZIP so existing ZIP-based providers can process it.
 */
public interface RarToZipConverter {

    /**
     * Converts an unencrypted, single-volume RAR4 or RAR5 archive into a temporary ZIP archive.
     *
     * @param archive source RAR archive
     * @return temporary ZIP archive owned by the caller
     * @throws IOException if extraction, conversion, or temporary-file cleanup fails
     */
    File convert(File archive) throws IOException;
}
