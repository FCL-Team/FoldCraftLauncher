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
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.IntBuffer;

public interface NV_gpu_program4 {

	/** Accepted by the &lt;pname&gt; parameter of GetProgramivARB: */
	int GL_PROGRAM_ATTRIB_COMPONENTS_NV = 0x8906;
	int GL_PROGRAM_RESULT_COMPONENTS_NV = 0x8907;
	int GL_MAX_PROGRAM_ATTRIB_COMPONENTS_NV = 0x8908;
	int GL_MAX_PROGRAM_RESULT_COMPONENTS_NV = 0x8909;
	int GL_MAX_PROGRAM_GENERIC_ATTRIBS_NV = 0x8DA5;
	int GL_MAX_PROGRAM_GENERIC_RESULTS_NV = 0x8DA6;

	// ---

	void glProgramLocalParameterI4iNV(@GLenum int target, @GLuint int index, int x, int y, int z, int w);

	@StripPostfix("params")
	void glProgramLocalParameterI4ivNV(@GLenum int target, @GLuint int index, @Check("4") @Const IntBuffer params);

	@StripPostfix("params")
	void glProgramLocalParametersI4ivNV(@GLenum int target, @GLuint int index,
										@AutoSize(value = "params", expression = " >> 2") @GLsizei int count, @Const IntBuffer params);

	// ---

	void glProgramLocalParameterI4uiNV(@GLenum int target, @GLuint int index, @GLuint int x, @GLuint int y, @GLuint int z, @GLuint int w);

	@StripPostfix("params")
	void glProgramLocalParameterI4uivNV(@GLenum int target, @GLuint int index, @Check("4") @Const @GLuint IntBuffer params);

	@StripPostfix("params")
	void glProgramLocalParametersI4uivNV(@GLenum int target, @GLuint int index,
										 @AutoSize(value = "params", expression = " >> 2") @GLsizei int count, @Const @GLuint IntBuffer params);

	// ---

	void glProgramEnvParameterI4iNV(@GLenum int target, @GLuint int index, int x, int y, int z, int w);

	@StripPostfix("params")
	void glProgramEnvParameterI4ivNV(@GLenum int target, @GLuint int index, @Check("4") @Const IntBuffer params);

	@StripPostfix("params")
	void glProgramEnvParametersI4ivNV(@GLenum int target, @GLuint int index,
									  @AutoSize(value = "params", expression = " >> 2") @GLsizei int count, @Const IntBuffer params);

	// ---

	void glProgramEnvParameterI4uiNV(@GLenum int target, @GLuint int index, @GLuint int x, @GLuint int y, @GLuint int z, @GLuint int w);

	@StripPostfix("params")
	void glProgramEnvParameterI4uivNV(@GLenum int target, @GLuint int index, @Check("4") @Const @GLuint IntBuffer params);

	@StripPostfix("params")
	void glProgramEnvParametersI4uivNV(@GLenum int target, @GLuint int index,
									   @AutoSize(value = "params", expression = " >> 2") @GLsizei int count, @Const @GLuint IntBuffer params);

	// ---

	@StripPostfix("params")
	void glGetProgramLocalParameterIivNV(@GLenum int target, @GLuint int index, @OutParameter @Check("4") IntBuffer params);

	@StripPostfix("params")
	void glGetProgramLocalParameterIuivNV(@GLenum int target, @GLuint int index, @OutParameter @Check("4") @GLuint IntBuffer params);

	// ---

	@StripPostfix("params")
	void glGetProgramEnvParameterIivNV(@GLenum int target, @GLuint int index, @OutParameter @Check("4")IntBuffer params);

	@StripPostfix("params")
	void glGetProgramEnvParameterIuivNV(@GLenum int target, @GLuint int index, @OutParameter @Check("4") @GLuint IntBuffer params);

}
