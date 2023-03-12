/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glPointParameteriNVPROC) (GLenum pname, GLint param);
typedef void (APIENTRY *glPointParameterivNVPROC) (GLenum pname, const GLint * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPointSprite_nglPointParameteriNV(JNIEnv *env, jclass clazz, jint pname, jint param, jlong function_pointer) {
	glPointParameteriNVPROC glPointParameteriNV = (glPointParameteriNVPROC)((intptr_t)function_pointer);
	glPointParameteriNV(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPointSprite_nglPointParameterivNV(JNIEnv *env, jclass clazz, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glPointParameterivNVPROC glPointParameterivNV = (glPointParameterivNVPROC)((intptr_t)function_pointer);
	glPointParameterivNV(pname, params_address);
}

