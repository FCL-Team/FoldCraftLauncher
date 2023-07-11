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

/**
 * <p/>
 * The GL12 imaging subset extension.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision$
 * $Id$
 */

@Extension(postfix = "")
@DeprecatedGL
public interface ARB_imaging {

	int GL_BLEND_COLOR = 0x8005;
	int GL_FUNC_ADD = 0x8006;
	int GL_MIN = 0x8007;
	int GL_MAX = 0x8008;
	int GL_BLEND_EQUATION = 0x8009;
	int GL_FUNC_SUBTRACT = 0x800A;
	int GL_FUNC_REVERSE_SUBTRACT = 0x800B;
	int GL_COLOR_MATRIX = 0x80B1;
	int GL_COLOR_MATRIX_STACK_DEPTH = 0x80B2;
	int GL_MAX_COLOR_MATRIX_STACK_DEPTH = 0x80B3;
	int GL_POST_COLOR_MATRIX_RED_SCALE = 0x80B4;
	int GL_POST_COLOR_MATRIX_GREEN_SCALE = 0x80B5;
	int GL_POST_COLOR_MATRIX_BLUE_SCALE = 0x80B6;
	int GL_POST_COLOR_MATRIX_ALPHA_SCALE = 0x80B7;
	int GL_POST_COLOR_MATRIX_RED_BIAS = 0x80B8;
	int GL_POST_COLOR_MATRIX_GREEN_BIAS = 0x80B9;
	int GL_POST_COLOR_MATRIX_BLUE_BIAS = 0x80BA;
	int GL_POST_COLOR_MATRIX_ALPHA_BIAS = 0x80BB;
	int GL_COLOR_TABLE = 0x80D0;
	int GL_POST_CONVOLUTION_COLOR_TABLE = 0x80D1;
	int GL_POST_COLOR_MATRIX_COLOR_TABLE = 0x80D2;
	int GL_PROXY_COLOR_TABLE = 0x80D3;
	int GL_PROXY_POST_CONVOLUTION_COLOR_TABLE = 0x80D4;
	int GL_PROXY_POST_COLOR_MATRIX_COLOR_TABLE = 0x80D5;
	int GL_COLOR_TABLE_SCALE = 0x80D6;
	int GL_COLOR_TABLE_BIAS = 0x80D7;
	int GL_COLOR_TABLE_FORMAT = 0x80D8;
	int GL_COLOR_TABLE_WIDTH = 0x80D9;
	int GL_COLOR_TABLE_RED_SIZE = 0x80DA;
	int GL_COLOR_TABLE_GREEN_SIZE = 0x80DB;
	int GL_COLOR_TABLE_BLUE_SIZE = 0x80DC;
	int GL_COLOR_TABLE_ALPHA_SIZE = 0x80DD;
	int GL_COLOR_TABLE_LUMINANCE_SIZE = 0x80DE;
	int GL_COLOR_TABLE_INTENSITY_SIZE = 0x80DF;
	int GL_CONVOLUTION_1D = 0x8010;
	int GL_CONVOLUTION_2D = 0x8011;
	int GL_SEPARABLE_2D = 0x8012;
	int GL_CONVOLUTION_BORDER_MODE = 0x8013;
	int GL_CONVOLUTION_FILTER_SCALE = 0x8014;
	int GL_CONVOLUTION_FILTER_BIAS = 0x8015;
	int GL_REDUCE = 0x8016;
	int GL_CONVOLUTION_FORMAT = 0x8017;
	int GL_CONVOLUTION_WIDTH = 0x8018;
	int GL_CONVOLUTION_HEIGHT = 0x8019;
	int GL_MAX_CONVOLUTION_WIDTH = 0x801A;
	int GL_MAX_CONVOLUTION_HEIGHT = 0x801B;
	int GL_POST_CONVOLUTION_RED_SCALE = 0x801C;
	int GL_POST_CONVOLUTION_GREEN_SCALE = 0x801D;
	int GL_POST_CONVOLUTION_BLUE_SCALE = 0x801E;
	int GL_POST_CONVOLUTION_ALPHA_SCALE = 0x801F;
	int GL_POST_CONVOLUTION_RED_BIAS = 0x8020;
	int GL_POST_CONVOLUTION_GREEN_BIAS = 0x8021;
	int GL_POST_CONVOLUTION_BLUE_BIAS = 0x8022;
	int GL_POST_CONVOLUTION_ALPHA_BIAS = 0x8023;
	int GL_IGNORE_BORDER = 0x8150;
	int GL_CONSTANT_BORDER = 0x8151;
	int GL_REPLICATE_BORDER = 0x8153;
	int GL_CONVOLUTION_BORDER_COLOR = 0x8154;
	int GL_HISTOGRAM = 0x8024;
	int GL_PROXY_HISTOGRAM = 0x8025;
	int GL_HISTOGRAM_WIDTH = 0x8026;
	int GL_HISTOGRAM_FORMAT = 0x8027;
	int GL_HISTOGRAM_RED_SIZE = 0x8028;
	int GL_HISTOGRAM_GREEN_SIZE = 0x8029;
	int GL_HISTOGRAM_BLUE_SIZE = 0x802A;
	int GL_HISTOGRAM_ALPHA_SIZE = 0x802B;
	int GL_HISTOGRAM_LUMINANCE_SIZE = 0x802C;
	int GL_HISTOGRAM_SINK = 0x802D;
	int GL_MINMAX = 0x802E;
	int GL_MINMAX_FORMAT = 0x802F;
	int GL_MINMAX_SINK = 0x8030;
	int GL_TABLE_TOO_LARGE = 0x8031;

	@DeprecatedGL
	void glColorTable(@GLenum int target, @GLenum int internalFormat, @GLsizei int width, @GLenum int format, @GLenum int type,
	                  @BufferObject(BufferKind.UnpackPBO)
	                  @Check("256")
	                  @Const
	                  @GLbyte
	                  @GLfloat
	                  @GLdouble Buffer data);

	@DeprecatedGL
	void glColorSubTable(@GLenum int target, @GLsizei int start, @GLsizei int count, @GLenum int format, @GLenum int type,
	                     @BufferObject(BufferKind.UnpackPBO)
	                     @Check("256")
	                     @Const
	                     @GLbyte
	                     @GLfloat
	                     @GLdouble Buffer data);

	@StripPostfix("params")
	@DeprecatedGL
	void glColorTableParameteriv(@GLenum int target, @GLenum int pname, @Check("4") @Const IntBuffer params);

	@StripPostfix("params")
	@DeprecatedGL
	void glColorTableParameterfv(@GLenum int target, @GLenum int pname, @Check("4") @Const FloatBuffer params);

	@DeprecatedGL
	void glCopyColorSubTable(@GLenum int target, @GLsizei int start, int x, int y, @GLsizei int width);

	@DeprecatedGL
	void glCopyColorTable(@GLenum int target, @GLenum int internalformat, int x, int y, @GLsizei int width);

	@DeprecatedGL
	void glGetColorTable(@GLenum int target, @GLenum int format, @GLenum int type,
	                     @OutParameter
	                     @Check("256")
	                     @GLbyte
	                     @GLfloat
	                     @GLdouble Buffer data);

	@StripPostfix("params")
	@DeprecatedGL
	void glGetColorTableParameteriv(@GLenum int target, @GLenum int pname, @Check("4") IntBuffer params);

	@StripPostfix("params")
	@DeprecatedGL
	void glGetColorTableParameterfv(@GLenum int target, @GLenum int pname, @Check("4") FloatBuffer params);

	@Reuse("GL14")
	void glBlendEquation(@GLenum int mode);

	@Reuse("GL14")
	void glBlendColor(@GLclampf float red, @GLclampf float green, @GLclampf float blue, @GLclampf float alpha);

	@DeprecatedGL
	void glHistogram(@GLenum int target, @GLsizei int width, @GLenum int internalformat, boolean sink);

	@DeprecatedGL
	void glResetHistogram(@GLenum int target);

	@DeprecatedGL
	void glGetHistogram(@GLenum int target, boolean reset, @GLenum int format, @GLenum int type,
	                    @OutParameter
	                    @BufferObject(BufferKind.PackPBO)
	                    @Check("256")
	                    @GLbyte
	                    @GLshort
	                    @GLint
	                    @GLfloat
	                    @GLdouble Buffer values);

	@StripPostfix("params")
	@DeprecatedGL
	void glGetHistogramParameterfv(@GLenum int target, @GLenum int pname, @OutParameter @Check("256") FloatBuffer params);

	@StripPostfix("params")
	@DeprecatedGL
	void glGetHistogramParameteriv(@GLenum int target, @GLenum int pname, @OutParameter @Check("256") IntBuffer params);

	@DeprecatedGL
	void glMinmax(@GLenum int target, @GLenum int internalformat, boolean sink);

	@DeprecatedGL
	void glResetMinmax(@GLenum int target);

	@DeprecatedGL
	void glGetMinmax(@GLenum int target, boolean reset, @GLenum int format, @GLenum int types,
	                 @OutParameter
	                 @BufferObject(BufferKind.PackPBO)
	                 @Check("4")
	                 @GLbyte
	                 @GLshort
	                 @GLint
	                 @GLfloat
	                 @GLdouble Buffer values);

	@StripPostfix("params")
	@DeprecatedGL
	void glGetMinmaxParameterfv(@GLenum int target, @GLenum int pname, @OutParameter @Check("4") FloatBuffer params);

	@StripPostfix("params")
	@DeprecatedGL
	void glGetMinmaxParameteriv(@GLenum int target, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	@DeprecatedGL
	void glConvolutionFilter1D(@GLenum int target, @GLenum int internalformat, @GLsizei int width, @GLenum int format, @GLenum int type,
	                           @BufferObject(BufferKind.UnpackPBO)
	                           @Check("GLChecks.calculateImageStorage(image, format, type, width, 1, 1)")
	                           @Const
	                           @GLbyte
	                           @GLshort
	                           @GLint
	                           @GLfloat
	                           @GLdouble Buffer image);

	@DeprecatedGL
	void glConvolutionFilter2D(@GLenum int target, @GLenum int internalformat, @GLsizei int width, @GLsizei int height, @GLenum int format, @GLenum int type,
	                           @BufferObject(BufferKind.UnpackPBO)
	                           @Check("GLChecks.calculateImageStorage(image, format, type, width, height, 1)")
	                           @Const
	                           @GLbyte
	                           @GLshort
	                           @GLint Buffer image);

	@DeprecatedGL
	void glConvolutionParameterf(@GLenum int target, @GLenum int pname, float params);

	@StripPostfix("params")
	@DeprecatedGL
	void glConvolutionParameterfv(@GLenum int target, @GLenum int pname, @Check("4") @Const FloatBuffer params);

	@DeprecatedGL
	void glConvolutionParameteri(@GLenum int target, @GLenum int pname, int params);

	@StripPostfix("params")
	@DeprecatedGL
	void glConvolutionParameteriv(@GLenum int target, @GLenum int pname, @Check("4") @Const IntBuffer params);

	@DeprecatedGL
	void glCopyConvolutionFilter1D(@GLenum int target, @GLenum int internalformat, int x, int y, @GLsizei int width);

	@DeprecatedGL
	void glCopyConvolutionFilter2D(@GLenum int target, @GLenum int internalformat, int x, int y, @GLsizei int width, @GLsizei int height);

	// TODO: check buffer size valid
	@DeprecatedGL
	void glGetConvolutionFilter(@GLenum int target, @GLenum int format, @GLenum int type,
	                            @OutParameter
	                            @BufferObject(BufferKind.PackPBO)
	                            @Check
	                            @GLbyte
	                            @GLshort
	                            @GLint
	                            @GLfloat
	                            @GLdouble Buffer image);

	@StripPostfix("params")
	@DeprecatedGL
	void glGetConvolutionParameterfv(@GLenum int target, @GLenum int pname, @OutParameter @Check("4") FloatBuffer params);

	@StripPostfix("params")
	@DeprecatedGL
	void glGetConvolutionParameteriv(@GLenum int target, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	// TODO: check buffer size valid
	@DeprecatedGL
	void glSeparableFilter2D(@GLenum int target, @GLenum int internalformat, @GLsizei int width, @GLsizei int height, @GLenum int format, @GLenum int type,
	                         @BufferObject(BufferKind.UnpackPBO)
	                         @Check
	                         @Const
	                         @GLbyte
	                         @GLshort
	                         @GLint
	                         @GLfloat
	                         @GLdouble Buffer row,
	                         @BufferObject(BufferKind.UnpackPBO)
	                         @Check
	                         @Const
	                         @GLbyte
	                         @GLshort
	                         @GLint
	                         @GLfloat
	                         @GLdouble Buffer column);

	// TODO: check buffer size valid
	@DeprecatedGL
	void glGetSeparableFilter(@GLenum int target, @GLenum int format, @GLenum int type,
	                          @OutParameter
	                          @BufferObject(BufferKind.PackPBO)
	                          @Check
	                          @GLbyte
	                          @GLshort
	                          @GLint
	                          @GLfloat
	                          @GLdouble Buffer row,
	                          @BufferObject(BufferKind.PackPBO)
	                          @Check
	                          @GLbyte
	                          @GLshort
	                          @GLint
	                          @GLdouble Buffer column,
	                          @BufferObject(BufferKind.PackPBO)
	                          @Check
	                          @GLbyte
	                          @GLshort
	                          @GLint
	                          @GLdouble Buffer span);
}
