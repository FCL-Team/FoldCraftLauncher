/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glPointParameterfARBPROC) (GLenum pname, GLfloat param);
typedef void (APIENTRY *glPointParameterfvARBPROC) (GLenum pname, const GLfloat * pfParams);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBPointParameters_nglPointParameterfARB(JNIEnv *env, jclass clazz, jint pname, jfloat param, jlong function_pointer) {
	glPointParameterfARBPROC glPointParameterfARB = (glPointParameterfARBPROC)((intptr_t)function_pointer);
	glPointParameterfARB(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBPointParameters_nglPointParameterfvARB(JNIEnv *env, jclass clazz, jint pname, jlong pfParams, jlong function_pointer) {
	const GLfloat *pfParams_address = (const GLfloat *)(intptr_t)pfParams;
	glPointParameterfvARBPROC glPointParameterfvARB = (glPointParameterfvARBPROC)((intptr_t)function_pointer);
	glPointParameterfvARB(pname, pfParams_address);
}

