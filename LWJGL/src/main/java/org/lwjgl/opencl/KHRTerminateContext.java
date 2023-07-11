/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opencl;

import org.lwjgl.*;
import java.nio.*;

public final class KHRTerminateContext {

	public static final int CL_DEVICE_TERMINATE_CAPABILITY_KHR = 0x200F,
		CL_CONTEXT_TERMINATE_KHR = 0x2010;

	private KHRTerminateContext() {}

	public static int clTerminateContextKHR(CLContext context) {
		long function_pointer = CLCapabilities.clTerminateContextKHR;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclTerminateContextKHR(context.getPointer(), function_pointer);
		return __result;
	}
	static native int nclTerminateContextKHR(long context, long function_pointer);
}
