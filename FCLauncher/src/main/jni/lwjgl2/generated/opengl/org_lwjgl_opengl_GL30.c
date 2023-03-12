/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GLubyte * (APIENTRY *glGetStringiPROC) (GLenum name, GLuint index);
typedef void (APIENTRY *glClearBufferfvPROC) (GLenum buffer, GLint drawbuffer, const GLfloat * value);
typedef void (APIENTRY *glClearBufferivPROC) (GLenum buffer, GLint drawbuffer, const GLint * value);
typedef void (APIENTRY *glClearBufferuivPROC) (GLenum buffer, GLint drawbuffer, const GLint * value);
typedef void (APIENTRY *glClearBufferfiPROC) (GLenum buffer, GLint drawbuffer, GLfloat depth, GLint stencil);
typedef void (APIENTRY *glVertexAttribI1iPROC) (GLuint index, GLint x);
typedef void (APIENTRY *glVertexAttribI2iPROC) (GLuint index, GLint x, GLint y);
typedef void (APIENTRY *glVertexAttribI3iPROC) (GLuint index, GLint x, GLint y, GLint z);
typedef void (APIENTRY *glVertexAttribI4iPROC) (GLuint index, GLint x, GLint y, GLint z, GLint w);
typedef void (APIENTRY *glVertexAttribI1uiPROC) (GLuint index, GLuint x);
typedef void (APIENTRY *glVertexAttribI2uiPROC) (GLuint index, GLuint x, GLuint y);
typedef void (APIENTRY *glVertexAttribI3uiPROC) (GLuint index, GLuint x, GLuint y, GLuint z);
typedef void (APIENTRY *glVertexAttribI4uiPROC) (GLuint index, GLuint x, GLuint y, GLuint z, GLuint w);
typedef void (APIENTRY *glVertexAttribI1ivPROC) (GLuint index, const GLint * v);
typedef void (APIENTRY *glVertexAttribI2ivPROC) (GLuint index, const GLint * v);
typedef void (APIENTRY *glVertexAttribI3ivPROC) (GLuint index, const GLint * v);
typedef void (APIENTRY *glVertexAttribI4ivPROC) (GLuint index, const GLint * v);
typedef void (APIENTRY *glVertexAttribI1uivPROC) (GLuint index, const GLuint * v);
typedef void (APIENTRY *glVertexAttribI2uivPROC) (GLuint index, const GLuint * v);
typedef void (APIENTRY *glVertexAttribI3uivPROC) (GLuint index, const GLuint * v);
typedef void (APIENTRY *glVertexAttribI4uivPROC) (GLuint index, const GLuint * v);
typedef void (APIENTRY *glVertexAttribI4bvPROC) (GLuint index, const GLbyte * v);
typedef void (APIENTRY *glVertexAttribI4svPROC) (GLuint index, const GLshort * v);
typedef void (APIENTRY *glVertexAttribI4ubvPROC) (GLuint index, const GLubyte * v);
typedef void (APIENTRY *glVertexAttribI4usvPROC) (GLuint index, const GLushort * v);
typedef void (APIENTRY *glVertexAttribIPointerPROC) (GLuint index, GLint size, GLenum type, GLsizei stride, const GLvoid * buffer);
typedef void (APIENTRY *glGetVertexAttribIivPROC) (GLuint index, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetVertexAttribIuivPROC) (GLuint index, GLenum pname, GLuint * params);
typedef void (APIENTRY *glUniform1uiPROC) (GLint location, GLuint v0);
typedef void (APIENTRY *glUniform2uiPROC) (GLint location, GLuint v0, GLuint v1);
typedef void (APIENTRY *glUniform3uiPROC) (GLint location, GLuint v0, GLuint v1, GLuint v2);
typedef void (APIENTRY *glUniform4uiPROC) (GLint location, GLuint v0, GLuint v1, GLuint v2, GLuint v3);
typedef void (APIENTRY *glUniform1uivPROC) (GLint location, GLsizei count, const GLuint * value);
typedef void (APIENTRY *glUniform2uivPROC) (GLint location, GLsizei count, const GLuint * value);
typedef void (APIENTRY *glUniform3uivPROC) (GLint location, GLsizei count, const GLuint * value);
typedef void (APIENTRY *glUniform4uivPROC) (GLint location, GLsizei count, const GLuint * value);
typedef void (APIENTRY *glGetUniformuivPROC) (GLuint program, GLint location, GLuint * params);
typedef void (APIENTRY *glBindFragDataLocationPROC) (GLuint program, GLuint colorNumber, const GLchar * name);
typedef GLint (APIENTRY *glGetFragDataLocationPROC) (GLuint program, const GLchar * name);
typedef void (APIENTRY *glBeginConditionalRenderPROC) (GLuint id, GLenum mode);
typedef void (APIENTRY *glEndConditionalRenderPROC) ();
typedef GLvoid * (APIENTRY *glMapBufferRangePROC) (GLenum target, GLintptr offset, GLsizeiptr length, GLbitfield access);
typedef void (APIENTRY *glFlushMappedBufferRangePROC) (GLenum target, GLintptr offset, GLsizeiptr length);
typedef void (APIENTRY *glClampColorPROC) (GLenum target, GLenum clamp);
typedef GLboolean (APIENTRY *glIsRenderbufferPROC) (GLuint renderbuffer);
typedef void (APIENTRY *glBindRenderbufferPROC) (GLenum target, GLuint renderbuffer);
typedef void (APIENTRY *glDeleteRenderbuffersPROC) (GLint n, const GLuint * renderbuffers);
typedef void (APIENTRY *glGenRenderbuffersPROC) (GLint n, GLuint * renderbuffers);
typedef void (APIENTRY *glRenderbufferStoragePROC) (GLenum target, GLenum internalformat, GLsizei width, GLsizei height);
typedef void (APIENTRY *glGetRenderbufferParameterivPROC) (GLenum target, GLenum pname, GLint * params);
typedef GLboolean (APIENTRY *glIsFramebufferPROC) (GLuint framebuffer);
typedef void (APIENTRY *glBindFramebufferPROC) (GLenum target, GLuint framebuffer);
typedef void (APIENTRY *glDeleteFramebuffersPROC) (GLint n, const GLuint * framebuffers);
typedef void (APIENTRY *glGenFramebuffersPROC) (GLint n, GLuint * framebuffers);
typedef GLenum (APIENTRY *glCheckFramebufferStatusPROC) (GLenum target);
typedef void (APIENTRY *glFramebufferTexture1DPROC) (GLenum target, GLenum attachment, GLenum textarget, GLuint texture, GLint level);
typedef void (APIENTRY *glFramebufferTexture2DPROC) (GLenum target, GLenum attachment, GLenum textarget, GLuint texture, GLint level);
typedef void (APIENTRY *glFramebufferTexture3DPROC) (GLenum target, GLenum attachment, GLenum textarget, GLuint texture, GLint level, GLint zoffset);
typedef void (APIENTRY *glFramebufferRenderbufferPROC) (GLenum target, GLenum attachment, GLenum renderbuffertarget, GLuint renderbuffer);
typedef void (APIENTRY *glGetFramebufferAttachmentParameterivPROC) (GLenum target, GLenum attachment, GLenum pname, GLint * params);
typedef void (APIENTRY *glGenerateMipmapPROC) (GLenum target);
typedef void (APIENTRY *glRenderbufferStorageMultisamplePROC) (GLenum target, GLsizei samples, GLenum internalformat, GLsizei width, GLsizei height);
typedef void (APIENTRY *glBlitFramebufferPROC) (GLint srcX0, GLint srcY0, GLint srcX1, GLint srcY1, GLint dstX0, GLint dstY0, GLint dstX1, GLint dstY1, GLbitfield mask, GLenum filter);
typedef void (APIENTRY *glTexParameterIivPROC) (GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glTexParameterIuivPROC) (GLenum target, GLenum pname, GLuint * params);
typedef void (APIENTRY *glGetTexParameterIivPROC) (GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetTexParameterIuivPROC) (GLenum target, GLenum pname, GLuint * params);
typedef void (APIENTRY *glFramebufferTextureLayerPROC) (GLenum target, GLenum attachment, GLuint texture, GLint level, GLint layer);
typedef void (APIENTRY *glColorMaskiPROC) (GLuint buf, GLboolean r, GLboolean g, GLboolean b, GLboolean a);
typedef void (APIENTRY *glGetBooleani_vPROC) (GLenum value, GLuint index, GLboolean * data);
typedef void (APIENTRY *glGetIntegeri_vPROC) (GLenum value, GLuint index, GLint * data);
typedef void (APIENTRY *glEnableiPROC) (GLenum target, GLuint index);
typedef void (APIENTRY *glDisableiPROC) (GLenum target, GLuint index);
typedef GLboolean (APIENTRY *glIsEnablediPROC) (GLenum target, GLuint index);
typedef void (APIENTRY *glBindBufferRangePROC) (GLenum target, GLuint index, GLuint buffer, GLintptr offset, GLsizeiptr size);
typedef void (APIENTRY *glBindBufferBasePROC) (GLenum target, GLuint index, GLuint buffer);
typedef void (APIENTRY *glBeginTransformFeedbackPROC) (GLenum primitiveMode);
typedef void (APIENTRY *glEndTransformFeedbackPROC) ();
typedef void (APIENTRY *glTransformFeedbackVaryingsPROC) (GLuint program, GLsizei count, const GLchar ** varyings, GLenum bufferMode);
typedef void (APIENTRY *glGetTransformFeedbackVaryingPROC) (GLuint program, GLuint index, GLsizei bufSize, GLsizei * length, GLsizei * size, GLenum * type, GLchar * name);
typedef void (APIENTRY *glBindVertexArrayPROC) (GLuint array);
typedef void (APIENTRY *glDeleteVertexArraysPROC) (GLsizei n, const GLuint * arrays);
typedef void (APIENTRY *glGenVertexArraysPROC) (GLsizei n, GLuint * arrays);
typedef GLboolean (APIENTRY *glIsVertexArrayPROC) (GLuint array);

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_GL30_nglGetStringi(JNIEnv *env, jclass clazz, jint name, jint index, jlong function_pointer) {
	glGetStringiPROC glGetStringi = (glGetStringiPROC)((intptr_t)function_pointer);
	GLubyte * __result = glGetStringi(name, index);
	return NewStringNativeUnsigned(env, __result);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglClearBufferfv(JNIEnv *env, jclass clazz, jint buffer, jint drawbuffer, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glClearBufferfvPROC glClearBufferfv = (glClearBufferfvPROC)((intptr_t)function_pointer);
	glClearBufferfv(buffer, drawbuffer, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglClearBufferiv(JNIEnv *env, jclass clazz, jint buffer, jint drawbuffer, jlong value, jlong function_pointer) {
	const GLint *value_address = (const GLint *)(intptr_t)value;
	glClearBufferivPROC glClearBufferiv = (glClearBufferivPROC)((intptr_t)function_pointer);
	glClearBufferiv(buffer, drawbuffer, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglClearBufferuiv(JNIEnv *env, jclass clazz, jint buffer, jint drawbuffer, jlong value, jlong function_pointer) {
	const GLint *value_address = (const GLint *)(intptr_t)value;
	glClearBufferuivPROC glClearBufferuiv = (glClearBufferuivPROC)((intptr_t)function_pointer);
	glClearBufferuiv(buffer, drawbuffer, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglClearBufferfi(JNIEnv *env, jclass clazz, jint buffer, jint drawbuffer, jfloat depth, jint stencil, jlong function_pointer) {
	glClearBufferfiPROC glClearBufferfi = (glClearBufferfiPROC)((intptr_t)function_pointer);
	glClearBufferfi(buffer, drawbuffer, depth, stencil);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI1i(JNIEnv *env, jclass clazz, jint index, jint x, jlong function_pointer) {
	glVertexAttribI1iPROC glVertexAttribI1i = (glVertexAttribI1iPROC)((intptr_t)function_pointer);
	glVertexAttribI1i(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI2i(JNIEnv *env, jclass clazz, jint index, jint x, jint y, jlong function_pointer) {
	glVertexAttribI2iPROC glVertexAttribI2i = (glVertexAttribI2iPROC)((intptr_t)function_pointer);
	glVertexAttribI2i(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI3i(JNIEnv *env, jclass clazz, jint index, jint x, jint y, jint z, jlong function_pointer) {
	glVertexAttribI3iPROC glVertexAttribI3i = (glVertexAttribI3iPROC)((intptr_t)function_pointer);
	glVertexAttribI3i(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI4i(JNIEnv *env, jclass clazz, jint index, jint x, jint y, jint z, jint w, jlong function_pointer) {
	glVertexAttribI4iPROC glVertexAttribI4i = (glVertexAttribI4iPROC)((intptr_t)function_pointer);
	glVertexAttribI4i(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI1ui(JNIEnv *env, jclass clazz, jint index, jint x, jlong function_pointer) {
	glVertexAttribI1uiPROC glVertexAttribI1ui = (glVertexAttribI1uiPROC)((intptr_t)function_pointer);
	glVertexAttribI1ui(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI2ui(JNIEnv *env, jclass clazz, jint index, jint x, jint y, jlong function_pointer) {
	glVertexAttribI2uiPROC glVertexAttribI2ui = (glVertexAttribI2uiPROC)((intptr_t)function_pointer);
	glVertexAttribI2ui(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI3ui(JNIEnv *env, jclass clazz, jint index, jint x, jint y, jint z, jlong function_pointer) {
	glVertexAttribI3uiPROC glVertexAttribI3ui = (glVertexAttribI3uiPROC)((intptr_t)function_pointer);
	glVertexAttribI3ui(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI4ui(JNIEnv *env, jclass clazz, jint index, jint x, jint y, jint z, jint w, jlong function_pointer) {
	glVertexAttribI4uiPROC glVertexAttribI4ui = (glVertexAttribI4uiPROC)((intptr_t)function_pointer);
	glVertexAttribI4ui(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI1iv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLint *v_address = (const GLint *)(intptr_t)v;
	glVertexAttribI1ivPROC glVertexAttribI1iv = (glVertexAttribI1ivPROC)((intptr_t)function_pointer);
	glVertexAttribI1iv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI2iv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLint *v_address = (const GLint *)(intptr_t)v;
	glVertexAttribI2ivPROC glVertexAttribI2iv = (glVertexAttribI2ivPROC)((intptr_t)function_pointer);
	glVertexAttribI2iv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI3iv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLint *v_address = (const GLint *)(intptr_t)v;
	glVertexAttribI3ivPROC glVertexAttribI3iv = (glVertexAttribI3ivPROC)((intptr_t)function_pointer);
	glVertexAttribI3iv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI4iv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLint *v_address = (const GLint *)(intptr_t)v;
	glVertexAttribI4ivPROC glVertexAttribI4iv = (glVertexAttribI4ivPROC)((intptr_t)function_pointer);
	glVertexAttribI4iv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI1uiv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLuint *v_address = (const GLuint *)(intptr_t)v;
	glVertexAttribI1uivPROC glVertexAttribI1uiv = (glVertexAttribI1uivPROC)((intptr_t)function_pointer);
	glVertexAttribI1uiv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI2uiv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLuint *v_address = (const GLuint *)(intptr_t)v;
	glVertexAttribI2uivPROC glVertexAttribI2uiv = (glVertexAttribI2uivPROC)((intptr_t)function_pointer);
	glVertexAttribI2uiv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI3uiv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLuint *v_address = (const GLuint *)(intptr_t)v;
	glVertexAttribI3uivPROC glVertexAttribI3uiv = (glVertexAttribI3uivPROC)((intptr_t)function_pointer);
	glVertexAttribI3uiv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI4uiv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLuint *v_address = (const GLuint *)(intptr_t)v;
	glVertexAttribI4uivPROC glVertexAttribI4uiv = (glVertexAttribI4uivPROC)((intptr_t)function_pointer);
	glVertexAttribI4uiv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI4bv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLbyte *v_address = (const GLbyte *)(intptr_t)v;
	glVertexAttribI4bvPROC glVertexAttribI4bv = (glVertexAttribI4bvPROC)((intptr_t)function_pointer);
	glVertexAttribI4bv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI4sv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLshort *v_address = (const GLshort *)(intptr_t)v;
	glVertexAttribI4svPROC glVertexAttribI4sv = (glVertexAttribI4svPROC)((intptr_t)function_pointer);
	glVertexAttribI4sv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI4ubv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLubyte *v_address = (const GLubyte *)(intptr_t)v;
	glVertexAttribI4ubvPROC glVertexAttribI4ubv = (glVertexAttribI4ubvPROC)((intptr_t)function_pointer);
	glVertexAttribI4ubv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribI4usv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLushort *v_address = (const GLushort *)(intptr_t)v;
	glVertexAttribI4usvPROC glVertexAttribI4usv = (glVertexAttribI4usvPROC)((intptr_t)function_pointer);
	glVertexAttribI4usv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribIPointer(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jint stride, jlong buffer, jlong function_pointer) {
	const GLvoid *buffer_address = (const GLvoid *)(intptr_t)buffer;
	glVertexAttribIPointerPROC glVertexAttribIPointer = (glVertexAttribIPointerPROC)((intptr_t)function_pointer);
	glVertexAttribIPointer(index, size, type, stride, buffer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglVertexAttribIPointerBO(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jint stride, jlong buffer_buffer_offset, jlong function_pointer) {
	const GLvoid *buffer_address = (const GLvoid *)(intptr_t)offsetToPointer(buffer_buffer_offset);
	glVertexAttribIPointerPROC glVertexAttribIPointer = (glVertexAttribIPointerPROC)((intptr_t)function_pointer);
	glVertexAttribIPointer(index, size, type, stride, buffer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglGetVertexAttribIiv(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetVertexAttribIivPROC glGetVertexAttribIiv = (glGetVertexAttribIivPROC)((intptr_t)function_pointer);
	glGetVertexAttribIiv(index, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglGetVertexAttribIuiv(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetVertexAttribIuivPROC glGetVertexAttribIuiv = (glGetVertexAttribIuivPROC)((intptr_t)function_pointer);
	glGetVertexAttribIuiv(index, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglUniform1ui(JNIEnv *env, jclass clazz, jint location, jint v0, jlong function_pointer) {
	glUniform1uiPROC glUniform1ui = (glUniform1uiPROC)((intptr_t)function_pointer);
	glUniform1ui(location, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglUniform2ui(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1, jlong function_pointer) {
	glUniform2uiPROC glUniform2ui = (glUniform2uiPROC)((intptr_t)function_pointer);
	glUniform2ui(location, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglUniform3ui(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1, jint v2, jlong function_pointer) {
	glUniform3uiPROC glUniform3ui = (glUniform3uiPROC)((intptr_t)function_pointer);
	glUniform3ui(location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglUniform4ui(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1, jint v2, jint v3, jlong function_pointer) {
	glUniform4uiPROC glUniform4ui = (glUniform4uiPROC)((intptr_t)function_pointer);
	glUniform4ui(location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglUniform1uiv(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glUniform1uivPROC glUniform1uiv = (glUniform1uivPROC)((intptr_t)function_pointer);
	glUniform1uiv(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglUniform2uiv(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glUniform2uivPROC glUniform2uiv = (glUniform2uivPROC)((intptr_t)function_pointer);
	glUniform2uiv(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglUniform3uiv(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glUniform3uivPROC glUniform3uiv = (glUniform3uivPROC)((intptr_t)function_pointer);
	glUniform3uiv(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglUniform4uiv(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glUniform4uivPROC glUniform4uiv = (glUniform4uivPROC)((intptr_t)function_pointer);
	glUniform4uiv(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglGetUniformuiv(JNIEnv *env, jclass clazz, jint program, jint location, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetUniformuivPROC glGetUniformuiv = (glGetUniformuivPROC)((intptr_t)function_pointer);
	glGetUniformuiv(program, location, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglBindFragDataLocation(JNIEnv *env, jclass clazz, jint program, jint colorNumber, jlong name, jlong function_pointer) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	glBindFragDataLocationPROC glBindFragDataLocation = (glBindFragDataLocationPROC)((intptr_t)function_pointer);
	glBindFragDataLocation(program, colorNumber, name_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL30_nglGetFragDataLocation(JNIEnv *env, jclass clazz, jint program, jlong name, jlong function_pointer) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	glGetFragDataLocationPROC glGetFragDataLocation = (glGetFragDataLocationPROC)((intptr_t)function_pointer);
	GLint __result = glGetFragDataLocation(program, name_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglBeginConditionalRender(JNIEnv *env, jclass clazz, jint id, jint mode, jlong function_pointer) {
	glBeginConditionalRenderPROC glBeginConditionalRender = (glBeginConditionalRenderPROC)((intptr_t)function_pointer);
	glBeginConditionalRender(id, mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglEndConditionalRender(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glEndConditionalRenderPROC glEndConditionalRender = (glEndConditionalRenderPROC)((intptr_t)function_pointer);
	glEndConditionalRender();
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_GL30_nglMapBufferRange(JNIEnv *env, jclass clazz, jint target, jlong offset, jlong length, jint access, jobject old_buffer, jlong function_pointer) {
	glMapBufferRangePROC glMapBufferRange = (glMapBufferRangePROC)((intptr_t)function_pointer);
	GLvoid * __result = glMapBufferRange(target, offset, length, access);
	return safeNewBufferCached(env, __result, length, old_buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglFlushMappedBufferRange(JNIEnv *env, jclass clazz, jint target, jlong offset, jlong length, jlong function_pointer) {
	glFlushMappedBufferRangePROC glFlushMappedBufferRange = (glFlushMappedBufferRangePROC)((intptr_t)function_pointer);
	glFlushMappedBufferRange(target, offset, length);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglClampColor(JNIEnv *env, jclass clazz, jint target, jint clamp, jlong function_pointer) {
	glClampColorPROC glClampColor = (glClampColorPROC)((intptr_t)function_pointer);
	glClampColor(target, clamp);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL30_nglIsRenderbuffer(JNIEnv *env, jclass clazz, jint renderbuffer, jlong function_pointer) {
	glIsRenderbufferPROC glIsRenderbuffer = (glIsRenderbufferPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsRenderbuffer(renderbuffer);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglBindRenderbuffer(JNIEnv *env, jclass clazz, jint target, jint renderbuffer, jlong function_pointer) {
	glBindRenderbufferPROC glBindRenderbuffer = (glBindRenderbufferPROC)((intptr_t)function_pointer);
	glBindRenderbuffer(target, renderbuffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglDeleteRenderbuffers(JNIEnv *env, jclass clazz, jint n, jlong renderbuffers, jlong function_pointer) {
	const GLuint *renderbuffers_address = (const GLuint *)(intptr_t)renderbuffers;
	glDeleteRenderbuffersPROC glDeleteRenderbuffers = (glDeleteRenderbuffersPROC)((intptr_t)function_pointer);
	glDeleteRenderbuffers(n, renderbuffers_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglGenRenderbuffers(JNIEnv *env, jclass clazz, jint n, jlong renderbuffers, jlong function_pointer) {
	GLuint *renderbuffers_address = (GLuint *)(intptr_t)renderbuffers;
	glGenRenderbuffersPROC glGenRenderbuffers = (glGenRenderbuffersPROC)((intptr_t)function_pointer);
	glGenRenderbuffers(n, renderbuffers_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglRenderbufferStorage(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint width, jint height, jlong function_pointer) {
	glRenderbufferStoragePROC glRenderbufferStorage = (glRenderbufferStoragePROC)((intptr_t)function_pointer);
	glRenderbufferStorage(target, internalformat, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglGetRenderbufferParameteriv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetRenderbufferParameterivPROC glGetRenderbufferParameteriv = (glGetRenderbufferParameterivPROC)((intptr_t)function_pointer);
	glGetRenderbufferParameteriv(target, pname, params_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL30_nglIsFramebuffer(JNIEnv *env, jclass clazz, jint framebuffer, jlong function_pointer) {
	glIsFramebufferPROC glIsFramebuffer = (glIsFramebufferPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsFramebuffer(framebuffer);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglBindFramebuffer(JNIEnv *env, jclass clazz, jint target, jint framebuffer, jlong function_pointer) {
	glBindFramebufferPROC glBindFramebuffer = (glBindFramebufferPROC)((intptr_t)function_pointer);
	glBindFramebuffer(target, framebuffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglDeleteFramebuffers(JNIEnv *env, jclass clazz, jint n, jlong framebuffers, jlong function_pointer) {
	const GLuint *framebuffers_address = (const GLuint *)(intptr_t)framebuffers;
	glDeleteFramebuffersPROC glDeleteFramebuffers = (glDeleteFramebuffersPROC)((intptr_t)function_pointer);
	glDeleteFramebuffers(n, framebuffers_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglGenFramebuffers(JNIEnv *env, jclass clazz, jint n, jlong framebuffers, jlong function_pointer) {
	GLuint *framebuffers_address = (GLuint *)(intptr_t)framebuffers;
	glGenFramebuffersPROC glGenFramebuffers = (glGenFramebuffersPROC)((intptr_t)function_pointer);
	glGenFramebuffers(n, framebuffers_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL30_nglCheckFramebufferStatus(JNIEnv *env, jclass clazz, jint target, jlong function_pointer) {
	glCheckFramebufferStatusPROC glCheckFramebufferStatus = (glCheckFramebufferStatusPROC)((intptr_t)function_pointer);
	GLenum __result = glCheckFramebufferStatus(target);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglFramebufferTexture1D(JNIEnv *env, jclass clazz, jint target, jint attachment, jint textarget, jint texture, jint level, jlong function_pointer) {
	glFramebufferTexture1DPROC glFramebufferTexture1D = (glFramebufferTexture1DPROC)((intptr_t)function_pointer);
	glFramebufferTexture1D(target, attachment, textarget, texture, level);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglFramebufferTexture2D(JNIEnv *env, jclass clazz, jint target, jint attachment, jint textarget, jint texture, jint level, jlong function_pointer) {
	glFramebufferTexture2DPROC glFramebufferTexture2D = (glFramebufferTexture2DPROC)((intptr_t)function_pointer);
	glFramebufferTexture2D(target, attachment, textarget, texture, level);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglFramebufferTexture3D(JNIEnv *env, jclass clazz, jint target, jint attachment, jint textarget, jint texture, jint level, jint zoffset, jlong function_pointer) {
	glFramebufferTexture3DPROC glFramebufferTexture3D = (glFramebufferTexture3DPROC)((intptr_t)function_pointer);
	glFramebufferTexture3D(target, attachment, textarget, texture, level, zoffset);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglFramebufferRenderbuffer(JNIEnv *env, jclass clazz, jint target, jint attachment, jint renderbuffertarget, jint renderbuffer, jlong function_pointer) {
	glFramebufferRenderbufferPROC glFramebufferRenderbuffer = (glFramebufferRenderbufferPROC)((intptr_t)function_pointer);
	glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglGetFramebufferAttachmentParameteriv(JNIEnv *env, jclass clazz, jint target, jint attachment, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetFramebufferAttachmentParameterivPROC glGetFramebufferAttachmentParameteriv = (glGetFramebufferAttachmentParameterivPROC)((intptr_t)function_pointer);
	glGetFramebufferAttachmentParameteriv(target, attachment, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglGenerateMipmap(JNIEnv *env, jclass clazz, jint target, jlong function_pointer) {
	glGenerateMipmapPROC glGenerateMipmap = (glGenerateMipmapPROC)((intptr_t)function_pointer);
	glGenerateMipmap(target);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglRenderbufferStorageMultisample(JNIEnv *env, jclass clazz, jint target, jint samples, jint internalformat, jint width, jint height, jlong function_pointer) {
	glRenderbufferStorageMultisamplePROC glRenderbufferStorageMultisample = (glRenderbufferStorageMultisamplePROC)((intptr_t)function_pointer);
	glRenderbufferStorageMultisample(target, samples, internalformat, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglBlitFramebuffer(JNIEnv *env, jclass clazz, jint srcX0, jint srcY0, jint srcX1, jint srcY1, jint dstX0, jint dstY0, jint dstX1, jint dstY1, jint mask, jint filter, jlong function_pointer) {
	glBlitFramebufferPROC glBlitFramebuffer = (glBlitFramebufferPROC)((intptr_t)function_pointer);
	glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglTexParameterIiv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glTexParameterIivPROC glTexParameterIiv = (glTexParameterIivPROC)((intptr_t)function_pointer);
	glTexParameterIiv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglTexParameterIuiv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glTexParameterIuivPROC glTexParameterIuiv = (glTexParameterIuivPROC)((intptr_t)function_pointer);
	glTexParameterIuiv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglGetTexParameterIiv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetTexParameterIivPROC glGetTexParameterIiv = (glGetTexParameterIivPROC)((intptr_t)function_pointer);
	glGetTexParameterIiv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglGetTexParameterIuiv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetTexParameterIuivPROC glGetTexParameterIuiv = (glGetTexParameterIuivPROC)((intptr_t)function_pointer);
	glGetTexParameterIuiv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglFramebufferTextureLayer(JNIEnv *env, jclass clazz, jint target, jint attachment, jint texture, jint level, jint layer, jlong function_pointer) {
	glFramebufferTextureLayerPROC glFramebufferTextureLayer = (glFramebufferTextureLayerPROC)((intptr_t)function_pointer);
	glFramebufferTextureLayer(target, attachment, texture, level, layer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglColorMaski(JNIEnv *env, jclass clazz, jint buf, jboolean r, jboolean g, jboolean b, jboolean a, jlong function_pointer) {
	glColorMaskiPROC glColorMaski = (glColorMaskiPROC)((intptr_t)function_pointer);
	glColorMaski(buf, r, g, b, a);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglGetBooleani_1v(JNIEnv *env, jclass clazz, jint value, jint index, jlong data, jlong function_pointer) {
	GLboolean *data_address = (GLboolean *)(intptr_t)data;
	glGetBooleani_vPROC glGetBooleani_v = (glGetBooleani_vPROC)((intptr_t)function_pointer);
	glGetBooleani_v(value, index, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglGetIntegeri_1v(JNIEnv *env, jclass clazz, jint value, jint index, jlong data, jlong function_pointer) {
	GLint *data_address = (GLint *)(intptr_t)data;
	glGetIntegeri_vPROC glGetIntegeri_v = (glGetIntegeri_vPROC)((intptr_t)function_pointer);
	glGetIntegeri_v(value, index, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglEnablei(JNIEnv *env, jclass clazz, jint target, jint index, jlong function_pointer) {
	glEnableiPROC glEnablei = (glEnableiPROC)((intptr_t)function_pointer);
	glEnablei(target, index);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglDisablei(JNIEnv *env, jclass clazz, jint target, jint index, jlong function_pointer) {
	glDisableiPROC glDisablei = (glDisableiPROC)((intptr_t)function_pointer);
	glDisablei(target, index);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL30_nglIsEnabledi(JNIEnv *env, jclass clazz, jint target, jint index, jlong function_pointer) {
	glIsEnablediPROC glIsEnabledi = (glIsEnablediPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsEnabledi(target, index);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglBindBufferRange(JNIEnv *env, jclass clazz, jint target, jint index, jint buffer, jlong offset, jlong size, jlong function_pointer) {
	glBindBufferRangePROC glBindBufferRange = (glBindBufferRangePROC)((intptr_t)function_pointer);
	glBindBufferRange(target, index, buffer, offset, size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglBindBufferBase(JNIEnv *env, jclass clazz, jint target, jint index, jint buffer, jlong function_pointer) {
	glBindBufferBasePROC glBindBufferBase = (glBindBufferBasePROC)((intptr_t)function_pointer);
	glBindBufferBase(target, index, buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglBeginTransformFeedback(JNIEnv *env, jclass clazz, jint primitiveMode, jlong function_pointer) {
	glBeginTransformFeedbackPROC glBeginTransformFeedback = (glBeginTransformFeedbackPROC)((intptr_t)function_pointer);
	glBeginTransformFeedback(primitiveMode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglEndTransformFeedback(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glEndTransformFeedbackPROC glEndTransformFeedback = (glEndTransformFeedbackPROC)((intptr_t)function_pointer);
	glEndTransformFeedback();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglTransformFeedbackVaryings(JNIEnv *env, jclass clazz, jint program, jint count, jlong varyings, jint bufferMode, jlong function_pointer) {
	const GLchar *varyings_address = (const GLchar *)(intptr_t)varyings;
	int _str_i;
	GLchar *_str_address;
	GLchar **varyings_str = (GLchar **) malloc(count * sizeof(GLchar *));
	glTransformFeedbackVaryingsPROC glTransformFeedbackVaryings = (glTransformFeedbackVaryingsPROC)((intptr_t)function_pointer);
	_str_i = 0;
	_str_address = (GLchar *)varyings_address;
	while ( _str_i < count ) {
		varyings_str[_str_i++] = _str_address;
		_str_address += strlen((const char *)_str_address) + 1;
	}
	glTransformFeedbackVaryings(program, count, (const GLchar **)varyings_str, bufferMode);
	free(varyings_str);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglGetTransformFeedbackVarying(JNIEnv *env, jclass clazz, jint program, jint index, jint bufSize, jlong length, jlong size, jlong type, jlong name, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLsizei *size_address = (GLsizei *)(intptr_t)size;
	GLenum *type_address = (GLenum *)(intptr_t)type;
	GLchar *name_address = (GLchar *)(intptr_t)name;
	glGetTransformFeedbackVaryingPROC glGetTransformFeedbackVarying = (glGetTransformFeedbackVaryingPROC)((intptr_t)function_pointer);
	glGetTransformFeedbackVarying(program, index, bufSize, length_address, size_address, type_address, name_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglBindVertexArray(JNIEnv *env, jclass clazz, jint array, jlong function_pointer) {
	glBindVertexArrayPROC glBindVertexArray = (glBindVertexArrayPROC)((intptr_t)function_pointer);
	glBindVertexArray(array);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglDeleteVertexArrays(JNIEnv *env, jclass clazz, jint n, jlong arrays, jlong function_pointer) {
	const GLuint *arrays_address = (const GLuint *)(intptr_t)arrays;
	glDeleteVertexArraysPROC glDeleteVertexArrays = (glDeleteVertexArraysPROC)((intptr_t)function_pointer);
	glDeleteVertexArrays(n, arrays_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL30_nglGenVertexArrays(JNIEnv *env, jclass clazz, jint n, jlong arrays, jlong function_pointer) {
	GLuint *arrays_address = (GLuint *)(intptr_t)arrays;
	glGenVertexArraysPROC glGenVertexArrays = (glGenVertexArraysPROC)((intptr_t)function_pointer);
	glGenVertexArrays(n, arrays_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL30_nglIsVertexArray(JNIEnv *env, jclass clazz, jint array, jlong function_pointer) {
	glIsVertexArrayPROC glIsVertexArray = (glIsVertexArrayPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsVertexArray(array);
	return __result;
}

