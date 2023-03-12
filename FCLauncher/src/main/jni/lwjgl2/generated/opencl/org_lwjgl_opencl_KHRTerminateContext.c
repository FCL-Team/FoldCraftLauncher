/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extcl.h"

typedef CL_API_ENTRY cl_int (CL_API_CALL *clTerminateContextKHRPROC) (cl_context context);

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_KHRTerminateContext_nclTerminateContextKHR(JNIEnv *env, jclass clazz, jlong context, jlong function_pointer) {
	clTerminateContextKHRPROC clTerminateContextKHR = (clTerminateContextKHRPROC)((intptr_t)function_pointer);
	cl_int __result = clTerminateContextKHR((cl_context)(intptr_t)context);
	return __result;
}

