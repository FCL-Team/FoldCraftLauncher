/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glGenQueriesARBPROC) (GLsizei n, GLuint * ids);
typedef void (APIENTRY *glDeleteQueriesARBPROC) (GLsizei n, GLuint * ids);
typedef GLboolean (APIENTRY *glIsQueryARBPROC) (GLuint id);
typedef void (APIENTRY *glBeginQueryARBPROC) (GLenum target, GLuint id);
typedef void (APIENTRY *glEndQueryARBPROC) (GLenum target);
typedef void (APIENTRY *glGetQueryivARBPROC) (GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetQueryObjectivARBPROC) (GLuint id, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetQueryObjectuivARBPROC) (GLuint id, GLenum pname, GLint * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_nglGenQueriesARB(JNIEnv *env, jclass clazz, jint n, jlong ids, jlong function_pointer) {
	GLuint *ids_address = (GLuint *)(intptr_t)ids;
	glGenQueriesARBPROC glGenQueriesARB = (glGenQueriesARBPROC)((intptr_t)function_pointer);
	glGenQueriesARB(n, ids_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_nglDeleteQueriesARB(JNIEnv *env, jclass clazz, jint n, jlong ids, jlong function_pointer) {
	GLuint *ids_address = (GLuint *)(intptr_t)ids;
	glDeleteQueriesARBPROC glDeleteQueriesARB = (glDeleteQueriesARBPROC)((intptr_t)function_pointer);
	glDeleteQueriesARB(n, ids_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_nglIsQueryARB(JNIEnv *env, jclass clazz, jint id, jlong function_pointer) {
	glIsQueryARBPROC glIsQueryARB = (glIsQueryARBPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsQueryARB(id);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_nglBeginQueryARB(JNIEnv *env, jclass clazz, jint target, jint id, jlong function_pointer) {
	glBeginQueryARBPROC glBeginQueryARB = (glBeginQueryARBPROC)((intptr_t)function_pointer);
	glBeginQueryARB(target, id);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_nglEndQueryARB(JNIEnv *env, jclass clazz, jint target, jlong function_pointer) {
	glEndQueryARBPROC glEndQueryARB = (glEndQueryARBPROC)((intptr_t)function_pointer);
	glEndQueryARB(target);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_nglGetQueryivARB(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetQueryivARBPROC glGetQueryivARB = (glGetQueryivARBPROC)((intptr_t)function_pointer);
	glGetQueryivARB(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_nglGetQueryObjectivARB(JNIEnv *env, jclass clazz, jint id, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetQueryObjectivARBPROC glGetQueryObjectivARB = (glGetQueryObjectivARBPROC)((intptr_t)function_pointer);
	glGetQueryObjectivARB(id, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_nglGetQueryObjectuivARB(JNIEnv *env, jclass clazz, jint id, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetQueryObjectuivARBPROC glGetQueryObjectuivARB = (glGetQueryObjectuivARBPROC)((intptr_t)function_pointer);
	glGetQueryObjectuivARB(id, pname, params_address);
}

