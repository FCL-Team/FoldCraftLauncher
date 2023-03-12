/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glVertexAttribParameteriAMDPROC) (GLuint index, GLenum pname, GLint param);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDInterleavedElements_nglVertexAttribParameteriAMD(JNIEnv *env, jclass clazz, jint index, jint pname, jint param, jlong function_pointer) {
	glVertexAttribParameteriAMDPROC glVertexAttribParameteriAMD = (glVertexAttribParameteriAMDPROC)((intptr_t)function_pointer);
	glVertexAttribParameteriAMD(index, pname, param);
}

