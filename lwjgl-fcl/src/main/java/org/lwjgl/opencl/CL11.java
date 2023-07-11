/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opencl;

import org.lwjgl.*;
import java.nio.*;

/**
 * The core OpenCL 1.1 API 
 */
public final class CL11 {

	/**
	 * Error Codes 
	 */
	public static final int CL_MISALIGNED_SUB_BUFFER_OFFSET = 0xFFFFFFF3,
		CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST = 0xFFFFFFF2,
		CL_INVALID_PROPERTY = 0xFFFFFFC0;

	/**
	 * OpenCL Version 
	 */
	public static final int CL_VERSION_1_1 = 0x1;

	/**
	 * cl_device_info 
	 */
	public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_HALF = 0x1034,
		CL_DEVICE_HOST_UNIFIED_MEMORY = 0x1035,
		CL_DEVICE_NATIVE_VECTOR_WIDTH_CHAR = 0x1036,
		CL_DEVICE_NATIVE_VECTOR_WIDTH_SHORT = 0x1037,
		CL_DEVICE_NATIVE_VECTOR_WIDTH_INT = 0x1038,
		CL_DEVICE_NATIVE_VECTOR_WIDTH_LONG = 0x1039,
		CL_DEVICE_NATIVE_VECTOR_WIDTH_FLOAT = 0x103A,
		CL_DEVICE_NATIVE_VECTOR_WIDTH_DOUBLE = 0x103B,
		CL_DEVICE_NATIVE_VECTOR_WIDTH_HALF = 0x103C,
		CL_DEVICE_OPENCL_C_VERSION = 0x103D;

	/**
	 * cl_device_fp_config - bitfield 
	 */
	public static final int CL_FP_SOFT_FLOAT = 0x40;

	/**
	 * cl_context_info 
	 */
	public static final int CL_CONTEXT_NUM_DEVICES = 0x1083;

	/**
	 * cl_channel_order 
	 */
	public static final int CL_Rx = 0x10BA,
		CL_RGx = 0x10BB,
		CL_RGBx = 0x10BC;

	/**
	 * cl_mem_info 
	 */
	public static final int CL_MEM_ASSOCIATED_MEMOBJECT = 0x1107,
		CL_MEM_OFFSET = 0x1108;

	/**
	 * cl_addressing_mode 
	 */
	public static final int CL_ADDRESS_MIRRORED_REPEAT = 0x1134;

	/**
	 * cl_kernel_work_group_info 
	 */
	public static final int CL_KERNEL_PREFERRED_WORK_GROUP_SIZE_MULTIPLE = 0x11B3,
		CL_KERNEL_PRIVATE_MEM_SIZE = 0x11B4;

	/**
	 * cl_event_info 
	 */
	public static final int CL_EVENT_CONTEXT = 0x11D4;

	/**
	 * cl_command_type 
	 */
	public static final int CL_COMMAND_READ_BUFFER_RECT = 0x1201,
		CL_COMMAND_WRITE_BUFFER_RECT = 0x1202,
		CL_COMMAND_COPY_BUFFER_RECT = 0x1203,
		CL_COMMAND_USER = 0x1204;

	/**
	 * cl_buffer_create_type 
	 */
	public static final int CL_BUFFER_CREATE_TYPE_REGION = 0x1220;

	private CL11() {}

	public static CLMem clCreateSubBuffer(CLMem buffer, long flags, int buffer_create_type, ByteBuffer buffer_create_info, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateSubBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(buffer_create_info, 2 * PointerBuffer.getPointerSize());
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLMem __result = CLMem.create(nclCreateSubBuffer(buffer.getPointer(), flags, buffer_create_type, MemoryUtil.getAddress(buffer_create_info), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), buffer.getParent());
		return __result;
	}
	static native long nclCreateSubBuffer(long buffer, long flags, int buffer_create_type, long buffer_create_info, long errcode_ret, long function_pointer);

	public static int clSetMemObjectDestructorCallback(CLMem memobj, CLMemObjectDestructorCallback pfn_notify) {
		long function_pointer = CLCapabilities.clSetMemObjectDestructorCallback;
		BufferChecks.checkFunctionAddress(function_pointer);
		long user_data = CallbackUtil.createGlobalRef(pfn_notify);
		int __result = 0;
		try {
			__result = nclSetMemObjectDestructorCallback(memobj.getPointer(), pfn_notify.getPointer(), user_data, function_pointer);
			return __result;
		} finally {
			CallbackUtil.checkCallback(__result, user_data);
		}
	}
	static native int nclSetMemObjectDestructorCallback(long memobj, long pfn_notify, long user_data, long function_pointer);

	public static int clEnqueueReadBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_read, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, ByteBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(buffer_offset, 3);
		BufferChecks.checkBuffer(host_offset, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueReadBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_read, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, DoubleBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(buffer_offset, 3);
		BufferChecks.checkBuffer(host_offset, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueReadBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_read, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, FloatBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(buffer_offset, 3);
		BufferChecks.checkBuffer(host_offset, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueReadBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_read, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, IntBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(buffer_offset, 3);
		BufferChecks.checkBuffer(host_offset, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueReadBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_read, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, LongBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(buffer_offset, 3);
		BufferChecks.checkBuffer(host_offset, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueReadBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_read, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, ShortBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(buffer_offset, 3);
		BufferChecks.checkBuffer(host_offset, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueReadBufferRect(long command_queue, long buffer, int blocking_read, long buffer_offset, long host_offset, long region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, long ptr, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clEnqueueWriteBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_write, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, ByteBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(buffer_offset, 3);
		BufferChecks.checkBuffer(host_offset, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueWriteBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_write, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, DoubleBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(buffer_offset, 3);
		BufferChecks.checkBuffer(host_offset, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueWriteBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_write, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, FloatBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(buffer_offset, 3);
		BufferChecks.checkBuffer(host_offset, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueWriteBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_write, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, IntBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(buffer_offset, 3);
		BufferChecks.checkBuffer(host_offset, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueWriteBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_write, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, LongBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(buffer_offset, 3);
		BufferChecks.checkBuffer(host_offset, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	public static int clEnqueueWriteBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_write, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, ShortBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(buffer_offset, 3);
		BufferChecks.checkBuffer(host_offset, 3);
		BufferChecks.checkBuffer(region, 3);
		BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueWriteBufferRect(long command_queue, long buffer, int blocking_write, long buffer_offset, long host_offset, long region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, long ptr, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static int clEnqueueCopyBufferRect(CLCommandQueue command_queue, CLMem src_buffer, CLMem dst_buffer, PointerBuffer src_origin, PointerBuffer dst_origin, PointerBuffer region, long src_row_pitch, long src_slice_pitch, long dst_row_pitch, long dst_slice_pitch, PointerBuffer event_wait_list, PointerBuffer event) {
		long function_pointer = CLCapabilities.clEnqueueCopyBufferRect;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(src_origin, 3);
		BufferChecks.checkBuffer(dst_origin, 3);
		BufferChecks.checkBuffer(region, 3);
		if (event_wait_list != null)
			BufferChecks.checkDirect(event_wait_list);
		if (event != null)
			BufferChecks.checkBuffer(event, 1);
		int __result = nclEnqueueCopyBufferRect(command_queue.getPointer(), src_buffer.getPointer(), dst_buffer.getPointer(), MemoryUtil.getAddress(src_origin), MemoryUtil.getAddress(dst_origin), MemoryUtil.getAddress(region), src_row_pitch, src_slice_pitch, dst_row_pitch, dst_slice_pitch, (event_wait_list == null ? 0 : event_wait_list.remaining()), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);
		return __result;
	}
	static native int nclEnqueueCopyBufferRect(long command_queue, long src_buffer, long dst_buffer, long src_origin, long dst_origin, long region, long src_row_pitch, long src_slice_pitch, long dst_row_pitch, long dst_slice_pitch, int event_wait_list_num_events_in_wait_list, long event_wait_list, long event, long function_pointer);

	public static CLEvent clCreateUserEvent(CLContext context, IntBuffer errcode_ret) {
		long function_pointer = CLCapabilities.clCreateUserEvent;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (errcode_ret != null)
			BufferChecks.checkBuffer(errcode_ret, 1);
		CLEvent __result = new CLEvent(nclCreateUserEvent(context.getPointer(), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
		return __result;
	}
	static native long nclCreateUserEvent(long context, long errcode_ret, long function_pointer);

	public static int clSetUserEventStatus(CLEvent event, int execution_status) {
		long function_pointer = CLCapabilities.clSetUserEventStatus;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclSetUserEventStatus(event.getPointer(), execution_status, function_pointer);
		return __result;
	}
	static native int nclSetUserEventStatus(long event, int execution_status, long function_pointer);

	public static int clSetEventCallback(CLEvent event, int command_exec_callback_type, CLEventCallback pfn_notify) {
		long function_pointer = CLCapabilities.clSetEventCallback;
		BufferChecks.checkFunctionAddress(function_pointer);
		long user_data = CallbackUtil.createGlobalRef(pfn_notify);
		pfn_notify.setRegistry(event.getParentRegistry());
		int __result = 0;
		try {
			__result = nclSetEventCallback(event.getPointer(), command_exec_callback_type, pfn_notify.getPointer(), user_data, function_pointer);
			return __result;
		} finally {
			CallbackUtil.checkCallback(__result, user_data);
		}
	}
	static native int nclSetEventCallback(long event, int command_exec_callback_type, long pfn_notify, long user_data, long function_pointer);
}
