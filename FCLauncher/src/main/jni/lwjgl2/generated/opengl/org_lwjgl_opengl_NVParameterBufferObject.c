/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glProgramBufferParametersfvNVPROC) (GLenum target, GLuint buffer, GLuint index, GLsizei count, const GLfloat * params);
typedef void (APIENTRY *glProgramBufferParametersIivNVPROC) (GLenum target, GLuint buffer, GLuint index, GLsizei count, const GLint * params);
typedef void (APIENTRY *glProgramBufferParametersIuivNVPROC) (GLenum target, GLuint buffer, GLuint index, GLuint count, const GLuint * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVParameterBufferObject_nglProgramBufferParametersfvNV(JNIEnv *env, jclass clazz, jint target, jint buffer, jint index, jint count, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glProgramBufferParametersfvNVPROC glProgramBufferParametersfvNV = (glProgramBufferParametersfvNVPROC)((intptr_t)function_pointer);
	glProgramBufferParametersfvNV(target, buffer, index, count, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVParameterBufferObject_nglProgramBufferParametersIivNV(JNIEnv *env, jclass clazz, jint target, jint buffer, jint index, jint count, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glProgramBufferParametersIivNVPROC glProgramBufferParametersIivNV = (glProgramBufferParametersIivNVPROC)((intptr_t)function_pointer);
	glProgramBufferParametersIivNV(target, buffer, index, count, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVParameterBufferObject_nglProgramBufferParametersIuivNV(JNIEnv *env, jclass clazz, jint target, jint buffer, jint index, jint count, jlong params, jlong function_pointer) {
	const GLuint *params_address = (const GLuint *)(intptr_t)params;
	glProgramBufferParametersIuivNVPROC glProgramBufferParametersIuivNV = (glProgramBufferParametersIuivNVPROC)((intptr_t)function_pointer);
	glProgramBufferParametersIuivNV(target, buffer, index, count, params_address);
}

