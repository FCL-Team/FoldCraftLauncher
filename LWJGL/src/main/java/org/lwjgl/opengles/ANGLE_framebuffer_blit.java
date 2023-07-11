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

import org.lwjgl.util.generator.opengl.GLbitfield;
import org.lwjgl.util.generator.opengl.GLenum;

public interface ANGLE_framebuffer_blit {

	/**
	 * Accepted by the &lt;target&gt; parameter of BindFramebuffer,
	 * CheckFramebufferStatus, FramebufferTexture2D, FramebufferTexture3DOES,
	 * FramebufferRenderbuffer, and
	 * GetFramebufferAttachmentParameteriv:
	 */
	int GL_READ_FRAMEBUFFER_ANGLE = 0x8CA8,
		GL_DRAW_FRAMEBUFFER_ANGLE = 0x8CA9;

	/** Accepted by the &lt;pname&gt; parameters of GetIntegerv and GetFloatv: */
	int GL_DRAW_FRAMEBUFFER_BINDING_ANGLE = 0x8CA6, // alias FRAMEBUFFER_BINDING
		GL_READ_FRAMEBUFFER_BINDING_ANGLE = 0x8CAA;

	void glBlitFramebufferANGLE(int srcX0, int srcY0, int srcX1, int srcY1,
	                            int dstX0, int dstY0, int dstX1, int dstY1,
	                            @GLbitfield int mask, @GLenum int filter);

}