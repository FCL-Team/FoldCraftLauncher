/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system.fcl;

import org.lwjgl.system.*;

import javax.annotation.*;
import java.nio.*;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.fcl.DynamicLinkLoader.*;

/** Implements a {@link SharedLibrary} on the Linux OS. */
public class FCLLibrary extends SharedLibrary.Default {

    public FCLLibrary(String name) {
        this(name, loadLibrary(name));
    }

    public FCLLibrary(String name, long handle) {
        super(name, handle);
    }

    private static long loadLibrary(String name) {
        long handle;
        try (MemoryStack stack = stackPush()) {
            handle = dlopen(stack.UTF8(name), RTLD_LAZY | RTLD_LOCAL);
        }
        if (handle == NULL) {
            throw new UnsatisfiedLinkError("Failed to dynamically load library: " + name + "(error = " + dlerror() + ")");
        }
        return handle;
    }

    @Nullable
    @Override
    public String getPath() {
        return SharedLibraryUtil.getLibraryPath(address());
    }

    @Override
    public long getFunctionAddress(ByteBuffer functionName) {
        return dlsym(address(), functionName);
    }

    @Override
    public void free() {
        dlclose(address());
    }

}