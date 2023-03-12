/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBindBufferRangeNVPROC) (GLenum target, GLuint index, GLuint buffer, GLintptr offset, GLsizeiptr size);
typedef void (APIENTRY *glBindBufferOffsetNVPROC) (GLenum target, GLuint index, GLuint buffer, GLintptr offset);
typedef void (APIENTRY *glBindBufferBaseNVPROC) (GLenum target, GLuint index, GLuint buffer);
typedef void (APIENTRY *glTransformFeedbackAttribsNVPROC) (GLsizei count, const GLint * attribs, GLenum bufferMode);
typedef void (APIENTRY *glTransformFeedbackVaryingsNVPROC) (GLuint program, GLsizei count, const GLint * locations, GLenum bufferMode);
typedef void (APIENTRY *glBeginTransformFeedbackNVPROC) (GLenum primitiveMode);
typedef void (APIENTRY *glEndTransformFeedbackNVPROC) ();
typedef GLint (APIENTRY *glGetVaryingLocationNVPROC) (GLuint program, const GLchar * name);
typedef void (APIENTRY *glGetActiveVaryingNVPROC) (GLuint program, GLuint index, GLsizei bufSize, GLsizei * length, GLsizei * size, GLenum * type, GLchar * name);
typedef void (APIENTRY *glActiveVaryingNVPROC) (GLuint program, const GLchar * name);
typedef void (APIENTRY *glGetTransformFeedbackVaryingNVPROC) (GLuint program, GLuint index, GLint * location);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback_nglBindBufferRangeNV(JNIEnv *env, jclass clazz, jint target, jint index, jint buffer, jlong offset, jlong size, jlong function_pointer) {
	glBindBufferRangeNVPROC glBindBufferRangeNV = (glBindBufferRangeNVPROC)((intptr_t)function_pointer);
	glBindBufferRangeNV(target, index, buffer, offset, size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback_nglBindBufferOffsetNV(JNIEnv *env, jclass clazz, jint target, jint index, jint buffer, jlong offset, jlong function_pointer) {
	glBindBufferOffsetNVPROC glBindBufferOffsetNV = (glBindBufferOffsetNVPROC)((intptr_t)function_pointer);
	glBindBufferOffsetNV(target, index, buffer, offset);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback_nglBindBufferBaseNV(JNIEnv *env, jclass clazz, jint target, jint index, jint buffer, jlong function_pointer) {
	glBindBufferBaseNVPROC glBindBufferBaseNV = (glBindBufferBaseNVPROC)((intptr_t)function_pointer);
	glBindBufferBaseNV(target, index, buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback_nglTransformFeedbackAttribsNV(JNIEnv *env, jclass clazz, jint count, jlong attribs, jint bufferMode, jlong function_pointer) {
	const GLint *attribs_address = (const GLint *)(intptr_t)attribs;
	glTransformFeedbackAttribsNVPROC glTransformFeedbackAttribsNV = (glTransformFeedbackAttribsNVPROC)((intptr_t)function_pointer);
	glTransformFeedbackAttribsNV(count, attribs_address, bufferMode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback_nglTransformFeedbackVaryingsNV(JNIEnv *env, jclass clazz, jint program, jint count, jlong locations, jint bufferMode, jlong function_pointer) {
	const GLint *locations_address = (const GLint *)(intptr_t)locations;
	glTransformFeedbackVaryingsNVPROC glTransformFeedbackVaryingsNV = (glTransformFeedbackVaryingsNVPROC)((intptr_t)function_pointer);
	glTransformFeedbackVaryingsNV(program, count, locations_address, bufferMode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback_nglBeginTransformFeedbackNV(JNIEnv *env, jclass clazz, jint primitiveMode, jlong function_pointer) {
	glBeginTransformFeedbackNVPROC glBeginTransformFeedbackNV = (glBeginTransformFeedbackNVPROC)((intptr_t)function_pointer);
	glBeginTransformFeedbackNV(primitiveMode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback_nglEndTransformFeedbackNV(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glEndTransformFeedbackNVPROC glEndTransformFeedbackNV = (glEndTransformFeedbackNVPROC)((intptr_t)function_pointer);
	glEndTransformFeedbackNV();
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_NVTransformFeedback_nglGetVaryingLocationNV(JNIEnv *env, jclass clazz, jint program, jlong name, jlong function_pointer) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	glGetVaryingLocationNVPROC glGetVaryingLocationNV = (glGetVaryingLocationNVPROC)((intptr_t)function_pointer);
	GLint __result = glGetVaryingLocationNV(program, name_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback_nglGetActiveVaryingNV(JNIEnv *env, jclass clazz, jint program, jint index, jint bufSize, jlong length, jlong size, jlong type, jlong name, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLsizei *size_address = (GLsizei *)(intptr_t)size;
	GLenum *type_address = (GLenum *)(intptr_t)type;
	GLchar *name_address = (GLchar *)(intptr_t)name;
	glGetActiveVaryingNVPROC glGetActiveVaryingNV = (glGetActiveVaryingNVPROC)((intptr_t)function_pointer);
	glGetActiveVaryingNV(program, index, bufSize, length_address, size_address, type_address, name_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback_nglActiveVaryingNV(JNIEnv *env, jclass clazz, jint program, jlong name, jlong function_pointer) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	glActiveVaryingNVPROC glActiveVaryingNV = (glActiveVaryingNVPROC)((intptr_t)function_pointer);
	glActiveVaryingNV(program, name_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback_nglGetTransformFeedbackVaryingNV(JNIEnv *env, jclass clazz, jint program, jint index, jlong location, jlong function_pointer) {
	GLint *location_address = (GLint *)(intptr_t)location;
	glGetTransformFeedbackVaryingNVPROC glGetTransformFeedbackVaryingNV = (glGetTransformFeedbackVaryingNVPROC)((intptr_t)function_pointer);
	glGetTransformFeedbackVaryingNV(program, index, location_address);
}

