/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public class NVProgram {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramivNV: 
	 */
	public static final int GL_PROGRAM_TARGET_NV = 0x8646,
		GL_PROGRAM_LENGTH_NV = 0x8627,
		GL_PROGRAM_RESIDENT_NV = 0x8647;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramStringNV: 
	 */
	public static final int GL_PROGRAM_STRING_NV = 0x8628;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_PROGRAM_ERROR_POSITION_NV = 0x864B;

	/**
	 * Accepted by the &lt;name&gt; parameter of GetString: 
	 */
	public static final int GL_PROGRAM_ERROR_STRING_NV = 0x8874;


	public static void glLoadProgramNV(int target, int programID, ByteBuffer string) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLoadProgramNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(string);
		nglLoadProgramNV(target, programID, string.remaining(), MemoryUtil.getAddress(string), function_pointer);
	}
	static native void nglLoadProgramNV(int target, int programID, int string_length, long string, long function_pointer);

	/** Overloads glLoadProgramNV. */
	public static void glLoadProgramNV(int target, int programID, CharSequence string) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLoadProgramNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglLoadProgramNV(target, programID, string.length(), APIUtil.getBuffer(caps, string), function_pointer);
	}

	public static void glBindProgramNV(int target, int programID) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindProgramNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindProgramNV(target, programID, function_pointer);
	}
	static native void nglBindProgramNV(int target, int programID, long function_pointer);

	public static void glDeleteProgramsNV(IntBuffer programs) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteProgramsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(programs);
		nglDeleteProgramsNV(programs.remaining(), MemoryUtil.getAddress(programs), function_pointer);
	}
	static native void nglDeleteProgramsNV(int programs_n, long programs, long function_pointer);

	/** Overloads glDeleteProgramsNV. */
	public static void glDeleteProgramsNV(int program) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteProgramsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteProgramsNV(1, APIUtil.getInt(caps, program), function_pointer);
	}

	public static void glGenProgramsNV(IntBuffer programs) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenProgramsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(programs);
		nglGenProgramsNV(programs.remaining(), MemoryUtil.getAddress(programs), function_pointer);
	}
	static native void nglGenProgramsNV(int programs_n, long programs, long function_pointer);

	/** Overloads glGenProgramsNV. */
	public static int glGenProgramsNV() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenProgramsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer programs = APIUtil.getBufferInt(caps);
		nglGenProgramsNV(1, MemoryUtil.getAddress(programs), function_pointer);
		return programs.get(0);
	}

	public static void glGetProgramNV(int programID, int parameterName, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetProgramivNV(programID, parameterName, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramivNV(int programID, int parameterName, long params, long function_pointer);

	/**
	 * Overloads glGetProgramivNV.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetProgramiNV} instead. 
	 */
	@Deprecated
	public static int glGetProgramNV(int programID, int parameterName) {
		return NVProgram.glGetProgramiNV(programID, parameterName);
	}

	/** Overloads glGetProgramivNV. */
	public static int glGetProgramiNV(int programID, int parameterName) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetProgramivNV(programID, parameterName, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetProgramStringNV(int programID, int parameterName, ByteBuffer paramString) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramStringNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(paramString);
		nglGetProgramStringNV(programID, parameterName, MemoryUtil.getAddress(paramString), function_pointer);
	}
	static native void nglGetProgramStringNV(int programID, int parameterName, long paramString, long function_pointer);

	/** Overloads glGetProgramStringNV. */
	public static String glGetProgramStringNV(int programID, int parameterName) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramStringNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		int programLength = glGetProgramiNV(programID, GL_PROGRAM_LENGTH_NV);
		ByteBuffer paramString = APIUtil.getBufferByte(caps, programLength);
		nglGetProgramStringNV(programID, parameterName, MemoryUtil.getAddress(paramString), function_pointer);
		paramString.limit(programLength);
		return APIUtil.getString(caps, paramString);
	}

	public static boolean glIsProgramNV(int programID) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsProgramNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsProgramNV(programID, function_pointer);
		return __result;
	}
	static native boolean nglIsProgramNV(int programID, long function_pointer);

	public static boolean glAreProgramsResidentNV(IntBuffer programIDs, ByteBuffer programResidences) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glAreProgramsResidentNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(programIDs);
		BufferChecks.checkBuffer(programResidences, programIDs.remaining());
		boolean __result = nglAreProgramsResidentNV(programIDs.remaining(), MemoryUtil.getAddress(programIDs), MemoryUtil.getAddress(programResidences), function_pointer);
		return __result;
	}
	static native boolean nglAreProgramsResidentNV(int programIDs_n, long programIDs, long programResidences, long function_pointer);

	public static void glRequestResidentProgramsNV(IntBuffer programIDs) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRequestResidentProgramsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(programIDs);
		nglRequestResidentProgramsNV(programIDs.remaining(), MemoryUtil.getAddress(programIDs), function_pointer);
	}
	static native void nglRequestResidentProgramsNV(int programIDs_n, long programIDs, long function_pointer);

	/** Overloads glRequestResidentProgramsNV. */
	public static void glRequestResidentProgramsNV(int programID) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRequestResidentProgramsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRequestResidentProgramsNV(1, APIUtil.getInt(caps, programID), function_pointer);
	}
}
