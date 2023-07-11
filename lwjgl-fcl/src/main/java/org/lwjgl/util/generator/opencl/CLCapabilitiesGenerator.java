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
package org.lwjgl.util.generator.opencl;

import org.lwjgl.util.generator.*;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * CLCapabilities generator.
 *
 * @author Spasi
 */
public class CLCapabilitiesGenerator {

	static void generateClassPrologue(final PrintWriter writer) {
		writer.println("public final class " + CLGeneratorProcessor.CLCAPS_CLASS_NAME + " {");
		writer.println();
	}

	static void generateSymbolAddresses(ProcessingEnvironment env, final PrintWriter writer, final TypeElement d) {
		if ( d.getAnnotation(CLPlatformExtension.class) == null && d.getAnnotation(CLDeviceExtension.class) == null && !d.getSimpleName().toString().startsWith("CL") ) {
			throw new RuntimeException("An OpenCL extension is missing an extension type annotation: " + d.getSimpleName());
		}
		final Alias alias_annotation = d.getAnnotation(Alias.class);
		final boolean aliased = alias_annotation != null && alias_annotation.postfix().length() > 0;

		boolean foundNative = false;
		for ( final ExecutableElement method : Utils.getMethods(d) ) {
			if ( method.getAnnotation(Alternate.class) != null || method.getAnnotation(Reuse.class) != null ) {
				continue;
			}

			if ( !foundNative ) {
				//writer.println("\t// " + d.getSimpleName());
				writer.println("\tstatic final boolean " + CLGeneratorProcessor.getExtensionName(d.getSimpleName().toString() + ";"));
				foundNative = true;
			}
			writer.print("\tstatic final long " + Utils.getFunctionAddressName(d, method) + " = CL.getFunctionAddress(");

			if ( aliased ) {
				writer.println("new String [] {\"" + Utils.getFunctionAddressName(d, method) + "\",\"" + method.getSimpleName() + alias_annotation.postfix() + "\"});");
			} else {
				writer.println("\"" + Utils.getFunctionAddressName(d, method) + "\");");
			}
		}

		if ( foundNative ) {
			writer.println();
		}
	}

	static void generateConstructor(ProcessingEnvironment env, final PrintWriter writer, final Set<? extends TypeElement> interface_decls) {
		writer.println("\tprivate " + CLGeneratorProcessor.CLCAPS_CLASS_NAME + "() {}");
		writer.println();
		writer.println("\tstatic {");

		for ( final TypeElement d : interface_decls ) {
			if ( d.getKind().isInterface() ) {
				if ( Utils.getMethods(d).isEmpty() ) {
					continue;
				}

				//writer.println("\t\tif ( " + getExtensionSupportedName(d.getSimpleName()) + "() )");
				//writer.println("\t\t\t" + SUPPORTED_EXTS + ".add(\"" + CLGeneratorProcessor.getExtensionName(d.getSimpleName()) + "\");");
				writer.println("\t\t" + CLGeneratorProcessor.getExtensionName(d.getSimpleName().toString()) + " = " + getExtensionSupportedName(d.getSimpleName().toString()) + "();");
			}
		}

		writer.println("\t}\n");
	}

	static void generateExtensionChecks(ProcessingEnvironment env, final PrintWriter writer, TypeElement d) {
		Iterator<? extends ExecutableElement> methods = Utils.getMethods(d).iterator();
		if ( !methods.hasNext() ) {
			return;
		}

		writer.println("\tprivate static boolean " + getExtensionSupportedName(d.getSimpleName().toString()) + "() {");
		writer.println("\t\treturn ");

		boolean first = true;
		while ( methods.hasNext() ) {
			ExecutableElement method = methods.next();
			if ( method.getAnnotation(Alternate.class) != null ) {
				continue;
			}

			if ( !first ) {
				writer.println(" &");
			} else {
				first = false;
			}

			final boolean optional = method.getAnnotation(Optional.class) != null;

			writer.print("\t\t\t");
			if ( optional ) {
				writer.print('(');
			}
			writer.print(Utils.getFunctionAddressName(d, method) + " != 0");
			if ( optional ) {
				writer.print(" || true)");
			}
		}
		writer.println(";");
		writer.println("\t}");
		writer.println();
	}

	private static String getExtensionSupportedName(final String class_name) {
		return "is" + class_name + "Supported";
	}

	public static void generateCapabilitiesGetters(final PrintWriter writer) {
		writer.println("\tpublic static CLPlatformCapabilities getPlatformCapabilities(final CLPlatform platform) {\n"
		               + "\t\tplatform.checkValid();\n"
		               + "\n"
		               + "\t\tCLPlatformCapabilities caps = (CLPlatformCapabilities)platform.getCapabilities();\n"
		               + "\t\tif ( caps == null )\n"
		               + "\t\t\tplatform.setCapabilities(caps = new CLPlatformCapabilities(platform));\n"
		               + "\n"
		               + "\t\treturn caps;\n"
		               + "\t}\n");

		writer.println("\tpublic static CLDeviceCapabilities getDeviceCapabilities(final CLDevice device) {\n"
		               + "\t\tdevice.checkValid();\n"
		               + "\n"
		               + "\t\tCLDeviceCapabilities caps = (CLDeviceCapabilities)device.getCapabilities();\n"
		               + "\t\tif ( caps == null )\n"
		               + "\t\t\tdevice.setCapabilities(caps = new CLDeviceCapabilities(device));\n"
		               + "\n"
		               + "\t\treturn caps;\n"
		               + "\t}\n");

	}
}
