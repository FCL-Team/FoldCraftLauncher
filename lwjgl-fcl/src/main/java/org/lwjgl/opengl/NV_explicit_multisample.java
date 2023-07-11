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
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public interface NV_explicit_multisample {

	/** Accepted by the &lt;pname&gt; parameter of GetMultisamplefvNV: */
	int GL_SAMPLE_POSITION_NV = 0x8E50;

	/**
	 * Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled, and by
	 * the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 * GetDoublev:
	 */

	int GL_SAMPLE_MASK_NV = 0x8E51;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanIndexedvEXT and
	 * GetIntegerIndexedvEXT:
	 */

	int GL_SAMPLE_MASK_VALUE_NV = 0x8E52;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 * and GetFloatv:
	 */

	int GL_TEXTURE_BINDING_RENDERBUFFER_NV = 0x8E53;
	int GL_TEXTURE_RENDERBUFFER_DATA_STORE_BINDING_NV = 0x8E54;
	int GL_MAX_SAMPLE_MASK_WORDS_NV = 0x8E59;

	/** Accepted by the &lt;target&gt; parameter of BindTexture, and TexRenderbufferNV: */

	int GL_TEXTURE_RENDERBUFFER_NV = 0x8E55;

	/** Returned by the &lt;type&gt; parameter of GetActiveUniform: */
	int GL_SAMPLER_RENDERBUFFER_NV = 0x8E56;
	int GL_INT_SAMPLER_RENDERBUFFER_NV = 0x8E57;
	int GL_UNSIGNED_INT_SAMPLER_RENDERBUFFER_NV = 0x8E58;

	@Reuse("EXTDrawBuffers2")
	@StripPostfix(value = "data", extension = "EXT")
	void glGetBooleanIndexedvEXT(@GLenum int pname, @GLuint int index, @OutParameter @Check("4") @GLboolean ByteBuffer data);

	@Reuse("EXTDrawBuffers2")
	@Alternate("glGetBooleanIndexedvEXT")
	@GLreturn("data")
	@StripPostfix(value = "data", extension = "EXT")
	void glGetBooleanIndexedvEXT2(@GLenum int pname, @GLuint int index, @OutParameter @GLboolean ByteBuffer data);

	@Reuse("EXTDrawBuffers2")
	@StripPostfix(value = "data", extension = "EXT")
	void glGetIntegerIndexedvEXT(@GLenum int pname, @GLuint int index, @OutParameter @Check("16") IntBuffer data);

	@Reuse("EXTDrawBuffers2")
	@Alternate("glGetIntegerIndexedvEXT")
	@GLreturn("data")
	@StripPostfix(value = "data", extension = "EXT")
	void glGetIntegerIndexedvEXT2(@GLenum int pname, @GLuint int index, @OutParameter IntBuffer data);

	@StripPostfix("val")
	void glGetMultisamplefvNV(@GLenum int pname, @GLuint int index, @OutParameter @Check("2") FloatBuffer val);

	void glSampleMaskIndexedNV(@GLuint int index, @GLbitfield int mask);

	void glTexRenderbufferNV(@GLenum int target, @GLuint int renderbuffer);

}