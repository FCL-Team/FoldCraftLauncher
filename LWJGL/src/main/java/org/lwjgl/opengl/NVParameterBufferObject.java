/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVParameterBufferObject {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramivARB: 
	 */
	public static final int GL_MAX_PROGRAM_PARAMETER_BUFFER_BINDINGS_NV = 0x8DA0,
		GL_MAX_PROGRAM_PARAMETER_BUFFER_SIZE_NV = 0x8DA1;

	/**
	 *  Accepted by the &lt;target&gt; parameter of ProgramBufferParametersfvNV,
	 *  ProgramBufferParametersIivNV, and ProgramBufferParametersIuivNV,
	 *  BindBufferRangeNV, BindBufferOffsetNV, BindBufferBaseNV, and BindBuffer
	 *  and the &lt;value&gt; parameter of GetIntegerIndexedvEXT:
	 */
	public static final int GL_VERTEX_PROGRAM_PARAMETER_BUFFER_NV = 0x8DA2,
		GL_GEOMETRY_PROGRAM_PARAMETER_BUFFER_NV = 0x8DA3,
		GL_FRAGMENT_PROGRAM_PARAMETER_BUFFER_NV = 0x8DA4;

	private NVParameterBufferObject() {}

	public static void glProgramBufferParametersNV(int target, int buffer, int index, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramBufferParametersfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglProgramBufferParametersfvNV(target, buffer, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramBufferParametersfvNV(int target, int buffer, int index, int params_count, long params, long function_pointer);

	public static void glProgramBufferParametersINV(int target, int buffer, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramBufferParametersIivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglProgramBufferParametersIivNV(target, buffer, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramBufferParametersIivNV(int target, int buffer, int index, int params_count, long params, long function_pointer);

	public static void glProgramBufferParametersIuNV(int target, int buffer, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramBufferParametersIuivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglProgramBufferParametersIuivNV(target, buffer, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramBufferParametersIuivNV(int target, int buffer, int index, int params_count, long params, long function_pointer);
}
