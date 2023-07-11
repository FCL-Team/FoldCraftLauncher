/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTGpuProgramParameters {

	private EXTGpuProgramParameters() {}

	public static void glProgramEnvParameters4EXT(int target, int index, int count, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramEnvParameters4fvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, count << 2);
		nglProgramEnvParameters4fvEXT(target, index, count, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramEnvParameters4fvEXT(int target, int index, int count, long params, long function_pointer);

	public static void glProgramLocalParameters4EXT(int target, int index, int count, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramLocalParameters4fvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, count << 2);
		nglProgramLocalParameters4fvEXT(target, index, count, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramLocalParameters4fvEXT(int target, int index, int count, long params, long function_pointer);
}
