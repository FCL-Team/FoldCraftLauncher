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
import org.lwjgl.util.generator.opengl.GLchar;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.ByteBuffer;

public interface ARB_blend_func_extended {

	/**
	 * Accepted by the &lt;src&gt; and &lt;dst&gt; parameters of BlendFunc and
	 * BlendFunci, and by the &lt;srcRGB&gt;, &lt;dstRGB&gt;, &lt;srcAlpha&gt; and &lt;dstAlpha&gt;
	 * parameters of BlendFuncSeparate and BlendFuncSeparatei:
	 */
	int GL_SRC1_COLOR = 0x88F9;
	int GL_SRC1_ALPHA = GL15.GL_SRC1_ALPHA;
	int GL_ONE_MINUS_SRC1_COLOR = 0x88FA;
	int GL_ONE_MINUS_SRC1_ALPHA = 0x88FB;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv
	 * and GetDoublev:
	 */
	int GL_MAX_DUAL_SOURCE_DRAW_BUFFERS = 0x88FC;

	@Reuse("GL33")
	void glBindFragDataLocationIndexed(@GLuint int program, @GLuint int colorNumber, @GLuint int index, @NullTerminated @Const @GLchar ByteBuffer name);

	@Reuse("GL33")
	@Alternate("glBindFragDataLocationIndexed")
	void glBindFragDataLocationIndexed(@GLuint int program, @GLuint int colorNumber, @GLuint int index, @NullTerminated CharSequence name);

	@Reuse("GL33")
	int glGetFragDataIndex(@GLuint int program, @NullTerminated @Const @GLchar ByteBuffer name);

	@Reuse("GL33")
	@Alternate("glGetFragDataIndex")
	int glGetFragDataIndex(@GLuint int program, @NullTerminated CharSequence name);

}