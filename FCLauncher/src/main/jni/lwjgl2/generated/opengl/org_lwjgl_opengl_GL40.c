/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBlendEquationiPROC) (GLuint buf, GLenum mode);
typedef void (APIENTRY *glBlendEquationSeparateiPROC) (GLuint buf, GLenum modeRGB, GLenum modeAlpha);
typedef void (APIENTRY *glBlendFunciPROC) (GLuint buf, GLenum src, GLenum dst);
typedef void (APIENTRY *glBlendFuncSeparateiPROC) (GLuint buf, GLenum srcRGB, GLenum dstRGB, GLenum srcAlpha, GLenum dstAlpha);
typedef void (APIENTRY *glDrawArraysIndirectPROC) (GLenum mode, const GLvoid * indirect);
typedef void (APIENTRY *glDrawElementsIndirectPROC) (GLenum mode, GLenum type, const GLvoid * indirect);
typedef void (APIENTRY *glUniform1dPROC) (GLint location, GLdouble x);
typedef void (APIENTRY *glUniform2dPROC) (GLint location, GLdouble x, GLdouble y);
typedef void (APIENTRY *glUniform3dPROC) (GLint location, GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glUniform4dPROC) (GLint location, GLdouble x, GLdouble y, GLdouble z, GLdouble w);
typedef void (APIENTRY *glUniform1dvPROC) (GLint location, GLsizei count, const GLdouble * value);
typedef void (APIENTRY *glUniform2dvPROC) (GLint location, GLsizei count, const GLdouble * value);
typedef void (APIENTRY *glUniform3dvPROC) (GLint location, GLsizei count, const GLdouble * value);
typedef void (APIENTRY *glUniform4dvPROC) (GLint location, GLsizei count, const GLdouble * value);
typedef void (APIENTRY *glUniformMatrix2dvPROC) (GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glUniformMatrix3dvPROC) (GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glUniformMatrix4dvPROC) (GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glUniformMatrix2x3dvPROC) (GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glUniformMatrix2x4dvPROC) (GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glUniformMatrix3x2dvPROC) (GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glUniformMatrix3x4dvPROC) (GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glUniformMatrix4x2dvPROC) (GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glUniformMatrix4x3dvPROC) (GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glGetUniformdvPROC) (GLuint program, GLint location, GLdouble * params);
typedef void (APIENTRY *glMinSampleShadingPROC) (GLfloat value);
typedef GLint (APIENTRY *glGetSubroutineUniformLocationPROC) (GLuint program, GLenum shadertype, const GLbyte * name);
typedef GLuint (APIENTRY *glGetSubroutineIndexPROC) (GLuint program, GLenum shadertype, const GLbyte * name);
typedef void (APIENTRY *glGetActiveSubroutineUniformivPROC) (GLuint program, GLenum shadertype, GLuint index, GLenum pname, GLint * values);
typedef void (APIENTRY *glGetActiveSubroutineUniformNamePROC) (GLuint program, GLenum shadertype, GLuint index, GLsizei bufsize, GLsizei * length, GLchar * name);
typedef void (APIENTRY *glGetActiveSubroutineNamePROC) (GLuint program, GLenum shadertype, GLuint index, GLsizei bufsize, GLsizei * length, GLchar * name);
typedef void (APIENTRY *glUniformSubroutinesuivPROC) (GLenum shadertype, GLsizei count, const GLuint * indices);
typedef void (APIENTRY *glGetUniformSubroutineuivPROC) (GLenum shadertype, GLint location, GLuint * params);
typedef void (APIENTRY *glGetProgramStageivPROC) (GLuint program, GLenum shadertype, GLenum pname, GLint * values);
typedef void (APIENTRY *glPatchParameteriPROC) (GLenum pname, GLint value);
typedef void (APIENTRY *glPatchParameterfvPROC) (GLenum pname, const GLfloat * values);
typedef void (APIENTRY *glBindTransformFeedbackPROC) (GLenum target, GLuint id);
typedef void (APIENTRY *glDeleteTransformFeedbacksPROC) (GLsizei n, const GLuint * ids);
typedef void (APIENTRY *glGenTransformFeedbacksPROC) (GLsizei n, GLuint * ids);
typedef GLboolean (APIENTRY *glIsTransformFeedbackPROC) (GLuint id);
typedef void (APIENTRY *glPauseTransformFeedbackPROC) ();
typedef void (APIENTRY *glResumeTransformFeedbackPROC) ();
typedef void (APIENTRY *glDrawTransformFeedbackPROC) (GLenum mode, GLuint id);
typedef void (APIENTRY *glDrawTransformFeedbackStreamPROC) (GLenum mode, GLuint id, GLuint stream);
typedef void (APIENTRY *glBeginQueryIndexedPROC) (GLenum target, GLuint index, GLuint id);
typedef void (APIENTRY *glEndQueryIndexedPROC) (GLenum target, GLuint index);
typedef void (APIENTRY *glGetQueryIndexedivPROC) (GLenum target, GLuint index, GLenum pname, GLint * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglBlendEquationi(JNIEnv *env, jclass clazz, jint buf, jint mode, jlong function_pointer) {
	glBlendEquationiPROC glBlendEquationi = (glBlendEquationiPROC)((intptr_t)function_pointer);
	glBlendEquationi(buf, mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglBlendEquationSeparatei(JNIEnv *env, jclass clazz, jint buf, jint modeRGB, jint modeAlpha, jlong function_pointer) {
	glBlendEquationSeparateiPROC glBlendEquationSeparatei = (glBlendEquationSeparateiPROC)((intptr_t)function_pointer);
	glBlendEquationSeparatei(buf, modeRGB, modeAlpha);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglBlendFunci(JNIEnv *env, jclass clazz, jint buf, jint src, jint dst, jlong function_pointer) {
	glBlendFunciPROC glBlendFunci = (glBlendFunciPROC)((intptr_t)function_pointer);
	glBlendFunci(buf, src, dst);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglBlendFuncSeparatei(JNIEnv *env, jclass clazz, jint buf, jint srcRGB, jint dstRGB, jint srcAlpha, jint dstAlpha, jlong function_pointer) {
	glBlendFuncSeparateiPROC glBlendFuncSeparatei = (glBlendFuncSeparateiPROC)((intptr_t)function_pointer);
	glBlendFuncSeparatei(buf, srcRGB, dstRGB, srcAlpha, dstAlpha);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglDrawArraysIndirect(JNIEnv *env, jclass clazz, jint mode, jlong indirect, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)indirect;
	glDrawArraysIndirectPROC glDrawArraysIndirect = (glDrawArraysIndirectPROC)((intptr_t)function_pointer);
	glDrawArraysIndirect(mode, indirect_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglDrawArraysIndirectBO(JNIEnv *env, jclass clazz, jint mode, jlong indirect_buffer_offset, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)offsetToPointer(indirect_buffer_offset);
	glDrawArraysIndirectPROC glDrawArraysIndirect = (glDrawArraysIndirectPROC)((intptr_t)function_pointer);
	glDrawArraysIndirect(mode, indirect_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglDrawElementsIndirect(JNIEnv *env, jclass clazz, jint mode, jint type, jlong indirect, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)indirect;
	glDrawElementsIndirectPROC glDrawElementsIndirect = (glDrawElementsIndirectPROC)((intptr_t)function_pointer);
	glDrawElementsIndirect(mode, type, indirect_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglDrawElementsIndirectBO(JNIEnv *env, jclass clazz, jint mode, jint type, jlong indirect_buffer_offset, jlong function_pointer) {
	const GLvoid *indirect_address = (const GLvoid *)(intptr_t)offsetToPointer(indirect_buffer_offset);
	glDrawElementsIndirectPROC glDrawElementsIndirect = (glDrawElementsIndirectPROC)((intptr_t)function_pointer);
	glDrawElementsIndirect(mode, type, indirect_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniform1d(JNIEnv *env, jclass clazz, jint location, jdouble x, jlong function_pointer) {
	glUniform1dPROC glUniform1d = (glUniform1dPROC)((intptr_t)function_pointer);
	glUniform1d(location, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniform2d(JNIEnv *env, jclass clazz, jint location, jdouble x, jdouble y, jlong function_pointer) {
	glUniform2dPROC glUniform2d = (glUniform2dPROC)((intptr_t)function_pointer);
	glUniform2d(location, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniform3d(JNIEnv *env, jclass clazz, jint location, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glUniform3dPROC glUniform3d = (glUniform3dPROC)((intptr_t)function_pointer);
	glUniform3d(location, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniform4d(JNIEnv *env, jclass clazz, jint location, jdouble x, jdouble y, jdouble z, jdouble w, jlong function_pointer) {
	glUniform4dPROC glUniform4d = (glUniform4dPROC)((intptr_t)function_pointer);
	glUniform4d(location, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniform1dv(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glUniform1dvPROC glUniform1dv = (glUniform1dvPROC)((intptr_t)function_pointer);
	glUniform1dv(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniform2dv(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glUniform2dvPROC glUniform2dv = (glUniform2dvPROC)((intptr_t)function_pointer);
	glUniform2dv(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniform3dv(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glUniform3dvPROC glUniform3dv = (glUniform3dvPROC)((intptr_t)function_pointer);
	glUniform3dv(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniform4dv(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glUniform4dvPROC glUniform4dv = (glUniform4dvPROC)((intptr_t)function_pointer);
	glUniform4dv(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniformMatrix2dv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glUniformMatrix2dvPROC glUniformMatrix2dv = (glUniformMatrix2dvPROC)((intptr_t)function_pointer);
	glUniformMatrix2dv(location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniformMatrix3dv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glUniformMatrix3dvPROC glUniformMatrix3dv = (glUniformMatrix3dvPROC)((intptr_t)function_pointer);
	glUniformMatrix3dv(location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniformMatrix4dv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glUniformMatrix4dvPROC glUniformMatrix4dv = (glUniformMatrix4dvPROC)((intptr_t)function_pointer);
	glUniformMatrix4dv(location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniformMatrix2x3dv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glUniformMatrix2x3dvPROC glUniformMatrix2x3dv = (glUniformMatrix2x3dvPROC)((intptr_t)function_pointer);
	glUniformMatrix2x3dv(location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniformMatrix2x4dv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glUniformMatrix2x4dvPROC glUniformMatrix2x4dv = (glUniformMatrix2x4dvPROC)((intptr_t)function_pointer);
	glUniformMatrix2x4dv(location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniformMatrix3x2dv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glUniformMatrix3x2dvPROC glUniformMatrix3x2dv = (glUniformMatrix3x2dvPROC)((intptr_t)function_pointer);
	glUniformMatrix3x2dv(location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniformMatrix3x4dv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glUniformMatrix3x4dvPROC glUniformMatrix3x4dv = (glUniformMatrix3x4dvPROC)((intptr_t)function_pointer);
	glUniformMatrix3x4dv(location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniformMatrix4x2dv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glUniformMatrix4x2dvPROC glUniformMatrix4x2dv = (glUniformMatrix4x2dvPROC)((intptr_t)function_pointer);
	glUniformMatrix4x2dv(location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniformMatrix4x3dv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glUniformMatrix4x3dvPROC glUniformMatrix4x3dv = (glUniformMatrix4x3dvPROC)((intptr_t)function_pointer);
	glUniformMatrix4x3dv(location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglGetUniformdv(JNIEnv *env, jclass clazz, jint program, jint location, jlong params, jlong function_pointer) {
	GLdouble *params_address = (GLdouble *)(intptr_t)params;
	glGetUniformdvPROC glGetUniformdv = (glGetUniformdvPROC)((intptr_t)function_pointer);
	glGetUniformdv(program, location, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglMinSampleShading(JNIEnv *env, jclass clazz, jfloat value, jlong function_pointer) {
	glMinSampleShadingPROC glMinSampleShading = (glMinSampleShadingPROC)((intptr_t)function_pointer);
	glMinSampleShading(value);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL40_nglGetSubroutineUniformLocation(JNIEnv *env, jclass clazz, jint program, jint shadertype, jlong name, jlong function_pointer) {
	const GLbyte *name_address = (const GLbyte *)(intptr_t)name;
	glGetSubroutineUniformLocationPROC glGetSubroutineUniformLocation = (glGetSubroutineUniformLocationPROC)((intptr_t)function_pointer);
	GLint __result = glGetSubroutineUniformLocation(program, shadertype, name_address);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL40_nglGetSubroutineIndex(JNIEnv *env, jclass clazz, jint program, jint shadertype, jlong name, jlong function_pointer) {
	const GLbyte *name_address = (const GLbyte *)(intptr_t)name;
	glGetSubroutineIndexPROC glGetSubroutineIndex = (glGetSubroutineIndexPROC)((intptr_t)function_pointer);
	GLuint __result = glGetSubroutineIndex(program, shadertype, name_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglGetActiveSubroutineUniformiv(JNIEnv *env, jclass clazz, jint program, jint shadertype, jint index, jint pname, jlong values, jlong function_pointer) {
	GLint *values_address = (GLint *)(intptr_t)values;
	glGetActiveSubroutineUniformivPROC glGetActiveSubroutineUniformiv = (glGetActiveSubroutineUniformivPROC)((intptr_t)function_pointer);
	glGetActiveSubroutineUniformiv(program, shadertype, index, pname, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglGetActiveSubroutineUniformName(JNIEnv *env, jclass clazz, jint program, jint shadertype, jint index, jint bufsize, jlong length, jlong name, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *name_address = (GLchar *)(intptr_t)name;
	glGetActiveSubroutineUniformNamePROC glGetActiveSubroutineUniformName = (glGetActiveSubroutineUniformNamePROC)((intptr_t)function_pointer);
	glGetActiveSubroutineUniformName(program, shadertype, index, bufsize, length_address, name_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglGetActiveSubroutineName(JNIEnv *env, jclass clazz, jint program, jint shadertype, jint index, jint bufsize, jlong length, jlong name, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *name_address = (GLchar *)(intptr_t)name;
	glGetActiveSubroutineNamePROC glGetActiveSubroutineName = (glGetActiveSubroutineNamePROC)((intptr_t)function_pointer);
	glGetActiveSubroutineName(program, shadertype, index, bufsize, length_address, name_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglUniformSubroutinesuiv(JNIEnv *env, jclass clazz, jint shadertype, jint count, jlong indices, jlong function_pointer) {
	const GLuint *indices_address = (const GLuint *)(intptr_t)indices;
	glUniformSubroutinesuivPROC glUniformSubroutinesuiv = (glUniformSubroutinesuivPROC)((intptr_t)function_pointer);
	glUniformSubroutinesuiv(shadertype, count, indices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglGetUniformSubroutineuiv(JNIEnv *env, jclass clazz, jint shadertype, jint location, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetUniformSubroutineuivPROC glGetUniformSubroutineuiv = (glGetUniformSubroutineuivPROC)((intptr_t)function_pointer);
	glGetUniformSubroutineuiv(shadertype, location, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglGetProgramStageiv(JNIEnv *env, jclass clazz, jint program, jint shadertype, jint pname, jlong values, jlong function_pointer) {
	GLint *values_address = (GLint *)(intptr_t)values;
	glGetProgramStageivPROC glGetProgramStageiv = (glGetProgramStageivPROC)((intptr_t)function_pointer);
	glGetProgramStageiv(program, shadertype, pname, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglPatchParameteri(JNIEnv *env, jclass clazz, jint pname, jint value, jlong function_pointer) {
	glPatchParameteriPROC glPatchParameteri = (glPatchParameteriPROC)((intptr_t)function_pointer);
	glPatchParameteri(pname, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglPatchParameterfv(JNIEnv *env, jclass clazz, jint pname, jlong values, jlong function_pointer) {
	const GLfloat *values_address = (const GLfloat *)(intptr_t)values;
	glPatchParameterfvPROC glPatchParameterfv = (glPatchParameterfvPROC)((intptr_t)function_pointer);
	glPatchParameterfv(pname, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglBindTransformFeedback(JNIEnv *env, jclass clazz, jint target, jint id, jlong function_pointer) {
	glBindTransformFeedbackPROC glBindTransformFeedback = (glBindTransformFeedbackPROC)((intptr_t)function_pointer);
	glBindTransformFeedback(target, id);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglDeleteTransformFeedbacks(JNIEnv *env, jclass clazz, jint n, jlong ids, jlong function_pointer) {
	const GLuint *ids_address = (const GLuint *)(intptr_t)ids;
	glDeleteTransformFeedbacksPROC glDeleteTransformFeedbacks = (glDeleteTransformFeedbacksPROC)((intptr_t)function_pointer);
	glDeleteTransformFeedbacks(n, ids_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglGenTransformFeedbacks(JNIEnv *env, jclass clazz, jint n, jlong ids, jlong function_pointer) {
	GLuint *ids_address = (GLuint *)(intptr_t)ids;
	glGenTransformFeedbacksPROC glGenTransformFeedbacks = (glGenTransformFeedbacksPROC)((intptr_t)function_pointer);
	glGenTransformFeedbacks(n, ids_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL40_nglIsTransformFeedback(JNIEnv *env, jclass clazz, jint id, jlong function_pointer) {
	glIsTransformFeedbackPROC glIsTransformFeedback = (glIsTransformFeedbackPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsTransformFeedback(id);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglPauseTransformFeedback(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glPauseTransformFeedbackPROC glPauseTransformFeedback = (glPauseTransformFeedbackPROC)((intptr_t)function_pointer);
	glPauseTransformFeedback();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglResumeTransformFeedback(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glResumeTransformFeedbackPROC glResumeTransformFeedback = (glResumeTransformFeedbackPROC)((intptr_t)function_pointer);
	glResumeTransformFeedback();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglDrawTransformFeedback(JNIEnv *env, jclass clazz, jint mode, jint id, jlong function_pointer) {
	glDrawTransformFeedbackPROC glDrawTransformFeedback = (glDrawTransformFeedbackPROC)((intptr_t)function_pointer);
	glDrawTransformFeedback(mode, id);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglDrawTransformFeedbackStream(JNIEnv *env, jclass clazz, jint mode, jint id, jint stream, jlong function_pointer) {
	glDrawTransformFeedbackStreamPROC glDrawTransformFeedbackStream = (glDrawTransformFeedbackStreamPROC)((intptr_t)function_pointer);
	glDrawTransformFeedbackStream(mode, id, stream);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglBeginQueryIndexed(JNIEnv *env, jclass clazz, jint target, jint index, jint id, jlong function_pointer) {
	glBeginQueryIndexedPROC glBeginQueryIndexed = (glBeginQueryIndexedPROC)((intptr_t)function_pointer);
	glBeginQueryIndexed(target, index, id);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglEndQueryIndexed(JNIEnv *env, jclass clazz, jint target, jint index, jlong function_pointer) {
	glEndQueryIndexedPROC glEndQueryIndexed = (glEndQueryIndexedPROC)((intptr_t)function_pointer);
	glEndQueryIndexed(target, index);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL40_nglGetQueryIndexediv(JNIEnv *env, jclass clazz, jint target, jint index, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetQueryIndexedivPROC glGetQueryIndexediv = (glGetQueryIndexedivPROC)((intptr_t)function_pointer);
	glGetQueryIndexediv(target, index, pname, params_address);
}

