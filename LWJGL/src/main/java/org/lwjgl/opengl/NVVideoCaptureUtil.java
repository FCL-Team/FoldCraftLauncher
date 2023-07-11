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
 * NV_video_capture extension.
 *
 * @author Spasi
 * @since 20/5/2011
 */
public final class NVVideoCaptureUtil {

	private NVVideoCaptureUtil() {}

	private static void checkExtension() {
		if ( LWJGLUtil.CHECKS && !GLContext.getCapabilities().GL_NV_video_capture )
			throw new IllegalStateException("NV_video_capture is not supported");
	}

	private static ByteBuffer getPeerInfo() {
		return ContextGL.getCurrentContext().getPeerInfo().getHandle();
	}

	/**
	 * After successfully locking a video capture device, use this method to bind it
	 * to the capture slot specified in the current context. This method is the cross-
	 * platform equivalent of glXBindVideoCaptureDeviceNV and wglBindVideoCaptureDeviceNV.
	 *
	 * @param video_slot the video slot
	 * @param device     the video capture device
	 *
	 * @return true if the binding was successful
	 */
	public static boolean glBindVideoCaptureDeviceNV(int video_slot, long device) {
		checkExtension();
		return nglBindVideoCaptureDeviceNV(getPeerInfo(), video_slot, device);
	}

	private static native boolean nglBindVideoCaptureDeviceNV(ByteBuffer peer_info, int video_slot, long device);

	/**
	 * Enumerate the available video capture devices. This method is the cross-platform
	 * equivalent of glXEnumerateVideoCaptureDevicesNV and wglEnumerateVideoCaptureDevicesNV.
	 * Since they are not really compatible, this method works like the WGL version. That is,
	 * you first call it with a null devices buffer, get the number of devices, then call it
	 * again with an appropriately sized buffer.
	 *
	 * @param devices the buffer to store devices in
	 *
	 * @return the number of available video capture devices
	 */
	public static int glEnumerateVideoCaptureDevicesNV(LongBuffer devices) {
		checkExtension();

		if ( devices != null )
			BufferChecks.checkBuffer(devices, 1);
		return nglEnumerateVideoCaptureDevicesNV(getPeerInfo(), devices, devices == null ? 0 : devices.position());
	}

	private static native int nglEnumerateVideoCaptureDevicesNV(ByteBuffer peer_info, LongBuffer devices, int devices_position);

	/**
	 * To lock a video capture device to a display connection, use this method.
	 * Before using a video capture device, it must be locked.  Once a
	 * video capture device is locked by a process, no other process can
	 * lock a video capture device with the same unique ID until the lock
	 * is released or the process ends.
	 *
	 * @param device the device to lock
	 *
	 * @return true if the lock was successful
	 */
	public static boolean glLockVideoCaptureDeviceNV(long device) {
		checkExtension();
		return nglLockVideoCaptureDeviceNV(getPeerInfo(), device);
	}

	private static native boolean nglLockVideoCaptureDeviceNV(ByteBuffer peer_info, long device);

	/**
	 * Use this method to query the unique ID of the physical device backing a
	 * video capture device handle.
	 *
	 * @param device    the device
	 * @param attribute the attribute to query
	 * @param value     the buffer to store the value in
	 *
	 * @return true if the query was successful
	 */
	public static boolean glQueryVideoCaptureDeviceNV(long device, int attribute, IntBuffer value) {
		checkExtension();

		BufferChecks.checkBuffer(value, 1);
		return nglQueryVideoCaptureDeviceNV(getPeerInfo(), device, attribute, value, value.position());
	}

	private static native boolean nglQueryVideoCaptureDeviceNV(ByteBuffer peer_info, long device, int attribute, IntBuffer value, int value_position);

	/**
	 * Use this method when finished capturing data on a locked video capture device
	 * to unlock it.
	 *
	 * @param device the device
	 *
	 * @return true if the device was unlocked successfully
	 */
	public static boolean glReleaseVideoCaptureDeviceNV(long device) {
		checkExtension();
		return nglReleaseVideoCaptureDeviceNV(getPeerInfo(), device);
	}

	private static native boolean nglReleaseVideoCaptureDeviceNV(ByteBuffer peer_info, long device);

}

