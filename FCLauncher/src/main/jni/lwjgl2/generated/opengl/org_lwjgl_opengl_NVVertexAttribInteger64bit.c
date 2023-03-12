/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glVertexAttribL1i64NVPROC) (GLuint index, GLint64EXT x);
typedef void (APIENTRY *glVertexAttribL2i64NVPROC) (GLuint index, GLint64EXT x, GLint64EXT y);
typedef void (APIENTRY *glVertexAttribL3i64NVPROC) (GLuint index, GLint64EXT x, GLint64EXT y, GLint64EXT z);
typedef void (APIENTRY *glVertexAttribL4i64NVPROC) (GLuint index, GLint64EXT x, GLint64EXT y, GLint64EXT z, GLint64EXT w);
typedef void (APIENTRY *glVertexAttribL1i64vNVPROC) (GLuint index, const GLint64EXT * v);
typedef void (APIENTRY *glVertexAttribL2i64vNVPROC) (GLuint index, const GLint64EXT * v);
typedef void (APIENTRY *glVertexAttribL3i64vNVPROC) (GLuint index, const GLint64EXT * v);
typedef void (APIENTRY *glVertexAttribL4i64vNVPROC) (GLuint index, const GLint64EXT * v);
typedef void (APIENTRY *glVertexAttribL1ui64NVPROC) (GLuint index, GLuint64EXT x);
typedef void (APIENTRY *glVertexAttribL2ui64NVPROC) (GLuint index, GLuint64EXT x, GLuint64EXT y);
typedef void (APIENTRY *glVertexAttribL3ui64NVPROC) (GLuint index, GLuint64EXT x, GLuint64EXT y, GLuint64EXT z);
typedef void (APIENTRY *glVertexAttribL4ui64NVPROC) (GLuint index, GLuint64EXT x, GLuint64EXT y, GLuint64EXT z, GLuint64EXT w);
typedef void (APIENTRY *glVertexAttribL1ui64vNVPROC) (GLuint index, const GLuint64EXT * v);
typedef void (APIENTRY *glVertexAttribL2ui64vNVPROC) (GLuint index, const GLuint64EXT * v);
typedef void (APIENTRY *glVertexAttribL3ui64vNVPROC) (GLuint index, const GLuint64EXT * v);
typedef void (APIENTRY *glVertexAttribL4ui64vNVPROC) (GLuint index, const GLuint64EXT * v);
typedef void (APIENTRY *glGetVertexAttribLi64vNVPROC) (GLuint index, GLenum pname, GLint64EXT * params);
typedef void (APIENTRY *glGetVertexAttribLui64vNVPROC) (GLuint index, GLenum pname, GLuint64EXT * params);
typedef void (APIENTRY *glVertexAttribLFormatNVPROC) (GLuint index, GLint size, GLenum type, GLsizei stride);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL1i64NV(JNIEnv *env, jclass clazz, jint index, jlong x, jlong function_pointer) {
	glVertexAttribL1i64NVPROC glVertexAttribL1i64NV = (glVertexAttribL1i64NVPROC)((intptr_t)function_pointer);
	glVertexAttribL1i64NV(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL2i64NV(JNIEnv *env, jclass clazz, jint index, jlong x, jlong y, jlong function_pointer) {
	glVertexAttribL2i64NVPROC glVertexAttribL2i64NV = (glVertexAttribL2i64NVPROC)((intptr_t)function_pointer);
	glVertexAttribL2i64NV(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL3i64NV(JNIEnv *env, jclass clazz, jint index, jlong x, jlong y, jlong z, jlong function_pointer) {
	glVertexAttribL3i64NVPROC glVertexAttribL3i64NV = (glVertexAttribL3i64NVPROC)((intptr_t)function_pointer);
	glVertexAttribL3i64NV(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL4i64NV(JNIEnv *env, jclass clazz, jint index, jlong x, jlong y, jlong z, jlong w, jlong function_pointer) {
	glVertexAttribL4i64NVPROC glVertexAttribL4i64NV = (glVertexAttribL4i64NVPROC)((intptr_t)function_pointer);
	glVertexAttribL4i64NV(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL1i64vNV(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLint64EXT *v_address = (const GLint64EXT *)(intptr_t)v;
	glVertexAttribL1i64vNVPROC glVertexAttribL1i64vNV = (glVertexAttribL1i64vNVPROC)((intptr_t)function_pointer);
	glVertexAttribL1i64vNV(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL2i64vNV(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLint64EXT *v_address = (const GLint64EXT *)(intptr_t)v;
	glVertexAttribL2i64vNVPROC glVertexAttribL2i64vNV = (glVertexAttribL2i64vNVPROC)((intptr_t)function_pointer);
	glVertexAttribL2i64vNV(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL3i64vNV(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLint64EXT *v_address = (const GLint64EXT *)(intptr_t)v;
	glVertexAttribL3i64vNVPROC glVertexAttribL3i64vNV = (glVertexAttribL3i64vNVPROC)((intptr_t)function_pointer);
	glVertexAttribL3i64vNV(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL4i64vNV(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLint64EXT *v_address = (const GLint64EXT *)(intptr_t)v;
	glVertexAttribL4i64vNVPROC glVertexAttribL4i64vNV = (glVertexAttribL4i64vNVPROC)((intptr_t)function_pointer);
	glVertexAttribL4i64vNV(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL1ui64NV(JNIEnv *env, jclass clazz, jint index, jlong x, jlong function_pointer) {
	glVertexAttribL1ui64NVPROC glVertexAttribL1ui64NV = (glVertexAttribL1ui64NVPROC)((intptr_t)function_pointer);
	glVertexAttribL1ui64NV(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL2ui64NV(JNIEnv *env, jclass clazz, jint index, jlong x, jlong y, jlong function_pointer) {
	glVertexAttribL2ui64NVPROC glVertexAttribL2ui64NV = (glVertexAttribL2ui64NVPROC)((intptr_t)function_pointer);
	glVertexAttribL2ui64NV(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL3ui64NV(JNIEnv *env, jclass clazz, jint index, jlong x, jlong y, jlong z, jlong function_pointer) {
	glVertexAttribL3ui64NVPROC glVertexAttribL3ui64NV = (glVertexAttribL3ui64NVPROC)((intptr_t)function_pointer);
	glVertexAttribL3ui64NV(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL4ui64NV(JNIEnv *env, jclass clazz, jint index, jlong x, jlong y, jlong z, jlong w, jlong function_pointer) {
	glVertexAttribL4ui64NVPROC glVertexAttribL4ui64NV = (glVertexAttribL4ui64NVPROC)((intptr_t)function_pointer);
	glVertexAttribL4ui64NV(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL1ui64vNV(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLuint64EXT *v_address = (const GLuint64EXT *)(intptr_t)v;
	glVertexAttribL1ui64vNVPROC glVertexAttribL1ui64vNV = (glVertexAttribL1ui64vNVPROC)((intptr_t)function_pointer);
	glVertexAttribL1ui64vNV(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL2ui64vNV(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLuint64EXT *v_address = (const GLuint64EXT *)(intptr_t)v;
	glVertexAttribL2ui64vNVPROC glVertexAttribL2ui64vNV = (glVertexAttribL2ui64vNVPROC)((intptr_t)function_pointer);
	glVertexAttribL2ui64vNV(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL3ui64vNV(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLuint64EXT *v_address = (const GLuint64EXT *)(intptr_t)v;
	glVertexAttribL3ui64vNVPROC glVertexAttribL3ui64vNV = (glVertexAttribL3ui64vNVPROC)((intptr_t)function_pointer);
	glVertexAttribL3ui64vNV(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL4ui64vNV(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLuint64EXT *v_address = (const GLuint64EXT *)(intptr_t)v;
	glVertexAttribL4ui64vNVPROC glVertexAttribL4ui64vNV = (glVertexAttribL4ui64vNVPROC)((intptr_t)function_pointer);
	glVertexAttribL4ui64vNV(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglGetVertexAttribLi64vNV(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLint64EXT *params_address = (GLint64EXT *)(intptr_t)params;
	glGetVertexAttribLi64vNVPROC glGetVertexAttribLi64vNV = (glGetVertexAttribLi64vNVPROC)((intptr_t)function_pointer);
	glGetVertexAttribLi64vNV(index, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglGetVertexAttribLui64vNV(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLuint64EXT *params_address = (GLuint64EXT *)(intptr_t)params;
	glGetVertexAttribLui64vNVPROC glGetVertexAttribLui64vNV = (glGetVertexAttribLui64vNVPROC)((intptr_t)function_pointer);
	glGetVertexAttribLui64vNV(index, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribLFormatNV(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jint stride, jlong function_pointer) {
	glVertexAttribLFormatNVPROC glVertexAttribLFormatNV = (glVertexAttribLFormatNVPROC)((intptr_t)function_pointer);
	glVertexAttribLFormatNV(index, size, type, stride);
}

