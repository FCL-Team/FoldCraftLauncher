/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glSampleCoverageARBPROC) (GLclampf value, GLboolean invert);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMultisample_nglSampleCoverageARB(JNIEnv *env, jclass clazz, jfloat value, jboolean invert, jlong function_pointer) {
	glSampleCoverageARBPROC glSampleCoverageARB = (glSampleCoverageARBPROC)((intptr_t)function_pointer);
	glSampleCoverageARB(value, invert);
}

