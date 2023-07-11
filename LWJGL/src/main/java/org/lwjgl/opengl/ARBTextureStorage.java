/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBTextureStorage {

	/**
	 * Accepted by the &lt;value&gt; parameter of GetTexParameter{if}v: 
	 */
	public static final int GL_TEXTURE_IMMUTABLE_FORMAT = 0x912F;

	private ARBTextureStorage() {}

	public static void glTexStorage1D(int target, int levels, int internalformat, int width) {
		GL42.glTexStorage1D(target, levels, internalformat, width);
	}

	public static void glTexStorage2D(int target, int levels, int internalformat, int width, int height) {
		GL42.glTexStorage2D(target, levels, internalformat, width, height);
	}

	public static void glTexStorage3D(int target, int levels, int internalformat, int width, int height, int depth) {
		GL42.glTexStorage3D(target, levels, internalformat, width, height, depth);
	}

	public static void glTextureStorage1DEXT(int texture, int target, int levels, int internalformat, int width) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureStorage1DEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureStorage1DEXT(texture, target, levels, internalformat, width, function_pointer);
	}
	static native void nglTextureStorage1DEXT(int texture, int target, int levels, int internalformat, int width, long function_pointer);

	public static void glTextureStorage2DEXT(int texture, int target, int levels, int internalformat, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureStorage2DEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureStorage2DEXT(texture, target, levels, internalformat, width, height, function_pointer);
	}
	static native void nglTextureStorage2DEXT(int texture, int target, int levels, int internalformat, int width, int height, long function_pointer);

	public static void glTextureStorage3DEXT(int texture, int target, int levels, int internalformat, int width, int height, int depth) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureStorage3DEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureStorage3DEXT(texture, target, levels, internalformat, width, height, depth, function_pointer);
	}
	static native void nglTextureStorage3DEXT(int texture, int target, int levels, int internalformat, int width, int height, int depth, long function_pointer);
}
