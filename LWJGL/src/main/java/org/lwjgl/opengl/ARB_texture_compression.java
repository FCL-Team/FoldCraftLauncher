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
import org.lwjgl.util.generator.opengl.GLvoid;

import java.nio.*;

public interface ARB_texture_compression {
	int GL_COMPRESSED_ALPHA_ARB = 0x84E9;
	int GL_COMPRESSED_LUMINANCE_ARB = 0x84EA;
	int GL_COMPRESSED_LUMINANCE_ALPHA_ARB = 0x84EB;
	int GL_COMPRESSED_INTENSITY_ARB = 0x84EC;
	int GL_COMPRESSED_RGB_ARB = 0x84ED;
	int GL_COMPRESSED_RGBA_ARB = 0x84EE;
	int GL_TEXTURE_COMPRESSION_HINT_ARB = 0x84EF;
	int GL_TEXTURE_COMPRESSED_IMAGE_SIZE_ARB = 0x86A0;
	int GL_TEXTURE_COMPRESSED_ARB = 0x86A1;
	int GL_NUM_COMPRESSED_TEXTURE_FORMATS_ARB = 0x86A2;
	int GL_COMPRESSED_TEXTURE_FORMATS_ARB = 0x86A3;

	void glCompressedTexImage1DARB(@GLenum int target, int level, @GLenum int internalformat, @GLsizei int width, int border, @AutoSize("pData") @GLsizei int imageSize,
	                               @BufferObject(BufferKind.UnpackPBO)
	                               @Check
	                               @Const
	                               @GLvoid
	                               ByteBuffer pData);

	void glCompressedTexImage2DARB(@GLenum int target, int level, @GLenum int internalformat, @GLsizei int width, @GLsizei int height, int border, @AutoSize("pData") @GLsizei int imageSize,
	                               @BufferObject(BufferKind.UnpackPBO)
	                               @Check
	                               @Const
	                               @GLvoid
	                               ByteBuffer pData);

	void glCompressedTexImage3DARB(@GLenum int target, int level, @GLenum int internalformat, @GLsizei int width, @GLsizei int height, @GLsizei int depth, int border, @AutoSize("pData") @GLsizei int imageSize,
	                               @BufferObject(BufferKind.UnpackPBO)
	                               @Check
	                               @Const
	                               @GLvoid
	                               ByteBuffer pData);

	void glCompressedTexSubImage1DARB(@GLenum int target, int level, int xoffset, @GLsizei int width, @GLenum int format, @AutoSize("pData") @GLsizei int imageSize,
	                                  @BufferObject(BufferKind.UnpackPBO)
	                                  @Check
	                                  @Const
	                                  @GLvoid
	                                  ByteBuffer pData);

	void glCompressedTexSubImage2DARB(@GLenum int target, int level, int xoffset, int yoffset, @GLsizei int width, @GLsizei int height, @GLenum int format, @AutoSize("pData") @GLsizei int imageSize,
	                                  @BufferObject(BufferKind.UnpackPBO)
	                                  @Check
	                                  @Const
	                                  @GLvoid
	                                  ByteBuffer pData);

	void glCompressedTexSubImage3DARB(@GLenum int target, int level, int xoffset, int yoffset, int zoffset, @GLsizei int width, @GLsizei int height, @GLsizei int depth, @GLenum int format, @AutoSize("pData") @GLsizei int imageSize,
	                                  @BufferObject(BufferKind.UnpackPBO)
	                                  @Check
	                                  @Const
	                                  @GLvoid
	                                  ByteBuffer pData);

	void glGetCompressedTexImageARB(@GLenum int target, int lod,
			                        @OutParameter
	                                @BufferObject(BufferKind.PackPBO)
	                                @Check
	                                @GLvoid
	                                ByteBuffer pImg);
}
