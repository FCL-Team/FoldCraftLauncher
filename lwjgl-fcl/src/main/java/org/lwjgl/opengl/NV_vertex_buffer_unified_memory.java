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

import java.nio.LongBuffer;

public interface NV_vertex_buffer_unified_memory {

	/**
	 * Accepted by the &lt;cap&gt; parameter of DisableClientState,
	 * EnableClientState, IsEnabled:
	 */
	int GL_VERTEX_ATTRIB_ARRAY_UNIFIED_NV = 0x8F1E;
	int GL_ELEMENT_ARRAY_UNIFIED_NV = 0x8F1F;

	/**
	 * Accepted by the &lt;pname&gt; parameter of BufferAddressRangeNV
	 * and the &lt;value&gt; parameter of GetIntegerui64i_vNV:
	 */
	int GL_VERTEX_ATTRIB_ARRAY_ADDRESS_NV = 0x8F20;
	int GL_TEXTURE_COORD_ARRAY_ADDRESS_NV = 0x8F25;

	/**
	 * Accepted by the &lt;pname&gt; parameter of BufferAddressRangeNV
	 * and the &lt;value&gt; parameter of GetIntegerui64vNV:
	 */
	int GL_VERTEX_ARRAY_ADDRESS_NV = 0x8F21;
	int GL_NORMAL_ARRAY_ADDRESS_NV = 0x8F22;
	int GL_COLOR_ARRAY_ADDRESS_NV = 0x8F23;
	int GL_INDEX_ARRAY_ADDRESS_NV = 0x8F24;
	int GL_EDGE_FLAG_ARRAY_ADDRESS_NV = 0x8F26;
	int GL_SECONDARY_COLOR_ARRAY_ADDRESS_NV = 0x8F27;
	int GL_FOG_COORD_ARRAY_ADDRESS_NV = 0x8F28;
	int GL_ELEMENT_ARRAY_ADDRESS_NV = 0x8F29;

	/** Accepted by the &lt;target&gt; parameter of GetIntegeri_vNV: */
	int GL_VERTEX_ATTRIB_ARRAY_LENGTH_NV = 0x8F2A;
	int GL_TEXTURE_COORD_ARRAY_LENGTH_NV = 0x8F2F;

	/** Accepted by the &lt;value&gt; parameter of GetIntegerv: */
	int GL_VERTEX_ARRAY_LENGTH_NV = 0x8F2B;
	int GL_NORMAL_ARRAY_LENGTH_NV = 0x8F2C;
	int GL_COLOR_ARRAY_LENGTH_NV = 0x8F2D;
	int GL_INDEX_ARRAY_LENGTH_NV = 0x8F2E;
	int GL_EDGE_FLAG_ARRAY_LENGTH_NV = 0x8F30;
	int GL_SECONDARY_COLOR_ARRAY_LENGTH_NV = 0x8F31;
	int GL_FOG_COORD_ARRAY_LENGTH_NV = 0x8F32;
	int GL_ELEMENT_ARRAY_LENGTH_NV = 0x8F33;

	void glBufferAddressRangeNV(@GLenum int pname, @GLuint int index, @GLuint64EXT long address, @GLsizeiptr long length);

	void glVertexFormatNV(int size, @GLenum int type, @GLsizei int stride);

	void glNormalFormatNV(@GLenum int type, @GLsizei int stride);

	void glColorFormatNV(int size, @GLenum int type, @GLsizei int stride);

	void glIndexFormatNV(@GLenum int type, @GLsizei int stride);

	void glTexCoordFormatNV(int size, @GLenum int type, @GLsizei int stride);

	void glEdgeFlagFormatNV(@GLsizei int stride);

	void glSecondaryColorFormatNV(int size, @GLenum int type, @GLsizei int stride);

	void glFogCoordFormatNV(@GLenum int type, @GLsizei int stride);

	void glVertexAttribFormatNV(@GLuint int index, int size, @GLenum int type, boolean normalized, @GLsizei int stride);

	void glVertexAttribIFormatNV(@GLuint int index, int size, @GLenum int type, @GLsizei int stride);

	@StripPostfix(value = "result", postfix = "i64i_v")
	void glGetIntegerui64i_vNV(@GLenum int value, @GLuint int index, @OutParameter @Check("1") @GLuint64EXT LongBuffer result);

	@Alternate("glGetIntegerui64i_vNV")
	@GLreturn("result")
	@StripPostfix(value = "result", postfix = "i_v")
	void glGetIntegerui64i_vNV2(@GLenum int value, @GLuint int index, @OutParameter @GLuint64EXT LongBuffer result);

}