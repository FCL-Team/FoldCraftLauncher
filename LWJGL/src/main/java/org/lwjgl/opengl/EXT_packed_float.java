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

public interface EXT_packed_float {

	/**
	 * Accepted by the &lt;internalformat&gt; parameter of TexImage1D,
	 * TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 * RenderbufferStorageEXT:
	 */
	int GL_R11F_G11F_B10F_EXT = 0x8C3A;

	/**
	 * Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels,
	 * TexImage1D, TexImage2D, GetTexImage, TexImage3D, TexSubImage1D,
	 * TexSubImage2D, TexSubImage3D, GetHistogram, GetMinmax,
	 * ConvolutionFilter1D, ConvolutionFilter2D, ConvolutionFilter3D,
	 * GetConvolutionFilter, SeparableFilter2D, GetSeparableFilter,
	 * ColorTable, ColorSubTable, and GetColorTable:
	 */
	int GL_UNSIGNED_INT_10F_11F_11F_REV_EXT = 0x8C3B;

	/**
	 * Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv, and
	 * GetDoublev:
	 */
	int GL_RGBA_SIGNED_COMPONENTS_EXT = 0x8C3C;

	/**
	 * Accepted as a value in the &lt;piAttribIList&gt; and &lt;pfAttribFList&gt;
	 * parameter arrays of wglChoosePixelFormatARB, and returned in the
	 * &lt;piValues&gt; parameter array of wglGetPixelFormatAttribivARB, and the
	 * &lt;pfValues&gt; parameter array of wglGetPixelFormatAttribfvARB:
	 */
	int WGL_TYPE_RGBA_UNSIGNED_FLOAT_EXT = 0x20A8;

	/**
	 * Accepted as values of the &lt;render_type&gt; arguments in the
	 * glXCreateNewContext and glXCreateContext functions
	 */
	int GLX_RGBA_UNSIGNED_FLOAT_TYPE_EXT = 0x20B1;

	/**
	 * Returned by glXGetFBConfigAttrib (when &lt;attribute&gt; is set to
	 * GLX_RENDER_TYPE) and accepted by the &lt;attrib_list&gt; parameter of
	 * glXChooseFBConfig (following the GLX_RENDER_TYPE token):
	 */
	int GLX_RGBA_UNSIGNED_FLOAT_BIT_EXT = 0x00000008;

}