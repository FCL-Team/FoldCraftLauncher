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
package org.lwjgl.util.generator.opengl;

import org.lwjgl.util.generator.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

/**
 * Generator tool for creating the ContexCapabilities class
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 3316 $ $Id: ContextGeneratorProcessorFactory.java 3316
 *          2010-04-09 23:57:40Z spasi $
 */
@SupportedAnnotationTypes({ "*" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedOptions({ "contextspecific", "generatechecks" })
public class GLESGeneratorProcessor extends AbstractProcessor {

	private static boolean first_round = true;

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if ( roundEnv.processingOver() || !first_round ) {
			System.exit(0);
			return true;
		}
		Map<String, String> options = processingEnv.getOptions();
		boolean generate_error_checks = options.containsKey("generatechecks");
		boolean context_specific = options.containsKey("contextspecific");
		try {
			generateContextCapabilitiesSource(ElementFilter.typesIn(roundEnv.getRootElements()), context_specific, generate_error_checks);
			first_round = false;
			return true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void generateContextCapabilitiesSource(Set<TypeElement> templates, boolean context_specific, boolean generate_error_checks) throws IOException {
		PrintWriter writer = new PrintWriter(processingEnv.getFiler().createSourceFile("org.lwjgl.opengles." + Utils.CONTEXT_CAPS_CLASS_NAME, processingEnv.getElementUtils().getPackageElement("org.lwjgl.opengles")).openWriter());
		writer.println("/* MACHINE GENERATED FILE, DO NOT EDIT */");
		writer.println();
		writer.println("package org.lwjgl.opengles;");
		writer.println();
		writer.println("import org.lwjgl.LWJGLException;");
		writer.println("import org.lwjgl.LWJGLUtil;");
		writer.println("import java.util.Set;");
		writer.println("import java.util.HashSet;");
		writer.println();
		GLESCapabilitiesGenerator.generateClassPrologue(writer, context_specific, generate_error_checks);
		for ( TypeElement interface_decl : templates ) {
			if ( interface_decl.getKind().isInterface() ) {
				if ( Utils.isFinal(interface_decl) ) {
					GLESCapabilitiesGenerator.generateField(writer, interface_decl);
				}
			}
		}
		writer.println();
		if ( context_specific ) {
			for ( TypeElement interface_decl : templates ) {
				if ( interface_decl.getKind().isInterface() ) {
					GLESCapabilitiesGenerator.generateSymbolAddresses(processingEnv, writer, interface_decl);
				}
			}
			writer.println();
			for ( TypeElement interface_decl : templates ) {
				if ( interface_decl.getKind().isInterface() ) {
					GLESCapabilitiesGenerator.generateAddressesInitializers(processingEnv, writer, interface_decl);
				}
			}
			writer.println();
		}

		if ( context_specific ) {
			writer.println("\tprivate static void remove(Set supported_extensions, String extension) {");
			writer.println("\t\tLWJGLUtil.log(extension + \" was reported as available but an entry point is missing\");");
			writer.println("\t\tsupported_extensions.remove(extension);");
			writer.println("\t}\n");
		}

		GLESCapabilitiesGenerator.generateInitStubsPrologue(writer, context_specific);
		for ( TypeElement interface_decl : templates ) {
			if ( interface_decl.getKind().isInterface() ) {
				GLESCapabilitiesGenerator.generateSuperClassAdds(writer, interface_decl, processingEnv);
			}
		}
		for ( TypeElement interface_decl : templates ) {
			if ( interface_decl.getKind().isInterface() ) {
				if ( "GLES20".equals(interface_decl.getSimpleName().toString()) ) {
					continue;
				}
				GLESCapabilitiesGenerator.generateInitStubs(processingEnv, writer, interface_decl, context_specific);
			}
		}
		GLESCapabilitiesGenerator.generateInitStubsEpilogue(writer, context_specific);
		writer.println();
		writer.println("\tstatic void unloadAllStubs() {");
		if ( !context_specific ) {
			writer.println("\t\tif (!loaded_stubs)");
			writer.println("\t\t\treturn;");
			for ( TypeElement interface_decl : templates ) {
				if ( interface_decl.getKind().isInterface() ) {
					GLESCapabilitiesGenerator.generateUnloadStubs(processingEnv, writer, interface_decl);
				}
			}
			writer.println("\t\tloaded_stubs = false;");
		}
		writer.println("\t}");
		writer.println();
		GLESCapabilitiesGenerator.generateInitializerPrologue(writer);
		for ( TypeElement interface_decl : templates ) {
			if ( interface_decl.getKind().isInterface() ) {
				if ( Utils.isFinal(interface_decl) ) {
					GLESCapabilitiesGenerator.generateInitializer(writer, interface_decl, processingEnv);
				}
			}
		}
		writer.println("\t}");
		writer.println("}");
		writer.close();
	}

}
