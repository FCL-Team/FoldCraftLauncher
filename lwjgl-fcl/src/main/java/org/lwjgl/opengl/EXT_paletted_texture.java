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

public interface EXT_paletted_texture {

	/**
	 * Accepted by the internalformat parameter of TexImage1D, TexImage2D and
	 * TexImage3DEXT:
	 */
	int GL_COLOR_INDEX1_EXT = 0x80E2;
	int GL_COLOR_INDEX2_EXT = 0x80E3;
	int GL_COLOR_INDEX4_EXT = 0x80E4;
	int GL_COLOR_INDEX8_EXT = 0x80E5;
	int GL_COLOR_INDEX12_EXT = 0x80E6;
	int GL_COLOR_INDEX16_EXT = 0x80E7;

	/**
	 * Accepted by the pname parameter of GetColorTableParameterivEXT and
	 * GetColorTableParameterfvEXT:
	 */
	int GL_COLOR_TABLE_FORMAT_EXT = 0x80D8;
	int GL_COLOR_TABLE_WIDTH_EXT = 0x80D9;
	int GL_COLOR_TABLE_RED_SIZE_EXT = 0x80DA;
	int GL_COLOR_TABLE_GREEN_SIZE_EXT = 0x80DB;
	int GL_COLOR_TABLE_BLUE_SIZE_EXT = 0x80DC;
	int GL_COLOR_TABLE_ALPHA_SIZE_EXT = 0x80DD;
	int GL_COLOR_TABLE_LUMINANCE_SIZE_EXT = 0x80DE;
	int GL_COLOR_TABLE_INTENSITY_SIZE_EXT = 0x80DF;

	/**
	 * Accepted by the value parameter of GetTexLevelParameter{if}v:
	 */
	int GL_TEXTURE_INDEX_SIZE_EXT = 0x80ED;

	void glColorTableEXT(@GLenum int target, @GLenum int internalFormat, @GLsizei int width, @GLenum int format, @GLenum int type,
	                     @Check("GLChecks.calculateImageStorage(data, format, type, width, 1, 1)")
	                     @Const
	                     @GLbyte
	                     @GLshort
	                     @GLint
	                     @GLfloat
	                     @GLdouble Buffer data);

	void glColorSubTableEXT(@GLenum int target, @GLsizei int start, @GLsizei int count, @GLenum int format, @GLenum int type,
	                        @Check("GLChecks.calculateImageStorage(data, format, type, count, 1, 1)")
	                        @Const
	                        @GLbyte
	                        @GLshort
	                        @GLint
	                        @GLfloat
	                        @GLdouble Buffer data);

	void glGetColorTableEXT(@GLenum int target, @GLenum int format, @GLenum int type,
			                @OutParameter
	                        @Check
	                        @GLbyte
	                        @GLshort
	                        @GLint
	                        @GLfloat
	                        @GLdouble Buffer data);

	@StripPostfix("params")
	void glGetColorTableParameterivEXT(@GLenum int target, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	@StripPostfix("params")
	void glGetColorTableParameterfvEXT(@GLenum int target, @GLenum int pname, @OutParameter @Check("4") FloatBuffer params);
}
