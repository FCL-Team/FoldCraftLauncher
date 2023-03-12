/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extcl.h"

typedef CL_API_ENTRY cl_int (CL_API_CALL *clIcdGetPlatformIDsKHRPROC) (cl_uint num_entries, cl_platform_id * platforms, cl_uint * num_platforms);

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_KHRICD_nclIcdGetPlatformIDsKHR(JNIEnv *env, jclass clazz, jint num_entries, jlong platforms, jlong num_platforms, jlong function_pointer) {
	cl_platform_id *platforms_address = (cl_platform_id *)(intptr_t)platforms;
	cl_uint *num_platforms_address = (cl_uint *)(intptr_t)num_platforms;
	clIcdGetPlatformIDsKHRPROC clIcdGetPlatformIDsKHR = (clIcdGetPlatformIDsKHRPROC)((intptr_t)function_pointer);
	cl_int __result = clIcdGetPlatformIDsKHR(num_entries, platforms_address, num_platforms_address);
	return __result;
}

