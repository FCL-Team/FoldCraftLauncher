/*
 * Copyright (c) 2002-2011 LWJGL Project
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
import java.nio.IntBuffer;

public interface EXT_debug_label {

	/**
	 * Accepted by the &lt;type&gt; parameter of LabelObjectEXT and
	 * GetObjectLabelEXT:
	 */
	int GL_BUFFER_OBJECT_EXT           = 0x9151,
		GL_SHADER_OBJECT_EXT           = 0x8B48,
		GL_PROGRAM_OBJECT_EXT          = 0x8B40,
		GL_VERTEX_ARRAY_OBJECT_EXT     = 0x9154,
		GL_QUERY_OBJECT_EXT            = 0x9153,
		GL_PROGRAM_PIPELINE_OBJECT_EXT = 0x8A4F;

	void glLabelObjectEXT(@GLenum int type, @GLuint int object,
	                      @AutoSize("label") @GLsizei int length,
	                      @Const @GLchar ByteBuffer label);

	@Alternate("glLabelObjectEXT")
	void glLabelObjectEXT(@GLenum int type, @GLuint int object,
	                      @Constant("label.length()") @GLsizei int length,
	                      CharSequence label);

	void glGetObjectLabelEXT(@GLenum int type, @GLuint int object,
	                         @AutoSize(value = "label", canBeNull = true) @GLsizei int bufSize,
	                         @Check(value = "1", canBeNull = true) @OutParameter @GLsizei IntBuffer length,
	                         @Check(canBeNull = true) @OutParameter @GLchar ByteBuffer label);

	@Alternate("glGetObjectLabelEXT")
	@GLreturn(value = "label", maxLength = "bufSize")
	void glGetObjectLabelEXT2(@GLenum int type, @GLuint int object,
	                          @GLsizei int bufSize,
	                          @Constant("MemoryUtil.getAddress0(label_length)") @OutParameter @GLsizei IntBuffer length,
	                          @OutParameter @GLchar ByteBuffer label);

}