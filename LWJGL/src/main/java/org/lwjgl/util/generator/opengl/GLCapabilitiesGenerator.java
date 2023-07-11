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

import org.lwjgl.util.generator.*;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Generator visitor for the context capabilities generator tool
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 3355 $
 *          $Id: ContextCapabilitiesGenerator.java 3355 2010-05-27 22:56:29Z spasi $
 */
public class GLCapabilitiesGenerator {

	private static final String STUBS_LOADED_NAME           = "loaded_stubs";
	private static final String ALL_INIT_METHOD_NAME        = "initAllStubs";
	private static final String POINTER_INITIALIZER_POSTFIX = "_initNativeFunctionAddresses";
	private static final String CACHED_EXTS_VAR_NAME        = "supported_extensions";
	private static final String PROFILE_MASK_VAR_NAME       = "profileMask";
	private static final String EXTENSION_PREFIX            = "GL_";
	private static final String CORE_PREFIX                 = "Open";

	public static void generateClassPrologue(PrintWriter writer, boolean context_specific, boolean generate_error_checks) {
		writer.println("public class " + Utils.CONTEXT_CAPS_CLASS_NAME + " {");
		writer.println("\tstatic final boolean DEBUG = " + Boolean.toString(generate_error_checks) + ";");
		writer.println("\tfinal APIUtil util = new APIUtil();");
		writer.println("\tfinal StateTracker tracker = new StateTracker();");
		writer.println();
		if ( !context_specific ) {
			writer.println("\tprivate static boolean " + STUBS_LOADED_NAME + " = false;");
		}
	}

	public static void generateInitializerPrologue(PrintWriter writer) {
		writer.println("\t" + Utils.CONTEXT_CAPS_CLASS_NAME + "(boolean forwardCompatible) throws LWJGLException {");
		writer.println("\t\tSet<String> " + CACHED_EXTS_VAR_NAME + " = " + ALL_INIT_METHOD_NAME + "(forwardCompatible);");
	}

	private static String translateFieldName(String interface_name) {
		if ( interface_name.startsWith("GL") )
			return CORE_PREFIX + interface_name;
		else
			return EXTENSION_PREFIX + interface_name;
	}

	public static void generateSuperClassAdds(PrintWriter writer, TypeElement d, ProcessingEnvironment env) {
		List<? extends TypeMirror> super_interfaces = d.getInterfaces();
		if ( super_interfaces.size() > 1 )
			throw new RuntimeException(d + " extends more than one other interface");
		if ( super_interfaces.size() == 1 ) {
			TypeMirror super_interface = super_interfaces.iterator().next();
			writer.print("\t\tif (" + CACHED_EXTS_VAR_NAME + ".contains(\"");
			writer.println(translateFieldName(d.getSimpleName().toString()) + "\"))");
			writer.print("\t\t\t");
			generateAddExtension(writer, env.getElementUtils().getTypeElement(super_interface.toString()));
		}
	}

	public static void generateInitializer(PrintWriter writer, TypeElement d, ProcessingEnvironment env) {
		String translated_field_name = translateFieldName(d.getSimpleName().toString());
		writer.print("\t\tthis." + translated_field_name + " = ");
		writer.print(CACHED_EXTS_VAR_NAME + ".contains(\"");
		writer.print(translated_field_name + "\")");
		List<? extends TypeMirror> super_interfaces = d.getInterfaces();
		if ( super_interfaces.size() > 1 )
			throw new RuntimeException(d + " extends more than one other interface");
		if ( super_interfaces.size() == 1 ) {
			TypeMirror super_interface = super_interfaces.iterator().next();
			writer.println();
			writer.print("\t\t\t&& " + CACHED_EXTS_VAR_NAME + ".contains(\"");
			writer.print(translateFieldName(env.getElementUtils().getTypeElement(super_interface.toString()).getSimpleName().toString()) + "\")");
		}
		Alias alias_annotation = d.getAnnotation(Alias.class);
		if ( alias_annotation != null ) {
			writer.println();
			writer.print("\t\t\t|| " + CACHED_EXTS_VAR_NAME + ".contains(\"");
			writer.print(translateFieldName(alias_annotation.value()) + "\")");
		}
		writer.println(";");
	}

	private static String getAddressesInitializerName(String class_name) {
		return class_name + POINTER_INITIALIZER_POSTFIX;
	}

	public static void generateInitStubsPrologue(PrintWriter writer, boolean context_specific) {
		writer.println("\tprivate Set<String> " + ALL_INIT_METHOD_NAME + "(boolean forwardCompatible) throws LWJGLException {");

		// Load the basic pointers we need to detect OpenGL version and supported extensions.
		writer.println("\t\tglGetError = GLContext.getFunctionAddress(\"glGetError\");");
		writer.println("\t\tglGetString = GLContext.getFunctionAddress(\"glGetString\");");

		// Initialize GL11.glGetIntegerv and GL30.glGetStringi here, in case we have created an OpenGL 3.0 context.
		// (they will be used in GLContext.getSupportedExtensions)
		writer.println("\t\tglGetIntegerv = GLContext.getFunctionAddress(\"glGetIntegerv\");");
		writer.println("\t\tglGetStringi = GLContext.getFunctionAddress(\"glGetStringi\");");

		// Get the supported extensions set.
		writer.println("\t\tGLContext.setCapabilities(this);");
		writer.println("\t\tSet<String> " + CACHED_EXTS_VAR_NAME + " = new HashSet<String>(256);");
		writer.println("\t\tint " + PROFILE_MASK_VAR_NAME + " = GLContext.getSupportedExtensions(" + CACHED_EXTS_VAR_NAME + ");");

		// Force forward compatible mode when OpenGL version is 3.1 or higher and ARB_compatibility is not available.
		writer.println("\t\tif ( supported_extensions.contains(\"OpenGL31\") && !(supported_extensions.contains(\"GL_ARB_compatibility\") || (profileMask & GL32.GL_CONTEXT_COMPATIBILITY_PROFILE_BIT) != 0) )");
		writer.println("\t\t\tforwardCompatible = true;");

		if ( !context_specific ) {
			writer.println("\t\tif (" + STUBS_LOADED_NAME + ")");
			writer.println("\t\t\treturn GLContext.getSupportedExtensions();");
			writer.println("\t\torg.lwjgl.opengl.GL11." + Utils.STUB_INITIALIZER_NAME + "();");
		} else {
			writer.println("\t\tif (!" + getAddressesInitializerName("GL11") + "(forwardCompatible))");
			writer.println("\t\t\tthrow new LWJGLException(\"GL11 not supported\");");
		}
	}

	public static void generateInitStubsEpilogue(PrintWriter writer, boolean context_specific) {
		if ( !context_specific ) {
			writer.println("\t\t" + STUBS_LOADED_NAME + " = true;");
		}
		writer.println("\t\treturn " + CACHED_EXTS_VAR_NAME + ";");
		writer.println("\t}");
	}

	public static void generateUnloadStubs(ProcessingEnvironment env, PrintWriter writer, TypeElement d) {
		if ( Utils.getMethods(d).size() > 0 ) {
			writer.print("\t\tGLContext.resetNativeStubs(" + Utils.getSimpleClassName(d));
			writer.println(".class);");
		}
	}

	public static void generateInitStubs(ProcessingEnvironment env, PrintWriter writer, TypeElement d, boolean context_specific) {
		if ( Utils.getMethods(d).size() > 0 ) {
			if ( context_specific ) {
				final Alias alias_annotation = d.getAnnotation(Alias.class);

				if ( d.getAnnotation(ForceInit.class) != null )
					writer.println("\t\t" + CACHED_EXTS_VAR_NAME + ".add(\"" + translateFieldName(d.getSimpleName().toString()) + "\");");
				writer.print("\t\tif (");
				if ( alias_annotation != null )
					writer.print("(");
				writer.print(CACHED_EXTS_VAR_NAME + ".contains(\"");
				writer.print(translateFieldName(d.getSimpleName().toString()) + "\")");
				if ( alias_annotation != null ) {
					writer.print(" || " + CACHED_EXTS_VAR_NAME + ".contains(\"");
					writer.print(translateFieldName(alias_annotation.value()) + "\"))");
				}
				writer.print(" && !" + getAddressesInitializerName(d.getSimpleName().toString()) + "(");
				if ( d.getAnnotation(DeprecatedGL.class) != null )
					writer.print("forwardCompatible");
				if ( d.getAnnotation(Dependent.class) != null ) {
					if ( d.getAnnotation(DeprecatedGL.class) != null )
						writer.print(",");
					writer.print("supported_extensions");
				}
				if ( alias_annotation != null ) {
					writer.println(")) {");
					writer.print("\t\t\tremove(" + CACHED_EXTS_VAR_NAME + ", \"");
					writer.println(translateFieldName(alias_annotation.value()) + "\");");
				} else
					writer.println("))");
				writer.print("\t\t\tremove(" + CACHED_EXTS_VAR_NAME + ", \"");
				writer.println(translateFieldName(d.getSimpleName().toString()) + "\");");
				if ( alias_annotation != null )
					writer.println("\t\t}");
			} else {
				writer.print("\t\tGLContext." + Utils.STUB_INITIALIZER_NAME + "(" + Utils.getSimpleClassName(d));
				writer.println(".class, " + CACHED_EXTS_VAR_NAME + ", \"" + translateFieldName(d.getSimpleName().toString()) + "\");");
			}
		}
	}

	private static void generateAddExtension(PrintWriter writer, TypeElement d) {
		writer.print(CACHED_EXTS_VAR_NAME + ".add(\"");
		writer.println(translateFieldName(d.getSimpleName().toString()) + "\");");
	}

	public static void generateAddressesInitializers(ProcessingEnvironment env, PrintWriter writer, TypeElement d) {
		Iterator<? extends ExecutableElement> methods = Utils.getMethods(d).iterator();
		if ( !methods.hasNext() )
			return;

		writer.print("\tprivate boolean " + getAddressesInitializerName(d.getSimpleName().toString()) + "(");

		boolean optional;
		boolean deprecated = d.getAnnotation(DeprecatedGL.class) != null;
		Dependent dependent = d.getAnnotation(Dependent.class);
		if ( deprecated )
			writer.print("boolean forwardCompatible");
		if ( dependent != null ) {
			if ( deprecated )
				writer.print(",");
			writer.print("Set<String> supported_extensions");
		}

		Alias alias_annotation = d.getAnnotation(Alias.class);
		boolean aliased = alias_annotation != null && alias_annotation.postfix().length() > 0;

		writer.println(") {");
		writer.println("\t\treturn ");

		boolean first = true;
		while ( methods.hasNext() ) {
			ExecutableElement method = methods.next();
			if ( method.getAnnotation(Alternate.class) != null )
				continue;

			if ( !first )
				writer.println(" &");
			else
				first = false;

			optional = method.getAnnotation(Optional.class) != null;
			deprecated = method.getAnnotation(DeprecatedGL.class) != null;
			dependent = method.getAnnotation(Dependent.class);

			writer.print("\t\t\t(");
			if ( optional )
				writer.print('(');
			if ( deprecated )
				writer.print("forwardCompatible || ");
			if ( dependent != null ) {
				if ( dependent.value().indexOf(',') == -1 )
					writer.print("!supported_extensions.contains(\"" + dependent.value() + "\") || ");
				else {
					writer.print("!(false");
					for ( String extension : dependent.value().split(",") )
						writer.print(" || supported_extensions.contains(\"" + extension + "\")");
					writer.print(") || ");
				}
			}
			if ( deprecated || dependent != null )
				writer.print('(');
			writer.print(Utils.getFunctionAddressName(d, method) + " = ");
			PlatformDependent platform_dependent = method.getAnnotation(PlatformDependent.class);
			if ( platform_dependent != null ) {
				EnumSet<Platform> platform_set = EnumSet.copyOf(Arrays.asList(platform_dependent.value()));
				writer.print("GLContext.getPlatformSpecificFunctionAddress(\"");
				writer.print(Platform.ALL.getPrefix() + "\", ");
				writer.print("new String[]{");
				Iterator<Platform> platforms = platform_set.iterator();
				while ( platforms.hasNext() ) {
					writer.print("\"" + platforms.next().getOSPrefix() + "\"");
					if ( platforms.hasNext() )
						writer.print(", ");
				}
				writer.print("}, new String[]{");
				platforms = platform_set.iterator();
				while ( platforms.hasNext() ) {
					writer.print("\"" + platforms.next().getPrefix() + "\"");
					if ( platforms.hasNext() )
						writer.print(", ");
				}
				writer.print("}, ");
			} else if ( aliased ) {
				writer.print("GLContext.getFunctionAddress(new String[] {\"" + method.getSimpleName() + "\",\"" + method.getSimpleName() + alias_annotation.postfix() + "\"})) != 0");
			} else
				writer.print("GLContext.getFunctionAddress(");
			if ( !aliased )
				writer.print("\"" + method.getSimpleName() + "\")) != 0");
			if ( deprecated || dependent != null )
				writer.print(')');
			if ( optional )
				writer.print(" || true)");
		}
		writer.println(";");
		writer.println("\t}");
		writer.println();
	}

	public static void generateSymbolAddresses(ProcessingEnvironment env, PrintWriter writer, TypeElement d) {
		boolean first = true;
		for ( final ExecutableElement method : Utils.getMethods(d) ) {
			if ( method.getAnnotation(Alternate.class) != null || method.getAnnotation(Reuse.class) != null )
				continue;

			if ( first ) {
				writer.println("\t// " + d.getSimpleName());
				first = false;
			}
			writer.println("\tlong " + Utils.getFunctionAddressName(d, method) + ";");
		}
	}

	public static void generateField(PrintWriter writer, TypeElement d) {
		writer.println("\tpublic final boolean " + translateFieldName(d.getSimpleName().toString()) + ";");
	}
}