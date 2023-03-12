/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glActiveTexturePROC) (GLenum texture);
typedef void (APIENTRY *glClientActiveTexturePROC) (GLenum texture);
typedef void (APIENTRY *glCompressedTexImage1DPROC) (GLenum target, GLint level, GLenum internalformat, GLsizei width, GLint border, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedTexImage2DPROC) (GLenum target, GLint level, GLenum internalformat, GLsizei width, GLsizei height, GLint border, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedTexImage3DPROC) (GLenum target, GLint level, GLenum internalformat, GLsizei width, GLsizei height, GLsizei depth, GLint border, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedTexSubImage1DPROC) (GLenum target, GLint level, GLint xoffset, GLsizei width, GLenum format, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedTexSubImage2DPROC) (GLenum target, GLint level, GLint xoffset, GLint yoffset, GLsizei width, GLsizei height, GLenum format, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedTexSubImage3DPROC) (GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLenum format, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glGetCompressedTexImagePROC) (GLenum target, GLint lod, GLvoid * img);
typedef void (APIENTRY *glMultiTexCoord1fPROC) (GLenum target, GLfloat s);
typedef void (APIENTRY *glMultiTexCoord1dPROC) (GLenum target, GLdouble s);
typedef void (APIENTRY *glMultiTexCoord2fPROC) (GLenum target, GLfloat s, GLfloat t);
typedef void (APIENTRY *glMultiTexCoord2dPROC) (GLenum target, GLdouble s, GLdouble t);
typedef void (APIENTRY *glMultiTexCoord3fPROC) (GLenum target, GLfloat s, GLfloat t, GLfloat r);
typedef void (APIENTRY *glMultiTexCoord3dPROC) (GLenum target, GLdouble s, GLdouble t, GLdouble r);
typedef void (APIENTRY *glMultiTexCoord4fPROC) (GLenum target, GLfloat s, GLfloat t, GLfloat r, GLfloat q);
typedef void (APIENTRY *glMultiTexCoord4dPROC) (GLenum target, GLdouble s, GLdouble t, GLdouble r, GLdouble q);
typedef void (APIENTRY *glLoadTransposeMatrixfPROC) (const GLfloat * m);
typedef void (APIENTRY *glLoadTransposeMatrixdPROC) (const GLdouble * m);
typedef void (APIENTRY *glMultTransposeMatrixfPROC) (const GLfloat * m);
typedef void (APIENTRY *glMultTransposeMatrixdPROC) (const GLdouble * m);
typedef void (APIENTRY *glSampleCoveragePROC) (GLfloat value, GLboolean invert);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglActiveTexture(JNIEnv *env, jclass clazz, jint texture, jlong function_pointer) {
	glActiveTexturePROC glActiveTexture = (glActiveTexturePROC)((intptr_t)function_pointer);
	glActiveTexture(texture);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglClientActiveTexture(JNIEnv *env, jclass clazz, jint texture, jlong function_pointer) {
	glClientActiveTexturePROC glClientActiveTexture = (glClientActiveTexturePROC)((intptr_t)function_pointer);
	glClientActiveTexture(texture);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglCompressedTexImage1D(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint border, jint imageSize, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glCompressedTexImage1DPROC glCompressedTexImage1D = (glCompressedTexImage1DPROC)((intptr_t)function_pointer);
	glCompressedTexImage1D(target, level, internalformat, width, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglCompressedTexImage1DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint border, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glCompressedTexImage1DPROC glCompressedTexImage1D = (glCompressedTexImage1DPROC)((intptr_t)function_pointer);
	glCompressedTexImage1D(target, level, internalformat, width, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglCompressedTexImage2D(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint imageSize, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glCompressedTexImage2DPROC glCompressedTexImage2D = (glCompressedTexImage2DPROC)((intptr_t)function_pointer);
	glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglCompressedTexImage2DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glCompressedTexImage2DPROC glCompressedTexImage2D = (glCompressedTexImage2DPROC)((intptr_t)function_pointer);
	glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglCompressedTexImage3D(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint height, jint depth, jint border, jint imageSize, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glCompressedTexImage3DPROC glCompressedTexImage3D = (glCompressedTexImage3DPROC)((intptr_t)function_pointer);
	glCompressedTexImage3D(target, level, internalformat, width, height, depth, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglCompressedTexImage3DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint height, jint depth, jint border, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glCompressedTexImage3DPROC glCompressedTexImage3D = (glCompressedTexImage3DPROC)((intptr_t)function_pointer);
	glCompressedTexImage3D(target, level, internalformat, width, height, depth, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglCompressedTexSubImage1D(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint width, jint format, jint imageSize, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glCompressedTexSubImage1DPROC glCompressedTexSubImage1D = (glCompressedTexSubImage1DPROC)((intptr_t)function_pointer);
	glCompressedTexSubImage1D(target, level, xoffset, width, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglCompressedTexSubImage1DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint width, jint format, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glCompressedTexSubImage1DPROC glCompressedTexSubImage1D = (glCompressedTexSubImage1DPROC)((intptr_t)function_pointer);
	glCompressedTexSubImage1D(target, level, xoffset, width, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglCompressedTexSubImage2D(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint imageSize, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glCompressedTexSubImage2DPROC glCompressedTexSubImage2D = (glCompressedTexSubImage2DPROC)((intptr_t)function_pointer);
	glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglCompressedTexSubImage2DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glCompressedTexSubImage2DPROC glCompressedTexSubImage2D = (glCompressedTexSubImage2DPROC)((intptr_t)function_pointer);
	glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglCompressedTexSubImage3D(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint imageSize, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glCompressedTexSubImage3DPROC glCompressedTexSubImage3D = (glCompressedTexSubImage3DPROC)((intptr_t)function_pointer);
	glCompressedTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglCompressedTexSubImage3DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glCompressedTexSubImage3DPROC glCompressedTexSubImage3D = (glCompressedTexSubImage3DPROC)((intptr_t)function_pointer);
	glCompressedTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglGetCompressedTexImage(JNIEnv *env, jclass clazz, jint target, jint lod, jlong img, jlong function_pointer) {
	GLvoid *img_address = (GLvoid *)(intptr_t)img;
	glGetCompressedTexImagePROC glGetCompressedTexImage = (glGetCompressedTexImagePROC)((intptr_t)function_pointer);
	glGetCompressedTexImage(target, lod, img_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglGetCompressedTexImageBO(JNIEnv *env, jclass clazz, jint target, jint lod, jlong img_buffer_offset, jlong function_pointer) {
	GLvoid *img_address = (GLvoid *)(intptr_t)offsetToPointer(img_buffer_offset);
	glGetCompressedTexImagePROC glGetCompressedTexImage = (glGetCompressedTexImagePROC)((intptr_t)function_pointer);
	glGetCompressedTexImage(target, lod, img_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglMultiTexCoord1f(JNIEnv *env, jclass clazz, jint target, jfloat s, jlong function_pointer) {
	glMultiTexCoord1fPROC glMultiTexCoord1f = (glMultiTexCoord1fPROC)((intptr_t)function_pointer);
	glMultiTexCoord1f(target, s);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglMultiTexCoord1d(JNIEnv *env, jclass clazz, jint target, jdouble s, jlong function_pointer) {
	glMultiTexCoord1dPROC glMultiTexCoord1d = (glMultiTexCoord1dPROC)((intptr_t)function_pointer);
	glMultiTexCoord1d(target, s);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglMultiTexCoord2f(JNIEnv *env, jclass clazz, jint target, jfloat s, jfloat t, jlong function_pointer) {
	glMultiTexCoord2fPROC glMultiTexCoord2f = (glMultiTexCoord2fPROC)((intptr_t)function_pointer);
	glMultiTexCoord2f(target, s, t);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglMultiTexCoord2d(JNIEnv *env, jclass clazz, jint target, jdouble s, jdouble t, jlong function_pointer) {
	glMultiTexCoord2dPROC glMultiTexCoord2d = (glMultiTexCoord2dPROC)((intptr_t)function_pointer);
	glMultiTexCoord2d(target, s, t);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglMultiTexCoord3f(JNIEnv *env, jclass clazz, jint target, jfloat s, jfloat t, jfloat r, jlong function_pointer) {
	glMultiTexCoord3fPROC glMultiTexCoord3f = (glMultiTexCoord3fPROC)((intptr_t)function_pointer);
	glMultiTexCoord3f(target, s, t, r);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglMultiTexCoord3d(JNIEnv *env, jclass clazz, jint target, jdouble s, jdouble t, jdouble r, jlong function_pointer) {
	glMultiTexCoord3dPROC glMultiTexCoord3d = (glMultiTexCoord3dPROC)((intptr_t)function_pointer);
	glMultiTexCoord3d(target, s, t, r);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglMultiTexCoord4f(JNIEnv *env, jclass clazz, jint target, jfloat s, jfloat t, jfloat r, jfloat q, jlong function_pointer) {
	glMultiTexCoord4fPROC glMultiTexCoord4f = (glMultiTexCoord4fPROC)((intptr_t)function_pointer);
	glMultiTexCoord4f(target, s, t, r, q);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglMultiTexCoord4d(JNIEnv *env, jclass clazz, jint target, jdouble s, jdouble t, jdouble r, jdouble q, jlong function_pointer) {
	glMultiTexCoord4dPROC glMultiTexCoord4d = (glMultiTexCoord4dPROC)((intptr_t)function_pointer);
	glMultiTexCoord4d(target, s, t, r, q);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglLoadTransposeMatrixf(JNIEnv *env, jclass clazz, jlong m, jlong function_pointer) {
	const GLfloat *m_address = (const GLfloat *)(intptr_t)m;
	glLoadTransposeMatrixfPROC glLoadTransposeMatrixf = (glLoadTransposeMatrixfPROC)((intptr_t)function_pointer);
	glLoadTransposeMatrixf(m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglLoadTransposeMatrixd(JNIEnv *env, jclass clazz, jlong m, jlong function_pointer) {
	const GLdouble *m_address = (const GLdouble *)(intptr_t)m;
	glLoadTransposeMatrixdPROC glLoadTransposeMatrixd = (glLoadTransposeMatrixdPROC)((intptr_t)function_pointer);
	glLoadTransposeMatrixd(m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglMultTransposeMatrixf(JNIEnv *env, jclass clazz, jlong m, jlong function_pointer) {
	const GLfloat *m_address = (const GLfloat *)(intptr_t)m;
	glMultTransposeMatrixfPROC glMultTransposeMatrixf = (glMultTransposeMatrixfPROC)((intptr_t)function_pointer);
	glMultTransposeMatrixf(m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglMultTransposeMatrixd(JNIEnv *env, jclass clazz, jlong m, jlong function_pointer) {
	const GLdouble *m_address = (const GLdouble *)(intptr_t)m;
	glMultTransposeMatrixdPROC glMultTransposeMatrixd = (glMultTransposeMatrixdPROC)((intptr_t)function_pointer);
	glMultTransposeMatrixd(m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL13_nglSampleCoverage(JNIEnv *env, jclass clazz, jfloat value, jboolean invert, jlong function_pointer) {
	glSampleCoveragePROC glSampleCoverage = (glSampleCoveragePROC)((intptr_t)function_pointer);
	glSampleCoverage(value, invert);
}

