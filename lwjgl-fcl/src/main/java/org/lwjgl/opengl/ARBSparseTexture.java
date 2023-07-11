/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBSparseTexture {

	/**
	 *  Accepted by the &lt;pname&gt; parameter to TexParameter{i f}{v},
	 *  TexParameterI{u}v, GetTexParameter{if}v and GetTexParameterIi{u}v:
	 */
	public static final int GL_TEXTURE_SPARSE_ARB = 0x91A6,
		GL_VIRTUAL_PAGE_SIZE_INDEX_ARB = 0x91A7;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetTexParameter{if}v and
	 *  GetTexParameterIi{u}v:
	 */
	public static final int GL_NUM_SPARSE_LEVELS_ARB = 0x91AA;

	/**
	 * Accepted by the &lt;pname&gt; parameter to GetInternalformativ: 
	 */
	public static final int GL_NUM_VIRTUAL_PAGE_SIZES_ARB = 0x91A8,
		GL_VIRTUAL_PAGE_SIZE_X_ARB = 0x9195,
		GL_VIRTUAL_PAGE_SIZE_Y_ARB = 0x9196,
		GL_VIRTUAL_PAGE_SIZE_Z_ARB = 0x9197;

	/**
	 *  Accepted by the &lt;pname&gt; parameter to GetIntegerv, GetFloatv, GetDoublev,
	 *  GetInteger64v, and GetBooleanv:
	 */
	public static final int GL_MAX_SPARSE_TEXTURE_SIZE_ARB = 0x9198,
		GL_MAX_SPARSE_3D_TEXTURE_SIZE_ARB = 0x9199,
		GL_MAX_SPARSE_ARRAY_TEXTURE_LAYERS_ARB = 0x919A,
		GL_SPARSE_TEXTURE_FULL_ARRAY_CUBE_MIPMAPS_ARB = 0x91A9;

	private ARBSparseTexture() {}

	public static void glTexPageCommitmentARB(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, boolean commit) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexPageCommitmentARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexPageCommitmentARB(target, level, xoffset, yoffset, zoffset, width, height, depth, commit, function_pointer);
	}
	static native void nglTexPageCommitmentARB(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, boolean commit, long function_pointer);

	public static void glTexturePageCommitmentEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, boolean commit) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexturePageCommitmentEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexturePageCommitmentEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, commit, function_pointer);
	}
	static native void nglTexturePageCommitmentEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, boolean commit, long function_pointer);
}
