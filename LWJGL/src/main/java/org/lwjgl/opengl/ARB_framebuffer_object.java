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
import org.lwjgl.util.generator.opengl.*;

import java.nio.IntBuffer;

@Extension(postfix = "")
public interface ARB_framebuffer_object {

	/**
	 * Accepted by the &lt;target&gt; parameter of BindFramebuffer,
	 * CheckFramebufferStatus, FramebufferTexture{1D|2D|3D},
	 * FramebufferRenderbuffer, and
	 * GetFramebufferAttachmentParameteriv:
	 */
	int GL_FRAMEBUFFER = 0x8D40;
	int GL_READ_FRAMEBUFFER = 0x8CA8;
	int GL_DRAW_FRAMEBUFFER = 0x8CA9;

	/**
	 * Accepted by the &lt;target&gt; parameter of BindRenderbuffer,
	 * RenderbufferStorage, and GetRenderbufferParameteriv, and
	 * returned by GetFramebufferAttachmentParameteriv:
	 */
	int GL_RENDERBUFFER = 0x8D41;

	/**
	 * Accepted by the &lt;internalformat&gt; parameter of
	 * RenderbufferStorage:
	 */
	int GL_STENCIL_INDEX1 = 0x8D46;
	int GL_STENCIL_INDEX4 = 0x8D47;
	int GL_STENCIL_INDEX8 = 0x8D48;
	int GL_STENCIL_INDEX16 = 0x8D49;

	/** Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameteriv: */
	int GL_RENDERBUFFER_WIDTH = 0x8D42;
	int GL_RENDERBUFFER_HEIGHT = 0x8D43;
	int GL_RENDERBUFFER_INTERNAL_FORMAT = 0x8D44;
	int GL_RENDERBUFFER_RED_SIZE = 0x8D50;
	int GL_RENDERBUFFER_GREEN_SIZE = 0x8D51;
	int GL_RENDERBUFFER_BLUE_SIZE = 0x8D52;
	int GL_RENDERBUFFER_ALPHA_SIZE = 0x8D53;
	int GL_RENDERBUFFER_DEPTH_SIZE = 0x8D54;
	int GL_RENDERBUFFER_STENCIL_SIZE = 0x8D55;
	int GL_RENDERBUFFER_SAMPLES = 0x8CAB;

	/**
	 * Accepted by the &lt;pname&gt; parameter of
	 * GetFramebufferAttachmentParameteriv:
	 */
	int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0x8CD0;
	int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0x8CD1;
	int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0x8CD2;
	int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0x8CD3;
	int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 0x8CD4;
	int GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING = 0x8210;
	int GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE = 0x8211;
	int GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE = 0x8212;
	int GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE = 0x8213;
	int GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE = 0x8214;
	int GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE = 0x8215;
	int GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE = 0x8216;
	int GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE = 0x8217;

	/** Returned in &lt;params&gt; by GetFramebufferAttachmentParameteriv: */
	int GL_SRGB = 0x8C40;
	int GL_UNSIGNED_NORMALIZED = 0x8C17;
	int GL_FRAMEBUFFER_DEFAULT = 0x8218;
	int GL_INDEX = 0x8222;

	/**
	 * Accepted by the &lt;attachment&gt; parameter of
	 * FramebufferTexture{1D|2D|3D}, FramebufferRenderbuffer, and
	 * GetFramebufferAttachmentParameteriv
	 */
	int GL_COLOR_ATTACHMENT0 = 0x8CE0;
	int GL_COLOR_ATTACHMENT1 = 0x8CE1;
	int GL_COLOR_ATTACHMENT2 = 0x8CE2;
	int GL_COLOR_ATTACHMENT3 = 0x8CE3;
	int GL_COLOR_ATTACHMENT4 = 0x8CE4;
	int GL_COLOR_ATTACHMENT5 = 0x8CE5;
	int GL_COLOR_ATTACHMENT6 = 0x8CE6;
	int GL_COLOR_ATTACHMENT7 = 0x8CE7;
	int GL_COLOR_ATTACHMENT8 = 0x8CE8;
	int GL_COLOR_ATTACHMENT9 = 0x8CE9;
	int GL_COLOR_ATTACHMENT10 = 0x8CEA;
	int GL_COLOR_ATTACHMENT11 = 0x8CEB;
	int GL_COLOR_ATTACHMENT12 = 0x8CEC;
	int GL_COLOR_ATTACHMENT13 = 0x8CED;
	int GL_COLOR_ATTACHMENT14 = 0x8CEE;
	int GL_COLOR_ATTACHMENT15 = 0x8CEF;
	int GL_DEPTH_ATTACHMENT = 0x8D00;
	int GL_STENCIL_ATTACHMENT = 0x8D20;
	int GL_DEPTH_STENCIL_ATTACHMENT = 0x821A;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	int GL_MAX_SAMPLES = 0x8D57;

	/** Returned by CheckFramebufferStatus(): */
	int GL_FRAMEBUFFER_COMPLETE = 0x8CD5;
	int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8CD6;
	int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8CD7;
	int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 0x8CDB;
	int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 0x8CDC;
	int GL_FRAMEBUFFER_UNSUPPORTED = 0x8CDD;
	int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 0x8D56;
	int GL_FRAMEBUFFER_UNDEFINED = 0x8219;

	/**
	 * Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv,
	 * and GetDoublev:
	 */
	int GL_FRAMEBUFFER_BINDING = 0x8CA6; // alias DRAW_FRAMEBUFFER_BINDING
	int GL_DRAW_FRAMEBUFFER_BINDING = 0x8CA6;
	int GL_READ_FRAMEBUFFER_BINDING = 0x8CAA;
	int GL_RENDERBUFFER_BINDING = 0x8CA7;
	int GL_MAX_COLOR_ATTACHMENTS = 0x8CDF;
	int GL_MAX_RENDERBUFFER_SIZE = 0x84E8;

	/** Returned by GetError(): */
	int GL_INVALID_FRAMEBUFFER_OPERATION = 0x0506;

	/**
	 * Accepted by the &lt;format&gt; parameter of DrawPixels, ReadPixels,
	 * TexImage1D, TexImage2D, TexImage3D, TexSubImage1D, TexSubImage2D,
	 * TexSubImage3D, and GetTexImage, by the &lt;type&gt; parameter of
	 * CopyPixels, by the &lt;internalformat&gt; parameter of TexImage1D,
	 * TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 * RenderbufferStorage, and returned in the &lt;data&gt; parameter of
	 * GetTexLevelParameter and GetRenderbufferParameteriv:
	 */
	int GL_DEPTH_STENCIL = 0x84F9;

	/**
	 * Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels,
	 * TexImage1D, TexImage2D, TexImage3D, TexSubImage1D, TexSubImage2D,
	 * TexSubImage3D, and GetTexImage:
	 */
	int GL_UNSIGNED_INT_24_8 = 0x84FA;

	/**
	 * Accepted by the &lt;internalformat&gt; parameter of TexImage1D,
	 * TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 * RenderbufferStorage, and returned in the &lt;data&gt; parameter of
	 * GetTexLevelParameter and GetRenderbufferParameteriv:
	 */
	int GL_DEPTH24_STENCIL8 = 0x88F0;

	/** Accepted by the &lt;value&gt; parameter of GetTexLevelParameter: */
	int GL_TEXTURE_STENCIL_SIZE = 0x88F1;

	@Reuse("GL30")
	boolean glIsRenderbuffer(@GLuint int renderbuffer);

	@Reuse("GL30")
	void glBindRenderbuffer(@GLenum int target, @GLuint int renderbuffer);

	@Reuse("GL30")
	void glDeleteRenderbuffers(@AutoSize("renderbuffers") @GLsizei int n, @Const @GLuint IntBuffer renderbuffers);

	@Reuse("GL30")
	@Alternate("glDeleteRenderbuffers")
	void glDeleteRenderbuffers(@Constant("1") @GLsizei int n, @Constant(value = "APIUtil.getInt(caps, renderbuffer)", keepParam = true) int renderbuffer);

	@Reuse("GL30")
	void glGenRenderbuffers(@AutoSize("renderbuffers") @GLsizei int n, @OutParameter @GLuint IntBuffer renderbuffers);

	@Reuse("GL30")
	@Alternate("glGenRenderbuffers")
	@GLreturn("renderbuffers")
	void glGenRenderbuffers2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer renderbuffers);

	@Reuse("GL30")
	void glRenderbufferStorage(@GLenum int target, @GLenum int internalformat,
	                           @GLsizei int width, @GLsizei int height);

	@Reuse("GL30")
	void glRenderbufferStorageMultisample(@GLenum int target, @GLsizei int samples,
	                                      @GLenum int internalformat,
	                                      @GLsizei int width, @GLsizei int height);

	@Reuse("GL30")
	@StripPostfix("params")
	void glGetRenderbufferParameteriv(@GLenum int target, @GLenum int pname, @Check("4") @OutParameter IntBuffer params);

	/** @deprecated Will be removed in 3.0. Use {@link #glGetRenderbufferParameteri} instead. */
	@Alternate("glGetRenderbufferParameteriv")
	@GLreturn("params")
	@StripPostfix("params")
	@Reuse(value = "ARBFramebufferObject", method = "glGetRenderbufferParameteri")
	@Deprecated
	void glGetRenderbufferParameteriv2(@GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	@Reuse("GL30")
	@Alternate("glGetRenderbufferParameteriv")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetRenderbufferParameteriv3(@GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	@Reuse("GL30")
	boolean glIsFramebuffer(@GLuint int framebuffer);

	@Reuse("GL30")
	void glBindFramebuffer(@GLenum int target, @GLuint int framebuffer);

	@Reuse("GL30")
	void glDeleteFramebuffers(@AutoSize("framebuffers") @GLsizei int n, @Const @GLuint IntBuffer framebuffers);

	@Reuse("GL30")
	@Alternate("glDeleteFramebuffers")
	void glDeleteFramebuffers(@Constant("1") @GLsizei int n, @Constant(value = "APIUtil.getInt(caps, framebuffer)", keepParam = true) int framebuffer);

	@Reuse("GL30")
	void glGenFramebuffers(@AutoSize("framebuffers") @GLsizei int n, @OutParameter @GLuint IntBuffer framebuffers);

	@Reuse("GL30")
	@Alternate("glGenFramebuffers")
	@GLreturn("framebuffers")
	void glGenFramebuffers2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer framebuffers);

	@Reuse("GL30")
	@GLenum
	int glCheckFramebufferStatus(@GLenum int target);

	@Reuse("GL30")
	void glFramebufferTexture1D(@GLenum int target, @GLenum int attachment,
	                            @GLenum int textarget, @GLuint int texture, int level);

	@Reuse("GL30")
	void glFramebufferTexture2D(@GLenum int target, @GLenum int attachment,
	                            @GLenum int textarget, @GLuint int texture, int level);

	@Reuse("GL30")
	void glFramebufferTexture3D(@GLenum int target, @GLenum int attachment,
	                            @GLenum int textarget, @GLuint int texture,
	                            int level, int layer);

	@Reuse("GL30")
	void glFramebufferTextureLayer(@GLenum int target, @GLenum int attachment,
	                               @GLuint int texture, int level, int layer);

	@Reuse("GL30")
	void glFramebufferRenderbuffer(@GLenum int target, @GLenum int attachment,
	                               @GLenum int renderbuffertarget, @GLuint int renderbuffer);

	@Reuse("GL30")
	@StripPostfix("params")
	void glGetFramebufferAttachmentParameteriv(@GLenum int target, @GLenum int attachment,
	                                           @GLenum int pname, @Check("4") @OutParameter IntBuffer params);

	/** @deprecated Will be removed in 3.0. Use {@link #glGetFramebufferAttachmentParameteri} instead. */
	@Alternate("glGetFramebufferAttachmentParameteriv")
	@GLreturn("params")
	@StripPostfix("params")
	@Reuse(value = "GL30", method = "glGetFramebufferAttachmentParameteri")
	@Deprecated
	void glGetFramebufferAttachmentParameteriv2(@GLenum int target, @GLenum int attachment,
	                                            @GLenum int pname, @OutParameter IntBuffer params);

	@Reuse("GL30")
	@Alternate("glGetFramebufferAttachmentParameteriv")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetFramebufferAttachmentParameteriv3(@GLenum int target, @GLenum int attachment,
	                                            @GLenum int pname, @OutParameter IntBuffer params);

	@Reuse("GL30")
	void glBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1,
	                       int dstX0, int dstY0, int dstX1, int dstY1,
	                       @GLbitfield int mask, @GLenum int filter);

	@Reuse("GL30")
	void glGenerateMipmap(@GLenum int target);

}