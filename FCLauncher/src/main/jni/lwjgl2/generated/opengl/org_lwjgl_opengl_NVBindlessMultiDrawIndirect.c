/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glMultiDrawArraysIndirectBindlessNVPROC) (GLenum mode, const GLvoid * indirect, GLsizei drawCount, GLsizei stride, GLint vertexBufferCount);
typedef void (APIENTRY *glMultiDrawElementsIndirectBindlessNVPROC) (GLenum mode, GLenum type, const GLvoid * indirect, GLsizei drawCount, GLsizei stride, GLint vertexBufferCount);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVBindlessMultiDrawIndirect_nglMultiDrawArraysIndirectBindlessNV(JNIEnv *env, jclass clazz, jint mode, jlong indirect, jint drawCount, jint stride, jint vertexBufferCount, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)indirect;
	glMultiDrawArraysIndirectBindlessNVPROC glMultiDrawArraysIndirectBindlessNV = (glMultiDrawArraysIndirectBindlessNVPROC)((intptr_t)function_pointer);
	glMultiDrawArraysIndirectBindlessNV(mode, indirect_address, drawCount, stride, vertexBufferCount);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVBindlessMultiDrawIndirect_nglMultiDrawArraysIndirectBindlessNVBO(JNIEnv *env, jclass clazz, jint mode, jlong indirect_buffer_offset, jint drawCount, jint stride, jint vertexBufferCount, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)offsetToPointer(indirect_buffer_offset);
	glMultiDrawArraysIndirectBindlessNVPROC glMultiDrawArraysIndirectBindlessNV = (glMultiDrawArraysIndirectBindlessNVPROC)((intptr_t)function_pointer);
	glMultiDrawArraysIndirectBindlessNV(mode, indirect_address, drawCount, stride, vertexBufferCount);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVBindlessMultiDrawIndirect_nglMultiDrawElementsIndirectBindlessNV(JNIEnv *env, jclass clazz, jint mode, jint type, jlong indirect, jint drawCount, jint stride, jint vertexBufferCount, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)indirect;
	glMultiDrawElementsIndirectBindlessNVPROC glMultiDrawElementsIndirectBindlessNV = (glMultiDrawElementsIndirectBindlessNVPROC)((intptr_t)function_pointer);
	glMultiDrawElementsIndirectBindlessNV(mode, type, indirect_address, drawCount, stride, vertexBufferCount);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVBindlessMultiDrawIndirect_nglMultiDrawElementsIndirectBindlessNVBO(JNIEnv *env, jclass clazz, jint mode, jint type, jlong indirect_buffer_offset, jint drawCount, jint stride, jint vertexBufferCount, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)offsetToPointer(indirect_buffer_offset);
	glMultiDrawElementsIndirectBindlessNVPROC glMultiDrawElementsIndirectBindlessNV = (glMultiDrawElementsIndirectBindlessNVPROC)((intptr_t)function_pointer);
	glMultiDrawElementsIndirectBindlessNV(mode, type, indirect_address, drawCount, stride, vertexBufferCount);
}

