/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBSparseBuffer {

	/**
	 * Accepted as part of the the &lt;flags&gt; parameter to BufferStorage 
	 */
	public static final int GL_SPARSE_STORAGE_BIT_ARB = 0x400;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetFloatv,
	 *  GetIntegerv, and GetInteger64v:
	 */
	public static final int GL_SPARSE_BUFFER_PAGE_SIZE_ARB = 0x82F8;

	private ARBSparseBuffer() {}

	public static void glBufferPageCommitmentARB(int target, long offset, long size, boolean commit) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferPageCommitmentARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBufferPageCommitmentARB(target, offset, size, commit, function_pointer);
	}
	static native void nglBufferPageCommitmentARB(int target, long offset, long size, boolean commit, long function_pointer);
}
