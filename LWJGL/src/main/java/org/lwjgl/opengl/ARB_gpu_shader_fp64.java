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
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.*;

@Dependent
@Extension(postfix = "")
public interface ARB_gpu_shader_fp64 {

	/**
	 * Returned in the &lt;type&gt; parameter of GetActiveUniform, and
	 * GetTransformFeedbackVarying:
	 */
	int GL_DOUBLE_VEC2 = 0x8FFC;
	int GL_DOUBLE_VEC3 = 0x8FFD;
	int GL_DOUBLE_VEC4 = 0x8FFE;
	int GL_DOUBLE_MAT2 = 0x8F46;
	int GL_DOUBLE_MAT3 = 0x8F47;
	int GL_DOUBLE_MAT4 = 0x8F48;
	int GL_DOUBLE_MAT2x3 = 0x8F49;
	int GL_DOUBLE_MAT2x4 = 0x8F4A;
	int GL_DOUBLE_MAT3x2 = 0x8F4B;
	int GL_DOUBLE_MAT3x4 = 0x8F4C;
	int GL_DOUBLE_MAT4x2 = 0x8F4D;
	int GL_DOUBLE_MAT4x3 = 0x8F4E;

	@Reuse("GL40")
	void glUniform1d(int location, double x);

	@Reuse("GL40")
	void glUniform2d(int location, double x, double y);

	@Reuse("GL40")
	void glUniform3d(int location, double x, double y, double z);

	@Reuse("GL40")
	void glUniform4d(int location, double x, double y, double z, double w);

	@Reuse("GL40")
	@StripPostfix("value")
	void glUniform1dv(int location, @AutoSize("value") @GLsizei int count, @Const DoubleBuffer value);

	@Reuse("GL40")
	@StripPostfix("value")
	void glUniform2dv(int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const DoubleBuffer value);

	@Reuse("GL40")
	@StripPostfix("value")
	void glUniform3dv(int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const DoubleBuffer value);

	@Reuse("GL40")
	@StripPostfix("value")
	void glUniform4dv(int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const DoubleBuffer value);

	@Reuse("GL40")
	@StripPostfix("value")
	void glUniformMatrix2dv(int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL40")
	@StripPostfix("value")
	void glUniformMatrix3dv(int location, @AutoSize(value = "value", expression = " / (3 * 3)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL40")
	@StripPostfix("value")
	void glUniformMatrix4dv(int location, @AutoSize(value = "value", expression = " >> 4") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL40")
	@StripPostfix("value")
	void glUniformMatrix2x3dv(int location, @AutoSize(value = "value", expression = " / (2 * 3)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL40")
	@StripPostfix("value")
	void glUniformMatrix2x4dv(int location, @AutoSize(value = "value", expression = " >> 3") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL40")
	@StripPostfix("value")
	void glUniformMatrix3x2dv(int location, @AutoSize(value = "value", expression = " / (3 * 2)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL40")
	@StripPostfix("value")
	void glUniformMatrix3x4dv(int location, @AutoSize(value = "value", expression = " / (3 * 4)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL40")
	@StripPostfix("value")
	void glUniformMatrix4x2dv(int location, @AutoSize(value = "value", expression = " >> 3") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL40")
	@StripPostfix("value")
	void glUniformMatrix4x3dv(int location, @AutoSize(value = "value", expression = " / (4 * 3)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL40")
	@StripPostfix("params")
	void glGetUniformdv(@GLuint int program, int location, @OutParameter @Check DoubleBuffer params);

	// ----

	@Dependent("GL_EXT_direct_state_access")
	void glProgramUniform1dEXT(@GLuint int program, int location, double x);

	@Dependent("GL_EXT_direct_state_access")
	void glProgramUniform2dEXT(@GLuint int program, int location, double x, double y);

	@Dependent("GL_EXT_direct_state_access")
	void glProgramUniform3dEXT(@GLuint int program, int location, double x, double y, double z);

	@Dependent("GL_EXT_direct_state_access")
	void glProgramUniform4dEXT(@GLuint int program, int location, double x, double y, double z, double w);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value="value", extension="EXT")
	void glProgramUniform1dvEXT(@GLuint int program, int location, @AutoSize(value = "value") @GLsizei int count, @Const DoubleBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value = "value", extension = "EXT")
	void glProgramUniform2dvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const DoubleBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value = "value", extension = "EXT")
	void glProgramUniform3dvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const DoubleBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value = "value", extension = "EXT")
	void glProgramUniform4dvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const DoubleBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value = "value", extension = "EXT")
	void glProgramUniformMatrix2dvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value = "value", extension = "EXT")
	void glProgramUniformMatrix3dvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " / (3 * 3)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value = "value", extension = "EXT")
	void glProgramUniformMatrix4dvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 4") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value = "value", extension = "EXT")
	void glProgramUniformMatrix2x3dvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " / (2 * 3)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value = "value", extension = "EXT")
	void glProgramUniformMatrix2x4dvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 3") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value = "value", extension = "EXT")
	void glProgramUniformMatrix3x2dvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " / (3 * 2)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value = "value", extension = "EXT")
	void glProgramUniformMatrix3x4dvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " / (3 * 4)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value = "value", extension = "EXT")
	void glProgramUniformMatrix4x2dvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 3") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value = "value", extension = "EXT")
	void glProgramUniformMatrix4x3dvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " / (4 * 3)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

}