/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBVertexArrayObject {

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_VERTEX_ARRAY_BINDING = 0x85B5;

	private ARBVertexArrayObject() {}

	public static void glBindVertexArray(int array) {
		GL30.glBindVertexArray(array);
	}

	public static void glDeleteVertexArrays(IntBuffer arrays) {
		GL30.glDeleteVertexArrays(arrays);
	}

	/** Overloads glDeleteVertexArrays. */
	public static void glDeleteVertexArrays(int array) {
		GL30.glDeleteVertexArrays(array);
	}

	public static void glGenVertexArrays(IntBuffer arrays) {
		GL30.glGenVertexArrays(arrays);
	}

	/** Overloads glGenVertexArrays. */
	public static int glGenVertexArrays() {
		return GL30.glGenVertexArrays();
	}

	public static boolean glIsVertexArray(int array) {
		return GL30.glIsVertexArray(array);
	}
}
