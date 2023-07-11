/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTStencilTwoSide {

	public static final int GL_STENCIL_TEST_TWO_SIDE_EXT = 0x8910,
		GL_ACTIVE_STENCIL_FACE_EXT = 0x8911;

	private EXTStencilTwoSide() {}

	public static void glActiveStencilFaceEXT(int face) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glActiveStencilFaceEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglActiveStencilFaceEXT(face, function_pointer);
	}
	static native void nglActiveStencilFaceEXT(int face, long function_pointer);
}
