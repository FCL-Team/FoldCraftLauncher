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

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLreturn;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.IntBuffer;

public interface OES_framebuffer_object {

	/**
	 * Accepted by the &lt;target&gt; parameter of BindFramebufferOES,
	 * CheckFramebufferStatusOES, FramebufferTexture{2D|3D}OES,
	 * FramebufferRenderbufferOES, and
	 * GetFramebufferAttachmentParameterivOES:
	 */
	int GL_FRAMEBUFFER_OES = 0x8D40;

	/**
	 * Accepted by the &lt;target&gt; parameter of BindRenderbufferOES,
	 * RenderbufferStorageOES, and GetRenderbufferParameterivOES, and
	 * returned by GetFramebufferAttachmentParameterivOES:
	 */
	int GL_RENDERBUFFER_OES = 0x8D41;

	/**
	 * Accepted by the &lt;internalformat&gt; parameter of
	 * RenderbufferStorageOES:
	 */
	int GL_DEPTH_COMPONENT16_OES = 0x81A5,
		GL_RGBA4_OES             = 0x8056,
		GL_RGB5_A1_OES           = 0x8057,
		GL_RGB565_OES            = 0x8D62,
		GL_STENCIL_INDEX1_OES    = 0x8D46,
		GL_STENCIL_INDEX4_OES    = 0x8D47,
		GL_STENCIL_INDEX8_OES    = 0x8D48;

	/** Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameterivOES: */
	int GL_RENDERBUFFER_WIDTH_OES           = 0x8D42,
		GL_RENDERBUFFER_HEIGHT_OES          = 0x8D43,
		GL_RENDERBUFFER_INTERNAL_FORMAT_OES = 0x8D44,
		GL_RENDERBUFFER_RED_SIZE_OES        = 0x8D50,
		GL_RENDERBUFFER_GREEN_SIZE_OES      = 0x8D51,
		GL_RENDERBUFFER_BLUE_SIZE_OES       = 0x8D52,
		GL_RENDERBUFFER_ALPHA_SIZE_OES      = 0x8D53,
		GL_RENDERBUFFER_DEPTH_SIZE_OES      = 0x8D54,
		GL_RENDERBUFFER_STENCIL_SIZE_OES    = 0x8D55;

	/**
	 * Accepted by the &lt;pname&gt; parameter of
	 * GetFramebufferAttachmentParameterivOES:
	 */
	int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_OES           = 0x8CD0,
		GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_OES           = 0x8CD1,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL_OES         = 0x8CD2,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE_OES = 0x8CD3,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_3D_ZOFFSET_OES    = 0x8CD4;

	/**
	 * Accepted by the &lt;attachment&gt; parameter of
	 * FramebufferTexture{2D|3D}OES, FramebufferRenderbufferOES, and
	 * GetFramebufferAttachmentParameterivOES
	 */
	int GL_COLOR_ATTACHMENT0_OES  = 0x8CE0,
		GL_DEPTH_ATTACHMENT_OES   = 0x8D00,
		GL_STENCIL_ATTACHMENT_OES = 0x8D20;

	/**
	 * Returned by GetFramebufferAttachmentParameterivOES when the
	 * &lt;pname&gt; parameter is FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_OES:
	 */
	int GL_NONE_OES = 0;

	/** Returned by CheckFramebufferStatusOES(): */
	int GL_FRAMEBUFFER_COMPLETE_OES                      = 0x8CD5,
		GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_OES         = 0x8CD6,
		GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_OES = 0x8CD7,
		GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_OES         = 0x8CD9,
		GL_FRAMEBUFFER_INCOMPLETE_FORMATS_OES            = 0x8CDA,
		GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_OES        = 0x8CDB,
		GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_OES        = 0x8CDC,
		GL_FRAMEBUFFER_UNSUPPORTED_OES                   = 0x8CDD;

	/** Accepted by GetIntegerv(): */
	int GL_FRAMEBUFFER_BINDING_OES   = 0x8CA6,
		GL_RENDERBUFFER_BINDING_OES  = 0x8CA7,
		GL_MAX_RENDERBUFFER_SIZE_OES = 0x84E8;

	/** Returned by GetError(): */
	int GL_INVALID_FRAMEBUFFER_OPERATION_OES = 0x0506;

	boolean glIsRenderbufferOES(@GLuint int renderbuffer);

	void glBindRenderbufferOES(@GLenum int target, @GLuint int renderbuffer);

	void glDeleteRenderbuffersOES(@AutoSize("renderbuffers") int n, @Const @GLuint IntBuffer renderbuffers);

	@Alternate("glDeleteRenderbuffersOES")
	void glDeleteRenderbuffersOES(@Constant("1") int n, @Constant(value = "APIUtil.getInt(renderbuffer)", keepParam = true) int renderbuffer);

	void glGenRenderbuffersOES(@AutoSize("renderbuffers") int n, @OutParameter @GLuint IntBuffer renderbuffers);

	@Alternate("glGenRenderbuffersOES")
	@GLreturn("renderbuffers")
	void glGenRenderbuffersOES2(@Constant("1") int n, @OutParameter @GLuint IntBuffer renderbuffers);

	void glRenderbufferStorageOES(@GLenum int target, @GLenum int internalformat, @GLsizei int width, @GLsizei int height);

	@StripPostfix("params")
	void glGetRenderbufferParameterivOES(@GLenum int target, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	/** @deprecated Will be removed in 3.0. Use {@link #glGetRenderbufferParameteriOES} instead. */
	@Alternate("glGetRenderbufferParameterivOES")
	@GLreturn("params")
	@StripPostfix("params")
	@Reuse(value = "OESFramebufferObject", method = "glGetRenderbufferParameteriOES")
	@Deprecated
	void glGetRenderbufferParameterivOES2(@GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	@Alternate("glGetRenderbufferParameterivOES")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetRenderbufferParameterivOES3(@GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	boolean glIsFramebufferOES(@GLuint int framebuffer);

	void glBindFramebufferOES(@GLenum int target, @GLuint int framebuffer);

	void glDeleteFramebuffersOES(@AutoSize("framebuffers") int n, @Const @GLuint IntBuffer framebuffers);

	@Alternate("glDeleteFramebuffersOES")
	void glDeleteFramebuffersOES(@Constant("1") int n, @Constant(value = "APIUtil.getInt(framebuffer)", keepParam = true) int framebuffer);

	void glGenFramebuffersOES(@AutoSize("framebuffers") int n, @OutParameter @GLuint IntBuffer framebuffers);

	@Alternate("glGenFramebuffersOES")
	@GLreturn("framebuffers")
	void glGenFramebuffersOES2(@Constant("1") int n, @OutParameter @GLuint IntBuffer framebuffers);

	@GLenum
	int glCheckFramebufferStatusOES(@GLenum int target);

	void glFramebufferTexture2DOES(@GLenum int target, @GLenum int attachment, @GLenum int textarget, @GLuint int texture, int level);

	void glFramebufferRenderbufferOES(@GLenum int target, @GLenum int attachment, @GLenum int renderbuffertarget, @GLuint int renderbuffer);

	@StripPostfix("params")
	void glGetFramebufferAttachmentParameterivOES(@GLenum int target, @GLenum int attachment, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	/** @deprecated Will be removed in 3.0. Use {@link #glGetFramebufferAttachmentParameteriOES} instead. */
	@Alternate("glGetFramebufferAttachmentParameterivOES")
	@GLreturn("params")
	@StripPostfix("params")
	@Reuse(value = "OESFramebufferObject", method = "glGetFramebufferAttachmentParameteriOES")
	@Deprecated
	void glGetFramebufferAttachmentParameterivOES2(@GLenum int target, @GLenum int attachment, @GLenum int pname, @OutParameter IntBuffer params);

	@Alternate("glGetFramebufferAttachmentParameterivOES")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetFramebufferAttachmentParameterivOES3(@GLenum int target, @GLenum int attachment, @GLenum int pname, @OutParameter IntBuffer params);

	void glGenerateMipmapOES(@GLenum int target);

}