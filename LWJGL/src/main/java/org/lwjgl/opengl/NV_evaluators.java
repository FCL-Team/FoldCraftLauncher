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
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLfloat;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.*;

public interface NV_evaluators {
	int GL_EVAL_2D_NV = 0x86C0;
	int GL_EVAL_TRIANGULAR_2D_NV = 0x86C1;
	int GL_MAP_TESSELLATION_NV = 0x86C2;
	int GL_MAP_ATTRIB_U_ORDER_NV = 0x86C3;
	int GL_MAP_ATTRIB_V_ORDER_NV = 0x86C4;
	int GL_EVAL_FRACTIONAL_TESSELLATION_NV = 0x86C5;
	int GL_EVAL_VERTEX_ATTRIB0_NV = 0x86C6;
	int GL_EVAL_VERTEX_ATTRIB1_NV = 0x86C7;
	int GL_EVAL_VERTEX_ATTRIB2_NV = 0x86C8;
	int GL_EVAL_VERTEX_ATTRIB3_NV = 0x86C9;
	int GL_EVAL_VERTEX_ATTRIB4_NV = 0x86CA;
	int GL_EVAL_VERTEX_ATTRIB5_NV = 0x86CB;
	int GL_EVAL_VERTEX_ATTRIB6_NV = 0x86CC;
	int GL_EVAL_VERTEX_ATTRIB7_NV = 0x86CD;
	int GL_EVAL_VERTEX_ATTRIB8_NV = 0x86CE;
	int GL_EVAL_VERTEX_ATTRIB9_NV = 0x86CF;
	int GL_EVAL_VERTEX_ATTRIB10_NV = 0x86D0;
	int GL_EVAL_VERTEX_ATTRIB11_NV = 0x86D1;
	int GL_EVAL_VERTEX_ATTRIB12_NV = 0x86D2;
	int GL_EVAL_VERTEX_ATTRIB13_NV = 0x86D3;
	int GL_EVAL_VERTEX_ATTRIB14_NV = 0x86D4;
	int GL_EVAL_VERTEX_ATTRIB15_NV = 0x86D5;
	int GL_MAX_MAP_TESSELLATION_NV = 0x86D6;
	int GL_MAX_RATIONAL_EVAL_ORDER_NV = 0x86D7;

	void glGetMapControlPointsNV(@GLenum int target, @GLuint int index, @GLenum int type, @GLsizei int ustride, @GLsizei int vstride, boolean packed,
			                     @OutParameter
	                             @Check
	                             @Const
	                             @GLfloat Buffer pPoints);

	void glMapControlPointsNV(@GLenum int target, @GLuint int index, @GLenum int type, @GLsizei int ustride, @GLsizei int vstride, int uorder, int vorder, boolean packed, @Check @Const @GLfloat Buffer pPoints);

	@StripPostfix("params")
	void glMapParameterfvNV(@GLenum int target, @GLenum int pname, @Check("4") @Const FloatBuffer params);

	@StripPostfix("params")
	void glMapParameterivNV(@GLenum int target, @GLenum int pname, @Check("4") @Const IntBuffer params);

	@StripPostfix("params")
	void glGetMapParameterfvNV(@GLenum int target, @GLenum int pname, @OutParameter @Check("4") @Const FloatBuffer params);

	@StripPostfix("params")
	void glGetMapParameterivNV(@GLenum int target, @GLenum int pname, @OutParameter @Check("4") @Const IntBuffer params);

	@StripPostfix("params")
	void glGetMapAttribParameterfvNV(@GLenum int target, @GLuint int index, @GLenum int pname, @OutParameter @Check("4") FloatBuffer params);

	@StripPostfix("params")
	void glGetMapAttribParameterivNV(@GLenum int target, @GLuint int index, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	void glEvalMapsNV(@GLenum int target, @GLenum int mode);
}
