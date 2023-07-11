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

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.GLbitfield;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.FloatBuffer;

@Extension(postfix = "")
public interface ARB_texture_multisample {

	/** Accepted by the &lt;pname&gt; parameter of GetMultisamplefv: */
	int GL_SAMPLE_POSITION = 0x8E50;

	/**
	 * Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled, and by
	 * the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 * GetDoublev:
	 */
	int GL_SAMPLE_MASK = 0x8E51;

	/**
	 * Accepted by the &lt;target&gt; parameter of GetBooleani_v and
	 * GetIntegeri_v:
	 */
	int GL_SAMPLE_MASK_VALUE = 0x8E52;

	/**
	 * Accepted by the &lt;target&gt; parameter of BindTexture and
	 * TexImage2DMultisample:
	 */
	int GL_TEXTURE_2D_MULTISAMPLE = 0x9100;

	/** Accepted by the &lt;target&gt; parameter of TexImage2DMultisample: */
	int GL_PROXY_TEXTURE_2D_MULTISAMPLE = 0x9101;

	/**
	 * Accepted by the &lt;target&gt; parameter of BindTexture and
	 * TexImage3DMultisample:
	 */
	int GL_TEXTURE_2D_MULTISAMPLE_ARRAY = 0x9102;

	/** Accepted by the &lt;target&gt; parameter of TexImage3DMultisample: */
	int GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY = 0x9103;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 * and GetFloatv:
	 */
	int GL_MAX_SAMPLE_MASK_WORDS = 0x8E59;
	int GL_MAX_COLOR_TEXTURE_SAMPLES = 0x910E;
	int GL_MAX_DEPTH_TEXTURE_SAMPLES = 0x910F;
	int GL_MAX_INTEGER_SAMPLES = 0x9110;
	int GL_TEXTURE_BINDING_2D_MULTISAMPLE = 0x9104;
	int GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY = 0x9105;

	/** Accepted by the &lt;pname&gt; parameter of GetTexLevelParameter */
	int GL_TEXTURE_SAMPLES = 0x9106;
	int GL_TEXTURE_FIXED_SAMPLE_LOCATIONS = 0x9107;

	/** Returned by the &lt;type&gt; parameter of GetActiveUniform: */
	int GL_SAMPLER_2D_MULTISAMPLE = 0x9108;
	int GL_INT_SAMPLER_2D_MULTISAMPLE = 0x9109;
	int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE = 0x910A;
	int GL_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910B;
	int GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910C;
	int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910D;

	@Reuse("GL32")
	void glTexImage2DMultisample(@GLenum int target, @GLsizei int samples, int internalformat,
	                             @GLsizei int width, @GLsizei int height,
	                             boolean fixedsamplelocations);

	@Reuse("GL32")
	void glTexImage3DMultisample(@GLenum int target, @GLsizei int samples, int internalformat,
	                             @GLsizei int width, @GLsizei int height, @GLsizei int depth,
	                             boolean fixedsamplelocations);

	@Reuse("GL32")
	@StripPostfix("val")
	void glGetMultisamplefv(@GLenum int pname, @GLuint int index, @OutParameter @Check("2") FloatBuffer val);

	@Reuse("GL32")
	void glSampleMaski(@GLuint int index, @GLbitfield int mask);

}