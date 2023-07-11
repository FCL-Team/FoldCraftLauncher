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
package org.lwjgl.util.generator;

/**
 *
 * Various utility methods to the generator.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$ $Id$
 */
import org.lwjgl.PointerBuffer;
import org.lwjgl.PointerWrapper;
import org.lwjgl.util.generator.opengl.GLboolean;
import org.lwjgl.util.generator.opengl.GLchar;
import org.lwjgl.util.generator.opengl.GLcharARB;
import org.lwjgl.util.generator.opengl.GLreturn;

import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.regex.Pattern;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.ElementFilter;

public class Utils {

	public static final  String TYPEDEF_POSTFIX                 = "PROC";
	public static final  String FUNCTION_POINTER_VAR_NAME       = "function_pointer";
	public static final  String FUNCTION_POINTER_POSTFIX        = "_pointer";
	public static final  String CHECKS_CLASS_NAME               = "GLChecks";
	public static final  String CONTEXT_CAPS_CLASS_NAME         = "ContextCapabilities";
	public static final  String STUB_INITIALIZER_NAME           = "initNativeStubs";
	public static final  String BUFFER_OBJECT_METHOD_POSTFIX    = "BO";
	public static final  String BUFFER_OBJECT_PARAMETER_POSTFIX = "_buffer_offset";
	public static final  String RESULT_SIZE_NAME                = "result_size";
	public static final  String RESULT_VAR_NAME                 = "__result";
	public static final  String CACHED_BUFFER_LENGTH_NAME       = "length";
	public static final  String CACHED_BUFFER_NAME              = "old_buffer";
	private static final String OVERLOADED_METHOD_PREFIX        = "n";

	public static String getTypedefName(ExecutableElement method) {
		Alternate alt_annotation = method.getAnnotation(Alternate.class);
		return (alt_annotation == null ? method.getSimpleName() : alt_annotation.value()) + TYPEDEF_POSTFIX;
	}

	public static String getFunctionAddressName(TypeElement interface_decl, ExecutableElement method) {
		return getFunctionAddressName(interface_decl, method, false);
	}

	public static String getFunctionAddressName(TypeElement interface_decl, ExecutableElement method, boolean forceAlt) {
		final Alternate alt_annotation = method.getAnnotation(Alternate.class);

                /* Removed prefix so that we can identify reusable entry points, removed postfix because it's not needed and looks nicer.
                 String interfaceName = interface_decl.getSimpleName(); // If we add this back, we need to fix @Reuse (add a param for the template name)
                 if ( alt_annotation == null || (alt_annotation.nativeAlt() && !forceAlt) )
                 return interfaceName + "_" + method.getSimpleName() + FUNCTION_POINTER_POSTFIX;
                 else
                 return interfaceName + "_" + alt_annotation.value() + FUNCTION_POINTER_POSTFIX;
                 */
		if ( alt_annotation == null || (alt_annotation.nativeAlt() && !forceAlt) ) {
			return method.getSimpleName().toString();
		} else {
			return alt_annotation.value();
		}
	}

	public static boolean isFinal(Element d) {
		Extension extension_annotation = d.getAnnotation(Extension.class);
		return extension_annotation == null || extension_annotation.isFinal();
	}

	private static class AnnotationMirrorComparator implements Comparator<AnnotationMirror> {

		/**
		 * Sort annotations.
		 */
		@Override
		public int compare(AnnotationMirror a1, AnnotationMirror a2) {
			String n1 = a1.getAnnotationType().toString();
			String n2 = a2.getAnnotationType().toString();
			return n1.compareTo(n2);
		}

		public boolean equals(AnnotationMirror a1, AnnotationMirror a2) {
			return compare(a1, a2) == 0;
		}
	}

	public static List<AnnotationMirror> getSortedAnnotations(List<? extends AnnotationMirror> annotations) {
		List<AnnotationMirror> annotation_list = new ArrayList<AnnotationMirror>(annotations);
		Collections.sort(annotation_list, new AnnotationMirrorComparator());
		return annotation_list;
	}

	public static String getReferenceName(TypeElement interface_decl, ExecutableElement method, VariableElement param) {
		return interface_decl.getSimpleName() + "_" + method.getSimpleName() + "_" + param.getSimpleName();
	}

	public static boolean isAddressableType(TypeMirror type) {
		return isAddressableType(getJavaType(type));
	}

	public static boolean isAddressableType(Class type) {
		if ( type.isArray() ) {
			final Class component_type = type.getComponentType();
			return isAddressableTypeImpl(component_type) || PointerWrapper.class.isAssignableFrom(component_type);
		}
		return isAddressableTypeImpl(type);
	}

	private static boolean isAddressableTypeImpl(Class type) {
		return Buffer.class.isAssignableFrom(type) || PointerBuffer.class.isAssignableFrom(type) || CharSequence.class.isAssignableFrom(type);
	}

	public static Class getJavaType(TypeMirror type_mirror) {
		JavaTypeTranslator translator = new JavaTypeTranslator();
		type_mirror.accept((TypeVisitor)translator, null);
		return translator.getType();
	}

	private static boolean hasParameterMultipleTypes(VariableElement param) {
		int num_native_annotations = 0;
		for ( AnnotationMirror annotation : param.getAnnotationMirrors() ) {
			if ( NativeTypeTranslator.getAnnotation(annotation, NativeType.class) != null ) {
				num_native_annotations++;
			}
		}
		return num_native_annotations > 1;
	}

	public static boolean isParameterMultiTyped(VariableElement param) {
		boolean result = Buffer.class.equals(Utils.getJavaType(param.asType()));
		if ( !result && hasParameterMultipleTypes(param) ) {
			throw new RuntimeException(param + " not defined as java.nio.Buffer but has multiple types");
		}
		return result;
	}

	public static VariableElement findParameter(ExecutableElement method, String name) {
		for ( VariableElement param : method.getParameters() ) {
			if ( param.getSimpleName().toString().equals(name) ) {
				return param;
			}
		}
		throw new RuntimeException("Parameter " + name + " not found");
	}

	public static void printDocComment(PrintWriter writer, Element decl, ProcessingEnvironment pe) {
		final String overloadsComment;
		if ( (decl instanceof ExecutableElement) && decl.getAnnotation(Alternate.class) != null ) {
			overloadsComment = "Overloads " + decl.getAnnotation(Alternate.class).value() + ".";
		} else {
			overloadsComment = null;
		}

		String doc_comment = pe.getElementUtils().getDocComment(decl);
		if ( doc_comment != null ) {
			final String tab = (decl instanceof TypeElement) ? "" : "\t";
			writer.println(tab + "/**");

			if ( overloadsComment != null ) {
				writer.println("\t * " + overloadsComment);
				writer.println("\t * <p>");
			}

			final StringTokenizer doc_lines = new StringTokenizer(doc_comment, "\n", true);
			boolean lastWasNL = false;
			while ( doc_lines.hasMoreTokens() ) {
				final String t = doc_lines.nextToken();
				if ( "\n".equals(t) ) {
					if ( lastWasNL ) {
						writer.println(tab + " * <p>");
					}
					lastWasNL = true;
				} else {
					writer.println(tab + " * " + t);
					lastWasNL = false;
				}
			}

			writer.println(tab + " */");
		} else if ( overloadsComment != null ) {
			writer.println("\t/** " + overloadsComment + " */");
		}
	}

	public static AnnotationMirror getParameterAutoAnnotation(VariableElement param) {
		for ( AnnotationMirror annotation : param.getAnnotationMirrors() ) {
			if ( NativeTypeTranslator.getAnnotation(annotation, Auto.class) != null ) {
				return annotation;
			}
		}
		return null;
	}

	// DISABLED: We always generate indirect methods. (affects OpenAL only at the time of this change)
	public static boolean isMethodIndirect(boolean generate_error_checks, boolean context_specific, ExecutableElement method) {
	            /*
	             for (VariableElement param : method.getParameters()) {
                 if (isAddressableType(param.getType()) || getParameterAutoAnnotation(param) != null ||
                 param.getAnnotation(Constant.class) != null)
                 return true;
                 }
                 return hasMethodBufferObjectParameter(method) || method.getAnnotation(Code.class) != null ||
                 method.getAnnotation(CachedResult.class) != null ||
                 (generate_error_checks && method.getAnnotation(NoErrorCheck.class) == null) ||
                 context_specific;
                 */
		return true;
	}

	private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

	public static String getNativeQualifiedName(String qualified_name) {
		return DOT_PATTERN.matcher(qualified_name).replaceAll("_");
	}

	public static String getQualifiedNativeMethodName(String qualified_class_name, String method_name) {
		// Escape '_' in method name
		if ( method_name.indexOf('_') != -1 ) {
			method_name = method_name.replace("_", "_1");
		}

		return "Java_" + getNativeQualifiedName(qualified_class_name) + "_" + method_name;
	}

	public static String getQualifiedNativeMethodName(String qualified_class_name, ExecutableElement method, boolean generate_error_checks, boolean context_specific) {
		String method_name = getSimpleNativeMethodName(method, generate_error_checks, context_specific);
		return getQualifiedNativeMethodName(qualified_class_name, method_name);
	}

	public static VariableElement getResultParameter(ExecutableElement method) {
		VariableElement result_param = null;
		for ( VariableElement param : method.getParameters() ) {
			if ( param.getAnnotation(Result.class) != null ) {
				if ( result_param != null ) {
					throw new RuntimeException("Multiple parameters annotated with Result in method " + method);
				}
				result_param = param;
			}
		}
		return result_param;
	}

	public static TypeMirror getMethodReturnType(ExecutableElement method) {
		TypeMirror result_type;
		VariableElement result_param = getResultParameter(method);
		if ( result_param != null ) {
			result_type = result_param.asType();
		} else {
			result_type = method.getReturnType();
		}
		return result_type;
	}

	public static String getMethodReturnType(ExecutableElement method, GLreturn return_annotation, boolean buffer) {
		VariableElement return_param = null;
		for ( VariableElement param : method.getParameters() ) {
			if ( param.getSimpleName().toString().equals(return_annotation.value()) ) {
				return_param = param;
				break;
			}
		}
		if ( return_param == null ) {
			throw new RuntimeException("The @GLreturn parameter \"" + return_annotation.value() + "\" could not be found in method: " + method);
		}

		TypeKind kind = NativeTypeTranslator.getPrimitiveKindFromBufferClass(Utils.getJavaType(return_param.asType()));
		if ( return_param.getAnnotation(GLboolean.class) != null ) {
			kind = TypeKind.BOOLEAN;
		}

		if ( kind == TypeKind.BYTE && (return_param.getAnnotation(GLchar.class) != null || return_param.getAnnotation(GLcharARB.class) != null) ) {
			return "String";
		} else {
			final String type = JavaTypeTranslator.getPrimitiveClassFromKind(kind).getName();
			return buffer ? Character.toUpperCase(type.charAt(0)) + type.substring(1) : type;
		}
	}

	public static boolean needResultSize(ExecutableElement method) {
		return getNIOBufferType(getMethodReturnType(method)) != null && method.getAnnotation(AutoSize.class) == null;
	}

	public static void printExtraCallArguments(PrintWriter writer, ExecutableElement method, String size_parameter_name) {
		writer.print(size_parameter_name);
		if ( method.getAnnotation(CachedResult.class) != null ) {
			writer.print(", " + CACHED_BUFFER_NAME);
		}
	}

	private static String getClassName(TypeElement interface_decl, String opengl_name) {
		Extension extension_annotation = interface_decl.getAnnotation(Extension.class);
		if ( extension_annotation != null && !"".equals(extension_annotation.className()) ) {
			return extension_annotation.className();
		}
		StringBuilder result = new StringBuilder();
		for ( int i = 0; i < opengl_name.length(); i++ ) {
			int ch = opengl_name.codePointAt(i);
			if ( ch == '_' ) {
				i++;
				result.appendCodePoint(Character.toUpperCase(opengl_name.codePointAt(i)));
			} else {
				result.appendCodePoint(ch);
			}
		}
		return result.toString();
	}

	public static boolean hasMethodBufferObjectParameter(ExecutableElement method) {
		for ( VariableElement param : method.getParameters() ) {
			if ( param.getAnnotation(BufferObject.class) != null ) {
				return true;
			}
		}
		return false;
	}

	public static String getQualifiedClassName(TypeElement interface_decl) {
		return interface_decl.getEnclosingElement().asType().toString() + "." + getSimpleClassName(interface_decl);
	}

	public static String getSimpleClassName(TypeElement interface_decl) {
		return getClassName(interface_decl, interface_decl.getSimpleName().toString());
	}

	public static Class<?> getNIOBufferType(TypeMirror t) {
		Class<?> param_type = getJavaType(t);
		if ( Buffer.class.isAssignableFrom(param_type) ) {
			return param_type;
		} else if ( param_type == CharSequence.class || param_type == CharSequence[].class || param_type == PointerBuffer.class ) {
			return ByteBuffer.class;
		} else {
			return null;
		}
	}

	public static String getSimpleNativeMethodName(ExecutableElement method, boolean generate_error_checks, boolean context_specific) {
		String method_name;
		Alternate alt_annotation = method.getAnnotation(Alternate.class);
		method_name = alt_annotation == null || alt_annotation.nativeAlt() ? method.getSimpleName().toString() : alt_annotation.value();
		if ( isMethodIndirect(generate_error_checks, context_specific, method) ) {
			method_name = OVERLOADED_METHOD_PREFIX + method_name;
		}
		return method_name;
	}

	static boolean isReturnParameter(ExecutableElement method, VariableElement param) {
		GLreturn string_annotation = method.getAnnotation(GLreturn.class);
		if ( string_annotation == null || !string_annotation.value().equals(param.getSimpleName().toString()) ) {
			return false;
		}

		if ( param.getAnnotation(OutParameter.class) == null ) {
			throw new RuntimeException("The parameter specified in @GLreturn is not annotated with @OutParameter in method: " + method);
		}

		if ( param.getAnnotation(Check.class) != null ) {
			throw new RuntimeException("The parameter specified in @GLreturn is annotated with @Check in method: " + method);
		}

		if ( param.getAnnotation(GLchar.class) != null && Utils.getJavaType(param.asType()).equals(ByteBuffer.class) && string_annotation.maxLength().length() == 0 ) {
			throw new RuntimeException("The @GLreturn annotation is missing a maxLength parameter in method: " + method);
		}

		return true;
	}

	static String getStringOffset(ExecutableElement method, VariableElement param) {
		String offset = null;
		for ( VariableElement p : method.getParameters() ) {
			if ( param != null && p.getSimpleName().equals(param.getSimpleName()) ) {
				break;
			}

			if ( p.getAnnotation(NullTerminated.class) != null ) {
				continue;
			}

			final Class type = Utils.getJavaType(p.asType());
			if ( type.equals(CharSequence.class) ) {
				if ( offset == null ) {
					offset = p.getSimpleName() + ".length()";
				} else {
					offset += " + " + p.getSimpleName() + ".length()";
				}
				//if ( p.getAnnotation(NullTerminated.class) != null ) offset += " + 1";
			} else if ( type.equals(CharSequence[].class) ) {
				if ( offset == null ) {
					offset = "APIUtil.getTotalLength(" + p.getSimpleName() + ")";
				} else {
					offset += " + APIUtil.getTotalLength(" + p.getSimpleName() + ")";
				}
				//if ( p.getAnnotation(NullTerminated.class) != null ) offset += " + " + p.getSimpleName() + ".length";
			}

		}
		return offset;
	}

	static void printGLReturnPre(PrintWriter writer, ExecutableElement method, GLreturn return_annotation, TypeMap type_map) {
		final String return_type = getMethodReturnType(method, return_annotation, true);

		if ( "String".equals(return_type) ) {
			if ( !return_annotation.forceMaxLength() ) {
				writer.println("IntBuffer " + return_annotation.value() + "_length = APIUtil.getLengths(" + type_map.getAPIUtilParam(false) + ");");
				writer.print("\t\t");
			}
			writer.print("ByteBuffer " + return_annotation.value() + " = APIUtil.getBufferByte(" + type_map.getAPIUtilParam(true) + return_annotation.maxLength());
	        /*
             Params that use the return buffer will advance its position while filling it. When we return, the position will be
             at the right spot for grabbing the returned string bytes. We only have to make sure that the original buffer was
             large enough to hold everything, so that no re-allocations happen while filling.
             */
			final String offset = getStringOffset(method, null);
			if ( offset != null ) {
				writer.print(" + " + offset);
			}
			writer.println(");");
		} else {
			final String buffer_type = "Boolean".equals(return_type) ? "Byte" : return_type;
			writer.print(buffer_type + "Buffer " + return_annotation.value() + " = APIUtil.getBuffer" + buffer_type + "(" + type_map.getAPIUtilParam(false));
			if ( "Byte".equals(buffer_type) ) {
				writer.print((type_map.getAPIUtilParam(false).length() > 0 ? ", " : "") + "1");
			}
			writer.println(");");
		}

		final Code code_annotation = method.getAnnotation(Code.class);
		if ( code_annotation != null && code_annotation.tryBlock() ) {
			writer.println("\t\ttry {");
			writer.print("\t\t\t");
		} else {
			writer.print("\t\t");
		}
	}

	static void printGLReturnPost(PrintWriter writer, ExecutableElement method, GLreturn return_annotation, TypeMap type_map) {
		final String return_type = getMethodReturnType(method, return_annotation, true);

		if ( "String".equals(return_type) ) {
			writer.print("\t\t" + return_annotation.value() + ".limit(");
			final String offset = getStringOffset(method, null);
			if ( offset != null ) {
				writer.print(offset + " + ");
			}
			if ( return_annotation.forceMaxLength() ) {
				writer.print(return_annotation.maxLength());
			} else {
				writer.print(return_annotation.value() + "_length.get(0)");
			}
			writer.println(");");
			writer.println("\t\treturn APIUtil.getString(" + type_map.getAPIUtilParam(true) + return_annotation.value() + ");");
		} else {
			writer.print("\t\treturn " + return_annotation.value() + ".get(0)");
			if ( "Boolean".equals(return_type) ) {
				writer.print(" == 1");
			}
			writer.println(";");
		}
	}

	public static Collection<VariableElement> getFields(TypeElement d) {
		return ElementFilter.fieldsIn(new LinkedHashSet<Element>(d.getEnclosedElements()));
	}

	public static Collection<ExecutableElement> getMethods(TypeElement d) {
		return ElementFilter.methodsIn(new LinkedHashSet<Element>(d.getEnclosedElements()));
	}

}
