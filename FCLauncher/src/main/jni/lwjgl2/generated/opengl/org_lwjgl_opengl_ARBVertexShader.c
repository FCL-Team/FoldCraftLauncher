/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glVertexAttrib1sARBPROC) (GLuint index, GLshort v0);
typedef void (APIENTRY *glVertexAttrib1fARBPROC) (GLuint index, GLfloat v0);
typedef void (APIENTRY *glVertexAttrib1dARBPROC) (GLuint index, GLdouble v0);
typedef void (APIENTRY *glVertexAttrib2sARBPROC) (GLuint index, GLshort v0, GLshort v1);
typedef void (APIENTRY *glVertexAttrib2fARBPROC) (GLuint index, GLfloat v0, GLfloat v1);
typedef void (APIENTRY *glVertexAttrib2dARBPROC) (GLuint index, GLdouble v0, GLdouble v1);
typedef void (APIENTRY *glVertexAttrib3sARBPROC) (GLuint index, GLshort v0, GLshort v1, GLshort v2);
typedef void (APIENTRY *glVertexAttrib3fARBPROC) (GLuint index, GLfloat v0, GLfloat v1, GLfloat v2);
typedef void (APIENTRY *glVertexAttrib3dARBPROC) (GLuint index, GLdouble v0, GLdouble v1, GLdouble v2);
typedef void (APIENTRY *glVertexAttrib4sARBPROC) (GLuint index, GLshort v0, GLshort v1, GLshort v2, GLshort v3);
typedef void (APIENTRY *glVertexAttrib4fARBPROC) (GLuint index, GLfloat v0, GLfloat v1, GLfloat v2, GLfloat v3);
typedef void (APIENTRY *glVertexAttrib4dARBPROC) (GLuint index, GLdouble v0, GLdouble v1, GLdouble v2, GLdouble v3);
typedef void (APIENTRY *glVertexAttrib4NubARBPROC) (GLuint index, GLubyte x, GLubyte y, GLubyte z, GLubyte w);
typedef void (APIENTRY *glVertexAttribPointerARBPROC) (GLuint index, GLint size, GLenum type, GLboolean normalized, GLsizei stride, const GLvoid * buffer);
typedef void (APIENTRY *glEnableVertexAttribArrayARBPROC) (GLuint index);
typedef void (APIENTRY *glDisableVertexAttribArrayARBPROC) (GLuint index);
typedef void (APIENTRY *glBindAttribLocationARBPROC) (GLhandleARB programObj, GLuint index, const GLcharARB * name);
typedef void (APIENTRY *glGetActiveAttribARBPROC) (GLhandleARB programObj, GLuint index, GLsizei maxLength, GLsizei * length, GLint * size, GLenum * type, GLcharARB * name);
typedef GLint (APIENTRY *glGetAttribLocationARBPROC) (GLhandleARB programObj, const GLcharARB * name);
typedef void (APIENTRY *glGetVertexAttribfvARBPROC) (GLuint index, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetVertexAttribdvARBPROC) (GLuint index, GLenum pname, GLdouble * params);
typedef void (APIENTRY *glGetVertexAttribivARBPROC) (GLuint index, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetVertexAttribPointervARBPROC) (GLuint index, GLenum pname, GLvoid ** result);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttrib1sARB(JNIEnv *env, jclass clazz, jint index, jshort v0, jlong function_pointer) {
	glVertexAttrib1sARBPROC glVertexAttrib1sARB = (glVertexAttrib1sARBPROC)((intptr_t)function_pointer);
	glVertexAttrib1sARB(index, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttrib1fARB(JNIEnv *env, jclass clazz, jint index, jfloat v0, jlong function_pointer) {
	glVertexAttrib1fARBPROC glVertexAttrib1fARB = (glVertexAttrib1fARBPROC)((intptr_t)function_pointer);
	glVertexAttrib1fARB(index, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttrib1dARB(JNIEnv *env, jclass clazz, jint index, jdouble v0, jlong function_pointer) {
	glVertexAttrib1dARBPROC glVertexAttrib1dARB = (glVertexAttrib1dARBPROC)((intptr_t)function_pointer);
	glVertexAttrib1dARB(index, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttrib2sARB(JNIEnv *env, jclass clazz, jint index, jshort v0, jshort v1, jlong function_pointer) {
	glVertexAttrib2sARBPROC glVertexAttrib2sARB = (glVertexAttrib2sARBPROC)((intptr_t)function_pointer);
	glVertexAttrib2sARB(index, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttrib2fARB(JNIEnv *env, jclass clazz, jint index, jfloat v0, jfloat v1, jlong function_pointer) {
	glVertexAttrib2fARBPROC glVertexAttrib2fARB = (glVertexAttrib2fARBPROC)((intptr_t)function_pointer);
	glVertexAttrib2fARB(index, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttrib2dARB(JNIEnv *env, jclass clazz, jint index, jdouble v0, jdouble v1, jlong function_pointer) {
	glVertexAttrib2dARBPROC glVertexAttrib2dARB = (glVertexAttrib2dARBPROC)((intptr_t)function_pointer);
	glVertexAttrib2dARB(index, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttrib3sARB(JNIEnv *env, jclass clazz, jint index, jshort v0, jshort v1, jshort v2, jlong function_pointer) {
	glVertexAttrib3sARBPROC glVertexAttrib3sARB = (glVertexAttrib3sARBPROC)((intptr_t)function_pointer);
	glVertexAttrib3sARB(index, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttrib3fARB(JNIEnv *env, jclass clazz, jint index, jfloat v0, jfloat v1, jfloat v2, jlong function_pointer) {
	glVertexAttrib3fARBPROC glVertexAttrib3fARB = (glVertexAttrib3fARBPROC)((intptr_t)function_pointer);
	glVertexAttrib3fARB(index, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttrib3dARB(JNIEnv *env, jclass clazz, jint index, jdouble v0, jdouble v1, jdouble v2, jlong function_pointer) {
	glVertexAttrib3dARBPROC glVertexAttrib3dARB = (glVertexAttrib3dARBPROC)((intptr_t)function_pointer);
	glVertexAttrib3dARB(index, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttrib4sARB(JNIEnv *env, jclass clazz, jint index, jshort v0, jshort v1, jshort v2, jshort v3, jlong function_pointer) {
	glVertexAttrib4sARBPROC glVertexAttrib4sARB = (glVertexAttrib4sARBPROC)((intptr_t)function_pointer);
	glVertexAttrib4sARB(index, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttrib4fARB(JNIEnv *env, jclass clazz, jint index, jfloat v0, jfloat v1, jfloat v2, jfloat v3, jlong function_pointer) {
	glVertexAttrib4fARBPROC glVertexAttrib4fARB = (glVertexAttrib4fARBPROC)((intptr_t)function_pointer);
	glVertexAttrib4fARB(index, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttrib4dARB(JNIEnv *env, jclass clazz, jint index, jdouble v0, jdouble v1, jdouble v2, jdouble v3, jlong function_pointer) {
	glVertexAttrib4dARBPROC glVertexAttrib4dARB = (glVertexAttrib4dARBPROC)((intptr_t)function_pointer);
	glVertexAttrib4dARB(index, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttrib4NubARB(JNIEnv *env, jclass clazz, jint index, jbyte x, jbyte y, jbyte z, jbyte w, jlong function_pointer) {
	glVertexAttrib4NubARBPROC glVertexAttrib4NubARB = (glVertexAttrib4NubARBPROC)((intptr_t)function_pointer);
	glVertexAttrib4NubARB(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttribPointerARB(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jboolean normalized, jint stride, jlong buffer, jlong function_pointer) {
	const GLvoid *buffer_address = (const GLvoid *)(intptr_t)buffer;
	glVertexAttribPointerARBPROC glVertexAttribPointerARB = (glVertexAttribPointerARBPROC)((intptr_t)function_pointer);
	glVertexAttribPointerARB(index, size, type, normalized, stride, buffer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglVertexAttribPointerARBBO(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jboolean normalized, jint stride, jlong buffer_buffer_offset, jlong function_pointer) {
	const GLvoid *buffer_address = (const GLvoid *)(intptr_t)offsetToPointer(buffer_buffer_offset);
	glVertexAttribPointerARBPROC glVertexAttribPointerARB = (glVertexAttribPointerARBPROC)((intptr_t)function_pointer);
	glVertexAttribPointerARB(index, size, type, normalized, stride, buffer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglEnableVertexAttribArrayARB(JNIEnv *env, jclass clazz, jint index, jlong function_pointer) {
	glEnableVertexAttribArrayARBPROC glEnableVertexAttribArrayARB = (glEnableVertexAttribArrayARBPROC)((intptr_t)function_pointer);
	glEnableVertexAttribArrayARB(index);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglDisableVertexAttribArrayARB(JNIEnv *env, jclass clazz, jint index, jlong function_pointer) {
	glDisableVertexAttribArrayARBPROC glDisableVertexAttribArrayARB = (glDisableVertexAttribArrayARBPROC)((intptr_t)function_pointer);
	glDisableVertexAttribArrayARB(index);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglBindAttribLocationARB(JNIEnv *env, jclass clazz, jint programObj, jint index, jlong name, jlong function_pointer) {
	const GLcharARB *name_address = (const GLcharARB *)(intptr_t)name;
	glBindAttribLocationARBPROC glBindAttribLocationARB = (glBindAttribLocationARBPROC)((intptr_t)function_pointer);
	glBindAttribLocationARB(programObj, index, name_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglGetActiveAttribARB(JNIEnv *env, jclass clazz, jint programObj, jint index, jint maxLength, jlong length, jlong size, jlong type, jlong name, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLint *size_address = (GLint *)(intptr_t)size;
	GLenum *type_address = (GLenum *)(intptr_t)type;
	GLcharARB *name_address = (GLcharARB *)(intptr_t)name;
	glGetActiveAttribARBPROC glGetActiveAttribARB = (glGetActiveAttribARBPROC)((intptr_t)function_pointer);
	glGetActiveAttribARB(programObj, index, maxLength, length_address, size_address, type_address, name_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglGetAttribLocationARB(JNIEnv *env, jclass clazz, jint programObj, jlong name, jlong function_pointer) {
	const GLcharARB *name_address = (const GLcharARB *)(intptr_t)name;
	glGetAttribLocationARBPROC glGetAttribLocationARB = (glGetAttribLocationARBPROC)((intptr_t)function_pointer);
	GLint __result = glGetAttribLocationARB(programObj, name_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglGetVertexAttribfvARB(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetVertexAttribfvARBPROC glGetVertexAttribfvARB = (glGetVertexAttribfvARBPROC)((intptr_t)function_pointer);
	glGetVertexAttribfvARB(index, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglGetVertexAttribdvARB(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLdouble *params_address = (GLdouble *)(intptr_t)params;
	glGetVertexAttribdvARBPROC glGetVertexAttribdvARB = (glGetVertexAttribdvARBPROC)((intptr_t)function_pointer);
	glGetVertexAttribdvARB(index, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglGetVertexAttribivARB(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetVertexAttribivARBPROC glGetVertexAttribivARB = (glGetVertexAttribivARBPROC)((intptr_t)function_pointer);
	glGetVertexAttribivARB(index, pname, params_address);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_ARBVertexShader_nglGetVertexAttribPointervARB(JNIEnv *env, jclass clazz, jint index, jint pname, jlong result_size, jlong function_pointer) {
	glGetVertexAttribPointervARBPROC glGetVertexAttribPointervARB = (glGetVertexAttribPointervARBPROC)((intptr_t)function_pointer);
	GLvoid * __result;
	glGetVertexAttribPointervARB(index, pname, &__result);
	return safeNewBuffer(env, __result, result_size);
}

