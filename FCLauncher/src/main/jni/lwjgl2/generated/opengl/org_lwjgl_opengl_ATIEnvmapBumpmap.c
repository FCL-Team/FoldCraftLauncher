/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glTexBumpParameterfvATIPROC) (GLenum pname, const GLfloat * param);
typedef void (APIENTRY *glTexBumpParameterivATIPROC) (GLenum pname, const GLint * param);
typedef void (APIENTRY *glGetTexBumpParameterfvATIPROC) (GLenum pname, GLfloat * param);
typedef void (APIENTRY *glGetTexBumpParameterivATIPROC) (GLenum pname, GLint * param);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIEnvmapBumpmap_nglTexBumpParameterfvATI(JNIEnv *env, jclass clazz, jint pname, jlong param, jlong function_pointer) {
	const GLfloat *param_address = (const GLfloat *)(intptr_t)param;
	glTexBumpParameterfvATIPROC glTexBumpParameterfvATI = (glTexBumpParameterfvATIPROC)((intptr_t)function_pointer);
	glTexBumpParameterfvATI(pname, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIEnvmapBumpmap_nglTexBumpParameterivATI(JNIEnv *env, jclass clazz, jint pname, jlong param, jlong function_pointer) {
	const GLint *param_address = (const GLint *)(intptr_t)param;
	glTexBumpParameterivATIPROC glTexBumpParameterivATI = (glTexBumpParameterivATIPROC)((intptr_t)function_pointer);
	glTexBumpParameterivATI(pname, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIEnvmapBumpmap_nglGetTexBumpParameterfvATI(JNIEnv *env, jclass clazz, jint pname, jlong param, jlong function_pointer) {
	GLfloat *param_address = (GLfloat *)(intptr_t)param;
	glGetTexBumpParameterfvATIPROC glGetTexBumpParameterfvATI = (glGetTexBumpParameterfvATIPROC)((intptr_t)function_pointer);
	glGetTexBumpParameterfvATI(pname, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIEnvmapBumpmap_nglGetTexBumpParameterivATI(JNIEnv *env, jclass clazz, jint pname, jlong param, jlong function_pointer) {
	GLint *param_address = (GLint *)(intptr_t)param;
	glGetTexBumpParameterivATIPROC glGetTexBumpParameterivATI = (glGetTexBumpParameterivATIPROC)((intptr_t)function_pointer);
	glGetTexBumpParameterivATI(pname, param_address);
}

