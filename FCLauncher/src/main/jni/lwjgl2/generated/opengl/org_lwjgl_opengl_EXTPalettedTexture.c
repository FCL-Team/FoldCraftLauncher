/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glColorTableEXTPROC) (GLenum target, GLenum internalFormat, GLsizei width, GLenum format, GLenum type, const GLvoid * data);
typedef void (APIENTRY *glColorSubTableEXTPROC) (GLenum target, GLsizei start, GLsizei count, GLenum format, GLenum type, const GLvoid * data);
typedef void (APIENTRY *glGetColorTableEXTPROC) (GLenum target, GLenum format, GLenum type, GLvoid * data);
typedef void (APIENTRY *glGetColorTableParameterivEXTPROC) (GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetColorTableParameterfvEXTPROC) (GLenum target, GLenum pname, GLfloat * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTPalettedTexture_nglColorTableEXT(JNIEnv *env, jclass clazz, jint target, jint internalFormat, jint width, jint format, jint type, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glColorTableEXTPROC glColorTableEXT = (glColorTableEXTPROC)((intptr_t)function_pointer);
	glColorTableEXT(target, internalFormat, width, format, type, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTPalettedTexture_nglColorSubTableEXT(JNIEnv *env, jclass clazz, jint target, jint start, jint count, jint format, jint type, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glColorSubTableEXTPROC glColorSubTableEXT = (glColorSubTableEXTPROC)((intptr_t)function_pointer);
	glColorSubTableEXT(target, start, count, format, type, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTPalettedTexture_nglGetColorTableEXT(JNIEnv *env, jclass clazz, jint target, jint format, jint type, jlong data, jlong function_pointer) {
	GLvoid *data_address = (GLvoid *)(intptr_t)data;
	glGetColorTableEXTPROC glGetColorTableEXT = (glGetColorTableEXTPROC)((intptr_t)function_pointer);
	glGetColorTableEXT(target, format, type, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTPalettedTexture_nglGetColorTableParameterivEXT(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetColorTableParameterivEXTPROC glGetColorTableParameterivEXT = (glGetColorTableParameterivEXTPROC)((intptr_t)function_pointer);
	glGetColorTableParameterivEXT(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTPalettedTexture_nglGetColorTableParameterfvEXT(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetColorTableParameterfvEXTPROC glGetColorTableParameterfvEXT = (glGetColorTableParameterfvEXTPROC)((intptr_t)function_pointer);
	glGetColorTableParameterfvEXT(target, pname, params_address);
}

