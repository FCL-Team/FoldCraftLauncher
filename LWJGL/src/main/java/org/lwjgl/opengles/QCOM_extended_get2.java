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
import org.lwjgl.util.generator.Check;
import org.lwjgl.util.generator.OutParameter;
import org.lwjgl.util.generator.opengl.GLchar;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface QCOM_extended_get2 {

	void glExtGetShadersQCOM(@OutParameter @Check("1") @GLuint IntBuffer shaders,
	                         @AutoSize("shaders") int maxShaders,
	                         @OutParameter @Check("1") IntBuffer numShaders);

	void glExtGetProgramsQCOM(@OutParameter @Check("1") @GLuint IntBuffer programs,
	                          @AutoSize("programs") int maxPrograms,
	                          @OutParameter @Check("1") IntBuffer numPrograms);

	boolean glExtIsProgramBinaryQCOM(@GLuint int program);

	void glExtGetProgramBinarySourceQCOM(@GLuint int program, @GLenum int shadertype,
	                                     @OutParameter @Check @GLchar ByteBuffer source, @OutParameter @Check("1") IntBuffer length);

}