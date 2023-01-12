/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glVertexArrayRangeAPPLEPROC) (GLsizei length, GLvoid * pointer);
typedef void (APIENTRY *glFlushVertexArrayRangeAPPLEPROC) (GLsizei length, GLvoid * pointer);
typedef void (APIENTRY *glVertexArrayParameteriAPPLEPROC) (GLenum pname, GLint param);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEVertexArrayRange_nglVertexArrayRangeAPPLE(JNIEnv *env, jclass clazz, jint length, jlong pointer, jlong function_pointer) {
	GLvoid *pointer_address = (GLvoid *)(intptr_t)pointer;
	glVertexArrayRangeAPPLEPROC glVertexArrayRangeAPPLE = (glVertexArrayRangeAPPLEPROC)((intptr_t)function_pointer);
	glVertexArrayRangeAPPLE(length, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEVertexArrayRange_nglFlushVertexArrayRangeAPPLE(JNIEnv *env, jclass clazz, jint length, jlong pointer, jlong function_pointer) {
	GLvoid *pointer_address = (GLvoid *)(intptr_t)pointer;
	glFlushVertexArrayRangeAPPLEPROC glFlushVertexArrayRangeAPPLE = (glFlushVertexArrayRangeAPPLEPROC)((intptr_t)function_pointer);
	glFlushVertexArrayRangeAPPLE(length, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEVertexArrayRange_nglVertexArrayParameteriAPPLE(JNIEnv *env, jclass clazz, jint pname, jint param, jlong function_pointer) {
	glVertexArrayParameteriAPPLEPROC glVertexArrayParameteriAPPLE = (glVertexArrayParameteriAPPLEPROC)((intptr_t)function_pointer);
	glVertexArrayParameteriAPPLE(pname, param);
}

