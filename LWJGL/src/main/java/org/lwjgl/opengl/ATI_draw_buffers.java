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
import org.lwjgl.util.generator.opengl.GLsizei;

import java.nio.*;

public interface ATI_draw_buffers {

	/**
	 * Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv,
	 * and GetDoublev:
	 */
	int GL_MAX_DRAW_BUFFERS_ATI = 0x8824;
	int GL_DRAW_BUFFER0_ATI = 0x8825;
	int GL_DRAW_BUFFER1_ATI = 0x8826;
	int GL_DRAW_BUFFER2_ATI = 0x8827;
	int GL_DRAW_BUFFER3_ATI = 0x8828;
	int GL_DRAW_BUFFER4_ATI = 0x8829;
	int GL_DRAW_BUFFER5_ATI = 0x882A;
	int GL_DRAW_BUFFER6_ATI = 0x882B;
	int GL_DRAW_BUFFER7_ATI = 0x882C;
	int GL_DRAW_BUFFER8_ATI = 0x882D;
	int GL_DRAW_BUFFER9_ATI = 0x882E;
	int GL_DRAW_BUFFER10_ATI = 0x882F;
	int GL_DRAW_BUFFER11_ATI = 0x8830;
	int GL_DRAW_BUFFER12_ATI = 0x8831;
	int GL_DRAW_BUFFER13_ATI = 0x8832;
	int GL_DRAW_BUFFER14_ATI = 0x8833;
	int GL_DRAW_BUFFER15_ATI = 0x8834;

	void glDrawBuffersATI(@AutoSize("buffers") @GLsizei int size, @Const @GLenum IntBuffer buffers);

	@Alternate("glDrawBuffersATI")
	void glDrawBuffersATI(@Constant("1") @GLsizei int size, @Constant(value = "APIUtil.getInt(caps, buffer)", keepParam = true) int buffer);
}
