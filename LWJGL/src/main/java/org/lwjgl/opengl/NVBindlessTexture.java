/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVBindlessTexture {

	private NVBindlessTexture() {}

	public static long glGetTextureHandleNV(int texture) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureHandleNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		long __result = nglGetTextureHandleNV(texture, function_pointer);
		return __result;
	}
	static native long nglGetTextureHandleNV(int texture, long function_pointer);

	public static long glGetTextureSamplerHandleNV(int texture, int sampler) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureSamplerHandleNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		long __result = nglGetTextureSamplerHandleNV(texture, sampler, function_pointer);
		return __result;
	}
	static native long nglGetTextureSamplerHandleNV(int texture, int sampler, long function_pointer);

	public static void glMakeTextureHandleResidentNV(long handle) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMakeTextureHandleResidentNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMakeTextureHandleResidentNV(handle, function_pointer);
	}
	static native void nglMakeTextureHandleResidentNV(long handle, long function_pointer);

	public static void glMakeTextureHandleNonResidentNV(long handle) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMakeTextureHandleNonResidentNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMakeTextureHandleNonResidentNV(handle, function_pointer);
	}
	static native void nglMakeTextureHandleNonResidentNV(long handle, long function_pointer);

	public static long glGetImageHandleNV(int texture, int level, boolean layered, int layer, int format) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetImageHandleNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		long __result = nglGetImageHandleNV(texture, level, layered, layer, format, function_pointer);
		return __result;
	}
	static native long nglGetImageHandleNV(int texture, int level, boolean layered, int layer, int format, long function_pointer);

	public static void glMakeImageHandleResidentNV(long handle, int access) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMakeImageHandleResidentNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMakeImageHandleResidentNV(handle, access, function_pointer);
	}
	static native void nglMakeImageHandleResidentNV(long handle, int access, long function_pointer);

	public static void glMakeImageHandleNonResidentNV(long handle) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMakeImageHandleNonResidentNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMakeImageHandleNonResidentNV(handle, function_pointer);
	}
	static native void nglMakeImageHandleNonResidentNV(long handle, long function_pointer);

	public static void glUniformHandleui64NV(int location, long value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformHandleui64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniformHandleui64NV(location, value, function_pointer);
	}
	static native void nglUniformHandleui64NV(int location, long value, long function_pointer);

	public static void glUniformHandleuNV(int location, LongBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformHandleui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniformHandleui64vNV(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniformHandleui64vNV(int location, int value_count, long value, long function_pointer);

	public static void glProgramUniformHandleui64NV(int program, int location, long value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformHandleui64NV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniformHandleui64NV(program, location, value, function_pointer);
	}
	static native void nglProgramUniformHandleui64NV(int program, int location, long value, long function_pointer);

	public static void glProgramUniformHandleuNV(int program, int location, LongBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformHandleui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(values);
		nglProgramUniformHandleui64vNV(program, location, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglProgramUniformHandleui64vNV(int program, int location, int values_count, long values, long function_pointer);

	public static boolean glIsTextureHandleResidentNV(long handle) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsTextureHandleResidentNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsTextureHandleResidentNV(handle, function_pointer);
		return __result;
	}
	static native boolean nglIsTextureHandleResidentNV(long handle, long function_pointer);

	public static boolean glIsImageHandleResidentNV(long handle) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsImageHandleResidentNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsImageHandleResidentNV(handle, function_pointer);
		return __result;
	}
	static native boolean nglIsImageHandleResidentNV(long handle, long function_pointer);
}
