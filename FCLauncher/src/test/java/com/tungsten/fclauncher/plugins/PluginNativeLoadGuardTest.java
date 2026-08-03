package com.tungsten.fclauncher.plugins;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PluginNativeLoadGuardTest {
    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void customEnvironmentCannotOverrideNativeLoadInputs() {
        assertTrue(PluginNativeLoadGuard.isProtectedNativeEnvironmentVariable("LD_LIBRARY_PATH"));
        assertTrue(PluginNativeLoadGuard.isProtectedNativeEnvironmentVariable("LD_PRELOAD"));
        assertTrue(PluginNativeLoadGuard.isProtectedNativeEnvironmentVariable("POJAVEXEC_EGL"));
        assertTrue(PluginNativeLoadGuard.isProtectedNativeEnvironmentVariable("DRIVER_PATH"));
        assertTrue(PluginNativeLoadGuard.isProtectedNativeEnvironmentVariable("VK_ICD_FILENAMES"));
        assertTrue(PluginNativeLoadGuard.isProtectedNativeEnvironmentVariable("TMPDIR"));
        assertTrue(PluginNativeLoadGuard.isProtectedNativeEnvironmentVariable("POJAV_NATIVEDIR"));
    }

    @Test
    public void unrelatedCustomEnvironmentRemainsSupported() {
        assertFalse(PluginNativeLoadGuard.isProtectedNativeEnvironmentVariable("JAVA_TOOL_OPTIONS"));
        assertFalse(PluginNativeLoadGuard.isProtectedNativeEnvironmentVariable("POJAV_RENDERER"));
        assertFalse(PluginNativeLoadGuard.isProtectedNativeEnvironmentVariable("CUSTOM_GAME_FLAG"));
    }

    @Test
    public void pluginDeclaredPathMayStayInsideItsOwnLibraryDirectory() throws IOException {
        File pluginDirectory = temporaryFolder.newFolder("plugin-lib");
        String inside = new File(pluginDirectory, "vulkan.json").getPath();

        PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                "Renderer", pluginDirectory.getPath(), "VK_ICD_FILENAMES", inside);
        PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                "Renderer", pluginDirectory.getPath(), "LD_LIBRARY_PATH", pluginDirectory.getPath());
    }

    @Test
    public void pluginDeclaredPathCannotEscapeItsOwnLibraryDirectory() throws IOException {
        File pluginDirectory = temporaryFolder.newFolder("plugin-lib");
        File shared = temporaryFolder.newFolder("shared-storage");

        for (String key : new String[]{
                "VK_ICD_FILENAMES", "VK_LAYER_PATH", "VK_ADD_LAYER_PATH", "VK_DRIVER_FILES",
                "VK_ADD_DRIVER_FILES", "LIBGL_DRIVERS_PATH", "LD_PRELOAD", "LD_LIBRARY_PATH", "PATH"
        }) {
            String value = new File(shared, "evil.so").getPath();
            assertThrows(key, IOException.class, () -> PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                    "Renderer", pluginDirectory.getPath(), key, value));
        }
    }

    @Test
    public void pluginDeclaredPathCannotSmuggleAnOutsideEntryIntoAPathList() throws IOException {
        File pluginDirectory = temporaryFolder.newFolder("plugin-lib");
        File shared = temporaryFolder.newFolder("shared-storage");

        assertThrows(IOException.class, () -> PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                "Renderer",
                pluginDirectory.getPath(),
                "LD_LIBRARY_PATH",
                pluginDirectory.getPath() + ":" + shared.getPath()));
    }

    @Test
    public void pluginCannotReplaceLauncherOwnedVariables() throws IOException {
        File pluginDirectory = temporaryFolder.newFolder("plugin-lib");

        for (String key : new String[]{
                "TMPDIR", "FCL_NATIVEDIR", "POJAV_NATIVEDIR", "MOD_ANDROID_RUNTIME",
                "RENDERER_HANDLE", "VULKAN_PTR"
        }) {
            assertThrows(key, IOException.class, () -> PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                    "Native plugin", pluginDirectory.getPath(), key, pluginDirectory.getPath()));
        }
    }

    @Test
    public void passthroughRendererMayStillPointAtReadOnlySystemDrivers() throws IOException {
        File pluginDirectory = temporaryFolder.newFolder("plugin-lib");

        PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                "Renderer", pluginDirectory.getPath(), "LIBGL_DRIVERS_PATH", "/vendor/lib64/egl");
        PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                "Renderer",
                pluginDirectory.getPath(),
                "LD_LIBRARY_PATH",
                pluginDirectory.getPath() + ":/system/lib64:/vendor/lib64/hw");
    }

    @Test
    public void aSystemLookalikePathIsNotTreatedAsASystemPath() throws IOException {
        File pluginDirectory = temporaryFolder.newFolder("plugin-lib");

        assertThrows(IOException.class, () -> PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                "Renderer", pluginDirectory.getPath(), "LIBGL_DRIVERS_PATH", "/systemx/lib64"));
        assertThrows(IOException.class, () -> PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                "Renderer", pluginDirectory.getPath(), "LIBGL_DRIVERS_PATH", "/vendor/../sdcard/lib"));
    }

    @Test
    public void aSymlinkOutOfThePluginDirectoryDoesNotPassAsAnInsidePath() throws IOException {
        File pluginDirectory = temporaryFolder.newFolder("plugin-lib");
        File shared = temporaryFolder.newFolder("shared-storage");
        File escape = new File(pluginDirectory, "drivers");
        try {
            Files.createSymbolicLink(escape.toPath(), shared.toPath());
        } catch (UnsupportedOperationException | IOException unsupported) {
            return; // The filesystem under test cannot express this; the check itself is unchanged.
        }

        assertThrows(IOException.class, () -> PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                "Renderer", pluginDirectory.getPath(), "LIBGL_DRIVERS_PATH", escape.getPath()));
    }

    @Test
    public void pluginKeepsUnrelatedTuningVariables() throws IOException {
        File pluginDirectory = temporaryFolder.newFolder("plugin-lib");

        PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                "Renderer", pluginDirectory.getPath(), "LIBGL_ES", "3");
        PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                "Renderer", pluginDirectory.getPath(), "POJAV_RENDERER", "opengles3");
    }

    @Test
    public void aDriverNameIsAcceptedOnlyAsABareName() throws IOException {
        File pluginDirectory = temporaryFolder.newFolder("plugin-lib");

        for (String key : new String[]{"MESA_LOADER_DRIVER_OVERRIDE", "GALLIUM_DRIVER"}) {
            PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                    "Renderer", pluginDirectory.getPath(), key, "zink");

            // Mesa concatenates this into "<search dir>/<name>_dri.so" without rejecting separators,
            // so a name carrying a traversal escapes the directory constrained by LIBGL_DRIVERS_PATH.
            for (String escape : new String[]{
                    "../../../../../../sdcard/payload", "/sdcard/payload", "a/b", "zink\u0000x", ""
            }) {
                assertThrows(key + " <- " + escape, IOException.class,
                        () -> PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                                "Renderer", pluginDirectory.getPath(), key, escape));
            }
        }
    }

    @Test
    public void pointerCarryingVariablesAreLauncherOwned() {
        // Native constructors adopt these as raw pointers via strtoul, so no caller may supply one.
        assertTrue(PluginNativeLoadGuard.isProtectedNativeEnvironmentVariable("POJAV_ENVIRON"));
        assertTrue(PluginNativeLoadGuard.isProtectedNativeEnvironmentVariable("FCL_ENVIRON"));
    }

    @Test
    public void everyProtectedVariableIsClassifiedForPlugins() throws IOException {
        File pluginDirectory = temporaryFolder.newFolder("plugin-lib");
        // A protected variable that belongs to no bucket would be silently passed through to native
        // code. Assert that no such variable exists by requiring each one to be actively rejected
        // when a plugin declares an escaping value for it.
        for (String key : PluginNativeLoadGuard.protectedNativeEnvironmentVariablesForTest()) {
            assertThrows("unclassified protected variable: " + key, IOException.class,
                    () -> PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                            "Renderer", pluginDirectory.getPath(), key, "/sdcard/attacker/payload"));
        }
    }

    @Test
    public void theProtectedSetIsPartitionedByExactlyOnePluginPolicy() {
        // The runtime backstop throws on an unclassified protected variable; this keeps the partition
        // total by construction, so a newly protected variable fails here rather than at launch.
        for (String key : PluginNativeLoadGuard.protectedNativeEnvironmentVariablesForTest()) {
            assertNotEquals("unclassified protected variable: " + key,
                    PluginNativeLoadGuard.PluginEnvironmentPolicy.UNCLASSIFIED,
                    PluginNativeLoadGuard.pluginEnvironmentPolicy(key));
            assertNotEquals("protected variable treated as unprotected: " + key,
                    PluginNativeLoadGuard.PluginEnvironmentPolicy.UNPROTECTED,
                    PluginNativeLoadGuard.pluginEnvironmentPolicy(key));
        }
        assertEquals(PluginNativeLoadGuard.PluginEnvironmentPolicy.NATIVE_PATH,
                PluginNativeLoadGuard.pluginEnvironmentPolicy("LD_PRELOAD"));
        assertEquals(PluginNativeLoadGuard.PluginEnvironmentPolicy.UNPROTECTED,
                PluginNativeLoadGuard.pluginEnvironmentPolicy("LIBGL_ES"));
    }

    @Test
    public void everyConsumerParsesAPluginEntryIdentically() {
        // The guard authorized split[1]; the loader and the exporter must see the same string.
        assertArrayEquals(new String[]{"DLOPEN", "a.so=b"},
                PluginNativeLoadGuard.parsePluginEnvironmentEntry("DLOPEN=a.so=b"));
        assertArrayEquals(new String[]{"LIBGL_ES", "3"},
                PluginNativeLoadGuard.parsePluginEnvironmentEntry("LIBGL_ES=3"));
        assertNull(PluginNativeLoadGuard.parsePluginEnvironmentEntry("DLOPEN"));
        assertNull(PluginNativeLoadGuard.parsePluginEnvironmentEntry("DLOPEN="));
        assertNull(PluginNativeLoadGuard.parsePluginEnvironmentEntry("=value"));
        assertNull(PluginNativeLoadGuard.parsePluginEnvironmentEntry(null));
    }
}
