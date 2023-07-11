/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVGpuProgram4 {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramivARB: 
	 */
	public static final int GL_PROGRAM_ATTRIB_COMPONENTS_NV = 0x8906,
		GL_PROGRAM_RESULT_COMPONENTS_NV = 0x8907,
		GL_MAX_PROGRAM_ATTRIB_COMPONENTS_NV = 0x8908,
		GL_MAX_PROGRAM_RESULT_COMPONENTS_NV = 0x8909,
		GL_MAX_PROGRAM_GENERIC_ATTRIBS_NV = 0x8DA5,
		GL_MAX_PROGRAM_GENERIC_RESULTS_NV = 0x8DA6;

	private NVGpuProgram4() {}

	public static void glProgramLocalParameterI4iNV(int target, int index, int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramLocalParameterI4iNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramLocalParameterI4iNV(target, index, x, y, z, w, function_pointer);
	}
	static native void nglProgramLocalParameterI4iNV(int target, int index, int x, int y, int z, int w, long function_pointer);

	public static void glProgramLocalParameterI4NV(int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramLocalParameterI4ivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglProgramLocalParameterI4ivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramLocalParameterI4ivNV(int target, int index, long params, long function_pointer);

	public static void glProgramLocalParametersI4NV(int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramLocalParametersI4ivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglProgramLocalParametersI4ivNV(target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramLocalParametersI4ivNV(int target, int index, int params_count, long params, long function_pointer);

	public static void glProgramLocalParameterI4uiNV(int target, int index, int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramLocalParameterI4uiNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramLocalParameterI4uiNV(target, index, x, y, z, w, function_pointer);
	}
	static native void nglProgramLocalParameterI4uiNV(int target, int index, int x, int y, int z, int w, long function_pointer);

	public static void glProgramLocalParameterI4uNV(int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramLocalParameterI4uivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglProgramLocalParameterI4uivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramLocalParameterI4uivNV(int target, int index, long params, long function_pointer);

	public static void glProgramLocalParametersI4uNV(int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramLocalParametersI4uivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglProgramLocalParametersI4uivNV(target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramLocalParametersI4uivNV(int target, int index, int params_count, long params, long function_pointer);

	public static void glProgramEnvParameterI4iNV(int target, int index, int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramEnvParameterI4iNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramEnvParameterI4iNV(target, index, x, y, z, w, function_pointer);
	}
	static native void nglProgramEnvParameterI4iNV(int target, int index, int x, int y, int z, int w, long function_pointer);

	public static void glProgramEnvParameterI4NV(int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramEnvParameterI4ivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglProgramEnvParameterI4ivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramEnvParameterI4ivNV(int target, int index, long params, long function_pointer);

	public static void glProgramEnvParametersI4NV(int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramEnvParametersI4ivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglProgramEnvParametersI4ivNV(target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramEnvParametersI4ivNV(int target, int index, int params_count, long params, long function_pointer);

	public static void glProgramEnvParameterI4uiNV(int target, int index, int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramEnvParameterI4uiNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramEnvParameterI4uiNV(target, index, x, y, z, w, function_pointer);
	}
	static native void nglProgramEnvParameterI4uiNV(int target, int index, int x, int y, int z, int w, long function_pointer);

	public static void glProgramEnvParameterI4uNV(int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramEnvParameterI4uivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglProgramEnvParameterI4uivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramEnvParameterI4uivNV(int target, int index, long params, long function_pointer);

	public static void glProgramEnvParametersI4uNV(int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramEnvParametersI4uivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglProgramEnvParametersI4uivNV(target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramEnvParametersI4uivNV(int target, int index, int params_count, long params, long function_pointer);

	public static void glGetProgramLocalParameterINV(int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramLocalParameterIivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetProgramLocalParameterIivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramLocalParameterIivNV(int target, int index, long params, long function_pointer);

	public static void glGetProgramLocalParameterIuNV(int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramLocalParameterIuivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetProgramLocalParameterIuivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramLocalParameterIuivNV(int target, int index, long params, long function_pointer);

	public static void glGetProgramEnvParameterINV(int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramEnvParameterIivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetProgramEnvParameterIivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramEnvParameterIivNV(int target, int index, long params, long function_pointer);

	public static void glGetProgramEnvParameterIuNV(int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramEnvParameterIuivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetProgramEnvParameterIuivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramEnvParameterIuivNV(int target, int index, long params, long function_pointer);
}
