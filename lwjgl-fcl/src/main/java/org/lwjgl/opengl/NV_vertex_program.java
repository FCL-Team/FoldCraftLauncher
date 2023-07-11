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

public interface NV_vertex_program extends NV_program {

	/**
	 Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled,
	 and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 and GetDoublev, and by the &lt;target&gt; parameter of BindProgramNV,
	 ExecuteProgramNV, GetProgramParameter[df]vNV, GetTrackMatrixivNV,
	 LoadProgramNV, ProgramParameter[s]4[df][v]NV, and TrackMatrixNV:
	 */
	int GL_VERTEX_PROGRAM_NV = 0x8620;

	/**
	 Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled,
	 and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 and GetDoublev:
	 */
	int GL_VERTEX_PROGRAM_POINT_SIZE_NV = 0x8642;
	int GL_VERTEX_PROGRAM_TWO_SIDE_NV = 0x8643;

	/**
	 Accepted by the &lt;target&gt; parameter of ExecuteProgramNV and
	 LoadProgramNV:
	 */
	int GL_VERTEX_STATE_PROGRAM_NV = 0x8621;

	/**
	 Accepted by the &lt;pname&gt; parameter of GetVertexAttrib[dfi]vNV:
	 */
	int GL_ATTRIB_ARRAY_SIZE_NV = 0x8623;
	int GL_ATTRIB_ARRAY_STRIDE_NV = 0x8624;
	int GL_ATTRIB_ARRAY_TYPE_NV = 0x8625;
	int GL_CURRENT_ATTRIB_NV = 0x8626;

	/**
	 Accepted by the &lt;pname&gt; parameter of GetProgramParameterfvNV
	 and GetProgramParameterdvNV:
	 */
	int GL_PROGRAM_PARAMETER_NV = 0x8644;

	/**
	 Accepted by the &lt;pname&gt; parameter of GetVertexAttribPointervNV:
	 */
	int GL_ATTRIB_ARRAY_POINTER_NV = 0x8645;

	/**
	 Accepted by the &lt;pname&gt; parameter of GetTrackMatrixivNV:
	 */
	int GL_TRACK_MATRIX_NV = 0x8648;
	int GL_TRACK_MATRIX_TRANSFORM_NV = 0x8649;

	/**
	 Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 GetFloatv, and GetDoublev:
	 */
	int GL_MAX_TRACK_MATRIX_STACK_DEPTH_NV = 0x862E;
	int GL_MAX_TRACK_MATRICES_NV = 0x862F;
	int GL_CURRENT_MATRIX_STACK_DEPTH_NV = 0x8640;
	int GL_CURRENT_MATRIX_NV = 0x8641;
	int GL_VERTEX_PROGRAM_BINDING_NV = 0x864A;

	/**
	 Accepted by the &lt;matrix&gt; parameter of TrackMatrixNV:
	 */
	int GL_MODELVIEW_PROJECTION_NV = 0x8629;

	/**
	 Accepted by the &lt;matrix&gt; parameter of TrackMatrixNV and by the
	 &lt;mode&gt; parameter of MatrixMode:
	 */
	int GL_MATRIX0_NV = 0x8630;
	int GL_MATRIX1_NV = 0x8631;
	int GL_MATRIX2_NV = 0x8632;
	int GL_MATRIX3_NV = 0x8633;
	int GL_MATRIX4_NV = 0x8634;
	int GL_MATRIX5_NV = 0x8635;
	int GL_MATRIX6_NV = 0x8636;
	int GL_MATRIX7_NV = 0x8637;

	/**
	 Accepted by the &lt;transform&gt; parameter of TrackMatrixNV:
	 */
	int GL_IDENTITY_NV = 0x862A;
	int GL_INVERSE_NV = 0x862B;
	int GL_TRANSPOSE_NV = 0x862C;
	int GL_INVERSE_TRANSPOSE_NV = 0x862D;

	/**
	 Accepted by the &lt;array&gt; parameter of EnableClientState and
	 DisableClientState, by the &lt;cap&gt; parameter of IsEnabled, and by
	 the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 GetDoublev:
	 */
	int GL_VERTEX_ATTRIB_ARRAY0_NV = 0x8650;
	int GL_VERTEX_ATTRIB_ARRAY1_NV = 0x8651;
	int GL_VERTEX_ATTRIB_ARRAY2_NV = 0x8652;
	int GL_VERTEX_ATTRIB_ARRAY3_NV = 0x8653;
	int GL_VERTEX_ATTRIB_ARRAY4_NV = 0x8654;
	int GL_VERTEX_ATTRIB_ARRAY5_NV = 0x8655;
	int GL_VERTEX_ATTRIB_ARRAY6_NV = 0x8656;
	int GL_VERTEX_ATTRIB_ARRAY7_NV = 0x8657;
	int GL_VERTEX_ATTRIB_ARRAY8_NV = 0x8658;
	int GL_VERTEX_ATTRIB_ARRAY9_NV = 0x8659;
	int GL_VERTEX_ATTRIB_ARRAY10_NV = 0x865A;
	int GL_VERTEX_ATTRIB_ARRAY11_NV = 0x865B;
	int GL_VERTEX_ATTRIB_ARRAY12_NV = 0x865C;
	int GL_VERTEX_ATTRIB_ARRAY13_NV = 0x865D;
	int GL_VERTEX_ATTRIB_ARRAY14_NV = 0x865E;
	int GL_VERTEX_ATTRIB_ARRAY15_NV = 0x865F;

	/**
	 Accepted by the &lt;target&gt; parameter of GetMapdv, GetMapfv, GetMapiv,
	 Map1d and Map1f and by the &lt;cap&gt; parameter of Enable, Disable, and
	 IsEnabled, and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 GetFloatv, and GetDoublev:
	 */
	int GL_MAP1_VERTEX_ATTRIB0_4_NV = 0x8660;
	int GL_MAP1_VERTEX_ATTRIB1_4_NV = 0x8661;
	int GL_MAP1_VERTEX_ATTRIB2_4_NV = 0x8662;
	int GL_MAP1_VERTEX_ATTRIB3_4_NV = 0x8663;
	int GL_MAP1_VERTEX_ATTRIB4_4_NV = 0x8664;
	int GL_MAP1_VERTEX_ATTRIB5_4_NV = 0x8665;
	int GL_MAP1_VERTEX_ATTRIB6_4_NV = 0x8666;
	int GL_MAP1_VERTEX_ATTRIB7_4_NV = 0x8667;
	int GL_MAP1_VERTEX_ATTRIB8_4_NV = 0x8668;
	int GL_MAP1_VERTEX_ATTRIB9_4_NV = 0x8669;
	int GL_MAP1_VERTEX_ATTRIB10_4_NV = 0x866A;
	int GL_MAP1_VERTEX_ATTRIB11_4_NV = 0x866B;
	int GL_MAP1_VERTEX_ATTRIB12_4_NV = 0x866C;
	int GL_MAP1_VERTEX_ATTRIB13_4_NV = 0x866D;
	int GL_MAP1_VERTEX_ATTRIB14_4_NV = 0x866E;
	int GL_MAP1_VERTEX_ATTRIB15_4_NV = 0x866F;

	/**
	 Accepted by the &lt;target&gt; parameter of GetMapdv, GetMapfv, GetMapiv,
	 Map2d and Map2f and by the &lt;cap&gt; parameter of Enable, Disable, and
	 IsEnabled, and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 GetFloatv, and GetDoublev:
	 */
	int GL_MAP2_VERTEX_ATTRIB0_4_NV = 0x8670;
	int GL_MAP2_VERTEX_ATTRIB1_4_NV = 0x8671;
	int GL_MAP2_VERTEX_ATTRIB2_4_NV = 0x8672;
	int GL_MAP2_VERTEX_ATTRIB3_4_NV = 0x8673;
	int GL_MAP2_VERTEX_ATTRIB4_4_NV = 0x8674;
	int GL_MAP2_VERTEX_ATTRIB5_4_NV = 0x8675;
	int GL_MAP2_VERTEX_ATTRIB6_4_NV = 0x8676;
	int GL_MAP2_VERTEX_ATTRIB7_4_NV = 0x8677;
	int GL_MAP2_VERTEX_ATTRIB8_4_NV = 0x8678;
	int GL_MAP2_VERTEX_ATTRIB9_4_NV = 0x8679;
	int GL_MAP2_VERTEX_ATTRIB10_4_NV = 0x867A;
	int GL_MAP2_VERTEX_ATTRIB11_4_NV = 0x867B;
	int GL_MAP2_VERTEX_ATTRIB12_4_NV = 0x867C;
	int GL_MAP2_VERTEX_ATTRIB13_4_NV = 0x867D;
	int GL_MAP2_VERTEX_ATTRIB14_4_NV = 0x867E;
	int GL_MAP2_VERTEX_ATTRIB15_4_NV = 0x867F;

	void glExecuteProgramNV(@GLenum int target, @GLuint int id, @Check("4") @Const FloatBuffer params);

	@StripPostfix("params")
	void glGetProgramParameterfvNV(@GLenum int target, @GLuint int index, @GLenum int parameterName, @OutParameter @Check("4") FloatBuffer params);

	@StripPostfix("params")
	void glGetProgramParameterdvNV(@GLenum int target, @GLuint int index, @GLenum int parameterName, @OutParameter @Check("4") DoubleBuffer params);

	@StripPostfix("params")
	void glGetTrackMatrixivNV(@GLenum int target, @GLuint int address, @GLenum int parameterName, @OutParameter @Check("4") IntBuffer params);

	@StripPostfix("params")
	void glGetVertexAttribfvNV(@GLuint int index, @GLenum int parameterName, @OutParameter @Check("4") FloatBuffer params);

	@StripPostfix("params")
	void glGetVertexAttribdvNV(@GLuint int index, @GLenum int parameterName, @OutParameter @Check("4") DoubleBuffer params);

	@StripPostfix("params")
	void glGetVertexAttribivNV(@GLuint int index, @GLenum int parameterName, @OutParameter @Check("4") IntBuffer params);

	@StripPostfix("pointer")
	void glGetVertexAttribPointervNV(@GLuint int index, @GLenum int parameterName, @Result @GLvoid ByteBuffer pointer);

	void glProgramParameter4fNV(@GLenum int target, @GLuint int index, float x, float y, float z, float w);

	void glProgramParameter4dNV(@GLenum int target, @GLuint int index, double x, double y, double z, double w);

	@StripPostfix("params")
	void glProgramParameters4fvNV(@GLenum int target, @GLuint int index, @AutoSize(value = "params", expression = " >> 2") @GLuint int count,
	                              @Const FloatBuffer params);

	@StripPostfix("params")
	void glProgramParameters4dvNV(@GLenum int target, @GLuint int index, @AutoSize(value = "params", expression = " >> 2") @GLuint int count,
	                              @Const DoubleBuffer params);

	void glTrackMatrixNV(@GLenum int target, @GLuint int address, @GLenum int matrix, @GLenum int transform);

	void glVertexAttribPointerNV(@GLuint int index, int size, @GLenum int type, @GLsizei int stride,
                                 @CachedReference(index="index",name="glVertexAttribPointer_buffer")
	                             @BufferObject(BufferKind.ArrayVBO)
	                             @Check
	                             @Const
	                             @GLbyte
	                             @GLubyte
	                             @GLshort
	                             @GLushort
	                             @GLint
	                             @GLuint
	                             @GLfloat
	                             @GLdouble Buffer buffer);

	@NoErrorCheck
	void glVertexAttrib1sNV(@GLuint int index, short x);

	@NoErrorCheck
	void glVertexAttrib1fNV(@GLuint int index, float x);

	@NoErrorCheck
	void glVertexAttrib1dNV(@GLuint int index, double x);

	@NoErrorCheck
	void glVertexAttrib2sNV(@GLuint int index, short x, short y);

	@NoErrorCheck
	void glVertexAttrib2fNV(@GLuint int index, float x, float y);

	@NoErrorCheck
	void glVertexAttrib2dNV(@GLuint int index, double x, double y);

	@NoErrorCheck
	void glVertexAttrib3sNV(@GLuint int index, short x, short y, short z);

	@NoErrorCheck
	void glVertexAttrib3fNV(@GLuint int index, float x, float y, float z);

	@NoErrorCheck
	void glVertexAttrib3dNV(@GLuint int index, double x, double y, double z);

	@NoErrorCheck
	void glVertexAttrib4sNV(@GLuint int index, short x, short y, short z, short w);

	@NoErrorCheck
	void glVertexAttrib4fNV(@GLuint int index, float x, float y, float z, float w);

	@NoErrorCheck
	void glVertexAttrib4dNV(@GLuint int index, double x, double y, double z, double w);

	@NoErrorCheck
	void glVertexAttrib4ubNV(@GLuint int index, @GLubyte byte x, @GLubyte byte y, @GLubyte byte z, @GLubyte byte w);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribs1svNV(@GLuint int index, @AutoSize("v") @GLsizei int n, @Const ShortBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribs1fvNV(@GLuint int index, @AutoSize("v") @GLsizei int n, @Const FloatBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribs1dvNV(@GLuint int index, @AutoSize("v") @GLsizei int n, @Const DoubleBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribs2svNV(@GLuint int index, @AutoSize(value = "v", expression = " >> 1") @GLsizei int n, @Const ShortBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribs2fvNV(@GLuint int index, @AutoSize(value = "v", expression = " >> 1") @GLsizei int n, @Const FloatBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribs2dvNV(@GLuint int index, @AutoSize(value = "v", expression = " >> 1") @GLsizei int n, @Const DoubleBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribs3svNV(@GLuint int index, @AutoSize(value = "v", expression = " / 3") @GLsizei int n, @Const ShortBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribs3fvNV(@GLuint int index, @AutoSize(value = "v", expression = " / 3") @GLsizei int n, @Const FloatBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribs3dvNV(@GLuint int index, @AutoSize(value = "v", expression = " / 3") @GLsizei int n, @Const DoubleBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribs4svNV(@GLuint int index, @AutoSize(value = "v", expression = " >> 2") @GLsizei int n, @Const ShortBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribs4fvNV(@GLuint int index, @AutoSize(value = "v", expression = " >> 2") @GLsizei int n, @Const FloatBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribs4dvNV(@GLuint int index, @AutoSize(value = "v", expression = " >> 2") @GLsizei int n, @Const DoubleBuffer v);
}
