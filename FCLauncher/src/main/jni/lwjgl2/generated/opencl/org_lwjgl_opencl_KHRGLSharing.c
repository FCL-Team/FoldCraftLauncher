/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extcl.h"

typedef CL_API_ENTRY cl_int (CL_API_CALL *clGetGLContextInfoKHRPROC) (const cl_context_properties * properties, cl_gl_context_info param_name, size_t param_value_size, cl_void * param_value, size_t * param_value_size_ret);

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_KHRGLSharing_nclGetGLContextInfoKHR(JNIEnv *env, jclass clazz, jlong properties, jint param_name, jlong param_value_size, jlong param_value, jlong param_value_size_ret, jlong function_pointer) {
	const cl_context_properties *properties_address = (const cl_context_properties *)(intptr_t)properties;
	cl_void *param_value_address = (cl_void *)(intptr_t)param_value;
	size_t *param_value_size_ret_address = (size_t *)(intptr_t)param_value_size_ret;
	clGetGLContextInfoKHRPROC clGetGLContextInfoKHR = (clGetGLContextInfoKHRPROC)((intptr_t)function_pointer);
	cl_int __result = clGetGLContextInfoKHR(properties_address, param_name, param_value_size, param_value_address, param_value_size_ret_address);
	return __result;
}

