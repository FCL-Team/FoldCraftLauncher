/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVVertexAttribInteger64bit {

	/**
	 *  Accepted by the &lt;type&gt; parameter of VertexAttribLPointerEXT,
	 *  VertexArrayVertexAttribLOffsetEXT, and VertexAttribLFormatNV:
	 */
	public static final int GL_INT64_NV = 0x140E,
		GL_UNSIGNED_INT64_NV = 0x140F;

	private NVVertexAttribInteger64bit() {}

	public static void glVertexAttribL1i64NV(int index, long x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL1i64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL1i64NV(index, x, function_pointer);
	}
	static native void nglVertexAttribL1i64NV(int index, long x, long function_pointer);

	public static void glVertexAttribL2i64NV(int index, long x, long y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL2i64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL2i64NV(index, x, y, function_pointer);
	}
	static native void nglVertexAttribL2i64NV(int index, long x, long y, long function_pointer);

	public static void glVertexAttribL3i64NV(int index, long x, long y, long z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL3i64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL3i64NV(index, x, y, z, function_pointer);
	}
	static native void nglVertexAttribL3i64NV(int index, long x, long y, long z, long function_pointer);

	public static void glVertexAttribL4i64NV(int index, long x, long y, long z, long w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL4i64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL4i64NV(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttribL4i64NV(int index, long x, long y, long z, long w, long function_pointer);

	public static void glVertexAttribL1NV(int index, LongBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL1i64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 1);
		nglVertexAttribL1i64vNV(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL1i64vNV(int index, long v, long function_pointer);

	public static void glVertexAttribL2NV(int index, LongBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL2i64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 2);
		nglVertexAttribL2i64vNV(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL2i64vNV(int index, long v, long function_pointer);

	public static void glVertexAttribL3NV(int index, LongBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL3i64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 3);
		nglVertexAttribL3i64vNV(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL3i64vNV(int index, long v, long function_pointer);

	public static void glVertexAttribL4NV(int index, LongBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL4i64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribL4i64vNV(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL4i64vNV(int index, long v, long function_pointer);

	public static void glVertexAttribL1ui64NV(int index, long x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL1ui64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL1ui64NV(index, x, function_pointer);
	}
	static native void nglVertexAttribL1ui64NV(int index, long x, long function_pointer);

	public static void glVertexAttribL2ui64NV(int index, long x, long y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL2ui64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL2ui64NV(index, x, y, function_pointer);
	}
	static native void nglVertexAttribL2ui64NV(int index, long x, long y, long function_pointer);

	public static void glVertexAttribL3ui64NV(int index, long x, long y, long z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL3ui64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL3ui64NV(index, x, y, z, function_pointer);
	}
	static native void nglVertexAttribL3ui64NV(int index, long x, long y, long z, long function_pointer);

	public static void glVertexAttribL4ui64NV(int index, long x, long y, long z, long w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL4ui64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL4ui64NV(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttribL4ui64NV(int index, long x, long y, long z, long w, long function_pointer);

	public static void glVertexAttribL1uNV(int index, LongBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL1ui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 1);
		nglVertexAttribL1ui64vNV(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL1ui64vNV(int index, long v, long function_pointer);

	public static void glVertexAttribL2uNV(int index, LongBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL2ui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 2);
		nglVertexAttribL2ui64vNV(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL2ui64vNV(int index, long v, long function_pointer);

	public static void glVertexAttribL3uNV(int index, LongBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL3ui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 3);
		nglVertexAttribL3ui64vNV(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL3ui64vNV(int index, long v, long function_pointer);

	public static void glVertexAttribL4uNV(int index, LongBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL4ui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribL4ui64vNV(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL4ui64vNV(int index, long v, long function_pointer);

	public static void glGetVertexAttribLNV(int index, int pname, LongBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribLi64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribLi64vNV(index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribLi64vNV(int index, int pname, long params, long function_pointer);

	public static void glGetVertexAttribLuNV(int index, int pname, LongBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribLui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribLui64vNV(index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribLui64vNV(int index, int pname, long params, long function_pointer);

	public static void glVertexAttribLFormatNV(int index, int size, int type, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribLFormatNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribLFormatNV(index, size, type, stride, function_pointer);
	}
	static native void nglVertexAttribLFormatNV(int index, int size, int type, int stride, long function_pointer);
}
