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

import org.lwjgl.util.generator.opengl.GLbitfield;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLint;

public interface EXT_framebuffer_blit {

	/**
	 Accepted by the &lt;target&gt; parameter of BindFramebufferEXT,
	 CheckFramebufferStatusEXT, FramebufferTexture{1D|2D|3D}EXT,
	 FramebufferRenderbufferEXT, and
	 GetFramebufferAttachmentParameterivEXT.
	 */
	int GL_READ_FRAMEBUFFER_EXT = 0x8CA8;
	int GL_DRAW_FRAMEBUFFER_EXT = 0x8CA9;

	/**
	 Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv, and GetDoublev.
	 */
	int GL_DRAW_FRAMEBUFFER_BINDING_EXT = 0x8CA6; // alias FRAMEBUFFER_BINDING_EXT
	int GL_READ_FRAMEBUFFER_BINDING_EXT = 0x8CAA;

	/**
	 Transfers a rectangle of pixel values from one
	 region of the read framebuffer to another in the draw framebuffer.
	 &lt;mask&gt; is the bitwise OR of a number of values indicating which
	 buffers are to be copied. The values are COLOR_BUFFER_BIT,
	 DEPTH_BUFFER_BIT, and STENCIL_BUFFER_BIT.
	 The pixels corresponding to these buffers are
	 copied from the source rectangle, bound by the locations (srcX0,
	 srcY0) and (srcX1, srcY1) inclusive, to the destination rectangle,
	 bound by the locations (dstX0, dstY0) and (dstX1, dstY1)
	 inclusive.
	 If the source and destination rectangle dimensions do not match,
	 the source image is stretched to fit the destination
	 rectangle. &lt;filter&gt; must be LINEAR or NEAREST and specifies the
	 method of interpolation to be applied if the image is
	 stretched.
	 */
	void glBlitFramebufferEXT(
			@GLint int srcX0, @GLint int srcY0, @GLint int srcX1, @GLint int srcY1,
	        @GLint int dstX0, @GLint int dstY0, @GLint int dstX1, @GLint int dstY1,
	        @GLbitfield int mask, @GLenum int filter);

}