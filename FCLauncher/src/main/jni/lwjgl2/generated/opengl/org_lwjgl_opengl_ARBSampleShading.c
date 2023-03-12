/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glMinSampleShadingARBPROC) (GLclampf value);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBSampleShading_nglMinSampleShadingARB(JNIEnv *env, jclass clazz, jfloat value, jlong function_pointer) {
	glMinSampleShadingARBPROC glMinSampleShadingARB = (glMinSampleShadingARBPROC)((intptr_t)function_pointer);
	glMinSampleShadingARB(value);
}

