/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glClearColorIiEXTPROC) (GLint r, GLint g, GLint b, GLint a);
typedef void (APIENTRY *glClearColorIuiEXTPROC) (GLuint r, GLuint g, GLuint b, GLuint a);
typedef void (APIENTRY *glTexParameterIivEXTPROC) (GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glTexParameterIuivEXTPROC) (GLenum target, GLenum pname, GLuint * params);
typedef void (APIENTRY *glGetTexParameterIivEXTPROC) (GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetTexParameterIuivEXTPROC) (GLenum target, GLenum pname, GLuint * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTextureInteger_nglClearColorIiEXT(JNIEnv *env, jclass clazz, jint r, jint g, jint b, jint a, jlong function_pointer) {
	glClearColorIiEXTPROC glClearColorIiEXT = (glClearColorIiEXTPROC)((intptr_t)function_pointer);
	glClearColorIiEXT(r, g, b, a);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTextureInteger_nglClearColorIuiEXT(JNIEnv *env, jclass clazz, jint r, jint g, jint b, jint a, jlong function_pointer) {
	glClearColorIuiEXTPROC glClearColorIuiEXT = (glClearColorIuiEXTPROC)((intptr_t)function_pointer);
	glClearColorIuiEXT(r, g, b, a);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTextureInteger_nglTexParameterIivEXT(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glTexParameterIivEXTPROC glTexParameterIivEXT = (glTexParameterIivEXTPROC)((intptr_t)function_pointer);
	glTexParameterIivEXT(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTextureInteger_nglTexParameterIuivEXT(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glTexParameterIuivEXTPROC glTexParameterIuivEXT = (glTexParameterIuivEXTPROC)((intptr_t)function_pointer);
	glTexParameterIuivEXT(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTextureInteger_nglGetTexParameterIivEXT(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetTexParameterIivEXTPROC glGetTexParameterIivEXT = (glGetTexParameterIivEXTPROC)((intptr_t)function_pointer);
	glGetTexParameterIivEXT(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTextureInteger_nglGetTexParameterIuivEXT(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetTexParameterIuivEXTPROC glGetTexParameterIuivEXT = (glGetTexParameterIuivEXTPROC)((intptr_t)function_pointer);
	glGetTexParameterIuivEXT(target, pname, params_address);
}

