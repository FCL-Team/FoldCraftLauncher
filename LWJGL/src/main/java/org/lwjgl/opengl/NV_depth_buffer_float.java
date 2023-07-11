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

public interface NV_depth_buffer_float {

	/**
	 * Accepted by the &lt;internalformat&gt; parameter of TexImage1D, TexImage2D,
	 * TexImage3D, CopyTexImage1D, CopyTexImage2D, and RenderbufferStorageEXT,
	 * and returned in the &lt;data&gt; parameter of GetTexLevelParameter and
	 * GetRenderbufferParameterivEXT:
	 */
	int GL_DEPTH_COMPONENT32F_NV = 0x8DAB;
	int GL_DEPTH32F_STENCIL8_NV = 0x8DAC;

	/**
	 * Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels, TexImage1D,
	 * TexImage2D, TexImage3D, TexSubImage1D, TexSubImage2D, TexSubImage3D, and
	 * GetTexImage:
	 */
	int GL_FLOAT_32_UNSIGNED_INT_24_8_REV_NV = 0x8DAD;

	/**
	 * Accepted by the &lt;pname&gt; parameters of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	int GL_DEPTH_BUFFER_FLOAT_MODE_NV = 0x8DAF;

	void glDepthRangedNV(double n, double f);

	void glClearDepthdNV(double d);

	void glDepthBoundsdNV(double zmin, double zmax);

}