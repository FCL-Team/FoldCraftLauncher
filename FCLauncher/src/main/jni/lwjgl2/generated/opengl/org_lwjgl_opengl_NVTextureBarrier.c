/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glTextureBarrierNVPROC) ();

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTextureBarrier_nglTextureBarrierNV(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glTextureBarrierNVPROC glTextureBarrierNV = (glTextureBarrierNVPROC)((intptr_t)function_pointer);
	glTextureBarrierNV();
}

