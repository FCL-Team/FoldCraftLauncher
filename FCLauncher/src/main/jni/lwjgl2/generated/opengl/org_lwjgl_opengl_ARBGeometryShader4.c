/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glProgramParameteriARBPROC) (GLuint program, GLenum pname, GLint value);
typedef void (APIENTRY *glFramebufferTextureARBPROC) (GLenum target, GLenum attachment, GLuint texture, GLint level);
typedef void (APIENTRY *glFramebufferTextureLayerARBPROC) (GLenum target, GLenum attachment, GLuint texture, GLint level, GLint layer);
typedef void (APIENTRY *glFramebufferTextureFaceARBPROC) (GLenum target, GLenum attachment, GLuint texture, GLint level, GLenum face);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGeometryShader4_nglProgramParameteriARB(JNIEnv *env, jclass clazz, jint program, jint pname, jint value, jlong function_pointer) {
	glProgramParameteriARBPROC glProgramParameteriARB = (glProgramParameteriARBPROC)((intptr_t)function_pointer);
	glProgramParameteriARB(program, pname, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGeometryShader4_nglFramebufferTextureARB(JNIEnv *env, jclass clazz, jint target, jint attachment, jint texture, jint level, jlong function_pointer) {
	glFramebufferTextureARBPROC glFramebufferTextureARB = (glFramebufferTextureARBPROC)((intptr_t)function_pointer);
	glFramebufferTextureARB(target, attachment, texture, level);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGeometryShader4_nglFramebufferTextureLayerARB(JNIEnv *env, jclass clazz, jint target, jint attachment, jint texture, jint level, jint layer, jlong function_pointer) {
	glFramebufferTextureLayerARBPROC glFramebufferTextureLayerARB = (glFramebufferTextureLayerARBPROC)((intptr_t)function_pointer);
	glFramebufferTextureLayerARB(target, attachment, texture, level, layer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGeometryShader4_nglFramebufferTextureFaceARB(JNIEnv *env, jclass clazz, jint target, jint attachment, jint texture, jint level, jint face, jlong function_pointer) {
	glFramebufferTextureFaceARBPROC glFramebufferTextureFaceARB = (glFramebufferTextureFaceARBPROC)((intptr_t)function_pointer);
	glFramebufferTextureFaceARB(target, attachment, texture, level, face);
}

