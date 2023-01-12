/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glUniform1i64NVPROC) (GLint location, GLint64EXT x);
typedef void (APIENTRY *glUniform2i64NVPROC) (GLint location, GLint64EXT x, GLint64EXT y);
typedef void (APIENTRY *glUniform3i64NVPROC) (GLint location, GLint64EXT x, GLint64EXT y, GLint64EXT z);
typedef void (APIENTRY *glUniform4i64NVPROC) (GLint location, GLint64EXT x, GLint64EXT y, GLint64EXT z, GLint64EXT w);
typedef void (APIENTRY *glUniform1i64vNVPROC) (GLint location, GLsizei count, const GLint64EXT * value);
typedef void (APIENTRY *glUniform2i64vNVPROC) (GLint location, GLsizei count, const GLint64EXT * value);
typedef void (APIENTRY *glUniform3i64vNVPROC) (GLint location, GLsizei count, const GLint64EXT * value);
typedef void (APIENTRY *glUniform4i64vNVPROC) (GLint location, GLsizei count, const GLint64EXT * value);
typedef void (APIENTRY *glUniform1ui64NVPROC) (GLint location, GLuint64EXT x);
typedef void (APIENTRY *glUniform2ui64NVPROC) (GLint location, GLuint64EXT x, GLuint64EXT y);
typedef void (APIENTRY *glUniform3ui64NVPROC) (GLint location, GLuint64EXT x, GLuint64EXT y, GLuint64EXT z);
typedef void (APIENTRY *glUniform4ui64NVPROC) (GLint location, GLuint64EXT x, GLuint64EXT y, GLuint64EXT z, GLuint64EXT w);
typedef void (APIENTRY *glUniform1ui64vNVPROC) (GLint location, GLsizei count, const GLuint64EXT * value);
typedef void (APIENTRY *glUniform2ui64vNVPROC) (GLint location, GLsizei count, const GLuint64EXT * value);
typedef void (APIENTRY *glUniform3ui64vNVPROC) (GLint location, GLsizei count, const GLuint64EXT * value);
typedef void (APIENTRY *glUniform4ui64vNVPROC) (GLint location, GLsizei count, const GLuint64EXT * value);
typedef void (APIENTRY *glGetUniformi64vNVPROC) (GLuint program, GLint location, GLint64EXT * params);
typedef void (APIENTRY *glGetUniformui64vNVPROC) (GLuint program, GLint location, GLuint64EXT * params);
typedef void (APIENTRY *glProgramUniform1i64NVPROC) (GLuint program, GLint location, GLint64EXT x);
typedef void (APIENTRY *glProgramUniform2i64NVPROC) (GLuint program, GLint location, GLint64EXT x, GLint64EXT y);
typedef void (APIENTRY *glProgramUniform3i64NVPROC) (GLuint program, GLint location, GLint64EXT x, GLint64EXT y, GLint64EXT z);
typedef void (APIENTRY *glProgramUniform4i64NVPROC) (GLuint program, GLint location, GLint64EXT x, GLint64EXT y, GLint64EXT z, GLint64EXT w);
typedef void (APIENTRY *glProgramUniform1i64vNVPROC) (GLuint program, GLint location, GLsizei count, const GLint64EXT * value);
typedef void (APIENTRY *glProgramUniform2i64vNVPROC) (GLuint program, GLint location, GLsizei count, const GLint64EXT * value);
typedef void (APIENTRY *glProgramUniform3i64vNVPROC) (GLuint program, GLint location, GLsizei count, const GLint64EXT * value);
typedef void (APIENTRY *glProgramUniform4i64vNVPROC) (GLuint program, GLint location, GLsizei count, const GLint64EXT * value);
typedef void (APIENTRY *glProgramUniform1ui64NVPROC) (GLuint program, GLint location, GLuint64EXT x);
typedef void (APIENTRY *glProgramUniform2ui64NVPROC) (GLuint program, GLint location, GLuint64EXT x, GLuint64EXT y);
typedef void (APIENTRY *glProgramUniform3ui64NVPROC) (GLuint program, GLint location, GLuint64EXT x, GLuint64EXT y, GLuint64EXT z);
typedef void (APIENTRY *glProgramUniform4ui64NVPROC) (GLuint program, GLint location, GLuint64EXT x, GLuint64EXT y, GLuint64EXT z, GLuint64EXT w);
typedef void (APIENTRY *glProgramUniform1ui64vNVPROC) (GLuint program, GLint location, GLsizei count, const GLuint64EXT * value);
typedef void (APIENTRY *glProgramUniform2ui64vNVPROC) (GLuint program, GLint location, GLsizei count, const GLuint64EXT * value);
typedef void (APIENTRY *glProgramUniform3ui64vNVPROC) (GLuint program, GLint location, GLsizei count, const GLuint64EXT * value);
typedef void (APIENTRY *glProgramUniform4ui64vNVPROC) (GLuint program, GLint location, GLsizei count, const GLuint64EXT * value);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform1i64NV(JNIEnv *env, jclass clazz, jint location, jlong x, jlong function_pointer) {
	glUniform1i64NVPROC glUniform1i64NV = (glUniform1i64NVPROC)((intptr_t)function_pointer);
	glUniform1i64NV(location, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform2i64NV(JNIEnv *env, jclass clazz, jint location, jlong x, jlong y, jlong function_pointer) {
	glUniform2i64NVPROC glUniform2i64NV = (glUniform2i64NVPROC)((intptr_t)function_pointer);
	glUniform2i64NV(location, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform3i64NV(JNIEnv *env, jclass clazz, jint location, jlong x, jlong y, jlong z, jlong function_pointer) {
	glUniform3i64NVPROC glUniform3i64NV = (glUniform3i64NVPROC)((intptr_t)function_pointer);
	glUniform3i64NV(location, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform4i64NV(JNIEnv *env, jclass clazz, jint location, jlong x, jlong y, jlong z, jlong w, jlong function_pointer) {
	glUniform4i64NVPROC glUniform4i64NV = (glUniform4i64NVPROC)((intptr_t)function_pointer);
	glUniform4i64NV(location, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform1i64vNV(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint64EXT *value_address = (const GLint64EXT *)(intptr_t)value;
	glUniform1i64vNVPROC glUniform1i64vNV = (glUniform1i64vNVPROC)((intptr_t)function_pointer);
	glUniform1i64vNV(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform2i64vNV(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint64EXT *value_address = (const GLint64EXT *)(intptr_t)value;
	glUniform2i64vNVPROC glUniform2i64vNV = (glUniform2i64vNVPROC)((intptr_t)function_pointer);
	glUniform2i64vNV(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform3i64vNV(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint64EXT *value_address = (const GLint64EXT *)(intptr_t)value;
	glUniform3i64vNVPROC glUniform3i64vNV = (glUniform3i64vNVPROC)((intptr_t)function_pointer);
	glUniform3i64vNV(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform4i64vNV(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint64EXT *value_address = (const GLint64EXT *)(intptr_t)value;
	glUniform4i64vNVPROC glUniform4i64vNV = (glUniform4i64vNVPROC)((intptr_t)function_pointer);
	glUniform4i64vNV(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform1ui64NV(JNIEnv *env, jclass clazz, jint location, jlong x, jlong function_pointer) {
	glUniform1ui64NVPROC glUniform1ui64NV = (glUniform1ui64NVPROC)((intptr_t)function_pointer);
	glUniform1ui64NV(location, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform2ui64NV(JNIEnv *env, jclass clazz, jint location, jlong x, jlong y, jlong function_pointer) {
	glUniform2ui64NVPROC glUniform2ui64NV = (glUniform2ui64NVPROC)((intptr_t)function_pointer);
	glUniform2ui64NV(location, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform3ui64NV(JNIEnv *env, jclass clazz, jint location, jlong x, jlong y, jlong z, jlong function_pointer) {
	glUniform3ui64NVPROC glUniform3ui64NV = (glUniform3ui64NVPROC)((intptr_t)function_pointer);
	glUniform3ui64NV(location, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform4ui64NV(JNIEnv *env, jclass clazz, jint location, jlong x, jlong y, jlong z, jlong w, jlong function_pointer) {
	glUniform4ui64NVPROC glUniform4ui64NV = (glUniform4ui64NVPROC)((intptr_t)function_pointer);
	glUniform4ui64NV(location, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform1ui64vNV(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint64EXT *value_address = (const GLuint64EXT *)(intptr_t)value;
	glUniform1ui64vNVPROC glUniform1ui64vNV = (glUniform1ui64vNVPROC)((intptr_t)function_pointer);
	glUniform1ui64vNV(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform2ui64vNV(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint64EXT *value_address = (const GLuint64EXT *)(intptr_t)value;
	glUniform2ui64vNVPROC glUniform2ui64vNV = (glUniform2ui64vNVPROC)((intptr_t)function_pointer);
	glUniform2ui64vNV(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform3ui64vNV(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint64EXT *value_address = (const GLuint64EXT *)(intptr_t)value;
	glUniform3ui64vNVPROC glUniform3ui64vNV = (glUniform3ui64vNVPROC)((intptr_t)function_pointer);
	glUniform3ui64vNV(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglUniform4ui64vNV(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint64EXT *value_address = (const GLuint64EXT *)(intptr_t)value;
	glUniform4ui64vNVPROC glUniform4ui64vNV = (glUniform4ui64vNVPROC)((intptr_t)function_pointer);
	glUniform4ui64vNV(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglGetUniformi64vNV(JNIEnv *env, jclass clazz, jint program, jint location, jlong params, jlong function_pointer) {
	GLint64EXT *params_address = (GLint64EXT *)(intptr_t)params;
	glGetUniformi64vNVPROC glGetUniformi64vNV = (glGetUniformi64vNVPROC)((intptr_t)function_pointer);
	glGetUniformi64vNV(program, location, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglGetUniformui64vNV(JNIEnv *env, jclass clazz, jint program, jint location, jlong params, jlong function_pointer) {
	GLuint64EXT *params_address = (GLuint64EXT *)(intptr_t)params;
	glGetUniformui64vNVPROC glGetUniformui64vNV = (glGetUniformui64vNVPROC)((intptr_t)function_pointer);
	glGetUniformui64vNV(program, location, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform1i64NV(JNIEnv *env, jclass clazz, jint program, jint location, jlong x, jlong function_pointer) {
	glProgramUniform1i64NVPROC glProgramUniform1i64NV = (glProgramUniform1i64NVPROC)((intptr_t)function_pointer);
	glProgramUniform1i64NV(program, location, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform2i64NV(JNIEnv *env, jclass clazz, jint program, jint location, jlong x, jlong y, jlong function_pointer) {
	glProgramUniform2i64NVPROC glProgramUniform2i64NV = (glProgramUniform2i64NVPROC)((intptr_t)function_pointer);
	glProgramUniform2i64NV(program, location, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform3i64NV(JNIEnv *env, jclass clazz, jint program, jint location, jlong x, jlong y, jlong z, jlong function_pointer) {
	glProgramUniform3i64NVPROC glProgramUniform3i64NV = (glProgramUniform3i64NVPROC)((intptr_t)function_pointer);
	glProgramUniform3i64NV(program, location, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform4i64NV(JNIEnv *env, jclass clazz, jint program, jint location, jlong x, jlong y, jlong z, jlong w, jlong function_pointer) {
	glProgramUniform4i64NVPROC glProgramUniform4i64NV = (glProgramUniform4i64NVPROC)((intptr_t)function_pointer);
	glProgramUniform4i64NV(program, location, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform1i64vNV(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint64EXT *value_address = (const GLint64EXT *)(intptr_t)value;
	glProgramUniform1i64vNVPROC glProgramUniform1i64vNV = (glProgramUniform1i64vNVPROC)((intptr_t)function_pointer);
	glProgramUniform1i64vNV(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform2i64vNV(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint64EXT *value_address = (const GLint64EXT *)(intptr_t)value;
	glProgramUniform2i64vNVPROC glProgramUniform2i64vNV = (glProgramUniform2i64vNVPROC)((intptr_t)function_pointer);
	glProgramUniform2i64vNV(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform3i64vNV(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint64EXT *value_address = (const GLint64EXT *)(intptr_t)value;
	glProgramUniform3i64vNVPROC glProgramUniform3i64vNV = (glProgramUniform3i64vNVPROC)((intptr_t)function_pointer);
	glProgramUniform3i64vNV(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform4i64vNV(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint64EXT *value_address = (const GLint64EXT *)(intptr_t)value;
	glProgramUniform4i64vNVPROC glProgramUniform4i64vNV = (glProgramUniform4i64vNVPROC)((intptr_t)function_pointer);
	glProgramUniform4i64vNV(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform1ui64NV(JNIEnv *env, jclass clazz, jint program, jint location, jlong x, jlong function_pointer) {
	glProgramUniform1ui64NVPROC glProgramUniform1ui64NV = (glProgramUniform1ui64NVPROC)((intptr_t)function_pointer);
	glProgramUniform1ui64NV(program, location, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform2ui64NV(JNIEnv *env, jclass clazz, jint program, jint location, jlong x, jlong y, jlong function_pointer) {
	glProgramUniform2ui64NVPROC glProgramUniform2ui64NV = (glProgramUniform2ui64NVPROC)((intptr_t)function_pointer);
	glProgramUniform2ui64NV(program, location, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform3ui64NV(JNIEnv *env, jclass clazz, jint program, jint location, jlong x, jlong y, jlong z, jlong function_pointer) {
	glProgramUniform3ui64NVPROC glProgramUniform3ui64NV = (glProgramUniform3ui64NVPROC)((intptr_t)function_pointer);
	glProgramUniform3ui64NV(program, location, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform4ui64NV(JNIEnv *env, jclass clazz, jint program, jint location, jlong x, jlong y, jlong z, jlong w, jlong function_pointer) {
	glProgramUniform4ui64NVPROC glProgramUniform4ui64NV = (glProgramUniform4ui64NVPROC)((intptr_t)function_pointer);
	glProgramUniform4ui64NV(program, location, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform1ui64vNV(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint64EXT *value_address = (const GLuint64EXT *)(intptr_t)value;
	glProgramUniform1ui64vNVPROC glProgramUniform1ui64vNV = (glProgramUniform1ui64vNVPROC)((intptr_t)function_pointer);
	glProgramUniform1ui64vNV(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform2ui64vNV(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint64EXT *value_address = (const GLuint64EXT *)(intptr_t)value;
	glProgramUniform2ui64vNVPROC glProgramUniform2ui64vNV = (glProgramUniform2ui64vNVPROC)((intptr_t)function_pointer);
	glProgramUniform2ui64vNV(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform3ui64vNV(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint64EXT *value_address = (const GLuint64EXT *)(intptr_t)value;
	glProgramUniform3ui64vNVPROC glProgramUniform3ui64vNV = (glProgramUniform3ui64vNVPROC)((intptr_t)function_pointer);
	glProgramUniform3ui64vNV(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuShader5_nglProgramUniform4ui64vNV(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint64EXT *value_address = (const GLuint64EXT *)(intptr_t)value;
	glProgramUniform4ui64vNVPROC glProgramUniform4ui64vNV = (glProgramUniform4ui64vNVPROC)((intptr_t)function_pointer);
	glProgramUniform4ui64vNV(program, location, count, value_address);
}

