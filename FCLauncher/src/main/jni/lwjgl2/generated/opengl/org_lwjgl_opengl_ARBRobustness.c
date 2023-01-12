/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GLenum (APIENTRY *glGetGraphicsResetStatusARBPROC) ();
typedef void (APIENTRY *glGetnMapdvARBPROC) (GLenum target, GLenum query, GLsizei bufSize, GLdouble * v);
typedef void (APIENTRY *glGetnMapfvARBPROC) (GLenum target, GLenum query, GLsizei bufSize, GLfloat * v);
typedef void (APIENTRY *glGetnMapivARBPROC) (GLenum target, GLenum query, GLsizei bufSize, GLint * v);
typedef void (APIENTRY *glGetnPixelMapfvARBPROC) (GLenum map, GLsizei bufSize, GLfloat * values);
typedef void (APIENTRY *glGetnPixelMapuivARBPROC) (GLenum map, GLsizei bufSize, GLuint * values);
typedef void (APIENTRY *glGetnPixelMapusvARBPROC) (GLenum map, GLsizei bufSize, GLushort * values);
typedef void (APIENTRY *glGetnPolygonStippleARBPROC) (GLsizei bufSize, GLubyte * pattern);
typedef void (APIENTRY *glGetnTexImageARBPROC) (GLenum target, GLint level, GLenum format, GLenum type, GLsizei bufSize, GLvoid * img);
typedef void (APIENTRY *glReadnPixelsARBPROC) (GLint x, GLint y, GLsizei width, GLsizei height, GLenum format, GLenum type, GLsizei bufSize, GLvoid * data);
typedef void (APIENTRY *glGetnColorTableARBPROC) (GLenum target, GLenum format, GLenum type, GLsizei bufSize, GLvoid * table);
typedef void (APIENTRY *glGetnConvolutionFilterARBPROC) (GLenum target, GLenum format, GLenum type, GLsizei bufSize, GLvoid * image);
typedef void (APIENTRY *glGetnSeparableFilterARBPROC) (GLenum target, GLenum format, GLenum type, GLsizei rowBufSize, GLvoid * row, GLsizei columnBufSize, GLvoid * column, GLvoid * span);
typedef void (APIENTRY *glGetnHistogramARBPROC) (GLenum target, GLboolean reset, GLenum format, GLenum type, GLsizei bufSize, GLvoid * values);
typedef void (APIENTRY *glGetnMinmaxARBPROC) (GLenum target, GLboolean reset, GLenum format, GLenum type, GLsizei bufSize, GLvoid * values);
typedef void (APIENTRY *glGetnCompressedTexImageARBPROC) (GLenum target, GLint lod, GLsizei bufSize, GLvoid * img);
typedef void (APIENTRY *glGetnUniformfvARBPROC) (GLuint program, GLint location, GLsizei bufSize, GLfloat * params);
typedef void (APIENTRY *glGetnUniformivARBPROC) (GLuint program, GLint location, GLsizei bufSize, GLint * params);
typedef void (APIENTRY *glGetnUniformuivARBPROC) (GLuint program, GLint location, GLsizei bufSize, GLuint * params);
typedef void (APIENTRY *glGetnUniformdvARBPROC) (GLuint program, GLint location, GLsizei bufSize, GLdouble * params);

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetGraphicsResetStatusARB(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glGetGraphicsResetStatusARBPROC glGetGraphicsResetStatusARB = (glGetGraphicsResetStatusARBPROC)((intptr_t)function_pointer);
	GLenum __result = glGetGraphicsResetStatusARB();
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnMapdvARB(JNIEnv *env, jclass clazz, jint target, jint query, jint bufSize, jlong v, jlong function_pointer) {
	GLdouble *v_address = (GLdouble *)(intptr_t)v;
	glGetnMapdvARBPROC glGetnMapdvARB = (glGetnMapdvARBPROC)((intptr_t)function_pointer);
	glGetnMapdvARB(target, query, bufSize, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnMapfvARB(JNIEnv *env, jclass clazz, jint target, jint query, jint bufSize, jlong v, jlong function_pointer) {
	GLfloat *v_address = (GLfloat *)(intptr_t)v;
	glGetnMapfvARBPROC glGetnMapfvARB = (glGetnMapfvARBPROC)((intptr_t)function_pointer);
	glGetnMapfvARB(target, query, bufSize, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnMapivARB(JNIEnv *env, jclass clazz, jint target, jint query, jint bufSize, jlong v, jlong function_pointer) {
	GLint *v_address = (GLint *)(intptr_t)v;
	glGetnMapivARBPROC glGetnMapivARB = (glGetnMapivARBPROC)((intptr_t)function_pointer);
	glGetnMapivARB(target, query, bufSize, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnPixelMapfvARB(JNIEnv *env, jclass clazz, jint map, jint bufSize, jlong values, jlong function_pointer) {
	GLfloat *values_address = (GLfloat *)(intptr_t)values;
	glGetnPixelMapfvARBPROC glGetnPixelMapfvARB = (glGetnPixelMapfvARBPROC)((intptr_t)function_pointer);
	glGetnPixelMapfvARB(map, bufSize, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnPixelMapuivARB(JNIEnv *env, jclass clazz, jint map, jint bufSize, jlong values, jlong function_pointer) {
	GLuint *values_address = (GLuint *)(intptr_t)values;
	glGetnPixelMapuivARBPROC glGetnPixelMapuivARB = (glGetnPixelMapuivARBPROC)((intptr_t)function_pointer);
	glGetnPixelMapuivARB(map, bufSize, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnPixelMapusvARB(JNIEnv *env, jclass clazz, jint map, jint bufSize, jlong values, jlong function_pointer) {
	GLushort *values_address = (GLushort *)(intptr_t)values;
	glGetnPixelMapusvARBPROC glGetnPixelMapusvARB = (glGetnPixelMapusvARBPROC)((intptr_t)function_pointer);
	glGetnPixelMapusvARB(map, bufSize, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnPolygonStippleARB(JNIEnv *env, jclass clazz, jint bufSize, jlong pattern, jlong function_pointer) {
	GLubyte *pattern_address = (GLubyte *)(intptr_t)pattern;
	glGetnPolygonStippleARBPROC glGetnPolygonStippleARB = (glGetnPolygonStippleARBPROC)((intptr_t)function_pointer);
	glGetnPolygonStippleARB(bufSize, pattern_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnTexImageARB(JNIEnv *env, jclass clazz, jint target, jint level, jint format, jint type, jint bufSize, jlong img, jlong function_pointer) {
	GLvoid *img_address = (GLvoid *)(intptr_t)img;
	glGetnTexImageARBPROC glGetnTexImageARB = (glGetnTexImageARBPROC)((intptr_t)function_pointer);
	glGetnTexImageARB(target, level, format, type, bufSize, img_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnTexImageARBBO(JNIEnv *env, jclass clazz, jint target, jint level, jint format, jint type, jint bufSize, jlong img_buffer_offset, jlong function_pointer) {
	GLvoid *img_address = (GLvoid *)(intptr_t)offsetToPointer(img_buffer_offset);
	glGetnTexImageARBPROC glGetnTexImageARB = (glGetnTexImageARBPROC)((intptr_t)function_pointer);
	glGetnTexImageARB(target, level, format, type, bufSize, img_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglReadnPixelsARB(JNIEnv *env, jclass clazz, jint x, jint y, jint width, jint height, jint format, jint type, jint bufSize, jlong data, jlong function_pointer) {
	GLvoid *data_address = (GLvoid *)(intptr_t)data;
	glReadnPixelsARBPROC glReadnPixelsARB = (glReadnPixelsARBPROC)((intptr_t)function_pointer);
	glReadnPixelsARB(x, y, width, height, format, type, bufSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglReadnPixelsARBBO(JNIEnv *env, jclass clazz, jint x, jint y, jint width, jint height, jint format, jint type, jint bufSize, jlong data_buffer_offset, jlong function_pointer) {
	GLvoid *data_address = (GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glReadnPixelsARBPROC glReadnPixelsARB = (glReadnPixelsARBPROC)((intptr_t)function_pointer);
	glReadnPixelsARB(x, y, width, height, format, type, bufSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnColorTableARB(JNIEnv *env, jclass clazz, jint target, jint format, jint type, jint bufSize, jlong table, jlong function_pointer) {
	GLvoid *table_address = (GLvoid *)(intptr_t)table;
	glGetnColorTableARBPROC glGetnColorTableARB = (glGetnColorTableARBPROC)((intptr_t)function_pointer);
	glGetnColorTableARB(target, format, type, bufSize, table_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnConvolutionFilterARB(JNIEnv *env, jclass clazz, jint target, jint format, jint type, jint bufSize, jlong image, jlong function_pointer) {
	GLvoid *image_address = (GLvoid *)(intptr_t)image;
	glGetnConvolutionFilterARBPROC glGetnConvolutionFilterARB = (glGetnConvolutionFilterARBPROC)((intptr_t)function_pointer);
	glGetnConvolutionFilterARB(target, format, type, bufSize, image_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnConvolutionFilterARBBO(JNIEnv *env, jclass clazz, jint target, jint format, jint type, jint bufSize, jlong image_buffer_offset, jlong function_pointer) {
	GLvoid *image_address = (GLvoid *)(intptr_t)offsetToPointer(image_buffer_offset);
	glGetnConvolutionFilterARBPROC glGetnConvolutionFilterARB = (glGetnConvolutionFilterARBPROC)((intptr_t)function_pointer);
	glGetnConvolutionFilterARB(target, format, type, bufSize, image_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnSeparableFilterARB(JNIEnv *env, jclass clazz, jint target, jint format, jint type, jint rowBufSize, jlong row, jint columnBufSize, jlong column, jlong span, jlong function_pointer) {
	GLvoid *row_address = (GLvoid *)(intptr_t)row;
	GLvoid *column_address = (GLvoid *)(intptr_t)column;
	GLvoid *span_address = (GLvoid *)(intptr_t)span;
	glGetnSeparableFilterARBPROC glGetnSeparableFilterARB = (glGetnSeparableFilterARBPROC)((intptr_t)function_pointer);
	glGetnSeparableFilterARB(target, format, type, rowBufSize, row_address, columnBufSize, column_address, span_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnSeparableFilterARBBO(JNIEnv *env, jclass clazz, jint target, jint format, jint type, jint rowBufSize, jlong row_buffer_offset, jint columnBufSize, jlong column_buffer_offset, jlong span_buffer_offset, jlong function_pointer) {
	GLvoid *row_address = (GLvoid *)(intptr_t)offsetToPointer(row_buffer_offset);
	GLvoid *column_address = (GLvoid *)(intptr_t)offsetToPointer(column_buffer_offset);
	GLvoid *span_address = (GLvoid *)(intptr_t)offsetToPointer(span_buffer_offset);
	glGetnSeparableFilterARBPROC glGetnSeparableFilterARB = (glGetnSeparableFilterARBPROC)((intptr_t)function_pointer);
	glGetnSeparableFilterARB(target, format, type, rowBufSize, row_address, columnBufSize, column_address, span_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnHistogramARB(JNIEnv *env, jclass clazz, jint target, jboolean reset, jint format, jint type, jint bufSize, jlong values, jlong function_pointer) {
	GLvoid *values_address = (GLvoid *)(intptr_t)values;
	glGetnHistogramARBPROC glGetnHistogramARB = (glGetnHistogramARBPROC)((intptr_t)function_pointer);
	glGetnHistogramARB(target, reset, format, type, bufSize, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnHistogramARBBO(JNIEnv *env, jclass clazz, jint target, jboolean reset, jint format, jint type, jint bufSize, jlong values_buffer_offset, jlong function_pointer) {
	GLvoid *values_address = (GLvoid *)(intptr_t)offsetToPointer(values_buffer_offset);
	glGetnHistogramARBPROC glGetnHistogramARB = (glGetnHistogramARBPROC)((intptr_t)function_pointer);
	glGetnHistogramARB(target, reset, format, type, bufSize, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnMinmaxARB(JNIEnv *env, jclass clazz, jint target, jboolean reset, jint format, jint type, jint bufSize, jlong values, jlong function_pointer) {
	GLvoid *values_address = (GLvoid *)(intptr_t)values;
	glGetnMinmaxARBPROC glGetnMinmaxARB = (glGetnMinmaxARBPROC)((intptr_t)function_pointer);
	glGetnMinmaxARB(target, reset, format, type, bufSize, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnMinmaxARBBO(JNIEnv *env, jclass clazz, jint target, jboolean reset, jint format, jint type, jint bufSize, jlong values_buffer_offset, jlong function_pointer) {
	GLvoid *values_address = (GLvoid *)(intptr_t)offsetToPointer(values_buffer_offset);
	glGetnMinmaxARBPROC glGetnMinmaxARB = (glGetnMinmaxARBPROC)((intptr_t)function_pointer);
	glGetnMinmaxARB(target, reset, format, type, bufSize, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnCompressedTexImageARB(JNIEnv *env, jclass clazz, jint target, jint lod, jint bufSize, jlong img, jlong function_pointer) {
	GLvoid *img_address = (GLvoid *)(intptr_t)img;
	glGetnCompressedTexImageARBPROC glGetnCompressedTexImageARB = (glGetnCompressedTexImageARBPROC)((intptr_t)function_pointer);
	glGetnCompressedTexImageARB(target, lod, bufSize, img_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnCompressedTexImageARBBO(JNIEnv *env, jclass clazz, jint target, jint lod, jint bufSize, jlong img_buffer_offset, jlong function_pointer) {
	GLvoid *img_address = (GLvoid *)(intptr_t)offsetToPointer(img_buffer_offset);
	glGetnCompressedTexImageARBPROC glGetnCompressedTexImageARB = (glGetnCompressedTexImageARBPROC)((intptr_t)function_pointer);
	glGetnCompressedTexImageARB(target, lod, bufSize, img_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnUniformfvARB(JNIEnv *env, jclass clazz, jint program, jint location, jint bufSize, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetnUniformfvARBPROC glGetnUniformfvARB = (glGetnUniformfvARBPROC)((intptr_t)function_pointer);
	glGetnUniformfvARB(program, location, bufSize, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnUniformivARB(JNIEnv *env, jclass clazz, jint program, jint location, jint bufSize, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetnUniformivARBPROC glGetnUniformivARB = (glGetnUniformivARBPROC)((intptr_t)function_pointer);
	glGetnUniformivARB(program, location, bufSize, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnUniformuivARB(JNIEnv *env, jclass clazz, jint program, jint location, jint bufSize, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetnUniformuivARBPROC glGetnUniformuivARB = (glGetnUniformuivARBPROC)((intptr_t)function_pointer);
	glGetnUniformuivARB(program, location, bufSize, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBRobustness_nglGetnUniformdvARB(JNIEnv *env, jclass clazz, jint program, jint location, jint bufSize, jlong params, jlong function_pointer) {
	GLdouble *params_address = (GLdouble *)(intptr_t)params;
	glGetnUniformdvARBPROC glGetnUniformdvARB = (glGetnUniformdvARBPROC)((intptr_t)function_pointer);
	glGetnUniformdvARB(program, location, bufSize, params_address);
}

