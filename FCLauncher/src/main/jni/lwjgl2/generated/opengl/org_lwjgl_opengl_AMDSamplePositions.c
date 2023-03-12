/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glSetMultisamplefvAMDPROC) (GLenum pname, GLuint index, const GLfloat * val);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDSamplePositions_nglSetMultisamplefvAMD(JNIEnv *env, jclass clazz, jint pname, jint index, jlong val, jlong function_pointer) {
	const GLfloat *val_address = (const GLfloat *)(intptr_t)val;
	glSetMultisamplefvAMDPROC glSetMultisamplefvAMD = (glSetMultisamplefvAMDPROC)((intptr_t)function_pointer);
	glSetMultisamplefvAMD(pname, index, val_address);
}

