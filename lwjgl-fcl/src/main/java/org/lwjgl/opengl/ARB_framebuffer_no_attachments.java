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

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLreturn;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.IntBuffer;

@Dependent
@Extension(postfix = "")
public interface ARB_framebuffer_no_attachments {

	/**
	 * Accepted by the &lt;pname&gt; parameter of FramebufferParameteri,
	 * GetFramebufferParameteriv, NamedFramebufferParameteriEXT, and
	 * GetNamedFramebufferParameterivEXT:
	 */
	int GL_FRAMEBUFFER_DEFAULT_WIDTH                  = 0x9310,
		GL_FRAMEBUFFER_DEFAULT_HEIGHT                 = 0x9311,
		GL_FRAMEBUFFER_DEFAULT_LAYERS                 = 0x9312,
		GL_FRAMEBUFFER_DEFAULT_SAMPLES                = 0x9313,
		GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS = 0x9314;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetBooleanv,
	 * GetInteger64v, GetFloatv, and GetDoublev:
	 */
	int GL_MAX_FRAMEBUFFER_WIDTH   = 0x9315,
		GL_MAX_FRAMEBUFFER_HEIGHT  = 0x9316,
		GL_MAX_FRAMEBUFFER_LAYERS  = 0x9317,
		GL_MAX_FRAMEBUFFER_SAMPLES = 0x9318;

	@Reuse("GL43")
	void glFramebufferParameteri(@GLenum int target, @GLenum int pname, int param);

	@Reuse("GL43")
	@StripPostfix("params")
	void glGetFramebufferParameteriv(@GLenum int target, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Reuse("GL43")
	@Alternate("glGetFramebufferParameteriv")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetFramebufferParameteriv2(@GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	@Dependent("GL_EXT_direct_state_access")
	void glNamedFramebufferParameteriEXT(@GLuint int framebuffer, @GLenum int pname,
	                                     int param);

	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value = "params", extension = "EXT")
	void glGetNamedFramebufferParameterivEXT(@GLuint int framebuffer, @GLenum int pname,
	                                         @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetNamedFramebufferParameterivEXT")
	@GLreturn("params")
	@Dependent("GL_EXT_direct_state_access")
	@StripPostfix(value = "params", extension = "EXT")
	void glGetNamedFramebufferParameterivEXT2(@GLuint int framebuffer, @GLenum int pname,
	                                          @OutParameter IntBuffer params);

}