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
import org.lwjgl.util.generator.Alternate;
import org.lwjgl.util.generator.opengl.*;

import java.nio.*;

@Extension(postfix = "ARB", isFinal = false)
public interface ARB_program {

	/** Accepted by the &lt;format&gt; parameter of ProgramStringARB: */
	int GL_PROGRAM_FORMAT_ASCII_ARB = 0x8875;

	/** Accepted by the &lt;pname&gt; parameter of GetProgramivARB: */
	int GL_PROGRAM_LENGTH_ARB = 0x8627;
	int GL_PROGRAM_FORMAT_ARB = 0x8876;
	int GL_PROGRAM_BINDING_ARB = 0x8677;
	int GL_PROGRAM_INSTRUCTIONS_ARB = 0x88A0;
	int GL_MAX_PROGRAM_INSTRUCTIONS_ARB = 0x88A1;
	int GL_PROGRAM_NATIVE_INSTRUCTIONS_ARB = 0x88A2;
	int GL_MAX_PROGRAM_NATIVE_INSTRUCTIONS_ARB = 0x88A3;
	int GL_PROGRAM_TEMPORARIES_ARB = 0x88A4;
	int GL_MAX_PROGRAM_TEMPORARIES_ARB = 0x88A5;
	int GL_PROGRAM_NATIVE_TEMPORARIES_ARB = 0x88A6;
	int GL_MAX_PROGRAM_NATIVE_TEMPORARIES_ARB = 0x88A7;
	int GL_PROGRAM_PARAMETERS_ARB = 0x88A8;
	int GL_MAX_PROGRAM_PARAMETERS_ARB = 0x88A9;
	int GL_PROGRAM_NATIVE_PARAMETERS_ARB = 0x88AA;
	int GL_MAX_PROGRAM_NATIVE_PARAMETERS_ARB = 0x88AB;
	int GL_PROGRAM_ATTRIBS_ARB = 0x88AC;
	int GL_MAX_PROGRAM_ATTRIBS_ARB = 0x88AD;
	int GL_PROGRAM_NATIVE_ATTRIBS_ARB = 0x88AE;
	int GL_MAX_PROGRAM_NATIVE_ATTRIBS_ARB = 0x88AF;
	int GL_MAX_PROGRAM_LOCAL_PARAMETERS_ARB = 0x88B4;
	int GL_MAX_PROGRAM_ENV_PARAMETERS_ARB = 0x88B5;
	int GL_PROGRAM_UNDER_NATIVE_LIMITS_ARB = 0x88B6;

	/** Accepted by the &lt;pname&gt; parameter of GetProgramStringARB: */
	int GL_PROGRAM_STRING_ARB = 0x8628;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	int GL_PROGRAM_ERROR_POSITION_ARB = 0x864B;
	int GL_CURRENT_MATRIX_ARB = 0x8641;
	int GL_TRANSPOSE_CURRENT_MATRIX_ARB = 0x88B7;
	int GL_CURRENT_MATRIX_STACK_DEPTH_ARB = 0x8640;
	int GL_MAX_PROGRAM_MATRICES_ARB = 0x862F;
	int GL_MAX_PROGRAM_MATRIX_STACK_DEPTH_ARB = 0x862E;

	/** Accepted by the &lt;name&gt; parameter of GetString: */
	int GL_PROGRAM_ERROR_STRING_ARB = 0x8874;

	/** Accepted by the &lt;mode&gt; parameter of MatrixMode: */
	int GL_MATRIX0_ARB = 0x88C0;
	int GL_MATRIX1_ARB = 0x88C1;
	int GL_MATRIX2_ARB = 0x88C2;
	int GL_MATRIX3_ARB = 0x88C3;
	int GL_MATRIX4_ARB = 0x88C4;
	int GL_MATRIX5_ARB = 0x88C5;
	int GL_MATRIX6_ARB = 0x88C6;
	int GL_MATRIX7_ARB = 0x88C7;
	int GL_MATRIX8_ARB = 0x88C8;
	int GL_MATRIX9_ARB = 0x88C9;
	int GL_MATRIX10_ARB = 0x88CA;
	int GL_MATRIX11_ARB = 0x88CB;
	int GL_MATRIX12_ARB = 0x88CC;
	int GL_MATRIX13_ARB = 0x88CD;
	int GL_MATRIX14_ARB = 0x88CE;
	int GL_MATRIX15_ARB = 0x88CF;
	int GL_MATRIX16_ARB = 0x88D0;
	int GL_MATRIX17_ARB = 0x88D1;
	int GL_MATRIX18_ARB = 0x88D2;
	int GL_MATRIX19_ARB = 0x88D3;
	int GL_MATRIX20_ARB = 0x88D4;
	int GL_MATRIX21_ARB = 0x88D5;
	int GL_MATRIX22_ARB = 0x88D6;
	int GL_MATRIX23_ARB = 0x88D7;
	int GL_MATRIX24_ARB = 0x88D8;
	int GL_MATRIX25_ARB = 0x88D9;
	int GL_MATRIX26_ARB = 0x88DA;
	int GL_MATRIX27_ARB = 0x88DB;
	int GL_MATRIX28_ARB = 0x88DC;
	int GL_MATRIX29_ARB = 0x88DD;
	int GL_MATRIX30_ARB = 0x88DE;
	int GL_MATRIX31_ARB = 0x88DF;

	void glProgramStringARB(@GLenum int target, @GLenum int format, @AutoSize("string") @GLsizei int length, @Const @GLbyte Buffer string);

	@Alternate("glProgramStringARB")
	void glProgramStringARB(@GLenum int target, @GLenum int format, @Constant("string.length()") @GLsizei int length, CharSequence string);

	void glBindProgramARB(@GLenum int target, @GLuint int program);

	void glDeleteProgramsARB(@AutoSize("programs") @GLsizei int n, @Const @GLuint IntBuffer programs);

	@Alternate("glDeleteProgramsARB")
	void glDeleteProgramsARB(@Constant("1") @GLsizei int n, @Constant(value = "APIUtil.getInt(caps, program)", keepParam = true) int program);

	void glGenProgramsARB(@AutoSize("programs") @GLsizei int n, @OutParameter @GLuint IntBuffer programs);

	@Alternate("glGenProgramsARB")
	@GLreturn("programs")
	void glGenProgramsARB2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer programs);

	void glProgramEnvParameter4fARB(int target, int index, float x, float y, float z, float w);

	void glProgramEnvParameter4dARB(int target, int index, double x, double y, double z, double w);

	@StripPostfix("params")
	void glProgramEnvParameter4fvARB(@GLenum int target, @GLuint int index, @Check("4") @Const FloatBuffer params);

	@StripPostfix("params")
	void glProgramEnvParameter4dvARB(@GLenum int target, @GLuint int index, @Check("4") @Const DoubleBuffer params);

	void glProgramLocalParameter4fARB(@GLenum int target, @GLuint int index, float x, float y, float z, float w);

	void glProgramLocalParameter4dARB(@GLenum int target, @GLuint int index, double x, double y, double z, double w);

	@StripPostfix("params")
	void glProgramLocalParameter4fvARB(@GLenum int target, @GLuint int index, @Check("4") @Const FloatBuffer params);

	@StripPostfix("params")
	void glProgramLocalParameter4dvARB(@GLenum int target, @GLuint int index, @Check("4") @Const DoubleBuffer params);

	@StripPostfix("params")
	void glGetProgramEnvParameterfvARB(@GLenum int target, @GLuint int index, @OutParameter @Check("4") FloatBuffer params);

	@StripPostfix("params")
	void glGetProgramEnvParameterdvARB(@GLenum int target, @GLuint int index, @OutParameter @Check("4") DoubleBuffer params);

	@StripPostfix("params")
	void glGetProgramLocalParameterfvARB(@GLenum int target, @GLuint int index, @OutParameter @Check("4") FloatBuffer params);

	@StripPostfix("params")
	void glGetProgramLocalParameterdvARB(@GLenum int target, @GLuint int index, @OutParameter @Check("4") DoubleBuffer params);

	@StripPostfix("params")
	void glGetProgramivARB(@GLenum int target, @GLenum int parameterName, @OutParameter @Check("4") IntBuffer params);

	/** @deprecated Will be removed in 3.0. Use {@link #glGetProgramiARB} instead. */
	@Alternate("glGetProgramivARB")
	@GLreturn("params")
	@StripPostfix("params")
	@Reuse(value = "ARBProgram", method = "glGetProgramiARB")
	@Deprecated
	void glGetProgramivARB2(@GLenum int target, @GLenum int parameterName, @OutParameter IntBuffer params);

	@Alternate("glGetProgramivARB")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetProgramivARB3(@GLenum int target, @GLenum int parameterName, @OutParameter IntBuffer params);

	void glGetProgramStringARB(@GLenum int target, @GLenum int parameterName, @OutParameter @Check @GLbyte Buffer paramString);

	@Alternate("glGetProgramStringARB")
	@Code("\t\tint programLength = glGetProgramiARB(target, GL_PROGRAM_LENGTH_ARB);")
	@GLreturn(value="paramString", maxLength = "programLength", forceMaxLength = true)
	void glGetProgramStringARB2(@GLenum int target, @GLenum int parameterName, @OutParameter @GLchar ByteBuffer paramString);

	boolean glIsProgramARB(@GLuint int program);
}
