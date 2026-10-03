/*
 * Fold Craft Launcher
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.tungsten.fclcore.launch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Launch gating and private-cache extraction for the bundled hand-depth agent. */
public final class VulkanHandDepthFix {
    private VulkanHandDepthFix() {
    }

    /** Never inject into GL, custom drivers, other Java versions or mod-loader entry points. */
    public static boolean isApplicable(boolean enabled, boolean systemDriver, String backend,
                                       int javaVersion, String mainClass) {
        return enabled && systemDriver && "vulkan".equals(backend) && javaVersion == 25
                && "net.minecraft.client.main.Main".equals(mainClass);
    }

    /** Atomically refresh the generated private agent; the caller owns/closes the asset stream. */
    public static void extractAgent(InputStream source, Path destination) throws IOException {
        Path target = destination.toAbsolutePath().normalize();
        Files.createDirectories(target.getParent());
        Path temporary = Files.createTempFile(target.getParent(), "vulkan-hand-depth-", ".jar");
        try {
            Files.copy(source, temporary, StandardCopyOption.REPLACE_EXISTING);
            try {
                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ignored) {
                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}
