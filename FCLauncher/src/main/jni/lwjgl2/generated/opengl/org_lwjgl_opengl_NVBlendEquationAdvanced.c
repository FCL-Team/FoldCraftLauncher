/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBlendParameteriNVPROC) (GLenum pname, GLint value);
typedef void (APIENTRY *glBlendBarrierNVPROC) ();

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVBlendEquationAdvanced_nglBlendParameteriNV(JNIEnv *env, jclass clazz, jint pname, jint value, jlong function_pointer) {
	glBlendParameteriNVPROC glBlendParameteriNV = (glBlendParameteriNVPROC)((intptr_t)function_pointer);
	glBlendParameteriNV(pname, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVBlendEquationAdvanced_nglBlendBarrierNV(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glBlendBarrierNVPROC glBlendBarrierNV = (glBlendBarrierNVPROC)((intptr_t)function_pointer);
	glBlendBarrierNV();
}

