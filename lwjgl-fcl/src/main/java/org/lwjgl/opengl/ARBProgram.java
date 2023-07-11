/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public class ARBProgram {

	/**
	 * Accepted by the &lt;format&gt; parameter of ProgramStringARB: 
	 */
	public static final int GL_PROGRAM_FORMAT_ASCII_ARB = 0x8875;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramivARB: 
	 */
	public static final int GL_PROGRAM_LENGTH_ARB = 0x8627,
		GL_PROGRAM_FORMAT_ARB = 0x8876,
		GL_PROGRAM_BINDING_ARB = 0x8677,
		GL_PROGRAM_INSTRUCTIONS_ARB = 0x88A0,
		GL_MAX_PROGRAM_INSTRUCTIONS_ARB = 0x88A1,
		GL_PROGRAM_NATIVE_INSTRUCTIONS_ARB = 0x88A2,
		GL_MAX_PROGRAM_NATIVE_INSTRUCTIONS_ARB = 0x88A3,
		GL_PROGRAM_TEMPORARIES_ARB = 0x88A4,
		GL_MAX_PROGRAM_TEMPORARIES_ARB = 0x88A5,
		GL_PROGRAM_NATIVE_TEMPORARIES_ARB = 0x88A6,
		GL_MAX_PROGRAM_NATIVE_TEMPORARIES_ARB = 0x88A7,
		GL_PROGRAM_PARAMETERS_ARB = 0x88A8,
		GL_MAX_PROGRAM_PARAMETERS_ARB = 0x88A9,
		GL_PROGRAM_NATIVE_PARAMETERS_ARB = 0x88AA,
		GL_MAX_PROGRAM_NATIVE_PARAMETERS_ARB = 0x88AB,
		GL_PROGRAM_ATTRIBS_ARB = 0x88AC,
		GL_MAX_PROGRAM_ATTRIBS_ARB = 0x88AD,
		GL_PROGRAM_NATIVE_ATTRIBS_ARB = 0x88AE,
		GL_MAX_PROGRAM_NATIVE_ATTRIBS_ARB = 0x88AF,
		GL_MAX_PROGRAM_LOCAL_PARAMETERS_ARB = 0x88B4,
		GL_MAX_PROGRAM_ENV_PARAMETERS_ARB = 0x88B5,
		GL_PROGRAM_UNDER_NATIVE_LIMITS_ARB = 0x88B6;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramStringARB: 
	 */
	public static final int GL_PROGRAM_STRING_ARB = 0x8628;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_PROGRAM_ERROR_POSITION_ARB = 0x864B,
		GL_CURRENT_MATRIX_ARB = 0x8641,
		GL_TRANSPOSE_CURRENT_MATRIX_ARB = 0x88B7,
		GL_CURRENT_MATRIX_STACK_DEPTH_ARB = 0x8640,
		GL_MAX_PROGRAM_MATRICES_ARB = 0x862F,
		GL_MAX_PROGRAM_MATRIX_STACK_DEPTH_ARB = 0x862E;

	/**
	 * Accepted by the &lt;name&gt; parameter of GetString: 
	 */
	public static final int GL_PROGRAM_ERROR_STRING_ARB = 0x8874;

	/**
	 * Accepted by the &lt;mode&gt; parameter of MatrixMode: 
	 */
	public static final int GL_MATRIX0_ARB = 0x88C0,
		GL_MATRIX1_ARB = 0x88C1,
		GL_MATRIX2_ARB = 0x88C2,
		GL_MATRIX3_ARB = 0x88C3,
		GL_MATRIX4_ARB = 0x88C4,
		GL_MATRIX5_ARB = 0x88C5,
		GL_MATRIX6_ARB = 0x88C6,
		GL_MATRIX7_ARB = 0x88C7,
		GL_MATRIX8_ARB = 0x88C8,
		GL_MATRIX9_ARB = 0x88C9,
		GL_MATRIX10_ARB = 0x88CA,
		GL_MATRIX11_ARB = 0x88CB,
		GL_MATRIX12_ARB = 0x88CC,
		GL_MATRIX13_ARB = 0x88CD,
		GL_MATRIX14_ARB = 0x88CE,
		GL_MATRIX15_ARB = 0x88CF,
		GL_MATRIX16_ARB = 0x88D0,
		GL_MATRIX17_ARB = 0x88D1,
		GL_MATRIX18_ARB = 0x88D2,
		GL_MATRIX19_ARB = 0x88D3,
		GL_MATRIX20_ARB = 0x88D4,
		GL_MATRIX21_ARB = 0x88D5,
		GL_MATRIX22_ARB = 0x88D6,
		GL_MATRIX23_ARB = 0x88D7,
		GL_MATRIX24_ARB = 0x88D8,
		GL_MATRIX25_ARB = 0x88D9,
		GL_MATRIX26_ARB = 0x88DA,
		GL_MATRIX27_ARB = 0x88DB,
		GL_MATRIX28_ARB = 0x88DC,
		GL_MATRIX29_ARB = 0x88DD,
		GL_MATRIX30_ARB = 0x88DE,
		GL_MATRIX31_ARB = 0x88DF;


	public static void glProgramStringARB(int target, int format, ByteBuffer string) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramStringARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(string);
		nglProgramStringARB(target, format, string.remaining(), MemoryUtil.getAddress(string), function_pointer);
	}
	static native void nglProgramStringARB(int target, int format, int string_length, long string, long function_pointer);

	/** Overloads glProgramStringARB. */
	public static void glProgramStringARB(int target, int format, CharSequence string) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramStringARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramStringARB(target, format, string.length(), APIUtil.getBuffer(caps, string), function_pointer);
	}

	public static void glBindProgramARB(int target, int program) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindProgramARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindProgramARB(target, program, function_pointer);
	}
	static native void nglBindProgramARB(int target, int program, long function_pointer);

	public static void glDeleteProgramsARB(IntBuffer programs) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteProgramsARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(programs);
		nglDeleteProgramsARB(programs.remaining(), MemoryUtil.getAddress(programs), function_pointer);
	}
	static native void nglDeleteProgramsARB(int programs_n, long programs, long function_pointer);

	/** Overloads glDeleteProgramsARB. */
	public static void glDeleteProgramsARB(int program) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteProgramsARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteProgramsARB(1, APIUtil.getInt(caps, program), function_pointer);
	}

	public static void glGenProgramsARB(IntBuffer programs) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenProgramsARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(programs);
		nglGenProgramsARB(programs.remaining(), MemoryUtil.getAddress(programs), function_pointer);
	}
	static native void nglGenProgramsARB(int programs_n, long programs, long function_pointer);

	/** Overloads glGenProgramsARB. */
	public static int glGenProgramsARB() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenProgramsARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer programs = APIUtil.getBufferInt(caps);
		nglGenProgramsARB(1, MemoryUtil.getAddress(programs), function_pointer);
		return programs.get(0);
	}

	public static void glProgramEnvParameter4fARB(int target, int index, float x, float y, float z, float w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramEnvParameter4fARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramEnvParameter4fARB(target, index, x, y, z, w, function_pointer);
	}
	static native void nglProgramEnvParameter4fARB(int target, int index, float x, float y, float z, float w, long function_pointer);

	public static void glProgramEnvParameter4dARB(int target, int index, double x, double y, double z, double w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramEnvParameter4dARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramEnvParameter4dARB(target, index, x, y, z, w, function_pointer);
	}
	static native void nglProgramEnvParameter4dARB(int target, int index, double x, double y, double z, double w, long function_pointer);

	public static void glProgramEnvParameter4ARB(int target, int index, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramEnvParameter4fvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglProgramEnvParameter4fvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramEnvParameter4fvARB(int target, int index, long params, long function_pointer);

	public static void glProgramEnvParameter4ARB(int target, int index, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramEnvParameter4dvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglProgramEnvParameter4dvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramEnvParameter4dvARB(int target, int index, long params, long function_pointer);

	public static void glProgramLocalParameter4fARB(int target, int index, float x, float y, float z, float w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramLocalParameter4fARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramLocalParameter4fARB(target, index, x, y, z, w, function_pointer);
	}
	static native void nglProgramLocalParameter4fARB(int target, int index, float x, float y, float z, float w, long function_pointer);

	public static void glProgramLocalParameter4dARB(int target, int index, double x, double y, double z, double w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramLocalParameter4dARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramLocalParameter4dARB(target, index, x, y, z, w, function_pointer);
	}
	static native void nglProgramLocalParameter4dARB(int target, int index, double x, double y, double z, double w, long function_pointer);

	public static void glProgramLocalParameter4ARB(int target, int index, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramLocalParameter4fvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglProgramLocalParameter4fvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramLocalParameter4fvARB(int target, int index, long params, long function_pointer);

	public static void glProgramLocalParameter4ARB(int target, int index, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramLocalParameter4dvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglProgramLocalParameter4dvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglProgramLocalParameter4dvARB(int target, int index, long params, long function_pointer);

	public static void glGetProgramEnvParameterARB(int target, int index, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramEnvParameterfvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetProgramEnvParameterfvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramEnvParameterfvARB(int target, int index, long params, long function_pointer);

	public static void glGetProgramEnvParameterARB(int target, int index, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramEnvParameterdvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetProgramEnvParameterdvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramEnvParameterdvARB(int target, int index, long params, long function_pointer);

	public static void glGetProgramLocalParameterARB(int target, int index, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramLocalParameterfvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetProgramLocalParameterfvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramLocalParameterfvARB(int target, int index, long params, long function_pointer);

	public static void glGetProgramLocalParameterARB(int target, int index, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramLocalParameterdvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetProgramLocalParameterdvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramLocalParameterdvARB(int target, int index, long params, long function_pointer);

	public static void glGetProgramARB(int target, int parameterName, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetProgramivARB(target, parameterName, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramivARB(int target, int parameterName, long params, long function_pointer);

	/**
	 * Overloads glGetProgramivARB.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetProgramiARB} instead. 
	 */
	@Deprecated
	public static int glGetProgramARB(int target, int parameterName) {
		return ARBProgram.glGetProgramiARB(target, parameterName);
	}

	/** Overloads glGetProgramivARB. */
	public static int glGetProgramiARB(int target, int parameterName) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetProgramivARB(target, parameterName, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetProgramStringARB(int target, int parameterName, ByteBuffer paramString) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramStringARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(paramString);
		nglGetProgramStringARB(target, parameterName, MemoryUtil.getAddress(paramString), function_pointer);
	}
	static native void nglGetProgramStringARB(int target, int parameterName, long paramString, long function_pointer);

	/** Overloads glGetProgramStringARB. */
	public static String glGetProgramStringARB(int target, int parameterName) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramStringARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		int programLength = glGetProgramiARB(target, GL_PROGRAM_LENGTH_ARB);
		ByteBuffer paramString = APIUtil.getBufferByte(caps, programLength);
		nglGetProgramStringARB(target, parameterName, MemoryUtil.getAddress(paramString), function_pointer);
		paramString.limit(programLength);
		return APIUtil.getString(caps, paramString);
	}

	public static boolean glIsProgramARB(int program) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsProgramARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsProgramARB(program, function_pointer);
		return __result;
	}
	static native boolean nglIsProgramARB(int program, long function_pointer);
}
