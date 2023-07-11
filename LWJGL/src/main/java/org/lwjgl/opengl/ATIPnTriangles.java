/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ATIPnTriangles {

	public static final int GL_PN_TRIANGLES_ATI = 0x87F0,
		GL_MAX_PN_TRIANGLES_TESSELATION_LEVEL_ATI = 0x87F1,
		GL_PN_TRIANGLES_POINT_MODE_ATI = 0x87F2,
		GL_PN_TRIANGLES_NORMAL_MODE_ATI = 0x87F3,
		GL_PN_TRIANGLES_TESSELATION_LEVEL_ATI = 0x87F4,
		GL_PN_TRIANGLES_POINT_MODE_LINEAR_ATI = 0x87F5,
		GL_PN_TRIANGLES_POINT_MODE_CUBIC_ATI = 0x87F6,
		GL_PN_TRIANGLES_NORMAL_MODE_LINEAR_ATI = 0x87F7,
		GL_PN_TRIANGLES_NORMAL_MODE_QUADRATIC_ATI = 0x87F8;

	private ATIPnTriangles() {}

	public static void glPNTrianglesfATI(int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPNTrianglesfATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPNTrianglesfATI(pname, param, function_pointer);
	}
	static native void nglPNTrianglesfATI(int pname, float param, long function_pointer);

	public static void glPNTrianglesiATI(int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPNTrianglesiATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPNTrianglesiATI(pname, param, function_pointer);
	}
	static native void nglPNTrianglesiATI(int pname, int param, long function_pointer);
}
