/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBlendEquationPROC) (GLenum mode);
typedef void (APIENTRY *glBlendColorPROC) (GLfloat red, GLfloat green, GLfloat blue, GLfloat alpha);
typedef void (APIENTRY *glFogCoordfPROC) (GLfloat coord);
typedef void (APIENTRY *glFogCoorddPROC) (GLdouble coord);
typedef void (APIENTRY *glFogCoordPointerPROC) (GLenum type, GLsizei stride, const GLvoid * data);
typedef void (APIENTRY *glMultiDrawArraysPROC) (GLenum mode, GLint * piFirst, GLsizei * piCount, GLsizei primcount);
typedef void (APIENTRY *glPointParameteriPROC) (GLenum pname, GLint param);
typedef void (APIENTRY *glPointParameterfPROC) (GLenum pname, GLfloat param);
typedef void (APIENTRY *glPointParameterivPROC) (GLenum pname, const GLint * params);
typedef void (APIENTRY *glPointParameterfvPROC) (GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glSecondaryColor3bPROC) (GLbyte red, GLbyte green, GLbyte blue);
typedef void (APIENTRY *glSecondaryColor3fPROC) (GLfloat red, GLfloat green, GLfloat blue);
typedef void (APIENTRY *glSecondaryColor3dPROC) (GLdouble red, GLdouble green, GLdouble blue);
typedef void (APIENTRY *glSecondaryColor3ubPROC) (GLubyte red, GLubyte green, GLubyte blue);
typedef void (APIENTRY *glSecondaryColorPointerPROC) (GLint size, GLenum type, GLsizei stride, const GLvoid * data);
typedef void (APIENTRY *glBlendFuncSeparatePROC) (GLenum sfactorRGB, GLenum dfactorRGB, GLenum sfactorAlpha, GLenum dfactorAlpha);
typedef void (APIENTRY *glWindowPos2fPROC) (GLfloat x, GLfloat y);
typedef void (APIENTRY *glWindowPos2dPROC) (GLdouble x, GLdouble y);
typedef void (APIENTRY *glWindowPos2iPROC) (GLint x, GLint y);
typedef void (APIENTRY *glWindowPos3fPROC) (GLfloat x, GLfloat y, GLfloat z);
typedef void (APIENTRY *glWindowPos3dPROC) (GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glWindowPos3iPROC) (GLint x, GLint y, GLint z);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglBlendEquation(JNIEnv *env, jclass clazz, jint mode, jlong function_pointer) {
	glBlendEquationPROC glBlendEquation = (glBlendEquationPROC)((intptr_t)function_pointer);
	glBlendEquation(mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglBlendColor(JNIEnv *env, jclass clazz, jfloat red, jfloat green, jfloat blue, jfloat alpha, jlong function_pointer) {
	glBlendColorPROC glBlendColor = (glBlendColorPROC)((intptr_t)function_pointer);
	glBlendColor(red, green, blue, alpha);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglFogCoordf(JNIEnv *env, jclass clazz, jfloat coord, jlong function_pointer) {
	glFogCoordfPROC glFogCoordf = (glFogCoordfPROC)((intptr_t)function_pointer);
	glFogCoordf(coord);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglFogCoordd(JNIEnv *env, jclass clazz, jdouble coord, jlong function_pointer) {
	glFogCoorddPROC glFogCoordd = (glFogCoorddPROC)((intptr_t)function_pointer);
	glFogCoordd(coord);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglFogCoordPointer(JNIEnv *env, jclass clazz, jint type, jint stride, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glFogCoordPointerPROC glFogCoordPointer = (glFogCoordPointerPROC)((intptr_t)function_pointer);
	glFogCoordPointer(type, stride, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglFogCoordPointerBO(JNIEnv *env, jclass clazz, jint type, jint stride, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glFogCoordPointerPROC glFogCoordPointer = (glFogCoordPointerPROC)((intptr_t)function_pointer);
	glFogCoordPointer(type, stride, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglMultiDrawArrays(JNIEnv *env, jclass clazz, jint mode, jlong piFirst, jlong piCount, jint primcount, jlong function_pointer) {
	GLint *piFirst_address = (GLint *)(intptr_t)piFirst;
	GLsizei *piCount_address = (GLsizei *)(intptr_t)piCount;
	glMultiDrawArraysPROC glMultiDrawArrays = (glMultiDrawArraysPROC)((intptr_t)function_pointer);
	glMultiDrawArrays(mode, piFirst_address, piCount_address, primcount);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglPointParameteri(JNIEnv *env, jclass clazz, jint pname, jint param, jlong function_pointer) {
	glPointParameteriPROC glPointParameteri = (glPointParameteriPROC)((intptr_t)function_pointer);
	glPointParameteri(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglPointParameterf(JNIEnv *env, jclass clazz, jint pname, jfloat param, jlong function_pointer) {
	glPointParameterfPROC glPointParameterf = (glPointParameterfPROC)((intptr_t)function_pointer);
	glPointParameterf(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglPointParameteriv(JNIEnv *env, jclass clazz, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glPointParameterivPROC glPointParameteriv = (glPointParameterivPROC)((intptr_t)function_pointer);
	glPointParameteriv(pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglPointParameterfv(JNIEnv *env, jclass clazz, jint pname, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glPointParameterfvPROC glPointParameterfv = (glPointParameterfvPROC)((intptr_t)function_pointer);
	glPointParameterfv(pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglSecondaryColor3b(JNIEnv *env, jclass clazz, jbyte red, jbyte green, jbyte blue, jlong function_pointer) {
	glSecondaryColor3bPROC glSecondaryColor3b = (glSecondaryColor3bPROC)((intptr_t)function_pointer);
	glSecondaryColor3b(red, green, blue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglSecondaryColor3f(JNIEnv *env, jclass clazz, jfloat red, jfloat green, jfloat blue, jlong function_pointer) {
	glSecondaryColor3fPROC glSecondaryColor3f = (glSecondaryColor3fPROC)((intptr_t)function_pointer);
	glSecondaryColor3f(red, green, blue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglSecondaryColor3d(JNIEnv *env, jclass clazz, jdouble red, jdouble green, jdouble blue, jlong function_pointer) {
	glSecondaryColor3dPROC glSecondaryColor3d = (glSecondaryColor3dPROC)((intptr_t)function_pointer);
	glSecondaryColor3d(red, green, blue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglSecondaryColor3ub(JNIEnv *env, jclass clazz, jbyte red, jbyte green, jbyte blue, jlong function_pointer) {
	glSecondaryColor3ubPROC glSecondaryColor3ub = (glSecondaryColor3ubPROC)((intptr_t)function_pointer);
	glSecondaryColor3ub(red, green, blue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglSecondaryColorPointer(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glSecondaryColorPointerPROC glSecondaryColorPointer = (glSecondaryColorPointerPROC)((intptr_t)function_pointer);
	glSecondaryColorPointer(size, type, stride, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglSecondaryColorPointerBO(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glSecondaryColorPointerPROC glSecondaryColorPointer = (glSecondaryColorPointerPROC)((intptr_t)function_pointer);
	glSecondaryColorPointer(size, type, stride, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglBlendFuncSeparate(JNIEnv *env, jclass clazz, jint sfactorRGB, jint dfactorRGB, jint sfactorAlpha, jint dfactorAlpha, jlong function_pointer) {
	glBlendFuncSeparatePROC glBlendFuncSeparate = (glBlendFuncSeparatePROC)((intptr_t)function_pointer);
	glBlendFuncSeparate(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglWindowPos2f(JNIEnv *env, jclass clazz, jfloat x, jfloat y, jlong function_pointer) {
	glWindowPos2fPROC glWindowPos2f = (glWindowPos2fPROC)((intptr_t)function_pointer);
	glWindowPos2f(x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglWindowPos2d(JNIEnv *env, jclass clazz, jdouble x, jdouble y, jlong function_pointer) {
	glWindowPos2dPROC glWindowPos2d = (glWindowPos2dPROC)((intptr_t)function_pointer);
	glWindowPos2d(x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglWindowPos2i(JNIEnv *env, jclass clazz, jint x, jint y, jlong function_pointer) {
	glWindowPos2iPROC glWindowPos2i = (glWindowPos2iPROC)((intptr_t)function_pointer);
	glWindowPos2i(x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglWindowPos3f(JNIEnv *env, jclass clazz, jfloat x, jfloat y, jfloat z, jlong function_pointer) {
	glWindowPos3fPROC glWindowPos3f = (glWindowPos3fPROC)((intptr_t)function_pointer);
	glWindowPos3f(x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglWindowPos3d(JNIEnv *env, jclass clazz, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glWindowPos3dPROC glWindowPos3d = (glWindowPos3dPROC)((intptr_t)function_pointer);
	glWindowPos3d(x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL14_nglWindowPos3i(JNIEnv *env, jclass clazz, jint x, jint y, jint z, jlong function_pointer) {
	glWindowPos3iPROC glWindowPos3i = (glWindowPos3iPROC)((intptr_t)function_pointer);
	glWindowPos3i(x, y, z);
}

