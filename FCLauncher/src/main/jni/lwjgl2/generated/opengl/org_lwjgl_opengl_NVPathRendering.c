/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glPathCommandsNVPROC) (GLuint path, GLsizei numCommands, const GLubyte * commands, GLsizei numCoords, GLenum coordType, const GLvoid * coords);
typedef void (APIENTRY *glPathCoordsNVPROC) (GLuint path, GLsizei numCoords, GLenum coordType, const GLvoid * coords);
typedef void (APIENTRY *glPathSubCommandsNVPROC) (GLuint path, GLsizei commandStart, GLsizei commandsToDelete, GLsizei numCommands, const GLubyte * commands, GLsizei numCoords, GLenum coordType, const GLvoid * coords);
typedef void (APIENTRY *glPathSubCoordsNVPROC) (GLuint path, GLsizei coordStart, GLsizei numCoords, GLenum coordType, const GLvoid * coords);
typedef void (APIENTRY *glPathStringNVPROC) (GLuint path, GLenum format, GLsizei length, const GLvoid * pathString);
typedef void (APIENTRY *glPathGlyphsNVPROC) (GLuint firstPathName, GLenum fontTarget, const GLvoid * fontName, GLbitfield fontStyle, GLsizei numGlyphs, GLenum type, const GLvoid * charcodes, GLenum handleMissingGlyphs, GLuint pathParameterTemplate, GLfloat emScale);
typedef void (APIENTRY *glPathGlyphRangeNVPROC) (GLuint firstPathName, GLenum fontTarget, const GLvoid * fontName, GLbitfield fontStyle, GLuint firstGlyph, GLsizei numGlyphs, GLenum handleMissingGlyphs, GLuint pathParameterTemplate, GLfloat emScale);
typedef void (APIENTRY *glWeightPathsNVPROC) (GLuint resultPath, GLsizei numPaths, const GLuint * paths, const GLfloat * weights);
typedef void (APIENTRY *glCopyPathNVPROC) (GLuint resultPath, GLuint srcPath);
typedef void (APIENTRY *glInterpolatePathsNVPROC) (GLuint resultPath, GLuint pathA, GLuint pathB, GLfloat weight);
typedef void (APIENTRY *glTransformPathNVPROC) (GLuint resultPath, GLuint srcPath, GLenum transformType, const GLfloat * transformValues);
typedef void (APIENTRY *glPathParameterivNVPROC) (GLuint path, GLenum pname, const GLint * value);
typedef void (APIENTRY *glPathParameteriNVPROC) (GLuint path, GLenum pname, GLint value);
typedef void (APIENTRY *glPathParameterfvNVPROC) (GLuint path, GLenum pname, const GLfloat * value);
typedef void (APIENTRY *glPathParameterfNVPROC) (GLuint path, GLenum pname, GLfloat value);
typedef void (APIENTRY *glPathDashArrayNVPROC) (GLuint path, GLsizei dashCount, const GLfloat * dashArray);
typedef GLuint (APIENTRY *glGenPathsNVPROC) (GLsizei range);
typedef void (APIENTRY *glDeletePathsNVPROC) (GLuint path, GLsizei range);
typedef GLboolean (APIENTRY *glIsPathNVPROC) (GLuint path);
typedef void (APIENTRY *glPathStencilFuncNVPROC) (GLenum func, GLint ref, GLuint mask);
typedef void (APIENTRY *glPathStencilDepthOffsetNVPROC) (GLfloat factor, GLint units);
typedef void (APIENTRY *glStencilFillPathNVPROC) (GLuint path, GLenum fillMode, GLuint mask);
typedef void (APIENTRY *glStencilStrokePathNVPROC) (GLuint path, GLint reference, GLuint mask);
typedef void (APIENTRY *glStencilFillPathInstancedNVPROC) (GLsizei numPaths, GLenum pathNameType, const GLvoid * paths, GLuint pathBase, GLenum fillMode, GLuint mask, GLenum transformType, const GLfloat * transformValues);
typedef void (APIENTRY *glStencilStrokePathInstancedNVPROC) (GLsizei numPaths, GLenum pathNameType, const GLvoid * paths, GLuint pathBase, GLint reference, GLuint mask, GLenum transformType, const GLfloat * transformValues);
typedef void (APIENTRY *glPathCoverDepthFuncNVPROC) (GLenum zfunc);
typedef void (APIENTRY *glPathColorGenNVPROC) (GLenum color, GLenum genMode, GLenum colorFormat, const GLfloat * coeffs);
typedef void (APIENTRY *glPathTexGenNVPROC) (GLenum texCoordSet, GLenum genMode, GLint components, const GLfloat * coeffs);
typedef void (APIENTRY *glPathFogGenNVPROC) (GLenum genMode);
typedef void (APIENTRY *glCoverFillPathNVPROC) (GLuint path, GLenum coverMode);
typedef void (APIENTRY *glCoverStrokePathNVPROC) (GLuint name, GLenum coverMode);
typedef void (APIENTRY *glCoverFillPathInstancedNVPROC) (GLsizei numPaths, GLenum pathNameType, const GLvoid * paths, GLuint pathBase, GLenum coverMode, GLenum transformType, const GLfloat * transformValues);
typedef void (APIENTRY *glCoverStrokePathInstancedNVPROC) (GLsizei numPaths, GLenum pathNameType, const GLvoid * paths, GLuint pathBase, GLenum coverMode, GLenum transformType, const GLfloat * transformValues);
typedef void (APIENTRY *glGetPathParameterivNVPROC) (GLuint name, GLenum param, GLint * value);
typedef void (APIENTRY *glGetPathParameterfvNVPROC) (GLuint name, GLenum param, GLfloat * value);
typedef void (APIENTRY *glGetPathCommandsNVPROC) (GLuint name, GLubyte * commands);
typedef void (APIENTRY *glGetPathCoordsNVPROC) (GLuint name, GLfloat * coords);
typedef void (APIENTRY *glGetPathDashArrayNVPROC) (GLuint name, GLfloat * dashArray);
typedef void (APIENTRY *glGetPathMetricsNVPROC) (GLbitfield metricQueryMask, GLsizei numPaths, GLenum pathNameType, const GLvoid * paths, GLuint pathBase, GLsizei stride, GLfloat * metrics);
typedef void (APIENTRY *glGetPathMetricRangeNVPROC) (GLbitfield metricQueryMask, GLuint fistPathName, GLsizei numPaths, GLsizei stride, GLfloat * metrics);
typedef void (APIENTRY *glGetPathSpacingNVPROC) (GLenum pathListMode, GLsizei numPaths, GLenum pathNameType, const GLvoid * paths, GLuint pathBase, GLfloat advanceScale, GLfloat kerningScale, GLenum transformType, GLfloat * returnedSpacing);
typedef void (APIENTRY *glGetPathColorGenivNVPROC) (GLenum color, GLenum pname, GLint * value);
typedef void (APIENTRY *glGetPathColorGenfvNVPROC) (GLenum color, GLenum pname, GLfloat * value);
typedef void (APIENTRY *glGetPathTexGenivNVPROC) (GLenum texCoordSet, GLenum pname, GLint * value);
typedef void (APIENTRY *glGetPathTexGenfvNVPROC) (GLenum texCoordSet, GLenum pname, GLfloat * value);
typedef GLboolean (APIENTRY *glIsPointInFillPathNVPROC) (GLuint path, GLuint mask, GLfloat x, GLfloat y);
typedef GLboolean (APIENTRY *glIsPointInStrokePathNVPROC) (GLuint path, GLfloat x, GLfloat y);
typedef GLfloat (APIENTRY *glGetPathLengthNVPROC) (GLuint path, GLsizei startSegment, GLsizei numSegments);
typedef GLboolean (APIENTRY *glPointAlongPathNVPROC) (GLuint path, GLsizei startSegment, GLsizei numSegments, GLfloat distance, GLfloat * x, GLfloat * y, GLfloat * tangentX, GLfloat * tangentY);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathCommandsNV(JNIEnv *env, jclass clazz, jint path, jint numCommands, jlong commands, jint numCoords, jint coordType, jlong coords, jlong function_pointer) {
	const GLubyte *commands_address = (const GLubyte *)(intptr_t)commands;
	const GLvoid *coords_address = (const GLvoid *)(intptr_t)coords;
	glPathCommandsNVPROC glPathCommandsNV = (glPathCommandsNVPROC)((intptr_t)function_pointer);
	glPathCommandsNV(path, numCommands, commands_address, numCoords, coordType, coords_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathCoordsNV(JNIEnv *env, jclass clazz, jint path, jint numCoords, jint coordType, jlong coords, jlong function_pointer) {
	const GLvoid *coords_address = (const GLvoid *)(intptr_t)coords;
	glPathCoordsNVPROC glPathCoordsNV = (glPathCoordsNVPROC)((intptr_t)function_pointer);
	glPathCoordsNV(path, numCoords, coordType, coords_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathSubCommandsNV(JNIEnv *env, jclass clazz, jint path, jint commandStart, jint commandsToDelete, jint numCommands, jlong commands, jint numCoords, jint coordType, jlong coords, jlong function_pointer) {
	const GLubyte *commands_address = (const GLubyte *)(intptr_t)commands;
	const GLvoid *coords_address = (const GLvoid *)(intptr_t)coords;
	glPathSubCommandsNVPROC glPathSubCommandsNV = (glPathSubCommandsNVPROC)((intptr_t)function_pointer);
	glPathSubCommandsNV(path, commandStart, commandsToDelete, numCommands, commands_address, numCoords, coordType, coords_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathSubCoordsNV(JNIEnv *env, jclass clazz, jint path, jint coordStart, jint numCoords, jint coordType, jlong coords, jlong function_pointer) {
	const GLvoid *coords_address = (const GLvoid *)(intptr_t)coords;
	glPathSubCoordsNVPROC glPathSubCoordsNV = (glPathSubCoordsNVPROC)((intptr_t)function_pointer);
	glPathSubCoordsNV(path, coordStart, numCoords, coordType, coords_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathStringNV(JNIEnv *env, jclass clazz, jint path, jint format, jint length, jlong pathString, jlong function_pointer) {
	const GLvoid *pathString_address = (const GLvoid *)(intptr_t)pathString;
	glPathStringNVPROC glPathStringNV = (glPathStringNVPROC)((intptr_t)function_pointer);
	glPathStringNV(path, format, length, pathString_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathGlyphsNV(JNIEnv *env, jclass clazz, jint firstPathName, jint fontTarget, jlong fontName, jint fontStyle, jint numGlyphs, jint type, jlong charcodes, jint handleMissingGlyphs, jint pathParameterTemplate, jfloat emScale, jlong function_pointer) {
	const GLvoid *fontName_address = (const GLvoid *)(intptr_t)fontName;
	const GLvoid *charcodes_address = (const GLvoid *)(intptr_t)charcodes;
	glPathGlyphsNVPROC glPathGlyphsNV = (glPathGlyphsNVPROC)((intptr_t)function_pointer);
	glPathGlyphsNV(firstPathName, fontTarget, fontName_address, fontStyle, numGlyphs, type, charcodes_address, handleMissingGlyphs, pathParameterTemplate, emScale);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathGlyphRangeNV(JNIEnv *env, jclass clazz, jint firstPathName, jint fontTarget, jlong fontName, jint fontStyle, jint firstGlyph, jint numGlyphs, jint handleMissingGlyphs, jint pathParameterTemplate, jfloat emScale, jlong function_pointer) {
	const GLvoid *fontName_address = (const GLvoid *)(intptr_t)fontName;
	glPathGlyphRangeNVPROC glPathGlyphRangeNV = (glPathGlyphRangeNVPROC)((intptr_t)function_pointer);
	glPathGlyphRangeNV(firstPathName, fontTarget, fontName_address, fontStyle, firstGlyph, numGlyphs, handleMissingGlyphs, pathParameterTemplate, emScale);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglWeightPathsNV(JNIEnv *env, jclass clazz, jint resultPath, jint numPaths, jlong paths, jlong weights, jlong function_pointer) {
	const GLuint *paths_address = (const GLuint *)(intptr_t)paths;
	const GLfloat *weights_address = (const GLfloat *)(intptr_t)weights;
	glWeightPathsNVPROC glWeightPathsNV = (glWeightPathsNVPROC)((intptr_t)function_pointer);
	glWeightPathsNV(resultPath, numPaths, paths_address, weights_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglCopyPathNV(JNIEnv *env, jclass clazz, jint resultPath, jint srcPath, jlong function_pointer) {
	glCopyPathNVPROC glCopyPathNV = (glCopyPathNVPROC)((intptr_t)function_pointer);
	glCopyPathNV(resultPath, srcPath);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglInterpolatePathsNV(JNIEnv *env, jclass clazz, jint resultPath, jint pathA, jint pathB, jfloat weight, jlong function_pointer) {
	glInterpolatePathsNVPROC glInterpolatePathsNV = (glInterpolatePathsNVPROC)((intptr_t)function_pointer);
	glInterpolatePathsNV(resultPath, pathA, pathB, weight);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglTransformPathNV(JNIEnv *env, jclass clazz, jint resultPath, jint srcPath, jint transformType, jlong transformValues, jlong function_pointer) {
	const GLfloat *transformValues_address = (const GLfloat *)(intptr_t)transformValues;
	glTransformPathNVPROC glTransformPathNV = (glTransformPathNVPROC)((intptr_t)function_pointer);
	glTransformPathNV(resultPath, srcPath, transformType, transformValues_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathParameterivNV(JNIEnv *env, jclass clazz, jint path, jint pname, jlong value, jlong function_pointer) {
	const GLint *value_address = (const GLint *)(intptr_t)value;
	glPathParameterivNVPROC glPathParameterivNV = (glPathParameterivNVPROC)((intptr_t)function_pointer);
	glPathParameterivNV(path, pname, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathParameteriNV(JNIEnv *env, jclass clazz, jint path, jint pname, jint value, jlong function_pointer) {
	glPathParameteriNVPROC glPathParameteriNV = (glPathParameteriNVPROC)((intptr_t)function_pointer);
	glPathParameteriNV(path, pname, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathParameterfvNV(JNIEnv *env, jclass clazz, jint path, jint pname, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glPathParameterfvNVPROC glPathParameterfvNV = (glPathParameterfvNVPROC)((intptr_t)function_pointer);
	glPathParameterfvNV(path, pname, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathParameterfNV(JNIEnv *env, jclass clazz, jint path, jint pname, jfloat value, jlong function_pointer) {
	glPathParameterfNVPROC glPathParameterfNV = (glPathParameterfNVPROC)((intptr_t)function_pointer);
	glPathParameterfNV(path, pname, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathDashArrayNV(JNIEnv *env, jclass clazz, jint path, jint dashCount, jlong dashArray, jlong function_pointer) {
	const GLfloat *dashArray_address = (const GLfloat *)(intptr_t)dashArray;
	glPathDashArrayNVPROC glPathDashArrayNV = (glPathDashArrayNVPROC)((intptr_t)function_pointer);
	glPathDashArrayNV(path, dashCount, dashArray_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglGenPathsNV(JNIEnv *env, jclass clazz, jint range, jlong function_pointer) {
	glGenPathsNVPROC glGenPathsNV = (glGenPathsNVPROC)((intptr_t)function_pointer);
	GLuint __result = glGenPathsNV(range);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglDeletePathsNV(JNIEnv *env, jclass clazz, jint path, jint range, jlong function_pointer) {
	glDeletePathsNVPROC glDeletePathsNV = (glDeletePathsNVPROC)((intptr_t)function_pointer);
	glDeletePathsNV(path, range);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglIsPathNV(JNIEnv *env, jclass clazz, jint path, jlong function_pointer) {
	glIsPathNVPROC glIsPathNV = (glIsPathNVPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsPathNV(path);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathStencilFuncNV(JNIEnv *env, jclass clazz, jint func, jint ref, jint mask, jlong function_pointer) {
	glPathStencilFuncNVPROC glPathStencilFuncNV = (glPathStencilFuncNVPROC)((intptr_t)function_pointer);
	glPathStencilFuncNV(func, ref, mask);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathStencilDepthOffsetNV(JNIEnv *env, jclass clazz, jfloat factor, jint units, jlong function_pointer) {
	glPathStencilDepthOffsetNVPROC glPathStencilDepthOffsetNV = (glPathStencilDepthOffsetNVPROC)((intptr_t)function_pointer);
	glPathStencilDepthOffsetNV(factor, units);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglStencilFillPathNV(JNIEnv *env, jclass clazz, jint path, jint fillMode, jint mask, jlong function_pointer) {
	glStencilFillPathNVPROC glStencilFillPathNV = (glStencilFillPathNVPROC)((intptr_t)function_pointer);
	glStencilFillPathNV(path, fillMode, mask);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglStencilStrokePathNV(JNIEnv *env, jclass clazz, jint path, jint reference, jint mask, jlong function_pointer) {
	glStencilStrokePathNVPROC glStencilStrokePathNV = (glStencilStrokePathNVPROC)((intptr_t)function_pointer);
	glStencilStrokePathNV(path, reference, mask);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglStencilFillPathInstancedNV(JNIEnv *env, jclass clazz, jint numPaths, jint pathNameType, jlong paths, jint pathBase, jint fillMode, jint mask, jint transformType, jlong transformValues, jlong function_pointer) {
	const GLvoid *paths_address = (const GLvoid *)(intptr_t)paths;
	const GLfloat *transformValues_address = (const GLfloat *)(intptr_t)transformValues;
	glStencilFillPathInstancedNVPROC glStencilFillPathInstancedNV = (glStencilFillPathInstancedNVPROC)((intptr_t)function_pointer);
	glStencilFillPathInstancedNV(numPaths, pathNameType, paths_address, pathBase, fillMode, mask, transformType, transformValues_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglStencilStrokePathInstancedNV(JNIEnv *env, jclass clazz, jint numPaths, jint pathNameType, jlong paths, jint pathBase, jint reference, jint mask, jint transformType, jlong transformValues, jlong function_pointer) {
	const GLvoid *paths_address = (const GLvoid *)(intptr_t)paths;
	const GLfloat *transformValues_address = (const GLfloat *)(intptr_t)transformValues;
	glStencilStrokePathInstancedNVPROC glStencilStrokePathInstancedNV = (glStencilStrokePathInstancedNVPROC)((intptr_t)function_pointer);
	glStencilStrokePathInstancedNV(numPaths, pathNameType, paths_address, pathBase, reference, mask, transformType, transformValues_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathCoverDepthFuncNV(JNIEnv *env, jclass clazz, jint zfunc, jlong function_pointer) {
	glPathCoverDepthFuncNVPROC glPathCoverDepthFuncNV = (glPathCoverDepthFuncNVPROC)((intptr_t)function_pointer);
	glPathCoverDepthFuncNV(zfunc);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathColorGenNV(JNIEnv *env, jclass clazz, jint color, jint genMode, jint colorFormat, jlong coeffs, jlong function_pointer) {
	const GLfloat *coeffs_address = (const GLfloat *)(intptr_t)coeffs;
	glPathColorGenNVPROC glPathColorGenNV = (glPathColorGenNVPROC)((intptr_t)function_pointer);
	glPathColorGenNV(color, genMode, colorFormat, coeffs_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathTexGenNV(JNIEnv *env, jclass clazz, jint texCoordSet, jint genMode, jint components, jlong coeffs, jlong function_pointer) {
	const GLfloat *coeffs_address = (const GLfloat *)(intptr_t)coeffs;
	glPathTexGenNVPROC glPathTexGenNV = (glPathTexGenNVPROC)((intptr_t)function_pointer);
	glPathTexGenNV(texCoordSet, genMode, components, coeffs_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPathFogGenNV(JNIEnv *env, jclass clazz, jint genMode, jlong function_pointer) {
	glPathFogGenNVPROC glPathFogGenNV = (glPathFogGenNVPROC)((intptr_t)function_pointer);
	glPathFogGenNV(genMode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglCoverFillPathNV(JNIEnv *env, jclass clazz, jint path, jint coverMode, jlong function_pointer) {
	glCoverFillPathNVPROC glCoverFillPathNV = (glCoverFillPathNVPROC)((intptr_t)function_pointer);
	glCoverFillPathNV(path, coverMode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglCoverStrokePathNV(JNIEnv *env, jclass clazz, jint name, jint coverMode, jlong function_pointer) {
	glCoverStrokePathNVPROC glCoverStrokePathNV = (glCoverStrokePathNVPROC)((intptr_t)function_pointer);
	glCoverStrokePathNV(name, coverMode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglCoverFillPathInstancedNV(JNIEnv *env, jclass clazz, jint numPaths, jint pathNameType, jlong paths, jint pathBase, jint coverMode, jint transformType, jlong transformValues, jlong function_pointer) {
	const GLvoid *paths_address = (const GLvoid *)(intptr_t)paths;
	const GLfloat *transformValues_address = (const GLfloat *)(intptr_t)transformValues;
	glCoverFillPathInstancedNVPROC glCoverFillPathInstancedNV = (glCoverFillPathInstancedNVPROC)((intptr_t)function_pointer);
	glCoverFillPathInstancedNV(numPaths, pathNameType, paths_address, pathBase, coverMode, transformType, transformValues_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglCoverStrokePathInstancedNV(JNIEnv *env, jclass clazz, jint numPaths, jint pathNameType, jlong paths, jint pathBase, jint coverMode, jint transformType, jlong transformValues, jlong function_pointer) {
	const GLvoid *paths_address = (const GLvoid *)(intptr_t)paths;
	const GLfloat *transformValues_address = (const GLfloat *)(intptr_t)transformValues;
	glCoverStrokePathInstancedNVPROC glCoverStrokePathInstancedNV = (glCoverStrokePathInstancedNVPROC)((intptr_t)function_pointer);
	glCoverStrokePathInstancedNV(numPaths, pathNameType, paths_address, pathBase, coverMode, transformType, transformValues_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglGetPathParameterivNV(JNIEnv *env, jclass clazz, jint name, jint param, jlong value, jlong function_pointer) {
	GLint *value_address = (GLint *)(intptr_t)value;
	glGetPathParameterivNVPROC glGetPathParameterivNV = (glGetPathParameterivNVPROC)((intptr_t)function_pointer);
	glGetPathParameterivNV(name, param, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglGetPathParameterfvNV(JNIEnv *env, jclass clazz, jint name, jint param, jlong value, jlong function_pointer) {
	GLfloat *value_address = (GLfloat *)(intptr_t)value;
	glGetPathParameterfvNVPROC glGetPathParameterfvNV = (glGetPathParameterfvNVPROC)((intptr_t)function_pointer);
	glGetPathParameterfvNV(name, param, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglGetPathCommandsNV(JNIEnv *env, jclass clazz, jint name, jlong commands, jlong function_pointer) {
	GLubyte *commands_address = (GLubyte *)(intptr_t)commands;
	glGetPathCommandsNVPROC glGetPathCommandsNV = (glGetPathCommandsNVPROC)((intptr_t)function_pointer);
	glGetPathCommandsNV(name, commands_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglGetPathCoordsNV(JNIEnv *env, jclass clazz, jint name, jlong coords, jlong function_pointer) {
	GLfloat *coords_address = (GLfloat *)(intptr_t)coords;
	glGetPathCoordsNVPROC glGetPathCoordsNV = (glGetPathCoordsNVPROC)((intptr_t)function_pointer);
	glGetPathCoordsNV(name, coords_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglGetPathDashArrayNV(JNIEnv *env, jclass clazz, jint name, jlong dashArray, jlong function_pointer) {
	GLfloat *dashArray_address = (GLfloat *)(intptr_t)dashArray;
	glGetPathDashArrayNVPROC glGetPathDashArrayNV = (glGetPathDashArrayNVPROC)((intptr_t)function_pointer);
	glGetPathDashArrayNV(name, dashArray_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglGetPathMetricsNV(JNIEnv *env, jclass clazz, jint metricQueryMask, jint numPaths, jint pathNameType, jlong paths, jint pathBase, jint stride, jlong metrics, jlong function_pointer) {
	const GLvoid *paths_address = (const GLvoid *)(intptr_t)paths;
	GLfloat *metrics_address = (GLfloat *)(intptr_t)metrics;
	glGetPathMetricsNVPROC glGetPathMetricsNV = (glGetPathMetricsNVPROC)((intptr_t)function_pointer);
	glGetPathMetricsNV(metricQueryMask, numPaths, pathNameType, paths_address, pathBase, stride, metrics_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglGetPathMetricRangeNV(JNIEnv *env, jclass clazz, jint metricQueryMask, jint fistPathName, jint numPaths, jint stride, jlong metrics, jlong function_pointer) {
	GLfloat *metrics_address = (GLfloat *)(intptr_t)metrics;
	glGetPathMetricRangeNVPROC glGetPathMetricRangeNV = (glGetPathMetricRangeNVPROC)((intptr_t)function_pointer);
	glGetPathMetricRangeNV(metricQueryMask, fistPathName, numPaths, stride, metrics_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglGetPathSpacingNV(JNIEnv *env, jclass clazz, jint pathListMode, jint numPaths, jint pathNameType, jlong paths, jint pathBase, jfloat advanceScale, jfloat kerningScale, jint transformType, jlong returnedSpacing, jlong function_pointer) {
	const GLvoid *paths_address = (const GLvoid *)(intptr_t)paths;
	GLfloat *returnedSpacing_address = (GLfloat *)(intptr_t)returnedSpacing;
	glGetPathSpacingNVPROC glGetPathSpacingNV = (glGetPathSpacingNVPROC)((intptr_t)function_pointer);
	glGetPathSpacingNV(pathListMode, numPaths, pathNameType, paths_address, pathBase, advanceScale, kerningScale, transformType, returnedSpacing_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglGetPathColorGenivNV(JNIEnv *env, jclass clazz, jint color, jint pname, jlong value, jlong function_pointer) {
	GLint *value_address = (GLint *)(intptr_t)value;
	glGetPathColorGenivNVPROC glGetPathColorGenivNV = (glGetPathColorGenivNVPROC)((intptr_t)function_pointer);
	glGetPathColorGenivNV(color, pname, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglGetPathColorGenfvNV(JNIEnv *env, jclass clazz, jint color, jint pname, jlong value, jlong function_pointer) {
	GLfloat *value_address = (GLfloat *)(intptr_t)value;
	glGetPathColorGenfvNVPROC glGetPathColorGenfvNV = (glGetPathColorGenfvNVPROC)((intptr_t)function_pointer);
	glGetPathColorGenfvNV(color, pname, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglGetPathTexGenivNV(JNIEnv *env, jclass clazz, jint texCoordSet, jint pname, jlong value, jlong function_pointer) {
	GLint *value_address = (GLint *)(intptr_t)value;
	glGetPathTexGenivNVPROC glGetPathTexGenivNV = (glGetPathTexGenivNVPROC)((intptr_t)function_pointer);
	glGetPathTexGenivNV(texCoordSet, pname, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglGetPathTexGenfvNV(JNIEnv *env, jclass clazz, jint texCoordSet, jint pname, jlong value, jlong function_pointer) {
	GLfloat *value_address = (GLfloat *)(intptr_t)value;
	glGetPathTexGenfvNVPROC glGetPathTexGenfvNV = (glGetPathTexGenfvNVPROC)((intptr_t)function_pointer);
	glGetPathTexGenfvNV(texCoordSet, pname, value_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglIsPointInFillPathNV(JNIEnv *env, jclass clazz, jint path, jint mask, jfloat x, jfloat y, jlong function_pointer) {
	glIsPointInFillPathNVPROC glIsPointInFillPathNV = (glIsPointInFillPathNVPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsPointInFillPathNV(path, mask, x, y);
	return __result;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglIsPointInStrokePathNV(JNIEnv *env, jclass clazz, jint path, jfloat x, jfloat y, jlong function_pointer) {
	glIsPointInStrokePathNVPROC glIsPointInStrokePathNV = (glIsPointInStrokePathNVPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsPointInStrokePathNV(path, x, y);
	return __result;
}

JNIEXPORT jfloat JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglGetPathLengthNV(JNIEnv *env, jclass clazz, jint path, jint startSegment, jint numSegments, jlong function_pointer) {
	glGetPathLengthNVPROC glGetPathLengthNV = (glGetPathLengthNVPROC)((intptr_t)function_pointer);
	GLfloat __result = glGetPathLengthNV(path, startSegment, numSegments);
	return __result;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_NVPathRendering_nglPointAlongPathNV(JNIEnv *env, jclass clazz, jint path, jint startSegment, jint numSegments, jfloat distance, jlong x, jlong y, jlong tangentX, jlong tangentY, jlong function_pointer) {
	GLfloat *x_address = (GLfloat *)(intptr_t)x;
	GLfloat *y_address = (GLfloat *)(intptr_t)y;
	GLfloat *tangentX_address = (GLfloat *)(intptr_t)tangentX;
	GLfloat *tangentY_address = (GLfloat *)(intptr_t)tangentY;
	glPointAlongPathNVPROC glPointAlongPathNV = (glPointAlongPathNVPROC)((intptr_t)function_pointer);
	GLboolean __result = glPointAlongPathNV(path, startSegment, numSegments, distance, x_address, y_address, tangentX_address, tangentY_address);
	return __result;
}

