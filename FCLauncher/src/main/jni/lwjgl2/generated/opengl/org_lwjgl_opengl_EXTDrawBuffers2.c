/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glColorMaskIndexedEXTPROC) (GLuint buf, GLboolean r, GLboolean g, GLboolean b, GLboolean a);
typedef void (APIENTRY *glGetBooleanIndexedvEXTPROC) (GLenum value, GLuint index, GLboolean * data);
typedef void (APIENTRY *glGetIntegerIndexedvEXTPROC) (GLenum value, GLuint index, GLint * data);
typedef void (APIENTRY *glEnableIndexedEXTPROC) (GLenum target, GLuint index);
typedef void (APIENTRY *glDisableIndexedEXTPROC) (GLenum target, GLuint index);
typedef GLboolean (APIENTRY *glIsEnabledIndexedEXTPROC) (GLenum target, GLuint index);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDrawBuffers2_nglColorMaskIndexedEXT(JNIEnv *env, jclass clazz, jint buf, jboolean r, jboolean g, jboolean b, jboolean a, jlong function_pointer) {
	glColorMaskIndexedEXTPROC glColorMaskIndexedEXT = (glColorMaskIndexedEXTPROC)((intptr_t)function_pointer);
	glColorMaskIndexedEXT(buf, r, g, b, a);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDrawBuffers2_nglGetBooleanIndexedvEXT(JNIEnv *env, jclass clazz, jint value, jint index, jlong data, jlong function_pointer) {
	GLboolean *data_address = (GLboolean *)(intptr_t)data;
	glGetBooleanIndexedvEXTPROC glGetBooleanIndexedvEXT = (glGetBooleanIndexedvEXTPROC)((intptr_t)function_pointer);
	glGetBooleanIndexedvEXT(value, index, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDrawBuffers2_nglGetIntegerIndexedvEXT(JNIEnv *env, jclass clazz, jint value, jint index, jlong data, jlong function_pointer) {
	GLint *data_address = (GLint *)(intptr_t)data;
	glGetIntegerIndexedvEXTPROC glGetIntegerIndexedvEXT = (glGetIntegerIndexedvEXTPROC)((intptr_t)function_pointer);
	glGetIntegerIndexedvEXT(value, index, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDrawBuffers2_nglEnableIndexedEXT(JNIEnv *env, jclass clazz, jint target, jint index, jlong function_pointer) {
	glEnableIndexedEXTPROC glEnableIndexedEXT = (glEnableIndexedEXTPROC)((intptr_t)function_pointer);
	glEnableIndexedEXT(target, index);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDrawBuffers2_nglDisableIndexedEXT(JNIEnv *env, jclass clazz, jint target, jint index, jlong function_pointer) {
	glDisableIndexedEXTPROC glDisableIndexedEXT = (glDisableIndexedEXTPROC)((intptr_t)function_pointer);
	glDisableIndexedEXT(target, index);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_EXTDrawBuffers2_nglIsEnabledIndexedEXT(JNIEnv *env, jclass clazz, jint target, jint index, jlong function_pointer) {
	glIsEnabledIndexedEXTPROC glIsEnabledIndexedEXT = (glIsEnabledIndexedEXTPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsEnabledIndexedEXT(target, index);
	return __result;
}

