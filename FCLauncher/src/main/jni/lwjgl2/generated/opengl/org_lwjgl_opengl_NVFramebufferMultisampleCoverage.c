/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glRenderbufferStorageMultisampleCoverageNVPROC) (GLenum target, GLsizei coverageSamples, GLsizei colorSamples, GLenum internalformat, GLsizei width, GLsizei height);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVFramebufferMultisampleCoverage_nglRenderbufferStorageMultisampleCoverageNV(JNIEnv *env, jclass clazz, jint target, jint coverageSamples, jint colorSamples, jint internalformat, jint width, jint height, jlong function_pointer) {
	glRenderbufferStorageMultisampleCoverageNVPROC glRenderbufferStorageMultisampleCoverageNV = (glRenderbufferStorageMultisampleCoverageNVPROC)((intptr_t)function_pointer);
	glRenderbufferStorageMultisampleCoverageNV(target, coverageSamples, colorSamples, internalformat, width, height);
}

