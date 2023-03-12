/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GLsync (APIENTRY *glCreateSyncFromCLeventARBPROC) (cl_context context, cl_event event, GLbitfield flags);

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_ARBCLEvent_nglCreateSyncFromCLeventARB(JNIEnv *env, jclass clazz, jlong context, jlong event, jint flags, jlong function_pointer) {
	glCreateSyncFromCLeventARBPROC glCreateSyncFromCLeventARB = (glCreateSyncFromCLeventARBPROC)((intptr_t)function_pointer);
	GLsync __result = glCreateSyncFromCLeventARB((cl_context)(intptr_t)context, (cl_event)(intptr_t)event, flags);
	return (intptr_t)__result;
}

