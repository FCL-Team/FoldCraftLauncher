/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opencl;

import org.lwjgl.*;
import java.nio.*;

public final class KHRGLSharing {

	/**
	 *  Returned by clCreateContext, clCreateContextFromType, and
	 *  clGetGLContextInfoKHR when an invalid OpenGL context or share group
	 *  object handle is specified in &lt;properties&gt;:
	 */
	public static final int CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR = 0xFFFFFC18;

	/**
	 * Accepted as the &lt;param_name&gt; argument of clGetGLContextInfoKHR: 
	 */
	public static final int CL_CURRENT_DEVICE_FOR_GL_CONTEXT_KHR = 0x2006,
		CL_DEVICES_FOR_GL_CONTEXT_KHR = 0x2007;

	/**
	 *  Accepted as an attribute name in the 'properties' argument of
	 *  clCreateContext and clCreateContextFromType:
	 */
	public static final int CL_GL_CONTEXT_KHR = 0x2008,
		CL_EGL_DISPLAY_KHR = 0x2009,
		CL_GLX_DISPLAY_KHR = 0x200A,
		CL_WGL_HDC_KHR = 0x200B,
		CL_CGL_SHAREGROUP_KHR = 0x200C;

	private KHRGLSharing() {}

	public static int clGetGLContextInfoKHR(PointerBuffer properties, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetGLContextInfoKHR;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(properties);
		BufferChecks.checkNullTerminated(properties);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		if ( param_value_size_ret == null && APIUtil.isDevicesParam(param_name) ) param_value_size_ret = APIUtil.getBufferPointer();
		int __result = nclGetGLContextInfoKHR(MemoryUtil.getAddress(properties), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		if ( __result == CL10.CL_SUCCESS && param_value != null && APIUtil.isDevicesParam(param_name) ) APIUtil.getCLPlatform(properties).registerCLDevices(param_value, param_value_size_ret);
		return __result;
	}
	static native int nclGetGLContextInfoKHR(long properties, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);
}
