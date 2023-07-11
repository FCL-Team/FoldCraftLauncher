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

import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;

public interface EXT_texture_storage {

	/** Accepted by the &lt;value&gt; parameter of GetTexParameter{if}v: */

	int GL_TEXTURE_IMMUTABLE_FORMAT_EXT = 0x912F;
	/**
	 * Accepted by the &lt;internalformat&gt; parameter of TexStorage* when
	 * implemented on OpenGL ES:
	 */
	int GL_ALPHA8_EXT                   = 0x803C,
		GL_LUMINANCE8_EXT               = 0x8040,
		GL_LUMINANCE8_ALPHA8_EXT        = 0x8045,
		GL_RGBA32F_EXT                  = 0x8814,
		GL_RGB32F_EXT                   = 0x8815,
		GL_ALPHA32F_EXT                 = 0x8816,
		GL_LUMINANCE32F_EXT             = 0x8818,
		GL_LUMINANCE_ALPHA32F_EXT       = 0x8819,
		GL_RGBA16F_EXT                  = 0x881A,
		GL_RGB16F_EXT                   = 0x881B,
		GL_ALPHA16F_EXT                 = 0x881C,
		GL_LUMINANCE16F_EXT             = 0x881E,
		GL_LUMINANCE_ALPHA16F_EXT       = 0x881F,
		GL_RGB10_A2_EXT                 = 0x8059,
		GL_RGB10_EXT                    = 0x8052,
		GL_BGRA8_EXT                    = 0x93A1;

	void glTexStorage1DEXT(@GLenum int target, @GLsizei int levels,
	                       @GLenum int internalformat,
	                       @GLsizei int width);

	void glTexStorage2DEXT(@GLenum int target, @GLsizei int levels,
	                       @GLenum int internalformat,
	                       @GLsizei int width, @GLsizei int height);

	void glTexStorage3DEXT(@GLenum int target, @GLsizei int levels,
	                       @GLenum int internalformat,
	                       @GLsizei int width, @GLsizei int height, @GLsizei int depth);

	void glTextureStorage1DEXT(@GLuint int texture, @GLenum int target, @GLsizei int levels,
	                           @GLenum int internalformat,
	                           @GLsizei int width);

	void glTextureStorage2DEXT(@GLuint int texture, @GLenum int target, @GLsizei int levels,
	                           @GLenum int internalformat,
	                           @GLsizei int width, @GLsizei int height);

	void glTextureStorage3DEXT(@GLuint int texture, @GLenum int target, @GLsizei int levels,
	                           @GLenum int internalformat,
	                           @GLsizei int width, @GLsizei int height, @GLsizei int depth);

}