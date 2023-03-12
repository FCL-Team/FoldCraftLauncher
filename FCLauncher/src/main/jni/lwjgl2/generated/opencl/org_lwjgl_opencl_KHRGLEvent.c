/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extcl.h"

typedef CL_API_ENTRY cl_event (CL_API_CALL *clCreateEventFromGLsyncKHRPROC) (cl_context context, cl_GLsync sync, cl_int * errcode_ret);

JNIEXPORT jlong JNICALL Java_org_lwjgl_opencl_KHRGLEvent_nclCreateEventFromGLsyncKHR(JNIEnv *env, jclass clazz, jlong context, jlong sync, jlong errcode_ret, jlong function_pointer) {
	cl_int *errcode_ret_address = (cl_int *)(intptr_t)errcode_ret;
	clCreateEventFromGLsyncKHRPROC clCreateEventFromGLsyncKHR = (clCreateEventFromGLsyncKHRPROC)((intptr_t)function_pointer);
	cl_event __result = clCreateEventFromGLsyncKHR((cl_context)(intptr_t)context, (cl_GLsync)(intptr_t)sync, errcode_ret_address);
	return (intptr_t)__result;
}

