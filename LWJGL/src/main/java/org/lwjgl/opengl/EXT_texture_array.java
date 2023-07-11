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

import org.lwjgl.util.generator.Reuse;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLuint;

public interface EXT_texture_array {

	/**
	 * Accepted by the &lt;target&gt; parameter of TexParameteri, TexParameteriv,
	 * TexParameterf, TexParameterfv, and BindTexture:
	 */
	int GL_TEXTURE_1D_ARRAY_EXT = 0x8C18;
	int GL_TEXTURE_2D_ARRAY_EXT = 0x8C1A;

	/**
	 * Accepted by the &lt;target&gt; parameter of TexImage3D, TexSubImage3D,
	 * CopyTexSubImage3D, CompressedTexImage3D, and CompressedTexSubImage3D:
	 */
	int GL_PROXY_TEXTURE_2D_ARRAY_EXT = 0x8C1B;

	/**
	 * Accepted by the &lt;target&gt; parameter of TexImage2D, TexSubImage2D,
	 * CopyTexImage2D, CopyTexSubImage2D, CompressedTexImage2D, and
	 * CompressedTexSubImage2D:
	 */
	int GL_PROXY_TEXTURE_1D_ARRAY_EXT = 0x8C19;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv
	 * and GetFloatv:
	 */
	int GL_TEXTURE_BINDING_1D_ARRAY_EXT = 0x8C1C;
	int GL_TEXTURE_BINDING_2D_ARRAY_EXT = 0x8C1D;
	int GL_MAX_ARRAY_TEXTURE_LAYERS_EXT = 0x88FF;

	/**
	 * Accepted by the &lt;param&gt; parameter of TexParameterf, TexParameteri,
	 * TexParameterfv, and TexParameteriv when the &lt;pname&gt; parameter is
	 * TEXTURE_COMPARE_MODE_ARB:
	 */
	int GL_COMPARE_REF_DEPTH_TO_TEXTURE_EXT = 0x884E;

	/**
	 * Accepted by the &lt;pname&gt; parameter of
	 * GetFramebufferAttachmentParameterivEXT:
	 */
	int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER_EXT = 0x8CD4;

	/** Returned by the &lt;type&gt; parameter of GetActiveUniform: */
	int GL_SAMPLER_1D_ARRAY_EXT = 0x8DC0;
	int GL_SAMPLER_2D_ARRAY_EXT = 0x8DC1;
	int GL_SAMPLER_1D_ARRAY_SHADOW_EXT = 0x8DC3;
	int GL_SAMPLER_2D_ARRAY_SHADOW_EXT = 0x8DC4;

	@Reuse("EXTGeometryShader4")
	void glFramebufferTextureLayerEXT(@GLenum int target, @GLenum int attachment, @GLuint int texture, int level, int layer);

}