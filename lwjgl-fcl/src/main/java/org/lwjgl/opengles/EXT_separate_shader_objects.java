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
package org.lwjgl.opengles;

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public interface EXT_separate_shader_objects {

	/** Accepted by &lt;stages&gt; parameter to UseProgramStagesEXT: */
	int GL_VERTEX_SHADER_BIT_EXT   = 0x00000001,
		GL_FRAGMENT_SHADER_BIT_EXT = 0x00000002,
		GL_ALL_SHADER_BITS_EXT     = 0xFFFFFFFF;

	/**
	 * Accepted by the &lt;pname&gt; parameter of ProgramParameteriEXT and
	 * GetProgramiv:
	 */
	int GL_PROGRAM_SEPARABLE_EXT = 0x8258;

	/** Accepted by &lt;type&gt; parameter to GetProgramPipelineivEXT: */
	int GL_ACTIVE_PROGRAM_EXT = 0x8259;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, and
	 * GetFloatv:
	 */
	int GL_PROGRAM_PIPELINE_BINDING_EXT = 0x825A;

	void glUseProgramStagesEXT(@GLuint int pipeline, @GLbitfield int stages, @GLuint int program);

	void glActiveShaderProgramEXT(@GLuint int pipeline, @GLuint int program);

	/** Single null-terminated source code string. */
	@StripPostfix(value = "string", hasPostfix = false)
	@GLuint
	int glCreateShaderProgramvEXT(@GLenum int type, @Constant("1") @GLsizei int count, @NullTerminated @Check @Const @Indirect @GLchar ByteBuffer string);

	/** Multiple null-terminated source code strings, one after the other. */
	@Alternate(value = "glCreateShaderProgramvEXT", nativeAlt = true)
	@StripPostfix(value = "strings", hasPostfix = false)
	@GLuint
	int glCreateShaderProgramvEXT2(@GLenum int type, @GLsizei int count, @NullTerminated("count") @Check @Const @Indirect @GLchar @PointerArray("count") ByteBuffer strings);

	@Alternate(value = "glCreateShaderProgramvEXT", nativeAlt = true)
	@StripPostfix(value = "strings", hasPostfix = false)
	@GLuint
	int glCreateShaderProgramvEXT3(@GLenum int type, @Constant("strings.length") @GLsizei int count, @NullTerminated @Check("1") @PointerArray(value = "count") @Const @NativeType("GLchar") ByteBuffer[] strings);

	@Alternate("glCreateShaderProgramvEXT")
	@StripPostfix(value = "string", hasPostfix = false)
	@GLuint
	int glCreateShaderProgramvEXT(@GLenum int type, @Constant("1") @GLsizei int count, @NullTerminated CharSequence string);

	@Alternate(value = "glCreateShaderProgramvEXT", nativeAlt = true, skipNative = true)
	@StripPostfix(value = "strings", hasPostfix = false)
	@GLuint
	int glCreateShaderProgramvEXT2(@GLenum int type, @Constant("strings.length") @GLsizei int count,
	                               @Const @NullTerminated @PointerArray(value = "count") CharSequence[] strings);

	void glBindProgramPipelineEXT(@GLuint int pipeline);

	void glDeleteProgramPipelinesEXT(@AutoSize("pipelines") @GLsizei int n, @Const @GLuint IntBuffer pipelines);

	@Alternate("glDeleteProgramPipelinesEXT")
	void glDeleteProgramPipelinesEXT(@Constant("1") @GLsizei int n, @Constant(value = "APIUtil.getInt(pipeline)", keepParam = true) int pipeline);

	void glGenProgramPipelinesEXT(@AutoSize("pipelines") @GLsizei int n, @OutParameter @GLuint IntBuffer pipelines);

	@Alternate("glGenProgramPipelinesEXT")
	@GLreturn("pipelines")
	void glGenProgramPipelinesEXT2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer pipelines);

	boolean glIsProgramPipelineEXT(@GLuint int pipeline);

	void glProgramParameteriEXT(@GLuint int program, @GLenum int pname, int value);

	@StripPostfix("params")
	void glGetProgramPipelineivEXT(@GLuint int pipeline, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetProgramPipelineivEXT")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetProgramPipelineivEXT2(@GLuint int pipeline, @GLenum int pname, @OutParameter IntBuffer params);

	void glProgramUniform1iEXT(@GLuint int program, int location, int v0);

	void glProgramUniform2iEXT(@GLuint int program, int location, int v0, int v1);

	void glProgramUniform3iEXT(@GLuint int program, int location, int v0, int v1, int v2);

	void glProgramUniform4iEXT(@GLuint int program, int location, int v0, int v1, int v2, int v3);

	void glProgramUniform1fEXT(@GLuint int program, int location, float v0);

	void glProgramUniform2fEXT(@GLuint int program, int location, float v0, float v1);

	void glProgramUniform3fEXT(@GLuint int program, int location, float v0, float v1, float v2);

	void glProgramUniform4fEXT(@GLuint int program, int location, float v0, float v1, float v2, float v3);

	@StripPostfix("value")
	void glProgramUniform1ivEXT(@GLuint int program, int location, @AutoSize("value") @GLsizei int count, @Const IntBuffer value);

	@StripPostfix("value")
	void glProgramUniform2ivEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const IntBuffer value);

	@StripPostfix("value")
	void glProgramUniform3ivEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const IntBuffer value);

	@StripPostfix("value")
	void glProgramUniform4ivEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const IntBuffer value);

	@StripPostfix("value")
	void glProgramUniform1fvEXT(@GLuint int program, int location, @AutoSize("value") @GLsizei int count, @Const FloatBuffer value);

	@StripPostfix("value")
	void glProgramUniform2fvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const FloatBuffer value);

	@StripPostfix("value")
	void glProgramUniform3fvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const FloatBuffer value);

	@StripPostfix("value")
	void glProgramUniform4fvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const FloatBuffer value);

	@StripPostfix("value")
	void glProgramUniformMatrix2fvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@StripPostfix("value")
	void glProgramUniformMatrix3fvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " / (3 * 3)") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@StripPostfix("value")
	void glProgramUniformMatrix4fvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 4") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	void glValidateProgramPipelineEXT(@GLuint int pipeline);

	void glGetProgramPipelineInfoLogEXT(@GLuint int pipeline, @AutoSize("infoLog") @GLsizei int bufSize,
	                                    @OutParameter @Check(value = "1", canBeNull = true) @GLsizei IntBuffer length,
	                                    @OutParameter @GLchar ByteBuffer infoLog);

	@Alternate("glGetProgramPipelineInfoLogEXT")
	@GLreturn(value = "infoLog", maxLength = "bufSize")
	void glGetProgramPipelineInfoLogEXT2(@GLuint int pipeline, @GLsizei int bufSize,
	                                     @OutParameter @GLsizei @Constant("MemoryUtil.getAddress0(infoLog_length)") IntBuffer length,
	                                     @OutParameter @GLchar ByteBuffer infoLog);

}