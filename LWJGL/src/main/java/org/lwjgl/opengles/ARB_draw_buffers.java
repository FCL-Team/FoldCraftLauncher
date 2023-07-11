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
import org.lwjgl.util.generator.AutoSize;
import org.lwjgl.util.generator.Const;
import org.lwjgl.util.generator.Constant;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLsizei;

import java.nio.IntBuffer;

public interface ARB_draw_buffers {

	/** Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv: */
	int GL_MAX_DRAW_BUFFERS_ARB = 0x8824,
		GL_DRAW_BUFFER0_ARB     = 0x8825,
		GL_DRAW_BUFFER1_ARB     = 0x8826,
		GL_DRAW_BUFFER2_ARB     = 0x8827,
		GL_DRAW_BUFFER3_ARB     = 0x8828,
		GL_DRAW_BUFFER4_ARB     = 0x8829,
		GL_DRAW_BUFFER5_ARB     = 0x882A,
		GL_DRAW_BUFFER6_ARB     = 0x882B,
		GL_DRAW_BUFFER7_ARB     = 0x882C,
		GL_DRAW_BUFFER8_ARB     = 0x882D,
		GL_DRAW_BUFFER9_ARB     = 0x882E,
		GL_DRAW_BUFFER10_ARB    = 0x882F,
		GL_DRAW_BUFFER11_ARB    = 0x8830,
		GL_DRAW_BUFFER12_ARB    = 0x8831,
		GL_DRAW_BUFFER13_ARB    = 0x8832,
		GL_DRAW_BUFFER14_ARB    = 0x8833,
		GL_DRAW_BUFFER15_ARB    = 0x8834;

	void glDrawBuffersARB(@AutoSize("buffers") @GLsizei int size, @Const @GLenum IntBuffer buffers);

	@Alternate("glDrawBuffersARB")
	void glDrawBuffersARB(@Constant("1") @GLsizei int size, @Constant(value = "APIUtil.getInt(buffer)", keepParam = true) int buffer);

}