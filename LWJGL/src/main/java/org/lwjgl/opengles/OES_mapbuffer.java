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

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLvoid;

import java.nio.ByteBuffer;

public interface OES_mapbuffer {

	/** Accepted by the &lt;access&gt; parameter of MapBufferOES: */
	int GL_WRITE_ONLY_OES = 0x88B9;

	/** Accepted by the &lt;value&gt; parameter of GetBufferParameteriv: */
	int GL_BUFFER_ACCESS_OES = 0x88BB,
		GL_BUFFER_MAPPED_OES = 0x88BC;

	/** Accepted by the &lt;pname&gt; parameter of GetBufferPointervOES: */
	int GL_BUFFER_MAP_POINTER_OES = 0x88BD;

	@StripPostfix("pointer")
	@CachedResult
	@AutoSize("GLES20.glGetBufferParameteri(target, GLES20.GL_BUFFER_SIZE)")
	void glGetBufferPointervOES(@GLenum int target, @GLenum int pname, @OutParameter @Result @GLvoid ByteBuffer pointer);

	/**
	 * glMapBufferOES maps a GL buffer object to a ByteBuffer. The old_buffer argument can be null,
	 * in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 * it will be returned if it points to the same mapped memory and has the same capacity as the buffer object,
	 * otherwise a new ByteBuffer is created. That way, an application will normally use glMapBuffer like this:
	 * <p/>
	 * ByteBuffer mapped_buffer; mapped_buffer = glMapBufferOES(..., ..., null); ... // Another map on the same buffer mapped_buffer = glMapBufferOES(..., ..., mapped_buffer);
	 * <p/>
	 * Only ByteBuffers returned from this method are to be passed as the old_buffer argument. User-created ByteBuffers cannot be reused.
	 * <p/>
	 * The version of this method without an explicit length argument calls glGetBufferParameter internally to
	 * retrieve the current buffer object size, which may cause a pipeline flush and reduce application performance.
	 * <p/>
	 * The version of this method with an explicit length argument is a fast alternative to the one without. No GL call
	 * is made to retrieve the buffer object size, so the user is responsible for tracking and using the appropriate length.<br>
	 * Security warning: The length argument should match the buffer object size. Reading from or writing to outside
	 * the memory region that corresponds to the mapped buffer object will cause native crashes.
	 *
	 * @param old_buffer A ByteBuffer. If this argument points to the same address and has the same capacity as the new mapping, it will be returned and no new buffer will be created.
	 *
	 * @return A ByteBuffer representing the mapped buffer memory.
	 */
	@CachedResult
	@GLvoid
	@AutoSize("GLES20.glGetBufferParameteri(target, GLES20.GL_BUFFER_SIZE)")
	ByteBuffer glMapBufferOES(@GLenum int target, @GLenum int access);

	boolean glUnmapBufferOES(@GLenum int target);

}