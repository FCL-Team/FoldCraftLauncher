/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBindFragDataLocationIndexedPROC) (GLuint program, GLuint colorNumber, GLuint index, const GLchar * name);
typedef GLint (APIENTRY *glGetFragDataIndexPROC) (GLuint program, const GLchar * name);
typedef void (APIENTRY *glGenSamplersPROC) (GLsizei count, GLuint * samplers);
typedef void (APIENTRY *glDeleteSamplersPROC) (GLsizei count, const GLuint * samplers);
typedef GLboolean (APIENTRY *glIsSamplerPROC) (GLuint sampler);
typedef void (APIENTRY *glBindSamplerPROC) (GLenum unit, GLuint sampler);
typedef void (APIENTRY *glSamplerParameteriPROC) (GLuint sampler, GLenum pname, GLint param);
typedef void (APIENTRY *glSamplerParameterfPROC) (GLuint sampler, GLenum pname, GLfloat param);
typedef void (APIENTRY *glSamplerParameterivPROC) (GLuint sampler, GLenum pname, const GLint * params);
typedef void (APIENTRY *glSamplerParameterfvPROC) (GLuint sampler, GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glSamplerParameterIivPROC) (GLuint sampler, GLenum pname, const GLint * params);
typedef void (APIENTRY *glSamplerParameterIuivPROC) (GLuint sampler, GLenum pname, const GLuint * params);
typedef void (APIENTRY *glGetSamplerParameterivPROC) (GLuint sampler, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetSamplerParameterfvPROC) (GLuint sampler, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetSamplerParameterIivPROC) (GLuint sampler, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetSamplerParameterIuivPROC) (GLuint sampler, GLenum pname, GLint * params);
typedef void (APIENTRY *glQueryCounterPROC) (GLuint id, GLenum target);
typedef void (APIENTRY *glGetQueryObjecti64vPROC) (GLuint id, GLenum pname, GLint64 * params);
typedef void (APIENTRY *glGetQueryObjectui64vPROC) (GLuint id, GLenum pname, GLuint64 * params);
typedef void (APIENTRY *glVertexAttribDivisorPROC) (GLuint index, GLuint divisor);
typedef void (APIENTRY *glVertexP2uiPROC) (GLenum type, GLuint value);
typedef void (APIENTRY *glVertexP3uiPROC) (GLenum type, GLuint value);
typedef void (APIENTRY *glVertexP4uiPROC) (GLenum type, GLuint value);
typedef void (APIENTRY *glVertexP2uivPROC) (GLenum type, const GLuint * value);
typedef void (APIENTRY *glVertexP3uivPROC) (GLenum type, const GLuint * value);
typedef void (APIENTRY *glVertexP4uivPROC) (GLenum type, const GLuint * value);
typedef void (APIENTRY *glTexCoordP1uiPROC) (GLenum type, GLuint coords);
typedef void (APIENTRY *glTexCoordP2uiPROC) (GLenum type, GLuint coords);
typedef void (APIENTRY *glTexCoordP3uiPROC) (GLenum type, GLuint coords);
typedef void (APIENTRY *glTexCoordP4uiPROC) (GLenum type, GLuint coords);
typedef void (APIENTRY *glTexCoordP1uivPROC) (GLenum type, const GLuint * coords);
typedef void (APIENTRY *glTexCoordP2uivPROC) (GLenum type, const GLuint * coords);
typedef void (APIENTRY *glTexCoordP3uivPROC) (GLenum type, const GLuint * coords);
typedef void (APIENTRY *glTexCoordP4uivPROC) (GLenum type, const GLuint * coords);
typedef void (APIENTRY *glMultiTexCoordP1uiPROC) (GLenum texture, GLenum type, GLuint coords);
typedef void (APIENTRY *glMultiTexCoordP2uiPROC) (GLenum texture, GLenum type, GLuint coords);
typedef void (APIENTRY *glMultiTexCoordP3uiPROC) (GLenum texture, GLenum type, GLuint coords);
typedef void (APIENTRY *glMultiTexCoordP4uiPROC) (GLenum texture, GLenum type, GLuint coords);
typedef void (APIENTRY *glMultiTexCoordP1uivPROC) (GLenum texture, GLenum type, const GLuint * coords);
typedef void (APIENTRY *glMultiTexCoordP2uivPROC) (GLenum texture, GLenum type, const GLuint * coords);
typedef void (APIENTRY *glMultiTexCoordP3uivPROC) (GLenum texture, GLenum type, const GLuint * coords);
typedef void (APIENTRY *glMultiTexCoordP4uivPROC) (GLenum texture, GLenum type, const GLuint * coords);
typedef void (APIENTRY *glNormalP3uiPROC) (GLenum type, GLuint coords);
typedef void (APIENTRY *glNormalP3uivPROC) (GLenum type, const GLuint * coords);
typedef void (APIENTRY *glColorP3uiPROC) (GLenum type, GLuint color);
typedef void (APIENTRY *glColorP4uiPROC) (GLenum type, GLuint color);
typedef void (APIENTRY *glColorP3uivPROC) (GLenum type, const GLuint * color);
typedef void (APIENTRY *glColorP4uivPROC) (GLenum type, const GLuint * color);
typedef void (APIENTRY *glSecondaryColorP3uiPROC) (GLenum type, GLuint color);
typedef void (APIENTRY *glSecondaryColorP3uivPROC) (GLenum type, const GLuint * color);
typedef void (APIENTRY *glVertexAttribP1uiPROC) (GLuint index, GLenum type, GLboolean normalized, GLuint value);
typedef void (APIENTRY *glVertexAttribP2uiPROC) (GLuint index, GLenum type, GLboolean normalized, GLuint value);
typedef void (APIENTRY *glVertexAttribP3uiPROC) (GLuint index, GLenum type, GLboolean normalized, GLuint value);
typedef void (APIENTRY *glVertexAttribP4uiPROC) (GLuint index, GLenum type, GLboolean normalized, GLuint value);
typedef void (APIENTRY *glVertexAttribP1uivPROC) (GLuint index, GLenum type, GLboolean normalized, const GLuint * value);
typedef void (APIENTRY *glVertexAttribP2uivPROC) (GLuint index, GLenum type, GLboolean normalized, const GLuint * value);
typedef void (APIENTRY *glVertexAttribP3uivPROC) (GLuint index, GLenum type, GLboolean normalized, const GLuint * value);
typedef void (APIENTRY *glVertexAttribP4uivPROC) (GLuint index, GLenum type, GLboolean normalized, const GLuint * value);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglBindFragDataLocationIndexed(JNIEnv *env, jclass clazz, jint program, jint colorNumber, jint index, jlong name, jlong function_pointer) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	glBindFragDataLocationIndexedPROC glBindFragDataLocationIndexed = (glBindFragDataLocationIndexedPROC)((intptr_t)function_pointer);
	glBindFragDataLocationIndexed(program, colorNumber, index, name_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL33_nglGetFragDataIndex(JNIEnv *env, jclass clazz, jint program, jlong name, jlong function_pointer) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	glGetFragDataIndexPROC glGetFragDataIndex = (glGetFragDataIndexPROC)((intptr_t)function_pointer);
	GLint __result = glGetFragDataIndex(program, name_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglGenSamplers(JNIEnv *env, jclass clazz, jint count, jlong samplers, jlong function_pointer) {
	GLuint *samplers_address = (GLuint *)(intptr_t)samplers;
	glGenSamplersPROC glGenSamplers = (glGenSamplersPROC)((intptr_t)function_pointer);
	glGenSamplers(count, samplers_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglDeleteSamplers(JNIEnv *env, jclass clazz, jint count, jlong samplers, jlong function_pointer) {
	const GLuint *samplers_address = (const GLuint *)(intptr_t)samplers;
	glDeleteSamplersPROC glDeleteSamplers = (glDeleteSamplersPROC)((intptr_t)function_pointer);
	glDeleteSamplers(count, samplers_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL33_nglIsSampler(JNIEnv *env, jclass clazz, jint sampler, jlong function_pointer) {
	glIsSamplerPROC glIsSampler = (glIsSamplerPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsSampler(sampler);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglBindSampler(JNIEnv *env, jclass clazz, jint unit, jint sampler, jlong function_pointer) {
	glBindSamplerPROC glBindSampler = (glBindSamplerPROC)((intptr_t)function_pointer);
	glBindSampler(unit, sampler);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglSamplerParameteri(JNIEnv *env, jclass clazz, jint sampler, jint pname, jint param, jlong function_pointer) {
	glSamplerParameteriPROC glSamplerParameteri = (glSamplerParameteriPROC)((intptr_t)function_pointer);
	glSamplerParameteri(sampler, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglSamplerParameterf(JNIEnv *env, jclass clazz, jint sampler, jint pname, jfloat param, jlong function_pointer) {
	glSamplerParameterfPROC glSamplerParameterf = (glSamplerParameterfPROC)((intptr_t)function_pointer);
	glSamplerParameterf(sampler, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglSamplerParameteriv(JNIEnv *env, jclass clazz, jint sampler, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glSamplerParameterivPROC glSamplerParameteriv = (glSamplerParameterivPROC)((intptr_t)function_pointer);
	glSamplerParameteriv(sampler, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglSamplerParameterfv(JNIEnv *env, jclass clazz, jint sampler, jint pname, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glSamplerParameterfvPROC glSamplerParameterfv = (glSamplerParameterfvPROC)((intptr_t)function_pointer);
	glSamplerParameterfv(sampler, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglSamplerParameterIiv(JNIEnv *env, jclass clazz, jint sampler, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glSamplerParameterIivPROC glSamplerParameterIiv = (glSamplerParameterIivPROC)((intptr_t)function_pointer);
	glSamplerParameterIiv(sampler, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglSamplerParameterIuiv(JNIEnv *env, jclass clazz, jint sampler, jint pname, jlong params, jlong function_pointer) {
	const GLuint *params_address = (const GLuint *)(intptr_t)params;
	glSamplerParameterIuivPROC glSamplerParameterIuiv = (glSamplerParameterIuivPROC)((intptr_t)function_pointer);
	glSamplerParameterIuiv(sampler, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglGetSamplerParameteriv(JNIEnv *env, jclass clazz, jint sampler, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetSamplerParameterivPROC glGetSamplerParameteriv = (glGetSamplerParameterivPROC)((intptr_t)function_pointer);
	glGetSamplerParameteriv(sampler, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglGetSamplerParameterfv(JNIEnv *env, jclass clazz, jint sampler, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetSamplerParameterfvPROC glGetSamplerParameterfv = (glGetSamplerParameterfvPROC)((intptr_t)function_pointer);
	glGetSamplerParameterfv(sampler, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglGetSamplerParameterIiv(JNIEnv *env, jclass clazz, jint sampler, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetSamplerParameterIivPROC glGetSamplerParameterIiv = (glGetSamplerParameterIivPROC)((intptr_t)function_pointer);
	glGetSamplerParameterIiv(sampler, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglGetSamplerParameterIuiv(JNIEnv *env, jclass clazz, jint sampler, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetSamplerParameterIuivPROC glGetSamplerParameterIuiv = (glGetSamplerParameterIuivPROC)((intptr_t)function_pointer);
	glGetSamplerParameterIuiv(sampler, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglQueryCounter(JNIEnv *env, jclass clazz, jint id, jint target, jlong function_pointer) {
	glQueryCounterPROC glQueryCounter = (glQueryCounterPROC)((intptr_t)function_pointer);
	glQueryCounter(id, target);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglGetQueryObjecti64v(JNIEnv *env, jclass clazz, jint id, jint pname, jlong params, jlong function_pointer) {
	GLint64 *params_address = (GLint64 *)(intptr_t)params;
	glGetQueryObjecti64vPROC glGetQueryObjecti64v = (glGetQueryObjecti64vPROC)((intptr_t)function_pointer);
	glGetQueryObjecti64v(id, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglGetQueryObjectui64v(JNIEnv *env, jclass clazz, jint id, jint pname, jlong params, jlong function_pointer) {
	GLuint64 *params_address = (GLuint64 *)(intptr_t)params;
	glGetQueryObjectui64vPROC glGetQueryObjectui64v = (glGetQueryObjectui64vPROC)((intptr_t)function_pointer);
	glGetQueryObjectui64v(id, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexAttribDivisor(JNIEnv *env, jclass clazz, jint index, jint divisor, jlong function_pointer) {
	glVertexAttribDivisorPROC glVertexAttribDivisor = (glVertexAttribDivisorPROC)((intptr_t)function_pointer);
	glVertexAttribDivisor(index, divisor);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexP2ui(JNIEnv *env, jclass clazz, jint type, jint value, jlong function_pointer) {
	glVertexP2uiPROC glVertexP2ui = (glVertexP2uiPROC)((intptr_t)function_pointer);
	glVertexP2ui(type, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexP3ui(JNIEnv *env, jclass clazz, jint type, jint value, jlong function_pointer) {
	glVertexP3uiPROC glVertexP3ui = (glVertexP3uiPROC)((intptr_t)function_pointer);
	glVertexP3ui(type, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexP4ui(JNIEnv *env, jclass clazz, jint type, jint value, jlong function_pointer) {
	glVertexP4uiPROC glVertexP4ui = (glVertexP4uiPROC)((intptr_t)function_pointer);
	glVertexP4ui(type, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexP2uiv(JNIEnv *env, jclass clazz, jint type, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glVertexP2uivPROC glVertexP2uiv = (glVertexP2uivPROC)((intptr_t)function_pointer);
	glVertexP2uiv(type, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexP3uiv(JNIEnv *env, jclass clazz, jint type, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glVertexP3uivPROC glVertexP3uiv = (glVertexP3uivPROC)((intptr_t)function_pointer);
	glVertexP3uiv(type, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexP4uiv(JNIEnv *env, jclass clazz, jint type, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glVertexP4uivPROC glVertexP4uiv = (glVertexP4uivPROC)((intptr_t)function_pointer);
	glVertexP4uiv(type, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglTexCoordP1ui(JNIEnv *env, jclass clazz, jint type, jint coords, jlong function_pointer) {
	glTexCoordP1uiPROC glTexCoordP1ui = (glTexCoordP1uiPROC)((intptr_t)function_pointer);
	glTexCoordP1ui(type, coords);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglTexCoordP2ui(JNIEnv *env, jclass clazz, jint type, jint coords, jlong function_pointer) {
	glTexCoordP2uiPROC glTexCoordP2ui = (glTexCoordP2uiPROC)((intptr_t)function_pointer);
	glTexCoordP2ui(type, coords);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglTexCoordP3ui(JNIEnv *env, jclass clazz, jint type, jint coords, jlong function_pointer) {
	glTexCoordP3uiPROC glTexCoordP3ui = (glTexCoordP3uiPROC)((intptr_t)function_pointer);
	glTexCoordP3ui(type, coords);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglTexCoordP4ui(JNIEnv *env, jclass clazz, jint type, jint coords, jlong function_pointer) {
	glTexCoordP4uiPROC glTexCoordP4ui = (glTexCoordP4uiPROC)((intptr_t)function_pointer);
	glTexCoordP4ui(type, coords);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglTexCoordP1uiv(JNIEnv *env, jclass clazz, jint type, jlong coords, jlong function_pointer) {
	const GLuint *coords_address = (const GLuint *)(intptr_t)coords;
	glTexCoordP1uivPROC glTexCoordP1uiv = (glTexCoordP1uivPROC)((intptr_t)function_pointer);
	glTexCoordP1uiv(type, coords_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglTexCoordP2uiv(JNIEnv *env, jclass clazz, jint type, jlong coords, jlong function_pointer) {
	const GLuint *coords_address = (const GLuint *)(intptr_t)coords;
	glTexCoordP2uivPROC glTexCoordP2uiv = (glTexCoordP2uivPROC)((intptr_t)function_pointer);
	glTexCoordP2uiv(type, coords_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglTexCoordP3uiv(JNIEnv *env, jclass clazz, jint type, jlong coords, jlong function_pointer) {
	const GLuint *coords_address = (const GLuint *)(intptr_t)coords;
	glTexCoordP3uivPROC glTexCoordP3uiv = (glTexCoordP3uivPROC)((intptr_t)function_pointer);
	glTexCoordP3uiv(type, coords_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglTexCoordP4uiv(JNIEnv *env, jclass clazz, jint type, jlong coords, jlong function_pointer) {
	const GLuint *coords_address = (const GLuint *)(intptr_t)coords;
	glTexCoordP4uivPROC glTexCoordP4uiv = (glTexCoordP4uivPROC)((intptr_t)function_pointer);
	glTexCoordP4uiv(type, coords_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglMultiTexCoordP1ui(JNIEnv *env, jclass clazz, jint texture, jint type, jint coords, jlong function_pointer) {
	glMultiTexCoordP1uiPROC glMultiTexCoordP1ui = (glMultiTexCoordP1uiPROC)((intptr_t)function_pointer);
	glMultiTexCoordP1ui(texture, type, coords);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglMultiTexCoordP2ui(JNIEnv *env, jclass clazz, jint texture, jint type, jint coords, jlong function_pointer) {
	glMultiTexCoordP2uiPROC glMultiTexCoordP2ui = (glMultiTexCoordP2uiPROC)((intptr_t)function_pointer);
	glMultiTexCoordP2ui(texture, type, coords);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglMultiTexCoordP3ui(JNIEnv *env, jclass clazz, jint texture, jint type, jint coords, jlong function_pointer) {
	glMultiTexCoordP3uiPROC glMultiTexCoordP3ui = (glMultiTexCoordP3uiPROC)((intptr_t)function_pointer);
	glMultiTexCoordP3ui(texture, type, coords);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglMultiTexCoordP4ui(JNIEnv *env, jclass clazz, jint texture, jint type, jint coords, jlong function_pointer) {
	glMultiTexCoordP4uiPROC glMultiTexCoordP4ui = (glMultiTexCoordP4uiPROC)((intptr_t)function_pointer);
	glMultiTexCoordP4ui(texture, type, coords);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglMultiTexCoordP1uiv(JNIEnv *env, jclass clazz, jint texture, jint type, jlong coords, jlong function_pointer) {
	const GLuint *coords_address = (const GLuint *)(intptr_t)coords;
	glMultiTexCoordP1uivPROC glMultiTexCoordP1uiv = (glMultiTexCoordP1uivPROC)((intptr_t)function_pointer);
	glMultiTexCoordP1uiv(texture, type, coords_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglMultiTexCoordP2uiv(JNIEnv *env, jclass clazz, jint texture, jint type, jlong coords, jlong function_pointer) {
	const GLuint *coords_address = (const GLuint *)(intptr_t)coords;
	glMultiTexCoordP2uivPROC glMultiTexCoordP2uiv = (glMultiTexCoordP2uivPROC)((intptr_t)function_pointer);
	glMultiTexCoordP2uiv(texture, type, coords_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglMultiTexCoordP3uiv(JNIEnv *env, jclass clazz, jint texture, jint type, jlong coords, jlong function_pointer) {
	const GLuint *coords_address = (const GLuint *)(intptr_t)coords;
	glMultiTexCoordP3uivPROC glMultiTexCoordP3uiv = (glMultiTexCoordP3uivPROC)((intptr_t)function_pointer);
	glMultiTexCoordP3uiv(texture, type, coords_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglMultiTexCoordP4uiv(JNIEnv *env, jclass clazz, jint texture, jint type, jlong coords, jlong function_pointer) {
	const GLuint *coords_address = (const GLuint *)(intptr_t)coords;
	glMultiTexCoordP4uivPROC glMultiTexCoordP4uiv = (glMultiTexCoordP4uivPROC)((intptr_t)function_pointer);
	glMultiTexCoordP4uiv(texture, type, coords_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglNormalP3ui(JNIEnv *env, jclass clazz, jint type, jint coords, jlong function_pointer) {
	glNormalP3uiPROC glNormalP3ui = (glNormalP3uiPROC)((intptr_t)function_pointer);
	glNormalP3ui(type, coords);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglNormalP3uiv(JNIEnv *env, jclass clazz, jint type, jlong coords, jlong function_pointer) {
	const GLuint *coords_address = (const GLuint *)(intptr_t)coords;
	glNormalP3uivPROC glNormalP3uiv = (glNormalP3uivPROC)((intptr_t)function_pointer);
	glNormalP3uiv(type, coords_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglColorP3ui(JNIEnv *env, jclass clazz, jint type, jint color, jlong function_pointer) {
	glColorP3uiPROC glColorP3ui = (glColorP3uiPROC)((intptr_t)function_pointer);
	glColorP3ui(type, color);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglColorP4ui(JNIEnv *env, jclass clazz, jint type, jint color, jlong function_pointer) {
	glColorP4uiPROC glColorP4ui = (glColorP4uiPROC)((intptr_t)function_pointer);
	glColorP4ui(type, color);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglColorP3uiv(JNIEnv *env, jclass clazz, jint type, jlong color, jlong function_pointer) {
	const GLuint *color_address = (const GLuint *)(intptr_t)color;
	glColorP3uivPROC glColorP3uiv = (glColorP3uivPROC)((intptr_t)function_pointer);
	glColorP3uiv(type, color_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglColorP4uiv(JNIEnv *env, jclass clazz, jint type, jlong color, jlong function_pointer) {
	const GLuint *color_address = (const GLuint *)(intptr_t)color;
	glColorP4uivPROC glColorP4uiv = (glColorP4uivPROC)((intptr_t)function_pointer);
	glColorP4uiv(type, color_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglSecondaryColorP3ui(JNIEnv *env, jclass clazz, jint type, jint color, jlong function_pointer) {
	glSecondaryColorP3uiPROC glSecondaryColorP3ui = (glSecondaryColorP3uiPROC)((intptr_t)function_pointer);
	glSecondaryColorP3ui(type, color);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglSecondaryColorP3uiv(JNIEnv *env, jclass clazz, jint type, jlong color, jlong function_pointer) {
	const GLuint *color_address = (const GLuint *)(intptr_t)color;
	glSecondaryColorP3uivPROC glSecondaryColorP3uiv = (glSecondaryColorP3uivPROC)((intptr_t)function_pointer);
	glSecondaryColorP3uiv(type, color_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexAttribP1ui(JNIEnv *env, jclass clazz, jint index, jint type, jboolean normalized, jint value, jlong function_pointer) {
	glVertexAttribP1uiPROC glVertexAttribP1ui = (glVertexAttribP1uiPROC)((intptr_t)function_pointer);
	glVertexAttribP1ui(index, type, normalized, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexAttribP2ui(JNIEnv *env, jclass clazz, jint index, jint type, jboolean normalized, jint value, jlong function_pointer) {
	glVertexAttribP2uiPROC glVertexAttribP2ui = (glVertexAttribP2uiPROC)((intptr_t)function_pointer);
	glVertexAttribP2ui(index, type, normalized, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexAttribP3ui(JNIEnv *env, jclass clazz, jint index, jint type, jboolean normalized, jint value, jlong function_pointer) {
	glVertexAttribP3uiPROC glVertexAttribP3ui = (glVertexAttribP3uiPROC)((intptr_t)function_pointer);
	glVertexAttribP3ui(index, type, normalized, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexAttribP4ui(JNIEnv *env, jclass clazz, jint index, jint type, jboolean normalized, jint value, jlong function_pointer) {
	glVertexAttribP4uiPROC glVertexAttribP4ui = (glVertexAttribP4uiPROC)((intptr_t)function_pointer);
	glVertexAttribP4ui(index, type, normalized, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexAttribP1uiv(JNIEnv *env, jclass clazz, jint index, jint type, jboolean normalized, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glVertexAttribP1uivPROC glVertexAttribP1uiv = (glVertexAttribP1uivPROC)((intptr_t)function_pointer);
	glVertexAttribP1uiv(index, type, normalized, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexAttribP2uiv(JNIEnv *env, jclass clazz, jint index, jint type, jboolean normalized, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glVertexAttribP2uivPROC glVertexAttribP2uiv = (glVertexAttribP2uivPROC)((intptr_t)function_pointer);
	glVertexAttribP2uiv(index, type, normalized, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexAttribP3uiv(JNIEnv *env, jclass clazz, jint index, jint type, jboolean normalized, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glVertexAttribP3uivPROC glVertexAttribP3uiv = (glVertexAttribP3uivPROC)((intptr_t)function_pointer);
	glVertexAttribP3uiv(index, type, normalized, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL33_nglVertexAttribP4uiv(JNIEnv *env, jclass clazz, jint index, jint type, jboolean normalized, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glVertexAttribP4uivPROC glVertexAttribP4uiv = (glVertexAttribP4uivPROC)((intptr_t)function_pointer);
	glVertexAttribP4uiv(index, type, normalized, value_address);
}

