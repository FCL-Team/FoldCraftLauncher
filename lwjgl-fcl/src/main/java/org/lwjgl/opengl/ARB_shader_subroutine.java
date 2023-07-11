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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

@Extension(postfix = "")
public interface ARB_shader_subroutine {

	/** Accepted by the &lt;pname&gt; parameter of GetProgramStageiv: */
	int GL_ACTIVE_SUBROUTINES = 0x8DE5;
	int GL_ACTIVE_SUBROUTINE_UNIFORMS = 0x8DE6;
	int GL_ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS = 0x8E47;
	int GL_ACTIVE_SUBROUTINE_MAX_LENGTH = 0x8E48;
	int GL_ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH = 0x8E49;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, GetDoublev, and GetInteger64v:
	 */
	int GL_MAX_SUBROUTINES = 0x8DE7;
	int GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS = 0x8DE8;

	/** Accepted by the &lt;pname&gt; parameter of GetActiveSubroutineUniformiv: */
	int GL_NUM_COMPATIBLE_SUBROUTINES = 0x8E4A;
	int GL_COMPATIBLE_SUBROUTINES = 0x8E4B;

	@Reuse("GL40")
	int glGetSubroutineUniformLocation(@GLuint int program, @GLenum int shadertype, @Const @NullTerminated ByteBuffer name);

	@Alternate("glGetSubroutineUniformLocation")
	@Reuse("GL40")
	int glGetSubroutineUniformLocation(@GLuint int program, @GLenum int shadertype, @NullTerminated CharSequence name);

	@Reuse("GL40")
	@GLuint
	int glGetSubroutineIndex(@GLuint int program, @GLenum int shadertype, @Const @NullTerminated ByteBuffer name);

	@Alternate("glGetSubroutineIndex")
	@Reuse("GL40")
	int glGetSubroutineIndex(@GLuint int program, @GLenum int shadertype, @NullTerminated CharSequence name);

	@Reuse("GL40")
	@StripPostfix("values")
	void glGetActiveSubroutineUniformiv(@GLuint int program, @GLenum int shadertype, @GLuint int index, @GLenum int pname,
	                                    @Check("1") @OutParameter IntBuffer values);

	@Reuse("GL40")
	@Alternate("glGetActiveSubroutineUniformiv")
	@GLreturn("values")
	@StripPostfix(value = "values", hasPostfix = false)
	void glGetActiveSubroutineUniformiv2(@GLuint int program, @GLenum int shadertype, @GLuint int index, @GLenum int pname,
	                                     @OutParameter IntBuffer values);

	@Reuse("GL40")
	void glGetActiveSubroutineUniformName(@GLuint int program, @GLenum int shadertype, @GLuint int index, @AutoSize("name") @GLsizei int bufsize,
	                                      @OutParameter @Check(value = "1", canBeNull = true) @GLsizei IntBuffer length,
	                                      @OutParameter ByteBuffer name);

	@Reuse("GL40")
	@Alternate("glGetActiveSubroutineUniformName")
	@GLreturn(value = "name", maxLength = "bufsize")
	void glGetActiveSubroutineUniformName2(@GLuint int program, @GLenum int shadertype, @GLuint int index, @GLsizei int bufsize,
	                                       @OutParameter @Constant("MemoryUtil.getAddress0(name_length)") @GLsizei IntBuffer length,
	                                       @OutParameter @GLchar ByteBuffer name);

	@Reuse("GL40")
	void glGetActiveSubroutineName(@GLuint int program, @GLenum int shadertype, @GLuint int index, @AutoSize("name") @GLsizei int bufsize,
	                               @OutParameter @Check(value = "1", canBeNull = true) @GLsizei IntBuffer length,
	                               @OutParameter ByteBuffer name);

	@Reuse("GL40")
	@Alternate("glGetActiveSubroutineName")
	@GLreturn(value = "name", maxLength = "bufsize")
	void glGetActiveSubroutineName2(@GLuint int program, @GLenum int shadertype, @GLuint int index, @GLsizei int bufsize,
	                                @OutParameter @Constant("MemoryUtil.getAddress0(name_length)") @GLsizei IntBuffer length,
	                                @OutParameter @GLchar ByteBuffer name);

	@Reuse("GL40")
	@StripPostfix("indices")
	void glUniformSubroutinesuiv(@GLenum int shadertype, @AutoSize("indices") @GLsizei int count, @Const @GLuint IntBuffer indices);

	@Reuse("GL40")
	@StripPostfix("params")
	void glGetUniformSubroutineuiv(@GLenum int shadertype, int location, @Check("1") @OutParameter @GLuint IntBuffer params);

	@Reuse("GL40")
	@Alternate("glGetUniformSubroutineuiv")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetUniformSubroutineuiv2(@GLenum int shadertype, int location, @OutParameter @GLuint IntBuffer params);

	@Reuse("GL40")
	@StripPostfix("values")
	void glGetProgramStageiv(@GLuint int program, @GLenum int shadertype, @GLenum int pname, @Check("1") @OutParameter IntBuffer values);

	@Reuse("GL40")
	@Alternate("glGetProgramStageiv")
	@GLreturn("values")
	@StripPostfix(value = "values", hasPostfix = false)
	void glGetProgramStageiv2(@GLuint int program, @GLenum int shadertype, @GLenum int pname, @OutParameter IntBuffer values);

}