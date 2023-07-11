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

import org.lwjgl.util.generator.opengl.GLenum;

public interface NV_coverage_sample {

	/**
	 * Accepted by the &lt;attrib_list&gt; parameter of eglChooseConfig
	 * and eglCreatePbufferSurface, and by the &lt;attribute&gt;
	 * parameter of eglGetConfigAttrib:
	 */
	int EGL_COVERAGE_BUFFERS_NV = 0x30E0,
		EGL_COVERAGE_SAMPLES_NV = 0x30E1;

	/**
	 * Accepted by the &lt;internalformat&gt; parameter of
	 * RenderbufferStorageEXT and the &lt;format&gt; parameter of ReadPixels:
	 */
	int GL_COVERAGE_COMPONENT_NV = 0x8522;

	/**
	 * Accepted by the &lt;internalformat&gt; parameter of
	 * RenderbufferStorageEXT:
	 */
	int GL_COVERAGE_COMPONENT4_NV = 0x8523;

	/** Accepted by the &lt;operation&gt; parameter of CoverageOperationNV: */
	int GL_COVERAGE_ALL_FRAGMENTS_NV  = 0x8524,
		GL_COVERAGE_EDGE_FRAGMENTS_NV = 0x8525,
		GL_COVERAGE_AUTOMATIC_NV      = 0x8526;

	/**
	 * Accepted by the &lt;attachment&gt; parameter of
	 * FramebufferRenderbuffer, and GetFramebufferAttachmentParameteriv:
	 */
	int GL_COVERAGE_ATTACHMENT_NV = 0x8527;

	/** Accepted by the &lt;buf&gt; parameter of Clear: */
	int GL_COVERAGE_BUFFER_BIT_NV = 0x8000;

	/** Accepted by the &lt;pname&gt; parameter of GetIntegerv: */
	int GL_COVERAGE_BUFFERS_NV = 0x8528,
		GL_COVERAGE_SAMPLES_NV = 0x8529;

	void glCoverageMaskNV(boolean mask);

	void glCoverageOperationNV(@GLenum int operation);

}