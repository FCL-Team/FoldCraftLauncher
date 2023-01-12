/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBindBufferARBPROC) (GLenum target, GLuint buffer);
typedef void (APIENTRY *glDeleteBuffersARBPROC) (GLsizei n, const GLuint * buffers);
typedef void (APIENTRY *glGenBuffersARBPROC) (GLsizei n, GLuint * buffers);
typedef GLboolean (APIENTRY *glIsBufferARBPROC) (GLuint buffer);
typedef void (APIENTRY *glBufferDataARBPROC) (GLenum target, GLsizeiptrARB size, const GLvoid * data, GLenum usage);
typedef void (APIENTRY *glBufferSubDataARBPROC) (GLenum target, GLintptrARB offset, GLsizeiptrARB size, const GLvoid * data);
typedef void (APIENTRY *glGetBufferSubDataARBPROC) (GLenum target, GLintptrARB offset, GLsizeiptrARB size, GLvoid * data);
typedef GLvoid * (APIENTRY *glMapBufferARBPROC) (GLenum target, GLenum access);
typedef GLboolean (APIENTRY *glUnmapBufferARBPROC) (GLenum target);
typedef void (APIENTRY *glGetBufferParameterivARBPROC) (GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetBufferPointervARBPROC) (GLenum target, GLenum pname, GLvoid ** pointer);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBufferObject_nglBindBufferARB(JNIEnv *env, jclass clazz, jint target, jint buffer, jlong function_pointer) {
	glBindBufferARBPROC glBindBufferARB = (glBindBufferARBPROC)((intptr_t)function_pointer);
	glBindBufferARB(target, buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBufferObject_nglDeleteBuffersARB(JNIEnv *env, jclass clazz, jint n, jlong buffers, jlong function_pointer) {
	const GLuint *buffers_address = (const GLuint *)(intptr_t)buffers;
	glDeleteBuffersARBPROC glDeleteBuffersARB = (glDeleteBuffersARBPROC)((intptr_t)function_pointer);
	glDeleteBuffersARB(n, buffers_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBufferObject_nglGenBuffersARB(JNIEnv *env, jclass clazz, jint n, jlong buffers, jlong function_pointer) {
	GLuint *buffers_address = (GLuint *)(intptr_t)buffers;
	glGenBuffersARBPROC glGenBuffersARB = (glGenBuffersARBPROC)((intptr_t)function_pointer);
	glGenBuffersARB(n, buffers_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_ARBBufferObject_nglIsBufferARB(JNIEnv *env, jclass clazz, jint buffer, jlong function_pointer) {
	glIsBufferARBPROC glIsBufferARB = (glIsBufferARBPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsBufferARB(buffer);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBufferObject_nglBufferDataARB(JNIEnv *env, jclass clazz, jint target, jlong size, jlong data, jint usage, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glBufferDataARBPROC glBufferDataARB = (glBufferDataARBPROC)((intptr_t)function_pointer);
	glBufferDataARB(target, size, data_address, usage);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBufferObject_nglBufferSubDataARB(JNIEnv *env, jclass clazz, jint target, jlong offset, jlong size, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glBufferSubDataARBPROC glBufferSubDataARB = (glBufferSubDataARBPROC)((intptr_t)function_pointer);
	glBufferSubDataARB(target, offset, size, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBufferObject_nglGetBufferSubDataARB(JNIEnv *env, jclass clazz, jint target, jlong offset, jlong size, jlong data, jlong function_pointer) {
	GLvoid *data_address = (GLvoid *)(intptr_t)data;
	glGetBufferSubDataARBPROC glGetBufferSubDataARB = (glGetBufferSubDataARBPROC)((intptr_t)function_pointer);
	glGetBufferSubDataARB(target, offset, size, data_address);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_ARBBufferObject_nglMapBufferARB(JNIEnv *env, jclass clazz, jint target, jint access, jlong result_size, jobject old_buffer, jlong function_pointer) {
	glMapBufferARBPROC glMapBufferARB = (glMapBufferARBPROC)((intptr_t)function_pointer);
	GLvoid * __result = glMapBufferARB(target, access);
	return safeNewBufferCached(env, __result, result_size, old_buffer);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_ARBBufferObject_nglUnmapBufferARB(JNIEnv *env, jclass clazz, jint target, jlong function_pointer) {
	glUnmapBufferARBPROC glUnmapBufferARB = (glUnmapBufferARBPROC)((intptr_t)function_pointer);
	GLboolean __result = glUnmapBufferARB(target);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBufferObject_nglGetBufferParameterivARB(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetBufferParameterivARBPROC glGetBufferParameterivARB = (glGetBufferParameterivARBPROC)((intptr_t)function_pointer);
	glGetBufferParameterivARB(target, pname, params_address);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_ARBBufferObject_nglGetBufferPointervARB(JNIEnv *env, jclass clazz, jint target, jint pname, jlong result_size, jlong function_pointer) {
	glGetBufferPointervARBPROC glGetBufferPointervARB = (glGetBufferPointervARBPROC)((intptr_t)function_pointer);
	GLvoid * __result;
	glGetBufferPointervARB(target, pname, &__result);
	return safeNewBuffer(env, __result, result_size);
}

