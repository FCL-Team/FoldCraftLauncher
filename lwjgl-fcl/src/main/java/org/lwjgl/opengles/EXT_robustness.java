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

import org.lwjgl.util.generator.AutoSize;
import org.lwjgl.util.generator.OutParameter;
import org.lwjgl.util.generator.StripPostfix;
import org.lwjgl.util.generator.opengl.*;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public interface EXT_robustness {

	/** Returned by GetGraphicsResetStatusEXT: */
	int GL_NO_ERROR                   = 0x0000,
		GL_GUILTY_CONTEXT_RESET_EXT   = 0x8253,
		GL_INNOCENT_CONTEXT_RESET_EXT = 0x8254,
		GL_UNKNOWN_CONTEXT_RESET_EXT  = 0x8255;

	/**
	 * Accepted by the &lt;value&gt; parameter of GetBooleanv, GetIntegerv,
	 * and GetFloatv:
	 */
	int GL_CONTEXT_ROBUST_ACCESS_EXT       = 0x90F3,
		GL_RESET_NOTIFICATION_STRATEGY_EXT = 0x8256;

	/**
	 * Returned by GetIntegerv and related simple queries when &lt;value&gt; is
	 * RESET_NOTIFICATION_STRATEGY_EXT :
	 */
	int GL_LOSE_CONTEXT_ON_RESET_EXT = 0x8252,
		GL_NO_RESET_NOTIFICATION_EXT = 0x8261;

	@GLenum
	int glGetGraphicsResetStatusEXT();

	void glReadnPixelsEXT(int x, int y, @GLsizei int width, @GLsizei int height,
	                      @GLenum int format, @GLenum int type, @AutoSize("data") @GLsizei int bufSize,
	                      @OutParameter @GLbyte @GLshort @GLint @GLfloat Buffer data);

	@StripPostfix("params")
	void glGetnUniformfvEXT(@GLuint int program, int location, @AutoSize("params") @GLsizei int bufSize, @OutParameter FloatBuffer params);

	@StripPostfix("params")
	void glGetnUniformivEXT(@GLuint int program, int location, @AutoSize("params") @GLsizei int bufSize, @OutParameter IntBuffer params);

}