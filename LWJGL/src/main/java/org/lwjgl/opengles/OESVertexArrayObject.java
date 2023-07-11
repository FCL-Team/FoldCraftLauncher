/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class OESVertexArrayObject {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv: 
	 */
	public static final int GL_VERTEX_ARRAY_BINDING_OES = 0x85B5;

	private OESVertexArrayObject() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glBindVertexArrayOES(int array) {
		nglBindVertexArrayOES(array);
	}
	static native void nglBindVertexArrayOES(int array);

	public static void glDeleteVertexArraysOES(IntBuffer arrays) {
		BufferChecks.checkDirect(arrays);
		nglDeleteVertexArraysOES(arrays.remaining(), MemoryUtil.getAddress(arrays));
	}
	static native void nglDeleteVertexArraysOES(int arrays_n, long arrays);

	/** Overloads glDeleteVertexArraysOES. */
	public static void glDeleteVertexArraysOES(int array) {
		nglDeleteVertexArraysOES(1, APIUtil.getInt(array));
	}

	public static void glGenVertexArraysOES(IntBuffer arrays) {
		BufferChecks.checkDirect(arrays);
		nglGenVertexArraysOES(arrays.remaining(), MemoryUtil.getAddress(arrays));
	}
	static native void nglGenVertexArraysOES(int arrays_n, long arrays);

	/** Overloads glGenVertexArraysOES. */
	public static int glGenVertexArraysOES() {
		IntBuffer arrays = APIUtil.getBufferInt();
		nglGenVertexArraysOES(1, MemoryUtil.getAddress(arrays));
		return arrays.get(0);
	}

	public static boolean glIsVertexArrayOES(int array) {
		boolean __result = nglIsVertexArrayOES(array);
		return __result;
	}
	static native boolean nglIsVertexArrayOES(int array);
}
