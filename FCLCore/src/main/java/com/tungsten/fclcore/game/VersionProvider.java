package com.tungsten.fclcore.game;

/**
 * Supports version accessing.
 *
 * @see Version#resolve
 */
public interface VersionProvider {

    /**
     * Does the version of id exist?
     *
     * @param id the id of version
     * @return true if the version exists
     */
    boolean hasVersion(String id);

    /**
     * Get the version
     *
     * @param id the id of version
     * @return the version you want
     */
    Version getVersion(String id) throws VersionNotFoundException;
}
