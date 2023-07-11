/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ATIEnvmapBumpmap {

	public static final int GL_BUMP_ROT_MATRIX_ATI = 0x8775,
		GL_BUMP_ROT_MATRIX_SIZE_ATI = 0x8776,
		GL_BUMP_NUM_TEX_UNITS_ATI = 0x8777,
		GL_BUMP_TEX_UNITS_ATI = 0x8778,
		GL_DUDV_ATI = 0x8779,
		GL_DU8DV8_ATI = 0x877A,
		GL_BUMP_ENVMAP_ATI = 0x877B,
		GL_BUMP_TARGET_ATI = 0x877C;

	private ATIEnvmapBumpmap() {}

	public static void glTexBumpParameterATI(int pname, FloatBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexBumpParameterfvATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(param, 4);
		nglTexBumpParameterfvATI(pname, MemoryUtil.getAddress(param), function_pointer);
	}
	static native void nglTexBumpParameterfvATI(int pname, long param, long function_pointer);

	public static void glTexBumpParameterATI(int pname, IntBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexBumpParameterivATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(param, 4);
		nglTexBumpParameterivATI(pname, MemoryUtil.getAddress(param), function_pointer);
	}
	static native void nglTexBumpParameterivATI(int pname, long param, long function_pointer);

	public static void glGetTexBumpParameterATI(int pname, FloatBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexBumpParameterfvATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(param, 4);
		nglGetTexBumpParameterfvATI(pname, MemoryUtil.getAddress(param), function_pointer);
	}
	static native void nglGetTexBumpParameterfvATI(int pname, long param, long function_pointer);

	public static void glGetTexBumpParameterATI(int pname, IntBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexBumpParameterivATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(param, 4);
		nglGetTexBumpParameterivATI(pname, MemoryUtil.getAddress(param), function_pointer);
	}
	static native void nglGetTexBumpParameterivATI(int pname, long param, long function_pointer);
}
