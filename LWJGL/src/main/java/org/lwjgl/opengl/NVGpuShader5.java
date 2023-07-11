/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVGpuShader5 {

	/**
	 *  Returned by the &lt;type&gt; parameter of GetActiveAttrib, GetActiveUniform, and
	 *  GetTransformFeedbackVarying:
	 */
	public static final int GL_INT64_NV = 0x140E,
		GL_UNSIGNED_INT64_NV = 0x140F,
		GL_INT8_NV = 0x8FE0,
		GL_INT8_VEC2_NV = 0x8FE1,
		GL_INT8_VEC3_NV = 0x8FE2,
		GL_INT8_VEC4_NV = 0x8FE3,
		GL_INT16_NV = 0x8FE4,
		GL_INT16_VEC2_NV = 0x8FE5,
		GL_INT16_VEC3_NV = 0x8FE6,
		GL_INT16_VEC4_NV = 0x8FE7,
		GL_INT64_VEC2_NV = 0x8FE9,
		GL_INT64_VEC3_NV = 0x8FEA,
		GL_INT64_VEC4_NV = 0x8FEB,
		GL_UNSIGNED_INT8_NV = 0x8FEC,
		GL_UNSIGNED_INT8_VEC2_NV = 0x8FED,
		GL_UNSIGNED_INT8_VEC3_NV = 0x8FEE,
		GL_UNSIGNED_INT8_VEC4_NV = 0x8FEF,
		GL_UNSIGNED_INT16_NV = 0x8FF0,
		GL_UNSIGNED_INT16_VEC2_NV = 0x8FF1,
		GL_UNSIGNED_INT16_VEC3_NV = 0x8FF2,
		GL_UNSIGNED_INT16_VEC4_NV = 0x8FF3,
		GL_UNSIGNED_INT64_VEC2_NV = 0x8FF5,
		GL_UNSIGNED_INT64_VEC3_NV = 0x8FF6,
		GL_UNSIGNED_INT64_VEC4_NV = 0x8FF7,
		GL_FLOAT16_NV = 0x8FF8,
		GL_FLOAT16_VEC2_NV = 0x8FF9,
		GL_FLOAT16_VEC3_NV = 0x8FFA,
		GL_FLOAT16_VEC4_NV = 0x8FFB;

	/**
	 * Accepted by the &lt;primitiveMode&gt; parameter of BeginTransformFeedback: 
	 */
	public static final int GL_PATCHES = 0xE;

	private NVGpuShader5() {}

	public static void glUniform1i64NV(int location, long x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform1i64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform1i64NV(location, x, function_pointer);
	}
	static native void nglUniform1i64NV(int location, long x, long function_pointer);

	public static void glUniform2i64NV(int location, long x, long y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform2i64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform2i64NV(location, x, y, function_pointer);
	}
	static native void nglUniform2i64NV(int location, long x, long y, long function_pointer);

	public static void glUniform3i64NV(int location, long x, long y, long z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform3i64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform3i64NV(location, x, y, z, function_pointer);
	}
	static native void nglUniform3i64NV(int location, long x, long y, long z, long function_pointer);

	public static void glUniform4i64NV(int location, long x, long y, long z, long w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform4i64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform4i64NV(location, x, y, z, w, function_pointer);
	}
	static native void nglUniform4i64NV(int location, long x, long y, long z, long w, long function_pointer);

	public static void glUniform1NV(int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform1i64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform1i64vNV(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform1i64vNV(int location, int value_count, long value, long function_pointer);

	public static void glUniform2NV(int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform2i64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform2i64vNV(location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform2i64vNV(int location, int value_count, long value, long function_pointer);

	public static void glUniform3NV(int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform3i64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform3i64vNV(location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform3i64vNV(int location, int value_count, long value, long function_pointer);

	public static void glUniform4NV(int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform4i64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform4i64vNV(location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform4i64vNV(int location, int value_count, long value, long function_pointer);

	public static void glUniform1ui64NV(int location, long x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform1ui64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform1ui64NV(location, x, function_pointer);
	}
	static native void nglUniform1ui64NV(int location, long x, long function_pointer);

	public static void glUniform2ui64NV(int location, long x, long y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform2ui64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform2ui64NV(location, x, y, function_pointer);
	}
	static native void nglUniform2ui64NV(int location, long x, long y, long function_pointer);

	public static void glUniform3ui64NV(int location, long x, long y, long z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform3ui64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform3ui64NV(location, x, y, z, function_pointer);
	}
	static native void nglUniform3ui64NV(int location, long x, long y, long z, long function_pointer);

	public static void glUniform4ui64NV(int location, long x, long y, long z, long w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform4ui64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform4ui64NV(location, x, y, z, w, function_pointer);
	}
	static native void nglUniform4ui64NV(int location, long x, long y, long z, long w, long function_pointer);

	public static void glUniform1uNV(int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform1ui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform1ui64vNV(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform1ui64vNV(int location, int value_count, long value, long function_pointer);

	public static void glUniform2uNV(int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform2ui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform2ui64vNV(location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform2ui64vNV(int location, int value_count, long value, long function_pointer);

	public static void glUniform3uNV(int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform3ui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform3ui64vNV(location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform3ui64vNV(int location, int value_count, long value, long function_pointer);

	public static void glUniform4uNV(int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform4ui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform4ui64vNV(location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform4ui64vNV(int location, int value_count, long value, long function_pointer);

	public static void glGetUniformNV(int program, int location, LongBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformi64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetUniformi64vNV(program, location, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetUniformi64vNV(int program, int location, long params, long function_pointer);

	public static void glGetUniformuNV(int program, int location, LongBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetUniformui64vNV(program, location, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetUniformui64vNV(int program, int location, long params, long function_pointer);

	public static void glProgramUniform1i64NV(int program, int location, long x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform1i64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform1i64NV(program, location, x, function_pointer);
	}
	static native void nglProgramUniform1i64NV(int program, int location, long x, long function_pointer);

	public static void glProgramUniform2i64NV(int program, int location, long x, long y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform2i64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform2i64NV(program, location, x, y, function_pointer);
	}
	static native void nglProgramUniform2i64NV(int program, int location, long x, long y, long function_pointer);

	public static void glProgramUniform3i64NV(int program, int location, long x, long y, long z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform3i64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform3i64NV(program, location, x, y, z, function_pointer);
	}
	static native void nglProgramUniform3i64NV(int program, int location, long x, long y, long z, long function_pointer);

	public static void glProgramUniform4i64NV(int program, int location, long x, long y, long z, long w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform4i64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform4i64NV(program, location, x, y, z, w, function_pointer);
	}
	static native void nglProgramUniform4i64NV(int program, int location, long x, long y, long z, long w, long function_pointer);

	public static void glProgramUniform1NV(int program, int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform1i64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform1i64vNV(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform1i64vNV(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform2NV(int program, int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform2i64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform2i64vNV(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform2i64vNV(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform3NV(int program, int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform3i64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform3i64vNV(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform3i64vNV(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform4NV(int program, int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform4i64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform4i64vNV(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform4i64vNV(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform1ui64NV(int program, int location, long x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform1ui64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform1ui64NV(program, location, x, function_pointer);
	}
	static native void nglProgramUniform1ui64NV(int program, int location, long x, long function_pointer);

	public static void glProgramUniform2ui64NV(int program, int location, long x, long y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform2ui64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform2ui64NV(program, location, x, y, function_pointer);
	}
	static native void nglProgramUniform2ui64NV(int program, int location, long x, long y, long function_pointer);

	public static void glProgramUniform3ui64NV(int program, int location, long x, long y, long z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform3ui64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform3ui64NV(program, location, x, y, z, function_pointer);
	}
	static native void nglProgramUniform3ui64NV(int program, int location, long x, long y, long z, long function_pointer);

	public static void glProgramUniform4ui64NV(int program, int location, long x, long y, long z, long w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform4ui64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform4ui64NV(program, location, x, y, z, w, function_pointer);
	}
	static native void nglProgramUniform4ui64NV(int program, int location, long x, long y, long z, long w, long function_pointer);

	public static void glProgramUniform1uNV(int program, int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform1ui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform1ui64vNV(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform1ui64vNV(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform2uNV(int program, int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform2ui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform2ui64vNV(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform2ui64vNV(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform3uNV(int program, int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform3ui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform3ui64vNV(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform3ui64vNV(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform4uNV(int program, int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform4ui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform4ui64vNV(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform4ui64vNV(int program, int location, int value_count, long value, long function_pointer);
}
