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

@ForceInit
@Dependent
@DeprecatedGL
public interface EXT_direct_state_access {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanIndexedvEXT,
	 * GetIntegerIndexedvEXT, GetFloatIndexedvEXT, GetDoubleIndexedvEXT:
	 * GetBooleani_v, GetIntegeri_v, GetFloati_vEXT, GetDoublei_vEXT:
	 */
	int GL_PROGRAM_MATRIX_EXT = 0x8E2D;
	int GL_TRANSPOSE_PROGRAM_MATRIX_EXT = 0x8E2E;
	int GL_PROGRAM_MATRIX_STACK_DEPTH_EXT = 0x8E2F;

	/*
	OpenGL 1.1: New client commands
	 */

	@DeprecatedGL
	void glClientAttribDefaultEXT(@GLbitfield int mask);

	@DeprecatedGL
	void glPushClientAttribDefaultEXT(@GLbitfield int mask);

	/*
	OpenGL 1.0: New matrix commands add "Matrix" prefix to name,
    drops "Matrix" suffix from name, and add initial "enum matrixMode"
    parameter
	 */

	@StripPostfix("m")
	@DeprecatedGL
	void glMatrixLoadfEXT(@GLenum int matrixMode, @Check("16") @Const FloatBuffer m);

	@StripPostfix("m")
	@DeprecatedGL
	void glMatrixLoaddEXT(@GLenum int matrixMode, @Check("16") @Const DoubleBuffer m);

	@StripPostfix("m")
	@DeprecatedGL
	void glMatrixMultfEXT(@GLenum int matrixMode, @Check("16") @Const FloatBuffer m);

	@StripPostfix("m")
	@DeprecatedGL
	void glMatrixMultdEXT(@GLenum int matrixMode, @Check("16") @Const DoubleBuffer m);

	@DeprecatedGL
	void glMatrixLoadIdentityEXT(@GLenum int matrixMode);

	@DeprecatedGL
	void glMatrixRotatefEXT(@GLenum int matrixMode, float angle, float x, float y, float z);

	@DeprecatedGL
	void glMatrixRotatedEXT(@GLenum int matrixMode, double angle, double x, double y, double z);

	@DeprecatedGL
	void glMatrixScalefEXT(@GLenum int matrixMode, float x, float y, float z);

	@DeprecatedGL
	void glMatrixScaledEXT(@GLenum int matrixMode, double x, double y, double z);

	@DeprecatedGL
	void glMatrixTranslatefEXT(@GLenum int matrixMode, float x, float y, float z);

	@DeprecatedGL
	void glMatrixTranslatedEXT(@GLenum int matrixMode, double x, double y, double z);

	@DeprecatedGL
	void glMatrixOrthoEXT(@GLenum int matrixMode, double l, double r, double b, double t, double n, double f);

	@DeprecatedGL
	void glMatrixFrustumEXT(@GLenum int matrixMode, double l, double r, double b, double t, double n, double f);

	@DeprecatedGL
	void glMatrixPushEXT(@GLenum int matrixMode);

	@DeprecatedGL
	void glMatrixPopEXT(@GLenum int matrixMode);

	/*
	OpenGL 1.1: New texture object commands and queries replace "Tex"
    in name with "Texture" and add initial "uint texture" parameter
	 */

	void glTextureParameteriEXT(@GLuint int texture, @GLenum int target, @GLenum int pname, int param);

	@StripPostfix("param")
	void glTextureParameterivEXT(@GLuint int texture, @GLenum int target, @GLenum int pname, @Check("4") @Const IntBuffer param);

	void glTextureParameterfEXT(@GLuint int texture, @GLenum int target, @GLenum int pname, float param);

	@StripPostfix("param")
	void glTextureParameterfvEXT(@GLuint int texture, @GLenum int target, @GLenum int pname, @Check("4") @Const FloatBuffer param);

	void glTextureImage1DEXT(@GLuint int texture, @GLenum int target, int level,
	                         int internalformat, @GLsizei int width, int border, @GLenum int format, @GLenum int type,
	                         @BufferObject(BufferKind.UnpackPBO)
	                         @Check(value = "GLChecks.calculateTexImage1DStorage(pixels, format, type, width)", canBeNull = true)
	                         @Const
	                         @GLbyte
	                         @GLshort
	                         @GLint
	                         @GLfloat
	                         @GLdouble Buffer pixels);

	void glTextureImage2DEXT(@GLuint int texture, @GLenum int target, int level,
	                         int internalformat, @GLsizei int width, @GLsizei int height, int border, @GLenum int format, @GLenum int type,
	                         @BufferObject(BufferKind.UnpackPBO)
	                         @Check(value = "GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height)", canBeNull = true)
	                         @Const
	                         @GLbyte
	                         @GLshort
	                         @GLint
	                         @GLfloat
	                         @GLdouble Buffer pixels);

	void glTextureSubImage1DEXT(@GLuint int texture, @GLenum int target, int level,
	                            int xoffset, @GLsizei int width, @GLenum int format, @GLenum int type,
	                            @BufferObject(BufferKind.UnpackPBO)
	                            @Check("GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1)")
	                            @Const
	                            @GLbyte
	                            @GLshort
	                            @GLint
	                            @GLfloat
	                            @GLdouble Buffer pixels);

	void glTextureSubImage2DEXT(@GLuint int texture, @GLenum int target, int level,
	                            int xoffset, int yoffset, @GLsizei int width, @GLsizei int height, @GLenum int format, @GLenum int type,
	                            @BufferObject(BufferKind.UnpackPBO)
	                            @Check("GLChecks.calculateImageStorage(pixels, format, type, width, height, 1)")
	                            @Const
	                            @GLbyte
	                            @GLshort
	                            @GLint
	                            @GLfloat
	                            @GLdouble Buffer pixels);

	void glCopyTextureImage1DEXT(@GLuint int texture, @GLenum int target, int level, @GLenum int internalformat, int x, int y, @GLsizei int width, int border);

	void glCopyTextureImage2DEXT(@GLuint int texture, @GLenum int target, int level, @GLenum int internalformat, int x, int y, @GLsizei int width, @GLsizei int height, int border);

	void glCopyTextureSubImage1DEXT(@GLuint int texture, @GLenum int target, int level, int xoffset, int x, int y, @GLsizei int width);

	void glCopyTextureSubImage2DEXT(@GLuint int texture, @GLenum int target, int level, int xoffset, int yoffset, int x, int y, @GLsizei int width, @GLsizei int height);

	void glGetTextureImageEXT(@GLuint int texture, @GLenum int target, int level,
	                          @GLenum int format, @GLenum int type,
	                          @OutParameter
	                          @BufferObject(BufferKind.PackPBO)
	                          @Check("GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1)")
	                          @GLbyte
	                          @GLshort
	                          @GLint
	                          @GLfloat
	                          @GLdouble Buffer pixels);

	@StripPostfix("params")
	void glGetTextureParameterfvEXT(@GLuint int texture, @GLenum int target, @GLenum int pname, @OutParameter @Check("4") FloatBuffer params);

	@Alternate("glGetTextureParameterfvEXT")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetTextureParameterfvEXT2(@GLuint int texture, @GLenum int target, @GLenum int pname, @OutParameter FloatBuffer params);

	@StripPostfix("params")
	void glGetTextureParameterivEXT(@GLuint int texture, @GLenum int target, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	@Alternate("glGetTextureParameterivEXT")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetTextureParameterivEXT2(@GLuint int texture, @GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	@StripPostfix("params")
	void glGetTextureLevelParameterfvEXT(@GLuint int texture, @GLenum int target, int level, @GLenum int pname, @OutParameter @Check("4") FloatBuffer params);

	@Alternate("glGetTextureLevelParameterfvEXT")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetTextureLevelParameterfvEXT2(@GLuint int texture, @GLenum int target, int level, @GLenum int pname, @OutParameter FloatBuffer params);

	@StripPostfix("params")
	void glGetTextureLevelParameterivEXT(@GLuint int texture, @GLenum int target, int level, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	@Alternate("glGetTextureLevelParameterivEXT")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetTextureLevelParameterivEXT2(@GLuint int texture, @GLenum int target, int level, @GLenum int pname, @OutParameter IntBuffer params);

	/*
	OpenGL 1.2: New 3D texture object commands replace "Tex" in name with
    "Texture" and adds initial "uint texture" parameter
	 */

	@Dependent("OpenGL12")
	void glTextureImage3DEXT(@GLuint int texture, @GLenum int target, int level,
	                         int internalformat, @GLsizei int width, @GLsizei int height, @GLsizei int depth, int border, @GLenum int format, @GLenum int type,
	                         @BufferObject(BufferKind.UnpackPBO)
	                         @Check(value = "GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth)", canBeNull = true)
	                         @Const
	                         @GLbyte
	                         @GLshort
	                         @GLint
	                         @GLfloat
	                         @GLdouble Buffer pixels);

	@Dependent("OpenGL12")
	void glTextureSubImage3DEXT(@GLuint int texture, @GLenum int target, int level,
	                            int xoffset, int yoffset, int zoffset, @GLsizei int width, @GLsizei int height, @GLsizei int depth, @GLenum int format, @GLenum int type,
	                            @BufferObject(BufferKind.UnpackPBO)
	                            @Check("GLChecks.calculateImageStorage(pixels, format, type, width, height, depth)")
	                            @Const
	                            @GLbyte
	                            @GLshort
	                            @GLint
	                            @GLfloat
	                            @GLdouble Buffer pixels);

	@Dependent("OpenGL12")
	void glCopyTextureSubImage3DEXT(@GLuint int texture, @GLenum int target, int level,
	                                int xoffset, int yoffset, int zoffset, int x, int y, @GLsizei int width, @GLsizei int height);

	/*
	OpenGL 1.2.1: New multitexture commands and queries prefix "Multi"
    before "Tex" and add an initial "enum texunit" parameter (to identify
    the texture unit
	 */

	@Dependent("OpenGL13")
	void glBindMultiTextureEXT(@GLenum int texunit, @GLenum int target, @GLuint int texture);

	@Dependent("OpenGL13")
	@DeprecatedGL
	void glMultiTexCoordPointerEXT(@GLenum int texunit, int size, @AutoType("pointer") @GLenum int type, @GLsizei int stride,
	                               @BufferObject(BufferKind.ArrayVBO)
	                               @Check
	                               @Const
	                               @GLfloat
	                               @GLdouble
	                               Buffer pointer);

	@Dependent("OpenGL13")
	@DeprecatedGL
	void glMultiTexEnvfEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, float param);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	@DeprecatedGL
	void glMultiTexEnvfvEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, @Check("4") @Const FloatBuffer params);

	@Dependent("OpenGL13")
	@DeprecatedGL
	void glMultiTexEnviEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, int param);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	@DeprecatedGL
	void glMultiTexEnvivEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, @Check("4") @Const IntBuffer params);

	@Dependent("OpenGL13")
	@DeprecatedGL
	void glMultiTexGendEXT(@GLenum int texunit, @GLenum int coord, @GLenum int pname, double param);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	@DeprecatedGL
	void glMultiTexGendvEXT(@GLenum int texunit, @GLenum int coord, @GLenum int pname, @Check("4") @Const DoubleBuffer params);

	@Dependent("OpenGL13")
	@DeprecatedGL
	void glMultiTexGenfEXT(@GLenum int texunit, @GLenum int coord, @GLenum int pname, float param);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	@DeprecatedGL
	void glMultiTexGenfvEXT(@GLenum int texunit, @GLenum int coord, @GLenum int pname, @Check("4") @Const FloatBuffer params);

	@Dependent("OpenGL13")
	@DeprecatedGL
	void glMultiTexGeniEXT(@GLenum int texunit, @GLenum int coord, @GLenum int pname, int param);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	@DeprecatedGL
	void glMultiTexGenivEXT(@GLenum int texunit, @GLenum int coord, @GLenum int pname, @Check("4") @Const IntBuffer params);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	@DeprecatedGL
	void glGetMultiTexEnvfvEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, @Check("4") @OutParameter FloatBuffer params);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	@DeprecatedGL
	void glGetMultiTexEnvivEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, @Check("4") @OutParameter IntBuffer params);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	@DeprecatedGL
	void glGetMultiTexGendvEXT(@GLenum int texunit, @GLenum int coord, @GLenum int pname, @Check("4") @OutParameter DoubleBuffer params);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	@DeprecatedGL
	void glGetMultiTexGenfvEXT(@GLenum int texunit, @GLenum int coord, @GLenum int pname, @Check("4") @OutParameter FloatBuffer params);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	@DeprecatedGL
	void glGetMultiTexGenivEXT(@GLenum int texunit, @GLenum int coord, @GLenum int pname, @Check("4") @OutParameter IntBuffer params);

	@Dependent("OpenGL13")
	void glMultiTexParameteriEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, int param);

	@Dependent("OpenGL13")
	@StripPostfix("param")
	void glMultiTexParameterivEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, @Check("4") @Const IntBuffer param);

	@Dependent("OpenGL13")
	void glMultiTexParameterfEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, float param);

	@Dependent("OpenGL13")
	@StripPostfix("param")
	void glMultiTexParameterfvEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, @Check("4") @Const FloatBuffer param);

	@Dependent("OpenGL13")
	void glMultiTexImage1DEXT(@GLenum int texunit, @GLenum int target, int level,
	                          int internalformat, @GLsizei int width, int border, @GLenum int format, @GLenum int type,
	                          @BufferObject(BufferKind.UnpackPBO)
	                          @Check(value = "GLChecks.calculateTexImage1DStorage(pixels, format, type, width)", canBeNull = true)
	                          @Const
	                          @GLbyte
	                          @GLshort
	                          @GLint
	                          @GLfloat
	                          @GLdouble Buffer pixels);

	@Dependent("OpenGL13")
	void glMultiTexImage2DEXT(@GLenum int texunit, @GLenum int target, int level,
	                          int internalformat, @GLsizei int width, @GLsizei int height, int border, @GLenum int format, @GLenum int type,
	                          @BufferObject(BufferKind.UnpackPBO)
	                          @Check(value = "GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height)", canBeNull = true)
	                          @Const
	                          @GLbyte
	                          @GLshort
	                          @GLint
	                          @GLfloat
	                          @GLdouble Buffer pixels);

	@Dependent("OpenGL13")
	void glMultiTexSubImage1DEXT(@GLenum int texunit, @GLenum int target, int level,
	                             int xoffset, @GLsizei int width, @GLenum int format, @GLenum int type,
	                             @BufferObject(BufferKind.UnpackPBO)
	                             @Check("GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1)")
	                             @Const
	                             @GLbyte
	                             @GLshort
	                             @GLint
	                             @GLfloat
	                             @GLdouble Buffer pixels);

	@Dependent("OpenGL13")
	void glMultiTexSubImage2DEXT(@GLenum int texunit, @GLenum int target, int level,
	                             int xoffset, int yoffset, @GLsizei int width, @GLsizei int height, @GLenum int format, @GLenum int type,
	                             @BufferObject(BufferKind.UnpackPBO)
	                             @Check("GLChecks.calculateImageStorage(pixels, format, type, width, height, 1)")
	                             @Const
	                             @GLbyte
	                             @GLshort
	                             @GLint
	                             @GLfloat
	                             @GLdouble Buffer pixels);

	@Dependent("OpenGL13")
	void glCopyMultiTexImage1DEXT(@GLenum int texunit, @GLenum int target, int level, @GLenum int internalformat, int x, int y, @GLsizei int width, int border);

	@Dependent("OpenGL13")
	void glCopyMultiTexImage2DEXT(@GLenum int texunit, @GLenum int target, int level, @GLenum int internalformat, int x, int y, @GLsizei int width, @GLsizei int height, int border);

	@Dependent("OpenGL13")
	void glCopyMultiTexSubImage1DEXT(@GLenum int texunit, @GLenum int target, int level, int xoffset, int x, int y, @GLsizei int width);

	@Dependent("OpenGL13")
	void glCopyMultiTexSubImage2DEXT(@GLenum int texunit, @GLenum int target, int level, int xoffset, int yoffset, int x, int y, @GLsizei int width, @GLsizei int height);

	@Dependent("OpenGL13")
	void glGetMultiTexImageEXT(@GLenum int texunit, @GLenum int target, int level, @GLenum int format, @GLenum int type,
	                           @OutParameter
	                           @BufferObject(BufferKind.PackPBO)
	                           @Check("GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1)")
	                           @GLbyte
	                           @GLshort
	                           @GLint
	                           @GLfloat
	                           @GLdouble Buffer pixels);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	void glGetMultiTexParameterfvEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, @Check("4") @OutParameter FloatBuffer params);

	@Alternate("glGetMultiTexParameterfvEXT")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetMultiTexParameterfvEXT2(@GLenum int texunit, @GLenum int target, @GLenum int pname, @OutParameter FloatBuffer params);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	void glGetMultiTexParameterivEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, @Check("4") @OutParameter IntBuffer params);

	@Alternate("glGetMultiTexParameterivEXT")
	@GLreturn("params")
	@Dependent("OpenGL13")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetMultiTexParameterivEXT2(@GLenum int texunit, @GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	void glGetMultiTexLevelParameterfvEXT(@GLenum int texunit, @GLenum int target, int level, @GLenum int pname, @Check("4") @OutParameter FloatBuffer params);

	@Alternate("glGetMultiTexLevelParameterfvEXT")
	@GLreturn("params")
	@Dependent("OpenGL13")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetMultiTexLevelParameterfvEXT2(@GLenum int texunit, @GLenum int target, int level, @GLenum int pname, @OutParameter FloatBuffer params);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	void glGetMultiTexLevelParameterivEXT(@GLenum int texunit, @GLenum int target, int level, @GLenum int pname, @Check("4") @OutParameter IntBuffer params);

	@Alternate("glGetMultiTexLevelParameterivEXT")
	@GLreturn("params")
	@Dependent("OpenGL13")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetMultiTexLevelParameterivEXT2(@GLenum int texunit, @GLenum int target, int level, @GLenum int pname, @OutParameter IntBuffer params);

	@Dependent("OpenGL13")
	void glMultiTexImage3DEXT(@GLenum int texunit, @GLenum int target, int level,
	                          int internalformat, @GLsizei int width, @GLsizei int height, @GLsizei int depth, int border, @GLenum int format, @GLenum int type,
	                          @BufferObject(BufferKind.UnpackPBO)
	                          @Check(value = "GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth)", canBeNull = true)
	                          @Const
	                          @GLbyte
	                          @GLshort
	                          @GLint
	                          @GLfloat
	                          @GLdouble Buffer pixels);

	@Dependent("OpenGL13")
	void glMultiTexSubImage3DEXT(@GLenum int texunit, @GLenum int target, int level,
	                             int xoffset, int yoffset, int zoffset, @GLsizei int width, @GLsizei int height, @GLsizei int depth, @GLenum int format, @GLenum int type,
	                             @BufferObject(BufferKind.UnpackPBO)
	                             @Check("GLChecks.calculateImageStorage(pixels, format, type, width, height, depth)")
	                             @Const
	                             @GLbyte
	                             @GLshort
	                             @GLint
	                             @GLfloat
	                             @GLdouble Buffer pixels);

	@Dependent("OpenGL13")
	void glCopyMultiTexSubImage3DEXT(@GLenum int texunit, @GLenum int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, @GLsizei int width, @GLsizei int height);

	/*
	OpenGL 1.2.1: New indexed texture commands and queries append
    "Indexed" to name and add "uint index" parameter (to identify the
    texture unit index) after state name parameters (if any) and before
    state value parameters
	 */

	@Dependent("OpenGL13")
	@DeprecatedGL
	void glEnableClientStateIndexedEXT(@GLenum int array, @GLuint int index);

	@Dependent("OpenGL13")
	@DeprecatedGL
	void glDisableClientStateIndexedEXT(@GLenum int array, @GLuint int index);

	/*
	OpenGL 3.0: New indexed texture commands and queries append "i"
    to name and add "uint index" parameter (to identify the texture
    unit index) after state name parameters (if any) and before state
    value parameters
	 */

	@Optional(reason = "AMD does not expose this (last driver checked: 14.7)")
	@Dependent("OpenGL30")
	void glEnableClientStateiEXT(@GLenum int array, @GLuint int index);

	@Optional(reason = "AMD does not expose this (last driver checked: 14.7)")
	@Dependent("OpenGL30")
	void glDisableClientStateiEXT(@GLenum int array, @GLuint int index);

	/*
	OpenGL 1.2.1: New indexed generic queries (added for indexed texture
    state) append "Indexed" to name and add "uint index" parameter
    (to identify the texture unit) after state name parameters (if any)
    and before state value parameters
	 */

	@Dependent("OpenGL13")
	@StripPostfix("params")
	void glGetFloatIndexedvEXT(@GLenum int pname, @GLuint int index, @OutParameter @Check("16") FloatBuffer params);

	@Alternate("glGetFloatIndexedvEXT")
	@GLreturn("params")
	@Dependent("OpenGL13")
	@StripPostfix("params")
	void glGetFloatIndexedvEXT2(@GLenum int pname, @GLuint int index, @OutParameter FloatBuffer params);

	@Dependent("OpenGL13")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetDoubleIndexedvEXT(@GLenum int pname, @GLuint int index, @OutParameter @Check("16") DoubleBuffer params);

	@Alternate("glGetDoubleIndexedvEXT")
	@GLreturn("params")
	@Dependent("OpenGL13")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetDoubleIndexedvEXT2(@GLenum int pname, @GLuint int index, @OutParameter DoubleBuffer params);

	@Dependent("OpenGL13")
	@StripPostfix("params")
	void glGetPointerIndexedvEXT(@GLenum int pname, @GLuint int index, @Result @GLvoid ByteBuffer params);

	/*
    OpenGL 3.0: New indexed generic queries (added for indexed texture
    state) replace "v" for "i_v" to name and add "uint index" parameter
    (to identify the texture unit) after state name parameters (if any)
    and before state value parameters
	 */

	@Optional(reason = "AMD does not expose this (last driver checked: 14.7)")
	@Dependent("OpenGL30")
	@StripPostfix("params")
	void glGetFloati_vEXT(@GLenum int pname, @GLuint int index, @OutParameter @Check("16") FloatBuffer params);

	@Alternate("glGetFloati_vEXT")
	@GLreturn("params")
	@Dependent("OpenGL30")
	@StripPostfix("params")
	void glGetFloati_vEXT2(@GLenum int pname, @GLuint int index, @OutParameter FloatBuffer params);

	@Optional(reason = "AMD does not expose this (last driver checked: 14.7)")
	@Dependent("OpenGL30")
	@StripPostfix("params")
	void glGetDoublei_vEXT(@GLenum int pname, @GLuint int index, @OutParameter @Check("16") DoubleBuffer params);

	@Alternate("glGetDoublei_vEXT")
	@GLreturn("params")
	@Dependent("OpenGL30")
	@StripPostfix("params")
	void glGetDoublei_vEXT2(@GLenum int pname, @GLuint int index, @OutParameter DoubleBuffer params);

	@Optional(reason = "AMD does not expose this (last driver checked: 14.7)")
	@Dependent("OpenGL30")
	@StripPostfix(value = "params", postfix = "i_v")
	void glGetPointeri_vEXT(@GLenum int pname, @GLuint int index, @Result @GLvoid ByteBuffer params);

	/*
	OpenGL 1.2.1:  Extend the functionality of these EXT_draw_buffers2
    commands and queries for multitexture
    TODO: Why 1.2.1 and not EXT_draw_buffers2?
	 */

	@Reuse("EXTDrawBuffers2")
	@Dependent("OpenGL13")
	void glEnableIndexedEXT(@GLenum int cap, @GLuint int index);

	@Reuse("EXTDrawBuffers2")
	@Dependent("OpenGL13")
	void glDisableIndexedEXT(@GLenum int cap, @GLuint int index);

	@Reuse("EXTDrawBuffers2")
	@Dependent("OpenGL13")
	boolean glIsEnabledIndexedEXT(@GLenum int cap, @GLuint int index);

	@Reuse("EXTDrawBuffers2")
	@Dependent("OpenGL13")
	@StripPostfix("params")
	void glGetIntegerIndexedvEXT(@GLenum int pname, @GLuint int index, @OutParameter @Check("16") IntBuffer params);

	@Reuse("EXTDrawBuffers2")
	@Alternate("glGetIntegerIndexedvEXT")
	@GLreturn("params")
	@Dependent("OpenGL13")
	@StripPostfix("params")
	void glGetIntegerIndexedvEXT2(@GLenum int pname, @GLuint int index, @OutParameter IntBuffer params);

	@Reuse("EXTDrawBuffers2")
	@Dependent("OpenGL13")
	@StripPostfix("params")
	void glGetBooleanIndexedvEXT(@GLenum int pname, @GLuint int index, @OutParameter @Check("4") @GLboolean ByteBuffer params);

	@Reuse("EXTDrawBuffers2")
	@Alternate("glGetBooleanIndexedvEXT")
	@GLreturn("params")
	@Dependent("OpenGL13")
	@StripPostfix("params")
	void glGetBooleanIndexedvEXT2(@GLenum int pname, @GLuint int index, @OutParameter @GLboolean ByteBuffer params);

	/*
	ARB_vertex_program: New program commands and queries add "Named"
    prefix to name and adds initial "uint program" parameter
	 */

	@Dependent("GL_ARB_vertex_program")
	void glNamedProgramStringEXT(@GLuint int program, @GLenum int target, @GLenum int format, @AutoSize("string") @GLsizei int len, @Const @GLvoid Buffer string);

	@Alternate("glNamedProgramStringEXT")
	@Dependent("GL_ARB_vertex_program")
	void glNamedProgramStringEXT(@GLuint int program, @GLenum int target, @GLenum int format, @Constant("string.length()") @GLsizei int length, CharSequence string);

	@Dependent("GL_ARB_vertex_program")
	void glNamedProgramLocalParameter4dEXT(@GLuint int program, @GLenum int target, @GLuint int index, double x, double y, double z, double w);

	@Dependent("GL_ARB_vertex_program")
	@StripPostfix("params")
	void glNamedProgramLocalParameter4dvEXT(@GLuint int program, @GLenum int target, @GLuint int index, @Const @Check("4") DoubleBuffer params);

	@Dependent("GL_ARB_vertex_program")
	void glNamedProgramLocalParameter4fEXT(@GLuint int program, @GLenum int target, @GLuint int index, float x, float y, float z, float w);

	@Dependent("GL_ARB_vertex_program")
	@StripPostfix("params")
	void glNamedProgramLocalParameter4fvEXT(@GLuint int program, @GLenum int target, @GLuint int index, @Const @Check("4") FloatBuffer params);

	@Dependent("GL_ARB_vertex_program")
	@StripPostfix("params")
	void glGetNamedProgramLocalParameterdvEXT(@GLuint int program, @GLenum int target, @GLuint int index, @OutParameter @Check("4") DoubleBuffer params);

	@Dependent("GL_ARB_vertex_program")
	@StripPostfix("params")
	void glGetNamedProgramLocalParameterfvEXT(@GLuint int program, @GLenum int target, @GLuint int index, @OutParameter @Check("4") FloatBuffer params);

	@Dependent("GL_ARB_vertex_program")
	@StripPostfix("params")
	void glGetNamedProgramivEXT(@GLuint int program, @GLenum int target, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	@Alternate("glGetNamedProgramivEXT")
	@GLreturn("params")
	@Dependent("GL_ARB_vertex_program")
	@StripPostfix("params")
	void glGetNamedProgramivEXT2(@GLuint int program, @GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	@Dependent("GL_ARB_vertex_program")
	void glGetNamedProgramStringEXT(@GLuint int program, @GLenum int target, @GLenum int pname, @OutParameter @Check @GLvoid ByteBuffer string);

	@Alternate("glGetNamedProgramStringEXT")
	@Code("\t\tint programLength = glGetNamedProgramEXT(program, target, ARBProgram.GL_PROGRAM_LENGTH_ARB);")
	@GLreturn(value = "paramString", maxLength = "programLength", forceMaxLength = true)
	void glGetNamedProgramStringEXT2(@GLuint int program, @GLenum int target, @GLenum int pname, @OutParameter @GLchar ByteBuffer paramString);

	/*
	OpenGL 1.3: New compressed texture object commands replace "Tex"
    in name with "Texture" and add initial "uint texture" parameter
	*/

	@Dependent("OpenGL13")
	void glCompressedTextureImage3DEXT(@GLuint int texture, @GLenum int target, int level,
	                                   @GLenum int internalformat, @GLsizei int width, @GLsizei int height, @GLsizei int depth, int border, @AutoSize("data") @GLsizei int imageSize,
	                                   @BufferObject(BufferKind.UnpackPBO)
	                                   @Check
	                                   @Const
	                                   @GLvoid
	                                   ByteBuffer data);

	@Dependent("OpenGL13")
	void glCompressedTextureImage2DEXT(@GLuint int texture, @GLenum int target, int level,
	                                   @GLenum int internalformat, @GLsizei int width, @GLsizei int height, int border, @AutoSize("data") @GLsizei int imageSize,
	                                   @BufferObject(BufferKind.UnpackPBO)
	                                   @Check
	                                   @Const
	                                   @GLvoid
	                                   ByteBuffer data);

	@Dependent("OpenGL13")
	void glCompressedTextureImage1DEXT(@GLuint int texture, @GLenum int target, int level,
	                                   @GLenum int internalformat, @GLsizei int width, int border, @AutoSize("data") @GLsizei int imageSize,
	                                   @BufferObject(BufferKind.UnpackPBO)
	                                   @Check
	                                   @Const
	                                   @GLvoid
	                                   ByteBuffer data);

	@Dependent("OpenGL13")
	void glCompressedTextureSubImage3DEXT(@GLuint int texture, @GLenum int target, int level,
	                                      int xoffset, int yoffset, int zoffset, @GLsizei int width, @GLsizei int height, @GLsizei int depth,
	                                      @GLenum int format, @AutoSize("data") @GLsizei int imageSize,
	                                      @BufferObject(BufferKind.UnpackPBO)
	                                      @Check
	                                      @Const
	                                      @GLvoid
	                                      ByteBuffer data);

	@Dependent("OpenGL13")
	void glCompressedTextureSubImage2DEXT(@GLuint int texture, @GLenum int target, int level,
	                                      int xoffset, int yoffset, @GLsizei int width, @GLsizei int height, @GLenum int format, @AutoSize("data") @GLsizei int imageSize,
	                                      @BufferObject(BufferKind.UnpackPBO)
	                                      @Check
	                                      @Const
	                                      @GLvoid
	                                      ByteBuffer data);

	@Dependent("OpenGL13")
	void glCompressedTextureSubImage1DEXT(@GLuint int texture, @GLenum int target, int level,
	                                      int xoffset, @GLsizei int width, @GLenum int format, @AutoSize("data") @GLsizei int imageSize,
	                                      @BufferObject(BufferKind.UnpackPBO)
	                                      @Check
	                                      @Const
	                                      @GLvoid
	                                      ByteBuffer data);

	@Dependent("OpenGL13")
	void glGetCompressedTextureImageEXT(@GLuint int texture, @GLenum int target, int level,
	                                    @OutParameter
	                                    @BufferObject(BufferKind.PackPBO)
	                                    @Check
	                                    @GLbyte
	                                    @GLshort
	                                    @GLint Buffer img);

	/*
	OpenGL 1.3: New multitexture compressed texture commands and queries
    prefix "Multi" before "Tex" and add an initial "enum texunit"
    parameter (to identify the texture unit)
	 */

	@Dependent("OpenGL13")
	void glCompressedMultiTexImage3DEXT(@GLenum int texunit, @GLenum int target, int level,
	                                    @GLenum int internalformat, @GLsizei int width, @GLsizei int height, @GLsizei int depth, int border, @AutoSize("data") @GLsizei int imageSize,
	                                    @BufferObject(BufferKind.UnpackPBO)
	                                    @Check
	                                    @Const
	                                    @GLvoid
	                                    ByteBuffer data);

	@Dependent("OpenGL13")
	void glCompressedMultiTexImage2DEXT(@GLenum int texunit, @GLenum int target, int level,
	                                    @GLenum int internalformat, @GLsizei int width, @GLsizei int height, int border, @AutoSize("data") @GLsizei int imageSize,
	                                    @BufferObject(BufferKind.UnpackPBO)
	                                    @Check
	                                    @Const
	                                    @GLvoid
	                                    ByteBuffer data);

	@Dependent("OpenGL13")
	void glCompressedMultiTexImage1DEXT(@GLenum int texunit, @GLenum int target, int level,
	                                    @GLenum int internalformat, @GLsizei int width, int border, @AutoSize("data") @GLsizei int imageSize,
	                                    @BufferObject(BufferKind.UnpackPBO)
	                                    @Check
	                                    @Const
	                                    @GLvoid
	                                    ByteBuffer data);

	@Dependent("OpenGL13")
	void glCompressedMultiTexSubImage3DEXT(@GLenum int texunit, @GLenum int target, int level,
	                                       int xoffset, int yoffset, int zoffset, @GLsizei int width, @GLsizei int height, @GLsizei int depth,
	                                       @GLenum int format, @AutoSize("data") @GLsizei int imageSize,
	                                       @BufferObject(BufferKind.UnpackPBO)
	                                       @Check
	                                       @Const
	                                       @GLvoid
	                                       ByteBuffer data);

	@Dependent("OpenGL13")
	void glCompressedMultiTexSubImage2DEXT(@GLenum int texunit, @GLenum int target, int level,
	                                       int xoffset, int yoffset, @GLsizei int width, @GLsizei int height, @GLenum int format, @AutoSize("data") @GLsizei int imageSize,
	                                       @BufferObject(BufferKind.UnpackPBO)
	                                       @Check
	                                       @Const
	                                       @GLvoid
	                                       ByteBuffer data);

	@Dependent("OpenGL13")
	void glCompressedMultiTexSubImage1DEXT(@GLenum int texunit, @GLenum int target, int level,
	                                       int xoffset, @GLsizei int width, @GLenum int format, @AutoSize("data") @GLsizei int imageSize,
	                                       @BufferObject(BufferKind.UnpackPBO)
	                                       @Check
	                                       @Const
	                                       @GLvoid
	                                       ByteBuffer data);


	@Dependent("OpenGL13")
	void glGetCompressedMultiTexImageEXT(@GLenum int texunit, @GLenum int target, int level,
	                                     @OutParameter
	                                     @BufferObject(BufferKind.PackPBO)
	                                     @Check
	                                     @GLbyte
	                                     @GLshort
	                                     @GLint Buffer img);

	/*
	OpenGL 1.3: New transpose matrix commands add "Matrix" suffix
    to name, drops "Matrix" suffix from name, and add initial "enum
    matrixMode" parameter
	 */

	@Dependent("OpenGL13")
	@StripPostfix("m")
	@DeprecatedGL
	void glMatrixLoadTransposefEXT(@GLenum int matrixMode, @Check("16") @Const FloatBuffer m);

	@Dependent("OpenGL13")
	@StripPostfix("m")
	@DeprecatedGL
	void glMatrixLoadTransposedEXT(@GLenum int matrixMode, @Check("16") @Const DoubleBuffer m);

	@Dependent("OpenGL13")
	@StripPostfix("m")
	@DeprecatedGL
	void glMatrixMultTransposefEXT(@GLenum int matrixMode, @Check("16") @Const FloatBuffer m);

	@Dependent("OpenGL13")
	@StripPostfix("m")
	@DeprecatedGL
	void glMatrixMultTransposedEXT(@GLenum int matrixMode, @Check("16") @Const DoubleBuffer m);

	/*
	OpenGL 1.5: New buffer commands and queries replace "Buffer" with
    "NamedBuffer" in name and replace "enum target" parameter with
    "uint buffer"
	 */

	@Dependent("OpenGL15")
	@GenerateAutos
	void glNamedBufferDataEXT(@GLuint int buffer, @AutoSize("data") @GLsizeiptr long size,
	                          @Check
	                          @Const
	                          @GLbyte
	                          @GLshort
	                          @GLint
	                          @GLfloat
	                          @GLdouble Buffer data, @GLenum int usage);

	@Dependent("OpenGL15")
	void glNamedBufferSubDataEXT(@GLuint int buffer, @GLintptr long offset, @AutoSize("data") @GLsizeiptr long size,
	                             @Check
	                             @Const
	                             @GLbyte
	                             @GLshort
	                             @GLint
	                             @GLfloat
	                             @GLdouble Buffer data);

	/**
	 * glMapNamedBufferEXT maps a GL buffer object to a ByteBuffer. The old_buffer argument can be null,
	 * in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 * it will be returned if it points to the same mapped memory and has the same capacity as the buffer object,
	 * otherwise a new ByteBuffer is created. That way, an application will normally use glMapNamedBufferEXT like this:
	 * <p/>
	 * ByteBuffer mapped_buffer; mapped_buffer = glMapNamedBufferEXT(..., ..., null); ... // Another map on the same buffer mapped_buffer = glMapNamedBufferEXT(..., ..., mapped_buffer);
	 * <p/>
	 * Only ByteBuffers returned from this method are to be passed as the old_buffer argument. User-created ByteBuffers cannot be reused.
	 * <p/>
	 * The version of this method without an explicit length argument calls glGetNamedBufferParameterEXT internally to
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
	@Dependent("OpenGL15")
	@CachedResult
	@GLvoid
	@AutoSize("glGetNamedBufferParameterEXT(buffer, GL15.GL_BUFFER_SIZE)")
	ByteBuffer glMapNamedBufferEXT(@GLuint int buffer, @GLenum int access);

	@Dependent("OpenGL15")
	boolean glUnmapNamedBufferEXT(@GLuint int buffer);

	@Dependent("OpenGL15")
	@StripPostfix("params")
	void glGetNamedBufferParameterivEXT(@GLuint int buffer, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	@Alternate("glGetNamedBufferParameterivEXT")
	@GLreturn("params")
	@Dependent("OpenGL15")
	@StripPostfix("params")
	void glGetNamedBufferParameterivEXT2(@GLuint int buffer, @GLenum int pname, @OutParameter IntBuffer params);

	@Dependent("OpenGL15")
	@StripPostfix("params")
	@AutoSize("glGetNamedBufferParameterEXT(buffer, GL15.GL_BUFFER_SIZE)")
	void glGetNamedBufferPointervEXT(@GLuint int buffer, @GLenum int pname, @OutParameter @Result @GLvoid ByteBuffer params);

	@Dependent("OpenGL15")
	void glGetNamedBufferSubDataEXT(@GLuint int buffer, @GLintptr long offset, @AutoSize("data") @GLsizeiptr long size,
	                                @OutParameter
	                                @Check
	                                @GLbyte
	                                @GLshort
	                                @GLint
	                                @GLfloat
	                                @GLdouble Buffer data);

	/*
	OpenGL 2.0: New uniform commands add "Program" prefix to name and
	add initial "uint program" parameter
	 */

	@Dependent("OpenGL20")
	void glProgramUniform1fEXT(@GLuint int program, int location, float v0);

	@Dependent("OpenGL20")
	void glProgramUniform2fEXT(@GLuint int program, int location, float v0, float v1);

	@Dependent("OpenGL20")
	void glProgramUniform3fEXT(@GLuint int program, int location, float v0, float v1, float v2);

	@Dependent("OpenGL20")
	void glProgramUniform4fEXT(@GLuint int program, int location, float v0, float v1, float v2, float v3);

	@Dependent("OpenGL20")
	void glProgramUniform1iEXT(@GLuint int program, int location, int v0);

	@Dependent("OpenGL20")
	void glProgramUniform2iEXT(@GLuint int program, int location, int v0, int v1);

	@Dependent("OpenGL20")
	void glProgramUniform3iEXT(@GLuint int program, int location, int v0, int v1, int v2);

	@Dependent("OpenGL20")
	void glProgramUniform4iEXT(@GLuint int program, int location, int v0, int v1, int v2, int v3);

	@Dependent("OpenGL20")
	@StripPostfix("value")
	void glProgramUniform1fvEXT(@GLuint int program, int location, @AutoSize(value = "value") @GLsizei int count, @Const FloatBuffer value);

	@Dependent("OpenGL20")
	@StripPostfix("value")
	void glProgramUniform2fvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const FloatBuffer value);

	@Dependent("OpenGL20")
	@StripPostfix("value")
	void glProgramUniform3fvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const FloatBuffer value);

	@Dependent("OpenGL20")
	@StripPostfix("value")
	void glProgramUniform4fvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const FloatBuffer value);

	@Dependent("OpenGL20")
	@StripPostfix("value")
	void glProgramUniform1ivEXT(@GLuint int program, int location, @AutoSize(value = "value") @GLsizei int count, @Const IntBuffer value);

	@Dependent("OpenGL20")
	@StripPostfix("value")
	void glProgramUniform2ivEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const IntBuffer value);

	@Dependent("OpenGL20")
	@StripPostfix("value")
	void glProgramUniform3ivEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const IntBuffer value);

	@Dependent("OpenGL20")
	@StripPostfix("value")
	void glProgramUniform4ivEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const IntBuffer value);

	@Dependent("OpenGL20")
	@StripPostfix("value")
	void glProgramUniformMatrix2fvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Dependent("OpenGL20")
	@StripPostfix("value")
	void glProgramUniformMatrix3fvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " / (3 * 3)") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Dependent("OpenGL20")
	@StripPostfix("value")
	void glProgramUniformMatrix4fvEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 4") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	/*
	OpenGL 2.1: New uniform matrix commands add "Program" prefix to
    name and add initial "uint program" parameter
	 */

	@Dependent("OpenGL21")
	@StripPostfix("value")
	void glProgramUniformMatrix2x3fvEXT(@GLuint int program, int location,
	                                    @AutoSize(value = "value", expression = " / (2 * 3)") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Dependent("OpenGL21")
	@StripPostfix("value")
	void glProgramUniformMatrix3x2fvEXT(@GLuint int program, int location,
	                                    @AutoSize(value = "value", expression = " / (3 * 2)") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Dependent("OpenGL21")
	@StripPostfix("value")
	void glProgramUniformMatrix2x4fvEXT(@GLuint int program, int location,
	                                    @AutoSize(value = "value", expression = " >> 3") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Dependent("OpenGL21")
	@StripPostfix("value")
	void glProgramUniformMatrix4x2fvEXT(@GLuint int program, int location,
	                                    @AutoSize(value = "value", expression = " >> 3") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Dependent("OpenGL21")
	@StripPostfix("value")
	void glProgramUniformMatrix3x4fvEXT(@GLuint int program, int location,
	                                    @AutoSize(value = "value", expression = " / (3 * 4)") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	@Dependent("OpenGL21")
	@StripPostfix("value")
	void glProgramUniformMatrix4x3fvEXT(@GLuint int program, int location,
	                                    @AutoSize(value = "value", expression = " / (4 * 3)") @GLsizei int count, boolean transpose, @Const FloatBuffer value);

	/*
	EXT_texture_buffer_object:  New texture buffer object command
    replaces "Tex" in name with "Texture" and adds initial "uint texture"
    parameter
	 */

	@Dependent("GL_EXT_texture_buffer_object")
	void glTextureBufferEXT(@GLuint int texture, @GLenum int target, @GLenum int internalformat, @GLuint int buffer);

	/*
	EXT_texture_buffer_object: New multitexture texture buffer command
    prefixes "Multi" before "Tex" and add an initial "enum texunit"
    parameter (to identify the texture unit)
	 */

	@Dependent("GL_EXT_texture_buffer_object")
	void glMultiTexBufferEXT(@GLenum int texunit, @GLenum int target, @GLenum int internalformat, @GLuint int buffer);

	/*
	EXT_texture_integer: New integer texture object commands and queries
    replace "Tex" in name with "Texture" and add initial "uint texture"
    parameter
	 */

	@Dependent("GL_EXT_texture_integer")
	@StripPostfix("params")
	void glTextureParameterIivEXT(@GLuint int texture, @GLenum int target, @GLenum int pname, @Check("4") @Const IntBuffer params);

	@Alternate("glTextureParameterIivEXT")
	@Dependent("GL_EXT_texture_integer")
	@StripPostfix("param")
	void glTextureParameterIivEXT(@GLuint int texture, @GLenum int target, @GLenum int pname, @Constant(value = "APIUtil.getInt(caps, param)", keepParam = true) int param);

	@Dependent("GL_EXT_texture_integer")
	@StripPostfix("params")
	void glTextureParameterIuivEXT(@GLuint int texture, @GLenum int target, @GLenum int pname, @Check("4") @Const @GLuint IntBuffer params);

	@Alternate("glTextureParameterIuivEXT")
	@Dependent("GL_EXT_texture_integer")
	@StripPostfix("param")
	void glTextureParameterIuivEXT(@GLuint int texture, @GLenum int target, @GLenum int pname, @Constant(value = "APIUtil.getInt(caps, param)", keepParam = true) @GLuint int param);

	@Dependent("GL_EXT_texture_integer")
	@StripPostfix("params")
	void glGetTextureParameterIivEXT(@GLuint int texture, @GLenum int target, @GLenum int pname, @Check("4") @OutParameter IntBuffer params);

	@Alternate("glGetTextureParameterIivEXT")
	@GLreturn("params")
	@Dependent("GL_EXT_texture_integer")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetTextureParameterIivEXT2(@GLuint int texture, @GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	@Dependent("GL_EXT_texture_integer")
	@StripPostfix("params")
	void glGetTextureParameterIuivEXT(@GLuint int texture, @GLenum int target, @GLenum int pname, @Check("4") @OutParameter @GLuint IntBuffer params);

	@Alternate("glGetTextureParameterIuivEXT")
	@GLreturn("params")
	@Dependent("GL_EXT_texture_integer")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetTextureParameterIuivEXT2(@GLuint int texture, @GLenum int target, @GLenum int pname, @OutParameter @GLuint IntBuffer params);

	/*
	EXT_texture_integer: New multitexture integer texture commands and
    queries prefix "Multi" before "Tex" and add an initial "enum texunit"
    parameter (to identify the texture unit)
	 */

	@Dependent("GL_EXT_texture_integer")
	@StripPostfix("params")
	void glMultiTexParameterIivEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, @Check("4") @Const IntBuffer params);

	@Alternate("glMultiTexParameterIivEXT")
	@Dependent("GL_EXT_texture_integer")
	@StripPostfix("param")
	void glMultiTexParameterIivEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, @Constant(value = "APIUtil.getInt(caps, param)", keepParam = true) int param);

	@Dependent("GL_EXT_texture_integer")
	@StripPostfix("params")
	void glMultiTexParameterIuivEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, @Check("4") @Const @GLuint IntBuffer params);

	@Alternate("glMultiTexParameterIuivEXT")
	@Dependent("GL_EXT_texture_integer")
	@StripPostfix("param")
	void glMultiTexParameterIuivEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, @Constant(value = "APIUtil.getInt(caps, param)", keepParam = true) int param);

	@Dependent("GL_EXT_texture_integer")
	@StripPostfix("params")
	void glGetMultiTexParameterIivEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, @Check("4") @OutParameter IntBuffer params);

	@Alternate("glGetMultiTexParameterIivEXT")
	@GLreturn("params")
	@Dependent("GL_EXT_texture_integer")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetMultiTexParameterIivEXT2(@GLenum int texunit, @GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	@Dependent("GL_EXT_texture_integer")
	@StripPostfix("params")
	void glGetMultiTexParameterIuivEXT(@GLenum int texunit, @GLenum int target, @GLenum int pname, @Check("4") @OutParameter @GLuint IntBuffer params);

	@Alternate("glGetMultiTexParameterIuivEXT")
	@GLreturn("params")
	@Dependent("GL_EXT_texture_integer")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetMultiTexParameterIuivEXT2(@GLenum int texunit, @GLenum int target, @GLenum int pname, @OutParameter @GLuint IntBuffer params);

	/*
	EXT_gpu_shader4: New integer uniform commands add "Program" prefix
    to name and add initial "uint program" parameter
	 */

	@Dependent("GL_EXT_gpu_shader4")
	void glProgramUniform1uiEXT(@GLuint int program, int location, @GLuint int v0);

	@Dependent("GL_EXT_gpu_shader4")
	void glProgramUniform2uiEXT(@GLuint int program, int location, @GLuint int v0, @GLuint int v1);

	@Dependent("GL_EXT_gpu_shader4")
	void glProgramUniform3uiEXT(@GLuint int program, int location, @GLuint int v0, @GLuint int v1, @GLuint int v2);

	@Dependent("GL_EXT_gpu_shader4")
	void glProgramUniform4uiEXT(@GLuint int program, int location, @GLuint int v0, @GLuint int v1, @GLuint int v2, @GLuint int v3);

	@Dependent("GL_EXT_gpu_shader4")
	@StripPostfix("value")
	void glProgramUniform1uivEXT(@GLuint int program, int location, @AutoSize(value = "value") @GLsizei int count, @Const @GLuint IntBuffer value);

	@Dependent("GL_EXT_gpu_shader4")
	@StripPostfix("value")
	void glProgramUniform2uivEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const @GLuint IntBuffer value);

	@Dependent("GL_EXT_gpu_shader4")
	@StripPostfix("value")
	void glProgramUniform3uivEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const @GLuint IntBuffer value);

	@Dependent("GL_EXT_gpu_shader4")
	@StripPostfix("value")
	void glProgramUniform4uivEXT(@GLuint int program, int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const @GLuint IntBuffer value);

	/*
	EXT_gpu_program_parameters: New program command adds "Named" prefix
    to name and adds "uint program" parameter
	 */

	@Dependent("GL_EXT_gpu_program_parameters")
	@StripPostfix("params")
	void glNamedProgramLocalParameters4fvEXT(@GLuint int program, @GLenum int target, @GLuint int index,
	                                         @AutoSize(value = "params", expression = " >> 2") @GLsizei int count, @Const FloatBuffer params);

	/*
	NV_gpu_program4: New program commands and queries add "Named"
    prefix to name and replace "enum target" with "uint program"
	 */

	@Dependent("GL_NV_gpu_program4")
	void glNamedProgramLocalParameterI4iEXT(@GLuint int program, @GLenum int target, @GLuint int index, int x, int y, int z, int w);

	@Dependent("GL_NV_gpu_program4")
	@StripPostfix("params")
	void glNamedProgramLocalParameterI4ivEXT(@GLuint int program, @GLenum int target, @GLuint int index, @Check("4") @Const IntBuffer params);

	@Dependent("GL_NV_gpu_program4")
	@StripPostfix("params")
	void glNamedProgramLocalParametersI4ivEXT(@GLuint int program, @GLenum int target, @GLuint int index,
	                                          @AutoSize(value = "params", expression = " >> 2") @GLsizei int count, @Const IntBuffer params);

	@Dependent("GL_NV_gpu_program4")
	void glNamedProgramLocalParameterI4uiEXT(@GLuint int program, @GLenum int target, @GLuint int index, @GLuint int x, @GLuint int y, @GLuint int z, @GLuint int w);

	@Dependent("GL_NV_gpu_program4")
	@StripPostfix("params")
	void glNamedProgramLocalParameterI4uivEXT(@GLuint int program, @GLenum int target, @GLuint int index, @Check("4") @Const @GLuint IntBuffer params);

	@Dependent("GL_NV_gpu_program4")
	@StripPostfix("params")
	void glNamedProgramLocalParametersI4uivEXT(@GLuint int program, @GLenum int target, @GLuint int index,
	                                           @AutoSize(value = "params", expression = " >> 2") @GLsizei int count, @Const @GLuint IntBuffer params);

	@Dependent("GL_NV_gpu_program4")
	@StripPostfix("params")
	void glGetNamedProgramLocalParameterIivEXT(@GLuint int program, @GLenum int target, @GLuint int index, @Check("4") @OutParameter IntBuffer params);

	@Dependent("GL_NV_gpu_program4")
	@StripPostfix("params")
	void glGetNamedProgramLocalParameterIuivEXT(@GLuint int program, @GLenum int target, @GLuint int index, @Check("4") @OutParameter @GLuint IntBuffer params);

	/*
	OpenGL 3.0: New renderbuffer commands add "Named" prefix to name
    and replace "enum target" with "uint renderbuffer"
	 */

	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	void glNamedRenderbufferStorageEXT(@GLuint int renderbuffer, @GLenum int internalformat, @GLsizei int width, @GLsizei int height);

	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	@StripPostfix("params")
	void glGetNamedRenderbufferParameterivEXT(@GLuint int renderbuffer, @GLenum int pname, @Check("4") @OutParameter IntBuffer params);

	@Alternate("glGetNamedRenderbufferParameterivEXT")
	@GLreturn("params")
	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	@StripPostfix("params")
	void glGetNamedRenderbufferParameterivEXT2(@GLuint int renderbuffer, @GLenum int pname, @OutParameter IntBuffer params);

	/*
	EXT_framebuffer_multisample: New renderbuffer commands add "Named"
    prefix to name and replace "enum target" with "uint renderbuffer"
	 */

	@Dependent("OpenGL30,GL_EXT_framebuffer_multisample")
	void glNamedRenderbufferStorageMultisampleEXT(@GLuint int renderbuffer, @GLsizei int samples, @GLenum int internalformat, @GLsizei int width, @GLsizei int height);

	/*
	NV_framebuffer_multisample_coverage: New renderbuffer commands
    add "Named" prefix to name and replace "enum target" with "uint
    renderbuffer"
	 */

	@Dependent("GL_NV_framebuffer_multisample_coverage")
	void glNamedRenderbufferStorageMultisampleCoverageEXT(@GLuint int renderbuffer, @GLsizei int coverageSamples, @GLsizei int colorSamples, @GLenum int internalformat, @GLsizei int width, @GLsizei int height);

	/*
	OpenGL 3.0: New framebuffer commands add "Named" prefix to name
    and replace "enum target" with "uint framebuffer"
	 */

	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	@GLenum
	int glCheckNamedFramebufferStatusEXT(@GLuint int framebuffer, @GLenum int target);

	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	void glNamedFramebufferTexture1DEXT(@GLuint int framebuffer, @GLenum int attachment, @GLenum int textarget, @GLuint int texture, int level);

	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	void glNamedFramebufferTexture2DEXT(@GLuint int framebuffer, @GLenum int attachment, @GLenum int textarget, @GLuint int texture, int level);

	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	void glNamedFramebufferTexture3DEXT(@GLuint int framebuffer, @GLenum int attachment, @GLenum int textarget, @GLuint int texture, int level, int zoffset);

	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	void glNamedFramebufferRenderbufferEXT(@GLuint int framebuffer, @GLenum int attachment, @GLenum int renderbuffertarget, @GLuint int renderbuffer);

	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	@StripPostfix("params")
	void glGetNamedFramebufferAttachmentParameterivEXT(@GLuint int framebuffer, @GLenum int attachment, @GLenum int pname, @Check("4") @OutParameter IntBuffer params);

	@Alternate("glGetNamedFramebufferAttachmentParameterivEXT")
	@GLreturn("params")
	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	@StripPostfix("params")
	void glGetNamedFramebufferAttachmentParameterivEXT2(@GLuint int framebuffer, @GLenum int attachment, @GLenum int pname, @OutParameter IntBuffer params);

	/*
	    OpenGL 3.0: New texture commands add "Texture" within name and
        replace "enum target" with "uint texture"
	     */

	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	void glGenerateTextureMipmapEXT(@GLuint int texture, @GLenum int target);

	/*
	OpenGL 3.0: New texture commands add "MultiTex" within name and
    replace "enum target" with "enum texunit"
	 */

	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	void glGenerateMultiTexMipmapEXT(@GLenum int texunit, @GLenum int target);

	/*
	OpenGL 3.0: New framebuffer commands
	 */

	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	void glFramebufferDrawBufferEXT(@GLuint int framebuffer, @GLenum int mode);

	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	void glFramebufferDrawBuffersEXT(@GLuint int framebuffer, @AutoSize("bufs") @GLsizei int n, @Const @GLenum IntBuffer bufs);

	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	void glFramebufferReadBufferEXT(@GLuint int framebuffer, @GLenum int mode);

	/*
	OpenGL 3.0: New framebuffer query
	 */

	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	@StripPostfix("param")
	void glGetFramebufferParameterivEXT(@GLuint int framebuffer, @GLenum int pname, @Check("4") @OutParameter IntBuffer param);

	@Alternate("glGetFramebufferParameterivEXT")
	@GLreturn("param")
	@Dependent("OpenGL30,GL_EXT_framebuffer_object")
	@StripPostfix("param")
	void glGetFramebufferParameterivEXT2(@GLuint int framebuffer, @GLenum int pname, @OutParameter IntBuffer param);

	/*
	OpenGL 3.1: New buffer data copy command
	 */

	@Dependent("OpenGL31,GL_ARB_copy_buffer")
	void glNamedCopyBufferSubDataEXT(@GLuint int readBuffer, @GLuint int writeBuffer, @GLintptr long readoffset, @GLintptr long writeoffset, @GLsizeiptr long size);

	/*
	EXT_geometry_shader4 or NV_geometry_program4: New framebuffer commands
    add "Named" prefix to name and replace "enum target" with "uint
    framebuffer"
	 */

	@Dependent("GL_EXT_geometry_shader4,GL_NV_geometry_program4")
	void glNamedFramebufferTextureEXT(@GLuint int framebuffer, @GLenum int attachment, @GLuint int texture, int level);

	@Dependent("GL_EXT_geometry_shader4,GL_NV_geometry_program4")
	void glNamedFramebufferTextureLayerEXT(@GLuint int framebuffer, @GLenum int attachment, @GLuint int texture, int level, int layer);

	@Dependent("GL_EXT_geometry_shader4,GL_NV_geometry_program4")
	void glNamedFramebufferTextureFaceEXT(@GLuint int framebuffer, @GLenum int attachment, @GLuint int texture, int level, @GLenum int face);

	/*
	NV_explicit_multisample:  New texture renderbuffer object command
    replaces "Tex" in name with "Texture" and add initial "uint texture"
    parameter
	 */

	@Dependent("GL_NV_explicit_multisample")
	void glTextureRenderbufferEXT(@GLuint int texture, @GLenum int target, @GLuint int renderbuffer);

	/*
	NV_explicit_multisample: New multitexture texture renderbuffer command
    prefixes "Multi" before "Tex" and add an initial "enum texunit"
    parameter (to identify the texture unit)
	 */

	@Dependent("GL_NV_explicit_multisample")
	void glMultiTexRenderbufferEXT(@GLenum int texunit, @GLenum int target, @GLuint int renderbuffer);

	/*
	OpenGL 3.0: New vertex array specification commands for vertex
	array objects prefix "VertexArray", add initial "uint vaobj" and
	"uint buffer" parameters, change "Pointer" suffix to "Offset",
	and change the final parameter from "const void *" to "intptr offset"
	 */

	@Dependent("OpenGL30")
	@DeprecatedGL
	void glVertexArrayVertexOffsetEXT(@GLuint int vaobj, @GLuint int buffer, int size, @GLenum int type, @GLsizei int stride, @GLintptr long offset);

	@Dependent("OpenGL30")
	@DeprecatedGL
	void glVertexArrayColorOffsetEXT(@GLuint int vaobj, @GLuint int buffer, int size, @GLenum int type, @GLsizei int stride, @GLintptr long offset);

	@Dependent("OpenGL30")
	@DeprecatedGL
	void glVertexArrayEdgeFlagOffsetEXT(@GLuint int vaobj, @GLuint int buffer, @GLsizei int stride, @GLintptr long offset);

	@Dependent("OpenGL30")
	void glVertexArrayIndexOffsetEXT(@GLuint int vaobj, @GLuint int buffer, @GLenum int type, @GLsizei int stride, @GLintptr long offset);

	@Dependent("OpenGL30")
	@DeprecatedGL
	void glVertexArrayNormalOffsetEXT(@GLuint int vaobj, @GLuint int buffer, @GLenum int type, @GLsizei int stride, @GLintptr long offset);

	@Dependent("OpenGL30")
	@DeprecatedGL
	void glVertexArrayTexCoordOffsetEXT(@GLuint int vaobj, @GLuint int buffer, int size, @GLenum int type, @GLsizei int stride, @GLintptr long offset);

	@Dependent("OpenGL30")
	@DeprecatedGL
	void glVertexArrayMultiTexCoordOffsetEXT(@GLuint int vaobj, @GLuint int buffer, @GLenum int texunit, int size, @GLenum int type, @GLsizei int stride, @GLintptr long offset);

	@Dependent("OpenGL30")
	@DeprecatedGL
	void glVertexArrayFogCoordOffsetEXT(@GLuint int vaobj, @GLuint int buffer, @GLenum int type, @GLsizei int stride, @GLintptr long offset);

	@Dependent("OpenGL30")
	@DeprecatedGL
	void glVertexArraySecondaryColorOffsetEXT(@GLuint int vaobj, @GLuint int buffer, int size, @GLenum int type, @GLsizei int stride, @GLintptr long offset);

	@Dependent("OpenGL30")
	void glVertexArrayVertexAttribOffsetEXT(@GLuint int vaobj, @GLuint int buffer, @GLuint int index, int size, @GLenum int type, boolean normalized, @GLsizei int stride, @GLintptr long offset);

	@Dependent("OpenGL30")
	void glVertexArrayVertexAttribIOffsetEXT(@GLuint int vaobj, @GLuint int buffer, @GLuint int index, int size, @GLenum int type, @GLsizei int stride, @GLintptr long offset);

	/*
	OpenGL 3.0: New vertex array enable commands for vertex array
	objects change "ClientState" to "VertexArray" and add an initial
	"uint vaobj" parameter
	 */

	@Dependent("OpenGL30")
	void glEnableVertexArrayEXT(@GLuint int vaobj, @GLenum int array);

	@Dependent("OpenGL30")
	void glDisableVertexArrayEXT(@GLuint int vaobj, @GLenum int array);

	/*
	OpenGL 3.0: New vertex attrib array enable commands for vertex
	array objects change "VertexAttribArray" to "VertexArrayAttrib"
	and add an initial "uint vaobj" parameter
	 */

	@Dependent("OpenGL30")
	void glEnableVertexArrayAttribEXT(@GLuint int vaobj, @GLuint int index);

	@Dependent("OpenGL30")
	void glDisableVertexArrayAttribEXT(@GLuint int vaobj, @GLuint int index);

	/*
	OpenGL 3.0: New queries for vertex array objects
	 */

	@Dependent("OpenGL30")
	@StripPostfix("param")
	void glGetVertexArrayIntegervEXT(@GLuint int vaobj, @GLenum int pname, @OutParameter @Check("16") IntBuffer param);

	@Alternate("glGetVertexArrayIntegervEXT")
	@GLreturn("param")
	@Dependent("OpenGL30")
	@StripPostfix("param")
	void glGetVertexArrayIntegervEXT2(@GLuint int vaobj, @GLenum int pname, @OutParameter IntBuffer param);

	@Dependent("OpenGL30")
	@StripPostfix("param")
	void glGetVertexArrayPointervEXT(@GLuint int vaobj, @GLenum int pname, @Result @GLvoid ByteBuffer param);

	@Dependent("OpenGL30")
	@StripPostfix(value = "param")
	void glGetVertexArrayIntegeri_vEXT(@GLuint int vaobj, @GLuint int index, @GLenum int pname, @OutParameter @Check("16") IntBuffer param);

	@Alternate("glGetVertexArrayIntegeri_vEXT")
	@GLreturn("param")
	@Dependent("OpenGL30")
	@StripPostfix(value = "param", postfix = "_v")
	void glGetVertexArrayIntegeri_vEXT2(@GLuint int vaobj, @GLuint int index, @GLenum int pname, @OutParameter IntBuffer param);

	@Dependent("OpenGL30")
	@StripPostfix(value = "param", postfix = "i_v")
	void glGetVertexArrayPointeri_vEXT(@GLuint int vaobj, @GLuint int index, @GLenum int pname, @Result @GLvoid ByteBuffer param);

	/*
	OpenGL 3.0: New buffer commands replace "Buffer" with "NamedBuffer"
	in name and replace "enum target" parameter with "uint buffer"
	 */

	/**
	 * glMapNamedBufferRangeEXT maps a GL buffer object range to a ByteBuffer. The old_buffer argument can be null,
	 * in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 * it will be returned if it points to the same mapped memory and has the same capacity as the buffer object,
	 * otherwise a new ByteBuffer is created. That way, an application will normally use glMapNamedBufferRangeEXT like this:
	 * <p/>
	 * ByteBuffer mapped_buffer; mapped_buffer = glMapNamedBufferRangeEXT(..., ..., ..., ..., null); ... // Another map on the same buffer mapped_buffer = glMapNamedBufferRangeEXT(..., ..., ..., ..., mapped_buffer);
	 * <p/>
	 * Only ByteBuffers returned from this method are to be passed as the old_buffer argument. User-created ByteBuffers cannot be reused.
	 *
	 * @param old_buffer A ByteBuffer. If this argument points to the same address and has the same capacity as the new mapping, it will be returned and no new buffer will be created.
	 *
	 * @return A ByteBuffer representing the mapped buffer memory.
	 */
	@Dependent("OpenGL30")
	@CachedResult(isRange = true)
	@GLvoid
	@AutoSize("length")
	ByteBuffer glMapNamedBufferRangeEXT(@GLuint int buffer, @GLintptr long offset, @GLsizeiptr long length, @GLbitfield int access);

	@Dependent("OpenGL30")
	void glFlushMappedNamedBufferRangeEXT(@GLuint int buffer, @GLintptr long offset, @GLsizeiptr long length);

}