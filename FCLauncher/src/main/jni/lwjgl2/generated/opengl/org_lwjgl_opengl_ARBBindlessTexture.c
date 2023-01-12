/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GLuint64 (APIENTRY *glGetTextureHandleARBPROC) (GLuint texture);
typedef GLuint64 (APIENTRY *glGetTextureSamplerHandleARBPROC) (GLuint texture, GLuint sampler);
typedef void (APIENTRY *glMakeTextureHandleResidentARBPROC) (GLuint64 handle);
typedef void (APIENTRY *glMakeTextureHandleNonResidentARBPROC) (GLuint64 handle);
typedef GLuint64 (APIENTRY *glGetImageHandleARBPROC) (GLuint texture, GLint level, GLboolean layered, GLint layer, GLenum format);
typedef void (APIENTRY *glMakeImageHandleResidentARBPROC) (GLuint64 handle, GLenum access);
typedef void (APIENTRY *glMakeImageHandleNonResidentARBPROC) (GLuint64 handle);
typedef void (APIENTRY *glUniformHandleui64ARBPROC) (GLint location, GLuint64 value);
typedef void (APIENTRY *glUniformHandleui64vARBPROC) (GLint location, GLsizei count, const GLuint64 * value);
typedef void (APIENTRY *glProgramUniformHandleui64ARBPROC) (GLuint program, GLint location, GLuint64 value);
typedef void (APIENTRY *glProgramUniformHandleui64vARBPROC) (GLuint program, GLint location, GLsizei count, const GLuint64 * values);
typedef GLboolean (APIENTRY *glIsTextureHandleResidentARBPROC) (GLuint64 handle);
typedef GLboolean (APIENTRY *glIsImageHandleResidentARBPROC) (GLuint64 handle);
typedef void (APIENTRY *glVertexAttribL1ui64ARBPROC) (GLuint index, GLuint64EXT x);
typedef void (APIENTRY *glVertexAttribL1ui64vARBPROC) (GLuint index, const GLuint64EXT * v);
typedef void (APIENTRY *glGetVertexAttribLui64vARBPROC) (GLuint index, GLenum pname, GLuint64EXT * params);

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglGetTextureHandleARB(JNIEnv *env, jclass clazz, jint texture, jlong function_pointer) {
	glGetTextureHandleARBPROC glGetTextureHandleARB = (glGetTextureHandleARBPROC)((intptr_t)function_pointer);
	GLuint64 __result = glGetTextureHandleARB(texture);
	return __result;
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglGetTextureSamplerHandleARB(JNIEnv *env, jclass clazz, jint texture, jint sampler, jlong function_pointer) {
	glGetTextureSamplerHandleARBPROC glGetTextureSamplerHandleARB = (glGetTextureSamplerHandleARBPROC)((intptr_t)function_pointer);
	GLuint64 __result = glGetTextureSamplerHandleARB(texture, sampler);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglMakeTextureHandleResidentARB(JNIEnv *env, jclass clazz, jlong handle, jlong function_pointer) {
	glMakeTextureHandleResidentARBPROC glMakeTextureHandleResidentARB = (glMakeTextureHandleResidentARBPROC)((intptr_t)function_pointer);
	glMakeTextureHandleResidentARB(handle);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglMakeTextureHandleNonResidentARB(JNIEnv *env, jclass clazz, jlong handle, jlong function_pointer) {
	glMakeTextureHandleNonResidentARBPROC glMakeTextureHandleNonResidentARB = (glMakeTextureHandleNonResidentARBPROC)((intptr_t)function_pointer);
	glMakeTextureHandleNonResidentARB(handle);
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglGetImageHandleARB(JNIEnv *env, jclass clazz, jint texture, jint level, jboolean layered, jint layer, jint format, jlong function_pointer) {
	glGetImageHandleARBPROC glGetImageHandleARB = (glGetImageHandleARBPROC)((intptr_t)function_pointer);
	GLuint64 __result = glGetImageHandleARB(texture, level, layered, layer, format);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglMakeImageHandleResidentARB(JNIEnv *env, jclass clazz, jlong handle, jint access, jlong function_pointer) {
	glMakeImageHandleResidentARBPROC glMakeImageHandleResidentARB = (glMakeImageHandleResidentARBPROC)((intptr_t)function_pointer);
	glMakeImageHandleResidentARB(handle, access);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglMakeImageHandleNonResidentARB(JNIEnv *env, jclass clazz, jlong handle, jlong function_pointer) {
	glMakeImageHandleNonResidentARBPROC glMakeImageHandleNonResidentARB = (glMakeImageHandleNonResidentARBPROC)((intptr_t)function_pointer);
	glMakeImageHandleNonResidentARB(handle);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglUniformHandleui64ARB(JNIEnv *env, jclass clazz, jint location, jlong value, jlong function_pointer) {
	glUniformHandleui64ARBPROC glUniformHandleui64ARB = (glUniformHandleui64ARBPROC)((intptr_t)function_pointer);
	glUniformHandleui64ARB(location, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglUniformHandleui64vARB(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint64 *value_address = (const GLuint64 *)(intptr_t)value;
	glUniformHandleui64vARBPROC glUniformHandleui64vARB = (glUniformHandleui64vARBPROC)((intptr_t)function_pointer);
	glUniformHandleui64vARB(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglProgramUniformHandleui64ARB(JNIEnv *env, jclass clazz, jint program, jint location, jlong value, jlong function_pointer) {
	glProgramUniformHandleui64ARBPROC glProgramUniformHandleui64ARB = (glProgramUniformHandleui64ARBPROC)((intptr_t)function_pointer);
	glProgramUniformHandleui64ARB(program, location, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglProgramUniformHandleui64vARB(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong values, jlong function_pointer) {
	const GLuint64 *values_address = (const GLuint64 *)(intptr_t)values;
	glProgramUniformHandleui64vARBPROC glProgramUniformHandleui64vARB = (glProgramUniformHandleui64vARBPROC)((intptr_t)function_pointer);
	glProgramUniformHandleui64vARB(program, location, count, values_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglIsTextureHandleResidentARB(JNIEnv *env, jclass clazz, jlong handle, jlong function_pointer) {
	glIsTextureHandleResidentARBPROC glIsTextureHandleResidentARB = (glIsTextureHandleResidentARBPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsTextureHandleResidentARB(handle);
	return __result;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglIsImageHandleResidentARB(JNIEnv *env, jclass clazz, jlong handle, jlong function_pointer) {
	glIsImageHandleResidentARBPROC glIsImageHandleResidentARB = (glIsImageHandleResidentARBPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsImageHandleResidentARB(handle);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglVertexAttribL1ui64ARB(JNIEnv *env, jclass clazz, jint index, jlong x, jlong function_pointer) {
	glVertexAttribL1ui64ARBPROC glVertexAttribL1ui64ARB = (glVertexAttribL1ui64ARBPROC)((intptr_t)function_pointer);
	glVertexAttribL1ui64ARB(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglVertexAttribL1ui64vARB(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLuint64EXT *v_address = (const GLuint64EXT *)(intptr_t)v;
	glVertexAttribL1ui64vARBPROC glVertexAttribL1ui64vARB = (glVertexAttribL1ui64vARBPROC)((intptr_t)function_pointer);
	glVertexAttribL1ui64vARB(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglGetVertexAttribLui64vARB(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLuint64EXT *params_address = (GLuint64EXT *)(intptr_t)params;
	glGetVertexAttribLui64vARBPROC glGetVertexAttribLui64vARB = (glGetVertexAttribLui64vARBPROC)((intptr_t)function_pointer);
	glGetVertexAttribLui64vARB(index, pname, params_address);
}

