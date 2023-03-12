/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glPNTrianglesfATIPROC) (GLenum pname, GLfloat param);
typedef void (APIENTRY *glPNTrianglesiATIPROC) (GLenum pname, GLint param);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIPnTriangles_nglPNTrianglesfATI(JNIEnv *env, jclass clazz, jint pname, jfloat param, jlong function_pointer) {
	glPNTrianglesfATIPROC glPNTrianglesfATI = (glPNTrianglesfATIPROC)((intptr_t)function_pointer);
	glPNTrianglesfATI(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIPnTriangles_nglPNTrianglesiATI(JNIEnv *env, jclass clazz, jint pname, jint param, jlong function_pointer) {
	glPNTrianglesiATIPROC glPNTrianglesiATI = (glPNTrianglesiATIPROC)((intptr_t)function_pointer);
	glPNTrianglesiATI(pname, param);
}

