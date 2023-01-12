/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBindVertexArrayAPPLEPROC) (GLuint array);
typedef void (APIENTRY *glDeleteVertexArraysAPPLEPROC) (GLsizei n, const GLuint * arrays);
typedef void (APIENTRY *glGenVertexArraysAPPLEPROC) (GLsizei n, GLuint * arrays);
typedef GLboolean (APIENTRY *glIsVertexArrayAPPLEPROC) (GLuint array);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEVertexArrayObject_nglBindVertexArrayAPPLE(JNIEnv *env, jclass clazz, jint array, jlong function_pointer) {
	glBindVertexArrayAPPLEPROC glBindVertexArrayAPPLE = (glBindVertexArrayAPPLEPROC)((intptr_t)function_pointer);
	glBindVertexArrayAPPLE(array);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEVertexArrayObject_nglDeleteVertexArraysAPPLE(JNIEnv *env, jclass clazz, jint n, jlong arrays, jlong function_pointer) {
	const GLuint *arrays_address = (const GLuint *)(intptr_t)arrays;
	glDeleteVertexArraysAPPLEPROC glDeleteVertexArraysAPPLE = (glDeleteVertexArraysAPPLEPROC)((intptr_t)function_pointer);
	glDeleteVertexArraysAPPLE(n, arrays_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEVertexArrayObject_nglGenVertexArraysAPPLE(JNIEnv *env, jclass clazz, jint n, jlong arrays, jlong function_pointer) {
	GLuint *arrays_address = (GLuint *)(intptr_t)arrays;
	glGenVertexArraysAPPLEPROC glGenVertexArraysAPPLE = (glGenVertexArraysAPPLEPROC)((intptr_t)function_pointer);
	glGenVertexArraysAPPLE(n, arrays_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_APPLEVertexArrayObject_nglIsVertexArrayAPPLE(JNIEnv *env, jclass clazz, jint array, jlong function_pointer) {
	glIsVertexArrayAPPLEPROC glIsVertexArrayAPPLE = (glIsVertexArrayAPPLEPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsVertexArrayAPPLE(array);
	return __result;
}

