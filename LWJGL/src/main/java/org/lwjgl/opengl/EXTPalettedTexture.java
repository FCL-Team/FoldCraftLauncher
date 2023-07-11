/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTPalettedTexture {

	/**
	 *  Accepted by the internalformat parameter of TexImage1D, TexImage2D and
	 *  TexImage3DEXT:
	 */
	public static final int GL_COLOR_INDEX1_EXT = 0x80E2,
		GL_COLOR_INDEX2_EXT = 0x80E3,
		GL_COLOR_INDEX4_EXT = 0x80E4,
		GL_COLOR_INDEX8_EXT = 0x80E5,
		GL_COLOR_INDEX12_EXT = 0x80E6,
		GL_COLOR_INDEX16_EXT = 0x80E7;

	/**
	 *  Accepted by the pname parameter of GetColorTableParameterivEXT and
	 *  GetColorTableParameterfvEXT:
	 */
	public static final int GL_COLOR_TABLE_FORMAT_EXT = 0x80D8,
		GL_COLOR_TABLE_WIDTH_EXT = 0x80D9,
		GL_COLOR_TABLE_RED_SIZE_EXT = 0x80DA,
		GL_COLOR_TABLE_GREEN_SIZE_EXT = 0x80DB,
		GL_COLOR_TABLE_BLUE_SIZE_EXT = 0x80DC,
		GL_COLOR_TABLE_ALPHA_SIZE_EXT = 0x80DD,
		GL_COLOR_TABLE_LUMINANCE_SIZE_EXT = 0x80DE,
		GL_COLOR_TABLE_INTENSITY_SIZE_EXT = 0x80DF;

	/**
	 *  Accepted by the value parameter of GetTexLevelParameter{if}v:
	 */
	public static final int GL_TEXTURE_INDEX_SIZE_EXT = 0x80ED;

	private EXTPalettedTexture() {}

	public static void glColorTableEXT(int target, int internalFormat, int width, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, width, 1, 1));
		nglColorTableEXT(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glColorTableEXT(int target, int internalFormat, int width, int format, int type, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, width, 1, 1));
		nglColorTableEXT(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glColorTableEXT(int target, int internalFormat, int width, int format, int type, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, width, 1, 1));
		nglColorTableEXT(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glColorTableEXT(int target, int internalFormat, int width, int format, int type, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, width, 1, 1));
		nglColorTableEXT(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glColorTableEXT(int target, int internalFormat, int width, int format, int type, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, width, 1, 1));
		nglColorTableEXT(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglColorTableEXT(int target, int internalFormat, int width, int format, int type, long data, long function_pointer);

	public static void glColorSubTableEXT(int target, int start, int count, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorSubTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, count, 1, 1));
		nglColorSubTableEXT(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glColorSubTableEXT(int target, int start, int count, int format, int type, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorSubTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, count, 1, 1));
		nglColorSubTableEXT(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glColorSubTableEXT(int target, int start, int count, int format, int type, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorSubTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, count, 1, 1));
		nglColorSubTableEXT(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glColorSubTableEXT(int target, int start, int count, int format, int type, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorSubTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, count, 1, 1));
		nglColorSubTableEXT(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glColorSubTableEXT(int target, int start, int count, int format, int type, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorSubTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, count, 1, 1));
		nglColorSubTableEXT(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglColorSubTableEXT(int target, int start, int count, int format, int type, long data, long function_pointer);

	public static void glGetColorTableEXT(int target, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetColorTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetColorTableEXT(target, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetColorTableEXT(int target, int format, int type, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetColorTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetColorTableEXT(target, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetColorTableEXT(int target, int format, int type, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetColorTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetColorTableEXT(target, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetColorTableEXT(int target, int format, int type, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetColorTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetColorTableEXT(target, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetColorTableEXT(int target, int format, int type, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetColorTableEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetColorTableEXT(target, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglGetColorTableEXT(int target, int format, int type, long data, long function_pointer);

	public static void glGetColorTableParameterEXT(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetColorTableParameterivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetColorTableParameterivEXT(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetColorTableParameterivEXT(int target, int pname, long params, long function_pointer);

	public static void glGetColorTableParameterEXT(int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetColorTableParameterfvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetColorTableParameterfvEXT(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetColorTableParameterfvEXT(int target, int pname, long params, long function_pointer);
}
