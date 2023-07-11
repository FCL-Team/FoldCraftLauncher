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

import org.lwjgl.util.generator.PointerWrapper;
import org.lwjgl.util.generator.opengl.GLenum;

public interface OES_EGL_image_external {

	/**
	 * Accepted as a target in the &lt;target&gt; parameter of BindTexture and
	 * EGLImageTargetTexture2DOES:
	 */
	int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

	/** Returned in the &lt;type&gt; parameter of GetActiveUniform: */
	int GL_SAMPLER_EXTERNAL_OES = 0x8D66;

	/** Accepted as &lt;value&gt; in GetIntegerv() and GetFloatv() queries: */
	int GL_TEXTURE_BINDING_EXTERNAL_OES = 0x8D67;

	/** Accepted as &lt;value&gt; in GetTexParameter*() queries: */
	int GL_REQUIRED_TEXTURE_IMAGE_UNITS_OES = 0x8D68;

	void glEGLImageTargetTexture2DOES(@GLenum int target, @PointerWrapper("GLeglImageOES") EGLImageOES image);

}