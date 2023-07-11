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

import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

/**
 * This class exposes the platform specific functionality present in the
 * NV_present_video extension.
 *
 * @author Spasi
 * @since 20/5/2011
 */
public final class NVPresentVideoUtil {

	private NVPresentVideoUtil() {}

	private static void checkExtension() {
		if ( LWJGLUtil.CHECKS && !GLContext.getCapabilities().GL_NV_present_video )
			throw new IllegalStateException("NV_present_video is not supported");
	}

	private static ByteBuffer getPeerInfo() {
		return ContextGL.getCurrentContext().getPeerInfo().getHandle();
	}

	/**
	 * Enumerate the available video output devices. This method is the cross-platform
	 * equivalent of glXEnumerateVideoDevicesNV and wglEnumerateVideoDevicesNV. Since they are
	 * not really compatible, this method works like the WGL version. That is, you first
	 * call it with a null devices buffer, get the number of devices, then call it again
	 * with an appropriately sized buffer.
	 *
	 * @param devices the buffer to store devices in
	 *
	 * @return the number of available video output devices
	 */
	public static int glEnumerateVideoDevicesNV(LongBuffer devices) {
		checkExtension();

		if ( devices != null )
			BufferChecks.checkBuffer(devices, 1);
		return nglEnumerateVideoDevicesNV(getPeerInfo(), devices, devices == null ? 0 : devices.position());
	}

	private static native int nglEnumerateVideoDevicesNV(ByteBuffer peer_info, LongBuffer devices, int devices_position);

	/**
	 * Binds the video output device specified to one of the context's available video output slots.
	 * This method is the cross-platform equivalent of glXBindVideoDeviceNV and wglBindVideoDeviceNV.
	 * To release a video device without binding another device to the same slot, call it with
	 * video_device set to 0 (will use INVALID_HANDLE_VALUE on WGL).
	 *
	 * @param video_slot   the video slot
	 * @param video_device the video device
	 * @param attrib_list  the attributes to use
	 *
	 * @return true if the binding was successful
	 */
	public static boolean glBindVideoDeviceNV(int video_slot, long video_device, IntBuffer attrib_list) {
		checkExtension();

		if ( attrib_list != null )
			BufferChecks.checkNullTerminated(attrib_list);
		return nglBindVideoDeviceNV(getPeerInfo(), video_slot, video_device, attrib_list, attrib_list == null ? 0 : attrib_list.position());
	}

	private static native boolean nglBindVideoDeviceNV(ByteBuffer peer_info, int video_slot, long video_device, IntBuffer attrib_list, int attrib_list_position);

	/**
	 * Queries an attribute associated with the current context. This method is the cross-platform
	 * equivalent of glXQueryContext and wglQueryCurrentContextNV.
	 *
	 * @param attrib the attribute to query
	 * @param value  the buffer to store the value in
	 */
	public static boolean glQueryContextNV(int attrib, IntBuffer value) {
		checkExtension();

		BufferChecks.checkBuffer(value, 1);
		ContextGL ctx = ContextGL.getCurrentContext();
		return nglQueryContextNV(ctx.getPeerInfo().getHandle(), ctx.getHandle(), attrib, value, value.position());
	}

	private static native boolean nglQueryContextNV(ByteBuffer peer_info, ByteBuffer context_handle, int attrib, IntBuffer value, int value_position);

}
