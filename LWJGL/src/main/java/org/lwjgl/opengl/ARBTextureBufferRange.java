/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBTextureBufferRange {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetTexLevelParameter: 
	 */
	public static final int GL_TEXTURE_BUFFER_OFFSET = 0x919D,
		GL_TEXTURE_BUFFER_SIZE = 0x919E;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT = 0x919F;

	private ARBTextureBufferRange() {}

	public static void glTexBufferRange(int target, int internalformat, int buffer, long offset, long size) {
		GL43.glTexBufferRange(target, internalformat, buffer, offset, size);
	}

	public static void glTextureBufferRangeEXT(int texture, int target, int internalformat, int buffer, long offset, long size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureBufferRangeEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureBufferRangeEXT(texture, target, internalformat, buffer, offset, size, function_pointer);
	}
	static native void nglTextureBufferRangeEXT(int texture, int target, int internalformat, int buffer, long offset, long size, long function_pointer);
}
