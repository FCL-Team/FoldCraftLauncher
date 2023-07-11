/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBInstancedArrays {

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetVertexAttribdv,
	 *  GetVertexAttribfv, and GetVertexAttribiv:
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_DIVISOR_ARB = 0x88FE;

	private ARBInstancedArrays() {}

	public static void glVertexAttribDivisorARB(int index, int divisor) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribDivisorARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribDivisorARB(index, divisor, function_pointer);
	}
	static native void nglVertexAttribDivisorARB(int index, int divisor, long function_pointer);
}
