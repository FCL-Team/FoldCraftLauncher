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

public interface ARB_ES3_compatibility {

	/** Accepted by the &lt;internalformat&gt; parameter of CompressedTexImage2D */
	int GL_COMPRESSED_RGB8_ETC2                      = 0x9274,
		GL_COMPRESSED_SRGB8_ETC2                     = 0x9275,
		GL_COMPRESSED_RGB8_PUNCHTHROUGH_ALPHA1_ETC2  = 0x9276,
		GL_COMPRESSED_SRGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 0x9277,
		GL_COMPRESSED_RGBA8_ETC2_EAC                 = 0x9278,
		GL_COMPRESSED_SRGB8_ALPHA8_ETC2_EAC          = 0x9279,
		GL_COMPRESSED_R11_EAC                        = 0x9270,
		GL_COMPRESSED_SIGNED_R11_EAC                 = 0x9271,
		GL_COMPRESSED_RG11_EAC                       = 0x9272,
		GL_COMPRESSED_SIGNED_RG11_EAC                = 0x9273;

	/** Accepted by the &lt;target&gt; parameter of Enable and Disable: */
	int GL_PRIMITIVE_RESTART_FIXED_INDEX = 0x8D69;

	/**
	 * Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery,
	 * GetQueryIndexediv and GetQueryiv:
	 */
	int GL_ANY_SAMPLES_PASSED_CONSERVATIVE = 0x8D6A;

	/** Accepted by the &lt;value&gt; parameter of the GetInteger* functions: */
	int GL_MAX_ELEMENT_INDEX = 0x8D6B;

}