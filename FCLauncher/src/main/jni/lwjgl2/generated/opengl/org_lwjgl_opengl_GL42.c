/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glGetActiveAtomicCounterBufferivPROC) (GLuint program, GLuint bufferIndex, GLenum pname, GLint * params);
typedef void (APIENTRY *glTexStorage1DPROC) (GLenum target, GLsizei levels, GLenum internalformat, GLsizei width);
typedef void (APIENTRY *glTexStorage2DPROC) (GLenum target, GLsizei levels, GLenum internalformat, GLsizei width, GLsizei height);
typedef void (APIENTRY *glTexStorage3DPROC) (GLenum target, GLsizei levels, GLenum internalformat, GLsizei width, GLsizei height, GLsizei depth);
typedef void (APIENTRY *glDrawTransformFeedbackInstancedPROC) (GLenum mode, GLuint id, GLsizei primcount);
typedef void (APIENTRY *glDrawTransformFeedbackStreamInstancedPROC) (GLenum mode, GLuint id, GLuint stream, GLsizei primcount);
typedef void (APIENTRY *glDrawArraysInstancedBaseInstancePROC) (GLenum mode, GLint first, GLsizei count, GLsizei primcount, GLuint baseinstance);
typedef void (APIENTRY *glDrawElementsInstancedBaseInstancePROC) (GLenum mode, GLsizei count, GLenum type, const GLvoid * indices, GLsizei primcount, GLuint baseinstance);
typedef void (APIENTRY *glDrawElementsInstancedBaseVertexBaseInstancePROC) (GLenum mode, GLsizei count, GLenum type, const GLvoid * indices, GLsizei primcount, GLint basevertex, GLuint baseinstance);
typedef void (APIENTRY *glBindImageTexturePROC) (GLuint unit, GLuint texture, GLint level, GLboolean layered, GLint layer, GLenum access, GLenum format);
typedef void (APIENTRY *glMemoryBarrierPROC) (GLbitfield barriers);
typedef void (APIENTRY *glGetInternalformativPROC) (GLenum target, GLenum internalformat, GLenum pname, GLsizei bufSize, GLint * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL42_nglGetActiveAtomicCounterBufferiv(JNIEnv *env, jclass clazz, jint program, jint bufferIndex, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetActiveAtomicCounterBufferivPROC glGetActiveAtomicCounterBufferiv = (glGetActiveAtomicCounterBufferivPROC)((intptr_t)function_pointer);
	glGetActiveAtomicCounterBufferiv(program, bufferIndex, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL42_nglTexStorage1D(JNIEnv *env, jclass clazz, jint target, jint levels, jint internalformat, jint width, jlong function_pointer) {
	glTexStorage1DPROC glTexStorage1D = (glTexStorage1DPROC)((intptr_t)function_pointer);
	glTexStorage1D(target, levels, internalformat, width);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL42_nglTexStorage2D(JNIEnv *env, jclass clazz, jint target, jint levels, jint internalformat, jint width, jint height, jlong function_pointer) {
	glTexStorage2DPROC glTexStorage2D = (glTexStorage2DPROC)((intptr_t)function_pointer);
	glTexStorage2D(target, levels, internalformat, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL42_nglTexStorage3D(JNIEnv *env, jclass clazz, jint target, jint levels, jint internalformat, jint width, jint height, jint depth, jlong function_pointer) {
	glTexStorage3DPROC glTexStorage3D = (glTexStorage3DPROC)((intptr_t)function_pointer);
	glTexStorage3D(target, levels, internalformat, width, height, depth);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL42_nglDrawTransformFeedbackInstanced(JNIEnv *env, jclass clazz, jint mode, jint id, jint primcount, jlong function_pointer) {
	glDrawTransformFeedbackInstancedPROC glDrawTransformFeedbackInstanced = (glDrawTransformFeedbackInstancedPROC)((intptr_t)function_pointer);
	glDrawTransformFeedbackInstanced(mode, id, primcount);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL42_nglDrawTransformFeedbackStreamInstanced(JNIEnv *env, jclass clazz, jint mode, jint id, jint stream, jint primcount, jlong function_pointer) {
	glDrawTransformFeedbackStreamInstancedPROC glDrawTransformFeedbackStreamInstanced = (glDrawTransformFeedbackStreamInstancedPROC)((intptr_t)function_pointer);
	glDrawTransformFeedbackStreamInstanced(mode, id, stream, primcount);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL42_nglDrawArraysInstancedBaseInstance(JNIEnv *env, jclass clazz, jint mode, jint first, jint count, jint primcount, jint baseinstance, jlong function_pointer) {
	glDrawArraysInstancedBaseInstancePROC glDrawArraysInstancedBaseInstance = (glDrawArraysInstancedBaseInstancePROC)((intptr_t)function_pointer);
	glDrawArraysInstancedBaseInstance(mode, first, count, primcount, baseinstance);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL42_nglDrawElementsInstancedBaseInstance(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices, jint primcount, jint baseinstance, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)indices;
	glDrawElementsInstancedBaseInstancePROC glDrawElementsInstancedBaseInstance = (glDrawElementsInstancedBaseInstancePROC)((intptr_t)function_pointer);
	glDrawElementsInstancedBaseInstance(mode, count, type, indices_address, primcount, baseinstance);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL42_nglDrawElementsInstancedBaseInstanceBO(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices_buffer_offset, jint primcount, jint baseinstance, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)offsetToPointer(indices_buffer_offset);
	glDrawElementsInstancedBaseInstancePROC glDrawElementsInstancedBaseInstance = (glDrawElementsInstancedBaseInstancePROC)((intptr_t)function_pointer);
	glDrawElementsInstancedBaseInstance(mode, count, type, indices_address, primcount, baseinstance);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL42_nglDrawElementsInstancedBaseVertexBaseInstance(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices, jint primcount, jint basevertex, jint baseinstance, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)indices;
	glDrawElementsInstancedBaseVertexBaseInstancePROC glDrawElementsInstancedBaseVertexBaseInstance = (glDrawElementsInstancedBaseVertexBaseInstancePROC)((intptr_t)function_pointer);
	glDrawElementsInstancedBaseVertexBaseInstance(mode, count, type, indices_address, primcount, basevertex, baseinstance);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL42_nglDrawElementsInstancedBaseVertexBaseInstanceBO(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices_buffer_offset, jint primcount, jint basevertex, jint baseinstance, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)offsetToPointer(indices_buffer_offset);
	glDrawElementsInstancedBaseVertexBaseInstancePROC glDrawElementsInstancedBaseVertexBaseInstance = (glDrawElementsInstancedBaseVertexBaseInstancePROC)((intptr_t)function_pointer);
	glDrawElementsInstancedBaseVertexBaseInstance(mode, count, type, indices_address, primcount, basevertex, baseinstance);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL42_nglBindImageTexture(JNIEnv *env, jclass clazz, jint unit, jint texture, jint level, jboolean layered, jint layer, jint access, jint format, jlong function_pointer) {
	glBindImageTexturePROC glBindImageTexture = (glBindImageTexturePROC)((intptr_t)function_pointer);
	glBindImageTexture(unit, texture, level, layered, layer, access, format);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL42_nglMemoryBarrier(JNIEnv *env, jclass clazz, jint barriers, jlong function_pointer) {
	glMemoryBarrierPROC glMemoryBarrier = (glMemoryBarrierPROC)((intptr_t)function_pointer);
	glMemoryBarrier(barriers);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL42_nglGetInternalformativ(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint pname, jint bufSize, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetInternalformativPROC glGetInternalformativ = (glGetInternalformativPROC)((intptr_t)function_pointer);
	glGetInternalformativ(target, internalformat, pname, bufSize, params_address);
}

