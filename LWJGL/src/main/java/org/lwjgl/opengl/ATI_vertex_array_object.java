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

import java.nio.*;

public interface ATI_vertex_array_object {
	int GL_STATIC_ATI = 0x8760;
	int GL_DYNAMIC_ATI = 0x8761;
	int GL_PRESERVE_ATI = 0x8762;
	int GL_DISCARD_ATI = 0x8763;
	int GL_OBJECT_BUFFER_SIZE_ATI = 0x8764;
	int GL_OBJECT_BUFFER_USAGE_ATI = 0x8765;
	int GL_ARRAY_OBJECT_BUFFER_ATI = 0x8766;
	int GL_ARRAY_OBJECT_OFFSET_ATI = 0x8767;

	@GenerateAutos
	@GLuint
	int glNewObjectBufferATI(@AutoSize("pPointer") @GLsizei int size,
	                         @Const
	                         @GLbyte
	                         @GLshort
	                         @GLint
	                         @GLfloat
	                         @GLdouble Buffer pPointer, @GLenum int usage);

	boolean glIsObjectBufferATI(@GLuint int buffer);

	void glUpdateObjectBufferATI(@GLuint int buffer, @GLuint int offset, @AutoSize("pPointer") @GLsizei int size,
	                             @Const
	                             @GLbyte
	                             @GLshort
	                             @GLint
	                             @GLfloat
	                             @GLdouble Buffer pPointer, @GLenum int preserve);

	@StripPostfix("params")
	void glGetObjectBufferfvATI(@GLuint int buffer, @GLenum int pname, @OutParameter @Check FloatBuffer params);

	@StripPostfix("params")
	void glGetObjectBufferivATI(@GLuint int buffer, @GLenum int pname, @OutParameter @Check IntBuffer params);

	@Alternate("glGetObjectBufferivATI")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetObjectBufferivATI2(@GLuint int buffer, @GLenum int pname, @OutParameter IntBuffer params);

	void glFreeObjectBufferATI(@GLuint int buffer);

	void glArrayObjectATI(@GLenum int array, int size, @GLenum int type, @GLsizei int stride, @GLuint int buffer, @GLuint int offset);

	@StripPostfix("params")
	void glGetArrayObjectfvATI(@GLenum int array, @GLenum int pname, @OutParameter @Check("4") FloatBuffer params);

	@StripPostfix("params")
	void glGetArrayObjectivATI(@GLenum int array, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	void glVariantArrayObjectATI(@GLuint int id, @GLenum int type, @GLsizei int stride, @GLuint int buffer, @GLuint int offset);

	@StripPostfix("params")
	void glGetVariantArrayObjectfvATI(@GLuint int id, @GLenum int pname, @OutParameter @Check("4") FloatBuffer params);

	@StripPostfix("params")
	void glGetVariantArrayObjectivATI(@GLuint int id, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);
}
