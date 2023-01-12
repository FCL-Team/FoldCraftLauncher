/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBeginVideoCaptureNVPROC) (GLuint video_capture_slot);
typedef void (APIENTRY *glBindVideoCaptureStreamBufferNVPROC) (GLuint video_capture_slot, GLuint stream, GLenum frame_region, GLintptrARB offset);
typedef void (APIENTRY *glBindVideoCaptureStreamTextureNVPROC) (GLuint video_capture_slot, GLuint stream, GLenum frame_region, GLenum target, GLuint texture);
typedef void (APIENTRY *glEndVideoCaptureNVPROC) (GLuint video_capture_slot);
typedef void (APIENTRY *glGetVideoCaptureivNVPROC) (GLuint video_capture_slot, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetVideoCaptureStreamivNVPROC) (GLuint video_capture_slot, GLuint stream, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetVideoCaptureStreamfvNVPROC) (GLuint video_capture_slot, GLuint stream, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetVideoCaptureStreamdvNVPROC) (GLuint video_capture_slot, GLuint stream, GLenum pname, GLdouble * params);
typedef GLenum (APIENTRY *glVideoCaptureNVPROC) (GLuint video_capture_slot, GLuint * sequence_num, GLuint64EXT * capture_time);
typedef void (APIENTRY *glVideoCaptureStreamParameterivNVPROC) (GLuint video_capture_slot, GLuint stream, GLenum pname, const GLint * params);
typedef void (APIENTRY *glVideoCaptureStreamParameterfvNVPROC) (GLuint video_capture_slot, GLuint stream, GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glVideoCaptureStreamParameterdvNVPROC) (GLuint video_capture_slot, GLuint stream, GLenum pname, const GLdouble * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVideoCapture_nglBeginVideoCaptureNV(JNIEnv *env, jclass clazz, jint video_capture_slot, jlong function_pointer) {
	glBeginVideoCaptureNVPROC glBeginVideoCaptureNV = (glBeginVideoCaptureNVPROC)((intptr_t)function_pointer);
	glBeginVideoCaptureNV(video_capture_slot);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVideoCapture_nglBindVideoCaptureStreamBufferNV(JNIEnv *env, jclass clazz, jint video_capture_slot, jint stream, jint frame_region, jlong offset, jlong function_pointer) {
	glBindVideoCaptureStreamBufferNVPROC glBindVideoCaptureStreamBufferNV = (glBindVideoCaptureStreamBufferNVPROC)((intptr_t)function_pointer);
	glBindVideoCaptureStreamBufferNV(video_capture_slot, stream, frame_region, offset);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVideoCapture_nglBindVideoCaptureStreamTextureNV(JNIEnv *env, jclass clazz, jint video_capture_slot, jint stream, jint frame_region, jint target, jint texture, jlong function_pointer) {
	glBindVideoCaptureStreamTextureNVPROC glBindVideoCaptureStreamTextureNV = (glBindVideoCaptureStreamTextureNVPROC)((intptr_t)function_pointer);
	glBindVideoCaptureStreamTextureNV(video_capture_slot, stream, frame_region, target, texture);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVideoCapture_nglEndVideoCaptureNV(JNIEnv *env, jclass clazz, jint video_capture_slot, jlong function_pointer) {
	glEndVideoCaptureNVPROC glEndVideoCaptureNV = (glEndVideoCaptureNVPROC)((intptr_t)function_pointer);
	glEndVideoCaptureNV(video_capture_slot);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVideoCapture_nglGetVideoCaptureivNV(JNIEnv *env, jclass clazz, jint video_capture_slot, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetVideoCaptureivNVPROC glGetVideoCaptureivNV = (glGetVideoCaptureivNVPROC)((intptr_t)function_pointer);
	glGetVideoCaptureivNV(video_capture_slot, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVideoCapture_nglGetVideoCaptureStreamivNV(JNIEnv *env, jclass clazz, jint video_capture_slot, jint stream, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetVideoCaptureStreamivNVPROC glGetVideoCaptureStreamivNV = (glGetVideoCaptureStreamivNVPROC)((intptr_t)function_pointer);
	glGetVideoCaptureStreamivNV(video_capture_slot, stream, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVideoCapture_nglGetVideoCaptureStreamfvNV(JNIEnv *env, jclass clazz, jint video_capture_slot, jint stream, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetVideoCaptureStreamfvNVPROC glGetVideoCaptureStreamfvNV = (glGetVideoCaptureStreamfvNVPROC)((intptr_t)function_pointer);
	glGetVideoCaptureStreamfvNV(video_capture_slot, stream, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVideoCapture_nglGetVideoCaptureStreamdvNV(JNIEnv *env, jclass clazz, jint video_capture_slot, jint stream, jint pname, jlong params, jlong function_pointer) {
	GLdouble *params_address = (GLdouble *)(intptr_t)params;
	glGetVideoCaptureStreamdvNVPROC glGetVideoCaptureStreamdvNV = (glGetVideoCaptureStreamdvNVPROC)((intptr_t)function_pointer);
	glGetVideoCaptureStreamdvNV(video_capture_slot, stream, pname, params_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_NVVideoCapture_nglVideoCaptureNV(JNIEnv *env, jclass clazz, jint video_capture_slot, jlong sequence_num, jlong capture_time, jlong function_pointer) {
	GLuint *sequence_num_address = (GLuint *)(intptr_t)sequence_num;
	GLuint64EXT *capture_time_address = (GLuint64EXT *)(intptr_t)capture_time;
	glVideoCaptureNVPROC glVideoCaptureNV = (glVideoCaptureNVPROC)((intptr_t)function_pointer);
	GLenum __result = glVideoCaptureNV(video_capture_slot, sequence_num_address, capture_time_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVideoCapture_nglVideoCaptureStreamParameterivNV(JNIEnv *env, jclass clazz, jint video_capture_slot, jint stream, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glVideoCaptureStreamParameterivNVPROC glVideoCaptureStreamParameterivNV = (glVideoCaptureStreamParameterivNVPROC)((intptr_t)function_pointer);
	glVideoCaptureStreamParameterivNV(video_capture_slot, stream, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVideoCapture_nglVideoCaptureStreamParameterfvNV(JNIEnv *env, jclass clazz, jint video_capture_slot, jint stream, jint pname, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glVideoCaptureStreamParameterfvNVPROC glVideoCaptureStreamParameterfvNV = (glVideoCaptureStreamParameterfvNVPROC)((intptr_t)function_pointer);
	glVideoCaptureStreamParameterfvNV(video_capture_slot, stream, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVideoCapture_nglVideoCaptureStreamParameterdvNV(JNIEnv *env, jclass clazz, jint video_capture_slot, jint stream, jint pname, jlong params, jlong function_pointer) {
	const GLdouble *params_address = (const GLdouble *)(intptr_t)params;
	glVideoCaptureStreamParameterdvNVPROC glVideoCaptureStreamParameterdvNV = (glVideoCaptureStreamParameterdvNVPROC)((intptr_t)function_pointer);
	glVideoCaptureStreamParameterdvNV(video_capture_slot, stream, pname, params_address);
}

