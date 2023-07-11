/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opencl;

import org.lwjgl.*;
import java.nio.*;

/**
 * The core OpenCL 1.2 OpenGL interrop functionality. 
 */
public final class CL12GL {

	public static final int CL_GL_OBJECT_TEXTURE2D_ARRAY = 0x200E,
		CL_GL_OBJECT_TEXTURE1D = 0x200F,
		CL_GL_OBJECT_TEXTURE1D_ARRAY = 0x2010,
		CL_GL_OBJECT_TEXTURE_BUFFER = 0x2011;

	private CL12GL() {}

	public static CLMem clCreateFromGLTexture(CLContext context, long flags, int target, int miplevel, int texture, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateFromGLTexture;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateFromGLTexture(context.getPointer(), flags, target, miplevel, texture, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateFromGLTexture(long context, long flags, int target, int miplevel, int texture, long errcode_ret, long function_pointer);
}
