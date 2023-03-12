/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glDepthRangedNVPROC) (GLdouble n, GLdouble f);
typedef void (APIENTRY *glClearDepthdNVPROC) (GLdouble d);
typedef void (APIENTRY *glDepthBoundsdNVPROC) (GLdouble zmin, GLdouble zmax);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVDepthBufferFloat_nglDepthRangedNV(JNIEnv *env, jclass clazz, jdouble n, jdouble f, jlong function_pointer) {
	glDepthRangedNVPROC glDepthRangedNV = (glDepthRangedNVPROC)((intptr_t)function_pointer);
	glDepthRangedNV(n, f);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVDepthBufferFloat_nglClearDepthdNV(JNIEnv *env, jclass clazz, jdouble d, jlong function_pointer) {
	glClearDepthdNVPROC glClearDepthdNV = (glClearDepthdNVPROC)((intptr_t)function_pointer);
	glClearDepthdNV(d);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVDepthBufferFloat_nglDepthBoundsdNV(JNIEnv *env, jclass clazz, jdouble zmin, jdouble zmax, jlong function_pointer) {
	glDepthBoundsdNVPROC glDepthBoundsdNV = (glDepthBoundsdNVPROC)((intptr_t)function_pointer);
	glDepthBoundsdNV(zmin, zmax);
}

