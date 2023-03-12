/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glWeightbvARBPROC) (GLint size, GLbyte * pWeights);
typedef void (APIENTRY *glWeightsvARBPROC) (GLint size, GLshort * pWeights);
typedef void (APIENTRY *glWeightivARBPROC) (GLint size, GLint * pWeights);
typedef void (APIENTRY *glWeightfvARBPROC) (GLint size, GLfloat * pWeights);
typedef void (APIENTRY *glWeightdvARBPROC) (GLint size, GLdouble * pWeights);
typedef void (APIENTRY *glWeightubvARBPROC) (GLint size, GLubyte * pWeights);
typedef void (APIENTRY *glWeightusvARBPROC) (GLint size, GLushort * pWeights);
typedef void (APIENTRY *glWeightuivARBPROC) (GLint size, GLuint * pWeights);
typedef void (APIENTRY *glWeightPointerARBPROC) (GLint size, GLenum type, GLsizei stride, const GLvoid * pPointer);
typedef void (APIENTRY *glVertexBlendARBPROC) (GLint count);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexBlend_nglWeightbvARB(JNIEnv *env, jclass clazz, jint size, jlong pWeights, jlong function_pointer) {
	GLbyte *pWeights_address = (GLbyte *)(intptr_t)pWeights;
	glWeightbvARBPROC glWeightbvARB = (glWeightbvARBPROC)((intptr_t)function_pointer);
	glWeightbvARB(size, pWeights_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexBlend_nglWeightsvARB(JNIEnv *env, jclass clazz, jint size, jlong pWeights, jlong function_pointer) {
	GLshort *pWeights_address = (GLshort *)(intptr_t)pWeights;
	glWeightsvARBPROC glWeightsvARB = (glWeightsvARBPROC)((intptr_t)function_pointer);
	glWeightsvARB(size, pWeights_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexBlend_nglWeightivARB(JNIEnv *env, jclass clazz, jint size, jlong pWeights, jlong function_pointer) {
	GLint *pWeights_address = (GLint *)(intptr_t)pWeights;
	glWeightivARBPROC glWeightivARB = (glWeightivARBPROC)((intptr_t)function_pointer);
	glWeightivARB(size, pWeights_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexBlend_nglWeightfvARB(JNIEnv *env, jclass clazz, jint size, jlong pWeights, jlong function_pointer) {
	GLfloat *pWeights_address = (GLfloat *)(intptr_t)pWeights;
	glWeightfvARBPROC glWeightfvARB = (glWeightfvARBPROC)((intptr_t)function_pointer);
	glWeightfvARB(size, pWeights_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexBlend_nglWeightdvARB(JNIEnv *env, jclass clazz, jint size, jlong pWeights, jlong function_pointer) {
	GLdouble *pWeights_address = (GLdouble *)(intptr_t)pWeights;
	glWeightdvARBPROC glWeightdvARB = (glWeightdvARBPROC)((intptr_t)function_pointer);
	glWeightdvARB(size, pWeights_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexBlend_nglWeightubvARB(JNIEnv *env, jclass clazz, jint size, jlong pWeights, jlong function_pointer) {
	GLubyte *pWeights_address = (GLubyte *)(intptr_t)pWeights;
	glWeightubvARBPROC glWeightubvARB = (glWeightubvARBPROC)((intptr_t)function_pointer);
	glWeightubvARB(size, pWeights_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexBlend_nglWeightusvARB(JNIEnv *env, jclass clazz, jint size, jlong pWeights, jlong function_pointer) {
	GLushort *pWeights_address = (GLushort *)(intptr_t)pWeights;
	glWeightusvARBPROC glWeightusvARB = (glWeightusvARBPROC)((intptr_t)function_pointer);
	glWeightusvARB(size, pWeights_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexBlend_nglWeightuivARB(JNIEnv *env, jclass clazz, jint size, jlong pWeights, jlong function_pointer) {
	GLuint *pWeights_address = (GLuint *)(intptr_t)pWeights;
	glWeightuivARBPROC glWeightuivARB = (glWeightuivARBPROC)((intptr_t)function_pointer);
	glWeightuivARB(size, pWeights_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexBlend_nglWeightPointerARB(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong pPointer, jlong function_pointer) {
	const GLvoid *pPointer_address = (const GLvoid *)(intptr_t)pPointer;
	glWeightPointerARBPROC glWeightPointerARB = (glWeightPointerARBPROC)((intptr_t)function_pointer);
	glWeightPointerARB(size, type, stride, pPointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexBlend_nglWeightPointerARBBO(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong pPointer_buffer_offset, jlong function_pointer) {
	const GLvoid *pPointer_address = (const GLvoid *)(intptr_t)offsetToPointer(pPointer_buffer_offset);
	glWeightPointerARBPROC glWeightPointerARB = (glWeightPointerARBPROC)((intptr_t)function_pointer);
	glWeightPointerARB(size, type, stride, pPointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexBlend_nglVertexBlendARB(JNIEnv *env, jclass clazz, jint count, jlong function_pointer) {
	glVertexBlendARBPROC glVertexBlendARB = (glVertexBlendARBPROC)((intptr_t)function_pointer);
	glVertexBlendARB(count);
}

