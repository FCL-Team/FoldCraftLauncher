/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVGeometryProgram4 {

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled, and by
	 *  the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 *  GetDoublev:
	 */
	public static final int GL_GEOMETRY_PROGRAM_NV = 0x8C26;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramivARB: 
	 */
	public static final int GL_MAX_PROGRAM_OUTPUT_VERTICES_NV = 0x8C27,
		GL_MAX_PROGRAM_TOTAL_OUTPUT_COMPONENTS_NV = 0x8C28;

	private NVGeometryProgram4() {}

	public static void glProgramVertexLimitNV(int target, int limit) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramVertexLimitNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramVertexLimitNV(target, limit, function_pointer);
	}
	static native void nglProgramVertexLimitNV(int target, int limit, long function_pointer);

	public static void glFramebufferTextureEXT(int target, int attachment, int texture, int level) {
		EXTGeometryShader4.glFramebufferTextureEXT(target, attachment, texture, level);
	}

	public static void glFramebufferTextureLayerEXT(int target, int attachment, int texture, int level, int layer) {
		EXTGeometryShader4.glFramebufferTextureLayerEXT(target, attachment, texture, level, layer);
	}

	public static void glFramebufferTextureFaceEXT(int target, int attachment, int texture, int level, int face) {
		EXTGeometryShader4.glFramebufferTextureFaceEXT(target, attachment, texture, level, face);
	}
}
