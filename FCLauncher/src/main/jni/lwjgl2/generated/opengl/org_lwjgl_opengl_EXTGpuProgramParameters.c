/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glProgramEnvParameters4fvEXTPROC) (GLenum target, GLuint index, GLsizei count, const GLfloat * params);
typedef void (APIENTRY *glProgramLocalParameters4fvEXTPROC) (GLenum target, GLuint index, GLsizei count, const GLfloat * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuProgramParameters_nglProgramEnvParameters4fvEXT(JNIEnv *env, jclass clazz, jint target, jint index, jint count, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glProgramEnvParameters4fvEXTPROC glProgramEnvParameters4fvEXT = (glProgramEnvParameters4fvEXTPROC)((intptr_t)function_pointer);
	glProgramEnvParameters4fvEXT(target, index, count, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuProgramParameters_nglProgramLocalParameters4fvEXT(JNIEnv *env, jclass clazz, jint target, jint index, jint count, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glProgramLocalParameters4fvEXTPROC glProgramLocalParameters4fvEXT = (glProgramLocalParameters4fvEXTPROC)((intptr_t)function_pointer);
	glProgramLocalParameters4fvEXT(target, index, count, params_address);
}

