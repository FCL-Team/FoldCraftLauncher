/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTCompiledVertexArray {

	public static final int GL_ARRAY_ELEMENT_LOCK_FIRST_EXT = 0x81A8,
		GL_ARRAY_ELEMENT_LOCK_COUNT_EXT = 0x81A9;

	private EXTCompiledVertexArray() {}

	public static void glLockArraysEXT(int first, int count) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLockArraysEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglLockArraysEXT(first, count, function_pointer);
	}
	static native void nglLockArraysEXT(int first, int count, long function_pointer);

	public static void glUnlockArraysEXT() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUnlockArraysEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUnlockArraysEXT(function_pointer);
	}
	static native void nglUnlockArraysEXT(long function_pointer);
}
