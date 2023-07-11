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
import org.lwjgl.util.generator.opengl.GLdouble;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLfloat;
import org.lwjgl.util.generator.opengl.GLsizei;

import java.nio.*;

public interface EXT_fog_coord {
	int GL_FOG_COORDINATE_SOURCE_EXT = 0x8450;
	int GL_FOG_COORDINATE_EXT = 0x8451;
	int GL_FRAGMENT_DEPTH_EXT = 0x8452;
	int GL_CURRENT_FOG_COORDINATE_EXT = 0x8453;
	int GL_FOG_COORDINATE_ARRAY_TYPE_EXT = 0x8454;
	int GL_FOG_COORDINATE_ARRAY_STRIDE_EXT = 0x8455;
	int GL_FOG_COORDINATE_ARRAY_POINTER_EXT = 0x8456;
	int GL_FOG_COORDINATE_ARRAY_EXT = 0x8457;

	void glFogCoordfEXT(float coord);

	void glFogCoorddEXT(double coord);

	void glFogCoordPointerEXT(@AutoType("data") @GLenum int type, @GLsizei int stride,
	                          @CachedReference
	                          @BufferObject(BufferKind.ArrayVBO)
	                          @Check
	                          @Const
	                          @GLfloat
	                          @GLdouble Buffer data);
}
