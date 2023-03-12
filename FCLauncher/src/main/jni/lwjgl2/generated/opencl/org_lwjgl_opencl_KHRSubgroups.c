/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extcl.h"

typedef CL_API_ENTRY cl_int (CL_API_CALL *clGetKernelSubGroupInfoKHRPROC) (cl_kernel kernel, cl_device_id device, cl_kernel_sub_group_info param_name, size_t input_value_size, const cl_void * input_value, size_t param_value_size, cl_void * param_value, size_t * param_value_size_ret);

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_KHRSubgroups_nclGetKernelSubGroupInfoKHR(JNIEnv *env, jclass clazz, jlong kernel, jlong device, jint param_name, jlong input_value_size, jlong input_value, jlong param_value_size, jlong param_value, jlong param_value_size_ret, jlong function_pointer) {
	const cl_void *input_value_address = (const cl_void *)(intptr_t)input_value;
	cl_void *param_value_address = (cl_void *)(intptr_t)param_value;
	size_t *param_value_size_ret_address = (size_t *)(intptr_t)param_value_size_ret;
	clGetKernelSubGroupInfoKHRPROC clGetKernelSubGroupInfoKHR = (clGetKernelSubGroupInfoKHRPROC)((intptr_t)function_pointer);
	cl_int __result = clGetKernelSubGroupInfoKHR((cl_kernel)(intptr_t)kernel, (cl_device_id)(intptr_t)device, param_name, input_value_size, input_value_address, param_value_size, param_value_address, param_value_size_ret_address);
	return __result;
}

