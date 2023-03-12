/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glLockArraysEXTPROC) (GLint first, GLsizei count);
typedef void (APIENTRY *glUnlockArraysEXTPROC) ();

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTCompiledVertexArray_nglLockArraysEXT(JNIEnv *env, jclass clazz, jint first, jint count, jlong function_pointer) {
	glLockArraysEXTPROC glLockArraysEXT = (glLockArraysEXTPROC)((intptr_t)function_pointer);
	glLockArraysEXT(first, count);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTCompiledVertexArray_nglUnlockArraysEXT(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glUnlockArraysEXTPROC glUnlockArraysEXT = (glUnlockArraysEXTPROC)((intptr_t)function_pointer);
	glUnlockArraysEXT();
}

