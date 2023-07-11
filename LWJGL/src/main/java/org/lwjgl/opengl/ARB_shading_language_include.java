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
import org.lwjgl.util.generator.opengl.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface ARB_shading_language_include {

	/** Accepted by the &lt;type&gt; parameter of NamedStringARB: */
	int GL_SHADER_INCLUDE_ARB = 0x8DAE;

	/** Accepted by the &lt;pname&gt; parameter of GetNamedStringivARB: */
	int GL_NAMED_STRING_LENGTH_ARB = 0x8DE9;
	int GL_NAMED_STRING_TYPE_ARB = 0x8DEA;

	void glNamedStringARB(@GLenum int type,
	                      @AutoSize("name") int namelen, @Const @GLchar ByteBuffer name,
	                      @AutoSize("string") int stringlen, @Const @GLchar ByteBuffer string);

	@Alternate("glNamedStringARB")
	void glNamedStringARB(@GLenum int type,
	                      @Constant("name.length()") int namelen, CharSequence name,
	                      @Constant("string.length()") int stringlen, CharSequence string);

	void glDeleteNamedStringARB(@AutoSize("name") int namelen, @Const @GLchar ByteBuffer name);

	@Alternate("glDeleteNamedStringARB")
	void glDeleteNamedStringARB(@Constant("name.length()") int namelen, CharSequence name);

	void glCompileShaderIncludeARB(@GLuint int shader, @GLsizei int count,
	                               @Const @NullTerminated("count") @PointerArray("count") @GLchar ByteBuffer path,
	                               @Constant("0L") @Const IntBuffer length);

	@Alternate(value = "glCompileShaderIncludeARB", nativeAlt = true)
	void glCompileShaderIncludeARB2(@GLuint int shader, @Constant("path.length") @GLsizei int count,
	                                @Const @PointerArray(value = "count", lengths = "length") CharSequence[] path,
	                                @Constant("APIUtil.getLengths(caps, path)") @Const IntBuffer length);

	boolean glIsNamedStringARB(@AutoSize("name") int namelen, @Const @GLchar ByteBuffer name);

	@Alternate("glIsNamedStringARB")
	boolean glIsNamedStringARB(@Constant("name.length()") int namelen, CharSequence name);

	void glGetNamedStringARB(@AutoSize("name") int namelen, @Const @GLchar ByteBuffer name,
	                         @AutoSize("string") @GLsizei int bufSize,
	                         @OutParameter @Check(value = "1", canBeNull = true) IntBuffer stringlen,
	                         @OutParameter @GLchar ByteBuffer string);

	@Alternate("glGetNamedStringARB")
	void glGetNamedStringARB(@Constant("name.length()") int namelen, CharSequence name,
	                         @AutoSize("string") @GLsizei int bufSize,
	                         @OutParameter @Check(value = "1", canBeNull = true) IntBuffer stringlen,
	                         @OutParameter @GLchar ByteBuffer string);

	@Alternate("glGetNamedStringARB")
	@GLreturn(value = "string", maxLength = "bufSize")
	void glGetNamedStringARB2(@Constant("name.length()") int namelen, CharSequence name,
	                          @GLsizei int bufSize,
	                          @OutParameter @Constant("MemoryUtil.getAddress0(string_length)") IntBuffer stringlen,
	                          @OutParameter @GLchar ByteBuffer string);

	@StripPostfix("params")
	void glGetNamedStringivARB(@AutoSize("name") int namelen, @Const @GLchar ByteBuffer name, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetNamedStringivARB")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetNamedStringivARB(@Constant("name.length()") int namelen, CharSequence name, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetNamedStringivARB")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetNamedStringivARB2(@Constant("name.length()") int namelen, CharSequence name, @GLenum int pname, @OutParameter IntBuffer params);

}