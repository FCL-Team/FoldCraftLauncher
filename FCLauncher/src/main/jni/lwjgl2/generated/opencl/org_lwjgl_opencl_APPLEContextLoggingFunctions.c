/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extcl.h"

typedef CL_API_ENTRY void (CL_API_CALL *clLogMessagesToSystemLogAPPLEPROC) (const cl_char * errstr, const cl_void * private_info, size_t cb, cl_void * user_data);
typedef CL_API_ENTRY void (CL_API_CALL *clLogMessagesToStdoutAPPLEPROC) (const cl_char * errstr, const cl_void * private_info, size_t cb, cl_void * user_data);
typedef CL_API_ENTRY void (CL_API_CALL *clLogMessagesToStderrAPPLEPROC) (const cl_char * errstr, const cl_void * private_info, size_t cb, cl_void * user_data);

JNIEXPORT void JNICALL Java_org_lwjgl_opencl_APPLEContextLoggingFunctions_nclLogMessagesToSystemLogAPPLE(JNIEnv *env, jclass clazz, jlong errstr, jlong private_info, jlong cb, jlong user_data, jlong function_pointer) {
	const cl_char *errstr_address = (const cl_char *)(intptr_t)errstr;
	const cl_void *private_info_address = (const cl_void *)(intptr_t)private_info;
	cl_void *user_data_address = (cl_void *)(intptr_t)user_data;
	clLogMessagesToSystemLogAPPLEPROC clLogMessagesToSystemLogAPPLE = (clLogMessagesToSystemLogAPPLEPROC)((intptr_t)function_pointer);
	clLogMessagesToSystemLogAPPLE(errstr_address, private_info_address, cb, user_data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opencl_APPLEContextLoggingFunctions_nclLogMessagesToStdoutAPPLE(JNIEnv *env, jclass clazz, jlong errstr, jlong private_info, jlong cb, jlong user_data, jlong function_pointer) {
	const cl_char *errstr_address = (const cl_char *)(intptr_t)errstr;
	const cl_void *private_info_address = (const cl_void *)(intptr_t)private_info;
	cl_void *user_data_address = (cl_void *)(intptr_t)user_data;
	clLogMessagesToStdoutAPPLEPROC clLogMessagesToStdoutAPPLE = (clLogMessagesToStdoutAPPLEPROC)((intptr_t)function_pointer);
	clLogMessagesToStdoutAPPLE(errstr_address, private_info_address, cb, user_data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opencl_APPLEContextLoggingFunctions_nclLogMessagesToStderrAPPLE(JNIEnv *env, jclass clazz, jlong errstr, jlong private_info, jlong cb, jlong user_data, jlong function_pointer) {
	const cl_char *errstr_address = (const cl_char *)(intptr_t)errstr;
	const cl_void *private_info_address = (const cl_void *)(intptr_t)private_info;
	cl_void *user_data_address = (cl_void *)(intptr_t)user_data;
	clLogMessagesToStderrAPPLEPROC clLogMessagesToStderrAPPLE = (clLogMessagesToStderrAPPLEPROC)((intptr_t)function_pointer);
	clLogMessagesToStderrAPPLE(errstr_address, private_info_address, cb, user_data_address);
}

