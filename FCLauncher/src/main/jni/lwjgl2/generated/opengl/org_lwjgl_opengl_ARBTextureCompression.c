/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glCompressedTexImage1DARBPROC) (GLenum target, GLint level, GLenum internalformat, GLsizei width, GLint border, GLsizei imageSize, const GLvoid * pData);
typedef void (APIENTRY *glCompressedTexImage2DARBPROC) (GLenum target, GLint level, GLenum internalformat, GLsizei width, GLsizei height, GLint border, GLsizei imageSize, const GLvoid * pData);
typedef void (APIENTRY *glCompressedTexImage3DARBPROC) (GLenum target, GLint level, GLenum internalformat, GLsizei width, GLsizei height, GLsizei depth, GLint border, GLsizei imageSize, const GLvoid * pData);
typedef void (APIENTRY *glCompressedTexSubImage1DARBPROC) (GLenum target, GLint level, GLint xoffset, GLsizei width, GLenum format, GLsizei imageSize, const GLvoid * pData);
typedef void (APIENTRY *glCompressedTexSubImage2DARBPROC) (GLenum target, GLint level, GLint xoffset, GLint yoffset, GLsizei width, GLsizei height, GLenum format, GLsizei imageSize, const GLvoid * pData);
typedef void (APIENTRY *glCompressedTexSubImage3DARBPROC) (GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLenum format, GLsizei imageSize, const GLvoid * pData);
typedef void (APIENTRY *glGetCompressedTexImageARBPROC) (GLenum target, GLint lod, GLvoid * pImg);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureCompression_nglCompressedTexImage1DARB(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint border, jint imageSize, jlong pData, jlong function_pointer) {
	const GLvoid *pData_address = (const GLvoid *)(intptr_t)pData;
	glCompressedTexImage1DARBPROC glCompressedTexImage1DARB = (glCompressedTexImage1DARBPROC)((intptr_t)function_pointer);
	glCompressedTexImage1DARB(target, level, internalformat, width, border, imageSize, pData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureCompression_nglCompressedTexImage1DARBBO(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint border, jint imageSize, jlong pData_buffer_offset, jlong function_pointer) {
	const GLvoid *pData_address = (const GLvoid *)(intptr_t)offsetToPointer(pData_buffer_offset);
	glCompressedTexImage1DARBPROC glCompressedTexImage1DARB = (glCompressedTexImage1DARBPROC)((intptr_t)function_pointer);
	glCompressedTexImage1DARB(target, level, internalformat, width, border, imageSize, pData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureCompression_nglCompressedTexImage2DARB(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint imageSize, jlong pData, jlong function_pointer) {
	const GLvoid *pData_address = (const GLvoid *)(intptr_t)pData;
	glCompressedTexImage2DARBPROC glCompressedTexImage2DARB = (glCompressedTexImage2DARBPROC)((intptr_t)function_pointer);
	glCompressedTexImage2DARB(target, level, internalformat, width, height, border, imageSize, pData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureCompression_nglCompressedTexImage2DARBBO(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint imageSize, jlong pData_buffer_offset, jlong function_pointer) {
	const GLvoid *pData_address = (const GLvoid *)(intptr_t)offsetToPointer(pData_buffer_offset);
	glCompressedTexImage2DARBPROC glCompressedTexImage2DARB = (glCompressedTexImage2DARBPROC)((intptr_t)function_pointer);
	glCompressedTexImage2DARB(target, level, internalformat, width, height, border, imageSize, pData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureCompression_nglCompressedTexImage3DARB(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint height, jint depth, jint border, jint imageSize, jlong pData, jlong function_pointer) {
	const GLvoid *pData_address = (const GLvoid *)(intptr_t)pData;
	glCompressedTexImage3DARBPROC glCompressedTexImage3DARB = (glCompressedTexImage3DARBPROC)((intptr_t)function_pointer);
	glCompressedTexImage3DARB(target, level, internalformat, width, height, depth, border, imageSize, pData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureCompression_nglCompressedTexImage3DARBBO(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint height, jint depth, jint border, jint imageSize, jlong pData_buffer_offset, jlong function_pointer) {
	const GLvoid *pData_address = (const GLvoid *)(intptr_t)offsetToPointer(pData_buffer_offset);
	glCompressedTexImage3DARBPROC glCompressedTexImage3DARB = (glCompressedTexImage3DARBPROC)((intptr_t)function_pointer);
	glCompressedTexImage3DARB(target, level, internalformat, width, height, depth, border, imageSize, pData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureCompression_nglCompressedTexSubImage1DARB(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint width, jint format, jint imageSize, jlong pData, jlong function_pointer) {
	const GLvoid *pData_address = (const GLvoid *)(intptr_t)pData;
	glCompressedTexSubImage1DARBPROC glCompressedTexSubImage1DARB = (glCompressedTexSubImage1DARBPROC)((intptr_t)function_pointer);
	glCompressedTexSubImage1DARB(target, level, xoffset, width, format, imageSize, pData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureCompression_nglCompressedTexSubImage1DARBBO(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint width, jint format, jint imageSize, jlong pData_buffer_offset, jlong function_pointer) {
	const GLvoid *pData_address = (const GLvoid *)(intptr_t)offsetToPointer(pData_buffer_offset);
	glCompressedTexSubImage1DARBPROC glCompressedTexSubImage1DARB = (glCompressedTexSubImage1DARBPROC)((intptr_t)function_pointer);
	glCompressedTexSubImage1DARB(target, level, xoffset, width, format, imageSize, pData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureCompression_nglCompressedTexSubImage2DARB(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint imageSize, jlong pData, jlong function_pointer) {
	const GLvoid *pData_address = (const GLvoid *)(intptr_t)pData;
	glCompressedTexSubImage2DARBPROC glCompressedTexSubImage2DARB = (glCompressedTexSubImage2DARBPROC)((intptr_t)function_pointer);
	glCompressedTexSubImage2DARB(target, level, xoffset, yoffset, width, height, format, imageSize, pData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureCompression_nglCompressedTexSubImage2DARBBO(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint imageSize, jlong pData_buffer_offset, jlong function_pointer) {
	const GLvoid *pData_address = (const GLvoid *)(intptr_t)offsetToPointer(pData_buffer_offset);
	glCompressedTexSubImage2DARBPROC glCompressedTexSubImage2DARB = (glCompressedTexSubImage2DARBPROC)((intptr_t)function_pointer);
	glCompressedTexSubImage2DARB(target, level, xoffset, yoffset, width, height, format, imageSize, pData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureCompression_nglCompressedTexSubImage3DARB(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint imageSize, jlong pData, jlong function_pointer) {
	const GLvoid *pData_address = (const GLvoid *)(intptr_t)pData;
	glCompressedTexSubImage3DARBPROC glCompressedTexSubImage3DARB = (glCompressedTexSubImage3DARBPROC)((intptr_t)function_pointer);
	glCompressedTexSubImage3DARB(target, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, pData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureCompression_nglCompressedTexSubImage3DARBBO(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint imageSize, jlong pData_buffer_offset, jlong function_pointer) {
	const GLvoid *pData_address = (const GLvoid *)(intptr_t)offsetToPointer(pData_buffer_offset);
	glCompressedTexSubImage3DARBPROC glCompressedTexSubImage3DARB = (glCompressedTexSubImage3DARBPROC)((intptr_t)function_pointer);
	glCompressedTexSubImage3DARB(target, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, pData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureCompression_nglGetCompressedTexImageARB(JNIEnv *env, jclass clazz, jint target, jint lod, jlong pImg, jlong function_pointer) {
	GLvoid *pImg_address = (GLvoid *)(intptr_t)pImg;
	glGetCompressedTexImageARBPROC glGetCompressedTexImageARB = (glGetCompressedTexImageARBPROC)((intptr_t)function_pointer);
	glGetCompressedTexImageARB(target, lod, pImg_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureCompression_nglGetCompressedTexImageARBBO(JNIEnv *env, jclass clazz, jint target, jint lod, jlong pImg_buffer_offset, jlong function_pointer) {
	GLvoid *pImg_address = (GLvoid *)(intptr_t)offsetToPointer(pImg_buffer_offset);
	glGetCompressedTexImageARBPROC glGetCompressedTexImageARB = (glGetCompressedTexImageARBPROC)((intptr_t)function_pointer);
	glGetCompressedTexImageARB(target, lod, pImg_address);
}

