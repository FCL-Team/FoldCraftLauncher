/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBTextureBufferObject {

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, MapBufferRangeARB, BindTexture, UnmapBuffer,
	 *  GetBufferSubData, GetBufferParameteriv, GetBufferPointerv, and TexBufferARB,
	 *  and the <pname> parameter of GetBooleanv, GetDoublev, GetFloatv, and
	 *  GetIntegerv:
	 */
	public static final int GL_TEXTURE_BUFFER_ARB = 0x8C2A;

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetBooleanv, GetDoublev,
	 *  GetFloatv, and GetIntegerv:
	 */
	public static final int GL_MAX_TEXTURE_BUFFER_SIZE_ARB = 0x8C2B,
		GL_TEXTURE_BINDING_BUFFER_ARB = 0x8C2C,
		GL_TEXTURE_BUFFER_DATA_STORE_BINDING_ARB = 0x8C2D,
		GL_TEXTURE_BUFFER_FORMAT_ARB = 0x8C2E;

	private ARBTextureBufferObject() {}

	public static void glTexBufferARB(int target, int internalformat, int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexBufferARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexBufferARB(target, internalformat, buffer, function_pointer);
	}
	static native void nglTexBufferARB(int target, int internalformat, int buffer, long function_pointer);
}
