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

public interface NV_float_buffer {

	/**
	 * Accepted by the &lt;internalformat&gt; parameter of TexImage2D and
	 * CopyTexImage2D:
	 */
	int GL_FLOAT_R_NV = 0x8880;
	int GL_FLOAT_RG_NV = 0x8881;
	int GL_FLOAT_RGB_NV = 0x8882;
	int GL_FLOAT_RGBA_NV = 0x8883;
	int GL_FLOAT_R16_NV = 0x8884;
	int GL_FLOAT_R32_NV = 0x8885;
	int GL_FLOAT_RG16_NV = 0x8886;
	int GL_FLOAT_RG32_NV = 0x8887;
	int GL_FLOAT_RGB16_NV = 0x8888;
	int GL_FLOAT_RGB32_NV = 0x8889;
	int GL_FLOAT_RGBA16_NV = 0x888A;
	int GL_FLOAT_RGBA32_NV = 0x888B;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetTexLevelParameterfv and
	 * GetTexLevelParameteriv:
	 */
	int GL_TEXTURE_FLOAT_COMPONENTS_NV = 0x888C;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 * and GetDoublev:
	 */
	int GL_FLOAT_CLEAR_COLOR_VALUE_NV = 0x888D;
	int GL_FLOAT_RGBA_MODE_NV = 0x888E;

	/**
	 * Accepted in the &lt;piAttributes&gt; array of wglGetPixelFormatAttribivARB and
	 * wglGetPixelFormatAttribfvARB and in the &lt;piAttribIList&gt; and
	 * &lt;pfAttribFList&gt; arrays of wglChoosePixelFormatARB:
	 */
	/*
	 int WGL_FLOAT_COMPONENTS_NV = 0x20B0;
	 int WGL_BIND_TO_TEXTURE_RECTANGLE_FLOAT_R_NV = 0x20B1;
	 int WGL_BIND_TO_TEXTURE_RECTANGLE_FLOAT_RG_NV = 0x20B2;
	 int WGL_BIND_TO_TEXTURE_RECTANGLE_FLOAT_RGB_NV = 0x20B3;
	 int WGL_BIND_TO_TEXTURE_RECTANGLE_FLOAT_RGBA_NV = 0x20B4;
	 */

	/**
	 * Accepted in the &lt;piAttribIList&gt; array of wglCreatePbufferARB and returned
	 * in the &lt;value&gt; parameter of wglQueryPbufferARB when &lt;iAttribute&gt; is
	 * WGL_TEXTURE_FORMAT_ARB:
	 */
	/*
	 int WGL_TEXTURE_FLOAT_R_NV = 0x20B5;
	 int WGL_TEXTURE_FLOAT_RG_NV = 0x20B6;
	 int WGL_TEXTURE_FLOAT_RGB_NV = 0x20B7;
	 int WGL_TEXTURE_FLOAT_RGBA_NV = 0x20B8;
	 */

}
