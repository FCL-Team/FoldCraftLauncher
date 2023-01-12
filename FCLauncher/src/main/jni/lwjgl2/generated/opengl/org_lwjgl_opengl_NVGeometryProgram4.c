/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glProgramVertexLimitNVPROC) (GLenum target, GLint limit);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVGeometryProgram4_nglProgramVertexLimitNV(JNIEnv *env, jclass clazz, jint target, jint limit, jlong function_pointer) {
	glProgramVertexLimitNVPROC glProgramVertexLimitNV = (glProgramVertexLimitNVPROC)((intptr_t)function_pointer);
	glProgramVertexLimitNV(target, limit);
}

