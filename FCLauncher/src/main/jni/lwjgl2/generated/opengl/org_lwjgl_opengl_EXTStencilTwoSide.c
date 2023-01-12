/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glActiveStencilFaceEXTPROC) (GLenum face);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTStencilTwoSide_nglActiveStencilFaceEXT(JNIEnv *env, jclass clazz, jint face, jlong function_pointer) {
	glActiveStencilFaceEXTPROC glActiveStencilFaceEXT = (glActiveStencilFaceEXTPROC)((intptr_t)function_pointer);
	glActiveStencilFaceEXT(face);
}

