/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glProgramParameteriEXTPROC) (GLuint program, GLenum pname, GLint value);
typedef void (APIENTRY *glFramebufferTextureEXTPROC) (GLenum target, GLenum attachment, GLuint texture, GLint level);
typedef void (APIENTRY *glFramebufferTextureLayerEXTPROC) (GLenum target, GLenum attachment, GLuint texture, GLint level, GLint layer);
typedef void (APIENTRY *glFramebufferTextureFaceEXTPROC) (GLenum target, GLenum attachment, GLuint texture, GLint level, GLenum face);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGeometryShader4_nglProgramParameteriEXT(JNIEnv *env, jclass clazz, jint program, jint pname, jint value, jlong function_pointer) {
	glProgramParameteriEXTPROC glProgramParameteriEXT = (glProgramParameteriEXTPROC)((intptr_t)function_pointer);
	glProgramParameteriEXT(program, pname, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGeometryShader4_nglFramebufferTextureEXT(JNIEnv *env, jclass clazz, jint target, jint attachment, jint texture, jint level, jlong function_pointer) {
	glFramebufferTextureEXTPROC glFramebufferTextureEXT = (glFramebufferTextureEXTPROC)((intptr_t)function_pointer);
	glFramebufferTextureEXT(target, attachment, texture, level);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGeometryShader4_nglFramebufferTextureLayerEXT(JNIEnv *env, jclass clazz, jint target, jint attachment, jint texture, jint level, jint layer, jlong function_pointer) {
	glFramebufferTextureLayerEXTPROC glFramebufferTextureLayerEXT = (glFramebufferTextureLayerEXTPROC)((intptr_t)function_pointer);
	glFramebufferTextureLayerEXT(target, attachment, texture, level, layer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGeometryShader4_nglFramebufferTextureFaceEXT(JNIEnv *env, jclass clazz, jint target, jint attachment, jint texture, jint level, jint face, jlong function_pointer) {
	glFramebufferTextureFaceEXTPROC glFramebufferTextureFaceEXT = (glFramebufferTextureFaceEXTPROC)((intptr_t)function_pointer);
	glFramebufferTextureFaceEXT(target, attachment, texture, level, face);
}

