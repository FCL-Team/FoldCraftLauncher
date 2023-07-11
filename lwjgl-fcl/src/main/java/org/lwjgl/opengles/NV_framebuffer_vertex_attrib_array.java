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

import org.lwjgl.util.generator.opengl.*;

public interface NV_framebuffer_vertex_attrib_array {

	int GL_FRAMEBUFFER_ATTACHABLE_NV                                = 0x852A,
		GL_VERTEX_ATTRIB_ARRAY_NV                                   = 0x852B,
		GL_FRAMEBUFFER_ATTACHMENT_VERTEX_ATTRIB_ARRAY_SIZE_NV       = 0x852C,
		GL_FRAMEBUFFER_ATTACHMENT_VERTEX_ATTRIB_ARRAY_TYPE_NV       = 0x852D,
		GL_FRAMEBUFFER_ATTACHMENT_VERTEX_ATTRIB_ARRAY_NORMALIZED_NV = 0x852E,
		GL_FRAMEBUFFER_ATTACHMENT_VERTEX_ATTRIB_ARRAY_OFFSET_NV     = 0x852F,
		GL_FRAMEBUFFER_ATTACHMENT_VERTEX_ATTRIB_ARRAY_WIDTH_NV      = 0x8530,
		GL_FRAMEBUFFER_ATTACHMENT_VERTEX_ATTRIB_ARRAY_STRIDE_NV     = 0x8531,
		GL_FRAMEBUFFER_ATTACHMENT_VERTEX_ATTRIB_ARRAY_HEIGHT_NV     = 0x8532;

	void glFramebufferVertexAttribArrayNV(@GLenum int target, @GLenum int attachment,
	                                      @GLenum int buffertarget, @GLuint int bufferobject,
	                                      @GLint int size, @GLenum int type, @GLboolean boolean normalized, @GLintptr long offset,
	                                      @GLsizeiptr long width, @GLsizeiptr long height, @GLsizei int stride);

}