/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opencl;

import org.lwjgl.*;
import java.nio.*;

/**
 * The core OpenCL 1.1 API 
 */
public final class CL12 {

	/**
	 * Error Codes 
	 */
	public static final int CL_COMPILE_PROGRAM_FAILURE = 0xFFFFFFF1,
		CL_LINKER_NOT_AVAILABLE = 0xFFFFFFF0,
		CL_LINK_PROGRAM_FAILURE = 0xFFFFFFEF,
		CL_DEVICE_PARTITION_FAILED = 0xFFFFFFEE,
		CL_KERNEL_ARG_INFO_NOT_AVAILABLE = 0xFFFFFFED,
		CL_INVALID_IMAGE_DESCRIPTOR = 0xFFFFFFBF,
		CL_INVALID_COMPILER_OPTIONS = 0xFFFFFFBE,
		CL_INVALID_LINKER_OPTIONS = 0xFFFFFFBD,
		CL_INVALID_DEVICE_PARTITION_COUNT = 0xFFFFFFBC;

	/**
	 * OpenCL Version 
	 */
	public static final int CL_VERSION_1_2 = 0x1;

	/**
	 * cl_bool 
	 */
	public static final int CL_BLOCKING = 0x1,
		CL_NON_BLOCKING = 0x0;

	/**
	 * cl_device_type - bitfield 
	 */
	public static final int CL_DEVICE_TYPE_CUSTOM = 0x10,
		CL_DEVICE_DOUBLE_FP_CONFIG = 0x1032,
		CL_DEVICE_LINKER_AVAILABLE = 0x103E,
		CL_DEVICE_BUILT_IN_KERNELS = 0x103F,
		CL_DEVICE_IMAGE_MAX_BUFFER_SIZE = 0x1040,
		CL_DEVICE_IMAGE_MAX_ARRAY_SIZE = 0x1041,
		CL_DEVICE_PARENT_DEVICE = 0x1042,
		CL_DEVICE_PARTITION_MAX_SUB_DEVICES = 0x1043,
		CL_DEVICE_PARTITION_PROPERTIES = 0x1044,
		CL_DEVICE_PARTITION_AFFINITY_DOMAIN = 0x1045,
		CL_DEVICE_PARTITION_TYPE = 0x1046,
		CL_DEVICE_REFERENCE_COUNT = 0x1047,
		CL_DEVICE_PREFERRED_INTEROP_USER_SYNC = 0x1048,
		CL_DEVICE_PRINTF_BUFFER_SIZE = 0x1049,
		CL_FP_CORRECTLY_ROUNDED_DIVIDE_SQRT = 0x80,
		CL_CONTEXT_INTEROP_USER_SYNC = 0x1085,
		CL_DEVICE_PARTITION_EQUALLY = 0x1086,
		CL_DEVICE_PARTITION_BY_COUNTS = 0x1087,
		CL_DEVICE_PARTITION_BY_COUNTS_LIST_END = 0x0,
		CL_DEVICE_PARTITION_BY_AFFINITY_DOMAIN = 0x1088,
		CL_DEVICE_AFFINITY_DOMAIN_NUMA = 0x1,
		CL_DEVICE_AFFINITY_DOMAIN_L4_CACHE = 0x2,
		CL_DEVICE_AFFINITY_DOMAIN_L3_CACHE = 0x4,
		CL_DEVICE_AFFINITY_DOMAIN_L2_CACHE = 0x8,
		CL_DEVICE_AFFINITY_DOMAIN_L1_CACHE = 0x10,
		CL_DEVICE_AFFINITY_DOMAIN_NEXT_PARTITIONABLE = 0x20,
		CL_MEM_HOST_WRITE_ONLY = 0x80,
		CL_MEM_HOST_READ_ONLY = 0x100,
		CL_MEM_HOST_NO_ACCESS = 0x200,
		CL_MIGRATE_MEM_OBJECT_HOST = 0x1,
		CL_MIGRATE_MEM_OBJECT_CONTENT_UNDEFINED = 0x2,
		CL_MEM_OBJECT_IMAGE2D_ARRAY = 0x10F3,
		CL_MEM_OBJECT_IMAGE1D = 0x10F4,
		CL_MEM_OBJECT_IMAGE1D_ARRAY = 0x10F5,
		CL_MEM_OBJECT_IMAGE1D_BUFFER = 0x10F6,
		CL_IMAGE_ARRAY_SIZE = 0x1117,
		CL_IMAGE_BUFFER = 0x1118,
		CL_IMAGE_NUM_MIP_LEVELS = 0x1119,
		CL_IMAGE_NUM_SAMPLES = 0x111A,
		CL_MAP_WRITE_INVALIDATE_REGION = 0x4,
		CL_PROGRAM_NUM_KERNELS = 0x1167,
		CL_PROGRAM_KERNEL_NAMES = 0x1168,
		CL_PROGRAM_BINARY_TYPE = 0x1184,
		CL_PROGRAM_BINARY_TYPE_NONE = 0x0,
		CL_PROGRAM_BINARY_TYPE_COMPILED_OBJECT = 0x1,
		CL_PROGRAM_BINARY_TYPE_LIBRARY = 0x2,
		CL_PROGRAM_BINARY_TYPE_EXECUTABLE = 0x4,
		CL_KERNEL_ATTRIBUTES = 0x1195,
		CL_KERNEL_ARG_ADDRESS_QUALIFIER = 0x1196,
		CL_KERNEL_ARG_ACCESS_QUALIFIER = 0x1197,
		CL_KERNEL_ARG_TYPE_NAME = 0x1198,
		CL_KERNEL_ARG_TYPE_QUALIFIER = 0x1199,
		CL_KERNEL_ARG_NAME = 0x119A,
		CL_KERNEL_ARG_ADDRESS_GLOBAL = 0x119A,
		CL_KERNEL_ARG_ADDRESS_LOCAL = 0x119B,
		CL_KERNEL_ARG_ADDRESS_CONSTANT = 0x119C,
		CL_KERNEL_ARG_ADDRESS_PRIVATE = 0x119D,
		CL_KERNEL_ARG_ACCESS_READ_ONLY = 0x11A0,
		CL_KERNEL_ARG_ACCESS_WRITE_ONLY = 0x11A1,
		CL_KERNEL_ARG_ACCESS_READ_WRITE = 0x11A2,
		CL_KERNEL_ARG_ACCESS_NONE = 0x11A3,
		CL_KERNEL_ARG_TYPE_NONE = 0x0,
		CL_KERNEL_ARG_TYPE_CONST = 0x1,
		CL_KERNEL_ARG_TYPE_RESTRICT = 0x2,
		CL_KERNEL_ARG_TYPE_VOLATILE = 0x4,
		CL_KERNEL_GLOBAL_WORK_SIZE = 0x11B5,
		CL_COMMAND_BARRIER = 0x1205,
		CL_COMMAND_MIGRATE_MEM_OBJECTS = 0x1206,
		CL_COMMAND_FILL_BUFFER = 0x1207,
		CL_COMMAND_FILL_IMAGE = 0x1208;

	private CL12() {}

	public static int clRetainDevice(CLDevice device) {
		long function_pointer = CLCapabilities.clRetainDevice;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclRetainDevice(device.getPointer(), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) device.retain();
		return __result;
	}
	static native int nclRetainDevice(long device, long function_pointer);

	/**
	 *  Warning: LWJGL will not automatically release any objects associated with sub-devices.
	 *  The user is responsible for tracking and releasing everything prior to calling this method.
	 * <p>
	 *  @param device the parent CLDevice
	 * <p>
	 *  @return the error code
	 */
	public static int clReleaseDevice(CLDevice device) {
		long function_pointer = CLCapabilities.clReleaseDevice;
		BufferChecks.checkFunctionAddress(function_pointer);
		APIUtil.releaseObjects(device);
		int __result = nclReleaseDevice(device.getPointer(), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) device.release();
		return __result;
	}
	static native int nclReleaseDevice(long device, long function_pointer);

	public static int clCreateSubDevices(CLDevice in_device, LongBuffer properties, PointerBuffer out_devices, IntBuffer num_devices_ret) {
		long function_pointer = CLCapabilities.clCreateSubDevices;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(properties);
		BufferChecks.checkNullTerminated(properties);
		if (out_devices != null)
			BufferChecks.checkDirect(out_devices);
		if (num_devices_ret != null)
			BufferChecks.checkBuffer(num_devices_ret, 1);
		int __result = nclCreateSubDevices(in_device.getPointer(), MemoryUtil.getAddress(properties), (out_devices == null ? 0 : out_devices.remaining()), MemoryUtil.getAddressSafe(out_devices), MemoryUtil.getAddressSafe(num_devices_ret), function_pointer);
		if ( __result == CL10.CL_SUCCESS && out_devices != null ) in_device.registerSubCLDevices(out_devices);
		return __result;
	}
	static native int nclCreateSubDevices(long in_device, long properties, int out_devices_num_devices, long out_devices, long num_devices_ret, long function_pointer);

	public static CLMem clCreateImage(CLContext context, long flags, ByteBuffer image_format, ByteBuffer image_desc, ByteBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(image_format, 2 * 4);
		BufferChecks.checkBuffer(image_desc, 7 * PointerBuffer.getPointerSize() + 2 * 4 + PointerBuffer.getPointerSize());
		if (host_ptr != null)
			BufferChecks.checkDirect(host_ptr);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateImage(context.getPointer(), flags, MemoryUtil.getAddress(image_format), MemoryUtil.getAddress(image_desc), MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateImage(CLContext context, long flags, ByteBuffer image_format, ByteBuffer image_desc, FloatBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(image_format, 2 * 4);
		BufferChecks.checkBuffer(image_desc, 7 * PointerBuffer.getPointerSize() + 2 * 4 + PointerBuffer.getPointerSize());
		if (host_ptr != null)
			BufferChecks.checkDirect(host_ptr);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateImage(context.getPointer(), flags, MemoryUtil.getAddress(image_format), MemoryUtil.getAddress(image_desc), MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateImage(CLContext context, long flags, ByteBuffer image_format, ByteBuffer image_desc, IntBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(image_format, 2 * 4);
		BufferChecks.checkBuffer(image_desc, 7 * PointerBuffer.getPointerSize() + 2 * 4 + PointerBuffer.getPointerSize());
		if (host_ptr != null)
			BufferChecks.checkDirect(host_ptr);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateImage(context.getPointer(), flags, MemoryUtil.getAddress(image_format), MemoryUtil.getAddress(image_desc), MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateImage(CLContext context, long flags, ByteBuffer image_format, ByteBuffer image_desc, ShortBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(image_format, 2 * 4);
		BufferChecks.checkBuffer(image_desc, 7 * PointerBuffer.getPointerSize() + 2 * 4 + PointerBuffer.getPointerSize());
		if (host_ptr != null)
			BufferChecks.checkDirect(host_ptr);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateImage(context.getPointer(), flags, MemoryUtil.getAddress(image_format), MemoryUtil.getAddress(image_desc), MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateImage(long context, long flags, long image_format, long image_desc, long host_ptr, long errcode_ret, long function_pointer);

	public static CLProgram clCreateProgramWithBuiltInKernels(CLContext context, PointerBuffer device_list, ByteBuffer kernel_names, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateProgramWithBuiltInKernels;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(device_list, 1);
		BufferChecks.checkDirect(kernel_names);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLProgram __result = new CLProgram(nclCreateProgramWithBuiltInKernels(context.getPointer(), device_list.remaining(), MemoryUtil.getAddress(device_list), MemoryUtil.getAddress(kernel_names), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateProgramWithBuiltInKernels(long context, int device_list_num_devices, long device_list, long kernel_names, long errcode_ret, long function_pointer);

	/** Overloads clCreateProgramWithBuiltInKernels. */
	public static CLProgram clCreateProgramWithBuiltInKernels(CLContext context, PointerBuffer device_list, CharSequence kernel_names, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateProgramWithBuiltInKernels;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(device_list, 1);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLProgram __result = new CLProgram(nclCreateProgramWithBuiltInKernels(context.getPointer(), device_list.remaining(), MemoryUtil.getAddress(device_list), APIUtil.getBuffer(kernel_names), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}

	/**
	 * Single null-terminated header include name. 
	 */
	public static int clCompileProgram(CLProgram program, PointerBuffer device_list, ByteBuffer options, PointerBuffer input_header, ByteBuffer header_include_name, CLCompileProgramCallback pfn_notify) {
		long function_pointer = CLCapabilities.clCompileProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (device_list != null)
			BufferChecks.checkDirect(device_list);
		BufferChecks.checkDirect(options);
		BufferChecks.checkNullTerminated(options);
		BufferChecks.checkBuffer(input_header, 1);
		BufferChecks.checkDirect(header_include_name);
		BufferChecks.checkNullTerminated(header_include_name);
		long user_data = CallbackUtil.createGlobalRef(pfn_notify);
		if ( pfn_notify != null ) pfn_notify.setContext(program.getParent());
		int __result = 0;
		try {
			__result = nclCompileProgram(program.getPointer(), (device_list == null ? 0 : device_list.remaining()), MemoryUtil.getAddressSafe(device_list), MemoryUtil.getAddress(options), 1, MemoryUtil.getAddress(input_header), MemoryUtil.getAddress(header_include_name), pfn_notify == null ? 0 : pfn_notify.getPointer(), user_data, function_pointer);
			return __result;
		} finally {
			CallbackUtil.checkCallback(__result, user_data);
		}
	}
	static native int nclCompileProgram(long program, int device_list_num_devices, long device_list, long options, int num_input_headers, long input_header, long header_include_name, long pfn_notify, long user_data, long function_pointer);

	/**
	 * Overloads clCompileProgram.
	 * <p>
	 * Multiple null-terminated header include names, one after the other. 
	 */
	public static int clCompileProgramMulti(CLProgram program, PointerBuffer device_list, ByteBuffer options, PointerBuffer input_headers, ByteBuffer header_include_names, CLCompileProgramCallback pfn_notify) {
		long function_pointer = CLCapabilities.clCompileProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (device_list != null)
			BufferChecks.checkDirect(device_list);
		BufferChecks.checkDirect(options);
		BufferChecks.checkNullTerminated(options);
		BufferChecks.checkBuffer(input_headers, 1);
		BufferChecks.checkDirect(header_include_names);
		BufferChecks.checkNullTerminated(header_include_names, input_headers.remaining());
		long user_data = CallbackUtil.createGlobalRef(pfn_notify);
		if ( pfn_notify != null ) pfn_notify.setContext(program.getParent());
		int __result = 0;
		try {
			__result = nclCompileProgramMulti(program.getPointer(), (device_list == null ? 0 : device_list.remaining()), MemoryUtil.getAddressSafe(device_list), MemoryUtil.getAddress(options), input_headers.remaining(), MemoryUtil.getAddress(input_headers), MemoryUtil.getAddress(header_include_names), pfn_notify == null ? 0 : pfn_notify.getPointer(), user_data, function_pointer);
			return __result;
		} finally {
			CallbackUtil.checkCallback(__result, user_data);
		}
	}
	static native int nclCompileProgramMulti(long program, int device_list_num_devices, long device_list, long options, int input_headers_num_input_headers, long input_headers, long header_include_names, long pfn_notify, long user_data, long function_pointer);

	/** Overloads clCompileProgram. */
	public static int clCompileProgram(CLProgram program, PointerBuffer device_list, ByteBuffer options, PointerBuffer input_headers, ByteBuffer[] header_include_names, CLCompileProgramCallback pfn_notify) {
		long function_pointer = CLCapabilities.clCompileProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (device_list != null)
			BufferChecks.checkDirect(device_list);
		BufferChecks.checkDirect(options);
		BufferChecks.checkNullTerminated(options);
		BufferChecks.checkBuffer(input_headers, header_include_names.length);
		BufferChecks.checkArray(header_include_names, 1);
		long user_data = CallbackUtil.createGlobalRef(pfn_notify);
		if ( pfn_notify != null ) pfn_notify.setContext(program.getParent());
		int __result = 0;
		try {
			__result = nclCompileProgram3(program.getPointer(), (device_list == null ? 0 : device_list.remaining()), MemoryUtil.getAddressSafe(device_list), MemoryUtil.getAddress(options), header_include_names.length, MemoryUtil.getAddress(input_headers), header_include_names, pfn_notify == null ? 0 : pfn_notify.getPointer(), user_data, function_pointer);
			return __result;
		} finally {
			CallbackUtil.checkCallback(__result, user_data);
		}
	}
	static native int nclCompileProgram3(long program, int device_list_num_devices, long device_list, long options, int num_input_headers, long input_headers, ByteBuffer[] header_include_names, long pfn_notify, long user_data, long function_pointer);

	/** Overloads clCompileProgram. */
	public static int clCompileProgram(CLProgram program, PointerBuffer device_list, CharSequence options, PointerBuffer input_header, CharSequence header_include_name, CLCompileProgramCallback pfn_notify) {
		long function_pointer = CLCapabilities.clCompileProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (device_list != null)
			BufferChecks.checkDirect(device_list);
		BufferChecks.checkBuffer(input_header, 1);
		long user_data = CallbackUtil.createGlobalRef(pfn_notify);
		if ( pfn_notify != null ) pfn_notify.setContext(program.getParent());
		int __result = 0;
		try {
			__result = nclCompileProgram(program.getPointer(), (device_list == null ? 0 : device_list.remaining()), MemoryUtil.getAddressSafe(device_list), APIUtil.getBufferNT(options), 1, MemoryUtil.getAddress(input_header), APIUtil.getBufferNT(header_include_name), pfn_notify == null ? 0 : pfn_notify.getPointer(), user_data, function_pointer);
			return __result;
		} finally {
			CallbackUtil.checkCallback(__result, user_data);
		}
	}

	/** Overloads clCompileProgram. */
	public static int clCompileProgram(CLProgram program, PointerBuffer device_list, CharSequence options, PointerBuffer input_header, CharSequence[] header_include_name, CLCompileProgramCallback pfn_notify) {
		long function_pointer = CLCapabilities.clCompileProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (device_list != null)
			BufferChecks.checkDirect(device_list);
		BufferChecks.checkBuffer(input_header, 1);
		BufferChecks.checkArray(header_include_name);
		long user_data = CallbackUtil.createGlobalRef(pfn_notify);
		if ( pfn_notify != null ) pfn_notify.setContext(program.getParent());
		int __result = 0;
		try {
			__result = nclCompileProgramMulti(program.getPointer(), (device_list == null ? 0 : device_list.remaining()), MemoryUtil.getAddressSafe(device_list), APIUtil.getBufferNT(options), input_header.remaining(), MemoryUtil.getAddress(input_header), APIUtil.getBufferNT(header_include_name), pfn_notify == null ? 0 : pfn_notify.getPointer(), user_data, function_pointer);
			return __result;
		} finally {
			CallbackUtil.checkCallback(__result, user_data);
		}
	}

	public static CLProgram clLinkProgram(CLContext context, PointerBuffer device_list, ByteBuffer options, PointerBuffer input_programs, CLLinkProgramCallback pfn_notify, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clLinkProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (device_list != null)
			BufferChecks.checkDirect(device_list);
		BufferChecks.checkDirect(options);
		BufferChecks.checkNullTerminated(options);
		BufferChecks.checkDirect(input_programs);
		BufferChecks.checkBuffer(errcode_ret, 1);
		long user_data = CallbackUtil.createGlobalRef(pfn_notify);
		if ( pfn_notify != null ) pfn_notify.setContext(context);
		CLProgram __result = null;
		try {
			__result = new CLProgram(nclLinkProgram(context.getPointer(), (device_list == null ? 0 : device_list.remaining()), MemoryUtil.getAddressSafe(device_list), MemoryUtil.getAddress(options), input_programs.remaining(), MemoryUtil.getAddress(input_programs), pfn_notify == null ? 0 : pfn_notify.getPointer(), user_data, MemoryUtil.getAddress(errcode_ret), function_pointer), context);
			return __result;
		} finally {
			CallbackUtil.checkCallback(errcode_ret.get(errcode_ret.position()), user_data);
		}
	}
	static native long nclLinkProgram(long context, int device_list_num_devices, long device_list, long options, int input_programs_num_input_programs, long input_programs, long pfn_notify, long user_data, long errcode_ret, long function_pointer);

	/** Overloads clLinkProgram. */
	public static CLProgram clLinkProgram(CLContext context, PointerBuffer device_list, CharSequence options, PointerBuffer input_programs, CLLinkProgramCallback pfn_notify, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clLinkProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (device_list != null)
			BufferChecks.checkDirect(device_list);
		BufferChecks.checkDirect(input_programs);
		BufferChecks.checkBuffer(errcode_ret, 1);
		long user_data = CallbackUtil.createGlobalRef(pfn_notify);
		if ( pfn_notify != null ) pfn_notify.setContext(context);
		CLProgram __result = null;
		try {
			__result = new CLProgram(nclLinkProgram(context.getPointer(), (device_list == null ? 0 : device_list.remaining()), MemoryUtil.getAddressSafe(device_list), APIUtil.getBufferNT(options), input_programs.remaining(), MemoryUtil.getAddress(input_programs), pfn_notify == null ? 0 : pfn_notify.getPointer(), user_data, MemoryUtil.getAddress(errcode_ret), function_pointer), context);
			return __result;
		} finally {
			CallbackUtil.checkCallback(errcode_ret.get(errcode_ret.position()), user_data);
		}
	}

	public static int clUnloadPlatformCompiler(CLPlatform platform) {
		long function_pointer = CLCapabilities.clUnloadPlatformCompiler;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclUnloadPlatformCompiler(platform.getPointer(), function_pointer);
		return __result;
	}
	static native int nclUnloadPlatformCompiler(long platform, long function_pointer);

	public static int clGetKernelArgInfo(CLKernel kernel, int arg_indx, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetKernelArgInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetKernelArgInfo(kernel.getPointer(), arg_indx, param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetKernelArgInfo(long kernel, int arg_indx, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);

	public static int clEnqueueFillBuffer(CLCommandQueue command_queue, CLMem buffer, ByteBuffer pattern, long offset, long size, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueFillBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pattern);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueFillBuffer(command_queue.getPointer(), buffer.getPointer(), MemoryUtil.getAddress(pattern), pattern.remaining(), offset, size, (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		return __result;
	}
	static native int nclEnqueueFillBuffer(long command_queue, long buffer, long pattern, long pattern_pattern_size, long offset, long size, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clEnqueueFillImage(CLCommandQueue command_queue, CLMem image, ByteBuffer fill_color, PointerBuffer origin, PointerBuffer region, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueFillImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(fill_color, 4 * 4);
		BufferChecks.checkBuffer(origin, 3);
		BufferChecks.checkBuffer(region, 3);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueFillImage(command_queue.getPointer(), image.getPointer(), MemoryUtil.getAddress(fill_color), MemoryUtil.getAddress(origin), MemoryUtil.getAddress(region), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		return __result;
	}
	static native int nclEnqueueFillImage(long command_queue, long image, long fill_color, long origin, long region, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clEnqueueMigrateMemObjects(CLCommandQueue command_queue, PointerBuffer mem_objects, long flags, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueMigrateMemObjects;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(mem_objects);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueMigrateMemObjects(command_queue.getPointer(), mem_objects.remaining(), MemoryUtil.getAddress(mem_objects), flags, (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		return __result;
	}
	static native int nclEnqueueMigrateMemObjects(long command_queue, int mem_objects_num_mem_objects, long mem_objects, long flags, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clEnqueueMarkerWithWaitList(CLCommandQueue command_queue, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueMarkerWithWaitList;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueMarkerWithWaitList(command_queue.getPointer(), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		return __result;
	}
	static native int nclEnqueueMarkerWithWaitList(long command_queue, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clEnqueueBarrierWithWaitList(CLCommandQueue command_queue, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueBarrierWithWaitList;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueBarrierWithWaitList(command_queue.getPointer(), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		return __result;
	}
	static native int nclEnqueueBarrierWithWaitList(long command_queue, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clSetPrintfCallback(CLContext context, CLPrintfCallback pfn_notify) {
		long function_pointer = CLCapabilities.clSetPrintfCallback;
		BufferChecks.checkFunctionAddress(function_pointer);
		long user_data = CallbackUtil.createGlobalRef(pfn_notify);
		int __result = 0;
		try {
			__result = nclSetPrintfCallback(context.getPointer(), pfn_notify.getPointer(), user_data, function_pointer);
			return __result;
		} finally {
			context.setPrintfCallback(user_data, __result);
		}
	}
	static native int nclSetPrintfCallback(long context, long pfn_notify, long user_data, long function_pointer);

	static CLFunctionAddress clGetExtensionFunctionAddressForPlatform(CLPlatform platform, ByteBuffer func_name) {
		long function_pointer = CLCapabilities.clGetExtensionFunctionAddressForPlatform;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(func_name);
		BufferChecks.checkNullTerminated(func_name);
		CLFunctionAddress __result = new CLFunctionAddress(nclGetExtensionFunctionAddressForPlatform(platform.getPointer(), MemoryUtil.getAddress(func_name), function_pointer));
		return __result;
	}
	static native long nclGetExtensionFunctionAddressForPlatform(long platform, long func_name, long function_pointer);

	/** Overloads clGetExtensionFunctionAddressForPlatform. */
	static CLFunctionAddress clGetExtensionFunctionAddressForPlatform(CLPlatform platform, CharSequence func_name) {
		long function_pointer = CLCapabilities.clGetExtensionFunctionAddressForPlatform;
		BufferChecks.checkFunctionAddress(function_pointer);
		CLFunctionAddress __result = new CLFunctionAddress(nclGetExtensionFunctionAddressForPlatform(platform.getPointer(), APIUtil.getBufferNT(func_name), function_pointer));
		return __result;
	}
}
