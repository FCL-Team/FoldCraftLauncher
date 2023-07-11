/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class OESTexture3D {

	/**
	 *  Accepted by the &lt;target&gt; parameter of TexImage3DOES, TexSubImage3DOES,
	 *  CopyTexSubImage3DOES, CompressedTexImage3DOES and
	 *  CompressedTexSubImage3DOES, GetTexParameteriv, and GetTexParameterfv:
	 */
	public static final int GL_TEXTURE_3D_OES = 0x806F;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of TexParameteriv, TexParameterfv,
	 *  GetTexParameteriv, and GetTexParameterfv:
	 */
	public static final int GL_TEXTURE_WRAP_R_OES = 0x8072;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, and
	 *  GetFloatv:
	 */
	public static final int GL_MAX_3D_TEXTURE_SIZE_OES = 0x8073,
		GL_TEXTURE_BINDING_3D_OES = 0x806A;

	private OESTexture3D() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glTexImage3DOES(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, ByteBuffer pixels) {
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTexImage3DOES(target, level, internalFormat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels));
	}
	public static void glTexImage3DOES(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, FloatBuffer pixels) {
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTexImage3DOES(target, level, internalFormat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels));
	}
	public static void glTexImage3DOES(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, IntBuffer pixels) {
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTexImage3DOES(target, level, internalFormat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels));
	}
	public static void glTexImage3DOES(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, ShortBuffer pixels) {
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTexImage3DOES(target, level, internalFormat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels));
	}
	static native void nglTexImage3DOES(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, long pixels);

	public static void glTexSubImage3DOES(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTexSubImage3DOES(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels));
	}
	public static void glTexSubImage3DOES(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer pixels) {
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTexSubImage3DOES(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels));
	}
	public static void glTexSubImage3DOES(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer pixels) {
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTexSubImage3DOES(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels));
	}
	public static void glTexSubImage3DOES(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer pixels) {
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTexSubImage3DOES(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels));
	}
	static native void nglTexSubImage3DOES(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels);

	public static void glCopyTexSubImage3DOES(int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height) {
		nglCopyTexSubImage3DOES(target, level, xoffset, yoffset, zoffset, x, y, width, height);
	}
	static native void nglCopyTexSubImage3DOES(int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height);

	public static void glCompressedTexImage3DOES(int target, int level, int internalformat, int width, int height, int depth, int border, int imageSize, ByteBuffer data) {
		BufferChecks.checkDirect(data);
		nglCompressedTexImage3DOES(target, level, internalformat, width, height, depth, border, imageSize, MemoryUtil.getAddress(data));
	}
	static native void nglCompressedTexImage3DOES(int target, int level, int internalformat, int width, int height, int depth, int border, int imageSize, long data);

	public static void glCompressedTexSubImage3DOES(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, ByteBuffer data) {
		BufferChecks.checkDirect(data);
		nglCompressedTexSubImage3DOES(target, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, MemoryUtil.getAddress(data));
	}
	static native void nglCompressedTexSubImage3DOES(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, long data);

	public static void glFramebufferTexture3DOES(int target, int attachment, int textarget, int texture, int level, int zoffset) {
		nglFramebufferTexture3DOES(target, attachment, textarget, texture, level, zoffset);
	}
	static native void nglFramebufferTexture3DOES(int target, int attachment, int textarget, int texture, int level, int zoffset);
}
