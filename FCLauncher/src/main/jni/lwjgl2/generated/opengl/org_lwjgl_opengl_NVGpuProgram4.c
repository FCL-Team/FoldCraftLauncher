/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glProgramLocalParameterI4iNVPROC) (GLenum target, GLuint index, GLint x, GLint y, GLint z, GLint w);
typedef void (APIENTRY *glProgramLocalParameterI4ivNVPROC) (GLenum target, GLuint index, const GLint * params);
typedef void (APIENTRY *glProgramLocalParametersI4ivNVPROC) (GLenum target, GLuint index, GLsizei count, const GLint * params);
typedef void (APIENTRY *glProgramLocalParameterI4uiNVPROC) (GLenum target, GLuint index, GLuint x, GLuint y, GLuint z, GLuint w);
typedef void (APIENTRY *glProgramLocalParameterI4uivNVPROC) (GLenum target, GLuint index, const GLuint * params);
typedef void (APIENTRY *glProgramLocalParametersI4uivNVPROC) (GLenum target, GLuint index, GLsizei count, const GLuint * params);
typedef void (APIENTRY *glProgramEnvParameterI4iNVPROC) (GLenum target, GLuint index, GLint x, GLint y, GLint z, GLint w);
typedef void (APIENTRY *glProgramEnvParameterI4ivNVPROC) (GLenum target, GLuint index, const GLint * params);
typedef void (APIENTRY *glProgramEnvParametersI4ivNVPROC) (GLenum target, GLuint index, GLsizei count, const GLint * params);
typedef void (APIENTRY *glProgramEnvParameterI4uiNVPROC) (GLenum target, GLuint index, GLuint x, GLuint y, GLuint z, GLuint w);
typedef void (APIENTRY *glProgramEnvParameterI4uivNVPROC) (GLenum target, GLuint index, const GLuint * params);
typedef void (APIENTRY *glProgramEnvParametersI4uivNVPROC) (GLenum target, GLuint index, GLsizei count, const GLuint * params);
typedef void (APIENTRY *glGetProgramLocalParameterIivNVPROC) (GLenum target, GLuint index, GLint * params);
typedef void (APIENTRY *glGetProgramLocalParameterIuivNVPROC) (GLenum target, GLuint index, GLuint * params);
typedef void (APIENTRY *glGetProgramEnvParameterIivNVPROC) (GLenum target, GLuint index, GLint * params);
typedef void (APIENTRY *glGetProgramEnvParameterIuivNVPROC) (GLenum target, GLuint index, GLuint * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglProgramLocalParameterI4iNV(JNIEnv *env, jclass clazz, jint target, jint index, jint x, jint y, jint z, jint w, jlong function_pointer) {
	glProgramLocalParameterI4iNVPROC glProgramLocalParameterI4iNV = (glProgramLocalParameterI4iNVPROC)((intptr_t)function_pointer);
	glProgramLocalParameterI4iNV(target, index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglProgramLocalParameterI4ivNV(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glProgramLocalParameterI4ivNVPROC glProgramLocalParameterI4ivNV = (glProgramLocalParameterI4ivNVPROC)((intptr_t)function_pointer);
	glProgramLocalParameterI4ivNV(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglProgramLocalParametersI4ivNV(JNIEnv *env, jclass clazz, jint target, jint index, jint count, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glProgramLocalParametersI4ivNVPROC glProgramLocalParametersI4ivNV = (glProgramLocalParametersI4ivNVPROC)((intptr_t)function_pointer);
	glProgramLocalParametersI4ivNV(target, index, count, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglProgramLocalParameterI4uiNV(JNIEnv *env, jclass clazz, jint target, jint index, jint x, jint y, jint z, jint w, jlong function_pointer) {
	glProgramLocalParameterI4uiNVPROC glProgramLocalParameterI4uiNV = (glProgramLocalParameterI4uiNVPROC)((intptr_t)function_pointer);
	glProgramLocalParameterI4uiNV(target, index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglProgramLocalParameterI4uivNV(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	const GLuint *params_address = (const GLuint *)(intptr_t)params;
	glProgramLocalParameterI4uivNVPROC glProgramLocalParameterI4uivNV = (glProgramLocalParameterI4uivNVPROC)((intptr_t)function_pointer);
	glProgramLocalParameterI4uivNV(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglProgramLocalParametersI4uivNV(JNIEnv *env, jclass clazz, jint target, jint index, jint count, jlong params, jlong function_pointer) {
	const GLuint *params_address = (const GLuint *)(intptr_t)params;
	glProgramLocalParametersI4uivNVPROC glProgramLocalParametersI4uivNV = (glProgramLocalParametersI4uivNVPROC)((intptr_t)function_pointer);
	glProgramLocalParametersI4uivNV(target, index, count, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglProgramEnvParameterI4iNV(JNIEnv *env, jclass clazz, jint target, jint index, jint x, jint y, jint z, jint w, jlong function_pointer) {
	glProgramEnvParameterI4iNVPROC glProgramEnvParameterI4iNV = (glProgramEnvParameterI4iNVPROC)((intptr_t)function_pointer);
	glProgramEnvParameterI4iNV(target, index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglProgramEnvParameterI4ivNV(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glProgramEnvParameterI4ivNVPROC glProgramEnvParameterI4ivNV = (glProgramEnvParameterI4ivNVPROC)((intptr_t)function_pointer);
	glProgramEnvParameterI4ivNV(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglProgramEnvParametersI4ivNV(JNIEnv *env, jclass clazz, jint target, jint index, jint count, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glProgramEnvParametersI4ivNVPROC glProgramEnvParametersI4ivNV = (glProgramEnvParametersI4ivNVPROC)((intptr_t)function_pointer);
	glProgramEnvParametersI4ivNV(target, index, count, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglProgramEnvParameterI4uiNV(JNIEnv *env, jclass clazz, jint target, jint index, jint x, jint y, jint z, jint w, jlong function_pointer) {
	glProgramEnvParameterI4uiNVPROC glProgramEnvParameterI4uiNV = (glProgramEnvParameterI4uiNVPROC)((intptr_t)function_pointer);
	glProgramEnvParameterI4uiNV(target, index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglProgramEnvParameterI4uivNV(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	const GLuint *params_address = (const GLuint *)(intptr_t)params;
	glProgramEnvParameterI4uivNVPROC glProgramEnvParameterI4uivNV = (glProgramEnvParameterI4uivNVPROC)((intptr_t)function_pointer);
	glProgramEnvParameterI4uivNV(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglProgramEnvParametersI4uivNV(JNIEnv *env, jclass clazz, jint target, jint index, jint count, jlong params, jlong function_pointer) {
	const GLuint *params_address = (const GLuint *)(intptr_t)params;
	glProgramEnvParametersI4uivNVPROC glProgramEnvParametersI4uivNV = (glProgramEnvParametersI4uivNVPROC)((intptr_t)function_pointer);
	glProgramEnvParametersI4uivNV(target, index, count, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglGetProgramLocalParameterIivNV(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetProgramLocalParameterIivNVPROC glGetProgramLocalParameterIivNV = (glGetProgramLocalParameterIivNVPROC)((intptr_t)function_pointer);
	glGetProgramLocalParameterIivNV(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglGetProgramLocalParameterIuivNV(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetProgramLocalParameterIuivNVPROC glGetProgramLocalParameterIuivNV = (glGetProgramLocalParameterIuivNVPROC)((intptr_t)function_pointer);
	glGetProgramLocalParameterIuivNV(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglGetProgramEnvParameterIivNV(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetProgramEnvParameterIivNVPROC glGetProgramEnvParameterIivNV = (glGetProgramEnvParameterIivNVPROC)((intptr_t)function_pointer);
	glGetProgramEnvParameterIivNV(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGpuProgram4_nglGetProgramEnvParameterIuivNV(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetProgramEnvParameterIuivNVPROC glGetProgramEnvParameterIuivNV = (glGetProgramEnvParameterIuivNVPROC)((intptr_t)function_pointer);
	glGetProgramEnvParameterIuivNV(target, index, params_address);
}

