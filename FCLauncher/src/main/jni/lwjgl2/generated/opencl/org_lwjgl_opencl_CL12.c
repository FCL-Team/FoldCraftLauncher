/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extcl.h"

typedef CL_API_ENTRY cl_int (CL_API_CALL *clRetainDevicePROC) (cl_device_id device);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clReleaseDevicePROC) (cl_device_id device);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clCreateSubDevicesPROC) (cl_device_id in_device, const cl_device_partition_property * properties, cl_uint num_devices, cl_device_id * out_devices, cl_uint * num_devices_ret);
typedef CL_API_ENTRY cl_mem (CL_API_CALL *clCreateImagePROC) (cl_context context, cl_mem_flags flags, const cl_image_format * image_format, const cl_image_desc * image_desc, cl_void * host_ptr, cl_int * errcode_ret);
typedef CL_API_ENTRY cl_program (CL_API_CALL *clCreateProgramWithBuiltInKernelsPROC) (cl_context context, cl_uint num_devices, const cl_device_id * device_list, const cl_char * kernel_names, cl_int * errcode_ret);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clCompileProgramPROC) (cl_program program, cl_uint num_devices, const cl_device_id * device_list, const cl_char * options, cl_uint num_input_headers, const cl_program * input_header, const cl_char ** header_include_name, cl_program_callback pfn_notify, void * user_data);
typedef CL_API_ENTRY cl_program (CL_API_CALL *clLinkProgramPROC) (cl_context context, cl_uint num_devices, const cl_device_id * device_list, const cl_char * options, cl_uint num_input_programs, const cl_program * input_programs, cl_program_callback pfn_notify, void * user_data, cl_int * errcode_ret);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clUnloadPlatformCompilerPROC) (cl_platform_id platform);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clGetKernelArgInfoPROC) (cl_kernel kernel, cl_uint arg_indx, cl_kernel_arg_info param_name, size_t param_value_size, cl_void * param_value, size_t * param_value_size_ret);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clEnqueueFillBufferPROC) (cl_command_queue command_queue, cl_mem buffer, const cl_void * pattern, size_t pattern_size, size_t offset, size_t size, cl_uint num_events_in_wait_list, const cl_event * event_wait_list, cl_event * event);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clEnqueueFillImagePROC) (cl_command_queue command_queue, cl_mem image, const cl_void * fill_color, const size_t * origin, const size_t * region, cl_uint num_events_in_wait_list, const cl_event * event_wait_list, cl_event * event);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clEnqueueMigrateMemObjectsPROC) (cl_command_queue command_queue, cl_uint num_mem_objects, const cl_mem * mem_objects, cl_mem_migration_flags flags, cl_uint num_events_in_wait_list, const cl_event * event_wait_list, cl_event * event);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clEnqueueMarkerWithWaitListPROC) (cl_command_queue command_queue, cl_uint num_events_in_wait_list, const cl_event * event_wait_list, cl_event * event);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clEnqueueBarrierWithWaitListPROC) (cl_command_queue command_queue, cl_uint num_events_in_wait_list, const cl_event * event_wait_list, cl_event * event);
typedef CL_API_ENTRY cl_int (CL_API_CALL *clSetPrintfCallbackPROC) (cl_context context, cl_printf_callback pfn_notify, void * user_data);
typedef CL_API_ENTRY void * (CL_API_CALL *clGetExtensionFunctionAddressForPlatformPROC) (cl_platform_id platform, const cl_char * func_name);

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL12_nclRetainDevice(JNIEnv *env, jclass clazz, jlong device, jlong function_pointer) {
	clRetainDevicePROC clRetainDevice = (clRetainDevicePROC)((intptr_t)function_pointer);
	cl_int __result = clRetainDevice((cl_device_id)(intptr_t)device);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL12_nclReleaseDevice(JNIEnv *env, jclass clazz, jlong device, jlong function_pointer) {
	clReleaseDevicePROC clReleaseDevice = (clReleaseDevicePROC)((intptr_t)function_pointer);
	cl_int __result = clReleaseDevice((cl_device_id)(intptr_t)device);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL12_nclCreateSubDevices(JNIEnv *env, jclass clazz, jlong in_device, jlong properties, jint num_devices, jlong out_devices, jlong num_devices_ret, jlong function_pointer) {
	const cl_device_partition_property *properties_address = (const cl_device_partition_property *)(intptr_t)properties;
	cl_device_id *out_devices_address = (cl_device_id *)(intptr_t)out_devices;
	cl_uint *num_devices_ret_address = (cl_uint *)(intptr_t)num_devices_ret;
	clCreateSubDevicesPROC clCreateSubDevices = (clCreateSubDevicesPROC)((intptr_t)function_pointer);
	cl_int __result = clCreateSubDevices((cl_device_id)(intptr_t)in_device, properties_address, num_devices, out_devices_address, num_devices_ret_address);
	return __result;
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opencl_CL12_nclCreateImage(JNIEnv *env, jclass clazz, jlong context, jlong flags, jlong image_format, jlong image_desc, jlong host_ptr, jlong errcode_ret, jlong function_pointer) {
	const cl_image_format *image_format_address = (const cl_image_format *)(intptr_t)image_format;
	const cl_image_desc *image_desc_address = (const cl_image_desc *)(intptr_t)image_desc;
	cl_void *host_ptr_address = (cl_void *)(intptr_t)host_ptr;
	cl_int *errcode_ret_address = (cl_int *)(intptr_t)errcode_ret;
	clCreateImagePROC clCreateImage = (clCreateImagePROC)((intptr_t)function_pointer);
	cl_mem __result = clCreateImage((cl_context)(intptr_t)context, flags, image_format_address, image_desc_address, host_ptr_address, errcode_ret_address);
	return (intptr_t)__result;
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opencl_CL12_nclCreateProgramWithBuiltInKernels(JNIEnv *env, jclass clazz, jlong context, jint num_devices, jlong device_list, jlong kernel_names, jlong errcode_ret, jlong function_pointer) {
	const cl_device_id *device_list_address = (const cl_device_id *)(intptr_t)device_list;
	const cl_char *kernel_names_address = (const cl_char *)(intptr_t)kernel_names;
	cl_int *errcode_ret_address = (cl_int *)(intptr_t)errcode_ret;
	clCreateProgramWithBuiltInKernelsPROC clCreateProgramWithBuiltInKernels = (clCreateProgramWithBuiltInKernelsPROC)((intptr_t)function_pointer);
	cl_program __result = clCreateProgramWithBuiltInKernels((cl_context)(intptr_t)context, num_devices, device_list_address, kernel_names_address, errcode_ret_address);
	return (intptr_t)__result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL12_nclCompileProgram(JNIEnv *env, jclass clazz, jlong program, jint num_devices, jlong device_list, jlong options, jint num_input_headers, jlong input_header, jlong header_include_name, jlong pfn_notify, jlong user_data, jlong function_pointer) {
	const cl_device_id *device_list_address = (const cl_device_id *)(intptr_t)device_list;
	const cl_char *options_address = (const cl_char *)(intptr_t)options;
	const cl_program *input_header_address = (const cl_program *)(intptr_t)input_header;
	const cl_char *header_include_name_address = (const cl_char *)(intptr_t)header_include_name;
	clCompileProgramPROC clCompileProgram = (clCompileProgramPROC)((intptr_t)function_pointer);
	cl_int __result = clCompileProgram((cl_program)(intptr_t)program, num_devices, device_list_address, options_address, num_input_headers, input_header_address, (const cl_char **)&header_include_name_address, (cl_program_callback)(intptr_t)pfn_notify, (void *)(intptr_t)user_data);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL12_nclCompileProgramMulti(JNIEnv *env, jclass clazz, jlong program, jint num_devices, jlong device_list, jlong options, jint num_input_headers, jlong input_headers, jlong header_include_names, jlong pfn_notify, jlong user_data, jlong function_pointer) {
	const cl_device_id *device_list_address = (const cl_device_id *)(intptr_t)device_list;
	const cl_char *options_address = (const cl_char *)(intptr_t)options;
	const cl_program *input_headers_address = (const cl_program *)(intptr_t)input_headers;
	const cl_char *header_include_names_address = (const cl_char *)(intptr_t)header_include_names;
	int _str_i;
	cl_char *_str_address;
	cl_char **header_include_names_str = (cl_char **) malloc(num_input_headers * sizeof(cl_char *));
	clCompileProgramPROC clCompileProgram = (clCompileProgramPROC)((intptr_t)function_pointer);
	cl_int __result;
	_str_i = 0;
	_str_address = (cl_char *)header_include_names_address;
	while ( _str_i < num_input_headers ) {
		header_include_names_str[_str_i++] = _str_address;
		_str_address += strlen((const char *)_str_address) + 1;
	}
	__result = clCompileProgram((cl_program)(intptr_t)program, num_devices, device_list_address, options_address, num_input_headers, input_headers_address, (const cl_char **)&header_include_names_str, (cl_program_callback)(intptr_t)pfn_notify, (void *)(intptr_t)user_data);
	free(header_include_names_str);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL12_nclCompileProgram3(JNIEnv *env, jclass clazz, jlong program, jint num_devices, jlong device_list, jlong options, jint num_input_headers, jlong input_headers, jobjectArray header_include_names, jlong pfn_notify, jlong user_data, jlong function_pointer) {
	const cl_device_id *device_list_address = (const cl_device_id *)(intptr_t)device_list;
	const cl_char *options_address = (const cl_char *)(intptr_t)options;
	const cl_program *input_headers_address = (const cl_program *)(intptr_t)input_headers;
	int _ptr_i;
	jobject _ptr_object;
	cl_char **header_include_names_ptr = (cl_char **) malloc(num_input_headers * sizeof(cl_char *));
	clCompileProgramPROC clCompileProgram = (clCompileProgramPROC)((intptr_t)function_pointer);
	cl_int __result;
	_ptr_i = 0;
	while ( _ptr_i < num_input_headers ) {
		_ptr_object = (*env)->GetObjectArrayElement(env, header_include_names, _ptr_i);
		header_include_names_ptr[_ptr_i++] = (cl_char *)(*env)->GetDirectBufferAddress(env, _ptr_object);
	}
	__result = clCompileProgram((cl_program)(intptr_t)program, num_devices, device_list_address, options_address, num_input_headers, input_headers_address, (const cl_char **)header_include_names_ptr, (cl_program_callback)(intptr_t)pfn_notify, (void *)(intptr_t)user_data);
	free(header_include_names_ptr);
	return __result;
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opencl_CL12_nclLinkProgram(JNIEnv *env, jclass clazz, jlong context, jint num_devices, jlong device_list, jlong options, jint num_input_programs, jlong input_programs, jlong pfn_notify, jlong user_data, jlong errcode_ret, jlong function_pointer) {
	const cl_device_id *device_list_address = (const cl_device_id *)(intptr_t)device_list;
	const cl_char *options_address = (const cl_char *)(intptr_t)options;
	const cl_program *input_programs_address = (const cl_program *)(intptr_t)input_programs;
	cl_int *errcode_ret_address = (cl_int *)(intptr_t)errcode_ret;
	clLinkProgramPROC clLinkProgram = (clLinkProgramPROC)((intptr_t)function_pointer);
	cl_program __result = clLinkProgram((cl_context)(intptr_t)context, num_devices, device_list_address, options_address, num_input_programs, input_programs_address, (cl_program_callback)(intptr_t)pfn_notify, (void *)(intptr_t)user_data, errcode_ret_address);
	return (intptr_t)__result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL12_nclUnloadPlatformCompiler(JNIEnv *env, jclass clazz, jlong platform, jlong function_pointer) {
	clUnloadPlatformCompilerPROC clUnloadPlatformCompiler = (clUnloadPlatformCompilerPROC)((intptr_t)function_pointer);
	cl_int __result = clUnloadPlatformCompiler((cl_platform_id)(intptr_t)platform);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL12_nclGetKernelArgInfo(JNIEnv *env, jclass clazz, jlong kernel, jint arg_indx, jint param_name, jlong param_value_size, jlong param_value, jlong param_value_size_ret, jlong function_pointer) {
	cl_void *param_value_address = (cl_void *)(intptr_t)param_value;
	size_t *param_value_size_ret_address = (size_t *)(intptr_t)param_value_size_ret;
	clGetKernelArgInfoPROC clGetKernelArgInfo = (clGetKernelArgInfoPROC)((intptr_t)function_pointer);
	cl_int __result = clGetKernelArgInfo((cl_kernel)(intptr_t)kernel, arg_indx, param_name, param_value_size, param_value_address, param_value_size_ret_address);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL12_nclEnqueueFillBuffer(JNIEnv *env, jclass clazz, jlong command_queue, jlong buffer, jlong pattern, jlong pattern_size, jlong offset, jlong size, jint num_events_in_wait_list, jlong event_wait_list, jlong event, jlong function_pointer) {
	const cl_void *pattern_address = (const cl_void *)(intptr_t)pattern;
	const cl_event *event_wait_list_address = (const cl_event *)(intptr_t)event_wait_list;
	cl_event *event_address = (cl_event *)(intptr_t)event;
	clEnqueueFillBufferPROC clEnqueueFillBuffer = (clEnqueueFillBufferPROC)((intptr_t)function_pointer);
	cl_int __result = clEnqueueFillBuffer((cl_command_queue)(intptr_t)command_queue, (cl_mem)(intptr_t)buffer, pattern_address, pattern_size, offset, size, num_events_in_wait_list, event_wait_list_address, event_address);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL12_nclEnqueueFillImage(JNIEnv *env, jclass clazz, jlong command_queue, jlong image, jlong fill_color, jlong origin, jlong region, jint num_events_in_wait_list, jlong event_wait_list, jlong event, jlong function_pointer) {
	const cl_void *fill_color_address = (const cl_void *)(intptr_t)fill_color;
	const size_t *origin_address = (const size_t *)(intptr_t)origin;
	const size_t *region_address = (const size_t *)(intptr_t)region;
	const cl_event *event_wait_list_address = (const cl_event *)(intptr_t)event_wait_list;
	cl_event *event_address = (cl_event *)(intptr_t)event;
	clEnqueueFillImagePROC clEnqueueFillImage = (clEnqueueFillImagePROC)((intptr_t)function_pointer);
	cl_int __result = clEnqueueFillImage((cl_command_queue)(intptr_t)command_queue, (cl_mem)(intptr_t)image, fill_color_address, origin_address, region_address, num_events_in_wait_list, event_wait_list_address, event_address);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL12_nclEnqueueMigrateMemObjects(JNIEnv *env, jclass clazz, jlong command_queue, jint num_mem_objects, jlong mem_objects, jlong flags, jint num_events_in_wait_list, jlong event_wait_list, jlong event, jlong function_pointer) {
	const cl_mem *mem_objects_address = (const cl_mem *)(intptr_t)mem_objects;
	const cl_event *event_wait_list_address = (const cl_event *)(intptr_t)event_wait_list;
	cl_event *event_address = (cl_event *)(intptr_t)event;
	clEnqueueMigrateMemObjectsPROC clEnqueueMigrateMemObjects = (clEnqueueMigrateMemObjectsPROC)((intptr_t)function_pointer);
	cl_int __result = clEnqueueMigrateMemObjects((cl_command_queue)(intptr_t)command_queue, num_mem_objects, mem_objects_address, flags, num_events_in_wait_list, event_wait_list_address, event_address);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL12_nclEnqueueMarkerWithWaitList(JNIEnv *env, jclass clazz, jlong command_queue, jint num_events_in_wait_list, jlong event_wait_list, jlong event, jlong function_pointer) {
	const cl_event *event_wait_list_address = (const cl_event *)(intptr_t)event_wait_list;
	cl_event *event_address = (cl_event *)(intptr_t)event;
	clEnqueueMarkerWithWaitListPROC clEnqueueMarkerWithWaitList = (clEnqueueMarkerWithWaitListPROC)((intptr_t)function_pointer);
	cl_int __result = clEnqueueMarkerWithWaitList((cl_command_queue)(intptr_t)command_queue, num_events_in_wait_list, event_wait_list_address, event_address);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL12_nclEnqueueBarrierWithWaitList(JNIEnv *env, jclass clazz, jlong command_queue, jint num_events_in_wait_list, jlong event_wait_list, jlong event, jlong function_pointer) {
	const cl_event *event_wait_list_address = (const cl_event *)(intptr_t)event_wait_list;
	cl_event *event_address = (cl_event *)(intptr_t)event;
	clEnqueueBarrierWithWaitListPROC clEnqueueBarrierWithWaitList = (clEnqueueBarrierWithWaitListPROC)((intptr_t)function_pointer);
	cl_int __result = clEnqueueBarrierWithWaitList((cl_command_queue)(intptr_t)command_queue, num_events_in_wait_list, event_wait_list_address, event_address);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_CL12_nclSetPrintfCallback(JNIEnv *env, jclass clazz, jlong context, jlong pfn_notify, jlong user_data, jlong function_pointer) {
	clSetPrintfCallbackPROC clSetPrintfCallback = (clSetPrintfCallbackPROC)((intptr_t)function_pointer);
	cl_int __result = clSetPrintfCallback((cl_context)(intptr_t)context, (cl_printf_callback)(intptr_t)pfn_notify, (void *)(intptr_t)user_data);
	return __result;
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opencl_CL12_nclGetExtensionFunctionAddressForPlatform(JNIEnv *env, jclass clazz, jlong platform, jlong func_name, jlong function_pointer) {
	const cl_char *func_name_address = (const cl_char *)(intptr_t)func_name;
	clGetExtensionFunctionAddressForPlatformPROC clGetExtensionFunctionAddressForPlatform = (clGetExtensionFunctionAddressForPlatformPROC)((intptr_t)function_pointer);
	void * __result = clGetExtensionFunctionAddressForPlatform((cl_platform_id)(intptr_t)platform, func_name_address);
	return (intptr_t)__result;
}

