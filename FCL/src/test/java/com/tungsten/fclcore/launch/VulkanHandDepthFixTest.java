package com.tungsten.fclcore.launch;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class VulkanHandDepthFixTest {
    @Rule
    public TemporaryFolder temporary = new TemporaryFolder();

    @Test
    public void launchMustBeOptedInAndUseNativeSystemVulkanOnJava25() {
        String main = "net.minecraft.client.main.Main";
        assertTrue(VulkanHandDepthFix.isApplicable(true, true, "vulkan", 25, main));
        assertFalse(VulkanHandDepthFix.isApplicable(false, true, "vulkan", 25, main));
        assertFalse(VulkanHandDepthFix.isApplicable(true, false, "vulkan", 25, main));
        for (String backend : new String[]{"opengl", "default", null})
            assertFalse(VulkanHandDepthFix.isApplicable(true, true, backend, 25, main));
        for (int javaVersion : new int[]{8, 17, 21, 24, 26})
            assertFalse(VulkanHandDepthFix.isApplicable(true, true, "vulkan", javaVersion, main));
        for (String entry : new String[]{"net.fabricmc.loader.impl.launch.knot.KnotClient", "cpw.mods.bootstraplauncher.BootstrapLauncher", null})
            assertFalse(VulkanHandDepthFix.isApplicable(true, true, "vulkan", 25, entry));
    }

    @Test
    public void packagedAgentIsRefreshedWithoutTemporaryFiles() throws Exception {
        Path destination = temporary.getRoot().toPath().resolve("compatibility/agent.jar");
        VulkanHandDepthFix.extractAgent(new ByteArrayInputStream(new byte[]{1, 2, 3}), destination);
        assertArrayEquals(new byte[]{1, 2, 3}, Files.readAllBytes(destination));
        VulkanHandDepthFix.extractAgent(new ByteArrayInputStream(new byte[]{4, 5}), destination);
        assertArrayEquals(new byte[]{4, 5}, Files.readAllBytes(destination));
        try (var files = Files.list(destination.getParent())) {
            assertEquals(1, files.count());
        }
    }

    @Test
    public void failedExtractionKeepsPreviousAgentAndCleansTemporaryFile() throws Exception {
        Path destination = temporary.getRoot().toPath().resolve("agent.jar");
        Files.write(destination, new byte[]{9});
        InputStream broken = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Simulated asset read failure");
            }
        };
        assertThrows(IOException.class, () -> VulkanHandDepthFix.extractAgent(broken, destination));
        assertArrayEquals(new byte[]{9}, Files.readAllBytes(destination));
        try (var files = Files.list(destination.getParent())) {
            assertEquals(1, files.count());
        }
    }
}
