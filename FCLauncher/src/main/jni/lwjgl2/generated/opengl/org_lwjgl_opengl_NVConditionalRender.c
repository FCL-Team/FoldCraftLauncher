/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBeginConditionalRenderNVPROC) (GLuint id, GLenum mode);
typedef void (APIENTRY *glEndConditionalRenderNVPROC) ();

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVConditionalRender_nglBeginConditionalRenderNV(JNIEnv *env, jclass clazz, jint id, jint mode, jlong function_pointer) {
	glBeginConditionalRenderNVPROC glBeginConditionalRenderNV = (glBeginConditionalRenderNVPROC)((intptr_t)function_pointer);
	glBeginConditionalRenderNV(id, mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVConditionalRender_nglEndConditionalRenderNV(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glEndConditionalRenderNVPROC glEndConditionalRenderNV = (glEndConditionalRenderNVPROC)((intptr_t)function_pointer);
	glEndConditionalRenderNV();
}

