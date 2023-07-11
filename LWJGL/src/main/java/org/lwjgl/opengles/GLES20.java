/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class GLES20 {

	/**
	 * ClearBufferMask 
	 */
	public static final int GL_DEPTH_BUFFER_BIT = 0x100,
		GL_STENCIL_BUFFER_BIT = 0x400,
		GL_COLOR_BUFFER_BIT = 0x4000;

	/**
	 * Boolean 
	 */
	public static final int GL_FALSE = 0x0,
		GL_TRUE = 0x1;

	/**
	 * BeginMode 
	 */
	public static final int GL_POINTS = 0x0,
		GL_LINES = 0x1,
		GL_LINE_LOOP = 0x2,
		GL_LINE_STRIP = 0x3,
		GL_TRIANGLES = 0x4,
		GL_TRIANGLE_STRIP = 0x5,
		GL_TRIANGLE_FAN = 0x6;

	/**
	 * BlendingFactorDest 
	 */
	public static final int GL_ZERO = 0x0,
		GL_ONE = 0x1,
		GL_SRC_COLOR = 0x300,
		GL_ONE_MINUS_SRC_COLOR = 0x301,
		GL_SRC_ALPHA = 0x302,
		GL_ONE_MINUS_SRC_ALPHA = 0x303,
		GL_DST_ALPHA = 0x304,
		GL_ONE_MINUS_DST_ALPHA = 0x305;

	/**
	 * BlendingFactorSrc 
	 */
	public static final int GL_DST_COLOR = 0x306,
		GL_ONE_MINUS_DST_COLOR = 0x307,
		GL_SRC_ALPHA_SATURATE = 0x308;

	/**
	 * BlendEquationSeparate 
	 */
	public static final int GL_FUNC_ADD = 0x8006,
		GL_BLEND_EQUATION = 0x8009,
		GL_BLEND_EQUATION_RGB = 0x8009,
		GL_BLEND_EQUATION_ALPHA = 0x883D;

	/**
	 * BlendSubtract 
	 */
	public static final int GL_FUNC_SUBTRACT = 0x800A,
		GL_FUNC_REVERSE_SUBTRACT = 0x800B;

	/**
	 * Separate Blend Functions 
	 */
	public static final int GL_BLEND_DST_RGB = 0x80C8,
		GL_BLEND_SRC_RGB = 0x80C9,
		GL_BLEND_DST_ALPHA = 0x80CA,
		GL_BLEND_SRC_ALPHA = 0x80CB,
		GL_CONSTANT_COLOR = 0x8001,
		GL_ONE_MINUS_CONSTANT_COLOR = 0x8002,
		GL_CONSTANT_ALPHA = 0x8003,
		GL_ONE_MINUS_CONSTANT_ALPHA = 0x8004,
		GL_BLEND_COLOR = 0x8005;

	/**
	 * Buffer Objects 
	 */
	public static final int GL_ARRAY_BUFFER = 0x8892,
		GL_ELEMENT_ARRAY_BUFFER = 0x8893,
		GL_ARRAY_BUFFER_BINDING = 0x8894,
		GL_ELEMENT_ARRAY_BUFFER_BINDING = 0x8895,
		GL_STREAM_DRAW = 0x88E0,
		GL_STATIC_DRAW = 0x88E4,
		GL_DYNAMIC_DRAW = 0x88E8,
		GL_BUFFER_SIZE = 0x8764,
		GL_BUFFER_USAGE = 0x8765,
		GL_CURRENT_VERTEX_ATTRIB = 0x8626;

	/**
	 * CullFaceMode 
	 */
	public static final int GL_FRONT = 0x404,
		GL_BACK = 0x405,
		GL_FRONT_AND_BACK = 0x408;

	/**
	 * EnableCap 
	 */
	public static final int GL_TEXTURE_2D = 0xDE1,
		GL_CULL_FACE = 0xB44,
		GL_BLEND = 0xBE2,
		GL_DITHER = 0xBD0,
		GL_STENCIL_TEST = 0xB90,
		GL_DEPTH_TEST = 0xB71,
		GL_SCISSOR_TEST = 0xC11,
		GL_POLYGON_OFFSET_FILL = 0x8037,
		GL_SAMPLE_ALPHA_TO_COVERAGE = 0x809E,
		GL_SAMPLE_COVERAGE = 0x80A0;

	/**
	 * ErrorCode 
	 */
	public static final int GL_NO_ERROR = 0x0,
		GL_INVALID_ENUM = 0x500,
		GL_INVALID_VALUE = 0x501,
		GL_INVALID_OPERATION = 0x502,
		GL_OUT_OF_MEMORY = 0x505;

	/**
	 * FrontFaceDirection 
	 */
	public static final int GL_CW = 0x900,
		GL_CCW = 0x901;

	/**
	 * GetPName 
	 */
	public static final int GL_LINE_WIDTH = 0xB21,
		GL_ALIASED_POINT_SIZE_RANGE = 0x846D,
		GL_ALIASED_LINE_WIDTH_RANGE = 0x846E,
		GL_CULL_FACE_MODE = 0xB45,
		GL_FRONT_FACE = 0xB46,
		GL_DEPTH_RANGE = 0xB70,
		GL_DEPTH_WRITEMASK = 0xB72,
		GL_DEPTH_CLEAR_VALUE = 0xB73,
		GL_DEPTH_FUNC = 0xB74,
		GL_STENCIL_CLEAR_VALUE = 0xB91,
		GL_STENCIL_FUNC = 0xB92,
		GL_STENCIL_FAIL = 0xB94,
		GL_STENCIL_PASS_DEPTH_FAIL = 0xB95,
		GL_STENCIL_PASS_DEPTH_PASS = 0xB96,
		GL_STENCIL_REF = 0xB97,
		GL_STENCIL_VALUE_MASK = 0xB93,
		GL_STENCIL_WRITEMASK = 0xB98,
		GL_STENCIL_BACK_FUNC = 0x8800,
		GL_STENCIL_BACK_FAIL = 0x8801,
		GL_STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802,
		GL_STENCIL_BACK_PASS_DEPTH_PASS = 0x8803,
		GL_STENCIL_BACK_REF = 0x8CA3,
		GL_STENCIL_BACK_VALUE_MASK = 0x8CA4,
		GL_STENCIL_BACK_WRITEMASK = 0x8CA5,
		GL_VIEWPORT = 0xBA2,
		GL_SCISSOR_BOX = 0xC10,
		GL_COLOR_CLEAR_VALUE = 0xC22,
		GL_COLOR_WRITEMASK = 0xC23,
		GL_UNPACK_ALIGNMENT = 0xCF5,
		GL_PACK_ALIGNMENT = 0xD05,
		GL_MAX_TEXTURE_SIZE = 0xD33,
		GL_MAX_VIEWPORT_DIMS = 0xD3A,
		GL_SUBPIXEL_BITS = 0xD50,
		GL_RED_BITS = 0xD52,
		GL_GREEN_BITS = 0xD53,
		GL_BLUE_BITS = 0xD54,
		GL_ALPHA_BITS = 0xD55,
		GL_DEPTH_BITS = 0xD56,
		GL_STENCIL_BITS = 0xD57,
		GL_POLYGON_OFFSET_UNITS = 0x2A00,
		GL_POLYGON_OFFSET_FACTOR = 0x8038,
		GL_TEXTURE_BINDING_2D = 0x8069,
		GL_SAMPLE_BUFFERS = 0x80A8,
		GL_SAMPLES = 0x80A9,
		GL_SAMPLE_COVERAGE_VALUE = 0x80AA,
		GL_SAMPLE_COVERAGE_INVERT = 0x80AB;

	/**
	 * GetTextureParameter 
	 */
	public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS = 0x86A2,
		GL_COMPRESSED_TEXTURE_FORMATS = 0x86A3;

	/**
	 * HintMode 
	 */
	public static final int GL_DONT_CARE = 0x1100,
		GL_FASTEST = 0x1101,
		GL_NICEST = 0x1102;

	/**
	 * HintTarget 
	 */
	public static final int GL_GENERATE_MIPMAP_HINT = 0x8192;

	/**
	 * DataType 
	 */
	public static final int GL_BYTE = 0x1400,
		GL_UNSIGNED_BYTE = 0x1401,
		GL_SHORT = 0x1402,
		GL_UNSIGNED_SHORT = 0x1403,
		GL_INT = 0x1404,
		GL_UNSIGNED_INT = 0x1405,
		GL_FLOAT = 0x1406,
		GL_FIXED = 0x140C;

	/**
	 * PixelFormat 
	 */
	public static final int GL_DEPTH_COMPONENT = 0x1902,
		GL_ALPHA = 0x1906,
		GL_RGB = 0x1907,
		GL_RGBA = 0x1908,
		GL_LUMINANCE = 0x1909,
		GL_LUMINANCE_ALPHA = 0x190A;

	/**
	 * PixelType 
	 */
	public static final int GL_UNSIGNED_SHORT_4_4_4_4 = 0x8033,
		GL_UNSIGNED_SHORT_5_5_5_1 = 0x8034,
		GL_UNSIGNED_SHORT_5_6_5 = 0x8363;

	/**
	 * Shaders 
	 */
	public static final int GL_FRAGMENT_SHADER = 0x8B30,
		GL_VERTEX_SHADER = 0x8B31,
		GL_MAX_VERTEX_ATTRIBS = 0x8869,
		GL_MAX_VERTEX_UNIFORM_VECTORS = 0x8DFB,
		GL_MAX_VARYING_VECTORS = 0x8DFC,
		GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D,
		GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0x8B4C,
		GL_MAX_TEXTURE_IMAGE_UNITS = 0x8872,
		GL_MAX_FRAGMENT_UNIFORM_VECTORS = 0x8DFD,
		GL_SHADER_TYPE = 0x8B4F,
		GL_DELETE_STATUS = 0x8B80,
		GL_LINK_STATUS = 0x8B82,
		GL_VALIDATE_STATUS = 0x8B83,
		GL_ATTACHED_SHADERS = 0x8B85,
		GL_ACTIVE_UNIFORMS = 0x8B86,
		GL_ACTIVE_UNIFORM_MAX_LENGTH = 0x8B87,
		GL_ACTIVE_ATTRIBUTES = 0x8B89,
		GL_ACTIVE_ATTRIBUTE_MAX_LENGTH = 0x8B8A,
		GL_SHADING_LANGUAGE_VERSION = 0x8B8C,
		GL_CURRENT_PROGRAM = 0x8B8D;

	/**
	 * StencilFunction 
	 */
	public static final int GL_NEVER = 0x200,
		GL_LESS = 0x201,
		GL_EQUAL = 0x202,
		GL_LEQUAL = 0x203,
		GL_GREATER = 0x204,
		GL_NOTEQUAL = 0x205,
		GL_GEQUAL = 0x206,
		GL_ALWAYS = 0x207;

	/**
	 * StencilOp 
	 */
	public static final int GL_KEEP = 0x1E00,
		GL_REPLACE = 0x1E01,
		GL_INCR = 0x1E02,
		GL_DECR = 0x1E03,
		GL_INVERT = 0x150A,
		GL_INCR_WRAP = 0x8507,
		GL_DECR_WRAP = 0x8508;

	/**
	 * StringName 
	 */
	public static final int GL_VENDOR = 0x1F00,
		GL_RENDERER = 0x1F01,
		GL_VERSION = 0x1F02,
		GL_EXTENSIONS = 0x1F03;

	/**
	 * TextureMagFilter 
	 */
	public static final int GL_NEAREST = 0x2600,
		GL_LINEAR = 0x2601;

	/**
	 * TextureMinFilter 
	 */
	public static final int GL_NEAREST_MIPMAP_NEAREST = 0x2700,
		GL_LINEAR_MIPMAP_NEAREST = 0x2701,
		GL_NEAREST_MIPMAP_LINEAR = 0x2702,
		GL_LINEAR_MIPMAP_LINEAR = 0x2703;

	/**
	 * TextureParameterName 
	 */
	public static final int GL_TEXTURE_MAG_FILTER = 0x2800,
		GL_TEXTURE_MIN_FILTER = 0x2801,
		GL_TEXTURE_WRAP_S = 0x2802,
		GL_TEXTURE_WRAP_T = 0x2803;

	/**
	 * TextureTarget 
	 */
	public static final int GL_TEXTURE = 0x1702,
		GL_TEXTURE_CUBE_MAP = 0x8513,
		GL_TEXTURE_BINDING_CUBE_MAP = 0x8514,
		GL_TEXTURE_CUBE_MAP_POSITIVE_X = 0x8515,
		GL_TEXTURE_CUBE_MAP_NEGATIVE_X = 0x8516,
		GL_TEXTURE_CUBE_MAP_POSITIVE_Y = 0x8517,
		GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = 0x8518,
		GL_TEXTURE_CUBE_MAP_POSITIVE_Z = 0x8519,
		GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = 0x851A,
		GL_MAX_CUBE_MAP_TEXTURE_SIZE = 0x851C;

	/**
	 * TextureUnit 
	 */
	public static final int GL_TEXTURE0 = 0x84C0,
		GL_TEXTURE1 = 0x84C1,
		GL_TEXTURE2 = 0x84C2,
		GL_TEXTURE3 = 0x84C3,
		GL_TEXTURE4 = 0x84C4,
		GL_TEXTURE5 = 0x84C5,
		GL_TEXTURE6 = 0x84C6,
		GL_TEXTURE7 = 0x84C7,
		GL_TEXTURE8 = 0x84C8,
		GL_TEXTURE9 = 0x84C9,
		GL_TEXTURE10 = 0x84CA,
		GL_TEXTURE11 = 0x84CB,
		GL_TEXTURE12 = 0x84CC,
		GL_TEXTURE13 = 0x84CD,
		GL_TEXTURE14 = 0x84CE,
		GL_TEXTURE15 = 0x84CF,
		GL_TEXTURE16 = 0x84D0,
		GL_TEXTURE17 = 0x84D1,
		GL_TEXTURE18 = 0x84D2,
		GL_TEXTURE19 = 0x84D3,
		GL_TEXTURE20 = 0x84D4,
		GL_TEXTURE21 = 0x84D5,
		GL_TEXTURE22 = 0x84D6,
		GL_TEXTURE23 = 0x84D7,
		GL_TEXTURE24 = 0x84D8,
		GL_TEXTURE25 = 0x84D9,
		GL_TEXTURE26 = 0x84DA,
		GL_TEXTURE27 = 0x84DB,
		GL_TEXTURE28 = 0x84DC,
		GL_TEXTURE29 = 0x84DD,
		GL_TEXTURE30 = 0x84DE,
		GL_TEXTURE31 = 0x84DF,
		GL_ACTIVE_TEXTURE = 0x84E0;

	/**
	 * TextureWrapMode 
	 */
	public static final int GL_REPEAT = 0x2901,
		GL_CLAMP_TO_EDGE = 0x812F,
		GL_MIRRORED_REPEAT = 0x8370;

	/**
	 * Uniform Types 
	 */
	public static final int GL_FLOAT_VEC2 = 0x8B50,
		GL_FLOAT_VEC3 = 0x8B51,
		GL_FLOAT_VEC4 = 0x8B52,
		GL_INT_VEC2 = 0x8B53,
		GL_INT_VEC3 = 0x8B54,
		GL_INT_VEC4 = 0x8B55,
		GL_BOOL = 0x8B56,
		GL_BOOL_VEC2 = 0x8B57,
		GL_BOOL_VEC3 = 0x8B58,
		GL_BOOL_VEC4 = 0x8B59,
		GL_FLOAT_MAT2 = 0x8B5A,
		GL_FLOAT_MAT3 = 0x8B5B,
		GL_FLOAT_MAT4 = 0x8B5C,
		GL_SAMPLER_2D = 0x8B5E,
		GL_SAMPLER_CUBE = 0x8B60;

	/**
	 * Vertex Arrays 
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED = 0x8622,
		GL_VERTEX_ATTRIB_ARRAY_SIZE = 0x8623,
		GL_VERTEX_ATTRIB_ARRAY_STRIDE = 0x8624,
		GL_VERTEX_ATTRIB_ARRAY_TYPE = 0x8625,
		GL_VERTEX_ATTRIB_ARRAY_NORMALIZED = 0x886A,
		GL_VERTEX_ATTRIB_ARRAY_POINTER = 0x8645,
		GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 0x889F;

	/**
	 * Read Format 
	 */
	public static final int GL_IMPLEMENTATION_COLOR_READ_TYPE = 0x8B9A,
		GL_IMPLEMENTATION_COLOR_READ_FORMAT = 0x8B9B;

	/**
	 * Shader Source 
	 */
	public static final int GL_COMPILE_STATUS = 0x8B81,
		GL_INFO_LOG_LENGTH = 0x8B84,
		GL_SHADER_SOURCE_LENGTH = 0x8B88,
		GL_SHADER_COMPILER = 0x8DFA;

	/**
	 * Shader Binary 
	 */
	public static final int GL_SHADER_BINARY_FORMATS = 0x8DF8,
		GL_NUM_SHADER_BINARY_FORMATS = 0x8DF9;

	/**
	 * Shader Precision-Specified Types 
	 */
	public static final int GL_LOW_FLOAT = 0x8DF0,
		GL_MEDIUM_FLOAT = 0x8DF1,
		GL_HIGH_FLOAT = 0x8DF2,
		GL_LOW_INT = 0x8DF3,
		GL_MEDIUM_INT = 0x8DF4,
		GL_HIGH_INT = 0x8DF5;

	/**
	 * Framebuffer Object. 
	 */
	public static final int GL_FRAMEBUFFER = 0x8D40,
		GL_RENDERBUFFER = 0x8D41,
		GL_RGBA4 = 0x8056,
		GL_RGB5_A1 = 0x8057,
		GL_RGB565 = 0x8D62,
		GL_DEPTH_COMPONENT16 = 0x81A5,
		GL_STENCIL_INDEX = 0x1901,
		GL_STENCIL_INDEX8 = 0x8D48,
		GL_RENDERBUFFER_WIDTH = 0x8D42,
		GL_RENDERBUFFER_HEIGHT = 0x8D43,
		GL_RENDERBUFFER_INTERNAL_FORMAT = 0x8D44,
		GL_RENDERBUFFER_RED_SIZE = 0x8D50,
		GL_RENDERBUFFER_GREEN_SIZE = 0x8D51,
		GL_RENDERBUFFER_BLUE_SIZE = 0x8D52,
		GL_RENDERBUFFER_ALPHA_SIZE = 0x8D53,
		GL_RENDERBUFFER_DEPTH_SIZE = 0x8D54,
		GL_RENDERBUFFER_STENCIL_SIZE = 0x8D55,
		GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0x8CD0,
		GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0x8CD1,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0x8CD2,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0x8CD3,
		GL_COLOR_ATTACHMENT0 = 0x8CE0,
		GL_DEPTH_ATTACHMENT = 0x8D00,
		GL_STENCIL_ATTACHMENT = 0x8D20,
		GL_NONE = 0x0,
		GL_FRAMEBUFFER_COMPLETE = 0x8CD5,
		GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8CD6,
		GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8CD7,
		GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 0x8CD9,
		GL_FRAMEBUFFER_UNSUPPORTED = 0x8CDD,
		GL_FRAMEBUFFER_BINDING = 0x8CA6,
		GL_RENDERBUFFER_BINDING = 0x8CA7,
		GL_MAX_RENDERBUFFER_SIZE = 0x84E8,
		GL_INVALID_FRAMEBUFFER_OPERATION = 0x506;

	private GLES20() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glActiveTexture(int texture) {
		nglActiveTexture(texture);
	}
	static native void nglActiveTexture(int texture);

	public static void glAttachShader(int program, int shader) {
		nglAttachShader(program, shader);
	}
	static native void nglAttachShader(int program, int shader);

	public static void glBindAttribLocation(int program, int index, ByteBuffer name) {
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		nglBindAttribLocation(program, index, MemoryUtil.getAddress(name));
	}
	static native void nglBindAttribLocation(int program, int index, long name);

	/** Overloads glBindAttribLocation. */
	public static void glBindAttribLocation(int program, int index, CharSequence name) {
		nglBindAttribLocation(program, index, APIUtil.getBufferNT(name));
	}

	public static void glBindBuffer(int target, int buffer) {
		StateTracker.bindBuffer(target, buffer);
		nglBindBuffer(target, buffer);
	}
	static native void nglBindBuffer(int target, int buffer);

	public static void glBindFramebuffer(int target, int framebuffer) {
		nglBindFramebuffer(target, framebuffer);
	}
	static native void nglBindFramebuffer(int target, int framebuffer);

	public static void glBindRenderbuffer(int target, int renderbuffer) {
		nglBindRenderbuffer(target, renderbuffer);
	}
	static native void nglBindRenderbuffer(int target, int renderbuffer);

	public static void glBindTexture(int target, int texture) {
		nglBindTexture(target, texture);
	}
	static native void nglBindTexture(int target, int texture);

	public static void glBlendColor(float red, float green, float blue, float alpha) {
		nglBlendColor(red, green, blue, alpha);
	}
	static native void nglBlendColor(float red, float green, float blue, float alpha);

	public static void glBlendEquation(int mode) {
		nglBlendEquation(mode);
	}
	static native void nglBlendEquation(int mode);

	public static void glBlendEquationSeparate(int modeRGB, int modeAlpha) {
		nglBlendEquationSeparate(modeRGB, modeAlpha);
	}
	static native void nglBlendEquationSeparate(int modeRGB, int modeAlpha);

	public static void glBlendFunc(int sfactor, int dfactor) {
		nglBlendFunc(sfactor, dfactor);
	}
	static native void nglBlendFunc(int sfactor, int dfactor);

	public static void glBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
		nglBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
	}
	static native void nglBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha);

	public static void glBufferData(int target, long data_size, int usage) {
		nglBufferData(target, data_size, 0L, usage);
	}
	public static void glBufferData(int target, ByteBuffer data, int usage) {
		BufferChecks.checkDirect(data);
		nglBufferData(target, data.remaining(), MemoryUtil.getAddress(data), usage);
	}
	public static void glBufferData(int target, FloatBuffer data, int usage) {
		BufferChecks.checkDirect(data);
		nglBufferData(target, (data.remaining() << 2), MemoryUtil.getAddress(data), usage);
	}
	public static void glBufferData(int target, IntBuffer data, int usage) {
		BufferChecks.checkDirect(data);
		nglBufferData(target, (data.remaining() << 2), MemoryUtil.getAddress(data), usage);
	}
	public static void glBufferData(int target, ShortBuffer data, int usage) {
		BufferChecks.checkDirect(data);
		nglBufferData(target, (data.remaining() << 1), MemoryUtil.getAddress(data), usage);
	}
	static native void nglBufferData(int target, long data_size, long data, int usage);

	public static void glBufferSubData(int target, long offset, ByteBuffer data) {
		BufferChecks.checkDirect(data);
		nglBufferSubData(target, offset, data.remaining(), MemoryUtil.getAddress(data));
	}
	public static void glBufferSubData(int target, long offset, FloatBuffer data) {
		BufferChecks.checkDirect(data);
		nglBufferSubData(target, offset, (data.remaining() << 2), MemoryUtil.getAddress(data));
	}
	public static void glBufferSubData(int target, long offset, IntBuffer data) {
		BufferChecks.checkDirect(data);
		nglBufferSubData(target, offset, (data.remaining() << 2), MemoryUtil.getAddress(data));
	}
	public static void glBufferSubData(int target, long offset, ShortBuffer data) {
		BufferChecks.checkDirect(data);
		nglBufferSubData(target, offset, (data.remaining() << 1), MemoryUtil.getAddress(data));
	}
	static native void nglBufferSubData(int target, long offset, long data_size, long data);

	public static int glCheckFramebufferStatus(int target) {
		int __result = nglCheckFramebufferStatus(target);
		return __result;
	}
	static native int nglCheckFramebufferStatus(int target);

	public static void glClear(int mask) {
		nglClear(mask);
	}
	static native void nglClear(int mask);

	public static void glClearColor(float red, float green, float blue, float alpha) {
		nglClearColor(red, green, blue, alpha);
	}
	static native void nglClearColor(float red, float green, float blue, float alpha);

	public static void glClearDepthf(float depth) {
		nglClearDepthf(depth);
	}
	static native void nglClearDepthf(float depth);

	public static void glClearStencil(int s) {
		nglClearStencil(s);
	}
	static native void nglClearStencil(int s);

	public static void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
		nglColorMask(red, green, blue, alpha);
	}
	static native void nglColorMask(boolean red, boolean green, boolean blue, boolean alpha);

	public static void glCompileShader(int shader) {
		nglCompileShader(shader);
	}
	static native void nglCompileShader(int shader);

	public static void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, ByteBuffer data) {
		BufferChecks.checkDirect(data);
		nglCompressedTexImage2D(target, level, internalformat, width, height, border, data.remaining(), MemoryUtil.getAddress(data));
	}
	static native void nglCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int data_imageSize, long data);

	public static void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, ByteBuffer data) {
		BufferChecks.checkDirect(data);
		nglCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, data.remaining(), MemoryUtil.getAddress(data));
	}
	static native void nglCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int data_imageSize, long data);

	public static void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border) {
		nglCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
	}
	static native void nglCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border);

	public static void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
		nglCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
	}
	static native void nglCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height);

	public static int glCreateProgram() {
		int __result = nglCreateProgram();
		return __result;
	}
	static native int nglCreateProgram();

	public static int glCreateShader(int type) {
		int __result = nglCreateShader(type);
		return __result;
	}
	static native int nglCreateShader(int type);

	public static void glCullFace(int mode) {
		nglCullFace(mode);
	}
	static native void nglCullFace(int mode);

	public static void glDeleteBuffers(IntBuffer buffers) {
		BufferChecks.checkDirect(buffers);
		nglDeleteBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers));
	}
	static native void nglDeleteBuffers(int buffers_n, long buffers);

	/** Overloads glDeleteBuffers. */
	public static void glDeleteBuffers(int buffer) {
		nglDeleteBuffers(1, APIUtil.getInt(buffer));
	}

	public static void glDeleteFramebuffers(IntBuffer framebuffers) {
		BufferChecks.checkDirect(framebuffers);
		nglDeleteFramebuffers(framebuffers.remaining(), MemoryUtil.getAddress(framebuffers));
	}
	static native void nglDeleteFramebuffers(int framebuffers_n, long framebuffers);

	/** Overloads glDeleteFramebuffers. */
	public static void glDeleteFramebuffers(int framebuffer) {
		nglDeleteFramebuffers(1, APIUtil.getInt(framebuffer));
	}

	public static void glDeleteProgram(int program) {
		nglDeleteProgram(program);
	}
	static native void nglDeleteProgram(int program);

	public static void glDeleteRenderbuffers(IntBuffer renderbuffers) {
		BufferChecks.checkDirect(renderbuffers);
		nglDeleteRenderbuffers(renderbuffers.remaining(), MemoryUtil.getAddress(renderbuffers));
	}
	static native void nglDeleteRenderbuffers(int renderbuffers_n, long renderbuffers);

	/** Overloads glDeleteRenderbuffers. */
	public static void glDeleteRenderbuffers(int renderbuffer) {
		nglDeleteRenderbuffers(1, APIUtil.getInt(renderbuffer));
	}

	public static void glDeleteShader(int shader) {
		nglDeleteShader(shader);
	}
	static native void nglDeleteShader(int shader);

	public static void glDeleteTextures(IntBuffer textures) {
		BufferChecks.checkDirect(textures);
		nglDeleteTextures(textures.remaining(), MemoryUtil.getAddress(textures));
	}
	static native void nglDeleteTextures(int textures_n, long textures);

	/** Overloads glDeleteTextures. */
	public static void glDeleteTextures(int texture) {
		nglDeleteTextures(1, APIUtil.getInt(texture));
	}

	public static void glDepthFunc(int func) {
		nglDepthFunc(func);
	}
	static native void nglDepthFunc(int func);

	public static void glDepthMask(boolean flag) {
		nglDepthMask(flag);
	}
	static native void nglDepthMask(boolean flag);

	public static void glDepthRangef(float zNear, float zFar) {
		nglDepthRangef(zNear, zFar);
	}
	static native void nglDepthRangef(float zNear, float zFar);

	public static void glDetachShader(int program, int shader) {
		nglDetachShader(program, shader);
	}
	static native void nglDetachShader(int program, int shader);

	public static void glDisable(int cap) {
		nglDisable(cap);
	}
	static native void nglDisable(int cap);

	public static void glDisableVertexAttribArray(int index) {
		nglDisableVertexAttribArray(index);
	}
	static native void nglDisableVertexAttribArray(int index);

	public static void glDrawArrays(int mode, int first, int count) {
		nglDrawArrays(mode, first, count);
	}
	static native void nglDrawArrays(int mode, int first, int count);

	public static void glDrawElements(int mode, ByteBuffer indices) {
		GLChecks.ensureElementVBOdisabled();
		BufferChecks.checkDirect(indices);
		nglDrawElements(mode, indices.remaining(), GLES20.GL_UNSIGNED_BYTE, MemoryUtil.getAddress(indices));
	}
	public static void glDrawElements(int mode, IntBuffer indices) {
		GLChecks.ensureElementVBOdisabled();
		BufferChecks.checkDirect(indices);
		nglDrawElements(mode, indices.remaining(), GLES20.GL_UNSIGNED_INT, MemoryUtil.getAddress(indices));
	}
	public static void glDrawElements(int mode, ShortBuffer indices) {
		GLChecks.ensureElementVBOdisabled();
		BufferChecks.checkDirect(indices);
		nglDrawElements(mode, indices.remaining(), GLES20.GL_UNSIGNED_SHORT, MemoryUtil.getAddress(indices));
	}
	static native void nglDrawElements(int mode, int indices_count, int type, long indices);
	public static void glDrawElements(int mode, int indices_count, int type, long indices_buffer_offset) {
		GLChecks.ensureElementVBOenabled();
		nglDrawElementsBO(mode, indices_count, type, indices_buffer_offset);
	}
	static native void nglDrawElementsBO(int mode, int indices_count, int type, long indices_buffer_offset);

	public static void glEnable(int cap) {
		nglEnable(cap);
	}
	static native void nglEnable(int cap);

	public static void glEnableVertexAttribArray(int index) {
		nglEnableVertexAttribArray(index);
	}
	static native void nglEnableVertexAttribArray(int index);

	public static void glFinish() {
		nglFinish();
	}
	static native void nglFinish();

	public static void glFlush() {
		nglFlush();
	}
	static native void nglFlush();

	public static void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
		nglFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer);
	}
	static native void nglFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer);

	public static void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
		nglFramebufferTexture2D(target, attachment, textarget, texture, level);
	}
	static native void nglFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level);

	public static void glFrontFace(int mode) {
		nglFrontFace(mode);
	}
	static native void nglFrontFace(int mode);

	public static void glGenBuffers(IntBuffer buffers) {
		BufferChecks.checkDirect(buffers);
		nglGenBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers));
	}
	static native void nglGenBuffers(int buffers_n, long buffers);

	/** Overloads glGenBuffers. */
	public static int glGenBuffers() {
		IntBuffer buffers = APIUtil.getBufferInt();
		nglGenBuffers(1, MemoryUtil.getAddress(buffers));
		return buffers.get(0);
	}

	public static void glGenerateMipmap(int target) {
		nglGenerateMipmap(target);
	}
	static native void nglGenerateMipmap(int target);

	public static void glGenFramebuffers(IntBuffer framebuffers) {
		BufferChecks.checkDirect(framebuffers);
		nglGenFramebuffers(framebuffers.remaining(), MemoryUtil.getAddress(framebuffers));
	}
	static native void nglGenFramebuffers(int framebuffers_n, long framebuffers);

	/** Overloads glGenFramebuffers. */
	public static int glGenFramebuffers() {
		IntBuffer framebuffers = APIUtil.getBufferInt();
		nglGenFramebuffers(1, MemoryUtil.getAddress(framebuffers));
		return framebuffers.get(0);
	}

	public static void glGenRenderbuffers(IntBuffer renderbuffers) {
		BufferChecks.checkDirect(renderbuffers);
		nglGenRenderbuffers(renderbuffers.remaining(), MemoryUtil.getAddress(renderbuffers));
	}
	static native void nglGenRenderbuffers(int renderbuffers_n, long renderbuffers);

	/** Overloads glGenRenderbuffers. */
	public static int glGenRenderbuffers() {
		IntBuffer renderbuffers = APIUtil.getBufferInt();
		nglGenRenderbuffers(1, MemoryUtil.getAddress(renderbuffers));
		return renderbuffers.get(0);
	}

	public static void glGenTextures(IntBuffer textures) {
		BufferChecks.checkDirect(textures);
		nglGenTextures(textures.remaining(), MemoryUtil.getAddress(textures));
	}
	static native void nglGenTextures(int textures_n, long textures);

	/** Overloads glGenTextures. */
	public static int glGenTextures() {
		IntBuffer textures = APIUtil.getBufferInt();
		nglGenTextures(1, MemoryUtil.getAddress(textures));
		return textures.get(0);
	}

	public static void glGetActiveAttrib(int program, int index, IntBuffer length, IntBuffer size, IntBuffer type, ByteBuffer name) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkBuffer(size, 1);
		BufferChecks.checkBuffer(type, 1);
		BufferChecks.checkDirect(name);
		nglGetActiveAttrib(program, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name));
	}
	static native void nglGetActiveAttrib(int program, int index, int name_bufsize, long length, long size, long type, long name);

	/**
	 * Overloads glGetActiveAttrib.
	 * <p>
	 * Overloads glGetActiveAttrib. This version returns both size and type in the sizeType buffer (at .position() and .position() + 1). 
	 */
	public static String glGetActiveAttrib(int program, int index, int bufsize, IntBuffer sizeType) {
		BufferChecks.checkBuffer(sizeType, 2);
		IntBuffer name_length = APIUtil.getLengths();
		ByteBuffer name = APIUtil.getBufferByte(bufsize);
		nglGetActiveAttrib(program, index, bufsize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(sizeType), MemoryUtil.getAddress(sizeType, sizeType.position() + 1), MemoryUtil.getAddress(name));
		name.limit(name_length.get(0));
		return APIUtil.getString(name);
	}

	/**
	 * Overloads glGetActiveAttrib.
	 * <p>
	 * Overloads glGetActiveAttrib. This version returns only the attrib name. 
	 */
	public static String glGetActiveAttrib(int program, int index, int bufsize) {
		IntBuffer name_length = APIUtil.getLengths();
		ByteBuffer name = APIUtil.getBufferByte(bufsize);
		nglGetActiveAttrib(program, index, bufsize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress0(APIUtil.getBufferInt()), MemoryUtil.getAddress(APIUtil.getBufferInt(), 1), MemoryUtil.getAddress(name));
		name.limit(name_length.get(0));
		return APIUtil.getString(name);
	}

	/**
	 * Overloads glGetActiveAttrib.
	 * <p>
	 * Overloads glGetActiveAttrib. This version returns only the attrib size. 
	 */
	public static int glGetActiveAttribSize(int program, int index) {
		IntBuffer size = APIUtil.getBufferInt();
		nglGetActiveAttrib(program, index, 0, 0L, MemoryUtil.getAddress(size), MemoryUtil.getAddress(size, 1), APIUtil.getBufferByte0());
		return size.get(0);
	}

	/**
	 * Overloads glGetActiveAttrib.
	 * <p>
	 * Overloads glGetActiveAttrib. This version returns only the attrib type. 
	 */
	public static int glGetActiveAttribType(int program, int index) {
		IntBuffer type = APIUtil.getBufferInt();
		nglGetActiveAttrib(program, index, 0, 0L, MemoryUtil.getAddress(type, 1), MemoryUtil.getAddress(type), APIUtil.getBufferByte0());
		return type.get(0);
	}

	public static void glGetActiveUniform(int program, int index, IntBuffer length, IntBuffer size, IntBuffer type, ByteBuffer name) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkBuffer(size, 1);
		BufferChecks.checkBuffer(type, 1);
		BufferChecks.checkDirect(name);
		nglGetActiveUniform(program, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name));
	}
	static native void nglGetActiveUniform(int program, int index, int name_bufsize, long length, long size, long type, long name);

	/**
	 * Overloads glGetActiveUniform.
	 * <p>
	 * Overloads glGetActiveUniform. This version returns both size and type in the sizeType buffer (at .position() and .position() + 1). 
	 */
	public static String glGetActiveUniform(int program, int index, int bufsize, IntBuffer sizeType) {
		BufferChecks.checkBuffer(sizeType, 2);
		IntBuffer name_length = APIUtil.getLengths();
		ByteBuffer name = APIUtil.getBufferByte(bufsize);
		nglGetActiveUniform(program, index, bufsize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(sizeType), MemoryUtil.getAddress(sizeType, sizeType.position() + 1), MemoryUtil.getAddress(name));
		name.limit(name_length.get(0));
		return APIUtil.getString(name);
	}

	/**
	 * Overloads glGetActiveUniform.
	 * <p>
	 * Overloads glGetActiveUniformARB. This version returns only the uniform name. 
	 */
	public static String glGetActiveUniform(int program, int index, int bufsize) {
		IntBuffer name_length = APIUtil.getLengths();
		ByteBuffer name = APIUtil.getBufferByte(bufsize);
		nglGetActiveUniform(program, index, bufsize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress0(APIUtil.getBufferInt()), MemoryUtil.getAddress(APIUtil.getBufferInt(), 1), MemoryUtil.getAddress(name));
		name.limit(name_length.get(0));
		return APIUtil.getString(name);
	}

	/**
	 * Overloads glGetActiveUniform.
	 * <p>
	 * Overloads glGetActiveUniform. This version returns only the uniform size. 
	 */
	public static int glGetActiveUniformSize(int program, int index) {
		IntBuffer size = APIUtil.getBufferInt();
		nglGetActiveUniform(program, index, 0, 0L, MemoryUtil.getAddress(size), MemoryUtil.getAddress(size, 1), APIUtil.getBufferByte0());
		return size.get(0);
	}

	/**
	 * Overloads glGetActiveUniform.
	 * <p>
	 * Overloads glGetActiveUniform. This version returns only the uniform type. 
	 */
	public static int glGetActiveUniformType(int program, int index) {
		IntBuffer type = APIUtil.getBufferInt();
		nglGetActiveUniform(program, index, 0, 0L, MemoryUtil.getAddress(type, 1), MemoryUtil.getAddress(type), APIUtil.getBufferByte0());
		return type.get(0);
	}

	public static void glGetAttachedShaders(int program, IntBuffer count, IntBuffer shaders) {
		if (count != null)
			BufferChecks.checkBuffer(count, 1);
		BufferChecks.checkDirect(shaders);
		nglGetAttachedShaders(program, shaders.remaining(), MemoryUtil.getAddressSafe(count), MemoryUtil.getAddress(shaders));
	}
	static native void nglGetAttachedShaders(int program, int shaders_maxCount, long count, long shaders);

	public static int glGetAttribLocation(int program, ByteBuffer name) {
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetAttribLocation(program, MemoryUtil.getAddress(name));
		return __result;
	}
	static native int nglGetAttribLocation(int program, long name);

	/** Overloads glGetAttribLocation. */
	public static int glGetAttribLocation(int program, CharSequence name) {
		int __result = nglGetAttribLocation(program, APIUtil.getBufferNT(name));
		return __result;
	}

	public static void glGetBoolean(int pname, ByteBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetBooleanv(pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetBooleanv(int pname, long params);

	/** Overloads glGetBooleanv. */
	public static boolean glGetBoolean(int pname) {
		ByteBuffer params = APIUtil.getBufferByte(1);
		nglGetBooleanv(pname, MemoryUtil.getAddress(params));
		return params.get(0) == 1;
	}

	public static void glGetBufferParameter(int target, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetBufferParameteriv(target, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetBufferParameteriv(int target, int pname, long params);

	/**
	 * Overloads glGetBufferParameteriv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetBufferParameteri} instead. 
	 */
	@Deprecated
	public static int glGetBufferParameter(int target, int pname) {
		return GLES20.glGetBufferParameteri(target, pname);
	}

	/** Overloads glGetBufferParameteriv. */
	public static int glGetBufferParameteri(int target, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetBufferParameteriv(target, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static int glGetError() {
		int __result = nglGetError();
		return __result;
	}
	static native int nglGetError();

	public static void glGetFloat(int pname, FloatBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetFloatv(pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetFloatv(int pname, long params);

	/** Overloads glGetFloatv. */
	public static float glGetFloat(int pname) {
		FloatBuffer params = APIUtil.getBufferFloat();
		nglGetFloatv(pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGetFramebufferAttachmentParameter(int target, int attachment, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetFramebufferAttachmentParameteriv(target, attachment, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetFramebufferAttachmentParameteriv(int target, int attachment, int pname, long params);

	/**
	 * Overloads glGetFramebufferAttachmentParameteriv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetFramebufferAttachmentParameteri} instead. 
	 */
	@Deprecated
	public static int glGetFramebufferAttachmentParameter(int target, int attachment, int pname) {
		return GLES20.glGetFramebufferAttachmentParameteri(target, attachment, pname);
	}

	/** Overloads glGetFramebufferAttachmentParameteriv. */
	public static int glGetFramebufferAttachmentParameteri(int target, int attachment, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetFramebufferAttachmentParameteriv(target, attachment, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGetInteger(int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetIntegerv(pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetIntegerv(int pname, long params);

	/** Overloads glGetIntegerv. */
	public static int glGetInteger(int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetIntegerv(pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGetProgram(int program, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetProgramiv(program, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetProgramiv(int program, int pname, long params);

	/**
	 * Overloads glGetProgramiv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetProgrami} instead. 
	 */
	@Deprecated
	public static int glGetProgram(int program, int pname) {
		return GLES20.glGetProgrami(program, pname);
	}

	/** Overloads glGetProgramiv. */
	public static int glGetProgrami(int program, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetProgramiv(program, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGetProgramInfoLog(int program, IntBuffer length, ByteBuffer infoLog) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(infoLog);
		nglGetProgramInfoLog(program, infoLog.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(infoLog));
	}
	static native void nglGetProgramInfoLog(int program, int infoLog_bufsize, long length, long infoLog);

	/** Overloads glGetProgramInfoLog. */
	public static String glGetProgramInfoLog(int program, int bufsize) {
		IntBuffer infoLog_length = APIUtil.getLengths();
		ByteBuffer infoLog = APIUtil.getBufferByte(bufsize);
		nglGetProgramInfoLog(program, bufsize, MemoryUtil.getAddress0(infoLog_length), MemoryUtil.getAddress(infoLog));
		infoLog.limit(infoLog_length.get(0));
		return APIUtil.getString(infoLog);
	}

	public static void glGetRenderbufferParameter(int target, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetRenderbufferParameteriv(target, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetRenderbufferParameteriv(int target, int pname, long params);

	/**
	 * Overloads glGetRenderbufferParameteriv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetRenderbufferParameteri} instead. 
	 */
	@Deprecated
	public static int glGetRenderbufferParameter(int target, int pname) {
		return GLES20.glGetRenderbufferParameteri(target, pname);
	}

	/** Overloads glGetRenderbufferParameteriv. */
	public static int glGetRenderbufferParameteri(int target, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetRenderbufferParameteriv(target, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGetShader(int shader, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetShaderiv(shader, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetShaderiv(int shader, int pname, long params);

	/**
	 * Overloads glGetShaderiv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetShaderi} instead. 
	 */
	@Deprecated
	public static int glGetShader(int shader, int pname) {
		return GLES20.glGetShaderi(shader, pname);
	}

	/** Overloads glGetShaderiv. */
	public static int glGetShaderi(int shader, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetShaderiv(shader, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGetShaderInfoLog(int shader, IntBuffer length, ByteBuffer infoLog) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(infoLog);
		nglGetShaderInfoLog(shader, infoLog.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(infoLog));
	}
	static native void nglGetShaderInfoLog(int shader, int infoLog_bufsize, long length, long infoLog);

	/** Overloads glGetShaderInfoLog. */
	public static String glGetShaderInfoLog(int shader, int bufsize) {
		IntBuffer infoLog_length = APIUtil.getLengths();
		ByteBuffer infoLog = APIUtil.getBufferByte(bufsize);
		nglGetShaderInfoLog(shader, bufsize, MemoryUtil.getAddress0(infoLog_length), MemoryUtil.getAddress(infoLog));
		infoLog.limit(infoLog_length.get(0));
		return APIUtil.getString(infoLog);
	}

	public static void glGetShaderPrecisionFormat(int shadertype, int precisiontype, IntBuffer range, IntBuffer precision) {
		BufferChecks.checkBuffer(range, 2);
		BufferChecks.checkBuffer(precision, 1);
		nglGetShaderPrecisionFormat(shadertype, precisiontype, MemoryUtil.getAddress(range), MemoryUtil.getAddress(precision));
	}
	static native void nglGetShaderPrecisionFormat(int shadertype, int precisiontype, long range, long precision);

	public static void glGetShaderSource(int shader, IntBuffer length, ByteBuffer source) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(source);
		nglGetShaderSource(shader, source.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(source));
	}
	static native void nglGetShaderSource(int shader, int source_bufsize, long length, long source);

	/** Overloads glGetShaderSource. */
	public static String glGetShaderSource(int shader, int bufsize) {
		IntBuffer source_length = APIUtil.getLengths();
		ByteBuffer source = APIUtil.getBufferByte(bufsize);
		nglGetShaderSource(shader, bufsize, MemoryUtil.getAddress0(source_length), MemoryUtil.getAddress(source));
		source.limit(source_length.get(0));
		return APIUtil.getString(source);
	}

	public static String glGetString(int name) {
		String __result = nglGetString(name);
		return __result;
	}
	static native String nglGetString(int name);

	public static void glGetTexParameter(int target, int pname, FloatBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetTexParameterfv(target, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetTexParameterfv(int target, int pname, long params);

	/** Overloads glGetTexParameterfv. */
	public static float glGetTexParameterf(int target, int pname) {
		FloatBuffer params = APIUtil.getBufferFloat();
		nglGetTexParameterfv(target, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGetTexParameter(int target, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetTexParameteriv(target, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetTexParameteriv(int target, int pname, long params);

	/** Overloads glGetTexParameteriv. */
	public static int glGetTexParameteri(int target, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetTexParameteriv(target, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGetUniform(int program, int location, FloatBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetUniformfv(program, location, MemoryUtil.getAddress(params));
	}
	static native void nglGetUniformfv(int program, int location, long params);

	public static void glGetUniform(int program, int location, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetUniformiv(program, location, MemoryUtil.getAddress(params));
	}
	static native void nglGetUniformiv(int program, int location, long params);

	/**
	 *  Returns the location of the uniform with the specified name. The ByteBuffer should contain the uniform name as a
	 *  <b>null-terminated</b> string.
	 * <p>
	 *  @param program
	 *  @param name
	 */
	public static int glGetUniformLocation(int program, ByteBuffer name) {
		BufferChecks.checkBuffer(name, 1);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetUniformLocation(program, MemoryUtil.getAddress(name));
		return __result;
	}
	static native int nglGetUniformLocation(int program, long name);

	/** Overloads glGetUniformLocation. */
	public static int glGetUniformLocation(int program, CharSequence name) {
		int __result = nglGetUniformLocation(program, APIUtil.getBufferNT(name));
		return __result;
	}

	public static void glGetVertexAttrib(int index, int pname, FloatBuffer params) {
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribfv(index, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetVertexAttribfv(int index, int pname, long params);

	public static void glGetVertexAttrib(int index, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribiv(index, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetVertexAttribiv(int index, int pname, long params);

	public static ByteBuffer glGetVertexAttribPointer(int index, int pname, long result_size) {
		ByteBuffer __result = nglGetVertexAttribPointerv(index, pname, result_size);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglGetVertexAttribPointerv(int index, int pname, long result_size);

	public static void glHint(int target, int mode) {
		nglHint(target, mode);
	}
	static native void nglHint(int target, int mode);

	public static boolean glIsBuffer(int buffer) {
		boolean __result = nglIsBuffer(buffer);
		return __result;
	}
	static native boolean nglIsBuffer(int buffer);

	public static boolean glIsEnabled(int cap) {
		boolean __result = nglIsEnabled(cap);
		return __result;
	}
	static native boolean nglIsEnabled(int cap);

	public static boolean glIsFramebuffer(int framebuffer) {
		boolean __result = nglIsFramebuffer(framebuffer);
		return __result;
	}
	static native boolean nglIsFramebuffer(int framebuffer);

	public static boolean glIsProgram(int program) {
		boolean __result = nglIsProgram(program);
		return __result;
	}
	static native boolean nglIsProgram(int program);

	public static boolean glIsRenderbuffer(int renderbuffer) {
		boolean __result = nglIsRenderbuffer(renderbuffer);
		return __result;
	}
	static native boolean nglIsRenderbuffer(int renderbuffer);

	public static boolean glIsShader(int shader) {
		boolean __result = nglIsShader(shader);
		return __result;
	}
	static native boolean nglIsShader(int shader);

	public static boolean glIsTexture(int texture) {
		boolean __result = nglIsTexture(texture);
		return __result;
	}
	static native boolean nglIsTexture(int texture);

	public static void glLineWidth(float width) {
		nglLineWidth(width);
	}
	static native void nglLineWidth(float width);

	public static void glLinkProgram(int program) {
		nglLinkProgram(program);
	}
	static native void nglLinkProgram(int program);

	public static void glPixelStorei(int pname, int param) {
		nglPixelStorei(pname, param);
	}
	static native void nglPixelStorei(int pname, int param);

	public static void glPolygonOffset(float factor, float units) {
		nglPolygonOffset(factor, units);
	}
	static native void nglPolygonOffset(float factor, float units);

	public static void glReadPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglReadPixels(x, y, width, height, format, type, MemoryUtil.getAddress(pixels));
	}
	public static void glReadPixels(int x, int y, int width, int height, int format, int type, FloatBuffer pixels) {
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglReadPixels(x, y, width, height, format, type, MemoryUtil.getAddress(pixels));
	}
	public static void glReadPixels(int x, int y, int width, int height, int format, int type, IntBuffer pixels) {
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglReadPixels(x, y, width, height, format, type, MemoryUtil.getAddress(pixels));
	}
	public static void glReadPixels(int x, int y, int width, int height, int format, int type, ShortBuffer pixels) {
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglReadPixels(x, y, width, height, format, type, MemoryUtil.getAddress(pixels));
	}
	static native void nglReadPixels(int x, int y, int width, int height, int format, int type, long pixels);

	public static void glReleaseShaderCompiler() {
		nglReleaseShaderCompiler();
	}
	static native void nglReleaseShaderCompiler();

	public static void glRenderbufferStorage(int target, int internalformat, int width, int height) {
		nglRenderbufferStorage(target, internalformat, width, height);
	}
	static native void nglRenderbufferStorage(int target, int internalformat, int width, int height);

	public static void glSampleCoverage(float value, boolean invert) {
		nglSampleCoverage(value, invert);
	}
	static native void nglSampleCoverage(float value, boolean invert);

	public static void glScissor(int x, int y, int width, int height) {
		nglScissor(x, y, width, height);
	}
	static native void nglScissor(int x, int y, int width, int height);

	public static void glShaderBinary(IntBuffer shaders, int binaryformat, ByteBuffer binary) {
		BufferChecks.checkDirect(shaders);
		BufferChecks.checkDirect(binary);
		nglShaderBinary(shaders.remaining(), MemoryUtil.getAddress(shaders), binaryformat, MemoryUtil.getAddress(binary), binary.remaining());
	}
	static native void nglShaderBinary(int shaders_n, long shaders, int binaryformat, long binary, int binary_length);

	/**
	 *  glShaderSource allows multiple, optionally null-terminated, source strings to define a shader program.
	 *  <p/>
	 *  This method uses just a single string, that should NOT be null-terminated.
	 * <p>
	 *  @param shader
	 *  @param string
	 */
	public static void glShaderSource(int shader, ByteBuffer string) {
		BufferChecks.checkDirect(string);
		nglShaderSource(shader, 1, MemoryUtil.getAddress(string), string.remaining());
	}
	static native void nglShaderSource(int shader, int count, long string, int string_length);

	/** Overloads glShaderSource. */
	public static void glShaderSource(int shader, CharSequence string) {
		nglShaderSource(shader, 1, APIUtil.getBuffer(string), string.length());
	}

	/** Overloads glShaderSource. */
	public static void glShaderSource(int shader, CharSequence[] strings) {
		BufferChecks.checkArray(strings);
		nglShaderSource3(shader, strings.length, APIUtil.getBuffer(strings), APIUtil.getLengths(strings));
	}
	static native void nglShaderSource3(int shader, int count, long strings, long length);

	public static void glStencilFunc(int func, int ref, int mask) {
		nglStencilFunc(func, ref, mask);
	}
	static native void nglStencilFunc(int func, int ref, int mask);

	public static void glStencilFuncSeparate(int face, int func, int ref, int mask) {
		nglStencilFuncSeparate(face, func, ref, mask);
	}
	static native void nglStencilFuncSeparate(int face, int func, int ref, int mask);

	public static void glStencilMask(int mask) {
		nglStencilMask(mask);
	}
	static native void nglStencilMask(int mask);

	public static void glStencilMaskSeparate(int face, int mask) {
		nglStencilMaskSeparate(face, mask);
	}
	static native void nglStencilMaskSeparate(int face, int mask);

	public static void glStencilOp(int fail, int zfail, int zpass) {
		nglStencilOp(fail, zfail, zpass);
	}
	static native void nglStencilOp(int fail, int zfail, int zpass);

	public static void glStencilOpSeparate(int face, int fail, int zfail, int zpass) {
		nglStencilOpSeparate(face, fail, zfail, zpass);
	}
	static native void nglStencilOpSeparate(int face, int fail, int zfail, int zpass);

	public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglTexImage2D(target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels));
	}
	public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, FloatBuffer pixels) {
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglTexImage2D(target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels));
	}
	public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels) {
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglTexImage2D(target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels));
	}
	public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ShortBuffer pixels) {
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglTexImage2D(target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels));
	}
	static native void nglTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, long pixels);

	public static void glTexParameterf(int target, int pname, float param) {
		nglTexParameterf(target, pname, param);
	}
	static native void nglTexParameterf(int target, int pname, float param);

	public static void glTexParameter(int target, int pname, FloatBuffer param) {
		BufferChecks.checkBuffer(param, 4);
		nglTexParameterfv(target, pname, MemoryUtil.getAddress(param));
	}
	static native void nglTexParameterfv(int target, int pname, long param);

	public static void glTexParameteri(int target, int pname, int param) {
		nglTexParameteri(target, pname, param);
	}
	static native void nglTexParameteri(int target, int pname, int param);

	public static void glTexParameter(int target, int pname, IntBuffer param) {
		BufferChecks.checkBuffer(param, 4);
		nglTexParameteriv(target, pname, MemoryUtil.getAddress(param));
	}
	static native void nglTexParameteriv(int target, int pname, long param);

	public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels));
	}
	public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, FloatBuffer pixels) {
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels));
	}
	public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, IntBuffer pixels) {
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels));
	}
	public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ShortBuffer pixels) {
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels));
	}
	static native void nglTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels);

	public static void glUniform1f(int location, float x) {
		nglUniform1f(location, x);
	}
	static native void nglUniform1f(int location, float x);

	public static void glUniform1(int location, FloatBuffer v) {
		BufferChecks.checkDirect(v);
		nglUniform1fv(location, v.remaining(), MemoryUtil.getAddress(v));
	}
	static native void nglUniform1fv(int location, int v_count, long v);

	public static void glUniform1i(int location, int x) {
		nglUniform1i(location, x);
	}
	static native void nglUniform1i(int location, int x);

	public static void glUniform1(int location, IntBuffer v) {
		BufferChecks.checkDirect(v);
		nglUniform1iv(location, v.remaining(), MemoryUtil.getAddress(v));
	}
	static native void nglUniform1iv(int location, int v_count, long v);

	public static void glUniform2f(int location, float x, float y) {
		nglUniform2f(location, x, y);
	}
	static native void nglUniform2f(int location, float x, float y);

	public static void glUniform2(int location, FloatBuffer v) {
		BufferChecks.checkDirect(v);
		nglUniform2fv(location, v.remaining() >> 1, MemoryUtil.getAddress(v));
	}
	static native void nglUniform2fv(int location, int v_count, long v);

	public static void glUniform2i(int location, int x, int y) {
		nglUniform2i(location, x, y);
	}
	static native void nglUniform2i(int location, int x, int y);

	public static void glUniform2(int location, IntBuffer v) {
		BufferChecks.checkDirect(v);
		nglUniform2iv(location, v.remaining() >> 1, MemoryUtil.getAddress(v));
	}
	static native void nglUniform2iv(int location, int v_count, long v);

	public static void glUniform3f(int location, float x, float y, float z) {
		nglUniform3f(location, x, y, z);
	}
	static native void nglUniform3f(int location, float x, float y, float z);

	public static void glUniform3(int location, FloatBuffer v) {
		BufferChecks.checkDirect(v);
		nglUniform3fv(location, v.remaining() / 3, MemoryUtil.getAddress(v));
	}
	static native void nglUniform3fv(int location, int v_count, long v);

	public static void glUniform3i(int location, int x, int y, int z) {
		nglUniform3i(location, x, y, z);
	}
	static native void nglUniform3i(int location, int x, int y, int z);

	public static void glUniform3(int location, IntBuffer v) {
		BufferChecks.checkDirect(v);
		nglUniform3iv(location, v.remaining() / 3, MemoryUtil.getAddress(v));
	}
	static native void nglUniform3iv(int location, int v_count, long v);

	public static void glUniform4f(int location, float x, float y, float z, float w) {
		nglUniform4f(location, x, y, z, w);
	}
	static native void nglUniform4f(int location, float x, float y, float z, float w);

	public static void glUniform4(int location, FloatBuffer v) {
		BufferChecks.checkDirect(v);
		nglUniform4fv(location, v.remaining() >> 2, MemoryUtil.getAddress(v));
	}
	static native void nglUniform4fv(int location, int v_count, long v);

	public static void glUniform4i(int location, int x, int y, int z, int w) {
		nglUniform4i(location, x, y, z, w);
	}
	static native void nglUniform4i(int location, int x, int y, int z, int w);

	public static void glUniform4(int location, IntBuffer v) {
		BufferChecks.checkDirect(v);
		nglUniform4iv(location, v.remaining() >> 2, MemoryUtil.getAddress(v));
	}
	static native void nglUniform4iv(int location, int v_count, long v);

	public static void glUniformMatrix2(int location, boolean transpose, FloatBuffer matrices) {
		BufferChecks.checkDirect(matrices);
		nglUniformMatrix2fv(location, matrices.remaining() >> 2, transpose, MemoryUtil.getAddress(matrices));
	}
	static native void nglUniformMatrix2fv(int location, int matrices_count, boolean transpose, long matrices);

	public static void glUniformMatrix3(int location, boolean transpose, FloatBuffer matrices) {
		BufferChecks.checkDirect(matrices);
		nglUniformMatrix3fv(location, matrices.remaining() / (3 * 3), transpose, MemoryUtil.getAddress(matrices));
	}
	static native void nglUniformMatrix3fv(int location, int matrices_count, boolean transpose, long matrices);

	public static void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrices) {
		BufferChecks.checkDirect(matrices);
		nglUniformMatrix4fv(location, matrices.remaining() >> 4, transpose, MemoryUtil.getAddress(matrices));
	}
	static native void nglUniformMatrix4fv(int location, int matrices_count, boolean transpose, long matrices);

	public static void glUseProgram(int program) {
		nglUseProgram(program);
	}
	static native void nglUseProgram(int program);

	public static void glValidateProgram(int program) {
		nglValidateProgram(program);
	}
	static native void nglValidateProgram(int program);

	public static void glVertexAttrib1f(int indx, float x) {
		nglVertexAttrib1f(indx, x);
	}
	static native void nglVertexAttrib1f(int indx, float x);

	public static void glVertexAttrib1(int indx, FloatBuffer values) {
		BufferChecks.checkBuffer(values, 1);
		nglVertexAttrib1fv(indx, MemoryUtil.getAddress(values));
	}
	static native void nglVertexAttrib1fv(int indx, long values);

	public static void glVertexAttrib2f(int indx, float x, float y) {
		nglVertexAttrib2f(indx, x, y);
	}
	static native void nglVertexAttrib2f(int indx, float x, float y);

	public static void glVertexAttrib2(int indx, FloatBuffer values) {
		BufferChecks.checkBuffer(values, 2);
		nglVertexAttrib2fv(indx, MemoryUtil.getAddress(values));
	}
	static native void nglVertexAttrib2fv(int indx, long values);

	public static void glVertexAttrib3f(int indx, float x, float y, float z) {
		nglVertexAttrib3f(indx, x, y, z);
	}
	static native void nglVertexAttrib3f(int indx, float x, float y, float z);

	public static void glVertexAttrib3(int indx, FloatBuffer values) {
		BufferChecks.checkBuffer(values, 3);
		nglVertexAttrib3fv(indx, MemoryUtil.getAddress(values));
	}
	static native void nglVertexAttrib3fv(int indx, long values);

	public static void glVertexAttrib4f(int indx, float x, float y, float z, float w) {
		nglVertexAttrib4f(indx, x, y, z, w);
	}
	static native void nglVertexAttrib4f(int indx, float x, float y, float z, float w);

	public static void glVertexAttrib4(int indx, FloatBuffer values) {
		BufferChecks.checkBuffer(values, 4);
		nglVertexAttrib4fv(indx, MemoryUtil.getAddress(values));
	}
	static native void nglVertexAttrib4fv(int indx, long values);

	public static void glVertexAttribPointer(int index, int size, boolean normalized, int stride, FloatBuffer buffer) {
		GLChecks.ensureArrayVBOdisabled();
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getTracker().glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointer(index, size, GLES20.GL_FLOAT, normalized, stride, MemoryUtil.getAddress(buffer));
	}
	public static void glVertexAttribPointer(int index, int size, boolean unsigned, boolean normalized, int stride, ByteBuffer buffer) {
		GLChecks.ensureArrayVBOdisabled();
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getTracker().glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointer(index, size, unsigned ? GLES20.GL_UNSIGNED_BYTE : GLES20.GL_BYTE, normalized, stride, MemoryUtil.getAddress(buffer));
	}
	public static void glVertexAttribPointer(int index, int size, boolean unsigned, boolean normalized, int stride, IntBuffer buffer) {
		GLChecks.ensureArrayVBOdisabled();
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getTracker().glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointer(index, size, unsigned ? GLES20.GL_UNSIGNED_INT : GLES20.GL_INT, normalized, stride, MemoryUtil.getAddress(buffer));
	}
	public static void glVertexAttribPointer(int index, int size, boolean unsigned, boolean normalized, int stride, ShortBuffer buffer) {
		GLChecks.ensureArrayVBOdisabled();
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getTracker().glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointer(index, size, unsigned ? GLES20.GL_UNSIGNED_SHORT : GLES20.GL_SHORT, normalized, stride, MemoryUtil.getAddress(buffer));
	}
	static native void nglVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long buffer);
	public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long buffer_buffer_offset) {
		GLChecks.ensureArrayVBOenabled();
		nglVertexAttribPointerBO(index, size, type, normalized, stride, buffer_buffer_offset);
	}
	static native void nglVertexAttribPointerBO(int index, int size, int type, boolean normalized, int stride, long buffer_buffer_offset);

	public static void glViewport(int x, int y, int width, int height) {
		nglViewport(x, y, width, height);
	}
	static native void nglViewport(int x, int y, int width, int height);
}
