/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glUniformBufferEXTPROC) (GLuint program, GLint location, GLuint buffer);
typedef GLint (APIENTRY *glGetUniformBufferSizeEXTPROC) (GLuint program, GLint location);
typedef GLintptr (APIENTRY *glGetUniformOffsetEXTPROC) (GLuint program, GLint location);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTBindableUniform_nglUniformBufferEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint buffer, jlong function_pointer) {
	glUniformBufferEXTPROC glUniformBufferEXT = (glUniformBufferEXTPROC)((intptr_t)function_pointer);
	glUniformBufferEXT(program, location, buffer);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_EXTBindableUniform_nglGetUniformBufferSizeEXT(JNIEnv *env, jclass clazz, jint program, jint location, jlong function_pointer) {
	glGetUniformBufferSizeEXTPROC glGetUniformBufferSizeEXT = (glGetUniformBufferSizeEXTPROC)((intptr_t)function_pointer);
	GLint __result = glGetUniformBufferSizeEXT(program, location);
	return __result;
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_EXTBindableUniform_nglGetUniformOffsetEXT(JNIEnv *env, jclass clazz, jint program, jint location, jlong function_pointer) {
	glGetUniformOffsetEXTPROC glGetUniformOffsetEXT = (glGetUniformOffsetEXTPROC)((intptr_t)function_pointer);
	GLintptr __result = glGetUniformOffsetEXT(program, location);
	return __result;
}

