/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBlendEquationiARBPROC) (GLuint buf, GLenum mode);
typedef void (APIENTRY *glBlendEquationSeparateiARBPROC) (GLuint buf, GLenum modeRGB, GLenum modeAlpha);
typedef void (APIENTRY *glBlendFunciARBPROC) (GLuint buf, GLenum src, GLenum dst);
typedef void (APIENTRY *glBlendFuncSeparateiARBPROC) (GLuint buf, GLenum srcRGB, GLenum dstRGB, GLenum srcAlpha, GLenum dstAlpha);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBDrawBuffersBlend_nglBlendEquationiARB(JNIEnv *env, jclass clazz, jint buf, jint mode, jlong function_pointer) {
	glBlendEquationiARBPROC glBlendEquationiARB = (glBlendEquationiARBPROC)((intptr_t)function_pointer);
	glBlendEquationiARB(buf, mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBDrawBuffersBlend_nglBlendEquationSeparateiARB(JNIEnv *env, jclass clazz, jint buf, jint modeRGB, jint modeAlpha, jlong function_pointer) {
	glBlendEquationSeparateiARBPROC glBlendEquationSeparateiARB = (glBlendEquationSeparateiARBPROC)((intptr_t)function_pointer);
	glBlendEquationSeparateiARB(buf, modeRGB, modeAlpha);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBDrawBuffersBlend_nglBlendFunciARB(JNIEnv *env, jclass clazz, jint buf, jint src, jint dst, jlong function_pointer) {
	glBlendFunciARBPROC glBlendFunciARB = (glBlendFunciARBPROC)((intptr_t)function_pointer);
	glBlendFunciARB(buf, src, dst);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBDrawBuffersBlend_nglBlendFuncSeparateiARB(JNIEnv *env, jclass clazz, jint buf, jint srcRGB, jint dstRGB, jint srcAlpha, jint dstAlpha, jlong function_pointer) {
	glBlendFuncSeparateiARBPROC glBlendFuncSeparateiARB = (glBlendFuncSeparateiARBPROC)((intptr_t)function_pointer);
	glBlendFuncSeparateiARB(buf, srcRGB, dstRGB, srcAlpha, dstAlpha);
}

