/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVDepthBufferFloat {

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of TexImage1D, TexImage2D,
	 *  TexImage3D, CopyTexImage1D, CopyTexImage2D, and RenderbufferStorageEXT,
	 *  and returned in the &lt;data&gt; parameter of GetTexLevelParameter and
	 *  GetRenderbufferParameterivEXT:
	 */
	public static final int GL_DEPTH_COMPONENT32F_NV = 0x8DAB,
		GL_DEPTH32F_STENCIL8_NV = 0x8DAC;

	/**
	 *  Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels, TexImage1D,
	 *  TexImage2D, TexImage3D, TexSubImage1D, TexSubImage2D, TexSubImage3D, and
	 *  GetTexImage:
	 */
	public static final int GL_FLOAT_32_UNSIGNED_INT_24_8_REV_NV = 0x8DAD;

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_DEPTH_BUFFER_FLOAT_MODE_NV = 0x8DAF;

	private NVDepthBufferFloat() {}

	public static void glDepthRangedNV(double n, double f) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDepthRangedNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDepthRangedNV(n, f, function_pointer);
	}
	static native void nglDepthRangedNV(double n, double f, long function_pointer);

	public static void glClearDepthdNV(double d) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearDepthdNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClearDepthdNV(d, function_pointer);
	}
	static native void nglClearDepthdNV(double d, long function_pointer);

	public static void glDepthBoundsdNV(double zmin, double zmax) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDepthBoundsdNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDepthBoundsdNV(zmin, zmax, function_pointer);
	}
	static native void nglDepthBoundsdNV(double zmin, double zmax, long function_pointer);
}
