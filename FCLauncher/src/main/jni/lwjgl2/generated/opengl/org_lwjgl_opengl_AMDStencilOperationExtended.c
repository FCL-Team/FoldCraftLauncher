/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glStencilOpValueAMDPROC) (GLenum face, GLuint value);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDStencilOperationExtended_nglStencilOpValueAMD(JNIEnv *env, jclass clazz, jint face, jint value, jlong function_pointer) {
	glStencilOpValueAMDPROC glStencilOpValueAMD = (glStencilOpValueAMDPROC)((intptr_t)function_pointer);
	glStencilOpValueAMD(face, value);
}

