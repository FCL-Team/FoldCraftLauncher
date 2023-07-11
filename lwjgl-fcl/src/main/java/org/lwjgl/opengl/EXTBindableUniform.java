/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTBindableUniform {

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_MAX_VERTEX_BINDABLE_UNIFORMS_EXT = 0x8DE2,
		GL_MAX_FRAGMENT_BINDABLE_UNIFORMS_EXT = 0x8DE3,
		GL_MAX_GEOMETRY_BINDABLE_UNIFORMS_EXT = 0x8DE4,
		GL_MAX_BINDABLE_UNIFORM_SIZE_EXT = 0x8DED,
		GL_UNIFORM_BUFFER_BINDING_EXT = 0x8DEF;

	/**
	 *  Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData, and
	 *  GetBufferPointerv:
	 */
	public static final int GL_UNIFORM_BUFFER_EXT = 0x8DEE;

	private EXTBindableUniform() {}

	public static void glUniformBufferEXT(int program, int location, int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformBufferEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniformBufferEXT(program, location, buffer, function_pointer);
	}
	static native void nglUniformBufferEXT(int program, int location, int buffer, long function_pointer);

	public static int glGetUniformBufferSizeEXT(int program, int location) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformBufferSizeEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetUniformBufferSizeEXT(program, location, function_pointer);
		return __result;
	}
	static native int nglGetUniformBufferSizeEXT(int program, int location, long function_pointer);

	public static long glGetUniformOffsetEXT(int program, int location) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformOffsetEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		long __result = nglGetUniformOffsetEXT(program, location, function_pointer);
		return __result;
	}
	static native long nglGetUniformOffsetEXT(int program, int location, long function_pointer);
}
