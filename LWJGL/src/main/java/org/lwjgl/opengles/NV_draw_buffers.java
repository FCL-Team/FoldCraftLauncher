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

public interface NV_draw_buffers {

	/**
	 * Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv,
	 * and GetDoublev:
	 */
	int GL_MAX_DRAW_BUFFERS_NV = 0x8824,
		GL_DRAW_BUFFER0_NV     = 0x8825,
		GL_DRAW_BUFFER1_NV     = 0x8826,
		GL_DRAW_BUFFER2_NV     = 0x8827,
		GL_DRAW_BUFFER3_NV     = 0x8828,
		GL_DRAW_BUFFER4_NV     = 0x8829,
		GL_DRAW_BUFFER5_NV     = 0x882A,
		GL_DRAW_BUFFER6_NV     = 0x882B,
		GL_DRAW_BUFFER7_NV     = 0x882C,
		GL_DRAW_BUFFER8_NV     = 0x882D,
		GL_DRAW_BUFFER9_NV     = 0x882E,
		GL_DRAW_BUFFER10_NV    = 0x882F,
		GL_DRAW_BUFFER11_NV    = 0x8830,
		GL_DRAW_BUFFER12_NV    = 0x8831,
		GL_DRAW_BUFFER13_NV    = 0x8832,
		GL_DRAW_BUFFER14_NV    = 0x8833,
		GL_DRAW_BUFFER15_NV    = 0x8834;

	/** Accepted by the &lt;bufs&gt; parameter of DrawBuffersNV: */
	int GL_COLOR_ATTACHMENT0_NV  = 0x8CE0,
		GL_COLOR_ATTACHMENT1_NV  = 0x8CE1,
		GL_COLOR_ATTACHMENT2_NV  = 0x8CE2,
		GL_COLOR_ATTACHMENT3_NV  = 0x8CE3,
		GL_COLOR_ATTACHMENT4_NV  = 0x8CE4,
		GL_COLOR_ATTACHMENT5_NV  = 0x8CE5,
		GL_COLOR_ATTACHMENT6_NV  = 0x8CE6,
		GL_COLOR_ATTACHMENT7_NV  = 0x8CE7,
		GL_COLOR_ATTACHMENT8_NV  = 0x8CE8,
		GL_COLOR_ATTACHMENT9_NV  = 0x8CE9,
		GL_COLOR_ATTACHMENT10_NV = 0x8CEA,
		GL_COLOR_ATTACHMENT11_NV = 0x8CEB,
		GL_COLOR_ATTACHMENT12_NV = 0x8CEC,
		GL_COLOR_ATTACHMENT13_NV = 0x8CED,
		GL_COLOR_ATTACHMENT14_NV = 0x8CEE,
		GL_COLOR_ATTACHMENT15_NV = 0x8CEF;

	void glDrawBuffersNV(@AutoSize("bufs") @GLsizei int n, @Const @GLenum IntBuffer bufs);

	@Alternate("glDrawBuffersNV")
	void glDrawBuffersNV(@Constant("1") @GLsizei int n, @Constant(value = "APIUtil.getInt(buf)", keepParam = true) int buf);

}