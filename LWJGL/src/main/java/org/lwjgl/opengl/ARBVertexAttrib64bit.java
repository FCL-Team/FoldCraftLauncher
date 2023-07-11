/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBVertexAttrib64bit {

	/**
	 * Returned in the &lt;type&gt; parameter of GetActiveAttrib: 
	 */
	public static final int GL_DOUBLE_VEC2 = 0x8FFC,
		GL_DOUBLE_VEC3 = 0x8FFD,
		GL_DOUBLE_VEC4 = 0x8FFE,
		GL_DOUBLE_MAT2 = 0x8F46,
		GL_DOUBLE_MAT3 = 0x8F47,
		GL_DOUBLE_MAT4 = 0x8F48,
		GL_DOUBLE_MAT2x3 = 0x8F49,
		GL_DOUBLE_MAT2x4 = 0x8F4A,
		GL_DOUBLE_MAT3x2 = 0x8F4B,
		GL_DOUBLE_MAT3x4 = 0x8F4C,
		GL_DOUBLE_MAT4x2 = 0x8F4D,
		GL_DOUBLE_MAT4x3 = 0x8F4E;

	private ARBVertexAttrib64bit() {}

	public static void glVertexAttribL1d(int index, double x) {
		GL41.glVertexAttribL1d(index, x);
	}

	public static void glVertexAttribL2d(int index, double x, double y) {
		GL41.glVertexAttribL2d(index, x, y);
	}

	public static void glVertexAttribL3d(int index, double x, double y, double z) {
		GL41.glVertexAttribL3d(index, x, y, z);
	}

	public static void glVertexAttribL4d(int index, double x, double y, double z, double w) {
		GL41.glVertexAttribL4d(index, x, y, z, w);
	}

	public static void glVertexAttribL1(int index, DoubleBuffer v) {
		GL41.glVertexAttribL1(index, v);
	}

	public static void glVertexAttribL2(int index, DoubleBuffer v) {
		GL41.glVertexAttribL2(index, v);
	}

	public static void glVertexAttribL3(int index, DoubleBuffer v) {
		GL41.glVertexAttribL3(index, v);
	}

	public static void glVertexAttribL4(int index, DoubleBuffer v) {
		GL41.glVertexAttribL4(index, v);
	}

	public static void glVertexAttribLPointer(int index, int size, int stride, DoubleBuffer pointer) {
		GL41.glVertexAttribLPointer(index, size, stride, pointer);
	}
	public static void glVertexAttribLPointer(int index, int size, int stride, long pointer_buffer_offset) {
		GL41.glVertexAttribLPointer(index, size, stride, pointer_buffer_offset);
	}

	public static void glGetVertexAttribL(int index, int pname, DoubleBuffer params) {
		GL41.glGetVertexAttribL(index, pname, params);
	}

	public static void glVertexArrayVertexAttribLOffsetEXT(int vaobj, int buffer, int index, int size, int type, int stride, long offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexArrayVertexAttribLOffsetEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexArrayVertexAttribLOffsetEXT(vaobj, buffer, index, size, type, stride, offset, function_pointer);
	}
	static native void nglVertexArrayVertexAttribLOffsetEXT(int vaobj, int buffer, int index, int size, int type, int stride, long offset, long function_pointer);
}
