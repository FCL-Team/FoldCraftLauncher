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
import org.lwjgl.util.generator.opengl.GLint64EXT;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;
import org.lwjgl.util.generator.opengl.GLuint64EXT;

import java.nio.LongBuffer;

@Dependent
public interface NV_gpu_shader5 {

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveAttrib, GetActiveUniform, and
	 * GetTransformFeedbackVarying:
	 */
	int GL_INT64_NV = 0x140E;
	int GL_UNSIGNED_INT64_NV = 0x140F;

	int GL_INT8_NV = 0x8FE0;
	int GL_INT8_VEC2_NV = 0x8FE1;
	int GL_INT8_VEC3_NV = 0x8FE2;
	int GL_INT8_VEC4_NV = 0x8FE3;
	int GL_INT16_NV = 0x8FE4;
	int GL_INT16_VEC2_NV = 0x8FE5;
	int GL_INT16_VEC3_NV = 0x8FE6;
	int GL_INT16_VEC4_NV = 0x8FE7;
	int GL_INT64_VEC2_NV = 0x8FE9;
	int GL_INT64_VEC3_NV = 0x8FEA;
	int GL_INT64_VEC4_NV = 0x8FEB;
	int GL_UNSIGNED_INT8_NV = 0x8FEC;
	int GL_UNSIGNED_INT8_VEC2_NV = 0x8FED;
	int GL_UNSIGNED_INT8_VEC3_NV = 0x8FEE;
	int GL_UNSIGNED_INT8_VEC4_NV = 0x8FEF;
	int GL_UNSIGNED_INT16_NV = 0x8FF0;
	int GL_UNSIGNED_INT16_VEC2_NV = 0x8FF1;
	int GL_UNSIGNED_INT16_VEC3_NV = 0x8FF2;
	int GL_UNSIGNED_INT16_VEC4_NV = 0x8FF3;
	int GL_UNSIGNED_INT64_VEC2_NV = 0x8FF5;
	int GL_UNSIGNED_INT64_VEC3_NV = 0x8FF6;
	int GL_UNSIGNED_INT64_VEC4_NV = 0x8FF7;
	int GL_FLOAT16_NV = 0x8FF8;
	int GL_FLOAT16_VEC2_NV = 0x8FF9;
	int GL_FLOAT16_VEC3_NV = 0x8FFA;
	int GL_FLOAT16_VEC4_NV = 0x8FFB;

	/** Accepted by the &lt;primitiveMode&gt; parameter of BeginTransformFeedback: */
	int GL_PATCHES = ARB_tessellation_shader.GL_PATCHES;

	void glUniform1i64NV(int location, @GLint64EXT long x);

	void glUniform2i64NV(int location, @GLint64EXT long x, @GLint64EXT long y);

	void glUniform3i64NV(int location, @GLint64EXT long x, @GLint64EXT long y, @GLint64EXT long z);

	void glUniform4i64NV(int location, @GLint64EXT long x, @GLint64EXT long y, @GLint64EXT long z, @GLint64EXT long w);

	@StripPostfix("value")
	void glUniform1i64vNV(int location, @AutoSize("value") @GLsizei int count, @Const @GLint64EXT LongBuffer value);

	@StripPostfix("value")
	void glUniform2i64vNV(int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const @GLint64EXT LongBuffer value);

	@StripPostfix("value")
	void glUniform3i64vNV(int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const @GLint64EXT LongBuffer value);

	@StripPostfix("value")
	void glUniform4i64vNV(int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const @GLint64EXT LongBuffer value);

	void glUniform1ui64NV(int location, @GLuint64EXT long x);

	void glUniform2ui64NV(int location, @GLuint64EXT long x, @GLuint64EXT long y);

	void glUniform3ui64NV(int location, @GLuint64EXT long x, @GLuint64EXT long y, @GLuint64EXT long z);

	void glUniform4ui64NV(int location, @GLuint64EXT long x, @GLuint64EXT long y, @GLuint64EXT long z, @GLuint64EXT long w);

	@StripPostfix("value")
	void glUniform1ui64vNV(int location, @AutoSize("value") @GLsizei int count, @Const @GLuint64EXT LongBuffer value);

	@StripPostfix("value")
	void glUniform2ui64vNV(int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const @GLuint64EXT LongBuffer value);

	@StripPostfix("value")
	void glUniform3ui64vNV(int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const @GLuint64EXT LongBuffer value);

	@StripPostfix("value")
	void glUniform4ui64vNV(int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const @GLuint64EXT LongBuffer value);

	@StripPostfix("params")
	void glGetUniformi64vNV(@GLuint int program, int location, @OutParameter @Check("1") @GLint64EXT LongBuffer params);

	@StripPostfix("params")
	void glGetUniformui64vNV(@GLuint int program, int location, @OutParameter @Check("1") @GLuint64EXT LongBuffer params);

	// -------------

	@Dependent("GL_EXT_direct_state_access")
	void glProgramUniform1i64NV(@GLuint int program, int location, @GLint64EXT long x);

	@Dependent("GL_EXT_direct_state_access")
	void glProgramUniform2i64NV(@GLuint int program, int location, @GLint64EXT long x, @GLint64EXT long y);

	@Dependent("GL_EXT_direct_state_access")
	void glProgramUniform3i64NV(@GLuint int program, int location, @GLint64EXT long x, @GLint64EXT long y, @GLint64EXT long z);

	@Dependent("GL_EXT_direct_state_access")
	void glProgramUniform4i64NV(@GLuint int program, int location, @GLint64EXT long x, @GLint64EXT long y, @GLint64EXT long z, @GLint64EXT long w);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix("value")
	void glProgramUniform1i64vNV(@GLuint int program, int location, @AutoSize("value") @GLsizei int count, @Const @GLint64EXT LongBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix("value")
	void glProgramUniform2i64vNV(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const @GLint64EXT LongBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix("value")
	void glProgramUniform3i64vNV(@GLuint int program, int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const @GLint64EXT LongBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix("value")
	void glProgramUniform4i64vNV(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const @GLint64EXT LongBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	void glProgramUniform1ui64NV(@GLuint int program, int location, @GLuint64EXT long x);

	@Dependent("GL_EXT_direct_state_access")
	void glProgramUniform2ui64NV(@GLuint int program, int location, @GLuint64EXT long x, @GLuint64EXT long y);

	@Dependent("GL_EXT_direct_state_access")
	void glProgramUniform3ui64NV(@GLuint int program, int location, @GLuint64EXT long x, @GLuint64EXT long y, @GLuint64EXT long z);

	@Dependent("GL_EXT_direct_state_access")
	void glProgramUniform4ui64NV(@GLuint int program, int location, @GLuint64EXT long x, @GLuint64EXT long y, @GLuint64EXT long z, @GLuint64EXT long w);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix("value")
	void glProgramUniform1ui64vNV(@GLuint int program, int location, @AutoSize("value") @GLsizei int count, @Const @GLuint64EXT LongBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix("value")
	void glProgramUniform2ui64vNV(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const @GLuint64EXT LongBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix("value")
	void glProgramUniform3ui64vNV(@GLuint int program, int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const @GLuint64EXT LongBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix("value")
	void glProgramUniform4ui64vNV(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const @GLuint64EXT LongBuffer value);

}