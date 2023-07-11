/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTSeparateShaderObjects {

	/**
	 * Accepted by &lt;type&gt; parameter to GetIntegerv and GetFloatv: 
	 */
	public static final int GL_ACTIVE_PROGRAM_EXT = 0x8B8D;

	private EXTSeparateShaderObjects() {}

	public static void glUseShaderProgramEXT(int type, int program) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUseShaderProgramEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUseShaderProgramEXT(type, program, function_pointer);
	}
	static native void nglUseShaderProgramEXT(int type, int program, long function_pointer);

	public static void glActiveProgramEXT(int program) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glActiveProgramEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglActiveProgramEXT(program, function_pointer);
	}
	static native void nglActiveProgramEXT(int program, long function_pointer);

	public static int glCreateShaderProgramEXT(int type, ByteBuffer string) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateShaderProgramEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(string);
		BufferChecks.checkNullTerminated(string);
		int __result = nglCreateShaderProgramEXT(type, MemoryUtil.getAddress(string), function_pointer);
		return __result;
	}
	static native int nglCreateShaderProgramEXT(int type, long string, long function_pointer);

	/** Overloads glCreateShaderProgramEXT. */
	public static int glCreateShaderProgramEXT(int type, CharSequence string) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateShaderProgramEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglCreateShaderProgramEXT(type, APIUtil.getBufferNT(caps, string), function_pointer);
		return __result;
	}
}
