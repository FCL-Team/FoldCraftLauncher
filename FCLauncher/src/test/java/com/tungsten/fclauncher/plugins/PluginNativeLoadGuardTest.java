package com.tungsten.fclauncher.plugins;

import com.tungsten.verifiedpluginload.model.TrustSource;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PluginNativeLoadGuardTest {
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
}
