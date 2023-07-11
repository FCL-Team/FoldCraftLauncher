/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class QCOMExtendedGet2 {

	private QCOMExtendedGet2() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glExtGetShadersQCOM(IntBuffer shaders, IntBuffer numShaders) {
		BufferChecks.checkBuffer(shaders, 1);
		BufferChecks.checkBuffer(numShaders, 1);
		nglExtGetShadersQCOM(MemoryUtil.getAddress(shaders), shaders.remaining(), MemoryUtil.getAddress(numShaders));
	}
	static native void nglExtGetShadersQCOM(long shaders, int shaders_maxShaders, long numShaders);

	public static void glExtGetProgramsQCOM(IntBuffer programs, IntBuffer numPrograms) {
		BufferChecks.checkBuffer(programs, 1);
		BufferChecks.checkBuffer(numPrograms, 1);
		nglExtGetProgramsQCOM(MemoryUtil.getAddress(programs), programs.remaining(), MemoryUtil.getAddress(numPrograms));
	}
	static native void nglExtGetProgramsQCOM(long programs, int programs_maxPrograms, long numPrograms);

	public static boolean glExtIsProgramBinaryQCOM(int program) {
		boolean __result = nglExtIsProgramBinaryQCOM(program);
		return __result;
	}
	static native boolean nglExtIsProgramBinaryQCOM(int program);

	public static void glExtGetProgramBinarySourceQCOM(int program, int shadertype, ByteBuffer source, IntBuffer length) {
		BufferChecks.checkDirect(source);
		BufferChecks.checkBuffer(length, 1);
		nglExtGetProgramBinarySourceQCOM(program, shadertype, MemoryUtil.getAddress(source), MemoryUtil.getAddress(length));
	}
	static native void nglExtGetProgramBinarySourceQCOM(int program, int shadertype, long source, long length);
}
