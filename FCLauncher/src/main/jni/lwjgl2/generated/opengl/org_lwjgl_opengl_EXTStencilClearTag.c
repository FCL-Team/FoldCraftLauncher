/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glStencilClearTagEXTPROC) (GLsizei stencilTagBits, GLuint stencilClearTag);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTStencilClearTag_nglStencilClearTagEXT(JNIEnv *env, jclass clazz, jint stencilTagBits, jint stencilClearTag, jlong function_pointer) {
	glStencilClearTagEXTPROC glStencilClearTagEXT = (glStencilClearTagEXTPROC)((intptr_t)function_pointer);
	glStencilClearTagEXT(stencilTagBits, stencilClearTag);
}

