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
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public interface ARB_shader_objects {

	/** Accepted by the &lt;pname&gt; argument of GetHandleARB: */
	int GL_PROGRAM_OBJECT_ARB = 0x8B40;

	/** Accepted by the &lt;pname&gt; parameter of GetObjectParameter{fi}vARB: */
	int GL_OBJECT_TYPE_ARB = 0x8B4E;
	int GL_OBJECT_SUBTYPE_ARB = 0x8B4F;
	int GL_OBJECT_DELETE_STATUS_ARB = 0x8B80;
	int GL_OBJECT_COMPILE_STATUS_ARB = 0x8B81;
	int GL_OBJECT_LINK_STATUS_ARB = 0x8B82;
	int GL_OBJECT_VALIDATE_STATUS_ARB = 0x8B83;
	int GL_OBJECT_INFO_LOG_LENGTH_ARB = 0x8B84;
	int GL_OBJECT_ATTACHED_OBJECTS_ARB = 0x8B85;
	int GL_OBJECT_ACTIVE_UNIFORMS_ARB = 0x8B86;
	int GL_OBJECT_ACTIVE_UNIFORM_MAX_LENGTH_ARB = 0x8B87;
	int GL_OBJECT_SHADER_SOURCE_LENGTH_ARB = 0x8B88;

	/** Returned by the &lt;params&gt; parameter of GetObjectParameter{fi}vARB: */
	int GL_SHADER_OBJECT_ARB = 0x8B48;

	/** Returned by the &lt;type&gt; parameter of GetActiveUniformARB: */
	int GL_FLOAT_VEC2_ARB = 0x8B50;
	int GL_FLOAT_VEC3_ARB = 0x8B51;
	int GL_FLOAT_VEC4_ARB = 0x8B52;
	int GL_INT_VEC2_ARB = 0x8B53;
	int GL_INT_VEC3_ARB = 0x8B54;
	int GL_INT_VEC4_ARB = 0x8B55;
	int GL_BOOL_ARB = 0x8B56;
	int GL_BOOL_VEC2_ARB = 0x8B57;
	int GL_BOOL_VEC3_ARB = 0x8B58;
	int GL_BOOL_VEC4_ARB = 0x8B59;
	int GL_FLOAT_MAT2_ARB = 0x8B5A;
	int GL_FLOAT_MAT3_ARB = 0x8B5B;
	int GL_FLOAT_MAT4_ARB = 0x8B5C;
	int GL_SAMPLER_1D_ARB = 0x8B5D;
	int GL_SAMPLER_2D_ARB = 0x8B5E;
	int GL_SAMPLER_3D_ARB = 0x8B5F;
	int GL_SAMPLER_CUBE_ARB = 0x8B60;
	int GL_SAMPLER_1D_SHADOW_ARB = 0x8B61;
	int GL_SAMPLER_2D_SHADOW_ARB = 0x8B62;
	int GL_SAMPLER_2D_RECT_ARB = 0x8B63;
	int GL_SAMPLER_2D_RECT_SHADOW_ARB = 0x8B64;

	void glDeleteObjectARB(@GLhandleARB int obj);

	@GLhandleARB
	int glGetHandleARB(@GLenum int pname);

	void glDetachObjectARB(@GLhandleARB int containerObj, @GLhandleARB int attachedObj);

	@GLhandleARB
	int glCreateShaderObjectARB(@GLenum int shaderType);

	/**
	 * The ARB_shader_objects extension allows multiple, optionally null-terminated, source strings to define a shader program.
	 * <p/>
	 * This method uses just a single string, that should NOT be null-terminated.
	 */
	void glShaderSourceARB(@GLhandleARB int shader, @Constant("1") @GLsizei int count,
	                       @Indirect @Const @GLcharARB @Check ByteBuffer string,
	                       @AutoSize("string") @Indirect @Const @GLint int length);

	@Alternate("glShaderSourceARB")
	void glShaderSourceARB2(@GLhandleARB int shader, @Constant("1") @GLsizei int count, CharSequence string, @Constant("string.length()") @Indirect @Const int length);

	@Alternate(value = "glShaderSourceARB", nativeAlt = true)
	void glShaderSourceARB3(@GLhandleARB int shader, @Constant("strings.length") @GLsizei int count,
	                        @Const @PointerArray(value = "count", lengths = "length") CharSequence[] strings,
	                        @Constant("APIUtil.getLengths(caps, strings)") @Const IntBuffer length);

	void glCompileShaderARB(@GLhandleARB int shaderObj);

	@GLhandleARB
	int glCreateProgramObjectARB();

	void glAttachObjectARB(@GLhandleARB int containerObj, @GLhandleARB int obj);

	void glLinkProgramARB(@GLhandleARB int programObj);

	void glUseProgramObjectARB(@GLhandleARB int programObj);

	void glValidateProgramARB(@GLhandleARB int programObj);

	void glUniform1fARB(int location, float v0);

	void glUniform2fARB(int location, float v0, float v1);

	void glUniform3fARB(int location, float v0, float v1, float v2);

	void glUniform4fARB(int location, float v0, float v1, float v2, float v3);

	void glUniform1iARB(int location, int v0);

	void glUniform2iARB(int location, int v0, int v1);

	void glUniform3iARB(int location, int v0, int v1, int v2);

	void glUniform4iARB(int location, int v0, int v1, int v2, int v3);

	@StripPostfix("values")
	void glUniform1fvARB(int location, @AutoSize("values") @GLsizei int count, @Const FloatBuffer values);

	@StripPostfix("values")
	void glUniform2fvARB(int location, @AutoSize(value = "values", expression = " >> 1") @GLsizei int count, @Const FloatBuffer values);

	@StripPostfix("values")
	void glUniform3fvARB(int location, @AutoSize(value = "values", expression = " / 3") @GLsizei int count, @Const FloatBuffer values);

	@StripPostfix("values")
	void glUniform4fvARB(int location, @AutoSize(value = "values", expression = " >> 2") @GLsizei int count, @Const FloatBuffer values);

	@StripPostfix("values")
	void glUniform1ivARB(int location, @AutoSize(value = "values") @GLsizei int count, @Const IntBuffer values);

	@StripPostfix("values")
	void glUniform2ivARB(int location, @AutoSize(value = "values", expression = " >> 1") @GLsizei int count, @Const IntBuffer values);

	@StripPostfix("values")
	void glUniform3ivARB(int location, @AutoSize(value = "values", expression = " / 3") @GLsizei int count, @Const IntBuffer values);

	@StripPostfix("values")
	void glUniform4ivARB(int location, @AutoSize(value = "values", expression = " >> 2") @GLsizei int count, @Const IntBuffer values);

	@StripPostfix("matrices")
	void glUniformMatrix2fvARB(int location, @AutoSize(value = "matrices", expression = " >> 2") @GLsizei int count, boolean transpose, @Const FloatBuffer matrices);

	@StripPostfix("matrices")
	void glUniformMatrix3fvARB(int location, @AutoSize(value = "matrices", expression = " / (3 * 3)") @GLsizei int count, boolean transpose, @Const FloatBuffer matrices);

	@StripPostfix("matrices")
	void glUniformMatrix4fvARB(int location, @AutoSize(value = "matrices", expression = " >> 4") @GLsizei int count, boolean transpose, @Const FloatBuffer matrices);

	@StripPostfix("params")
	void glGetObjectParameterfvARB(@GLhandleARB int obj, @GLenum int pname, @OutParameter @Check FloatBuffer params);

	@Alternate("glGetObjectParameterfvARB")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetObjectParameterfvARB2(@GLhandleARB int obj, @GLenum int pname, @OutParameter FloatBuffer params);

	@StripPostfix("params")
	void glGetObjectParameterivARB(@GLhandleARB int obj, @GLenum int pname, @OutParameter @Check IntBuffer params);

	@Alternate("glGetObjectParameterivARB")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetObjectParameterivARB2(@GLhandleARB int obj, @GLenum int pname, @OutParameter IntBuffer params);

	void glGetInfoLogARB(@GLhandleARB int obj, @AutoSize("infoLog") @GLsizei int maxLength,
	                     @OutParameter @GLsizei @Check(value = "1", canBeNull = true) IntBuffer length,
	                     @OutParameter @GLcharARB ByteBuffer infoLog);

	@Alternate("glGetInfoLogARB")
	@GLreturn(value = "infoLog", maxLength = "maxLength")
	void glGetInfoLogARB2(@GLhandleARB int obj, @GLsizei int maxLength,
	                      @OutParameter @GLsizei @Constant("MemoryUtil.getAddress0(infoLog_length)") IntBuffer length,
	                      @OutParameter @GLcharARB ByteBuffer infoLog);

	void glGetAttachedObjectsARB(@GLhandleARB int containerObj, @AutoSize("obj") @GLsizei int maxCount,
	                             @OutParameter @GLsizei @Check(value = "1", canBeNull = true) IntBuffer count,
	                             @OutParameter @GLhandleARB IntBuffer obj);

	/**
	 * Returns the location of the uniform with the specified name. The ByteBuffer should contain the uniform name as a <b>null-terminated</b> string.
	 *
	 * @param programObj
	 * @param name
	 */
	int glGetUniformLocationARB(@GLhandleARB int programObj, @NullTerminated @Const @GLcharARB ByteBuffer name);

	@Alternate("glGetUniformLocationARB")
	int glGetUniformLocationARB(@GLhandleARB int programObj, @NullTerminated CharSequence name);

	void glGetActiveUniformARB(@GLhandleARB int programObj, @GLuint int index, @AutoSize("name") @GLsizei int maxLength,
	                           @OutParameter @GLsizei @Check(value = "1", canBeNull = true) IntBuffer length,
	                           @OutParameter @Check("1") IntBuffer size,
	                           @OutParameter @GLenum @Check("1") IntBuffer type,
	                           @OutParameter @GLcharARB ByteBuffer name);

	/** Overloads glGetActiveUniformARB. This version returns both size and type in the sizeType buffer (at .position() and .position() + 1). */
	@Alternate("glGetActiveUniformARB")
	@GLreturn(value = "name", maxLength = "maxLength")
	void glGetActiveUniformARB2(@GLhandleARB int programObj, @GLuint int index, @GLsizei int maxLength,
	                            @OutParameter @GLsizei @Constant("MemoryUtil.getAddress0(name_length)") IntBuffer length,
	                            @OutParameter @Check("2") IntBuffer sizeType,
	                            @OutParameter @GLenum @Constant("MemoryUtil.getAddress(sizeType, sizeType.position() + 1)") IntBuffer type,
	                            @OutParameter @GLcharARB ByteBuffer name);

	/** Overloads glGetActiveUniformARB. This version returns only the uniform name. */
	@Alternate(value = "glGetActiveUniformARB", javaAlt = true)
	@GLreturn(value = "name", maxLength = "maxLength")
	void glGetActiveUniformARB(@GLhandleARB int programObj, @GLuint int index, @GLsizei int maxLength,
	                           @OutParameter @GLsizei @Constant("MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress0(APIUtil.getBufferInt(caps)), MemoryUtil.getAddress(APIUtil.getBufferInt(caps), 1)") IntBuffer length,
	                           @OutParameter @GLcharARB ByteBuffer name);

	/** Overloads glGetActiveUniformARB. This version returns only the uniform size. */
	@Alternate(value = "glGetActiveUniformARB", javaAlt = true)
	@GLreturn(value = "size")
	void glGetActiveUniformSizeARB(@GLhandleARB int programObj, @GLuint int index, @Constant("0") @GLsizei int maxLength,
	                               @OutParameter @GLsizei @Constant("0L") IntBuffer length,
	                               @OutParameter IntBuffer size,
	                               @OutParameter @GLenum @Constant("MemoryUtil.getAddress(size, 1)") IntBuffer type, // Reuse size buffer and ignore
	                               @OutParameter @GLcharARB @Constant("APIUtil.getBufferByte0(caps)") ByteBuffer name);

	/** Overloads glGetActiveUniformARB. This version returns only the uniform type. */
	@Alternate(value = "glGetActiveUniformARB", javaAlt = true)
	@GLreturn(value = "type")
	void glGetActiveUniformTypeARB(@GLhandleARB int programObj, @GLuint int index, @Constant("0") @GLsizei int maxLength,
	                               @OutParameter @GLsizei @Constant("0L") IntBuffer length,
	                               @OutParameter @Constant("MemoryUtil.getAddress(type, 1)") IntBuffer size, // Reuse type buffer and ignore
	                               @OutParameter @GLenum IntBuffer type,
	                               @OutParameter @GLcharARB @Constant("APIUtil.getBufferByte0(caps)") ByteBuffer name);

	@StripPostfix("params")
	void glGetUniformfvARB(@GLhandleARB int programObj, int location, @OutParameter @Check FloatBuffer params);

	@StripPostfix("params")
	void glGetUniformivARB(@GLhandleARB int programObj, int location, @OutParameter @Check IntBuffer params);

	void glGetShaderSourceARB(@GLhandleARB int obj, @AutoSize("source") @GLsizei int maxLength,
	                          @OutParameter @GLsizei @Check(value = "1", canBeNull = true) IntBuffer length,
	                          @OutParameter @GLcharARB ByteBuffer source);

	@Alternate("glGetShaderSourceARB")
	@GLreturn(value = "source", maxLength = "maxLength")
	void glGetShaderSourceARB2(@GLhandleARB int obj, @GLsizei int maxLength,
	                           @OutParameter @GLsizei @Constant("MemoryUtil.getAddress0(source_length)") IntBuffer length,
	                           @OutParameter @GLcharARB ByteBuffer source);

}
