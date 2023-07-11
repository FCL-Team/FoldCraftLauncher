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

import java.nio.*;

@Dependent
@DeprecatedGL
public interface ARB_robustness {

	/** Returned by GetGraphicsResetStatusARB: */
	int GL_GUILTY_CONTEXT_RESET_ARB = 0x8253,
		GL_INNOCENT_CONTEXT_RESET_ARB = 0x8254,
		GL_UNKNOWN_CONTEXT_RESET_ARB = 0x8255;

	/**
	 * Accepted by the &lt;value&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetInteger64v, GetFloatv, and GetDoublev:
	 */
	int GL_RESET_NOTIFICATION_STRATEGY_ARB = 0x8256;

	/**
	 * Returned by GetIntegerv and related simple queries when
	 * &lt;value&gt; is RESET_NOTIFICATION_STRATEGY_ARB:
	 */
	int GL_LOSE_CONTEXT_ON_RESET_ARB = 0x8252,
		GL_NO_RESET_NOTIFICATION_ARB = 0x8261;

	/** Returned by GetIntegerv when &lt;pname&gt; is CONTEXT_FLAGS: */
	int GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT_ARB = 0x00000004;

	@GLenum
	int glGetGraphicsResetStatusARB();

	@DeprecatedGL
	void glGetnMapdvARB(@GLenum int target, @GLenum int query, @Constant("v.remaining() << 3") @GLsizei int bufSize, @OutParameter @Check DoubleBuffer v);

	@DeprecatedGL
	void glGetnMapfvARB(@GLenum int target, @GLenum int query, @Constant("v.remaining() << 2") @GLsizei int bufSize, @OutParameter @Check FloatBuffer v);

	@DeprecatedGL
	void glGetnMapivARB(@GLenum int target, @GLenum int query, @Constant("v.remaining() << 2") @GLsizei int bufSize, @OutParameter @Check IntBuffer v);

	@DeprecatedGL
	void glGetnPixelMapfvARB(@GLenum int map, @Constant("values.remaining() << 2") @GLsizei int bufSize, @OutParameter @Check FloatBuffer values);

	@DeprecatedGL
	void glGetnPixelMapuivARB(@GLenum int map, @Constant("values.remaining() << 2") @GLsizei int bufSize, @OutParameter @Check @GLuint IntBuffer values);

	@DeprecatedGL
	void glGetnPixelMapusvARB(@GLenum int map, @Constant("values.remaining() << 1") @GLsizei int bufSize, @OutParameter @Check @GLushort ShortBuffer values);

	@DeprecatedGL
	void glGetnPolygonStippleARB(@AutoSize("pattern") @GLsizei int bufSize, @OutParameter @GLubyte ByteBuffer pattern);

	void glGetnTexImageARB(@GLenum int target, int level, @GLenum int format, @GLenum int type, @AutoSize("img") @GLsizei int bufSize,
	                       @OutParameter
	                       @BufferObject(BufferKind.PackPBO)
	                       @GLbyte
	                       @GLshort
	                       @GLint
	                       @GLfloat
	                       @GLdouble Buffer img);

	void glReadnPixelsARB(int x, int y, @GLsizei int width, @GLsizei int height,
	                      @GLenum int format, @GLenum int type, @AutoSize("data") @GLsizei int bufSize,
	                      @OutParameter
	                      @BufferObject(BufferKind.PackPBO)
	                      @GLbyte
	                      @GLshort
	                      @GLint
	                      @GLfloat
	                      @GLdouble Buffer data);

	@Dependent("GL_ARB_imaging")
	void glGetnColorTableARB(@GLenum int target, @GLenum int format, @GLenum int type, @AutoSize("table") @GLsizei int bufSize,
	                         @OutParameter
	                         @GLbyte
	                         @GLfloat
	                         @GLdouble Buffer table);

	@Dependent("GL_ARB_imaging")
	void glGetnConvolutionFilterARB(@GLenum int target, @GLenum int format, @GLenum int type, @AutoSize("image") @GLsizei int bufSize,
	                                @OutParameter
	                                @BufferObject(BufferKind.PackPBO)
	                                @GLbyte
	                                @GLshort
	                                @GLint
	                                @GLfloat
	                                @GLdouble Buffer image);

	@Dependent("GL_ARB_imaging")
	void glGetnSeparableFilterARB(@GLenum int target, @GLenum int format, @GLenum int type,
	                              @AutoSize("row") @GLsizei int rowBufSize,
	                              @OutParameter
	                              @BufferObject(BufferKind.PackPBO)
	                              @GLbyte
	                              @GLshort
	                              @GLint
	                              @GLfloat
	                              @GLdouble Buffer row,
	                              @AutoSize("column") @GLsizei int columnBufSize,
	                              @OutParameter
	                              @BufferObject(BufferKind.PackPBO)
	                              @GLbyte
	                              @GLshort
	                              @GLint
	                              @GLfloat
	                              @GLdouble Buffer column,
	                              @OutParameter
	                              @BufferObject(BufferKind.PackPBO)
	                              @Check
	                              @GLbyte
	                              @GLshort
	                              @GLint
	                              @GLfloat
	                              @GLdouble Buffer span);

	@Dependent("GL_ARB_imaging")
	void glGetnHistogramARB(@GLenum int target, boolean reset, @GLenum int format, @GLenum int type, @AutoSize("values") @GLsizei int bufSize,
	                        @OutParameter
	                        @BufferObject(BufferKind.PackPBO)
	                        @GLbyte
	                        @GLshort
	                        @GLint
	                        @GLfloat
	                        @GLdouble Buffer values);

	@Dependent("GL_ARB_imaging")
	void glGetnMinmaxARB(@GLenum int target, boolean reset, @GLenum int format, @GLenum int type, @AutoSize("values") @GLsizei int bufSize,
	                     @OutParameter
	                     @BufferObject(BufferKind.PackPBO)
	                     @GLbyte
	                     @GLshort
	                     @GLint
	                     @GLfloat
	                     @GLdouble Buffer values);

	@Dependent("OpenGL13")
	void glGetnCompressedTexImageARB(@GLenum int target, int lod, @AutoSize("img") @GLsizei int bufSize,
	                                 @OutParameter
	                                 @BufferObject(BufferKind.PackPBO)
	                                 @GLbyte
	                                 @GLshort
	                                 @GLint Buffer img);

	@Dependent("OpenGL20")
	void glGetnUniformfvARB(@GLuint int program, int location, @Constant("params.remaining() << 2") @GLsizei int bufSize, @OutParameter @Check FloatBuffer params);

	@Dependent("OpenGL20")
	void glGetnUniformivARB(@GLuint int program, int location, @Constant("params.remaining() << 2") @GLsizei int bufSize, @OutParameter @Check IntBuffer params);

	@Dependent("OpenGL20")
	void glGetnUniformuivARB(@GLuint int program, int location, @Constant("params.remaining() << 2") @GLsizei int bufSize, @OutParameter @Check @GLuint IntBuffer params);

	@Dependent("OpenGL20")
	void glGetnUniformdvARB(@GLuint int program, int location, @Constant("params.remaining() << 3") @GLsizei int bufSize, @OutParameter @Check DoubleBuffer params);
}