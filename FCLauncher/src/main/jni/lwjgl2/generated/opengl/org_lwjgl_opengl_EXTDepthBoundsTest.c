/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glDepthBoundsEXTPROC) (GLclampd zmin, GLclampd zmax);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDepthBoundsTest_nglDepthBoundsEXT(JNIEnv *env, jclass clazz, jdouble zmin, jdouble zmax, jlong function_pointer) {
	glDepthBoundsEXTPROC glDepthBoundsEXT = (glDepthBoundsEXTPROC)((intptr_t)function_pointer);
	glDepthBoundsEXT(zmin, zmax);
}

