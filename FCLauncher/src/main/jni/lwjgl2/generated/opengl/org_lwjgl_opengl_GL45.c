/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glClipControlPROC) (GLenum origin, GLenum depth);
typedef void (APIENTRY *glCreateTransformFeedbacksPROC) (GLsizei n, GLuint * ids);
typedef void (APIENTRY *glTransformFeedbackBufferBasePROC) (GLuint xfb, GLuint index, GLuint buffer);
typedef void (APIENTRY *glTransformFeedbackBufferRangePROC) (GLuint xfb, GLuint index, GLuint buffer, GLintptr offset, GLsizeiptr size);
typedef void (APIENTRY *glGetTransformFeedbackivPROC) (GLuint xfb, GLenum pname, GLint * param);
typedef void (APIENTRY *glGetTransformFeedbacki_vPROC) (GLuint xfb, GLenum pname, GLuint index, GLint * param);
typedef void (APIENTRY *glGetTransformFeedbacki64_vPROC) (GLuint xfb, GLenum pname, GLuint index, GLint64 * param);
typedef void (APIENTRY *glCreateBuffersPROC) (GLsizei n, GLuint * buffers);
typedef void (APIENTRY *glNamedBufferStoragePROC) (GLuint buffer, GLsizeiptr size, const GLvoid * data, GLbitfield flags);
typedef void (APIENTRY *glNamedBufferDataPROC) (GLuint buffer, GLsizeiptr size, const GLvoid * data, GLenum usage);
typedef void (APIENTRY *glNamedBufferSubDataPROC) (GLuint buffer, GLintptr offset, GLsizeiptr size, const GLvoid * data);
typedef void (APIENTRY *glCopyNamedBufferSubDataPROC) (GLuint readBuffer, GLuint writeBuffer, GLintptr readOffset, GLintptr writeOffset, GLsizeiptr size);
typedef void (APIENTRY *glClearNamedBufferDataPROC) (GLuint buffer, GLenum internalformat, GLenum format, GLenum type, const GLvoid * data);
typedef void (APIENTRY *glClearNamedBufferSubDataPROC) (GLuint buffer, GLenum internalformat, GLintptr offset, GLsizeiptr size, GLenum format, GLenum type, const GLvoid * data);
typedef GLvoid * (APIENTRY *glMapNamedBufferPROC) (GLuint buffer, GLenum access);
typedef GLvoid * (APIENTRY *glMapNamedBufferRangePROC) (GLuint buffer, GLintptr offset, GLsizeiptr length, GLbitfield access);
typedef GLboolean (APIENTRY *glUnmapNamedBufferPROC) (GLuint buffer);
typedef void (APIENTRY *glFlushMappedNamedBufferRangePROC) (GLuint buffer, GLintptr offset, GLsizeiptr length);
typedef void (APIENTRY *glGetNamedBufferParameterivPROC) (GLuint buffer, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetNamedBufferParameteri64vPROC) (GLuint buffer, GLenum pname, GLint64 * params);
typedef void (APIENTRY *glGetNamedBufferPointervPROC) (GLuint buffer, GLenum pname, GLvoid ** params);
typedef void (APIENTRY *glGetNamedBufferSubDataPROC) (GLuint buffer, GLintptr offset, GLsizeiptr size, GLvoid * data);
typedef void (APIENTRY *glCreateFramebuffersPROC) (GLsizei n, GLuint * framebuffers);
typedef void (APIENTRY *glNamedFramebufferRenderbufferPROC) (GLuint framebuffer, GLenum attachment, GLenum renderbuffertarget, GLuint renderbuffer);
typedef void (APIENTRY *glNamedFramebufferParameteriPROC) (GLuint framebuffer, GLenum pname, GLint param);
typedef void (APIENTRY *glNamedFramebufferTexturePROC) (GLuint framebuffer, GLenum attachment, GLuint texture, GLint level);
typedef void (APIENTRY *glNamedFramebufferTextureLayerPROC) (GLuint framebuffer, GLenum attachment, GLuint texture, GLint level, GLint layer);
typedef void (APIENTRY *glNamedFramebufferDrawBufferPROC) (GLuint framebuffer, GLenum mode);
typedef void (APIENTRY *glNamedFramebufferDrawBuffersPROC) (GLuint framebuffer, GLsizei n, const GLenum * bufs);
typedef void (APIENTRY *glNamedFramebufferReadBufferPROC) (GLuint framebuffer, GLenum mode);
typedef void (APIENTRY *glInvalidateNamedFramebufferDataPROC) (GLuint framebuffer, GLsizei numAttachments, const GLenum * attachments);
typedef void (APIENTRY *glInvalidateNamedFramebufferSubDataPROC) (GLuint framebuffer, GLsizei numAttachments, const GLenum * attachments, GLint x, GLint y, GLsizei width, GLsizei height);
typedef void (APIENTRY *glClearNamedFramebufferivPROC) (GLuint framebuffer, GLenum buffer, GLint drawbuffer, const GLint * value);
typedef void (APIENTRY *glClearNamedFramebufferuivPROC) (GLuint framebuffer, GLenum buffer, GLint drawbuffer, const GLuint * value);
typedef void (APIENTRY *glClearNamedFramebufferfvPROC) (GLuint framebuffer, GLenum buffer, GLint drawbuffer, const GLfloat * value);
typedef void (APIENTRY *glClearNamedFramebufferfiPROC) (GLuint framebuffer, GLenum buffer, GLfloat depth, GLint stencil);
typedef void (APIENTRY *glBlitNamedFramebufferPROC) (GLuint readFramebuffer, GLuint drawFramebuffer, GLint srcX0, GLint srcY0, GLint srcX1, GLint srcY1, GLint dstX0, GLint dstY0, GLint dstX1, GLint dstY1, GLbitfield mask, GLenum filter);
typedef GLenum (APIENTRY *glCheckNamedFramebufferStatusPROC) (GLuint framebuffer, GLenum target);
typedef void (APIENTRY *glGetNamedFramebufferParameterivPROC) (GLuint framebuffer, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetNamedFramebufferAttachmentParameterivPROC) (GLuint framebuffer, GLenum attachment, GLenum pname, GLint * params);
typedef void (APIENTRY *glCreateRenderbuffersPROC) (GLsizei n, GLuint * renderbuffers);
typedef void (APIENTRY *glNamedRenderbufferStoragePROC) (GLuint renderbuffer, GLenum internalformat, GLsizei width, GLsizei height);
typedef void (APIENTRY *glNamedRenderbufferStorageMultisamplePROC) (GLuint renderbuffer, GLsizei samples, GLenum internalformat, GLsizei width, GLsizei height);
typedef void (APIENTRY *glGetNamedRenderbufferParameterivPROC) (GLuint renderbuffer, GLenum pname, GLint * params);
typedef void (APIENTRY *glCreateTexturesPROC) (GLenum target, GLsizei n, GLuint * textures);
typedef void (APIENTRY *glTextureBufferPROC) (GLuint texture, GLenum internalformat, GLuint buffer);
typedef void (APIENTRY *glTextureBufferRangePROC) (GLuint texture, GLenum internalformat, GLuint buffer, GLintptr offset, GLsizeiptr size);
typedef void (APIENTRY *glTextureStorage1DPROC) (GLuint texture, GLsizei levels, GLenum internalformat, GLsizei width);
typedef void (APIENTRY *glTextureStorage2DPROC) (GLuint texture, GLsizei levels, GLenum internalformat, GLsizei width, GLsizei height);
typedef void (APIENTRY *glTextureStorage3DPROC) (GLuint texture, GLsizei levels, GLenum internalformat, GLsizei width, GLsizei height, GLsizei depth);
typedef void (APIENTRY *glTextureStorage2DMultisamplePROC) (GLuint texture, GLsizei samples, GLenum internalformat, GLsizei width, GLsizei height, GLboolean fixedsamplelocations);
typedef void (APIENTRY *glTextureStorage3DMultisamplePROC) (GLuint texture, GLsizei samples, GLenum internalformat, GLsizei width, GLsizei height, GLsizei depth, GLboolean fixedsamplelocations);
typedef void (APIENTRY *glTextureSubImage1DPROC) (GLuint texture, GLint level, GLint xoffset, GLsizei width, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glTextureSubImage2DPROC) (GLuint texture, GLint level, GLint xoffset, GLint yoffset, GLsizei width, GLsizei height, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glTextureSubImage3DPROC) (GLuint texture, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glCompressedTextureSubImage1DPROC) (GLuint texture, GLint level, GLint xoffset, GLsizei width, GLenum format, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedTextureSubImage2DPROC) (GLuint texture, GLint level, GLint xoffset, GLint yoffset, GLsizei width, GLsizei height, GLenum format, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedTextureSubImage3DPROC) (GLuint texture, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLenum format, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCopyTextureSubImage1DPROC) (GLuint texture, GLint level, GLint xoffset, GLint x, GLint y, GLsizei width);
typedef void (APIENTRY *glCopyTextureSubImage2DPROC) (GLuint texture, GLint level, GLint xoffset, GLint yoffset, GLint x, GLint y, GLsizei width, GLsizei height);
typedef void (APIENTRY *glCopyTextureSubImage3DPROC) (GLuint texture, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLint x, GLint y, GLsizei width, GLsizei height);
typedef void (APIENTRY *glTextureParameterfPROC) (GLuint texture, GLenum pname, GLfloat param);
typedef void (APIENTRY *glTextureParameterfvPROC) (GLuint texture, GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glTextureParameteriPROC) (GLuint texture, GLenum pname, GLint param);
typedef void (APIENTRY *glTextureParameterIivPROC) (GLuint texture, GLenum pname, const GLint * params);
typedef void (APIENTRY *glTextureParameterIuivPROC) (GLuint texture, GLenum pname, const GLuint * params);
typedef void (APIENTRY *glTextureParameterivPROC) (GLuint texture, GLenum pname, const GLint * params);
typedef void (APIENTRY *glGenerateTextureMipmapPROC) (GLuint texture);
typedef void (APIENTRY *glBindTextureUnitPROC) (GLuint unit, GLuint texture);
typedef void (APIENTRY *glGetTextureImagePROC) (GLuint texture, GLint level, GLenum format, GLenum type, GLsizei bufSize, GLvoid * pixels);
typedef void (APIENTRY *glGetCompressedTextureImagePROC) (GLuint texture, GLint level, GLsizei bufSize, GLvoid * pixels);
typedef void (APIENTRY *glGetTextureLevelParameterfvPROC) (GLuint texture, GLint level, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetTextureLevelParameterivPROC) (GLuint texture, GLint level, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetTextureParameterfvPROC) (GLuint texture, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetTextureParameterIivPROC) (GLuint texture, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetTextureParameterIuivPROC) (GLuint texture, GLenum pname, GLuint * params);
typedef void (APIENTRY *glGetTextureParameterivPROC) (GLuint texture, GLenum pname, GLint * params);
typedef void (APIENTRY *glCreateVertexArraysPROC) (GLsizei n, GLuint * arrays);
typedef void (APIENTRY *glDisableVertexArrayAttribPROC) (GLuint vaobj, GLuint index);
typedef void (APIENTRY *glEnableVertexArrayAttribPROC) (GLuint vaobj, GLuint index);
typedef void (APIENTRY *glVertexArrayElementBufferPROC) (GLuint vaobj, GLuint buffer);
typedef void (APIENTRY *glVertexArrayVertexBufferPROC) (GLuint vaobj, GLuint bindingindex, GLuint buffer, GLintptr offset, GLsizei stride);
typedef void (APIENTRY *glVertexArrayVertexBuffersPROC) (GLuint vaobj, GLuint first, GLsizei count, const GLuint * buffers, const GLintptr * offsets, const GLsizei * strides);
typedef void (APIENTRY *glVertexArrayAttribFormatPROC) (GLuint vaobj, GLuint attribindex, GLint size, GLenum type, GLboolean normalized, GLuint relativeoffset);
typedef void (APIENTRY *glVertexArrayAttribIFormatPROC) (GLuint vaobj, GLuint attribindex, GLint size, GLenum type, GLuint relativeoffset);
typedef void (APIENTRY *glVertexArrayAttribLFormatPROC) (GLuint vaobj, GLuint attribindex, GLint size, GLenum type, GLuint relativeoffset);
typedef void (APIENTRY *glVertexArrayAttribBindingPROC) (GLuint vaobj, GLuint attribindex, GLuint bindingindex);
typedef void (APIENTRY *glVertexArrayBindingDivisorPROC) (GLuint vaobj, GLuint bindingindex, GLuint divisor);
typedef void (APIENTRY *glGetVertexArrayivPROC) (GLuint vaobj, GLenum pname, GLint * param);
typedef void (APIENTRY *glGetVertexArrayIndexedivPROC) (GLuint vaobj, GLuint index, GLenum pname, GLint * param);
typedef void (APIENTRY *glGetVertexArrayIndexed64ivPROC) (GLuint vaobj, GLuint index, GLenum pname, GLint64 * param);
typedef void (APIENTRY *glCreateSamplersPROC) (GLsizei n, GLuint * samplers);
typedef void (APIENTRY *glCreateProgramPipelinesPROC) (GLsizei n, GLuint * pipelines);
typedef void (APIENTRY *glCreateQueriesPROC) (GLenum target, GLsizei n, GLuint * ids);
typedef void (APIENTRY *glMemoryBarrierByRegionPROC) (GLbitfield barriers);
typedef void (APIENTRY *glGetTextureSubImagePROC) (GLuint texture, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLenum format, GLenum type, GLsizei bufSize, GLvoid * pixels);
typedef void (APIENTRY *glGetCompressedTextureSubImagePROC) (GLuint texture, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLsizei bufSize, GLvoid * pixels);
typedef void (APIENTRY *glTextureBarrierPROC) ();
typedef GLenum (APIENTRY *glGetGraphicsResetStatusPROC) ();
typedef void (APIENTRY *glReadnPixelsPROC) (GLint x, GLint y, GLsizei width, GLsizei height, GLenum format, GLenum type, GLsizei bufSize, GLvoid * pixels);
typedef void (APIENTRY *glGetnUniformfvPROC) (GLuint program, GLint location, GLsizei bufSize, GLfloat * params);
typedef void (APIENTRY *glGetnUniformivPROC) (GLuint program, GLint location, GLsizei bufSize, GLint * params);
typedef void (APIENTRY *glGetnUniformuivPROC) (GLuint program, GLint location, GLsizei bufSize, GLuint * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglClipControl(JNIEnv *env, jclass clazz, jint origin, jint depth, jlong function_pointer) {
	glClipControlPROC glClipControl = (glClipControlPROC)((intptr_t)function_pointer);
	glClipControl(origin, depth);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCreateTransformFeedbacks(JNIEnv *env, jclass clazz, jint n, jlong ids, jlong function_pointer) {
	GLuint *ids_address = (GLuint *)(intptr_t)ids;
	glCreateTransformFeedbacksPROC glCreateTransformFeedbacks = (glCreateTransformFeedbacksPROC)((intptr_t)function_pointer);
	glCreateTransformFeedbacks(n, ids_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTransformFeedbackBufferBase(JNIEnv *env, jclass clazz, jint xfb, jint index, jint buffer, jlong function_pointer) {
	glTransformFeedbackBufferBasePROC glTransformFeedbackBufferBase = (glTransformFeedbackBufferBasePROC)((intptr_t)function_pointer);
	glTransformFeedbackBufferBase(xfb, index, buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTransformFeedbackBufferRange(JNIEnv *env, jclass clazz, jint xfb, jint index, jint buffer, jlong offset, jlong size, jlong function_pointer) {
	glTransformFeedbackBufferRangePROC glTransformFeedbackBufferRange = (glTransformFeedbackBufferRangePROC)((intptr_t)function_pointer);
	glTransformFeedbackBufferRange(xfb, index, buffer, offset, size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetTransformFeedbackiv(JNIEnv *env, jclass clazz, jint xfb, jint pname, jlong param, jlong function_pointer) {
	GLint *param_address = (GLint *)(intptr_t)param;
	glGetTransformFeedbackivPROC glGetTransformFeedbackiv = (glGetTransformFeedbackivPROC)((intptr_t)function_pointer);
	glGetTransformFeedbackiv(xfb, pname, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetTransformFeedbacki_1v(JNIEnv *env, jclass clazz, jint xfb, jint pname, jint index, jlong param, jlong function_pointer) {
	GLint *param_address = (GLint *)(intptr_t)param;
	glGetTransformFeedbacki_vPROC glGetTransformFeedbacki_v = (glGetTransformFeedbacki_vPROC)((intptr_t)function_pointer);
	glGetTransformFeedbacki_v(xfb, pname, index, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetTransformFeedbacki64_1v(JNIEnv *env, jclass clazz, jint xfb, jint pname, jint index, jlong param, jlong function_pointer) {
	GLint64 *param_address = (GLint64 *)(intptr_t)param;
	glGetTransformFeedbacki64_vPROC glGetTransformFeedbacki64_v = (glGetTransformFeedbacki64_vPROC)((intptr_t)function_pointer);
	glGetTransformFeedbacki64_v(xfb, pname, index, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCreateBuffers(JNIEnv *env, jclass clazz, jint n, jlong buffers, jlong function_pointer) {
	GLuint *buffers_address = (GLuint *)(intptr_t)buffers;
	glCreateBuffersPROC glCreateBuffers = (glCreateBuffersPROC)((intptr_t)function_pointer);
	glCreateBuffers(n, buffers_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglNamedBufferStorage(JNIEnv *env, jclass clazz, jint buffer, jlong size, jlong data, jint flags, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glNamedBufferStoragePROC glNamedBufferStorage = (glNamedBufferStoragePROC)((intptr_t)function_pointer);
	glNamedBufferStorage(buffer, size, data_address, flags);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglNamedBufferData(JNIEnv *env, jclass clazz, jint buffer, jlong size, jlong data, jint usage, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glNamedBufferDataPROC glNamedBufferData = (glNamedBufferDataPROC)((intptr_t)function_pointer);
	glNamedBufferData(buffer, size, data_address, usage);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglNamedBufferSubData(JNIEnv *env, jclass clazz, jint buffer, jlong offset, jlong size, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glNamedBufferSubDataPROC glNamedBufferSubData = (glNamedBufferSubDataPROC)((intptr_t)function_pointer);
	glNamedBufferSubData(buffer, offset, size, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCopyNamedBufferSubData(JNIEnv *env, jclass clazz, jint readBuffer, jint writeBuffer, jlong readOffset, jlong writeOffset, jlong size, jlong function_pointer) {
	glCopyNamedBufferSubDataPROC glCopyNamedBufferSubData = (glCopyNamedBufferSubDataPROC)((intptr_t)function_pointer);
	glCopyNamedBufferSubData(readBuffer, writeBuffer, readOffset, writeOffset, size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglClearNamedBufferData(JNIEnv *env, jclass clazz, jint buffer, jint internalformat, jint format, jint type, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glClearNamedBufferDataPROC glClearNamedBufferData = (glClearNamedBufferDataPROC)((intptr_t)function_pointer);
	glClearNamedBufferData(buffer, internalformat, format, type, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglClearNamedBufferSubData(JNIEnv *env, jclass clazz, jint buffer, jint internalformat, jlong offset, jlong size, jint format, jint type, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glClearNamedBufferSubDataPROC glClearNamedBufferSubData = (glClearNamedBufferSubDataPROC)((intptr_t)function_pointer);
	glClearNamedBufferSubData(buffer, internalformat, offset, size, format, type, data_address);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_GL45_nglMapNamedBuffer(JNIEnv *env, jclass clazz, jint buffer, jint access, jlong result_size, jobject old_buffer, jlong function_pointer) {
	glMapNamedBufferPROC glMapNamedBuffer = (glMapNamedBufferPROC)((intptr_t)function_pointer);
	GLvoid * __result = glMapNamedBuffer(buffer, access);
	return safeNewBufferCached(env, __result, result_size, old_buffer);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_GL45_nglMapNamedBufferRange(JNIEnv *env, jclass clazz, jint buffer, jlong offset, jlong length, jint access, jobject old_buffer, jlong function_pointer) {
	glMapNamedBufferRangePROC glMapNamedBufferRange = (glMapNamedBufferRangePROC)((intptr_t)function_pointer);
	GLvoid * __result = glMapNamedBufferRange(buffer, offset, length, access);
	return safeNewBufferCached(env, __result, length, old_buffer);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL45_nglUnmapNamedBuffer(JNIEnv *env, jclass clazz, jint buffer, jlong function_pointer) {
	glUnmapNamedBufferPROC glUnmapNamedBuffer = (glUnmapNamedBufferPROC)((intptr_t)function_pointer);
	GLboolean __result = glUnmapNamedBuffer(buffer);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglFlushMappedNamedBufferRange(JNIEnv *env, jclass clazz, jint buffer, jlong offset, jlong length, jlong function_pointer) {
	glFlushMappedNamedBufferRangePROC glFlushMappedNamedBufferRange = (glFlushMappedNamedBufferRangePROC)((intptr_t)function_pointer);
	glFlushMappedNamedBufferRange(buffer, offset, length);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetNamedBufferParameteriv(JNIEnv *env, jclass clazz, jint buffer, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetNamedBufferParameterivPROC glGetNamedBufferParameteriv = (glGetNamedBufferParameterivPROC)((intptr_t)function_pointer);
	glGetNamedBufferParameteriv(buffer, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetNamedBufferParameteri64v(JNIEnv *env, jclass clazz, jint buffer, jint pname, jlong params, jlong function_pointer) {
	GLint64 *params_address = (GLint64 *)(intptr_t)params;
	glGetNamedBufferParameteri64vPROC glGetNamedBufferParameteri64v = (glGetNamedBufferParameteri64vPROC)((intptr_t)function_pointer);
	glGetNamedBufferParameteri64v(buffer, pname, params_address);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_GL45_nglGetNamedBufferPointerv(JNIEnv *env, jclass clazz, jint buffer, jint pname, jlong result_size, jlong function_pointer) {
	glGetNamedBufferPointervPROC glGetNamedBufferPointerv = (glGetNamedBufferPointervPROC)((intptr_t)function_pointer);
	GLvoid * __result;
	glGetNamedBufferPointerv(buffer, pname, &__result);
	return safeNewBuffer(env, __result, result_size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetNamedBufferSubData(JNIEnv *env, jclass clazz, jint buffer, jlong offset, jlong size, jlong data, jlong function_pointer) {
	GLvoid *data_address = (GLvoid *)(intptr_t)data;
	glGetNamedBufferSubDataPROC glGetNamedBufferSubData = (glGetNamedBufferSubDataPROC)((intptr_t)function_pointer);
	glGetNamedBufferSubData(buffer, offset, size, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCreateFramebuffers(JNIEnv *env, jclass clazz, jint n, jlong framebuffers, jlong function_pointer) {
	GLuint *framebuffers_address = (GLuint *)(intptr_t)framebuffers;
	glCreateFramebuffersPROC glCreateFramebuffers = (glCreateFramebuffersPROC)((intptr_t)function_pointer);
	glCreateFramebuffers(n, framebuffers_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglNamedFramebufferRenderbuffer(JNIEnv *env, jclass clazz, jint framebuffer, jint attachment, jint renderbuffertarget, jint renderbuffer, jlong function_pointer) {
	glNamedFramebufferRenderbufferPROC glNamedFramebufferRenderbuffer = (glNamedFramebufferRenderbufferPROC)((intptr_t)function_pointer);
	glNamedFramebufferRenderbuffer(framebuffer, attachment, renderbuffertarget, renderbuffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglNamedFramebufferParameteri(JNIEnv *env, jclass clazz, jint framebuffer, jint pname, jint param, jlong function_pointer) {
	glNamedFramebufferParameteriPROC glNamedFramebufferParameteri = (glNamedFramebufferParameteriPROC)((intptr_t)function_pointer);
	glNamedFramebufferParameteri(framebuffer, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglNamedFramebufferTexture(JNIEnv *env, jclass clazz, jint framebuffer, jint attachment, jint texture, jint level, jlong function_pointer) {
	glNamedFramebufferTexturePROC glNamedFramebufferTexture = (glNamedFramebufferTexturePROC)((intptr_t)function_pointer);
	glNamedFramebufferTexture(framebuffer, attachment, texture, level);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglNamedFramebufferTextureLayer(JNIEnv *env, jclass clazz, jint framebuffer, jint attachment, jint texture, jint level, jint layer, jlong function_pointer) {
	glNamedFramebufferTextureLayerPROC glNamedFramebufferTextureLayer = (glNamedFramebufferTextureLayerPROC)((intptr_t)function_pointer);
	glNamedFramebufferTextureLayer(framebuffer, attachment, texture, level, layer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglNamedFramebufferDrawBuffer(JNIEnv *env, jclass clazz, jint framebuffer, jint mode, jlong function_pointer) {
	glNamedFramebufferDrawBufferPROC glNamedFramebufferDrawBuffer = (glNamedFramebufferDrawBufferPROC)((intptr_t)function_pointer);
	glNamedFramebufferDrawBuffer(framebuffer, mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglNamedFramebufferDrawBuffers(JNIEnv *env, jclass clazz, jint framebuffer, jint n, jlong bufs, jlong function_pointer) {
	const GLenum *bufs_address = (const GLenum *)(intptr_t)bufs;
	glNamedFramebufferDrawBuffersPROC glNamedFramebufferDrawBuffers = (glNamedFramebufferDrawBuffersPROC)((intptr_t)function_pointer);
	glNamedFramebufferDrawBuffers(framebuffer, n, bufs_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglNamedFramebufferReadBuffer(JNIEnv *env, jclass clazz, jint framebuffer, jint mode, jlong function_pointer) {
	glNamedFramebufferReadBufferPROC glNamedFramebufferReadBuffer = (glNamedFramebufferReadBufferPROC)((intptr_t)function_pointer);
	glNamedFramebufferReadBuffer(framebuffer, mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglInvalidateNamedFramebufferData(JNIEnv *env, jclass clazz, jint framebuffer, jint numAttachments, jlong attachments, jlong function_pointer) {
	const GLenum *attachments_address = (const GLenum *)(intptr_t)attachments;
	glInvalidateNamedFramebufferDataPROC glInvalidateNamedFramebufferData = (glInvalidateNamedFramebufferDataPROC)((intptr_t)function_pointer);
	glInvalidateNamedFramebufferData(framebuffer, numAttachments, attachments_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglInvalidateNamedFramebufferSubData(JNIEnv *env, jclass clazz, jint framebuffer, jint numAttachments, jlong attachments, jint x, jint y, jint width, jint height, jlong function_pointer) {
	const GLenum *attachments_address = (const GLenum *)(intptr_t)attachments;
	glInvalidateNamedFramebufferSubDataPROC glInvalidateNamedFramebufferSubData = (glInvalidateNamedFramebufferSubDataPROC)((intptr_t)function_pointer);
	glInvalidateNamedFramebufferSubData(framebuffer, numAttachments, attachments_address, x, y, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglClearNamedFramebufferiv(JNIEnv *env, jclass clazz, jint framebuffer, jint buffer, jint drawbuffer, jlong value, jlong function_pointer) {
	const GLint *value_address = (const GLint *)(intptr_t)value;
	glClearNamedFramebufferivPROC glClearNamedFramebufferiv = (glClearNamedFramebufferivPROC)((intptr_t)function_pointer);
	glClearNamedFramebufferiv(framebuffer, buffer, drawbuffer, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglClearNamedFramebufferuiv(JNIEnv *env, jclass clazz, jint framebuffer, jint buffer, jint drawbuffer, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glClearNamedFramebufferuivPROC glClearNamedFramebufferuiv = (glClearNamedFramebufferuivPROC)((intptr_t)function_pointer);
	glClearNamedFramebufferuiv(framebuffer, buffer, drawbuffer, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglClearNamedFramebufferfv(JNIEnv *env, jclass clazz, jint framebuffer, jint buffer, jint drawbuffer, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glClearNamedFramebufferfvPROC glClearNamedFramebufferfv = (glClearNamedFramebufferfvPROC)((intptr_t)function_pointer);
	glClearNamedFramebufferfv(framebuffer, buffer, drawbuffer, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglClearNamedFramebufferfi(JNIEnv *env, jclass clazz, jint framebuffer, jint buffer, jfloat depth, jint stencil, jlong function_pointer) {
	glClearNamedFramebufferfiPROC glClearNamedFramebufferfi = (glClearNamedFramebufferfiPROC)((intptr_t)function_pointer);
	glClearNamedFramebufferfi(framebuffer, buffer, depth, stencil);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglBlitNamedFramebuffer(JNIEnv *env, jclass clazz, jint readFramebuffer, jint drawFramebuffer, jint srcX0, jint srcY0, jint srcX1, jint srcY1, jint dstX0, jint dstY0, jint dstX1, jint dstY1, jint mask, jint filter, jlong function_pointer) {
	glBlitNamedFramebufferPROC glBlitNamedFramebuffer = (glBlitNamedFramebufferPROC)((intptr_t)function_pointer);
	glBlitNamedFramebuffer(readFramebuffer, drawFramebuffer, srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL45_nglCheckNamedFramebufferStatus(JNIEnv *env, jclass clazz, jint framebuffer, jint target, jlong function_pointer) {
	glCheckNamedFramebufferStatusPROC glCheckNamedFramebufferStatus = (glCheckNamedFramebufferStatusPROC)((intptr_t)function_pointer);
	GLenum __result = glCheckNamedFramebufferStatus(framebuffer, target);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetNamedFramebufferParameteriv(JNIEnv *env, jclass clazz, jint framebuffer, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetNamedFramebufferParameterivPROC glGetNamedFramebufferParameteriv = (glGetNamedFramebufferParameterivPROC)((intptr_t)function_pointer);
	glGetNamedFramebufferParameteriv(framebuffer, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetNamedFramebufferAttachmentParameteriv(JNIEnv *env, jclass clazz, jint framebuffer, jint attachment, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetNamedFramebufferAttachmentParameterivPROC glGetNamedFramebufferAttachmentParameteriv = (glGetNamedFramebufferAttachmentParameterivPROC)((intptr_t)function_pointer);
	glGetNamedFramebufferAttachmentParameteriv(framebuffer, attachment, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCreateRenderbuffers(JNIEnv *env, jclass clazz, jint n, jlong renderbuffers, jlong function_pointer) {
	GLuint *renderbuffers_address = (GLuint *)(intptr_t)renderbuffers;
	glCreateRenderbuffersPROC glCreateRenderbuffers = (glCreateRenderbuffersPROC)((intptr_t)function_pointer);
	glCreateRenderbuffers(n, renderbuffers_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglNamedRenderbufferStorage(JNIEnv *env, jclass clazz, jint renderbuffer, jint internalformat, jint width, jint height, jlong function_pointer) {
	glNamedRenderbufferStoragePROC glNamedRenderbufferStorage = (glNamedRenderbufferStoragePROC)((intptr_t)function_pointer);
	glNamedRenderbufferStorage(renderbuffer, internalformat, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglNamedRenderbufferStorageMultisample(JNIEnv *env, jclass clazz, jint renderbuffer, jint samples, jint internalformat, jint width, jint height, jlong function_pointer) {
	glNamedRenderbufferStorageMultisamplePROC glNamedRenderbufferStorageMultisample = (glNamedRenderbufferStorageMultisamplePROC)((intptr_t)function_pointer);
	glNamedRenderbufferStorageMultisample(renderbuffer, samples, internalformat, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetNamedRenderbufferParameteriv(JNIEnv *env, jclass clazz, jint renderbuffer, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetNamedRenderbufferParameterivPROC glGetNamedRenderbufferParameteriv = (glGetNamedRenderbufferParameterivPROC)((intptr_t)function_pointer);
	glGetNamedRenderbufferParameteriv(renderbuffer, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCreateTextures(JNIEnv *env, jclass clazz, jint target, jint n, jlong textures, jlong function_pointer) {
	GLuint *textures_address = (GLuint *)(intptr_t)textures;
	glCreateTexturesPROC glCreateTextures = (glCreateTexturesPROC)((intptr_t)function_pointer);
	glCreateTextures(target, n, textures_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureBuffer(JNIEnv *env, jclass clazz, jint texture, jint internalformat, jint buffer, jlong function_pointer) {
	glTextureBufferPROC glTextureBuffer = (glTextureBufferPROC)((intptr_t)function_pointer);
	glTextureBuffer(texture, internalformat, buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureBufferRange(JNIEnv *env, jclass clazz, jint texture, jint internalformat, jint buffer, jlong offset, jlong size, jlong function_pointer) {
	glTextureBufferRangePROC glTextureBufferRange = (glTextureBufferRangePROC)((intptr_t)function_pointer);
	glTextureBufferRange(texture, internalformat, buffer, offset, size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureStorage1D(JNIEnv *env, jclass clazz, jint texture, jint levels, jint internalformat, jint width, jlong function_pointer) {
	glTextureStorage1DPROC glTextureStorage1D = (glTextureStorage1DPROC)((intptr_t)function_pointer);
	glTextureStorage1D(texture, levels, internalformat, width);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureStorage2D(JNIEnv *env, jclass clazz, jint texture, jint levels, jint internalformat, jint width, jint height, jlong function_pointer) {
	glTextureStorage2DPROC glTextureStorage2D = (glTextureStorage2DPROC)((intptr_t)function_pointer);
	glTextureStorage2D(texture, levels, internalformat, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureStorage3D(JNIEnv *env, jclass clazz, jint texture, jint levels, jint internalformat, jint width, jint height, jint depth, jlong function_pointer) {
	glTextureStorage3DPROC glTextureStorage3D = (glTextureStorage3DPROC)((intptr_t)function_pointer);
	glTextureStorage3D(texture, levels, internalformat, width, height, depth);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureStorage2DMultisample(JNIEnv *env, jclass clazz, jint texture, jint samples, jint internalformat, jint width, jint height, jboolean fixedsamplelocations, jlong function_pointer) {
	glTextureStorage2DMultisamplePROC glTextureStorage2DMultisample = (glTextureStorage2DMultisamplePROC)((intptr_t)function_pointer);
	glTextureStorage2DMultisample(texture, samples, internalformat, width, height, fixedsamplelocations);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureStorage3DMultisample(JNIEnv *env, jclass clazz, jint texture, jint samples, jint internalformat, jint width, jint height, jint depth, jboolean fixedsamplelocations, jlong function_pointer) {
	glTextureStorage3DMultisamplePROC glTextureStorage3DMultisample = (glTextureStorage3DMultisamplePROC)((intptr_t)function_pointer);
	glTextureStorage3DMultisample(texture, samples, internalformat, width, height, depth, fixedsamplelocations);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureSubImage1D(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint width, jint format, jint type, jlong pixels, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)pixels;
	glTextureSubImage1DPROC glTextureSubImage1D = (glTextureSubImage1DPROC)((intptr_t)function_pointer);
	glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureSubImage1DBO(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint width, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glTextureSubImage1DPROC glTextureSubImage1D = (glTextureSubImage1DPROC)((intptr_t)function_pointer);
	glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureSubImage2D(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint type, jlong pixels, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)pixels;
	glTextureSubImage2DPROC glTextureSubImage2D = (glTextureSubImage2DPROC)((intptr_t)function_pointer);
	glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureSubImage2DBO(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glTextureSubImage2DPROC glTextureSubImage2D = (glTextureSubImage2DPROC)((intptr_t)function_pointer);
	glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureSubImage3D(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint type, jlong pixels, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)pixels;
	glTextureSubImage3DPROC glTextureSubImage3D = (glTextureSubImage3DPROC)((intptr_t)function_pointer);
	glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureSubImage3DBO(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glTextureSubImage3DPROC glTextureSubImage3D = (glTextureSubImage3DPROC)((intptr_t)function_pointer);
	glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCompressedTextureSubImage1D(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint width, jint format, jint imageSize, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glCompressedTextureSubImage1DPROC glCompressedTextureSubImage1D = (glCompressedTextureSubImage1DPROC)((intptr_t)function_pointer);
	glCompressedTextureSubImage1D(texture, level, xoffset, width, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCompressedTextureSubImage1DBO(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint width, jint format, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glCompressedTextureSubImage1DPROC glCompressedTextureSubImage1D = (glCompressedTextureSubImage1DPROC)((intptr_t)function_pointer);
	glCompressedTextureSubImage1D(texture, level, xoffset, width, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCompressedTextureSubImage2D(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint imageSize, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glCompressedTextureSubImage2DPROC glCompressedTextureSubImage2D = (glCompressedTextureSubImage2DPROC)((intptr_t)function_pointer);
	glCompressedTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCompressedTextureSubImage2DBO(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glCompressedTextureSubImage2DPROC glCompressedTextureSubImage2D = (glCompressedTextureSubImage2DPROC)((intptr_t)function_pointer);
	glCompressedTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCompressedTextureSubImage3D(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint imageSize, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glCompressedTextureSubImage3DPROC glCompressedTextureSubImage3D = (glCompressedTextureSubImage3DPROC)((intptr_t)function_pointer);
	glCompressedTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCompressedTextureSubImage3DBO(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glCompressedTextureSubImage3DPROC glCompressedTextureSubImage3D = (glCompressedTextureSubImage3DPROC)((intptr_t)function_pointer);
	glCompressedTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCopyTextureSubImage1D(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint x, jint y, jint width, jlong function_pointer) {
	glCopyTextureSubImage1DPROC glCopyTextureSubImage1D = (glCopyTextureSubImage1DPROC)((intptr_t)function_pointer);
	glCopyTextureSubImage1D(texture, level, xoffset, x, y, width);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCopyTextureSubImage2D(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint x, jint y, jint width, jint height, jlong function_pointer) {
	glCopyTextureSubImage2DPROC glCopyTextureSubImage2D = (glCopyTextureSubImage2DPROC)((intptr_t)function_pointer);
	glCopyTextureSubImage2D(texture, level, xoffset, yoffset, x, y, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCopyTextureSubImage3D(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint zoffset, jint x, jint y, jint width, jint height, jlong function_pointer) {
	glCopyTextureSubImage3DPROC glCopyTextureSubImage3D = (glCopyTextureSubImage3DPROC)((intptr_t)function_pointer);
	glCopyTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, x, y, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureParameterf(JNIEnv *env, jclass clazz, jint texture, jint pname, jfloat param, jlong function_pointer) {
	glTextureParameterfPROC glTextureParameterf = (glTextureParameterfPROC)((intptr_t)function_pointer);
	glTextureParameterf(texture, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureParameterfv(JNIEnv *env, jclass clazz, jint texture, jint pname, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glTextureParameterfvPROC glTextureParameterfv = (glTextureParameterfvPROC)((intptr_t)function_pointer);
	glTextureParameterfv(texture, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureParameteri(JNIEnv *env, jclass clazz, jint texture, jint pname, jint param, jlong function_pointer) {
	glTextureParameteriPROC glTextureParameteri = (glTextureParameteriPROC)((intptr_t)function_pointer);
	glTextureParameteri(texture, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureParameterIiv(JNIEnv *env, jclass clazz, jint texture, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glTextureParameterIivPROC glTextureParameterIiv = (glTextureParameterIivPROC)((intptr_t)function_pointer);
	glTextureParameterIiv(texture, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureParameterIuiv(JNIEnv *env, jclass clazz, jint texture, jint pname, jlong params, jlong function_pointer) {
	const GLuint *params_address = (const GLuint *)(intptr_t)params;
	glTextureParameterIuivPROC glTextureParameterIuiv = (glTextureParameterIuivPROC)((intptr_t)function_pointer);
	glTextureParameterIuiv(texture, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureParameteriv(JNIEnv *env, jclass clazz, jint texture, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glTextureParameterivPROC glTextureParameteriv = (glTextureParameterivPROC)((intptr_t)function_pointer);
	glTextureParameteriv(texture, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGenerateTextureMipmap(JNIEnv *env, jclass clazz, jint texture, jlong function_pointer) {
	glGenerateTextureMipmapPROC glGenerateTextureMipmap = (glGenerateTextureMipmapPROC)((intptr_t)function_pointer);
	glGenerateTextureMipmap(texture);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglBindTextureUnit(JNIEnv *env, jclass clazz, jint unit, jint texture, jlong function_pointer) {
	glBindTextureUnitPROC glBindTextureUnit = (glBindTextureUnitPROC)((intptr_t)function_pointer);
	glBindTextureUnit(unit, texture);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetTextureImage(JNIEnv *env, jclass clazz, jint texture, jint level, jint format, jint type, jint bufSize, jlong pixels, jlong function_pointer) {
	GLvoid *pixels_address = (GLvoid *)(intptr_t)pixels;
	glGetTextureImagePROC glGetTextureImage = (glGetTextureImagePROC)((intptr_t)function_pointer);
	glGetTextureImage(texture, level, format, type, bufSize, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetTextureImageBO(JNIEnv *env, jclass clazz, jint texture, jint level, jint format, jint type, jint bufSize, jlong pixels_buffer_offset, jlong function_pointer) {
	GLvoid *pixels_address = (GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glGetTextureImagePROC glGetTextureImage = (glGetTextureImagePROC)((intptr_t)function_pointer);
	glGetTextureImage(texture, level, format, type, bufSize, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetCompressedTextureImage(JNIEnv *env, jclass clazz, jint texture, jint level, jint bufSize, jlong pixels, jlong function_pointer) {
	GLvoid *pixels_address = (GLvoid *)(intptr_t)pixels;
	glGetCompressedTextureImagePROC glGetCompressedTextureImage = (glGetCompressedTextureImagePROC)((intptr_t)function_pointer);
	glGetCompressedTextureImage(texture, level, bufSize, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetCompressedTextureImageBO(JNIEnv *env, jclass clazz, jint texture, jint level, jint bufSize, jlong pixels_buffer_offset, jlong function_pointer) {
	GLvoid *pixels_address = (GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glGetCompressedTextureImagePROC glGetCompressedTextureImage = (glGetCompressedTextureImagePROC)((intptr_t)function_pointer);
	glGetCompressedTextureImage(texture, level, bufSize, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetTextureLevelParameterfv(JNIEnv *env, jclass clazz, jint texture, jint level, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetTextureLevelParameterfvPROC glGetTextureLevelParameterfv = (glGetTextureLevelParameterfvPROC)((intptr_t)function_pointer);
	glGetTextureLevelParameterfv(texture, level, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetTextureLevelParameteriv(JNIEnv *env, jclass clazz, jint texture, jint level, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetTextureLevelParameterivPROC glGetTextureLevelParameteriv = (glGetTextureLevelParameterivPROC)((intptr_t)function_pointer);
	glGetTextureLevelParameteriv(texture, level, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetTextureParameterfv(JNIEnv *env, jclass clazz, jint texture, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetTextureParameterfvPROC glGetTextureParameterfv = (glGetTextureParameterfvPROC)((intptr_t)function_pointer);
	glGetTextureParameterfv(texture, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetTextureParameterIiv(JNIEnv *env, jclass clazz, jint texture, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetTextureParameterIivPROC glGetTextureParameterIiv = (glGetTextureParameterIivPROC)((intptr_t)function_pointer);
	glGetTextureParameterIiv(texture, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetTextureParameterIuiv(JNIEnv *env, jclass clazz, jint texture, jint pname, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetTextureParameterIuivPROC glGetTextureParameterIuiv = (glGetTextureParameterIuivPROC)((intptr_t)function_pointer);
	glGetTextureParameterIuiv(texture, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetTextureParameteriv(JNIEnv *env, jclass clazz, jint texture, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetTextureParameterivPROC glGetTextureParameteriv = (glGetTextureParameterivPROC)((intptr_t)function_pointer);
	glGetTextureParameteriv(texture, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCreateVertexArrays(JNIEnv *env, jclass clazz, jint n, jlong arrays, jlong function_pointer) {
	GLuint *arrays_address = (GLuint *)(intptr_t)arrays;
	glCreateVertexArraysPROC glCreateVertexArrays = (glCreateVertexArraysPROC)((intptr_t)function_pointer);
	glCreateVertexArrays(n, arrays_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglDisableVertexArrayAttrib(JNIEnv *env, jclass clazz, jint vaobj, jint index, jlong function_pointer) {
	glDisableVertexArrayAttribPROC glDisableVertexArrayAttrib = (glDisableVertexArrayAttribPROC)((intptr_t)function_pointer);
	glDisableVertexArrayAttrib(vaobj, index);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglEnableVertexArrayAttrib(JNIEnv *env, jclass clazz, jint vaobj, jint index, jlong function_pointer) {
	glEnableVertexArrayAttribPROC glEnableVertexArrayAttrib = (glEnableVertexArrayAttribPROC)((intptr_t)function_pointer);
	glEnableVertexArrayAttrib(vaobj, index);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglVertexArrayElementBuffer(JNIEnv *env, jclass clazz, jint vaobj, jint buffer, jlong function_pointer) {
	glVertexArrayElementBufferPROC glVertexArrayElementBuffer = (glVertexArrayElementBufferPROC)((intptr_t)function_pointer);
	glVertexArrayElementBuffer(vaobj, buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglVertexArrayVertexBuffer(JNIEnv *env, jclass clazz, jint vaobj, jint bindingindex, jint buffer, jlong offset, jint stride, jlong function_pointer) {
	glVertexArrayVertexBufferPROC glVertexArrayVertexBuffer = (glVertexArrayVertexBufferPROC)((intptr_t)function_pointer);
	glVertexArrayVertexBuffer(vaobj, bindingindex, buffer, offset, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglVertexArrayVertexBuffers(JNIEnv *env, jclass clazz, jint vaobj, jint first, jint count, jlong buffers, jlong offsets, jlong strides, jlong function_pointer) {
	const GLuint *buffers_address = (const GLuint *)(intptr_t)buffers;
	const GLintptr *offsets_address = (const GLintptr *)(intptr_t)offsets;
	const GLsizei *strides_address = (const GLsizei *)(intptr_t)strides;
	glVertexArrayVertexBuffersPROC glVertexArrayVertexBuffers = (glVertexArrayVertexBuffersPROC)((intptr_t)function_pointer);
	glVertexArrayVertexBuffers(vaobj, first, count, buffers_address, offsets_address, strides_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglVertexArrayAttribFormat(JNIEnv *env, jclass clazz, jint vaobj, jint attribindex, jint size, jint type, jboolean normalized, jint relativeoffset, jlong function_pointer) {
	glVertexArrayAttribFormatPROC glVertexArrayAttribFormat = (glVertexArrayAttribFormatPROC)((intptr_t)function_pointer);
	glVertexArrayAttribFormat(vaobj, attribindex, size, type, normalized, relativeoffset);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglVertexArrayAttribIFormat(JNIEnv *env, jclass clazz, jint vaobj, jint attribindex, jint size, jint type, jint relativeoffset, jlong function_pointer) {
	glVertexArrayAttribIFormatPROC glVertexArrayAttribIFormat = (glVertexArrayAttribIFormatPROC)((intptr_t)function_pointer);
	glVertexArrayAttribIFormat(vaobj, attribindex, size, type, relativeoffset);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglVertexArrayAttribLFormat(JNIEnv *env, jclass clazz, jint vaobj, jint attribindex, jint size, jint type, jint relativeoffset, jlong function_pointer) {
	glVertexArrayAttribLFormatPROC glVertexArrayAttribLFormat = (glVertexArrayAttribLFormatPROC)((intptr_t)function_pointer);
	glVertexArrayAttribLFormat(vaobj, attribindex, size, type, relativeoffset);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglVertexArrayAttribBinding(JNIEnv *env, jclass clazz, jint vaobj, jint attribindex, jint bindingindex, jlong function_pointer) {
	glVertexArrayAttribBindingPROC glVertexArrayAttribBinding = (glVertexArrayAttribBindingPROC)((intptr_t)function_pointer);
	glVertexArrayAttribBinding(vaobj, attribindex, bindingindex);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglVertexArrayBindingDivisor(JNIEnv *env, jclass clazz, jint vaobj, jint bindingindex, jint divisor, jlong function_pointer) {
	glVertexArrayBindingDivisorPROC glVertexArrayBindingDivisor = (glVertexArrayBindingDivisorPROC)((intptr_t)function_pointer);
	glVertexArrayBindingDivisor(vaobj, bindingindex, divisor);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetVertexArrayiv(JNIEnv *env, jclass clazz, jint vaobj, jint pname, jlong param, jlong function_pointer) {
	GLint *param_address = (GLint *)(intptr_t)param;
	glGetVertexArrayivPROC glGetVertexArrayiv = (glGetVertexArrayivPROC)((intptr_t)function_pointer);
	glGetVertexArrayiv(vaobj, pname, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetVertexArrayIndexediv(JNIEnv *env, jclass clazz, jint vaobj, jint index, jint pname, jlong param, jlong function_pointer) {
	GLint *param_address = (GLint *)(intptr_t)param;
	glGetVertexArrayIndexedivPROC glGetVertexArrayIndexediv = (glGetVertexArrayIndexedivPROC)((intptr_t)function_pointer);
	glGetVertexArrayIndexediv(vaobj, index, pname, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetVertexArrayIndexed64iv(JNIEnv *env, jclass clazz, jint vaobj, jint index, jint pname, jlong param, jlong function_pointer) {
	GLint64 *param_address = (GLint64 *)(intptr_t)param;
	glGetVertexArrayIndexed64ivPROC glGetVertexArrayIndexed64iv = (glGetVertexArrayIndexed64ivPROC)((intptr_t)function_pointer);
	glGetVertexArrayIndexed64iv(vaobj, index, pname, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCreateSamplers(JNIEnv *env, jclass clazz, jint n, jlong samplers, jlong function_pointer) {
	GLuint *samplers_address = (GLuint *)(intptr_t)samplers;
	glCreateSamplersPROC glCreateSamplers = (glCreateSamplersPROC)((intptr_t)function_pointer);
	glCreateSamplers(n, samplers_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCreateProgramPipelines(JNIEnv *env, jclass clazz, jint n, jlong pipelines, jlong function_pointer) {
	GLuint *pipelines_address = (GLuint *)(intptr_t)pipelines;
	glCreateProgramPipelinesPROC glCreateProgramPipelines = (glCreateProgramPipelinesPROC)((intptr_t)function_pointer);
	glCreateProgramPipelines(n, pipelines_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglCreateQueries(JNIEnv *env, jclass clazz, jint target, jint n, jlong ids, jlong function_pointer) {
	GLuint *ids_address = (GLuint *)(intptr_t)ids;
	glCreateQueriesPROC glCreateQueries = (glCreateQueriesPROC)((intptr_t)function_pointer);
	glCreateQueries(target, n, ids_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglMemoryBarrierByRegion(JNIEnv *env, jclass clazz, jint barriers, jlong function_pointer) {
	glMemoryBarrierByRegionPROC glMemoryBarrierByRegion = (glMemoryBarrierByRegionPROC)((intptr_t)function_pointer);
	glMemoryBarrierByRegion(barriers);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetTextureSubImage(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint type, jint bufSize, jlong pixels, jlong function_pointer) {
	GLvoid *pixels_address = (GLvoid *)(intptr_t)pixels;
	glGetTextureSubImagePROC glGetTextureSubImage = (glGetTextureSubImagePROC)((intptr_t)function_pointer);
	glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, bufSize, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetTextureSubImageBO(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint type, jint bufSize, jlong pixels_buffer_offset, jlong function_pointer) {
	GLvoid *pixels_address = (GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glGetTextureSubImagePROC glGetTextureSubImage = (glGetTextureSubImagePROC)((intptr_t)function_pointer);
	glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, bufSize, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetCompressedTextureSubImage(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint bufSize, jlong pixels, jlong function_pointer) {
	GLvoid *pixels_address = (GLvoid *)(intptr_t)pixels;
	glGetCompressedTextureSubImagePROC glGetCompressedTextureSubImage = (glGetCompressedTextureSubImagePROC)((intptr_t)function_pointer);
	glGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, bufSize, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetCompressedTextureSubImageBO(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint bufSize, jlong pixels_buffer_offset, jlong function_pointer) {
	GLvoid *pixels_address = (GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glGetCompressedTextureSubImagePROC glGetCompressedTextureSubImage = (glGetCompressedTextureSubImagePROC)((intptr_t)function_pointer);
	glGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, bufSize, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglTextureBarrier(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glTextureBarrierPROC glTextureBarrier = (glTextureBarrierPROC)((intptr_t)function_pointer);
	glTextureBarrier();
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL45_nglGetGraphicsResetStatus(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glGetGraphicsResetStatusPROC glGetGraphicsResetStatus = (glGetGraphicsResetStatusPROC)((intptr_t)function_pointer);
	GLenum __result = glGetGraphicsResetStatus();
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglReadnPixels(JNIEnv *env, jclass clazz, jint x, jint y, jint width, jint height, jint format, jint type, jint bufSize, jlong pixels, jlong function_pointer) {
	GLvoid *pixels_address = (GLvoid *)(intptr_t)pixels;
	glReadnPixelsPROC glReadnPixels = (glReadnPixelsPROC)((intptr_t)function_pointer);
	glReadnPixels(x, y, width, height, format, type, bufSize, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglReadnPixelsBO(JNIEnv *env, jclass clazz, jint x, jint y, jint width, jint height, jint format, jint type, jint bufSize, jlong pixels_buffer_offset, jlong function_pointer) {
	GLvoid *pixels_address = (GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glReadnPixelsPROC glReadnPixels = (glReadnPixelsPROC)((intptr_t)function_pointer);
	glReadnPixels(x, y, width, height, format, type, bufSize, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetnUniformfv(JNIEnv *env, jclass clazz, jint program, jint location, jint bufSize, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetnUniformfvPROC glGetnUniformfv = (glGetnUniformfvPROC)((intptr_t)function_pointer);
	glGetnUniformfv(program, location, bufSize, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetnUniformiv(JNIEnv *env, jclass clazz, jint program, jint location, jint bufSize, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetnUniformivPROC glGetnUniformiv = (glGetnUniformivPROC)((intptr_t)function_pointer);
	glGetnUniformiv(program, location, bufSize, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL45_nglGetnUniformuiv(JNIEnv *env, jclass clazz, jint program, jint location, jint bufSize, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetnUniformuivPROC glGetnUniformuiv = (glGetnUniformuivPROC)((intptr_t)function_pointer);
	glGetnUniformuiv(program, location, bufSize, params_address);
}

