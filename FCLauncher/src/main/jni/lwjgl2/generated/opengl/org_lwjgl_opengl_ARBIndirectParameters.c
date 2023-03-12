/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glMultiDrawArraysIndirectCountARBPROC) (GLenum mode, const GLvoid * indirect, GLintptr drawcount, GLsizei maxdrawcount, GLsizei stride);
typedef void (APIENTRY *glMultiDrawElementsIndirectCountARBPROC) (GLenum mode, GLenum type, const GLvoid * indirect, GLintptr drawcount, GLsizei maxdrawcount, GLsizei stride);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBIndirectParameters_nglMultiDrawArraysIndirectCountARB(JNIEnv *env, jclass clazz, jint mode, jlong indirect, jlong drawcount, jint maxdrawcount, jint stride, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)indirect;
	glMultiDrawArraysIndirectCountARBPROC glMultiDrawArraysIndirectCountARB = (glMultiDrawArraysIndirectCountARBPROC)((intptr_t)function_pointer);
	glMultiDrawArraysIndirectCountARB(mode, indirect_address, drawcount, maxdrawcount, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBIndirectParameters_nglMultiDrawArraysIndirectCountARBBO(JNIEnv *env, jclass clazz, jint mode, jlong indirect_buffer_offset, jlong drawcount, jint maxdrawcount, jint stride, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)offsetToPointer(indirect_buffer_offset);
	glMultiDrawArraysIndirectCountARBPROC glMultiDrawArraysIndirectCountARB = (glMultiDrawArraysIndirectCountARBPROC)((intptr_t)function_pointer);
	glMultiDrawArraysIndirectCountARB(mode, indirect_address, drawcount, maxdrawcount, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBIndirectParameters_nglMultiDrawElementsIndirectCountARB(JNIEnv *env, jclass clazz, jint mode, jint type, jlong indirect, jlong drawcount, jint maxdrawcount, jint stride, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)indirect;
	glMultiDrawElementsIndirectCountARBPROC glMultiDrawElementsIndirectCountARB = (glMultiDrawElementsIndirectCountARBPROC)((intptr_t)function_pointer);
	glMultiDrawElementsIndirectCountARB(mode, type, indirect_address, drawcount, maxdrawcount, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBIndirectParameters_nglMultiDrawElementsIndirectCountARBBO(JNIEnv *env, jclass clazz, jint mode, jint type, jlong indirect_buffer_offset, jlong drawcount, jint maxdrawcount, jint stride, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)offsetToPointer(indirect_buffer_offset);
	glMultiDrawElementsIndirectCountARBPROC glMultiDrawElementsIndirectCountARB = (glMultiDrawElementsIndirectCountARBPROC)((intptr_t)function_pointer);
	glMultiDrawElementsIndirectCountARB(mode, type, indirect_address, drawcount, maxdrawcount, stride);
}

