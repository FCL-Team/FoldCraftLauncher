/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class EXTTextureStorage {

	/**
	 * Accepted by the &lt;value&gt; parameter of GetTexParameter{if}v: 
	 */
	public static final int GL_TEXTURE_IMMUTABLE_FORMAT_EXT = 0x912F;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of TexStorage* when
	 *  implemented on OpenGL ES:
	 */
	public static final int GL_ALPHA8_EXT = 0x803C,
		GL_LUMINANCE8_EXT = 0x8040,
		GL_LUMINANCE8_ALPHA8_EXT = 0x8045,
		GL_RGBA32F_EXT = 0x8814,
		GL_RGB32F_EXT = 0x8815,
		GL_ALPHA32F_EXT = 0x8816,
		GL_LUMINANCE32F_EXT = 0x8818,
		GL_LUMINANCE_ALPHA32F_EXT = 0x8819,
		GL_RGBA16F_EXT = 0x881A,
		GL_RGB16F_EXT = 0x881B,
		GL_ALPHA16F_EXT = 0x881C,
		GL_LUMINANCE16F_EXT = 0x881E,
		GL_LUMINANCE_ALPHA16F_EXT = 0x881F,
		GL_RGB10_A2_EXT = 0x8059,
		GL_RGB10_EXT = 0x8052,
		GL_BGRA8_EXT = 0x93A1;

	private EXTTextureStorage() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glTexStorage1DEXT(int target, int levels, int internalformat, int width) {
		nglTexStorage1DEXT(target, levels, internalformat, width);
	}
	static native void nglTexStorage1DEXT(int target, int levels, int internalformat, int width);

	public static void glTexStorage2DEXT(int target, int levels, int internalformat, int width, int height) {
		nglTexStorage2DEXT(target, levels, internalformat, width, height);
	}
	static native void nglTexStorage2DEXT(int target, int levels, int internalformat, int width, int height);

	public static void glTexStorage3DEXT(int target, int levels, int internalformat, int width, int height, int depth) {
		nglTexStorage3DEXT(target, levels, internalformat, width, height, depth);
	}
	static native void nglTexStorage3DEXT(int target, int levels, int internalformat, int width, int height, int depth);

	public static void glTextureStorage1DEXT(int texture, int target, int levels, int internalformat, int width) {
		nglTextureStorage1DEXT(texture, target, levels, internalformat, width);
	}
	static native void nglTextureStorage1DEXT(int texture, int target, int levels, int internalformat, int width);

	public static void glTextureStorage2DEXT(int texture, int target, int levels, int internalformat, int width, int height) {
		nglTextureStorage2DEXT(texture, target, levels, internalformat, width, height);
	}
	static native void nglTextureStorage2DEXT(int texture, int target, int levels, int internalformat, int width, int height);

	public static void glTextureStorage3DEXT(int texture, int target, int levels, int internalformat, int width, int height, int depth) {
		nglTextureStorage3DEXT(texture, target, levels, internalformat, width, height, depth);
	}
	static native void nglTextureStorage3DEXT(int texture, int target, int levels, int internalformat, int width, int height, int depth);
}
