/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glVertexStream2fATIPROC) (GLenum stream, GLfloat x, GLfloat y);
typedef void (APIENTRY *glVertexStream2dATIPROC) (GLenum stream, GLdouble x, GLdouble y);
typedef void (APIENTRY *glVertexStream2iATIPROC) (GLenum stream, GLint x, GLint y);
typedef void (APIENTRY *glVertexStream2sATIPROC) (GLenum stream, GLshort x, GLshort y);
typedef void (APIENTRY *glVertexStream3fATIPROC) (GLenum stream, GLfloat x, GLfloat y, GLfloat z);
typedef void (APIENTRY *glVertexStream3dATIPROC) (GLenum stream, GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glVertexStream3iATIPROC) (GLenum stream, GLint x, GLint y, GLint z);
typedef void (APIENTRY *glVertexStream3sATIPROC) (GLenum stream, GLshort x, GLshort y, GLshort z);
typedef void (APIENTRY *glVertexStream4fATIPROC) (GLenum stream, GLfloat x, GLfloat y, GLfloat z, GLfloat w);
typedef void (APIENTRY *glVertexStream4dATIPROC) (GLenum stream, GLdouble x, GLdouble y, GLdouble z, GLdouble w);
typedef void (APIENTRY *glVertexStream4iATIPROC) (GLenum stream, GLint x, GLint y, GLint z, GLint w);
typedef void (APIENTRY *glVertexStream4sATIPROC) (GLenum stream, GLshort x, GLshort y, GLshort z, GLshort w);
typedef void (APIENTRY *glNormalStream3bATIPROC) (GLenum stream, GLbyte x, GLbyte y, GLbyte z);
typedef void (APIENTRY *glNormalStream3fATIPROC) (GLenum stream, GLfloat x, GLfloat y, GLfloat z);
typedef void (APIENTRY *glNormalStream3dATIPROC) (GLenum stream, GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glNormalStream3iATIPROC) (GLenum stream, GLint x, GLint y, GLint z);
typedef void (APIENTRY *glNormalStream3sATIPROC) (GLenum stream, GLshort x, GLshort y, GLshort z);
typedef void (APIENTRY *glClientActiveVertexStreamATIPROC) (GLenum stream);
typedef void (APIENTRY *glVertexBlendEnvfATIPROC) (GLenum pname, GLfloat param);
typedef void (APIENTRY *glVertexBlendEnviATIPROC) (GLenum pname, GLint param);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglVertexStream2fATI(JNIEnv *env, jclass clazz, jint stream, jfloat x, jfloat y, jlong function_pointer) {
	glVertexStream2fATIPROC glVertexStream2fATI = (glVertexStream2fATIPROC)((intptr_t)function_pointer);
	glVertexStream2fATI(stream, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglVertexStream2dATI(JNIEnv *env, jclass clazz, jint stream, jdouble x, jdouble y, jlong function_pointer) {
	glVertexStream2dATIPROC glVertexStream2dATI = (glVertexStream2dATIPROC)((intptr_t)function_pointer);
	glVertexStream2dATI(stream, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglVertexStream2iATI(JNIEnv *env, jclass clazz, jint stream, jint x, jint y, jlong function_pointer) {
	glVertexStream2iATIPROC glVertexStream2iATI = (glVertexStream2iATIPROC)((intptr_t)function_pointer);
	glVertexStream2iATI(stream, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglVertexStream2sATI(JNIEnv *env, jclass clazz, jint stream, jshort x, jshort y, jlong function_pointer) {
	glVertexStream2sATIPROC glVertexStream2sATI = (glVertexStream2sATIPROC)((intptr_t)function_pointer);
	glVertexStream2sATI(stream, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglVertexStream3fATI(JNIEnv *env, jclass clazz, jint stream, jfloat x, jfloat y, jfloat z, jlong function_pointer) {
	glVertexStream3fATIPROC glVertexStream3fATI = (glVertexStream3fATIPROC)((intptr_t)function_pointer);
	glVertexStream3fATI(stream, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglVertexStream3dATI(JNIEnv *env, jclass clazz, jint stream, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glVertexStream3dATIPROC glVertexStream3dATI = (glVertexStream3dATIPROC)((intptr_t)function_pointer);
	glVertexStream3dATI(stream, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglVertexStream3iATI(JNIEnv *env, jclass clazz, jint stream, jint x, jint y, jint z, jlong function_pointer) {
	glVertexStream3iATIPROC glVertexStream3iATI = (glVertexStream3iATIPROC)((intptr_t)function_pointer);
	glVertexStream3iATI(stream, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglVertexStream3sATI(JNIEnv *env, jclass clazz, jint stream, jshort x, jshort y, jshort z, jlong function_pointer) {
	glVertexStream3sATIPROC glVertexStream3sATI = (glVertexStream3sATIPROC)((intptr_t)function_pointer);
	glVertexStream3sATI(stream, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglVertexStream4fATI(JNIEnv *env, jclass clazz, jint stream, jfloat x, jfloat y, jfloat z, jfloat w, jlong function_pointer) {
	glVertexStream4fATIPROC glVertexStream4fATI = (glVertexStream4fATIPROC)((intptr_t)function_pointer);
	glVertexStream4fATI(stream, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglVertexStream4dATI(JNIEnv *env, jclass clazz, jint stream, jdouble x, jdouble y, jdouble z, jdouble w, jlong function_pointer) {
	glVertexStream4dATIPROC glVertexStream4dATI = (glVertexStream4dATIPROC)((intptr_t)function_pointer);
	glVertexStream4dATI(stream, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglVertexStream4iATI(JNIEnv *env, jclass clazz, jint stream, jint x, jint y, jint z, jint w, jlong function_pointer) {
	glVertexStream4iATIPROC glVertexStream4iATI = (glVertexStream4iATIPROC)((intptr_t)function_pointer);
	glVertexStream4iATI(stream, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglVertexStream4sATI(JNIEnv *env, jclass clazz, jint stream, jshort x, jshort y, jshort z, jshort w, jlong function_pointer) {
	glVertexStream4sATIPROC glVertexStream4sATI = (glVertexStream4sATIPROC)((intptr_t)function_pointer);
	glVertexStream4sATI(stream, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglNormalStream3bATI(JNIEnv *env, jclass clazz, jint stream, jbyte x, jbyte y, jbyte z, jlong function_pointer) {
	glNormalStream3bATIPROC glNormalStream3bATI = (glNormalStream3bATIPROC)((intptr_t)function_pointer);
	glNormalStream3bATI(stream, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglNormalStream3fATI(JNIEnv *env, jclass clazz, jint stream, jfloat x, jfloat y, jfloat z, jlong function_pointer) {
	glNormalStream3fATIPROC glNormalStream3fATI = (glNormalStream3fATIPROC)((intptr_t)function_pointer);
	glNormalStream3fATI(stream, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglNormalStream3dATI(JNIEnv *env, jclass clazz, jint stream, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glNormalStream3dATIPROC glNormalStream3dATI = (glNormalStream3dATIPROC)((intptr_t)function_pointer);
	glNormalStream3dATI(stream, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglNormalStream3iATI(JNIEnv *env, jclass clazz, jint stream, jint x, jint y, jint z, jlong function_pointer) {
	glNormalStream3iATIPROC glNormalStream3iATI = (glNormalStream3iATIPROC)((intptr_t)function_pointer);
	glNormalStream3iATI(stream, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglNormalStream3sATI(JNIEnv *env, jclass clazz, jint stream, jshort x, jshort y, jshort z, jlong function_pointer) {
	glNormalStream3sATIPROC glNormalStream3sATI = (glNormalStream3sATIPROC)((intptr_t)function_pointer);
	glNormalStream3sATI(stream, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglClientActiveVertexStreamATI(JNIEnv *env, jclass clazz, jint stream, jlong function_pointer) {
	glClientActiveVertexStreamATIPROC glClientActiveVertexStreamATI = (glClientActiveVertexStreamATIPROC)((intptr_t)function_pointer);
	glClientActiveVertexStreamATI(stream);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglVertexBlendEnvfATI(JNIEnv *env, jclass clazz, jint pname, jfloat param, jlong function_pointer) {
	glVertexBlendEnvfATIPROC glVertexBlendEnvfATI = (glVertexBlendEnvfATIPROC)((intptr_t)function_pointer);
	glVertexBlendEnvfATI(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexStreams_nglVertexBlendEnviATI(JNIEnv *env, jclass clazz, jint pname, jint param, jlong function_pointer) {
	glVertexBlendEnviATIPROC glVertexBlendEnviATI = (glVertexBlendEnviATIPROC)((intptr_t)function_pointer);
	glVertexBlendEnviATI(pname, param);
}

