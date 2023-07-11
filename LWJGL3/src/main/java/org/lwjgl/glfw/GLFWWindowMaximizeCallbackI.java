/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.glfw;

import org.lwjgl.system.*;

import static org.lwjgl.system.dyncall.DynCallback.*;

/**
 * Instances of this interface may be passed to the {@link GLFW#glfwSetWindowMaximizeCallback SetWindowMaximizeCallback} method.
 * 
 * <h3>Type</h3>
 * 
 * <pre><code>
 * void (*) (
 *     GLFWwindow *window,
 *     int maximized
 * )</code></pre>
 *
 * @since version 3.3
 */
@FunctionalInterface
@NativeType("GLFWwindowmaximizefun")
public interface GLFWWindowMaximizeCallbackI extends CallbackI.V {

    String SIGNATURE = "(pi)v";

    @Override
    default String getSignature() { return SIGNATURE; }

    @Override
    default void callback(long args) {
        invoke(
            dcbArgPointer(args),
            dcbArgInt(args) != 0
        );
    }

    /**
     * Will be called when the specified window is maximized or restored.
     *
     * @param window    the window that was maximized or restored.
     * @param maximized {@link GLFW#GLFW_TRUE TRUE} if the window was maximized, or {@link GLFW#GLFW_FALSE FALSE} if it was restored
     */
    void invoke(@NativeType("GLFWwindow *") long window, @NativeType("int") boolean maximized);

}