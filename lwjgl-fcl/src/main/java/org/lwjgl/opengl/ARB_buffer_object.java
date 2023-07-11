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

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

@Extension(postfix = "ARB", isFinal = false)
public interface ARB_buffer_object {

	/** Accepted by the &lt;usage&gt; parameter of BufferDataARB: */
	int GL_STREAM_DRAW_ARB = 0x88E0;
	int GL_STREAM_READ_ARB = 0x88E1;
	int GL_STREAM_COPY_ARB = 0x88E2;
	int GL_STATIC_DRAW_ARB = 0x88E4;
	int GL_STATIC_READ_ARB = 0x88E5;
	int GL_STATIC_COPY_ARB = 0x88E6;
	int GL_DYNAMIC_DRAW_ARB = 0x88E8;
	int GL_DYNAMIC_READ_ARB = 0x88E9;
	int GL_DYNAMIC_COPY_ARB = 0x88EA;

	/** Accepted by the &lt;access&gt; parameter of MapBufferARB: */
	int GL_READ_ONLY_ARB = 0x88B8;
	int GL_WRITE_ONLY_ARB = 0x88B9;
	int GL_READ_WRITE_ARB = 0x88BA;

	/** Accepted by the &lt;pname&gt; parameter of GetBufferParameterivARB: */
	int GL_BUFFER_SIZE_ARB = 0x8764;
	int GL_BUFFER_USAGE_ARB = 0x8765;
	int GL_BUFFER_ACCESS_ARB = 0x88BB;
	int GL_BUFFER_MAPPED_ARB = 0x88BC;
	int GL_BUFFER_MAP_POINTER_ARB = 0x88BD;

	@Code("		StateTracker.bindBuffer(caps, target, buffer);")
	void glBindBufferARB(@GLenum int target, @GLuint int buffer);

	void glDeleteBuffersARB(@AutoSize("buffers") @GLsizei int n, @Const @GLuint IntBuffer buffers);

	@Alternate("glDeleteBuffersARB")
	void glDeleteBuffersARB(@Constant("1") @GLsizei int n, @Constant(value = "APIUtil.getInt(caps, buffer)", keepParam = true) int buffer);

	void glGenBuffersARB(@AutoSize("buffers") @GLsizei int n, @OutParameter @GLuint IntBuffer buffers);

	@Alternate("glGenBuffersARB")
	@GLreturn("buffers")
	void glGenBuffersARB2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer buffers);

	boolean glIsBufferARB(@GLuint int buffer);

	@GenerateAutos
	void glBufferDataARB(@GLenum int target, @AutoSize("data") @GLsizeiptrARB long size,
	                     @Check
	                     @Const
	                     @GLbyte
	                     @GLshort
	                     @GLint
	                     @GLfloat
	                     @GLdouble Buffer data, @GLenum int usage);

	void glBufferSubDataARB(@GLenum int target, @GLintptrARB long offset, @AutoSize("data") @GLsizeiptrARB long size,
	                        @Check
	                        @Const
	                        @GLbyte
	                        @GLshort
	                        @GLint
	                        @GLfloat
	                        @GLdouble Buffer data);

	void glGetBufferSubDataARB(@GLenum int target, @GLintptrARB long offset, @AutoSize("data") @GLsizeiptrARB long size,
	                           @OutParameter
	                           @Check
	                           @GLbyte
	                           @GLshort
	                           @GLint
	                           @GLfloat
	                           @GLdouble Buffer data);

	/**
	 * glMapBufferARB maps a GL buffer object to a ByteBuffer. The old_buffer argument can be null,
	 * in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 * it will be returned if it points to the same mapped memory and has the same capacity as the buffer object,
	 * otherwise a new ByteBuffer is created. That way, an application will normally use glMapBufferARB like this:
	 * <p/>
	 * ByteBuffer mapped_buffer; mapped_buffer = glMapBufferARB(..., ..., null); ... // Another map on the same buffer mapped_buffer = glMapBufferARB(..., ..., mapped_buffer);
	 * <p/>
	 * Only ByteBuffers returned from this method are to be passed as the old_buffer argument. User-created ByteBuffers cannot be reused.
	 * <p/>
	 * The version of this method without an explicit length argument calls glGetBufferParameterARB internally to
	 * retrieve the current buffer object size, which may cause a pipeline flush and reduce application performance.
	 * <p/>
	 * The version of this method with an explicit length argument is a fast alternative to the one without. No GL call
	 * is made to retrieve the buffer object size, so the user is responsible for tracking and using the appropriate length.<br>
	 * Security warning: The length argument should match the buffer object size. Reading from or writing to outside
	 * the memory region that corresponds to the mapped buffer object will cause native crashes.
	 *
	 * @param length     the length of the mapped memory in bytes.
	 * @param old_buffer A ByteBuffer. If this argument points to the same address and has the same capacity as the new mapping, it will be returned and no new buffer will be created.
	 *
	 * @return A ByteBuffer representing the mapped buffer memory.
	 */
	@CachedResult
	@GLvoid
	@AutoSize("glGetBufferParameteriARB(target, GL_BUFFER_SIZE_ARB)")
	ByteBuffer glMapBufferARB(@GLenum int target, @GLenum int access);

	boolean glUnmapBufferARB(@GLenum int target);

	@StripPostfix("params")
	void glGetBufferParameterivARB(@GLenum int target, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	/** @deprecated Will be removed in 3.0. Use {@link #glGetBufferParameteriARB} instead. */
	@Alternate("glGetBufferParameterivARB")
	@GLreturn("params")
	@StripPostfix("params")
	@Reuse(value = "ARBBufferObject", method = "glGetBufferParameteriARB")
	@Deprecated
	void glGetBufferParameterivARB2(@GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	@Alternate("glGetBufferParameterivARB")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetBufferParameterivARB3(@GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	@StripPostfix("pointer")
	@AutoSize("glGetBufferParameteriARB(target, GL_BUFFER_SIZE_ARB)")
	void glGetBufferPointervARB(@GLenum int target, @GLenum int pname, @Result @GLvoid ByteBuffer pointer);
}
