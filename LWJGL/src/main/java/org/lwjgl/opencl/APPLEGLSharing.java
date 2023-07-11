/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opencl;

import org.lwjgl.*;
import java.nio.*;

public final class APPLEGLSharing {

	/**
	 *  This enumerated value can be specified as part of the &lt;properties&gt; argument passed to clCreateContext
	 *  to allow OpenCL compliant devices in an existing CGL share group to be used as the devices in
	 *  the newly created CL context. GL objects that were allocated in the given CGL share group can
	 *  now be shared between CL and GL.
	 */
	public static final int CL_CONTEXT_PROPERTY_USE_CGL_SHAREGROUP_APPLE = 0x10000000;

	/**
	 *  Returns a cl_device_id for the CL device associated with the virtual screen for
	 *  the given CGL context.  Return type: cl_device_id
	 */
	public static final int CL_CGL_DEVICE_FOR_CURRENT_VIRTUAL_SCREEN_APPLE = 0x10000002;

	/**
	 *  Returns an array of cl_device_ids for the CL device(s) corresponding to
	 *  the virtual screen(s) for the given CGL context.   Return type: cl_device_id[]
	 */
	public static final int CL_CGL_DEVICES_FOR_SUPPORTED_VIRTUAL_SCREENS_APPLE = 0x10000003;

	/**
	 * Error code returned by clGetGLContextInfoAPPLE if an invalid platform_gl_ctx is provided 
	 */
	public static final int CL_INVALID_GL_CONTEXT_APPLE = 0xFFFFFC18;

	private APPLEGLSharing() {}

	public static int clGetGLContextInfoAPPLE(CLContext context, PointerBuffer platform_gl_ctx, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetGLContextInfoAPPLE;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(platform_gl_ctx, 1);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		if ( param_value_size_ret == null && APIUtil.isDevicesParam(param_name) ) param_value_size_ret = APIUtil.getBufferPointer();
		int __result = nclGetGLContextInfoAPPLE(context.getPointer(), MemoryUtil.getAddress(platform_gl_ctx), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		if ( __result == CL10.CL_SUCCESS && param_value != null && APIUtil.isDevicesParam(param_name) ) context.getParent().registerCLDevices(param_value, param_value_size_ret);
		return __result;
	}
	static native int nclGetGLContextInfoAPPLE(long context, long platform_gl_ctx, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);
}
