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
import org.lwjgl.util.generator.opengl.*;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@Extension(postfix = "")
public interface ARB_separate_shader_objects {

	/** Accepted by &lt;stages&gt; parameter to UseProgramStages: */
	int GL_VERTEX_SHADER_BIT          = 0x00000001,
		GL_FRAGMENT_SHADER_BIT        = 0x00000002,
		GL_GEOMETRY_SHADER_BIT        = 0x00000004,
		GL_TESS_CONTROL_SHADER_BIT    = 0x00000008,
		GL_TESS_EVALUATION_SHADER_BIT = 0x00000010,
		GL_ALL_SHADER_BITS            = 0xFFFFFFFF;

	/**
	 * Accepted by the &lt;pname&gt; parameter of ProgramParameteri and
	 * GetProgramiv:
	 */
	int GL_PROGRAM_SEPARABLE = 0x8258;

	/** Accepted by &lt;type&gt; parameter to GetProgramPipelineiv: */
	int GL_ACTIVE_PROGRAM = 0x8259;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetInteger64v, GetFloatv, and GetDoublev:
	 */
	int GL_PROGRAM_PIPELINE_BINDING = 0x825A;

	@Reuse("GL41")
	void glUseProgramStages(@GLuint int pipeline, @GLbitfield int stages, @GLuint int program);

	@Reuse("GL41")
	void glActiveShaderProgram(@GLuint int pipeline, @GLuint int program);

	/** Single null-terminated source code string. */
	@Reuse("GL41")
	@StripPostfix(value = "string", hasPostfix = false)
	@GLuint
	int glCreateShaderProgramv(@GLenum int type, @Constant("1") @GLsizei int count, @NullTerminated @Check @Const @Indirect @GLchar ByteBuffer string);

	/** Multiple null-terminated source code strings, one after the other. */
	@Reuse("GL41")
	@Alternate(value = "glCreateShaderProgramv", nativeAlt = true)
	@StripPostfix(value = "strings", hasPostfix = false)
	@GLuint
	int glCreateShaderProgramv2(@GLenum int type, @GLsizei int count, @NullTerminated("count") @Check @Const @Indirect @GLchar @PointerArray("count") ByteBuffer strings);

	@Reuse("GL41")
	@Alternate(value = "glCreateShaderProgramv", nativeAlt = true)
	@StripPostfix(value = "strings", hasPostfix = false)
	@GLuint
	int glCreateShaderProgramv3(@GLenum int type, @Constant("strings.length") @GLsizei int count, @NullTerminated @Check("1") @PointerArray(value = "count") @Const @NativeType("GLchar") ByteBuffer[] strings);

	@Reuse("GL41")
	@Alternate("glCreateShaderProgramv")
	@StripPostfix(value = "string", hasPostfix = false)
	@GLuint
	int glCreateShaderProgramv(@GLenum int type, @Constant("1") @GLsizei int count, @NullTerminated CharSequence string);

	@Reuse("GL41")
	@Alternate(value = "glCreateShaderProgramv", nativeAlt = true, skipNative = true)
	@StripPostfix(value = "strings", hasPostfix = false)
	@GLuint
	int glCreateShaderProgramv2(@GLenum int type, @Constant("strings.length") @GLsizei int count,
	                            @Const @NullTerminated @PointerArray(value = "count") CharSequence[] strings);

	@Reuse("GL41")
	void glBindProgramPipeline(@GLuint int pipeline);

	@Reuse("GL41")
	void glDeleteProgramPipelines(@AutoSize("pipelines") @GLsizei int n, @Const @GLuint IntBuffer pipelines);

	@Reuse("GL41")
	@Alternate("glDeleteProgramPipelines")
	void glDeleteProgramPipelines(@Constant("1") @GLsizei int n, @Constant(value = "APIUtil.getInt(caps, pipeline)", keepParam = true) int pipeline);

	@Reuse("GL41")
	void glGenProgramPipelines(@AutoSize("pipelines") @GLsizei int n, @OutParameter @GLuint IntBuffer pipelines);

	@Reuse("GL41")
	@Alternate("glGenProgramPipelines")
	@GLreturn("pipelines")
	void glGenProgramPipelines2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer pipelines);

	@Reuse("GL41")
	boolean glIsProgramPipeline(@GLuint int pipeline);

	@Reuse("GL41")
	void glProgramParameteri(@GLuint int program, @GLenum int pname, int value);

	@Reuse("GL41")
	@StripPostfix("params")
	void glGetProgramPipelineiv(@GLuint int pipeline, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Reuse("GL41")
	@Alternate("glGetProgramPipelineiv")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetProgramPipelineiv2(@GLuint int pipeline, @GLenum int pname, @OutParameter IntBuffer params);

	@Reuse("GL41")
	void glProgramUniform1i(@GLuint int program, int location, int v0);

	@Reuse("GL41")
	void glProgramUniform2i(@GLuint int program, int location, int v0, int v1);

	@Reuse("GL41")
	void glProgramUniform3i(@GLuint int program, int location, int v0, int v1, int v2);

	@Reuse("GL41")
	void glProgramUniform4i(@GLuint int program, int location, int v0, int v1, int v2, int v3);

	@Reuse("GL41")
	void glProgramUniform1f(@GLuint int program, int location, float v0);

	@Reuse("GL41")
	void glProgramUniform2f(@GLuint int program, int location, float v0, float v1);

	@Reuse("GL41")
	void glProgramUniform3f(@GLuint int program, int location, float v0, float v1, float v2);

	@Reuse("GL41")
	void glProgramUniform4f(@GLuint int program, int location, float v0, float v1, float v2, float v3);

	@Reuse("GL41")
	void glProgramUniform1d(@GLuint int program, int location, double v0);

	@Reuse("GL41")
	void glProgramUniform2d(@GLuint int program, int location, double v0, double v1);

	@Reuse("GL41")
	void glProgramUniform3d(@GLuint int program, int location, double v0, double v1, double v2);

	@Reuse("GL41")
	void glProgramUniform4d(@GLuint int program, int location, double v0, double v1, double v2, double v3);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform1iv(@GLuint int program, int location, @AutoSize("value") @GLsizei int count, @Const IntBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform2iv(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const IntBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform3iv(@GLuint int program, int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const IntBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform4iv(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const IntBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform1fv(@GLuint int program, int location, @AutoSize("value") @GLsizei int count, @Const FloatBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform2fv(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const FloatBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform3fv(@GLuint int program, int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const FloatBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform4fv(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const FloatBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform1dv(@GLuint int program, int location, @AutoSize("value") @GLsizei int count, @Const DoubleBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform2dv(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const DoubleBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform3dv(@GLuint int program, int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const DoubleBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform4dv(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const DoubleBuffer value);

	@Reuse("GL41")
	void glProgramUniform1ui(@GLuint int program, int location, int v0);

	@Reuse("GL41")
	void glProgramUniform2ui(@GLuint int program, int location, int v0, int v1);

	@Reuse("GL41")
	void glProgramUniform3ui(@GLuint int program, int location, int v0, int v1, int v2);

	@Reuse("GL41")
	void glProgramUniform4ui(@GLuint int program, int location, int v0, int v1, int v2, int v3);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform1uiv(@GLuint int program, int location, @AutoSize("value") @GLsizei int count, @Const IntBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform2uiv(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const IntBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform3uiv(@GLuint int program, int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const IntBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniform4uiv(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const IntBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix2fv(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix3fv(@GLuint int program, int location, @AutoSize(value = "value", expression = " / (3 * 3)") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix4fv(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 4") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix2dv(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix3dv(@GLuint int program, int location, @AutoSize(value = "value", expression = " / (3 * 3)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix4dv(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 4") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix2x3fv(@GLuint int program, int location,
	                                 @AutoSize(value = "value", expression = " / (2 * 3)") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix3x2fv(@GLuint int program, int location,
	                                 @AutoSize(value = "value", expression = " / (3 * 2)") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix2x4fv(@GLuint int program, int location,
	                                 @AutoSize(value = "value", expression = " >> 3") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix4x2fv(@GLuint int program, int location,
	                                 @AutoSize(value = "value", expression = " >> 3") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix3x4fv(@GLuint int program, int location,
	                                 @AutoSize(value = "value", expression = " / (3 * 4)") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix4x3fv(@GLuint int program, int location,
	                                 @AutoSize(value = "value", expression = " / (4 * 3)") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix2x3dv(@GLuint int program, int location,
	                                 @AutoSize(value = "value", expression = " / (2 * 3)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix3x2dv(@GLuint int program, int location,
	                                 @AutoSize(value = "value", expression = " / (3 * 2)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix2x4dv(@GLuint int program, int location,
	                                 @AutoSize(value = "value", expression = " >> 3") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix4x2dv(@GLuint int program, int location,
	                                 @AutoSize(value = "value", expression = " >> 3") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix3x4dv(@GLuint int program, int location,
	                                 @AutoSize(value = "value", expression = " / (3 * 4)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL41")
	@StripPostfix("value")
	void glProgramUniformMatrix4x3dv(@GLuint int program, int location,
	                                 @AutoSize(value = "value", expression = " / (4 * 3)") @GLsizei int count, boolean transpose, @Const DoubleBuffer value);

	@Reuse("GL41")
	void glValidateProgramPipeline(@GLuint int pipeline);

	@Reuse("GL41")
	void glGetProgramPipelineInfoLog(@GLuint int pipeline, @AutoSize("infoLog") @GLsizei int bufSize,
	                                 @OutParameter @Check(value = "1", canBeNull = true) @GLsizei IntBuffer length,
	                                 @OutParameter @GLchar ByteBuffer infoLog);

	@Reuse("GL41")
	@Alternate("glGetProgramPipelineInfoLog")
	@GLreturn(value = "infoLog", maxLength = "bufSize")
	void glGetProgramPipelineInfoLog2(@GLuint int pipeline, @GLsizei int bufSize,
	                                  @OutParameter @GLsizei @Constant("MemoryUtil.getAddress0(infoLog_length)") IntBuffer length,
	                                  @OutParameter @GLchar ByteBuffer infoLog);

}