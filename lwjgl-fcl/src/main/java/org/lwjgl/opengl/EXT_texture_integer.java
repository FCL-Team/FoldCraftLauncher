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
import org.lwjgl.util.generator.opengl.GLreturn;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.IntBuffer;

public interface EXT_texture_integer {

	/**
	 * Accepted by the &lt;pname&gt; parameters of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	int GL_RGBA_INTEGER_MODE_EXT = 0x8D9E;

	/**
	 * Accepted by the &lt;internalFormat&gt; parameter of TexImage1D,
	 * TexImage2D, and TexImage3D:
	 */
	int GL_RGBA32UI_EXT = 0x8D70;
	int GL_RGB32UI_EXT = 0x8D71;
	int GL_ALPHA32UI_EXT = 0x8D72;
	int GL_INTENSITY32UI_EXT = 0x8D73;
	int GL_LUMINANCE32UI_EXT = 0x8D74;
	int GL_LUMINANCE_ALPHA32UI_EXT = 0x8D75;

	int GL_RGBA16UI_EXT = 0x8D76;
	int GL_RGB16UI_EXT = 0x8D77;
	int GL_ALPHA16UI_EXT = 0x8D78;
	int GL_INTENSITY16UI_EXT = 0x8D79;
	int GL_LUMINANCE16UI_EXT = 0x8D7A;
	int GL_LUMINANCE_ALPHA16UI_EXT = 0x8D7B;

	int GL_RGBA8UI_EXT = 0x8D7C;
	int GL_RGB8UI_EXT = 0x8D7D;
	int GL_ALPHA8UI_EXT = 0x8D7E;
	int GL_INTENSITY8UI_EXT = 0x8D7F;
	int GL_LUMINANCE8UI_EXT = 0x8D80;
	int GL_LUMINANCE_ALPHA8UI_EXT = 0x8D81;

	int GL_RGBA32I_EXT = 0x8D82;
	int GL_RGB32I_EXT = 0x8D83;
	int GL_ALPHA32I_EXT = 0x8D84;
	int GL_INTENSITY32I_EXT = 0x8D85;
	int GL_LUMINANCE32I_EXT = 0x8D86;
	int GL_LUMINANCE_ALPHA32I_EXT = 0x8D87;

	int GL_RGBA16I_EXT = 0x8D88;
	int GL_RGB16I_EXT = 0x8D89;
	int GL_ALPHA16I_EXT = 0x8D8A;
	int GL_INTENSITY16I_EXT = 0x8D8B;
	int GL_LUMINANCE16I_EXT = 0x8D8C;
	int GL_LUMINANCE_ALPHA16I_EXT = 0x8D8D;

	int GL_RGBA8I_EXT = 0x8D8E;
	int GL_RGB8I_EXT = 0x8D8F;
	int GL_ALPHA8I_EXT = 0x8D90;
	int GL_INTENSITY8I_EXT = 0x8D91;
	int GL_LUMINANCE8I_EXT = 0x8D92;
	int GL_LUMINANCE_ALPHA8I_EXT = 0x8D93;

	/**
	 * Accepted by the &lt;format&gt; parameter of TexImage1D, TexImage2D,
	 * TexImage3D, TexSubImage1D, TexSubImage2D, TexSubImage3D,
	 * DrawPixels and ReadPixels:
	 */
	int GL_RED_INTEGER_EXT = 0x8D94;
	int GL_GREEN_INTEGER_EXT = 0x8D95;
	int GL_BLUE_INTEGER_EXT = 0x8D96;
	int GL_ALPHA_INTEGER_EXT = 0x8D97;
	int GL_RGB_INTEGER_EXT = 0x8D98;
	int GL_RGBA_INTEGER_EXT = 0x8D99;
	int GL_BGR_INTEGER_EXT = 0x8D9A;
	int GL_BGRA_INTEGER_EXT = 0x8D9B;
	int GL_LUMINANCE_INTEGER_EXT = 0x8D9C;
	int GL_LUMINANCE_ALPHA_INTEGER_EXT = 0x8D9D;

	void glClearColorIiEXT(int r, int g, int b, int a);

	void glClearColorIuiEXT(@GLuint int r, @GLuint int g, @GLuint int b, @GLuint int a);

	@StripPostfix("params")
	void glTexParameterIivEXT(@GLenum int target, @GLenum int pname, @Check("4") IntBuffer params);

	@Alternate("glTexParameterIivEXT")
	@StripPostfix(value = "param", hasPostfix = false)
	void glTexParameterIivEXT(@GLenum int target, @GLenum int pname, @Constant(value = "APIUtil.getInt(caps, param)", keepParam = true) int param);

	@StripPostfix("params")
	void glTexParameterIuivEXT(@GLenum int target, @GLenum int pname, @Check("4") @GLuint IntBuffer params);

	@Alternate("glTexParameterIuivEXT")
	@StripPostfix(value = "param", hasPostfix = false)
	void glTexParameterIuivEXT(@GLenum int target, @GLenum int pname, @Constant(value = "APIUtil.getInt(caps, param)", keepParam = true) int param);

	@StripPostfix("params")
	void glGetTexParameterIivEXT(@GLenum int target, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	@Alternate("glGetTexParameterIivEXT")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetTexParameterIivEXT2(@GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	@StripPostfix("params")
	void glGetTexParameterIuivEXT(@GLenum int target, @GLenum int pname, @OutParameter @Check("4") @GLuint IntBuffer params);

	@Alternate("glGetTexParameterIuivEXT")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetTexParameterIuivEXT2(@GLenum int target, @GLenum int pname, @OutParameter @GLuint IntBuffer params);

}
