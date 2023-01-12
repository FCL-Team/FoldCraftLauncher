/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extcl.h"

typedef CL_API_ENTRY cl_int (CL_API_CALL *clSetMemObjectDestructorAPPLEPROC) (cl_mem memobj, cl_mem_object_destructor_callback pfn_notify, void * user_data);

JNIEXPORT jint JNICALL Java_org_lwjgl_opencl_APPLESetMemObjectDestructor_nclSetMemObjectDestructorAPPLE(JNIEnv *env, jclass clazz, jlong memobj, jlong pfn_notify, jlong user_data, jlong function_pointer) {
	clSetMemObjectDestructorAPPLEPROC clSetMemObjectDestructorAPPLE = (clSetMemObjectDestructorAPPLEPROC)((intptr_t)function_pointer);
	cl_int __result = clSetMemObjectDestructorAPPLE((cl_mem)(intptr_t)memobj, (cl_mem_object_destructor_callback)(intptr_t)pfn_notify, (void *)(intptr_t)user_data);
	return __result;
}

