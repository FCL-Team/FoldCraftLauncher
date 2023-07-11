/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVVertexProgram extends NVProgram {

	/**
	 * 	 Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled,
	 * 	 and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 * 	 and GetDoublev, and by the &lt;target&gt; parameter of BindProgramNV,
	 * 	 ExecuteProgramNV, GetProgramParameter[df]vNV, GetTrackMatrixivNV,
	 * 	 LoadProgramNV, ProgramParameter[s]4[df][v]NV, and TrackMatrixNV:
	 */
	public static final int GL_VERTEX_PROGRAM_NV = 0x8620;

	/**
	 * 	 Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled,
	 * 	 and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 * 	 and GetDoublev:
	 */
	public static final int GL_VERTEX_PROGRAM_POINT_SIZE_NV = 0x8642,
		GL_VERTEX_PROGRAM_TWO_SIDE_NV = 0x8643;

	/**
	 * 	 Accepted by the &lt;target&gt; parameter of ExecuteProgramNV and
	 * 	 LoadProgramNV:
	 */
	public static final int GL_VERTEX_STATE_PROGRAM_NV = 0x8621;

	/**
	 * 	 Accepted by the &lt;pname&gt; parameter of GetVertexAttrib[dfi]vNV:
	 */
	public static final int GL_ATTRIB_ARRAY_SIZE_NV = 0x8623,
		GL_ATTRIB_ARRAY_STRIDE_NV = 0x8624,
		GL_ATTRIB_ARRAY_TYPE_NV = 0x8625,
		GL_CURRENT_ATTRIB_NV = 0x8626;

	/**
	 * 	 Accepted by the &lt;pname&gt; parameter of GetProgramParameterfvNV
	 * 	 and GetProgramParameterdvNV:
	 */
	public static final int GL_PROGRAM_PARAMETER_NV = 0x8644;

	/**
	 * 	 Accepted by the &lt;pname&gt; parameter of GetVertexAttribPointervNV:
	 */
	public static final int GL_ATTRIB_ARRAY_POINTER_NV = 0x8645;

	/**
	 * 	 Accepted by the &lt;pname&gt; parameter of GetTrackMatrixivNV:
	 */
	public static final int GL_TRACK_MATRIX_NV = 0x8648,
		GL_TRACK_MATRIX_TRANSFORM_NV = 0x8649;

	/**
	 * 	 Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * 	 GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_TRACK_MATRIX_STACK_DEPTH_NV = 0x862E,
		GL_MAX_TRACK_MATRICES_NV = 0x862F,
		GL_CURRENT_MATRIX_STACK_DEPTH_NV = 0x8640,
		GL_CURRENT_MATRIX_NV = 0x8641,
		GL_VERTEX_PROGRAM_BINDING_NV = 0x864A;

	/**
	 * 	 Accepted by the &lt;matrix&gt; parameter of TrackMatrixNV:
	 */
	public static final int GL_MODELVIEW_PROJECTION_NV = 0x8629;

	/**
	 * 	 Accepted by the &lt;matrix&gt; parameter of TrackMatrixNV and by the
	 * 	 &lt;mode&gt; parameter of MatrixMode:
	 */
	public static final int GL_MATRIX0_NV = 0x8630,
		GL_MATRIX1_NV = 0x8631,
		GL_MATRIX2_NV = 0x8632,
		GL_MATRIX3_NV = 0x8633,
		GL_MATRIX4_NV = 0x8634,
		GL_MATRIX5_NV = 0x8635,
		GL_MATRIX6_NV = 0x8636,
		GL_MATRIX7_NV = 0x8637;

	/**
	 * 	 Accepted by the &lt;transform&gt; parameter of TrackMatrixNV:
	 */
	public static final int GL_IDENTITY_NV = 0x862A,
		GL_INVERSE_NV = 0x862B,
		GL_TRANSPOSE_NV = 0x862C,
		GL_INVERSE_TRANSPOSE_NV = 0x862D;

	/**
	 * 	 Accepted by the &lt;array&gt; parameter of EnableClientState and
	 * 	 DisableClientState, by the &lt;cap&gt; parameter of IsEnabled, and by
	 * 	 the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 * 	 GetDoublev:
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY0_NV = 0x8650,
		GL_VERTEX_ATTRIB_ARRAY1_NV = 0x8651,
		GL_VERTEX_ATTRIB_ARRAY2_NV = 0x8652,
		GL_VERTEX_ATTRIB_ARRAY3_NV = 0x8653,
		GL_VERTEX_ATTRIB_ARRAY4_NV = 0x8654,
		GL_VERTEX_ATTRIB_ARRAY5_NV = 0x8655,
		GL_VERTEX_ATTRIB_ARRAY6_NV = 0x8656,
		GL_VERTEX_ATTRIB_ARRAY7_NV = 0x8657,
		GL_VERTEX_ATTRIB_ARRAY8_NV = 0x8658,
		GL_VERTEX_ATTRIB_ARRAY9_NV = 0x8659,
		GL_VERTEX_ATTRIB_ARRAY10_NV = 0x865A,
		GL_VERTEX_ATTRIB_ARRAY11_NV = 0x865B,
		GL_VERTEX_ATTRIB_ARRAY12_NV = 0x865C,
		GL_VERTEX_ATTRIB_ARRAY13_NV = 0x865D,
		GL_VERTEX_ATTRIB_ARRAY14_NV = 0x865E,
		GL_VERTEX_ATTRIB_ARRAY15_NV = 0x865F;

	/**
	 * 	 Accepted by the &lt;target&gt; parameter of GetMapdv, GetMapfv, GetMapiv,
	 * 	 Map1d and Map1f and by the &lt;cap&gt; parameter of Enable, Disable, and
	 * 	 IsEnabled, and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * 	 GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAP1_VERTEX_ATTRIB0_4_NV = 0x8660,
		GL_MAP1_VERTEX_ATTRIB1_4_NV = 0x8661,
		GL_MAP1_VERTEX_ATTRIB2_4_NV = 0x8662,
		GL_MAP1_VERTEX_ATTRIB3_4_NV = 0x8663,
		GL_MAP1_VERTEX_ATTRIB4_4_NV = 0x8664,
		GL_MAP1_VERTEX_ATTRIB5_4_NV = 0x8665,
		GL_MAP1_VERTEX_ATTRIB6_4_NV = 0x8666,
		GL_MAP1_VERTEX_ATTRIB7_4_NV = 0x8667,
		GL_MAP1_VERTEX_ATTRIB8_4_NV = 0x8668,
		GL_MAP1_VERTEX_ATTRIB9_4_NV = 0x8669,
		GL_MAP1_VERTEX_ATTRIB10_4_NV = 0x866A,
		GL_MAP1_VERTEX_ATTRIB11_4_NV = 0x866B,
		GL_MAP1_VERTEX_ATTRIB12_4_NV = 0x866C,
		GL_MAP1_VERTEX_ATTRIB13_4_NV = 0x866D,
		GL_MAP1_VERTEX_ATTRIB14_4_NV = 0x866E,
		GL_MAP1_VERTEX_ATTRIB15_4_NV = 0x866F;

	/**
	 * 	 Accepted by the &lt;target&gt; parameter of GetMapdv, GetMapfv, GetMapiv,
	 * 	 Map2d and Map2f and by the &lt;cap&gt; parameter of Enable, Disable, and
	 * 	 IsEnabled, and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * 	 GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAP2_VERTEX_ATTRIB0_4_NV = 0x8670,
		GL_MAP2_VERTEX_ATTRIB1_4_NV = 0x8671,
		GL_MAP2_VERTEX_ATTRIB2_4_NV = 0x8672,
		GL_MAP2_VERTEX_ATTRIB3_4_NV = 0x8673,
		GL_MAP2_VERTEX_ATTRIB4_4_NV = 0x8674,
		GL_MAP2_VERTEX_ATTRIB5_4_NV = 0x8675,
		GL_MAP2_VERTEX_ATTRIB6_4_NV = 0x8676,
		GL_MAP2_VERTEX_ATTRIB7_4_NV = 0x8677,
		GL_MAP2_VERTEX_ATTRIB8_4_NV = 0x8678,
		GL_MAP2_VERTEX_ATTRIB9_4_NV = 0x8679,
		GL_MAP2_VERTEX_ATTRIB10_4_NV = 0x867A,
		GL_MAP2_VERTEX_ATTRIB11_4_NV = 0x867B,
		GL_MAP2_VERTEX_ATTRIB12_4_NV = 0x867C,
		GL_MAP2_VERTEX_ATTRIB13_4_NV = 0x867D,
		GL_MAP2_VERTEX_ATTRIB14_4_NV = 0x867E,
		GL_MAP2_VERTEX_ATTRIB15_4_NV = 0x867F;

	private NVVertexProgram() {}

	public static void glExecuteProgramNV(int target, int id, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glExecuteProgramNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglExecuteProgramNV(target, id, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglExecuteProgramNV(int target, int id, long params, long function_pointer);

	public static void glGetProgramParameterNV(int target, int index, int parameterName, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetProgramParameterfvNV(target, index, parameterName, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramParameterfvNV(int target, int index, int parameterName, long params, long function_pointer);

	public static void glGetProgramParameterNV(int target, int index, int parameterName, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramParameterdvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetProgramParameterdvNV(target, index, parameterName, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramParameterdvNV(int target, int index, int parameterName, long params, long function_pointer);

	public static void glGetTrackMatrixNV(int target, int address, int parameterName, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTrackMatrixivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetTrackMatrixivNV(target, address, parameterName, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTrackMatrixivNV(int target, int address, int parameterName, long params, long function_pointer);

	public static void glGetVertexAttribNV(int index, int parameterName, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribfvNV(index, parameterName, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribfvNV(int index, int parameterName, long params, long function_pointer);

	public static void glGetVertexAttribNV(int index, int parameterName, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribdvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribdvNV(index, parameterName, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribdvNV(int index, int parameterName, long params, long function_pointer);

	public static void glGetVertexAttribNV(int index, int parameterName, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribivNV(index, parameterName, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribivNV(int index, int parameterName, long params, long function_pointer);

	public static ByteBuffer glGetVertexAttribPointerNV(int index, int parameterName, long result_size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribPointervNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer __result = nglGetVertexAttribPointervNV(index, parameterName, result_size, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglGetVertexAttribPointervNV(int index, int parameterName, long result_size, long function_pointer);

	public static void glProgramParameter4fNV(int target, int index, float x, float y, float z, float w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramParameter4fNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramParameter4fNV(target, index, x, y, z, w, function_pointer);
	}
	static native void nglProgramParameter4fNV(int target, int index, float x, float y, float z, float w, long function_pointer);

	public static void glProgramParameter4dNV(int target, int index, double x, double y, double z, double w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramParameter4dNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramParameter4dNV(target, index, x, y, z, w, function_pointer);
	}
	static native void nglProgramParameter4dNV(int target, int index, double x, double y, double z, double w, long function_pointer);

	public static void glProgramParameters4NV(int target, int index, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramParameters4fvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglProgramParameters4fvNV(target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramParameters4fvNV(int target, int index, int params_count, long params, long function_pointer);

	public static void glProgramParameters4NV(int target, int index, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramParameters4dvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglProgramParameters4dvNV(target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramParameters4dvNV(int target, int index, int params_count, long params, long function_pointer);

	public static void glTrackMatrixNV(int target, int address, int matrix, int transform) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTrackMatrixNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTrackMatrixNV(target, address, matrix, transform, function_pointer);
	}
	static native void nglTrackMatrixNV(int target, int address, int matrix, int transform, long function_pointer);

	public static void glVertexAttribPointerNV(int index, int size, int type, int stride, DoubleBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointerNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointerNV(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribPointerNV(int index, int size, int type, int stride, FloatBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointerNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointerNV(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribPointerNV(int index, int size, int type, int stride, ByteBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointerNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointerNV(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribPointerNV(int index, int size, int type, int stride, IntBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointerNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointerNV(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribPointerNV(int index, int size, int type, int stride, ShortBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointerNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointerNV(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	static native void nglVertexAttribPointerNV(int index, int size, int type, int stride, long buffer, long function_pointer);
	public static void glVertexAttribPointerNV(int index, int size, int type, int stride, long buffer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointerNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglVertexAttribPointerNVBO(index, size, type, stride, buffer_buffer_offset, function_pointer);
	}
	static native void nglVertexAttribPointerNVBO(int index, int size, int type, int stride, long buffer_buffer_offset, long function_pointer);

	public static void glVertexAttrib1sNV(int index, short x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib1sNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib1sNV(index, x, function_pointer);
	}
	static native void nglVertexAttrib1sNV(int index, short x, long function_pointer);

	public static void glVertexAttrib1fNV(int index, float x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib1fNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib1fNV(index, x, function_pointer);
	}
	static native void nglVertexAttrib1fNV(int index, float x, long function_pointer);

	public static void glVertexAttrib1dNV(int index, double x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib1dNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib1dNV(index, x, function_pointer);
	}
	static native void nglVertexAttrib1dNV(int index, double x, long function_pointer);

	public static void glVertexAttrib2sNV(int index, short x, short y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib2sNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib2sNV(index, x, y, function_pointer);
	}
	static native void nglVertexAttrib2sNV(int index, short x, short y, long function_pointer);

	public static void glVertexAttrib2fNV(int index, float x, float y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib2fNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib2fNV(index, x, y, function_pointer);
	}
	static native void nglVertexAttrib2fNV(int index, float x, float y, long function_pointer);

	public static void glVertexAttrib2dNV(int index, double x, double y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib2dNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib2dNV(index, x, y, function_pointer);
	}
	static native void nglVertexAttrib2dNV(int index, double x, double y, long function_pointer);

	public static void glVertexAttrib3sNV(int index, short x, short y, short z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib3sNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib3sNV(index, x, y, z, function_pointer);
	}
	static native void nglVertexAttrib3sNV(int index, short x, short y, short z, long function_pointer);

	public static void glVertexAttrib3fNV(int index, float x, float y, float z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib3fNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib3fNV(index, x, y, z, function_pointer);
	}
	static native void nglVertexAttrib3fNV(int index, float x, float y, float z, long function_pointer);

	public static void glVertexAttrib3dNV(int index, double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib3dNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib3dNV(index, x, y, z, function_pointer);
	}
	static native void nglVertexAttrib3dNV(int index, double x, double y, double z, long function_pointer);

	public static void glVertexAttrib4sNV(int index, short x, short y, short z, short w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib4sNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib4sNV(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttrib4sNV(int index, short x, short y, short z, short w, long function_pointer);

	public static void glVertexAttrib4fNV(int index, float x, float y, float z, float w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib4fNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib4fNV(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttrib4fNV(int index, float x, float y, float z, float w, long function_pointer);

	public static void glVertexAttrib4dNV(int index, double x, double y, double z, double w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib4dNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib4dNV(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttrib4dNV(int index, double x, double y, double z, double w, long function_pointer);

	public static void glVertexAttrib4ubNV(int index, byte x, byte y, byte z, byte w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib4ubNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib4ubNV(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttrib4ubNV(int index, byte x, byte y, byte z, byte w, long function_pointer);

	public static void glVertexAttribs1NV(int index, ShortBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribs1svNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglVertexAttribs1svNV(index, v.remaining(), MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribs1svNV(int index, int v_n, long v, long function_pointer);

	public static void glVertexAttribs1NV(int index, FloatBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribs1fvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglVertexAttribs1fvNV(index, v.remaining(), MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribs1fvNV(int index, int v_n, long v, long function_pointer);

	public static void glVertexAttribs1NV(int index, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribs1dvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglVertexAttribs1dvNV(index, v.remaining(), MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribs1dvNV(int index, int v_n, long v, long function_pointer);

	public static void glVertexAttribs2NV(int index, ShortBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribs2svNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglVertexAttribs2svNV(index, v.remaining() >> 1, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribs2svNV(int index, int v_n, long v, long function_pointer);

	public static void glVertexAttribs2NV(int index, FloatBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribs2fvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglVertexAttribs2fvNV(index, v.remaining() >> 1, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribs2fvNV(int index, int v_n, long v, long function_pointer);

	public static void glVertexAttribs2NV(int index, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribs2dvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglVertexAttribs2dvNV(index, v.remaining() >> 1, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribs2dvNV(int index, int v_n, long v, long function_pointer);

	public static void glVertexAttribs3NV(int index, ShortBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribs3svNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglVertexAttribs3svNV(index, v.remaining() / 3, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribs3svNV(int index, int v_n, long v, long function_pointer);

	public static void glVertexAttribs3NV(int index, FloatBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribs3fvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglVertexAttribs3fvNV(index, v.remaining() / 3, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribs3fvNV(int index, int v_n, long v, long function_pointer);

	public static void glVertexAttribs3NV(int index, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribs3dvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglVertexAttribs3dvNV(index, v.remaining() / 3, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribs3dvNV(int index, int v_n, long v, long function_pointer);

	public static void glVertexAttribs4NV(int index, ShortBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribs4svNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglVertexAttribs4svNV(index, v.remaining() >> 2, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribs4svNV(int index, int v_n, long v, long function_pointer);

	public static void glVertexAttribs4NV(int index, FloatBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribs4fvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglVertexAttribs4fvNV(index, v.remaining() >> 2, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribs4fvNV(int index, int v_n, long v, long function_pointer);

	public static void glVertexAttribs4NV(int index, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribs4dvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglVertexAttribs4dvNV(index, v.remaining() >> 2, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribs4dvNV(int index, int v_n, long v, long function_pointer);
}
