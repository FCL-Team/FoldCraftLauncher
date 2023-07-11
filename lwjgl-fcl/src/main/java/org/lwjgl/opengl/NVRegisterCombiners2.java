/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVRegisterCombiners2 {

	public static final int GL_PER_STAGE_CONSTANTS_NV = 0x8535;

	private NVRegisterCombiners2() {}

	public static void glCombinerStageParameterNV(int stage, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCombinerStageParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglCombinerStageParameterfvNV(stage, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglCombinerStageParameterfvNV(int stage, int pname, long params, long function_pointer);

	public static void glGetCombinerStageParameterNV(int stage, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCombinerStageParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetCombinerStageParameterfvNV(stage, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetCombinerStageParameterfvNV(int stage, int pname, long params, long function_pointer);
}
