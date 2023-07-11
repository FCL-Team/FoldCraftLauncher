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

public interface EXT_transform_feedback {

	/**
	 * Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 * BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData,
	 * GetBufferPointerv, BindBufferRangeEXT, BindBufferOffsetEXT and
	 * BindBufferBaseEXT:
	 */
	int GL_TRANSFORM_FEEDBACK_BUFFER_EXT = 0x8C8E;

	/**
	 * Accepted by the &lt;param&gt; parameter of GetIntegerIndexedvEXT and
	 * GetBooleanIndexedvEXT:
	 */
	int GL_TRANSFORM_FEEDBACK_BUFFER_START_EXT = 0x8C84;
	int GL_TRANSFORM_FEEDBACK_BUFFER_SIZE_EXT = 0x8C85;

	/**
	 * Accepted by the &lt;param&gt; parameter of GetIntegerIndexedvEXT and
	 * GetBooleanIndexedvEXT, and by the &lt;pname&gt; parameter of GetBooleanv,
	 * GetDoublev, GetIntegerv, and GetFloatv:
	 */
	int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING_EXT = 0x8C8F;

	/** Accepted by the &lt;bufferMode&gt; parameter of TransformFeedbackVaryingsEXT: */
	int GL_INTERLEAVED_ATTRIBS_EXT = 0x8C8C;
	int GL_SEPARATE_ATTRIBS_EXT = 0x8C8D;

	/**
	 * Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery, and
	 * GetQueryiv:
	 */
	int GL_PRIMITIVES_GENERATED_EXT = 0x8C87;
	int GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN_EXT = 0x8C88;

	/**
	 * Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled, and by
	 * the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 * GetDoublev:
	 */
	int GL_RASTERIZER_DISCARD_EXT = 0x8C89;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 * and GetFloatv:
	 */
	int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS_EXT = 0x8C8A;
	int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS_EXT = 0x8C8B;
	int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS_EXT = 0x8C80;

	/** Accepted by the &lt;pname&gt; parameter of GetProgramiv: */
	int GL_TRANSFORM_FEEDBACK_VARYINGS_EXT = 0x8C83;
	int GL_TRANSFORM_FEEDBACK_BUFFER_MODE_EXT = 0x8C7F;
	int GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH_EXT = 0x8C76;

	void glBindBufferRangeEXT(@GLenum int target, @GLuint int index, @GLuint int buffer, @GLintptr long offset, @GLsizeiptr long size);

	void glBindBufferOffsetEXT(@GLenum int target, @GLuint int index, @GLuint int buffer, @GLintptr long offset);

	void glBindBufferBaseEXT(@GLenum int target, @GLuint int index, @GLuint int buffer);

	void glBeginTransformFeedbackEXT(@GLenum int primitiveMode);

	void glEndTransformFeedbackEXT();

	void glTransformFeedbackVaryingsEXT(@GLuint int program, @GLsizei int count,
	                                    @Const @NullTerminated("count") @GLchar @PointerArray("count") ByteBuffer varyings,
	                                    @GLenum int bufferMode);

	@Alternate("glTransformFeedbackVaryingsEXT")
	void glTransformFeedbackVaryingsEXT(@GLuint int program, @Constant("varyings.length") @GLsizei int count,
	                                    @Const @NullTerminated @PointerArray("count") CharSequence[] varyings,
	                                    @GLenum int bufferMode);

	void glGetTransformFeedbackVaryingEXT(@GLuint int program, @GLuint int index, @AutoSize("name") @GLsizei int bufSize,
	                                      @OutParameter @GLsizei @Check(value = "1", canBeNull = true) IntBuffer length,
	                                      @OutParameter @GLsizei @Check("1") IntBuffer size,
	                                      @OutParameter @GLenum @Check("1") IntBuffer type,
	                                      @OutParameter @GLchar ByteBuffer name);

	@Alternate("glGetTransformFeedbackVaryingEXT")
	@GLreturn(value = "name", maxLength = "bufSize")
	void glGetTransformFeedbackVaryingEXT2(@GLuint int program, @GLuint int index, @GLsizei int bufSize,
	                                       @OutParameter @GLsizei @Constant("MemoryUtil.getAddress0(name_length)") IntBuffer length,
	                                       @OutParameter @GLsizei @Check("1") IntBuffer size,
	                                       @OutParameter @GLenum @Check("1") IntBuffer type,
	                                       @OutParameter @GLchar ByteBuffer name);

}