/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBufferAddressRangeNVPROC) (GLenum pname, GLuint index, GLuint64EXT address, GLsizeiptr length);
typedef void (APIENTRY *glVertexFormatNVPROC) (GLint size, GLenum type, GLsizei stride);
typedef void (APIENTRY *glNormalFormatNVPROC) (GLenum type, GLsizei stride);
typedef void (APIENTRY *glColorFormatNVPROC) (GLint size, GLenum type, GLsizei stride);
typedef void (APIENTRY *glIndexFormatNVPROC) (GLenum type, GLsizei stride);
typedef void (APIENTRY *glTexCoordFormatNVPROC) (GLint size, GLenum type, GLsizei stride);
typedef void (APIENTRY *glEdgeFlagFormatNVPROC) (GLsizei stride);
typedef void (APIENTRY *glSecondaryColorFormatNVPROC) (GLint size, GLenum type, GLsizei stride);
typedef void (APIENTRY *glFogCoordFormatNVPROC) (GLenum type, GLsizei stride);
typedef void (APIENTRY *glVertexAttribFormatNVPROC) (GLuint index, GLint size, GLenum type, GLboolean normalized, GLsizei stride);
typedef void (APIENTRY *glVertexAttribIFormatNVPROC) (GLuint index, GLint size, GLenum type, GLsizei stride);
typedef void (APIENTRY *glGetIntegerui64i_vNVPROC) (GLenum value, GLuint index, GLuint64EXT * result);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexBufferUnifiedMemory_nglBufferAddressRangeNV(JNIEnv *env, jclass clazz, jint pname, jint index, jlong address, jlong length, jlong function_pointer) {
	glBufferAddressRangeNVPROC glBufferAddressRangeNV = (glBufferAddressRangeNVPROC)((intptr_t)function_pointer);
	glBufferAddressRangeNV(pname, index, address, length);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexBufferUnifiedMemory_nglVertexFormatNV(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong function_pointer) {
	glVertexFormatNVPROC glVertexFormatNV = (glVertexFormatNVPROC)((intptr_t)function_pointer);
	glVertexFormatNV(size, type, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexBufferUnifiedMemory_nglNormalFormatNV(JNIEnv *env, jclass clazz, jint type, jint stride, jlong function_pointer) {
	glNormalFormatNVPROC glNormalFormatNV = (glNormalFormatNVPROC)((intptr_t)function_pointer);
	glNormalFormatNV(type, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexBufferUnifiedMemory_nglColorFormatNV(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong function_pointer) {
	glColorFormatNVPROC glColorFormatNV = (glColorFormatNVPROC)((intptr_t)function_pointer);
	glColorFormatNV(size, type, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexBufferUnifiedMemory_nglIndexFormatNV(JNIEnv *env, jclass clazz, jint type, jint stride, jlong function_pointer) {
	glIndexFormatNVPROC glIndexFormatNV = (glIndexFormatNVPROC)((intptr_t)function_pointer);
	glIndexFormatNV(type, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexBufferUnifiedMemory_nglTexCoordFormatNV(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong function_pointer) {
	glTexCoordFormatNVPROC glTexCoordFormatNV = (glTexCoordFormatNVPROC)((intptr_t)function_pointer);
	glTexCoordFormatNV(size, type, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexBufferUnifiedMemory_nglEdgeFlagFormatNV(JNIEnv *env, jclass clazz, jint stride, jlong function_pointer) {
	glEdgeFlagFormatNVPROC glEdgeFlagFormatNV = (glEdgeFlagFormatNVPROC)((intptr_t)function_pointer);
	glEdgeFlagFormatNV(stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexBufferUnifiedMemory_nglSecondaryColorFormatNV(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong function_pointer) {
	glSecondaryColorFormatNVPROC glSecondaryColorFormatNV = (glSecondaryColorFormatNVPROC)((intptr_t)function_pointer);
	glSecondaryColorFormatNV(size, type, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexBufferUnifiedMemory_nglFogCoordFormatNV(JNIEnv *env, jclass clazz, jint type, jint stride, jlong function_pointer) {
	glFogCoordFormatNVPROC glFogCoordFormatNV = (glFogCoordFormatNVPROC)((intptr_t)function_pointer);
	glFogCoordFormatNV(type, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexBufferUnifiedMemory_nglVertexAttribFormatNV(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jboolean normalized, jint stride, jlong function_pointer) {
	glVertexAttribFormatNVPROC glVertexAttribFormatNV = (glVertexAttribFormatNVPROC)((intptr_t)function_pointer);
	glVertexAttribFormatNV(index, size, type, normalized, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexBufferUnifiedMemory_nglVertexAttribIFormatNV(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jint stride, jlong function_pointer) {
	glVertexAttribIFormatNVPROC glVertexAttribIFormatNV = (glVertexAttribIFormatNVPROC)((intptr_t)function_pointer);
	glVertexAttribIFormatNV(index, size, type, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexBufferUnifiedMemory_nglGetIntegerui64i_1vNV(JNIEnv *env, jclass clazz, jint value, jint index, jlong result, jlong function_pointer) {
	GLuint64EXT *result_address = (GLuint64EXT *)(intptr_t)result;
	glGetIntegerui64i_vNVPROC glGetIntegerui64i_vNV = (glGetIntegerui64i_vNVPROC)((intptr_t)function_pointer);
	glGetIntegerui64i_vNV(value, index, result_address);
}

