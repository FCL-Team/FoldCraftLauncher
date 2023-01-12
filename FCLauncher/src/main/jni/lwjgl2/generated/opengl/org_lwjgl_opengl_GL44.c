/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBufferStoragePROC) (GLenum target, GLsizeiptr size, const GLvoid * data, GLbitfield flags);
typedef void (APIENTRY *glClearTexImagePROC) (GLuint texture, GLint level, GLenum format, GLenum type, const GLvoid * data);
typedef void (APIENTRY *glClearTexSubImagePROC) (GLuint texture, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLenum format, GLenum type, const GLvoid * data);
typedef void (APIENTRY *glBindBuffersBasePROC) (GLenum target, GLuint first, GLsizei count, const GLuint * buffers);
typedef void (APIENTRY *glBindBuffersRangePROC) (GLenum target, GLuint first, GLsizei count, const GLuint * buffers, const GLintptr * offsets, const GLsizeiptr * sizes);
typedef void (APIENTRY *glBindTexturesPROC) (GLuint first, GLsizei count, const GLuint * textures);
typedef void (APIENTRY *glBindSamplersPROC) (GLuint first, GLsizei count, const GLuint * samplers);
typedef void (APIENTRY *glBindImageTexturesPROC) (GLuint first, GLsizei count, const GLuint * textures);
typedef void (APIENTRY *glBindVertexBuffersPROC) (GLuint first, GLsizei count, const GLuint * buffers, const GLintptr * offsets, const GLsizei * strides);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL44_nglBufferStorage(JNIEnv *env, jclass clazz, jint target, jlong size, jlong data, jint flags, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glBufferStoragePROC glBufferStorage = (glBufferStoragePROC)((intptr_t)function_pointer);
	glBufferStorage(target, size, data_address, flags);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL44_nglClearTexImage(JNIEnv *env, jclass clazz, jint texture, jint level, jint format, jint type, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glClearTexImagePROC glClearTexImage = (glClearTexImagePROC)((intptr_t)function_pointer);
	glClearTexImage(texture, level, format, type, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL44_nglClearTexSubImage(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint type, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glClearTexSubImagePROC glClearTexSubImage = (glClearTexSubImagePROC)((intptr_t)function_pointer);
	glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL44_nglBindBuffersBase(JNIEnv *env, jclass clazz, jint target, jint first, jint count, jlong buffers, jlong function_pointer) {
	const GLuint *buffers_address = (const GLuint *)(intptr_t)buffers;
	glBindBuffersBasePROC glBindBuffersBase = (glBindBuffersBasePROC)((intptr_t)function_pointer);
	glBindBuffersBase(target, first, count, buffers_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL44_nglBindBuffersRange(JNIEnv *env, jclass clazz, jint target, jint first, jint count, jlong buffers, jlong offsets, jlong sizes, jlong function_pointer) {
	const GLuint *buffers_address = (const GLuint *)(intptr_t)buffers;
	const GLintptr *offsets_address = (const GLintptr *)(intptr_t)offsets;
	const GLsizeiptr *sizes_address = (const GLsizeiptr *)(intptr_t)sizes;
	glBindBuffersRangePROC glBindBuffersRange = (glBindBuffersRangePROC)((intptr_t)function_pointer);
	glBindBuffersRange(target, first, count, buffers_address, offsets_address, sizes_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL44_nglBindTextures(JNIEnv *env, jclass clazz, jint first, jint count, jlong textures, jlong function_pointer) {
	const GLuint *textures_address = (const GLuint *)(intptr_t)textures;
	glBindTexturesPROC glBindTextures = (glBindTexturesPROC)((intptr_t)function_pointer);
	glBindTextures(first, count, textures_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL44_nglBindSamplers(JNIEnv *env, jclass clazz, jint first, jint count, jlong samplers, jlong function_pointer) {
	const GLuint *samplers_address = (const GLuint *)(intptr_t)samplers;
	glBindSamplersPROC glBindSamplers = (glBindSamplersPROC)((intptr_t)function_pointer);
	glBindSamplers(first, count, samplers_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL44_nglBindImageTextures(JNIEnv *env, jclass clazz, jint first, jint count, jlong textures, jlong function_pointer) {
	const GLuint *textures_address = (const GLuint *)(intptr_t)textures;
	glBindImageTexturesPROC glBindImageTextures = (glBindImageTexturesPROC)((intptr_t)function_pointer);
	glBindImageTextures(first, count, textures_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL44_nglBindVertexBuffers(JNIEnv *env, jclass clazz, jint first, jint count, jlong buffers, jlong offsets, jlong strides, jlong function_pointer) {
	const GLuint *buffers_address = (const GLuint *)(intptr_t)buffers;
	const GLintptr *offsets_address = (const GLintptr *)(intptr_t)offsets;
	const GLsizei *strides_address = (const GLsizei *)(intptr_t)strides;
	glBindVertexBuffersPROC glBindVertexBuffers = (glBindVertexBuffersPROC)((intptr_t)function_pointer);
	glBindVertexBuffers(first, count, buffers_address, offsets_address, strides_address);
}

