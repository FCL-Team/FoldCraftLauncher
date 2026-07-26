package com.tungsten.fclauncher.plugins;

import com.vpl.verifiedpluginload.model.TrustSource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertFalse;
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
    public void explicitKeyTrustRequiresTheUntrustedPluginSetting() {
        assertFalse(PluginNativeLoadGuard.isExplicitKeyTrustAllowed(TrustSource.KEY, false));
        assertTrue(PluginNativeLoadGuard.isExplicitKeyTrustAllowed(TrustSource.KEY, true));
        assertTrue(PluginNativeLoadGuard.isExplicitKeyTrustAllowed(TrustSource.AUTHOR, false));
        assertTrue(PluginNativeLoadGuard.isExplicitKeyTrustAllowed(null, false));
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

        // A driver name is resolved out of LIBGL_DRIVERS_PATH, which is itself constrained above.
        PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                "Renderer", pluginDirectory.getPath(), "MESA_LOADER_DRIVER_OVERRIDE", "zink");
        PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                "Renderer", pluginDirectory.getPath(), "LIBGL_ES", "3");
        PluginNativeLoadGuard.verifyPluginDeclaredEnvironment(
                "Renderer", pluginDirectory.getPath(), "POJAV_RENDERER", "opengles3");
    }
}
