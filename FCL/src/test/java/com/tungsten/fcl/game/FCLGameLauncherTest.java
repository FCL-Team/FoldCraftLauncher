package com.tungsten.fcl.game;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * fml.toml 的 earlyWindowControl 合并逻辑：
 * 关闭早期加载屏时把关闭键置于文件头部并移除同名键，无需关闭时只移除残留的关闭键，
 * 其余内容原样保留。
 */
public class FCLGameLauncherTest {

    @Test
    public void writesDisabledKeyForVulkanBackend() {
        assertEquals("earlyWindowControl = false\n", FCLGameLauncher.buildFmlTomlContent("", true));
    }

    @Test
    public void keepsDisabledKeyUnchanged() {
        String content = "earlyWindowControl = false\n";
        assertEquals(content, FCLGameLauncher.buildFmlTomlContent(content, true));
    }

    @Test
    public void keepsRootDisabledKeyLayout() {
        String content = "# comment\nearlyWindowControl = false\nfoo = 1\n";
        assertEquals(content, FCLGameLauncher.buildFmlTomlContent(content, true));
    }

    @Test
    public void movesDisabledKeyOutOfTableScope() {
        String content = "[table]\nearlyWindowControl = false\n";
        assertEquals("earlyWindowControl = false\n[table]\n",
                FCLGameLauncher.buildFmlTomlContent(content, true));
    }

    @Test
    public void overridesEnabledKey() {
        String content = "earlyWindowControl = true\n# comment\nfoo = 1\n";
        assertEquals("earlyWindowControl = false\n# comment\nfoo = 1\n",
                FCLGameLauncher.buildFmlTomlContent(content, true));
    }

    @Test
    public void removesKeysInsideTableScope() {
        String content = "earlyWindowControl = false\n[table]\nearlyWindowControl = true\n";
        assertEquals("earlyWindowControl = false\n[table]\n",
                FCLGameLauncher.buildFmlTomlContent(content, true));
    }

    @Test
    public void removesLeftoverKeyForOlderVersions() {
        String content = "earlyWindowControl = false\nfoo = 1\n";
        assertEquals("foo = 1\n", FCLGameLauncher.buildFmlTomlContent(content, false));
    }

    @Test
    public void keepsContentWithoutKeyForOlderVersions() {
        String content = "foo = 1\n[table]\nbar = 2\n";
        assertEquals(content, FCLGameLauncher.buildFmlTomlContent(content, false));
    }
}
