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

public interface ARB_matrix_palette {

	int GL_MATRIX_PALETTE_ARB = 0x8840;
	int GL_MAX_MATRIX_PALETTE_STACK_DEPTH_ARB = 0x8841;
	int GL_MAX_PALETTE_MATRICES_ARB = 0x8842;
	int GL_CURRENT_PALETTE_MATRIX_ARB = 0x8843;
	int GL_MATRIX_INDEX_ARRAY_ARB = 0x8844;
	int GL_CURRENT_MATRIX_INDEX_ARB = 0x8845;
	int GL_MATRIX_INDEX_ARRAY_SIZE_ARB = 0x8846;
	int GL_MATRIX_INDEX_ARRAY_TYPE_ARB = 0x8847;
	int GL_MATRIX_INDEX_ARRAY_STRIDE_ARB = 0x8848;
	int GL_MATRIX_INDEX_ARRAY_POINTER_ARB = 0x8849;

	void glCurrentPaletteMatrixARB(int index);

	void glMatrixIndexPointerARB(int size, @AutoType("pPointer") @GLenum int type, @GLsizei int stride,
	                             @CachedReference
	                             @BufferObject(BufferKind.ArrayVBO)
	                             @Check
	                             @Const
	                             @GLubyte
	                             @GLushort
	                             @GLuint Buffer pPointer);

	@StripPostfix("pIndices")
	void glMatrixIndexubvARB(@AutoSize("pIndices") int size, @GLubyte ByteBuffer pIndices);

	@StripPostfix("pIndices")
	void glMatrixIndexusvARB(@AutoSize("pIndices") int size, @GLushort ShortBuffer pIndices);

	@StripPostfix("pIndices")
	void glMatrixIndexuivARB(@AutoSize("pIndices") int size, @GLuint IntBuffer pIndices);
}
