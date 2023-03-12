/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glEnableVertexAttribAPPLEPROC) (GLuint index, GLenum pname);
typedef void (APIENTRY *glDisableVertexAttribAPPLEPROC) (GLuint index, GLenum pname);
typedef GLboolean (APIENTRY *glIsVertexAttribEnabledAPPLEPROC) (GLuint index, GLenum pname);
typedef void (APIENTRY *glMapVertexAttrib1dAPPLEPROC) (GLuint index, GLuint size, GLdouble u1, GLdouble u2, GLint stride, GLint order, const GLdouble * points);
typedef void (APIENTRY *glMapVertexAttrib1fAPPLEPROC) (GLuint index, GLuint size, GLfloat u1, GLfloat u2, GLint stride, GLint order, const GLfloat * points);
typedef void (APIENTRY *glMapVertexAttrib2dAPPLEPROC) (GLuint index, GLuint size, GLdouble u1, GLdouble u2, GLint ustride, GLint uorder, GLdouble v1, GLdouble v2, GLint vstride, GLint vorder, const GLdouble * points);
typedef void (APIENTRY *glMapVertexAttrib2fAPPLEPROC) (GLuint index, GLuint size, GLfloat u1, GLfloat u2, GLint ustride, GLint uorder, GLfloat v1, GLfloat v2, GLint vstride, GLint vorder, const GLfloat * points);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEVertexProgramEvaluators_nglEnableVertexAttribAPPLE(JNIEnv *env, jclass clazz, jint index, jint pname, jlong function_pointer) {
	glEnableVertexAttribAPPLEPROC glEnableVertexAttribAPPLE = (glEnableVertexAttribAPPLEPROC)((intptr_t)function_pointer);
	glEnableVertexAttribAPPLE(index, pname);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEVertexProgramEvaluators_nglDisableVertexAttribAPPLE(JNIEnv *env, jclass clazz, jint index, jint pname, jlong function_pointer) {
	glDisableVertexAttribAPPLEPROC glDisableVertexAttribAPPLE = (glDisableVertexAttribAPPLEPROC)((intptr_t)function_pointer);
	glDisableVertexAttribAPPLE(index, pname);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_APPLEVertexProgramEvaluators_nglIsVertexAttribEnabledAPPLE(JNIEnv *env, jclass clazz, jint index, jint pname, jlong function_pointer) {
	glIsVertexAttribEnabledAPPLEPROC glIsVertexAttribEnabledAPPLE = (glIsVertexAttribEnabledAPPLEPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsVertexAttribEnabledAPPLE(index, pname);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEVertexProgramEvaluators_nglMapVertexAttrib1dAPPLE(JNIEnv *env, jclass clazz, jint index, jint size, jdouble u1, jdouble u2, jint stride, jint order, jlong points, jlong function_pointer) {
	const GLdouble *points_address = (const GLdouble *)(intptr_t)points;
	glMapVertexAttrib1dAPPLEPROC glMapVertexAttrib1dAPPLE = (glMapVertexAttrib1dAPPLEPROC)((intptr_t)function_pointer);
	glMapVertexAttrib1dAPPLE(index, size, u1, u2, stride, order, points_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEVertexProgramEvaluators_nglMapVertexAttrib1fAPPLE(JNIEnv *env, jclass clazz, jint index, jint size, jfloat u1, jfloat u2, jint stride, jint order, jlong points, jlong function_pointer) {
	const GLfloat *points_address = (const GLfloat *)(intptr_t)points;
	glMapVertexAttrib1fAPPLEPROC glMapVertexAttrib1fAPPLE = (glMapVertexAttrib1fAPPLEPROC)((intptr_t)function_pointer);
	glMapVertexAttrib1fAPPLE(index, size, u1, u2, stride, order, points_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEVertexProgramEvaluators_nglMapVertexAttrib2dAPPLE(JNIEnv *env, jclass clazz, jint index, jint size, jdouble u1, jdouble u2, jint ustride, jint uorder, jdouble v1, jdouble v2, jint vstride, jint vorder, jlong points, jlong function_pointer) {
	const GLdouble *points_address = (const GLdouble *)(intptr_t)points;
	glMapVertexAttrib2dAPPLEPROC glMapVertexAttrib2dAPPLE = (glMapVertexAttrib2dAPPLEPROC)((intptr_t)function_pointer);
	glMapVertexAttrib2dAPPLE(index, size, u1, u2, ustride, uorder, v1, v2, vstride, vorder, points_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEVertexProgramEvaluators_nglMapVertexAttrib2fAPPLE(JNIEnv *env, jclass clazz, jint index, jint size, jfloat u1, jfloat u2, jint ustride, jint uorder, jfloat v1, jfloat v2, jint vstride, jint vorder, jlong points, jlong function_pointer) {
	const GLfloat *points_address = (const GLfloat *)(intptr_t)points;
	glMapVertexAttrib2fAPPLEPROC glMapVertexAttrib2fAPPLE = (glMapVertexAttrib2fAPPLEPROC)((intptr_t)function_pointer);
	glMapVertexAttrib2fAPPLE(index, size, u1, u2, ustride, uorder, v1, v2, vstride, vorder, points_address);
}

