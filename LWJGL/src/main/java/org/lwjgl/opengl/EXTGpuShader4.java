/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTGpuShader4 {

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetVertexAttribdv,
	 *  GetVertexAttribfv, GetVertexAttribiv, GetVertexAttribIivEXT, and
	 *  GetVertexAttribIuivEXT:
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_INTEGER_EXT = 0x88FD;

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveUniform: 
	 */
	public static final int GL_SAMPLER_1D_ARRAY_EXT = 0x8DC0,
		GL_SAMPLER_2D_ARRAY_EXT = 0x8DC1,
		GL_SAMPLER_BUFFER_EXT = 0x8DC2,
		GL_SAMPLER_1D_ARRAY_SHADOW_EXT = 0x8DC3,
		GL_SAMPLER_2D_ARRAY_SHADOW_EXT = 0x8DC4,
		GL_SAMPLER_CUBE_SHADOW_EXT = 0x8DC5,
		GL_UNSIGNED_INT_VEC2_EXT = 0x8DC6,
		GL_UNSIGNED_INT_VEC3_EXT = 0x8DC7,
		GL_UNSIGNED_INT_VEC4_EXT = 0x8DC8,
		GL_INT_SAMPLER_1D_EXT = 0x8DC9,
		GL_INT_SAMPLER_2D_EXT = 0x8DCA,
		GL_INT_SAMPLER_3D_EXT = 0x8DCB,
		GL_INT_SAMPLER_CUBE_EXT = 0x8DCC,
		GL_INT_SAMPLER_2D_RECT_EXT = 0x8DCD,
		GL_INT_SAMPLER_1D_ARRAY_EXT = 0x8DCE,
		GL_INT_SAMPLER_2D_ARRAY_EXT = 0x8DCF,
		GL_INT_SAMPLER_BUFFER_EXT = 0x8DD0,
		GL_UNSIGNED_INT_SAMPLER_1D_EXT = 0x8DD1,
		GL_UNSIGNED_INT_SAMPLER_2D_EXT = 0x8DD2,
		GL_UNSIGNED_INT_SAMPLER_3D_EXT = 0x8DD3,
		GL_UNSIGNED_INT_SAMPLER_CUBE_EXT = 0x8DD4,
		GL_UNSIGNED_INT_SAMPLER_2D_RECT_EXT = 0x8DD5,
		GL_UNSIGNED_INT_SAMPLER_1D_ARRAY_EXT = 0x8DD6,
		GL_UNSIGNED_INT_SAMPLER_2D_ARRAY_EXT = 0x8DD7,
		GL_UNSIGNED_INT_SAMPLER_BUFFER_EXT = 0x8DD8;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_MIN_PROGRAM_TEXEL_OFFSET_EXT = 0x8904,
		GL_MAX_PROGRAM_TEXEL_OFFSET_EXT = 0x8905;

	private EXTGpuShader4() {}

	public static void glVertexAttribI1iEXT(int index, int x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI1iEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI1iEXT(index, x, function_pointer);
	}
	static native void nglVertexAttribI1iEXT(int index, int x, long function_pointer);

	public static void glVertexAttribI2iEXT(int index, int x, int y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI2iEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI2iEXT(index, x, y, function_pointer);
	}
	static native void nglVertexAttribI2iEXT(int index, int x, int y, long function_pointer);

	public static void glVertexAttribI3iEXT(int index, int x, int y, int z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI3iEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI3iEXT(index, x, y, z, function_pointer);
	}
	static native void nglVertexAttribI3iEXT(int index, int x, int y, int z, long function_pointer);

	public static void glVertexAttribI4iEXT(int index, int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4iEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI4iEXT(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttribI4iEXT(int index, int x, int y, int z, int w, long function_pointer);

	public static void glVertexAttribI1uiEXT(int index, int x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI1uiEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI1uiEXT(index, x, function_pointer);
	}
	static native void nglVertexAttribI1uiEXT(int index, int x, long function_pointer);

	public static void glVertexAttribI2uiEXT(int index, int x, int y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI2uiEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI2uiEXT(index, x, y, function_pointer);
	}
	static native void nglVertexAttribI2uiEXT(int index, int x, int y, long function_pointer);

	public static void glVertexAttribI3uiEXT(int index, int x, int y, int z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI3uiEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI3uiEXT(index, x, y, z, function_pointer);
	}
	static native void nglVertexAttribI3uiEXT(int index, int x, int y, int z, long function_pointer);

	public static void glVertexAttribI4uiEXT(int index, int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4uiEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI4uiEXT(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttribI4uiEXT(int index, int x, int y, int z, int w, long function_pointer);

	public static void glVertexAttribI1EXT(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI1ivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 1);
		nglVertexAttribI1ivEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI1ivEXT(int index, long v, long function_pointer);

	public static void glVertexAttribI2EXT(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI2ivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 2);
		nglVertexAttribI2ivEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI2ivEXT(int index, long v, long function_pointer);

	public static void glVertexAttribI3EXT(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI3ivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 3);
		nglVertexAttribI3ivEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI3ivEXT(int index, long v, long function_pointer);

	public static void glVertexAttribI4EXT(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4ivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribI4ivEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI4ivEXT(int index, long v, long function_pointer);

	public static void glVertexAttribI1uEXT(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI1uivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 1);
		nglVertexAttribI1uivEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI1uivEXT(int index, long v, long function_pointer);

	public static void glVertexAttribI2uEXT(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI2uivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 2);
		nglVertexAttribI2uivEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI2uivEXT(int index, long v, long function_pointer);

	public static void glVertexAttribI3uEXT(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI3uivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 3);
		nglVertexAttribI3uivEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI3uivEXT(int index, long v, long function_pointer);

	public static void glVertexAttribI4uEXT(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4uivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribI4uivEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI4uivEXT(int index, long v, long function_pointer);

	public static void glVertexAttribI4EXT(int index, ByteBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4bvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribI4bvEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI4bvEXT(int index, long v, long function_pointer);

	public static void glVertexAttribI4EXT(int index, ShortBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4svEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribI4svEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI4svEXT(int index, long v, long function_pointer);

	public static void glVertexAttribI4uEXT(int index, ByteBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4ubvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribI4ubvEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI4ubvEXT(int index, long v, long function_pointer);

	public static void glVertexAttribI4uEXT(int index, ShortBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4usvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribI4usvEXT(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI4usvEXT(int index, long v, long function_pointer);

	public static void glVertexAttribIPointerEXT(int index, int size, int type, int stride, ByteBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribIPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribIPointerEXT(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribIPointerEXT(int index, int size, int type, int stride, IntBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribIPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribIPointerEXT(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribIPointerEXT(int index, int size, int type, int stride, ShortBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribIPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribIPointerEXT(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	static native void nglVertexAttribIPointerEXT(int index, int size, int type, int stride, long buffer, long function_pointer);
	public static void glVertexAttribIPointerEXT(int index, int size, int type, int stride, long buffer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribIPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglVertexAttribIPointerEXTBO(index, size, type, stride, buffer_buffer_offset, function_pointer);
	}
	static native void nglVertexAttribIPointerEXTBO(int index, int size, int type, int stride, long buffer_buffer_offset, long function_pointer);

	public static void glGetVertexAttribIEXT(int index, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribIivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribIivEXT(index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribIivEXT(int index, int pname, long params, long function_pointer);

	public static void glGetVertexAttribIuEXT(int index, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribIuivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribIuivEXT(index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribIuivEXT(int index, int pname, long params, long function_pointer);

	public static void glUniform1uiEXT(int location, int v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform1uiEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform1uiEXT(location, v0, function_pointer);
	}
	static native void nglUniform1uiEXT(int location, int v0, long function_pointer);

	public static void glUniform2uiEXT(int location, int v0, int v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform2uiEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform2uiEXT(location, v0, v1, function_pointer);
	}
	static native void nglUniform2uiEXT(int location, int v0, int v1, long function_pointer);

	public static void glUniform3uiEXT(int location, int v0, int v1, int v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform3uiEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform3uiEXT(location, v0, v1, v2, function_pointer);
	}
	static native void nglUniform3uiEXT(int location, int v0, int v1, int v2, long function_pointer);

	public static void glUniform4uiEXT(int location, int v0, int v1, int v2, int v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform4uiEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform4uiEXT(location, v0, v1, v2, v3, function_pointer);
	}
	static native void nglUniform4uiEXT(int location, int v0, int v1, int v2, int v3, long function_pointer);

	public static void glUniform1uEXT(int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform1uivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform1uivEXT(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform1uivEXT(int location, int value_count, long value, long function_pointer);

	public static void glUniform2uEXT(int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform2uivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform2uivEXT(location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform2uivEXT(int location, int value_count, long value, long function_pointer);

	public static void glUniform3uEXT(int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform3uivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform3uivEXT(location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform3uivEXT(int location, int value_count, long value, long function_pointer);

	public static void glUniform4uEXT(int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform4uivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform4uivEXT(location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform4uivEXT(int location, int value_count, long value, long function_pointer);

	public static void glGetUniformuEXT(int program, int location, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformuivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetUniformuivEXT(program, location, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetUniformuivEXT(int program, int location, long params, long function_pointer);

	public static void glBindFragDataLocationEXT(int program, int colorNumber, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindFragDataLocationEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		nglBindFragDataLocationEXT(program, colorNumber, MemoryUtil.getAddress(name), function_pointer);
	}
	static native void nglBindFragDataLocationEXT(int program, int colorNumber, long name, long function_pointer);

	/** Overloads glBindFragDataLocationEXT. */
	public static void glBindFragDataLocationEXT(int program, int colorNumber, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindFragDataLocationEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindFragDataLocationEXT(program, colorNumber, APIUtil.getBufferNT(caps, name), function_pointer);
	}

	public static int glGetFragDataLocationEXT(int program, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFragDataLocationEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetFragDataLocationEXT(program, MemoryUtil.getAddress(name), function_pointer);
		return __result;
	}
	static native int nglGetFragDataLocationEXT(int program, long name, long function_pointer);

	/** Overloads glGetFragDataLocationEXT. */
	public static int glGetFragDataLocationEXT(int program, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFragDataLocationEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetFragDataLocationEXT(program, APIUtil.getBufferNT(caps, name), function_pointer);
		return __result;
	}
}
