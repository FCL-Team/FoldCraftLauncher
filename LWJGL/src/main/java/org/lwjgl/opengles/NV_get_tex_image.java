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

import org.lwjgl.util.generator.Alternate;
import org.lwjgl.util.generator.Check;
import org.lwjgl.util.generator.OutParameter;
import org.lwjgl.util.generator.StripPostfix;
import org.lwjgl.util.generator.opengl.*;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public interface NV_get_tex_image {

	int GL_TEXTURE_WIDTH_NV                 = 0x1000,
		GL_TEXTURE_HEIGHT_NV                = 0x1001,
		GL_TEXTURE_INTERNAL_FORMAT_NV       = 0x1003,
		GL_TEXTURE_COMPONENTS_NV            = GL_TEXTURE_INTERNAL_FORMAT_NV,
		GL_TEXTURE_BORDER_NV                = 0x1005,
		GL_TEXTURE_RED_SIZE_NV              = 0x805C,
		GL_TEXTURE_GREEN_SIZE_NV            = 0x805D,
		GL_TEXTURE_BLUE_SIZE_NV             = 0x805E,
		GL_TEXTURE_ALPHA_SIZE_NV            = 0x805F,
		GL_TEXTURE_LUMINANCE_SIZE_NV        = 0x8060,
		GL_TEXTURE_INTENSITY_SIZE_NV        = 0x8061,
		GL_TEXTURE_DEPTH_NV                 = 0x8071,
		GL_TEXTURE_COMPRESSED_IMAGE_SIZE_NV = 0x86A0,
		GL_TEXTURE_COMPRESSED_NV            = 0x86A1,
		GL_TEXTURE_DEPTH_SIZE_NV            = 0x884A;

	void glGetTexImageNV(@GLenum int target, @GLint int level, @GLenum int format, @GLenum int type,
	                     @OutParameter
	                     @Check("GLChecks.calculateImageStorage(img, format, type, 1, 1, 1)")
	                     @GLbyte
	                     @GLshort
	                     @GLint
	                     @GLfloat Buffer img);

	void glGetCompressedTexImageNV(@GLenum int target, @GLint int level, @OutParameter @Check @GLvoid ByteBuffer img);

	@StripPostfix("params")
	void glGetTexLevelParameterfvNV(@GLenum int target, @GLint int level, @GLenum int pname, @OutParameter @Check("1") @GLfloat FloatBuffer params);

	@Alternate("glGetTexLevelParameterfvNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetTexLevelParameterfvNV2(@GLenum int target, @GLint int level, @GLenum int pname, @OutParameter @GLfloat FloatBuffer params);

	@StripPostfix("params")
	void glGetTexLevelParameterivNV(@GLenum int target, @GLint int level, @GLenum int pname, @OutParameter @Check("1") @GLint IntBuffer params);

	@Alternate("glGetTexLevelParameterivNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetTexLevelParameterivNV2(@GLenum int target, @GLint int level, @GLenum int pname, @OutParameter @GLint IntBuffer params);

}