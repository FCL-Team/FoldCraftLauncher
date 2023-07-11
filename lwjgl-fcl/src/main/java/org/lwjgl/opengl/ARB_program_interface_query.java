/*
 * Copyright (c) 2002-2012 LWJGL Project
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
import org.lwjgl.util.generator.opengl.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

@Extension(postfix = "")
public interface ARB_program_interface_query {

	/**
	 * Accepted by the &lt;programInterface&gt; parameter of GetProgramInterfaceiv,
	 * GetProgramResourceIndex, GetProgramResourceName, GetProgramResourceiv,
	 * GetProgramResourceLocation, and GetProgramResourceLocationIndex:
	 */
	int GL_UNIFORM                            = 0x92E1,
		GL_UNIFORM_BLOCK                      = 0x92E2,
		GL_PROGRAM_INPUT                      = 0x92E3,
		GL_PROGRAM_OUTPUT                     = 0x92E4,
		GL_BUFFER_VARIABLE                    = 0x92E5,
		GL_SHADER_STORAGE_BLOCK               = 0x92E6,
		GL_VERTEX_SUBROUTINE                  = 0x92E8,
		GL_TESS_CONTROL_SUBROUTINE            = 0x92E9,
		GL_TESS_EVALUATION_SUBROUTINE         = 0x92EA,
		GL_GEOMETRY_SUBROUTINE                = 0x92EB,
		GL_FRAGMENT_SUBROUTINE                = 0x92EC,
		GL_COMPUTE_SUBROUTINE                 = 0x92ED,
		GL_VERTEX_SUBROUTINE_UNIFORM          = 0x92EE,
		GL_TESS_CONTROL_SUBROUTINE_UNIFORM    = 0x92EF,
		GL_TESS_EVALUATION_SUBROUTINE_UNIFORM = 0x92F0,
		GL_GEOMETRY_SUBROUTINE_UNIFORM        = 0x92F1,
		GL_FRAGMENT_SUBROUTINE_UNIFORM        = 0x92F2,
		GL_COMPUTE_SUBROUTINE_UNIFORM         = 0x92F3,
		GL_TRANSFORM_FEEDBACK_VARYING         = 0x92F4;

	/** Accepted by the &lt;pname&gt; parameter of GetProgramInterfaceiv: */
	int GL_ACTIVE_RESOURCES               = 0x92F5,
		GL_MAX_NAME_LENGTH                = 0x92F6,
		GL_MAX_NUM_ACTIVE_VARIABLES       = 0x92F7,
		GL_MAX_NUM_COMPATIBLE_SUBROUTINES = 0x92F8;

	/** Accepted in the &lt;props&gt; array of GetProgramResourceiv: */
	int GL_NAME_LENGTH                          = 0x92F9,
		GL_TYPE                                 = 0x92FA,
		GL_ARRAY_SIZE                           = 0x92FB,
		GL_OFFSET                               = 0x92FC,
		GL_BLOCK_INDEX                          = 0x92FD,
		GL_ARRAY_STRIDE                         = 0x92FE,
		GL_MATRIX_STRIDE                        = 0x92FF,
		GL_IS_ROW_MAJOR                         = 0x9300,
		GL_ATOMIC_COUNTER_BUFFER_INDEX          = 0x9301,
		GL_BUFFER_BINDING                       = 0x9302,
		GL_BUFFER_DATA_SIZE                     = 0x9303,
		GL_NUM_ACTIVE_VARIABLES                 = 0x9304,
		GL_ACTIVE_VARIABLES                     = 0x9305,
		GL_REFERENCED_BY_VERTEX_SHADER          = 0x9306,
		GL_REFERENCED_BY_TESS_CONTROL_SHADER    = 0x9307,
		GL_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x9308,
		GL_REFERENCED_BY_GEOMETRY_SHADER        = 0x9309,
		GL_REFERENCED_BY_FRAGMENT_SHADER        = 0x930A,
		GL_REFERENCED_BY_COMPUTE_SHADER         = 0x930B,
		GL_TOP_LEVEL_ARRAY_SIZE                 = 0x930C,
		GL_TOP_LEVEL_ARRAY_STRIDE               = 0x930D,
		GL_LOCATION                             = 0x930E,
		GL_LOCATION_INDEX                       = 0x930F,
		GL_IS_PER_PATCH                         = 0x92E7;

	@Reuse("GL43")
	@StripPostfix("params")
	void glGetProgramInterfaceiv(@GLuint int program, @GLenum int programInterface,
	                             @GLenum int pname, @Check("1") @OutParameter IntBuffer params);

	@Reuse("GL43")
	@Alternate("glGetProgramInterfaceiv")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetProgramInterfaceiv2(@GLuint int program, @GLenum int programInterface,
	                              @GLenum int pname, @OutParameter IntBuffer params);

	@Reuse("GL43")
	@GLuint
	int glGetProgramResourceIndex(@GLuint int program, @GLenum int programInterface,
	                              @NullTerminated @Const @GLchar ByteBuffer name);

	@Reuse("GL43")
	@Alternate("glGetProgramResourceIndex")
	@GLuint
	int glGetProgramResourceIndex(@GLuint int program, @GLenum int programInterface,
	                              @NullTerminated CharSequence name);

	@Reuse("GL43")
	void glGetProgramResourceName(@GLuint int program, @GLenum int programInterface,
	                              @GLuint int index, @AutoSize(value = "name", canBeNull = true) @GLsizei int bufSize, @Check(value = "1", canBeNull = true) @OutParameter @GLsizei IntBuffer length,
	                              @Check(canBeNull = true) @OutParameter @GLchar ByteBuffer name);

	@Reuse("GL43")
	@Alternate("glGetProgramResourceName")
	@GLreturn(value = "name", maxLength = "bufSize")
	void glGetProgramResourceName2(@GLuint int program, @GLenum int programInterface,
	                               @GLuint int index, @GLsizei int bufSize,
	                               @OutParameter @GLsizei @Constant("MemoryUtil.getAddress0(name_length)") IntBuffer length,
	                               @OutParameter @GLchar ByteBuffer name);

	@Reuse("GL43")
	@StripPostfix("params")
	void glGetProgramResourceiv(@GLuint int program, @GLenum int programInterface,
	                            @GLuint int index, @AutoSize("props") @GLsizei int propCount,
	                            @Const @GLenum IntBuffer props, @AutoSize("params") @GLsizei int bufSize,
	                            @Check(value = "1", canBeNull = true) @OutParameter @GLsizei IntBuffer length, @OutParameter IntBuffer params);

	@Reuse("GL43")
	int glGetProgramResourceLocation(@GLuint int program, @GLenum int programInterface,
	                                 @NullTerminated @Const @GLchar ByteBuffer name);

	@Reuse("GL43")
	@Alternate("glGetProgramResourceLocation")
	int glGetProgramResourceLocation(@GLuint int program, @GLenum int programInterface,
	                                 @NullTerminated CharSequence name);

	@Reuse("GL43")
	int glGetProgramResourceLocationIndex(@GLuint int program, @GLenum int programInterface,
	                                      @NullTerminated @Const @GLchar ByteBuffer name);

	@Reuse("GL43")
	@Alternate("glGetProgramResourceLocationIndex")
	int glGetProgramResourceLocationIndex(@GLuint int program, @GLenum int programInterface,
	                                      @NullTerminated CharSequence name);

}