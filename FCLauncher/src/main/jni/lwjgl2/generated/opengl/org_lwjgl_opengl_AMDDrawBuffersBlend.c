/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBlendFuncIndexedAMDPROC) (GLuint buf, GLenum src, GLenum dst);
typedef void (APIENTRY *glBlendFuncSeparateIndexedAMDPROC) (GLuint buf, GLenum srcRGB, GLenum dstRGB, GLenum srcAlpha, GLenum dstAlpha);
typedef void (APIENTRY *glBlendEquationIndexedAMDPROC) (GLuint buf, GLenum mode);
typedef void (APIENTRY *glBlendEquationSeparateIndexedAMDPROC) (GLuint buf, GLenum modeRGB, GLenum modeAlpha);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDDrawBuffersBlend_nglBlendFuncIndexedAMD(JNIEnv *env, jclass clazz, jint buf, jint src, jint dst, jlong function_pointer) {
	glBlendFuncIndexedAMDPROC glBlendFuncIndexedAMD = (glBlendFuncIndexedAMDPROC)((intptr_t)function_pointer);
	glBlendFuncIndexedAMD(buf, src, dst);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDDrawBuffersBlend_nglBlendFuncSeparateIndexedAMD(JNIEnv *env, jclass clazz, jint buf, jint srcRGB, jint dstRGB, jint srcAlpha, jint dstAlpha, jlong function_pointer) {
	glBlendFuncSeparateIndexedAMDPROC glBlendFuncSeparateIndexedAMD = (glBlendFuncSeparateIndexedAMDPROC)((intptr_t)function_pointer);
	glBlendFuncSeparateIndexedAMD(buf, srcRGB, dstRGB, srcAlpha, dstAlpha);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDDrawBuffersBlend_nglBlendEquationIndexedAMD(JNIEnv *env, jclass clazz, jint buf, jint mode, jlong function_pointer) {
	glBlendEquationIndexedAMDPROC glBlendEquationIndexedAMD = (glBlendEquationIndexedAMDPROC)((intptr_t)function_pointer);
	glBlendEquationIndexedAMD(buf, mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDDrawBuffersBlend_nglBlendEquationSeparateIndexedAMD(JNIEnv *env, jclass clazz, jint buf, jint modeRGB, jint modeAlpha, jlong function_pointer) {
	glBlendEquationSeparateIndexedAMDPROC glBlendEquationSeparateIndexedAMD = (glBlendEquationSeparateIndexedAMDPROC)((intptr_t)function_pointer);
	glBlendEquationSeparateIndexedAMD(buf, modeRGB, modeAlpha);
}

