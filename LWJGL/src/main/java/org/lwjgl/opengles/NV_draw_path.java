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

import org.lwjgl.util.generator.AutoSize;
import org.lwjgl.util.generator.Check;
import org.lwjgl.util.generator.Const;
import org.lwjgl.util.generator.opengl.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface NV_draw_path {

	/** Accepted as the &lt;paramType&gt; parameter of PathParameterNV: */
	int GL_PATH_QUALITY_NV       = 0x8ED8,
		GL_FILL_RULE_NV          = 0x8ED9,
		GL_STROKE_CAP0_STYLE_NV  = 0x8EE0,
		GL_STROKE_CAP1_STYLE_NV  = 0x8EE1,
		GL_STROKE_CAP2_STYLE_NV  = 0x8EE2,
		GL_STROKE_CAP3_STYLE_NV  = 0x8EE3,
		GL_STROKE_JOIN_STYLE_NV  = 0x8EE8,
		GL_STROKE_MITER_LIMIT_NV = 0x8EE9;

	/** Values for the ILL_RULE_NV path parameter: */
	int GL_EVEN_ODD_NV = 0x8EF0,
		GL_NON_ZERO_NV = 0x8EF1;

	/** Values for the CAP[0-3]_STYLE_NV path parameter: */
	int GL_CAP_BUTT_NV     = 0x8EF4,
		GL_CAP_ROUND_NV    = 0x8EF5,
		GL_CAP_SQUARE_NV   = 0x8EF6,
		GL_CAP_TRIANGLE_NV = 0x8EF7;

	/** Values for the JOIN_STYLE_NV path parameter: */
	int GL_JOIN_MITER_NV         = 0x8EFC,
		GL_JOIN_ROUND_NV         = 0x8EFD,
		GL_JOIN_BEVEL_NV         = 0x8EFE,
		GL_JOIN_CLIPPED_MITER_NV = 0x8EFF;

	/** Accepted as the &lt;target&gt; parameter of PathMatrixNV: */
	int GL_MATRIX_PATH_TO_CLIP_NV   = 0x8F04,
		GL_MATRIX_STROKE_TO_PATH_NV = 0x8F05,
		GL_MATRIX_PATH_COORD0_NV    = 0x8F08,
		GL_MATRIX_PATH_COORD1_NV    = 0x8F09,
		GL_MATRIX_PATH_COORD2_NV    = 0x8F0A,
		GL_MATRIX_PATH_COORD3_NV    = 0x8F0B;

	/** Accepted as the &lt;mode&gt; parameter of DrawPathbufferNV: */
	int GL_FILL_PATH_NV   = 0x8F18,
		GL_STROKE_PATH_NV = 0x8F19;

	/** Accepted as path commands by CreatePathNV: */
	byte GL_MOVE_TO_NV            = 0x00,
		GL_LINE_TO_NV             = 0x01,
		GL_QUADRATIC_BEZIER_TO_NV = 0x02,
		GL_CUBIC_BEZIER_TO_NV     = 0x03,
		GL_START_MARKER_NV        = 0x20,
		GL_CLOSE_NV               = 0x21,
		GL_STROKE_CAP0_NV         = 0x40,
		GL_STROKE_CAP1_NV         = 0x41,
		GL_STROKE_CAP2_NV         = 0x42,
		GL_STROKE_CAP3_NV         = 0x43;

	@GLuint
	int glCreatePathNV(@GLenum int datatype, @AutoSize("commands") @GLsizei int numCommands, @Const @GLubyte ByteBuffer commands);

	void glDeletePathNV(@GLuint int path);

	void glPathVerticesNV(@GLuint int path, @Const @Check @GLvoid ByteBuffer vertices);

	void glPathParameterfNV(@GLuint int path, @GLenum int paramType, float param);

	void glPathParameteriNV(@GLuint int path, @GLenum int paramType, int param);

	@GLuint
	int glCreatePathProgramNV();

	void glPathMatrixNV(@GLenum int target, @Const @Check("16") FloatBuffer value);

	void glDrawPathNV(@GLuint int path, @GLenum int mode);

	@GLuint
	int glCreatePathbufferNV(@GLsizei int capacity);

	void glDeletePathbufferNV(@GLuint int buffer);

	void glPathbufferPathNV(@GLuint int buffer, int index, @GLuint int path);

	void glPathbufferPositionNV(@GLuint int buffer, int index, float x, float y);

	void glDrawPathbufferNV(@GLuint int buffer, @GLenum int mode);

}