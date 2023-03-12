/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBeginVertexShaderEXTPROC) ();
typedef void (APIENTRY *glEndVertexShaderEXTPROC) ();
typedef void (APIENTRY *glBindVertexShaderEXTPROC) (GLuint id);
typedef GLuint (APIENTRY *glGenVertexShadersEXTPROC) (GLuint range);
typedef void (APIENTRY *glDeleteVertexShaderEXTPROC) (GLuint id);
typedef void (APIENTRY *glShaderOp1EXTPROC) (GLenum op, GLuint res, GLuint arg1);
typedef void (APIENTRY *glShaderOp2EXTPROC) (GLenum op, GLuint res, GLuint arg1, GLuint arg2);
typedef void (APIENTRY *glShaderOp3EXTPROC) (GLenum op, GLuint res, GLuint arg1, GLuint arg2, GLuint arg3);
typedef void (APIENTRY *glSwizzleEXTPROC) (GLuint res, GLuint in, GLenum outX, GLenum outY, GLenum outZ, GLenum outW);
typedef void (APIENTRY *glWriteMaskEXTPROC) (GLuint res, GLuint in, GLenum outX, GLenum outY, GLenum outZ, GLenum outW);
typedef void (APIENTRY *glInsertComponentEXTPROC) (GLuint res, GLuint src, GLuint num);
typedef void (APIENTRY *glExtractComponentEXTPROC) (GLuint res, GLuint src, GLuint num);
typedef GLuint (APIENTRY *glGenSymbolsEXTPROC) (GLenum dataType, GLenum storageType, GLenum range, GLuint components);
typedef void (APIENTRY *glSetInvariantEXTPROC) (GLuint id, GLenum type, const GLvoid * pAddr);
typedef void (APIENTRY *glSetLocalConstantEXTPROC) (GLuint id, GLenum type, const GLvoid * pAddr);
typedef void (APIENTRY *glVariantbvEXTPROC) (GLuint id, const GLbyte * pAddr);
typedef void (APIENTRY *glVariantsvEXTPROC) (GLuint id, const GLshort * pAddr);
typedef void (APIENTRY *glVariantivEXTPROC) (GLuint id, const GLint * pAddr);
typedef void (APIENTRY *glVariantfvEXTPROC) (GLuint id, const GLfloat * pAddr);
typedef void (APIENTRY *glVariantdvEXTPROC) (GLuint id, const GLdouble * pAddr);
typedef void (APIENTRY *glVariantubvEXTPROC) (GLuint id, const GLubyte * pAddr);
typedef void (APIENTRY *glVariantusvEXTPROC) (GLuint id, const GLushort * pAddr);
typedef void (APIENTRY *glVariantuivEXTPROC) (GLuint id, const GLuint * pAddr);
typedef void (APIENTRY *glVariantPointerEXTPROC) (GLuint id, GLenum type, GLuint stride, const GLvoid * pAddr);
typedef void (APIENTRY *glEnableVariantClientStateEXTPROC) (GLuint id);
typedef void (APIENTRY *glDisableVariantClientStateEXTPROC) (GLuint id);
typedef GLuint (APIENTRY *glBindLightParameterEXTPROC) (GLenum light, GLenum value);
typedef GLuint (APIENTRY *glBindMaterialParameterEXTPROC) (GLenum face, GLenum value);
typedef GLuint (APIENTRY *glBindTexGenParameterEXTPROC) (GLenum unit, GLenum coord, GLenum value);
typedef GLuint (APIENTRY *glBindTextureUnitParameterEXTPROC) (GLenum unit, GLenum value);
typedef GLuint (APIENTRY *glBindParameterEXTPROC) (GLenum value);
typedef GLboolean (APIENTRY *glIsVariantEnabledEXTPROC) (GLuint id, GLenum cap);
typedef void (APIENTRY *glGetVariantBooleanvEXTPROC) (GLuint id, GLenum value, GLbyte * pbData);
typedef void (APIENTRY *glGetVariantIntegervEXTPROC) (GLuint id, GLenum value, GLint * pbData);
typedef void (APIENTRY *glGetVariantFloatvEXTPROC) (GLuint id, GLenum value, GLfloat * pbData);
typedef void (APIENTRY *glGetVariantPointervEXTPROC) (GLuint id, GLenum value, GLvoid ** pbData);
typedef void (APIENTRY *glGetInvariantBooleanvEXTPROC) (GLuint id, GLenum value, GLbyte * pbData);
typedef void (APIENTRY *glGetInvariantIntegervEXTPROC) (GLuint id, GLenum value, GLint * pbData);
typedef void (APIENTRY *glGetInvariantFloatvEXTPROC) (GLuint id, GLenum value, GLfloat * pbData);
typedef void (APIENTRY *glGetLocalConstantBooleanvEXTPROC) (GLuint id, GLenum value, GLbyte * pbData);
typedef void (APIENTRY *glGetLocalConstantIntegervEXTPROC) (GLuint id, GLenum value, GLint * pbData);
typedef void (APIENTRY *glGetLocalConstantFloatvEXTPROC) (GLuint id, GLenum value, GLfloat * pbData);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglBeginVertexShaderEXT(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glBeginVertexShaderEXTPROC glBeginVertexShaderEXT = (glBeginVertexShaderEXTPROC)((intptr_t)function_pointer);
	glBeginVertexShaderEXT();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglEndVertexShaderEXT(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glEndVertexShaderEXTPROC glEndVertexShaderEXT = (glEndVertexShaderEXTPROC)((intptr_t)function_pointer);
	glEndVertexShaderEXT();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglBindVertexShaderEXT(JNIEnv *env, jclass clazz, jint id, jlong function_pointer) {
	glBindVertexShaderEXTPROC glBindVertexShaderEXT = (glBindVertexShaderEXTPROC)((intptr_t)function_pointer);
	glBindVertexShaderEXT(id);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglGenVertexShadersEXT(JNIEnv *env, jclass clazz, jint range, jlong function_pointer) {
	glGenVertexShadersEXTPROC glGenVertexShadersEXT = (glGenVertexShadersEXTPROC)((intptr_t)function_pointer);
	GLuint __result = glGenVertexShadersEXT(range);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglDeleteVertexShaderEXT(JNIEnv *env, jclass clazz, jint id, jlong function_pointer) {
	glDeleteVertexShaderEXTPROC glDeleteVertexShaderEXT = (glDeleteVertexShaderEXTPROC)((intptr_t)function_pointer);
	glDeleteVertexShaderEXT(id);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglShaderOp1EXT(JNIEnv *env, jclass clazz, jint op, jint res, jint arg1, jlong function_pointer) {
	glShaderOp1EXTPROC glShaderOp1EXT = (glShaderOp1EXTPROC)((intptr_t)function_pointer);
	glShaderOp1EXT(op, res, arg1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglShaderOp2EXT(JNIEnv *env, jclass clazz, jint op, jint res, jint arg1, jint arg2, jlong function_pointer) {
	glShaderOp2EXTPROC glShaderOp2EXT = (glShaderOp2EXTPROC)((intptr_t)function_pointer);
	glShaderOp2EXT(op, res, arg1, arg2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglShaderOp3EXT(JNIEnv *env, jclass clazz, jint op, jint res, jint arg1, jint arg2, jint arg3, jlong function_pointer) {
	glShaderOp3EXTPROC glShaderOp3EXT = (glShaderOp3EXTPROC)((intptr_t)function_pointer);
	glShaderOp3EXT(op, res, arg1, arg2, arg3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglSwizzleEXT(JNIEnv *env, jclass clazz, jint res, jint in, jint outX, jint outY, jint outZ, jint outW, jlong function_pointer) {
	glSwizzleEXTPROC glSwizzleEXT = (glSwizzleEXTPROC)((intptr_t)function_pointer);
	glSwizzleEXT(res, in, outX, outY, outZ, outW);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglWriteMaskEXT(JNIEnv *env, jclass clazz, jint res, jint in, jint outX, jint outY, jint outZ, jint outW, jlong function_pointer) {
	glWriteMaskEXTPROC glWriteMaskEXT = (glWriteMaskEXTPROC)((intptr_t)function_pointer);
	glWriteMaskEXT(res, in, outX, outY, outZ, outW);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglInsertComponentEXT(JNIEnv *env, jclass clazz, jint res, jint src, jint num, jlong function_pointer) {
	glInsertComponentEXTPROC glInsertComponentEXT = (glInsertComponentEXTPROC)((intptr_t)function_pointer);
	glInsertComponentEXT(res, src, num);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglExtractComponentEXT(JNIEnv *env, jclass clazz, jint res, jint src, jint num, jlong function_pointer) {
	glExtractComponentEXTPROC glExtractComponentEXT = (glExtractComponentEXTPROC)((intptr_t)function_pointer);
	glExtractComponentEXT(res, src, num);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglGenSymbolsEXT(JNIEnv *env, jclass clazz, jint dataType, jint storageType, jint range, jint components, jlong function_pointer) {
	glGenSymbolsEXTPROC glGenSymbolsEXT = (glGenSymbolsEXTPROC)((intptr_t)function_pointer);
	GLuint __result = glGenSymbolsEXT(dataType, storageType, range, components);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglSetInvariantEXT(JNIEnv *env, jclass clazz, jint id, jint type, jlong pAddr, jlong function_pointer) {
	const GLvoid *pAddr_address = (const GLvoid *)(intptr_t)pAddr;
	glSetInvariantEXTPROC glSetInvariantEXT = (glSetInvariantEXTPROC)((intptr_t)function_pointer);
	glSetInvariantEXT(id, type, pAddr_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglSetLocalConstantEXT(JNIEnv *env, jclass clazz, jint id, jint type, jlong pAddr, jlong function_pointer) {
	const GLvoid *pAddr_address = (const GLvoid *)(intptr_t)pAddr;
	glSetLocalConstantEXTPROC glSetLocalConstantEXT = (glSetLocalConstantEXTPROC)((intptr_t)function_pointer);
	glSetLocalConstantEXT(id, type, pAddr_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglVariantbvEXT(JNIEnv *env, jclass clazz, jint id, jlong pAddr, jlong function_pointer) {
	const GLbyte *pAddr_address = (const GLbyte *)(intptr_t)pAddr;
	glVariantbvEXTPROC glVariantbvEXT = (glVariantbvEXTPROC)((intptr_t)function_pointer);
	glVariantbvEXT(id, pAddr_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglVariantsvEXT(JNIEnv *env, jclass clazz, jint id, jlong pAddr, jlong function_pointer) {
	const GLshort *pAddr_address = (const GLshort *)(intptr_t)pAddr;
	glVariantsvEXTPROC glVariantsvEXT = (glVariantsvEXTPROC)((intptr_t)function_pointer);
	glVariantsvEXT(id, pAddr_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglVariantivEXT(JNIEnv *env, jclass clazz, jint id, jlong pAddr, jlong function_pointer) {
	const GLint *pAddr_address = (const GLint *)(intptr_t)pAddr;
	glVariantivEXTPROC glVariantivEXT = (glVariantivEXTPROC)((intptr_t)function_pointer);
	glVariantivEXT(id, pAddr_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglVariantfvEXT(JNIEnv *env, jclass clazz, jint id, jlong pAddr, jlong function_pointer) {
	const GLfloat *pAddr_address = (const GLfloat *)(intptr_t)pAddr;
	glVariantfvEXTPROC glVariantfvEXT = (glVariantfvEXTPROC)((intptr_t)function_pointer);
	glVariantfvEXT(id, pAddr_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglVariantdvEXT(JNIEnv *env, jclass clazz, jint id, jlong pAddr, jlong function_pointer) {
	const GLdouble *pAddr_address = (const GLdouble *)(intptr_t)pAddr;
	glVariantdvEXTPROC glVariantdvEXT = (glVariantdvEXTPROC)((intptr_t)function_pointer);
	glVariantdvEXT(id, pAddr_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglVariantubvEXT(JNIEnv *env, jclass clazz, jint id, jlong pAddr, jlong function_pointer) {
	const GLubyte *pAddr_address = (const GLubyte *)(intptr_t)pAddr;
	glVariantubvEXTPROC glVariantubvEXT = (glVariantubvEXTPROC)((intptr_t)function_pointer);
	glVariantubvEXT(id, pAddr_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglVariantusvEXT(JNIEnv *env, jclass clazz, jint id, jlong pAddr, jlong function_pointer) {
	const GLushort *pAddr_address = (const GLushort *)(intptr_t)pAddr;
	glVariantusvEXTPROC glVariantusvEXT = (glVariantusvEXTPROC)((intptr_t)function_pointer);
	glVariantusvEXT(id, pAddr_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglVariantuivEXT(JNIEnv *env, jclass clazz, jint id, jlong pAddr, jlong function_pointer) {
	const GLuint *pAddr_address = (const GLuint *)(intptr_t)pAddr;
	glVariantuivEXTPROC glVariantuivEXT = (glVariantuivEXTPROC)((intptr_t)function_pointer);
	glVariantuivEXT(id, pAddr_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglVariantPointerEXT(JNIEnv *env, jclass clazz, jint id, jint type, jint stride, jlong pAddr, jlong function_pointer) {
	const GLvoid *pAddr_address = (const GLvoid *)(intptr_t)pAddr;
	glVariantPointerEXTPROC glVariantPointerEXT = (glVariantPointerEXTPROC)((intptr_t)function_pointer);
	glVariantPointerEXT(id, type, stride, pAddr_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglVariantPointerEXTBO(JNIEnv *env, jclass clazz, jint id, jint type, jint stride, jlong pAddr_buffer_offset, jlong function_pointer) {
	const GLvoid *pAddr_address = (const GLvoid *)(intptr_t)offsetToPointer(pAddr_buffer_offset);
	glVariantPointerEXTPROC glVariantPointerEXT = (glVariantPointerEXTPROC)((intptr_t)function_pointer);
	glVariantPointerEXT(id, type, stride, pAddr_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglEnableVariantClientStateEXT(JNIEnv *env, jclass clazz, jint id, jlong function_pointer) {
	glEnableVariantClientStateEXTPROC glEnableVariantClientStateEXT = (glEnableVariantClientStateEXTPROC)((intptr_t)function_pointer);
	glEnableVariantClientStateEXT(id);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglDisableVariantClientStateEXT(JNIEnv *env, jclass clazz, jint id, jlong function_pointer) {
	glDisableVariantClientStateEXTPROC glDisableVariantClientStateEXT = (glDisableVariantClientStateEXTPROC)((intptr_t)function_pointer);
	glDisableVariantClientStateEXT(id);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglBindLightParameterEXT(JNIEnv *env, jclass clazz, jint light, jint value, jlong function_pointer) {
	glBindLightParameterEXTPROC glBindLightParameterEXT = (glBindLightParameterEXTPROC)((intptr_t)function_pointer);
	GLuint __result = glBindLightParameterEXT(light, value);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglBindMaterialParameterEXT(JNIEnv *env, jclass clazz, jint face, jint value, jlong function_pointer) {
	glBindMaterialParameterEXTPROC glBindMaterialParameterEXT = (glBindMaterialParameterEXTPROC)((intptr_t)function_pointer);
	GLuint __result = glBindMaterialParameterEXT(face, value);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglBindTexGenParameterEXT(JNIEnv *env, jclass clazz, jint unit, jint coord, jint value, jlong function_pointer) {
	glBindTexGenParameterEXTPROC glBindTexGenParameterEXT = (glBindTexGenParameterEXTPROC)((intptr_t)function_pointer);
	GLuint __result = glBindTexGenParameterEXT(unit, coord, value);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglBindTextureUnitParameterEXT(JNIEnv *env, jclass clazz, jint unit, jint value, jlong function_pointer) {
	glBindTextureUnitParameterEXTPROC glBindTextureUnitParameterEXT = (glBindTextureUnitParameterEXTPROC)((intptr_t)function_pointer);
	GLuint __result = glBindTextureUnitParameterEXT(unit, value);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglBindParameterEXT(JNIEnv *env, jclass clazz, jint value, jlong function_pointer) {
	glBindParameterEXTPROC glBindParameterEXT = (glBindParameterEXTPROC)((intptr_t)function_pointer);
	GLuint __result = glBindParameterEXT(value);
	return __result;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglIsVariantEnabledEXT(JNIEnv *env, jclass clazz, jint id, jint cap, jlong function_pointer) {
	glIsVariantEnabledEXTPROC glIsVariantEnabledEXT = (glIsVariantEnabledEXTPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsVariantEnabledEXT(id, cap);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglGetVariantBooleanvEXT(JNIEnv *env, jclass clazz, jint id, jint value, jlong pbData, jlong function_pointer) {
	GLbyte *pbData_address = (GLbyte *)(intptr_t)pbData;
	glGetVariantBooleanvEXTPROC glGetVariantBooleanvEXT = (glGetVariantBooleanvEXTPROC)((intptr_t)function_pointer);
	glGetVariantBooleanvEXT(id, value, pbData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglGetVariantIntegervEXT(JNIEnv *env, jclass clazz, jint id, jint value, jlong pbData, jlong function_pointer) {
	GLint *pbData_address = (GLint *)(intptr_t)pbData;
	glGetVariantIntegervEXTPROC glGetVariantIntegervEXT = (glGetVariantIntegervEXTPROC)((intptr_t)function_pointer);
	glGetVariantIntegervEXT(id, value, pbData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglGetVariantFloatvEXT(JNIEnv *env, jclass clazz, jint id, jint value, jlong pbData, jlong function_pointer) {
	GLfloat *pbData_address = (GLfloat *)(intptr_t)pbData;
	glGetVariantFloatvEXTPROC glGetVariantFloatvEXT = (glGetVariantFloatvEXTPROC)((intptr_t)function_pointer);
	glGetVariantFloatvEXT(id, value, pbData_address);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglGetVariantPointervEXT(JNIEnv *env, jclass clazz, jint id, jint value, jlong result_size, jlong function_pointer) {
	glGetVariantPointervEXTPROC glGetVariantPointervEXT = (glGetVariantPointervEXTPROC)((intptr_t)function_pointer);
	GLvoid * __result;
	glGetVariantPointervEXT(id, value, &__result);
	return safeNewBuffer(env, __result, result_size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglGetInvariantBooleanvEXT(JNIEnv *env, jclass clazz, jint id, jint value, jlong pbData, jlong function_pointer) {
	GLbyte *pbData_address = (GLbyte *)(intptr_t)pbData;
	glGetInvariantBooleanvEXTPROC glGetInvariantBooleanvEXT = (glGetInvariantBooleanvEXTPROC)((intptr_t)function_pointer);
	glGetInvariantBooleanvEXT(id, value, pbData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglGetInvariantIntegervEXT(JNIEnv *env, jclass clazz, jint id, jint value, jlong pbData, jlong function_pointer) {
	GLint *pbData_address = (GLint *)(intptr_t)pbData;
	glGetInvariantIntegervEXTPROC glGetInvariantIntegervEXT = (glGetInvariantIntegervEXTPROC)((intptr_t)function_pointer);
	glGetInvariantIntegervEXT(id, value, pbData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglGetInvariantFloatvEXT(JNIEnv *env, jclass clazz, jint id, jint value, jlong pbData, jlong function_pointer) {
	GLfloat *pbData_address = (GLfloat *)(intptr_t)pbData;
	glGetInvariantFloatvEXTPROC glGetInvariantFloatvEXT = (glGetInvariantFloatvEXTPROC)((intptr_t)function_pointer);
	glGetInvariantFloatvEXT(id, value, pbData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglGetLocalConstantBooleanvEXT(JNIEnv *env, jclass clazz, jint id, jint value, jlong pbData, jlong function_pointer) {
	GLbyte *pbData_address = (GLbyte *)(intptr_t)pbData;
	glGetLocalConstantBooleanvEXTPROC glGetLocalConstantBooleanvEXT = (glGetLocalConstantBooleanvEXTPROC)((intptr_t)function_pointer);
	glGetLocalConstantBooleanvEXT(id, value, pbData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglGetLocalConstantIntegervEXT(JNIEnv *env, jclass clazz, jint id, jint value, jlong pbData, jlong function_pointer) {
	GLint *pbData_address = (GLint *)(intptr_t)pbData;
	glGetLocalConstantIntegervEXTPROC glGetLocalConstantIntegervEXT = (glGetLocalConstantIntegervEXTPROC)((intptr_t)function_pointer);
	glGetLocalConstantIntegervEXT(id, value, pbData_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexShader_nglGetLocalConstantFloatvEXT(JNIEnv *env, jclass clazz, jint id, jint value, jlong pbData, jlong function_pointer) {
	GLfloat *pbData_address = (GLfloat *)(intptr_t)pbData;
	glGetLocalConstantFloatvEXTPROC glGetLocalConstantFloatvEXT = (glGetLocalConstantFloatvEXTPROC)((intptr_t)function_pointer);
	glGetLocalConstantFloatvEXT(id, value, pbData_address);
}

