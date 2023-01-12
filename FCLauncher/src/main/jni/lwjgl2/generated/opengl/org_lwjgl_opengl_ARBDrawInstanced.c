/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glDrawArraysInstancedARBPROC) (GLenum mode, GLint first, GLsizei count, GLsizei primcount);
typedef void (APIENTRY *glDrawElementsInstancedARBPROC) (GLenum mode, GLsizei count, GLenum type, const GLvoid * indices, GLsizei primcount);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBDrawInstanced_nglDrawArraysInstancedARB(JNIEnv *env, jclass clazz, jint mode, jint first, jint count, jint primcount, jlong function_pointer) {
	glDrawArraysInstancedARBPROC glDrawArraysInstancedARB = (glDrawArraysInstancedARBPROC)((intptr_t)function_pointer);
	glDrawArraysInstancedARB(mode, first, count, primcount);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBDrawInstanced_nglDrawElementsInstancedARB(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices, jint primcount, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)indices;
	glDrawElementsInstancedARBPROC glDrawElementsInstancedARB = (glDrawElementsInstancedARBPROC)((intptr_t)function_pointer);
	glDrawElementsInstancedARB(mode, count, type, indices_address, primcount);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBDrawInstanced_nglDrawElementsInstancedARBBO(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices_buffer_offset, jint primcount, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)offsetToPointer(indices_buffer_offset);
	glDrawElementsInstancedARBPROC glDrawElementsInstancedARB = (glDrawElementsInstancedARBPROC)((intptr_t)function_pointer);
	glDrawElementsInstancedARB(mode, count, type, indices_address, primcount);
}

