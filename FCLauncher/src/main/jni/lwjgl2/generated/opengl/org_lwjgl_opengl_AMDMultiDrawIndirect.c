/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glMultiDrawArraysIndirectAMDPROC) (GLenum mode, const GLvoid * indirect, GLsizei primcount, GLsizei stride);
typedef void (APIENTRY *glMultiDrawElementsIndirectAMDPROC) (GLenum mode, GLenum type, const GLvoid * indirect, GLsizei primcount, GLsizei stride);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDMultiDrawIndirect_nglMultiDrawArraysIndirectAMD(JNIEnv *env, jclass clazz, jint mode, jlong indirect, jint primcount, jint stride, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)indirect;
	glMultiDrawArraysIndirectAMDPROC glMultiDrawArraysIndirectAMD = (glMultiDrawArraysIndirectAMDPROC)((intptr_t)function_pointer);
	glMultiDrawArraysIndirectAMD(mode, indirect_address, primcount, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDMultiDrawIndirect_nglMultiDrawArraysIndirectAMDBO(JNIEnv *env, jclass clazz, jint mode, jlong indirect_buffer_offset, jint primcount, jint stride, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)offsetToPointer(indirect_buffer_offset);
	glMultiDrawArraysIndirectAMDPROC glMultiDrawArraysIndirectAMD = (glMultiDrawArraysIndirectAMDPROC)((intptr_t)function_pointer);
	glMultiDrawArraysIndirectAMD(mode, indirect_address, primcount, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDMultiDrawIndirect_nglMultiDrawElementsIndirectAMD(JNIEnv *env, jclass clazz, jint mode, jint type, jlong indirect, jint primcount, jint stride, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)indirect;
	glMultiDrawElementsIndirectAMDPROC glMultiDrawElementsIndirectAMD = (glMultiDrawElementsIndirectAMDPROC)((intptr_t)function_pointer);
	glMultiDrawElementsIndirectAMD(mode, type, indirect_address, primcount, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDMultiDrawIndirect_nglMultiDrawElementsIndirectAMDBO(JNIEnv *env, jclass clazz, jint mode, jint type, jlong indirect_buffer_offset, jint primcount, jint stride, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)offsetToPointer(indirect_buffer_offset);
	glMultiDrawElementsIndirectAMDPROC glMultiDrawElementsIndirectAMD = (glMultiDrawElementsIndirectAMDPROC)((intptr_t)function_pointer);
	glMultiDrawElementsIndirectAMD(mode, type, indirect_address, primcount, stride);
}

