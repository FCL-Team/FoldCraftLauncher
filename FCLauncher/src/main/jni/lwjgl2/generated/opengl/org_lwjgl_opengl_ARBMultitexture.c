/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glClientActiveTextureARBPROC) (GLenum texture);
typedef void (APIENTRY *glActiveTextureARBPROC) (GLenum texture);
typedef void (APIENTRY *glMultiTexCoord1fARBPROC) (GLenum target, GLfloat s);
typedef void (APIENTRY *glMultiTexCoord1dARBPROC) (GLenum target, GLdouble s);
typedef void (APIENTRY *glMultiTexCoord1iARBPROC) (GLenum target, GLint s);
typedef void (APIENTRY *glMultiTexCoord1sARBPROC) (GLenum target, GLshort s);
typedef void (APIENTRY *glMultiTexCoord2fARBPROC) (GLenum target, GLfloat s, GLfloat t);
typedef void (APIENTRY *glMultiTexCoord2dARBPROC) (GLenum target, GLdouble s, GLdouble t);
typedef void (APIENTRY *glMultiTexCoord2iARBPROC) (GLenum target, GLint s, GLint t);
typedef void (APIENTRY *glMultiTexCoord2sARBPROC) (GLenum target, GLshort s, GLshort t);
typedef void (APIENTRY *glMultiTexCoord3fARBPROC) (GLenum target, GLfloat s, GLfloat t, GLfloat r);
typedef void (APIENTRY *glMultiTexCoord3dARBPROC) (GLenum target, GLdouble s, GLdouble t, GLdouble r);
typedef void (APIENTRY *glMultiTexCoord3iARBPROC) (GLenum target, GLint s, GLint t, GLint r);
typedef void (APIENTRY *glMultiTexCoord3sARBPROC) (GLenum target, GLshort s, GLshort t, GLshort r);
typedef void (APIENTRY *glMultiTexCoord4fARBPROC) (GLenum target, GLfloat s, GLfloat t, GLfloat r, GLfloat q);
typedef void (APIENTRY *glMultiTexCoord4dARBPROC) (GLenum target, GLdouble s, GLdouble t, GLdouble r, GLdouble q);
typedef void (APIENTRY *glMultiTexCoord4iARBPROC) (GLenum target, GLint s, GLint t, GLint r, GLint q);
typedef void (APIENTRY *glMultiTexCoord4sARBPROC) (GLenum target, GLshort s, GLshort t, GLshort r, GLshort q);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglClientActiveTextureARB(JNIEnv *env, jclass clazz, jint texture, jlong function_pointer) {
	glClientActiveTextureARBPROC glClientActiveTextureARB = (glClientActiveTextureARBPROC)((intptr_t)function_pointer);
	glClientActiveTextureARB(texture);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglActiveTextureARB(JNIEnv *env, jclass clazz, jint texture, jlong function_pointer) {
	glActiveTextureARBPROC glActiveTextureARB = (glActiveTextureARBPROC)((intptr_t)function_pointer);
	glActiveTextureARB(texture);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord1fARB(JNIEnv *env, jclass clazz, jint target, jfloat s, jlong function_pointer) {
	glMultiTexCoord1fARBPROC glMultiTexCoord1fARB = (glMultiTexCoord1fARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord1fARB(target, s);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord1dARB(JNIEnv *env, jclass clazz, jint target, jdouble s, jlong function_pointer) {
	glMultiTexCoord1dARBPROC glMultiTexCoord1dARB = (glMultiTexCoord1dARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord1dARB(target, s);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord1iARB(JNIEnv *env, jclass clazz, jint target, jint s, jlong function_pointer) {
	glMultiTexCoord1iARBPROC glMultiTexCoord1iARB = (glMultiTexCoord1iARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord1iARB(target, s);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord1sARB(JNIEnv *env, jclass clazz, jint target, jshort s, jlong function_pointer) {
	glMultiTexCoord1sARBPROC glMultiTexCoord1sARB = (glMultiTexCoord1sARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord1sARB(target, s);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord2fARB(JNIEnv *env, jclass clazz, jint target, jfloat s, jfloat t, jlong function_pointer) {
	glMultiTexCoord2fARBPROC glMultiTexCoord2fARB = (glMultiTexCoord2fARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord2fARB(target, s, t);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord2dARB(JNIEnv *env, jclass clazz, jint target, jdouble s, jdouble t, jlong function_pointer) {
	glMultiTexCoord2dARBPROC glMultiTexCoord2dARB = (glMultiTexCoord2dARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord2dARB(target, s, t);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord2iARB(JNIEnv *env, jclass clazz, jint target, jint s, jint t, jlong function_pointer) {
	glMultiTexCoord2iARBPROC glMultiTexCoord2iARB = (glMultiTexCoord2iARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord2iARB(target, s, t);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord2sARB(JNIEnv *env, jclass clazz, jint target, jshort s, jshort t, jlong function_pointer) {
	glMultiTexCoord2sARBPROC glMultiTexCoord2sARB = (glMultiTexCoord2sARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord2sARB(target, s, t);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord3fARB(JNIEnv *env, jclass clazz, jint target, jfloat s, jfloat t, jfloat r, jlong function_pointer) {
	glMultiTexCoord3fARBPROC glMultiTexCoord3fARB = (glMultiTexCoord3fARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord3fARB(target, s, t, r);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord3dARB(JNIEnv *env, jclass clazz, jint target, jdouble s, jdouble t, jdouble r, jlong function_pointer) {
	glMultiTexCoord3dARBPROC glMultiTexCoord3dARB = (glMultiTexCoord3dARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord3dARB(target, s, t, r);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord3iARB(JNIEnv *env, jclass clazz, jint target, jint s, jint t, jint r, jlong function_pointer) {
	glMultiTexCoord3iARBPROC glMultiTexCoord3iARB = (glMultiTexCoord3iARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord3iARB(target, s, t, r);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord3sARB(JNIEnv *env, jclass clazz, jint target, jshort s, jshort t, jshort r, jlong function_pointer) {
	glMultiTexCoord3sARBPROC glMultiTexCoord3sARB = (glMultiTexCoord3sARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord3sARB(target, s, t, r);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord4fARB(JNIEnv *env, jclass clazz, jint target, jfloat s, jfloat t, jfloat r, jfloat q, jlong function_pointer) {
	glMultiTexCoord4fARBPROC glMultiTexCoord4fARB = (glMultiTexCoord4fARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord4fARB(target, s, t, r, q);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord4dARB(JNIEnv *env, jclass clazz, jint target, jdouble s, jdouble t, jdouble r, jdouble q, jlong function_pointer) {
	glMultiTexCoord4dARBPROC glMultiTexCoord4dARB = (glMultiTexCoord4dARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord4dARB(target, s, t, r, q);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord4iARB(JNIEnv *env, jclass clazz, jint target, jint s, jint t, jint r, jint q, jlong function_pointer) {
	glMultiTexCoord4iARBPROC glMultiTexCoord4iARB = (glMultiTexCoord4iARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord4iARB(target, s, t, r, q);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultitexture_nglMultiTexCoord4sARB(JNIEnv *env, jclass clazz, jint target, jshort s, jshort t, jshort r, jshort q, jlong function_pointer) {
	glMultiTexCoord4sARBPROC glMultiTexCoord4sARB = (glMultiTexCoord4sARBPROC)((intptr_t)function_pointer);
	glMultiTexCoord4sARB(target, s, t, r, q);
}

