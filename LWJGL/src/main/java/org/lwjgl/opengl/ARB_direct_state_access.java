/*
 * Copyright (c) 2002-2014 LWJGL Project
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

import org.lwjgl.PointerBuffer;
import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.*;

import java.nio.*;

@Extension(postfix = "")
public interface ARB_direct_state_access {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetTextureParameter{if}v and
	 * GetTextureParameterI{i ui}v:
	 */
	int GL_TEXTURE_TARGET = 0x1006;

	/** Accepted by the &lt;pname&gt; parameter of GetQueryObjectiv: */
	int GL_QUERY_TARGET = 0x82EA;

	/** Accepted by the &lt;pname&gt; parameter of GetIntegeri_v: */
	int GL_TEXTURE_BINDING = 0x82EB;

	// Transform Feedback object functions

	@Reuse("GL45")
	void glCreateTransformFeedbacks(@AutoSize("ids") @GLsizei int n, @OutParameter @GLuint IntBuffer ids);

	@Alternate("glCreateTransformFeedbacks")
	@GLreturn("ids")
	@Reuse("GL45")
	void glCreateTransformFeedbacks2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer ids);

	@Reuse("GL45")
	void glTransformFeedbackBufferBase(@GLuint int xfb, @GLuint int index, @GLuint int buffer);

	@Reuse("GL45")
	void glTransformFeedbackBufferRange(@GLuint int xfb, @GLuint int index, @GLuint int buffer, @GLintptr long offset, @GLsizeiptr long size);

	@StripPostfix("param")
	@Reuse("GL45")
	void glGetTransformFeedbackiv(@GLuint int xfb, @GLenum int pname, @OutParameter @Check("1") IntBuffer param);

	@Alternate("glGetTransformFeedbackiv")
	@GLreturn("param")
	@StripPostfix(value = "param", hasPostfix = false)
	@Reuse("GL45")
	void glGetTransformFeedbackiv2(@GLuint int xfb, @GLenum int pname, @OutParameter IntBuffer param);

	@StripPostfix("param")
	@Reuse("GL45")
	void glGetTransformFeedbacki_v(@GLuint int xfb, @GLenum int pname, @GLuint int index, @OutParameter @Check("1") IntBuffer param);

	@Alternate("glGetTransformFeedbacki_v")
	@GLreturn("param")
	@StripPostfix(value = "param", postfix = "_v")
	@Reuse("GL45")
	void glGetTransformFeedbacki_v2(@GLuint int xfb, @GLenum int pname, @GLuint int index, @OutParameter IntBuffer param);

	@StripPostfix("param")
	@Reuse("GL45")
	void glGetTransformFeedbacki64_v(@GLuint int xfb, @GLenum int pname, @GLuint int index, @OutParameter @Check("1") @GLint64 LongBuffer param);

	@Alternate("glGetTransformFeedbacki64_v")
	@GLreturn("param")
	@StripPostfix(value = "param", postfix = "_v")
	@Reuse("GL45")
	void glGetTransformFeedbacki64_v2(@GLuint int xfb, @GLenum int pname, @GLuint int index, @OutParameter @GLint64 LongBuffer param);

	// Buffer object functions

	@Reuse("GL45")
	void glCreateBuffers(@AutoSize("buffers") @GLsizei int n, @OutParameter @GLuint IntBuffer buffers);

	@Alternate("glCreateBuffers")
	@GLreturn("buffers")
	@Reuse("GL45")
	void glCreateBuffers2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer buffers);

	@Reuse("GL45")
	void glNamedBufferStorage(@GLuint int buffer, @AutoSize("data") @GLsizeiptr long size,
		@Const
		@GLbyte
		@GLshort
		@GLint
		@GLuint64
		@GLfloat
		@GLdouble Buffer data,
		@GLbitfield int flags);

	@Alternate("glNamedBufferStorage")
	@Reuse("GL45")
	void glNamedBufferStorage2(@GLuint int buffer, @GLsizeiptr long size, @Constant("0L") @Const Buffer data, @GLbitfield int flags);

	@GenerateAutos
	@Reuse("GL45")
	void glNamedBufferData(@GLuint int buffer, @AutoSize("data") @GLsizeiptr long size,
		@Check
		@Const
		@GLbyte
		@GLshort
		@GLint
		@GLfloat
		@GLdouble Buffer data,
		@GLenum int usage);

	@Reuse("GL45")
	void glNamedBufferSubData(@GLuint int buffer, @GLintptr long offset, @AutoSize("data") @GLsizeiptr long size,
		@Check
		@Const
		@GLbyte
		@GLshort
		@GLint
		@GLfloat
		@GLdouble Buffer data);

	@Reuse("GL45")
	void glCopyNamedBufferSubData(@GLuint int readBuffer, @GLuint int writeBuffer, @GLintptr long readOffset, @GLintptr long writeOffset, @GLsizeiptr long size);

	@Reuse("GL45")
	void glClearNamedBufferData(@GLuint int buffer, @GLenum int internalformat, @GLenum int format, @GLenum int type, @Check("1") @Const @GLvoid ByteBuffer data);

	@Reuse("GL45")
	void glClearNamedBufferSubData(@GLuint int buffer, @GLenum int internalformat, @GLintptr long offset, @GLsizeiptr long size, @GLenum int format, @GLenum int type, @Check("1") @Const @GLvoid ByteBuffer data);

	/**
	 * Maps a buffer object's data store.
	 * <p/>
	 * <b>LWJGL note</b>: This method comes in 2 flavors:
	 * <ol>
	 * <li>{@link #glMapNamedBuffer(int, int, ByteBuffer)} - Calls {@link #glGetNamedBufferParameteri} to retrieve the buffer size and the {@code old_buffer} parameter is reused if the returned size and pointer match the buffer capacity and address, respectively.</li>
	 * <li>{@link #glMapNamedBuffer(int, int, int, ByteBuffer)} - The buffer size is explicitly specified and the {@code old_buffer} parameter is reused if {@code size} and the returned pointer match the buffer capacity and address, respectively. This is the most efficient method.</li>
	 * </ol>
	 *
	 * @param buffer the buffer object being mapped
	 * @param access the access policy, indicating whether it will be possible to read from, write to, or both read from and write to the buffer object's mapped data store
	 */
	@CachedResult
	@GLvoid
	@AutoSize("glGetNamedBufferParameteri(buffer, GL15.GL_BUFFER_SIZE)")
	@Reuse("GL45")
	ByteBuffer glMapNamedBuffer(@GLuint int buffer, @GLenum int access);

	@CachedResult(isRange = true)
	@GLvoid
	@AutoSize("length")
	@Reuse("GL45")
	ByteBuffer glMapNamedBufferRange(@GLuint int buffer, @GLintptr long offset, @GLsizeiptr long length, @GLbitfield int access);

	@Reuse("GL45")
	boolean glUnmapNamedBuffer(@GLuint int buffer);

	@Reuse("GL45")
	void glFlushMappedNamedBufferRange(@GLuint int buffer, @GLintptr long offset, @GLsizeiptr long length);

	@StripPostfix("params")
	@Reuse("GL45")
	void glGetNamedBufferParameteriv(@GLuint int buffer, @GLenum int pname, @OutParameter @Check IntBuffer params);

	@Alternate("glGetNamedBufferParameteriv")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	@Reuse("GL45")
	void glGetNamedBufferParameteriv2(@GLuint int buffer, @GLenum int pname, @OutParameter IntBuffer params);

	@StripPostfix("params")
	@Reuse("GL45")
	void glGetNamedBufferParameteri64v(@GLuint int buffer, @GLenum int pname, @OutParameter @Check("1") @GLint64 LongBuffer params);

	@Alternate("glGetNamedBufferParameteri64v")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	@Reuse("GL45")
	void glGetNamedBufferParameteri64v2(@GLuint int buffer, @GLenum int pname, @OutParameter @GLint64 LongBuffer params);

	@StripPostfix("params")
	@AutoSize("glGetNamedBufferParameteri(buffer, GL15.GL_BUFFER_SIZE)")
	@Reuse("GL45")
	void glGetNamedBufferPointerv(@GLuint int buffer, @GLenum int pname, @Result @GLvoid ByteBuffer params);

	@Reuse("GL45")
	void glGetNamedBufferSubData(@GLuint int buffer, @GLintptr long offset, @AutoSize("data") @GLsizeiptr long size,
		@OutParameter
		@Check
		@GLbyte
		@GLshort
		@GLint
		@GLfloat
		@GLdouble Buffer data);

	// Framebuffer object functions

	@Reuse("GL45")
	void glCreateFramebuffers(@AutoSize("framebuffers") @GLsizei int n, @OutParameter @GLuint IntBuffer framebuffers);

	@Alternate("glCreateFramebuffers")
	@GLreturn("framebuffers")
	@Reuse("GL45")
	void glCreateFramebuffers2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer framebuffers);

	@Reuse("GL45")
	void glNamedFramebufferRenderbuffer(@GLuint int framebuffer, @GLenum int attachment, @GLenum int renderbuffertarget, @GLuint int renderbuffer);

	@Reuse("GL45")
	void glNamedFramebufferParameteri(@GLuint int framebuffer, @GLenum int pname, int param);

	@Reuse("GL45")
	void glNamedFramebufferTexture(@GLuint int framebuffer, @GLenum int attachment, @GLuint int texture, int level);

	@Reuse("GL45")
	void glNamedFramebufferTextureLayer(@GLuint int framebuffer, @GLenum int attachment, @GLuint int texture, int level, int layer);

	@Reuse("GL45")
	void glNamedFramebufferDrawBuffer(@GLuint int framebuffer, @GLenum int mode);

	@Reuse("GL45")
	void glNamedFramebufferDrawBuffers(@GLuint int framebuffer, @AutoSize("bufs") @GLsizei int n, @Const @GLenum IntBuffer bufs);

	@Reuse("GL45")
	void glNamedFramebufferReadBuffer(@GLuint int framebuffer, @GLenum int mode);

	@Reuse("GL45")
	void glInvalidateNamedFramebufferData(@GLuint int framebuffer, @AutoSize("attachments") @GLsizei int numAttachments, @Const @GLenum IntBuffer attachments);

	@Reuse("GL45")
	void glInvalidateNamedFramebufferSubData(@GLuint int framebuffer, @AutoSize("attachments") @GLsizei int numAttachments, @Const @GLenum IntBuffer attachments, int x, int y, @GLsizei int width, @GLsizei int height);

	@StripPostfix("value")
	@Reuse("GL45")
	void glClearNamedFramebufferiv(@GLuint int framebuffer, @GLenum int buffer, int drawbuffer, @Const @Check("1") IntBuffer value);

	@StripPostfix("value")
	@Reuse("GL45")
	void glClearNamedFramebufferuiv(@GLuint int framebuffer, @GLenum int buffer, int drawbuffer, @Const @Check("4") @GLuint IntBuffer value);

	@StripPostfix("value")
	@Reuse("GL45")
	void glClearNamedFramebufferfv(@GLuint int framebuffer, @GLenum int buffer, int drawbuffer, @Const @Check("1") FloatBuffer value);

	@Reuse("GL45")
	void glClearNamedFramebufferfi(@GLuint int framebuffer, @GLenum int buffer, float depth, int stencil);

	@Reuse("GL45")
	void glBlitNamedFramebuffer(
		@GLuint int readFramebuffer, @GLuint int drawFramebuffer,
		int srcX0, int srcY0, int srcX1, int srcY1,
		int dstX0, int dstY0, int dstX1, int dstY1,
		@GLbitfield int mask, @GLenum int filter);

	@GLenum
	@Reuse("GL45")
	int glCheckNamedFramebufferStatus(@GLuint int framebuffer, @GLenum int target);

	@StripPostfix("params")
	@Reuse("GL45")
	void glGetNamedFramebufferParameteriv(@GLuint int framebuffer, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetNamedFramebufferParameteriv")
	@GLreturn("params")
	@StripPostfix("params")
	@Reuse("GL45")
	void glGetNamedFramebufferParameteriv2(@GLuint int framebuffer, @GLenum int pname, @OutParameter IntBuffer params);

	@StripPostfix("params")
	@Reuse("GL45")
	void glGetNamedFramebufferAttachmentParameteriv(@GLuint int framebuffer, @GLenum int attachment, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetNamedFramebufferAttachmentParameteriv")
	@GLreturn("params")
	@StripPostfix("params")
	@Reuse("GL45")
	void glGetNamedFramebufferAttachmentParameteriv2(@GLuint int framebuffer, @GLenum int attachment, @GLenum int pname, @OutParameter IntBuffer params);

	// Renderbuffer object functions

	@Reuse("GL45")
	void glCreateRenderbuffers(@AutoSize("renderbuffers") @GLsizei int n, @OutParameter @GLuint IntBuffer renderbuffers);

	@Alternate("glCreateRenderbuffers")
	@GLreturn("renderbuffers")
	@Reuse("GL45")
	void glCreateRenderbuffers2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer renderbuffers);

	@Reuse("GL45")
	void glNamedRenderbufferStorage(@GLuint int renderbuffer, @GLenum int internalformat, @GLsizei int width, @GLsizei int height);

	@Reuse("GL45")
	void glNamedRenderbufferStorageMultisample(@GLuint int renderbuffer, @GLsizei int samples, @GLenum int internalformat, @GLsizei int width, @GLsizei int height);

	@StripPostfix("params")
	@Reuse("GL45")
	void glGetNamedRenderbufferParameteriv(@GLuint int renderbuffer, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetNamedRenderbufferParameteriv")
	@GLreturn("params")
	@StripPostfix("params")
	@Reuse("GL45")
	void glGetNamedRenderbufferParameteriv2(@GLuint int renderbuffer, @GLenum int pname, @OutParameter IntBuffer params);

	// Texture object functions

	@Reuse("GL45")
	void glCreateTextures(@GLenum int target, @AutoSize("textures") @GLsizei int n, @OutParameter @GLuint IntBuffer textures);

	@Alternate("glCreateTextures")
	@GLreturn("textures")
	@Reuse("GL45")
	void glCreateTextures2(@GLenum int target, @Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer textures);

	@Reuse("GL45")
	void glTextureBuffer(@GLuint int texture, @GLenum int internalformat, @GLuint int buffer);

	@Reuse("GL45")
	void glTextureBufferRange(@GLuint int texture, @GLenum int internalformat, @GLuint int buffer, @GLintptr long offset, @GLsizeiptr long size);

	@Reuse("GL45")
	void glTextureStorage1D(@GLuint int texture, @GLsizei int levels, @GLenum int internalformat, @GLsizei int width);

	@Reuse("GL45")
	void glTextureStorage2D(@GLuint int texture, @GLsizei int levels, @GLenum int internalformat, @GLsizei int width, @GLsizei int height);

	@Reuse("GL45")
	void glTextureStorage3D(@GLuint int texture, @GLsizei int levels, @GLenum int internalformat, @GLsizei int width, @GLsizei int height, @GLsizei int depth);

	@Reuse("GL45")
	void glTextureStorage2DMultisample(@GLuint int texture, @GLsizei int samples, @GLenum int internalformat, @GLsizei int width, @GLsizei int height, boolean fixedsamplelocations);

	@Reuse("GL45")
	void glTextureStorage3DMultisample(@GLuint int texture, @GLsizei int samples, @GLenum int internalformat, @GLsizei int width, @GLsizei int height, @GLsizei int depth, boolean fixedsamplelocations);

	@Reuse("GL45")
	void glTextureSubImage1D(@GLuint int texture, int level, int xoffset, @GLsizei int width, @GLenum int format, @GLenum int type,
		@BufferObject(BufferKind.UnpackPBO)
		@Check("GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1)")
		@Const
		@GLbyte
		@GLshort
		@GLint
		@GLfloat
		@GLdouble Buffer pixels);

	@Reuse("GL45")
	void glTextureSubImage2D(@GLuint int texture, int level, int xoffset, int yoffset, @GLsizei int width, @GLsizei int height, @GLenum int format, @GLenum int type,
		@BufferObject(BufferKind.UnpackPBO)
		@Check("GLChecks.calculateImageStorage(pixels, format, type, width, height, 1)")
		@Const
		@GLbyte
		@GLshort
		@GLint
		@GLfloat
		@GLdouble Buffer pixels);

	@Reuse("GL45")
	void glTextureSubImage3D(@GLuint int texture, int level, int xoffset, int yoffset, int zoffset, @GLsizei int width, @GLsizei int height, @GLsizei int depth, @GLenum int format, @GLenum int type,
		@BufferObject(BufferKind.UnpackPBO)
		@Check("GLChecks.calculateImageStorage(pixels, format, type, width, height, depth)")
		@Const
		@GLbyte
		@GLshort
		@GLint
		@GLfloat
		@GLdouble Buffer pixels);

	@Reuse("GL45")
	void glCompressedTextureSubImage1D(@GLuint int texture, int level, int xoffset, @GLsizei int width, @GLenum int format, @AutoSize("data") @GLsizei int imageSize,
		@BufferObject(BufferKind.UnpackPBO)
		@Check
		@Const
		@GLvoid
		ByteBuffer data);

	@Reuse("GL45")
	void glCompressedTextureSubImage2D(@GLuint int texture, int level, int xoffset, int yoffset, @GLsizei int width, @GLsizei int height, @GLenum int format, @AutoSize("data") @GLsizei int imageSize,
		@BufferObject(BufferKind.UnpackPBO)
		@Check
		@Const
		@GLvoid
		ByteBuffer data);

	@Reuse("GL45")
	void glCompressedTextureSubImage3D(@GLuint int texture, int level, int xoffset, int yoffset, int zoffset, @GLsizei int width, @GLsizei int height, @GLsizei int depth, @GLenum int format, @GLsizei int imageSize,
		@BufferObject(BufferKind.UnpackPBO)
		@Check
		@Const
		@GLvoid
		ByteBuffer data);

	@Reuse("GL45")
	void glCopyTextureSubImage1D(@GLuint int texture, int level, int xoffset, int x, int y, @GLsizei int width);

	@Reuse("GL45")
	void glCopyTextureSubImage2D(@GLuint int texture, int level, int xoffset, int yoffset, int x, int y, @GLsizei int width, @GLsizei int height);

	@Reuse("GL45")
	void glCopyTextureSubImage3D(@GLuint int texture, int level, int xoffset, int yoffset, int zoffset, int x, int y, @GLsizei int width, @GLsizei int height);

	@Reuse("GL45")
	void glTextureParameterf(@GLuint int texture, @GLenum int pname, float param);

	@StripPostfix("params")
	@Reuse("GL45")
	void glTextureParameterfv(@GLuint int texture, @GLenum int pname, @Const @Check("4") FloatBuffer params);

	@Reuse("GL45")
	void glTextureParameteri(@GLuint int texture, @GLenum int pname, int param);

	@StripPostfix("params")
	@Reuse("GL45")
	void glTextureParameterIiv(@GLuint int texture, @GLenum int pname, @Const @Check("1") IntBuffer params);

	@StripPostfix("params")
	@Reuse("GL45")
	void glTextureParameterIuiv(@GLuint int texture, @GLenum int pname, @Const @Check("1") @GLuint IntBuffer params);

	@StripPostfix("params")
	@Reuse("GL45")
	void glTextureParameteriv(@GLuint int texture, @GLenum int pname, @Const @Check("4") IntBuffer params);

	@Reuse("GL45")
	void glGenerateTextureMipmap(@GLuint int texture);

	@Reuse("GL45")
	void glBindTextureUnit(@GLuint int unit, @GLuint int texture);

	@Reuse("GL45")
	void glGetTextureImage(@GLuint int texture, int level, @GLenum int format, @GLenum int type, @AutoSize("pixels") @GLsizei int bufSize,
		@OutParameter
		@BufferObject(BufferKind.PackPBO)
		@GLbyte
		@GLshort
		@GLint
		@GLfloat
		@GLdouble Buffer pixels);

	@Reuse("GL45")
	void glGetCompressedTextureImage(@GLuint int texture, int level, @AutoSize("pixels") @GLsizei int bufSize,
		@OutParameter
		@BufferObject(BufferKind.PackPBO)
		@Check
		@GLbyte
		@GLshort
		@GLint Buffer pixels);

	@StripPostfix("params")
	@Reuse("GL45")
	void glGetTextureLevelParameterfv(@GLuint int texture, int level, @GLenum int pname, @OutParameter @Check("1") FloatBuffer params);

	@Alternate("glGetTextureLevelParameterfv")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	@Reuse("GL45")
	void glGetTextureLevelParameterfv2(@GLuint int texture, int level, @GLenum int pname, @OutParameter FloatBuffer params);

	@StripPostfix("params")
	@Reuse("GL45")
	void glGetTextureLevelParameteriv(@GLuint int texture, int level, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetTextureLevelParameteriv")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	@Reuse("GL45")
	void glGetTextureLevelParameteriv2(@GLuint int texture, int level, @GLenum int pname, @OutParameter IntBuffer params);

	@StripPostfix("params")
	@Reuse("GL45")
	void glGetTextureParameterfv(@GLuint int texture, @GLenum int pname, @OutParameter @Check("1") FloatBuffer params);

	@Alternate("glGetTextureParameterfv")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	@Reuse("GL45")
	void glGetTextureParameterfv2(@GLuint int texture, @GLenum int pname, @OutParameter FloatBuffer params);

	@StripPostfix("params")
	@Reuse("GL45")
	void glGetTextureParameterIiv(@GLuint int texture, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetTextureParameterIiv")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	@Reuse("GL45")
	void glGetTextureParameterIiv2(@GLuint int texture, @GLenum int pname, @OutParameter IntBuffer params);

	@StripPostfix("params")
	@Reuse("GL45")
	void glGetTextureParameterIuiv(@GLuint int texture, @GLenum int pname, @OutParameter @Check("1") @GLuint IntBuffer params);

	@Alternate("glGetTextureParameterIuiv")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	@Reuse("GL45")
	void glGetTextureParameterIuiv2(@GLuint int texture, @GLenum int pname, @OutParameter @GLuint IntBuffer params);

	@StripPostfix("params")
	@Reuse("GL45")
	void glGetTextureParameteriv(@GLuint int texture, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetTextureParameteriv")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	@Reuse("GL45")
	void glGetTextureParameteriv2(@GLuint int texture, @GLenum int pname, @OutParameter IntBuffer params);

	// Vertex Array object functions

	@Reuse("GL45")
	void glCreateVertexArrays(@AutoSize("arrays") @GLsizei int n, @OutParameter @GLuint IntBuffer arrays);

	@Alternate("glCreateVertexArrays")
	@GLreturn("arrays")
	@Reuse("GL45")
	void glCreateVertexArrays2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer arrays);

	@Reuse("GL45")
	void glDisableVertexArrayAttrib(@GLuint int vaobj, @GLuint int index);

	@Reuse("GL45")
	void glEnableVertexArrayAttrib(@GLuint int vaobj, @GLuint int index);

	@Reuse("GL45")
	void glVertexArrayElementBuffer(@GLuint int vaobj, @GLuint int buffer);

	@Reuse("GL45")
	void glVertexArrayVertexBuffer(@GLuint int vaobj, @GLuint int bindingindex, @GLuint int buffer, @GLintptr long offset, @GLsizei int stride);

	@Reuse("GL45")
	void glVertexArrayVertexBuffers(@GLuint int vaobj, @GLuint int first, @GLsizei int count,
		@Check(value = "count", canBeNull = true) @Const @GLuint IntBuffer buffers,
		@Check(value = "count", canBeNull = true) @Const @GLintptr PointerBuffer offsets,
		@Check(value = "count", canBeNull = true) @Const @GLsizei IntBuffer strides);

	@Reuse("GL45")
	void glVertexArrayAttribFormat(@GLuint int vaobj, @GLuint int attribindex, int size, @GLenum int type, boolean normalized, @GLuint int relativeoffset);

	@Reuse("GL45")
	void glVertexArrayAttribIFormat(@GLuint int vaobj, @GLuint int attribindex, int size, @GLenum int type, @GLuint int relativeoffset);

	@Reuse("GL45")
	void glVertexArrayAttribLFormat(@GLuint int vaobj, @GLuint int attribindex, int size, @GLenum int type, @GLuint int relativeoffset);

	@Reuse("GL45")
	void glVertexArrayAttribBinding(@GLuint int vaobj, @GLuint int attribindex, @GLuint int bindingindex);

	@Reuse("GL45")
	void glVertexArrayBindingDivisor(@GLuint int vaobj, @GLuint int bindingindex, @GLuint int divisor);

	@StripPostfix("param")
	@Reuse("GL45")
	void glGetVertexArrayiv(@GLuint int vaobj, @GLenum int pname, @OutParameter @Check("1") IntBuffer param);

	@Alternate("glGetVertexArrayiv")
	@GLreturn("param")
	@StripPostfix("param")
	@Reuse("GL45")
	void glGetVertexArrayiv2(@GLuint int vaobj, @GLenum int pname, @OutParameter IntBuffer param);

	@StripPostfix("param")
	@Reuse("GL45")
	void glGetVertexArrayIndexediv(@GLuint int vaobj, @GLuint int index, @GLenum int pname, @OutParameter @Check("1") IntBuffer param);

	@Alternate("glGetVertexArrayIndexediv")
	@GLreturn("param")
	@StripPostfix("param")
	@Reuse("GL45")
	void glGetVertexArrayIndexediv2(@GLuint int vaobj, @GLuint int index, @GLenum int pname, @OutParameter IntBuffer param);

	@StripPostfix("param")
	@Reuse("GL45")
	void glGetVertexArrayIndexed64iv(@GLuint int vaobj, @GLuint int index, @GLenum int pname, @OutParameter @Check("1") @GLint64 LongBuffer param);

	@Alternate("glGetVertexArrayIndexed64iv")
	@GLreturn("param")
	@StripPostfix("param")
	@Reuse("GL45")
	void glGetVertexArrayIndexed64iv2(@GLuint int vaobj, @GLuint int index, @GLenum int pname, @OutParameter @GLint64 LongBuffer param);

	// Sampler object functions

	@Reuse("GL45")
	void glCreateSamplers(@AutoSize("samplers") @GLsizei int n, @OutParameter @GLuint IntBuffer samplers);

	@Alternate("glCreateSamplers")
	@GLreturn("samplers")
	@Reuse("GL45")
	void glCreateSamplers2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer samplers);

	// Program Pipeline object functions

	@Reuse("GL45")
	void glCreateProgramPipelines(@AutoSize("pipelines") @GLsizei int n, @OutParameter @GLuint IntBuffer pipelines);

	@Alternate("glCreateProgramPipelines")
	@GLreturn("pipelines")
	@Reuse("GL45")
	void glCreateProgramPipelines2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer pipelines);

	// Query object functions

	@Reuse("GL45")
	void glCreateQueries(@GLenum int target, @AutoSize("ids") @GLsizei int n, @OutParameter @GLuint IntBuffer ids);

	@Alternate("glCreateQueries")
	@GLreturn("ids")
	@Reuse("GL45")
	void glCreateQueries2(@GLenum int target, @Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer ids);

}