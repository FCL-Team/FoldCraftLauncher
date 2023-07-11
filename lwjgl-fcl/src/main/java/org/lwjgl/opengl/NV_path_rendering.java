/*
 * Copyright (c) 2002-2011 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lwjgl.opengl;

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public interface NV_path_rendering {

	/**
	 * Accepted in elements of the &lt;commands&gt; array parameter of
	 * PathCommandsNV and PathSubCommandsNV:
	 */
	int GL_CLOSE_PATH_NV                         = 0x00,
		GL_MOVE_TO_NV                            = 0x02,
		GL_RELATIVE_MOVE_TO_NV                   = 0x03,
		GL_LINE_TO_NV                            = 0x04,
		GL_RELATIVE_LINE_TO_NV                   = 0x05,
		GL_HORIZONTAL_LINE_TO_NV                 = 0x06,
		GL_RELATIVE_HORIZONTAL_LINE_TO_NV        = 0x07,
		GL_VERTICAL_LINE_TO_NV                   = 0x08,
		GL_RELATIVE_VERTICAL_LINE_TO_NV          = 0x09,
		GL_QUADRATIC_CURVE_TO_NV                 = 0x0A,
		GL_RELATIVE_QUADRATIC_CURVE_TO_NV        = 0x0B,
		GL_CUBIC_CURVE_TO_NV                     = 0x0C,
		GL_RELATIVE_CUBIC_CURVE_TO_NV            = 0x0D,
		GL_SMOOTH_QUADRATIC_CURVE_TO_NV          = 0x0E,
		GL_RELATIVE_SMOOTH_QUADRATIC_CURVE_TO_NV = 0x0F,
		GL_SMOOTH_CUBIC_CURVE_TO_NV              = 0x10,
		GL_RELATIVE_SMOOTH_CUBIC_CURVE_TO_NV     = 0x11,
		GL_SMALL_CCW_ARC_TO_NV                   = 0x12,
		GL_RELATIVE_SMALL_CCW_ARC_TO_NV          = 0x13,
		GL_SMALL_CW_ARC_TO_NV                    = 0x14,
		GL_RELATIVE_SMALL_CW_ARC_TO_NV           = 0x15,
		GL_LARGE_CCW_ARC_TO_NV                   = 0x16,
		GL_RELATIVE_LARGE_CCW_ARC_TO_NV          = 0x17,
		GL_LARGE_CW_ARC_TO_NV                    = 0x18,
		GL_RELATIVE_LARGE_CW_ARC_TO_NV           = 0x19,
		GL_CIRCULAR_CCW_ARC_TO_NV                = 0xF8,
		GL_CIRCULAR_CW_ARC_TO_NV                 = 0xFA,
		GL_CIRCULAR_TANGENT_ARC_TO_NV            = 0xFC,
		GL_ARC_TO_NV                             = 0xFE,
		GL_RELATIVE_ARC_TO_NV                    = 0xFF;

	/** Accepted by the &lt;format&gt; parameter of PathStringNV: */
	int GL_PATH_FORMAT_SVG_NV = 0x9070,
		GL_PATH_FORMAT_PS_NV  = 0x9071;

	/**
	 * Accepted by the &lt;fontTarget&gt; parameter of PathGlyphsNV and
	 * PathGlyphRangeNV:
	 */
	int GL_STANDARD_FONT_NAME_NV = 0x9072,
		GL_SYSTEM_FONT_NAME_NV   = 0x9073,
		GL_FILE_NAME_NV          = 0x9074;

	/**
	 * Accepted by the &lt;handleMissingGlyph&gt; parameter of PathGlyphsNV and
	 * PathGlyphRangeNV:
	 */
	int GL_SKIP_MISSING_GLYPH_NV = 0x90A9,
		GL_USE_MISSING_GLYPH_NV  = 0x90AA;

	/**
	 * Accepted by the &lt;pname&gt; parameter of PathParameterfNV,
	 * PathParameterfvNV, GetPathParameterfvNV, PathParameteriNV,
	 * PathParameterivNV, and GetPathParameterivNV:
	 */
	int GL_PATH_STROKE_WIDTH_NV      = 0x9075,
		GL_PATH_INITIAL_END_CAP_NV   = 0x9077,
		GL_PATH_TERMINAL_END_CAP_NV  = 0x9078,
		GL_PATH_JOIN_STYLE_NV        = 0x9079,
		GL_PATH_MITER_LIMIT_NV       = 0x907A,
		GL_PATH_INITIAL_DASH_CAP_NV  = 0x907C,
		GL_PATH_TERMINAL_DASH_CAP_NV = 0x907D,
		GL_PATH_DASH_OFFSET_NV       = 0x907E,
		GL_PATH_CLIENT_LENGTH_NV     = 0x907F,
		GL_PATH_DASH_OFFSET_RESET_NV = 0x90B4,

	GL_PATH_FILL_MODE_NV             = 0x9080,
		GL_PATH_FILL_MASK_NV         = 0x9081,
		GL_PATH_FILL_COVER_MODE_NV   = 0x9082,
		GL_PATH_STROKE_COVER_MODE_NV = 0x9083,
		GL_PATH_STROKE_MASK_NV       = 0x9084;

	/**
	 * Accepted by the &lt;pname&gt; parameter of PathParameterfNV and
	 * PathParameterfvNV:
	 */
	int GL_PATH_END_CAPS_NV  = 0x9076,
		GL_PATH_DASH_CAPS_NV = 0x907B;

	/**
	 * Accepted by the &lt;fillMode&gt; parameter of StencilFillPathNV and
	 * StencilFillPathInstancedNV:
	 */
	int GL_COUNT_UP_NV   = 0x9088,
		GL_COUNT_DOWN_NV = 0x9089;

	/**
	 * Accepted by the &lt;color&gt; parameter of PathColorGenNV,
	 * GetPathColorGenivNV, and GetPathColorGenfvNV:
	 */
	int GL_PRIMARY_COLOR      = 0x8577,  // from OpenGL 1.3
		GL_PRIMARY_COLOR_NV   = 0x852C,  // from NV_register_combiners
		GL_SECONDARY_COLOR_NV = 0x852D;  // from NV_register_combiners

	/**
	 * Accepted by the &lt;genMode&gt; parameter of PathColorGenNV and
	 * PathTexGenNV:
	 */
	int GL_PATH_OBJECT_BOUNDING_BOX_NV = 0x908A;

	/**
	 * Accepted by the &lt;coverMode&gt; parameter of CoverFillPathNV and
	 * CoverFillPathInstancedNV:
	 */
	int GL_CONVEX_HULL_NV  = 0x908B,
		GL_BOUNDING_BOX_NV = 0x908D;

	/**
	 * Accepted by the &lt;transformType&gt; parameter of
	 * StencilFillPathInstancedNV, StencilStrokePathInstancedNV,
	 * CoverFillPathInstancedNV, and CoverStrokePathInstancedNV:
	 */
	int GL_TRANSLATE_X_NV         = 0x908E,
		GL_TRANSLATE_Y_NV         = 0x908F,
		GL_TRANSLATE_2D_NV        = 0x9090,
		GL_TRANSLATE_3D_NV        = 0x9091,
		GL_AFFINE_2D_NV           = 0x9092,
		GL_AFFINE_3D_NV           = 0x9094,
		GL_TRANSPOSE_AFFINE_2D_NV = 0x9096,
		GL_TRANSPOSE_AFFINE_3D_NV = 0x9098;

	/**
	 * Accepted by the &lt;type&gt; or &lt;pathNameType&gt; parameter of CallLists,
	 * StencilFillPathInstancedNV, StencilStrokePathInstancedNV,
	 * CoverFillPathInstancedNV, CoverStrokePathInstancedNV,
	 * GetPathMetricsNV, and GetPathSpacingNV:
	 */
	int GL_UTF8_NV  = 0x909A,
		GL_UTF16_NV = 0x909B;

	/** Accepted by the &lt;coverMode&gt; parameter of CoverFillPathInstancedNV: */
	int GL_BOUNDING_BOX_OF_BOUNDING_BOXES_NV = 0x909C;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetPathParameterfvNV and
	 * GetPathParameterivNV:
	 */
	int GL_PATH_COMMAND_COUNT_NV       = 0x909D,
		GL_PATH_COORD_COUNT_NV         = 0x909E,
		GL_PATH_DASH_ARRAY_COUNT_NV    = 0x909F,
		GL_PATH_COMPUTED_LENGTH_NV     = 0x90A0,
		GL_PATH_FILL_BOUNDING_BOX_NV   = 0x90A1,
		GL_PATH_STROKE_BOUNDING_BOX_NV = 0x90A2;

	/**
	 * Accepted by the &lt;value&gt; parameter of PathParameterfNV,
	 * PathParameterfvNV, PathParameteriNV, and PathParameterivNV
	 * when &lt;pname&gt; is one of PATH_END_CAPS_NV, PATH_INTIAL_END_CAP_NV,
	 * PATH_TERMINAL_END_CAP_NV, PATH_DASH_CAPS_NV, PATH_INITIAL_DASH_CAP_NV,
	 * and PATH_TERMINAL_DASH_CAP_NV:
	 */
	int GL_SQUARE_NV     = 0x90A3,
		GL_ROUND_NV      = 0x90A4,
		GL_TRIANGULAR_NV = 0x90A5;

	/**
	 * Accepted by the &lt;value&gt; parameter of PathParameterfNV,
	 * PathParameterfvNV, PathParameteriNV, and PathParameterivNV
	 * when &lt;pname&gt; is PATH_JOIN_STYLE_NV:
	 */
	int GL_BEVEL_NV          = 0x90A6,
		GL_MITER_REVERT_NV   = 0x90A7,
		GL_MITER_TRUNCATE_NV = 0x90A8;

	/**
	 * Accepted by the &lt;value&gt; parameter of PathParameterfNV,
	 * PathParameterfvNV, PathParameteriNV, and PathParameterivNV when
	 * &lt;pname&gt; is PATH_DASH_OFFSET_RESET_NV
	 */
	int GL_MOVE_TO_RESETS_NV    = 0x90B5,
		GL_MOVE_TO_CONTINUES_NV = 0x90B6;

	/** Accepted by the &lt;fontStyle&gt; parameter of PathStringNV: */
	int GL_BOLD_BIT_NV   = 0x01,
		GL_ITALIC_BIT_NV = 0x02;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetInteger64v, GetFloatv, and GetDoublev:
	 */
	int GL_PATH_ERROR_POSITION_NV = 0x90AB,

	GL_PATH_FOG_GEN_MODE_NV = 0x90AC,

	GL_PATH_STENCIL_FUNC_NV           = 0x90B7,
		GL_PATH_STENCIL_REF_NV        = 0x90B8,
		GL_PATH_STENCIL_VALUE_MASK_NV = 0x90B9,

	GL_PATH_STENCIL_DEPTH_OFFSET_FACTOR_NV    = 0x90BD,
		GL_PATH_STENCIL_DEPTH_OFFSET_UNITS_NV = 0x90BE,

	GL_PATH_COVER_DEPTH_FUNC_NV = 0x90BF;

	/**
	 * Accepted as a bit within the &lt;metricQueryMask&gt; parameter of
	 * GetPathMetricRangeNV or GetPathMetricsNV:
	 */

	int GL_GLYPH_WIDTH_BIT_NV                      = 0x01, // per-glyph metrics
		GL_GLYPH_HEIGHT_BIT_NV                     = 0x02,
		GL_GLYPH_HORIZONTAL_BEARING_X_BIT_NV       = 0x04,
		GL_GLYPH_HORIZONTAL_BEARING_Y_BIT_NV       = 0x08,
		GL_GLYPH_HORIZONTAL_BEARING_ADVANCE_BIT_NV = 0x10,
		GL_GLYPH_VERTICAL_BEARING_X_BIT_NV         = 0x20,
		GL_GLYPH_VERTICAL_BEARING_Y_BIT_NV         = 0x40,
		GL_GLYPH_VERTICAL_BEARING_ADVANCE_BIT_NV   = 0x80,
		GL_GLYPH_HAS_KERNING_NV                    = 0x100,
		GL_FONT_X_MIN_BOUNDS_NV                    = 0x00010000, // per-font face metrics
		GL_FONT_Y_MIN_BOUNDS_NV                    = 0x00020000,
		GL_FONT_X_MAX_BOUNDS_NV                    = 0x00040000,
		GL_FONT_Y_MAX_BOUNDS_NV                    = 0x00080000,
		GL_FONT_UNITS_PER_EM_NV                    = 0x00100000,
		GL_FONT_ASCENDER_NV                        = 0x00200000,
		GL_FONT_DESCENDER_NV                       = 0x00400000,
		GL_FONT_HEIGHT_NV                          = 0x00800000,
		GL_FONT_MAX_ADVANCE_WIDTH_NV               = 0x01000000,
		GL_FONT_MAX_ADVANCE_HEIGHT_NV              = 0x02000000,
		GL_FONT_UNDERLINE_POSITION_NV              = 0x04000000,
		GL_FONT_UNDERLINE_THICKNESS_NV             = 0x08000000,
		GL_FONT_HAS_KERNING_NV                     = 0x10000000;

	/** Accepted by the &lt;pathListMode&gt; parameter of GetPathSpacingNV: */
	int GL_ACCUM_ADJACENT_PAIRS_NV = 0x90AD,
		GL_ADJACENT_PAIRS_NV       = 0x90AE,
		GL_FIRST_TO_REST_NV        = 0x90AF;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetPathColorGenivNV,
	 * GetPathColorGenfvNV, GetPathTexGenivNV and GetPathTexGenfvNV:
	 */
	int GL_PATH_GEN_MODE_NV         = 0x90B0,
		GL_PATH_GEN_COEFF_NV        = 0x90B1,
		GL_PATH_GEN_COLOR_FORMAT_NV = 0x90B2,
		GL_PATH_GEN_COMPONENTS_NV   = 0x90B3;

	void glPathCommandsNV(@GLuint int path,
	                      @AutoSize("commands") @GLsizei int numCommands, @Const @GLubyte ByteBuffer commands,
	                      @AutoSize("coords") @GLsizei int numCoords, @GLenum int coordType,
	                      @Const @GLvoid ByteBuffer coords);

	void glPathCoordsNV(@GLuint int path,
	                    @AutoSize("coords") @GLsizei int numCoords, @GLenum int coordType,
	                    @Const @GLvoid ByteBuffer coords);

	void glPathSubCommandsNV(@GLuint int path,
	                         @GLsizei int commandStart, @GLsizei int commandsToDelete,
	                         @AutoSize("commands") @GLsizei int numCommands, @Const @GLubyte ByteBuffer commands,
	                         @AutoSize("coords") @GLsizei int numCoords, @GLenum int coordType,
	                         @Const @GLvoid ByteBuffer coords);

	void glPathSubCoordsNV(@GLuint int path,
	                       @GLsizei int coordStart,
	                       @AutoSize("coords") @GLsizei int numCoords, @GLenum int coordType,
	                       @Const @GLvoid ByteBuffer coords);

	void glPathStringNV(@GLuint int path, @GLenum int format,
	                    @AutoSize("pathString") @GLsizei int length, @Const @GLvoid ByteBuffer pathString);

	void glPathGlyphsNV(@GLuint int firstPathName,
	                    @GLenum int fontTarget,
	                    @NullTerminated @Const @GLvoid ByteBuffer fontName,
	                    @GLbitfield int fontStyle,
	                    @AutoSize(value = "charcodes", expression = " / GLChecks.calculateBytesPerCharCode(type)") @GLsizei int numGlyphs, @GLenum int type,
	                    @Const @GLvoid ByteBuffer charcodes,
	                    @GLenum int handleMissingGlyphs,
	                    @GLuint int pathParameterTemplate,
	                    float emScale);

	void glPathGlyphRangeNV(@GLuint int firstPathName,
	                        @GLenum int fontTarget,
	                        @NullTerminated @Const @GLvoid ByteBuffer fontName,
	                        @GLbitfield int fontStyle,
	                        @GLuint int firstGlyph,
	                        @GLsizei int numGlyphs,
	                        @GLenum int handleMissingGlyphs,
	                        @GLuint int pathParameterTemplate,
	                        float emScale);

	void glWeightPathsNV(@GLuint int resultPath,
	                     @AutoSize("paths") @GLsizei int numPaths,
	                     @Const @GLuint IntBuffer paths, @Check("paths.remaining()") @Const FloatBuffer weights);

	void glCopyPathNV(@GLuint int resultPath, @GLuint int srcPath);

	void glInterpolatePathsNV(@GLuint int resultPath,
	                          @GLuint int pathA, @GLuint int pathB,
	                          float weight);

	void glTransformPathNV(@GLuint int resultPath,
	                       @GLuint int srcPath,
	                       @GLenum int transformType,
	                       @Check(value = "GLChecks.calculateTransformPathValues(transformType)", canBeNull = true) @Const FloatBuffer transformValues);

	@StripPostfix("value")
	void glPathParameterivNV(@GLuint int path, @GLenum int pname, @Check("4") @Const IntBuffer value);

	void glPathParameteriNV(@GLuint int path, @GLenum int pname, int value);

	@StripPostfix("value")
	void glPathParameterfvNV(@GLuint int path, @GLenum int pname, @Check("4") @Const FloatBuffer value);

	void glPathParameterfNV(@GLuint int path, @GLenum int pname, float value);

	void glPathDashArrayNV(@GLuint int path,
	                       @AutoSize("dashArray") @GLsizei int dashCount, @Const FloatBuffer dashArray);

	// PATH NAME MANAGEMENT

	@GLuint
	int glGenPathsNV(@GLsizei int range);

	void glDeletePathsNV(@GLuint int path, @GLsizei int range);

	boolean glIsPathNV(@GLuint int path);

	// PATH STENCILING

	void glPathStencilFuncNV(@GLenum int func, int ref, @GLuint int mask);

	void glPathStencilDepthOffsetNV(float factor, int units);

	void glStencilFillPathNV(@GLuint int path,
	                         @GLenum int fillMode, @GLuint int mask);

	void glStencilStrokePathNV(@GLuint int path,
	                           int reference, @GLuint int mask);

	void glStencilFillPathInstancedNV(@AutoSize(value="paths", expression = " / GLChecks.calculateBytesPerPathName(pathNameType)") @GLsizei int numPaths,
	                                  @GLenum int pathNameType, @Const @GLvoid ByteBuffer paths,
	                                  @GLuint int pathBase,
	                                  @GLenum int fillMode, @GLuint int mask,
	                                  @GLenum int transformType,
	                                  @Check(value = "GLChecks.calculateTransformPathValues(transformType)", canBeNull = true) @Const FloatBuffer transformValues);

	void glStencilStrokePathInstancedNV(@AutoSize(value = "paths", expression = " / GLChecks.calculateBytesPerPathName(pathNameType)") @GLsizei int numPaths,
	                                    @GLenum int pathNameType, @Const @GLvoid ByteBuffer paths,
	                                    @GLuint int pathBase,
	                                    int reference, @GLuint int mask,
	                                    @GLenum int transformType,
	                                    @Check(value = "GLChecks.calculateTransformPathValues(transformType)", canBeNull = true) @Const FloatBuffer transformValues);

	// PATH COVERING

	void glPathCoverDepthFuncNV(@GLenum int zfunc);

	void glPathColorGenNV(@GLenum int color,
	                      @GLenum int genMode,
	                      @GLenum int colorFormat, @Check(value = "GLChecks.calculatePathColorGenCoeffsCount(genMode, colorFormat)", canBeNull = true) @Const FloatBuffer coeffs);

	void glPathTexGenNV(@GLenum int texCoordSet,
	                    @GLenum int genMode,
	                    @AutoSize(value="coeffs", expression="GLChecks.calculatePathTextGenCoeffsPerComponent(coeffs, genMode)", useExpression = true, canBeNull = true) int components, @Check(canBeNull = true) @Const FloatBuffer coeffs);

	void glPathFogGenNV(@GLenum int genMode);

	void glCoverFillPathNV(@GLuint int path, @GLenum int coverMode);

	void glCoverStrokePathNV(@GLuint int name, @GLenum int coverMode);

	void glCoverFillPathInstancedNV(@AutoSize(value = "paths", expression = " / GLChecks.calculateBytesPerPathName(pathNameType)") @GLsizei int numPaths,
	                                @GLenum int pathNameType, @Const @GLvoid ByteBuffer paths,
	                                @GLuint int pathBase,
	                                @GLenum int coverMode,
	                                @GLenum int transformType,
	                                @Check(value = "GLChecks.calculateTransformPathValues(transformType)", canBeNull = true) @Const FloatBuffer transformValues);

	void glCoverStrokePathInstancedNV(@AutoSize(value = "paths", expression = " / GLChecks.calculateBytesPerPathName(pathNameType)") @GLsizei int numPaths,
	                                  @GLenum int pathNameType, @Const @GLvoid ByteBuffer paths,
	                                  @GLuint int pathBase,
	                                  @GLenum int coverMode,
	                                  @GLenum int transformType,
	                                  @Check(value = "GLChecks.calculateTransformPathValues(transformType)", canBeNull = true) @Const FloatBuffer transformValues);

	// PATH QUERIES

	@StripPostfix("value")
	void glGetPathParameterivNV(@GLuint int name, @GLenum int param, @Check("4") @OutParameter IntBuffer value);

	@Alternate("glGetPathParameterivNV")
	@GLreturn("value")
	@StripPostfix(value = "value", hasPostfix = false)
	void glGetPathParameterivNV2(@GLuint int name, @GLenum int param, @OutParameter IntBuffer value);

	void glGetPathParameterfvNV(@GLuint int name, @GLenum int param, @Check("4") @OutParameter FloatBuffer value);

	@Alternate("glGetPathParameterfvNV")
	@GLreturn("value")
	@StripPostfix(value = "value", hasPostfix = false)
	void glGetPathParameterfvNV2(@GLuint int name, @GLenum int param, @OutParameter FloatBuffer value);

	void glGetPathCommandsNV(@GLuint int name, @Check @OutParameter @GLubyte ByteBuffer commands);

	void glGetPathCoordsNV(@GLuint int name, @Check @OutParameter FloatBuffer coords);

	void glGetPathDashArrayNV(@GLuint int name, @Check @OutParameter FloatBuffer dashArray);

	void glGetPathMetricsNV(@GLbitfield int metricQueryMask,
	                        @AutoSize(value = "paths", expression = " / GLChecks.calculateBytesPerPathName(pathNameType)") @GLsizei int numPaths,
	                        @GLenum int pathNameType, @Const @GLvoid ByteBuffer paths,
	                        @GLuint int pathBase,
	                        @GLsizei int stride,
	                        @Check("GLChecks.calculateMetricsSize(metricQueryMask, stride)") @OutParameter FloatBuffer metrics);

	void glGetPathMetricRangeNV(@GLbitfield int metricQueryMask,
	                            @GLuint int fistPathName,
	                            @GLsizei int numPaths,
	                            @GLsizei int stride,
	                            @Check("GLChecks.calculateMetricsSize(metricQueryMask, stride)") @OutParameter FloatBuffer metrics);

	@Code("\t\tint numPaths = paths.remaining() / GLChecks.calculateBytesPerPathName(pathNameType);")
	void glGetPathSpacingNV(@GLenum int pathListMode,
	                        @AutoSize(value = "paths", expression = "numPaths", useExpression = true) @GLsizei int numPaths,
	                        @GLenum int pathNameType, @Const @GLvoid ByteBuffer paths,
	                        @GLuint int pathBase,
	                        float advanceScale,
	                        float kerningScale,
	                        @GLenum int transformType,
	                        @Check("numPaths - 1") @OutParameter FloatBuffer returnedSpacing);

	@StripPostfix("value")
	void glGetPathColorGenivNV(@GLenum int color, @GLenum int pname, @Check("16") @OutParameter IntBuffer value);

	@Alternate("glGetPathColorGenivNV")
	@GLreturn("value")
	@StripPostfix(value = "value", hasPostfix = false)
	void glGetPathColorGenivNV2(@GLenum int color, @GLenum int pname, @OutParameter IntBuffer value);

	@StripPostfix("value")
	void glGetPathColorGenfvNV(@GLenum int color, @GLenum int pname, @Check("16") @OutParameter FloatBuffer value);

	@Alternate("glGetPathColorGenfvNV")
	@GLreturn("value")
	@StripPostfix(value = "value", hasPostfix = false)
	void glGetPathColorGenfvNV2(@GLenum int color, @GLenum int pname, @OutParameter FloatBuffer value);

	@StripPostfix("value")
	void glGetPathTexGenivNV(@GLenum int texCoordSet, @GLenum int pname, @Check("16") @OutParameter IntBuffer value);

	@Alternate("glGetPathTexGenivNV")
	@GLreturn("value")
	@StripPostfix(value = "value", hasPostfix = false)
	void glGetPathTexGenivNV2(@GLenum int texCoordSet, @GLenum int pname, @OutParameter IntBuffer value);

	@StripPostfix("value")
	void glGetPathTexGenfvNV(@GLenum int texCoordSet, @GLenum int pname, @Check("16") @OutParameter FloatBuffer value);

	@Alternate("glGetPathTexGenfvNV")
	@GLreturn("value")
	@StripPostfix(value = "value", hasPostfix = false)
	void glGetPathTexGenfvNV2(@GLenum int texCoordSet, @GLenum int pname, @OutParameter FloatBuffer value);

	boolean glIsPointInFillPathNV(@GLuint int path,
	                              @GLuint int mask, float x, float y);

	boolean glIsPointInStrokePathNV(@GLuint int path,
	                                float x, float y);

	float glGetPathLengthNV(@GLuint int path,
	                        @GLsizei int startSegment, @GLsizei int numSegments);

	boolean glPointAlongPathNV(@GLuint int path,
	                           @GLsizei int startSegment, @GLsizei int numSegments,
	                           float distance,
	                           @Check(value = "1", canBeNull = true) @OutParameter FloatBuffer x,
	                           @Check(value = "1", canBeNull = true) @OutParameter FloatBuffer y,
	                           @Check(value = "1", canBeNull = true) @OutParameter FloatBuffer tangentX,
	                           @Check(value = "1", canBeNull = true) @OutParameter FloatBuffer tangentY);

}