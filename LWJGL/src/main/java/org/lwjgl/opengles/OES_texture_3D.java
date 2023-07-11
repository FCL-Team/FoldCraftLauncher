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

import org.lwjgl.util.generator.Check;
import org.lwjgl.util.generator.Const;
import org.lwjgl.util.generator.opengl.*;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public interface OES_texture_3D {

	/**
	 * Accepted by the &lt;target&gt; parameter of TexImage3DOES, TexSubImage3DOES,
	 * CopyTexSubImage3DOES, CompressedTexImage3DOES and
	 * CompressedTexSubImage3DOES, GetTexParameteriv, and GetTexParameterfv:
	 */
	int GL_TEXTURE_3D_OES = 0x806F;

	/**
	 * Accepted by the &lt;pname&gt; parameter of TexParameteriv, TexParameterfv,
	 * GetTexParameteriv, and GetTexParameterfv:
	 */
	int GL_TEXTURE_WRAP_R_OES = 0x8072;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, and
	 * GetFloatv:
	 */
	int GL_MAX_3D_TEXTURE_SIZE_OES = 0x8073,
		GL_TEXTURE_BINDING_3D_OES  = 0x806A;

	void glTexImage3DOES(@GLenum int target, int level,
	                     @GLenum int internalFormat, @GLsizei int width, @GLsizei int height, @GLsizei int depth, int border,
	                     @GLenum int format, @GLenum int type,
	                     @Check(value = "GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth)", canBeNull = true)
	                     @Const @GLbyte @GLshort @GLint @GLfloat Buffer pixels);

	void glTexSubImage3DOES(@GLenum int target, int level,
	                        int xoffset, int yoffset, int zoffset,
	                        @GLsizei int width, @GLsizei int height, @GLsizei int depth,
	                        @GLenum int format, @GLenum int type,
	                        @Check("GLChecks.calculateImageStorage(pixels, format, type, width, height, depth)")
	                        @Const @GLbyte @GLshort @GLint @GLfloat Buffer pixels);

	void glCopyTexSubImage3DOES(@GLenum int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, @GLsizei int width, @GLsizei int height);

	void glCompressedTexImage3DOES(@GLenum int target, int level, @GLenum int internalformat,
	                               @GLsizei int width, @GLsizei int height, @GLsizei int depth,
	                               int border, @GLsizei int imageSize,
	                               @Check @Const @GLvoid ByteBuffer data);

	void glCompressedTexSubImage3DOES(@GLenum int target, int level,
	                                  int xoffset, int yoffset, int zoffset,
	                                  @GLsizei int width, @GLsizei int height, @GLsizei int depth,
	                                  @GLenum int format, @GLsizei int imageSize,
	                                  @Check @Const @GLvoid ByteBuffer data);

	void glFramebufferTexture3DOES(@GLenum int target, @GLenum int attachment, @GLenum int textarget, @GLuint int texture, int level, int zoffset);

}