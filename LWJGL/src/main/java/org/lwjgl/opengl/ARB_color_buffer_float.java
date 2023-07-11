/*
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lwjgl.opengl;

import org.lwjgl.util.generator.opengl.GLenum;

public interface ARB_color_buffer_float {

	/**
	 * Accepted by the &lt;pname&gt; parameters of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	int GL_RGBA_FLOAT_MODE_ARB = 0x8820;

	/**
	 * Accepted by the &lt;target&gt; parameter of ClampColorARB and the &lt;pname&gt;
	 * parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev.
	 */
	int GL_CLAMP_VERTEX_COLOR_ARB = 0x891A;
	int GL_CLAMP_FRAGMENT_COLOR_ARB = 0x891B;
	int GL_CLAMP_READ_COLOR_ARB = 0x891C;

	/** Accepted by the &lt;clamp&gt; parameter of ClampColorARB. */
	int GL_FIXED_ONLY_ARB = 0x891D;

	/**
	 * Accepted as a value in the &lt;piAttribIList&gt; and &lt;pfAttribFList&gt;
	 * parameter arrays of wglChoosePixelFormatARB, and returned in the
	 * &lt;piValues&gt; parameter array of wglGetPixelFormatAttribivARB, and the
	 * &lt;pfValues&gt; parameter array of wglGetPixelFormatAttribfvARB:
	 */
	int WGL_TYPE_RGBA_FLOAT_ARB = 0x21A0;

	/**
	 * Accepted as values of the &lt;render_type&gt; arguments in the
	 * glXCreateNewContext and glXCreateContext functions
	 */
	int GLX_RGBA_FLOAT_TYPE = 0x20B9;

	/** Accepted as a bit set in the GLX_RENDER_TYPE variable */
	int GLX_RGBA_FLOAT_BIT = 0x00000004;

	void glClampColorARB(@GLenum int target, @GLenum int clamp);

}