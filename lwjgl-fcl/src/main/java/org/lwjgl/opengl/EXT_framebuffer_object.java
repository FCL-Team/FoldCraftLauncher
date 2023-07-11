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
import org.lwjgl.util.generator.Alternate;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLreturn;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.IntBuffer;

public interface EXT_framebuffer_object {

	/**
	 * Accepted by the &lt;target&gt; parameter of BindFramebufferEXT,
	 * CheckFramebufferStatusEXT, FramebufferTexture{1D|2D|3D}EXT, and
	 * FramebufferRenderbufferEXT:
	 */
	int GL_FRAMEBUFFER_EXT = 0x8D40;

	/**
	 * Accepted by the &lt;target&gt; parameter of BindRenderbufferEXT,
	 * RenderbufferStorageEXT, and GetRenderbufferParameterivEXT, and
	 * returned by GetFramebufferAttachmentParameterivEXT:
	 */
	int GL_RENDERBUFFER_EXT = 0x8D41;

	/**
	 * Accepted by the &lt;internalformat&gt; parameter of
	 * RenderbufferStorageEXT:
	 */
	int GL_STENCIL_INDEX1_EXT = 0x8D46;
	int GL_STENCIL_INDEX4_EXT = 0x8D47;
	int GL_STENCIL_INDEX8_EXT = 0x8D48;
	int GL_STENCIL_INDEX16_EXT = 0x8D49;

	/** Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameterivEXT: */
	int GL_RENDERBUFFER_WIDTH_EXT = 0x8D42;
	int GL_RENDERBUFFER_HEIGHT_EXT = 0x8D43;
	int GL_RENDERBUFFER_INTERNAL_FORMAT_EXT = 0x8D44;
	int GL_RENDERBUFFER_RED_SIZE_EXT = 0x8D50;
	int GL_RENDERBUFFER_GREEN_SIZE_EXT = 0x8D51;
	int GL_RENDERBUFFER_BLUE_SIZE_EXT = 0x8D52;
	int GL_RENDERBUFFER_ALPHA_SIZE_EXT = 0x8D53;
	int GL_RENDERBUFFER_DEPTH_SIZE_EXT = 0x8D54;
	int GL_RENDERBUFFER_STENCIL_SIZE_EXT = 0x8D55;

	/**
	 * Accepted by the &lt;pname&gt; parameter of
	 * GetFramebufferAttachmentParameterivEXT:
	 */
	int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_EXT = 0x8CD0;
	int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_EXT = 0x8CD1;
	int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL_EXT = 0x8CD2;
	int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE_EXT = 0x8CD3;
	int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_3D_ZOFFSET_EXT = 0x8CD4;

	/**
	 * Accepted by the &lt;attachment&gt; parameter of
	 * FramebufferTexture{1D|2D|3D}EXT, FramebufferRenderbufferEXT, and
	 * GetFramebufferAttachmentParameterivEXT
	 */
	int GL_COLOR_ATTACHMENT0_EXT = 0x8CE0;
	int GL_COLOR_ATTACHMENT1_EXT = 0x8CE1;
	int GL_COLOR_ATTACHMENT2_EXT = 0x8CE2;
	int GL_COLOR_ATTACHMENT3_EXT = 0x8CE3;
	int GL_COLOR_ATTACHMENT4_EXT = 0x8CE4;
	int GL_COLOR_ATTACHMENT5_EXT = 0x8CE5;
	int GL_COLOR_ATTACHMENT6_EXT = 0x8CE6;
	int GL_COLOR_ATTACHMENT7_EXT = 0x8CE7;
	int GL_COLOR_ATTACHMENT8_EXT = 0x8CE8;
	int GL_COLOR_ATTACHMENT9_EXT = 0x8CE9;
	int GL_COLOR_ATTACHMENT10_EXT = 0x8CEA;
	int GL_COLOR_ATTACHMENT11_EXT = 0x8CEB;
	int GL_COLOR_ATTACHMENT12_EXT = 0x8CEC;
	int GL_COLOR_ATTACHMENT13_EXT = 0x8CED;
	int GL_COLOR_ATTACHMENT14_EXT = 0x8CEE;
	int GL_COLOR_ATTACHMENT15_EXT = 0x8CEF;
	int GL_DEPTH_ATTACHMENT_EXT = 0x8D00;
	int GL_STENCIL_ATTACHMENT_EXT = 0x8D20;

	/** Returned by CheckFramebufferStatusEXT(): */
	int GL_FRAMEBUFFER_COMPLETE_EXT = 0x8CD5;
	int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT = 0x8CD6;
	int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT = 0x8CD7;
	int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT = 0x8CD9;
	int GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT = 0x8CDA;
	int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT = 0x8CDB;
	int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT = 0x8CDC;
	int GL_FRAMEBUFFER_UNSUPPORTED_EXT = 0x8CDD;

	/** Accepted by GetIntegerv(): */
	int GL_FRAMEBUFFER_BINDING_EXT = 0x8CA6;
	int GL_RENDERBUFFER_BINDING_EXT = 0x8CA7;
	int GL_MAX_COLOR_ATTACHMENTS_EXT = 0x8CDF;
	int GL_MAX_RENDERBUFFER_SIZE_EXT = 0x84E8;

	/** Returned by GetError(): */
	int GL_INVALID_FRAMEBUFFER_OPERATION_EXT = 0x0506;

	boolean glIsRenderbufferEXT(@GLuint int renderbuffer);

	void glBindRenderbufferEXT(@GLenum int target, @GLuint int renderbuffer);

	void glDeleteRenderbuffersEXT(@AutoSize("renderbuffers") int n, @Const @GLuint IntBuffer renderbuffers);

	@Alternate("glDeleteRenderbuffersEXT")
	void glDeleteRenderbuffersEXT(@Constant("1") int n, @Constant(value = "APIUtil.getInt(caps, renderbuffer)", keepParam = true) int renderbuffer);

	void glGenRenderbuffersEXT(@AutoSize("renderbuffers") int n, @OutParameter @GLuint IntBuffer renderbuffers);

	@Alternate("glGenRenderbuffersEXT")
	@GLreturn("renderbuffers")
	void glGenRenderbuffersEXT2(@Constant("1") int n, @OutParameter @GLuint IntBuffer renderbuffers);

	void glRenderbufferStorageEXT(@GLenum int target, @GLenum int internalformat, @GLsizei int width, @GLsizei int height);

	@StripPostfix("params")
	void glGetRenderbufferParameterivEXT(@GLenum int target, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	/** @deprecated Will be removed in 3.0. Use {@link #glGetRenderbufferParameteriEXT} instead. */
	@Alternate("glGetRenderbufferParameterivEXT")
	@GLreturn("params")
	@StripPostfix("params")
	@Reuse(value = "EXTFramebufferObject", method = "glGetRenderbufferParameteriEXT")
	@Deprecated
	void glGetRenderbufferParameterivEXT2(@GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	@Alternate("glGetRenderbufferParameterivEXT")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetRenderbufferParameterivEXT3(@GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	boolean glIsFramebufferEXT(@GLuint int framebuffer);

	void glBindFramebufferEXT(@GLenum int target, @GLuint int framebuffer);

	void glDeleteFramebuffersEXT(@AutoSize("framebuffers") int n, @Const @GLuint IntBuffer framebuffers);

	@Alternate("glDeleteFramebuffersEXT")
	void glDeleteFramebuffersEXT(@Constant("1") int n, @Constant(value = "APIUtil.getInt(caps, framebuffer)", keepParam = true) int framebuffer);

	void glGenFramebuffersEXT(@AutoSize("framebuffers") int n, @OutParameter @GLuint IntBuffer framebuffers);

	@Alternate("glGenFramebuffersEXT")
	@GLreturn("framebuffers")
	void glGenFramebuffersEXT2(@Constant("1") int n, @OutParameter @GLuint IntBuffer framebuffers);

	@GLenum
	int glCheckFramebufferStatusEXT(@GLenum int target);

	void glFramebufferTexture1DEXT(@GLenum int target, @GLenum int attachment, @GLenum int textarget, @GLuint int texture, int level);

	void glFramebufferTexture2DEXT(@GLenum int target, @GLenum int attachment, @GLenum int textarget, @GLuint int texture, int level);

	void glFramebufferTexture3DEXT(@GLenum int target, @GLenum int attachment, @GLenum int textarget, @GLuint int texture, int level, int zoffset);

	void glFramebufferRenderbufferEXT(@GLenum int target, @GLenum int attachment, @GLenum int renderbuffertarget, @GLuint int renderbuffer);

	@StripPostfix("params")
	void glGetFramebufferAttachmentParameterivEXT(@GLenum int target, @GLenum int attachment, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	/** @deprecated Will be removed in 3.0. Use {@link #glGetFramebufferAttachmentParameteriEXT} instead. */
	@Alternate("glGetFramebufferAttachmentParameterivEXT")
	@GLreturn("params")
	@StripPostfix("params")
	@Reuse(value = "EXTFramebufferObject", method = "glGetFramebufferAttachmentParameteriEXT")
	@Deprecated
	void glGetFramebufferAttachmentParameterivEXT2(@GLenum int target, @GLenum int attachment, @GLenum int pname, @OutParameter IntBuffer params);

	@Alternate("glGetFramebufferAttachmentParameterivEXT")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetFramebufferAttachmentParameterivEXT3(@GLenum int target, @GLenum int attachment, @GLenum int pname, @OutParameter IntBuffer params);

	void glGenerateMipmapEXT(@GLenum int target);
}
