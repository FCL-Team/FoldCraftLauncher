/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class GL30 {

	public static final int GL_MAJOR_VERSION = 0x821B,
		GL_MINOR_VERSION = 0x821C,
		GL_NUM_EXTENSIONS = 0x821D,
		GL_CONTEXT_FLAGS = 0x821E,
		GL_CONTEXT_FLAG_FORWARD_COMPATIBLE_BIT = 0x1,
		GL_DEPTH_BUFFER = 0x8223,
		GL_STENCIL_BUFFER = 0x8224,
		GL_COMPRESSED_RED = 0x8225,
		GL_COMPRESSED_RG = 0x8226,
		GL_COMPARE_REF_TO_TEXTURE = 0x884E,
		GL_CLIP_DISTANCE0 = 0x3000,
		GL_CLIP_DISTANCE1 = 0x3001,
		GL_CLIP_DISTANCE2 = 0x3002,
		GL_CLIP_DISTANCE3 = 0x3003,
		GL_CLIP_DISTANCE4 = 0x3004,
		GL_CLIP_DISTANCE5 = 0x3005,
		GL_CLIP_DISTANCE6 = 0x3006,
		GL_CLIP_DISTANCE7 = 0x3007,
		GL_MAX_CLIP_DISTANCES = 0xD32,
		GL_MAX_VARYING_COMPONENTS = 0x8B4B,
		GL_BUFFER_ACCESS_FLAGS = 0x911F,
		GL_BUFFER_MAP_LENGTH = 0x9120,
		GL_BUFFER_MAP_OFFSET = 0x9121;

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetVertexAttribdv,
	 *  GetVertexAttribfv, GetVertexAttribiv, GetVertexAttribIiv, and
	 *  GetVertexAttribIuiv:
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_INTEGER = 0x88FD;

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveUniform: 
	 */
	public static final int GL_SAMPLER_BUFFER = 0x8DC2,
		GL_SAMPLER_CUBE_SHADOW = 0x8DC5,
		GL_UNSIGNED_INT_VEC2 = 0x8DC6,
		GL_UNSIGNED_INT_VEC3 = 0x8DC7,
		GL_UNSIGNED_INT_VEC4 = 0x8DC8,
		GL_INT_SAMPLER_1D = 0x8DC9,
		GL_INT_SAMPLER_2D = 0x8DCA,
		GL_INT_SAMPLER_3D = 0x8DCB,
		GL_INT_SAMPLER_CUBE = 0x8DCC,
		GL_INT_SAMPLER_2D_RECT = 0x8DCD,
		GL_INT_SAMPLER_1D_ARRAY = 0x8DCE,
		GL_INT_SAMPLER_2D_ARRAY = 0x8DCF,
		GL_INT_SAMPLER_BUFFER = 0x8DD0,
		GL_UNSIGNED_INT_SAMPLER_1D = 0x8DD1,
		GL_UNSIGNED_INT_SAMPLER_2D = 0x8DD2,
		GL_UNSIGNED_INT_SAMPLER_3D = 0x8DD3,
		GL_UNSIGNED_INT_SAMPLER_CUBE = 0x8DD4,
		GL_UNSIGNED_INT_SAMPLER_2D_RECT = 0x8DD5,
		GL_UNSIGNED_INT_SAMPLER_1D_ARRAY = 0x8DD6,
		GL_UNSIGNED_INT_SAMPLER_2D_ARRAY = 0x8DD7,
		GL_UNSIGNED_INT_SAMPLER_BUFFER = 0x8DD8;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_MIN_PROGRAM_TEXEL_OFFSET = 0x8904,
		GL_MAX_PROGRAM_TEXEL_OFFSET = 0x8905;

	/**
	 * Accepted by the &lt;mode&gt; parameter of BeginConditionalRender: 
	 */
	public static final int GL_QUERY_WAIT = 0x8E13,
		GL_QUERY_NO_WAIT = 0x8E14,
		GL_QUERY_BY_REGION_WAIT = 0x8E15,
		GL_QUERY_BY_REGION_NO_WAIT = 0x8E16;

	/**
	 * Accepted by the &lt;access&gt; parameter of MapBufferRange: 
	 */
	public static final int GL_MAP_READ_BIT = 0x1,
		GL_MAP_WRITE_BIT = 0x2,
		GL_MAP_INVALIDATE_RANGE_BIT = 0x4,
		GL_MAP_INVALIDATE_BUFFER_BIT = 0x8,
		GL_MAP_FLUSH_EXPLICIT_BIT = 0x10,
		GL_MAP_UNSYNCHRONIZED_BIT = 0x20;

	/**
	 *  Accepted by the &lt;target&gt; parameter of ClampColor and the &lt;pname&gt;
	 *  parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev.
	 */
	public static final int GL_CLAMP_VERTEX_COLOR = 0x891A,
		GL_CLAMP_FRAGMENT_COLOR = 0x891B,
		GL_CLAMP_READ_COLOR = 0x891C;

	/**
	 * Accepted by the &lt;clamp&gt; parameter of ClampColor. 
	 */
	public static final int GL_FIXED_ONLY = 0x891D;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of TexImage1D, TexImage2D,
	 *  TexImage3D, CopyTexImage1D, CopyTexImage2D, and RenderbufferStorageEXT,
	 *  and returned in the &lt;data&gt; parameter of GetTexLevelParameter and
	 *  GetRenderbufferParameterivEXT:
	 */
	public static final int GL_DEPTH_COMPONENT32F = 0x8CAC,
		GL_DEPTH32F_STENCIL8 = 0x8CAD;

	/**
	 *  Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels, TexImage1D,
	 *  TexImage2D, TexImage3D, TexSubImage1D, TexSubImage2D, TexSubImage3D, and
	 *  GetTexImage:
	 */
	public static final int GL_FLOAT_32_UNSIGNED_INT_24_8_REV = 0x8DAD;

	/**
	 * Accepted by the &lt;value&gt; parameter of GetTexLevelParameter: 
	 */
	public static final int GL_TEXTURE_RED_TYPE = 0x8C10,
		GL_TEXTURE_GREEN_TYPE = 0x8C11,
		GL_TEXTURE_BLUE_TYPE = 0x8C12,
		GL_TEXTURE_ALPHA_TYPE = 0x8C13,
		GL_TEXTURE_LUMINANCE_TYPE = 0x8C14,
		GL_TEXTURE_INTENSITY_TYPE = 0x8C15,
		GL_TEXTURE_DEPTH_TYPE = 0x8C16;

	/**
	 * Returned by the &lt;params&gt; parameter of GetTexLevelParameter: 
	 */
	public static final int GL_UNSIGNED_NORMALIZED = 0x8C17;

	/**
	 *  Accepted by the &lt;internalFormat&gt; parameter of TexImage1D,
	 *  TexImage2D, and TexImage3D:
	 */
	public static final int GL_RGBA32F = 0x8814,
		GL_RGB32F = 0x8815,
		GL_ALPHA32F = 0x8816,
		GL_RGBA16F = 0x881A,
		GL_RGB16F = 0x881B,
		GL_ALPHA16F = 0x881C;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of TexImage1D,
	 *  TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 *  RenderbufferStorage:
	 */
	public static final int GL_R11F_G11F_B10F = 0x8C3A;

	/**
	 *  Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels,
	 *  TexImage1D, TexImage2D, GetTexImage, TexImage3D, TexSubImage1D,
	 *  TexSubImage2D, TexSubImage3D, GetHistogram, GetMinmax,
	 *  ConvolutionFilter1D, ConvolutionFilter2D, ConvolutionFilter3D,
	 *  GetConvolutionFilter, SeparableFilter2D, GetSeparableFilter,
	 *  ColorTable, ColorSubTable, and GetColorTable:
	 */
	public static final int GL_UNSIGNED_INT_10F_11F_11F_REV = 0x8C3B;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of TexImage1D,
	 *  TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 *  RenderbufferStorage:
	 */
	public static final int GL_RGB9_E5 = 0x8C3D;

	/**
	 *  Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels,
	 *  TexImage1D, TexImage2D, GetTexImage, TexImage3D, TexSubImage1D,
	 *  TexSubImage2D, TexSubImage3D, GetHistogram, GetMinmax,
	 *  ConvolutionFilter1D, ConvolutionFilter2D, ConvolutionFilter3D,
	 *  GetConvolutionFilter, SeparableFilter2D, GetSeparableFilter,
	 *  ColorTable, ColorSubTable, and GetColorTable:
	 */
	public static final int GL_UNSIGNED_INT_5_9_9_9_REV = 0x8C3E;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetTexLevelParameterfv and
	 *  GetTexLevelParameteriv:
	 */
	public static final int GL_TEXTURE_SHARED_SIZE = 0x8C3F;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindFramebuffer,
	 *  CheckFramebufferStatus, FramebufferTexture{1D|2D|3D},
	 *  FramebufferRenderbuffer, and
	 *  GetFramebufferAttachmentParameteriv:
	 */
	public static final int GL_FRAMEBUFFER = 0x8D40,
		GL_READ_FRAMEBUFFER = 0x8CA8,
		GL_DRAW_FRAMEBUFFER = 0x8CA9;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindRenderbuffer,
	 *  RenderbufferStorage, and GetRenderbufferParameteriv, and
	 *  returned by GetFramebufferAttachmentParameteriv:
	 */
	public static final int GL_RENDERBUFFER = 0x8D41;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of
	 *  RenderbufferStorage:
	 */
	public static final int GL_STENCIL_INDEX1 = 0x8D46,
		GL_STENCIL_INDEX4 = 0x8D47,
		GL_STENCIL_INDEX8 = 0x8D48,
		GL_STENCIL_INDEX16 = 0x8D49;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameteriv: 
	 */
	public static final int GL_RENDERBUFFER_WIDTH = 0x8D42,
		GL_RENDERBUFFER_HEIGHT = 0x8D43,
		GL_RENDERBUFFER_INTERNAL_FORMAT = 0x8D44,
		GL_RENDERBUFFER_RED_SIZE = 0x8D50,
		GL_RENDERBUFFER_GREEN_SIZE = 0x8D51,
		GL_RENDERBUFFER_BLUE_SIZE = 0x8D52,
		GL_RENDERBUFFER_ALPHA_SIZE = 0x8D53,
		GL_RENDERBUFFER_DEPTH_SIZE = 0x8D54,
		GL_RENDERBUFFER_STENCIL_SIZE = 0x8D55;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of
	 *  GetFramebufferAttachmentParameteriv:
	 */
	public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0x8CD0,
		GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0x8CD1,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0x8CD2,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0x8CD3,
		GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING = 0x8210,
		GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE = 0x8211,
		GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE = 0x8212,
		GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE = 0x8213,
		GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE = 0x8214,
		GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE = 0x8215,
		GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE = 0x8216,
		GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE = 0x8217;

	/**
	 * Returned in &lt;params&gt; by GetFramebufferAttachmentParameteriv: 
	 */
	public static final int GL_FRAMEBUFFER_DEFAULT = 0x8218,
		GL_INDEX = 0x8222;

	/**
	 *  Accepted by the &lt;attachment&gt; parameter of
	 *  FramebufferTexture{1D|2D|3D}, FramebufferRenderbuffer, and
	 *  GetFramebufferAttachmentParameteriv
	 */
	public static final int GL_COLOR_ATTACHMENT0 = 0x8CE0,
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
		GL_DEPTH_ATTACHMENT = 0x8D00,
		GL_STENCIL_ATTACHMENT = 0x8D20,
		GL_DEPTH_STENCIL_ATTACHMENT = 0x821A;

	/**
	 * Returned by CheckFramebufferStatus(): 
	 */
	public static final int GL_FRAMEBUFFER_COMPLETE = 0x8CD5,
		GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8CD6,
		GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8CD7,
		GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 0x8CDB,
		GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 0x8CDC,
		GL_FRAMEBUFFER_UNSUPPORTED = 0x8CDD,
		GL_FRAMEBUFFER_UNDEFINED = 0x8219;

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_FRAMEBUFFER_BINDING = 0x8CA6,
		GL_RENDERBUFFER_BINDING = 0x8CA7,
		GL_MAX_COLOR_ATTACHMENTS = 0x8CDF,
		GL_MAX_RENDERBUFFER_SIZE = 0x84E8;

	/**
	 * Returned by GetError(): 
	 */
	public static final int GL_INVALID_FRAMEBUFFER_OPERATION = 0x506;

	/**
	 *  Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels,
	 *  TexImage1D, TexImage2D, TexImage3D, GetTexImage, TexSubImage1D,
	 *  TexSubImage2D, TexSubImage3D, GetHistogram, GetMinmax,
	 *  ConvolutionFilter1D, ConvolutionFilter2D, GetConvolutionFilter,
	 *  SeparableFilter2D, GetSeparableFilter, ColorTable, ColorSubTable,
	 *  and GetColorTable:
	 *  <p/>
	 *  Accepted by the &lt;type&gt; argument of VertexPointer, NormalPointer,
	 *  ColorPointer, SecondaryColorPointer, FogCoordPointer, TexCoordPointer,
	 *  and VertexAttribPointer:
	 */
	public static final int GL_HALF_FLOAT = 0x140B;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameteriv. 
	 */
	public static final int GL_RENDERBUFFER_SAMPLES = 0x8CAB;

	/**
	 * Returned by CheckFramebufferStatus. 
	 */
	public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 0x8D56;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev.
	 */
	public static final int GL_MAX_SAMPLES = 0x8D57;

	/**
	 * Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv, and GetDoublev. 
	 */
	public static final int GL_DRAW_FRAMEBUFFER_BINDING = 0x8CA6,
		GL_READ_FRAMEBUFFER_BINDING = 0x8CAA;

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_RGBA_INTEGER_MODE = 0x8D9E;

	/**
	 *  Accepted by the &lt;internalFormat&gt; parameter of TexImage1D,
	 *  TexImage2D, and TexImage3D:
	 */
	public static final int GL_RGBA32UI = 0x8D70,
		GL_RGB32UI = 0x8D71,
		GL_ALPHA32UI = 0x8D72,
		GL_RGBA16UI = 0x8D76,
		GL_RGB16UI = 0x8D77,
		GL_ALPHA16UI = 0x8D78,
		GL_RGBA8UI = 0x8D7C,
		GL_RGB8UI = 0x8D7D,
		GL_ALPHA8UI = 0x8D7E,
		GL_RGBA32I = 0x8D82,
		GL_RGB32I = 0x8D83,
		GL_ALPHA32I = 0x8D84,
		GL_RGBA16I = 0x8D88,
		GL_RGB16I = 0x8D89,
		GL_ALPHA16I = 0x8D8A,
		GL_RGBA8I = 0x8D8E,
		GL_RGB8I = 0x8D8F,
		GL_ALPHA8I = 0x8D90;

	/**
	 *  Accepted by the &lt;format&gt; parameter of TexImage1D, TexImage2D,
	 *  TexImage3D, TexSubImage1D, TexSubImage2D, TexSubImage3D,
	 *  DrawPixels and ReadPixels:
	 */
	public static final int GL_RED_INTEGER = 0x8D94,
		GL_GREEN_INTEGER = 0x8D95,
		GL_BLUE_INTEGER = 0x8D96,
		GL_ALPHA_INTEGER = 0x8D97,
		GL_RGB_INTEGER = 0x8D98,
		GL_RGBA_INTEGER = 0x8D99,
		GL_BGR_INTEGER = 0x8D9A,
		GL_BGRA_INTEGER = 0x8D9B;

	/**
	 *  Accepted by the &lt;target&gt; parameter of TexParameteri, TexParameteriv,
	 *  TexParameterf, TexParameterfv, and BindTexture:
	 */
	public static final int GL_TEXTURE_1D_ARRAY = 0x8C18,
		GL_TEXTURE_2D_ARRAY = 0x8C1A;

	/**
	 *  Accepted by the &lt;target&gt; parameter of TexImage3D, TexSubImage3D,
	 *  CopyTexSubImage3D, CompressedTexImage3D, and CompressedTexSubImage3D:
	 */
	public static final int GL_PROXY_TEXTURE_2D_ARRAY = 0x8C1B;

	/**
	 *  Accepted by the &lt;target&gt; parameter of TexImage2D, TexSubImage2D,
	 *  CopyTexImage2D, CopyTexSubImage2D, CompressedTexImage2D, and
	 *  CompressedTexSubImage2D:
	 */
	public static final int GL_PROXY_TEXTURE_1D_ARRAY = 0x8C19;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv
	 *  and GetFloatv:
	 */
	public static final int GL_TEXTURE_BINDING_1D_ARRAY = 0x8C1C,
		GL_TEXTURE_BINDING_2D_ARRAY = 0x8C1D,
		GL_MAX_ARRAY_TEXTURE_LAYERS = 0x88FF;

	/**
	 *  Accepted by the &lt;param&gt; parameter of TexParameterf, TexParameteri,
	 *  TexParameterfv, and TexParameteriv when the &lt;pname&gt; parameter is
	 *  TEXTURE_COMPARE_MODE_ARB:
	 */
	public static final int GL_COMPARE_REF_DEPTH_TO_TEXTURE = 0x884E;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of
	 *  GetFramebufferAttachmentParameteriv:
	 */
	public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 0x8CD4;

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveUniform: 
	 */
	public static final int GL_SAMPLER_1D_ARRAY = 0x8DC0,
		GL_SAMPLER_2D_ARRAY = 0x8DC1,
		GL_SAMPLER_1D_ARRAY_SHADOW = 0x8DC3,
		GL_SAMPLER_2D_ARRAY_SHADOW = 0x8DC4;

	/**
	 *  Accepted by the &lt;format&gt; parameter of DrawPixels, ReadPixels,
	 *  TexImage1D, TexImage2D, TexImage3D, TexSubImage1D, TexSubImage2D,
	 *  TexSubImage3D, and GetTexImage, by the &lt;type&gt; parameter of
	 *  CopyPixels, by the &lt;internalformat&gt; parameter of TexImage1D,
	 *  TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 *  RenderbufferStorage, and returned in the &lt;data&gt; parameter of
	 *  GetTexLevelParameter and GetRenderbufferParameteriv.
	 */
	public static final int GL_DEPTH_STENCIL = 0x84F9;

	/**
	 *  Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels,
	 *  TexImage1D, TexImage2D, TexImage3D, TexSubImage1D, TexSubImage2D,
	 *  TexSubImage3D, and GetTexImage.
	 */
	public static final int GL_UNSIGNED_INT_24_8 = 0x84FA;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of TexImage1D,
	 *  TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 *  RenderbufferStorage, and returned in the &lt;data&gt; parameter of
	 *  GetTexLevelParameter and GetRenderbufferParameteriv.
	 */
	public static final int GL_DEPTH24_STENCIL8 = 0x88F0;

	/**
	 * Accepted by the &lt;value&gt; parameter of GetTexLevelParameter. 
	 */
	public static final int GL_TEXTURE_STENCIL_SIZE = 0x88F1;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of TexImage2D,
	 *  CopyTexImage2D, and CompressedTexImage2D and the &lt;format&gt; parameter
	 *  of CompressedTexSubImage2D:
	 */
	public static final int GL_COMPRESSED_RED_RGTC1 = 0x8DBB,
		GL_COMPRESSED_SIGNED_RED_RGTC1 = 0x8DBC,
		GL_COMPRESSED_RG_RGTC2 = 0x8DBD,
		GL_COMPRESSED_SIGNED_RG_RGTC2 = 0x8DBE;

	/**
	 *  Accepted by the &lt;internalFormat&gt; parameter of TexImage1D, TexImage2D,
	 *  TexImage3D, CopyTexImage1D, and CopyTexImage2D:
	 */
	public static final int GL_R8 = 0x8229,
		GL_R16 = 0x822A,
		GL_RG8 = 0x822B,
		GL_RG16 = 0x822C,
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
		GL_RG32UI = 0x823C;

	/**
	 *  Accepted by the &lt;format&gt; parameter of TexImage3D, TexImage2D,
	 *  TexImage3D, TexSubImage1D, TexSubImage2D, TexSubImage3D,
	 *  DrawPixels and ReadPixels:
	 */
	public static final int GL_RG = 0x8227,
		GL_RG_INTEGER = 0x8228;

	/**
	 *  Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData,
	 *  GetBufferPointerv, BindBufferRange, BindBufferOffset and
	 *  BindBufferBase:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER = 0x8C8E;

	/**
	 *  Accepted by the &lt;param&gt; parameter of GetIntegerIndexedv and
	 *  GetBooleanIndexedv:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_START = 0x8C84,
		GL_TRANSFORM_FEEDBACK_BUFFER_SIZE = 0x8C85;

	/**
	 *  Accepted by the &lt;param&gt; parameter of GetIntegerIndexedv and
	 *  GetBooleanIndexedv, and by the &lt;pname&gt; parameter of GetBooleanv,
	 *  GetDoublev, GetIntegerv, and GetFloatv:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING = 0x8C8F;

	/**
	 * Accepted by the &lt;bufferMode&gt; parameter of TransformFeedbackVaryings: 
	 */
	public static final int GL_INTERLEAVED_ATTRIBS = 0x8C8C,
		GL_SEPARATE_ATTRIBS = 0x8C8D;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery, and
	 *  GetQueryiv:
	 */
	public static final int GL_PRIMITIVES_GENERATED = 0x8C87,
		GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN = 0x8C88;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled, and by
	 *  the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 *  GetDoublev:
	 */
	public static final int GL_RASTERIZER_DISCARD = 0x8C89;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS = 0x8C8A,
		GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS = 0x8C8B,
		GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS = 0x8C80;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramiv: 
	 */
	public static final int GL_TRANSFORM_FEEDBACK_VARYINGS = 0x8C83,
		GL_TRANSFORM_FEEDBACK_BUFFER_MODE = 0x8C7F,
		GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH = 0x8C76;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_VERTEX_ARRAY_BINDING = 0x85B5;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled,
	 *  and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_FRAMEBUFFER_SRGB = 0x8DB9;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_FRAMEBUFFER_SRGB_CAPABLE = 0x8DBA;

	private GL30() {}

	public static String glGetStringi(int name, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetStringi;
		BufferChecks.checkFunctionAddress(function_pointer);
		String __result = nglGetStringi(name, index, function_pointer);
		return __result;
	}
	static native String nglGetStringi(int name, int index, long function_pointer);

	public static void glClearBuffer(int buffer, int drawbuffer, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearBufferfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 4);
		nglClearBufferfv(buffer, drawbuffer, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglClearBufferfv(int buffer, int drawbuffer, long value, long function_pointer);

	public static void glClearBuffer(int buffer, int drawbuffer, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearBufferiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 4);
		nglClearBufferiv(buffer, drawbuffer, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglClearBufferiv(int buffer, int drawbuffer, long value, long function_pointer);

	public static void glClearBufferu(int buffer, int drawbuffer, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearBufferuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 4);
		nglClearBufferuiv(buffer, drawbuffer, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglClearBufferuiv(int buffer, int drawbuffer, long value, long function_pointer);

	public static void glClearBufferfi(int buffer, int drawbuffer, float depth, int stencil) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearBufferfi;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClearBufferfi(buffer, drawbuffer, depth, stencil, function_pointer);
	}
	static native void nglClearBufferfi(int buffer, int drawbuffer, float depth, int stencil, long function_pointer);

	public static void glVertexAttribI1i(int index, int x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI1i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI1i(index, x, function_pointer);
	}
	static native void nglVertexAttribI1i(int index, int x, long function_pointer);

	public static void glVertexAttribI2i(int index, int x, int y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI2i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI2i(index, x, y, function_pointer);
	}
	static native void nglVertexAttribI2i(int index, int x, int y, long function_pointer);

	public static void glVertexAttribI3i(int index, int x, int y, int z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI3i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI3i(index, x, y, z, function_pointer);
	}
	static native void nglVertexAttribI3i(int index, int x, int y, int z, long function_pointer);

	public static void glVertexAttribI4i(int index, int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI4i(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttribI4i(int index, int x, int y, int z, int w, long function_pointer);

	public static void glVertexAttribI1ui(int index, int x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI1ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI1ui(index, x, function_pointer);
	}
	static native void nglVertexAttribI1ui(int index, int x, long function_pointer);

	public static void glVertexAttribI2ui(int index, int x, int y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI2ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI2ui(index, x, y, function_pointer);
	}
	static native void nglVertexAttribI2ui(int index, int x, int y, long function_pointer);

	public static void glVertexAttribI3ui(int index, int x, int y, int z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI3ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI3ui(index, x, y, z, function_pointer);
	}
	static native void nglVertexAttribI3ui(int index, int x, int y, int z, long function_pointer);

	public static void glVertexAttribI4ui(int index, int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI4ui(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttribI4ui(int index, int x, int y, int z, int w, long function_pointer);

	public static void glVertexAttribI1(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI1iv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 1);
		nglVertexAttribI1iv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI1iv(int index, long v, long function_pointer);

	public static void glVertexAttribI2(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI2iv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 2);
		nglVertexAttribI2iv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI2iv(int index, long v, long function_pointer);

	public static void glVertexAttribI3(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI3iv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 3);
		nglVertexAttribI3iv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI3iv(int index, long v, long function_pointer);

	public static void glVertexAttribI4(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4iv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribI4iv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI4iv(int index, long v, long function_pointer);

	public static void glVertexAttribI1u(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI1uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 1);
		nglVertexAttribI1uiv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI1uiv(int index, long v, long function_pointer);

	public static void glVertexAttribI2u(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI2uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 2);
		nglVertexAttribI2uiv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI2uiv(int index, long v, long function_pointer);

	public static void glVertexAttribI3u(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI3uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 3);
		nglVertexAttribI3uiv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI3uiv(int index, long v, long function_pointer);

	public static void glVertexAttribI4u(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribI4uiv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI4uiv(int index, long v, long function_pointer);

	public static void glVertexAttribI4(int index, ByteBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4bv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribI4bv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI4bv(int index, long v, long function_pointer);

	public static void glVertexAttribI4(int index, ShortBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4sv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribI4sv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI4sv(int index, long v, long function_pointer);

	public static void glVertexAttribI4u(int index, ByteBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4ubv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribI4ubv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI4ubv(int index, long v, long function_pointer);

	public static void glVertexAttribI4u(int index, ShortBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribI4usv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribI4usv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribI4usv(int index, long v, long function_pointer);

	public static void glVertexAttribIPointer(int index, int size, int type, int stride, ByteBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribIPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribIPointer(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribIPointer(int index, int size, int type, int stride, IntBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribIPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribIPointer(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribIPointer(int index, int size, int type, int stride, ShortBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribIPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribIPointer(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	static native void nglVertexAttribIPointer(int index, int size, int type, int stride, long buffer, long function_pointer);
	public static void glVertexAttribIPointer(int index, int size, int type, int stride, long buffer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribIPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglVertexAttribIPointerBO(index, size, type, stride, buffer_buffer_offset, function_pointer);
	}
	static native void nglVertexAttribIPointerBO(int index, int size, int type, int stride, long buffer_buffer_offset, long function_pointer);

	public static void glGetVertexAttribI(int index, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribIiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribIiv(index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribIiv(int index, int pname, long params, long function_pointer);

	public static void glGetVertexAttribIu(int index, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribIuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribIuiv(index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribIuiv(int index, int pname, long params, long function_pointer);

	public static void glUniform1ui(int location, int v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform1ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform1ui(location, v0, function_pointer);
	}
	static native void nglUniform1ui(int location, int v0, long function_pointer);

	public static void glUniform2ui(int location, int v0, int v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform2ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform2ui(location, v0, v1, function_pointer);
	}
	static native void nglUniform2ui(int location, int v0, int v1, long function_pointer);

	public static void glUniform3ui(int location, int v0, int v1, int v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform3ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform3ui(location, v0, v1, v2, function_pointer);
	}
	static native void nglUniform3ui(int location, int v0, int v1, int v2, long function_pointer);

	public static void glUniform4ui(int location, int v0, int v1, int v2, int v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform4ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform4ui(location, v0, v1, v2, v3, function_pointer);
	}
	static native void nglUniform4ui(int location, int v0, int v1, int v2, int v3, long function_pointer);

	public static void glUniform1u(int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform1uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform1uiv(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform1uiv(int location, int value_count, long value, long function_pointer);

	public static void glUniform2u(int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform2uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform2uiv(location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform2uiv(int location, int value_count, long value, long function_pointer);

	public static void glUniform3u(int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform3uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform3uiv(location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform3uiv(int location, int value_count, long value, long function_pointer);

	public static void glUniform4u(int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform4uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform4uiv(location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform4uiv(int location, int value_count, long value, long function_pointer);

	public static void glGetUniformu(int program, int location, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetUniformuiv(program, location, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetUniformuiv(int program, int location, long params, long function_pointer);

	public static void glBindFragDataLocation(int program, int colorNumber, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindFragDataLocation;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		nglBindFragDataLocation(program, colorNumber, MemoryUtil.getAddress(name), function_pointer);
	}
	static native void nglBindFragDataLocation(int program, int colorNumber, long name, long function_pointer);

	/** Overloads glBindFragDataLocation. */
	public static void glBindFragDataLocation(int program, int colorNumber, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindFragDataLocation;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindFragDataLocation(program, colorNumber, APIUtil.getBufferNT(caps, name), function_pointer);
	}

	public static int glGetFragDataLocation(int program, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFragDataLocation;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetFragDataLocation(program, MemoryUtil.getAddress(name), function_pointer);
		return __result;
	}
	static native int nglGetFragDataLocation(int program, long name, long function_pointer);

	/** Overloads glGetFragDataLocation. */
	public static int glGetFragDataLocation(int program, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFragDataLocation;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetFragDataLocation(program, APIUtil.getBufferNT(caps, name), function_pointer);
		return __result;
	}

	public static void glBeginConditionalRender(int id, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBeginConditionalRender;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginConditionalRender(id, mode, function_pointer);
	}
	static native void nglBeginConditionalRender(int id, int mode, long function_pointer);

	public static void glEndConditionalRender() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEndConditionalRender;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndConditionalRender(function_pointer);
	}
	static native void nglEndConditionalRender(long function_pointer);

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
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapBufferRange;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapBufferRange(target, offset, length, access, old_buffer, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglMapBufferRange(int target, long offset, long length, int access, ByteBuffer old_buffer, long function_pointer);

	public static void glFlushMappedBufferRange(int target, long offset, long length) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFlushMappedBufferRange;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFlushMappedBufferRange(target, offset, length, function_pointer);
	}
	static native void nglFlushMappedBufferRange(int target, long offset, long length, long function_pointer);

	public static void glClampColor(int target, int clamp) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClampColor;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClampColor(target, clamp, function_pointer);
	}
	static native void nglClampColor(int target, int clamp, long function_pointer);

	public static boolean glIsRenderbuffer(int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsRenderbuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsRenderbuffer(renderbuffer, function_pointer);
		return __result;
	}
	static native boolean nglIsRenderbuffer(int renderbuffer, long function_pointer);

	public static void glBindRenderbuffer(int target, int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindRenderbuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindRenderbuffer(target, renderbuffer, function_pointer);
	}
	static native void nglBindRenderbuffer(int target, int renderbuffer, long function_pointer);

	public static void glDeleteRenderbuffers(IntBuffer renderbuffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteRenderbuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(renderbuffers);
		nglDeleteRenderbuffers(renderbuffers.remaining(), MemoryUtil.getAddress(renderbuffers), function_pointer);
	}
	static native void nglDeleteRenderbuffers(int renderbuffers_n, long renderbuffers, long function_pointer);

	/** Overloads glDeleteRenderbuffers. */
	public static void glDeleteRenderbuffers(int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteRenderbuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteRenderbuffers(1, APIUtil.getInt(caps, renderbuffer), function_pointer);
	}

	public static void glGenRenderbuffers(IntBuffer renderbuffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenRenderbuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(renderbuffers);
		nglGenRenderbuffers(renderbuffers.remaining(), MemoryUtil.getAddress(renderbuffers), function_pointer);
	}
	static native void nglGenRenderbuffers(int renderbuffers_n, long renderbuffers, long function_pointer);

	/** Overloads glGenRenderbuffers. */
	public static int glGenRenderbuffers() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenRenderbuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer renderbuffers = APIUtil.getBufferInt(caps);
		nglGenRenderbuffers(1, MemoryUtil.getAddress(renderbuffers), function_pointer);
		return renderbuffers.get(0);
	}

	public static void glRenderbufferStorage(int target, int internalformat, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRenderbufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRenderbufferStorage(target, internalformat, width, height, function_pointer);
	}
	static native void nglRenderbufferStorage(int target, int internalformat, int width, int height, long function_pointer);

	public static void glGetRenderbufferParameter(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetRenderbufferParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetRenderbufferParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetRenderbufferParameteriv(int target, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetRenderbufferParameteriv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetRenderbufferParameteri} instead. 
	 */
	@Deprecated
	public static int glGetRenderbufferParameter(int target, int pname) {
		return GL30.glGetRenderbufferParameteri(target, pname);
	}

	/** Overloads glGetRenderbufferParameteriv. */
	public static int glGetRenderbufferParameteri(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetRenderbufferParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetRenderbufferParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static boolean glIsFramebuffer(int framebuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsFramebuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsFramebuffer(framebuffer, function_pointer);
		return __result;
	}
	static native boolean nglIsFramebuffer(int framebuffer, long function_pointer);

	public static void glBindFramebuffer(int target, int framebuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindFramebuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindFramebuffer(target, framebuffer, function_pointer);
	}
	static native void nglBindFramebuffer(int target, int framebuffer, long function_pointer);

	public static void glDeleteFramebuffers(IntBuffer framebuffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteFramebuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(framebuffers);
		nglDeleteFramebuffers(framebuffers.remaining(), MemoryUtil.getAddress(framebuffers), function_pointer);
	}
	static native void nglDeleteFramebuffers(int framebuffers_n, long framebuffers, long function_pointer);

	/** Overloads glDeleteFramebuffers. */
	public static void glDeleteFramebuffers(int framebuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteFramebuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteFramebuffers(1, APIUtil.getInt(caps, framebuffer), function_pointer);
	}

	public static void glGenFramebuffers(IntBuffer framebuffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenFramebuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(framebuffers);
		nglGenFramebuffers(framebuffers.remaining(), MemoryUtil.getAddress(framebuffers), function_pointer);
	}
	static native void nglGenFramebuffers(int framebuffers_n, long framebuffers, long function_pointer);

	/** Overloads glGenFramebuffers. */
	public static int glGenFramebuffers() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenFramebuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer framebuffers = APIUtil.getBufferInt(caps);
		nglGenFramebuffers(1, MemoryUtil.getAddress(framebuffers), function_pointer);
		return framebuffers.get(0);
	}

	public static int glCheckFramebufferStatus(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCheckFramebufferStatus;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglCheckFramebufferStatus(target, function_pointer);
		return __result;
	}
	static native int nglCheckFramebufferStatus(int target, long function_pointer);

	public static void glFramebufferTexture1D(int target, int attachment, int textarget, int texture, int level) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFramebufferTexture1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTexture1D(target, attachment, textarget, texture, level, function_pointer);
	}
	static native void nglFramebufferTexture1D(int target, int attachment, int textarget, int texture, int level, long function_pointer);

	public static void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFramebufferTexture2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTexture2D(target, attachment, textarget, texture, level, function_pointer);
	}
	static native void nglFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level, long function_pointer);

	public static void glFramebufferTexture3D(int target, int attachment, int textarget, int texture, int level, int zoffset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFramebufferTexture3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTexture3D(target, attachment, textarget, texture, level, zoffset, function_pointer);
	}
	static native void nglFramebufferTexture3D(int target, int attachment, int textarget, int texture, int level, int zoffset, long function_pointer);

	public static void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFramebufferRenderbuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer, function_pointer);
	}
	static native void nglFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer, long function_pointer);

	public static void glGetFramebufferAttachmentParameter(int target, int attachment, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFramebufferAttachmentParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetFramebufferAttachmentParameteriv(target, attachment, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetFramebufferAttachmentParameteriv(int target, int attachment, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetFramebufferAttachmentParameteriv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetFramebufferAttachmentParameteri} instead. 
	 */
	@Deprecated
	public static int glGetFramebufferAttachmentParameter(int target, int attachment, int pname) {
		return GL30.glGetFramebufferAttachmentParameteri(target, attachment, pname);
	}

	/** Overloads glGetFramebufferAttachmentParameteriv. */
	public static int glGetFramebufferAttachmentParameteri(int target, int attachment, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFramebufferAttachmentParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetFramebufferAttachmentParameteriv(target, attachment, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGenerateMipmap(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenerateMipmap;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglGenerateMipmap(target, function_pointer);
	}
	static native void nglGenerateMipmap(int target, long function_pointer);

	/**
	 *  Establishes the data storage, format, dimensions, and number of
	 *  samples of a renderbuffer object's image.
	 */
	public static void glRenderbufferStorageMultisample(int target, int samples, int internalformat, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRenderbufferStorageMultisample;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRenderbufferStorageMultisample(target, samples, internalformat, width, height, function_pointer);
	}
	static native void nglRenderbufferStorageMultisample(int target, int samples, int internalformat, int width, int height, long function_pointer);

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
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlitFramebuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter, function_pointer);
	}
	static native void nglBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter, long function_pointer);

	public static void glTexParameterI(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexParameterIiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglTexParameterIiv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglTexParameterIiv(int target, int pname, long params, long function_pointer);

	/** Overloads glTexParameterIiv. */
	public static void glTexParameterIi(int target, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexParameterIiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexParameterIiv(target, pname, APIUtil.getInt(caps, param), function_pointer);
	}

	public static void glTexParameterIu(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexParameterIuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglTexParameterIuiv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglTexParameterIuiv(int target, int pname, long params, long function_pointer);

	/** Overloads glTexParameterIuiv. */
	public static void glTexParameterIui(int target, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexParameterIuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexParameterIuiv(target, pname, APIUtil.getInt(caps, param), function_pointer);
	}

	public static void glGetTexParameterI(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexParameterIiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetTexParameterIiv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTexParameterIiv(int target, int pname, long params, long function_pointer);

	/** Overloads glGetTexParameterIiv. */
	public static int glGetTexParameterIi(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexParameterIiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetTexParameterIiv(target, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTexParameterIu(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexParameterIuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetTexParameterIuiv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTexParameterIuiv(int target, int pname, long params, long function_pointer);

	/** Overloads glGetTexParameterIuiv. */
	public static int glGetTexParameterIui(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexParameterIuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetTexParameterIuiv(target, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glFramebufferTextureLayer(int target, int attachment, int texture, int level, int layer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFramebufferTextureLayer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTextureLayer(target, attachment, texture, level, layer, function_pointer);
	}
	static native void nglFramebufferTextureLayer(int target, int attachment, int texture, int level, int layer, long function_pointer);

	public static void glColorMaski(int buf, boolean r, boolean g, boolean b, boolean a) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorMaski;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColorMaski(buf, r, g, b, a, function_pointer);
	}
	static native void nglColorMaski(int buf, boolean r, boolean g, boolean b, boolean a, long function_pointer);

	public static void glGetBoolean(int value, int index, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBooleani_v;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, 4);
		nglGetBooleani_v(value, index, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglGetBooleani_v(int value, int index, long data, long function_pointer);

	/** Overloads glGetBooleani_v. */
	public static boolean glGetBoolean(int value, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBooleani_v;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer data = APIUtil.getBufferByte(caps, 1);
		nglGetBooleani_v(value, index, MemoryUtil.getAddress(data), function_pointer);
		return data.get(0) == 1;
	}

	public static void glGetInteger(int value, int index, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetIntegeri_v;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, 4);
		nglGetIntegeri_v(value, index, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglGetIntegeri_v(int value, int index, long data, long function_pointer);

	/** Overloads glGetIntegeri_v. */
	public static int glGetInteger(int value, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetIntegeri_v;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer data = APIUtil.getBufferInt(caps);
		nglGetIntegeri_v(value, index, MemoryUtil.getAddress(data), function_pointer);
		return data.get(0);
	}

	public static void glEnablei(int target, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEnablei;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEnablei(target, index, function_pointer);
	}
	static native void nglEnablei(int target, int index, long function_pointer);

	public static void glDisablei(int target, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDisablei;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDisablei(target, index, function_pointer);
	}
	static native void nglDisablei(int target, int index, long function_pointer);

	public static boolean glIsEnabledi(int target, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsEnabledi;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsEnabledi(target, index, function_pointer);
		return __result;
	}
	static native boolean nglIsEnabledi(int target, int index, long function_pointer);

	public static void glBindBufferRange(int target, int index, int buffer, long offset, long size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindBufferRange;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindBufferRange(target, index, buffer, offset, size, function_pointer);
	}
	static native void nglBindBufferRange(int target, int index, int buffer, long offset, long size, long function_pointer);

	public static void glBindBufferBase(int target, int index, int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindBufferBase;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindBufferBase(target, index, buffer, function_pointer);
	}
	static native void nglBindBufferBase(int target, int index, int buffer, long function_pointer);

	public static void glBeginTransformFeedback(int primitiveMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBeginTransformFeedback;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginTransformFeedback(primitiveMode, function_pointer);
	}
	static native void nglBeginTransformFeedback(int primitiveMode, long function_pointer);

	public static void glEndTransformFeedback() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEndTransformFeedback;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndTransformFeedback(function_pointer);
	}
	static native void nglEndTransformFeedback(long function_pointer);

	public static void glTransformFeedbackVaryings(int program, int count, ByteBuffer varyings, int bufferMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTransformFeedbackVaryings;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(varyings);
		BufferChecks.checkNullTerminated(varyings, count);
		nglTransformFeedbackVaryings(program, count, MemoryUtil.getAddress(varyings), bufferMode, function_pointer);
	}
	static native void nglTransformFeedbackVaryings(int program, int count, long varyings, int bufferMode, long function_pointer);

	/** Overloads glTransformFeedbackVaryings. */
	public static void glTransformFeedbackVaryings(int program, CharSequence[] varyings, int bufferMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTransformFeedbackVaryings;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkArray(varyings);
		nglTransformFeedbackVaryings(program, varyings.length, APIUtil.getBufferNT(caps, varyings), bufferMode, function_pointer);
	}

	public static void glGetTransformFeedbackVarying(int program, int index, IntBuffer length, IntBuffer size, IntBuffer type, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTransformFeedbackVarying;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkBuffer(size, 1);
		BufferChecks.checkBuffer(type, 1);
		BufferChecks.checkDirect(name);
		nglGetTransformFeedbackVarying(program, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name), function_pointer);
	}
	static native void nglGetTransformFeedbackVarying(int program, int index, int name_bufSize, long length, long size, long type, long name, long function_pointer);

	/** Overloads glGetTransformFeedbackVarying. */
	public static String glGetTransformFeedbackVarying(int program, int index, int bufSize, IntBuffer size, IntBuffer type) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTransformFeedbackVarying;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(size, 1);
		BufferChecks.checkBuffer(type, 1);
		IntBuffer name_length = APIUtil.getLengths(caps);
		ByteBuffer name = APIUtil.getBufferByte(caps, bufSize);
		nglGetTransformFeedbackVarying(program, index, bufSize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name), function_pointer);
		name.limit(name_length.get(0));
		return APIUtil.getString(caps, name);
	}

	public static void glBindVertexArray(int array) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindVertexArray;
		BufferChecks.checkFunctionAddress(function_pointer);
		StateTracker.bindVAO(caps, array);
		nglBindVertexArray(array, function_pointer);
	}
	static native void nglBindVertexArray(int array, long function_pointer);

	public static void glDeleteVertexArrays(IntBuffer arrays) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteVertexArrays;
		BufferChecks.checkFunctionAddress(function_pointer);
		StateTracker.deleteVAO(caps, arrays);
		BufferChecks.checkDirect(arrays);
		nglDeleteVertexArrays(arrays.remaining(), MemoryUtil.getAddress(arrays), function_pointer);
	}
	static native void nglDeleteVertexArrays(int arrays_n, long arrays, long function_pointer);

	/** Overloads glDeleteVertexArrays. */
	public static void glDeleteVertexArrays(int array) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteVertexArrays;
		BufferChecks.checkFunctionAddress(function_pointer);
		StateTracker.deleteVAO(caps, array);
		nglDeleteVertexArrays(1, APIUtil.getInt(caps, array), function_pointer);
	}

	public static void glGenVertexArrays(IntBuffer arrays) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenVertexArrays;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(arrays);
		nglGenVertexArrays(arrays.remaining(), MemoryUtil.getAddress(arrays), function_pointer);
	}
	static native void nglGenVertexArrays(int arrays_n, long arrays, long function_pointer);

	/** Overloads glGenVertexArrays. */
	public static int glGenVertexArrays() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenVertexArrays;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer arrays = APIUtil.getBufferInt(caps);
		nglGenVertexArrays(1, MemoryUtil.getAddress(arrays), function_pointer);
		return arrays.get(0);
	}

	public static boolean glIsVertexArray(int array) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsVertexArray;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsVertexArray(array, function_pointer);
		return __result;
	}
	static native boolean nglIsVertexArray(int array, long function_pointer);
}
