/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opencl;

import org.lwjgl.*;
import java.nio.*;

public final class KHRSubgroups {

	private KHRSubgroups() {}

	public static int clGetKernelSubGroupInfoKHR(CLKernel kernel, CLDevice device, int param_name, ByteBuffer input_value, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetKernelSubGroupInfoKHR;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (input_value != null)
			BufferChecks.checkDirect(input_value);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetKernelSubGroupInfoKHR(kernel.getPointer(), device == null ? 0 : device.getPointer(), param_name, (input_value == null ? 0 : input_value.remaining()), MemoryUtil.getAddressSafe(input_value), (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetKernelSubGroupInfoKHR(long kernel, long device, int param_name, long input_value_input_value_size, long input_value, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);
}
