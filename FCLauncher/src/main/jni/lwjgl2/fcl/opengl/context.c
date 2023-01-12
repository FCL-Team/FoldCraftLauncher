/*
 * Copyright (c) 2002-2008 LWJGL Project
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

/**
 * $Id$
 *
 * Include file to access public window features
 *
 * @author Tungsten
 * @version $Revision$
 */

#include <jni.h>
#include <fcl_bridge.h>
#include "extgl_egl.h"
#include "context.h"

EGLConfig *getFBConfigFromPeerInfo(JNIEnv *env, FCLPeerInfo *peer_info) {
	int attribs[] = {EGL_CONFIG_ID, peer_info->config_id, EGL_NONE, EGL_NONE};
	int num_elements;
	EGLBoolean ret;
	ret = lwjgl_eglChooseConfig(peer_info->display, attribs, NULL, 0, &num_elements);
	if (!ret) {
		throwException(env, "Could not find EGL 1.4 config from peer info");
		return NULL;
	}
	EGLConfig* configs = calloc(num_elements, sizeof(EGLConfig));
	ret = lwjgl_eglChooseConfig(peer_info->display, attribs, configs, num_elements, &num_elements);
	if (!ret) {
		free(configs);
		throwException(env, "Could not find EGL 1.4 config from peer info");
		return NULL;
	}
	// Check that only one FBConfig matches the config id
	if (num_elements != 1) {
		free(configs);
		throwException(env, "No unique EGL 1.4 config matches peer info");
		return NULL;
	}
	return configs;
}

static int convertToBPE(int bpp) {
	int bpe;
	switch (bpp) {
		case 0:
			bpe = 0;
			break;
		case 32:
		case 24:
			bpe = 8;
			break;
		case 16: /* Fall through */
		default:
			bpe = 4;
			break;
	}
	return bpe;
}

static EGLConfig *chooseVisualEGLFromBPP(JNIEnv *env, EGLDisplay disp, jobject pixel_format, int bpp, int drawable_type) {
	jclass cls_pixel_format = (*env)->GetObjectClass(env, pixel_format);
	int alpha = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "alpha", "I"));
	int depth = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "depth", "I"));
	int stencil = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "stencil", "I"));
	int samples = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "samples", "I"));
	
	int bpe = convertToBPE(bpp);
	attrib_list_t attrib_list;
	initAttribList(&attrib_list);
		
	putAttrib(&attrib_list, EGL_SURFACE_TYPE); putAttrib(&attrib_list, drawable_type);
	putAttrib(&attrib_list, EGL_DEPTH_SIZE); putAttrib(&attrib_list, depth);
	putAttrib(&attrib_list, EGL_RED_SIZE); putAttrib(&attrib_list, bpe);
	putAttrib(&attrib_list, EGL_GREEN_SIZE); putAttrib(&attrib_list, bpe);
	putAttrib(&attrib_list, EGL_BLUE_SIZE); putAttrib(&attrib_list, bpe);
	putAttrib(&attrib_list, EGL_ALPHA_SIZE); putAttrib(&attrib_list, alpha);
	putAttrib(&attrib_list, EGL_STENCIL_SIZE); putAttrib(&attrib_list, stencil);
	// Assume the caller has checked support for multisample
	if (samples > 0) {
		putAttrib(&attrib_list, EGL_SAMPLE_BUFFERS); putAttrib(&attrib_list, 1);
		putAttrib(&attrib_list, EGL_SAMPLES); putAttrib(&attrib_list, samples);
	}
	putAttrib(&attrib_list, EGL_NONE); putAttrib(&attrib_list, EGL_NONE);
	int num_formats = 0;
	EGLBoolean ret;
	ret = lwjgl_eglChooseConfig(disp, attrib_list.attribs, NULL, 0, &num_formats);
	if (!ret) {
		return NULL;
	}
	EGLConfig* configs = calloc(num_formats, sizeof(EGLConfig));
	ret = lwjgl_eglChooseConfig(disp, attrib_list.attribs, configs, num_formats, &num_formats);
	if (ret && num_formats > 0) {
		return configs;
	} else {
		if (configs != NULL)
			free(configs);
		return NULL;
	}
}

EGLConfig *chooseVisualEGL(JNIEnv *env, EGLDisplay disp, jobject pixel_format, bool use_display_bpp, int drawable_type) {
	jclass cls_pixel_format = (*env)->GetObjectClass(env, pixel_format);
	int bpp;
	if (use_display_bpp) {
		bpp = 32;
		EGLConfig *configs = chooseVisualEGLFromBPP(env, disp, pixel_format, bpp, drawable_type);
		if (configs != NULL)
			return configs;
		else
			bpp = 24;
	} else
		bpp = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "bpp", "I"));
	return chooseVisualEGLFromBPP(env, disp, pixel_format, bpp, drawable_type);
}

static void dumpVisualInfo(JNIEnv *env, EGLDisplay display, EGLConfig config) {
	int alpha, depth, stencil, r, g, b;
	int sample_buffers = 0;
	int samples = 0;
	lwjgl_eglGetConfigAttrib(display, config, EGL_RED_SIZE, &r);
	lwjgl_eglGetConfigAttrib(display, config, EGL_GREEN_SIZE, &g);
	lwjgl_eglGetConfigAttrib(display, config, EGL_BLUE_SIZE, &b);
	lwjgl_eglGetConfigAttrib(display, config, EGL_ALPHA_SIZE, &alpha);
	lwjgl_eglGetConfigAttrib(display, config, EGL_DEPTH_SIZE, &depth);
	lwjgl_eglGetConfigAttrib(display, config, EGL_STENCIL_SIZE, &stencil);
	lwjgl_eglGetConfigAttrib(display, config, EGL_SAMPLE_BUFFERS, &sample_buffers);
	lwjgl_eglGetConfigAttrib(display, config, EGL_SAMPLES, &samples);
	printfDebugJava(env, "Pixel format info: r = %d, g = %d, b = %d, a = %d, depth = %d, stencil = %d, sample buffers = %d, samples = %d", r, g, b, alpha, depth, stencil, sample_buffers, samples);
}

bool initPeerInfo(JNIEnv *env, jobject peer_info_handle, EGLDisplay display, jobject pixel_format, bool use_display_bpp, int drawable_type) {
	if ((*env)->GetDirectBufferCapacity(env, peer_info_handle) < sizeof(FCLPeerInfo)) {
		throwException(env, "Handle too small");
		return false;
	}
	FCLPeerInfo *peer_info = (*env)->GetDirectBufferAddress(env, peer_info_handle);
	if (!extgl_InitEGL(display)) {
		throwException(env, "Could not init EGL");
		return false;
	}
	jclass cls_pixel_format = (*env)->GetObjectClass(env, pixel_format);
	int colorSamples = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "colorSamples", "I"));
	if (colorSamples > 0) {
		throwException(env, "Color samples > 0 specified but there's no support.");
		return false;
	}
	bool floating_point = (bool)(*env)->GetBooleanField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "floating_point", "Z"));
	if (floating_point) {
		throwException(env, "Floating point specified but there's no support.");
		return false;
	}
	bool floating_point_packed = (bool)(*env)->GetBooleanField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "floating_point_packed", "Z"));
	if (floating_point_packed) {
		throwException(env, "Packed floating point specified but there's no support.");
		return false;
	}
	bool sRGB = (bool)(*env)->GetBooleanField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "sRGB", "Z"));
	if (sRGB) {
		throwException(env, "sRGB specified but there's no support.");
		return false;
	}

	EGLConfig *configs = chooseVisualEGL(env, display, pixel_format, use_display_bpp, drawable_type);
	if (configs == NULL) {
		throwException(env, "Could not choose EGL config");
		return false;
	}
	if (isDebugEnabled()) {
		dumpVisualInfo(env, display, configs[0]);
	}
	EGLint config_id;
	int result = lwjgl_eglGetConfigAttrib(display, configs[0], EGL_CONFIG_ID, &config_id);
	free(configs);
	if (result != EGL_TRUE) {
		throwException(env, "Could not get EGL_CONFIG_ID from EGLConfig");
		return false;
	}
	peer_info->config_id = config_id;
	peer_info->display = display;
	peer_info->drawable = EGL_NO_SURFACE;
	return true;
}
