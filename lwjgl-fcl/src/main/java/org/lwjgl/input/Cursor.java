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
package org.lwjgl.input;

import java.nio.IntBuffer;

import org.lwjgl.BufferChecks;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;

/**
 *
 * A class representing a native cursor. Instances of this
 * class can be used with Mouse.setCursor(), if available.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$
 * $Id$
 */

public class Cursor {
	/** 1 bit transparency for native cursor */
	public static final int		CURSOR_ONE_BIT_TRANSPARENCY	= 1;

	/** 8 bit alhpa native cursor */
	public static final int		CURSOR_8_BIT_ALPHA					= 2;

	/** animation native cursor */
	public static final int		CURSOR_ANIMATION						= 4;

	/** First element to display */
	private final CursorElement[] cursors;

	/** Index into list of cursors */
	private int index;

	private boolean destroyed;

	/**
	 * Constructs a new Cursor, with the given parameters. Mouse must have been created before you can create
	 * Cursor objects. Cursor images are in ARGB format, but only one bit transparancy is guaranteed to be supported.
	 * So to maximize portability, lwjgl applications should only create cursor images with 0x00 or 0xff as alpha values.
	 * The constructor will copy the images and delays, so there's no need to keep them around.
	 *
	 * @param width cursor image width
	 * @param height cursor image height
	 * @param xHotspot the x coordinate of the cursor hotspot
	 * @param yHotspot the y coordinate of the cursor hotspot
	 * @param numImages number of cursor images specified. Must be 1 if animations are not supported.
	 * @param images A buffer containing the images. The origin is at the lower left corner, like OpenGL.
	 * @param delays An int buffer of animation frame delays, if numImages is greater than 1, else null
	 * @throws LWJGLException if the cursor could not be created for any reason
	 */
	public Cursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
		synchronized (OpenGLPackageAccess.global_lock) {
			if ((getCapabilities() & CURSOR_ONE_BIT_TRANSPARENCY) == 0)
				throw new LWJGLException("Native cursors not supported");
			BufferChecks.checkBufferSize(images, width*height*numImages);
			if (delays != null)
				BufferChecks.checkBufferSize(delays, numImages);
			if (!Mouse.isCreated())
				throw new IllegalStateException("Mouse must be created before creating cursor objects");
			if (width*height*numImages > images.remaining())
				throw new IllegalArgumentException("width*height*numImages > images.remaining()");
			if (xHotspot >= width || xHotspot < 0)
				throw new IllegalArgumentException("xHotspot > width || xHotspot < 0");
			if (yHotspot >= height || yHotspot < 0)
				throw new IllegalArgumentException("yHotspot > height || yHotspot < 0");

			Sys.initialize();

			// Hmm
			yHotspot = height - 1 - yHotspot;

			// create cursor (or cursors if multiple images supplied)
			cursors = createCursors(width, height, xHotspot, yHotspot, numImages, images, delays);
		}
	}

	/**
	 * Gets the minimum size of a native cursor. Can only be called if
	 * The Mouse is created and cursor caps includes at least
	 * CURSOR_ONE_BIT_TRANSPARANCY.
	 *
	 * @return the maximum size of a native cursor
	 */
	public static int getMinCursorSize() {
		synchronized (OpenGLPackageAccess.global_lock) {
			if (!Mouse.isCreated())
				throw new IllegalStateException("Mouse must be created.");
			return Mouse.getImplementation().getMinCursorSize();
		}
	}

	/**
	 * Gets the maximum size of a native cursor. Can only be called if
	 * The Mouse is created and cursor caps includes at least
	 * CURSOR_ONE_BIT_TRANSPARANCY.
	 *
	 * @return the maximum size of a native cursor
	 */
	public static int getMaxCursorSize() {
		synchronized (OpenGLPackageAccess.global_lock) {
			if (!Mouse.isCreated())
				throw new IllegalStateException("Mouse must be created.");
			return Mouse.getImplementation().getMaxCursorSize();
		}
	}

	/**
	 * Get the capabilities of the native cursor. Return a bit mask of the native cursor capabilities.
	 * The CURSOR_ONE_BIT_TRANSPARANCY indicates support for cursors with one bit transparancy,
	 * the CURSOR_8_BIT_ALPHA indicates support for 8 bit alpha and CURSOR_ANIMATION indicates
	 * support for cursor animations.
	 *
	 * @return A bit mask with native cursor capabilities.
	 */
	public static int getCapabilities() {
		synchronized (OpenGLPackageAccess.global_lock) {
			if (Mouse.getImplementation() != null)
				return Mouse.getImplementation().getNativeCursorCapabilities();
			else
				return OpenGLPackageAccess.createImplementation().getNativeCursorCapabilities();
		}
	}

	/**
	 * Creates the actual cursor, using a platform specific class
	 */
	private static CursorElement[] createCursors(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
		// create copy and flip images to match ogl
		IntBuffer images_copy = BufferUtils.createIntBuffer(images.remaining());
		flipImages(width, height, numImages, images, images_copy);

		// Mac and Windows doesn't (afaik) allow for animation based cursors, except in the .ani
		// format on Windows, which we don't support.
		// The cursor animation was therefor developed using java side time tracking.
		// unfortunately X flickers when changing cursor. We therefore check for either
		// Windows, Mac or X and do accordingly.
		// we might want to split it into a X/Win/Mac cursor if it gets too cluttered

		CursorElement[] cursors;
		switch (LWJGLUtil.getPlatform()) {
			case LWJGLUtil.PLATFORM_MACOSX:
				
				// OS X requires the image format to be in ABGR format
				convertARGBtoABGR(images_copy);
				
				// create our cursor elements
				cursors = new CursorElement[numImages];
				for(int i=0; i<numImages; i++) {
					Object handle = Mouse.getImplementation().createCursor(width, height, xHotspot, yHotspot, 1, images_copy, null);
					long delay = (delays != null) ? delays.get(i) : 0;
					long timeout = System.currentTimeMillis();
					cursors[i] = new CursorElement(handle, delay, timeout);

					// offset to next image
					images_copy.position(width*height*(i+1));
				}
				break;
			case LWJGLUtil.PLATFORM_WINDOWS:
				// create our cursor elements
				cursors = new CursorElement[numImages];
				for(int i=0; i<numImages; i++) {

					// iterate through the images, and make sure that the pixels are either 0xffxxxxxx or 0x00000000
					int size = width * height;
					for(int j=0; j<size; j++) {
						int index = j + (i*size);
						int alpha = images_copy.get(index) >> 24 & 0xff;
						if(alpha != 0xff) {
							images_copy.put(index, 0);
						}
					}

					Object handle = Mouse.getImplementation().createCursor(width, height, xHotspot, yHotspot, 1, images_copy, null);
					long delay = (delays != null) ? delays.get(i) : 0;
					long timeout = System.currentTimeMillis();
					cursors[i] = new CursorElement(handle, delay, timeout);

					// offset to next image
					images_copy.position(width*height*(i+1));
				}
				break;
			case LWJGLUtil.PLATFORM_LINUX:
			case LWJGLUtil.PLATFORM_FCL:
				// create our cursor elements
				Object handle = Mouse.getImplementation().createCursor(width, height, xHotspot, yHotspot, numImages, images_copy, delays);
				CursorElement cursor_element = new CursorElement(handle, -1, -1);
				cursors = new CursorElement[]{cursor_element};
				break;
			default:
				throw new RuntimeException("Unknown OS");
		}
		return cursors;
	}
	
	/**
	 * Convert an IntBuffer image of ARGB format into ABGR
	 *
	 * @param imageBuffer image to convert
	 */
	private static void convertARGBtoABGR(IntBuffer imageBuffer) {
		for (int i = 0; i < imageBuffer.limit(); i++) {
			int argbColor = imageBuffer.get(i);
			
			byte alpha = (byte)(argbColor >>> 24);
	        byte blue = (byte)(argbColor >>> 16);
	        byte green = (byte)(argbColor >>> 8);
	        byte red = (byte)argbColor;
	        
	        int abgrColor = ((alpha & 0xff) << 24 ) + ((red & 0xff) << 16 ) + ((green & 0xff) << 8 ) + ((blue & 0xff) ); 
	        
	        imageBuffer.put(i, abgrColor);
		}
	}

	/**
	 * Flips the images so they're oriented according to opengl
	 *
	 * @param width Width of image
	 * @param height Height of images
	 * @param numImages How many images to flip
	 * @param images Source images
	 * @param images_copy Destination images
	 */
	private static void flipImages(int width, int height, int numImages, IntBuffer images, IntBuffer images_copy) {
		for (int i = 0; i < numImages; i++) {
			int start_index = i*width*height;
			flipImage(width, height, start_index, images, images_copy);
		}
	}

	/**
	 * @param width Width of image
	 * @param height Height of images
	 * @param start_index index into source buffer to copy to
	 * @param images Source images
	 * @param images_copy Destination images
	 */
	private static void flipImage(int width, int height, int start_index, IntBuffer images, IntBuffer images_copy) {
		for (int y = 0; y < height>>1; y++) {
			int index_y_1 = y*width + start_index;
			int index_y_2 = (height - y - 1)*width + start_index;
			for (int x = 0; x < width; x++) {
				int index1 = index_y_1 + x;
				int index2 = index_y_2 + x;
				int temp_pixel = images.get(index1 + images.position());
				images_copy.put(index1, images.get(index2 + images.position()));
				images_copy.put(index2, temp_pixel);
			}
		}
	}

	/**
	 * Gets the native handle associated with the cursor object.
	 */
	Object getHandle() {
		checkValid();
		return cursors[index].cursorHandle;
	}

	private void checkValid() {
		if (destroyed)
			throw new IllegalStateException("The cursor is destroyed");
	}

	/**
	 * Destroy the native cursor. If the cursor is current,
	 * the current native cursor is set to null (the default
	 * OS cursor)
	 */
	public void destroy() {
		synchronized (OpenGLPackageAccess.global_lock) {
			if (destroyed)
				return;
			if (Mouse.getNativeCursor() == this) {
				try {
					Mouse.setNativeCursor(null);
				} catch (LWJGLException e) {
					// ignore
				}
			}
			for ( CursorElement cursor : cursors ) {
				Mouse.getImplementation().destroyCursor(cursor.cursorHandle);
			}
			destroyed = true;
		}
	}

	/**
	 * Sets the timout property to the time it should be changed
	 */
	protected void setTimeout() {
		checkValid();
		cursors[index].timeout = System.currentTimeMillis() + cursors[index].delay;
	}

	/**
	 * Determines whether this cursor has timed out
	 * @return true if the this cursor has timed out, false if not
	 */
	protected boolean hasTimedOut() {
		checkValid();
		return cursors.length > 1 && cursors[index].timeout < System.currentTimeMillis();
	}

	/**
	 * Changes to the next cursor
	 */
	protected void nextCursor() {
		checkValid();
		index = ++index % cursors.length;
	}

	/**
	 * A single cursor element, used when animating
	 */
	private static class CursorElement {
		/** Handle to cursor */
		final Object cursorHandle;

		/** How long a delay this element should have */
		final long delay;

		/** Absolute time this element times out */
		long timeout;

		CursorElement(Object cursorHandle, long delay, long timeout) {
			this.cursorHandle = cursorHandle;
			this.delay = delay;
			this.timeout = timeout;
		}
	}
}
