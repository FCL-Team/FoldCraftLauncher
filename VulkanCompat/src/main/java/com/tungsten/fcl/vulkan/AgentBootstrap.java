/*
 * Fold Craft Launcher
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.tungsten.fcl.vulkan;

import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.net.URLClassLoader;

/** Keeps the agent's ASM private, avoiding other startup agents' older ASM copies. */
public final class AgentBootstrap {
    private AgentBootstrap() {
    }

    public static void premain(String args, Instrumentation instrumentation) {
        if (!Boolean.getBoolean("fcl.vulkan.handDepthFix") || Runtime.version().feature() != 25) return;
        try {
            URL location = AgentBootstrap.class.getProtectionDomain().getCodeSource().getLocation();
            // The transformer keeps this loader reachable for the JVM's lifetime.
            URLClassLoader loader = implementationLoader(location);
            Class<?> agent = loader.loadClass("com.tungsten.fcl.vulkan.HandDepthAgent");
            agent.getMethod("premain", Instrumentation.class).invoke(null, instrumentation);
        } catch (ReflectiveOperationException | RuntimeException failure) {
            System.err.println("[VulkanHandDepthFix] Agent unavailable; continuing without the workaround: " + failure);
        }
    }

    static URLClassLoader implementationLoader(URL location) {
        return new URLClassLoader(new URL[]{location}, ClassLoader.getPlatformClassLoader());
    }
}
