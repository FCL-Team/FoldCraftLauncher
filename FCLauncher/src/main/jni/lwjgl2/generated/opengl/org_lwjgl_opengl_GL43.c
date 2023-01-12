/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glClearBufferDataPROC) (GLenum target, GLenum internalformat, GLenum format, GLenum type, const GLvoid * data);
typedef void (APIENTRY *glClearBufferSubDataPROC) (GLenum target, GLenum internalformat, GLintptr offset, GLsizeiptr size, GLenum format, GLenum type, const GLvoid * data);
typedef void (APIENTRY *glDispatchComputePROC) (GLuint num_groups_x, GLuint num_groups_y, GLuint num_groups_z);
typedef void (APIENTRY *glDispatchComputeIndirectPROC) (GLintptr indirect);
typedef void (APIENTRY *glCopyImageSubDataPROC) (GLuint srcName, GLenum srcTarget, GLint srcLevel, GLint srcX, GLint srcY, GLint srcZ, GLuint dstName, GLenum dstTarget, GLint dstLevel, GLint dstX, GLint dstY, GLint dstZ, GLsizei srcWidth, GLsizei srcHeight, GLsizei srcDepth);
typedef void (APIENTRY *glDebugMessageControlPROC) (GLenum source, GLenum type, GLenum severity, GLsizei count, const GLuint * ids, GLboolean enabled);
typedef void (APIENTRY *glDebugMessageInsertPROC) (GLenum source, GLenum type, GLuint id, GLenum severity, GLsizei length, const GLchar * buf);
typedef void (APIENTRY *glDebugMessageCallbackPROC) (GLDEBUGPROC callback, GLvoid * userParam);
typedef GLuint (APIENTRY *glGetDebugMessageLogPROC) (GLuint count, GLsizei bufsize, GLenum * sources, GLenum * types, GLuint * ids, GLenum * severities, GLsizei * lengths, GLchar * messageLog);
typedef void (APIENTRY *glPushDebugGroupPROC) (GLenum source, GLuint id, GLsizei length, const GLchar * message);
typedef void (APIENTRY *glPopDebugGroupPROC) ();
typedef void (APIENTRY *glObjectLabelPROC) (GLenum identifier, GLuint name, GLsizei length, const GLchar * label);
typedef void (APIENTRY *glGetObjectLabelPROC) (GLenum identifier, GLuint name, GLsizei bufSize, GLsizei * length, GLchar * label);
typedef void (APIENTRY *glObjectPtrLabelPROC) (GLvoid * ptr, GLsizei length, const GLchar * label);
typedef void (APIENTRY *glGetObjectPtrLabelPROC) (GLvoid * ptr, GLsizei bufSize, GLsizei * length, GLchar * label);
typedef void (APIENTRY *glFramebufferParameteriPROC) (GLenum target, GLenum pname, GLint param);
typedef void (APIENTRY *glGetFramebufferParameterivPROC) (GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetInternalformati64vPROC) (GLenum target, GLenum internalformat, GLenum pname, GLsizei bufSize, GLint64 * params);
typedef void (APIENTRY *glInvalidateTexSubImagePROC) (GLuint texture, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth);
typedef void (APIENTRY *glInvalidateTexImagePROC) (GLuint texture, GLint level);
typedef void (APIENTRY *glInvalidateBufferSubDataPROC) (GLuint buffer, GLintptr offset, GLsizeiptr length);
typedef void (APIENTRY *glInvalidateBufferDataPROC) (GLuint buffer);
typedef void (APIENTRY *glInvalidateFramebufferPROC) (GLenum target, GLsizei numAttachments, const GLenum * attachments);
typedef void (APIENTRY *glInvalidateSubFramebufferPROC) (GLenum target, GLsizei numAttachments, const GLenum * attachments, GLint x, GLint y, GLsizei width, GLsizei height);
typedef void (APIENTRY *glMultiDrawArraysIndirectPROC) (GLenum mode, const GLvoid * indirect, GLsizei primcount, GLsizei stride);
typedef void (APIENTRY *glMultiDrawElementsIndirectPROC) (GLenum mode, GLenum type, const GLvoid * indirect, GLsizei primcount, GLsizei stride);
typedef void (APIENTRY *glGetProgramInterfaceivPROC) (GLuint program, GLenum programInterface, GLenum pname, GLint * params);
typedef GLuint (APIENTRY *glGetProgramResourceIndexPROC) (GLuint program, GLenum programInterface, const GLchar * name);
typedef void (APIENTRY *glGetProgramResourceNamePROC) (GLuint program, GLenum programInterface, GLuint index, GLsizei bufSize, GLsizei * length, GLchar * name);
typedef void (APIENTRY *glGetProgramResourceivPROC) (GLuint program, GLenum programInterface, GLuint index, GLsizei propCount, const GLenum * props, GLsizei bufSize, GLsizei * length, GLint * params);
typedef GLint (APIENTRY *glGetProgramResourceLocationPROC) (GLuint program, GLenum programInterface, const GLchar * name);
typedef GLint (APIENTRY *glGetProgramResourceLocationIndexPROC) (GLuint program, GLenum programInterface, const GLchar * name);
typedef void (APIENTRY *glShaderStorageBlockBindingPROC) (GLuint program, GLuint storageBlockIndex, GLuint storageBlockBinding);
typedef void (APIENTRY *glTexBufferRangePROC) (GLenum target, GLenum internalformat, GLuint buffer, GLintptr offset, GLsizeiptr size);
typedef void (APIENTRY *glTexStorage2DMultisamplePROC) (GLenum target, GLsizei samples, GLenum internalformat, GLsizei width, GLsizei height, GLboolean fixedsamplelocations);
typedef void (APIENTRY *glTexStorage3DMultisamplePROC) (GLenum target, GLsizei samples, GLenum internalformat, GLsizei width, GLsizei height, GLsizei depth, GLboolean fixedsamplelocations);
typedef void (APIENTRY *glTextureViewPROC) (GLuint texture, GLenum target, GLuint origtexture, GLenum internalformat, GLuint minlevel, GLuint numlevels, GLuint minlayer, GLuint numlayers);
typedef void (APIENTRY *glBindVertexBufferPROC) (GLuint bindingindex, GLuint buffer, GLintptr offset, GLsizei stride);
typedef void (APIENTRY *glVertexAttribFormatPROC) (GLuint attribindex, GLint size, GLenum type, GLboolean normalized, GLuint relativeoffset);
typedef void (APIENTRY *glVertexAttribIFormatPROC) (GLuint attribindex, GLint size, GLenum type, GLuint relativeoffset);
typedef void (APIENTRY *glVertexAttribLFormatPROC) (GLuint attribindex, GLint size, GLenum type, GLuint relativeoffset);
typedef void (APIENTRY *glVertexAttribBindingPROC) (GLuint attribindex, GLuint bindingindex);
typedef void (APIENTRY *glVertexBindingDivisorPROC) (GLuint bindingindex, GLuint divisor);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglClearBufferData(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint format, jint type, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glClearBufferDataPROC glClearBufferData = (glClearBufferDataPROC)((intptr_t)function_pointer);
	glClearBufferData(target, internalformat, format, type, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglClearBufferSubData(JNIEnv *env, jclass clazz, jint target, jint internalformat, jlong offset, jlong size, jint format, jint type, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glClearBufferSubDataPROC glClearBufferSubData = (glClearBufferSubDataPROC)((intptr_t)function_pointer);
	glClearBufferSubData(target, internalformat, offset, size, format, type, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglDispatchCompute(JNIEnv *env, jclass clazz, jint num_groups_x, jint num_groups_y, jint num_groups_z, jlong function_pointer) {
	glDispatchComputePROC glDispatchCompute = (glDispatchComputePROC)((intptr_t)function_pointer);
	glDispatchCompute(num_groups_x, num_groups_y, num_groups_z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglDispatchComputeIndirect(JNIEnv *env, jclass clazz, jlong indirect, jlong function_pointer) {
	glDispatchComputeIndirectPROC glDispatchComputeIndirect = (glDispatchComputeIndirectPROC)((intptr_t)function_pointer);
	glDispatchComputeIndirect(indirect);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglCopyImageSubData(JNIEnv *env, jclass clazz, jint srcName, jint srcTarget, jint srcLevel, jint srcX, jint srcY, jint srcZ, jint dstName, jint dstTarget, jint dstLevel, jint dstX, jint dstY, jint dstZ, jint srcWidth, jint srcHeight, jint srcDepth, jlong function_pointer) {
	glCopyImageSubDataPROC glCopyImageSubData = (glCopyImageSubDataPROC)((intptr_t)function_pointer);
	glCopyImageSubData(srcName, srcTarget, srcLevel, srcX, srcY, srcZ, dstName, dstTarget, dstLevel, dstX, dstY, dstZ, srcWidth, srcHeight, srcDepth);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglDebugMessageControl(JNIEnv *env, jclass clazz, jint source, jint type, jint severity, jint count, jlong ids, jboolean enabled, jlong function_pointer) {
	const GLuint *ids_address = (const GLuint *)(intptr_t)ids;
	glDebugMessageControlPROC glDebugMessageControl = (glDebugMessageControlPROC)((intptr_t)function_pointer);
	glDebugMessageControl(source, type, severity, count, ids_address, enabled);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglDebugMessageInsert(JNIEnv *env, jclass clazz, jint source, jint type, jint id, jint severity, jint length, jlong buf, jlong function_pointer) {
	const GLchar *buf_address = (const GLchar *)(intptr_t)buf;
	glDebugMessageInsertPROC glDebugMessageInsert = (glDebugMessageInsertPROC)((intptr_t)function_pointer);
	glDebugMessageInsert(source, type, id, severity, length, buf_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglDebugMessageCallback(JNIEnv *env, jclass clazz, jlong callback, jlong userParam, jlong function_pointer) {
	glDebugMessageCallbackPROC glDebugMessageCallback = (glDebugMessageCallbackPROC)((intptr_t)function_pointer);
	glDebugMessageCallback((GLDEBUGPROC)(intptr_t)callback, (GLvoid *)(intptr_t)userParam);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL43_nglGetDebugMessageLog(JNIEnv *env, jclass clazz, jint count, jint bufsize, jlong sources, jlong types, jlong ids, jlong severities, jlong lengths, jlong messageLog, jlong function_pointer) {
	GLenum *sources_address = (GLenum *)(intptr_t)sources;
	GLenum *types_address = (GLenum *)(intptr_t)types;
	GLuint *ids_address = (GLuint *)(intptr_t)ids;
	GLenum *severities_address = (GLenum *)(intptr_t)severities;
	GLsizei *lengths_address = (GLsizei *)(intptr_t)lengths;
	GLchar *messageLog_address = (GLchar *)(intptr_t)messageLog;
	glGetDebugMessageLogPROC glGetDebugMessageLog = (glGetDebugMessageLogPROC)((intptr_t)function_pointer);
	GLuint __result = glGetDebugMessageLog(count, bufsize, sources_address, types_address, ids_address, severities_address, lengths_address, messageLog_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglPushDebugGroup(JNIEnv *env, jclass clazz, jint source, jint id, jint length, jlong message, jlong function_pointer) {
	const GLchar *message_address = (const GLchar *)(intptr_t)message;
	glPushDebugGroupPROC glPushDebugGroup = (glPushDebugGroupPROC)((intptr_t)function_pointer);
	glPushDebugGroup(source, id, length, message_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglPopDebugGroup(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glPopDebugGroupPROC glPopDebugGroup = (glPopDebugGroupPROC)((intptr_t)function_pointer);
	glPopDebugGroup();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglObjectLabel(JNIEnv *env, jclass clazz, jint identifier, jint name, jint length, jlong label, jlong function_pointer) {
	const GLchar *label_address = (const GLchar *)(intptr_t)label;
	glObjectLabelPROC glObjectLabel = (glObjectLabelPROC)((intptr_t)function_pointer);
	glObjectLabel(identifier, name, length, label_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglGetObjectLabel(JNIEnv *env, jclass clazz, jint identifier, jint name, jint bufSize, jlong length, jlong label, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *label_address = (GLchar *)(intptr_t)label;
	glGetObjectLabelPROC glGetObjectLabel = (glGetObjectLabelPROC)((intptr_t)function_pointer);
	glGetObjectLabel(identifier, name, bufSize, length_address, label_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglObjectPtrLabel(JNIEnv *env, jclass clazz, jlong ptr, jint length, jlong label, jlong function_pointer) {
	const GLchar *label_address = (const GLchar *)(intptr_t)label;
	glObjectPtrLabelPROC glObjectPtrLabel = (glObjectPtrLabelPROC)((intptr_t)function_pointer);
	glObjectPtrLabel((GLvoid *)(intptr_t)ptr, length, label_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglGetObjectPtrLabel(JNIEnv *env, jclass clazz, jlong ptr, jint bufSize, jlong length, jlong label, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *label_address = (GLchar *)(intptr_t)label;
	glGetObjectPtrLabelPROC glGetObjectPtrLabel = (glGetObjectPtrLabelPROC)((intptr_t)function_pointer);
	glGetObjectPtrLabel((GLvoid *)(intptr_t)ptr, bufSize, length_address, label_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglFramebufferParameteri(JNIEnv *env, jclass clazz, jint target, jint pname, jint param, jlong function_pointer) {
	glFramebufferParameteriPROC glFramebufferParameteri = (glFramebufferParameteriPROC)((intptr_t)function_pointer);
	glFramebufferParameteri(target, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglGetFramebufferParameteriv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetFramebufferParameterivPROC glGetFramebufferParameteriv = (glGetFramebufferParameterivPROC)((intptr_t)function_pointer);
	glGetFramebufferParameteriv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglGetInternalformati64v(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint pname, jint bufSize, jlong params, jlong function_pointer) {
	GLint64 *params_address = (GLint64 *)(intptr_t)params;
	glGetInternalformati64vPROC glGetInternalformati64v = (glGetInternalformati64vPROC)((intptr_t)function_pointer);
	glGetInternalformati64v(target, internalformat, pname, bufSize, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglInvalidateTexSubImage(JNIEnv *env, jclass clazz, jint texture, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jlong function_pointer) {
	glInvalidateTexSubImagePROC glInvalidateTexSubImage = (glInvalidateTexSubImagePROC)((intptr_t)function_pointer);
	glInvalidateTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglInvalidateTexImage(JNIEnv *env, jclass clazz, jint texture, jint level, jlong function_pointer) {
	glInvalidateTexImagePROC glInvalidateTexImage = (glInvalidateTexImagePROC)((intptr_t)function_pointer);
	glInvalidateTexImage(texture, level);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglInvalidateBufferSubData(JNIEnv *env, jclass clazz, jint buffer, jlong offset, jlong length, jlong function_pointer) {
	glInvalidateBufferSubDataPROC glInvalidateBufferSubData = (glInvalidateBufferSubDataPROC)((intptr_t)function_pointer);
	glInvalidateBufferSubData(buffer, offset, length);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglInvalidateBufferData(JNIEnv *env, jclass clazz, jint buffer, jlong function_pointer) {
	glInvalidateBufferDataPROC glInvalidateBufferData = (glInvalidateBufferDataPROC)((intptr_t)function_pointer);
	glInvalidateBufferData(buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglInvalidateFramebuffer(JNIEnv *env, jclass clazz, jint target, jint numAttachments, jlong attachments, jlong function_pointer) {
	const GLenum *attachments_address = (const GLenum *)(intptr_t)attachments;
	glInvalidateFramebufferPROC glInvalidateFramebuffer = (glInvalidateFramebufferPROC)((intptr_t)function_pointer);
	glInvalidateFramebuffer(target, numAttachments, attachments_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglInvalidateSubFramebuffer(JNIEnv *env, jclass clazz, jint target, jint numAttachments, jlong attachments, jint x, jint y, jint width, jint height, jlong function_pointer) {
	const GLenum *attachments_address = (const GLenum *)(intptr_t)attachments;
	glInvalidateSubFramebufferPROC glInvalidateSubFramebuffer = (glInvalidateSubFramebufferPROC)((intptr_t)function_pointer);
	glInvalidateSubFramebuffer(target, numAttachments, attachments_address, x, y, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglMultiDrawArraysIndirect(JNIEnv *env, jclass clazz, jint mode, jlong indirect, jint primcount, jint stride, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)indirect;
	glMultiDrawArraysIndirectPROC glMultiDrawArraysIndirect = (glMultiDrawArraysIndirectPROC)((intptr_t)function_pointer);
	glMultiDrawArraysIndirect(mode, indirect_address, primcount, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglMultiDrawArraysIndirectBO(JNIEnv *env, jclass clazz, jint mode, jlong indirect_buffer_offset, jint primcount, jint stride, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)offsetToPointer(indirect_buffer_offset);
	glMultiDrawArraysIndirectPROC glMultiDrawArraysIndirect = (glMultiDrawArraysIndirectPROC)((intptr_t)function_pointer);
	glMultiDrawArraysIndirect(mode, indirect_address, primcount, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglMultiDrawElementsIndirect(JNIEnv *env, jclass clazz, jint mode, jint type, jlong indirect, jint primcount, jint stride, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)indirect;
	glMultiDrawElementsIndirectPROC glMultiDrawElementsIndirect = (glMultiDrawElementsIndirectPROC)((intptr_t)function_pointer);
	glMultiDrawElementsIndirect(mode, type, indirect_address, primcount, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglMultiDrawElementsIndirectBO(JNIEnv *env, jclass clazz, jint mode, jint type, jlong indirect_buffer_offset, jint primcount, jint stride, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)offsetToPointer(indirect_buffer_offset);
	glMultiDrawElementsIndirectPROC glMultiDrawElementsIndirect = (glMultiDrawElementsIndirectPROC)((intptr_t)function_pointer);
	glMultiDrawElementsIndirect(mode, type, indirect_address, primcount, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglGetProgramInterfaceiv(JNIEnv *env, jclass clazz, jint program, jint programInterface, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetProgramInterfaceivPROC glGetProgramInterfaceiv = (glGetProgramInterfaceivPROC)((intptr_t)function_pointer);
	glGetProgramInterfaceiv(program, programInterface, pname, params_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL43_nglGetProgramResourceIndex(JNIEnv *env, jclass clazz, jint program, jint programInterface, jlong name, jlong function_pointer) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	glGetProgramResourceIndexPROC glGetProgramResourceIndex = (glGetProgramResourceIndexPROC)((intptr_t)function_pointer);
	GLuint __result = glGetProgramResourceIndex(program, programInterface, name_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglGetProgramResourceName(JNIEnv *env, jclass clazz, jint program, jint programInterface, jint index, jint bufSize, jlong length, jlong name, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *name_address = (GLchar *)(intptr_t)name;
	glGetProgramResourceNamePROC glGetProgramResourceName = (glGetProgramResourceNamePROC)((intptr_t)function_pointer);
	glGetProgramResourceName(program, programInterface, index, bufSize, length_address, name_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglGetProgramResourceiv(JNIEnv *env, jclass clazz, jint program, jint programInterface, jint index, jint propCount, jlong props, jint bufSize, jlong length, jlong params, jlong function_pointer) {
	const GLenum *props_address = (const GLenum *)(intptr_t)props;
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetProgramResourceivPROC glGetProgramResourceiv = (glGetProgramResourceivPROC)((intptr_t)function_pointer);
	glGetProgramResourceiv(program, programInterface, index, propCount, props_address, bufSize, length_address, params_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL43_nglGetProgramResourceLocation(JNIEnv *env, jclass clazz, jint program, jint programInterface, jlong name, jlong function_pointer) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	glGetProgramResourceLocationPROC glGetProgramResourceLocation = (glGetProgramResourceLocationPROC)((intptr_t)function_pointer);
	GLint __result = glGetProgramResourceLocation(program, programInterface, name_address);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL43_nglGetProgramResourceLocationIndex(JNIEnv *env, jclass clazz, jint program, jint programInterface, jlong name, jlong function_pointer) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	glGetProgramResourceLocationIndexPROC glGetProgramResourceLocationIndex = (glGetProgramResourceLocationIndexPROC)((intptr_t)function_pointer);
	GLint __result = glGetProgramResourceLocationIndex(program, programInterface, name_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglShaderStorageBlockBinding(JNIEnv *env, jclass clazz, jint program, jint storageBlockIndex, jint storageBlockBinding, jlong function_pointer) {
	glShaderStorageBlockBindingPROC glShaderStorageBlockBinding = (glShaderStorageBlockBindingPROC)((intptr_t)function_pointer);
	glShaderStorageBlockBinding(program, storageBlockIndex, storageBlockBinding);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglTexBufferRange(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint buffer, jlong offset, jlong size, jlong function_pointer) {
	glTexBufferRangePROC glTexBufferRange = (glTexBufferRangePROC)((intptr_t)function_pointer);
	glTexBufferRange(target, internalformat, buffer, offset, size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglTexStorage2DMultisample(JNIEnv *env, jclass clazz, jint target, jint samples, jint internalformat, jint width, jint height, jboolean fixedsamplelocations, jlong function_pointer) {
	glTexStorage2DMultisamplePROC glTexStorage2DMultisample = (glTexStorage2DMultisamplePROC)((intptr_t)function_pointer);
	glTexStorage2DMultisample(target, samples, internalformat, width, height, fixedsamplelocations);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglTexStorage3DMultisample(JNIEnv *env, jclass clazz, jint target, jint samples, jint internalformat, jint width, jint height, jint depth, jboolean fixedsamplelocations, jlong function_pointer) {
	glTexStorage3DMultisamplePROC glTexStorage3DMultisample = (glTexStorage3DMultisamplePROC)((intptr_t)function_pointer);
	glTexStorage3DMultisample(target, samples, internalformat, width, height, depth, fixedsamplelocations);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglTextureView(JNIEnv *env, jclass clazz, jint texture, jint target, jint origtexture, jint internalformat, jint minlevel, jint numlevels, jint minlayer, jint numlayers, jlong function_pointer) {
	glTextureViewPROC glTextureView = (glTextureViewPROC)((intptr_t)function_pointer);
	glTextureView(texture, target, origtexture, internalformat, minlevel, numlevels, minlayer, numlayers);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglBindVertexBuffer(JNIEnv *env, jclass clazz, jint bindingindex, jint buffer, jlong offset, jint stride, jlong function_pointer) {
	glBindVertexBufferPROC glBindVertexBuffer = (glBindVertexBufferPROC)((intptr_t)function_pointer);
	glBindVertexBuffer(bindingindex, buffer, offset, stride);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglVertexAttribFormat(JNIEnv *env, jclass clazz, jint attribindex, jint size, jint type, jboolean normalized, jint relativeoffset, jlong function_pointer) {
	glVertexAttribFormatPROC glVertexAttribFormat = (glVertexAttribFormatPROC)((intptr_t)function_pointer);
	glVertexAttribFormat(attribindex, size, type, normalized, relativeoffset);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglVertexAttribIFormat(JNIEnv *env, jclass clazz, jint attribindex, jint size, jint type, jint relativeoffset, jlong function_pointer) {
	glVertexAttribIFormatPROC glVertexAttribIFormat = (glVertexAttribIFormatPROC)((intptr_t)function_pointer);
	glVertexAttribIFormat(attribindex, size, type, relativeoffset);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglVertexAttribLFormat(JNIEnv *env, jclass clazz, jint attribindex, jint size, jint type, jint relativeoffset, jlong function_pointer) {
	glVertexAttribLFormatPROC glVertexAttribLFormat = (glVertexAttribLFormatPROC)((intptr_t)function_pointer);
	glVertexAttribLFormat(attribindex, size, type, relativeoffset);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglVertexAttribBinding(JNIEnv *env, jclass clazz, jint attribindex, jint bindingindex, jlong function_pointer) {
	glVertexAttribBindingPROC glVertexAttribBinding = (glVertexAttribBindingPROC)((intptr_t)function_pointer);
	glVertexAttribBinding(attribindex, bindingindex);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL43_nglVertexBindingDivisor(JNIEnv *env, jclass clazz, jint bindingindex, jint divisor, jlong function_pointer) {
	glVertexBindingDivisorPROC glVertexBindingDivisor = (glVertexBindingDivisorPROC)((intptr_t)function_pointer);
	glVertexBindingDivisor(bindingindex, divisor);
}

