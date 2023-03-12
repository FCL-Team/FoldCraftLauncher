/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glPresentFrameKeyedNVPROC) (GLuint video_slot, GLuint64EXT minPresentTime, GLuint beginPresentTimeId, GLuint presentDurationId, GLenum type, GLenum target0, GLuint fill0, GLuint key0, GLenum target1, GLuint fill1, GLuint key1);
typedef void (APIENTRY *glPresentFrameDualFillNVPROC) (GLuint video_slot, GLuint64EXT minPresentTime, GLuint beginPresentTimeId, GLuint presentDurationId, GLenum type, GLenum target0, GLuint fill0, GLenum target1, GLuint fill1, GLenum target2, GLuint fill2, GLenum target3, GLuint fill3);
typedef void (APIENTRY *glGetVideoivNVPROC) (GLuint video_slot, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetVideouivNVPROC) (GLuint video_slot, GLenum pname, GLuint * params);
typedef void (APIENTRY *glGetVideoi64vNVPROC) (GLuint video_slot, GLenum pname, GLint64EXT * params);
typedef void (APIENTRY *glGetVideoui64vNVPROC) (GLuint video_slot, GLenum pname, GLuint64EXT * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPresentVideo_nglPresentFrameKeyedNV(JNIEnv *env, jclass clazz, jint video_slot, jlong minPresentTime, jint beginPresentTimeId, jint presentDurationId, jint type, jint target0, jint fill0, jint key0, jint target1, jint fill1, jint key1, jlong function_pointer) {
	glPresentFrameKeyedNVPROC glPresentFrameKeyedNV = (glPresentFrameKeyedNVPROC)((intptr_t)function_pointer);
	glPresentFrameKeyedNV(video_slot, minPresentTime, beginPresentTimeId, presentDurationId, type, target0, fill0, key0, target1, fill1, key1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPresentVideo_nglPresentFrameDualFillNV(JNIEnv *env, jclass clazz, jint video_slot, jlong minPresentTime, jint beginPresentTimeId, jint presentDurationId, jint type, jint target0, jint fill0, jint target1, jint fill1, jint target2, jint fill2, jint target3, jint fill3, jlong function_pointer) {
	glPresentFrameDualFillNVPROC glPresentFrameDualFillNV = (glPresentFrameDualFillNVPROC)((intptr_t)function_pointer);
	glPresentFrameDualFillNV(video_slot, minPresentTime, beginPresentTimeId, presentDurationId, type, target0, fill0, target1, fill1, target2, fill2, target3, fill3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPresentVideo_nglGetVideoivNV(JNIEnv *env, jclass clazz, jint video_slot, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetVideoivNVPROC glGetVideoivNV = (glGetVideoivNVPROC)((intptr_t)function_pointer);
	glGetVideoivNV(video_slot, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPresentVideo_nglGetVideouivNV(JNIEnv *env, jclass clazz, jint video_slot, jint pname, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetVideouivNVPROC glGetVideouivNV = (glGetVideouivNVPROC)((intptr_t)function_pointer);
	glGetVideouivNV(video_slot, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPresentVideo_nglGetVideoi64vNV(JNIEnv *env, jclass clazz, jint video_slot, jint pname, jlong params, jlong function_pointer) {
	GLint64EXT *params_address = (GLint64EXT *)(intptr_t)params;
	glGetVideoi64vNVPROC glGetVideoi64vNV = (glGetVideoi64vNVPROC)((intptr_t)function_pointer);
	glGetVideoi64vNV(video_slot, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPresentVideo_nglGetVideoui64vNV(JNIEnv *env, jclass clazz, jint video_slot, jint pname, jlong params, jlong function_pointer) {
	GLuint64EXT *params_address = (GLuint64EXT *)(intptr_t)params;
	glGetVideoui64vNVPROC glGetVideoui64vNV = (glGetVideoui64vNVPROC)((intptr_t)function_pointer);
	glGetVideoui64vNV(video_slot, pname, params_address);
}

