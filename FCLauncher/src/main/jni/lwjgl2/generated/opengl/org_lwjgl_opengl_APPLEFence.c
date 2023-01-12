/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glGenFencesAPPLEPROC) (GLsizei n, GLuint * fences);
typedef void (APIENTRY *glDeleteFencesAPPLEPROC) (GLsizei n, const GLuint * fences);
typedef void (APIENTRY *glSetFenceAPPLEPROC) (GLuint fence);
typedef GLboolean (APIENTRY *glIsFenceAPPLEPROC) (GLuint fence);
typedef GLboolean (APIENTRY *glTestFenceAPPLEPROC) (GLuint fence);
typedef void (APIENTRY *glFinishFenceAPPLEPROC) (GLuint fence);
typedef GLboolean (APIENTRY *glTestObjectAPPLEPROC) (GLenum object, GLuint name);
typedef void (APIENTRY *glFinishObjectAPPLEPROC) (GLenum object, GLint name);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEFence_nglGenFencesAPPLE(JNIEnv *env, jclass clazz, jint n, jlong fences, jlong function_pointer) {
	GLuint *fences_address = (GLuint *)(intptr_t)fences;
	glGenFencesAPPLEPROC glGenFencesAPPLE = (glGenFencesAPPLEPROC)((intptr_t)function_pointer);
	glGenFencesAPPLE(n, fences_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEFence_nglDeleteFencesAPPLE(JNIEnv *env, jclass clazz, jint n, jlong fences, jlong function_pointer) {
	const GLuint *fences_address = (const GLuint *)(intptr_t)fences;
	glDeleteFencesAPPLEPROC glDeleteFencesAPPLE = (glDeleteFencesAPPLEPROC)((intptr_t)function_pointer);
	glDeleteFencesAPPLE(n, fences_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEFence_nglSetFenceAPPLE(JNIEnv *env, jclass clazz, jint fence, jlong function_pointer) {
	glSetFenceAPPLEPROC glSetFenceAPPLE = (glSetFenceAPPLEPROC)((intptr_t)function_pointer);
	glSetFenceAPPLE(fence);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_APPLEFence_nglIsFenceAPPLE(JNIEnv *env, jclass clazz, jint fence, jlong function_pointer) {
	glIsFenceAPPLEPROC glIsFenceAPPLE = (glIsFenceAPPLEPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsFenceAPPLE(fence);
	return __result;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_APPLEFence_nglTestFenceAPPLE(JNIEnv *env, jclass clazz, jint fence, jlong function_pointer) {
	glTestFenceAPPLEPROC glTestFenceAPPLE = (glTestFenceAPPLEPROC)((intptr_t)function_pointer);
	GLboolean __result = glTestFenceAPPLE(fence);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEFence_nglFinishFenceAPPLE(JNIEnv *env, jclass clazz, jint fence, jlong function_pointer) {
	glFinishFenceAPPLEPROC glFinishFenceAPPLE = (glFinishFenceAPPLEPROC)((intptr_t)function_pointer);
	glFinishFenceAPPLE(fence);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_APPLEFence_nglTestObjectAPPLE(JNIEnv *env, jclass clazz, jint object, jint name, jlong function_pointer) {
	glTestObjectAPPLEPROC glTestObjectAPPLE = (glTestObjectAPPLEPROC)((intptr_t)function_pointer);
	GLboolean __result = glTestObjectAPPLE(object, name);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEFence_nglFinishObjectAPPLE(JNIEnv *env, jclass clazz, jint object, jint name, jlong function_pointer) {
	glFinishObjectAPPLEPROC glFinishObjectAPPLE = (glFinishObjectAPPLEPROC)((intptr_t)function_pointer);
	glFinishObjectAPPLE(object, name);
}

