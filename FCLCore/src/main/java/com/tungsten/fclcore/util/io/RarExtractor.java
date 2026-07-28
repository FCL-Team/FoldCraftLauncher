package com.tungsten.fclcore.util.io;

import java.io.File;
import java.io.IOException;

/**
 * Extracts a supported single-volume RAR archive while enforcing archive-entry safety rules.
 */
public interface RarExtractor {

    /**
     * Extracts an unencrypted RAR4 or RAR5 archive.
     *
     * @param archive source RAR archive
     * @param destination extraction directory
     * @throws IOException if the archive is invalid, unsupported, unsafe, or cannot be extracted
     */
    void extract(File archive, File destination) throws IOException;
}
