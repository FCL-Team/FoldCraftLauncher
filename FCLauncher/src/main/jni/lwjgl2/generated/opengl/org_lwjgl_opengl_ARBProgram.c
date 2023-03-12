/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glProgramStringARBPROC) (GLenum target, GLenum format, GLsizei length, const GLvoid * string);
typedef void (APIENTRY *glBindProgramARBPROC) (GLenum target, GLuint program);
typedef void (APIENTRY *glDeleteProgramsARBPROC) (GLsizei n, const GLuint * programs);
typedef void (APIENTRY *glGenProgramsARBPROC) (GLsizei n, GLuint * programs);
typedef void (APIENTRY *glProgramEnvParameter4fARBPROC) (GLint target, GLint index, GLfloat x, GLfloat y, GLfloat z, GLfloat w);
typedef void (APIENTRY *glProgramEnvParameter4dARBPROC) (GLint target, GLint index, GLdouble x, GLdouble y, GLdouble z, GLdouble w);
typedef void (APIENTRY *glProgramEnvParameter4fvARBPROC) (GLenum target, GLuint index, const GLfloat * params);
typedef void (APIENTRY *glProgramEnvParameter4dvARBPROC) (GLenum target, GLuint index, const GLdouble * params);
typedef void (APIENTRY *glProgramLocalParameter4fARBPROC) (GLenum target, GLuint index, GLfloat x, GLfloat y, GLfloat z, GLfloat w);
typedef void (APIENTRY *glProgramLocalParameter4dARBPROC) (GLenum target, GLuint index, GLdouble x, GLdouble y, GLdouble z, GLdouble w);
typedef void (APIENTRY *glProgramLocalParameter4fvARBPROC) (GLenum target, GLuint index, const GLfloat * params);
typedef void (APIENTRY *glProgramLocalParameter4dvARBPROC) (GLenum target, GLuint index, const GLdouble * params);
typedef void (APIENTRY *glGetProgramEnvParameterfvARBPROC) (GLenum target, GLuint index, GLfloat * params);
typedef void (APIENTRY *glGetProgramEnvParameterdvARBPROC) (GLenum target, GLuint index, GLdouble * params);
typedef void (APIENTRY *glGetProgramLocalParameterfvARBPROC) (GLenum target, GLuint index, GLfloat * params);
typedef void (APIENTRY *glGetProgramLocalParameterdvARBPROC) (GLenum target, GLuint index, GLdouble * params);
typedef void (APIENTRY *glGetProgramivARBPROC) (GLenum target, GLenum parameterName, GLint * params);
typedef void (APIENTRY *glGetProgramStringARBPROC) (GLenum target, GLenum parameterName, GLvoid * paramString);
typedef GLboolean (APIENTRY *glIsProgramARBPROC) (GLuint program);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglProgramStringARB(JNIEnv *env, jclass clazz, jint target, jint format, jint length, jlong string, jlong function_pointer) {
	const GLvoid *string_address = (const GLvoid *)(intptr_t)string;
	glProgramStringARBPROC glProgramStringARB = (glProgramStringARBPROC)((intptr_t)function_pointer);
	glProgramStringARB(target, format, length, string_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglBindProgramARB(JNIEnv *env, jclass clazz, jint target, jint program, jlong function_pointer) {
	glBindProgramARBPROC glBindProgramARB = (glBindProgramARBPROC)((intptr_t)function_pointer);
	glBindProgramARB(target, program);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglDeleteProgramsARB(JNIEnv *env, jclass clazz, jint n, jlong programs, jlong function_pointer) {
	const GLuint *programs_address = (const GLuint *)(intptr_t)programs;
	glDeleteProgramsARBPROC glDeleteProgramsARB = (glDeleteProgramsARBPROC)((intptr_t)function_pointer);
	glDeleteProgramsARB(n, programs_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglGenProgramsARB(JNIEnv *env, jclass clazz, jint n, jlong programs, jlong function_pointer) {
	GLuint *programs_address = (GLuint *)(intptr_t)programs;
	glGenProgramsARBPROC glGenProgramsARB = (glGenProgramsARBPROC)((intptr_t)function_pointer);
	glGenProgramsARB(n, programs_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglProgramEnvParameter4fARB(JNIEnv *env, jclass clazz, jint target, jint index, jfloat x, jfloat y, jfloat z, jfloat w, jlong function_pointer) {
	glProgramEnvParameter4fARBPROC glProgramEnvParameter4fARB = (glProgramEnvParameter4fARBPROC)((intptr_t)function_pointer);
	glProgramEnvParameter4fARB(target, index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglProgramEnvParameter4dARB(JNIEnv *env, jclass clazz, jint target, jint index, jdouble x, jdouble y, jdouble z, jdouble w, jlong function_pointer) {
	glProgramEnvParameter4dARBPROC glProgramEnvParameter4dARB = (glProgramEnvParameter4dARBPROC)((intptr_t)function_pointer);
	glProgramEnvParameter4dARB(target, index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglProgramEnvParameter4fvARB(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glProgramEnvParameter4fvARBPROC glProgramEnvParameter4fvARB = (glProgramEnvParameter4fvARBPROC)((intptr_t)function_pointer);
	glProgramEnvParameter4fvARB(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglProgramEnvParameter4dvARB(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	const GLdouble *params_address = (const GLdouble *)(intptr_t)params;
	glProgramEnvParameter4dvARBPROC glProgramEnvParameter4dvARB = (glProgramEnvParameter4dvARBPROC)((intptr_t)function_pointer);
	glProgramEnvParameter4dvARB(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglProgramLocalParameter4fARB(JNIEnv *env, jclass clazz, jint target, jint index, jfloat x, jfloat y, jfloat z, jfloat w, jlong function_pointer) {
	glProgramLocalParameter4fARBPROC glProgramLocalParameter4fARB = (glProgramLocalParameter4fARBPROC)((intptr_t)function_pointer);
	glProgramLocalParameter4fARB(target, index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglProgramLocalParameter4dARB(JNIEnv *env, jclass clazz, jint target, jint index, jdouble x, jdouble y, jdouble z, jdouble w, jlong function_pointer) {
	glProgramLocalParameter4dARBPROC glProgramLocalParameter4dARB = (glProgramLocalParameter4dARBPROC)((intptr_t)function_pointer);
	glProgramLocalParameter4dARB(target, index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglProgramLocalParameter4fvARB(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glProgramLocalParameter4fvARBPROC glProgramLocalParameter4fvARB = (glProgramLocalParameter4fvARBPROC)((intptr_t)function_pointer);
	glProgramLocalParameter4fvARB(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglProgramLocalParameter4dvARB(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	const GLdouble *params_address = (const GLdouble *)(intptr_t)params;
	glProgramLocalParameter4dvARBPROC glProgramLocalParameter4dvARB = (glProgramLocalParameter4dvARBPROC)((intptr_t)function_pointer);
	glProgramLocalParameter4dvARB(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglGetProgramEnvParameterfvARB(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetProgramEnvParameterfvARBPROC glGetProgramEnvParameterfvARB = (glGetProgramEnvParameterfvARBPROC)((intptr_t)function_pointer);
	glGetProgramEnvParameterfvARB(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglGetProgramEnvParameterdvARB(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	GLdouble *params_address = (GLdouble *)(intptr_t)params;
	glGetProgramEnvParameterdvARBPROC glGetProgramEnvParameterdvARB = (glGetProgramEnvParameterdvARBPROC)((intptr_t)function_pointer);
	glGetProgramEnvParameterdvARB(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglGetProgramLocalParameterfvARB(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetProgramLocalParameterfvARBPROC glGetProgramLocalParameterfvARB = (glGetProgramLocalParameterfvARBPROC)((intptr_t)function_pointer);
	glGetProgramLocalParameterfvARB(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglGetProgramLocalParameterdvARB(JNIEnv *env, jclass clazz, jint target, jint index, jlong params, jlong function_pointer) {
	GLdouble *params_address = (GLdouble *)(intptr_t)params;
	glGetProgramLocalParameterdvARBPROC glGetProgramLocalParameterdvARB = (glGetProgramLocalParameterdvARBPROC)((intptr_t)function_pointer);
	glGetProgramLocalParameterdvARB(target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglGetProgramivARB(JNIEnv *env, jclass clazz, jint target, jint parameterName, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetProgramivARBPROC glGetProgramivARB = (glGetProgramivARBPROC)((intptr_t)function_pointer);
	glGetProgramivARB(target, parameterName, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBProgram_nglGetProgramStringARB(JNIEnv *env, jclass clazz, jint target, jint parameterName, jlong paramString, jlong function_pointer) {
	GLvoid *paramString_address = (GLvoid *)(intptr_t)paramString;
	glGetProgramStringARBPROC glGetProgramStringARB = (glGetProgramStringARBPROC)((intptr_t)function_pointer);
	glGetProgramStringARB(target, parameterName, paramString_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_ARBProgram_nglIsProgramARB(JNIEnv *env, jclass clazz, jint program, jlong function_pointer) {
	glIsProgramARBPROC glIsProgramARB = (glIsProgramARBPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsProgramARB(program);
	return __result;
}

