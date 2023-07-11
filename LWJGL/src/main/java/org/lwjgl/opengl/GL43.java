/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class GL43 {

	/**
	 * No. of supported Shading Language Versions. Accepted by the &lt;pname&gt; parameter of GetIntegerv. 
	 */
	public static final int GL_NUM_SHADING_LANGUAGE_VERSIONS = 0x82E9;

	/**
	 * Vertex attrib array has unconverted doubles. Accepted by the &lt;pname&gt; parameter of GetVertexAttribiv. 
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_LONG = 0x874E;

	/**
	 * Accepted by the &lt;internalformat&gt; parameter of CompressedTexImage2D 
	 */
	public static final int GL_COMPRESSED_RGB8_ETC2 = 0x9274,
		GL_COMPRESSED_SRGB8_ETC2 = 0x9275,
		GL_COMPRESSED_RGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 0x9276,
		GL_COMPRESSED_SRGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 0x9277,
		GL_COMPRESSED_RGBA8_ETC2_EAC = 0x9278,
		GL_COMPRESSED_SRGB8_ALPHA8_ETC2_EAC = 0x9279,
		GL_COMPRESSED_R11_EAC = 0x9270,
		GL_COMPRESSED_SIGNED_R11_EAC = 0x9271,
		GL_COMPRESSED_RG11_EAC = 0x9272,
		GL_COMPRESSED_SIGNED_RG11_EAC = 0x9273;

	/**
	 * Accepted by the &lt;target&gt; parameter of Enable and Disable: 
	 */
	public static final int GL_PRIMITIVE_RESTART_FIXED_INDEX = 0x8D69;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery,
	 *  GetQueryIndexediv and GetQueryiv:
	 */
	public static final int GL_ANY_SAMPLES_PASSED_CONSERVATIVE = 0x8D6A;

	/**
	 * Accepted by the &lt;value&gt; parameter of the GetInteger* functions: 
	 */
	public static final int GL_MAX_ELEMENT_INDEX = 0x8D6B;

	/**
	 *  Accepted by the &lt;type&gt; parameter of CreateShader and returned in the
	 *  &lt;params&gt; parameter by GetShaderiv:
	 */
	public static final int GL_COMPUTE_SHADER = 0x91B9;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetBooleanv, GetFloatv,
	 *  GetDoublev and GetInteger64v:
	 */
	public static final int GL_MAX_COMPUTE_UNIFORM_BLOCKS = 0x91BB,
		GL_MAX_COMPUTE_TEXTURE_IMAGE_UNITS = 0x91BC,
		GL_MAX_COMPUTE_IMAGE_UNIFORMS = 0x91BD,
		GL_MAX_COMPUTE_SHARED_MEMORY_SIZE = 0x8262,
		GL_MAX_COMPUTE_UNIFORM_COMPONENTS = 0x8263,
		GL_MAX_COMPUTE_ATOMIC_COUNTER_BUFFERS = 0x8264,
		GL_MAX_COMPUTE_ATOMIC_COUNTERS = 0x8265,
		GL_MAX_COMBINED_COMPUTE_UNIFORM_COMPONENTS = 0x8266,
		GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS = 0x90EB;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegeri_v, GetBooleani_v,
	 *  GetFloati_v, GetDoublei_v and GetInteger64i_v:
	 */
	public static final int GL_MAX_COMPUTE_WORK_GROUP_COUNT = 0x91BE,
		GL_MAX_COMPUTE_WORK_GROUP_SIZE = 0x91BF;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramiv: 
	 */
	public static final int GL_COMPUTE_WORK_GROUP_SIZE = 0x8267;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetActiveUniformBlockiv: 
	 */
	public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_COMPUTE_SHADER = 0x90EC;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetActiveAtomicCounterBufferiv: 
	 */
	public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_COMPUTE_SHADER = 0x90ED;

	/**
	 *  Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData, and
	 *  GetBufferPointerv:
	 */
	public static final int GL_DISPATCH_INDIRECT_BUFFER = 0x90EE;

	/**
	 *  Accepted by the &lt;value&gt; parameter of GetIntegerv, GetBooleanv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_DISPATCH_INDIRECT_BUFFER_BINDING = 0x90EF;

	/**
	 * Accepted by the &lt;stages&gt; parameter of UseProgramStages: 
	 */
	public static final int GL_COMPUTE_SHADER_BIT = 0x20;

	/**
	 *  Tokens accepted by the &lt;target&gt; parameters of Enable, Disable, and
	 *  IsEnabled:
	 */
	public static final int GL_DEBUG_OUTPUT = 0x92E0,
		GL_DEBUG_OUTPUT_SYNCHRONOUS = 0x8242;

	/**
	 * Returned by GetIntegerv when &lt;pname&gt; is CONTEXT_FLAGS: 
	 */
	public static final int GL_CONTEXT_FLAG_DEBUG_BIT = 0x2;

	/**
	 *  Tokens accepted by the &lt;value&gt; parameters of GetBooleanv, GetIntegerv,
	 *  GetFloatv, GetDoublev and GetInteger64v:
	 */
	public static final int GL_MAX_DEBUG_MESSAGE_LENGTH = 0x9143,
		GL_MAX_DEBUG_LOGGED_MESSAGES = 0x9144,
		GL_DEBUG_LOGGED_MESSAGES = 0x9145,
		GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH = 0x8243,
		GL_MAX_DEBUG_GROUP_STACK_DEPTH = 0x826C,
		GL_DEBUG_GROUP_STACK_DEPTH = 0x826D,
		GL_MAX_LABEL_LENGTH = 0x82E8;

	/**
	 * Tokens accepted by the &lt;pname&gt; parameter of GetPointerv: 
	 */
	public static final int GL_DEBUG_CALLBACK_FUNCTION = 0x8244,
		GL_DEBUG_CALLBACK_USER_PARAM = 0x8245;

	/**
	 *  Tokens accepted or provided by the &lt;source&gt; parameters of
	 *  DebugMessageControl, DebugMessageInsert and DEBUGPROC, and the &lt;sources&gt;
	 *  parameter of GetDebugMessageLog:
	 */
	public static final int GL_DEBUG_SOURCE_API = 0x8246,
		GL_DEBUG_SOURCE_WINDOW_SYSTEM = 0x8247,
		GL_DEBUG_SOURCE_SHADER_COMPILER = 0x8248,
		GL_DEBUG_SOURCE_THIRD_PARTY = 0x8249,
		GL_DEBUG_SOURCE_APPLICATION = 0x824A,
		GL_DEBUG_SOURCE_OTHER = 0x824B;

	/**
	 *  Tokens accepted or provided by the &lt;type&gt; parameters of
	 *  DebugMessageControl, DebugMessageInsert and DEBUGPROC, and the &lt;types&gt;
	 *  parameter of GetDebugMessageLog:
	 */
	public static final int GL_DEBUG_TYPE_ERROR = 0x824C,
		GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 0x824D,
		GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR = 0x824E,
		GL_DEBUG_TYPE_PORTABILITY = 0x824F,
		GL_DEBUG_TYPE_PERFORMANCE = 0x8250,
		GL_DEBUG_TYPE_OTHER = 0x8251,
		GL_DEBUG_TYPE_MARKER = 0x8268;

	/**
	 *  Tokens accepted or provided by the &lt;type&gt; parameters of
	 *  DebugMessageControl and DEBUGPROC, and the &lt;types&gt; parameter of
	 *  GetDebugMessageLog:
	 */
	public static final int GL_DEBUG_TYPE_PUSH_GROUP = 0x8269,
		GL_DEBUG_TYPE_POP_GROUP = 0x826A;

	/**
	 *  Tokens accepted or provided by the &lt;severity&gt; parameters of
	 *  DebugMessageControl, DebugMessageInsert and DEBUGPROC callback functions,
	 *  and the &lt;severities&gt; parameter of GetDebugMessageLog:
	 */
	public static final int GL_DEBUG_SEVERITY_HIGH = 0x9146,
		GL_DEBUG_SEVERITY_MEDIUM = 0x9147,
		GL_DEBUG_SEVERITY_LOW = 0x9148,
		GL_DEBUG_SEVERITY_NOTIFICATION = 0x826B;

	/**
	 *  Tokens accepted or provided by the &lt;identifier&gt; parameters of
	 *  ObjectLabel and GetObjectLabel:
	 */
	public static final int GL_BUFFER = 0x82E0,
		GL_SHADER = 0x82E1,
		GL_PROGRAM = 0x82E2,
		GL_QUERY = 0x82E3,
		GL_PROGRAM_PIPELINE = 0x82E4,
		GL_SAMPLER = 0x82E6,
		GL_DISPLAY_LIST = 0x82E7;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, GetDoublev, and GetInteger64v:
	 */
	public static final int GL_MAX_UNIFORM_LOCATIONS = 0x826E;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of FramebufferParameteri,
	 *  GetFramebufferParameteriv, NamedFramebufferParameteriEXT, and
	 *  GetNamedFramebufferParameterivEXT:
	 */
	public static final int GL_FRAMEBUFFER_DEFAULT_WIDTH = 0x9310,
		GL_FRAMEBUFFER_DEFAULT_HEIGHT = 0x9311,
		GL_FRAMEBUFFER_DEFAULT_LAYERS = 0x9312,
		GL_FRAMEBUFFER_DEFAULT_SAMPLES = 0x9313,
		GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS = 0x9314;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetBooleanv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_FRAMEBUFFER_WIDTH = 0x9315,
		GL_MAX_FRAMEBUFFER_HEIGHT = 0x9316,
		GL_MAX_FRAMEBUFFER_LAYERS = 0x9317,
		GL_MAX_FRAMEBUFFER_SAMPLES = 0x9318;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetInternalformativ
	 *  and GetInternalformati64v:
	 */
	public static final int GL_INTERNALFORMAT_SUPPORTED = 0x826F,
		GL_INTERNALFORMAT_PREFERRED = 0x8270,
		GL_INTERNALFORMAT_RED_SIZE = 0x8271,
		GL_INTERNALFORMAT_GREEN_SIZE = 0x8272,
		GL_INTERNALFORMAT_BLUE_SIZE = 0x8273,
		GL_INTERNALFORMAT_ALPHA_SIZE = 0x8274,
		GL_INTERNALFORMAT_DEPTH_SIZE = 0x8275,
		GL_INTERNALFORMAT_STENCIL_SIZE = 0x8276,
		GL_INTERNALFORMAT_SHARED_SIZE = 0x8277,
		GL_INTERNALFORMAT_RED_TYPE = 0x8278,
		GL_INTERNALFORMAT_GREEN_TYPE = 0x8279,
		GL_INTERNALFORMAT_BLUE_TYPE = 0x827A,
		GL_INTERNALFORMAT_ALPHA_TYPE = 0x827B,
		GL_INTERNALFORMAT_DEPTH_TYPE = 0x827C,
		GL_INTERNALFORMAT_STENCIL_TYPE = 0x827D,
		GL_MAX_WIDTH = 0x827E,
		GL_MAX_HEIGHT = 0x827F,
		GL_MAX_DEPTH = 0x8280,
		GL_MAX_LAYERS = 0x8281,
		GL_MAX_COMBINED_DIMENSIONS = 0x8282,
		GL_COLOR_COMPONENTS = 0x8283,
		GL_DEPTH_COMPONENTS = 0x8284,
		GL_STENCIL_COMPONENTS = 0x8285,
		GL_COLOR_RENDERABLE = 0x8286,
		GL_DEPTH_RENDERABLE = 0x8287,
		GL_STENCIL_RENDERABLE = 0x8288,
		GL_FRAMEBUFFER_RENDERABLE = 0x8289,
		GL_FRAMEBUFFER_RENDERABLE_LAYERED = 0x828A,
		GL_FRAMEBUFFER_BLEND = 0x828B,
		GL_READ_PIXELS = 0x828C,
		GL_READ_PIXELS_FORMAT = 0x828D,
		GL_READ_PIXELS_TYPE = 0x828E,
		GL_TEXTURE_IMAGE_FORMAT = 0x828F,
		GL_TEXTURE_IMAGE_TYPE = 0x8290,
		GL_GET_TEXTURE_IMAGE_FORMAT = 0x8291,
		GL_GET_TEXTURE_IMAGE_TYPE = 0x8292,
		GL_MIPMAP = 0x8293,
		GL_MANUAL_GENERATE_MIPMAP = 0x8294,
		GL_AUTO_GENERATE_MIPMAP = 0x8295,
		GL_COLOR_ENCODING = 0x8296,
		GL_SRGB_READ = 0x8297,
		GL_SRGB_WRITE = 0x8298,
		GL_SRGB_DECODE_ARB = 0x8299,
		GL_FILTER = 0x829A,
		GL_VERTEX_TEXTURE = 0x829B,
		GL_TESS_CONTROL_TEXTURE = 0x829C,
		GL_TESS_EVALUATION_TEXTURE = 0x829D,
		GL_GEOMETRY_TEXTURE = 0x829E,
		GL_FRAGMENT_TEXTURE = 0x829F,
		GL_COMPUTE_TEXTURE = 0x82A0,
		GL_TEXTURE_SHADOW = 0x82A1,
		GL_TEXTURE_GATHER = 0x82A2,
		GL_TEXTURE_GATHER_SHADOW = 0x82A3,
		GL_SHADER_IMAGE_LOAD = 0x82A4,
		GL_SHADER_IMAGE_STORE = 0x82A5,
		GL_SHADER_IMAGE_ATOMIC = 0x82A6,
		GL_IMAGE_TEXEL_SIZE = 0x82A7,
		GL_IMAGE_COMPATIBILITY_CLASS = 0x82A8,
		GL_IMAGE_PIXEL_FORMAT = 0x82A9,
		GL_IMAGE_PIXEL_TYPE = 0x82AA,
		GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_TEST = 0x82AC,
		GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_TEST = 0x82AD,
		GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_WRITE = 0x82AE,
		GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_WRITE = 0x82AF,
		GL_TEXTURE_COMPRESSED_BLOCK_WIDTH = 0x82B1,
		GL_TEXTURE_COMPRESSED_BLOCK_HEIGHT = 0x82B2,
		GL_TEXTURE_COMPRESSED_BLOCK_SIZE = 0x82B3,
		GL_CLEAR_BUFFER = 0x82B4,
		GL_TEXTURE_VIEW = 0x82B5,
		GL_VIEW_COMPATIBILITY_CLASS = 0x82B6;

	/**
	 *  Returned as possible responses for various &lt;pname&gt; queries
	 *  to GetInternalformativ and GetInternalformati64v
	 */
	public static final int GL_FULL_SUPPORT = 0x82B7,
		GL_CAVEAT_SUPPORT = 0x82B8,
		GL_IMAGE_CLASS_4_X_32 = 0x82B9,
		GL_IMAGE_CLASS_2_X_32 = 0x82BA,
		GL_IMAGE_CLASS_1_X_32 = 0x82BB,
		GL_IMAGE_CLASS_4_X_16 = 0x82BC,
		GL_IMAGE_CLASS_2_X_16 = 0x82BD,
		GL_IMAGE_CLASS_1_X_16 = 0x82BE,
		GL_IMAGE_CLASS_4_X_8 = 0x82BF,
		GL_IMAGE_CLASS_2_X_8 = 0x82C0,
		GL_IMAGE_CLASS_1_X_8 = 0x82C1,
		GL_IMAGE_CLASS_11_11_10 = 0x82C2,
		GL_IMAGE_CLASS_10_10_10_2 = 0x82C3,
		GL_VIEW_CLASS_128_BITS = 0x82C4,
		GL_VIEW_CLASS_96_BITS = 0x82C5,
		GL_VIEW_CLASS_64_BITS = 0x82C6,
		GL_VIEW_CLASS_48_BITS = 0x82C7,
		GL_VIEW_CLASS_32_BITS = 0x82C8,
		GL_VIEW_CLASS_24_BITS = 0x82C9,
		GL_VIEW_CLASS_16_BITS = 0x82CA,
		GL_VIEW_CLASS_8_BITS = 0x82CB,
		GL_VIEW_CLASS_S3TC_DXT1_RGB = 0x82CC,
		GL_VIEW_CLASS_S3TC_DXT1_RGBA = 0x82CD,
		GL_VIEW_CLASS_S3TC_DXT3_RGBA = 0x82CE,
		GL_VIEW_CLASS_S3TC_DXT5_RGBA = 0x82CF,
		GL_VIEW_CLASS_RGTC1_RED = 0x82D0,
		GL_VIEW_CLASS_RGTC2_RG = 0x82D1,
		GL_VIEW_CLASS_BPTC_UNORM = 0x82D2,
		GL_VIEW_CLASS_BPTC_FLOAT = 0x82D3;

	/**
	 *  Accepted by the &lt;programInterface&gt; parameter of GetProgramInterfaceiv,
	 *  GetProgramResourceIndex, GetProgramResourceName, GetProgramResourceiv,
	 *  GetProgramResourceLocation, and GetProgramResourceLocationIndex:
	 */
	public static final int GL_UNIFORM = 0x92E1,
		GL_UNIFORM_BLOCK = 0x92E2,
		GL_PROGRAM_INPUT = 0x92E3,
		GL_PROGRAM_OUTPUT = 0x92E4,
		GL_BUFFER_VARIABLE = 0x92E5,
		GL_SHADER_STORAGE_BLOCK = 0x92E6,
		GL_VERTEX_SUBROUTINE = 0x92E8,
		GL_TESS_CONTROL_SUBROUTINE = 0x92E9,
		GL_TESS_EVALUATION_SUBROUTINE = 0x92EA,
		GL_GEOMETRY_SUBROUTINE = 0x92EB,
		GL_FRAGMENT_SUBROUTINE = 0x92EC,
		GL_COMPUTE_SUBROUTINE = 0x92ED,
		GL_VERTEX_SUBROUTINE_UNIFORM = 0x92EE,
		GL_TESS_CONTROL_SUBROUTINE_UNIFORM = 0x92EF,
		GL_TESS_EVALUATION_SUBROUTINE_UNIFORM = 0x92F0,
		GL_GEOMETRY_SUBROUTINE_UNIFORM = 0x92F1,
		GL_FRAGMENT_SUBROUTINE_UNIFORM = 0x92F2,
		GL_COMPUTE_SUBROUTINE_UNIFORM = 0x92F3,
		GL_TRANSFORM_FEEDBACK_VARYING = 0x92F4;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramInterfaceiv: 
	 */
	public static final int GL_ACTIVE_RESOURCES = 0x92F5,
		GL_MAX_NAME_LENGTH = 0x92F6,
		GL_MAX_NUM_ACTIVE_VARIABLES = 0x92F7,
		GL_MAX_NUM_COMPATIBLE_SUBROUTINES = 0x92F8;

	/**
	 * Accepted in the &lt;props&gt; array of GetProgramResourceiv: 
	 */
	public static final int GL_NAME_LENGTH = 0x92F9,
		GL_TYPE = 0x92FA,
		GL_ARRAY_SIZE = 0x92FB,
		GL_OFFSET = 0x92FC,
		GL_BLOCK_INDEX = 0x92FD,
		GL_ARRAY_STRIDE = 0x92FE,
		GL_MATRIX_STRIDE = 0x92FF,
		GL_IS_ROW_MAJOR = 0x9300,
		GL_ATOMIC_COUNTER_BUFFER_INDEX = 0x9301,
		GL_BUFFER_BINDING = 0x9302,
		GL_BUFFER_DATA_SIZE = 0x9303,
		GL_NUM_ACTIVE_VARIABLES = 0x9304,
		GL_ACTIVE_VARIABLES = 0x9305,
		GL_REFERENCED_BY_VERTEX_SHADER = 0x9306,
		GL_REFERENCED_BY_TESS_CONTROL_SHADER = 0x9307,
		GL_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x9308,
		GL_REFERENCED_BY_GEOMETRY_SHADER = 0x9309,
		GL_REFERENCED_BY_FRAGMENT_SHADER = 0x930A,
		GL_REFERENCED_BY_COMPUTE_SHADER = 0x930B,
		GL_TOP_LEVEL_ARRAY_SIZE = 0x930C,
		GL_TOP_LEVEL_ARRAY_STRIDE = 0x930D,
		GL_LOCATION = 0x930E,
		GL_LOCATION_INDEX = 0x930F,
		GL_IS_PER_PATCH = 0x92E7;

	/**
	 *  Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData, and
	 *  GetBufferPointerv:
	 */
	public static final int GL_SHADER_STORAGE_BUFFER = 0x90D2;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetIntegeri_v,
	 *  GetBooleanv, GetInteger64v, GetFloatv, GetDoublev, GetBooleani_v,
	 *  GetIntegeri_v, GetFloati_v, GetDoublei_v, and GetInteger64i_v:
	 */
	public static final int GL_SHADER_STORAGE_BUFFER_BINDING = 0x90D3;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegeri_v, GetBooleani_v,
	 *  GetIntegeri_v, GetFloati_v, GetDoublei_v, and GetInteger64i_v:
	 */
	public static final int GL_SHADER_STORAGE_BUFFER_START = 0x90D4,
		GL_SHADER_STORAGE_BUFFER_SIZE = 0x90D5;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetBooleanv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_VERTEX_SHADER_STORAGE_BLOCKS = 0x90D6,
		GL_MAX_GEOMETRY_SHADER_STORAGE_BLOCKS = 0x90D7,
		GL_MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS = 0x90D8,
		GL_MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS = 0x90D9,
		GL_MAX_FRAGMENT_SHADER_STORAGE_BLOCKS = 0x90DA,
		GL_MAX_COMPUTE_SHADER_STORAGE_BLOCKS = 0x90DB,
		GL_MAX_COMBINED_SHADER_STORAGE_BLOCKS = 0x90DC,
		GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS = 0x90DD,
		GL_MAX_SHADER_STORAGE_BLOCK_SIZE = 0x90DE,
		GL_SHADER_STORAGE_BUFFER_OFFSET_ALIGNMENT = 0x90DF;

	/**
	 * Accepted in the &lt;barriers&gt; bitfield in glMemoryBarrier: 
	 */
	public static final int GL_SHADER_STORAGE_BARRIER_BIT = 0x2000;

	/**
	 *  Alias for the existing token
	 *  MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS:
	 */
	public static final int GL_MAX_COMBINED_SHADER_OUTPUT_RESOURCES = 0x8F39;

	/**
	 * Accepted by the &lt;pname&gt; parameter of TexParameter* and GetTexParameter*: 
	 */
	public static final int GL_DEPTH_STENCIL_TEXTURE_MODE = 0x90EA;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetTexLevelParameter: 
	 */
	public static final int GL_TEXTURE_BUFFER_OFFSET = 0x919D,
		GL_TEXTURE_BUFFER_SIZE = 0x919E;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT = 0x919F;

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetTexParameterfv and
	 *  GetTexParameteriv:
	 */
	public static final int GL_TEXTURE_VIEW_MIN_LEVEL = 0x82DB,
		GL_TEXTURE_VIEW_NUM_LEVELS = 0x82DC,
		GL_TEXTURE_VIEW_MIN_LAYER = 0x82DD,
		GL_TEXTURE_VIEW_NUM_LAYERS = 0x82DE,
		GL_TEXTURE_IMMUTABLE_LEVELS = 0x82DF;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetVertexAttrib*v: 
	 */
	public static final int GL_VERTEX_ATTRIB_BINDING = 0x82D4,
		GL_VERTEX_ATTRIB_RELATIVE_OFFSET = 0x82D5;

	/**
	 *  Accepted by the &lt;target&gt; parameter of GetBooleani_v, GetIntegeri_v,
	 *  GetFloati_v, GetDoublei_v, and GetInteger64i_v:
	 */
	public static final int GL_VERTEX_BINDING_DIVISOR = 0x82D6,
		GL_VERTEX_BINDING_OFFSET = 0x82D7,
		GL_VERTEX_BINDING_STRIDE = 0x82D8;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegerv, ... 
	 */
	public static final int GL_MAX_VERTEX_ATTRIB_RELATIVE_OFFSET = 0x82D9,
		GL_MAX_VERTEX_ATTRIB_BINDINGS = 0x82DA;

	private GL43() {}

	public static void glClearBufferData(int target, int internalformat, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, 1);
		nglClearBufferData(target, internalformat, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglClearBufferData(int target, int internalformat, int format, int type, long data, long function_pointer);

	public static void glClearBufferSubData(int target, int internalformat, long offset, long size, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, 1);
		nglClearBufferSubData(target, internalformat, offset, size, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglClearBufferSubData(int target, int internalformat, long offset, long size, int format, int type, long data, long function_pointer);

	public static void glDispatchCompute(int num_groups_x, int num_groups_y, int num_groups_z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDispatchCompute;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDispatchCompute(num_groups_x, num_groups_y, num_groups_z, function_pointer);
	}
	static native void nglDispatchCompute(int num_groups_x, int num_groups_y, int num_groups_z, long function_pointer);

	public static void glDispatchComputeIndirect(long indirect) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDispatchComputeIndirect;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDispatchComputeIndirect(indirect, function_pointer);
	}
	static native void nglDispatchComputeIndirect(long indirect, long function_pointer);

	public static void glCopyImageSubData(int srcName, int srcTarget, int srcLevel, int srcX, int srcY, int srcZ, int dstName, int dstTarget, int dstLevel, int dstX, int dstY, int dstZ, int srcWidth, int srcHeight, int srcDepth) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyImageSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyImageSubData(srcName, srcTarget, srcLevel, srcX, srcY, srcZ, dstName, dstTarget, dstLevel, dstX, dstY, dstZ, srcWidth, srcHeight, srcDepth, function_pointer);
	}
	static native void nglCopyImageSubData(int srcName, int srcTarget, int srcLevel, int srcX, int srcY, int srcZ, int dstName, int dstTarget, int dstLevel, int dstX, int dstY, int dstZ, int srcWidth, int srcHeight, int srcDepth, long function_pointer);

	public static void glDebugMessageControl(int source, int type, int severity, IntBuffer ids, boolean enabled) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDebugMessageControl;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (ids != null)
			BufferChecks.checkDirect(ids);
		nglDebugMessageControl(source, type, severity, (ids == null ? 0 : ids.remaining()), MemoryUtil.getAddressSafe(ids), enabled, function_pointer);
	}
	static native void nglDebugMessageControl(int source, int type, int severity, int ids_count, long ids, boolean enabled, long function_pointer);

	public static void glDebugMessageInsert(int source, int type, int id, int severity, ByteBuffer buf) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDebugMessageInsert;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(buf);
		nglDebugMessageInsert(source, type, id, severity, buf.remaining(), MemoryUtil.getAddress(buf), function_pointer);
	}
	static native void nglDebugMessageInsert(int source, int type, int id, int severity, int buf_length, long buf, long function_pointer);

	/** Overloads glDebugMessageInsert. */
	public static void glDebugMessageInsert(int source, int type, int id, int severity, CharSequence buf) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDebugMessageInsert;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDebugMessageInsert(source, type, id, severity, buf.length(), APIUtil.getBuffer(caps, buf), function_pointer);
	}

	/**
	 *  The {@code KHRDebugCallback.Handler} implementation passed to this method will be used for
	 *  KHR_debug messages. If callback is null, any previously registered handler for the current
	 *  thread will be unregistered and stop receiving messages.
	 * <p>
	 *  @param callback the callback function to use
	 */
	public static void glDebugMessageCallback(KHRDebugCallback callback) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDebugMessageCallback;
		BufferChecks.checkFunctionAddress(function_pointer);
		long userParam = callback == null ? 0 : CallbackUtil.createGlobalRef(callback.getHandler());
		CallbackUtil.registerContextCallbackKHR(userParam);
		nglDebugMessageCallback(callback == null ? 0 : callback.getPointer(), userParam, function_pointer);
	}
	static native void nglDebugMessageCallback(long callback, long userParam, long function_pointer);

	public static int glGetDebugMessageLog(int count, IntBuffer sources, IntBuffer types, IntBuffer ids, IntBuffer severities, IntBuffer lengths, ByteBuffer messageLog) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetDebugMessageLog;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (sources != null)
			BufferChecks.checkBuffer(sources, count);
		if (types != null)
			BufferChecks.checkBuffer(types, count);
		if (ids != null)
			BufferChecks.checkBuffer(ids, count);
		if (severities != null)
			BufferChecks.checkBuffer(severities, count);
		if (lengths != null)
			BufferChecks.checkBuffer(lengths, count);
		if (messageLog != null)
			BufferChecks.checkDirect(messageLog);
		int __result = nglGetDebugMessageLog(count, (messageLog == null ? 0 : messageLog.remaining()), MemoryUtil.getAddressSafe(sources), MemoryUtil.getAddressSafe(types), MemoryUtil.getAddressSafe(ids), MemoryUtil.getAddressSafe(severities), MemoryUtil.getAddressSafe(lengths), MemoryUtil.getAddressSafe(messageLog), function_pointer);
		return __result;
	}
	static native int nglGetDebugMessageLog(int count, int messageLog_bufsize, long sources, long types, long ids, long severities, long lengths, long messageLog, long function_pointer);

	public static void glPushDebugGroup(int source, int id, ByteBuffer message) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPushDebugGroup;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(message);
		nglPushDebugGroup(source, id, message.remaining(), MemoryUtil.getAddress(message), function_pointer);
	}
	static native void nglPushDebugGroup(int source, int id, int message_length, long message, long function_pointer);

	/** Overloads glPushDebugGroup. */
	public static void glPushDebugGroup(int source, int id, CharSequence message) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPushDebugGroup;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPushDebugGroup(source, id, message.length(), APIUtil.getBuffer(caps, message), function_pointer);
	}

	public static void glPopDebugGroup() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPopDebugGroup;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPopDebugGroup(function_pointer);
	}
	static native void nglPopDebugGroup(long function_pointer);

	public static void glObjectLabel(int identifier, int name, ByteBuffer label) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glObjectLabel;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (label != null)
			BufferChecks.checkDirect(label);
		nglObjectLabel(identifier, name, (label == null ? 0 : label.remaining()), MemoryUtil.getAddressSafe(label), function_pointer);
	}
	static native void nglObjectLabel(int identifier, int name, int label_length, long label, long function_pointer);

	/** Overloads glObjectLabel. */
	public static void glObjectLabel(int identifier, int name, CharSequence label) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glObjectLabel;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglObjectLabel(identifier, name, label.length(), APIUtil.getBuffer(caps, label), function_pointer);
	}

	public static void glGetObjectLabel(int identifier, int name, IntBuffer length, ByteBuffer label) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetObjectLabel;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(label);
		nglGetObjectLabel(identifier, name, label.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(label), function_pointer);
	}
	static native void nglGetObjectLabel(int identifier, int name, int label_bufSize, long length, long label, long function_pointer);

	/** Overloads glGetObjectLabel. */
	public static String glGetObjectLabel(int identifier, int name, int bufSize) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetObjectLabel;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer label_length = APIUtil.getLengths(caps);
		ByteBuffer label = APIUtil.getBufferByte(caps, bufSize);
		nglGetObjectLabel(identifier, name, bufSize, MemoryUtil.getAddress0(label_length), MemoryUtil.getAddress(label), function_pointer);
		label.limit(label_length.get(0));
		return APIUtil.getString(caps, label);
	}

	public static void glObjectPtrLabel(PointerWrapper ptr, ByteBuffer label) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glObjectPtrLabel;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (label != null)
			BufferChecks.checkDirect(label);
		nglObjectPtrLabel(ptr.getPointer(), (label == null ? 0 : label.remaining()), MemoryUtil.getAddressSafe(label), function_pointer);
	}
	static native void nglObjectPtrLabel(long ptr, int label_length, long label, long function_pointer);

	/** Overloads glObjectPtrLabel. */
	public static void glObjectPtrLabel(PointerWrapper ptr, CharSequence label) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glObjectPtrLabel;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglObjectPtrLabel(ptr.getPointer(), label.length(), APIUtil.getBuffer(caps, label), function_pointer);
	}

	public static void glGetObjectPtrLabel(PointerWrapper ptr, IntBuffer length, ByteBuffer label) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetObjectPtrLabel;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(label);
		nglGetObjectPtrLabel(ptr.getPointer(), label.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(label), function_pointer);
	}
	static native void nglGetObjectPtrLabel(long ptr, int label_bufSize, long length, long label, long function_pointer);

	/** Overloads glGetObjectPtrLabel. */
	public static String glGetObjectPtrLabel(PointerWrapper ptr, int bufSize) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetObjectPtrLabel;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer label_length = APIUtil.getLengths(caps);
		ByteBuffer label = APIUtil.getBufferByte(caps, bufSize);
		nglGetObjectPtrLabel(ptr.getPointer(), bufSize, MemoryUtil.getAddress0(label_length), MemoryUtil.getAddress(label), function_pointer);
		label.limit(label_length.get(0));
		return APIUtil.getString(caps, label);
	}

	public static void glFramebufferParameteri(int target, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFramebufferParameteri;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferParameteri(target, pname, param, function_pointer);
	}
	static native void nglFramebufferParameteri(int target, int pname, int param, long function_pointer);

	public static void glGetFramebufferParameter(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFramebufferParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetFramebufferParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetFramebufferParameteriv(int target, int pname, long params, long function_pointer);

	/** Overloads glGetFramebufferParameteriv. */
	public static int glGetFramebufferParameteri(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFramebufferParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetFramebufferParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetInternalformat(int target, int internalformat, int pname, LongBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetInternalformati64v;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetInternalformati64v(target, internalformat, pname, params.remaining(), MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetInternalformati64v(int target, int internalformat, int pname, int params_bufSize, long params, long function_pointer);

	/** Overloads glGetInternalformati64v. */
	public static long glGetInternalformati64(int target, int internalformat, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetInternalformati64v;
		BufferChecks.checkFunctionAddress(function_pointer);
		LongBuffer params = APIUtil.getBufferLong(caps);
		nglGetInternalformati64v(target, internalformat, pname, 1, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glInvalidateTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInvalidateTexSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglInvalidateTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, function_pointer);
	}
	static native void nglInvalidateTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, long function_pointer);

	public static void glInvalidateTexImage(int texture, int level) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInvalidateTexImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglInvalidateTexImage(texture, level, function_pointer);
	}
	static native void nglInvalidateTexImage(int texture, int level, long function_pointer);

	public static void glInvalidateBufferSubData(int buffer, long offset, long length) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInvalidateBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglInvalidateBufferSubData(buffer, offset, length, function_pointer);
	}
	static native void nglInvalidateBufferSubData(int buffer, long offset, long length, long function_pointer);

	public static void glInvalidateBufferData(int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInvalidateBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglInvalidateBufferData(buffer, function_pointer);
	}
	static native void nglInvalidateBufferData(int buffer, long function_pointer);

	public static void glInvalidateFramebuffer(int target, IntBuffer attachments) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInvalidateFramebuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(attachments);
		nglInvalidateFramebuffer(target, attachments.remaining(), MemoryUtil.getAddress(attachments), function_pointer);
	}
	static native void nglInvalidateFramebuffer(int target, int attachments_numAttachments, long attachments, long function_pointer);

	public static void glInvalidateSubFramebuffer(int target, IntBuffer attachments, int x, int y, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInvalidateSubFramebuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(attachments);
		nglInvalidateSubFramebuffer(target, attachments.remaining(), MemoryUtil.getAddress(attachments), x, y, width, height, function_pointer);
	}
	static native void nglInvalidateSubFramebuffer(int target, int attachments_numAttachments, long attachments, int x, int y, int width, int height, long function_pointer);

	public static void glMultiDrawArraysIndirect(int mode, ByteBuffer indirect, int primcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawArraysIndirect;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, (stride == 0 ? 4 * 4 : stride) * primcount);
		nglMultiDrawArraysIndirect(mode, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
	}
	static native void nglMultiDrawArraysIndirect(int mode, long indirect, int primcount, int stride, long function_pointer);
	public static void glMultiDrawArraysIndirect(int mode, long indirect_buffer_offset, int primcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawArraysIndirect;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOenabled(caps);
		nglMultiDrawArraysIndirectBO(mode, indirect_buffer_offset, primcount, stride, function_pointer);
	}
	static native void nglMultiDrawArraysIndirectBO(int mode, long indirect_buffer_offset, int primcount, int stride, long function_pointer);

	/** Overloads glMultiDrawArraysIndirect. */
	public static void glMultiDrawArraysIndirect(int mode, IntBuffer indirect, int primcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawArraysIndirect;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, (stride == 0 ? 4 : stride >> 2) * primcount);
		nglMultiDrawArraysIndirect(mode, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
	}

	public static void glMultiDrawElementsIndirect(int mode, int type, ByteBuffer indirect, int primcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawElementsIndirect;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, (stride == 0 ? 5 * 4 : stride) * primcount);
		nglMultiDrawElementsIndirect(mode, type, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
	}
	static native void nglMultiDrawElementsIndirect(int mode, int type, long indirect, int primcount, int stride, long function_pointer);
	public static void glMultiDrawElementsIndirect(int mode, int type, long indirect_buffer_offset, int primcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawElementsIndirect;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOenabled(caps);
		nglMultiDrawElementsIndirectBO(mode, type, indirect_buffer_offset, primcount, stride, function_pointer);
	}
	static native void nglMultiDrawElementsIndirectBO(int mode, int type, long indirect_buffer_offset, int primcount, int stride, long function_pointer);

	/** Overloads glMultiDrawElementsIndirect. */
	public static void glMultiDrawElementsIndirect(int mode, int type, IntBuffer indirect, int primcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawElementsIndirect;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, (stride == 0 ? 5 : stride >> 2) * primcount);
		nglMultiDrawElementsIndirect(mode, type, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
	}

	public static void glGetProgramInterface(int program, int programInterface, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramInterfaceiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetProgramInterfaceiv(program, programInterface, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramInterfaceiv(int program, int programInterface, int pname, long params, long function_pointer);

	/** Overloads glGetProgramInterfaceiv. */
	public static int glGetProgramInterfacei(int program, int programInterface, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramInterfaceiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetProgramInterfaceiv(program, programInterface, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static int glGetProgramResourceIndex(int program, int programInterface, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramResourceIndex;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetProgramResourceIndex(program, programInterface, MemoryUtil.getAddress(name), function_pointer);
		return __result;
	}
	static native int nglGetProgramResourceIndex(int program, int programInterface, long name, long function_pointer);

	/** Overloads glGetProgramResourceIndex. */
	public static int glGetProgramResourceIndex(int program, int programInterface, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramResourceIndex;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetProgramResourceIndex(program, programInterface, APIUtil.getBufferNT(caps, name), function_pointer);
		return __result;
	}

	public static void glGetProgramResourceName(int program, int programInterface, int index, IntBuffer length, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramResourceName;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		if (name != null)
			BufferChecks.checkDirect(name);
		nglGetProgramResourceName(program, programInterface, index, (name == null ? 0 : name.remaining()), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddressSafe(name), function_pointer);
	}
	static native void nglGetProgramResourceName(int program, int programInterface, int index, int name_bufSize, long length, long name, long function_pointer);

	/** Overloads glGetProgramResourceName. */
	public static String glGetProgramResourceName(int program, int programInterface, int index, int bufSize) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramResourceName;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer name_length = APIUtil.getLengths(caps);
		ByteBuffer name = APIUtil.getBufferByte(caps, bufSize);
		nglGetProgramResourceName(program, programInterface, index, bufSize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(name), function_pointer);
		name.limit(name_length.get(0));
		return APIUtil.getString(caps, name);
	}

	public static void glGetProgramResource(int program, int programInterface, int index, IntBuffer props, IntBuffer length, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramResourceiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(props);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(params);
		nglGetProgramResourceiv(program, programInterface, index, props.remaining(), MemoryUtil.getAddress(props), params.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramResourceiv(int program, int programInterface, int index, int props_propCount, long props, int params_bufSize, long length, long params, long function_pointer);

	public static int glGetProgramResourceLocation(int program, int programInterface, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramResourceLocation;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetProgramResourceLocation(program, programInterface, MemoryUtil.getAddress(name), function_pointer);
		return __result;
	}
	static native int nglGetProgramResourceLocation(int program, int programInterface, long name, long function_pointer);

	/** Overloads glGetProgramResourceLocation. */
	public static int glGetProgramResourceLocation(int program, int programInterface, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramResourceLocation;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetProgramResourceLocation(program, programInterface, APIUtil.getBufferNT(caps, name), function_pointer);
		return __result;
	}

	public static int glGetProgramResourceLocationIndex(int program, int programInterface, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramResourceLocationIndex;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetProgramResourceLocationIndex(program, programInterface, MemoryUtil.getAddress(name), function_pointer);
		return __result;
	}
	static native int nglGetProgramResourceLocationIndex(int program, int programInterface, long name, long function_pointer);

	/** Overloads glGetProgramResourceLocationIndex. */
	public static int glGetProgramResourceLocationIndex(int program, int programInterface, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramResourceLocationIndex;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetProgramResourceLocationIndex(program, programInterface, APIUtil.getBufferNT(caps, name), function_pointer);
		return __result;
	}

	public static void glShaderStorageBlockBinding(int program, int storageBlockIndex, int storageBlockBinding) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glShaderStorageBlockBinding;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglShaderStorageBlockBinding(program, storageBlockIndex, storageBlockBinding, function_pointer);
	}
	static native void nglShaderStorageBlockBinding(int program, int storageBlockIndex, int storageBlockBinding, long function_pointer);

	public static void glTexBufferRange(int target, int internalformat, int buffer, long offset, long size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexBufferRange;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexBufferRange(target, internalformat, buffer, offset, size, function_pointer);
	}
	static native void nglTexBufferRange(int target, int internalformat, int buffer, long offset, long size, long function_pointer);

	public static void glTexStorage2DMultisample(int target, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexStorage2DMultisample;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexStorage2DMultisample(target, samples, internalformat, width, height, fixedsamplelocations, function_pointer);
	}
	static native void nglTexStorage2DMultisample(int target, int samples, int internalformat, int width, int height, boolean fixedsamplelocations, long function_pointer);

	public static void glTexStorage3DMultisample(int target, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexStorage3DMultisample;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexStorage3DMultisample(target, samples, internalformat, width, height, depth, fixedsamplelocations, function_pointer);
	}
	static native void nglTexStorage3DMultisample(int target, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations, long function_pointer);

	public static void glTextureView(int texture, int target, int origtexture, int internalformat, int minlevel, int numlevels, int minlayer, int numlayers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureView;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureView(texture, target, origtexture, internalformat, minlevel, numlevels, minlayer, numlayers, function_pointer);
	}
	static native void nglTextureView(int texture, int target, int origtexture, int internalformat, int minlevel, int numlevels, int minlayer, int numlayers, long function_pointer);

	public static void glBindVertexBuffer(int bindingindex, int buffer, long offset, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindVertexBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindVertexBuffer(bindingindex, buffer, offset, stride, function_pointer);
	}
	static native void nglBindVertexBuffer(int bindingindex, int buffer, long offset, int stride, long function_pointer);

	public static void glVertexAttribFormat(int attribindex, int size, int type, boolean normalized, int relativeoffset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribFormat;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribFormat(attribindex, size, type, normalized, relativeoffset, function_pointer);
	}
	static native void nglVertexAttribFormat(int attribindex, int size, int type, boolean normalized, int relativeoffset, long function_pointer);

	public static void glVertexAttribIFormat(int attribindex, int size, int type, int relativeoffset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribIFormat;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribIFormat(attribindex, size, type, relativeoffset, function_pointer);
	}
	static native void nglVertexAttribIFormat(int attribindex, int size, int type, int relativeoffset, long function_pointer);

	public static void glVertexAttribLFormat(int attribindex, int size, int type, int relativeoffset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribLFormat;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribLFormat(attribindex, size, type, relativeoffset, function_pointer);
	}
	static native void nglVertexAttribLFormat(int attribindex, int size, int type, int relativeoffset, long function_pointer);

	public static void glVertexAttribBinding(int attribindex, int bindingindex) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribBinding;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribBinding(attribindex, bindingindex, function_pointer);
	}
	static native void nglVertexAttribBinding(int attribindex, int bindingindex, long function_pointer);

	public static void glVertexBindingDivisor(int bindingindex, int divisor) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexBindingDivisor;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexBindingDivisor(bindingindex, divisor, function_pointer);
	}
	static native void nglVertexBindingDivisor(int bindingindex, int divisor, long function_pointer);
}
