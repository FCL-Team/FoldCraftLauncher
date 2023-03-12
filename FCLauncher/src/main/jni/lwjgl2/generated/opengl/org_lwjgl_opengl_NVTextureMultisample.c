/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glTexImage2DMultisampleCoverageNVPROC) (GLenum target, GLsizei coverageSamples, GLsizei colorSamples, GLint internalFormat, GLsizei width, GLsizei height, GLboolean fixedSampleLocations);
typedef void (APIENTRY *glTexImage3DMultisampleCoverageNVPROC) (GLenum target, GLsizei coverageSamples, GLsizei colorSamples, GLint internalFormat, GLsizei width, GLsizei height, GLsizei depth, GLboolean fixedSampleLocations);
typedef void (APIENTRY *glTextureImage2DMultisampleNVPROC) (GLuint texture, GLenum target, GLsizei samples, GLint internalFormat, GLsizei width, GLsizei height, GLboolean fixedSampleLocations);
typedef void (APIENTRY *glTextureImage3DMultisampleNVPROC) (GLuint texture, GLenum target, GLsizei samples, GLint internalFormat, GLsizei width, GLsizei height, GLsizei depth, GLboolean fixedSampleLocations);
typedef void (APIENTRY *glTextureImage2DMultisampleCoverageNVPROC) (GLuint texture, GLenum target, GLsizei coverageSamples, GLsizei colorSamples, GLint internalFormat, GLsizei width, GLsizei height, GLboolean fixedSampleLocations);
typedef void (APIENTRY *glTextureImage3DMultisampleCoverageNVPROC) (GLuint texture, GLenum target, GLsizei coverageSamples, GLsizei colorSamples, GLint internalFormat, GLsizei width, GLsizei height, GLsizei depth, GLboolean fixedSampleLocations);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTextureMultisample_nglTexImage2DMultisampleCoverageNV(JNIEnv *env, jclass clazz, jint target, jint coverageSamples, jint colorSamples, jint internalFormat, jint width, jint height, jboolean fixedSampleLocations, jlong function_pointer) {
	glTexImage2DMultisampleCoverageNVPROC glTexImage2DMultisampleCoverageNV = (glTexImage2DMultisampleCoverageNVPROC)((intptr_t)function_pointer);
	glTexImage2DMultisampleCoverageNV(target, coverageSamples, colorSamples, internalFormat, width, height, fixedSampleLocations);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTextureMultisample_nglTexImage3DMultisampleCoverageNV(JNIEnv *env, jclass clazz, jint target, jint coverageSamples, jint colorSamples, jint internalFormat, jint width, jint height, jint depth, jboolean fixedSampleLocations, jlong function_pointer) {
	glTexImage3DMultisampleCoverageNVPROC glTexImage3DMultisampleCoverageNV = (glTexImage3DMultisampleCoverageNVPROC)((intptr_t)function_pointer);
	glTexImage3DMultisampleCoverageNV(target, coverageSamples, colorSamples, internalFormat, width, height, depth, fixedSampleLocations);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTextureMultisample_nglTextureImage2DMultisampleNV(JNIEnv *env, jclass clazz, jint texture, jint target, jint samples, jint internalFormat, jint width, jint height, jboolean fixedSampleLocations, jlong function_pointer) {
	glTextureImage2DMultisampleNVPROC glTextureImage2DMultisampleNV = (glTextureImage2DMultisampleNVPROC)((intptr_t)function_pointer);
	glTextureImage2DMultisampleNV(texture, target, samples, internalFormat, width, height, fixedSampleLocations);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTextureMultisample_nglTextureImage3DMultisampleNV(JNIEnv *env, jclass clazz, jint texture, jint target, jint samples, jint internalFormat, jint width, jint height, jint depth, jboolean fixedSampleLocations, jlong function_pointer) {
	glTextureImage3DMultisampleNVPROC glTextureImage3DMultisampleNV = (glTextureImage3DMultisampleNVPROC)((intptr_t)function_pointer);
	glTextureImage3DMultisampleNV(texture, target, samples, internalFormat, width, height, depth, fixedSampleLocations);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTextureMultisample_nglTextureImage2DMultisampleCoverageNV(JNIEnv *env, jclass clazz, jint texture, jint target, jint coverageSamples, jint colorSamples, jint internalFormat, jint width, jint height, jboolean fixedSampleLocations, jlong function_pointer) {
	glTextureImage2DMultisampleCoverageNVPROC glTextureImage2DMultisampleCoverageNV = (glTextureImage2DMultisampleCoverageNVPROC)((intptr_t)function_pointer);
	glTextureImage2DMultisampleCoverageNV(texture, target, coverageSamples, colorSamples, internalFormat, width, height, fixedSampleLocations);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTextureMultisample_nglTextureImage3DMultisampleCoverageNV(JNIEnv *env, jclass clazz, jint texture, jint target, jint coverageSamples, jint colorSamples, jint internalFormat, jint width, jint height, jint depth, jboolean fixedSampleLocations, jlong function_pointer) {
	glTextureImage3DMultisampleCoverageNVPROC glTextureImage3DMultisampleCoverageNV = (glTextureImage3DMultisampleCoverageNVPROC)((intptr_t)function_pointer);
	glTextureImage3DMultisampleCoverageNV(texture, target, coverageSamples, colorSamples, internalFormat, width, height, depth, fixedSampleLocations);
}

