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
package org.lwjgl.opengles;

import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLException;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

import java.nio.IntBuffer;

/** EGL wrapper class. */
public final class EGL {

	/** EGL aliases */
	public static final int
		EGL_FALSE = 0,
		EGL_TRUE  = 1;

	/** Out-of-band handle values */
	public static final int
		EGL_DEFAULT_DISPLAY = 0,
		EGL_NO_CONTEXT      = 0,
		EGL_NO_DISPLAY      = 0,
		EGL_NO_SURFACE      = 0;

	/** Out-of-band attribute value */
	public static final int EGL_DONT_CARE = -1;

	/** Errors / GetError return values */
	public static final int
		EGL_SUCCESS             = 0x3000,
		EGL_NOT_INITIALIZED     = 0x3001,
		EGL_BAD_ACCESS          = 0x3002,
		EGL_BAD_ALLOC           = 0x3003,
		EGL_BAD_ATTRIBUTE       = 0x3004,
		EGL_BAD_CONFIG          = 0x3005,
		EGL_BAD_CONTEXT         = 0x3006,
		EGL_BAD_CURRENT_SURFACE = 0x3007,
		EGL_BAD_DISPLAY         = 0x3008,
		EGL_BAD_MATCH           = 0x3009,
		EGL_BAD_NATIVE_PIXMAP   = 0x300A,
		EGL_BAD_NATIVE_WINDOW   = 0x300B,
		EGL_BAD_PARAMETER       = 0x300C,
		EGL_BAD_SURFACE         = 0x300D,
		EGL_CONTEXT_LOST        = 0x300E;	// EGL 1.1 - IMG_power_management

	/** Reserved =0x300F;-=0x301F; for additional errors */

	/** Config attributes */
	public static final int
		EGL_BUFFER_SIZE             = 0x3020,
		EGL_ALPHA_SIZE              = 0x3021,
		EGL_BLUE_SIZE               = 0x3022,
		EGL_GREEN_SIZE              = 0x3023,
		EGL_RED_SIZE                = 0x3024,
		EGL_DEPTH_SIZE              = 0x3025,
		EGL_STENCIL_SIZE            = 0x3026,
		EGL_CONFIG_CAVEAT           = 0x3027,
		EGL_CONFIG_ID               = 0x3028,
		EGL_LEVEL                   = 0x3029,
		EGL_MAX_PBUFFER_HEIGHT      = 0x302A,
		EGL_MAX_PBUFFER_PIXELS      = 0x302B,
		EGL_MAX_PBUFFER_WIDTH       = 0x302C,
		EGL_NATIVE_RENDERABLE       = 0x302D,
		EGL_NATIVE_VISUAL_ID        = 0x302E,
		EGL_NATIVE_VISUAL_TYPE      = 0x302F,
		EGL_SAMPLES                 = 0x3031,
		EGL_SAMPLE_BUFFERS          = 0x3032,
		EGL_SURFACE_TYPE            = 0x3033,
		EGL_TRANSPARENT_TYPE        = 0x3034,
		EGL_TRANSPARENT_BLUE_VALUE  = 0x3035,
		EGL_TRANSPARENT_GREEN_VALUE = 0x3036,
		EGL_TRANSPARENT_RED_VALUE   = 0x3037,
		EGL_NONE                    = 0x3038, // Attrib list terminator
		EGL_BIND_TO_TEXTURE_RGB     = 0x3039,
		EGL_BIND_TO_TEXTURE_RGBA    = 0x303A,
		EGL_MIN_SWAP_INTERVAL       = 0x303B,
		EGL_MAX_SWAP_INTERVAL       = 0x303C,
		EGL_LUMINANCE_SIZE          = 0x303D,
		EGL_ALPHA_MASK_SIZE         = 0x303E,
		EGL_COLOR_BUFFER_TYPE       = 0x303F,
		EGL_RENDERABLE_TYPE         = 0x3040,
		EGL_MATCH_NATIVE_PIXMAP     = 0x3041, // Pseudo-attribute (not queryable)
		EGL_CONFORMANT              = 0x3042;

	/** Reserved =0x3041;-=0x304F; for additional config attributes */

	/** Config attribute values */
	public static final int
		EGL_SLOW_CONFIG           = 0x3050, // EGL_CONFIG_CAVEAT value
		EGL_NON_CONFORMANT_CONFIG = 0x3051, // EGL_CONFIG_CAVEAT value
		EGL_TRANSPARENT_RGB       = 0x3052, // EGL_TRANSPARENT_TYPE value
		EGL_RGB_BUFFER            = 0x308E, // EGL_COLOR_BUFFER_TYPE value
		EGL_LUMINANCE_BUFFER      = 0x308F;	// EGL_COLOR_BUFFER_TYPE value

	/** More config attribute values, for EGL_TEXTURE_FORMAT */
	public static final int
		EGL_NO_TEXTURE   = 0x305C,
		EGL_TEXTURE_RGB  = 0x305D,
		EGL_TEXTURE_RGBA = 0x305E,
		EGL_TEXTURE_2D   = 0x305F;

	/** EGL_SURFACE_TYPE mask bits */
	public static final int
		EGL_PBUFFER_BIT                 = 0x0001,
		EGL_PIXMAP_BIT                  = 0x0002,
		EGL_WINDOW_BIT                  = 0x0004,
		EGL_VG_COLORSPACE_LINEAR_BIT    = 0x0020,
		EGL_VG_ALPHA_FORMAT_PRE_BIT     = 0x0040,
		EGL_MULTISAMPLE_RESOLVE_BOX_BIT = 0x0200,
		EGL_SWAP_BEHAVIOR_PRESERVED_BIT = 0x0400;

	/** EGL_RENDERABLE_TYPE mask bits */
	public static final int
		EGL_OPENGL_ES_BIT  = 0x0001,
		EGL_OPENVG_BIT     = 0x0002,
		EGL_OPENGL_ES2_BIT = 0x0004,
		EGL_OPENGL_BIT     = 0x0008;

	/** QueryString targets */
	public static final int
		EGL_VENDOR      = 0x3053,
		EGL_VERSION     = 0x3054,
		EGL_EXTENSIONS  = 0x3055,
		EGL_CLIENT_APIS = 0x308D;

	/** QuerySurface / SurfaceAttrib / CreatePbufferSurface targets */
	public static final int
		EGL_HEIGHT                = 0x3056,
		EGL_WIDTH                 = 0x3057,
		EGL_LARGEST_PBUFFER       = 0x3058,
		EGL_TEXTURE_FORMAT        = 0x3080,
		EGL_TEXTURE_TARGET        = 0x3081,
		EGL_MIPMAP_TEXTURE        = 0x3082,
		EGL_MIPMAP_LEVEL          = 0x3083,
		EGL_RENDER_BUFFER         = 0x3086,
		EGL_VG_COLORSPACE         = 0x3087,
		EGL_VG_ALPHA_FORMAT       = 0x3088,
		EGL_HORIZONTAL_RESOLUTION = 0x3090,
		EGL_VERTICAL_RESOLUTION   = 0x3091,
		EGL_PIXEL_ASPECT_RATIO    = 0x3092,
		EGL_SWAP_BEHAVIOR         = 0x3093,
		EGL_MULTISAMPLE_RESOLVE   = 0x3099;

	/** EGL_RENDER_BUFFER values / BindTexImage / ReleaseTexImage buffer targets */
	public static final int
		EGL_BACK_BUFFER   = 0x3084,
		EGL_SINGLE_BUFFER = 0x3085;

	/** OpenVG color spaces */
	public static final int
		EGL_VG_COLORSPACE_sRGB   = 0x3089, // EGL_VG_COLORSPACE value
		EGL_VG_COLORSPACE_LINEAR = 0x308A;	// EGL_VG_COLORSPACE value

	/** OpenVG alpha formats */
	public static final int
		EGL_VG_ALPHA_FORMAT_NONPRE = 0x308B, // EGL_ALPHA_FORMAT value
		EGL_VG_ALPHA_FORMAT_PRE    = 0x308C;	// EGL_ALPHA_FORMAT

	/**
	 * Constant scale factor by which fractional display resolutions &
	 * aspect ratio are scaled when queried as integer values.
	 */
	public static final int EGL_DISPLAY_SCALING = 10000;

	/** Unknown display resolution/aspect ratio */
	public static final int EGL_UNKNOWN = -1;

	/** Back buffer swap behaviors */
	public static final int
		EGL_BUFFER_PRESERVED = 0x3094, // EGL_SWAP_BEHAVIOR value
		EGL_BUFFER_DESTROYED = 0x3095;	// EGL_SWAP_BEHAVIOR value

	/** CreatePbufferFromClientBuffer buffer types */
	static final int EGL_OPENVG_IMAGE = 0x3096;

	/** QueryContext targets */
	public static final int EGL_CONTEXT_CLIENT_TYPE = 0x3097;

	/** CreateContext attributes */
	public static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;

	/** Multisample resolution behaviors */
	public static final int
		EGL_MULTISAMPLE_RESOLVE_DEFAULT = 0x309A, // EGL_MULTISAMPLE_RESOLVE value
		EGL_MULTISAMPLE_RESOLVE_BOX     = 0x309B;	// EGL_MULTISAMPLE_RESOLVE value

	/** BindAPI/QueryAPI targets */
	public static final int
		EGL_OPENGL_ES_API = 0x30A0,
		EGL_OPENVG_API    = 0x30A1,
		EGL_OPENGL_API    = 0x30A2;

	/** GetCurrentSurface targets */
	public static final int
		EGL_DRAW = 0x3059,
		EGL_READ = 0x305A;

	/** WaitNative engines */
	static final int EGL_CORE_NATIVE_ENGINE = 0x305B;

	private EGL() {
	}

	public static native int eglGetError();

	/**
	 * Obtains an EGL display from the specified native display and initializes it.
	 *
	 * @param display_id the handle to the native display.
	 *
	 * @return the EGL Display
	 *
	 * @throws LWJGLException if no display is available or an EGL error occurs
	 */
	public static EGLDisplay eglGetDisplay(long display_id) throws LWJGLException {
		//LWJGLUtil.log("eglGetDisplay");
		final long pointer = neglGetDisplay(display_id);

		if ( pointer == EGL_NO_DISPLAY ) // No error is generated when this happens
			throw new LWJGLException("Failed to get EGL display from native display handle: " + display_id);

		return new EGLDisplay(pointer);
	}

	private static native long neglGetDisplay(long display_id);

	/**
	 * Initializes the specified EGL display.
	 *
	 * @param dpy     the EGL display to initialize
	 * @param version the EGL major and minor version will be returned in this buffer.
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	static void eglInitialize(EGLDisplay dpy, IntBuffer version) throws LWJGLException {
		//LWJGLUtil.log("eglInitialize");
		BufferChecks.checkBuffer(version, 2);
		if ( !neglInitialize(dpy.getPointer(), MemoryUtil.getAddress(version)) )
			throwEGLError("Failed to initialize EGL display.");
	}

	private static native boolean neglInitialize(long dpy_ptr, long version);

	/**
	 * Release the resources associated with the specified EGL display.
	 *
	 * @param dpy the EGL display to terminate
	 */
	static void eglTerminate(EGLDisplay dpy) throws LWJGLException {
		//LWJGLUtil.log("eglTerminate");
		if ( !neglTerminate(dpy.getPointer()) )
			throwEGLError("Failed to terminate EGL display.");
	}

	private static native boolean neglTerminate(long dpy_ptr);

	/**
	 * Returns a string describing some aspect of the EGL implementation running on the specified display.
	 *
	 * @param dpy  the EGL display to query
	 * @param name the value to query
	 *
	 * @return the description
	 */
	public static String eglQueryString(EGLDisplay dpy, int name) {
		//LWJGLUtil.log("eglQueryString");
		return neglQueryString(dpy.getPointer(), name);
	}

	private static native String neglQueryString(long dpy, int name);

	/**
	 * Returns the number of EGLConfigs that are available on the specified display.
	 *
	 * @param dpy the EGLDisplay
	 *
	 * @return the number of EGLConfigs available
	 *
	 * @throws LWJGLException if an EGL error occurs
	 * @see #eglGetConfigs(EGLDisplay, EGLConfig[], IntBuffer)
	 */
	static int eglGetConfigsNum(EGLDisplay dpy) throws LWJGLException {
		//LWJGLUtil.log("eglGetConfigsNum");
		IntBuffer num_config = APIUtil.getBufferInt();

		if ( !neglGetConfigs(dpy.getPointer(), 0L, 0, MemoryUtil.getAddress0(num_config)) )
			throwEGLError("Failed to get EGL configs.");

		return num_config.get(0);
	}

	/**
	 * Returns the available EGLConfigs on the speficied display. The number of available EGLConfigs
	 * is returned in the num_config parameter. The configs array may be null. If it is null, a new
	 * array will be allocated, with size equal to the result of {@link #eglGetConfigsNum(EGLDisplay)}  eglGetConfigsNum}.
	 * If it is not null, no more than {@code configs.length} EGLConfigs will be returned. If the array is bigger
	 * than the number of available EGLConfigs, the remaining array elements will not be affected.
	 *
	 * @param dpy        the EGLDisplay
	 * @param configs    the EGLConfigs array
	 * @param num_config the number of available EGLConfigs returned
	 *
	 * @return the available EGLConfigs
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	static EGLConfig[] eglGetConfigs(EGLDisplay dpy, EGLConfig[] configs, IntBuffer num_config) throws LWJGLException {
		//LWJGLUtil.log("eglGetConfigs");
		BufferChecks.checkBuffer(num_config, 1);

		if ( configs == null ) {
			if ( !neglGetConfigs(dpy.getPointer(), 0L, 0, MemoryUtil.getAddress(num_config)) )
				throwEGLError("Failed to get number of available EGL configs.");

			configs = new EGLConfig[num_config.get(num_config.position())];
		}

		final PointerBuffer configs_buffer = APIUtil.getBufferPointer(configs.length);
		if ( !neglGetConfigs(dpy.getPointer(), MemoryUtil.getAddress0(configs_buffer), configs.length, MemoryUtil.getAddress(num_config)) )
			throwEGLError("Failed to get EGL configs.");

		final int config_size = num_config.get(num_config.position());
		for ( int i = 0; i < config_size; i++ )
			configs[i] = new EGLConfig(dpy, configs_buffer.get(i));

		return configs;
	}

	private static native boolean neglGetConfigs(long dpy_ptr, long configs, int config_size, long num_config);

	/**
	 * Returns the number of EGLConfigs that are available on the specified display and
	 * match the speficied list of attributes.
	 *
	 * @param dpy         the EGLDisplay
	 * @param attrib_list the attribute list (may be null)
	 *
	 * @return the number of EGLConfigs available that satisft the attribute list
	 *
	 * @throws LWJGLException if an EGL error occurs
	 * @see #eglChooseConfig(EGLDisplay, IntBuffer, EGLConfig[], IntBuffer)
	 */
	static int eglChooseConfigNum(EGLDisplay dpy, IntBuffer attrib_list) throws LWJGLException {
		//LWJGLUtil.log("eglChooseConfigNum");
		checkAttribList(attrib_list);
		IntBuffer num_config = APIUtil.getBufferInt();

		if ( !neglChooseConfig(dpy.getPointer(), MemoryUtil.getAddressSafe(attrib_list), 0L, 0, MemoryUtil.getAddress0(num_config)) )
			throwEGLError("Failed to get EGL configs.");

		return num_config.get(0);
	}

	/**
	 * Returns the available EGLConfigs on the speficied display that satisfy the specified list of attributes.
	 * The number of available EGLConfigs is returned in the num_config parameter. The configs array may be null.
	 * If it is null, a new array will be allocated, with size equal to the result of {@link #eglGetConfigsNum(EGLDisplay)}  eglGetConfigsNum}.
	 * If it is not null, no more than {@code configs.length} EGLConfigs will be returned. If the array is bigger
	 * than the number of available EGLConfigs, the remaining array elements will not be affected.
	 *
	 * @param dpy         the EGLDisplay
	 * @param attrib_list the attribute list (may be null)
	 * @param configs     the EGLConfigs array
	 * @param num_config  the number of available EGLConfigs returned
	 *
	 * @return the available EGLConfigs that satisfy the attribute list
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	static EGLConfig[] eglChooseConfig(EGLDisplay dpy, IntBuffer attrib_list, EGLConfig[] configs, IntBuffer num_config) throws LWJGLException {
		//LWJGLUtil.log("eglChooseConfig");
		checkAttribList(attrib_list);
		BufferChecks.checkBuffer(num_config, 1);

		int config_size;
		if ( configs == null ) {
			if ( !neglChooseConfig(dpy.getPointer(), MemoryUtil.getAddressSafe(attrib_list), 0L, 0, MemoryUtil.getAddress(num_config)) )
				throwEGLError("Failed to get number of available EGL configs.");

			config_size = num_config.get(num_config.position());
		} else
			config_size = configs.length;

		//LWJGLUtil.log("config_size = " + config_size);
		PointerBuffer configs_buffer = APIUtil.getBufferPointer(config_size);
		if ( !neglChooseConfig(dpy.getPointer(), MemoryUtil.getAddressSafe(attrib_list), MemoryUtil.getAddress0(configs_buffer), config_size, MemoryUtil.getAddress(num_config)) )
			throwEGLError("Failed to choose EGL config.");

		// Get the true number of configurations (the first neglChooseConfig call may return more than the second)
		config_size = num_config.get(num_config.position());
		if ( configs == null )
			configs = new EGLConfig[config_size];
		for ( int i = 0; i < config_size; i++ )
			configs[i] = new EGLConfig(dpy, configs_buffer.get(i));

		return configs;
	}

	private static native boolean neglChooseConfig(long dpy_ptr, long attrib_list, long configs, int config_size, long num_config);

	/**
	 * Returns the value of an EGL config attribute.
	 *
	 * @param dpy       the EGL display
	 * @param config    the EGL config
	 * @param attribute the attribute
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	static int eglGetConfigAttrib(EGLDisplay dpy, EGLConfig config, int attribute) throws LWJGLException {
		//LWJGLUtil.log("eglGetConfigAttrib");
		final IntBuffer value = APIUtil.getBufferInt();

		if ( !neglGetConfigAttrib(dpy.getPointer(), config.getPointer(), attribute, MemoryUtil.getAddress(value)) )
			throwEGLError("Failed to get EGL config attribute.");

		return value.get(0);
	}

	private static native boolean neglGetConfigAttrib(long dpy_ptr, long config_ptr, int attribute, long value);

	/**
	 * Creates an on-screen rendering surface on the specified EGL display.
	 *
	 * @param dpy         the EGL display
	 * @param config      the EGL config
	 * @param win         the native window handle
	 * @param attrib_list an attribute list (may be null)
	 *
	 * @return the created EGL surface
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	static EGLSurface eglCreateWindowSurface(EGLDisplay dpy, EGLConfig config, long win, IntBuffer attrib_list) throws LWJGLException {
		//LWJGLUtil.log("eglCreateWindowSurface");
		checkAttribList(attrib_list);
		final long pointer = neglCreateWindowSurface(dpy.getPointer(), config.getPointer(), win, MemoryUtil.getAddressSafe(attrib_list));

		if ( pointer == EGL_NO_SURFACE )
			throwEGLError("Failed to create EGL window surface.");

		return new EGLSurface(dpy, config, pointer);
	}

	private static native long neglCreateWindowSurface(long dpy_ptr, long config_ptr, long win, long attrib_list);

	/**
	 * Creates an off-screen rendering surface on the specified EGL display.
	 *
	 * @param dpy         the EGL display
	 * @param config      the EGL config
	 * @param attrib_list an attribute list (may be null)
	 *
	 * @return the created EGL surface
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	static EGLSurface eglCreatePbufferSurface(EGLDisplay dpy, EGLConfig config, IntBuffer attrib_list) throws LWJGLException {
		//LWJGLUtil.log("eglCreatePbufferSurface");
		checkAttribList(attrib_list);
		final long pointer = neglCreatePbufferSurface(dpy.getPointer(), config.getPointer(), MemoryUtil.getAddressSafe(attrib_list));

		if ( pointer == EGL_NO_SURFACE )
			throwEGLError("Failed to create EGL pbuffer surface.");

		return new EGLSurface(dpy, config, pointer);
	}

	private static native long neglCreatePbufferSurface(long dpy_ptr, long config_ptr, long attrib_list);

	/*
	EGLAPI EGLSurface EGLAPIENTRY eglCreatePixmapSurface(EGLDisplay dpy, EGLConfig config, EGLNativePixmapType pixmap, const EGLint *attrib_list);
	*/

	/**
	 * Sets the specified EGL surface attribute to the specified value.
	 *
	 * @param dpy       the EGL display
	 * @param surface   the EGL surface
	 * @param attribute the attribute
	 * @param value     the attribute value
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	static void eglSurfaceAttrib(EGLDisplay dpy, EGLSurface surface, int attribute, int value) throws LWJGLException {
		//LWJGLUtil.log("eglSurfaceAttrib");
		if ( !neglSurfaceAttrib(dpy.getPointer(), surface.getPointer(), attribute, value) )
			throwEGLError("Failed to set surface attribute.");
	}

	private static native boolean neglSurfaceAttrib(long dpy_ptr, long surface_ptr, int attribute, int value);

	/**
	 * Destroys the specified EGL surface.
	 *
	 * @param dpy     the EGL display
	 * @param surface the EGL surface to destroy
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	static void eglDestroySurface(EGLDisplay dpy, EGLSurface surface) throws LWJGLException {
		//LWJGLUtil.log("eglDestroySurface");
		if ( !neglDestroySurface(dpy.getPointer(), surface.getPointer()) )
			throwEGLError("Failed to destroy EGL surface.");
	}

	private static native boolean neglDestroySurface(long dpy_ptr, long surface_ptr);

	/**
	 * Returns the value of the specified EGL surface attribute in the value parameter.
	 *
	 * @param dpy       the EGL display
	 * @param surface   the EGL surface
	 * @param attribute the surface attribute
	 * @param value     the attribute value will be returned here
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	public static void eglQuerySurface(EGLDisplay dpy, EGLSurface surface, int attribute, IntBuffer value) throws LWJGLException {
		//LWJGLUtil.log("eglQuerySurface");
		BufferChecks.checkBuffer(value, 1);
		if ( !neglQuerySurface(dpy.getPointer(), surface.getPointer(), attribute, MemoryUtil.getAddress(value)) )
			throwEGLError("Failed to query surface attribute.");
	}

	private static native boolean neglQuerySurface(long dpy_ptr, long surface_ptr, int attribute, long value);

	/**
	 * Binds the specified rendering API to the current thread.
	 *
	 * @param api the API to bind
	 *
	 * @return true if the bind was successful, false if an EGL error occurs
	 */
	public static native boolean eglBindAPI(int api);

	/**
	 * Returns the current rendering API.
	 *
	 * @return the rendering API bound to the current thread
	 */
	public static native int eglQueryAPI();

	/**
	 * Returns EGL to its state at thread initialization. This includes the following:<br>
	 * <p>
	 * For each client API supported by EGL, if there is a currently bound context,
	 * that context is released. This is equivalent to calling eglMakeCurrent
	 * with ctx set to EGL_NO_CONTEXT and both draw and read set to EGL_NO_SURFACE
	 * </p><br>
	 * <p>The current rendering API is reset to its value at thread initialization</p><br>
	 * <p>Any additional implementation-dependent per-thread state maintained by EGL
	 * is marked for deletion as soon as possible.</p>
	 *
	 * @return true if thread state was released successfully, false is an EGL error occurs
	 */
	static native boolean eglReleaseThread();

	/*
	EGLAPI EGLSurface EGLAPIENTRY eglCreatePbufferFromClientBuffer(EGLDisplay dpy, EGLenum buftype, EGLClientBuffer buffer, EGLConfig config, const EGLint *attrib_list);
	EGLAPI EGLBoolean EGLAPIENTRY eglBindTexImage(EGLDisplay dpy, EGLSurface surface, EGLint buffer);
	EGLAPI EGLBoolean EGLAPIENTRY eglReleaseTexImage(EGLDisplay dpy, EGLSurface surface, EGLint buffer);
	*/

	/**
	 * Specifies the minimum number of video frame periods per buffer swap for
	 * the window associated with the current context.
	 *
	 * @param dpy      the EGL display
	 * @param interval the frame interval
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	static void eglSwapInterval(EGLDisplay dpy, int interval) throws LWJGLException {
		//LWJGLUtil.log("eglSwapInterval");
		if ( !neglSwapInterval(dpy.getPointer(), interval) )
			throwEGLError("Failed to set swap interval.");
	}

	private static native boolean neglSwapInterval(long dpy_ptr, int interval);

	/**
	 * Creates a new EGL context for the current rendering API.
	 *
	 * @param dpy           the EGL display
	 * @param config        the EGL config
	 * @param share_context the EGL context to share data with
	 * @param attrib_list   the attribute list (may be null)
	 *
	 * @return the created EGL context
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	static EGLContext eglCreateContext(EGLDisplay dpy, EGLConfig config, EGLContext share_context, IntBuffer attrib_list) throws LWJGLException {
		//LWJGLUtil.log("eglCreateContext");
		checkAttribList(attrib_list);
		final long pointer = neglCreateContext(dpy.getPointer(), config.getPointer(),
		                                       share_context == null ? EGL_NO_CONTEXT : share_context.getPointer(),
		                                       MemoryUtil.getAddressSafe(attrib_list));

		if ( pointer == EGL_NO_CONTEXT )
			throwEGLError("Failed to create EGL context.");

		return new EGLContext(dpy, config, pointer);
	}

	private static native long neglCreateContext(long dpy_ptr, long config_ptr, long share_context_ptr, long attrib_list);

	/**
	 * Destroys a rendering context.
	 *
	 * @param dpy the EGL display
	 * @param ctx the EGL context
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	static void eglDestroyContext(EGLDisplay dpy, EGLContext ctx) throws LWJGLException {
		//LWJGLUtil.log("eglDestroyContext");
		if ( !neglDestroyContext(dpy.getPointer(), ctx.getPointer()) )
			throwEGLError("Failed to destroy context.");
	}

	private static native boolean neglDestroyContext(long dpy_ptr, long ctx_ptr);

	/**
	 * Binds the specified context to the current thread and to the draw and read surfaces.
	 *
	 * @param dpy  the EGL display
	 * @param draw the draw EGL surface
	 * @param read the read EGL surface
	 * @param ctx  the EGL context to make current
	 *
	 * @throws LWJGLException      if an EGL error occurs
	 * @throws PowerManagementEventException if an EGL power management event occurs
	 */
	static void eglMakeCurrent(EGLDisplay dpy, EGLSurface draw, EGLSurface read, EGLContext ctx) throws LWJGLException, PowerManagementEventException {
		//LWJGLUtil.log("eglMakeCurrent");
		if ( !neglMakeCurrent(dpy.getPointer(),
		                      draw == null ? EGL_NO_SURFACE : draw.getPointer(),
		                      read == null ? EGL_NO_SURFACE : read.getPointer(),
		                      ctx == null ? EGL_NO_CONTEXT : ctx.getPointer()) ) {
			final int error = eglGetError();
			if ( error == EGL_CONTEXT_LOST )
				throw new PowerManagementEventException();
			else
				throwEGLError("Failed to change the current context.", error);
		}
	}

	/**
	 * Releases the current context without assigning a new one.
	 *
	 * @param dpy the EGL display
	 *
	 * @throws LWJGLException      if an EGL error occurs
	 * @throws PowerManagementEventException if an EGL power management event occurs
	 * @see #eglMakeCurrent(EGLDisplay, EGLSurface, EGLSurface, EGLContext)
	 */
	public static void eglReleaseCurrent(EGLDisplay dpy) throws LWJGLException, PowerManagementEventException {
		//LWJGLUtil.log("eglReleaseCurrent");
		eglMakeCurrent(dpy, null, null, null);
	}

	private static native boolean neglMakeCurrent(long dpy_ptr, long draw_ptr, long read_ptr, long ctx_ptr);

	/**
	 * Returns the current EGL context for the current rendering API.
	 * If there is no context current, null is returned.
	 *
	 * @return the current context
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	public static EGLContext eglGetCurrentContext() throws LWJGLException {
		//LWJGLUtil.log("eglGetCurrentContext");
		// Get current context
		final long ctx = neglGetCurrentContext();
		if ( ctx == EGL_NO_CONTEXT )
			return null;

		// Get current display
		final EGLDisplay display = eglGetCurrentDisplay();

		// Query context's CONFIG_ID
		final IntBuffer attrib_list = APIUtil.getBufferInt();
		neglQueryContext(display.getPointer(), ctx, EGL_CONFIG_ID, MemoryUtil.getAddress0(attrib_list));

		final EGLConfig config = getEGLConfig(display, attrib_list);

		// Create the context handle
		return new EGLContext(display, config, ctx);
	}

	/**
	 * Returns true if the specified EGL context is the current context.
	 * This method is faster than using {@code #eglGetCurrentContext}
	 * and comparing the two EGLContext objects.
	 *
	 * @param context the EGL context
	 *
	 * @return true if the EGL context is current
	 *
	 * @see #eglGetCurrentContext()
	 */
	public static boolean eglIsCurrentContext(EGLContext context) {
		//LWJGLUtil.log("eglIsCurrentContext");
		return neglGetCurrentContext() == context.getPointer();
	}

	private static native long neglGetCurrentContext();

	/**
	 * Returns the EGL surfaces used for rendering by the current context.
	 * If there is no context current, null is returned.
	 *
	 * @param readdraw the read or draw surface
	 *
	 * @return the current surface
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	public static EGLSurface eglGetCurrentSurface(int readdraw) throws LWJGLException {
		//LWJGLUtil.log("eglGetCurrentSurface");
		final long surface = neglGetCurrentSurface(readdraw);
		if ( surface == EGL_NO_SURFACE )
			return null;

		// Get current display
		EGLDisplay display = eglGetCurrentDisplay();

		// Query context's CONFIG_ID
		final IntBuffer attrib_list = APIUtil.getBufferInt();
		if ( !neglQuerySurface(display.getPointer(), surface, EGL_CONFIG_ID, MemoryUtil.getAddress0(attrib_list)) )
			throwEGLError("Failed to query surface EGL config ID.");

		final EGLConfig config = getEGLConfig(display, attrib_list);

		// Create the surface handle
		return new EGLSurface(display, config, surface);
	}

	private static native long neglGetCurrentSurface(int readdraw);

	/**
	 * Returns the EGL display associated with the current context.
	 *
	 * @return the current display
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	public static EGLDisplay eglGetCurrentDisplay() throws LWJGLException {
		//LWJGLUtil.log("eglGetCurrentDisplay");
		return new EGLDisplay(neglGetCurrentDisplay());
	}

	private static native long neglGetCurrentDisplay();

	/**
	 * Returns the value of the specified EGL context attribute in the value parameter.
	 *
	 * @param dpy       the EGL display
	 * @param ctx       the EGL context
	 * @param attribute the context attribute
	 * @param value     the attribute value will be returned here
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	public static void eglQueryContext(EGLDisplay dpy, EGLContext ctx, int attribute, IntBuffer value) throws LWJGLException {
		//LWJGLUtil.log("eglQueryContext");
		BufferChecks.checkBuffer(value, 1);
		if ( !neglQueryContext(dpy.getPointer(), ctx.getPointer(), attribute, MemoryUtil.getAddress(value)) )
			throwEGLError("Failed to query context attribute.");
	}

	private static native boolean neglQueryContext(long dpy_ptr, long ctx_ptr, int attribute, long value);

	/**
	 * Prevents native rendering API functions from executing until any
	 * outstanding client API rendering affecting the same surface is complete.
	 *
	 * @return true if the wait was successful, false is an EGL error occurs
	 */
	public static native boolean eglWaitClient();

	/**
	 * This method does the equivalent of:<br>
	 * <code>
	 * EGLenum api = eglQueryAPI();
	 * eglBindAPI(EGL_OPENGL_ES_API);
	 * eglWaitClient();
	 * eglBindAPI(api);
	 * </code>
	 *
	 * @return true if the wait was successful, false if an EGL error occurs
	 */
	public static native boolean eglWaitGL();

	/**
	 * Prevents a client API command sequence from executing until any outstanding
	 * native rendering affecting the same surface is complete.
	 *
	 * @param engine the native rendering engine
	 *
	 * @return true if the wait was successful, false if an EGL error occurs
	 */
	public static native boolean eglWaitNative(int engine);

	/**
	 * Posts the color buffer to the window.
	 *
	 * @param dpy     the EGL display
	 * @param surface the EGL back-buffered window surface
	 *
	 * @throws LWJGLException      if an EGL occurs
	 * @throws PowerManagementEventException if an EGL power management event occurs
	 */
	static void eglSwapBuffers(EGLDisplay dpy, EGLSurface surface) throws LWJGLException, PowerManagementEventException {
		//LWJGLUtil.log("eglSwapBuffers");
		if ( !neglSwapBuffers(dpy.getPointer(), surface.getPointer()) ) {
			final int error = eglGetError();
			if ( error == EGL_CONTEXT_LOST )
				throw new PowerManagementEventException();
			else
				throwEGLError("Failed to swap buffers.", error);
		}
	}

	private static native boolean neglSwapBuffers(long dpy_ptr, long surface_ptr);

	//EGLAPI EGLBoolean EGLAPIENTRY eglCopyBuffers(EGLDisplay dpy, EGLSurface surface, EGLNativePixmapType target);

	/* --------------------------------
			HELPER METHODS
	-------------------------------- */

	static void checkAttribList(IntBuffer attrib_list) {
		if ( attrib_list == null )
			return;

		//BufferChecks.checkDirect(attrib_list);
		if ( attrib_list.remaining() % 2 != 1 )
			throw new IllegalArgumentException("Invalid number of values in attribute list.");
		if ( attrib_list.get(attrib_list.limit() - 1) != EGL_NONE )
			throw new IllegalArgumentException("The attribute list is not terminated with EGL_NONE.");
	}

	private static EGLConfig getEGLConfig(final EGLDisplay dpy, final IntBuffer attrib_list) throws LWJGLException {
		final int configID = attrib_list.get(0);

		// -- This fails on the emulator
		// Get EGL config used by the context
		attrib_list.put(0, EGL_CONFIG_ID).put(1, configID).put(2, EGL_NONE);

		final PointerBuffer configs_buffer = APIUtil.getBufferPointer(1);
		if ( !neglChooseConfig(dpy.getPointer(), MemoryUtil.getAddress(attrib_list), MemoryUtil.getAddress0(configs_buffer), 1, MemoryUtil.getAddress(attrib_list, 3)) )
			throwEGLError("Failed to choose EGL config.");

		return new EGLConfig(dpy, configs_buffer.get(0));

		// -- Emulator workaround
		/*
		EGLConfig config = null;

		final EGLConfig[] configs = eglGetConfigs(dpy, null, attrib_list);
		final int config_size = attrib_list.get(0);
		for ( int i = 0; i < config_size; i++ ) {
			if ( configs[i].getConfigID() == configID ) {
				config = configs[i];
				break;
			}
		}

		if ( config == null )
			throwEGLError("Failed to retrieve EGL config for current context.");

		return config;
		//*/
	}

	static void throwEGLError(final String msg) throws LWJGLException {
		throwEGLError(msg, eglGetError());
	}

	static void throwEGLError(String msg, final int error) throws LWJGLException {
		if ( error != EGL_SUCCESS )
			msg += " EGL error: " + Util.translateEGLErrorString(error);

		throw new LWJGLException(msg);
	}

}