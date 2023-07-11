/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

/**
 *  The core OpenGL1.1 API.
 * <p>
 *  @author cix_foo <cix_foo@users.sourceforge.net>
 *  @version $Revision$
 *           $Id$
 */
public final class GL11 {

	public static final int GL_ACCUM = 0x100,
		GL_LOAD = 0x101,
		GL_RETURN = 0x102,
		GL_MULT = 0x103,
		GL_ADD = 0x104,
		GL_NEVER = 0x200,
		GL_LESS = 0x201,
		GL_EQUAL = 0x202,
		GL_LEQUAL = 0x203,
		GL_GREATER = 0x204,
		GL_NOTEQUAL = 0x205,
		GL_GEQUAL = 0x206,
		GL_ALWAYS = 0x207,
		GL_CURRENT_BIT = 0x1,
		GL_POINT_BIT = 0x2,
		GL_LINE_BIT = 0x4,
		GL_POLYGON_BIT = 0x8,
		GL_POLYGON_STIPPLE_BIT = 0x10,
		GL_PIXEL_MODE_BIT = 0x20,
		GL_LIGHTING_BIT = 0x40,
		GL_FOG_BIT = 0x80,
		GL_DEPTH_BUFFER_BIT = 0x100,
		GL_ACCUM_BUFFER_BIT = 0x200,
		GL_STENCIL_BUFFER_BIT = 0x400,
		GL_VIEWPORT_BIT = 0x800,
		GL_TRANSFORM_BIT = 0x1000,
		GL_ENABLE_BIT = 0x2000,
		GL_COLOR_BUFFER_BIT = 0x4000,
		GL_HINT_BIT = 0x8000,
		GL_EVAL_BIT = 0x10000,
		GL_LIST_BIT = 0x20000,
		GL_TEXTURE_BIT = 0x40000,
		GL_SCISSOR_BIT = 0x80000,
		GL_ALL_ATTRIB_BITS = 0xFFFFF,
		GL_POINTS = 0x0,
		GL_LINES = 0x1,
		GL_LINE_LOOP = 0x2,
		GL_LINE_STRIP = 0x3,
		GL_TRIANGLES = 0x4,
		GL_TRIANGLE_STRIP = 0x5,
		GL_TRIANGLE_FAN = 0x6,
		GL_QUADS = 0x7,
		GL_QUAD_STRIP = 0x8,
		GL_POLYGON = 0x9,
		GL_ZERO = 0x0,
		GL_ONE = 0x1,
		GL_SRC_COLOR = 0x300,
		GL_ONE_MINUS_SRC_COLOR = 0x301,
		GL_SRC_ALPHA = 0x302,
		GL_ONE_MINUS_SRC_ALPHA = 0x303,
		GL_DST_ALPHA = 0x304,
		GL_ONE_MINUS_DST_ALPHA = 0x305,
		GL_DST_COLOR = 0x306,
		GL_ONE_MINUS_DST_COLOR = 0x307,
		GL_SRC_ALPHA_SATURATE = 0x308,
		GL_CONSTANT_COLOR = 0x8001,
		GL_ONE_MINUS_CONSTANT_COLOR = 0x8002,
		GL_CONSTANT_ALPHA = 0x8003,
		GL_ONE_MINUS_CONSTANT_ALPHA = 0x8004,
		GL_TRUE = 0x1,
		GL_FALSE = 0x0,
		GL_CLIP_PLANE0 = 0x3000,
		GL_CLIP_PLANE1 = 0x3001,
		GL_CLIP_PLANE2 = 0x3002,
		GL_CLIP_PLANE3 = 0x3003,
		GL_CLIP_PLANE4 = 0x3004,
		GL_CLIP_PLANE5 = 0x3005,
		GL_BYTE = 0x1400,
		GL_UNSIGNED_BYTE = 0x1401,
		GL_SHORT = 0x1402,
		GL_UNSIGNED_SHORT = 0x1403,
		GL_INT = 0x1404,
		GL_UNSIGNED_INT = 0x1405,
		GL_FLOAT = 0x1406,
		GL_2_BYTES = 0x1407,
		GL_3_BYTES = 0x1408,
		GL_4_BYTES = 0x1409,
		GL_DOUBLE = 0x140A,
		GL_NONE = 0x0,
		GL_FRONT_LEFT = 0x400,
		GL_FRONT_RIGHT = 0x401,
		GL_BACK_LEFT = 0x402,
		GL_BACK_RIGHT = 0x403,
		GL_FRONT = 0x404,
		GL_BACK = 0x405,
		GL_LEFT = 0x406,
		GL_RIGHT = 0x407,
		GL_FRONT_AND_BACK = 0x408,
		GL_AUX0 = 0x409,
		GL_AUX1 = 0x40A,
		GL_AUX2 = 0x40B,
		GL_AUX3 = 0x40C,
		GL_NO_ERROR = 0x0,
		GL_INVALID_ENUM = 0x500,
		GL_INVALID_VALUE = 0x501,
		GL_INVALID_OPERATION = 0x502,
		GL_STACK_OVERFLOW = 0x503,
		GL_STACK_UNDERFLOW = 0x504,
		GL_OUT_OF_MEMORY = 0x505,
		GL_2D = 0x600,
		GL_3D = 0x601,
		GL_3D_COLOR = 0x602,
		GL_3D_COLOR_TEXTURE = 0x603,
		GL_4D_COLOR_TEXTURE = 0x604,
		GL_PASS_THROUGH_TOKEN = 0x700,
		GL_POINT_TOKEN = 0x701,
		GL_LINE_TOKEN = 0x702,
		GL_POLYGON_TOKEN = 0x703,
		GL_BITMAP_TOKEN = 0x704,
		GL_DRAW_PIXEL_TOKEN = 0x705,
		GL_COPY_PIXEL_TOKEN = 0x706,
		GL_LINE_RESET_TOKEN = 0x707,
		GL_EXP = 0x800,
		GL_EXP2 = 0x801,
		GL_CW = 0x900,
		GL_CCW = 0x901,
		GL_COEFF = 0xA00,
		GL_ORDER = 0xA01,
		GL_DOMAIN = 0xA02,
		GL_CURRENT_COLOR = 0xB00,
		GL_CURRENT_INDEX = 0xB01,
		GL_CURRENT_NORMAL = 0xB02,
		GL_CURRENT_TEXTURE_COORDS = 0xB03,
		GL_CURRENT_RASTER_COLOR = 0xB04,
		GL_CURRENT_RASTER_INDEX = 0xB05,
		GL_CURRENT_RASTER_TEXTURE_COORDS = 0xB06,
		GL_CURRENT_RASTER_POSITION = 0xB07,
		GL_CURRENT_RASTER_POSITION_VALID = 0xB08,
		GL_CURRENT_RASTER_DISTANCE = 0xB09,
		GL_POINT_SMOOTH = 0xB10,
		GL_POINT_SIZE = 0xB11,
		GL_POINT_SIZE_RANGE = 0xB12,
		GL_POINT_SIZE_GRANULARITY = 0xB13,
		GL_LINE_SMOOTH = 0xB20,
		GL_LINE_WIDTH = 0xB21,
		GL_LINE_WIDTH_RANGE = 0xB22,
		GL_LINE_WIDTH_GRANULARITY = 0xB23,
		GL_LINE_STIPPLE = 0xB24,
		GL_LINE_STIPPLE_PATTERN = 0xB25,
		GL_LINE_STIPPLE_REPEAT = 0xB26,
		GL_LIST_MODE = 0xB30,
		GL_MAX_LIST_NESTING = 0xB31,
		GL_LIST_BASE = 0xB32,
		GL_LIST_INDEX = 0xB33,
		GL_POLYGON_MODE = 0xB40,
		GL_POLYGON_SMOOTH = 0xB41,
		GL_POLYGON_STIPPLE = 0xB42,
		GL_EDGE_FLAG = 0xB43,
		GL_CULL_FACE = 0xB44,
		GL_CULL_FACE_MODE = 0xB45,
		GL_FRONT_FACE = 0xB46,
		GL_LIGHTING = 0xB50,
		GL_LIGHT_MODEL_LOCAL_VIEWER = 0xB51,
		GL_LIGHT_MODEL_TWO_SIDE = 0xB52,
		GL_LIGHT_MODEL_AMBIENT = 0xB53,
		GL_SHADE_MODEL = 0xB54,
		GL_COLOR_MATERIAL_FACE = 0xB55,
		GL_COLOR_MATERIAL_PARAMETER = 0xB56,
		GL_COLOR_MATERIAL = 0xB57,
		GL_FOG = 0xB60,
		GL_FOG_INDEX = 0xB61,
		GL_FOG_DENSITY = 0xB62,
		GL_FOG_START = 0xB63,
		GL_FOG_END = 0xB64,
		GL_FOG_MODE = 0xB65,
		GL_FOG_COLOR = 0xB66,
		GL_DEPTH_RANGE = 0xB70,
		GL_DEPTH_TEST = 0xB71,
		GL_DEPTH_WRITEMASK = 0xB72,
		GL_DEPTH_CLEAR_VALUE = 0xB73,
		GL_DEPTH_FUNC = 0xB74,
		GL_ACCUM_CLEAR_VALUE = 0xB80,
		GL_STENCIL_TEST = 0xB90,
		GL_STENCIL_CLEAR_VALUE = 0xB91,
		GL_STENCIL_FUNC = 0xB92,
		GL_STENCIL_VALUE_MASK = 0xB93,
		GL_STENCIL_FAIL = 0xB94,
		GL_STENCIL_PASS_DEPTH_FAIL = 0xB95,
		GL_STENCIL_PASS_DEPTH_PASS = 0xB96,
		GL_STENCIL_REF = 0xB97,
		GL_STENCIL_WRITEMASK = 0xB98,
		GL_MATRIX_MODE = 0xBA0,
		GL_NORMALIZE = 0xBA1,
		GL_VIEWPORT = 0xBA2,
		GL_MODELVIEW_STACK_DEPTH = 0xBA3,
		GL_PROJECTION_STACK_DEPTH = 0xBA4,
		GL_TEXTURE_STACK_DEPTH = 0xBA5,
		GL_MODELVIEW_MATRIX = 0xBA6,
		GL_PROJECTION_MATRIX = 0xBA7,
		GL_TEXTURE_MATRIX = 0xBA8,
		GL_ATTRIB_STACK_DEPTH = 0xBB0,
		GL_CLIENT_ATTRIB_STACK_DEPTH = 0xBB1,
		GL_ALPHA_TEST = 0xBC0,
		GL_ALPHA_TEST_FUNC = 0xBC1,
		GL_ALPHA_TEST_REF = 0xBC2,
		GL_DITHER = 0xBD0,
		GL_BLEND_DST = 0xBE0,
		GL_BLEND_SRC = 0xBE1,
		GL_BLEND = 0xBE2,
		GL_LOGIC_OP_MODE = 0xBF0,
		GL_INDEX_LOGIC_OP = 0xBF1,
		GL_COLOR_LOGIC_OP = 0xBF2,
		GL_AUX_BUFFERS = 0xC00,
		GL_DRAW_BUFFER = 0xC01,
		GL_READ_BUFFER = 0xC02,
		GL_SCISSOR_BOX = 0xC10,
		GL_SCISSOR_TEST = 0xC11,
		GL_INDEX_CLEAR_VALUE = 0xC20,
		GL_INDEX_WRITEMASK = 0xC21,
		GL_COLOR_CLEAR_VALUE = 0xC22,
		GL_COLOR_WRITEMASK = 0xC23,
		GL_INDEX_MODE = 0xC30,
		GL_RGBA_MODE = 0xC31,
		GL_DOUBLEBUFFER = 0xC32,
		GL_STEREO = 0xC33,
		GL_RENDER_MODE = 0xC40,
		GL_PERSPECTIVE_CORRECTION_HINT = 0xC50,
		GL_POINT_SMOOTH_HINT = 0xC51,
		GL_LINE_SMOOTH_HINT = 0xC52,
		GL_POLYGON_SMOOTH_HINT = 0xC53,
		GL_FOG_HINT = 0xC54,
		GL_TEXTURE_GEN_S = 0xC60,
		GL_TEXTURE_GEN_T = 0xC61,
		GL_TEXTURE_GEN_R = 0xC62,
		GL_TEXTURE_GEN_Q = 0xC63,
		GL_PIXEL_MAP_I_TO_I = 0xC70,
		GL_PIXEL_MAP_S_TO_S = 0xC71,
		GL_PIXEL_MAP_I_TO_R = 0xC72,
		GL_PIXEL_MAP_I_TO_G = 0xC73,
		GL_PIXEL_MAP_I_TO_B = 0xC74,
		GL_PIXEL_MAP_I_TO_A = 0xC75,
		GL_PIXEL_MAP_R_TO_R = 0xC76,
		GL_PIXEL_MAP_G_TO_G = 0xC77,
		GL_PIXEL_MAP_B_TO_B = 0xC78,
		GL_PIXEL_MAP_A_TO_A = 0xC79,
		GL_PIXEL_MAP_I_TO_I_SIZE = 0xCB0,
		GL_PIXEL_MAP_S_TO_S_SIZE = 0xCB1,
		GL_PIXEL_MAP_I_TO_R_SIZE = 0xCB2,
		GL_PIXEL_MAP_I_TO_G_SIZE = 0xCB3,
		GL_PIXEL_MAP_I_TO_B_SIZE = 0xCB4,
		GL_PIXEL_MAP_I_TO_A_SIZE = 0xCB5,
		GL_PIXEL_MAP_R_TO_R_SIZE = 0xCB6,
		GL_PIXEL_MAP_G_TO_G_SIZE = 0xCB7,
		GL_PIXEL_MAP_B_TO_B_SIZE = 0xCB8,
		GL_PIXEL_MAP_A_TO_A_SIZE = 0xCB9,
		GL_UNPACK_SWAP_BYTES = 0xCF0,
		GL_UNPACK_LSB_FIRST = 0xCF1,
		GL_UNPACK_ROW_LENGTH = 0xCF2,
		GL_UNPACK_SKIP_ROWS = 0xCF3,
		GL_UNPACK_SKIP_PIXELS = 0xCF4,
		GL_UNPACK_ALIGNMENT = 0xCF5,
		GL_PACK_SWAP_BYTES = 0xD00,
		GL_PACK_LSB_FIRST = 0xD01,
		GL_PACK_ROW_LENGTH = 0xD02,
		GL_PACK_SKIP_ROWS = 0xD03,
		GL_PACK_SKIP_PIXELS = 0xD04,
		GL_PACK_ALIGNMENT = 0xD05,
		GL_MAP_COLOR = 0xD10,
		GL_MAP_STENCIL = 0xD11,
		GL_INDEX_SHIFT = 0xD12,
		GL_INDEX_OFFSET = 0xD13,
		GL_RED_SCALE = 0xD14,
		GL_RED_BIAS = 0xD15,
		GL_ZOOM_X = 0xD16,
		GL_ZOOM_Y = 0xD17,
		GL_GREEN_SCALE = 0xD18,
		GL_GREEN_BIAS = 0xD19,
		GL_BLUE_SCALE = 0xD1A,
		GL_BLUE_BIAS = 0xD1B,
		GL_ALPHA_SCALE = 0xD1C,
		GL_ALPHA_BIAS = 0xD1D,
		GL_DEPTH_SCALE = 0xD1E,
		GL_DEPTH_BIAS = 0xD1F,
		GL_MAX_EVAL_ORDER = 0xD30,
		GL_MAX_LIGHTS = 0xD31,
		GL_MAX_CLIP_PLANES = 0xD32,
		GL_MAX_TEXTURE_SIZE = 0xD33,
		GL_MAX_PIXEL_MAP_TABLE = 0xD34,
		GL_MAX_ATTRIB_STACK_DEPTH = 0xD35,
		GL_MAX_MODELVIEW_STACK_DEPTH = 0xD36,
		GL_MAX_NAME_STACK_DEPTH = 0xD37,
		GL_MAX_PROJECTION_STACK_DEPTH = 0xD38,
		GL_MAX_TEXTURE_STACK_DEPTH = 0xD39,
		GL_MAX_VIEWPORT_DIMS = 0xD3A,
		GL_MAX_CLIENT_ATTRIB_STACK_DEPTH = 0xD3B,
		GL_SUBPIXEL_BITS = 0xD50,
		GL_INDEX_BITS = 0xD51,
		GL_RED_BITS = 0xD52,
		GL_GREEN_BITS = 0xD53,
		GL_BLUE_BITS = 0xD54,
		GL_ALPHA_BITS = 0xD55,
		GL_DEPTH_BITS = 0xD56,
		GL_STENCIL_BITS = 0xD57,
		GL_ACCUM_RED_BITS = 0xD58,
		GL_ACCUM_GREEN_BITS = 0xD59,
		GL_ACCUM_BLUE_BITS = 0xD5A,
		GL_ACCUM_ALPHA_BITS = 0xD5B,
		GL_NAME_STACK_DEPTH = 0xD70,
		GL_AUTO_NORMAL = 0xD80,
		GL_MAP1_COLOR_4 = 0xD90,
		GL_MAP1_INDEX = 0xD91,
		GL_MAP1_NORMAL = 0xD92,
		GL_MAP1_TEXTURE_COORD_1 = 0xD93,
		GL_MAP1_TEXTURE_COORD_2 = 0xD94,
		GL_MAP1_TEXTURE_COORD_3 = 0xD95,
		GL_MAP1_TEXTURE_COORD_4 = 0xD96,
		GL_MAP1_VERTEX_3 = 0xD97,
		GL_MAP1_VERTEX_4 = 0xD98,
		GL_MAP2_COLOR_4 = 0xDB0,
		GL_MAP2_INDEX = 0xDB1,
		GL_MAP2_NORMAL = 0xDB2,
		GL_MAP2_TEXTURE_COORD_1 = 0xDB3,
		GL_MAP2_TEXTURE_COORD_2 = 0xDB4,
		GL_MAP2_TEXTURE_COORD_3 = 0xDB5,
		GL_MAP2_TEXTURE_COORD_4 = 0xDB6,
		GL_MAP2_VERTEX_3 = 0xDB7,
		GL_MAP2_VERTEX_4 = 0xDB8,
		GL_MAP1_GRID_DOMAIN = 0xDD0,
		GL_MAP1_GRID_SEGMENTS = 0xDD1,
		GL_MAP2_GRID_DOMAIN = 0xDD2,
		GL_MAP2_GRID_SEGMENTS = 0xDD3,
		GL_TEXTURE_1D = 0xDE0,
		GL_TEXTURE_2D = 0xDE1,
		GL_FEEDBACK_BUFFER_POINTER = 0xDF0,
		GL_FEEDBACK_BUFFER_SIZE = 0xDF1,
		GL_FEEDBACK_BUFFER_TYPE = 0xDF2,
		GL_SELECTION_BUFFER_POINTER = 0xDF3,
		GL_SELECTION_BUFFER_SIZE = 0xDF4,
		GL_TEXTURE_WIDTH = 0x1000,
		GL_TEXTURE_HEIGHT = 0x1001,
		GL_TEXTURE_INTERNAL_FORMAT = 0x1003,
		GL_TEXTURE_BORDER_COLOR = 0x1004,
		GL_TEXTURE_BORDER = 0x1005,
		GL_DONT_CARE = 0x1100,
		GL_FASTEST = 0x1101,
		GL_NICEST = 0x1102,
		GL_LIGHT0 = 0x4000,
		GL_LIGHT1 = 0x4001,
		GL_LIGHT2 = 0x4002,
		GL_LIGHT3 = 0x4003,
		GL_LIGHT4 = 0x4004,
		GL_LIGHT5 = 0x4005,
		GL_LIGHT6 = 0x4006,
		GL_LIGHT7 = 0x4007,
		GL_AMBIENT = 0x1200,
		GL_DIFFUSE = 0x1201,
		GL_SPECULAR = 0x1202,
		GL_POSITION = 0x1203,
		GL_SPOT_DIRECTION = 0x1204,
		GL_SPOT_EXPONENT = 0x1205,
		GL_SPOT_CUTOFF = 0x1206,
		GL_CONSTANT_ATTENUATION = 0x1207,
		GL_LINEAR_ATTENUATION = 0x1208,
		GL_QUADRATIC_ATTENUATION = 0x1209,
		GL_COMPILE = 0x1300,
		GL_COMPILE_AND_EXECUTE = 0x1301,
		GL_CLEAR = 0x1500,
		GL_AND = 0x1501,
		GL_AND_REVERSE = 0x1502,
		GL_COPY = 0x1503,
		GL_AND_INVERTED = 0x1504,
		GL_NOOP = 0x1505,
		GL_XOR = 0x1506,
		GL_OR = 0x1507,
		GL_NOR = 0x1508,
		GL_EQUIV = 0x1509,
		GL_INVERT = 0x150A,
		GL_OR_REVERSE = 0x150B,
		GL_COPY_INVERTED = 0x150C,
		GL_OR_INVERTED = 0x150D,
		GL_NAND = 0x150E,
		GL_SET = 0x150F,
		GL_EMISSION = 0x1600,
		GL_SHININESS = 0x1601,
		GL_AMBIENT_AND_DIFFUSE = 0x1602,
		GL_COLOR_INDEXES = 0x1603,
		GL_MODELVIEW = 0x1700,
		GL_PROJECTION = 0x1701,
		GL_TEXTURE = 0x1702,
		GL_COLOR = 0x1800,
		GL_DEPTH = 0x1801,
		GL_STENCIL = 0x1802,
		GL_COLOR_INDEX = 0x1900,
		GL_STENCIL_INDEX = 0x1901,
		GL_DEPTH_COMPONENT = 0x1902,
		GL_RED = 0x1903,
		GL_GREEN = 0x1904,
		GL_BLUE = 0x1905,
		GL_ALPHA = 0x1906,
		GL_RGB = 0x1907,
		GL_RGBA = 0x1908,
		GL_LUMINANCE = 0x1909,
		GL_LUMINANCE_ALPHA = 0x190A,
		GL_BITMAP = 0x1A00,
		GL_POINT = 0x1B00,
		GL_LINE = 0x1B01,
		GL_FILL = 0x1B02,
		GL_RENDER = 0x1C00,
		GL_FEEDBACK = 0x1C01,
		GL_SELECT = 0x1C02,
		GL_FLAT = 0x1D00,
		GL_SMOOTH = 0x1D01,
		GL_KEEP = 0x1E00,
		GL_REPLACE = 0x1E01,
		GL_INCR = 0x1E02,
		GL_DECR = 0x1E03,
		GL_VENDOR = 0x1F00,
		GL_RENDERER = 0x1F01,
		GL_VERSION = 0x1F02,
		GL_EXTENSIONS = 0x1F03,
		GL_S = 0x2000,
		GL_T = 0x2001,
		GL_R = 0x2002,
		GL_Q = 0x2003,
		GL_MODULATE = 0x2100,
		GL_DECAL = 0x2101,
		GL_TEXTURE_ENV_MODE = 0x2200,
		GL_TEXTURE_ENV_COLOR = 0x2201,
		GL_TEXTURE_ENV = 0x2300,
		GL_EYE_LINEAR = 0x2400,
		GL_OBJECT_LINEAR = 0x2401,
		GL_SPHERE_MAP = 0x2402,
		GL_TEXTURE_GEN_MODE = 0x2500,
		GL_OBJECT_PLANE = 0x2501,
		GL_EYE_PLANE = 0x2502,
		GL_NEAREST = 0x2600,
		GL_LINEAR = 0x2601,
		GL_NEAREST_MIPMAP_NEAREST = 0x2700,
		GL_LINEAR_MIPMAP_NEAREST = 0x2701,
		GL_NEAREST_MIPMAP_LINEAR = 0x2702,
		GL_LINEAR_MIPMAP_LINEAR = 0x2703,
		GL_TEXTURE_MAG_FILTER = 0x2800,
		GL_TEXTURE_MIN_FILTER = 0x2801,
		GL_TEXTURE_WRAP_S = 0x2802,
		GL_TEXTURE_WRAP_T = 0x2803,
		GL_CLAMP = 0x2900,
		GL_REPEAT = 0x2901,
		GL_CLIENT_PIXEL_STORE_BIT = 0x1,
		GL_CLIENT_VERTEX_ARRAY_BIT = 0x2,
		GL_ALL_CLIENT_ATTRIB_BITS = 0xFFFFFFFF,
		GL_POLYGON_OFFSET_FACTOR = 0x8038,
		GL_POLYGON_OFFSET_UNITS = 0x2A00,
		GL_POLYGON_OFFSET_POINT = 0x2A01,
		GL_POLYGON_OFFSET_LINE = 0x2A02,
		GL_POLYGON_OFFSET_FILL = 0x8037,
		GL_ALPHA4 = 0x803B,
		GL_ALPHA8 = 0x803C,
		GL_ALPHA12 = 0x803D,
		GL_ALPHA16 = 0x803E,
		GL_LUMINANCE4 = 0x803F,
		GL_LUMINANCE8 = 0x8040,
		GL_LUMINANCE12 = 0x8041,
		GL_LUMINANCE16 = 0x8042,
		GL_LUMINANCE4_ALPHA4 = 0x8043,
		GL_LUMINANCE6_ALPHA2 = 0x8044,
		GL_LUMINANCE8_ALPHA8 = 0x8045,
		GL_LUMINANCE12_ALPHA4 = 0x8046,
		GL_LUMINANCE12_ALPHA12 = 0x8047,
		GL_LUMINANCE16_ALPHA16 = 0x8048,
		GL_INTENSITY = 0x8049,
		GL_INTENSITY4 = 0x804A,
		GL_INTENSITY8 = 0x804B,
		GL_INTENSITY12 = 0x804C,
		GL_INTENSITY16 = 0x804D,
		GL_R3_G3_B2 = 0x2A10,
		GL_RGB4 = 0x804F,
		GL_RGB5 = 0x8050,
		GL_RGB8 = 0x8051,
		GL_RGB10 = 0x8052,
		GL_RGB12 = 0x8053,
		GL_RGB16 = 0x8054,
		GL_RGBA2 = 0x8055,
		GL_RGBA4 = 0x8056,
		GL_RGB5_A1 = 0x8057,
		GL_RGBA8 = 0x8058,
		GL_RGB10_A2 = 0x8059,
		GL_RGBA12 = 0x805A,
		GL_RGBA16 = 0x805B,
		GL_TEXTURE_RED_SIZE = 0x805C,
		GL_TEXTURE_GREEN_SIZE = 0x805D,
		GL_TEXTURE_BLUE_SIZE = 0x805E,
		GL_TEXTURE_ALPHA_SIZE = 0x805F,
		GL_TEXTURE_LUMINANCE_SIZE = 0x8060,
		GL_TEXTURE_INTENSITY_SIZE = 0x8061,
		GL_PROXY_TEXTURE_1D = 0x8063,
		GL_PROXY_TEXTURE_2D = 0x8064,
		GL_TEXTURE_PRIORITY = 0x8066,
		GL_TEXTURE_RESIDENT = 0x8067,
		GL_TEXTURE_BINDING_1D = 0x8068,
		GL_TEXTURE_BINDING_2D = 0x8069,
		GL_VERTEX_ARRAY = 0x8074,
		GL_NORMAL_ARRAY = 0x8075,
		GL_COLOR_ARRAY = 0x8076,
		GL_INDEX_ARRAY = 0x8077,
		GL_TEXTURE_COORD_ARRAY = 0x8078,
		GL_EDGE_FLAG_ARRAY = 0x8079,
		GL_VERTEX_ARRAY_SIZE = 0x807A,
		GL_VERTEX_ARRAY_TYPE = 0x807B,
		GL_VERTEX_ARRAY_STRIDE = 0x807C,
		GL_NORMAL_ARRAY_TYPE = 0x807E,
		GL_NORMAL_ARRAY_STRIDE = 0x807F,
		GL_COLOR_ARRAY_SIZE = 0x8081,
		GL_COLOR_ARRAY_TYPE = 0x8082,
		GL_COLOR_ARRAY_STRIDE = 0x8083,
		GL_INDEX_ARRAY_TYPE = 0x8085,
		GL_INDEX_ARRAY_STRIDE = 0x8086,
		GL_TEXTURE_COORD_ARRAY_SIZE = 0x8088,
		GL_TEXTURE_COORD_ARRAY_TYPE = 0x8089,
		GL_TEXTURE_COORD_ARRAY_STRIDE = 0x808A,
		GL_EDGE_FLAG_ARRAY_STRIDE = 0x808C,
		GL_VERTEX_ARRAY_POINTER = 0x808E,
		GL_NORMAL_ARRAY_POINTER = 0x808F,
		GL_COLOR_ARRAY_POINTER = 0x8090,
		GL_INDEX_ARRAY_POINTER = 0x8091,
		GL_TEXTURE_COORD_ARRAY_POINTER = 0x8092,
		GL_EDGE_FLAG_ARRAY_POINTER = 0x8093,
		GL_V2F = 0x2A20,
		GL_V3F = 0x2A21,
		GL_C4UB_V2F = 0x2A22,
		GL_C4UB_V3F = 0x2A23,
		GL_C3F_V3F = 0x2A24,
		GL_N3F_V3F = 0x2A25,
		GL_C4F_N3F_V3F = 0x2A26,
		GL_T2F_V3F = 0x2A27,
		GL_T4F_V4F = 0x2A28,
		GL_T2F_C4UB_V3F = 0x2A29,
		GL_T2F_C3F_V3F = 0x2A2A,
		GL_T2F_N3F_V3F = 0x2A2B,
		GL_T2F_C4F_N3F_V3F = 0x2A2C,
		GL_T4F_C4F_N3F_V4F = 0x2A2D,
		GL_LOGIC_OP = 0xBF1,
		GL_TEXTURE_COMPONENTS = 0x1003;

	private GL11() {}

	public static void glAccum(int op, float value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glAccum;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglAccum(op, value, function_pointer);
	}
	static native void nglAccum(int op, float value, long function_pointer);

	public static void glAlphaFunc(int func, float ref) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glAlphaFunc;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglAlphaFunc(func, ref, function_pointer);
	}
	static native void nglAlphaFunc(int func, float ref, long function_pointer);

	public static void glClearColor(float red, float green, float blue, float alpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearColor;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClearColor(red, green, blue, alpha, function_pointer);
	}
	static native void nglClearColor(float red, float green, float blue, float alpha, long function_pointer);

	public static void glClearAccum(float red, float green, float blue, float alpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearAccum;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClearAccum(red, green, blue, alpha, function_pointer);
	}
	static native void nglClearAccum(float red, float green, float blue, float alpha, long function_pointer);

	public static void glClear(int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClear;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClear(mask, function_pointer);
	}
	static native void nglClear(int mask, long function_pointer);

	public static void glCallLists(ByteBuffer lists) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCallLists;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(lists);
		nglCallLists(lists.remaining(), GL11.GL_UNSIGNED_BYTE, MemoryUtil.getAddress(lists), function_pointer);
	}
	public static void glCallLists(IntBuffer lists) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCallLists;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(lists);
		nglCallLists(lists.remaining(), GL11.GL_UNSIGNED_INT, MemoryUtil.getAddress(lists), function_pointer);
	}
	public static void glCallLists(ShortBuffer lists) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCallLists;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(lists);
		nglCallLists(lists.remaining(), GL11.GL_UNSIGNED_SHORT, MemoryUtil.getAddress(lists), function_pointer);
	}
	static native void nglCallLists(int lists_n, int type, long lists, long function_pointer);

	public static void glCallList(int list) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCallList;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCallList(list, function_pointer);
	}
	static native void nglCallList(int list, long function_pointer);

	public static void glBlendFunc(int sfactor, int dfactor) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendFunc;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendFunc(sfactor, dfactor, function_pointer);
	}
	static native void nglBlendFunc(int sfactor, int dfactor, long function_pointer);

	public static void glBitmap(int width, int height, float xorig, float yorig, float xmove, float ymove, ByteBuffer bitmap) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBitmap;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (bitmap != null)
			BufferChecks.checkBuffer(bitmap, (((width + 7)/8)*height));
		nglBitmap(width, height, xorig, yorig, xmove, ymove, MemoryUtil.getAddressSafe(bitmap), function_pointer);
	}
	static native void nglBitmap(int width, int height, float xorig, float yorig, float xmove, float ymove, long bitmap, long function_pointer);
	public static void glBitmap(int width, int height, float xorig, float yorig, float xmove, float ymove, long bitmap_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBitmap;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglBitmapBO(width, height, xorig, yorig, xmove, ymove, bitmap_buffer_offset, function_pointer);
	}
	static native void nglBitmapBO(int width, int height, float xorig, float yorig, float xmove, float ymove, long bitmap_buffer_offset, long function_pointer);

	public static void glBindTexture(int target, int texture) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindTexture;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindTexture(target, texture, function_pointer);
	}
	static native void nglBindTexture(int target, int texture, long function_pointer);

	public static void glPrioritizeTextures(IntBuffer textures, FloatBuffer priorities) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPrioritizeTextures;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(textures);
		BufferChecks.checkBuffer(priorities, textures.remaining());
		nglPrioritizeTextures(textures.remaining(), MemoryUtil.getAddress(textures), MemoryUtil.getAddress(priorities), function_pointer);
	}
	static native void nglPrioritizeTextures(int textures_n, long textures, long priorities, long function_pointer);

	public static boolean glAreTexturesResident(IntBuffer textures, ByteBuffer residences) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glAreTexturesResident;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(textures);
		BufferChecks.checkBuffer(residences, textures.remaining());
		boolean __result = nglAreTexturesResident(textures.remaining(), MemoryUtil.getAddress(textures), MemoryUtil.getAddress(residences), function_pointer);
		return __result;
	}
	static native boolean nglAreTexturesResident(int textures_n, long textures, long residences, long function_pointer);

	public static void glBegin(int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBegin;
		BufferChecks.checkFunctionAddress(function_pointer);
		if ( ContextCapabilities.DEBUG ) StateTracker.setBeginEnd(caps, true);
		nglBegin(mode, function_pointer);
	}
	static native void nglBegin(int mode, long function_pointer);

	public static void glEnd() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEnd;
		BufferChecks.checkFunctionAddress(function_pointer);
		if ( ContextCapabilities.DEBUG ) StateTracker.setBeginEnd(caps, false);
		nglEnd(function_pointer);
	}
	static native void nglEnd(long function_pointer);

	public static void glArrayElement(int i) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glArrayElement;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglArrayElement(i, function_pointer);
	}
	static native void nglArrayElement(int i, long function_pointer);

	public static void glClearDepth(double depth) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearDepth;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClearDepth(depth, function_pointer);
	}
	static native void nglClearDepth(double depth, long function_pointer);

	public static void glDeleteLists(int list, int range) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteLists;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteLists(list, range, function_pointer);
	}
	static native void nglDeleteLists(int list, int range, long function_pointer);

	public static void glDeleteTextures(IntBuffer textures) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteTextures;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(textures);
		nglDeleteTextures(textures.remaining(), MemoryUtil.getAddress(textures), function_pointer);
	}
	static native void nglDeleteTextures(int textures_n, long textures, long function_pointer);

	/** Overloads glDeleteTextures. */
	public static void glDeleteTextures(int texture) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteTextures;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteTextures(1, APIUtil.getInt(caps, texture), function_pointer);
	}

	public static void glCullFace(int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCullFace;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCullFace(mode, function_pointer);
	}
	static native void nglCullFace(int mode, long function_pointer);

	public static void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyTexSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height, function_pointer);
	}
	static native void nglCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height, long function_pointer);

	public static void glCopyTexSubImage1D(int target, int level, int xoffset, int x, int y, int width) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyTexSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyTexSubImage1D(target, level, xoffset, x, y, width, function_pointer);
	}
	static native void nglCopyTexSubImage1D(int target, int level, int xoffset, int x, int y, int width, long function_pointer);

	public static void glCopyTexImage2D(int target, int level, int internalFormat, int x, int y, int width, int height, int border) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyTexImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyTexImage2D(target, level, internalFormat, x, y, width, height, border, function_pointer);
	}
	static native void nglCopyTexImage2D(int target, int level, int internalFormat, int x, int y, int width, int height, int border, long function_pointer);

	public static void glCopyTexImage1D(int target, int level, int internalFormat, int x, int y, int width, int border) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyTexImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyTexImage1D(target, level, internalFormat, x, y, width, border, function_pointer);
	}
	static native void nglCopyTexImage1D(int target, int level, int internalFormat, int x, int y, int width, int border, long function_pointer);

	public static void glCopyPixels(int x, int y, int width, int height, int type) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyPixels(x, y, width, height, type, function_pointer);
	}
	static native void nglCopyPixels(int x, int y, int width, int height, int type, long function_pointer);

	public static void glColorPointer(int size, int stride, DoubleBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glColorPointer_pointer = pointer;
		nglColorPointer(size, GL11.GL_DOUBLE, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glColorPointer(int size, int stride, FloatBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glColorPointer_pointer = pointer;
		nglColorPointer(size, GL11.GL_FLOAT, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glColorPointer(int size, boolean unsigned, int stride, ByteBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glColorPointer_pointer = pointer;
		nglColorPointer(size, unsigned ? GL11.GL_UNSIGNED_BYTE : GL11.GL_BYTE, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	static native void nglColorPointer(int size, int type, int stride, long pointer, long function_pointer);
	public static void glColorPointer(int size, int type, int stride, long pointer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglColorPointerBO(size, type, stride, pointer_buffer_offset, function_pointer);
	}
	static native void nglColorPointerBO(int size, int type, int stride, long pointer_buffer_offset, long function_pointer);

	/** Overloads glColorPointer. */
	public static void glColorPointer(int size, int type, int stride, ByteBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glColorPointer_pointer = pointer;
		nglColorPointer(size, type, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}

	public static void glColorMaterial(int face, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorMaterial;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColorMaterial(face, mode, function_pointer);
	}
	static native void nglColorMaterial(int face, int mode, long function_pointer);

	public static void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorMask;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColorMask(red, green, blue, alpha, function_pointer);
	}
	static native void nglColorMask(boolean red, boolean green, boolean blue, boolean alpha, long function_pointer);

	public static void glColor3b(byte red, byte green, byte blue) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColor3b;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColor3b(red, green, blue, function_pointer);
	}
	static native void nglColor3b(byte red, byte green, byte blue, long function_pointer);

	public static void glColor3f(float red, float green, float blue) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColor3f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColor3f(red, green, blue, function_pointer);
	}
	static native void nglColor3f(float red, float green, float blue, long function_pointer);

	public static void glColor3d(double red, double green, double blue) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColor3d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColor3d(red, green, blue, function_pointer);
	}
	static native void nglColor3d(double red, double green, double blue, long function_pointer);

	public static void glColor3ub(byte red, byte green, byte blue) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColor3ub;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColor3ub(red, green, blue, function_pointer);
	}
	static native void nglColor3ub(byte red, byte green, byte blue, long function_pointer);

	public static void glColor4b(byte red, byte green, byte blue, byte alpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColor4b;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColor4b(red, green, blue, alpha, function_pointer);
	}
	static native void nglColor4b(byte red, byte green, byte blue, byte alpha, long function_pointer);

	public static void glColor4f(float red, float green, float blue, float alpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColor4f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColor4f(red, green, blue, alpha, function_pointer);
	}
	static native void nglColor4f(float red, float green, float blue, float alpha, long function_pointer);

	public static void glColor4d(double red, double green, double blue, double alpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColor4d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColor4d(red, green, blue, alpha, function_pointer);
	}
	static native void nglColor4d(double red, double green, double blue, double alpha, long function_pointer);

	public static void glColor4ub(byte red, byte green, byte blue, byte alpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColor4ub;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColor4ub(red, green, blue, alpha, function_pointer);
	}
	static native void nglColor4ub(byte red, byte green, byte blue, byte alpha, long function_pointer);

	public static void glClipPlane(int plane, DoubleBuffer equation) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClipPlane;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(equation, 4);
		nglClipPlane(plane, MemoryUtil.getAddress(equation), function_pointer);
	}
	static native void nglClipPlane(int plane, long equation, long function_pointer);

	public static void glClearStencil(int s) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearStencil;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClearStencil(s, function_pointer);
	}
	static native void nglClearStencil(int s, long function_pointer);

	public static void glEvalPoint1(int i) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEvalPoint1;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEvalPoint1(i, function_pointer);
	}
	static native void nglEvalPoint1(int i, long function_pointer);

	public static void glEvalPoint2(int i, int j) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEvalPoint2;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEvalPoint2(i, j, function_pointer);
	}
	static native void nglEvalPoint2(int i, int j, long function_pointer);

	public static void glEvalMesh1(int mode, int i1, int i2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEvalMesh1;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEvalMesh1(mode, i1, i2, function_pointer);
	}
	static native void nglEvalMesh1(int mode, int i1, int i2, long function_pointer);

	public static void glEvalMesh2(int mode, int i1, int i2, int j1, int j2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEvalMesh2;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEvalMesh2(mode, i1, i2, j1, j2, function_pointer);
	}
	static native void nglEvalMesh2(int mode, int i1, int i2, int j1, int j2, long function_pointer);

	public static void glEvalCoord1f(float u) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEvalCoord1f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEvalCoord1f(u, function_pointer);
	}
	static native void nglEvalCoord1f(float u, long function_pointer);

	public static void glEvalCoord1d(double u) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEvalCoord1d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEvalCoord1d(u, function_pointer);
	}
	static native void nglEvalCoord1d(double u, long function_pointer);

	public static void glEvalCoord2f(float u, float v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEvalCoord2f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEvalCoord2f(u, v, function_pointer);
	}
	static native void nglEvalCoord2f(float u, float v, long function_pointer);

	public static void glEvalCoord2d(double u, double v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEvalCoord2d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEvalCoord2d(u, v, function_pointer);
	}
	static native void nglEvalCoord2d(double u, double v, long function_pointer);

	public static void glEnableClientState(int cap) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEnableClientState;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEnableClientState(cap, function_pointer);
	}
	static native void nglEnableClientState(int cap, long function_pointer);

	public static void glDisableClientState(int cap) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDisableClientState;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDisableClientState(cap, function_pointer);
	}
	static native void nglDisableClientState(int cap, long function_pointer);

	public static void glEnable(int cap) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEnable;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEnable(cap, function_pointer);
	}
	static native void nglEnable(int cap, long function_pointer);

	public static void glDisable(int cap) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDisable;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDisable(cap, function_pointer);
	}
	static native void nglDisable(int cap, long function_pointer);

	public static void glEdgeFlagPointer(int stride, ByteBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEdgeFlagPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glEdgeFlagPointer_pointer = pointer;
		nglEdgeFlagPointer(stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	static native void nglEdgeFlagPointer(int stride, long pointer, long function_pointer);
	public static void glEdgeFlagPointer(int stride, long pointer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEdgeFlagPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglEdgeFlagPointerBO(stride, pointer_buffer_offset, function_pointer);
	}
	static native void nglEdgeFlagPointerBO(int stride, long pointer_buffer_offset, long function_pointer);

	public static void glEdgeFlag(boolean flag) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEdgeFlag;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEdgeFlag(flag, function_pointer);
	}
	static native void nglEdgeFlag(boolean flag, long function_pointer);

	public static void glDrawPixels(int width, int height, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglDrawPixels(width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glDrawPixels(int width, int height, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglDrawPixels(width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glDrawPixels(int width, int height, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglDrawPixels(width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	static native void nglDrawPixels(int width, int height, int format, int type, long pixels, long function_pointer);
	public static void glDrawPixels(int width, int height, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglDrawPixelsBO(width, height, format, type, pixels_buffer_offset, function_pointer);
	}
	static native void nglDrawPixelsBO(int width, int height, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glDrawElements(int mode, ByteBuffer indices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElements;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElements(mode, indices.remaining(), GL11.GL_UNSIGNED_BYTE, MemoryUtil.getAddress(indices), function_pointer);
	}
	public static void glDrawElements(int mode, IntBuffer indices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElements;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElements(mode, indices.remaining(), GL11.GL_UNSIGNED_INT, MemoryUtil.getAddress(indices), function_pointer);
	}
	public static void glDrawElements(int mode, ShortBuffer indices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElements;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElements(mode, indices.remaining(), GL11.GL_UNSIGNED_SHORT, MemoryUtil.getAddress(indices), function_pointer);
	}
	static native void nglDrawElements(int mode, int indices_count, int type, long indices, long function_pointer);
	public static void glDrawElements(int mode, int indices_count, int type, long indices_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElements;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOenabled(caps);
		nglDrawElementsBO(mode, indices_count, type, indices_buffer_offset, function_pointer);
	}
	static native void nglDrawElementsBO(int mode, int indices_count, int type, long indices_buffer_offset, long function_pointer);

	/** Overloads glDrawElements. */
	public static void glDrawElements(int mode, int count, int type, ByteBuffer indices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElements;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkBuffer(indices, count);
		nglDrawElements(mode, count, type, MemoryUtil.getAddress(indices), function_pointer);
	}

	public static void glDrawBuffer(int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDrawBuffer(mode, function_pointer);
	}
	static native void nglDrawBuffer(int mode, long function_pointer);

	public static void glDrawArrays(int mode, int first, int count) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawArrays;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDrawArrays(mode, first, count, function_pointer);
	}
	static native void nglDrawArrays(int mode, int first, int count, long function_pointer);

	public static void glDepthRange(double zNear, double zFar) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDepthRange;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDepthRange(zNear, zFar, function_pointer);
	}
	static native void nglDepthRange(double zNear, double zFar, long function_pointer);

	public static void glDepthMask(boolean flag) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDepthMask;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDepthMask(flag, function_pointer);
	}
	static native void nglDepthMask(boolean flag, long function_pointer);

	public static void glDepthFunc(int func) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDepthFunc;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDepthFunc(func, function_pointer);
	}
	static native void nglDepthFunc(int func, long function_pointer);

	public static void glFeedbackBuffer(int type, FloatBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFeedbackBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(buffer);
		nglFeedbackBuffer(buffer.remaining(), type, MemoryUtil.getAddress(buffer), function_pointer);
	}
	static native void nglFeedbackBuffer(int buffer_size, int type, long buffer, long function_pointer);

	public static void glGetPixelMap(int map, FloatBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPixelMapfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(values, 256);
		nglGetPixelMapfv(map, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglGetPixelMapfv(int map, long values, long function_pointer);
	public static void glGetPixelMapfv(int map, long values_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPixelMapfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetPixelMapfvBO(map, values_buffer_offset, function_pointer);
	}
	static native void nglGetPixelMapfvBO(int map, long values_buffer_offset, long function_pointer);

	public static void glGetPixelMapu(int map, IntBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPixelMapuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(values, 256);
		nglGetPixelMapuiv(map, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglGetPixelMapuiv(int map, long values, long function_pointer);
	public static void glGetPixelMapuiv(int map, long values_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPixelMapuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetPixelMapuivBO(map, values_buffer_offset, function_pointer);
	}
	static native void nglGetPixelMapuivBO(int map, long values_buffer_offset, long function_pointer);

	public static void glGetPixelMapu(int map, ShortBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPixelMapusv;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(values, 256);
		nglGetPixelMapusv(map, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglGetPixelMapusv(int map, long values, long function_pointer);
	public static void glGetPixelMapusv(int map, long values_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPixelMapusv;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetPixelMapusvBO(map, values_buffer_offset, function_pointer);
	}
	static native void nglGetPixelMapusvBO(int map, long values_buffer_offset, long function_pointer);

	public static void glGetMaterial(int face, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMaterialfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetMaterialfv(face, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetMaterialfv(int face, int pname, long params, long function_pointer);

	public static void glGetMaterial(int face, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMaterialiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetMaterialiv(face, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetMaterialiv(int face, int pname, long params, long function_pointer);

	public static void glGetMap(int target, int query, FloatBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMapfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 256);
		nglGetMapfv(target, query, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglGetMapfv(int target, int query, long v, long function_pointer);

	public static void glGetMap(int target, int query, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMapdv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 256);
		nglGetMapdv(target, query, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglGetMapdv(int target, int query, long v, long function_pointer);

	public static void glGetMap(int target, int query, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMapiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 256);
		nglGetMapiv(target, query, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglGetMapiv(int target, int query, long v, long function_pointer);

	public static void glGetLight(int light, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetLightfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetLightfv(light, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetLightfv(int light, int pname, long params, long function_pointer);

	public static void glGetLight(int light, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetLightiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetLightiv(light, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetLightiv(int light, int pname, long params, long function_pointer);

	public static int glGetError() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetError;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetError(function_pointer);
		return __result;
	}
	static native int nglGetError(long function_pointer);

	public static void glGetClipPlane(int plane, DoubleBuffer equation) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetClipPlane;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(equation, 4);
		nglGetClipPlane(plane, MemoryUtil.getAddress(equation), function_pointer);
	}
	static native void nglGetClipPlane(int plane, long equation, long function_pointer);

	public static void glGetBoolean(int pname, ByteBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBooleanv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 16);
		nglGetBooleanv(pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetBooleanv(int pname, long params, long function_pointer);

	/** Overloads glGetBooleanv. */
	public static boolean glGetBoolean(int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBooleanv;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer params = APIUtil.getBufferByte(caps, 1);
		nglGetBooleanv(pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0) == 1;
	}

	public static void glGetDouble(int pname, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetDoublev;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 16);
		nglGetDoublev(pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetDoublev(int pname, long params, long function_pointer);

	/** Overloads glGetDoublev. */
	public static double glGetDouble(int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetDoublev;
		BufferChecks.checkFunctionAddress(function_pointer);
		DoubleBuffer params = APIUtil.getBufferDouble(caps);
		nglGetDoublev(pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetFloat(int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFloatv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 16);
		nglGetFloatv(pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetFloatv(int pname, long params, long function_pointer);

	/** Overloads glGetFloatv. */
	public static float glGetFloat(int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFloatv;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params = APIUtil.getBufferFloat(caps);
		nglGetFloatv(pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetInteger(int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetIntegerv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 16);
		nglGetIntegerv(pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetIntegerv(int pname, long params, long function_pointer);

	/** Overloads glGetIntegerv. */
	public static int glGetInteger(int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetIntegerv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetIntegerv(pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGenTextures(IntBuffer textures) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenTextures;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(textures);
		nglGenTextures(textures.remaining(), MemoryUtil.getAddress(textures), function_pointer);
	}
	static native void nglGenTextures(int textures_n, long textures, long function_pointer);

	/** Overloads glGenTextures. */
	public static int glGenTextures() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenTextures;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer textures = APIUtil.getBufferInt(caps);
		nglGenTextures(1, MemoryUtil.getAddress(textures), function_pointer);
		return textures.get(0);
	}

	public static int glGenLists(int range) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenLists;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGenLists(range, function_pointer);
		return __result;
	}
	static native int nglGenLists(int range, long function_pointer);

	public static void glFrustum(double left, double right, double bottom, double top, double zNear, double zFar) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFrustum;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFrustum(left, right, bottom, top, zNear, zFar, function_pointer);
	}
	static native void nglFrustum(double left, double right, double bottom, double top, double zNear, double zFar, long function_pointer);

	public static void glFrontFace(int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFrontFace;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFrontFace(mode, function_pointer);
	}
	static native void nglFrontFace(int mode, long function_pointer);

	public static void glFogf(int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFogf(pname, param, function_pointer);
	}
	static native void nglFogf(int pname, float param, long function_pointer);

	public static void glFogi(int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogi;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFogi(pname, param, function_pointer);
	}
	static native void nglFogi(int pname, int param, long function_pointer);

	public static void glFog(int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglFogfv(pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglFogfv(int pname, long params, long function_pointer);

	public static void glFog(int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglFogiv(pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglFogiv(int pname, long params, long function_pointer);

	public static void glFlush() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFlush;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFlush(function_pointer);
	}
	static native void nglFlush(long function_pointer);

	public static void glFinish() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFinish;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFinish(function_pointer);
	}
	static native void nglFinish(long function_pointer);

	public static ByteBuffer glGetPointer(int pname, long result_size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPointerv;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer __result = nglGetPointerv(pname, result_size, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglGetPointerv(int pname, long result_size, long function_pointer);

	public static boolean glIsEnabled(int cap) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsEnabled;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsEnabled(cap, function_pointer);
		return __result;
	}
	static native boolean nglIsEnabled(int cap, long function_pointer);

	public static void glInterleavedArrays(int format, int stride, ByteBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInterleavedArrays;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		nglInterleavedArrays(format, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glInterleavedArrays(int format, int stride, DoubleBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInterleavedArrays;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		nglInterleavedArrays(format, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glInterleavedArrays(int format, int stride, FloatBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInterleavedArrays;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		nglInterleavedArrays(format, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glInterleavedArrays(int format, int stride, IntBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInterleavedArrays;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		nglInterleavedArrays(format, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glInterleavedArrays(int format, int stride, ShortBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInterleavedArrays;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		nglInterleavedArrays(format, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	static native void nglInterleavedArrays(int format, int stride, long pointer, long function_pointer);
	public static void glInterleavedArrays(int format, int stride, long pointer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInterleavedArrays;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglInterleavedArraysBO(format, stride, pointer_buffer_offset, function_pointer);
	}
	static native void nglInterleavedArraysBO(int format, int stride, long pointer_buffer_offset, long function_pointer);

	public static void glInitNames() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInitNames;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglInitNames(function_pointer);
	}
	static native void nglInitNames(long function_pointer);

	public static void glHint(int target, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glHint;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglHint(target, mode, function_pointer);
	}
	static native void nglHint(int target, int mode, long function_pointer);

	public static void glGetTexParameter(int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetTexParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTexParameterfv(int target, int pname, long params, long function_pointer);

	/** Overloads glGetTexParameterfv. */
	public static float glGetTexParameterf(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params = APIUtil.getBufferFloat(caps);
		nglGetTexParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTexParameter(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetTexParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTexParameteriv(int target, int pname, long params, long function_pointer);

	/** Overloads glGetTexParameteriv. */
	public static int glGetTexParameteri(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetTexParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTexLevelParameter(int target, int level, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexLevelParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetTexLevelParameterfv(target, level, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTexLevelParameterfv(int target, int level, int pname, long params, long function_pointer);

	/** Overloads glGetTexLevelParameterfv. */
	public static float glGetTexLevelParameterf(int target, int level, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexLevelParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params = APIUtil.getBufferFloat(caps);
		nglGetTexLevelParameterfv(target, level, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTexLevelParameter(int target, int level, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexLevelParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetTexLevelParameteriv(target, level, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTexLevelParameteriv(int target, int level, int pname, long params, long function_pointer);

	/** Overloads glGetTexLevelParameteriv. */
	public static int glGetTexLevelParameteri(int target, int level, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexLevelParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetTexLevelParameteriv(target, level, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTexImage(int target, int level, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetTexImage(target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetTexImage(int target, int level, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetTexImage(target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetTexImage(int target, int level, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetTexImage(target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetTexImage(int target, int level, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetTexImage(target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetTexImage(int target, int level, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetTexImage(target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	static native void nglGetTexImage(int target, int level, int format, int type, long pixels, long function_pointer);
	public static void glGetTexImage(int target, int level, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetTexImageBO(target, level, format, type, pixels_buffer_offset, function_pointer);
	}
	static native void nglGetTexImageBO(int target, int level, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glGetTexGen(int coord, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexGeniv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetTexGeniv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTexGeniv(int coord, int pname, long params, long function_pointer);

	/** Overloads glGetTexGeniv. */
	public static int glGetTexGeni(int coord, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexGeniv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetTexGeniv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTexGen(int coord, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexGenfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetTexGenfv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTexGenfv(int coord, int pname, long params, long function_pointer);

	/** Overloads glGetTexGenfv. */
	public static float glGetTexGenf(int coord, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexGenfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params = APIUtil.getBufferFloat(caps);
		nglGetTexGenfv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTexGen(int coord, int pname, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexGendv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetTexGendv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTexGendv(int coord, int pname, long params, long function_pointer);

	/** Overloads glGetTexGendv. */
	public static double glGetTexGend(int coord, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexGendv;
		BufferChecks.checkFunctionAddress(function_pointer);
		DoubleBuffer params = APIUtil.getBufferDouble(caps);
		nglGetTexGendv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTexEnv(int coord, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexEnviv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetTexEnviv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTexEnviv(int coord, int pname, long params, long function_pointer);

	/** Overloads glGetTexEnviv. */
	public static int glGetTexEnvi(int coord, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexEnviv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetTexEnviv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTexEnv(int coord, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexEnvfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetTexEnvfv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTexEnvfv(int coord, int pname, long params, long function_pointer);

	/** Overloads glGetTexEnvfv. */
	public static float glGetTexEnvf(int coord, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexEnvfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params = APIUtil.getBufferFloat(caps);
		nglGetTexEnvfv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static String glGetString(int name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetString;
		BufferChecks.checkFunctionAddress(function_pointer);
		String __result = nglGetString(name, function_pointer);
		return __result;
	}
	static native String nglGetString(int name, long function_pointer);

	public static void glGetPolygonStipple(ByteBuffer mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPolygonStipple;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(mask, 128);
		nglGetPolygonStipple(MemoryUtil.getAddress(mask), function_pointer);
	}
	static native void nglGetPolygonStipple(long mask, long function_pointer);
	public static void glGetPolygonStipple(long mask_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetPolygonStipple;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetPolygonStippleBO(mask_buffer_offset, function_pointer);
	}
	static native void nglGetPolygonStippleBO(long mask_buffer_offset, long function_pointer);

	public static boolean glIsList(int list) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsList;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsList(list, function_pointer);
		return __result;
	}
	static native boolean nglIsList(int list, long function_pointer);

	public static void glMaterialf(int face, int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMaterialf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMaterialf(face, pname, param, function_pointer);
	}
	static native void nglMaterialf(int face, int pname, float param, long function_pointer);

	public static void glMateriali(int face, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMateriali;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMateriali(face, pname, param, function_pointer);
	}
	static native void nglMateriali(int face, int pname, int param, long function_pointer);

	public static void glMaterial(int face, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMaterialfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglMaterialfv(face, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglMaterialfv(int face, int pname, long params, long function_pointer);

	public static void glMaterial(int face, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMaterialiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglMaterialiv(face, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglMaterialiv(int face, int pname, long params, long function_pointer);

	public static void glMapGrid1f(int un, float u1, float u2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapGrid1f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMapGrid1f(un, u1, u2, function_pointer);
	}
	static native void nglMapGrid1f(int un, float u1, float u2, long function_pointer);

	public static void glMapGrid1d(int un, double u1, double u2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapGrid1d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMapGrid1d(un, u1, u2, function_pointer);
	}
	static native void nglMapGrid1d(int un, double u1, double u2, long function_pointer);

	public static void glMapGrid2f(int un, float u1, float u2, int vn, float v1, float v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapGrid2f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMapGrid2f(un, u1, u2, vn, v1, v2, function_pointer);
	}
	static native void nglMapGrid2f(int un, float u1, float u2, int vn, float v1, float v2, long function_pointer);

	public static void glMapGrid2d(int un, double u1, double u2, int vn, double v1, double v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapGrid2d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMapGrid2d(un, u1, u2, vn, v1, v2, function_pointer);
	}
	static native void nglMapGrid2d(int un, double u1, double u2, int vn, double v1, double v2, long function_pointer);

	public static void glMap2f(int target, float u1, float u2, int ustride, int uorder, float v1, float v2, int vstride, int vorder, FloatBuffer points) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMap2f;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(points);
		nglMap2f(target, u1, u2, ustride, uorder, v1, v2, vstride, vorder, MemoryUtil.getAddress(points), function_pointer);
	}
	static native void nglMap2f(int target, float u1, float u2, int ustride, int uorder, float v1, float v2, int vstride, int vorder, long points, long function_pointer);

	public static void glMap2d(int target, double u1, double u2, int ustride, int uorder, double v1, double v2, int vstride, int vorder, DoubleBuffer points) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMap2d;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(points);
		nglMap2d(target, u1, u2, ustride, uorder, v1, v2, vstride, vorder, MemoryUtil.getAddress(points), function_pointer);
	}
	static native void nglMap2d(int target, double u1, double u2, int ustride, int uorder, double v1, double v2, int vstride, int vorder, long points, long function_pointer);

	public static void glMap1f(int target, float u1, float u2, int stride, int order, FloatBuffer points) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMap1f;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(points);
		nglMap1f(target, u1, u2, stride, order, MemoryUtil.getAddress(points), function_pointer);
	}
	static native void nglMap1f(int target, float u1, float u2, int stride, int order, long points, long function_pointer);

	public static void glMap1d(int target, double u1, double u2, int stride, int order, DoubleBuffer points) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMap1d;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(points);
		nglMap1d(target, u1, u2, stride, order, MemoryUtil.getAddress(points), function_pointer);
	}
	static native void nglMap1d(int target, double u1, double u2, int stride, int order, long points, long function_pointer);

	public static void glLogicOp(int opcode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLogicOp;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglLogicOp(opcode, function_pointer);
	}
	static native void nglLogicOp(int opcode, long function_pointer);

	public static void glLoadName(int name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLoadName;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglLoadName(name, function_pointer);
	}
	static native void nglLoadName(int name, long function_pointer);

	public static void glLoadMatrix(FloatBuffer m) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLoadMatrixf;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(m, 16);
		nglLoadMatrixf(MemoryUtil.getAddress(m), function_pointer);
	}
	static native void nglLoadMatrixf(long m, long function_pointer);

	public static void glLoadMatrix(DoubleBuffer m) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLoadMatrixd;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(m, 16);
		nglLoadMatrixd(MemoryUtil.getAddress(m), function_pointer);
	}
	static native void nglLoadMatrixd(long m, long function_pointer);

	public static void glLoadIdentity() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLoadIdentity;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglLoadIdentity(function_pointer);
	}
	static native void nglLoadIdentity(long function_pointer);

	public static void glListBase(int base) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glListBase;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglListBase(base, function_pointer);
	}
	static native void nglListBase(int base, long function_pointer);

	public static void glLineWidth(float width) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLineWidth;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglLineWidth(width, function_pointer);
	}
	static native void nglLineWidth(float width, long function_pointer);

	public static void glLineStipple(int factor, short pattern) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLineStipple;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglLineStipple(factor, pattern, function_pointer);
	}
	static native void nglLineStipple(int factor, short pattern, long function_pointer);

	public static void glLightModelf(int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLightModelf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglLightModelf(pname, param, function_pointer);
	}
	static native void nglLightModelf(int pname, float param, long function_pointer);

	public static void glLightModeli(int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLightModeli;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglLightModeli(pname, param, function_pointer);
	}
	static native void nglLightModeli(int pname, int param, long function_pointer);

	public static void glLightModel(int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLightModelfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglLightModelfv(pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglLightModelfv(int pname, long params, long function_pointer);

	public static void glLightModel(int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLightModeliv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglLightModeliv(pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglLightModeliv(int pname, long params, long function_pointer);

	public static void glLightf(int light, int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLightf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglLightf(light, pname, param, function_pointer);
	}
	static native void nglLightf(int light, int pname, float param, long function_pointer);

	public static void glLighti(int light, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLighti;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglLighti(light, pname, param, function_pointer);
	}
	static native void nglLighti(int light, int pname, int param, long function_pointer);

	public static void glLight(int light, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLightfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglLightfv(light, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglLightfv(int light, int pname, long params, long function_pointer);

	public static void glLight(int light, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLightiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglLightiv(light, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglLightiv(int light, int pname, long params, long function_pointer);

	public static boolean glIsTexture(int texture) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsTexture;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsTexture(texture, function_pointer);
		return __result;
	}
	static native boolean nglIsTexture(int texture, long function_pointer);

	public static void glMatrixMode(int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMatrixMode;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMatrixMode(mode, function_pointer);
	}
	static native void nglMatrixMode(int mode, long function_pointer);

	public static void glPolygonStipple(ByteBuffer mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPolygonStipple;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(mask, 128);
		nglPolygonStipple(MemoryUtil.getAddress(mask), function_pointer);
	}
	static native void nglPolygonStipple(long mask, long function_pointer);
	public static void glPolygonStipple(long mask_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPolygonStipple;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglPolygonStippleBO(mask_buffer_offset, function_pointer);
	}
	static native void nglPolygonStippleBO(long mask_buffer_offset, long function_pointer);

	public static void glPolygonOffset(float factor, float units) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPolygonOffset;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPolygonOffset(factor, units, function_pointer);
	}
	static native void nglPolygonOffset(float factor, float units, long function_pointer);

	public static void glPolygonMode(int face, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPolygonMode;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPolygonMode(face, mode, function_pointer);
	}
	static native void nglPolygonMode(int face, int mode, long function_pointer);

	public static void glPointSize(float size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPointSize;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPointSize(size, function_pointer);
	}
	static native void nglPointSize(float size, long function_pointer);

	public static void glPixelZoom(float xfactor, float yfactor) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelZoom;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPixelZoom(xfactor, yfactor, function_pointer);
	}
	static native void nglPixelZoom(float xfactor, float yfactor, long function_pointer);

	public static void glPixelTransferf(int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelTransferf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPixelTransferf(pname, param, function_pointer);
	}
	static native void nglPixelTransferf(int pname, float param, long function_pointer);

	public static void glPixelTransferi(int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelTransferi;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPixelTransferi(pname, param, function_pointer);
	}
	static native void nglPixelTransferi(int pname, int param, long function_pointer);

	public static void glPixelStoref(int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelStoref;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPixelStoref(pname, param, function_pointer);
	}
	static native void nglPixelStoref(int pname, float param, long function_pointer);

	public static void glPixelStorei(int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelStorei;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPixelStorei(pname, param, function_pointer);
	}
	static native void nglPixelStorei(int pname, int param, long function_pointer);

	public static void glPixelMap(int map, FloatBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelMapfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(values);
		nglPixelMapfv(map, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglPixelMapfv(int map, int values_mapsize, long values, long function_pointer);
	public static void glPixelMapfv(int map, int values_mapsize, long values_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelMapfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglPixelMapfvBO(map, values_mapsize, values_buffer_offset, function_pointer);
	}
	static native void nglPixelMapfvBO(int map, int values_mapsize, long values_buffer_offset, long function_pointer);

	public static void glPixelMapu(int map, IntBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelMapuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(values);
		nglPixelMapuiv(map, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglPixelMapuiv(int map, int values_mapsize, long values, long function_pointer);
	public static void glPixelMapuiv(int map, int values_mapsize, long values_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelMapuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglPixelMapuivBO(map, values_mapsize, values_buffer_offset, function_pointer);
	}
	static native void nglPixelMapuivBO(int map, int values_mapsize, long values_buffer_offset, long function_pointer);

	public static void glPixelMapu(int map, ShortBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelMapusv;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(values);
		nglPixelMapusv(map, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglPixelMapusv(int map, int values_mapsize, long values, long function_pointer);
	public static void glPixelMapusv(int map, int values_mapsize, long values_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelMapusv;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglPixelMapusvBO(map, values_mapsize, values_buffer_offset, function_pointer);
	}
	static native void nglPixelMapusvBO(int map, int values_mapsize, long values_buffer_offset, long function_pointer);

	public static void glPassThrough(float token) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPassThrough;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPassThrough(token, function_pointer);
	}
	static native void nglPassThrough(float token, long function_pointer);

	public static void glOrtho(double left, double right, double bottom, double top, double zNear, double zFar) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glOrtho;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglOrtho(left, right, bottom, top, zNear, zFar, function_pointer);
	}
	static native void nglOrtho(double left, double right, double bottom, double top, double zNear, double zFar, long function_pointer);

	public static void glNormalPointer(int stride, ByteBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormalPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glNormalPointer_pointer = pointer;
		nglNormalPointer(GL11.GL_BYTE, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glNormalPointer(int stride, DoubleBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormalPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glNormalPointer_pointer = pointer;
		nglNormalPointer(GL11.GL_DOUBLE, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glNormalPointer(int stride, FloatBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormalPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glNormalPointer_pointer = pointer;
		nglNormalPointer(GL11.GL_FLOAT, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glNormalPointer(int stride, IntBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormalPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glNormalPointer_pointer = pointer;
		nglNormalPointer(GL11.GL_INT, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	static native void nglNormalPointer(int type, int stride, long pointer, long function_pointer);
	public static void glNormalPointer(int type, int stride, long pointer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormalPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglNormalPointerBO(type, stride, pointer_buffer_offset, function_pointer);
	}
	static native void nglNormalPointerBO(int type, int stride, long pointer_buffer_offset, long function_pointer);

	/** Overloads glNormalPointer. */
	public static void glNormalPointer(int type, int stride, ByteBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormalPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glNormalPointer_pointer = pointer;
		nglNormalPointer(type, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}

	public static void glNormal3b(byte nx, byte ny, byte nz) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormal3b;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNormal3b(nx, ny, nz, function_pointer);
	}
	static native void nglNormal3b(byte nx, byte ny, byte nz, long function_pointer);

	public static void glNormal3f(float nx, float ny, float nz) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormal3f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNormal3f(nx, ny, nz, function_pointer);
	}
	static native void nglNormal3f(float nx, float ny, float nz, long function_pointer);

	public static void glNormal3d(double nx, double ny, double nz) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormal3d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNormal3d(nx, ny, nz, function_pointer);
	}
	static native void nglNormal3d(double nx, double ny, double nz, long function_pointer);

	public static void glNormal3i(int nx, int ny, int nz) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormal3i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNormal3i(nx, ny, nz, function_pointer);
	}
	static native void nglNormal3i(int nx, int ny, int nz, long function_pointer);

	public static void glNewList(int list, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNewList;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNewList(list, mode, function_pointer);
	}
	static native void nglNewList(int list, int mode, long function_pointer);

	public static void glEndList() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEndList;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndList(function_pointer);
	}
	static native void nglEndList(long function_pointer);

	public static void glMultMatrix(FloatBuffer m) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultMatrixf;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(m, 16);
		nglMultMatrixf(MemoryUtil.getAddress(m), function_pointer);
	}
	static native void nglMultMatrixf(long m, long function_pointer);

	public static void glMultMatrix(DoubleBuffer m) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultMatrixd;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(m, 16);
		nglMultMatrixd(MemoryUtil.getAddress(m), function_pointer);
	}
	static native void nglMultMatrixd(long m, long function_pointer);

	public static void glShadeModel(int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glShadeModel;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglShadeModel(mode, function_pointer);
	}
	static native void nglShadeModel(int mode, long function_pointer);

	public static void glSelectBuffer(IntBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSelectBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(buffer);
		nglSelectBuffer(buffer.remaining(), MemoryUtil.getAddress(buffer), function_pointer);
	}
	static native void nglSelectBuffer(int buffer_size, long buffer, long function_pointer);

	public static void glScissor(int x, int y, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glScissor;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglScissor(x, y, width, height, function_pointer);
	}
	static native void nglScissor(int x, int y, int width, int height, long function_pointer);

	public static void glScalef(float x, float y, float z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glScalef;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglScalef(x, y, z, function_pointer);
	}
	static native void nglScalef(float x, float y, float z, long function_pointer);

	public static void glScaled(double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glScaled;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglScaled(x, y, z, function_pointer);
	}
	static native void nglScaled(double x, double y, double z, long function_pointer);

	public static void glRotatef(float angle, float x, float y, float z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRotatef;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRotatef(angle, x, y, z, function_pointer);
	}
	static native void nglRotatef(float angle, float x, float y, float z, long function_pointer);

	public static void glRotated(double angle, double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRotated;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRotated(angle, x, y, z, function_pointer);
	}
	static native void nglRotated(double angle, double x, double y, double z, long function_pointer);

	public static int glRenderMode(int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRenderMode;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglRenderMode(mode, function_pointer);
		return __result;
	}
	static native int nglRenderMode(int mode, long function_pointer);

	public static void glRectf(float x1, float y1, float x2, float y2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRectf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRectf(x1, y1, x2, y2, function_pointer);
	}
	static native void nglRectf(float x1, float y1, float x2, float y2, long function_pointer);

	public static void glRectd(double x1, double y1, double x2, double y2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRectd;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRectd(x1, y1, x2, y2, function_pointer);
	}
	static native void nglRectd(double x1, double y1, double x2, double y2, long function_pointer);

	public static void glRecti(int x1, int y1, int x2, int y2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRecti;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRecti(x1, y1, x2, y2, function_pointer);
	}
	static native void nglRecti(int x1, int y1, int x2, int y2, long function_pointer);

	public static void glReadPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglReadPixels(x, y, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glReadPixels(int x, int y, int width, int height, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglReadPixels(x, y, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glReadPixels(int x, int y, int width, int height, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglReadPixels(x, y, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glReadPixels(int x, int y, int width, int height, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglReadPixels(x, y, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glReadPixels(int x, int y, int width, int height, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglReadPixels(x, y, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	static native void nglReadPixels(int x, int y, int width, int height, int format, int type, long pixels, long function_pointer);
	public static void glReadPixels(int x, int y, int width, int height, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglReadPixelsBO(x, y, width, height, format, type, pixels_buffer_offset, function_pointer);
	}
	static native void nglReadPixelsBO(int x, int y, int width, int height, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glReadBuffer(int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglReadBuffer(mode, function_pointer);
	}
	static native void nglReadBuffer(int mode, long function_pointer);

	public static void glRasterPos2f(float x, float y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRasterPos2f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRasterPos2f(x, y, function_pointer);
	}
	static native void nglRasterPos2f(float x, float y, long function_pointer);

	public static void glRasterPos2d(double x, double y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRasterPos2d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRasterPos2d(x, y, function_pointer);
	}
	static native void nglRasterPos2d(double x, double y, long function_pointer);

	public static void glRasterPos2i(int x, int y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRasterPos2i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRasterPos2i(x, y, function_pointer);
	}
	static native void nglRasterPos2i(int x, int y, long function_pointer);

	public static void glRasterPos3f(float x, float y, float z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRasterPos3f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRasterPos3f(x, y, z, function_pointer);
	}
	static native void nglRasterPos3f(float x, float y, float z, long function_pointer);

	public static void glRasterPos3d(double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRasterPos3d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRasterPos3d(x, y, z, function_pointer);
	}
	static native void nglRasterPos3d(double x, double y, double z, long function_pointer);

	public static void glRasterPos3i(int x, int y, int z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRasterPos3i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRasterPos3i(x, y, z, function_pointer);
	}
	static native void nglRasterPos3i(int x, int y, int z, long function_pointer);

	public static void glRasterPos4f(float x, float y, float z, float w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRasterPos4f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRasterPos4f(x, y, z, w, function_pointer);
	}
	static native void nglRasterPos4f(float x, float y, float z, float w, long function_pointer);

	public static void glRasterPos4d(double x, double y, double z, double w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRasterPos4d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRasterPos4d(x, y, z, w, function_pointer);
	}
	static native void nglRasterPos4d(double x, double y, double z, double w, long function_pointer);

	public static void glRasterPos4i(int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRasterPos4i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRasterPos4i(x, y, z, w, function_pointer);
	}
	static native void nglRasterPos4i(int x, int y, int z, int w, long function_pointer);

	public static void glPushName(int name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPushName;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPushName(name, function_pointer);
	}
	static native void nglPushName(int name, long function_pointer);

	public static void glPopName() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPopName;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPopName(function_pointer);
	}
	static native void nglPopName(long function_pointer);

	public static void glPushMatrix() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPushMatrix;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPushMatrix(function_pointer);
	}
	static native void nglPushMatrix(long function_pointer);

	public static void glPopMatrix() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPopMatrix;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPopMatrix(function_pointer);
	}
	static native void nglPopMatrix(long function_pointer);

	public static void glPushClientAttrib(int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPushClientAttrib;
		BufferChecks.checkFunctionAddress(function_pointer);
		StateTracker.pushAttrib(caps, mask);
		nglPushClientAttrib(mask, function_pointer);
	}
	static native void nglPushClientAttrib(int mask, long function_pointer);

	public static void glPopClientAttrib() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPopClientAttrib;
		BufferChecks.checkFunctionAddress(function_pointer);
		StateTracker.popAttrib(caps);
		nglPopClientAttrib(function_pointer);
	}
	static native void nglPopClientAttrib(long function_pointer);

	public static void glPushAttrib(int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPushAttrib;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPushAttrib(mask, function_pointer);
	}
	static native void nglPushAttrib(int mask, long function_pointer);

	public static void glPopAttrib() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPopAttrib;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPopAttrib(function_pointer);
	}
	static native void nglPopAttrib(long function_pointer);

	public static void glStencilFunc(int func, int ref, int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glStencilFunc;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglStencilFunc(func, ref, mask, function_pointer);
	}
	static native void nglStencilFunc(int func, int ref, int mask, long function_pointer);

	public static void glVertexPointer(int size, int stride, DoubleBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glVertexPointer_pointer = pointer;
		nglVertexPointer(size, GL11.GL_DOUBLE, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glVertexPointer(int size, int stride, FloatBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glVertexPointer_pointer = pointer;
		nglVertexPointer(size, GL11.GL_FLOAT, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glVertexPointer(int size, int stride, IntBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glVertexPointer_pointer = pointer;
		nglVertexPointer(size, GL11.GL_INT, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glVertexPointer(int size, int stride, ShortBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glVertexPointer_pointer = pointer;
		nglVertexPointer(size, GL11.GL_SHORT, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	static native void nglVertexPointer(int size, int type, int stride, long pointer, long function_pointer);
	public static void glVertexPointer(int size, int type, int stride, long pointer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglVertexPointerBO(size, type, stride, pointer_buffer_offset, function_pointer);
	}
	static native void nglVertexPointerBO(int size, int type, int stride, long pointer_buffer_offset, long function_pointer);

	/** Overloads glVertexPointer. */
	public static void glVertexPointer(int size, int type, int stride, ByteBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL11_glVertexPointer_pointer = pointer;
		nglVertexPointer(size, type, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}

	public static void glVertex2f(float x, float y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertex2f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertex2f(x, y, function_pointer);
	}
	static native void nglVertex2f(float x, float y, long function_pointer);

	public static void glVertex2d(double x, double y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertex2d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertex2d(x, y, function_pointer);
	}
	static native void nglVertex2d(double x, double y, long function_pointer);

	public static void glVertex2i(int x, int y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertex2i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertex2i(x, y, function_pointer);
	}
	static native void nglVertex2i(int x, int y, long function_pointer);

	public static void glVertex3f(float x, float y, float z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertex3f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertex3f(x, y, z, function_pointer);
	}
	static native void nglVertex3f(float x, float y, float z, long function_pointer);

	public static void glVertex3d(double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertex3d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertex3d(x, y, z, function_pointer);
	}
	static native void nglVertex3d(double x, double y, double z, long function_pointer);

	public static void glVertex3i(int x, int y, int z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertex3i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertex3i(x, y, z, function_pointer);
	}
	static native void nglVertex3i(int x, int y, int z, long function_pointer);

	public static void glVertex4f(float x, float y, float z, float w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertex4f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertex4f(x, y, z, w, function_pointer);
	}
	static native void nglVertex4f(float x, float y, float z, float w, long function_pointer);

	public static void glVertex4d(double x, double y, double z, double w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertex4d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertex4d(x, y, z, w, function_pointer);
	}
	static native void nglVertex4d(double x, double y, double z, double w, long function_pointer);

	public static void glVertex4i(int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertex4i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertex4i(x, y, z, w, function_pointer);
	}
	static native void nglVertex4i(int x, int y, int z, int w, long function_pointer);

	public static void glTranslatef(float x, float y, float z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTranslatef;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTranslatef(x, y, z, function_pointer);
	}
	static native void nglTranslatef(float x, float y, float z, long function_pointer);

	public static void glTranslated(double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTranslated;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTranslated(x, y, z, function_pointer);
	}
	static native void nglTranslated(double x, double y, double z, long function_pointer);

	public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglTexImage1D(target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglTexImage1D(target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglTexImage1D(target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglTexImage1D(target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglTexImage1D(target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	static native void nglTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, long pixels, long function_pointer);
	public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTexImage1DBO(target, level, internalformat, width, border, format, type, pixels_buffer_offset, function_pointer);
	}
	static native void nglTexImage1DBO(int target, int level, int internalformat, int width, int border, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglTexImage2D(target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglTexImage2D(target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglTexImage2D(target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglTexImage2D(target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglTexImage2D(target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
	}
	static native void nglTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, long pixels, long function_pointer);
	public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTexImage2DBO(target, level, internalformat, width, height, border, format, type, pixels_buffer_offset, function_pointer);
	}
	static native void nglTexImage2DBO(int target, int level, int internalformat, int width, int height, int border, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTexSubImage1D(target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTexSubImage1D(target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTexSubImage1D(target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTexSubImage1D(target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTexSubImage1D(target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	static native void nglTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, long pixels, long function_pointer);
	public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTexSubImage1DBO(target, level, xoffset, width, format, type, pixels_buffer_offset, function_pointer);
	}
	static native void nglTexSubImage1DBO(int target, int level, int xoffset, int width, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	static native void nglTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels, long function_pointer);
	public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTexSubImage2DBO(target, level, xoffset, yoffset, width, height, format, type, pixels_buffer_offset, function_pointer);
	}
	static native void nglTexSubImage2DBO(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glTexParameterf(int target, int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexParameterf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexParameterf(target, pname, param, function_pointer);
	}
	static native void nglTexParameterf(int target, int pname, float param, long function_pointer);

	public static void glTexParameteri(int target, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexParameteri;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexParameteri(target, pname, param, function_pointer);
	}
	static native void nglTexParameteri(int target, int pname, int param, long function_pointer);

	public static void glTexParameter(int target, int pname, FloatBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(param, 4);
		nglTexParameterfv(target, pname, MemoryUtil.getAddress(param), function_pointer);
	}
	static native void nglTexParameterfv(int target, int pname, long param, long function_pointer);

	public static void glTexParameter(int target, int pname, IntBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(param, 4);
		nglTexParameteriv(target, pname, MemoryUtil.getAddress(param), function_pointer);
	}
	static native void nglTexParameteriv(int target, int pname, long param, long function_pointer);

	public static void glTexGenf(int coord, int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexGenf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexGenf(coord, pname, param, function_pointer);
	}
	static native void nglTexGenf(int coord, int pname, float param, long function_pointer);

	public static void glTexGend(int coord, int pname, double param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexGend;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexGend(coord, pname, param, function_pointer);
	}
	static native void nglTexGend(int coord, int pname, double param, long function_pointer);

	public static void glTexGen(int coord, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexGenfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglTexGenfv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglTexGenfv(int coord, int pname, long params, long function_pointer);

	public static void glTexGen(int coord, int pname, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexGendv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglTexGendv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglTexGendv(int coord, int pname, long params, long function_pointer);

	public static void glTexGeni(int coord, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexGeni;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexGeni(coord, pname, param, function_pointer);
	}
	static native void nglTexGeni(int coord, int pname, int param, long function_pointer);

	public static void glTexGen(int coord, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexGeniv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglTexGeniv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglTexGeniv(int coord, int pname, long params, long function_pointer);

	public static void glTexEnvf(int target, int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexEnvf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexEnvf(target, pname, param, function_pointer);
	}
	static native void nglTexEnvf(int target, int pname, float param, long function_pointer);

	public static void glTexEnvi(int target, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexEnvi;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexEnvi(target, pname, param, function_pointer);
	}
	static native void nglTexEnvi(int target, int pname, int param, long function_pointer);

	public static void glTexEnv(int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexEnvfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglTexEnvfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglTexEnvfv(int target, int pname, long params, long function_pointer);

	public static void glTexEnv(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexEnviv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglTexEnviv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglTexEnviv(int target, int pname, long params, long function_pointer);

	public static void glTexCoordPointer(int size, int stride, DoubleBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glTexCoordPointer_buffer[StateTracker.getReferences(caps).glClientActiveTexture] = pointer;
		nglTexCoordPointer(size, GL11.GL_DOUBLE, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glTexCoordPointer(int size, int stride, FloatBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glTexCoordPointer_buffer[StateTracker.getReferences(caps).glClientActiveTexture] = pointer;
		nglTexCoordPointer(size, GL11.GL_FLOAT, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glTexCoordPointer(int size, int stride, IntBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glTexCoordPointer_buffer[StateTracker.getReferences(caps).glClientActiveTexture] = pointer;
		nglTexCoordPointer(size, GL11.GL_INT, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	public static void glTexCoordPointer(int size, int stride, ShortBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glTexCoordPointer_buffer[StateTracker.getReferences(caps).glClientActiveTexture] = pointer;
		nglTexCoordPointer(size, GL11.GL_SHORT, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	static native void nglTexCoordPointer(int size, int type, int stride, long pointer, long function_pointer);
	public static void glTexCoordPointer(int size, int type, int stride, long pointer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglTexCoordPointerBO(size, type, stride, pointer_buffer_offset, function_pointer);
	}
	static native void nglTexCoordPointerBO(int size, int type, int stride, long pointer_buffer_offset, long function_pointer);

	/** Overloads glTexCoordPointer. */
	public static void glTexCoordPointer(int size, int type, int stride, ByteBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glTexCoordPointer_buffer[StateTracker.getReferences(caps).glClientActiveTexture] = pointer;
		nglTexCoordPointer(size, type, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}

	public static void glTexCoord1f(float s) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoord1f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexCoord1f(s, function_pointer);
	}
	static native void nglTexCoord1f(float s, long function_pointer);

	public static void glTexCoord1d(double s) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoord1d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexCoord1d(s, function_pointer);
	}
	static native void nglTexCoord1d(double s, long function_pointer);

	public static void glTexCoord2f(float s, float t) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoord2f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexCoord2f(s, t, function_pointer);
	}
	static native void nglTexCoord2f(float s, float t, long function_pointer);

	public static void glTexCoord2d(double s, double t) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoord2d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexCoord2d(s, t, function_pointer);
	}
	static native void nglTexCoord2d(double s, double t, long function_pointer);

	public static void glTexCoord3f(float s, float t, float r) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoord3f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexCoord3f(s, t, r, function_pointer);
	}
	static native void nglTexCoord3f(float s, float t, float r, long function_pointer);

	public static void glTexCoord3d(double s, double t, double r) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoord3d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexCoord3d(s, t, r, function_pointer);
	}
	static native void nglTexCoord3d(double s, double t, double r, long function_pointer);

	public static void glTexCoord4f(float s, float t, float r, float q) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoord4f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexCoord4f(s, t, r, q, function_pointer);
	}
	static native void nglTexCoord4f(float s, float t, float r, float q, long function_pointer);

	public static void glTexCoord4d(double s, double t, double r, double q) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoord4d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexCoord4d(s, t, r, q, function_pointer);
	}
	static native void nglTexCoord4d(double s, double t, double r, double q, long function_pointer);

	public static void glStencilOp(int fail, int zfail, int zpass) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glStencilOp;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglStencilOp(fail, zfail, zpass, function_pointer);
	}
	static native void nglStencilOp(int fail, int zfail, int zpass, long function_pointer);

	public static void glStencilMask(int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glStencilMask;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglStencilMask(mask, function_pointer);
	}
	static native void nglStencilMask(int mask, long function_pointer);

	public static void glViewport(int x, int y, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glViewport;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglViewport(x, y, width, height, function_pointer);
	}
	static native void nglViewport(int x, int y, int width, int height, long function_pointer);
}
