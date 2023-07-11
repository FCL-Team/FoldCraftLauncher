/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ATISeparateStencil {

	public static final int GL_STENCIL_BACK_FUNC_ATI = 0x8800,
		GL_STENCIL_BACK_FAIL_ATI = 0x8801,
		GL_STENCIL_BACK_PASS_DEPTH_FAIL_ATI = 0x8802,
		GL_STENCIL_BACK_PASS_DEPTH_PASS_ATI = 0x8803;

	private ATISeparateStencil() {}

	public static void glStencilOpSeparateATI(int face, int sfail, int dpfail, int dppass) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glStencilOpSeparateATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglStencilOpSeparateATI(face, sfail, dpfail, dppass, function_pointer);
	}
	static native void nglStencilOpSeparateATI(int face, int sfail, int dpfail, int dppass, long function_pointer);

	public static void glStencilFuncSeparateATI(int frontfunc, int backfunc, int ref, int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glStencilFuncSeparateATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglStencilFuncSeparateATI(frontfunc, backfunc, ref, mask, function_pointer);
	}
	static native void nglStencilFuncSeparateATI(int frontfunc, int backfunc, int ref, int mask, long function_pointer);
}
