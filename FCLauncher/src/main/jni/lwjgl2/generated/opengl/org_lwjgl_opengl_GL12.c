/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glDrawRangeElementsPROC) (GLenum mode, GLuint start, GLuint end, GLsizei count, GLenum type, const GLvoid * indices);
typedef void (APIENTRY *glTexImage3DPROC) (GLenum target, GLint level, GLint internalFormat, GLsizei width, GLsizei height, GLsizei depth, GLint border, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glTexSubImage3DPROC) (GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glCopyTexSubImage3DPROC) (GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLint x, GLint y, GLsizei width, GLsizei height);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL12_nglDrawRangeElements(JNIEnv *env, jclass clazz, jint mode, jint start, jint end, jint count, jint type, jlong indices, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)indices;
	glDrawRangeElementsPROC glDrawRangeElements = (glDrawRangeElementsPROC)((intptr_t)function_pointer);
	glDrawRangeElements(mode, start, end, count, type, indices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL12_nglDrawRangeElementsBO(JNIEnv *env, jclass clazz, jint mode, jint start, jint end, jint count, jint type, jlong indices_buffer_offset, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)offsetToPointer(indices_buffer_offset);
	glDrawRangeElementsPROC glDrawRangeElements = (glDrawRangeElementsPROC)((intptr_t)function_pointer);
	glDrawRangeElements(mode, start, end, count, type, indices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL12_nglTexImage3D(JNIEnv *env, jclass clazz, jint target, jint level, jint internalFormat, jint width, jint height, jint depth, jint border, jint format, jint type, jlong pixels, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)pixels;
	glTexImage3DPROC glTexImage3D = (glTexImage3DPROC)((intptr_t)function_pointer);
	glTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL12_nglTexImage3DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint internalFormat, jint width, jint height, jint depth, jint border, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glTexImage3DPROC glTexImage3D = (glTexImage3DPROC)((intptr_t)function_pointer);
	glTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL12_nglTexSubImage3D(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint type, jlong pixels, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)pixels;
	glTexSubImage3DPROC glTexSubImage3D = (glTexSubImage3DPROC)((intptr_t)function_pointer);
	glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL12_nglTexSubImage3DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glTexSubImage3DPROC glTexSubImage3D = (glTexSubImage3DPROC)((intptr_t)function_pointer);
	glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL12_nglCopyTexSubImage3D(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint x, jint y, jint width, jint height, jlong function_pointer) {
	glCopyTexSubImage3DPROC glCopyTexSubImage3D = (glCopyTexSubImage3DPROC)((intptr_t)function_pointer);
	glCopyTexSubImage3D(target, level, xoffset, yoffset, zoffset, x, y, width, height);
}

