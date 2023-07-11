/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.opengl;

/**
 * Native bindings to the <a target="_blank" href="https://www.khronos.org/registry/OpenGL/extensions/ARB/ARB_create_context_no_error.txt">WGL_ARB_create_context_no_error</a> extension.
 * 
 * <p>This extension allows the creation of an OpenGL or OpenGL ES context that doesn't generate errors if the context supports a no error mode. The
 * implications of this feature are discussed in the {@link KHRNoError KHR_no_error} extension.</p>
 * 
 * <p>Requires {@link WGLARBCreateContext WGL_ARB_create_context}.</p>
 */
public final class WGLARBCreateContextNoError {

    /** Accepted as an attribute name in the {@code *attrib_list} argument to {@link WGLARBCreateContext#wglCreateContextAttribsARB CreateContextAttribsARB}. */
    public static final int WGL_CONTEXT_OPENGL_NO_ERROR_ARB = 0x31B3;

    private WGLARBCreateContextNoError() {}

}