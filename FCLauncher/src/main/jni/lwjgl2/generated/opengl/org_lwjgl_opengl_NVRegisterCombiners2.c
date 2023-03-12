/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glCombinerStageParameterfvNVPROC) (GLenum stage, GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glGetCombinerStageParameterfvNVPROC) (GLenum stage, GLenum pname, GLfloat * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVRegisterCombiners2_nglCombinerStageParameterfvNV(JNIEnv *env, jclass clazz, jint stage, jint pname, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glCombinerStageParameterfvNVPROC glCombinerStageParameterfvNV = (glCombinerStageParameterfvNVPROC)((intptr_t)function_pointer);
	glCombinerStageParameterfvNV(stage, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVRegisterCombiners2_nglGetCombinerStageParameterfvNV(JNIEnv *env, jclass clazz, jint stage, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetCombinerStageParameterfvNVPROC glGetCombinerStageParameterfvNV = (glGetCombinerStageParameterfvNVPROC)((intptr_t)function_pointer);
	glGetCombinerStageParameterfvNV(stage, pname, params_address);
}

