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

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public interface NV_parameter_buffer_object {

	/** Accepted by the &lt;pname&gt; parameter of GetProgramivARB: */
	int GL_MAX_PROGRAM_PARAMETER_BUFFER_BINDINGS_NV = 0x8DA0;
	int GL_MAX_PROGRAM_PARAMETER_BUFFER_SIZE_NV = 0x8DA1;

	/**
	 * Accepted by the &lt;target&gt; parameter of ProgramBufferParametersfvNV,
	 * ProgramBufferParametersIivNV, and ProgramBufferParametersIuivNV,
	 * BindBufferRangeNV, BindBufferOffsetNV, BindBufferBaseNV, and BindBuffer
	 * and the &lt;value&gt; parameter of GetIntegerIndexedvEXT:
	 */
	int GL_VERTEX_PROGRAM_PARAMETER_BUFFER_NV = 0x8DA2;
	int GL_GEOMETRY_PROGRAM_PARAMETER_BUFFER_NV = 0x8DA3;
	int GL_FRAGMENT_PROGRAM_PARAMETER_BUFFER_NV = 0x8DA4;

	@StripPostfix("params")
	void glProgramBufferParametersfvNV(@GLenum int target, @GLuint int buffer, @GLuint int index,
									   @AutoSize(value = "params", expression = " >> 2") @GLsizei int count, @Const FloatBuffer params);

	@StripPostfix("params")
	void glProgramBufferParametersIivNV(@GLenum int target, @GLuint int buffer, @GLuint int index,
										@AutoSize(value = "params", expression = " >> 2") @GLsizei int count, @Const IntBuffer params);

	@StripPostfix("params")
	void glProgramBufferParametersIuivNV(@GLenum int target, @GLuint int buffer, @GLuint int index,
										 @AutoSize(value = "params", expression = " >> 2") @GLuint int count, @Const @GLuint IntBuffer params);

}