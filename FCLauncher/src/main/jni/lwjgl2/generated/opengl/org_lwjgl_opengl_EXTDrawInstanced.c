/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glDrawArraysInstancedEXTPROC) (GLenum mode, GLint first, GLsizei count, GLsizei primcount);
typedef void (APIENTRY *glDrawElementsInstancedEXTPROC) (GLenum mode, GLsizei count, GLenum type, const GLvoid * indices, GLsizei primcount);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDrawInstanced_nglDrawArraysInstancedEXT(JNIEnv *env, jclass clazz, jint mode, jint first, jint count, jint primcount, jlong function_pointer) {
	glDrawArraysInstancedEXTPROC glDrawArraysInstancedEXT = (glDrawArraysInstancedEXTPROC)((intptr_t)function_pointer);
	glDrawArraysInstancedEXT(mode, first, count, primcount);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDrawInstanced_nglDrawElementsInstancedEXT(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices, jint primcount, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)indices;
	glDrawElementsInstancedEXTPROC glDrawElementsInstancedEXT = (glDrawElementsInstancedEXTPROC)((intptr_t)function_pointer);
	glDrawElementsInstancedEXT(mode, count, type, indices_address, primcount);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDrawInstanced_nglDrawElementsInstancedEXTBO(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices_buffer_offset, jint primcount, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)offsetToPointer(indices_buffer_offset);
	glDrawElementsInstancedEXTPROC glDrawElementsInstancedEXT = (glDrawElementsInstancedEXTPROC)((intptr_t)function_pointer);
	glDrawElementsInstancedEXT(mode, count, type, indices_address, primcount);
}

