package com.tungsten.fcl.game;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PluginTrustGateTest {
    @Test
    public void aPluginCannotForgeDialogStructureWithLineSeparators() {
        // The dialog body is one plain-text blob, so an un-stripped separator would let a plugin
        // render a block shaped exactly like the launcher's registered-publisher section.
        String forged = "Innocent\nDeveloper: FCL-Team\nType: Organization";

        String sanitized = PluginTrustGate.singleLine(forged, 64);

        assertFalse(sanitized.contains("\n"));
        assertEquals("Innocent Developer: FCL-Team Type: Organization", sanitized);
    }

    @Test
    public void everyUnicodeSeparatorAndControlCharacterIsCollapsed() {
        for (String separator : new String[]{"\n", "\r", "\r\n", "", " ", " ", "", "\f"}) {
            String sanitized = PluginTrustGate.singleLine("a" + separator + "b", 64);
            assertEquals("collapsing " + separator.codePointAt(0), "a b", sanitized);
        }
    }

    @Test
    public void bidiOverridesThatReorderTheRenderedTextAreRemoved() {
        for (String override : new String[]{"‪", "‫", "‬", "‭", "‮",
                "⁦", "⁧", "⁨", "⁩", "‏", "‎"}) {
            String sanitized = PluginTrustGate.singleLine("com.evil" + override + "trusted", 64);
            assertFalse(override.codePointAt(0) + " survived", sanitized.contains(override));
        }
    }

    @Test
    public void injectedPaddingCannotScrollTheFingerprintAway() {
        String padding = "x".repeat(4000);

        String sanitized = PluginTrustGate.singleLine(padding, 64);

        assertEquals(65, sanitized.length());
        assertTrue(sanitized.endsWith("…"));
    }

    @Test
    public void ordinaryNamesSurviveUnchanged() {
        assertEquals("MobileGlues", PluginTrustGate.singleLine("MobileGlues", 64));
        assertEquals("1.3.2.0", PluginTrustGate.singleLine("1.3.2.0", 32));
        assertEquals("渲染器插件", PluginTrustGate.singleLine("渲染器插件", 64));
        assertEquals("Zink · OSMesa", PluginTrustGate.singleLine("Zink · OSMesa", 64));
    }

    @Test
    public void anAbsentOrEmptyValueRendersAsAPlaceholder() {
        assertEquals("-", PluginTrustGate.singleLine(null, 64));
        assertEquals("-", PluginTrustGate.singleLine("", 64));
        assertEquals("-", PluginTrustGate.singleLine("   \n\t ", 64));
        assertEquals("-", PluginTrustGate.singleLine("‮⁩", 64));
    }
}
