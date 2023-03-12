/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GLuint (APIENTRY *glGenFragmentShadersATIPROC) (GLuint range);
typedef void (APIENTRY *glBindFragmentShaderATIPROC) (GLuint id);
typedef void (APIENTRY *glDeleteFragmentShaderATIPROC) (GLuint id);
typedef void (APIENTRY *glBeginFragmentShaderATIPROC) ();
typedef void (APIENTRY *glEndFragmentShaderATIPROC) ();
typedef void (APIENTRY *glPassTexCoordATIPROC) (GLuint dst, GLuint coord, GLenum swizzle);
typedef void (APIENTRY *glSampleMapATIPROC) (GLuint dst, GLuint interp, GLenum swizzle);
typedef void (APIENTRY *glColorFragmentOp1ATIPROC) (GLenum op, GLuint dst, GLuint dstMask, GLuint dstMod, GLuint arg1, GLuint arg1Rep, GLuint arg1Mod);
typedef void (APIENTRY *glColorFragmentOp2ATIPROC) (GLenum op, GLuint dst, GLuint dstMask, GLuint dstMod, GLuint arg1, GLuint arg1Rep, GLuint arg1Mod, GLuint arg2, GLuint arg2Rep, GLuint arg2Mod);
typedef void (APIENTRY *glColorFragmentOp3ATIPROC) (GLenum op, GLuint dst, GLuint dstMask, GLuint dstMod, GLuint arg1, GLuint arg1Rep, GLuint arg1Mod, GLuint arg2, GLuint arg2Rep, GLuint arg2Mod, GLuint arg3, GLuint arg3Rep, GLuint arg3Mod);
typedef void (APIENTRY *glAlphaFragmentOp1ATIPROC) (GLenum op, GLuint dst, GLuint dstMod, GLuint arg1, GLuint arg1Rep, GLuint arg1Mod);
typedef void (APIENTRY *glAlphaFragmentOp2ATIPROC) (GLenum op, GLuint dst, GLuint dstMod, GLuint arg1, GLuint arg1Rep, GLuint arg1Mod, GLuint arg2, GLuint arg2Rep, GLuint arg2Mod);
typedef void (APIENTRY *glAlphaFragmentOp3ATIPROC) (GLenum op, GLuint dst, GLuint dstMod, GLuint arg1, GLuint arg1Rep, GLuint arg1Mod, GLuint arg2, GLuint arg2Rep, GLuint arg2Mod, GLuint arg3, GLuint arg3Rep, GLuint arg3Mod);
typedef void (APIENTRY *glSetFragmentShaderConstantATIPROC) (GLuint dst, const GLfloat * pfValue);

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_ATIFragmentShader_nglGenFragmentShadersATI(JNIEnv *env, jclass clazz, jint range, jlong function_pointer) {
	glGenFragmentShadersATIPROC glGenFragmentShadersATI = (glGenFragmentShadersATIPROC)((intptr_t)function_pointer);
	GLuint __result = glGenFragmentShadersATI(range);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIFragmentShader_nglBindFragmentShaderATI(JNIEnv *env, jclass clazz, jint id, jlong function_pointer) {
	glBindFragmentShaderATIPROC glBindFragmentShaderATI = (glBindFragmentShaderATIPROC)((intptr_t)function_pointer);
	glBindFragmentShaderATI(id);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIFragmentShader_nglDeleteFragmentShaderATI(JNIEnv *env, jclass clazz, jint id, jlong function_pointer) {
	glDeleteFragmentShaderATIPROC glDeleteFragmentShaderATI = (glDeleteFragmentShaderATIPROC)((intptr_t)function_pointer);
	glDeleteFragmentShaderATI(id);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIFragmentShader_nglBeginFragmentShaderATI(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glBeginFragmentShaderATIPROC glBeginFragmentShaderATI = (glBeginFragmentShaderATIPROC)((intptr_t)function_pointer);
	glBeginFragmentShaderATI();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIFragmentShader_nglEndFragmentShaderATI(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glEndFragmentShaderATIPROC glEndFragmentShaderATI = (glEndFragmentShaderATIPROC)((intptr_t)function_pointer);
	glEndFragmentShaderATI();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIFragmentShader_nglPassTexCoordATI(JNIEnv *env, jclass clazz, jint dst, jint coord, jint swizzle, jlong function_pointer) {
	glPassTexCoordATIPROC glPassTexCoordATI = (glPassTexCoordATIPROC)((intptr_t)function_pointer);
	glPassTexCoordATI(dst, coord, swizzle);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIFragmentShader_nglSampleMapATI(JNIEnv *env, jclass clazz, jint dst, jint interp, jint swizzle, jlong function_pointer) {
	glSampleMapATIPROC glSampleMapATI = (glSampleMapATIPROC)((intptr_t)function_pointer);
	glSampleMapATI(dst, interp, swizzle);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIFragmentShader_nglColorFragmentOp1ATI(JNIEnv *env, jclass clazz, jint op, jint dst, jint dstMask, jint dstMod, jint arg1, jint arg1Rep, jint arg1Mod, jlong function_pointer) {
	glColorFragmentOp1ATIPROC glColorFragmentOp1ATI = (glColorFragmentOp1ATIPROC)((intptr_t)function_pointer);
	glColorFragmentOp1ATI(op, dst, dstMask, dstMod, arg1, arg1Rep, arg1Mod);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIFragmentShader_nglColorFragmentOp2ATI(JNIEnv *env, jclass clazz, jint op, jint dst, jint dstMask, jint dstMod, jint arg1, jint arg1Rep, jint arg1Mod, jint arg2, jint arg2Rep, jint arg2Mod, jlong function_pointer) {
	glColorFragmentOp2ATIPROC glColorFragmentOp2ATI = (glColorFragmentOp2ATIPROC)((intptr_t)function_pointer);
	glColorFragmentOp2ATI(op, dst, dstMask, dstMod, arg1, arg1Rep, arg1Mod, arg2, arg2Rep, arg2Mod);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIFragmentShader_nglColorFragmentOp3ATI(JNIEnv *env, jclass clazz, jint op, jint dst, jint dstMask, jint dstMod, jint arg1, jint arg1Rep, jint arg1Mod, jint arg2, jint arg2Rep, jint arg2Mod, jint arg3, jint arg3Rep, jint arg3Mod, jlong function_pointer) {
	glColorFragmentOp3ATIPROC glColorFragmentOp3ATI = (glColorFragmentOp3ATIPROC)((intptr_t)function_pointer);
	glColorFragmentOp3ATI(op, dst, dstMask, dstMod, arg1, arg1Rep, arg1Mod, arg2, arg2Rep, arg2Mod, arg3, arg3Rep, arg3Mod);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIFragmentShader_nglAlphaFragmentOp1ATI(JNIEnv *env, jclass clazz, jint op, jint dst, jint dstMod, jint arg1, jint arg1Rep, jint arg1Mod, jlong function_pointer) {
	glAlphaFragmentOp1ATIPROC glAlphaFragmentOp1ATI = (glAlphaFragmentOp1ATIPROC)((intptr_t)function_pointer);
	glAlphaFragmentOp1ATI(op, dst, dstMod, arg1, arg1Rep, arg1Mod);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIFragmentShader_nglAlphaFragmentOp2ATI(JNIEnv *env, jclass clazz, jint op, jint dst, jint dstMod, jint arg1, jint arg1Rep, jint arg1Mod, jint arg2, jint arg2Rep, jint arg2Mod, jlong function_pointer) {
	glAlphaFragmentOp2ATIPROC glAlphaFragmentOp2ATI = (glAlphaFragmentOp2ATIPROC)((intptr_t)function_pointer);
	glAlphaFragmentOp2ATI(op, dst, dstMod, arg1, arg1Rep, arg1Mod, arg2, arg2Rep, arg2Mod);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIFragmentShader_nglAlphaFragmentOp3ATI(JNIEnv *env, jclass clazz, jint op, jint dst, jint dstMod, jint arg1, jint arg1Rep, jint arg1Mod, jint arg2, jint arg2Rep, jint arg2Mod, jint arg3, jint arg3Rep, jint arg3Mod, jlong function_pointer) {
	glAlphaFragmentOp3ATIPROC glAlphaFragmentOp3ATI = (glAlphaFragmentOp3ATIPROC)((intptr_t)function_pointer);
	glAlphaFragmentOp3ATI(op, dst, dstMod, arg1, arg1Rep, arg1Mod, arg2, arg2Rep, arg2Mod, arg3, arg3Rep, arg3Mod);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIFragmentShader_nglSetFragmentShaderConstantATI(JNIEnv *env, jclass clazz, jint dst, jlong pfValue, jlong function_pointer) {
	const GLfloat *pfValue_address = (const GLfloat *)(intptr_t)pfValue;
	glSetFragmentShaderConstantATIPROC glSetFragmentShaderConstantATI = (glSetFragmentShaderConstantATIPROC)((intptr_t)function_pointer);
	glSetFragmentShaderConstantATI(dst, pfValue_address);
}

