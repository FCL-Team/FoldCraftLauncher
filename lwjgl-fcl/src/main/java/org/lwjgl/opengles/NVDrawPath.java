/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class NVDrawPath {

	/**
	 * Accepted as the &lt;paramType&gt; parameter of PathParameterNV: 
	 */
	public static final int GL_PATH_QUALITY_NV = 0x8ED8,
		GL_FILL_RULE_NV = 0x8ED9,
		GL_STROKE_CAP0_STYLE_NV = 0x8EE0,
		GL_STROKE_CAP1_STYLE_NV = 0x8EE1,
		GL_STROKE_CAP2_STYLE_NV = 0x8EE2,
		GL_STROKE_CAP3_STYLE_NV = 0x8EE3,
		GL_STROKE_JOIN_STYLE_NV = 0x8EE8,
		GL_STROKE_MITER_LIMIT_NV = 0x8EE9;

	/**
	 * Values for the ILL_RULE_NV path parameter: 
	 */
	public static final int GL_EVEN_ODD_NV = 0x8EF0,
		GL_NON_ZERO_NV = 0x8EF1;

	/**
	 * Values for the CAP[0-3]_STYLE_NV path parameter: 
	 */
	public static final int GL_CAP_BUTT_NV = 0x8EF4,
		GL_CAP_ROUND_NV = 0x8EF5,
		GL_CAP_SQUARE_NV = 0x8EF6,
		GL_CAP_TRIANGLE_NV = 0x8EF7;

	/**
	 * Values for the JOIN_STYLE_NV path parameter: 
	 */
	public static final int GL_JOIN_MITER_NV = 0x8EFC,
		GL_JOIN_ROUND_NV = 0x8EFD,
		GL_JOIN_BEVEL_NV = 0x8EFE,
		GL_JOIN_CLIPPED_MITER_NV = 0x8EFF;

	/**
	 * Accepted as the &lt;target&gt; parameter of PathMatrixNV: 
	 */
	public static final int GL_MATRIX_PATH_TO_CLIP_NV = 0x8F04,
		GL_MATRIX_STROKE_TO_PATH_NV = 0x8F05,
		GL_MATRIX_PATH_COORD0_NV = 0x8F08,
		GL_MATRIX_PATH_COORD1_NV = 0x8F09,
		GL_MATRIX_PATH_COORD2_NV = 0x8F0A,
		GL_MATRIX_PATH_COORD3_NV = 0x8F0B;

	/**
	 * Accepted as the &lt;mode&gt; parameter of DrawPathbufferNV: 
	 */
	public static final int GL_FILL_PATH_NV = 0x8F18,
		GL_STROKE_PATH_NV = 0x8F19;

	/**
	 * Accepted as path commands by CreatePathNV: 
	 */
	public static final byte GL_MOVE_TO_NV = 0x0,
		GL_LINE_TO_NV = 0x1,
		GL_QUADRATIC_BEZIER_TO_NV = 0x2,
		GL_CUBIC_BEZIER_TO_NV = 0x3,
		GL_START_MARKER_NV = 0x20,
		GL_CLOSE_NV = 0x21,
		GL_STROKE_CAP0_NV = 0x40,
		GL_STROKE_CAP1_NV = 0x41,
		GL_STROKE_CAP2_NV = 0x42,
		GL_STROKE_CAP3_NV = 0x43;

	private NVDrawPath() {}

	static native void initNativeStubs() throws LWJGLException;

	public static int glCreatePathNV(int datatype, ByteBuffer commands) {
		BufferChecks.checkDirect(commands);
		int __result = nglCreatePathNV(datatype, commands.remaining(), MemoryUtil.getAddress(commands));
		return __result;
	}
	static native int nglCreatePathNV(int datatype, int commands_numCommands, long commands);

	public static void glDeletePathNV(int path) {
		nglDeletePathNV(path);
	}
	static native void nglDeletePathNV(int path);

	public static void glPathVerticesNV(int path, ByteBuffer vertices) {
		BufferChecks.checkDirect(vertices);
		nglPathVerticesNV(path, MemoryUtil.getAddress(vertices));
	}
	static native void nglPathVerticesNV(int path, long vertices);

	public static void glPathParameterfNV(int path, int paramType, float param) {
		nglPathParameterfNV(path, paramType, param);
	}
	static native void nglPathParameterfNV(int path, int paramType, float param);

	public static void glPathParameteriNV(int path, int paramType, int param) {
		nglPathParameteriNV(path, paramType, param);
	}
	static native void nglPathParameteriNV(int path, int paramType, int param);

	public static int glCreatePathProgramNV() {
		int __result = nglCreatePathProgramNV();
		return __result;
	}
	static native int nglCreatePathProgramNV();

	public static void glPathMatrixNV(int target, FloatBuffer value) {
		BufferChecks.checkBuffer(value, 16);
		nglPathMatrixNV(target, MemoryUtil.getAddress(value));
	}
	static native void nglPathMatrixNV(int target, long value);

	public static void glDrawPathNV(int path, int mode) {
		nglDrawPathNV(path, mode);
	}
	static native void nglDrawPathNV(int path, int mode);

	public static int glCreatePathbufferNV(int capacity) {
		int __result = nglCreatePathbufferNV(capacity);
		return __result;
	}
	static native int nglCreatePathbufferNV(int capacity);

	public static void glDeletePathbufferNV(int buffer) {
		nglDeletePathbufferNV(buffer);
	}
	static native void nglDeletePathbufferNV(int buffer);

	public static void glPathbufferPathNV(int buffer, int index, int path) {
		nglPathbufferPathNV(buffer, index, path);
	}
	static native void nglPathbufferPathNV(int buffer, int index, int path);

	public static void glPathbufferPositionNV(int buffer, int index, float x, float y) {
		nglPathbufferPositionNV(buffer, index, x, y);
	}
	static native void nglPathbufferPositionNV(int buffer, int index, float x, float y);

	public static void glDrawPathbufferNV(int buffer, int mode) {
		nglDrawPathbufferNV(buffer, mode);
	}
	static native void nglDrawPathbufferNV(int buffer, int mode);
}
