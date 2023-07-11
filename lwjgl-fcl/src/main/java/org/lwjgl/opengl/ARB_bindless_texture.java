/*
 * Copyright (c) 2002-2013 LWJGL Project
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

import org.lwjgl.util.generator.AutoSize;
import org.lwjgl.util.generator.Check;
import org.lwjgl.util.generator.Const;
import org.lwjgl.util.generator.StripPostfix;
import org.lwjgl.util.generator.opengl.*;

import java.nio.LongBuffer;

public interface ARB_bindless_texture {

	/** Accepted by the &lt;type&gt; parameter of VertexAttribLPointer: */
	int GL_UNSIGNED_INT64_ARB = 0x140F;

	@GLuint64
	long glGetTextureHandleARB(@GLuint int texture);

	@GLuint64
	long glGetTextureSamplerHandleARB(@GLuint int texture, @GLuint int sampler);

	void glMakeTextureHandleResidentARB(@GLuint64 long handle);

	void glMakeTextureHandleNonResidentARB(@GLuint64 long handle);

	@GLuint64
	long glGetImageHandleARB(@GLuint int texture, int level, boolean layered, int layer, @GLenum int format);

	void glMakeImageHandleResidentARB(@GLuint64 long handle, @GLenum int access);

	void glMakeImageHandleNonResidentARB(@GLuint64 long handle);

	void glUniformHandleui64ARB(int location, @GLuint64 long value);

	@StripPostfix("value")
	void glUniformHandleui64vARB(int location, @AutoSize("value") @GLsizei int count, @Const @GLuint64 LongBuffer value);

	void glProgramUniformHandleui64ARB(@GLuint int program, int location, @GLuint64 long value);

	@StripPostfix("values")
	void glProgramUniformHandleui64vARB(@GLuint int program, int location, @AutoSize("values") @GLsizei int count, @Const @GLuint64 LongBuffer values);

	boolean glIsTextureHandleResidentARB(@GLuint64 long handle);

	boolean glIsImageHandleResidentARB(@GLuint64 long handle);

	void glVertexAttribL1ui64ARB(@GLuint int index, @GLuint64EXT long x);

	@StripPostfix("v")
	void glVertexAttribL1ui64vARB(@GLuint int index, @Check("1") @Const @GLuint64EXT LongBuffer v);

	@StripPostfix("params")
	void glGetVertexAttribLui64vARB(@GLuint int index, @GLenum int pname, @Check("4") @GLuint64EXT LongBuffer params);

}