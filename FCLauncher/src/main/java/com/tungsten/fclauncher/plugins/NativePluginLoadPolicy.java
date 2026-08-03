package com.tungsten.fclauncher.plugins;

import com.tungsten.fclauncher.FCLConfig;

import java.io.IOException;

/** Host-provided policy applied immediately before native plugin paths are used. */
public interface NativePluginLoadPolicy {
    boolean isEnabled();

    void verify(FCLConfig config) throws IOException;

    NativePluginLoadPolicy NO_OP = new NativePluginLoadPolicy() {
        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public void verify(FCLConfig config) throws IOException {
        }
    };
}
