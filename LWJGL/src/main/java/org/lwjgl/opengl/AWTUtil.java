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
package org.lwjgl.opengl;

/**
 * @author elias_naur
 */

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

final class AWTUtil {
	public static boolean hasWheel() {
		return true;
	}

	public static int getButtonCount() {
		return MouseEventQueue.NUM_BUTTONS;
	}

	public static int getNativeCursorCapabilities() {
		if (LWJGLUtil.getPlatform() != LWJGLUtil.PLATFORM_MACOSX || LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 4)) {
			int cursor_colors = Toolkit.getDefaultToolkit().getMaximumCursorColors();
			boolean supported = cursor_colors >= Short.MAX_VALUE && getMaxCursorSize() > 0;
			int caps = supported ? org.lwjgl.input.Cursor.CURSOR_8_BIT_ALPHA | org.lwjgl.input.Cursor.CURSOR_ONE_BIT_TRANSPARENCY: 0 | org.lwjgl.input.Cursor.CURSOR_ANIMATION;
			return caps;
		} else {
			/* Return no capability in Mac OS X 10.3 and earlier , as there are two unsolved bugs (both reported to apple along with
			   minimal test case):
			   1. When a custom cursor (or some standard) java.awt.Cursor is assigned to a
			   Componennt, it is reset to the default pointer cursor when the window is de-
			   activated and the re-activated. The Cursor can not be reset to the custom cursor,
			   with another setCursor.
			   2. When the cursor is moving in the top pixel row (y = 0 in AWT coordinates) in fullscreen
			   mode, no mouse moved events are reported, even though mouse pressed/released and dragged
			   events are reported
			 */
			return 0;
		}
	}

	public static Robot createRobot(final Component component) {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<Robot>() {
				public Robot run() throws Exception {
					return new Robot(component.getGraphicsConfiguration().getDevice());
				}
			});
		} catch (PrivilegedActionException e) {
			LWJGLUtil.log("Got exception while creating robot: " + e.getCause());
			return null;
		}
	}

	private static int transformY(Component component, int y) {
		return component.getHeight() - 1 - y;
	}

	/**
	 * Use reflection to access the JDK 1.5 pointer location, if possible and
	 * only if the given component is on the same screen as the cursor. Return
	 * null otherwise.
	 */
	private static Point getPointerLocation(final Component component) {
		try {
			final GraphicsConfiguration config = component.getGraphicsConfiguration();
			if (config != null) {
				PointerInfo pointer_info = AccessController.doPrivileged(new PrivilegedExceptionAction<PointerInfo>() {
					public PointerInfo run() throws Exception {
						return MouseInfo.getPointerInfo();
					}
				});
				GraphicsDevice device = pointer_info.getDevice();
				if (device == config.getDevice()) {
					return pointer_info.getLocation();
				}
				return null;
			}
		} catch (Exception e) {
			LWJGLUtil.log("Failed to query pointer location: " + e.getCause());
		}
		return null;
	}

	/**
	 * Use the 1.5 API to get the cursor position relative to the component. Return null
	 * if it fails (JDK <= 1.4).
	 */
	public static Point getCursorPosition(Component component) {
		try {
			Point pointer_location = getPointerLocation(component);
			if (pointer_location != null) {
				Point location = component.getLocationOnScreen();
				pointer_location.translate(-location.x, -location.y);
				pointer_location.move(pointer_location.x, transformY(component, pointer_location.y));
				return pointer_location;
			}
		} catch (IllegalComponentStateException e) {
			LWJGLUtil.log("Failed to set cursor position: " + e);
		} catch (NoClassDefFoundError e) { // Not JDK 1.5
			LWJGLUtil.log("Failed to query cursor position: " + e);
		}
		return null;
	}

	public static void setCursorPosition(Component component, Robot robot, int x, int y) {
		if (robot != null) {
			try {
				Point location = component.getLocationOnScreen();
				int transformed_x = location.x + x;
				int transformed_y = location.y + transformY(component, y);
				robot.mouseMove(transformed_x, transformed_y);
			} catch (IllegalComponentStateException e) {
				LWJGLUtil.log("Failed to set cursor position: " + e);
			}
		}
	}

	public static int getMinCursorSize() {
		Dimension min_size = Toolkit.getDefaultToolkit().getBestCursorSize(0, 0);
		return Math.max(min_size.width, min_size.height);
	}

	public static int getMaxCursorSize() {
		Dimension max_size = Toolkit.getDefaultToolkit().getBestCursorSize(10000, 10000);
		return Math.min(max_size.width, max_size.height);
	}

	/** Native cursor handles */
	public static Cursor createCursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
		BufferedImage cursor_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int[] pixels = new int[images.remaining()];
		int old_position = images.position();
		images.get(pixels);
		images.position(old_position);
		cursor_image.setRGB(0, 0, width, height, pixels, 0, width);
		return Toolkit.getDefaultToolkit().createCustomCursor(cursor_image, new Point(xHotspot, yHotspot), "LWJGL Custom cursor");
	}
}
