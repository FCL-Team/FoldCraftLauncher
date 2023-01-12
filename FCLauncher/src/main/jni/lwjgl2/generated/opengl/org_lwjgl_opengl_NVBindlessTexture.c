/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GLuint64 (APIENTRY *glGetTextureHandleNVPROC) (GLuint texture);
typedef GLuint64 (APIENTRY *glGetTextureSamplerHandleNVPROC) (GLuint texture, GLuint sampler);
typedef void (APIENTRY *glMakeTextureHandleResidentNVPROC) (GLuint64 handle);
typedef void (APIENTRY *glMakeTextureHandleNonResidentNVPROC) (GLuint64 handle);
typedef GLuint64 (APIENTRY *glGetImageHandleNVPROC) (GLuint texture, GLint level, GLboolean layered, GLint layer, GLenum format);
typedef void (APIENTRY *glMakeImageHandleResidentNVPROC) (GLuint64 handle, GLenum access);
typedef void (APIENTRY *glMakeImageHandleNonResidentNVPROC) (GLuint64 handle);
typedef void (APIENTRY *glUniformHandleui64NVPROC) (GLint location, GLuint64 value);
typedef void (APIENTRY *glUniformHandleui64vNVPROC) (GLint location, GLsizei count, const GLuint64 * value);
typedef void (APIENTRY *glProgramUniformHandleui64NVPROC) (GLuint program, GLint location, GLuint64 value);
typedef void (APIENTRY *glProgramUniformHandleui64vNVPROC) (GLuint program, GLint location, GLsizei count, const GLuint64 * values);
typedef GLboolean (APIENTRY *glIsTextureHandleResidentNVPROC) (GLuint64 handle);
typedef GLboolean (APIENTRY *glIsImageHandleResidentNVPROC) (GLuint64 handle);

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_NVBindlessTexture_nglGetTextureHandleNV(JNIEnv *env, jclass clazz, jint texture, jlong function_pointer) {
	glGetTextureHandleNVPROC glGetTextureHandleNV = (glGetTextureHandleNVPROC)((intptr_t)function_pointer);
	GLuint64 __result = glGetTextureHandleNV(texture);
	return __result;
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_NVBindlessTexture_nglGetTextureSamplerHandleNV(JNIEnv *env, jclass clazz, jint texture, jint sampler, jlong function_pointer) {
	glGetTextureSamplerHandleNVPROC glGetTextureSamplerHandleNV = (glGetTextureSamplerHandleNVPROC)((intptr_t)function_pointer);
	GLuint64 __result = glGetTextureSamplerHandleNV(texture, sampler);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVBindlessTexture_nglMakeTextureHandleResidentNV(JNIEnv *env, jclass clazz, jlong handle, jlong function_pointer) {
	glMakeTextureHandleResidentNVPROC glMakeTextureHandleResidentNV = (glMakeTextureHandleResidentNVPROC)((intptr_t)function_pointer);
	glMakeTextureHandleResidentNV(handle);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVBindlessTexture_nglMakeTextureHandleNonResidentNV(JNIEnv *env, jclass clazz, jlong handle, jlong function_pointer) {
	glMakeTextureHandleNonResidentNVPROC glMakeTextureHandleNonResidentNV = (glMakeTextureHandleNonResidentNVPROC)((intptr_t)function_pointer);
	glMakeTextureHandleNonResidentNV(handle);
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_NVBindlessTexture_nglGetImageHandleNV(JNIEnv *env, jclass clazz, jint texture, jint level, jboolean layered, jint layer, jint format, jlong function_pointer) {
	glGetImageHandleNVPROC glGetImageHandleNV = (glGetImageHandleNVPROC)((intptr_t)function_pointer);
	GLuint64 __result = glGetImageHandleNV(texture, level, layered, layer, format);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVBindlessTexture_nglMakeImageHandleResidentNV(JNIEnv *env, jclass clazz, jlong handle, jint access, jlong function_pointer) {
	glMakeImageHandleResidentNVPROC glMakeImageHandleResidentNV = (glMakeImageHandleResidentNVPROC)((intptr_t)function_pointer);
	glMakeImageHandleResidentNV(handle, access);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVBindlessTexture_nglMakeImageHandleNonResidentNV(JNIEnv *env, jclass clazz, jlong handle, jlong function_pointer) {
	glMakeImageHandleNonResidentNVPROC glMakeImageHandleNonResidentNV = (glMakeImageHandleNonResidentNVPROC)((intptr_t)function_pointer);
	glMakeImageHandleNonResidentNV(handle);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVBindlessTexture_nglUniformHandleui64NV(JNIEnv *env, jclass clazz, jint location, jlong value, jlong function_pointer) {
	glUniformHandleui64NVPROC glUniformHandleui64NV = (glUniformHandleui64NVPROC)((intptr_t)function_pointer);
	glUniformHandleui64NV(location, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVBindlessTexture_nglUniformHandleui64vNV(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint64 *value_address = (const GLuint64 *)(intptr_t)value;
	glUniformHandleui64vNVPROC glUniformHandleui64vNV = (glUniformHandleui64vNVPROC)((intptr_t)function_pointer);
	glUniformHandleui64vNV(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVBindlessTexture_nglProgramUniformHandleui64NV(JNIEnv *env, jclass clazz, jint program, jint location, jlong value, jlong function_pointer) {
	glProgramUniformHandleui64NVPROC glProgramUniformHandleui64NV = (glProgramUniformHandleui64NVPROC)((intptr_t)function_pointer);
	glProgramUniformHandleui64NV(program, location, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVBindlessTexture_nglProgramUniformHandleui64vNV(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong values, jlong function_pointer) {
	const GLuint64 *values_address = (const GLuint64 *)(intptr_t)values;
	glProgramUniformHandleui64vNVPROC glProgramUniformHandleui64vNV = (glProgramUniformHandleui64vNVPROC)((intptr_t)function_pointer);
	glProgramUniformHandleui64vNV(program, location, count, values_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_NVBindlessTexture_nglIsTextureHandleResidentNV(JNIEnv *env, jclass clazz, jlong handle, jlong function_pointer) {
	glIsTextureHandleResidentNVPROC glIsTextureHandleResidentNV = (glIsTextureHandleResidentNVPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsTextureHandleResidentNV(handle);
	return __result;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_NVBindlessTexture_nglIsImageHandleResidentNV(JNIEnv *env, jclass clazz, jlong handle, jlong function_pointer) {
	glIsImageHandleResidentNVPROC glIsImageHandleResidentNV = (glIsImageHandleResidentNVPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsImageHandleResidentNV(handle);
	return __result;
}

