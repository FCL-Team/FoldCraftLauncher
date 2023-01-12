/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glPointParameterfEXTPROC) (GLenum pname, GLfloat param);
typedef void (APIENTRY *glPointParameterfvEXTPROC) (GLenum pname, const GLfloat * pfParams);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTPointParameters_nglPointParameterfEXT(JNIEnv *env, jclass clazz, jint pname, jfloat param, jlong function_pointer) {
	glPointParameterfEXTPROC glPointParameterfEXT = (glPointParameterfEXTPROC)((intptr_t)function_pointer);
	glPointParameterfEXT(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTPointParameters_nglPointParameterfvEXT(JNIEnv *env, jclass clazz, jint pname, jlong pfParams, jlong function_pointer) {
	const GLfloat *pfParams_address = (const GLfloat *)(intptr_t)pfParams;
	glPointParameterfvEXTPROC glPointParameterfvEXT = (glPointParameterfvEXTPROC)((intptr_t)function_pointer);
	glPointParameterfvEXT(pname, pfParams_address);
}

