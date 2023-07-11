/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTProvokingVertex {

	/**
	 * Accepted by the &lt;mode&gt; parameter of ProvokingVertexEXT: 
	 */
	public static final int GL_FIRST_VERTEX_CONVENTION_EXT = 0x8E4D,
		GL_LAST_VERTEX_CONVENTION_EXT = 0x8E4E;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_PROVOKING_VERTEX_EXT = 0x8E4F,
		GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION_EXT = 0x8E4C;

	private EXTProvokingVertex() {}

	public static void glProvokingVertexEXT(int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProvokingVertexEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProvokingVertexEXT(mode, function_pointer);
	}
	static native void nglProvokingVertexEXT(int mode, long function_pointer);
}
