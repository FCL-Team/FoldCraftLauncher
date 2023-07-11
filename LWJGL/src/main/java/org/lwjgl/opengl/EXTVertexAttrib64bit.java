/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTVertexAttrib64bit {

	/**
	 * Returned in the &lt;type&gt; parameter of GetActiveAttrib: 
	 */
	public static final int GL_DOUBLE_VEC2_EXT = 0x8FFC,
		GL_DOUBLE_VEC3_EXT = 0x8FFD,
		GL_DOUBLE_VEC4_EXT = 0x8FFE,
		GL_DOUBLE_MAT2_EXT = 0x8F46,
		GL_DOUBLE_MAT3_EXT = 0x8F47,
		GL_DOUBLE_MAT4_EXT = 0x8F48,
		GL_DOUBLE_MAT2x3_EXT = 0x8F49,
		GL_DOUBLE_MAT2x4_EXT = 0x8F4A,
		GL_DOUBLE_MAT3x2_EXT = 0x8F4B,
		GL_DOUBLE_MAT3x4_EXT = 0x8F4C,
		GL_DOUBLE_MAT4x2_EXT = 0x8F4D,
		GL_DOUBLE_MAT4x3_EXT = 0x8F4E;

	private EXTVertexAttrib64bit() {}

	public static void glVertexAttribL1dEXT(int index, double x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL1dEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL1dEXT(index, x, function_pointer);
	}
	static native void nglVertexAttribL1dEXT(int index, double x, long function_pointer);

	public static void glVertexAttribL2dEXT(int index, double x, double y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL2dEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL2dEXT(index, x, y, function_pointer);
	}
	static native void nglVertexAttribL2dEXT(int index, double x, double y, long function_pointer);

	public static void glVertexAttribL3dEXT(int index, double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL3dEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL3dEXT(index, x, y, z, function_pointer);
	}
	static native void nglVertexAttribL3dEXT(int index, double x, double y, double z, long function_pointer);

	public static void glVertexAttribL4dEXT(int index, double x, double y, double z, double w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL4dEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL4dEXT(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttribL4dEXT(int index, double x, double y, double z, double w, long function_pointer);

	public static void glVertexAttribL1EXT(int index, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL1dvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 1);
		nglVertexAttribL1dvEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL1dvEXT(int index, long v, long function_pointer);

	public static void glVertexAttribL2EXT(int index, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL2dvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 2);
		nglVertexAttribL2dvEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL2dvEXT(int index, long v, long function_pointer);

	public static void glVertexAttribL3EXT(int index, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL3dvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 3);
		nglVertexAttribL3dvEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL3dvEXT(int index, long v, long function_pointer);

	public static void glVertexAttribL4EXT(int index, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL4dvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribL4dvEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL4dvEXT(int index, long v, long function_pointer);

	public static void glVertexAttribLPointerEXT(int index, int size, int stride, DoubleBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribLPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = pointer;
		nglVertexAttribLPointerEXT(index, size, GL11.GL_DOUBLE, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	static native void nglVertexAttribLPointerEXT(int index, int size, int type, int stride, long pointer, long function_pointer);
	public static void glVertexAttribLPointerEXT(int index, int size, int stride, long pointer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribLPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglVertexAttribLPointerEXTBO(index, size, GL11.GL_DOUBLE, stride, pointer_buffer_offset, function_pointer);
	}
	static native void nglVertexAttribLPointerEXTBO(int index, int size, int type, int stride, long pointer_buffer_offset, long function_pointer);

	public static void glGetVertexAttribLEXT(int index, int pname, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribLdvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribLdvEXT(index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribLdvEXT(int index, int pname, long params, long function_pointer);

	public static void glVertexArrayVertexAttribLOffsetEXT(int vaobj, int buffer, int index, int size, int type, int stride, long offset) {
		ARBVertexAttrib64bit.glVertexArrayVertexAttribLOffsetEXT(vaobj, buffer, index, size, type, stride, offset);
	}
}
