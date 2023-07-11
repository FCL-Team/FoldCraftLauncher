/*
 * Copyright (c) 2002-2012 LWJGL Project
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
import org.lwjgl.util.generator.Const;
import org.lwjgl.util.generator.StripPostfix;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;
import org.lwjgl.util.generator.opengl.GLuint64;

import java.nio.LongBuffer;

public interface NV_bindless_texture {

	@GLuint64
	long glGetTextureHandleNV(@GLuint int texture);

	@GLuint64
	long glGetTextureSamplerHandleNV(@GLuint int texture, @GLuint int sampler);

	void glMakeTextureHandleResidentNV(@GLuint64 long handle);

	void glMakeTextureHandleNonResidentNV(@GLuint64 long handle);

	@GLuint64
	long glGetImageHandleNV(@GLuint int texture, int level, boolean layered,
	                        int layer, @GLenum int format);

	void glMakeImageHandleResidentNV(@GLuint64 long handle, @GLenum int access);

	void glMakeImageHandleNonResidentNV(@GLuint64 long handle);

	void glUniformHandleui64NV(int location, @GLuint64 long value);

	@StripPostfix("value")
	void glUniformHandleui64vNV(int location, @AutoSize("value") @GLsizei int count, @Const @GLuint64 LongBuffer value);

	void glProgramUniformHandleui64NV(@GLuint int program, int location,
	                                  @GLuint64 long value);

	@StripPostfix("values")
	void glProgramUniformHandleui64vNV(@GLuint int program, int location,
	                                   @AutoSize("values") @GLsizei int count, @Const @GLuint64 LongBuffer values);

	boolean glIsTextureHandleResidentNV(@GLuint64 long handle);

	boolean glIsImageHandleResidentNV(@GLuint64 long handle);

}