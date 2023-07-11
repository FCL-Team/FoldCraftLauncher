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

import java.nio.Buffer;

public interface EXT_secondary_color {

	int GL_COLOR_SUM_EXT = 0x8458;
	int GL_CURRENT_SECONDARY_COLOR_EXT = 0x8459;
	int GL_SECONDARY_COLOR_ARRAY_SIZE_EXT = 0x845A;
	int GL_SECONDARY_COLOR_ARRAY_TYPE_EXT = 0x845B;
	int GL_SECONDARY_COLOR_ARRAY_STRIDE_EXT = 0x845C;
	int GL_SECONDARY_COLOR_ARRAY_POINTER_EXT = 0x845D;
	int GL_SECONDARY_COLOR_ARRAY_EXT = 0x845E;

	void glSecondaryColor3bEXT(byte red, byte green, byte blue);

	void glSecondaryColor3fEXT(float red, float green, float blue);

	void glSecondaryColor3dEXT(double red, double green, double blue);

	void glSecondaryColor3ubEXT(@GLubyte byte red, @GLubyte byte green, @GLubyte byte blue);

	void glSecondaryColorPointerEXT(int size, @AutoType("pPointer") @GLenum int type, @GLsizei int stride,
	                                @CachedReference
	                                @BufferObject(BufferKind.ArrayVBO)
	                                @Check
	                                @Const
	                                @GLbyte
	                                @GLubyte
	                                @GLfloat
	                                @GLdouble Buffer pPointer);
}
