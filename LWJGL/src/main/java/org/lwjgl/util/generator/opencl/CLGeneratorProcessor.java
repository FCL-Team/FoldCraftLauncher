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

import org.lwjgl.PointerWrapper;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLPlatform;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

/**
 * Generator tool for creating the OpenCL capabilities classes
 *
 * @author Spasi
 */
@SupportedAnnotationTypes({ "*" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedOptions({ "generatechecks", "contextspecific" })
public class CLGeneratorProcessor extends AbstractProcessor {

	public static final String CLCAPS_CLASS_NAME        = "CLCapabilities";
	public static final String PLATFORM_CAPS_CLASS_NAME = "CLPlatformCapabilities";
	public static final String DEVICE_CAPS_CLASS_NAME   = "CLDeviceCapabilities";

	private static final String EXTENSION_PREFIX = "CL_";
	private static final String CORE_PREFIX      = "Open";

	private static boolean first_round = true;

	static String getExtensionName(String interface_name) {
		if ( interface_name.startsWith("CL") ) {
			return CORE_PREFIX + interface_name;
		} else {
			return EXTENSION_PREFIX + interface_name;
		}
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if ( roundEnv.processingOver() || !first_round ) {
			System.exit(0);
			return true;
		}
		try {
			Set<TypeElement> templates = ElementFilter.typesIn(roundEnv.getRootElements());
			/**
			 * provide the full set of ex-InterfaceDeclaration
			 * annotated templates elements
			 */
			generateCLCapabilitiesSource(templates);
			generateCLPDCapabilitiesSource(templates, CLPlatformExtension.class, PLATFORM_CAPS_CLASS_NAME, CLPlatform.class, "platform");
			generateCLPDCapabilitiesSource(templates, CLDeviceExtension.class, DEVICE_CAPS_CLASS_NAME, CLDevice.class, "device");
			first_round = false;
			return true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void printHeader(final PrintWriter writer) {
		writer.println("/* MACHINE GENERATED FILE, DO NOT EDIT */");
		writer.println();
		writer.println("package org.lwjgl.opencl;");
		writer.println();
	}

	private void generateCLCapabilitiesSource(Set<TypeElement> templates) throws IOException {
		final PrintWriter writer = new PrintWriter(processingEnv.getFiler().createSourceFile("org.lwjgl.opencl." + CLCAPS_CLASS_NAME, processingEnv.getElementUtils().getPackageElement("org.lwjgl.opencl")).openWriter());
		printHeader(writer);

		CLCapabilitiesGenerator.generateClassPrologue(writer);
		for ( TypeElement d : templates ) {
			if ( d.getKind().isInterface() ) {
				CLCapabilitiesGenerator.generateSymbolAddresses(processingEnv, writer, d);
			}
		}
		writer.println();

		CLCapabilitiesGenerator.generateConstructor(processingEnv, writer, templates);

		CLCapabilitiesGenerator.generateCapabilitiesGetters(writer);
		for ( TypeElement d : templates ) {
			if ( d.getKind().isInterface() ) {
				CLCapabilitiesGenerator.generateExtensionChecks(processingEnv, writer, d);
			}
		}

		writer.println("}");
		writer.close();
	}

	private void generateCLPDCapabilitiesSource(Set<TypeElement> templates, final Class<? extends Annotation> capsType, final String capsName, final Class<? extends PointerWrapper> objectType, final String objectName) throws IOException {
		final PrintWriter writer = new PrintWriter(processingEnv.getFiler().createSourceFile("org.lwjgl.opencl." + capsName, processingEnv.getElementUtils().getPackageElement("org.lwjgl.opencl")).openWriter());
		printHeader(writer);
		writer.println("import java.util.*;");
		writer.println();

		CLPDCapabilitiesGenerator.generateClassPrologue(writer, capsName);

		for ( TypeElement t : templates ) {
			if ( t.getKind().isInterface() && t.getAnnotation(capsType) != null ) {
				CLPDCapabilitiesGenerator.generateExtensions(writer, (TypeElement)t);
			}
		}
		writer.println();

		CLPDCapabilitiesGenerator.generateConstructor(processingEnv, writer, templates, capsType, capsName, objectType, objectName);

		CLPDCapabilitiesGenerator.generateGetters(writer);

		CLPDCapabilitiesGenerator.generateToString(writer, templates, capsType);

		writer.println("}");
		writer.close();
	}
}
