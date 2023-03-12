/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glGenFencesNVPROC) (GLsizei n, GLuint * piFences);
typedef void (APIENTRY *glDeleteFencesNVPROC) (GLsizei n, const GLuint * piFences);
typedef void (APIENTRY *glSetFenceNVPROC) (GLuint fence, GLenum condition);
typedef GLboolean (APIENTRY *glTestFenceNVPROC) (GLuint fence);
typedef void (APIENTRY *glFinishFenceNVPROC) (GLuint fence);
typedef GLboolean (APIENTRY *glIsFenceNVPROC) (GLuint fence);
typedef void (APIENTRY *glGetFenceivNVPROC) (GLuint fence, GLenum pname, GLint * piParams);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVFence_nglGenFencesNV(JNIEnv *env, jclass clazz, jint n, jlong piFences, jlong function_pointer) {
	GLuint *piFences_address = (GLuint *)(intptr_t)piFences;
	glGenFencesNVPROC glGenFencesNV = (glGenFencesNVPROC)((intptr_t)function_pointer);
	glGenFencesNV(n, piFences_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVFence_nglDeleteFencesNV(JNIEnv *env, jclass clazz, jint n, jlong piFences, jlong function_pointer) {
	const GLuint *piFences_address = (const GLuint *)(intptr_t)piFences;
	glDeleteFencesNVPROC glDeleteFencesNV = (glDeleteFencesNVPROC)((intptr_t)function_pointer);
	glDeleteFencesNV(n, piFences_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVFence_nglSetFenceNV(JNIEnv *env, jclass clazz, jint fence, jint condition, jlong function_pointer) {
	glSetFenceNVPROC glSetFenceNV = (glSetFenceNVPROC)((intptr_t)function_pointer);
	glSetFenceNV(fence, condition);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_NVFence_nglTestFenceNV(JNIEnv *env, jclass clazz, jint fence, jlong function_pointer) {
	glTestFenceNVPROC glTestFenceNV = (glTestFenceNVPROC)((intptr_t)function_pointer);
	GLboolean __result = glTestFenceNV(fence);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVFence_nglFinishFenceNV(JNIEnv *env, jclass clazz, jint fence, jlong function_pointer) {
	glFinishFenceNVPROC glFinishFenceNV = (glFinishFenceNVPROC)((intptr_t)function_pointer);
	glFinishFenceNV(fence);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_NVFence_nglIsFenceNV(JNIEnv *env, jclass clazz, jint fence, jlong function_pointer) {
	glIsFenceNVPROC glIsFenceNV = (glIsFenceNVPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsFenceNV(fence);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVFence_nglGetFenceivNV(JNIEnv *env, jclass clazz, jint fence, jint pname, jlong piParams, jlong function_pointer) {
	GLint *piParams_address = (GLint *)(intptr_t)piParams;
	glGetFenceivNVPROC glGetFenceivNV = (glGetFenceivNVPROC)((intptr_t)function_pointer);
	glGetFenceivNV(fence, pname, piParams_address);
}

