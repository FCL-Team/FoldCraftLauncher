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

public interface ARB_texture_cube_map_array {

	/**
	 * Accepted by the &lt;target&gt; parameter of TexParameteri, TexParameteriv,
	 * TexParameterf, TexParameterfv, BindTexture, and GenerateMipmap:
	 * <p/>
	 * Accepted by the &lt;target&gt; parameter of TexImage3D, TexSubImage3D,
	 * CompressedTeximage3D, CompressedTexSubImage3D and CopyTexSubImage3D:
	 * <p/>
	 * Accepted by the &lt;tex&gt; parameter of GetTexImage:
	 */
	int GL_TEXTURE_CUBE_MAP_ARRAY_ARB = 0x9009;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev,
	 * GetIntegerv and GetFloatv:
	 */
	int GL_TEXTURE_BINDING_CUBE_MAP_ARRAY_ARB = 0x900A;

	/**
	 * Accepted by the &lt;target&gt; parameter of TexImage3D, TexSubImage3D,
	 * CompressedTeximage3D, CompressedTexSubImage3D and CopyTexSubImage3D:
	 */
	int GL_PROXY_TEXTURE_CUBE_MAP_ARRAY_ARB = 0x900B;

	/** Returned by the &lt;type&gt; parameter of GetActiveUniform: */
	int GL_SAMPLER_CUBE_MAP_ARRAY_ARB = 0x900C;
	int GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW_ARB = 0x900D;
	int GL_INT_SAMPLER_CUBE_MAP_ARRAY_ARB = 0x900E;
	int GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY_ARB = 0x900F;

}