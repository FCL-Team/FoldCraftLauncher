/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glMultiDrawArraysEXTPROC) (GLenum mode, GLint * piFirst, GLsizei * piCount, GLint primcount);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTMultiDrawArrays_nglMultiDrawArraysEXT(JNIEnv *env, jclass clazz, jint mode, jlong piFirst, jlong piCount, jint primcount, jlong function_pointer) {
	GLint *piFirst_address = (GLint *)(intptr_t)piFirst;
	GLsizei *piCount_address = (GLsizei *)(intptr_t)piCount;
	glMultiDrawArraysEXTPROC glMultiDrawArraysEXT = (glMultiDrawArraysEXTPROC)((intptr_t)function_pointer);
	glMultiDrawArraysEXT(mode, piFirst_address, piCount_address, primcount);
}

