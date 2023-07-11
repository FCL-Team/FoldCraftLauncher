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

import java.nio.*;

public interface NV_vertex_array_range {
	int GL_VERTEX_ARRAY_RANGE_NV = 0x851D;
	int GL_VERTEX_ARRAY_RANGE_LENGTH_NV = 0x851E;
	int GL_VERTEX_ARRAY_RANGE_VALID_NV = 0x851F;
	int GL_MAX_VERTEX_ARRAY_RANGE_ELEMENT_NV = 0x8520;
	int GL_VERTEX_ARRAY_RANGE_POINTER_NV = 0x8521;

	void glVertexArrayRangeNV(@AutoSize("pPointer") @GLsizei int size,
	                          @Const
	                          @GLbyte
	                          @GLshort
	                          @GLint
	                          @GLfloat
	                          @GLdouble Buffer pPointer);

	void glFlushVertexArrayRangeNV();

	@PlatformDependent({Platform.WGL, Platform.GLX})
	@GLvoid
	@AutoSize("size")
	ByteBuffer glAllocateMemoryNV(int size, float readFrequency, float writeFrequency, float priority);

	@PlatformDependent({Platform.WGL, Platform.GLX})
	void glFreeMemoryNV(@Check @GLbyte Buffer pointer);
}
