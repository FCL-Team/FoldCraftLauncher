/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glGetBufferParameteri64vPROC) (GLenum target, GLenum pname, GLint64EXT * params);
typedef void (APIENTRY *glDrawElementsBaseVertexPROC) (GLenum mode, GLsizei count, GLenum type, const GLvoid * indices, GLint basevertex);
typedef void (APIENTRY *glDrawRangeElementsBaseVertexPROC) (GLenum mode, GLuint start, GLuint end, GLsizei count, GLenum type, const GLvoid * indices, GLint basevertex);
typedef void (APIENTRY *glDrawElementsInstancedBaseVertexPROC) (GLenum mode, GLsizei count, GLenum type, const GLvoid * indices, GLsizei primcount, GLint basevertex);
typedef void (APIENTRY *glProvokingVertexPROC) (GLenum mode);
typedef void (APIENTRY *glTexImage2DMultisamplePROC) (GLenum target, GLsizei samples, GLint internalformat, GLsizei width, GLsizei height, GLboolean fixedsamplelocations);
typedef void (APIENTRY *glTexImage3DMultisamplePROC) (GLenum target, GLsizei samples, GLint internalformat, GLsizei width, GLsizei height, GLsizei depth, GLboolean fixedsamplelocations);
typedef void (APIENTRY *glGetMultisamplefvPROC) (GLenum pname, GLuint index, GLfloat * val);
typedef void (APIENTRY *glSampleMaskiPROC) (GLuint index, GLbitfield mask);
typedef void (APIENTRY *glFramebufferTexturePROC) (GLenum target, GLenum attachment, GLuint texture, GLint level);
typedef GLsync (APIENTRY *glFenceSyncPROC) (GLenum condition, GLbitfield flags);
typedef GLboolean (APIENTRY *glIsSyncPROC) (GLsync sync);
typedef void (APIENTRY *glDeleteSyncPROC) (GLsync sync);
typedef GLenum (APIENTRY *glClientWaitSyncPROC) (GLsync sync, GLbitfield flags, GLuint64 timeout);
typedef void (APIENTRY *glWaitSyncPROC) (GLsync sync, GLbitfield flags, GLuint64 timeout);
typedef void (APIENTRY *glGetInteger64vPROC) (GLenum pname, GLint64 * data);
typedef void (APIENTRY *glGetInteger64i_vPROC) (GLenum value, GLuint index, GLint64 * data);
typedef void (APIENTRY *glGetSyncivPROC) (GLsync sync, GLenum pname, GLsizei bufSize, GLsizei * length, GLint * values);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglGetBufferParameteri64v(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint64EXT *params_address = (GLint64EXT *)(intptr_t)params;
	glGetBufferParameteri64vPROC glGetBufferParameteri64v = (glGetBufferParameteri64vPROC)((intptr_t)function_pointer);
	glGetBufferParameteri64v(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglDrawElementsBaseVertex(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices, jint basevertex, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)indices;
	glDrawElementsBaseVertexPROC glDrawElementsBaseVertex = (glDrawElementsBaseVertexPROC)((intptr_t)function_pointer);
	glDrawElementsBaseVertex(mode, count, type, indices_address, basevertex);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglDrawElementsBaseVertexBO(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices_buffer_offset, jint basevertex, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)offsetToPointer(indices_buffer_offset);
	glDrawElementsBaseVertexPROC glDrawElementsBaseVertex = (glDrawElementsBaseVertexPROC)((intptr_t)function_pointer);
	glDrawElementsBaseVertex(mode, count, type, indices_address, basevertex);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglDrawRangeElementsBaseVertex(JNIEnv *env, jclass clazz, jint mode, jint start, jint end, jint count, jint type, jlong indices, jint basevertex, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)indices;
	glDrawRangeElementsBaseVertexPROC glDrawRangeElementsBaseVertex = (glDrawRangeElementsBaseVertexPROC)((intptr_t)function_pointer);
	glDrawRangeElementsBaseVertex(mode, start, end, count, type, indices_address, basevertex);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglDrawRangeElementsBaseVertexBO(JNIEnv *env, jclass clazz, jint mode, jint start, jint end, jint count, jint type, jlong indices_buffer_offset, jint basevertex, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)offsetToPointer(indices_buffer_offset);
	glDrawRangeElementsBaseVertexPROC glDrawRangeElementsBaseVertex = (glDrawRangeElementsBaseVertexPROC)((intptr_t)function_pointer);
	glDrawRangeElementsBaseVertex(mode, start, end, count, type, indices_address, basevertex);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglDrawElementsInstancedBaseVertex(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices, jint primcount, jint basevertex, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)indices;
	glDrawElementsInstancedBaseVertexPROC glDrawElementsInstancedBaseVertex = (glDrawElementsInstancedBaseVertexPROC)((intptr_t)function_pointer);
	glDrawElementsInstancedBaseVertex(mode, count, type, indices_address, primcount, basevertex);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglDrawElementsInstancedBaseVertexBO(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices_buffer_offset, jint primcount, jint basevertex, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)offsetToPointer(indices_buffer_offset);
	glDrawElementsInstancedBaseVertexPROC glDrawElementsInstancedBaseVertex = (glDrawElementsInstancedBaseVertexPROC)((intptr_t)function_pointer);
	glDrawElementsInstancedBaseVertex(mode, count, type, indices_address, primcount, basevertex);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglProvokingVertex(JNIEnv *env, jclass clazz, jint mode, jlong function_pointer) {
	glProvokingVertexPROC glProvokingVertex = (glProvokingVertexPROC)((intptr_t)function_pointer);
	glProvokingVertex(mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglTexImage2DMultisample(JNIEnv *env, jclass clazz, jint target, jint samples, jint internalformat, jint width, jint height, jboolean fixedsamplelocations, jlong function_pointer) {
	glTexImage2DMultisamplePROC glTexImage2DMultisample = (glTexImage2DMultisamplePROC)((intptr_t)function_pointer);
	glTexImage2DMultisample(target, samples, internalformat, width, height, fixedsamplelocations);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglTexImage3DMultisample(JNIEnv *env, jclass clazz, jint target, jint samples, jint internalformat, jint width, jint height, jint depth, jboolean fixedsamplelocations, jlong function_pointer) {
	glTexImage3DMultisamplePROC glTexImage3DMultisample = (glTexImage3DMultisamplePROC)((intptr_t)function_pointer);
	glTexImage3DMultisample(target, samples, internalformat, width, height, depth, fixedsamplelocations);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglGetMultisamplefv(JNIEnv *env, jclass clazz, jint pname, jint index, jlong val, jlong function_pointer) {
	GLfloat *val_address = (GLfloat *)(intptr_t)val;
	glGetMultisamplefvPROC glGetMultisamplefv = (glGetMultisamplefvPROC)((intptr_t)function_pointer);
	glGetMultisamplefv(pname, index, val_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglSampleMaski(JNIEnv *env, jclass clazz, jint index, jint mask, jlong function_pointer) {
	glSampleMaskiPROC glSampleMaski = (glSampleMaskiPROC)((intptr_t)function_pointer);
	glSampleMaski(index, mask);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglFramebufferTexture(JNIEnv *env, jclass clazz, jint target, jint attachment, jint texture, jint level, jlong function_pointer) {
	glFramebufferTexturePROC glFramebufferTexture = (glFramebufferTexturePROC)((intptr_t)function_pointer);
	glFramebufferTexture(target, attachment, texture, level);
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_GL32_nglFenceSync(JNIEnv *env, jclass clazz, jint condition, jint flags, jlong function_pointer) {
	glFenceSyncPROC glFenceSync = (glFenceSyncPROC)((intptr_t)function_pointer);
	GLsync __result = glFenceSync(condition, flags);
	return (intptr_t)__result;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL32_nglIsSync(JNIEnv *env, jclass clazz, jlong sync, jlong function_pointer) {
	glIsSyncPROC glIsSync = (glIsSyncPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsSync((GLsync)(intptr_t)sync);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglDeleteSync(JNIEnv *env, jclass clazz, jlong sync, jlong function_pointer) {
	glDeleteSyncPROC glDeleteSync = (glDeleteSyncPROC)((intptr_t)function_pointer);
	glDeleteSync((GLsync)(intptr_t)sync);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL32_nglClientWaitSync(JNIEnv *env, jclass clazz, jlong sync, jint flags, jlong timeout, jlong function_pointer) {
	glClientWaitSyncPROC glClientWaitSync = (glClientWaitSyncPROC)((intptr_t)function_pointer);
	GLenum __result = glClientWaitSync((GLsync)(intptr_t)sync, flags, timeout);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglWaitSync(JNIEnv *env, jclass clazz, jlong sync, jint flags, jlong timeout, jlong function_pointer) {
	glWaitSyncPROC glWaitSync = (glWaitSyncPROC)((intptr_t)function_pointer);
	glWaitSync((GLsync)(intptr_t)sync, flags, timeout);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglGetInteger64v(JNIEnv *env, jclass clazz, jint pname, jlong data, jlong function_pointer) {
	GLint64 *data_address = (GLint64 *)(intptr_t)data;
	glGetInteger64vPROC glGetInteger64v = (glGetInteger64vPROC)((intptr_t)function_pointer);
	glGetInteger64v(pname, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglGetInteger64i_1v(JNIEnv *env, jclass clazz, jint value, jint index, jlong data, jlong function_pointer) {
	GLint64 *data_address = (GLint64 *)(intptr_t)data;
	glGetInteger64i_vPROC glGetInteger64i_v = (glGetInteger64i_vPROC)((intptr_t)function_pointer);
	glGetInteger64i_v(value, index, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL32_nglGetSynciv(JNIEnv *env, jclass clazz, jlong sync, jint pname, jint bufSize, jlong length, jlong values, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLint *values_address = (GLint *)(intptr_t)values;
	glGetSyncivPROC glGetSynciv = (glGetSyncivPROC)((intptr_t)function_pointer);
	glGetSynciv((GLsync)(intptr_t)sync, pname, bufSize, length_address, values_address);
}

