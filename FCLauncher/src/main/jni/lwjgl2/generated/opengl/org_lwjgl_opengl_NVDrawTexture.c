/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glDrawTextureNVPROC) (GLuint texture, GLuint sampler, GLfloat x0, GLfloat y0, GLfloat x1, GLfloat y1, GLfloat z, GLfloat s0, GLfloat t0, GLfloat s1, GLfloat t1);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVDrawTexture_nglDrawTextureNV(JNIEnv *env, jclass clazz, jint texture, jint sampler, jfloat x0, jfloat y0, jfloat x1, jfloat y1, jfloat z, jfloat s0, jfloat t0, jfloat s1, jfloat t1, jlong function_pointer) {
	glDrawTextureNVPROC glDrawTextureNV = (glDrawTextureNVPROC)((intptr_t)function_pointer);
	glDrawTextureNV(texture, sampler, x0, y0, x1, y1, z, s0, t0, s1, t1);
}

