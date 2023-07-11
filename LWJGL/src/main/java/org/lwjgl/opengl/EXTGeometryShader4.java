/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTGeometryShader4 {

	/**
	 *  Accepted by the &lt;type&gt; parameter of CreateShader and returned by the
	 *  &lt;params&gt; parameter of GetShaderiv:
	 */
	public static final int GL_GEOMETRY_SHADER_EXT = 0x8DD9;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of ProgramParameteriEXT and
	 *  GetProgramiv:
	 */
	public static final int GL_GEOMETRY_VERTICES_OUT_EXT = 0x8DDA,
		GL_GEOMETRY_INPUT_TYPE_EXT = 0x8DDB,
		GL_GEOMETRY_OUTPUT_TYPE_EXT = 0x8DDC;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS_EXT = 0x8C29,
		GL_MAX_GEOMETRY_VARYING_COMPONENTS_EXT = 0x8DDD,
		GL_MAX_VERTEX_VARYING_COMPONENTS_EXT = 0x8DDE,
		GL_MAX_VARYING_COMPONENTS_EXT = 0x8B4B,
		GL_MAX_GEOMETRY_UNIFORM_COMPONENTS_EXT = 0x8DDF,
		GL_MAX_GEOMETRY_OUTPUT_VERTICES_EXT = 0x8DE0,
		GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS_EXT = 0x8DE1;

	/**
	 *  Accepted by the &lt;mode&gt; parameter of Begin, DrawArrays,
	 *  MultiDrawArrays, DrawElements, MultiDrawElements, and
	 *  DrawRangeElements:
	 */
	public static final int GL_LINES_ADJACENCY_EXT = 0xA,
		GL_LINE_STRIP_ADJACENCY_EXT = 0xB,
		GL_TRIANGLES_ADJACENCY_EXT = 0xC,
		GL_TRIANGLE_STRIP_ADJACENCY_EXT = 0xD;

	/**
	 * Returned by CheckFramebufferStatusEXT: 
	 */
	public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS_EXT = 0x8DA8,
		GL_FRAMEBUFFER_INCOMPLETE_LAYER_COUNT_EXT = 0x8DA9;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetFramebufferAttachment-
	 *  ParameterivEXT:
	 */
	public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED_EXT = 0x8DA7,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER_EXT = 0x8CD4;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled,
	 *  and by the &lt;pname&gt; parameter of GetIntegerv, GetFloatv, GetDoublev,
	 *  and GetBooleanv:
	 */
	public static final int GL_PROGRAM_POINT_SIZE_EXT = 0x8642;

	private EXTGeometryShader4() {}

	public static void glProgramParameteriEXT(int program, int pname, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramParameteriEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramParameteriEXT(program, pname, value, function_pointer);
	}
	static native void nglProgramParameteriEXT(int program, int pname, int value, long function_pointer);

	public static void glFramebufferTextureEXT(int target, int attachment, int texture, int level) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFramebufferTextureEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTextureEXT(target, attachment, texture, level, function_pointer);
	}
	static native void nglFramebufferTextureEXT(int target, int attachment, int texture, int level, long function_pointer);

	public static void glFramebufferTextureLayerEXT(int target, int attachment, int texture, int level, int layer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFramebufferTextureLayerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTextureLayerEXT(target, attachment, texture, level, layer, function_pointer);
	}
	static native void nglFramebufferTextureLayerEXT(int target, int attachment, int texture, int level, int layer, long function_pointer);

	public static void glFramebufferTextureFaceEXT(int target, int attachment, int texture, int level, int face) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFramebufferTextureFaceEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTextureFaceEXT(target, attachment, texture, level, face, function_pointer);
	}
	static native void nglFramebufferTextureFaceEXT(int target, int attachment, int texture, int level, int face, long function_pointer);
}
