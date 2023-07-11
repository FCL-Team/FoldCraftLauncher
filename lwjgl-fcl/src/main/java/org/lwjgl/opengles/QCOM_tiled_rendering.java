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
import org.lwjgl.util.generator.opengl.GLuint;

public interface QCOM_tiled_rendering {

	/**
	 * Accepted by the &lt;preserveMask&gt; parameter of StartTilingQCOM and
	 * EndTilingQCOM
	 */
	int GL_COLOR_BUFFER_BIT0_QCOM       = 0x00000001,
		GL_COLOR_BUFFER_BIT1_QCOM       = 0x00000002,
		GL_COLOR_BUFFER_BIT2_QCOM       = 0x00000004,
		GL_COLOR_BUFFER_BIT3_QCOM       = 0x00000008,
		GL_COLOR_BUFFER_BIT4_QCOM       = 0x00000010,
		GL_COLOR_BUFFER_BIT5_QCOM       = 0x00000020,
		GL_COLOR_BUFFER_BIT6_QCOM       = 0x00000040,
		GL_COLOR_BUFFER_BIT7_QCOM       = 0x00000080,
		GL_DEPTH_BUFFER_BIT0_QCOM       = 0x00000100,
		GL_DEPTH_BUFFER_BIT1_QCOM       = 0x00000200,
		GL_DEPTH_BUFFER_BIT2_QCOM       = 0x00000400,
		GL_DEPTH_BUFFER_BIT3_QCOM       = 0x00000800,
		GL_DEPTH_BUFFER_BIT4_QCOM       = 0x00001000,
		GL_DEPTH_BUFFER_BIT5_QCOM       = 0x00002000,
		GL_DEPTH_BUFFER_BIT6_QCOM       = 0x00004000,
		GL_DEPTH_BUFFER_BIT7_QCOM       = 0x00008000,
		GL_STENCIL_BUFFER_BIT0_QCOM     = 0x00010000,
		GL_STENCIL_BUFFER_BIT1_QCOM     = 0x00020000,
		GL_STENCIL_BUFFER_BIT2_QCOM     = 0x00040000,
		GL_STENCIL_BUFFER_BIT3_QCOM     = 0x00080000,
		GL_STENCIL_BUFFER_BIT4_QCOM     = 0x00100000,
		GL_STENCIL_BUFFER_BIT5_QCOM     = 0x00200000,
		GL_STENCIL_BUFFER_BIT6_QCOM     = 0x00400000,
		GL_STENCIL_BUFFER_BIT7_QCOM     = 0x00800000,
		GL_MULTISAMPLE_BUFFER_BIT0_QCOM = 0x01000000,
		GL_MULTISAMPLE_BUFFER_BIT1_QCOM = 0x02000000,
		GL_MULTISAMPLE_BUFFER_BIT2_QCOM = 0x04000000,
		GL_MULTISAMPLE_BUFFER_BIT3_QCOM = 0x08000000,
		GL_MULTISAMPLE_BUFFER_BIT4_QCOM = 0x10000000,
		GL_MULTISAMPLE_BUFFER_BIT5_QCOM = 0x20000000,
		GL_MULTISAMPLE_BUFFER_BIT6_QCOM = 0x40000000,
		GL_MULTISAMPLE_BUFFER_BIT7_QCOM = 0x80000000;

	void glStartTilingQCOM(@GLuint int x, @GLuint int y, @GLuint int width, @GLuint int height,
	                       @GLbitfield int preserveMask);

	void glEndTilingQCOM(@GLbitfield int preserveMask);

}