/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glProvokingVertexEXTPROC) (GLenum mode);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTProvokingVertex_nglProvokingVertexEXT(JNIEnv *env, jclass clazz, jint mode, jlong function_pointer) {
	glProvokingVertexEXTPROC glProvokingVertexEXT = (glProvokingVertexEXTPROC)((intptr_t)function_pointer);
	glProvokingVertexEXT(mode);
}

