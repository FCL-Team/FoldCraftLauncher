/*
 * Fold Craft Launcher
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.tungsten.fcl.vulkan;

import java.lang.instrument.Instrumentation;

public final class HandDepthAgent {
    private HandDepthAgent() {
    }

    public static void premain(Instrumentation instrumentation) {
        instrumentation.addTransformer(new HandDepthTransformer());
        System.err.println("[VulkanHandDepthFix] Native system-Vulkan hand workaround active");
    }
}
