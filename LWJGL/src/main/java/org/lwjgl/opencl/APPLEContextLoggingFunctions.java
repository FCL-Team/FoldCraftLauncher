/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opencl;

import org.lwjgl.*;
import java.nio.*;

final class APPLEContextLoggingFunctions {

	private APPLEContextLoggingFunctions() {}

	static void clLogMessagesToSystemLogAPPLE(ByteBuffer errstr, ByteBuffer private_info, ByteBuffer user_data) {
		long function_pointer = CLCapabilities.clLogMessagesToSystemLogAPPLE;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(errstr);
		BufferChecks.checkDirect(private_info);
		BufferChecks.checkDirect(user_data);
		nclLogMessagesToSystemLogAPPLE(MemoryUtil.getAddress(errstr), MemoryUtil.getAddress(private_info), private_info.remaining(), MemoryUtil.getAddress(user_data), function_pointer);
	}
	static native void nclLogMessagesToSystemLogAPPLE(long errstr, long private_info, long private_info_cb, long user_data, long function_pointer);

	static void clLogMessagesToStdoutAPPLE(ByteBuffer errstr, ByteBuffer private_info, ByteBuffer user_data) {
		long function_pointer = CLCapabilities.clLogMessagesToStdoutAPPLE;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(errstr);
		BufferChecks.checkDirect(private_info);
		BufferChecks.checkDirect(user_data);
		nclLogMessagesToStdoutAPPLE(MemoryUtil.getAddress(errstr), MemoryUtil.getAddress(private_info), private_info.remaining(), MemoryUtil.getAddress(user_data), function_pointer);
	}
	static native void nclLogMessagesToStdoutAPPLE(long errstr, long private_info, long private_info_cb, long user_data, long function_pointer);

	static void clLogMessagesToStderrAPPLE(ByteBuffer errstr, ByteBuffer private_info, ByteBuffer user_data) {
		long function_pointer = CLCapabilities.clLogMessagesToStderrAPPLE;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(errstr);
		BufferChecks.checkDirect(private_info);
		BufferChecks.checkDirect(user_data);
		nclLogMessagesToStderrAPPLE(MemoryUtil.getAddress(errstr), MemoryUtil.getAddress(private_info), private_info.remaining(), MemoryUtil.getAddress(user_data), function_pointer);
	}
	static native void nclLogMessagesToStderrAPPLE(long errstr, long private_info, long private_info_cb, long user_data, long function_pointer);
}
