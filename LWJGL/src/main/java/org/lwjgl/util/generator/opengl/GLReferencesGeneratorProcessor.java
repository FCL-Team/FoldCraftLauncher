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

import org.lwjgl.util.generator.Alternate;
import org.lwjgl.util.generator.CachedReference;
import org.lwjgl.util.generator.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

/**
 * Generator tool for creating the References class
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 3237 $ $Id: ReferencesGeneratorProcessorFactory.java 3237
 *          2009-09-08 15:07:15Z spasi $
 */
@SupportedAnnotationTypes({ "*" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedOptions({ "generatechecks", "contextspecific" })
public class GLReferencesGeneratorProcessor extends AbstractProcessor {

	private static final String REFERENCES_CLASS_NAME     = "References";
	private static final String REFERENCES_PARAMETER_NAME = "references";

	private static boolean first_round = true;

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if ( roundEnv.processingOver() || !first_round ) {
			System.exit(0);
			return true;
		}
		try {
			generateReferencesSource(processingEnv, ElementFilter.typesIn(roundEnv.getRootElements()));
			first_round = false;
			return true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void generateClearsFromParameters(PrintWriter writer, TypeElement interface_decl, ExecutableElement method) {
		for ( VariableElement param : method.getParameters() ) {
			CachedReference cached_reference_annotation = param.getAnnotation(CachedReference.class);
			if ( cached_reference_annotation != null && cached_reference_annotation.name().length() == 0 ) {
				Class nio_type = Utils.getNIOBufferType(param.asType());
				String reference_name = Utils.getReferenceName(interface_decl, method, param);
				writer.println("\t\tthis." + reference_name + " = null;");
			}
		}
	}

	private static void generateCopiesFromParameters(PrintWriter writer, TypeElement interface_decl, ExecutableElement method) {
		for ( VariableElement param : method.getParameters() ) {
			CachedReference cached_reference_annotation = param.getAnnotation(CachedReference.class);
			if ( cached_reference_annotation != null && cached_reference_annotation.name().length() == 0 ) {
				Class nio_type = Utils.getNIOBufferType(param.asType());
				String reference_name = Utils.getReferenceName(interface_decl, method, param);
				writer.print("\t\t\tthis." + reference_name + " = ");
				writer.println(REFERENCES_PARAMETER_NAME + "." + reference_name + ";");
			}
		}
	}

	private static void generateClearsFromMethods(ProcessingEnvironment env, PrintWriter writer, TypeElement interface_decl) {
		for ( ExecutableElement method : Utils.getMethods(interface_decl) ) {
			if ( method.getAnnotation(Alternate.class) != null ) {
				continue;
			}

			generateClearsFromParameters(writer, interface_decl, method);
		}
	}

	private static void generateCopiesFromMethods(ProcessingEnvironment env, PrintWriter writer, TypeElement interface_decl) {
		for ( ExecutableElement method : Utils.getMethods(interface_decl) ) {
			if ( method.getAnnotation(Alternate.class) != null ) {
				continue;
			}

			generateCopiesFromParameters(writer, interface_decl, method);
		}
	}

	private static void generateReferencesFromParameters(PrintWriter writer, TypeElement interface_decl, ExecutableElement method) {
		for ( VariableElement param : method.getParameters() ) {
			CachedReference cached_reference_annotation = param.getAnnotation(CachedReference.class);
			if ( cached_reference_annotation != null && cached_reference_annotation.name().length() == 0 ) {
				Class nio_type = Utils.getNIOBufferType(param.asType());
				if ( nio_type == null ) {
					throw new RuntimeException(param + " in method " + method + " in " + interface_decl + " is annotated with "
					                           + cached_reference_annotation.annotationType().getSimpleName() + " but the parameter is not a NIO buffer");
				}
				writer.print("\t" + nio_type.getName() + " " + Utils.getReferenceName(interface_decl, method, param));
				writer.println(";");
			}
		}
	}

	private static void generateReferencesFromMethods(ProcessingEnvironment env, PrintWriter writer, TypeElement interface_decl) {
		for ( ExecutableElement method : Utils.getMethods(interface_decl) ) {
			if ( method.getAnnotation(Alternate.class) != null ) {
				continue;
			}

			generateReferencesFromParameters(writer, interface_decl, method);
		}
	}

	private void generateReferencesSource(ProcessingEnvironment env, Set<TypeElement> templates) throws IOException {
		PrintWriter writer = new PrintWriter(processingEnv.getFiler().createSourceFile("org.lwjgl.opengl." + REFERENCES_CLASS_NAME, processingEnv.getElementUtils().getPackageElement("org.lwjgl.opengl")).openWriter());
		writer.println("/* MACHINE GENERATED FILE, DO NOT EDIT */");
		writer.println();
		writer.println("package org.lwjgl.opengl;");
		writer.println();
		writer.println("class " + REFERENCES_CLASS_NAME + " extends BaseReferences {");
		writer.println("\t" + REFERENCES_CLASS_NAME + "(ContextCapabilities caps) {");
		writer.println("\t\tsuper(caps);");
		writer.println("\t}");
		for ( TypeElement interface_decl : templates ) {
			if ( interface_decl.getKind().isInterface() ) {
				generateReferencesFromMethods(env, writer, interface_decl);
			}
		}
		writer.println();
		writer.println("\tvoid copy(" + REFERENCES_CLASS_NAME + " " + REFERENCES_PARAMETER_NAME + ", int mask) {");
		writer.println("\t\tsuper.copy(" + REFERENCES_PARAMETER_NAME + ", mask);");
		writer.println("\t\tif ( (mask & GL11.GL_CLIENT_VERTEX_ARRAY_BIT) != 0 ) {");
		for ( TypeElement interface_decl : templates ) {
			if ( interface_decl.getKind().isInterface() ) {
				generateCopiesFromMethods(processingEnv, writer, interface_decl);
			}
		}
		writer.println("\t\t}");
		writer.println("\t}");
		writer.println("\tvoid clear() {");
		writer.println("\t\tsuper.clear();");
		for ( TypeElement interface_decl : templates ) {
			if ( interface_decl.getKind().isInterface() ) {
				generateClearsFromMethods(processingEnv, writer, interface_decl);
			}
		}
		writer.println("\t}");
		writer.println("}");
		writer.close();
	}

}
