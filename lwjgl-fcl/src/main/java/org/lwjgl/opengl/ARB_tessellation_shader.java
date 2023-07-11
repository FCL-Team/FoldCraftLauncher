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
import org.lwjgl.util.generator.opengl.GLenum;

import java.nio.FloatBuffer;

@Extension(postfix = "")
public interface ARB_tessellation_shader {

	/**
	 * Accepted by the &lt;mode&gt; parameter of Begin and all vertex array functions
	 * that implicitly call Begin:
	 */
	int GL_PATCHES = 0xE;

	/**
	 * Accepted by the &lt;pname&gt; parameter of PatchParameteri, GetBooleanv,
	 * GetDoublev, GetFloatv, GetIntegerv, and GetInteger64v:
	 */
	int GL_PATCH_VERTICES = 0x8E72;

	/**
	 * Accepted by the &lt;pname&gt; parameter of PatchParameterfv, GetBooleanv,
	 * GetDoublev, GetFloatv, and GetIntegerv, and GetInteger64v:
	 */
	int GL_PATCH_DEFAULT_INNER_LEVEL = 0x8E73;
	int GL_PATCH_DEFAULT_OUTER_LEVEL = 0x8E74;

	/** Accepted by the &lt;pname&gt; parameter of GetProgramiv: */
	int GL_TESS_CONTROL_OUTPUT_VERTICES = 0x8E75;
	int GL_TESS_GEN_MODE = 0x8E76;
	int GL_TESS_GEN_SPACING = 0x8E77;
	int GL_TESS_GEN_VERTEX_ORDER = 0x8E78;
	int GL_TESS_GEN_POINT_MODE = 0x8E79;

	/** Returned by GetProgramiv when &lt;pname&gt; is TESS_GEN_MODE: */
	int GL_ISOLINES = 0x8E7A;

	/** Returned by GetProgramiv when &lt;pname&gt; is TESS_GEN_SPACING: */
	int GL_FRACTIONAL_ODD = 0x8E7B;
	int GL_FRACTIONAL_EVEN = 0x8E7C;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetFloatv,
	 * GetIntegerv, and GetInteger64v:
	 */
	int GL_MAX_PATCH_VERTICES = 0x8E7D;
	int GL_MAX_TESS_GEN_LEVEL = 0x8E7E;
	int GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS = 0x8E7F;
	int GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8E80;
	int GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS = 0x8E81;
	int GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS = 0x8E82;
	int GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS = 0x8E83;
	int GL_MAX_TESS_PATCH_COMPONENTS = 0x8E84;
	int GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS = 0x8E85;
	int GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS = 0x8E86;
	int GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS = 0x8E89;
	int GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS = 0x8E8A;
	int GL_MAX_TESS_CONTROL_INPUT_COMPONENTS = 0x886C;
	int GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS = 0x886D;
	int GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS = 0x8E1E;
	int GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8E1F;

	/** Accepted by the &lt;pname&gt; parameter of GetActiveUniformBlockiv: */
	int GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER = 0x84F0;
	int GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x84F1;

	/**
	 * Accepted by the &lt;type&gt; parameter of CreateShader and returned by the
	 * &lt;params&gt; parameter of GetShaderiv:
	 */
	int GL_TESS_EVALUATION_SHADER = 0x8E87;
	int GL_TESS_CONTROL_SHADER = 0x8E88;

	@Reuse("GL40")
	void glPatchParameteri(@GLenum int pname, int value);

	@Reuse("GL40")
	@StripPostfix("values")
	void glPatchParameterfv(@GLenum int pname, @Check("4") @Const FloatBuffer values);

}