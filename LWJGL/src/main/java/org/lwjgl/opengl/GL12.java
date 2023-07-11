/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

/**
 *  <p/>
 *  The core OpenGL1.2.1 API, with the imaging subset.
 * <p>
 *  @author cix_foo <cix_foo@users.sourceforge.net>
 *  @version $Revision$
 *           $Id$
 */
public final class GL12 {

	public static final int GL_TEXTURE_BINDING_3D = 0x806A,
		GL_PACK_SKIP_IMAGES = 0x806B,
		GL_PACK_IMAGE_HEIGHT = 0x806C,
		GL_UNPACK_SKIP_IMAGES = 0x806D,
		GL_UNPACK_IMAGE_HEIGHT = 0x806E,
		GL_TEXTURE_3D = 0x806F,
		GL_PROXY_TEXTURE_3D = 0x8070,
		GL_TEXTURE_DEPTH = 0x8071,
		GL_TEXTURE_WRAP_R = 0x8072,
		GL_MAX_3D_TEXTURE_SIZE = 0x8073,
		GL_BGR = 0x80E0,
		GL_BGRA = 0x80E1,
		GL_UNSIGNED_BYTE_3_3_2 = 0x8032,
		GL_UNSIGNED_BYTE_2_3_3_REV = 0x8362,
		GL_UNSIGNED_SHORT_5_6_5 = 0x8363,
		GL_UNSIGNED_SHORT_5_6_5_REV = 0x8364,
		GL_UNSIGNED_SHORT_4_4_4_4 = 0x8033,
		GL_UNSIGNED_SHORT_4_4_4_4_REV = 0x8365,
		GL_UNSIGNED_SHORT_5_5_5_1 = 0x8034,
		GL_UNSIGNED_SHORT_1_5_5_5_REV = 0x8366,
		GL_UNSIGNED_INT_8_8_8_8 = 0x8035,
		GL_UNSIGNED_INT_8_8_8_8_REV = 0x8367,
		GL_UNSIGNED_INT_10_10_10_2 = 0x8036,
		GL_UNSIGNED_INT_2_10_10_10_REV = 0x8368,
		GL_RESCALE_NORMAL = 0x803A,
		GL_LIGHT_MODEL_COLOR_CONTROL = 0x81F8,
		GL_SINGLE_COLOR = 0x81F9,
		GL_SEPARATE_SPECULAR_COLOR = 0x81FA,
		GL_CLAMP_TO_EDGE = 0x812F,
		GL_TEXTURE_MIN_LOD = 0x813A,
		GL_TEXTURE_MAX_LOD = 0x813B,
		GL_TEXTURE_BASE_LEVEL = 0x813C,
		GL_TEXTURE_MAX_LEVEL = 0x813D,
		GL_MAX_ELEMENTS_VERTICES = 0x80E8,
		GL_MAX_ELEMENTS_INDICES = 0x80E9,
		GL_ALIASED_POINT_SIZE_RANGE = 0x846D,
		GL_ALIASED_LINE_WIDTH_RANGE = 0x846E,
		GL_SMOOTH_POINT_SIZE_RANGE = 0xB12,
		GL_SMOOTH_POINT_SIZE_GRANULARITY = 0xB13,
		GL_SMOOTH_LINE_WIDTH_RANGE = 0xB22,
		GL_SMOOTH_LINE_WIDTH_GRANULARITY = 0xB23;

	private GL12() {}

	public static void glDrawRangeElements(int mode, int start, int end, ByteBuffer indices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawRangeElements;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawRangeElements(mode, start, end, indices.remaining(), GL11.GL_UNSIGNED_BYTE, MemoryUtil.getAddress(indices), function_pointer);
	}
	public static void glDrawRangeElements(int mode, int start, int end, IntBuffer indices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawRangeElements;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawRangeElements(mode, start, end, indices.remaining(), GL11.GL_UNSIGNED_INT, MemoryUtil.getAddress(indices), function_pointer);
	}
	public static void glDrawRangeElements(int mode, int start, int end, ShortBuffer indices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawRangeElements;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawRangeElements(mode, start, end, indices.remaining(), GL11.GL_UNSIGNED_SHORT, MemoryUtil.getAddress(indices), function_pointer);
	}
	static native void nglDrawRangeElements(int mode, int start, int end, int indices_count, int type, long indices, long function_pointer);
	public static void glDrawRangeElements(int mode, int start, int end, int indices_count, int type, long indices_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawRangeElements;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOenabled(caps);
		nglDrawRangeElementsBO(mode, start, end, indices_count, type, indices_buffer_offset, function_pointer);
	}
	static native void nglDrawRangeElementsBO(int mode, int start, int end, int indices_count, int type, long indices_buffer_offset, long function_pointer);

	public static void glTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	public static void glTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	public static void glTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	public static void glTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	public static void glTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	static native void nglTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, long pixels, long function_pointer);
	public static void glTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTexImage3DBO(target, level, internalFormat, width, height, depth, border, format, type, pixels_buffer_offset, function_pointer);
	}
	static native void nglTexImage3DBO(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	static native void nglTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels, long function_pointer);
	public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTexSubImage3DBO(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_buffer_offset, function_pointer);
	}
	static native void nglTexSubImage3DBO(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glCopyTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyTexSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyTexSubImage3D(target, level, xoffset, yoffset, zoffset, x, y, width, height, function_pointer);
	}
	static native void nglCopyTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height, long function_pointer);
}
