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
package org.lwjgl.openal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

/**
 * <p>
 * The ALC11 class implements features in OpenAL 1.1, specifically
 * ALC methods and properties.
 * </p>
 *
 * @author Brian Matzon <brian@matzon.dk>
 * @see ALC10
 * @version $Revision: 2286 $
 * $Id: ALC.java 2286 2006-03-23 19:32:21 +0000 (to, 23 mar 2006) matzon $
 */
public final class ALC11 {

	public static final int ALC_DEFAULT_ALL_DEVICES_SPECIFIER			= 0x1012;
	public static final int ALC_ALL_DEVICES_SPECIFIER					= 0x1013;

	public static final int ALC_CAPTURE_DEVICE_SPECIFIER				= 0x310;
	public static final int ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER		= 0x311;
	public static final int ALC_CAPTURE_SAMPLES							= 0x312;

	public static final int ALC_MONO_SOURCES							= 0x1010;
	public static final int ALC_STEREO_SOURCES							= 0x1011;

	/**
	 * The alcCaptureOpenDevice function allows the application to connect to a capture
	 * device. To obtain a list of all available capture devices, use getCaptureDevices a list of all
	 * capture devices will be returned. Retrieving ALC_CAPTURE_DEVICE_SPECIFIER with a valid capture device specified will result
	 * in the name of that device being returned as a single string.
	 *
	 * If the function returns null, then no sound driver/device has been found, or the
	 * requested format could not be fulfilled.
	 * The "deviceName" argument is a string that requests a certain device or
	 * device configuration. If null is specified, the implementation will provide an
	 * implementation specific default. The "frequency" and "format" arguments specify the format that
	 * audio data will be presented to the application, and match the values that can be passed to
	 * alBufferData. The implementation is expected to convert and resample to this format on
	 * behalf of the application. The "buffersize" argument specifies the number of sample frames
	 * to buffer in the AL, for example, requesting a format of AL_FORMAT_STEREO16 and
	 * a buffer size of 1024 would require the AL to store up to 1024 * 4 bytes of audio data.
	 * Note that the implementation may use a larger buffer than requested if it needs to, but the
	 * implementation will set up a buffer of at least the requested size.
	 * Specifying a compressed or extension-supplied format may result in failure, even if the
	 * extension is supplied for rendering.
	 *
	 * <i>LWJGL SPECIFIC: the actual created device is managed internally in lwjgl</i>
	 *
	 * @param devicename Name of device to open for capture
	 * @param frequency Frequency of samples to capture
	 * @param format Format of samples to capture
	 * @param buffersize Size of buffer to capture to
	 * @return ALCdevice if it was possible to open a device
	 */
	public static ALCdevice alcCaptureOpenDevice(String devicename, int frequency, int format, int buffersize) {
		ByteBuffer buffer = MemoryUtil.encodeASCII(devicename);
		long device_address = nalcCaptureOpenDevice(MemoryUtil.getAddressSafe(buffer), frequency, format, buffersize);
		if(device_address != 0) {
			ALCdevice device = new ALCdevice(device_address);
			synchronized (ALC10.devices) {
				ALC10.devices.put(device_address, device);
			}
			return device;
		}
		return null;
	}
	private static native long nalcCaptureOpenDevice(long devicename, int frequency, int format, int buffersize);

	/**
	 * The alcCaptureCloseDevice function allows the application to disconnect from a capture
	 * device.
	 *
	 * The return code will be true or false, indicating success or failure. If
	 * the device is null or invalid, an ALC_INVALID_DEVICE error will be generated.
	 * Once closed, a capture device is invalid.
	 * @return true if device was successfully closed
	 */
	public static boolean alcCaptureCloseDevice(ALCdevice device) {
		boolean result = nalcCaptureCloseDevice(ALC10.getDevice(device));
		synchronized (ALC10.devices) {
			device.setInvalid();
			ALC10.devices.remove(new Long(device.device));
		}
		return result;
	}
	static native boolean nalcCaptureCloseDevice(long device);

	/**
	 * Once a capture device has been opened via alcCaptureOpenDevice, it is made to start
	 * recording audio via the alcCaptureStart entry point:
	 *
	 * Once started, the device will record audio to an internal ring buffer, the size of which was
	 * specified when opening the device.
	 * The application may query the capture device to discover how much data is currently
	 * available via the alcGetInteger with the ALC_CAPTURE_SAMPLES token. This will
	 * report the number of sample frames currently available.
	 */
	public static  void alcCaptureStart(ALCdevice device) {
		nalcCaptureStart(ALC10.getDevice(device));
	}
	static native void nalcCaptureStart(long device);

	/**
	 * If the application doesn't need to capture more audio for an amount of time, they can halt
	 * the device without closing it via the alcCaptureStop entry point.
	 * The implementation is encouraged to optimize for this case. The amount of audio
	 * samples available after restarting a stopped capture device is reset to zero. The
	 * application does not need to stop the capture device to read from it.
	 */
	public static  void alcCaptureStop(ALCdevice device) {
		nalcCaptureStop(ALC10.getDevice(device));
	}
	static native void nalcCaptureStop(long device);

	/**
	 * When the application feels there are enough samples available to process, it can obtain
	 * them from the AL via the alcCaptureSamples entry point.
	 *
	 * The "buffer" argument specifies an application-allocated buffer that can contain at least
	 * "samples" sample frames. The implementation may defer conversion and resampling until
	 * this point. Requesting more sample frames than are currently available is an error.
	 *
	 * @param buffer Buffer to store samples in
	 * @param samples Number of samples to request
	 */
	public static  void alcCaptureSamples(ALCdevice device, ByteBuffer buffer, int samples ) {
		nalcCaptureSamples(ALC10.getDevice(device), MemoryUtil.getAddress(buffer), samples);
	}
	static native void nalcCaptureSamples(long device, long buffer, int samples );

	static native void initNativeStubs() throws LWJGLException;

	/**
	 * Initializes ALC11, including any extensions
	 * @return true if initialization was successfull
	 */
	static boolean initialize() {
		try {
			IntBuffer ib = BufferUtils.createIntBuffer(2);
			ALC10.alcGetInteger(AL.getDevice(), ALC10.ALC_MAJOR_VERSION, ib);
			ib.position(1);
			ALC10.alcGetInteger(AL.getDevice(), ALC10.ALC_MINOR_VERSION, ib);

			int major = ib.get(0);
			int minor = ib.get(1);

			// checking for version 1.x+
			if(major >= 1) {

				// checking for version 1.1+
				if(major > 1 || minor >= 1) {
					ALC11.initNativeStubs();
                                        AL11.initNativeStubs();
				}
			}
		} catch (LWJGLException le) {
			LWJGLUtil.log("failed to initialize ALC11: " + le);
			return false;
		}
		return true;
	}
}
