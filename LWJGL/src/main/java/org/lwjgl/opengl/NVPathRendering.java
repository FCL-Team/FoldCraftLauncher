/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVPathRendering {

	/**
	 *  Accepted in elements of the &lt;commands&gt; array parameter of
	 *  PathCommandsNV and PathSubCommandsNV:
	 */
	public static final int GL_CLOSE_PATH_NV = 0x0,
		GL_MOVE_TO_NV = 0x2,
		GL_RELATIVE_MOVE_TO_NV = 0x3,
		GL_LINE_TO_NV = 0x4,
		GL_RELATIVE_LINE_TO_NV = 0x5,
		GL_HORIZONTAL_LINE_TO_NV = 0x6,
		GL_RELATIVE_HORIZONTAL_LINE_TO_NV = 0x7,
		GL_VERTICAL_LINE_TO_NV = 0x8,
		GL_RELATIVE_VERTICAL_LINE_TO_NV = 0x9,
		GL_QUADRATIC_CURVE_TO_NV = 0xA,
		GL_RELATIVE_QUADRATIC_CURVE_TO_NV = 0xB,
		GL_CUBIC_CURVE_TO_NV = 0xC,
		GL_RELATIVE_CUBIC_CURVE_TO_NV = 0xD,
		GL_SMOOTH_QUADRATIC_CURVE_TO_NV = 0xE,
		GL_RELATIVE_SMOOTH_QUADRATIC_CURVE_TO_NV = 0xF,
		GL_SMOOTH_CUBIC_CURVE_TO_NV = 0x10,
		GL_RELATIVE_SMOOTH_CUBIC_CURVE_TO_NV = 0x11,
		GL_SMALL_CCW_ARC_TO_NV = 0x12,
		GL_RELATIVE_SMALL_CCW_ARC_TO_NV = 0x13,
		GL_SMALL_CW_ARC_TO_NV = 0x14,
		GL_RELATIVE_SMALL_CW_ARC_TO_NV = 0x15,
		GL_LARGE_CCW_ARC_TO_NV = 0x16,
		GL_RELATIVE_LARGE_CCW_ARC_TO_NV = 0x17,
		GL_LARGE_CW_ARC_TO_NV = 0x18,
		GL_RELATIVE_LARGE_CW_ARC_TO_NV = 0x19,
		GL_CIRCULAR_CCW_ARC_TO_NV = 0xF8,
		GL_CIRCULAR_CW_ARC_TO_NV = 0xFA,
		GL_CIRCULAR_TANGENT_ARC_TO_NV = 0xFC,
		GL_ARC_TO_NV = 0xFE,
		GL_RELATIVE_ARC_TO_NV = 0xFF;

	/**
	 * Accepted by the &lt;format&gt; parameter of PathStringNV: 
	 */
	public static final int GL_PATH_FORMAT_SVG_NV = 0x9070,
		GL_PATH_FORMAT_PS_NV = 0x9071;

	/**
	 *  Accepted by the &lt;fontTarget&gt; parameter of PathGlyphsNV and
	 *  PathGlyphRangeNV:
	 */
	public static final int GL_STANDARD_FONT_NAME_NV = 0x9072,
		GL_SYSTEM_FONT_NAME_NV = 0x9073,
		GL_FILE_NAME_NV = 0x9074;

	/**
	 *  Accepted by the &lt;handleMissingGlyph&gt; parameter of PathGlyphsNV and
	 *  PathGlyphRangeNV:
	 */
	public static final int GL_SKIP_MISSING_GLYPH_NV = 0x90A9,
		GL_USE_MISSING_GLYPH_NV = 0x90AA;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of PathParameterfNV,
	 *  PathParameterfvNV, GetPathParameterfvNV, PathParameteriNV,
	 *  PathParameterivNV, and GetPathParameterivNV:
	 */
	public static final int GL_PATH_STROKE_WIDTH_NV = 0x9075,
		GL_PATH_INITIAL_END_CAP_NV = 0x9077,
		GL_PATH_TERMINAL_END_CAP_NV = 0x9078,
		GL_PATH_JOIN_STYLE_NV = 0x9079,
		GL_PATH_MITER_LIMIT_NV = 0x907A,
		GL_PATH_INITIAL_DASH_CAP_NV = 0x907C,
		GL_PATH_TERMINAL_DASH_CAP_NV = 0x907D,
		GL_PATH_DASH_OFFSET_NV = 0x907E,
		GL_PATH_CLIENT_LENGTH_NV = 0x907F,
		GL_PATH_DASH_OFFSET_RESET_NV = 0x90B4,
		GL_PATH_FILL_MODE_NV = 0x9080,
		GL_PATH_FILL_MASK_NV = 0x9081,
		GL_PATH_FILL_COVER_MODE_NV = 0x9082,
		GL_PATH_STROKE_COVER_MODE_NV = 0x9083,
		GL_PATH_STROKE_MASK_NV = 0x9084;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of PathParameterfNV and
	 *  PathParameterfvNV:
	 */
	public static final int GL_PATH_END_CAPS_NV = 0x9076,
		GL_PATH_DASH_CAPS_NV = 0x907B;

	/**
	 *  Accepted by the &lt;fillMode&gt; parameter of StencilFillPathNV and
	 *  StencilFillPathInstancedNV:
	 */
	public static final int GL_COUNT_UP_NV = 0x9088,
		GL_COUNT_DOWN_NV = 0x9089;

	/**
	 *  Accepted by the &lt;color&gt; parameter of PathColorGenNV,
	 *  GetPathColorGenivNV, and GetPathColorGenfvNV:
	 */
	public static final int GL_PRIMARY_COLOR = 0x8577,
		GL_PRIMARY_COLOR_NV = 0x852C,
		GL_SECONDARY_COLOR_NV = 0x852D;

	/**
	 *  Accepted by the &lt;genMode&gt; parameter of PathColorGenNV and
	 *  PathTexGenNV:
	 */
	public static final int GL_PATH_OBJECT_BOUNDING_BOX_NV = 0x908A;

	/**
	 *  Accepted by the &lt;coverMode&gt; parameter of CoverFillPathNV and
	 *  CoverFillPathInstancedNV:
	 */
	public static final int GL_CONVEX_HULL_NV = 0x908B,
		GL_BOUNDING_BOX_NV = 0x908D;

	/**
	 *  Accepted by the &lt;transformType&gt; parameter of
	 *  StencilFillPathInstancedNV, StencilStrokePathInstancedNV,
	 *  CoverFillPathInstancedNV, and CoverStrokePathInstancedNV:
	 */
	public static final int GL_TRANSLATE_X_NV = 0x908E,
		GL_TRANSLATE_Y_NV = 0x908F,
		GL_TRANSLATE_2D_NV = 0x9090,
		GL_TRANSLATE_3D_NV = 0x9091,
		GL_AFFINE_2D_NV = 0x9092,
		GL_AFFINE_3D_NV = 0x9094,
		GL_TRANSPOSE_AFFINE_2D_NV = 0x9096,
		GL_TRANSPOSE_AFFINE_3D_NV = 0x9098;

	/**
	 *  Accepted by the &lt;type&gt; or &lt;pathNameType&gt; parameter of CallLists,
	 *  StencilFillPathInstancedNV, StencilStrokePathInstancedNV,
	 *  CoverFillPathInstancedNV, CoverStrokePathInstancedNV,
	 *  GetPathMetricsNV, and GetPathSpacingNV:
	 */
	public static final int GL_UTF8_NV = 0x909A,
		GL_UTF16_NV = 0x909B;

	/**
	 * Accepted by the &lt;coverMode&gt; parameter of CoverFillPathInstancedNV: 
	 */
	public static final int GL_BOUNDING_BOX_OF_BOUNDING_BOXES_NV = 0x909C;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetPathParameterfvNV and
	 *  GetPathParameterivNV:
	 */
	public static final int GL_PATH_COMMAND_COUNT_NV = 0x909D,
		GL_PATH_COORD_COUNT_NV = 0x909E,
		GL_PATH_DASH_ARRAY_COUNT_NV = 0x909F,
		GL_PATH_COMPUTED_LENGTH_NV = 0x90A0,
		GL_PATH_FILL_BOUNDING_BOX_NV = 0x90A1,
		GL_PATH_STROKE_BOUNDING_BOX_NV = 0x90A2;

	/**
	 *  Accepted by the &lt;value&gt; parameter of PathParameterfNV,
	 *  PathParameterfvNV, PathParameteriNV, and PathParameterivNV
	 *  when &lt;pname&gt; is one of PATH_END_CAPS_NV, PATH_INTIAL_END_CAP_NV,
	 *  PATH_TERMINAL_END_CAP_NV, PATH_DASH_CAPS_NV, PATH_INITIAL_DASH_CAP_NV,
	 *  and PATH_TERMINAL_DASH_CAP_NV:
	 */
	public static final int GL_SQUARE_NV = 0x90A3,
		GL_ROUND_NV = 0x90A4,
		GL_TRIANGULAR_NV = 0x90A5;

	/**
	 *  Accepted by the &lt;value&gt; parameter of PathParameterfNV,
	 *  PathParameterfvNV, PathParameteriNV, and PathParameterivNV
	 *  when &lt;pname&gt; is PATH_JOIN_STYLE_NV:
	 */
	public static final int GL_BEVEL_NV = 0x90A6,
		GL_MITER_REVERT_NV = 0x90A7,
		GL_MITER_TRUNCATE_NV = 0x90A8;

	/**
	 *  Accepted by the &lt;value&gt; parameter of PathParameterfNV,
	 *  PathParameterfvNV, PathParameteriNV, and PathParameterivNV when
	 *  &lt;pname&gt; is PATH_DASH_OFFSET_RESET_NV
	 */
	public static final int GL_MOVE_TO_RESETS_NV = 0x90B5,
		GL_MOVE_TO_CONTINUES_NV = 0x90B6;

	/**
	 * Accepted by the &lt;fontStyle&gt; parameter of PathStringNV: 
	 */
	public static final int GL_BOLD_BIT_NV = 0x1,
		GL_ITALIC_BIT_NV = 0x2;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_PATH_ERROR_POSITION_NV = 0x90AB,
		GL_PATH_FOG_GEN_MODE_NV = 0x90AC,
		GL_PATH_STENCIL_FUNC_NV = 0x90B7,
		GL_PATH_STENCIL_REF_NV = 0x90B8,
		GL_PATH_STENCIL_VALUE_MASK_NV = 0x90B9,
		GL_PATH_STENCIL_DEPTH_OFFSET_FACTOR_NV = 0x90BD,
		GL_PATH_STENCIL_DEPTH_OFFSET_UNITS_NV = 0x90BE,
		GL_PATH_COVER_DEPTH_FUNC_NV = 0x90BF;

	/**
	 *  Accepted as a bit within the &lt;metricQueryMask&gt; parameter of
	 *  GetPathMetricRangeNV or GetPathMetricsNV:
	 */
	public static final int GL_GLYPH_WIDTH_BIT_NV = 0x1,
		GL_GLYPH_HEIGHT_BIT_NV = 0x2,
		GL_GLYPH_HORIZONTAL_BEARING_X_BIT_NV = 0x4,
		GL_GLYPH_HORIZONTAL_BEARING_Y_BIT_NV = 0x8,
		GL_GLYPH_HORIZONTAL_BEARING_ADVANCE_BIT_NV = 0x10,
		GL_GLYPH_VERTICAL_BEARING_X_BIT_NV = 0x20,
		GL_GLYPH_VERTICAL_BEARING_Y_BIT_NV = 0x40,
		GL_GLYPH_VERTICAL_BEARING_ADVANCE_BIT_NV = 0x80,
		GL_GLYPH_HAS_KERNING_NV = 0x100,
		GL_FONT_X_MIN_BOUNDS_NV = 0x10000,
		GL_FONT_Y_MIN_BOUNDS_NV = 0x20000,
		GL_FONT_X_MAX_BOUNDS_NV = 0x40000,
		GL_FONT_Y_MAX_BOUNDS_NV = 0x80000,
		GL_FONT_UNITS_PER_EM_NV = 0x100000,
		GL_FONT_ASCENDER_NV = 0x200000,
		GL_FONT_DESCENDER_NV = 0x400000,
		GL_FONT_HEIGHT_NV = 0x800000,
		GL_FONT_MAX_ADVANCE_WIDTH_NV = 0x1000000,
		GL_FONT_MAX_ADVANCE_HEIGHT_NV = 0x2000000,
		GL_FONT_UNDERLINE_POSITION_NV = 0x4000000,
		GL_FONT_UNDERLINE_THICKNESS_NV = 0x8000000,
		GL_FONT_HAS_KERNING_NV = 0x10000000;

	/**
	 * Accepted by the &lt;pathListMode&gt; parameter of GetPathSpacingNV: 
	 */
	public static final int GL_ACCUM_ADJACENT_PAIRS_NV = 0x90AD,
		GL_ADJACENT_PAIRS_NV = 0x90AE,
		GL_FIRST_TO_REST_NV = 0x90AF;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetPathColorGenivNV,
	 *  GetPathColorGenfvNV, GetPathTexGenivNV and GetPathTexGenfvNV:
	 */
	public static final int GL_PATH_GEN_MODE_NV = 0x90B0,
		GL_PATH_GEN_COEFF_NV = 0x90B1,
		GL_PATH_GEN_COLOR_FORMAT_NV = 0x90B2,
		GL_PATH_GEN_COMPONENTS_NV = 0x90B3;

	private NVPathRendering() {}

	public static void glPathCommandsNV(int path, ByteBuffer commands, int coordType, ByteBuffer coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathCommandsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(commands);
		BufferChecks.checkDirect(coords);
		nglPathCommandsNV(path, commands.remaining(), MemoryUtil.getAddress(commands), coords.remaining(), coordType, MemoryUtil.getAddress(coords), function_pointer);
	}
	static native void nglPathCommandsNV(int path, int commands_numCommands, long commands, int coords_numCoords, int coordType, long coords, long function_pointer);

	public static void glPathCoordsNV(int path, int coordType, ByteBuffer coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathCoordsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(coords);
		nglPathCoordsNV(path, coords.remaining(), coordType, MemoryUtil.getAddress(coords), function_pointer);
	}
	static native void nglPathCoordsNV(int path, int coords_numCoords, int coordType, long coords, long function_pointer);

	public static void glPathSubCommandsNV(int path, int commandStart, int commandsToDelete, ByteBuffer commands, int coordType, ByteBuffer coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathSubCommandsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(commands);
		BufferChecks.checkDirect(coords);
		nglPathSubCommandsNV(path, commandStart, commandsToDelete, commands.remaining(), MemoryUtil.getAddress(commands), coords.remaining(), coordType, MemoryUtil.getAddress(coords), function_pointer);
	}
	static native void nglPathSubCommandsNV(int path, int commandStart, int commandsToDelete, int commands_numCommands, long commands, int coords_numCoords, int coordType, long coords, long function_pointer);

	public static void glPathSubCoordsNV(int path, int coordStart, int coordType, ByteBuffer coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathSubCoordsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(coords);
		nglPathSubCoordsNV(path, coordStart, coords.remaining(), coordType, MemoryUtil.getAddress(coords), function_pointer);
	}
	static native void nglPathSubCoordsNV(int path, int coordStart, int coords_numCoords, int coordType, long coords, long function_pointer);

	public static void glPathStringNV(int path, int format, ByteBuffer pathString) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathStringNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pathString);
		nglPathStringNV(path, format, pathString.remaining(), MemoryUtil.getAddress(pathString), function_pointer);
	}
	static native void nglPathStringNV(int path, int format, int pathString_length, long pathString, long function_pointer);

	public static void glPathGlyphsNV(int firstPathName, int fontTarget, ByteBuffer fontName, int fontStyle, int type, ByteBuffer charcodes, int handleMissingGlyphs, int pathParameterTemplate, float emScale) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathGlyphsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(fontName);
		BufferChecks.checkNullTerminated(fontName);
		BufferChecks.checkDirect(charcodes);
		nglPathGlyphsNV(firstPathName, fontTarget, MemoryUtil.getAddress(fontName), fontStyle, charcodes.remaining() / GLChecks.calculateBytesPerCharCode(type), type, MemoryUtil.getAddress(charcodes), handleMissingGlyphs, pathParameterTemplate, emScale, function_pointer);
	}
	static native void nglPathGlyphsNV(int firstPathName, int fontTarget, long fontName, int fontStyle, int charcodes_numGlyphs, int type, long charcodes, int handleMissingGlyphs, int pathParameterTemplate, float emScale, long function_pointer);

	public static void glPathGlyphRangeNV(int firstPathName, int fontTarget, ByteBuffer fontName, int fontStyle, int firstGlyph, int numGlyphs, int handleMissingGlyphs, int pathParameterTemplate, float emScale) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathGlyphRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(fontName);
		BufferChecks.checkNullTerminated(fontName);
		nglPathGlyphRangeNV(firstPathName, fontTarget, MemoryUtil.getAddress(fontName), fontStyle, firstGlyph, numGlyphs, handleMissingGlyphs, pathParameterTemplate, emScale, function_pointer);
	}
	static native void nglPathGlyphRangeNV(int firstPathName, int fontTarget, long fontName, int fontStyle, int firstGlyph, int numGlyphs, int handleMissingGlyphs, int pathParameterTemplate, float emScale, long function_pointer);

	public static void glWeightPathsNV(int resultPath, IntBuffer paths, FloatBuffer weights) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightPathsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(paths);
		BufferChecks.checkBuffer(weights, paths.remaining());
		nglWeightPathsNV(resultPath, paths.remaining(), MemoryUtil.getAddress(paths), MemoryUtil.getAddress(weights), function_pointer);
	}
	static native void nglWeightPathsNV(int resultPath, int paths_numPaths, long paths, long weights, long function_pointer);

	public static void glCopyPathNV(int resultPath, int srcPath) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyPathNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyPathNV(resultPath, srcPath, function_pointer);
	}
	static native void nglCopyPathNV(int resultPath, int srcPath, long function_pointer);

	public static void glInterpolatePathsNV(int resultPath, int pathA, int pathB, float weight) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInterpolatePathsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglInterpolatePathsNV(resultPath, pathA, pathB, weight, function_pointer);
	}
	static native void nglInterpolatePathsNV(int resultPath, int pathA, int pathB, float weight, long function_pointer);

	public static void glTransformPathNV(int resultPath, int srcPath, int transformType, FloatBuffer transformValues) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTransformPathNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (transformValues != null)
			BufferChecks.checkBuffer(transformValues, GLChecks.calculateTransformPathValues(transformType));
		nglTransformPathNV(resultPath, srcPath, transformType, MemoryUtil.getAddressSafe(transformValues), function_pointer);
	}
	static native void nglTransformPathNV(int resultPath, int srcPath, int transformType, long transformValues, long function_pointer);

	public static void glPathParameterNV(int path, int pname, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathParameterivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 4);
		nglPathParameterivNV(path, pname, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglPathParameterivNV(int path, int pname, long value, long function_pointer);

	public static void glPathParameteriNV(int path, int pname, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathParameteriNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPathParameteriNV(path, pname, value, function_pointer);
	}
	static native void nglPathParameteriNV(int path, int pname, int value, long function_pointer);

	public static void glPathParameterNV(int path, int pname, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 4);
		nglPathParameterfvNV(path, pname, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglPathParameterfvNV(int path, int pname, long value, long function_pointer);

	public static void glPathParameterfNV(int path, int pname, float value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathParameterfNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPathParameterfNV(path, pname, value, function_pointer);
	}
	static native void nglPathParameterfNV(int path, int pname, float value, long function_pointer);

	public static void glPathDashArrayNV(int path, FloatBuffer dashArray) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathDashArrayNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(dashArray);
		nglPathDashArrayNV(path, dashArray.remaining(), MemoryUtil.getAddress(dashArray), function_pointer);
	}
	static native void nglPathDashArrayNV(int path, int dashArray_dashCount, long dashArray, long function_pointer);

	public static int glGenPathsNV(int range) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenPathsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGenPathsNV(range, function_pointer);
		return __result;
	}
	static native int nglGenPathsNV(int range, long function_pointer);

	public static void glDeletePathsNV(int path, int range) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeletePathsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeletePathsNV(path, range, function_pointer);
	}
	static native void nglDeletePathsNV(int path, int range, long function_pointer);

	public static boolean glIsPathNV(int path) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsPathNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsPathNV(path, function_pointer);
		return __result;
	}
	static native boolean nglIsPathNV(int path, long function_pointer);

	public static void glPathStencilFuncNV(int func, int ref, int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathStencilFuncNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPathStencilFuncNV(func, ref, mask, function_pointer);
	}
	static native void nglPathStencilFuncNV(int func, int ref, int mask, long function_pointer);

	public static void glPathStencilDepthOffsetNV(float factor, int units) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathStencilDepthOffsetNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPathStencilDepthOffsetNV(factor, units, function_pointer);
	}
	static native void nglPathStencilDepthOffsetNV(float factor, int units, long function_pointer);

	public static void glStencilFillPathNV(int path, int fillMode, int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glStencilFillPathNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglStencilFillPathNV(path, fillMode, mask, function_pointer);
	}
	static native void nglStencilFillPathNV(int path, int fillMode, int mask, long function_pointer);

	public static void glStencilStrokePathNV(int path, int reference, int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glStencilStrokePathNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglStencilStrokePathNV(path, reference, mask, function_pointer);
	}
	static native void nglStencilStrokePathNV(int path, int reference, int mask, long function_pointer);

	public static void glStencilFillPathInstancedNV(int pathNameType, ByteBuffer paths, int pathBase, int fillMode, int mask, int transformType, FloatBuffer transformValues) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glStencilFillPathInstancedNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(paths);
		if (transformValues != null)
			BufferChecks.checkBuffer(transformValues, GLChecks.calculateTransformPathValues(transformType));
		nglStencilFillPathInstancedNV(paths.remaining() / GLChecks.calculateBytesPerPathName(pathNameType), pathNameType, MemoryUtil.getAddress(paths), pathBase, fillMode, mask, transformType, MemoryUtil.getAddressSafe(transformValues), function_pointer);
	}
	static native void nglStencilFillPathInstancedNV(int paths_numPaths, int pathNameType, long paths, int pathBase, int fillMode, int mask, int transformType, long transformValues, long function_pointer);

	public static void glStencilStrokePathInstancedNV(int pathNameType, ByteBuffer paths, int pathBase, int reference, int mask, int transformType, FloatBuffer transformValues) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glStencilStrokePathInstancedNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(paths);
		if (transformValues != null)
			BufferChecks.checkBuffer(transformValues, GLChecks.calculateTransformPathValues(transformType));
		nglStencilStrokePathInstancedNV(paths.remaining() / GLChecks.calculateBytesPerPathName(pathNameType), pathNameType, MemoryUtil.getAddress(paths), pathBase, reference, mask, transformType, MemoryUtil.getAddressSafe(transformValues), function_pointer);
	}
	static native void nglStencilStrokePathInstancedNV(int paths_numPaths, int pathNameType, long paths, int pathBase, int reference, int mask, int transformType, long transformValues, long function_pointer);

	public static void glPathCoverDepthFuncNV(int zfunc) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathCoverDepthFuncNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPathCoverDepthFuncNV(zfunc, function_pointer);
	}
	static native void nglPathCoverDepthFuncNV(int zfunc, long function_pointer);

	public static void glPathColorGenNV(int color, int genMode, int colorFormat, FloatBuffer coeffs) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathColorGenNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (coeffs != null)
			BufferChecks.checkBuffer(coeffs, GLChecks.calculatePathColorGenCoeffsCount(genMode, colorFormat));
		nglPathColorGenNV(color, genMode, colorFormat, MemoryUtil.getAddressSafe(coeffs), function_pointer);
	}
	static native void nglPathColorGenNV(int color, int genMode, int colorFormat, long coeffs, long function_pointer);

	public static void glPathTexGenNV(int texCoordSet, int genMode, FloatBuffer coeffs) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathTexGenNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (coeffs != null)
			BufferChecks.checkDirect(coeffs);
		nglPathTexGenNV(texCoordSet, genMode, GLChecks.calculatePathTextGenCoeffsPerComponent(coeffs, genMode), MemoryUtil.getAddressSafe(coeffs), function_pointer);
	}
	static native void nglPathTexGenNV(int texCoordSet, int genMode, int coeffs_components, long coeffs, long function_pointer);

	public static void glPathFogGenNV(int genMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPathFogGenNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPathFogGenNV(genMode, function_pointer);
	}
	static native void nglPathFogGenNV(int genMode, long function_pointer);

	public static void glCoverFillPathNV(int path, int coverMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCoverFillPathNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCoverFillPathNV(path, coverMode, function_pointer);
	}
	static native void nglCoverFillPathNV(int path, int coverMode, long function_pointer);

	public static void glCoverStrokePathNV(int name, int coverMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCoverStrokePathNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCoverStrokePathNV(name, coverMode, function_pointer);
	}
	static native void nglCoverStrokePathNV(int name, int coverMode, long function_pointer);

	public static void glCoverFillPathInstancedNV(int pathNameType, ByteBuffer paths, int pathBase, int coverMode, int transformType, FloatBuffer transformValues) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCoverFillPathInstancedNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(paths);
		if (transformValues != null)
			BufferChecks.checkBuffer(transformValues, GLChecks.calculateTransformPathValues(transformType));
		nglCoverFillPathInstancedNV(paths.remaining() / GLChecks.calculateBytesPerPathName(pathNameType), pathNameType, MemoryUtil.getAddress(paths), pathBase, coverMode, transformType, MemoryUtil.getAddressSafe(transformValues), function_pointer);
	}
	static native void nglCoverFillPathInstancedNV(int paths_numPaths, int pathNameType, long paths, int pathBase, int coverMode, int transformType, long transformValues, long function_pointer);

	public static void glCoverStrokePathInstancedNV(int pathNameType, ByteBuffer paths, int pathBase, int coverMode, int transformType, FloatBuffer transformValues) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCoverStrokePathInstancedNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(paths);
		if (transformValues != null)
			BufferChecks.checkBuffer(transformValues, GLChecks.calculateTransformPathValues(transformType));
		nglCoverStrokePathInstancedNV(paths.remaining() / GLChecks.calculateBytesPerPathName(pathNameType), pathNameType, MemoryUtil.getAddress(paths), pathBase, coverMode, transformType, MemoryUtil.getAddressSafe(transformValues), function_pointer);
	}
	static native void nglCoverStrokePathInstancedNV(int paths_numPaths, int pathNameType, long paths, int pathBase, int coverMode, int transformType, long transformValues, long function_pointer);

	public static void glGetPathParameterNV(int name, int param, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathParameterivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 4);
		nglGetPathParameterivNV(name, param, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglGetPathParameterivNV(int name, int param, long value, long function_pointer);

	/** Overloads glGetPathParameterivNV. */
	public static int glGetPathParameteriNV(int name, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathParameterivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer value = APIUtil.getBufferInt(caps);
		nglGetPathParameterivNV(name, param, MemoryUtil.getAddress(value), function_pointer);
		return value.get(0);
	}

	public static void glGetPathParameterfvNV(int name, int param, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 4);
		nglGetPathParameterfvNV(name, param, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglGetPathParameterfvNV(int name, int param, long value, long function_pointer);

	/** Overloads glGetPathParameterfvNV. */
	public static float glGetPathParameterfNV(int name, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer value = APIUtil.getBufferFloat(caps);
		nglGetPathParameterfvNV(name, param, MemoryUtil.getAddress(value), function_pointer);
		return value.get(0);
	}

	public static void glGetPathCommandsNV(int name, ByteBuffer commands) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathCommandsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(commands);
		nglGetPathCommandsNV(name, MemoryUtil.getAddress(commands), function_pointer);
	}
	static native void nglGetPathCommandsNV(int name, long commands, long function_pointer);

	public static void glGetPathCoordsNV(int name, FloatBuffer coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathCoordsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(coords);
		nglGetPathCoordsNV(name, MemoryUtil.getAddress(coords), function_pointer);
	}
	static native void nglGetPathCoordsNV(int name, long coords, long function_pointer);

	public static void glGetPathDashArrayNV(int name, FloatBuffer dashArray) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathDashArrayNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(dashArray);
		nglGetPathDashArrayNV(name, MemoryUtil.getAddress(dashArray), function_pointer);
	}
	static native void nglGetPathDashArrayNV(int name, long dashArray, long function_pointer);

	public static void glGetPathMetricsNV(int metricQueryMask, int pathNameType, ByteBuffer paths, int pathBase, int stride, FloatBuffer metrics) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathMetricsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(paths);
		BufferChecks.checkBuffer(metrics, GLChecks.calculateMetricsSize(metricQueryMask, stride));
		nglGetPathMetricsNV(metricQueryMask, paths.remaining() / GLChecks.calculateBytesPerPathName(pathNameType), pathNameType, MemoryUtil.getAddress(paths), pathBase, stride, MemoryUtil.getAddress(metrics), function_pointer);
	}
	static native void nglGetPathMetricsNV(int metricQueryMask, int paths_numPaths, int pathNameType, long paths, int pathBase, int stride, long metrics, long function_pointer);

	public static void glGetPathMetricRangeNV(int metricQueryMask, int fistPathName, int numPaths, int stride, FloatBuffer metrics) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathMetricRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(metrics, GLChecks.calculateMetricsSize(metricQueryMask, stride));
		nglGetPathMetricRangeNV(metricQueryMask, fistPathName, numPaths, stride, MemoryUtil.getAddress(metrics), function_pointer);
	}
	static native void nglGetPathMetricRangeNV(int metricQueryMask, int fistPathName, int numPaths, int stride, long metrics, long function_pointer);

	public static void glGetPathSpacingNV(int pathListMode, int pathNameType, ByteBuffer paths, int pathBase, float advanceScale, float kerningScale, int transformType, FloatBuffer returnedSpacing) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathSpacingNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		int numPaths = paths.remaining() / GLChecks.calculateBytesPerPathName(pathNameType);
		BufferChecks.checkDirect(paths);
		BufferChecks.checkBuffer(returnedSpacing, numPaths - 1);
		nglGetPathSpacingNV(pathListMode, numPaths, pathNameType, MemoryUtil.getAddress(paths), pathBase, advanceScale, kerningScale, transformType, MemoryUtil.getAddress(returnedSpacing), function_pointer);
	}
	static native void nglGetPathSpacingNV(int pathListMode, int paths_numPaths, int pathNameType, long paths, int pathBase, float advanceScale, float kerningScale, int transformType, long returnedSpacing, long function_pointer);

	public static void glGetPathColorGenNV(int color, int pname, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathColorGenivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 16);
		nglGetPathColorGenivNV(color, pname, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglGetPathColorGenivNV(int color, int pname, long value, long function_pointer);

	/** Overloads glGetPathColorGenivNV. */
	public static int glGetPathColorGeniNV(int color, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathColorGenivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer value = APIUtil.getBufferInt(caps);
		nglGetPathColorGenivNV(color, pname, MemoryUtil.getAddress(value), function_pointer);
		return value.get(0);
	}

	public static void glGetPathColorGenNV(int color, int pname, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathColorGenfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 16);
		nglGetPathColorGenfvNV(color, pname, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglGetPathColorGenfvNV(int color, int pname, long value, long function_pointer);

	/** Overloads glGetPathColorGenfvNV. */
	public static float glGetPathColorGenfNV(int color, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathColorGenfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer value = APIUtil.getBufferFloat(caps);
		nglGetPathColorGenfvNV(color, pname, MemoryUtil.getAddress(value), function_pointer);
		return value.get(0);
	}

	public static void glGetPathTexGenNV(int texCoordSet, int pname, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathTexGenivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 16);
		nglGetPathTexGenivNV(texCoordSet, pname, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglGetPathTexGenivNV(int texCoordSet, int pname, long value, long function_pointer);

	/** Overloads glGetPathTexGenivNV. */
	public static int glGetPathTexGeniNV(int texCoordSet, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathTexGenivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer value = APIUtil.getBufferInt(caps);
		nglGetPathTexGenivNV(texCoordSet, pname, MemoryUtil.getAddress(value), function_pointer);
		return value.get(0);
	}

	public static void glGetPathTexGenNV(int texCoordSet, int pname, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathTexGenfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 16);
		nglGetPathTexGenfvNV(texCoordSet, pname, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglGetPathTexGenfvNV(int texCoordSet, int pname, long value, long function_pointer);

	/** Overloads glGetPathTexGenfvNV. */
	public static float glGetPathTexGenfNV(int texCoordSet, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathTexGenfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer value = APIUtil.getBufferFloat(caps);
		nglGetPathTexGenfvNV(texCoordSet, pname, MemoryUtil.getAddress(value), function_pointer);
		return value.get(0);
	}

	public static boolean glIsPointInFillPathNV(int path, int mask, float x, float y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsPointInFillPathNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsPointInFillPathNV(path, mask, x, y, function_pointer);
		return __result;
	}
	static native boolean nglIsPointInFillPathNV(int path, int mask, float x, float y, long function_pointer);

	public static boolean glIsPointInStrokePathNV(int path, float x, float y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsPointInStrokePathNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsPointInStrokePathNV(path, x, y, function_pointer);
		return __result;
	}
	static native boolean nglIsPointInStrokePathNV(int path, float x, float y, long function_pointer);

	public static float glGetPathLengthNV(int path, int startSegment, int numSegments) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPathLengthNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		float __result = nglGetPathLengthNV(path, startSegment, numSegments, function_pointer);
		return __result;
	}
	static native float nglGetPathLengthNV(int path, int startSegment, int numSegments, long function_pointer);

	public static boolean glPointAlongPathNV(int path, int startSegment, int numSegments, float distance, FloatBuffer x, FloatBuffer y, FloatBuffer tangentX, FloatBuffer tangentY) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPointAlongPathNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (x != null)
			BufferChecks.checkBuffer(x, 1);
		if (y != null)
			BufferChecks.checkBuffer(y, 1);
		if (tangentX != null)
			BufferChecks.checkBuffer(tangentX, 1);
		if (tangentY != null)
			BufferChecks.checkBuffer(tangentY, 1);
		boolean __result = nglPointAlongPathNV(path, startSegment, numSegments, distance, MemoryUtil.getAddressSafe(x), MemoryUtil.getAddressSafe(y), MemoryUtil.getAddressSafe(tangentX), MemoryUtil.getAddressSafe(tangentY), function_pointer);
		return __result;
	}
	static native boolean nglPointAlongPathNV(int path, int startSegment, int numSegments, float distance, long x, long y, long tangentX, long tangentY, long function_pointer);
}
