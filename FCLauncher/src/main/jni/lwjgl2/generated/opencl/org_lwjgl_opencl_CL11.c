/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extcl.h"

typedef CL_API_ENTRY cl_mem (CL_API_CALL *clCreateSubBufferPROC) (cl_mem buffer, cl_mem_flags flags, cl_buffer_create_type buffer_create_type, const cl_void * buffer_create_info, cl_int * errcode_ret);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clSetMemObjectDestructorCallbackPROC) (cl_mem memobj, cl_mem_object_destructor_callback pfn_notify, void * user_data);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clEnqueueReadBufferRectPROC) (cl_command_queue command_queue, cl_mem buffer, cl_bool blocking_read, const size_t * buffer_offset, const size_t * host_offset, const size_t * region, size_t buffer_row_pitch, size_t buffer_slice_pitch, size_t host_row_pitch, size_t host_slice_pitch, cl_void * ptr, cl_uint num_events_in_wait_list, const cl_event * event_wait_list, cl_event * event);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clEnqueueWriteBufferRectPROC) (cl_command_queue command_queue, cl_mem buffer, cl_bool blocking_write, const size_t * buffer_offset, const size_t * host_offset, const size_t * region, size_t buffer_row_pitch, size_t buffer_slice_pitch, size_t host_row_pitch, size_t host_slice_pitch, const cl_void * ptr, cl_uint num_events_in_wait_list, const cl_event * event_wait_list, cl_event * event);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clEnqueueCopyBufferRectPROC) (cl_command_queue command_queue, cl_mem src_buffer, cl_mem dst_buffer, const size_t * src_origin, const size_t * dst_origin, const size_t * region, size_t src_row_pitch, size_t src_slice_pitch, size_t dst_row_pitch, size_t dst_slice_pitch, cl_uint num_events_in_wait_list, const cl_event * event_wait_list, cl_event * event);
typedef CL_API_ENTRY cl_event (CL_API_CALL *clCreateUserEventPROC) (cl_context context, cl_int * errcode_ret);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clSetUserEventStatusPROC) (cl_event event, cl_int execution_status);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clSetEventCallbackPROC) (cl_event event, cl_int command_exec_callback_type, cl_event_callback pfn_notify, void * user_data);

JNIEXPORT jlong JNICALL Java_org_lwjgl_opencl_CL11_nclCreateSubBuffer(JNIEnv *env, jclass clazz, jlong buffer, jlong flags, jint buffer_create_type, jlong buffer_create_info, jlong errcode_ret, jlong function_pointer) {
	const cl_void *buffer_create_info_address = (const cl_void *)(intptr_t)buffer_create_info;
	cl_int *errcode_ret_address = (cl_int *)(intptr_t)errcode_ret;
	clCreateSubBufferPROC clCreateSubBuffer = (clCreateSubBufferPROC)((intptr_t)function_pointer);
	cl_mem __result = clCreateSubBuffer((cl_mem)(intptr_t)buffer, flags, buffer_create_type, buffer_create_info_address, errcode_ret_address);
	return (intptr_t)__result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL11_nclSetMemObjectDestructorCallback(JNIEnv *env, jclass clazz, jlong memobj, jlong pfn_notify, jlong user_data, jlong function_pointer) {
	clSetMemObjectDestructorCallbackPROC clSetMemObjectDestructorCallback = (clSetMemObjectDestructorCallbackPROC)((intptr_t)function_pointer);
	cl_int __result = clSetMemObjectDestructorCallback((cl_mem)(intptr_t)memobj, (cl_mem_object_destructor_callback)(intptr_t)pfn_notify, (void *)(intptr_t)user_data);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL11_nclEnqueueReadBufferRect(JNIEnv *env, jclass clazz, jlong command_queue, jlong buffer, jint blocking_read, jlong buffer_offset, jlong host_offset, jlong region, jlong buffer_row_pitch, jlong buffer_slice_pitch, jlong host_row_pitch, jlong host_slice_pitch, jlong ptr, jint num_events_in_wait_list, jlong event_wait_list, jlong event, jlong function_pointer) {
	const size_t *buffer_offset_address = (const size_t *)(intptr_t)buffer_offset;
	const size_t *host_offset_address = (const size_t *)(intptr_t)host_offset;
	const size_t *region_address = (const size_t *)(intptr_t)region;
	cl_void *ptr_address = (cl_void *)(intptr_t)ptr;
	const cl_event *event_wait_list_address = (const cl_event *)(intptr_t)event_wait_list;
	cl_event *event_address = (cl_event *)(intptr_t)event;
	clEnqueueReadBufferRectPROC clEnqueueReadBufferRect = (clEnqueueReadBufferRectPROC)((intptr_t)function_pointer);
	cl_int __result = clEnqueueReadBufferRect((cl_command_queue)(intptr_t)command_queue, (cl_mem)(intptr_t)buffer, blocking_read, buffer_offset_address, host_offset_address, region_address, buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, ptr_address, num_events_in_wait_list, event_wait_list_address, event_address);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL11_nclEnqueueWriteBufferRect(JNIEnv *env, jclass clazz, jlong command_queue, jlong buffer, jint blocking_write, jlong buffer_offset, jlong host_offset, jlong region, jlong buffer_row_pitch, jlong buffer_slice_pitch, jlong host_row_pitch, jlong host_slice_pitch, jlong ptr, jint num_events_in_wait_list, jlong event_wait_list, jlong event, jlong function_pointer) {
	const size_t *buffer_offset_address = (const size_t *)(intptr_t)buffer_offset;
	const size_t *host_offset_address = (const size_t *)(intptr_t)host_offset;
	const size_t *region_address = (const size_t *)(intptr_t)region;
	const cl_void *ptr_address = (const cl_void *)(intptr_t)ptr;
	const cl_event *event_wait_list_address = (const cl_event *)(intptr_t)event_wait_list;
	cl_event *event_address = (cl_event *)(intptr_t)event;
	clEnqueueWriteBufferRectPROC clEnqueueWriteBufferRect = (clEnqueueWriteBufferRectPROC)((intptr_t)function_pointer);
	cl_int __result = clEnqueueWriteBufferRect((cl_command_queue)(intptr_t)command_queue, (cl_mem)(intptr_t)buffer, blocking_write, buffer_offset_address, host_offset_address, region_address, buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, ptr_address, num_events_in_wait_list, event_wait_list_address, event_address);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL11_nclEnqueueCopyBufferRect(JNIEnv *env, jclass clazz, jlong command_queue, jlong src_buffer, jlong dst_buffer, jlong src_origin, jlong dst_origin, jlong region, jlong src_row_pitch, jlong src_slice_pitch, jlong dst_row_pitch, jlong dst_slice_pitch, jint num_events_in_wait_list, jlong event_wait_list, jlong event, jlong function_pointer) {
	const size_t *src_origin_address = (const size_t *)(intptr_t)src_origin;
	const size_t *dst_origin_address = (const size_t *)(intptr_t)dst_origin;
	const size_t *region_address = (const size_t *)(intptr_t)region;
	const cl_event *event_wait_list_address = (const cl_event *)(intptr_t)event_wait_list;
	cl_event *event_address = (cl_event *)(intptr_t)event;
	clEnqueueCopyBufferRectPROC clEnqueueCopyBufferRect = (clEnqueueCopyBufferRectPROC)((intptr_t)function_pointer);
	cl_int __result = clEnqueueCopyBufferRect((cl_command_queue)(intptr_t)command_queue, (cl_mem)(intptr_t)src_buffer, (cl_mem)(intptr_t)dst_buffer, src_origin_address, dst_origin_address, region_address, src_row_pitch, src_slice_pitch, dst_row_pitch, dst_slice_pitch, num_events_in_wait_list, event_wait_list_address, event_address);
	return __result;
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opencl_CL11_nclCreateUserEvent(JNIEnv *env, jclass clazz, jlong context, jlong errcode_ret, jlong function_pointer) {
	cl_int *errcode_ret_address = (cl_int *)(intptr_t)errcode_ret;
	clCreateUserEventPROC clCreateUserEvent = (clCreateUserEventPROC)((intptr_t)function_pointer);
	cl_event __result = clCreateUserEvent((cl_context)(intptr_t)context, errcode_ret_address);
	return (intptr_t)__result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL11_nclSetUserEventStatus(JNIEnv *env, jclass clazz, jlong event, jint execution_status, jlong function_pointer) {
	clSetUserEventStatusPROC clSetUserEventStatus = (clSetUserEventStatusPROC)((intptr_t)function_pointer);
	cl_int __result = clSetUserEventStatus((cl_event)(intptr_t)event, execution_status);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL11_nclSetEventCallback(JNIEnv *env, jclass clazz, jlong event, jint command_exec_callback_type, jlong pfn_notify, jlong user_data, jlong function_pointer) {
	clSetEventCallbackPROC clSetEventCallback = (clSetEventCallbackPROC)((intptr_t)function_pointer);
	cl_int __result = clSetEventCallback((cl_event)(intptr_t)event, command_exec_callback_type, (cl_event_callback)(intptr_t)pfn_notify, (void *)(intptr_t)user_data);
	return __result;
}

