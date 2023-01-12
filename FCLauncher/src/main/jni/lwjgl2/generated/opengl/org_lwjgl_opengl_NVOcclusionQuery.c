/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glGenOcclusionQueriesNVPROC) (GLsizei n, GLuint * piIDs);
typedef void (APIENTRY *glDeleteOcclusionQueriesNVPROC) (GLsizei n, const GLuint * piIDs);
typedef GLboolean (APIENTRY *glIsOcclusionQueryNVPROC) (GLuint id);
typedef void (APIENTRY *glBeginOcclusionQueryNVPROC) (GLuint id);
typedef void (APIENTRY *glEndOcclusionQueryNVPROC) ();
typedef void (APIENTRY *glGetOcclusionQueryuivNVPROC) (GLuint id, GLenum pname, GLuint * params);
typedef void (APIENTRY *glGetOcclusionQueryivNVPROC) (GLuint id, GLenum pname, GLint * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVOcclusionQuery_nglGenOcclusionQueriesNV(JNIEnv *env, jclass clazz, jint n, jlong piIDs, jlong function_pointer) {
	GLuint *piIDs_address = (GLuint *)(intptr_t)piIDs;
	glGenOcclusionQueriesNVPROC glGenOcclusionQueriesNV = (glGenOcclusionQueriesNVPROC)((intptr_t)function_pointer);
	glGenOcclusionQueriesNV(n, piIDs_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVOcclusionQuery_nglDeleteOcclusionQueriesNV(JNIEnv *env, jclass clazz, jint n, jlong piIDs, jlong function_pointer) {
	const GLuint *piIDs_address = (const GLuint *)(intptr_t)piIDs;
	glDeleteOcclusionQueriesNVPROC glDeleteOcclusionQueriesNV = (glDeleteOcclusionQueriesNVPROC)((intptr_t)function_pointer);
	glDeleteOcclusionQueriesNV(n, piIDs_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_NVOcclusionQuery_nglIsOcclusionQueryNV(JNIEnv *env, jclass clazz, jint id, jlong function_pointer) {
	glIsOcclusionQueryNVPROC glIsOcclusionQueryNV = (glIsOcclusionQueryNVPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsOcclusionQueryNV(id);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVOcclusionQuery_nglBeginOcclusionQueryNV(JNIEnv *env, jclass clazz, jint id, jlong function_pointer) {
	glBeginOcclusionQueryNVPROC glBeginOcclusionQueryNV = (glBeginOcclusionQueryNVPROC)((intptr_t)function_pointer);
	glBeginOcclusionQueryNV(id);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVOcclusionQuery_nglEndOcclusionQueryNV(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glEndOcclusionQueryNVPROC glEndOcclusionQueryNV = (glEndOcclusionQueryNVPROC)((intptr_t)function_pointer);
	glEndOcclusionQueryNV();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVOcclusionQuery_nglGetOcclusionQueryuivNV(JNIEnv *env, jclass clazz, jint id, jint pname, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetOcclusionQueryuivNVPROC glGetOcclusionQueryuivNV = (glGetOcclusionQueryuivNVPROC)((intptr_t)function_pointer);
	glGetOcclusionQueryuivNV(id, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVOcclusionQuery_nglGetOcclusionQueryivNV(JNIEnv *env, jclass clazz, jint id, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetOcclusionQueryivNVPROC glGetOcclusionQueryivNV = (glGetOcclusionQueryivNVPROC)((intptr_t)function_pointer);
	glGetOcclusionQueryivNV(id, pname, params_address);
}

