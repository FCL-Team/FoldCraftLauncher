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
import org.lwjgl.util.generator.opengl.*;

import java.nio.ByteBuffer;

public interface ARB_map_buffer_range {

	/** Accepted by the &lt;access&gt; parameter of MapBufferRange: */
	int GL_MAP_READ_BIT = 0x0001;
	int GL_MAP_WRITE_BIT = 0x0002;
	int GL_MAP_INVALIDATE_RANGE_BIT = 0x0004;
	int GL_MAP_INVALIDATE_BUFFER_BIT = 0x0008;
	int GL_MAP_FLUSH_EXPLICIT_BIT = 0x0010;
	int GL_MAP_UNSYNCHRONIZED_BIT = 0x0020;

	/**
	 * glMapBufferRange maps a GL buffer object range to a ByteBuffer. The old_buffer argument can be null,
	 * in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 * it will be returned if it points to the same mapped memory and has the same capacity as the buffer object,
	 * otherwise a new ByteBuffer is created. That way, an application will normally use glMapBufferRange like this:
	 * <p/>
	 * ByteBuffer mapped_buffer; mapped_buffer = glMapBufferRange(..., ..., ..., ..., null); ... // Another map on the same buffer mapped_buffer = glMapBufferRange(..., ..., ..., ..., mapped_buffer);
	 * <p/>
	 * Only ByteBuffers returned from this method are to be passed as the old_buffer argument. User-created ByteBuffers cannot be reused.
	 *
	 * @param old_buffer    A ByteBuffer. If this argument points to the same address and has the same capacity as the new mapping, it will be returned and no new buffer will be created.
	 *
	 * @return A ByteBuffer representing the mapped buffer memory.
	 */
	@Reuse("GL30")
	@CachedResult(isRange = true)
	@GLvoid
	@AutoSize("length")
	ByteBuffer glMapBufferRange(@GLenum int target, @GLintptr long offset, @GLsizeiptr long length, @GLbitfield int access);

	@Reuse("GL30")
	void glFlushMappedBufferRange(@GLenum int target, @GLintptr long offset, @GLsizeiptr long length);

}