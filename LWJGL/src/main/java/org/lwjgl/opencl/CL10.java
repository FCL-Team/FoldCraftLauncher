/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opencl;

import org.lwjgl.*;
import java.nio.*;

/**
 * The core OpenCL 1.0 API 
 */
public final class CL10 {

	/**
	 * Error Codes 
	 */
	public static final int CL_SUCCESS = 0x0,
		CL_DEVICE_NOT_FOUND = 0xFFFFFFFF,
		CL_DEVICE_NOT_AVAILABLE = 0xFFFFFFFE,
		CL_COMPILER_NOT_AVAILABLE = 0xFFFFFFFD,
		CL_MEM_OBJECT_ALLOCATION_FAILURE = 0xFFFFFFFC,
		CL_OUT_OF_RESOURCES = 0xFFFFFFFB,
		CL_OUT_OF_HOST_MEMORY = 0xFFFFFFFA,
		CL_PROFILING_INFO_NOT_AVAILABLE = 0xFFFFFFF9,
		CL_MEM_COPY_OVERLAP = 0xFFFFFFF8,
		CL_IMAGE_FORMAT_MISMATCH = 0xFFFFFFF7,
		CL_IMAGE_FORMAT_NOT_SUPPORTED = 0xFFFFFFF6,
		CL_BUILD_PROGRAM_FAILURE = 0xFFFFFFF5,
		CL_MAP_FAILURE = 0xFFFFFFF4,
		CL_INVALID_VALUE = 0xFFFFFFE2,
		CL_INVALID_DEVICE_TYPE = 0xFFFFFFE1,
		CL_INVALID_PLATFORM = 0xFFFFFFE0,
		CL_INVALID_DEVICE = 0xFFFFFFDF,
		CL_INVALID_CONTEXT = 0xFFFFFFDE,
		CL_INVALID_QUEUE_PROPERTIES = 0xFFFFFFDD,
		CL_INVALID_COMMAND_QUEUE = 0xFFFFFFDC,
		CL_INVALID_HOST_PTR = 0xFFFFFFDB,
		CL_INVALID_MEM_OBJECT = 0xFFFFFFDA,
		CL_INVALID_IMAGE_FORMAT_DESCRIPTOR = 0xFFFFFFD9,
		CL_INVALID_IMAGE_SIZE = 0xFFFFFFD8,
		CL_INVALID_SAMPLER = 0xFFFFFFD7,
		CL_INVALID_BINARY = 0xFFFFFFD6,
		CL_INVALID_BUILD_OPTIONS = 0xFFFFFFD5,
		CL_INVALID_PROGRAM = 0xFFFFFFD4,
		CL_INVALID_PROGRAM_EXECUTABLE = 0xFFFFFFD3,
		CL_INVALID_KERNEL_NAME = 0xFFFFFFD2,
		CL_INVALID_KERNEL_DEFINITION = 0xFFFFFFD1,
		CL_INVALID_KERNEL = 0xFFFFFFD0,
		CL_INVALID_ARG_INDEX = 0xFFFFFFCF,
		CL_INVALID_ARG_VALUE = 0xFFFFFFCE,
		CL_INVALID_ARG_SIZE = 0xFFFFFFCD,
		CL_INVALID_KERNEL_ARGS = 0xFFFFFFCC,
		CL_INVALID_WORK_DIMENSION = 0xFFFFFFCB,
		CL_INVALID_WORK_GROUP_SIZE = 0xFFFFFFCA,
		CL_INVALID_WORK_ITEM_SIZE = 0xFFFFFFC9,
		CL_INVALID_GLOBAL_OFFSET = 0xFFFFFFC8,
		CL_INVALID_EVENT_WAIT_LIST = 0xFFFFFFC7,
		CL_INVALID_EVENT = 0xFFFFFFC6,
		CL_INVALID_OPERATION = 0xFFFFFFC5,
		CL_INVALID_GL_OBJECT = 0xFFFFFFC4,
		CL_INVALID_BUFFER_SIZE = 0xFFFFFFC3,
		CL_INVALID_MIP_LEVEL = 0xFFFFFFC2,
		CL_INVALID_GLOBAL_WORK_SIZE = 0xFFFFFFC1;

	/**
	 * OpenCL Version 
	 */
	public static final int CL_VERSION_1_0 = 0x1;

	/**
	 * cl_bool 
	 */
	public static final int CL_FALSE = 0x0,
		CL_TRUE = 0x1;

	/**
	 * cl_platform_info 
	 */
	public static final int CL_PLATFORM_PROFILE = 0x900,
		CL_PLATFORM_VERSION = 0x901,
		CL_PLATFORM_NAME = 0x902,
		CL_PLATFORM_VENDOR = 0x903,
		CL_PLATFORM_EXTENSIONS = 0x904;

	/**
	 * cl_device_type - bitfield 
	 */
	public static final int CL_DEVICE_TYPE_DEFAULT = 0x1,
		CL_DEVICE_TYPE_CPU = 0x2,
		CL_DEVICE_TYPE_GPU = 0x4,
		CL_DEVICE_TYPE_ACCELERATOR = 0x8,
		CL_DEVICE_TYPE_ALL = 0xFFFFFFFF;

	/**
	 * cl_device_info 
	 */
	public static final int CL_DEVICE_TYPE = 0x1000,
		CL_DEVICE_VENDOR_ID = 0x1001,
		CL_DEVICE_MAX_COMPUTE_UNITS = 0x1002,
		CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS = 0x1003,
		CL_DEVICE_MAX_WORK_GROUP_SIZE = 0x1004,
		CL_DEVICE_MAX_WORK_ITEM_SIZES = 0x1005,
		CL_DEVICE_PREFERRED_VECTOR_WIDTH_CHAR = 0x1006,
		CL_DEVICE_PREFERRED_VECTOR_WIDTH_SHORT = 0x1007,
		CL_DEVICE_PREFERRED_VECTOR_WIDTH_ = 0x1008,
		CL_DEVICE_PREFERRED_VECTOR_WIDTH_LONG = 0x1009,
		CL_DEVICE_PREFERRED_VECTOR_WIDTH_FLOAT = 0x100A,
		CL_DEVICE_PREFERRED_VECTOR_WIDTH_DOUBLE = 0x100B,
		CL_DEVICE_MAX_CLOCK_FREQUENCY = 0x100C,
		CL_DEVICE_ADDRESS_BITS = 0x100D,
		CL_DEVICE_MAX_READ_IMAGE_ARGS = 0x100E,
		CL_DEVICE_MAX_WRITE_IMAGE_ARGS = 0x100F,
		CL_DEVICE_MAX_MEM_ALLOC_SIZE = 0x1010,
		CL_DEVICE_IMAGE2D_MAX_WIDTH = 0x1011,
		CL_DEVICE_IMAGE2D_MAX_HEIGHT = 0x1012,
		CL_DEVICE_IMAGE3D_MAX_WIDTH = 0x1013,
		CL_DEVICE_IMAGE3D_MAX_HEIGHT = 0x1014,
		CL_DEVICE_IMAGE3D_MAX_DEPTH = 0x1015,
		CL_DEVICE_IMAGE_SUPPORT = 0x1016,
		CL_DEVICE_MAX_PARAMETER_SIZE = 0x1017,
		CL_DEVICE_MAX_SAMPLERS = 0x1018,
		CL_DEVICE_MEM_BASE_ADDR_ALIGN = 0x1019,
		CL_DEVICE_MIN_DATA_TYPE_ALIGN_SIZE = 0x101A,
		CL_DEVICE_SINGLE_FP_CONFIG = 0x101B,
		CL_DEVICE_GLOBAL_MEM_CACHE_TYPE = 0x101C,
		CL_DEVICE_GLOBAL_MEM_CACHELINE_SIZE = 0x101D,
		CL_DEVICE_GLOBAL_MEM_CACHE_SIZE = 0x101E,
		CL_DEVICE_GLOBAL_MEM_SIZE = 0x101F,
		CL_DEVICE_MAX_CONSTANT_BUFFER_SIZE = 0x1020,
		CL_DEVICE_MAX_CONSTANT_ARGS = 0x1021,
		CL_DEVICE_LOCAL_MEM_TYPE = 0x1022,
		CL_DEVICE_LOCAL_MEM_SIZE = 0x1023,
		CL_DEVICE_ERROR_CORRECTION_SUPPORT = 0x1024,
		CL_DEVICE_PROFILING_TIMER_RESOLUTION = 0x1025,
		CL_DEVICE_ENDIAN_LITTLE = 0x1026,
		CL_DEVICE_AVAILABLE = 0x1027,
		CL_DEVICE_COMPILER_AVAILABLE = 0x1028,
		CL_DEVICE_EXECUTION_CAPABILITIES = 0x1029,
		CL_DEVICE_QUEUE_PROPERTIES = 0x102A,
		CL_DEVICE_NAME = 0x102B,
		CL_DEVICE_VENDOR = 0x102C,
		CL_DRIVER_VERSION = 0x102D,
		CL_DEVICE_PROFILE = 0x102E,
		CL_DEVICE_VERSION = 0x102F,
		CL_DEVICE_EXTENSIONS = 0x1030,
		CL_DEVICE_PLATFORM = 0x1031;

	/**
	 * cl_device_fp_config - bitfield 
	 */
	public static final int CL_FP_DENORM = 0x1,
		CL_FP_INF_NAN = 0x2,
		CL_FP_ROUND_TO_NEAREST = 0x4,
		CL_FP_ROUND_TO_ZERO = 0x8,
		CL_FP_ROUND_TO_INF = 0x10,
		CL_FP_FMA = 0x20;

	/**
	 * cl_device_mem_cache_type 
	 */
	public static final int CL_NONE = 0x0,
		CL_READ_ONLY_CACHE = 0x1,
		CL_READ_WRITE_CACHE = 0x2;

	/**
	 * cl_device_local_mem_type 
	 */
	public static final int CL_LOCAL = 0x1,
		CL_GLOBAL = 0x2;

	/**
	 * cl_device_exec_capabilities - bitfield 
	 */
	public static final int CL_EXEC_KERNEL = 0x1,
		CL_EXEC_NATIVE_KERNEL = 0x2;

	/**
	 * cl_command_queue_properties - bitfield 
	 */
	public static final int CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE = 0x1,
		CL_QUEUE_PROFILING_ENABLE = 0x2;

	/**
	 * cl_context_info 
	 */
	public static final int CL_CONTEXT_REFERENCE_COUNT = 0x1080,
		CL_CONTEXT_DEVICES = 0x1081,
		CL_CONTEXT_PROPERTIES = 0x1082;

	/**
	 * cl_context_info + cl_context_properties 
	 */
	public static final int CL_CONTEXT_PLATFORM = 0x1084;

	/**
	 * cl_command_queue_info 
	 */
	public static final int CL_QUEUE_CONTEXT = 0x1090,
		CL_QUEUE_DEVICE = 0x1091,
		CL_QUEUE_REFERENCE_COUNT = 0x1092,
		CL_QUEUE_PROPERTIES = 0x1093;

	/**
	 * cl_mem_flags - bitfield 
	 */
	public static final int CL_MEM_READ_WRITE = 0x1,
		CL_MEM_WRITE_ONLY = 0x2,
		CL_MEM_READ_ONLY = 0x4,
		CL_MEM_USE_HOST_PTR = 0x8,
		CL_MEM_ALLOC_HOST_PTR = 0x10,
		CL_MEM_COPY_HOST_PTR = 0x20;

	/**
	 * cl_channel_order 
	 */
	public static final int CL_R = 0x10B0,
		CL_A = 0x10B1,
		CL_RG = 0x10B2,
		CL_RA = 0x10B3,
		CL_RGB = 0x10B4,
		CL_RGBA = 0x10B5,
		CL_BGRA = 0x10B6,
		CL_ARGB = 0x10B7,
		CL_INTENSITY = 0x10B8,
		CL_LUMINANCE = 0x10B9;

	/**
	 * cl_channel_type 
	 */
	public static final int CL_SNORM_INT8 = 0x10D0,
		CL_SNORM_INT16 = 0x10D1,
		CL_UNORM_INT8 = 0x10D2,
		CL_UNORM_INT16 = 0x10D3,
		CL_UNORM_SHORT_565 = 0x10D4,
		CL_UNORM_SHORT_555 = 0x10D5,
		CL_UNORM_INT_101010 = 0x10D6,
		CL_SIGNED_INT8 = 0x10D7,
		CL_SIGNED_INT16 = 0x10D8,
		CL_SIGNED_INT32 = 0x10D9,
		CL_UNSIGNED_INT8 = 0x10DA,
		CL_UNSIGNED_INT16 = 0x10DB,
		CL_UNSIGNED_INT32 = 0x10DC,
		CL_HALF_FLOAT = 0x10DD,
		CL_FLOAT = 0x10DE;

	/**
	 * cl_mem_object_type 
	 */
	public static final int CL_MEM_OBJECT_BUFFER = 0x10F0,
		CL_MEM_OBJECT_IMAGE2D = 0x10F1,
		CL_MEM_OBJECT_IMAGE3D = 0x10F2;

	/**
	 * cl_mem_info 
	 */
	public static final int CL_MEM_TYPE = 0x1100,
		CL_MEM_FLAGS = 0x1101,
		CL_MEM_SIZE = 0x1102,
		CL_MEM_HOST_PTR = 0x1103,
		CL_MEM_MAP_COUNT = 0x1104,
		CL_MEM_REFERENCE_COUNT = 0x1105,
		CL_MEM_CONTEXT = 0x1106;

	/**
	 * cl_image_info 
	 */
	public static final int CL_IMAGE_FORMAT = 0x1110,
		CL_IMAGE_ELEMENT_SIZE = 0x1111,
		CL_IMAGE_ROW_PITCH = 0x1112,
		CL_IMAGE_SLICE_PITCH = 0x1113,
		CL_IMAGE_WIDTH = 0x1114,
		CL_IMAGE_HEIGHT = 0x1115,
		CL_IMAGE_DEPTH = 0x1116;

	/**
	 * cl_addressing_mode 
	 */
	public static final int CL_ADDRESS_NONE = 0x1130,
		CL_ADDRESS_CLAMP_TO_EDGE = 0x1131,
		CL_ADDRESS_CLAMP = 0x1132,
		CL_ADDRESS_REPEAT = 0x1133;

	/**
	 * cl_filter_mode 
	 */
	public static final int CL_FILTER_NEAREST = 0x1140,
		CL_FILTER_LINEAR = 0x1141;

	/**
	 * cl_sampler_info 
	 */
	public static final int CL_SAMPLER_REFERENCE_COUNT = 0x1150,
		CL_SAMPLER_CONTEXT = 0x1151,
		CL_SAMPLER_NORMALIZED_COORDS = 0x1152,
		CL_SAMPLER_ADDRESSING_MODE = 0x1153,
		CL_SAMPLER_FILTER_MODE = 0x1154;

	/**
	 * cl_map_flags - bitfield 
	 */
	public static final int CL_MAP_READ = 0x1,
		CL_MAP_WRITE = 0x2;

	/**
	 * cl_program_info 
	 */
	public static final int CL_PROGRAM_REFERENCE_COUNT = 0x1160,
		CL_PROGRAM_CONTEXT = 0x1161,
		CL_PROGRAM_NUM_DEVICES = 0x1162,
		CL_PROGRAM_DEVICES = 0x1163,
		CL_PROGRAM_SOURCE = 0x1164,
		CL_PROGRAM_BINARY_SIZES = 0x1165,
		CL_PROGRAM_BINARIES = 0x1166;

	/**
	 * cl_program_build_info 
	 */
	public static final int CL_PROGRAM_BUILD_STATUS = 0x1181,
		CL_PROGRAM_BUILD_OPTIONS = 0x1182,
		CL_PROGRAM_BUILD_LOG = 0x1183;

	/**
	 * cl_build_status 
	 */
	public static final int CL_BUILD_SUCCESS = 0x0,
		CL_BUILD_NONE = 0xFFFFFFFF,
		CL_BUILD_ERROR = 0xFFFFFFFE,
		CL_BUILD_IN_PROGRESS = 0xFFFFFFFD;

	/**
	 * cl_kernel_info 
	 */
	public static final int CL_KERNEL_FUNCTION_NAME = 0x1190,
		CL_KERNEL_NUM_ARGS = 0x1191,
		CL_KERNEL_REFERENCE_COUNT = 0x1192,
		CL_KERNEL_CONTEXT = 0x1193,
		CL_KERNEL_PROGRAM = 0x1194;

	/**
	 * cl_kernel_work_group_info 
	 */
	public static final int CL_KERNEL_WORK_GROUP_SIZE = 0x11B0,
		CL_KERNEL_COMPILE_WORK_GROUP_SIZE = 0x11B1,
		CL_KERNEL_LOCAL_MEM_SIZE = 0x11B2;

	/**
	 * cl_event_info 
	 */
	public static final int CL_EVENT_COMMAND_QUEUE = 0x11D0,
		CL_EVENT_COMMAND_TYPE = 0x11D1,
		CL_EVENT_REFERENCE_COUNT = 0x11D2,
		CL_EVENT_COMMAND_EXECUTION_STATUS = 0x11D3;

	/**
	 * cl_command_type 
	 */
	public static final int CL_COMMAND_NDRANGE_KERNEL = 0x11F0,
		CL_COMMAND_TASK = 0x11F1,
		CL_COMMAND_NATIVE_KERNEL = 0x11F2,
		CL_COMMAND_READ_BUFFER = 0x11F3,
		CL_COMMAND_WRITE_BUFFER = 0x11F4,
		CL_COMMAND_COPY_BUFFER = 0x11F5,
		CL_COMMAND_READ_IMAGE = 0x11F6,
		CL_COMMAND_WRITE_IMAGE = 0x11F7,
		CL_COMMAND_COPY_IMAGE = 0x11F8,
		CL_COMMAND_COPY_IMAGE_TO_BUFFER = 0x11F9,
		CL_COMMAND_COPY_BUFFER_TO_IMAGE = 0x11FA,
		CL_COMMAND_MAP_BUFFER = 0x11FB,
		CL_COMMAND_MAP_IMAGE = 0x11FC,
		CL_COMMAND_UNMAP_MEM_OBJECT = 0x11FD,
		CL_COMMAND_MARKER = 0x11FE,
		CL_COMMAND_ACQUIRE_GL_OBJECTS = 0x11FF,
		CL_COMMAND_RELEASE_GL_OBJECTS = 0x1200;

	/**
	 * command execution status 
	 */
	public static final int CL_COMPLETE = 0x0,
		CL_RUNNING = 0x1,
		CL_SUBMITTED = 0x2,
		CL_QUEUED = 0x3;

	/**
	 * cl_profiling_info 
	 */
	public static final int CL_PROFILING_COMMAND_QUEUED = 0x1280,
		CL_PROFILING_COMMAND_SUBMIT = 0x1281,
		CL_PROFILING_COMMAND_START = 0x1282,
		CL_PROFILING_COMMAND_END = 0x1283;

	private CL10() {}

	public static int clGetPlatformIDs(PointerBuffer platforms, IntBuffer num_platforms) {
		long function_pointer = CLCapabilities.clGetPlatformIDs;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (platforms != null)
			BufferChecks.checkDirect(platforms);
		if (num_platforms != null)
			BufferChecks.checkBuffer(num_platforms, 1);
		if ( num_platforms == null ) num_platforms = APIUtil.getBufferInt();
		int __result = nclGetPlatformIDs((platforms == null ? 0 : platforms.remaining()), MemoryUtil.getAddressSafe(platforms), MemoryUtil.getAddressSafe(num_platforms), function_pointer);
		if ( __result == CL_SUCCESS && platforms != null ) CLPlatform.registerCLPlatforms(platforms, num_platforms);
		return __result;
	}
	static native int nclGetPlatformIDs(int platforms_num_entries, long platforms, long num_platforms, long function_pointer);

	public static int clGetPlatformInfo(CLPlatform platform, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetPlatformInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetPlatformInfo(platform == null ? 0 : platform.getPointer(), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetPlatformInfo(long platform, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);

	public static int clGetDeviceIDs(CLPlatform platform, long device_type, PointerBuffer devices, IntBuffer num_devices) {
		long function_pointer = CLCapabilities.clGetDeviceIDs;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (devices != null)
			BufferChecks.checkDirect(devices);
		if (num_devices != null)
			BufferChecks.checkBuffer(num_devices, 1);
		else
			num_devices = APIUtil.getBufferInt();
		int __result = nclGetDeviceIDs(platform.getPointer(), device_type, (devices == null ? 0 : devices.remaining()), MemoryUtil.getAddressSafe(devices), MemoryUtil.getAddressSafe(num_devices), function_pointer);
		if ( __result == CL_SUCCESS && devices != null ) platform.registerCLDevices(devices, num_devices);
		return __result;
	}
	static native int nclGetDeviceIDs(long platform, long device_type, int devices_num_entries, long devices, long num_devices, long function_pointer);

	public static int clGetDeviceInfo(CLDevice device, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetDeviceInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetDeviceInfo(device.getPointer(), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetDeviceInfo(long device, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);

	/**
	 * LWJGL requires CL_CONTEXT_PLATFORM to be present in the cl_context_properties buffer. 
	 */
	public static CLContext clCreateContext(PointerBuffer properties, PointerBuffer devices, CLContextCallback pfn_notify, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateContext;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(properties, 3);
		BufferChecks.checkNullTerminated(properties);
		BufferChecks.checkBuffer(devices, 1);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		long user_data = pfn_notify == null || pfn_notify.isCustom() ? 0 : CallbackUtil.createGlobalRef(pfn_notify);
		CLContext __result = null;
		try {
			__result = new CLContext(nclCreateContext(MemoryUtil.getAddress(properties), devices.remaining(), MemoryUtil.getAddress(devices), pfn_notify == null ? 0 : pfn_notify.getPointer(), user_data, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), APIUtil.getCLPlatform(properties));
			return __result;
		} finally {
			if ( __result != null ) __result.setContextCallback(user_data);
		}
	}
	static native long nclCreateContext(long properties, int devices_num_devices, long devices, long pfn_notify, long user_data, long errcode_ret, long function_pointer);

	/**
	 * Overloads clCreateContext.
	 * <p>
	 * LWJGL requires CL_CONTEXT_PLATFORM to be present in the cl_context_properties buffer. 
	 */
	public static CLContext clCreateContext(PointerBuffer properties, CLDevice device, CLContextCallback pfn_notify, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateContext;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(properties, 3);
		BufferChecks.checkNullTerminated(properties);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		long user_data = pfn_notify == null || pfn_notify.isCustom() ? 0 : CallbackUtil.createGlobalRef(pfn_notify);
		CLContext __result = null;
		try {
			__result = new CLContext(nclCreateContext(MemoryUtil.getAddress(properties), 1, APIUtil.getPointer(device), pfn_notify == null ? 0 : pfn_notify.getPointer(), user_data, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), APIUtil.getCLPlatform(properties));
			return __result;
		} finally {
			if ( __result != null ) __result.setContextCallback(user_data);
		}
	}

	/**
	 * LWJGL requires CL_CONTEXT_PLATFORM to be present in the cl_context_properties buffer. 
	 */
	public static CLContext clCreateContextFromType(PointerBuffer properties, long device_type, CLContextCallback pfn_notify, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateContextFromType;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(properties, 3);
		BufferChecks.checkNullTerminated(properties);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		long user_data = pfn_notify == null || pfn_notify.isCustom() ? 0 : CallbackUtil.createGlobalRef(pfn_notify);
		CLContext __result = null;
		try {
			__result = new CLContext(nclCreateContextFromType(MemoryUtil.getAddress(properties), device_type, pfn_notify == null ? 0 : pfn_notify.getPointer(), user_data, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), APIUtil.getCLPlatform(properties));
			return __result;
		} finally {
			if ( __result != null ) __result.setContextCallback(user_data);
		}
	}
	static native long nclCreateContextFromType(long properties, long device_type, long pfn_notify, long user_data, long errcode_ret, long function_pointer);

	public static int clRetainContext(CLContext context) {
		long function_pointer = CLCapabilities.clRetainContext;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclRetainContext(context.getPointer(), function_pointer);
		if ( __result == CL_SUCCESS ) context.retain();
		return __result;
	}
	static native int nclRetainContext(long context, long function_pointer);

	public static int clReleaseContext(CLContext context) {
		long function_pointer = CLCapabilities.clReleaseContext;
		BufferChecks.checkFunctionAddress(function_pointer);
		APIUtil.releaseObjects(context);
		int __result = nclReleaseContext(context.getPointer(), function_pointer);
		if ( __result == CL_SUCCESS ) context.releaseImpl();
		return __result;
	}
	static native int nclReleaseContext(long context, long function_pointer);

	public static int clGetContextInfo(CLContext context, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetContextInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		if ( param_value_size_ret == null && APIUtil.isDevicesParam(param_name) ) param_value_size_ret = APIUtil.getBufferPointer();
		int __result = nclGetContextInfo(context.getPointer(), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		if ( __result == CL_SUCCESS && param_value != null && APIUtil.isDevicesParam(param_name) ) context.getParent().registerCLDevices(param_value, param_value_size_ret);
		return __result;
	}
	static native int nclGetContextInfo(long context, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);

	public static CLCommandQueue clCreateCommandQueue(CLContext context, CLDevice device, long properties, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateCommandQueue;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLCommandQueue __result = new CLCommandQueue(nclCreateCommandQueue(context.getPointer(), device.getPointer(), properties, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context, device);
		return __result;
	}
	static native long nclCreateCommandQueue(long context, long device, long properties, long errcode_ret, long function_pointer);

	public static int clRetainCommandQueue(CLCommandQueue command_queue) {
		long function_pointer = CLCapabilities.clRetainCommandQueue;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclRetainCommandQueue(command_queue.getPointer(), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.retain();
		return __result;
	}
	static native int nclRetainCommandQueue(long command_queue, long function_pointer);

	public static int clReleaseCommandQueue(CLCommandQueue command_queue) {
		long function_pointer = CLCapabilities.clReleaseCommandQueue;
		BufferChecks.checkFunctionAddress(function_pointer);
		APIUtil.releaseObjects(command_queue);
		int __result = nclReleaseCommandQueue(command_queue.getPointer(), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.release();
		return __result;
	}
	static native int nclReleaseCommandQueue(long command_queue, long function_pointer);

	public static int clGetCommandQueueInfo(CLCommandQueue command_queue, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetCommandQueueInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetCommandQueueInfo(command_queue.getPointer(), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetCommandQueueInfo(long command_queue, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);

	public static CLMem clCreateBuffer(CLContext context, long flags, long host_ptr_size, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateBuffer(context.getPointer(), flags, host_ptr_size, 0L, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateBuffer(CLContext context, long flags, ByteBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(host_ptr);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateBuffer(context.getPointer(), flags, host_ptr.remaining(), MemoryUtil.getAddress(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateBuffer(CLContext context, long flags, DoubleBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(host_ptr);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateBuffer(context.getPointer(), flags, (host_ptr.remaining() << 3), MemoryUtil.getAddress(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateBuffer(CLContext context, long flags, FloatBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(host_ptr);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateBuffer(context.getPointer(), flags, (host_ptr.remaining() << 2), MemoryUtil.getAddress(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateBuffer(CLContext context, long flags, IntBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(host_ptr);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateBuffer(context.getPointer(), flags, (host_ptr.remaining() << 2), MemoryUtil.getAddress(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateBuffer(CLContext context, long flags, LongBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(host_ptr);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateBuffer(context.getPointer(), flags, (host_ptr.remaining() << 3), MemoryUtil.getAddress(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateBuffer(CLContext context, long flags, ShortBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(host_ptr);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateBuffer(context.getPointer(), flags, (host_ptr.remaining() << 1), MemoryUtil.getAddress(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateBuffer(long context, long flags, long host_ptr_size, long host_ptr, long errcode_ret, long function_pointer);

	public static int clEnqueueReadBuffer(CLCommandQueue command_queue, CLMem buffer, int blocking_read, long offset, ByteBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ptr);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadBuffer(command_queue.getPointer(), buffer.getPointer(), blocking_read, offset, ptr.remaining(), MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueReadBuffer(CLCommandQueue command_queue, CLMem buffer, int blocking_read, long offset, DoubleBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ptr);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadBuffer(command_queue.getPointer(), buffer.getPointer(), blocking_read, offset, (ptr.remaining() << 3), MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueReadBuffer(CLCommandQueue command_queue, CLMem buffer, int blocking_read, long offset, FloatBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ptr);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadBuffer(command_queue.getPointer(), buffer.getPointer(), blocking_read, offset, (ptr.remaining() << 2), MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueReadBuffer(CLCommandQueue command_queue, CLMem buffer, int blocking_read, long offset, IntBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ptr);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadBuffer(command_queue.getPointer(), buffer.getPointer(), blocking_read, offset, (ptr.remaining() << 2), MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueReadBuffer(CLCommandQueue command_queue, CLMem buffer, int blocking_read, long offset, LongBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ptr);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadBuffer(command_queue.getPointer(), buffer.getPointer(), blocking_read, offset, (ptr.remaining() << 3), MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueReadBuffer(CLCommandQueue command_queue, CLMem buffer, int blocking_read, long offset, ShortBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ptr);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadBuffer(command_queue.getPointer(), buffer.getPointer(), blocking_read, offset, (ptr.remaining() << 1), MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueReadBuffer(long command_queue, long buffer, int blocking_read, long offset, long ptr_size, long ptr, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clEnqueueWriteBuffer(CLCommandQueue command_queue, CLMem buffer, int blocking_write, long offset, ByteBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ptr);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteBuffer(command_queue.getPointer(), buffer.getPointer(), blocking_write, offset, ptr.remaining(), MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueWriteBuffer(CLCommandQueue command_queue, CLMem buffer, int blocking_write, long offset, DoubleBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ptr);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteBuffer(command_queue.getPointer(), buffer.getPointer(), blocking_write, offset, (ptr.remaining() << 3), MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueWriteBuffer(CLCommandQueue command_queue, CLMem buffer, int blocking_write, long offset, FloatBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ptr);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteBuffer(command_queue.getPointer(), buffer.getPointer(), blocking_write, offset, (ptr.remaining() << 2), MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueWriteBuffer(CLCommandQueue command_queue, CLMem buffer, int blocking_write, long offset, IntBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ptr);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteBuffer(command_queue.getPointer(), buffer.getPointer(), blocking_write, offset, (ptr.remaining() << 2), MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueWriteBuffer(CLCommandQueue command_queue, CLMem buffer, int blocking_write, long offset, LongBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ptr);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteBuffer(command_queue.getPointer(), buffer.getPointer(), blocking_write, offset, (ptr.remaining() << 3), MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueWriteBuffer(CLCommandQueue command_queue, CLMem buffer, int blocking_write, long offset, ShortBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ptr);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteBuffer(command_queue.getPointer(), buffer.getPointer(), blocking_write, offset, (ptr.remaining() << 1), MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueWriteBuffer(long command_queue, long buffer, int blocking_write, long offset, long ptr_size, long ptr, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clEnqueueCopyBuffer(CLCommandQueue command_queue, CLMem src_buffer, CLMem dst_buffer, long src_offset, long dst_offset, long size, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueCopyBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueCopyBuffer(command_queue.getPointer(), src_buffer.getPointer(), dst_buffer.getPointer(), src_offset, dst_offset, size, (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueCopyBuffer(long command_queue, long src_buffer, long dst_buffer, long src_offset, long dst_offset, long size, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static ByteBuffer clEnqueueMapBuffer(CLCommandQueue command_queue, CLMem buffer, int blocking_map, long map_flags, long offset, long size, PointerBuffer event_wait_list, PointerBuffer event, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clEnqueueMapBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		ByteBuffer __result = nclEnqueueMapBuffer(command_queue.getPointer(), buffer.getPointer(), blocking_map, map_flags, offset, size, (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), MemoryUtil.getAddressSafe(errcode_ret), size, function_pointer);
		if ( __result != null ) command_queue.registerCLEvent(event);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nclEnqueueMapBuffer(long command_queue, long buffer, int blocking_map, long map_flags, long offset, long size, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long errcode_ret, long result_size, long function_pointer);

	public static CLMem clCreateImage2D(CLContext context, long flags, ByteBuffer image_format, long image_width, long image_height, long image_row_pitch, ByteBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(image_format, 2 * 4);
		if (host_ptr != null)
			BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage2DSize(image_format, image_width, image_height, image_row_pitch));
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateImage2D(context.getPointer(), flags, MemoryUtil.getAddress(image_format), image_width, image_height, image_row_pitch, MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateImage2D(CLContext context, long flags, ByteBuffer image_format, long image_width, long image_height, long image_row_pitch, FloatBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(image_format, 2 * 4);
		if (host_ptr != null)
			BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage2DSize(image_format, image_width, image_height, image_row_pitch));
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateImage2D(context.getPointer(), flags, MemoryUtil.getAddress(image_format), image_width, image_height, image_row_pitch, MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateImage2D(CLContext context, long flags, ByteBuffer image_format, long image_width, long image_height, long image_row_pitch, IntBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(image_format, 2 * 4);
		if (host_ptr != null)
			BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage2DSize(image_format, image_width, image_height, image_row_pitch));
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateImage2D(context.getPointer(), flags, MemoryUtil.getAddress(image_format), image_width, image_height, image_row_pitch, MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateImage2D(CLContext context, long flags, ByteBuffer image_format, long image_width, long image_height, long image_row_pitch, ShortBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(image_format, 2 * 4);
		if (host_ptr != null)
			BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage2DSize(image_format, image_width, image_height, image_row_pitch));
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateImage2D(context.getPointer(), flags, MemoryUtil.getAddress(image_format), image_width, image_height, image_row_pitch, MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateImage2D(long context, long flags, long image_format, long image_width, long image_height, long image_row_pitch, long host_ptr, long errcode_ret, long function_pointer);

	public static CLMem clCreateImage3D(CLContext context, long flags, ByteBuffer image_format, long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, ByteBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(image_format, 2 * 4);
		if (host_ptr != null)
			BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage3DSize(image_format, image_width, image_height, image_height, image_row_pitch, image_slice_pitch));
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateImage3D(context.getPointer(), flags, MemoryUtil.getAddress(image_format), image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateImage3D(CLContext context, long flags, ByteBuffer image_format, long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, FloatBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(image_format, 2 * 4);
		if (host_ptr != null)
			BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage3DSize(image_format, image_width, image_height, image_height, image_row_pitch, image_slice_pitch));
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateImage3D(context.getPointer(), flags, MemoryUtil.getAddress(image_format), image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateImage3D(CLContext context, long flags, ByteBuffer image_format, long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, IntBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(image_format, 2 * 4);
		if (host_ptr != null)
			BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage3DSize(image_format, image_width, image_height, image_height, image_row_pitch, image_slice_pitch));
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateImage3D(context.getPointer(), flags, MemoryUtil.getAddress(image_format), image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	public static CLMem clCreateImage3D(CLContext context, long flags, ByteBuffer image_format, long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, ShortBuffer host_ptr, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(image_format, 2 * 4);
		if (host_ptr != null)
			BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage3DSize(image_format, image_width, image_height, image_height, image_row_pitch, image_slice_pitch));
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = new CLMem(nclCreateImage3D(context.getPointer(), flags, MemoryUtil.getAddress(image_format), image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateImage3D(long context, long flags, long image_format, long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, long host_ptr, long errcode_ret, long function_pointer);

	public static int clGetSupportedImageFormats(CLContext context, long flags, int image_type, ByteBuffer image_formats, IntBuffer num_image_formats) {
		long function_pointer = CLCapabilities.clGetSupportedImageFormats;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (image_formats != null)
			BufferChecks.checkDirect(image_formats);
		if (num_image_formats != null)
			BufferChecks.checkBuffer(num_image_formats, 1);
		int __result = nclGetSupportedImageFormats(context.getPointer(), flags, image_type, (image_formats == null ? 0 : image_formats.remaining()) / (2 * 4), MemoryUtil.getAddressSafe(image_formats), MemoryUtil.getAddressSafe(num_image_formats), function_pointer);
		return __result;
	}
	static native int nclGetSupportedImageFormats(long context, long flags, int image_type, int image_formats_num_entries, long image_formats, long num_image_formats, long function_pointer);

	public static int clEnqueueReadImage(CLCommandQueue command_queue, CLMem image, int blocking_read, PointerBuffer origin, PointerBuffer region, long row_pitch, long slice_pitch, ByteBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(origin, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateImageSize(region, row_pitch, slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadImage(command_queue.getPointer(), image.getPointer(), blocking_read, MemoryUtil.getAddress(origin), MemoryUtil.getAddress(region), row_pitch, slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueReadImage(CLCommandQueue command_queue, CLMem image, int blocking_read, PointerBuffer origin, PointerBuffer region, long row_pitch, long slice_pitch, FloatBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(origin, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateImageSize(region, row_pitch, slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadImage(command_queue.getPointer(), image.getPointer(), blocking_read, MemoryUtil.getAddress(origin), MemoryUtil.getAddress(region), row_pitch, slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueReadImage(CLCommandQueue command_queue, CLMem image, int blocking_read, PointerBuffer origin, PointerBuffer region, long row_pitch, long slice_pitch, IntBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(origin, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateImageSize(region, row_pitch, slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadImage(command_queue.getPointer(), image.getPointer(), blocking_read, MemoryUtil.getAddress(origin), MemoryUtil.getAddress(region), row_pitch, slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueReadImage(CLCommandQueue command_queue, CLMem image, int blocking_read, PointerBuffer origin, PointerBuffer region, long row_pitch, long slice_pitch, ShortBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(origin, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateImageSize(region, row_pitch, slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadImage(command_queue.getPointer(), image.getPointer(), blocking_read, MemoryUtil.getAddress(origin), MemoryUtil.getAddress(region), row_pitch, slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueReadImage(long command_queue, long image, int blocking_read, long origin, long region, long row_pitch, long slice_pitch, long ptr, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clEnqueueWriteImage(CLCommandQueue command_queue, CLMem image, int blocking_write, PointerBuffer origin, PointerBuffer region, long input_row_pitch, long input_slice_pitch, ByteBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(origin, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateImageSize(region, input_row_pitch, input_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteImage(command_queue.getPointer(), image.getPointer(), blocking_write, MemoryUtil.getAddress(origin), MemoryUtil.getAddress(region), input_row_pitch, input_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueWriteImage(CLCommandQueue command_queue, CLMem image, int blocking_write, PointerBuffer origin, PointerBuffer region, long input_row_pitch, long input_slice_pitch, FloatBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(origin, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateImageSize(region, input_row_pitch, input_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteImage(command_queue.getPointer(), image.getPointer(), blocking_write, MemoryUtil.getAddress(origin), MemoryUtil.getAddress(region), input_row_pitch, input_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueWriteImage(CLCommandQueue command_queue, CLMem image, int blocking_write, PointerBuffer origin, PointerBuffer region, long input_row_pitch, long input_slice_pitch, IntBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(origin, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateImageSize(region, input_row_pitch, input_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteImage(command_queue.getPointer(), image.getPointer(), blocking_write, MemoryUtil.getAddress(origin), MemoryUtil.getAddress(region), input_row_pitch, input_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueWriteImage(CLCommandQueue command_queue, CLMem image, int blocking_write, PointerBuffer origin, PointerBuffer region, long input_row_pitch, long input_slice_pitch, ShortBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(origin, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateImageSize(region, input_row_pitch, input_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteImage(command_queue.getPointer(), image.getPointer(), blocking_write, MemoryUtil.getAddress(origin), MemoryUtil.getAddress(region), input_row_pitch, input_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueWriteImage(long command_queue, long image, int blocking_write, long origin, long region, long input_row_pitch, long input_slice_pitch, long ptr, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clEnqueueCopyImage(CLCommandQueue command_queue, CLMem src_image, CLMem dst_image, PointerBuffer src_origin, PointerBuffer dst_origin, PointerBuffer region, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueCopyImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(src_origin, 3);
		BufferChecks.checkBuffer(dst_origin, 3);
		BufferChecks.checkBuffer(region, 3);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueCopyImage(command_queue.getPointer(), src_image.getPointer(), dst_image.getPointer(), MemoryUtil.getAddress(src_origin), MemoryUtil.getAddress(dst_origin), MemoryUtil.getAddress(region), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueCopyImage(long command_queue, long src_image, long dst_image, long src_origin, long dst_origin, long region, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clEnqueueCopyImageToBuffer(CLCommandQueue command_queue, CLMem src_image, CLMem dst_buffer, PointerBuffer src_origin, PointerBuffer region, long dst_offset, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueCopyImageToBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(src_origin, 3);
		BufferChecks.checkBuffer(region, 3);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueCopyImageToBuffer(command_queue.getPointer(), src_image.getPointer(), dst_buffer.getPointer(), MemoryUtil.getAddress(src_origin), MemoryUtil.getAddress(region), dst_offset, (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueCopyImageToBuffer(long command_queue, long src_image, long dst_buffer, long src_origin, long region, long dst_offset, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clEnqueueCopyBufferToImage(CLCommandQueue command_queue, CLMem src_buffer, CLMem dst_image, long src_offset, PointerBuffer dst_origin, PointerBuffer region, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueCopyBufferToImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(dst_origin, 3);
		BufferChecks.checkBuffer(region, 3);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueCopyBufferToImage(command_queue.getPointer(), src_buffer.getPointer(), dst_image.getPointer(), src_offset, MemoryUtil.getAddress(dst_origin), MemoryUtil.getAddress(region), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueCopyBufferToImage(long command_queue, long src_buffer, long dst_image, long src_offset, long dst_origin, long region, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static ByteBuffer clEnqueueMapImage(CLCommandQueue command_queue, CLMem image, int blocking_map, long map_flags, PointerBuffer origin, PointerBuffer region, PointerBuffer image_row_pitch, PointerBuffer image_slice_pitch, PointerBuffer event_wait_list, PointerBuffer event, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clEnqueueMapImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(origin, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(image_row_pitch, 1);
		if (image_slice_pitch != null)
			BufferChecks.checkBuffer(image_slice_pitch, 1);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		ByteBuffer __result = nclEnqueueMapImage(command_queue.getPointer(), image.getPointer(), blocking_map, map_flags, MemoryUtil.getAddress(origin), MemoryUtil.getAddress(region), MemoryUtil.getAddress(image_row_pitch), MemoryUtil.getAddressSafe(image_slice_pitch), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), MemoryUtil.getAddressSafe(errcode_ret), function_pointer);
		if ( __result != null ) command_queue.registerCLEvent(event);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nclEnqueueMapImage(long command_queue, long image, int blocking_map, long map_flags, long origin, long region, long image_row_pitch, long image_slice_pitch, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long errcode_ret, long function_pointer);

	public static int clGetImageInfo(CLMem image, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetImageInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetImageInfo(image.getPointer(), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetImageInfo(long image, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);

	public static int clRetainMemObject(CLMem memobj) {
		long function_pointer = CLCapabilities.clRetainMemObject;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclRetainMemObject(memobj.getPointer(), function_pointer);
		if ( __result == CL_SUCCESS ) memobj.retain();
		return __result;
	}
	static native int nclRetainMemObject(long memobj, long function_pointer);

	public static int clReleaseMemObject(CLMem memobj) {
		long function_pointer = CLCapabilities.clReleaseMemObject;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclReleaseMemObject(memobj.getPointer(), function_pointer);
		if ( __result == CL_SUCCESS ) memobj.release();
		return __result;
	}
	static native int nclReleaseMemObject(long memobj, long function_pointer);

	public static int clEnqueueUnmapMemObject(CLCommandQueue command_queue, CLMem memobj, ByteBuffer mapped_ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueUnmapMemObject;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(mapped_ptr);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueUnmapMemObject(command_queue.getPointer(), memobj.getPointer(), MemoryUtil.getAddress(mapped_ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueUnmapMemObject(long command_queue, long memobj, long mapped_ptr, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clGetMemObjectInfo(CLMem memobj, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetMemObjectInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetMemObjectInfo(memobj.getPointer(), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetMemObjectInfo(long memobj, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);

	public static CLSampler clCreateSampler(CLContext context, int normalized_coords, int addressing_mode, int filter_mode, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateSampler;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLSampler __result = new CLSampler(nclCreateSampler(context.getPointer(), normalized_coords, addressing_mode, filter_mode, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateSampler(long context, int normalized_coords, int addressing_mode, int filter_mode, long errcode_ret, long function_pointer);

	public static int clRetainSampler(CLSampler sampler) {
		long function_pointer = CLCapabilities.clRetainSampler;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclRetainSampler(sampler.getPointer(), function_pointer);
		if ( __result == CL_SUCCESS ) sampler.retain();
		return __result;
	}
	static native int nclRetainSampler(long sampler, long function_pointer);

	public static int clReleaseSampler(CLSampler sampler) {
		long function_pointer = CLCapabilities.clReleaseSampler;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclReleaseSampler(sampler.getPointer(), function_pointer);
		if ( __result == CL_SUCCESS ) sampler.release();
		return __result;
	}
	static native int nclReleaseSampler(long sampler, long function_pointer);

	public static int clGetSamplerInfo(CLSampler sampler, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetSamplerInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetSamplerInfo(sampler.getPointer(), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetSamplerInfo(long sampler, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);

	public static CLProgram clCreateProgramWithSource(CLContext context, ByteBuffer string, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateProgramWithSource;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(string);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLProgram __result = new CLProgram(nclCreateProgramWithSource(context.getPointer(), 1, MemoryUtil.getAddress(string), string.remaining(), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateProgramWithSource(long context, int count, long string, long string_lengths, long errcode_ret, long function_pointer);

	/** Overloads clCreateProgramWithSource. */
	public static CLProgram clCreateProgramWithSource(CLContext context, ByteBuffer strings, PointerBuffer lengths, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateProgramWithSource;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(strings, APIUtil.getSize(lengths));
		BufferChecks.checkBuffer(lengths, 1);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLProgram __result = new CLProgram(nclCreateProgramWithSource2(context.getPointer(), lengths.remaining(), MemoryUtil.getAddress(strings), MemoryUtil.getAddress(lengths), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateProgramWithSource2(long context, int lengths_count, long strings, long lengths, long errcode_ret, long function_pointer);

	/** Overloads clCreateProgramWithSource. */
	public static CLProgram clCreateProgramWithSource(CLContext context, ByteBuffer[] strings, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateProgramWithSource;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkArray(strings, 1);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLProgram __result = new CLProgram(nclCreateProgramWithSource3(context.getPointer(), strings.length, strings, APIUtil.getLengths(strings), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateProgramWithSource3(long context, int count, ByteBuffer[] strings, long lengths, long errcode_ret, long function_pointer);

	/** Overloads clCreateProgramWithSource. */
	public static CLProgram clCreateProgramWithSource(CLContext context, CharSequence string, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateProgramWithSource;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLProgram __result = new CLProgram(nclCreateProgramWithSource(context.getPointer(), 1, APIUtil.getBuffer(string), string.length(), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}

	/** Overloads clCreateProgramWithSource. */
	public static CLProgram clCreateProgramWithSource(CLContext context, CharSequence[] strings, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateProgramWithSource;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkArray(strings);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLProgram __result = new CLProgram(nclCreateProgramWithSource4(context.getPointer(), strings.length, APIUtil.getBuffer(strings), APIUtil.getLengths(strings), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateProgramWithSource4(long context, int count, long strings, long lengths, long errcode_ret, long function_pointer);

	public static CLProgram clCreateProgramWithBinary(CLContext context, CLDevice device, ByteBuffer binary, IntBuffer binary_status, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateProgramWithBinary;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(binary);
		BufferChecks.checkBuffer(binary_status, 1);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLProgram __result = new CLProgram(nclCreateProgramWithBinary(context.getPointer(), 1, device.getPointer(), binary.remaining(), MemoryUtil.getAddress(binary), MemoryUtil.getAddress(binary_status), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateProgramWithBinary(long context, int num_devices, long device, long binary_lengths, long binary, long binary_status, long errcode_ret, long function_pointer);

	/** Overloads clCreateProgramWithBinary. */
	public static CLProgram clCreateProgramWithBinary(CLContext context, PointerBuffer device_list, PointerBuffer lengths, ByteBuffer binaries, IntBuffer binary_status, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateProgramWithBinary;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(device_list, 1);
		BufferChecks.checkBuffer(lengths, device_list.remaining());
		BufferChecks.checkBuffer(binaries, APIUtil.getSize(lengths));
		BufferChecks.checkBuffer(binary_status, device_list.remaining());
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLProgram __result = new CLProgram(nclCreateProgramWithBinary2(context.getPointer(), device_list.remaining(), MemoryUtil.getAddress(device_list), MemoryUtil.getAddress(lengths), MemoryUtil.getAddress(binaries), MemoryUtil.getAddress(binary_status), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateProgramWithBinary2(long context, int device_list_num_devices, long device_list, long lengths, long binaries, long binary_status, long errcode_ret, long function_pointer);

	/** Overloads clCreateProgramWithBinary. */
	public static CLProgram clCreateProgramWithBinary(CLContext context, PointerBuffer device_list, ByteBuffer[] binaries, IntBuffer binary_status, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateProgramWithBinary;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(device_list, binaries.length);
		BufferChecks.checkArray(binaries, 1);
		BufferChecks.checkBuffer(binary_status, binaries.length);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLProgram __result = new CLProgram(nclCreateProgramWithBinary3(context.getPointer(), binaries.length, MemoryUtil.getAddress(device_list), APIUtil.getLengths(binaries), binaries, MemoryUtil.getAddress(binary_status), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateProgramWithBinary3(long context, int num_devices, long device_list, long lengths, ByteBuffer[] binaries, long binary_status, long errcode_ret, long function_pointer);

	public static int clRetainProgram(CLProgram program) {
		long function_pointer = CLCapabilities.clRetainProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclRetainProgram(program.getPointer(), function_pointer);
		if ( __result == CL_SUCCESS ) program.retain();
		return __result;
	}
	static native int nclRetainProgram(long program, long function_pointer);

	public static int clReleaseProgram(CLProgram program) {
		long function_pointer = CLCapabilities.clReleaseProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		APIUtil.releaseObjects(program);
		int __result = nclReleaseProgram(program.getPointer(), function_pointer);
		if ( __result == CL_SUCCESS ) program.release();
		return __result;
	}
	static native int nclReleaseProgram(long program, long function_pointer);

	public static int clBuildProgram(CLProgram program, PointerBuffer device_list, ByteBuffer options, CLBuildProgramCallback pfn_notify) {
		long function_pointer = CLCapabilities.clBuildProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (device_list != null)
			BufferChecks.checkDirect(device_list);
		BufferChecks.checkDirect(options);
		BufferChecks.checkNullTerminated(options);
		long user_data = CallbackUtil.createGlobalRef(pfn_notify);
		if ( pfn_notify != null ) pfn_notify.setContext(program.getParent());
		int __result = 0;
		try {
			__result = nclBuildProgram(program.getPointer(), (device_list == null ? 0 : device_list.remaining()), MemoryUtil.getAddressSafe(device_list), MemoryUtil.getAddress(options), pfn_notify == null ? 0 : pfn_notify.getPointer(), user_data, function_pointer);
			return __result;
		} finally {
			CallbackUtil.checkCallback(__result, user_data);
		}
	}
	static native int nclBuildProgram(long program, int device_list_num_devices, long device_list, long options, long pfn_notify, long user_data, long function_pointer);

	/** Overloads clBuildProgram. */
	public static int clBuildProgram(CLProgram program, PointerBuffer device_list, CharSequence options, CLBuildProgramCallback pfn_notify) {
		long function_pointer = CLCapabilities.clBuildProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (device_list != null)
			BufferChecks.checkDirect(device_list);
		long user_data = CallbackUtil.createGlobalRef(pfn_notify);
		if ( pfn_notify != null ) pfn_notify.setContext(program.getParent());
		int __result = 0;
		try {
			__result = nclBuildProgram(program.getPointer(), (device_list == null ? 0 : device_list.remaining()), MemoryUtil.getAddressSafe(device_list), APIUtil.getBufferNT(options), pfn_notify == null ? 0 : pfn_notify.getPointer(), user_data, function_pointer);
			return __result;
		} finally {
			CallbackUtil.checkCallback(__result, user_data);
		}
	}

	/** Overloads clBuildProgram. */
	public static int clBuildProgram(CLProgram program, CLDevice device, CharSequence options, CLBuildProgramCallback pfn_notify) {
		long function_pointer = CLCapabilities.clBuildProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		long user_data = CallbackUtil.createGlobalRef(pfn_notify);
		if ( pfn_notify != null ) pfn_notify.setContext(program.getParent());
		int __result = 0;
		try {
			__result = nclBuildProgram(program.getPointer(), 1, APIUtil.getPointer(device), APIUtil.getBufferNT(options), pfn_notify == null ? 0 : pfn_notify.getPointer(), user_data, function_pointer);
			return __result;
		} finally {
			CallbackUtil.checkCallback(__result, user_data);
		}
	}

	public static int clUnloadCompiler() {
		long function_pointer = CLCapabilities.clUnloadCompiler;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclUnloadCompiler(function_pointer);
		return __result;
	}
	static native int nclUnloadCompiler(long function_pointer);

	public static int clGetProgramInfo(CLProgram program, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetProgramInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetProgramInfo(program.getPointer(), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetProgramInfo(long program, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);

	/**
	 * Overloads clGetProgramInfo.
	 * <p>
	 *  This method can be used to get program binaries. The binary for each device (in the
	 *  order returned by <code>CL_PROGRAM_DEVICES</code>) will be written sequentially to
	 *  the <code>param_value</code> buffer. The buffer size must be big enough to hold
	 *  all the binaries, as returned by <code>CL_PROGRAM_BINARY_SIZES</code>.
	 * <p>
	 *  @param program              the program
	 *  @param param_value          the buffers where the binaries will be written to.
	 *  @param param_value_size_ret optional size result
	 * <p>
	 *  @return the error code
	 */
	public static int clGetProgramInfo(CLProgram program, PointerBuffer sizes, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetProgramInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(sizes, 1);
		BufferChecks.checkBuffer(param_value, APIUtil.getSize(sizes));
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetProgramInfo2(program.getPointer(), CL_PROGRAM_BINARIES, sizes.remaining(), MemoryUtil.getAddress(sizes), MemoryUtil.getAddress(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetProgramInfo2(long program, int param_name, long sizes_len, long sizes, long param_value, long param_value_size_ret, long function_pointer);

	/**
	 * Overloads clGetProgramInfo.
	 * <p>
	 *  This method can be used to get program binaries. The binary for each device (in the
	 *  order returned by <code>CL_PROGRAM_DEVICES</code>) will be written to the corresponding
	 *  slot of the <code>param_value</code> array. The size of each buffer must be big enough to
	 *  hold the corresponding binary, as returned by <code>CL_PROGRAM_BINARY_SIZES</code>.
	 * <p>
	 *  @param program              the program
	 *  @param param_value          the buffers where the binaries will be written to.
	 *  @param param_value_size_ret optional size result
	 * <p>
	 *  @return the error code
	 */
	public static int clGetProgramInfo(CLProgram program, ByteBuffer[] param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetProgramInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkArray(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetProgramInfo3(program.getPointer(), CL_PROGRAM_BINARIES, param_value.length, param_value, MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetProgramInfo3(long program, int param_name, long param_value_len, ByteBuffer[] param_value, long param_value_size_ret, long function_pointer);

	public static int clGetProgramBuildInfo(CLProgram program, CLDevice device, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetProgramBuildInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetProgramBuildInfo(program.getPointer(), device.getPointer(), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetProgramBuildInfo(long program, long device, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);

	public static CLKernel clCreateKernel(CLProgram program, ByteBuffer kernel_name, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateKernel;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(kernel_name);
		BufferChecks.checkNullTerminated(kernel_name);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLKernel __result = new CLKernel(nclCreateKernel(program.getPointer(), MemoryUtil.getAddress(kernel_name), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), program);
		return __result;
	}
	static native long nclCreateKernel(long program, long kernel_name, long errcode_ret, long function_pointer);

	/** Overloads clCreateKernel. */
	public static CLKernel clCreateKernel(CLProgram program, CharSequence kernel_name, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateKernel;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLKernel __result = new CLKernel(nclCreateKernel(program.getPointer(), APIUtil.getBufferNT(kernel_name), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), program);
		return __result;
	}

	public static int clCreateKernelsInProgram(CLProgram program, PointerBuffer kernels, IntBuffer num_kernels_ret) {
		long function_pointer = CLCapabilities.clCreateKernelsInProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (kernels != null)
			BufferChecks.checkDirect(kernels);
		if (num_kernels_ret != null)
			BufferChecks.checkBuffer(num_kernels_ret, 1);
		int __result = nclCreateKernelsInProgram(program.getPointer(), (kernels == null ? 0 : kernels.remaining()), MemoryUtil.getAddressSafe(kernels), MemoryUtil.getAddressSafe(num_kernels_ret), function_pointer);
		if ( __result == CL_SUCCESS && kernels != null ) program.registerCLKernels(kernels);
		return __result;
	}
	static native int nclCreateKernelsInProgram(long program, int kernels_num_kernels, long kernels, long num_kernels_ret, long function_pointer);

	public static int clRetainKernel(CLKernel kernel) {
		long function_pointer = CLCapabilities.clRetainKernel;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclRetainKernel(kernel.getPointer(), function_pointer);
		if ( __result == CL_SUCCESS ) kernel.retain();
		return __result;
	}
	static native int nclRetainKernel(long kernel, long function_pointer);

	public static int clReleaseKernel(CLKernel kernel) {
		long function_pointer = CLCapabilities.clReleaseKernel;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclReleaseKernel(kernel.getPointer(), function_pointer);
		if ( __result == CL_SUCCESS ) kernel.release();
		return __result;
	}
	static native int nclReleaseKernel(long kernel, long function_pointer);

	public static int clSetKernelArg(CLKernel kernel, int arg_index, long arg_value_arg_size) {
		long function_pointer = CLCapabilities.clSetKernelArg;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclSetKernelArg(kernel.getPointer(), arg_index, arg_value_arg_size, 0L, function_pointer);
		return __result;
	}
	public static int clSetKernelArg(CLKernel kernel, int arg_index, ByteBuffer arg_value) {
		long function_pointer = CLCapabilities.clSetKernelArg;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(arg_value);
		int __result = nclSetKernelArg(kernel.getPointer(), arg_index, arg_value.remaining(), MemoryUtil.getAddress(arg_value), function_pointer);
		return __result;
	}
	public static int clSetKernelArg(CLKernel kernel, int arg_index, DoubleBuffer arg_value) {
		long function_pointer = CLCapabilities.clSetKernelArg;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(arg_value);
		int __result = nclSetKernelArg(kernel.getPointer(), arg_index, (arg_value.remaining() << 3), MemoryUtil.getAddress(arg_value), function_pointer);
		return __result;
	}
	public static int clSetKernelArg(CLKernel kernel, int arg_index, FloatBuffer arg_value) {
		long function_pointer = CLCapabilities.clSetKernelArg;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(arg_value);
		int __result = nclSetKernelArg(kernel.getPointer(), arg_index, (arg_value.remaining() << 2), MemoryUtil.getAddress(arg_value), function_pointer);
		return __result;
	}
	public static int clSetKernelArg(CLKernel kernel, int arg_index, IntBuffer arg_value) {
		long function_pointer = CLCapabilities.clSetKernelArg;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(arg_value);
		int __result = nclSetKernelArg(kernel.getPointer(), arg_index, (arg_value.remaining() << 2), MemoryUtil.getAddress(arg_value), function_pointer);
		return __result;
	}
	public static int clSetKernelArg(CLKernel kernel, int arg_index, LongBuffer arg_value) {
		long function_pointer = CLCapabilities.clSetKernelArg;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(arg_value);
		int __result = nclSetKernelArg(kernel.getPointer(), arg_index, (arg_value.remaining() << 3), MemoryUtil.getAddress(arg_value), function_pointer);
		return __result;
	}
	public static int clSetKernelArg(CLKernel kernel, int arg_index, ShortBuffer arg_value) {
		long function_pointer = CLCapabilities.clSetKernelArg;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(arg_value);
		int __result = nclSetKernelArg(kernel.getPointer(), arg_index, (arg_value.remaining() << 1), MemoryUtil.getAddress(arg_value), function_pointer);
		return __result;
	}
	static native int nclSetKernelArg(long kernel, int arg_index, long arg_value_arg_size, long arg_value, long function_pointer);

	/** Overloads clSetKernelArg. */
	public static int clSetKernelArg(CLKernel kernel, int arg_index, CLObject arg_value) {
		long function_pointer = CLCapabilities.clSetKernelArg;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclSetKernelArg(kernel.getPointer(), arg_index, PointerBuffer.getPointerSize(), APIUtil.getPointerSafe(arg_value), function_pointer);
		return __result;
	}

	/** Overloads clSetKernelArg. */
	static int clSetKernelArg(CLKernel kernel, int arg_index, long arg_size, Buffer arg_value) {
		long function_pointer = CLCapabilities.clSetKernelArg;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclSetKernelArg(kernel.getPointer(), arg_index, arg_size, MemoryUtil.getAddress0(arg_value), function_pointer);
		return __result;
	}

	public static int clGetKernelInfo(CLKernel kernel, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetKernelInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetKernelInfo(kernel.getPointer(), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetKernelInfo(long kernel, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);

	public static int clGetKernelWorkGroupInfo(CLKernel kernel, CLDevice device, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetKernelWorkGroupInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetKernelWorkGroupInfo(kernel.getPointer(), device == null ? 0 : device.getPointer(), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetKernelWorkGroupInfo(long kernel, long device, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);

	public static int clEnqueueNDRangeKernel(CLCommandQueue command_queue, CLKernel kernel, int work_dim, PointerBuffer global_work_offset, PointerBuffer global_work_size, PointerBuffer local_work_size, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueNDRangeKernel;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (global_work_offset != null)
			BufferChecks.checkBuffer(global_work_offset, work_dim);
		if (global_work_size != null)
			BufferChecks.checkBuffer(global_work_size, work_dim);
		if (local_work_size != null)
			BufferChecks.checkBuffer(local_work_size, work_dim);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueNDRangeKernel(command_queue.getPointer(), kernel.getPointer(), work_dim, MemoryUtil.getAddressSafe(global_work_offset), MemoryUtil.getAddressSafe(global_work_size), MemoryUtil.getAddressSafe(local_work_size), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueNDRangeKernel(long command_queue, long kernel, int work_dim, long global_work_offset, long global_work_size, long local_work_size, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clEnqueueTask(CLCommandQueue command_queue, CLKernel kernel, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueTask;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueTask(command_queue.getPointer(), kernel.getPointer(), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueTask(long command_queue, long kernel, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	/**
	 *  Enqueues a native kernel to the specified command queue. The <code>mem_list</code> parameter
	 *  can be used to pass a list of <code>CLMem</code> objects that will be mapped to global memory space and
	 *  exposed as a <code>ByteBuffer</code> array in the <code>CLNativeKernel#execute</code> method. The
	 *  <code>sizes</code> parameter will be used to allocate direct <code>ByteBuffer</code>s with the correct
	 *  capacities. The user is responsible for passing appropriate values to avoid crashes.
	 * <p>
	 *  @param command_queue   the command queue
	 *  @param user_func       the native kernel
	 *  @param mem_list        the CLMem objects
	 *  @param sizes           the CLMem object sizes
	 *  @param event_wait_list the event wait list
	 *  @param event           the queue event
	 * <p>
	 *  @return the error code
	 */
	public static int clEnqueueNativeKernel(CLCommandQueue command_queue, CLNativeKernel user_func, CLMem[] mem_list, long[] sizes, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueNativeKernel;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (mem_list != null)
			BufferChecks.checkArray(mem_list, 1);
		if (sizes != null)
			BufferChecks.checkArray(sizes, mem_list.length);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		long user_func_ref = CallbackUtil.createGlobalRef(user_func);
		ByteBuffer args = APIUtil.getNativeKernelArgs(user_func_ref, mem_list, sizes);
		int __result = 0;
		try {
			__result = nclEnqueueNativeKernel(command_queue.getPointer(), user_func.getPointer(), MemoryUtil.getAddress0(args), args.remaining(), mem_list == null ? 0 : mem_list.length, mem_list, (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
			return __result;
		} finally {
			CallbackUtil.checkCallback(__result, user_func_ref);
		}
	}
	static native int nclEnqueueNativeKernel(long command_queue, long user_func, long args, long args_cb_args, int num_mem_objects, CLMem[] mem_list, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clWaitForEvents(PointerBuffer event_list) {
		long function_pointer = CLCapabilities.clWaitForEvents;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(event_list, 1);
		int __result = nclWaitForEvents(event_list.remaining(), MemoryUtil.getAddress(event_list), function_pointer);
		return __result;
	}
	static native int nclWaitForEvents(int event_list_num_events, long event_list, long function_pointer);

	/** Overloads clWaitForEvents. */
	public static int clWaitForEvents(CLEvent event) {
		long function_pointer = CLCapabilities.clWaitForEvents;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclWaitForEvents(1, APIUtil.getPointer(event), function_pointer);
		return __result;
	}

	public static int clGetEventInfo(CLEvent event, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetEventInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetEventInfo(event.getPointer(), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetEventInfo(long event, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);

	public static int clRetainEvent(CLEvent event) {
		long function_pointer = CLCapabilities.clRetainEvent;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclRetainEvent(event.getPointer(), function_pointer);
		if ( __result == CL_SUCCESS ) event.retain();
		return __result;
	}
	static native int nclRetainEvent(long event, long function_pointer);

	public static int clReleaseEvent(CLEvent event) {
		long function_pointer = CLCapabilities.clReleaseEvent;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclReleaseEvent(event.getPointer(), function_pointer);
		if ( __result == CL_SUCCESS ) event.release();
		return __result;
	}
	static native int nclReleaseEvent(long event, long function_pointer);

	public static int clEnqueueMarker(CLCommandQueue command_queue, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueMarker;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueMarker(command_queue.getPointer(), MemoryUtil.getAddress(event), function_pointer);
		if ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueMarker(long command_queue, long event, long function_pointer);

	public static int clEnqueueBarrier(CLCommandQueue command_queue) {
		long function_pointer = CLCapabilities.clEnqueueBarrier;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclEnqueueBarrier(command_queue.getPointer(), function_pointer);
		return __result;
	}
	static native int nclEnqueueBarrier(long command_queue, long function_pointer);

	public static int clEnqueueWaitForEvents(CLCommandQueue command_queue, PointerBuffer event_list) {
		long function_pointer = CLCapabilities.clEnqueueWaitForEvents;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(event_list, 1);
		int __result = nclEnqueueWaitForEvents(command_queue.getPointer(), event_list.remaining(), MemoryUtil.getAddress(event_list), function_pointer);
		return __result;
	}
	static native int nclEnqueueWaitForEvents(long command_queue, int event_list_num_events, long event_list, long function_pointer);

	/** Overloads clEnqueueWaitForEvents. */
	public static int clEnqueueWaitForEvents(CLCommandQueue command_queue, CLEvent event) {
		long function_pointer = CLCapabilities.clEnqueueWaitForEvents;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclEnqueueWaitForEvents(command_queue.getPointer(), 1, APIUtil.getPointer(event), function_pointer);
		return __result;
	}

	public static int clGetEventProfilingInfo(CLEvent event, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
		long function_pointer = CLCapabilities.clGetEventProfilingInfo;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (param_value != null)
			BufferChecks.checkDirect(param_value);
		if (param_value_size_ret != null)
			BufferChecks.checkBuffer(param_value_size_ret, 1);
		int __result = nclGetEventProfilingInfo(event.getPointer(), param_name, (param_value == null ? 0 : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
		return __result;
	}
	static native int nclGetEventProfilingInfo(long event, int param_name, long param_value_param_value_size, long param_value, long param_value_size_ret, long function_pointer);

	public static int clFlush(CLCommandQueue command_queue) {
		long function_pointer = CLCapabilities.clFlush;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclFlush(command_queue.getPointer(), function_pointer);
		return __result;
	}
	static native int nclFlush(long command_queue, long function_pointer);

	public static int clFinish(CLCommandQueue command_queue) {
		long function_pointer = CLCapabilities.clFinish;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclFinish(command_queue.getPointer(), function_pointer);
		return __result;
	}
	static native int nclFinish(long command_queue, long function_pointer);

	static CLFunctionAddress clGetExtensionFunctionAddress(ByteBuffer func_name) {
		long function_pointer = CLCapabilities.clGetExtensionFunctionAddress;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(func_name);
		BufferChecks.checkNullTerminated(func_name);
		CLFunctionAddress __result = new CLFunctionAddress(nclGetExtensionFunctionAddress(MemoryUtil.getAddress(func_name), function_pointer));
		return __result;
	}
	static native long nclGetExtensionFunctionAddress(long func_name, long function_pointer);

	/** Overloads clGetExtensionFunctionAddress. */
	static CLFunctionAddress clGetExtensionFunctionAddress(CharSequence func_name) {
		long function_pointer = CLCapabilities.clGetExtensionFunctionAddress;
		BufferChecks.checkFunctionAddress(function_pointer);
		CLFunctionAddress __result = new CLFunctionAddress(nclGetExtensionFunctionAddress(APIUtil.getBufferNT(func_name), function_pointer));
		return __result;
	}
}
