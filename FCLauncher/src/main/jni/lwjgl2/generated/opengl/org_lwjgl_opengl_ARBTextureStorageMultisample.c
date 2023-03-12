/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glTextureStorage2DMultisampleEXTPROC) (GLuint texture, GLenum target, GLsizei samples, GLenum internalformat, GLsizei width, GLsizei height, GLboolean fixedsamplelocations);
typedef void (APIENTRY *glTextureStorage3DMultisampleEXTPROC) (GLuint texture, GLenum target, GLsizei samples, GLenum internalformat, GLsizei width, GLsizei height, GLsizei depth, GLboolean fixedsamplelocations);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureStorageMultisample_nglTextureStorage2DMultisampleEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint samples, jint internalformat, jint width, jint height, jboolean fixedsamplelocations, jlong function_pointer) {
	glTextureStorage2DMultisampleEXTPROC glTextureStorage2DMultisampleEXT = (glTextureStorage2DMultisampleEXTPROC)((intptr_t)function_pointer);
	glTextureStorage2DMultisampleEXT(texture, target, samples, internalformat, width, height, fixedsamplelocations);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureStorageMultisample_nglTextureStorage3DMultisampleEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint samples, jint internalformat, jint width, jint height, jint depth, jboolean fixedsamplelocations, jlong function_pointer) {
	glTextureStorage3DMultisampleEXTPROC glTextureStorage3DMultisampleEXT = (glTextureStorage3DMultisampleEXTPROC)((intptr_t)function_pointer);
	glTextureStorage3DMultisampleEXT(texture, target, samples, internalformat, width, height, depth, fixedsamplelocations);
}

