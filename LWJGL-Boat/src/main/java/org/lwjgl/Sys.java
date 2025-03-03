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
package org.lwjgl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.Platform;

import java.awt.Desktop;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * <p>
 * System class (named Sys so as not to conflict with java.lang.System)
 * </p>
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision$
 * $Id$
 */
public final class Sys {

	/**
	 * No constructor for Sys.
	 */
	private Sys() {
	}

	/**
	 * Return the version of the core LWJGL libraries as a String.
	 */
	public static String getVersion() {
		return Version.getVersion();
	}

	/**
	 * Initialization. This is just a dummy method to trigger the static constructor.
	 */
	public static void initialize() {
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
	}

	/**
	 * Obtains the number of ticks that the hires timer does in a second. This method is fast;
	 * it should be called as frequently as possible, as it recalibrates the timer.
	 *
	 * @return timer resolution in ticks per second or 0 if no timer is present.
	 */
	public static long getTimerResolution() {
		return 1000;
	}

	/**
	 * Gets the current value of the hires timer, in ticks. When the Sys class is first loaded
	 * the hires timer is reset to 0. If no hires timer is present then this method will always
	 * return 0.<p><strong>NOTEZ BIEN</strong> that the hires timer WILL wrap around.
	 *
	 * @return the current hires time, in ticks (always >= 0)
	 */
	public static long getTime() {
		return GLFW.glfwGetTimerValue();
	}

	public static long getNanoTime() {
		return System.nanoTime();
	}

	/**
	 * Attempt to display a modal alert to the user. This method should be used
	 * when a game fails to initialize properly or crashes out losing its display
	 * in the process. It is provided because AWT may not be available on the target
	 * platform, although on Mac and Linux and other platforms supporting AWT we
	 * delegate the task to AWT instead of doing it ourselves.
	 * <p>
	 * The alert should display the title and the message and then the current
	 * thread should block until the user dismisses the alert - typically with an
	 * OK button click.
	 * <p>
	 * It may be that the user's system has no windowing system installed for some
	 * reason, in which case this method may do nothing at all, or attempt to provide
	 * some console output.
	 *
	 * @param title The title of the alert. We suggest using the title of your game.
	 * @param message The message text for the alert.
	 */
	public static void alert(String title, String message) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			LWJGLUtil.log("Caught exception while setting Look-and-Feel: " + e);
		}
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Open the system web browser and point it at the specified URL. It is recommended
	 * that this not be called whilst your game is running, but on application exit in
	 * a shutdown hook, as the screen resolution will not be reset when the browser is
	 * brought into view.
	 * <p>
	 * There is no guarantee that this will work, nor that we can detect if it has
	 * failed - hence we don't return success code or throw an Exception. This is just a
	 * best attempt at opening the URL given - don't rely on it to work!
	 * <p>
	 * @param url The URL. Ensure that the URL is properly encoded.
	 * @return false if we are CERTAIN the call has failed
	 */
	public static boolean openURL(String url) {
		if (!Desktop.isDesktopSupported())
			return false;

		Desktop desktop = Desktop.getDesktop();
		if (!desktop.isSupported(Desktop.Action.BROWSE))
			return false;

		try {
			desktop.browse(new URI(url));
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Get the contents of the system clipboard. The system might not have a
	 * clipboard (particularly if it doesn't even have a keyboard) in which case
	 * we return null. Otherwise we return a String, which may be the empty
	 * string "".
	 *
	 * @return a String, or null if there is no system clipboard.
	 */
	public static String getClipboard() {
		return GLFW.glfwGetClipboardString(GLFW.glfwGetPrimaryMonitor());
	}

    public static boolean is64Bit() {
        String osArch = System.getProperty("os.arch");
        return osArch.contains("64") || osArch.startsWith("armv8");
    }
}
