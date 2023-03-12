/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glColorTablePROC) (GLenum target, GLenum internalFormat, GLsizei width, GLenum format, GLenum type, const GLvoid * data);
typedef void (APIENTRY *glColorSubTablePROC) (GLenum target, GLsizei start, GLsizei count, GLenum format, GLenum type, const GLvoid * data);
typedef void (APIENTRY *glColorTableParameterivPROC) (GLenum target, GLenum pname, const GLint * params);
typedef void (APIENTRY *glColorTableParameterfvPROC) (GLenum target, GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glCopyColorSubTablePROC) (GLenum target, GLsizei start, GLint x, GLint y, GLsizei width);
typedef void (APIENTRY *glCopyColorTablePROC) (GLenum target, GLenum internalformat, GLint x, GLint y, GLsizei width);
typedef void (APIENTRY *glGetColorTablePROC) (GLenum target, GLenum format, GLenum type, GLvoid * data);
typedef void (APIENTRY *glGetColorTableParameterivPROC) (GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetColorTableParameterfvPROC) (GLenum target, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glHistogramPROC) (GLenum target, GLsizei width, GLenum internalformat, GLboolean sink);
typedef void (APIENTRY *glResetHistogramPROC) (GLenum target);
typedef void (APIENTRY *glGetHistogramPROC) (GLenum target, GLboolean reset, GLenum format, GLenum type, GLvoid * values);
typedef void (APIENTRY *glGetHistogramParameterfvPROC) (GLenum target, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetHistogramParameterivPROC) (GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glMinmaxPROC) (GLenum target, GLenum internalformat, GLboolean sink);
typedef void (APIENTRY *glResetMinmaxPROC) (GLenum target);
typedef void (APIENTRY *glGetMinmaxPROC) (GLenum target, GLboolean reset, GLenum format, GLenum types, GLvoid * values);
typedef void (APIENTRY *glGetMinmaxParameterfvPROC) (GLenum target, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetMinmaxParameterivPROC) (GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glConvolutionFilter1DPROC) (GLenum target, GLenum internalformat, GLsizei width, GLenum format, GLenum type, const GLvoid * image);
typedef void (APIENTRY *glConvolutionFilter2DPROC) (GLenum target, GLenum internalformat, GLsizei width, GLsizei height, GLenum format, GLenum type, const GLvoid * image);
typedef void (APIENTRY *glConvolutionParameterfPROC) (GLenum target, GLenum pname, GLfloat params);
typedef void (APIENTRY *glConvolutionParameterfvPROC) (GLenum target, GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glConvolutionParameteriPROC) (GLenum target, GLenum pname, GLint params);
typedef void (APIENTRY *glConvolutionParameterivPROC) (GLenum target, GLenum pname, const GLint * params);
typedef void (APIENTRY *glCopyConvolutionFilter1DPROC) (GLenum target, GLenum internalformat, GLint x, GLint y, GLsizei width);
typedef void (APIENTRY *glCopyConvolutionFilter2DPROC) (GLenum target, GLenum internalformat, GLint x, GLint y, GLsizei width, GLsizei height);
typedef void (APIENTRY *glGetConvolutionFilterPROC) (GLenum target, GLenum format, GLenum type, GLvoid * image);
typedef void (APIENTRY *glGetConvolutionParameterfvPROC) (GLenum target, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetConvolutionParameterivPROC) (GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glSeparableFilter2DPROC) (GLenum target, GLenum internalformat, GLsizei width, GLsizei height, GLenum format, GLenum type, const GLvoid * row, const GLvoid * column);
typedef void (APIENTRY *glGetSeparableFilterPROC) (GLenum target, GLenum format, GLenum type, GLvoid * row, GLvoid * column, GLvoid * span);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglColorTable(JNIEnv *env, jclass clazz, jint target, jint internalFormat, jint width, jint format, jint type, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glColorTablePROC glColorTable = (glColorTablePROC)((intptr_t)function_pointer);
	glColorTable(target, internalFormat, width, format, type, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglColorTableBO(JNIEnv *env, jclass clazz, jint target, jint internalFormat, jint width, jint format, jint type, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glColorTablePROC glColorTable = (glColorTablePROC)((intptr_t)function_pointer);
	glColorTable(target, internalFormat, width, format, type, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglColorSubTable(JNIEnv *env, jclass clazz, jint target, jint start, jint count, jint format, jint type, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glColorSubTablePROC glColorSubTable = (glColorSubTablePROC)((intptr_t)function_pointer);
	glColorSubTable(target, start, count, format, type, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglColorSubTableBO(JNIEnv *env, jclass clazz, jint target, jint start, jint count, jint format, jint type, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glColorSubTablePROC glColorSubTable = (glColorSubTablePROC)((intptr_t)function_pointer);
	glColorSubTable(target, start, count, format, type, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglColorTableParameteriv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glColorTableParameterivPROC glColorTableParameteriv = (glColorTableParameterivPROC)((intptr_t)function_pointer);
	glColorTableParameteriv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglColorTableParameterfv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glColorTableParameterfvPROC glColorTableParameterfv = (glColorTableParameterfvPROC)((intptr_t)function_pointer);
	glColorTableParameterfv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglCopyColorSubTable(JNIEnv *env, jclass clazz, jint target, jint start, jint x, jint y, jint width, jlong function_pointer) {
	glCopyColorSubTablePROC glCopyColorSubTable = (glCopyColorSubTablePROC)((intptr_t)function_pointer);
	glCopyColorSubTable(target, start, x, y, width);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglCopyColorTable(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint x, jint y, jint width, jlong function_pointer) {
	glCopyColorTablePROC glCopyColorTable = (glCopyColorTablePROC)((intptr_t)function_pointer);
	glCopyColorTable(target, internalformat, x, y, width);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetColorTable(JNIEnv *env, jclass clazz, jint target, jint format, jint type, jlong data, jlong function_pointer) {
	GLvoid *data_address = (GLvoid *)(intptr_t)data;
	glGetColorTablePROC glGetColorTable = (glGetColorTablePROC)((intptr_t)function_pointer);
	glGetColorTable(target, format, type, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetColorTableParameteriv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetColorTableParameterivPROC glGetColorTableParameteriv = (glGetColorTableParameterivPROC)((intptr_t)function_pointer);
	glGetColorTableParameteriv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetColorTableParameterfv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetColorTableParameterfvPROC glGetColorTableParameterfv = (glGetColorTableParameterfvPROC)((intptr_t)function_pointer);
	glGetColorTableParameterfv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglHistogram(JNIEnv *env, jclass clazz, jint target, jint width, jint internalformat, jboolean sink, jlong function_pointer) {
	glHistogramPROC glHistogram = (glHistogramPROC)((intptr_t)function_pointer);
	glHistogram(target, width, internalformat, sink);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglResetHistogram(JNIEnv *env, jclass clazz, jint target, jlong function_pointer) {
	glResetHistogramPROC glResetHistogram = (glResetHistogramPROC)((intptr_t)function_pointer);
	glResetHistogram(target);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetHistogram(JNIEnv *env, jclass clazz, jint target, jboolean reset, jint format, jint type, jlong values, jlong function_pointer) {
	GLvoid *values_address = (GLvoid *)(intptr_t)values;
	glGetHistogramPROC glGetHistogram = (glGetHistogramPROC)((intptr_t)function_pointer);
	glGetHistogram(target, reset, format, type, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetHistogramBO(JNIEnv *env, jclass clazz, jint target, jboolean reset, jint format, jint type, jlong values_buffer_offset, jlong function_pointer) {
	GLvoid *values_address = (GLvoid *)(intptr_t)offsetToPointer(values_buffer_offset);
	glGetHistogramPROC glGetHistogram = (glGetHistogramPROC)((intptr_t)function_pointer);
	glGetHistogram(target, reset, format, type, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetHistogramParameterfv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetHistogramParameterfvPROC glGetHistogramParameterfv = (glGetHistogramParameterfvPROC)((intptr_t)function_pointer);
	glGetHistogramParameterfv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetHistogramParameteriv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetHistogramParameterivPROC glGetHistogramParameteriv = (glGetHistogramParameterivPROC)((intptr_t)function_pointer);
	glGetHistogramParameteriv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglMinmax(JNIEnv *env, jclass clazz, jint target, jint internalformat, jboolean sink, jlong function_pointer) {
	glMinmaxPROC glMinmax = (glMinmaxPROC)((intptr_t)function_pointer);
	glMinmax(target, internalformat, sink);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglResetMinmax(JNIEnv *env, jclass clazz, jint target, jlong function_pointer) {
	glResetMinmaxPROC glResetMinmax = (glResetMinmaxPROC)((intptr_t)function_pointer);
	glResetMinmax(target);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetMinmax(JNIEnv *env, jclass clazz, jint target, jboolean reset, jint format, jint types, jlong values, jlong function_pointer) {
	GLvoid *values_address = (GLvoid *)(intptr_t)values;
	glGetMinmaxPROC glGetMinmax = (glGetMinmaxPROC)((intptr_t)function_pointer);
	glGetMinmax(target, reset, format, types, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetMinmaxBO(JNIEnv *env, jclass clazz, jint target, jboolean reset, jint format, jint types, jlong values_buffer_offset, jlong function_pointer) {
	GLvoid *values_address = (GLvoid *)(intptr_t)offsetToPointer(values_buffer_offset);
	glGetMinmaxPROC glGetMinmax = (glGetMinmaxPROC)((intptr_t)function_pointer);
	glGetMinmax(target, reset, format, types, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetMinmaxParameterfv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetMinmaxParameterfvPROC glGetMinmaxParameterfv = (glGetMinmaxParameterfvPROC)((intptr_t)function_pointer);
	glGetMinmaxParameterfv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetMinmaxParameteriv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetMinmaxParameterivPROC glGetMinmaxParameteriv = (glGetMinmaxParameterivPROC)((intptr_t)function_pointer);
	glGetMinmaxParameteriv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglConvolutionFilter1D(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint width, jint format, jint type, jlong image, jlong function_pointer) {
	const GLvoid *image_address = (const GLvoid *)(intptr_t)image;
	glConvolutionFilter1DPROC glConvolutionFilter1D = (glConvolutionFilter1DPROC)((intptr_t)function_pointer);
	glConvolutionFilter1D(target, internalformat, width, format, type, image_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglConvolutionFilter1DBO(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint width, jint format, jint type, jlong image_buffer_offset, jlong function_pointer) {
	const GLvoid *image_address = (const GLvoid *)(intptr_t)offsetToPointer(image_buffer_offset);
	glConvolutionFilter1DPROC glConvolutionFilter1D = (glConvolutionFilter1DPROC)((intptr_t)function_pointer);
	glConvolutionFilter1D(target, internalformat, width, format, type, image_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglConvolutionFilter2D(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint width, jint height, jint format, jint type, jlong image, jlong function_pointer) {
	const GLvoid *image_address = (const GLvoid *)(intptr_t)image;
	glConvolutionFilter2DPROC glConvolutionFilter2D = (glConvolutionFilter2DPROC)((intptr_t)function_pointer);
	glConvolutionFilter2D(target, internalformat, width, height, format, type, image_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglConvolutionFilter2DBO(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint width, jint height, jint format, jint type, jlong image_buffer_offset, jlong function_pointer) {
	const GLvoid *image_address = (const GLvoid *)(intptr_t)offsetToPointer(image_buffer_offset);
	glConvolutionFilter2DPROC glConvolutionFilter2D = (glConvolutionFilter2DPROC)((intptr_t)function_pointer);
	glConvolutionFilter2D(target, internalformat, width, height, format, type, image_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglConvolutionParameterf(JNIEnv *env, jclass clazz, jint target, jint pname, jfloat params, jlong function_pointer) {
	glConvolutionParameterfPROC glConvolutionParameterf = (glConvolutionParameterfPROC)((intptr_t)function_pointer);
	glConvolutionParameterf(target, pname, params);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglConvolutionParameterfv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glConvolutionParameterfvPROC glConvolutionParameterfv = (glConvolutionParameterfvPROC)((intptr_t)function_pointer);
	glConvolutionParameterfv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglConvolutionParameteri(JNIEnv *env, jclass clazz, jint target, jint pname, jint params, jlong function_pointer) {
	glConvolutionParameteriPROC glConvolutionParameteri = (glConvolutionParameteriPROC)((intptr_t)function_pointer);
	glConvolutionParameteri(target, pname, params);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglConvolutionParameteriv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glConvolutionParameterivPROC glConvolutionParameteriv = (glConvolutionParameterivPROC)((intptr_t)function_pointer);
	glConvolutionParameteriv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglCopyConvolutionFilter1D(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint x, jint y, jint width, jlong function_pointer) {
	glCopyConvolutionFilter1DPROC glCopyConvolutionFilter1D = (glCopyConvolutionFilter1DPROC)((intptr_t)function_pointer);
	glCopyConvolutionFilter1D(target, internalformat, x, y, width);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglCopyConvolutionFilter2D(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint x, jint y, jint width, jint height, jlong function_pointer) {
	glCopyConvolutionFilter2DPROC glCopyConvolutionFilter2D = (glCopyConvolutionFilter2DPROC)((intptr_t)function_pointer);
	glCopyConvolutionFilter2D(target, internalformat, x, y, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetConvolutionFilter(JNIEnv *env, jclass clazz, jint target, jint format, jint type, jlong image, jlong function_pointer) {
	GLvoid *image_address = (GLvoid *)(intptr_t)image;
	glGetConvolutionFilterPROC glGetConvolutionFilter = (glGetConvolutionFilterPROC)((intptr_t)function_pointer);
	glGetConvolutionFilter(target, format, type, image_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetConvolutionFilterBO(JNIEnv *env, jclass clazz, jint target, jint format, jint type, jlong image_buffer_offset, jlong function_pointer) {
	GLvoid *image_address = (GLvoid *)(intptr_t)offsetToPointer(image_buffer_offset);
	glGetConvolutionFilterPROC glGetConvolutionFilter = (glGetConvolutionFilterPROC)((intptr_t)function_pointer);
	glGetConvolutionFilter(target, format, type, image_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetConvolutionParameterfv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetConvolutionParameterfvPROC glGetConvolutionParameterfv = (glGetConvolutionParameterfvPROC)((intptr_t)function_pointer);
	glGetConvolutionParameterfv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetConvolutionParameteriv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetConvolutionParameterivPROC glGetConvolutionParameteriv = (glGetConvolutionParameterivPROC)((intptr_t)function_pointer);
	glGetConvolutionParameteriv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglSeparableFilter2D(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint width, jint height, jint format, jint type, jlong row, jlong column, jlong function_pointer) {
	const GLvoid *row_address = (const GLvoid *)(intptr_t)row;
	const GLvoid *column_address = (const GLvoid *)(intptr_t)column;
	glSeparableFilter2DPROC glSeparableFilter2D = (glSeparableFilter2DPROC)((intptr_t)function_pointer);
	glSeparableFilter2D(target, internalformat, width, height, format, type, row_address, column_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglSeparableFilter2DBO(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint width, jint height, jint format, jint type, jlong row_buffer_offset, jlong column_buffer_offset, jlong function_pointer) {
	const GLvoid *row_address = (const GLvoid *)(intptr_t)offsetToPointer(row_buffer_offset);
	const GLvoid *column_address = (const GLvoid *)(intptr_t)offsetToPointer(column_buffer_offset);
	glSeparableFilter2DPROC glSeparableFilter2D = (glSeparableFilter2DPROC)((intptr_t)function_pointer);
	glSeparableFilter2D(target, internalformat, width, height, format, type, row_address, column_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetSeparableFilter(JNIEnv *env, jclass clazz, jint target, jint format, jint type, jlong row, jlong column, jlong span, jlong function_pointer) {
	GLvoid *row_address = (GLvoid *)(intptr_t)row;
	GLvoid *column_address = (GLvoid *)(intptr_t)column;
	GLvoid *span_address = (GLvoid *)(intptr_t)span;
	glGetSeparableFilterPROC glGetSeparableFilter = (glGetSeparableFilterPROC)((intptr_t)function_pointer);
	glGetSeparableFilter(target, format, type, row_address, column_address, span_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBImaging_nglGetSeparableFilterBO(JNIEnv *env, jclass clazz, jint target, jint format, jint type, jlong row_buffer_offset, jlong column_buffer_offset, jlong span_buffer_offset, jlong function_pointer) {
	GLvoid *row_address = (GLvoid *)(intptr_t)offsetToPointer(row_buffer_offset);
	GLvoid *column_address = (GLvoid *)(intptr_t)offsetToPointer(column_buffer_offset);
	GLvoid *span_address = (GLvoid *)(intptr_t)offsetToPointer(span_buffer_offset);
	glGetSeparableFilterPROC glGetSeparableFilter = (glGetSeparableFilterPROC)((intptr_t)function_pointer);
	glGetSeparableFilter(target, format, type, row_address, column_address, span_address);
}

