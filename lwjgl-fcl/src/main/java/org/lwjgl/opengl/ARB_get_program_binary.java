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
import org.lwjgl.util.generator.opengl.GLvoid;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface ARB_get_program_binary {

	/**
	 * Accepted by the &lt;pname&gt; parameter of ProgramParameteri and
	 * GetProgramiv:
	 */
	int GL_PROGRAM_BINARY_RETRIEVABLE_HINT = 0x8257;

	/** Accepted by the &lt;pname&gt; parameter of GetProgramiv: */
	int GL_PROGRAM_BINARY_LENGTH = 0x8741;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetInteger64v, GetFloatv and GetDoublev:
	 */
	int GL_NUM_PROGRAM_BINARY_FORMATS = 0x87FE,
		GL_PROGRAM_BINARY_FORMATS = 0x87FF;

	@Reuse("GL41")
	void glGetProgramBinary(@GLuint int program, @AutoSize("binary") @GLsizei int bufSize,
	                        @Check(value = "1", canBeNull = true) @GLsizei IntBuffer length,
	                        @Check("1") @GLenum IntBuffer binaryFormat,
	                        @OutParameter @GLvoid ByteBuffer binary);

	@Reuse("GL41")
	void glProgramBinary(@GLuint int program, @GLenum int binaryFormat, @Const @GLvoid ByteBuffer binary, @AutoSize("binary") @GLsizei int length);

	@Reuse("GL41")
	void glProgramParameteri(@GLuint int program, @GLenum int pname, int value);

}