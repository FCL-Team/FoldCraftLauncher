/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class GLES30 {

	public static final int GL_READ_BUFFER = 0xC02,
		GL_UNPACK_ROW_LENGTH = 0xCF2,
		GL_UNPACK_SKIP_ROWS = 0xCF3,
		GL_UNPACK_SKIP_PIXELS = 0xCF4,
		GL_PACK_ROW_LENGTH = 0xD02,
		GL_PACK_SKIP_ROWS = 0xD03,
		GL_PACK_SKIP_PIXELS = 0xD04,
		GL_COLOR = 0x1800,
		GL_DEPTH = 0x1801,
		GL_STENCIL = 0x1802,
		GL_RED = 0x1903,
		GL_RGB8 = 0x8051,
		GL_RGBA8 = 0x8058,
		GL_RGB10_A2 = 0x8059,
		GL_TEXTURE_BINDING_3D = 0x806A,
		GL_PACK_SKIP_IMAGES = 0x806B,
		GL_PACK_IMAGE_HEIGHT = 0x806C,
		GL_UNPACK_SKIP_IMAGES = 0x806D,
		GL_UNPACK_IMAGE_HEIGHT = 0x806E,
		GL_TEXTURE_3D = 0x806F,
		GL_TEXTURE_WRAP_R = 0x8072,
		GL_MAX_3D_TEXTURE_SIZE = 0x8073,
		GL_UNSIGNED_INT_2_10_10_10_REV = 0x8368,
		GL_MAX_ELEMENTS_VERTICES = 0x80E8,
		GL_MAX_ELEMENTS_INDICES = 0x80E9,
		GL_TEXTURE_MIN_LOD = 0x813A,
		GL_TEXTURE_MAX_LOD = 0x813B,
		GL_TEXTURE_BASE_LEVEL = 0x813C,
		GL_TEXTURE_MAX_LEVEL = 0x813D,
		GL_MIN = 0x8007,
		GL_MAX = 0x8008,
		GL_DEPTH_COMPONENT24 = 0x81A6,
		GL_MAX_TEXTURE_LOD_BIAS = 0x84FD,
		GL_TEXTURE_COMPARE_MODE = 0x884C,
		GL_TEXTURE_COMPARE_FUNC = 0x884D,
		GL_CURRENT_QUERY = 0x8865,
		GL_QUERY_RESULT = 0x8866,
		GL_QUERY_RESULT_AVAILABLE = 0x8867,
		GL_BUFFER_MAPPED = 0x88BC,
		GL_BUFFER_MAP_POINTER = 0x88BD,
		GL_STREAM_READ = 0x88E1,
		GL_STREAM_COPY = 0x88E2,
		GL_STATIC_READ = 0x88E5,
		GL_STATIC_COPY = 0x88E6,
		GL_DYNAMIC_READ = 0x88E9,
		GL_DYNAMIC_COPY = 0x88EA,
		GL_MAX_DRAW_BUFFERS = 0x8824,
		GL_DRAW_BUFFER0 = 0x8825,
		GL_DRAW_BUFFER1 = 0x8826,
		GL_DRAW_BUFFER2 = 0x8827,
		GL_DRAW_BUFFER3 = 0x8828,
		GL_DRAW_BUFFER4 = 0x8829,
		GL_DRAW_BUFFER5 = 0x882A,
		GL_DRAW_BUFFER6 = 0x882B,
		GL_DRAW_BUFFER7 = 0x882C,
		GL_DRAW_BUFFER8 = 0x882D,
		GL_DRAW_BUFFER9 = 0x882E,
		GL_DRAW_BUFFER10 = 0x882F,
		GL_DRAW_BUFFER11 = 0x8830,
		GL_DRAW_BUFFER12 = 0x8831,
		GL_DRAW_BUFFER13 = 0x8832,
		GL_DRAW_BUFFER14 = 0x8833,
		GL_DRAW_BUFFER15 = 0x8834,
		GL_MAX_FRAGMENT_UNIFORM_COMPONENTS = 0x8B49,
		GL_MAX_VERTEX_UNIFORM_COMPONENTS = 0x8B4A,
		GL_SAMPLER_3D = 0x8B5F,
		GL_SAMPLER_2D_SHADOW = 0x8B62,
		GL_FRAGMENT_SHADER_DERIVATIVE_HINT = 0x8B8B,
		GL_PIXEL_PACK_BUFFER = 0x88EB,
		GL_PIXEL_UNPACK_BUFFER = 0x88EC,
		GL_PIXEL_PACK_BUFFER_BINDING = 0x88ED,
		GL_PIXEL_UNPACK_BUFFER_BINDING = 0x88EF,
		GL_FLOAT_MAT2x3 = 0x8B65,
		GL_FLOAT_MAT2x4 = 0x8B66,
		GL_FLOAT_MAT3x2 = 0x8B67,
		GL_FLOAT_MAT3x4 = 0x8B68,
		GL_FLOAT_MAT4x2 = 0x8B69,
		GL_FLOAT_MAT4x3 = 0x8B6A,
		GL_SRGB = 0x8C40,
		GL_SRGB8 = 0x8C41,
		GL_SRGB8_ALPHA8 = 0x8C43,
		GL_COMPARE_REF_TO_TEXTURE = 0x884E,
		GL_MAJOR_VERSION = 0x821B,
		GL_MINOR_VERSION = 0x821C,
		GL_NUM_EXTENSIONS = 0x821D,
		GL_RGBA32F = 0x8814,
		GL_RGB32F = 0x8815,
		GL_RGBA16F = 0x881A,
		GL_RGB16F = 0x881B,
		GL_VERTEX_ATTRIB_ARRAY_INTEGER = 0x88FD,
		GL_MAX_ARRAY_TEXTURE_LAYERS = 0x88FF,
		GL_MIN_PROGRAM_TEXEL_OFFSET = 0x8904,
		GL_MAX_PROGRAM_TEXEL_OFFSET = 0x8905,
		GL_MAX_VARYING_COMPONENTS = 0x8B4B,
		GL_TEXTURE_2D_ARRAY = 0x8C1A,
		GL_TEXTURE_BINDING_2D_ARRAY = 0x8C1D,
		GL_R11F_G11F_B10F = 0x8C3A,
		GL_UNSIGNED_INT_10F_11F_11F_REV = 0x8C3B,
		GL_RGB9_E5 = 0x8C3D,
		GL_UNSIGNED_INT_5_9_9_9_REV = 0x8C3E,
		GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH = 0x8C76,
		GL_TRANSFORM_FEEDBACK_BUFFER_MODE = 0x8C7F,
		GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS = 0x8C80,
		GL_TRANSFORM_FEEDBACK_VARYINGS = 0x8C83,
		GL_TRANSFORM_FEEDBACK_BUFFER_START = 0x8C84,
		GL_TRANSFORM_FEEDBACK_BUFFER_SIZE = 0x8C85,
		GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN = 0x8C88,
		GL_RASTERIZER_DISCARD = 0x8C89,
		GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS = 0x8C8A,
		GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS = 0x8C8B,
		GL_INTERLEAVED_ATTRIBS = 0x8C8C,
		GL_SEPARATE_ATTRIBS = 0x8C8D,
		GL_TRANSFORM_FEEDBACK_BUFFER = 0x8C8E,
		GL_TRANSFORM_FEEDBACK_BUFFER_BINDING = 0x8C8F,
		GL_RGBA32UI = 0x8D70,
		GL_RGB32UI = 0x8D71,
		GL_RGBA16UI = 0x8D76,
		GL_RGB16UI = 0x8D77,
		GL_RGBA8UI = 0x8D7C,
		GL_RGB8UI = 0x8D7D,
		GL_RGBA32I = 0x8D82,
		GL_RGB32I = 0x8D83,
		GL_RGBA16I = 0x8D88,
		GL_RGB16I = 0x8D89,
		GL_RGBA8I = 0x8D8E,
		GL_RGB8I = 0x8D8F,
		GL_RED_INTEGER = 0x8D94,
		GL_RGB_INTEGER = 0x8D98,
		GL_RGBA_INTEGER = 0x8D99,
		GL_SAMPLER_2D_ARRAY = 0x8DC1,
		GL_SAMPLER_2D_ARRAY_SHADOW = 0x8DC4,
		GL_SAMPLER_CUBE_SHADOW = 0x8DC5,
		GL_UNSIGNED_INT_VEC2 = 0x8DC6,
		GL_UNSIGNED_INT_VEC3 = 0x8DC7,
		GL_UNSIGNED_INT_VEC4 = 0x8DC8,
		GL_INT_SAMPLER_2D = 0x8DCA,
		GL_INT_SAMPLER_3D = 0x8DCB,
		GL_INT_SAMPLER_CUBE = 0x8DCC,
		GL_INT_SAMPLER_2D_ARRAY = 0x8DCF,
		GL_UNSIGNED_INT_SAMPLER_2D = 0x8DD2,
		GL_UNSIGNED_INT_SAMPLER_3D = 0x8DD3,
		GL_UNSIGNED_INT_SAMPLER_CUBE = 0x8DD4,
		GL_UNSIGNED_INT_SAMPLER_2D_ARRAY = 0x8DD7,
		GL_BUFFER_ACCESS_FLAGS = 0x911F,
		GL_BUFFER_MAP_LENGTH = 0x9120,
		GL_BUFFER_MAP_OFFSET = 0x9121,
		GL_DEPTH_COMPONENT32F = 0x8CAC,
		GL_DEPTH32F_STENCIL8 = 0x8CAD,
		GL_FLOAT_32_UNSIGNED_INT_24_8_REV = 0x8DAD,
		GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING = 0x8210,
		GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE = 0x8211,
		GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE = 0x8212,
		GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE = 0x8213,
		GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE = 0x8214,
		GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE = 0x8215,
		GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE = 0x8216,
		GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE = 0x8217,
		GL_FRAMEBUFFER_DEFAULT = 0x8218,
		GL_FRAMEBUFFER_UNDEFINED = 0x8219,
		GL_DEPTH_STENCIL_ATTACHMENT = 0x821A,
		GL_DEPTH_STENCIL = 0x84F9,
		GL_UNSIGNED_INT_24_8 = 0x84FA,
		GL_DEPTH24_STENCIL8 = 0x88F0,
		GL_UNSIGNED_NORMALIZED = 0x8C17,
		GL_DRAW_FRAMEBUFFER_BINDING = 0x8CA6,
		GL_READ_FRAMEBUFFER = 0x8CA8,
		GL_DRAW_FRAMEBUFFER = 0x8CA9,
		GL_READ_FRAMEBUFFER_BINDING = 0x8CAA,
		GL_RENDERBUFFER_SAMPLES = 0x8CAB,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 0x8CD4,
		GL_MAX_COLOR_ATTACHMENTS = 0x8CDF,
		GL_COLOR_ATTACHMENT1 = 0x8CE1,
		GL_COLOR_ATTACHMENT2 = 0x8CE2,
		GL_COLOR_ATTACHMENT3 = 0x8CE3,
		GL_COLOR_ATTACHMENT4 = 0x8CE4,
		GL_COLOR_ATTACHMENT5 = 0x8CE5,
		GL_COLOR_ATTACHMENT6 = 0x8CE6,
		GL_COLOR_ATTACHMENT7 = 0x8CE7,
		GL_COLOR_ATTACHMENT8 = 0x8CE8,
		GL_COLOR_ATTACHMENT9 = 0x8CE9,
		GL_COLOR_ATTACHMENT10 = 0x8CEA,
		GL_COLOR_ATTACHMENT11 = 0x8CEB,
		GL_COLOR_ATTACHMENT12 = 0x8CEC,
		GL_COLOR_ATTACHMENT13 = 0x8CED,
		GL_COLOR_ATTACHMENT14 = 0x8CEE,
		GL_COLOR_ATTACHMENT15 = 0x8CEF,
		GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 0x8D56,
		GL_MAX_SAMPLES = 0x8D57,
		GL_HALF_FLOAT = 0x140B,
		GL_MAP_READ_BIT = 0x1,
		GL_MAP_WRITE_BIT = 0x2,
		GL_MAP_INVALIDATE_RANGE_BIT = 0x4,
		GL_MAP_INVALIDATE_BUFFER_BIT = 0x8,
		GL_MAP_FLUSH_EXPLICIT_BIT = 0x10,
		GL_MAP_UNSYNCHRONIZED_BIT = 0x20,
		GL_RG = 0x8227,
		GL_RG_INTEGER = 0x8228,
		GL_R8 = 0x8229,
		GL_RG8 = 0x822B,
		GL_R16F = 0x822D,
		GL_R32F = 0x822E,
		GL_RG16F = 0x822F,
		GL_RG32F = 0x8230,
		GL_R8I = 0x8231,
		GL_R8UI = 0x8232,
		GL_R16I = 0x8233,
		GL_R16UI = 0x8234,
		GL_R32I = 0x8235,
		GL_R32UI = 0x8236,
		GL_RG8I = 0x8237,
		GL_RG8UI = 0x8238,
		GL_RG16I = 0x8239,
		GL_RG16UI = 0x823A,
		GL_RG32I = 0x823B,
		GL_RG32UI = 0x823C,
		GL_VERTEX_ARRAY_BINDING = 0x85B5,
		GL_R8_SNORM = 0x8F94,
		GL_RG8_SNORM = 0x8F95,
		GL_RGB8_SNORM = 0x8F96,
		GL_RGBA8_SNORM = 0x8F97,
		GL_SIGNED_NORMALIZED = 0x8F9C,
		GL_PRIMITIVE_RESTART_FIXED_INDEX = 0x8D69,
		GL_COPY_READ_BUFFER = 0x8F36,
		GL_COPY_WRITE_BUFFER = 0x8F37,
		GL_COPY_READ_BUFFER_BINDING = 0x8F36,
		GL_COPY_WRITE_BUFFER_BINDING = 0x8F37,
		GL_UNIFORM_BUFFER = 0x8A11,
		GL_UNIFORM_BUFFER_BINDING = 0x8A28,
		GL_UNIFORM_BUFFER_START = 0x8A29,
		GL_UNIFORM_BUFFER_SIZE = 0x8A2A,
		GL_MAX_VERTEX_UNIFORM_BLOCKS = 0x8A2B,
		GL_MAX_FRAGMENT_UNIFORM_BLOCKS = 0x8A2D,
		GL_MAX_COMBINED_UNIFORM_BLOCKS = 0x8A2E,
		GL_MAX_UNIFORM_BUFFER_BINDINGS = 0x8A2F,
		GL_MAX_UNIFORM_BLOCK_SIZE = 0x8A30,
		GL_MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS = 0x8A31,
		GL_MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS = 0x8A33,
		GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT = 0x8A34,
		GL_ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH = 0x8A35,
		GL_ACTIVE_UNIFORM_BLOCKS = 0x8A36,
		GL_UNIFORM_TYPE = 0x8A37,
		GL_UNIFORM_SIZE = 0x8A38,
		GL_UNIFORM_NAME_LENGTH = 0x8A39,
		GL_UNIFORM_BLOCK_INDEX = 0x8A3A,
		GL_UNIFORM_OFFSET = 0x8A3B,
		GL_UNIFORM_ARRAY_STRIDE = 0x8A3C,
		GL_UNIFORM_MATRIX_STRIDE = 0x8A3D,
		GL_UNIFORM_IS_ROW_MAJOR = 0x8A3E,
		GL_UNIFORM_BLOCK_BINDING = 0x8A3F,
		GL_UNIFORM_BLOCK_DATA_SIZE = 0x8A40,
		GL_UNIFORM_BLOCK_NAME_LENGTH = 0x8A41,
		GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS = 0x8A42,
		GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES = 0x8A43,
		GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER = 0x8A44,
		GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER = 0x8A46,
		GL_INVALID_INDEX = 0xFFFFFFFF,
		GL_MAX_VERTEX_OUTPUT_COMPONENTS = 0x9122,
		GL_MAX_FRAGMENT_INPUT_COMPONENTS = 0x9125,
		GL_MAX_SERVER_WAIT_TIMEOUT = 0x9111,
		GL_OBJECT_TYPE = 0x9112,
		GL_SYNC_CONDITION = 0x9113,
		GL_SYNC_STATUS = 0x9114,
		GL_SYNC_FLAGS = 0x9115,
		GL_SYNC_FENCE = 0x9116,
		GL_SYNC_GPU_COMMANDS_COMPLETE = 0x9117,
		GL_UNSIGNALED = 0x9118,
		GL_SIGNALED = 0x9119,
		GL_ALREADY_SIGNALED = 0x911A,
		GL_TIMEOUT_EXPIRED = 0x911B,
		GL_CONDITION_SATISFIED = 0x911C,
		GL_WAIT_FAILED = 0x911D,
		GL_SYNC_FLUSH_COMMANDS_BIT = 0x1;

	public static final long GL_TIMEOUT_IGNORED = 0xFFFFFFFFFFFFFFFFL;

	public static final int GL_VERTEX_ATTRIB_ARRAY_DIVISOR = 0x88FE,
		GL_ANY_SAMPLES_PASSED = 0x8C2F,
		GL_ANY_SAMPLES_PASSED_CONSERVATIVE = 0x8D6A,
		GL_SAMPLER_BINDING = 0x8919,
		GL_RGB10_A2UI = 0x906F,
		GL_TEXTURE_SWIZZLE_R = 0x8E42,
		GL_TEXTURE_SWIZZLE_G = 0x8E43,
		GL_TEXTURE_SWIZZLE_B = 0x8E44,
		GL_TEXTURE_SWIZZLE_A = 0x8E45,
		GL_GREEN = 0x1904,
		GL_BLUE = 0x1905,
		GL_INT_2_10_10_10_REV = 0x8D9F,
		GL_TRANSFORM_FEEDBACK = 0x8E22,
		GL_TRANSFORM_FEEDBACK_PAUSED = 0x8E23,
		GL_TRANSFORM_FEEDBACK_ACTIVE = 0x8E24,
		GL_TRANSFORM_FEEDBACK_BINDING = 0x8E25,
		GL_PROGRAM_BINARY_RETRIEVABLE_HINT = 0x8257,
		GL_PROGRAM_BINARY_LENGTH = 0x8741,
		GL_NUM_PROGRAM_BINARY_FORMATS = 0x87FE,
		GL_PROGRAM_BINARY_FORMATS = 0x87FF,
		GL_COMPRESSED_R11_EAC = 0x9270,
		GL_COMPRESSED_SIGNED_R11_EAC = 0x9271,
		GL_COMPRESSED_RG11_EAC = 0x9272,
		GL_COMPRESSED_SIGNED_RG11_EAC = 0x9273,
		GL_COMPRESSED_RGB8_ETC2 = 0x9274,
		GL_COMPRESSED_SRGB8_ETC2 = 0x9275,
		GL_COMPRESSED_RGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 0x9276,
		GL_COMPRESSED_SRGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 0x9277,
		GL_COMPRESSED_RGBA8_ETC2_EAC = 0x9278,
		GL_COMPRESSED_SRGB8_ALPHA8_ETC2_EAC = 0x9279,
		GL_TEXTURE_IMMUTABLE_FORMAT = 0x912F,
		GL_MAX_ELEMENT_INDEX = 0x8D6B,
		GL_NUM_SAMPLE_COUNTS = 0x9380;

	private GLES30() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glReadBuffer(int mode) {
		nglReadBuffer(mode);
	}
	static native void nglReadBuffer(int mode);

	public static void glDrawRangeElements(int mode, int start, int end, ByteBuffer indices) {
		GLChecks.ensureElementVBOdisabled();
		BufferChecks.checkDirect(indices);
		nglDrawRangeElements(mode, start, end, indices.remaining(), GLES20.GL_UNSIGNED_BYTE, MemoryUtil.getAddress(indices));
	}
	public static void glDrawRangeElements(int mode, int start, int end, IntBuffer indices) {
		GLChecks.ensureElementVBOdisabled();
		BufferChecks.checkDirect(indices);
		nglDrawRangeElements(mode, start, end, indices.remaining(), GLES20.GL_UNSIGNED_INT, MemoryUtil.getAddress(indices));
	}
	public static void glDrawRangeElements(int mode, int start, int end, ShortBuffer indices) {
		GLChecks.ensureElementVBOdisabled();
		BufferChecks.checkDirect(indices);
		nglDrawRangeElements(mode, start, end, indices.remaining(), GLES20.GL_UNSIGNED_SHORT, MemoryUtil.getAddress(indices));
	}
	static native void nglDrawRangeElements(int mode, int start, int end, int indices_count, int type, long indices);
	public static void glDrawRangeElements(int mode, int start, int end, int indices_count, int type, long indices_buffer_offset) {
		GLChecks.ensureElementVBOenabled();
		nglDrawRangeElementsBO(mode, start, end, indices_count, type, indices_buffer_offset);
	}
	static native void nglDrawRangeElementsBO(int mode, int start, int end, int indices_count, int type, long indices_buffer_offset);

	public static void glTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, ByteBuffer pixels) {
		GLChecks.ensureUnpackPBOdisabled();
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels));
	}
	public static void glTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, FloatBuffer pixels) {
		GLChecks.ensureUnpackPBOdisabled();
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels));
	}
	public static void glTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, IntBuffer pixels) {
		GLChecks.ensureUnpackPBOdisabled();
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels));
	}
	public static void glTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, ShortBuffer pixels) {
		GLChecks.ensureUnpackPBOdisabled();
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels));
	}
	static native void nglTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, long pixels);
	public static void glTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, long pixels_buffer_offset) {
		GLChecks.ensureUnpackPBOenabled();
		nglTexImage3DBO(target, level, internalFormat, width, height, depth, border, format, type, pixels_buffer_offset);
	}
	static native void nglTexImage3DBO(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, long pixels_buffer_offset);

	public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
		GLChecks.ensureUnpackPBOdisabled();
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels));
	}
	public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer pixels) {
		GLChecks.ensureUnpackPBOdisabled();
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels));
	}
	public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer pixels) {
		GLChecks.ensureUnpackPBOdisabled();
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels));
	}
	public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer pixels) {
		GLChecks.ensureUnpackPBOdisabled();
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels));
	}
	static native void nglTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels);
	public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels_buffer_offset) {
		GLChecks.ensureUnpackPBOenabled();
		nglTexSubImage3DBO(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_buffer_offset);
	}
	static native void nglTexSubImage3DBO(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels_buffer_offset);

	public static void glCopyTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height) {
		nglCopyTexSubImage3D(target, level, xoffset, yoffset, zoffset, x, y, width, height);
	}
	static native void nglCopyTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height);

	public static void glCompressedTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, ByteBuffer data) {
		GLChecks.ensureUnpackPBOdisabled();
		BufferChecks.checkDirect(data);
		nglCompressedTexImage3D(target, level, internalformat, width, height, depth, border, data.remaining(), MemoryUtil.getAddress(data));
	}
	static native void nglCompressedTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int data_imageSize, long data);
	public static void glCompressedTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int data_imageSize, long data_buffer_offset) {
		GLChecks.ensureUnpackPBOenabled();
		nglCompressedTexImage3DBO(target, level, internalformat, width, height, depth, border, data_imageSize, data_buffer_offset);
	}
	static native void nglCompressedTexImage3DBO(int target, int level, int internalformat, int width, int height, int depth, int border, int data_imageSize, long data_buffer_offset);

	public static void glCompressedTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, ByteBuffer data) {
		GLChecks.ensureUnpackPBOdisabled();
		BufferChecks.checkDirect(data);
		nglCompressedTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, data.remaining(), MemoryUtil.getAddress(data));
	}
	static native void nglCompressedTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int data_imageSize, long data);
	public static void glCompressedTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int data_imageSize, long data_buffer_offset) {
		GLChecks.ensureUnpackPBOenabled();
		nglCompressedTexSubImage3DBO(target, level, xoffset, yoffset, zoffset, width, height, depth, format, data_imageSize, data_buffer_offset);
	}
	static native void nglCompressedTexSubImage3DBO(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int data_imageSize, long data_buffer_offset);

	public static void glGenQueries(IntBuffer ids) {
		BufferChecks.checkDirect(ids);
		nglGenQueries(ids.remaining(), MemoryUtil.getAddress(ids));
	}
	static native void nglGenQueries(int ids_n, long ids);

	/** Overloads glGenQueries. */
	public static int glGenQueries() {
		IntBuffer ids = APIUtil.getBufferInt();
		nglGenQueries(1, MemoryUtil.getAddress(ids));
		return ids.get(0);
	}

	public static void glDeleteQueries(IntBuffer ids) {
		BufferChecks.checkDirect(ids);
		nglDeleteQueries(ids.remaining(), MemoryUtil.getAddress(ids));
	}
	static native void nglDeleteQueries(int ids_n, long ids);

	/** Overloads glDeleteQueries. */
	public static void glDeleteQueries(int id) {
		nglDeleteQueries(1, APIUtil.getInt(id));
	}

	public static boolean glIsQuery(int id) {
		boolean __result = nglIsQuery(id);
		return __result;
	}
	static native boolean nglIsQuery(int id);

	public static void glBeginQuery(int target, int id) {
		nglBeginQuery(target, id);
	}
	static native void nglBeginQuery(int target, int id);

	public static void glEndQuery(int target) {
		nglEndQuery(target);
	}
	static native void nglEndQuery(int target);

	public static void glGetQuery(int target, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetQueryiv(target, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetQueryiv(int target, int pname, long params);

	/** Overloads glGetQueryiv. */
	public static int glGetQueryi(int target, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetQueryiv(target, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGetQueryObjectu(int id, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetQueryObjectuiv(id, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetQueryObjectuiv(int id, int pname, long params);

	/** Overloads glGetQueryObjectuiv. */
	public static int glGetQueryObjectui(int id, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetQueryObjectuiv(id, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static boolean glUnmapBuffer(int target) {
		boolean __result = nglUnmapBuffer(target);
		return __result;
	}
	static native boolean nglUnmapBuffer(int target);

	public static ByteBuffer glGetBufferPointer(int target, int pname) {
		ByteBuffer __result = nglGetBufferPointerv(target, pname, GLES20.glGetBufferParameteri(target, GLES20.GL_BUFFER_SIZE));
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglGetBufferPointerv(int target, int pname, long result_size);

	public static void glDrawBuffers(IntBuffer buffers) {
		BufferChecks.checkDirect(buffers);
		nglDrawBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers));
	}
	static native void nglDrawBuffers(int buffers_size, long buffers);

	/** Overloads glDrawBuffers. */
	public static void glDrawBuffers(int buffer) {
		nglDrawBuffers(1, APIUtil.getInt(buffer));
	}

	public static void glUniformMatrix2x3(int location, boolean transpose, FloatBuffer matrices) {
		BufferChecks.checkDirect(matrices);
		nglUniformMatrix2x3fv(location, matrices.remaining() / (2 * 3), transpose, MemoryUtil.getAddress(matrices));
	}
	static native void nglUniformMatrix2x3fv(int location, int matrices_count, boolean transpose, long matrices);

	public static void glUniformMatrix3x2(int location, boolean transpose, FloatBuffer matrices) {
		BufferChecks.checkDirect(matrices);
		nglUniformMatrix3x2fv(location, matrices.remaining() / (3 * 2), transpose, MemoryUtil.getAddress(matrices));
	}
	static native void nglUniformMatrix3x2fv(int location, int matrices_count, boolean transpose, long matrices);

	public static void glUniformMatrix2x4(int location, boolean transpose, FloatBuffer matrices) {
		BufferChecks.checkDirect(matrices);
		nglUniformMatrix2x4fv(location, matrices.remaining() >> 3, transpose, MemoryUtil.getAddress(matrices));
	}
	static native void nglUniformMatrix2x4fv(int location, int matrices_count, boolean transpose, long matrices);

	public static void glUniformMatrix4x2(int location, boolean transpose, FloatBuffer matrices) {
		BufferChecks.checkDirect(matrices);
		nglUniformMatrix4x2fv(location, matrices.remaining() >> 3, transpose, MemoryUtil.getAddress(matrices));
	}
	static native void nglUniformMatrix4x2fv(int location, int matrices_count, boolean transpose, long matrices);

	public static void glUniformMatrix3x4(int location, boolean transpose, FloatBuffer matrices) {
		BufferChecks.checkDirect(matrices);
		nglUniformMatrix3x4fv(location, matrices.remaining() / (3 * 4), transpose, MemoryUtil.getAddress(matrices));
	}
	static native void nglUniformMatrix3x4fv(int location, int matrices_count, boolean transpose, long matrices);

	public static void glUniformMatrix4x3(int location, boolean transpose, FloatBuffer matrices) {
		BufferChecks.checkDirect(matrices);
		nglUniformMatrix4x3fv(location, matrices.remaining() / (4 * 3), transpose, MemoryUtil.getAddress(matrices));
	}
	static native void nglUniformMatrix4x3fv(int location, int matrices_count, boolean transpose, long matrices);

	/**
	 *  Transfers a rectangle of pixel values from one
	 *  region of the read framebuffer to another in the draw framebuffer.
	 *  &lt;mask&gt; is the bitwise OR of a number of values indicating which
	 *  buffers are to be copied. The values are COLOR_BUFFER_BIT,
	 *  DEPTH_BUFFER_BIT, and STENCIL_BUFFER_BIT.
	 *  The pixels corresponding to these buffers are
	 *  copied from the source rectangle, bound by the locations (srcX0,
	 *  srcY0) and (srcX1, srcY1) inclusive, to the destination rectangle,
	 *  bound by the locations (dstX0, dstY0) and (dstX1, dstY1)
	 *  inclusive.
	 *  If the source and destination rectangle dimensions do not match,
	 *  the source image is stretched to fit the destination
	 *  rectangle. &lt;filter&gt; must be LINEAR or NEAREST and specifies the
	 *  method of interpolation to be applied if the image is
	 *  stretched.
	 */
	public static void glBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
		nglBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
	}
	static native void nglBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter);

	/**
	 *  Establishes the data storage, format, dimensions, and number of
	 *  samples of a renderbuffer object's image.
	 */
	public static void glRenderbufferStorageMultisample(int target, int samples, int internalformat, int width, int height) {
		nglRenderbufferStorageMultisample(target, samples, internalformat, width, height);
	}
	static native void nglRenderbufferStorageMultisample(int target, int samples, int internalformat, int width, int height);

	public static void glFramebufferTextureLayer(int target, int attachment, int texture, int level, int layer) {
		nglFramebufferTextureLayer(target, attachment, texture, level, layer);
	}
	static native void nglFramebufferTextureLayer(int target, int attachment, int texture, int level, int layer);

	/**
	 *  glMapBufferRange maps a GL buffer object range to a ByteBuffer. The old_buffer argument can be null,
	 *  in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 *  it will be returned if it points to the same mapped memory and has the same capacity as the buffer object,
	 *  otherwise a new ByteBuffer is created. That way, an application will normally use glMapBufferRange like this:
	 *  <p/>
	 *  ByteBuffer mapped_buffer; mapped_buffer = glMapBufferRange(..., ..., ..., ..., null); ... // Another map on the same buffer mapped_buffer = glMapBufferRange(..., ..., ..., ..., mapped_buffer);
	 *  <p/>
	 *  Only ByteBuffers returned from this method are to be passed as the old_buffer argument. User-created ByteBuffers cannot be reused.
	 * <p>
	 *  @param old_buffer A ByteBuffer. If this argument points to the same address and has the same capacity as the new mapping, it will be returned and no new buffer will be created.
	 * <p>
	 *  @return A ByteBuffer representing the mapped buffer memory.
	 */
	public static ByteBuffer glMapBufferRange(int target, long offset, long length, int access, ByteBuffer old_buffer) {
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapBufferRange(target, offset, length, access, old_buffer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglMapBufferRange(int target, long offset, long length, int access, ByteBuffer old_buffer);

	public static void glFlushMappedBufferRange(int target, long offset, long length) {
		nglFlushMappedBufferRange(target, offset, length);
	}
	static native void nglFlushMappedBufferRange(int target, long offset, long length);

	public static void glBindVertexArray(int array) {
		StateTracker.bindVAO(array);
		nglBindVertexArray(array);
	}
	static native void nglBindVertexArray(int array);

	public static void glDeleteVertexArrays(IntBuffer arrays) {
		StateTracker.deleteVAO(arrays);
		BufferChecks.checkDirect(arrays);
		nglDeleteVertexArrays(arrays.remaining(), MemoryUtil.getAddress(arrays));
	}
	static native void nglDeleteVertexArrays(int arrays_n, long arrays);

	/** Overloads glDeleteVertexArrays. */
	public static void glDeleteVertexArrays(int array) {
		StateTracker.deleteVAO(array);
		nglDeleteVertexArrays(1, APIUtil.getInt(array));
	}

	public static void glGenVertexArrays(IntBuffer arrays) {
		BufferChecks.checkDirect(arrays);
		nglGenVertexArrays(arrays.remaining(), MemoryUtil.getAddress(arrays));
	}
	static native void nglGenVertexArrays(int arrays_n, long arrays);

	/** Overloads glGenVertexArrays. */
	public static int glGenVertexArrays() {
		IntBuffer arrays = APIUtil.getBufferInt();
		nglGenVertexArrays(1, MemoryUtil.getAddress(arrays));
		return arrays.get(0);
	}

	public static boolean glIsVertexArray(int array) {
		boolean __result = nglIsVertexArray(array);
		return __result;
	}
	static native boolean nglIsVertexArray(int array);

	public static void glGetInteger(int value, int index, IntBuffer data) {
		BufferChecks.checkBuffer(data, 4);
		nglGetIntegeri_v(value, index, MemoryUtil.getAddress(data));
	}
	static native void nglGetIntegeri_v(int value, int index, long data);

	/** Overloads glGetIntegeri_v. */
	public static int glGetInteger(int value, int index) {
		IntBuffer data = APIUtil.getBufferInt();
		nglGetIntegeri_v(value, index, MemoryUtil.getAddress(data));
		return data.get(0);
	}

	public static void glBeginTransformFeedback(int primitiveMode) {
		nglBeginTransformFeedback(primitiveMode);
	}
	static native void nglBeginTransformFeedback(int primitiveMode);

	public static void glEndTransformFeedback() {
		nglEndTransformFeedback();
	}
	static native void nglEndTransformFeedback();

	public static void glBindBufferRange(int target, int index, int buffer, long offset, long size) {
		nglBindBufferRange(target, index, buffer, offset, size);
	}
	static native void nglBindBufferRange(int target, int index, int buffer, long offset, long size);

	public static void glBindBufferBase(int target, int index, int buffer) {
		nglBindBufferBase(target, index, buffer);
	}
	static native void nglBindBufferBase(int target, int index, int buffer);

	public static void glTransformFeedbackVaryings(int program, int count, ByteBuffer varyings, int bufferMode) {
		BufferChecks.checkDirect(varyings);
		BufferChecks.checkNullTerminated(varyings, count);
		nglTransformFeedbackVaryings(program, count, MemoryUtil.getAddress(varyings), bufferMode);
	}
	static native void nglTransformFeedbackVaryings(int program, int count, long varyings, int bufferMode);

	/** Overloads glTransformFeedbackVaryings. */
	public static void glTransformFeedbackVaryings(int program, CharSequence[] varyings, int bufferMode) {
		BufferChecks.checkArray(varyings);
		nglTransformFeedbackVaryings(program, varyings.length, APIUtil.getBufferNT(varyings), bufferMode);
	}

	public static void glGetTransformFeedbackVarying(int program, int index, IntBuffer length, IntBuffer size, IntBuffer type, ByteBuffer name) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkBuffer(size, 1);
		BufferChecks.checkBuffer(type, 1);
		BufferChecks.checkDirect(name);
		nglGetTransformFeedbackVarying(program, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name));
	}
	static native void nglGetTransformFeedbackVarying(int program, int index, int name_bufSize, long length, long size, long type, long name);

	/** Overloads glGetTransformFeedbackVarying. */
	public static String glGetTransformFeedbackVarying(int program, int index, int bufSize, IntBuffer size, IntBuffer type) {
		BufferChecks.checkBuffer(size, 1);
		BufferChecks.checkBuffer(type, 1);
		IntBuffer name_length = APIUtil.getLengths();
		ByteBuffer name = APIUtil.getBufferByte(bufSize);
		nglGetTransformFeedbackVarying(program, index, bufSize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name));
		name.limit(name_length.get(0));
		return APIUtil.getString(name);
	}

	public static void glVertexAttribIPointer(int index, int size, int type, int stride, ByteBuffer buffer) {
		GLChecks.ensureArrayVBOdisabled();
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getTracker().glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribIPointer(index, size, type, stride, MemoryUtil.getAddress(buffer));
	}
	public static void glVertexAttribIPointer(int index, int size, int type, int stride, IntBuffer buffer) {
		GLChecks.ensureArrayVBOdisabled();
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getTracker().glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribIPointer(index, size, type, stride, MemoryUtil.getAddress(buffer));
	}
	public static void glVertexAttribIPointer(int index, int size, int type, int stride, ShortBuffer buffer) {
		GLChecks.ensureArrayVBOdisabled();
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getTracker().glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribIPointer(index, size, type, stride, MemoryUtil.getAddress(buffer));
	}
	static native void nglVertexAttribIPointer(int index, int size, int type, int stride, long buffer);
	public static void glVertexAttribIPointer(int index, int size, int type, int stride, long buffer_buffer_offset) {
		GLChecks.ensureArrayVBOenabled();
		nglVertexAttribIPointerBO(index, size, type, stride, buffer_buffer_offset);
	}
	static native void nglVertexAttribIPointerBO(int index, int size, int type, int stride, long buffer_buffer_offset);

	public static void glGetVertexAttribI(int index, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribIiv(index, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetVertexAttribIiv(int index, int pname, long params);

	public static void glGetVertexAttribIu(int index, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribIuiv(index, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetVertexAttribIuiv(int index, int pname, long params);

	public static void glVertexAttribI4i(int index, int x, int y, int z, int w) {
		nglVertexAttribI4i(index, x, y, z, w);
	}
	static native void nglVertexAttribI4i(int index, int x, int y, int z, int w);

	public static void glVertexAttribI4ui(int index, int x, int y, int z, int w) {
		nglVertexAttribI4ui(index, x, y, z, w);
	}
	static native void nglVertexAttribI4ui(int index, int x, int y, int z, int w);

	public static void glVertexAttribI4(int index, IntBuffer v) {
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribI4iv(index, MemoryUtil.getAddress(v));
	}
	static native void nglVertexAttribI4iv(int index, long v);

	public static void glVertexAttribI4u(int index, IntBuffer v) {
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribI4uiv(index, MemoryUtil.getAddress(v));
	}
	static native void nglVertexAttribI4uiv(int index, long v);

	public static void glGetUniformu(int program, int location, IntBuffer params) {
		BufferChecks.checkDirect(params);
		nglGetUniformuiv(program, location, MemoryUtil.getAddress(params));
	}
	static native void nglGetUniformuiv(int program, int location, long params);

	public static int glGetFragDataLocation(int program, ByteBuffer name) {
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetFragDataLocation(program, MemoryUtil.getAddress(name));
		return __result;
	}
	static native int nglGetFragDataLocation(int program, long name);

	/** Overloads glGetFragDataLocation. */
	public static int glGetFragDataLocation(int program, CharSequence name) {
		int __result = nglGetFragDataLocation(program, APIUtil.getBufferNT(name));
		return __result;
	}

	public static void glUniform1ui(int location, int v0) {
		nglUniform1ui(location, v0);
	}
	static native void nglUniform1ui(int location, int v0);

	public static void glUniform2ui(int location, int v0, int v1) {
		nglUniform2ui(location, v0, v1);
	}
	static native void nglUniform2ui(int location, int v0, int v1);

	public static void glUniform3ui(int location, int v0, int v1, int v2) {
		nglUniform3ui(location, v0, v1, v2);
	}
	static native void nglUniform3ui(int location, int v0, int v1, int v2);

	public static void glUniform4ui(int location, int v0, int v1, int v2, int v3) {
		nglUniform4ui(location, v0, v1, v2, v3);
	}
	static native void nglUniform4ui(int location, int v0, int v1, int v2, int v3);

	public static void glUniform1u(int location, IntBuffer value) {
		BufferChecks.checkDirect(value);
		nglUniform1uiv(location, value.remaining(), MemoryUtil.getAddress(value));
	}
	static native void nglUniform1uiv(int location, int value_count, long value);

	public static void glUniform2u(int location, IntBuffer value) {
		BufferChecks.checkDirect(value);
		nglUniform2uiv(location, value.remaining() >> 1, MemoryUtil.getAddress(value));
	}
	static native void nglUniform2uiv(int location, int value_count, long value);

	public static void glUniform3u(int location, IntBuffer value) {
		BufferChecks.checkDirect(value);
		nglUniform3uiv(location, value.remaining() / 3, MemoryUtil.getAddress(value));
	}
	static native void nglUniform3uiv(int location, int value_count, long value);

	public static void glUniform4u(int location, IntBuffer value) {
		BufferChecks.checkDirect(value);
		nglUniform4uiv(location, value.remaining() >> 2, MemoryUtil.getAddress(value));
	}
	static native void nglUniform4uiv(int location, int value_count, long value);

	public static void glClearBuffer(int buffer, int drawbuffer, FloatBuffer value) {
		BufferChecks.checkBuffer(value, 4);
		nglClearBufferfv(buffer, drawbuffer, MemoryUtil.getAddress(value));
	}
	static native void nglClearBufferfv(int buffer, int drawbuffer, long value);

	public static void glClearBuffer(int buffer, int drawbuffer, IntBuffer value) {
		BufferChecks.checkBuffer(value, 4);
		nglClearBufferiv(buffer, drawbuffer, MemoryUtil.getAddress(value));
	}
	static native void nglClearBufferiv(int buffer, int drawbuffer, long value);

	public static void glClearBufferu(int buffer, int drawbuffer, IntBuffer value) {
		BufferChecks.checkBuffer(value, 4);
		nglClearBufferuiv(buffer, drawbuffer, MemoryUtil.getAddress(value));
	}
	static native void nglClearBufferuiv(int buffer, int drawbuffer, long value);

	public static void glClearBufferfi(int buffer, int drawbuffer, float depth, int stencil) {
		nglClearBufferfi(buffer, drawbuffer, depth, stencil);
	}
	static native void nglClearBufferfi(int buffer, int drawbuffer, float depth, int stencil);

	public static String glGetStringi(int name, int index) {
		String __result = nglGetStringi(name, index);
		return __result;
	}
	static native String nglGetStringi(int name, int index);

	public static void glCopyBufferSubData(int readtarget, int writetarget, long readoffset, long writeoffset, long size) {
		nglCopyBufferSubData(readtarget, writetarget, readoffset, writeoffset, size);
	}
	static native void nglCopyBufferSubData(int readtarget, int writetarget, long readoffset, long writeoffset, long size);

	public static void glGetUniformIndices(int program, ByteBuffer uniformNames, IntBuffer uniformIndices) {
		BufferChecks.checkDirect(uniformNames);
		BufferChecks.checkNullTerminated(uniformNames, uniformIndices.remaining());
		BufferChecks.checkDirect(uniformIndices);
		nglGetUniformIndices(program, uniformIndices.remaining(), MemoryUtil.getAddress(uniformNames), MemoryUtil.getAddress(uniformIndices));
	}
	static native void nglGetUniformIndices(int program, int uniformIndices_uniformCount, long uniformNames, long uniformIndices);

	/** Overloads glGetUniformIndices. */
	public static void glGetUniformIndices(int program, CharSequence[] uniformNames, IntBuffer uniformIndices) {
		BufferChecks.checkArray(uniformNames);
		BufferChecks.checkBuffer(uniformIndices, uniformNames.length);
		nglGetUniformIndices(program, uniformNames.length, APIUtil.getBufferNT(uniformNames), MemoryUtil.getAddress(uniformIndices));
	}

	public static void glGetActiveUniforms(int program, IntBuffer uniformIndices, int pname, IntBuffer params) {
		BufferChecks.checkDirect(uniformIndices);
		BufferChecks.checkBuffer(params, uniformIndices.remaining());
		nglGetActiveUniformsiv(program, uniformIndices.remaining(), MemoryUtil.getAddress(uniformIndices), pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetActiveUniformsiv(int program, int uniformIndices_uniformCount, long uniformIndices, int pname, long params);

	/** Overloads glGetActiveUniformsiv. */
	public static int glGetActiveUniformsi(int program, int uniformIndex, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetActiveUniformsiv(program, 1, MemoryUtil.getAddress(params.put(1, uniformIndex), 1), pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static int glGetUniformBlockIndex(int program, ByteBuffer uniformBlockName) {
		BufferChecks.checkDirect(uniformBlockName);
		BufferChecks.checkNullTerminated(uniformBlockName);
		int __result = nglGetUniformBlockIndex(program, MemoryUtil.getAddress(uniformBlockName));
		return __result;
	}
	static native int nglGetUniformBlockIndex(int program, long uniformBlockName);

	/** Overloads glGetUniformBlockIndex. */
	public static int glGetUniformBlockIndex(int program, CharSequence uniformBlockName) {
		int __result = nglGetUniformBlockIndex(program, APIUtil.getBufferNT(uniformBlockName));
		return __result;
	}

	public static void glGetActiveUniformBlock(int program, int uniformBlockIndex, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 16);
		nglGetActiveUniformBlockiv(program, uniformBlockIndex, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetActiveUniformBlockiv(int program, int uniformBlockIndex, int pname, long params);

	/** Overloads glGetActiveUniformBlockiv. */
	public static int glGetActiveUniformBlocki(int program, int uniformBlockIndex, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetActiveUniformBlockiv(program, uniformBlockIndex, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGetActiveUniformBlockName(int program, int uniformBlockIndex, IntBuffer length, ByteBuffer uniformBlockName) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(uniformBlockName);
		nglGetActiveUniformBlockName(program, uniformBlockIndex, uniformBlockName.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(uniformBlockName));
	}
	static native void nglGetActiveUniformBlockName(int program, int uniformBlockIndex, int uniformBlockName_bufSize, long length, long uniformBlockName);

	/** Overloads glGetActiveUniformBlockName. */
	public static String glGetActiveUniformBlockName(int program, int uniformBlockIndex, int bufSize) {
		IntBuffer uniformBlockName_length = APIUtil.getLengths();
		ByteBuffer uniformBlockName = APIUtil.getBufferByte(bufSize);
		nglGetActiveUniformBlockName(program, uniformBlockIndex, bufSize, MemoryUtil.getAddress0(uniformBlockName_length), MemoryUtil.getAddress(uniformBlockName));
		uniformBlockName.limit(uniformBlockName_length.get(0));
		return APIUtil.getString(uniformBlockName);
	}

	public static void glUniformBlockBinding(int program, int uniformBlockIndex, int uniformBlockBinding) {
		nglUniformBlockBinding(program, uniformBlockIndex, uniformBlockBinding);
	}
	static native void nglUniformBlockBinding(int program, int uniformBlockIndex, int uniformBlockBinding);

	public static void glDrawArraysInstanced(int mode, int first, int count, int primcount) {
		nglDrawArraysInstanced(mode, first, count, primcount);
	}
	static native void nglDrawArraysInstanced(int mode, int first, int count, int primcount);

	public static void glDrawElementsInstanced(int mode, ByteBuffer indices, int primcount) {
		GLChecks.ensureElementVBOdisabled();
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstanced(mode, indices.remaining(), GLES20.GL_UNSIGNED_BYTE, MemoryUtil.getAddress(indices), primcount);
	}
	public static void glDrawElementsInstanced(int mode, IntBuffer indices, int primcount) {
		GLChecks.ensureElementVBOdisabled();
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstanced(mode, indices.remaining(), GLES20.GL_UNSIGNED_INT, MemoryUtil.getAddress(indices), primcount);
	}
	public static void glDrawElementsInstanced(int mode, ShortBuffer indices, int primcount) {
		GLChecks.ensureElementVBOdisabled();
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstanced(mode, indices.remaining(), GLES20.GL_UNSIGNED_SHORT, MemoryUtil.getAddress(indices), primcount);
	}
	static native void nglDrawElementsInstanced(int mode, int indices_count, int type, long indices, int primcount);
	public static void glDrawElementsInstanced(int mode, int indices_count, int type, long indices_buffer_offset, int primcount) {
		GLChecks.ensureElementVBOenabled();
		nglDrawElementsInstancedBO(mode, indices_count, type, indices_buffer_offset, primcount);
	}
	static native void nglDrawElementsInstancedBO(int mode, int indices_count, int type, long indices_buffer_offset, int primcount);

	public static GLSync glFenceSync(int condition, int flags) {
		GLSync __result = new GLSync(nglFenceSync(condition, flags));
		return __result;
	}
	static native long nglFenceSync(int condition, int flags);

	public static boolean glIsSync(GLSync sync) {
		boolean __result = nglIsSync(sync.getPointer());
		return __result;
	}
	static native boolean nglIsSync(long sync);

	public static void glDeleteSync(GLSync sync) {
		nglDeleteSync(sync.getPointer());
	}
	static native void nglDeleteSync(long sync);

	public static int glClientWaitSync(GLSync sync, int flags, long timeout) {
		int __result = nglClientWaitSync(sync.getPointer(), flags, timeout);
		return __result;
	}
	static native int nglClientWaitSync(long sync, int flags, long timeout);

	public static void glWaitSync(GLSync sync, int flags, long timeout) {
		nglWaitSync(sync.getPointer(), flags, timeout);
	}
	static native void nglWaitSync(long sync, int flags, long timeout);

	public static void glGetInteger64(int pname, LongBuffer data) {
		BufferChecks.checkBuffer(data, 1);
		nglGetInteger64v(pname, MemoryUtil.getAddress(data));
	}
	static native void nglGetInteger64v(int pname, long data);

	/** Overloads glGetInteger64v. */
	public static long glGetInteger64(int pname) {
		LongBuffer data = APIUtil.getBufferLong();
		nglGetInteger64v(pname, MemoryUtil.getAddress(data));
		return data.get(0);
	}

	public static void glGetInteger64(int value, int index, LongBuffer data) {
		BufferChecks.checkBuffer(data, 4);
		nglGetInteger64i_v(value, index, MemoryUtil.getAddress(data));
	}
	static native void nglGetInteger64i_v(int value, int index, long data);

	/** Overloads glGetInteger64i_v. */
	public static long glGetInteger64(int value, int index) {
		LongBuffer data = APIUtil.getBufferLong();
		nglGetInteger64i_v(value, index, MemoryUtil.getAddress(data));
		return data.get(0);
	}

	public static void glGetSync(GLSync sync, int pname, IntBuffer length, IntBuffer values) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(values);
		nglGetSynciv(sync.getPointer(), pname, values.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(values));
	}
	static native void nglGetSynciv(long sync, int pname, int values_bufSize, long length, long values);

	/** Overloads glGetSynciv. */
	public static int glGetSynci(GLSync sync, int pname) {
		IntBuffer values = APIUtil.getBufferInt();
		nglGetSynciv(sync.getPointer(), pname, 1, 0L, MemoryUtil.getAddress(values));
		return values.get(0);
	}

	public static void glGetBufferParameter(int target, int pname, LongBuffer params) {
		BufferChecks.checkBuffer(params, 4);
		nglGetBufferParameteri64v(target, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetBufferParameteri64v(int target, int pname, long params);

	/** Overloads glGetBufferParameteri64v. */
	public static long glGetBufferParameteri64(int target, int pname) {
		LongBuffer params = APIUtil.getBufferLong();
		nglGetBufferParameteri64v(target, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGenSamplers(IntBuffer samplers) {
		BufferChecks.checkDirect(samplers);
		nglGenSamplers(samplers.remaining(), MemoryUtil.getAddress(samplers));
	}
	static native void nglGenSamplers(int samplers_count, long samplers);

	/** Overloads glGenSamplers. */
	public static int glGenSamplers() {
		IntBuffer samplers = APIUtil.getBufferInt();
		nglGenSamplers(1, MemoryUtil.getAddress(samplers));
		return samplers.get(0);
	}

	public static void glDeleteSamplers(IntBuffer samplers) {
		BufferChecks.checkDirect(samplers);
		nglDeleteSamplers(samplers.remaining(), MemoryUtil.getAddress(samplers));
	}
	static native void nglDeleteSamplers(int samplers_count, long samplers);

	/** Overloads glDeleteSamplers. */
	public static void glDeleteSamplers(int sampler) {
		nglDeleteSamplers(1, APIUtil.getInt(sampler));
	}

	public static boolean glIsSampler(int sampler) {
		boolean __result = nglIsSampler(sampler);
		return __result;
	}
	static native boolean nglIsSampler(int sampler);

	public static void glBindSampler(int unit, int sampler) {
		nglBindSampler(unit, sampler);
	}
	static native void nglBindSampler(int unit, int sampler);

	public static void glSamplerParameteri(int sampler, int pname, int param) {
		nglSamplerParameteri(sampler, pname, param);
	}
	static native void nglSamplerParameteri(int sampler, int pname, int param);

	public static void glSamplerParameterf(int sampler, int pname, float param) {
		nglSamplerParameterf(sampler, pname, param);
	}
	static native void nglSamplerParameterf(int sampler, int pname, float param);

	public static void glSamplerParameter(int sampler, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 4);
		nglSamplerParameteriv(sampler, pname, MemoryUtil.getAddress(params));
	}
	static native void nglSamplerParameteriv(int sampler, int pname, long params);

	public static void glSamplerParameter(int sampler, int pname, FloatBuffer params) {
		BufferChecks.checkBuffer(params, 4);
		nglSamplerParameterfv(sampler, pname, MemoryUtil.getAddress(params));
	}
	static native void nglSamplerParameterfv(int sampler, int pname, long params);

	public static void glGetSamplerParameter(int sampler, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 4);
		nglGetSamplerParameteriv(sampler, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetSamplerParameteriv(int sampler, int pname, long params);

	/** Overloads glGetSamplerParameteriv. */
	public static int glGetSamplerParameteri(int sampler, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetSamplerParameteriv(sampler, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGetSamplerParameter(int sampler, int pname, FloatBuffer params) {
		BufferChecks.checkBuffer(params, 4);
		nglGetSamplerParameterfv(sampler, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetSamplerParameterfv(int sampler, int pname, long params);

	/** Overloads glGetSamplerParameterfv. */
	public static float glGetSamplerParameterf(int sampler, int pname) {
		FloatBuffer params = APIUtil.getBufferFloat();
		nglGetSamplerParameterfv(sampler, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glVertexAttribDivisor(int index, int divisor) {
		nglVertexAttribDivisor(index, divisor);
	}
	static native void nglVertexAttribDivisor(int index, int divisor);

	public static void glBindTransformFeedback(int target, int id) {
		nglBindTransformFeedback(target, id);
	}
	static native void nglBindTransformFeedback(int target, int id);

	public static void glDeleteTransformFeedbacks(IntBuffer ids) {
		BufferChecks.checkDirect(ids);
		nglDeleteTransformFeedbacks(ids.remaining(), MemoryUtil.getAddress(ids));
	}
	static native void nglDeleteTransformFeedbacks(int ids_n, long ids);

	/** Overloads glDeleteTransformFeedbacks. */
	public static void glDeleteTransformFeedbacks(int id) {
		nglDeleteTransformFeedbacks(1, APIUtil.getInt(id));
	}

	public static void glGenTransformFeedbacks(IntBuffer ids) {
		BufferChecks.checkDirect(ids);
		nglGenTransformFeedbacks(ids.remaining(), MemoryUtil.getAddress(ids));
	}
	static native void nglGenTransformFeedbacks(int ids_n, long ids);

	/** Overloads glGenTransformFeedbacks. */
	public static int glGenTransformFeedbacks() {
		IntBuffer ids = APIUtil.getBufferInt();
		nglGenTransformFeedbacks(1, MemoryUtil.getAddress(ids));
		return ids.get(0);
	}

	public static boolean glIsTransformFeedback(int id) {
		boolean __result = nglIsTransformFeedback(id);
		return __result;
	}
	static native boolean nglIsTransformFeedback(int id);

	public static void glPauseTransformFeedback() {
		nglPauseTransformFeedback();
	}
	static native void nglPauseTransformFeedback();

	public static void glResumeTransformFeedback() {
		nglResumeTransformFeedback();
	}
	static native void nglResumeTransformFeedback();

	public static void glGetProgramBinary(int program, IntBuffer length, IntBuffer binaryFormat, ByteBuffer binary) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkBuffer(binaryFormat, 1);
		BufferChecks.checkDirect(binary);
		nglGetProgramBinary(program, binary.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(binaryFormat), MemoryUtil.getAddress(binary));
	}
	static native void nglGetProgramBinary(int program, int binary_bufSize, long length, long binaryFormat, long binary);

	public static void glProgramBinary(int program, int binaryFormat, ByteBuffer binary) {
		BufferChecks.checkDirect(binary);
		nglProgramBinary(program, binaryFormat, MemoryUtil.getAddress(binary), binary.remaining());
	}
	static native void nglProgramBinary(int program, int binaryFormat, long binary, int binary_length);

	public static void glProgramParameteri(int program, int pname, int value) {
		nglProgramParameteri(program, pname, value);
	}
	static native void nglProgramParameteri(int program, int pname, int value);

	public static void glInvalidateFramebuffer(int target, IntBuffer attachments) {
		BufferChecks.checkDirect(attachments);
		nglInvalidateFramebuffer(target, attachments.remaining(), MemoryUtil.getAddress(attachments));
	}
	static native void nglInvalidateFramebuffer(int target, int attachments_numAttachments, long attachments);

	public static void glInvalidateSubFramebuffer(int target, IntBuffer attachments, int x, int y, int width, int height) {
		BufferChecks.checkDirect(attachments);
		nglInvalidateSubFramebuffer(target, attachments.remaining(), MemoryUtil.getAddress(attachments), x, y, width, height);
	}
	static native void nglInvalidateSubFramebuffer(int target, int attachments_numAttachments, long attachments, int x, int y, int width, int height);

	public static void glTexStorage2D(int target, int levels, int internalformat, int width, int height) {
		nglTexStorage2D(target, levels, internalformat, width, height);
	}
	static native void nglTexStorage2D(int target, int levels, int internalformat, int width, int height);

	public static void glTexStorage3D(int target, int levels, int internalformat, int width, int height, int depth) {
		nglTexStorage3D(target, levels, internalformat, width, height, depth);
	}
	static native void nglTexStorage3D(int target, int levels, int internalformat, int width, int height, int depth);

	public static void glGetInternalformat(int target, int internalformat, int pname, IntBuffer params) {
		BufferChecks.checkDirect(params);
		nglGetInternalformativ(target, internalformat, pname, params.remaining(), MemoryUtil.getAddress(params));
	}
	static native void nglGetInternalformativ(int target, int internalformat, int pname, int params_bufSize, long params);

	/** Overloads glGetInternalformativ. */
	public static int glGetInternalformat(int target, int internalformat, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetInternalformativ(target, internalformat, pname, 1, MemoryUtil.getAddress(params));
		return params.get(0);
	}
}
