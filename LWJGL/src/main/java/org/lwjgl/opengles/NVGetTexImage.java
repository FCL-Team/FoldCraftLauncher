/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class NVGetTexImage {

	public static final int GL_TEXTURE_WIDTH_NV = 0x1000,
		GL_TEXTURE_HEIGHT_NV = 0x1001,
		GL_TEXTURE_INTERNAL_FORMAT_NV = 0x1003,
		GL_TEXTURE_COMPONENTS_NV = 0x1003,
		GL_TEXTURE_BORDER_NV = 0x1005,
		GL_TEXTURE_RED_SIZE_NV = 0x805C,
		GL_TEXTURE_GREEN_SIZE_NV = 0x805D,
		GL_TEXTURE_BLUE_SIZE_NV = 0x805E,
		GL_TEXTURE_ALPHA_SIZE_NV = 0x805F,
		GL_TEXTURE_LUMINANCE_SIZE_NV = 0x8060,
		GL_TEXTURE_INTENSITY_SIZE_NV = 0x8061,
		GL_TEXTURE_DEPTH_NV = 0x8071,
		GL_TEXTURE_COMPRESSED_IMAGE_SIZE_NV = 0x86A0,
		GL_TEXTURE_COMPRESSED_NV = 0x86A1,
		GL_TEXTURE_DEPTH_SIZE_NV = 0x884A;

	private NVGetTexImage() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glGetTexImageNV(int target, int level, int format, int type, ByteBuffer img) {
		BufferChecks.checkBuffer(img, GLChecks.calculateImageStorage(img, format, type, 1, 1, 1));
		nglGetTexImageNV(target, level, format, type, MemoryUtil.getAddress(img));
	}
	public static void glGetTexImageNV(int target, int level, int format, int type, FloatBuffer img) {
		BufferChecks.checkBuffer(img, GLChecks.calculateImageStorage(img, format, type, 1, 1, 1));
		nglGetTexImageNV(target, level, format, type, MemoryUtil.getAddress(img));
	}
	public static void glGetTexImageNV(int target, int level, int format, int type, IntBuffer img) {
		BufferChecks.checkBuffer(img, GLChecks.calculateImageStorage(img, format, type, 1, 1, 1));
		nglGetTexImageNV(target, level, format, type, MemoryUtil.getAddress(img));
	}
	public static void glGetTexImageNV(int target, int level, int format, int type, ShortBuffer img) {
		BufferChecks.checkBuffer(img, GLChecks.calculateImageStorage(img, format, type, 1, 1, 1));
		nglGetTexImageNV(target, level, format, type, MemoryUtil.getAddress(img));
	}
	static native void nglGetTexImageNV(int target, int level, int format, int type, long img);

	public static void glGetCompressedTexImageNV(int target, int level, ByteBuffer img) {
		BufferChecks.checkDirect(img);
		nglGetCompressedTexImageNV(target, level, MemoryUtil.getAddress(img));
	}
	static native void nglGetCompressedTexImageNV(int target, int level, long img);

	public static void glGetTexLevelParameterNV(int target, int level, int pname, FloatBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetTexLevelParameterfvNV(target, level, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetTexLevelParameterfvNV(int target, int level, int pname, long params);

	/** Overloads glGetTexLevelParameterfvNV. */
	public static float glGetTexLevelParameterfNV(int target, int level, int pname) {
		FloatBuffer params = APIUtil.getBufferFloat();
		nglGetTexLevelParameterfvNV(target, level, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGetTexLevelParameterNV(int target, int level, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetTexLevelParameterivNV(target, level, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetTexLevelParameterivNV(int target, int level, int pname, long params);

	/** Overloads glGetTexLevelParameterivNV. */
	public static int glGetTexLevelParameteriNV(int target, int level, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetTexLevelParameterivNV(target, level, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}
}
