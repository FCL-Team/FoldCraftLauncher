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
 * This class generates the methods in the generated java source files.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$ $Id$
 */
import org.lwjgl.PointerBuffer;
import org.lwjgl.util.generator.opengl.GLreturn;

import java.io.PrintWriter;
import java.nio.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class JavaMethodsGenerator {

	private static final String SAVED_PARAMETER_POSTFIX = "_saved";

	public static void generateMethodsJava(ProcessingEnvironment env, TypeMap type_map, PrintWriter writer, TypeElement interface_decl, boolean generate_error_checks, boolean context_specific) {
		for ( ExecutableElement method : Utils.getMethods(interface_decl) ) {
			generateMethodJava(env, type_map, writer, interface_decl, method, generate_error_checks, context_specific);
		}
	}

	/**
	 * TODO : fix info multi-type methods print.
	 */
	private static void generateMethodJava(ProcessingEnvironment env, TypeMap type_map, PrintWriter writer, TypeElement interface_decl, ExecutableElement method, boolean generate_error_checks, boolean context_specific) {
		writer.println();
		if ( Utils.isMethodIndirect(generate_error_checks, context_specific, method) ) {
			if ( method.getAnnotation(GenerateAutos.class) != null ) {
				printMethodWithMultiType(env, type_map, writer, interface_decl, method, TypeInfo.getDefaultTypeInfoMap(method), Mode.AUTOS, generate_error_checks, context_specific);
			}
			Collection<Map<VariableElement, TypeInfo>> cross_product = TypeInfo.getTypeInfoCrossProduct(type_map, method);
			for ( Map<VariableElement, TypeInfo> typeinfos_instance : cross_product ) {
				printMethodWithMultiType(env, type_map, writer, interface_decl, method, typeinfos_instance, Mode.NORMAL, generate_error_checks, context_specific);
			}
		}
		if ( method.getAnnotation(CachedResult.class) != null && !method.getAnnotation(CachedResult.class).isRange() ) {
			printMethodWithMultiType(env, type_map, writer, interface_decl, method, TypeInfo.getDefaultTypeInfoMap(method), Mode.CACHEDRESULT, generate_error_checks, context_specific);
		}

		Reuse reuse_annotation = method.getAnnotation(Reuse.class);
		Alternate alt_annotation = method.getAnnotation(Alternate.class);
		if ( alt_annotation == null || (alt_annotation.nativeAlt() && !alt_annotation.skipNative()) ) {
			if ( alt_annotation != null && method.getSimpleName().toString().equals(alt_annotation.value()) ) {
				throw new RuntimeException("An alternate function with native code should have a different name than the main function.");
			}

			if ( reuse_annotation == null ) {
				printJavaNativeStub(env, writer, method, Mode.NORMAL, generate_error_checks, context_specific);
			}

			if ( Utils.hasMethodBufferObjectParameter(method) ) {
				printMethodWithMultiType(env, type_map, writer, interface_decl, method, TypeInfo.getDefaultTypeInfoMap(method), Mode.BUFFEROBJECT, generate_error_checks, context_specific);
				if ( reuse_annotation == null ) {
					printJavaNativeStub(env, writer, method, Mode.BUFFEROBJECT, generate_error_checks, context_specific);
				}
			}
		}
	}

	private static void printJavaNativeStub(ProcessingEnvironment env, PrintWriter writer, ExecutableElement method, Mode mode, boolean generate_error_checks, boolean context_specific) {
		if ( Utils.isMethodIndirect(generate_error_checks, context_specific, method) ) {
			writer.print("\tstatic native ");
		} else {
			Utils.printDocComment(writer, method, env);
			writer.print("\tpublic static native ");
		}
		writer.print(getResultType(method, true));
		writer.print(" " + Utils.getSimpleNativeMethodName(method, generate_error_checks, context_specific));
		if ( mode == Mode.BUFFEROBJECT ) {
			writer.print(Utils.BUFFER_OBJECT_METHOD_POSTFIX);
		}
		writer.print("(");
		boolean first_parameter = generateParametersJava(writer, method, TypeInfo.getDefaultTypeInfoMap(method), true, true, mode);
		if ( context_specific ) {
			if ( !first_parameter ) {
				writer.print(", ");
			}
			writer.print("long " + Utils.FUNCTION_POINTER_VAR_NAME);
		}
		writer.println(");");
	}

	private static boolean generateParametersJava(PrintWriter writer, ExecutableElement method, Map<VariableElement, TypeInfo> typeinfos_instance, boolean native_stub, final boolean printTypes, Mode mode) {
		boolean first_parameter = true;
		for ( VariableElement param : method.getParameters() ) {
			if ( native_stub && (param.getAnnotation(Helper.class) != null && !param.getAnnotation(Helper.class).passToNative()) ) {
				continue;
			}
			final Constant constant_annotation = param.getAnnotation(Constant.class);
			if ( constant_annotation != null && constant_annotation.isNative() ) {
				continue;
			}
			AnnotationMirror auto_annotation_mirror = Utils.getParameterAutoAnnotation(param);
			boolean hide_auto_parameter = mode == Mode.NORMAL && !native_stub && auto_annotation_mirror != null;
			if ( hide_auto_parameter ) {
				AutoType auto_type_annotation = param.getAnnotation(AutoType.class);
				if ( auto_type_annotation != null ) {
					VariableElement auto_parameter = Utils.findParameter(method, auto_type_annotation.value());
					TypeInfo auto_param_type_info = typeinfos_instance.get(auto_parameter);
					if ( auto_param_type_info.getSignedness() == Signedness.BOTH ) {
						if ( !first_parameter ) {
							writer.print(", ");
						}
						first_parameter = false;
						if ( printTypes ) {
							writer.print("boolean ");
						}
						writer.print(TypeInfo.UNSIGNED_PARAMETER_NAME);
					}
				}
			} else if ( param.getAnnotation(Result.class) == null
			            && (native_stub || ((param.getAnnotation(Constant.class) == null || param.getAnnotation(Constant.class).keepParam()) && !Utils.isReturnParameter(method, param)))
			            && (mode != Mode.AUTOS || getAutoTypeParameter(method, param) == null) ) {
				first_parameter = generateParameterJava(writer, param, typeinfos_instance.get(param), native_stub, printTypes, first_parameter, mode);
			}
		}
		CachedResult cached_result_annotation = method.getAnnotation(CachedResult.class);
		TypeMirror result_type = Utils.getMethodReturnType(method);
		if ( (native_stub && Utils.getNIOBufferType(result_type) != null) || Utils.needResultSize(method) ) {
			AutoSize auto_size_annotation = method.getAnnotation(AutoSize.class);
			if ( auto_size_annotation == null || !auto_size_annotation.isNative() ) {
				if ( cached_result_annotation == null || !cached_result_annotation.isRange() ) {
					if ( !first_parameter ) {
						writer.print(", ");
					}
					first_parameter = false;
					if ( printTypes ) {
						writer.print("long ");
					}
					writer.print(Utils.RESULT_SIZE_NAME);
				}
			}
		}
		if ( cached_result_annotation != null ) {
			if ( !first_parameter ) {
				writer.print(", ");
			}

			if ( mode == Mode.CACHEDRESULT ) {
				if ( printTypes ) {
					writer.print("long ");
				}
				writer.print(Utils.CACHED_BUFFER_LENGTH_NAME + ", ");
			}

			first_parameter = false;
			if ( printTypes ) {
				writer.print(getResultType(method, native_stub));
			}
			writer.print(" " + Utils.CACHED_BUFFER_NAME);
		}
		return first_parameter;
	}

	private static boolean generateParameterJava(PrintWriter writer, VariableElement param, TypeInfo type_info, boolean native_stub, final boolean printTypes, boolean first_parameter, Mode mode) {
		Class buffer_type = Utils.getNIOBufferType(param.asType());
		if ( !first_parameter ) {
			writer.print(", ");
		}
		BufferObject bo_annotation = param.getAnnotation(BufferObject.class);
		if ( bo_annotation != null && mode == Mode.BUFFEROBJECT ) {
			if ( buffer_type == null ) {
				throw new RuntimeException("type of " + param + " is not a nio Buffer parameter but is annotated as buffer object");
			}
			if ( printTypes ) {
				writer.print("long ");
			}
			writer.print(param.getSimpleName() + Utils.BUFFER_OBJECT_PARAMETER_POSTFIX);
		} else {
			if ( native_stub && param.getAnnotation(PointerWrapper.class) != null ) {
				writer.print("long ");
			} else {
				Class type = type_info.getType();
				if ( native_stub && (type == CharSequence.class || type == CharSequence[].class || type == PointerBuffer.class || Buffer.class.isAssignableFrom(type)) ) {
					writer.print("long ");
				} else if ( printTypes ) {
					writer.print(type.getSimpleName() + " ");
				}
			}
			AutoSize auto_size_annotation = param.getAnnotation(AutoSize.class);
			if ( auto_size_annotation != null ) {
				writer.print(auto_size_annotation.value() + "_");
			}
			writer.print(param.getSimpleName());
		}
		return false;
	}

	private static void printBufferObjectCheck(PrintWriter writer, BufferKind kind, Mode mode, boolean context_specific) {
		String bo_check_method_name = kind.toString();
		writer.print("\t\t" + Utils.CHECKS_CLASS_NAME + ".ensure" + bo_check_method_name);
		if ( mode == Mode.BUFFEROBJECT ) {
			writer.print("enabled");
		} else {
			writer.print("disabled");
		}

		if ( context_specific ) {
			writer.println("(caps);");
		} else {
			writer.println("();");
		}
	}

	private static void printBufferObjectChecks(PrintWriter writer, ExecutableElement method, Mode mode, boolean context_specific) {
		EnumSet<BufferKind> check_set = EnumSet.noneOf(BufferKind.class);
		for ( VariableElement param : method.getParameters() ) {
			BufferObject bo_annotation = param.getAnnotation(BufferObject.class);
			if ( bo_annotation != null ) {
				check_set.add(bo_annotation.value());
			}
		}
		for ( BufferKind kind : check_set ) {
			printBufferObjectCheck(writer, kind, mode, context_specific);
		}
	}

	private static void printMethodWithMultiType(ProcessingEnvironment env, TypeMap type_map, PrintWriter writer, TypeElement interface_decl, ExecutableElement method, Map<VariableElement, TypeInfo> typeinfos_instance, Mode mode, boolean generate_error_checks, boolean context_specific) {
		Utils.printDocComment(writer, method, env);
		if ( method.getAnnotation(Deprecated.class) != null ) {
			writer.println("\t@Deprecated");
		}
		if ( interface_decl.getAnnotation(Private.class) == null && method.getAnnotation(Private.class) == null ) {
			writer.print("\tpublic static ");
		} else {
			writer.print("\tstatic ");
		}
		writer.print(getResultType(method, false));
		StripPostfix strip_annotation = method.getAnnotation(StripPostfix.class);
		String method_name;
		Alternate alt_annotation = method.getAnnotation(Alternate.class);
		method_name = alt_annotation == null || alt_annotation.javaAlt() ? method.getSimpleName().toString() : alt_annotation.value();
		if ( strip_annotation != null && mode == Mode.NORMAL ) {
			method_name = getPostfixStrippedName(type_map, interface_decl, method);
		}
		writer.print(" " + method_name + "(");
		generateParametersJava(writer, method, typeinfos_instance, false, true, mode);
		writer.println(") {");

		final TypeMirror result_type = Utils.getMethodReturnType(method);
		boolean has_result = !result_type.equals(env.getTypeUtils().getNoType(TypeKind.VOID));

		final Reuse reuse_annotation = method.getAnnotation(Reuse.class);
		if ( reuse_annotation != null ) {
			writer.print("\t\t");
			if ( has_result || method.getAnnotation(GLreturn.class) != null ) {
				writer.print("return ");
			}

			writer.print(reuse_annotation.value() + "." + (reuse_annotation.method().length() > 0 ? reuse_annotation.method() : method_name) + "(");
			generateParametersJava(writer, method, typeinfos_instance, false, false, mode);
			writer.println(");\n\t}");
			return;
		}

		if ( context_specific ) {
			type_map.printCapabilitiesInit(writer);
			writer.print("\t\tlong " + Utils.FUNCTION_POINTER_VAR_NAME + " = " + type_map.getCapabilities() + ".");
			writer.println(Utils.getFunctionAddressName(interface_decl, method, true) + ";");
			writer.print("\t\tBufferChecks.checkFunctionAddress(");
			writer.println(Utils.FUNCTION_POINTER_VAR_NAME + ");");
		}
		final Code code_annotation = method.getAnnotation(Code.class);
		if ( code_annotation != null && code_annotation.value().length() > 0 ) {
			writer.println(code_annotation.value());
		}
		printBufferObjectChecks(writer, method, mode, context_specific);
		printParameterChecks(writer, method, typeinfos_instance, mode, generate_error_checks);
		printParameterCaching(writer, interface_decl, method, mode, context_specific);

		if ( code_annotation != null && code_annotation.javaBeforeNative().length() > 0 ) {
			writer.println(code_annotation.javaBeforeNative());
		}
		writer.print("\t\t");

		final PointerWrapper pointer_wrapper_annotation = method.getAnnotation(PointerWrapper.class);
		if ( has_result ) {
			writer.print(getResultType(method, false) + " " + Utils.RESULT_VAR_NAME);

			if ( code_annotation != null && code_annotation.tryBlock() ) {
				writer.print(" = " + getDefaultResultValue(method));
				writer.println(";\n\t\ttry {");
				writer.print("\t\t\t" + Utils.RESULT_VAR_NAME);
			}

			writer.print(" = ");
			if ( pointer_wrapper_annotation != null ) {
				if ( pointer_wrapper_annotation.factory().length() > 0 ) {
					writer.print(pointer_wrapper_annotation.factory() + "(");
				} else {
					writer.print("new " + getResultType(method, false) + "(");
				}
			}
		} else if ( method.getAnnotation(GLreturn.class) != null ) {
			has_result = true;
			Utils.printGLReturnPre(writer, method, method.getAnnotation(GLreturn.class), type_map);
		}
		writer.print(Utils.getSimpleNativeMethodName(method, generate_error_checks, context_specific));
		if ( mode == Mode.BUFFEROBJECT ) {
			writer.print(Utils.BUFFER_OBJECT_METHOD_POSTFIX);
		}
		writer.print("(");
		boolean first_parameter = printMethodCallArguments(writer, method, typeinfos_instance, mode, type_map);
		if ( context_specific ) {
			if ( !first_parameter ) {
				writer.print(", ");
			}
			writer.print(Utils.FUNCTION_POINTER_VAR_NAME);
		}
		if ( has_result && pointer_wrapper_annotation != null ) {
			writer.print(")");
			if ( pointer_wrapper_annotation.params().length() > 0 ) {
				writer.print(", " + pointer_wrapper_annotation.params());
			}
		}
		writer.println(");");

		if ( code_annotation != null && code_annotation.javaAfterNative().length() > 0 ) {
			writer.println(code_annotation.javaAfterNative());
		}

		final String tabs = code_annotation != null && code_annotation.tryBlock() ? "\t\t\t" : "\t\t";
		if ( generate_error_checks && method.getAnnotation(NoErrorCheck.class) == null ) {
			type_map.printErrorCheckMethod(writer, method, tabs);
		}
		// DISABLED: indirect buffer support
		//printNondirectParameterCopies(writer, method, mode);
		if ( has_result ) {
			if ( method.getAnnotation(GLreturn.class) == null ) {
				if ( ByteBuffer.class.equals(Utils.getJavaType(result_type)) ) {
					writer.println(tabs + "return LWJGLUtil.CHECKS && " + Utils.RESULT_VAR_NAME + " == null ? null : " + Utils.RESULT_VAR_NAME + ".order(ByteOrder.nativeOrder());"); // safeNewBuffer returns a direct ByteBuffer with BIG_ENDIAN order.
				} else {
					writer.println(tabs + "return " + Utils.RESULT_VAR_NAME + ";");
				}
			} else {
				Utils.printGLReturnPost(writer, method, method.getAnnotation(GLreturn.class), type_map);
			}
		}

		if ( code_annotation != null && code_annotation.tryBlock() ) {
			writer.println("\t\t} finally {");
			writer.println(code_annotation.javaFinally());
			writer.println("\t\t}");
		}
		writer.println("\t}");
	}

	private static String getExtensionPostfix(TypeElement interface_decl) {
		String interface_simple_name = interface_decl.getSimpleName().toString();
		Extension extension_annotation = interface_decl.getAnnotation(Extension.class);
		if ( extension_annotation == null ) {
			int underscore_index = interface_simple_name.indexOf("_");
			if ( underscore_index != -1 ) {
				return interface_simple_name.substring(0, underscore_index);
			} else {
				return "";
			}
		} else {
			return extension_annotation.postfix();
		}
	}

	private static VariableElement getAutoTypeParameter(ExecutableElement method, VariableElement target_parameter) {
		for ( VariableElement param : method.getParameters() ) {
			AnnotationMirror auto_annotation = Utils.getParameterAutoAnnotation(param);
			if ( auto_annotation != null ) {
				Class annotation_type = NativeTypeTranslator.getClassFromType(auto_annotation.getAnnotationType());
				String parameter_name;
				if ( annotation_type.equals(AutoType.class) ) {
					parameter_name = param.getAnnotation(AutoType.class).value();
				} else if ( annotation_type.equals(AutoSize.class) ) {
					parameter_name = param.getAnnotation(AutoSize.class).value();
				} else {
					throw new RuntimeException("Unknown annotation type " + annotation_type);
				}
				if ( target_parameter.getSimpleName().toString().equals(parameter_name) ) {
					return param;
				}
			}
		}
		return null;
	}

	private static boolean hasAnyParameterAutoTypeAnnotation(ExecutableElement method, VariableElement target_param) {
		for ( VariableElement param : method.getParameters() ) {
			AutoType auto_type_annotation = param.getAnnotation(AutoType.class);
			if ( auto_type_annotation != null ) {
				VariableElement type_target_param = Utils.findParameter(method, auto_type_annotation.value());
				if ( target_param.equals(type_target_param) ) {
					return true;
				}
			}
		}
		return false;
	}

	private static final Map<String, Pattern> postfixPatterns = new HashMap<String, Pattern>();

	private static Pattern getPostfixPattern(String regex) {
		Pattern pattern = postfixPatterns.get(regex);
		if ( pattern == null ) {
			postfixPatterns.put(regex, pattern = Pattern.compile(regex));
		}
		return pattern;
	}

	private static String getPostfixStrippedName(TypeMap type_map, TypeElement interface_decl, ExecutableElement method) {
		StripPostfix strip_annotation = method.getAnnotation(StripPostfix.class);
		VariableElement postfix_parameter = Utils.findParameter(method, strip_annotation.value());
		String postfix = strip_annotation.postfix();
		boolean postfixOverride = !("NULL".equals(postfix) && strip_annotation.hasPostfix());
		if ( !postfixOverride ) {
			PostfixTranslator translator = new PostfixTranslator(type_map, postfix_parameter);
			postfix_parameter.asType().accept(translator, null);
			postfix = translator.getSignature();
		} else if ( !strip_annotation.hasPostfix() ) {
			postfix = "";
		}

		String method_name;
		Alternate alt_annotation = method.getAnnotation(Alternate.class);
		method_name = alt_annotation == null || alt_annotation.javaAlt() ? method.getSimpleName().toString() : alt_annotation.value();

		String extension_postfix = "NULL".equals(strip_annotation.extension()) ? getExtensionPostfix(interface_decl) : strip_annotation.extension();

		Matcher matcher = getPostfixPattern(
			postfixOverride
				? (postfix + "(?:v)?" + extension_postfix + "$")
				: ("(?:" + postfix + "(?:v)?|i(?:64)?_v|v)" + extension_postfix + "$")
		).matcher(method_name);

		if ( !matcher.find() ) {
			throw new RuntimeException(method_name + " is specified as being postfix stripped on parameter " + postfix_parameter + ", but it's postfix is neither '" + postfix + "' nor 'v'");
		}

		return method_name.substring(0, matcher.start()) + extension_postfix;
	}

	private static int getBufferElementSizeExponent(Class c) {
		if ( IntBuffer.class.equals(c) ) {
			return 2;
		} else if ( LongBuffer.class.equals(c) ) {
			return 3;
		} else if ( DoubleBuffer.class.equals(c) ) {
			return 3;
		} else if ( ShortBuffer.class.equals(c) ) {
			return 1;
		} else if ( ByteBuffer.class.equals(c) ) {
			return 0;
		} else if ( FloatBuffer.class.equals(c) ) {
			return 2;
		} else {
			throw new RuntimeException(c + " is not allowed");
		}
	}

	private static boolean printMethodCallArgument(PrintWriter writer, ExecutableElement method, VariableElement param, Map<VariableElement, TypeInfo> typeinfos_instance, Mode mode, boolean first_parameter, TypeMap type_map) {
		if ( !first_parameter ) {
			writer.print(", ");
		}

		AnnotationMirror auto_annotation = Utils.getParameterAutoAnnotation(param);
		Constant constant_annotation = param.getAnnotation(Constant.class);
		if ( constant_annotation != null ) {
			writer.print(constant_annotation.value());
		} else if ( auto_annotation != null && mode == Mode.NORMAL ) {
			Class param_type = NativeTypeTranslator.getClassFromType(auto_annotation.getAnnotationType());
			if ( AutoType.class.equals(param_type) ) {
				final AutoType auto_type_annotation = param.getAnnotation(AutoType.class);
				final VariableElement auto_parameter = Utils.findParameter(method, auto_type_annotation.value());
				final String auto_type = typeinfos_instance.get(auto_parameter).getAutoType();
				if ( auto_type == null ) {
					throw new RuntimeException("No auto type for parameter " + param.getSimpleName() + " in method " + method);
				}
				writer.print(auto_type);
			} else if ( AutoSize.class.equals(param_type) ) {
				final AutoSize auto_size_annotation = param.getAnnotation(AutoSize.class);
				if ( !auto_size_annotation.useExpression() ) {
					final String auto_parameter_name = auto_size_annotation.value();
					final VariableElement auto_target_param = Utils.findParameter(method, auto_parameter_name);
					final TypeInfo auto_target_type_info = typeinfos_instance.get(auto_target_param);
					final boolean shift_remaining = !hasAnyParameterAutoTypeAnnotation(method, auto_target_param) && Utils.isParameterMultiTyped(auto_target_param);
					int shifting = 0;
					if ( shift_remaining ) {
						shifting = getBufferElementSizeExponent(auto_target_type_info.getType());
						if ( shifting > 0 ) {
							writer.print("(");
						}
					}
					if ( auto_size_annotation.canBeNull() ) {
						writer.print("(" + auto_parameter_name + " == null ? 0 : " + auto_parameter_name + ".remaining())");
					} else {
						writer.print(auto_parameter_name + ".remaining()");
					}
					// Shift the remaining if the target parameter is multityped and there's no AutoType to track type
					if ( shift_remaining && shifting > 0 ) {
						writer.print(" << " + shifting);
						writer.print(")");
					}
				}
				writer.print(auto_size_annotation.expression());
			} else {
				throw new RuntimeException("Unknown auto annotation " + param_type);
			}
		} else {
			if ( mode == Mode.BUFFEROBJECT && param.getAnnotation(BufferObject.class) != null ) {
				writer.print(param.getSimpleName() + Utils.BUFFER_OBJECT_PARAMETER_POSTFIX);
			} else {
				Class type = typeinfos_instance.get(param).getType();
				Check check_annotation = param.getAnnotation(Check.class);
				boolean hide_buffer = mode == Mode.AUTOS && getAutoTypeParameter(method, param) != null;
				if ( hide_buffer ) {
					writer.print("0L");
				} else {
					if ( type == CharSequence.class || type == CharSequence[].class ) {
						final String offset = Utils.getStringOffset(method, param);

						writer.print("APIUtil.getBuffer");
						if ( param.getAnnotation(NullTerminated.class) != null ) {
							writer.print("NT");
						}
						writer.print('(');
						writer.print(type_map.getAPIUtilParam(true));
						writer.print(param.getSimpleName());
						if ( offset != null ) {
							writer.print(", " + offset);
						}
						writer.print(")");
					} else {
						final AutoSize auto_size_annotation = param.getAnnotation(AutoSize.class);
						if ( auto_size_annotation != null ) {
							writer.print(auto_size_annotation.value() + "_");
						}

						final Class buffer_type = Utils.getNIOBufferType(param.asType());
						if ( buffer_type == null ) {
							writer.print(param.getSimpleName());
						} else {
							writer.print("MemoryUtil.getAddress");
							if ( check_annotation != null && check_annotation.canBeNull() ) {
								writer.print("Safe");
							}
							writer.print("(");
							writer.print(param.getSimpleName());
							writer.print(")");
						}
					}
				}
				if ( type != long.class ) {
					PointerWrapper pointer_annotation = param.getAnnotation(PointerWrapper.class);
					if ( pointer_annotation != null ) {
						if ( pointer_annotation.canBeNull() ) {
							writer.print(" == null ? 0 : " + param.getSimpleName());
						}
						writer.print(".getPointer()");
					}
				}
			}
		}
		return false;
	}

	private static boolean printMethodCallArguments(PrintWriter writer, ExecutableElement method, Map<VariableElement, TypeInfo> typeinfos_instance, Mode mode, TypeMap type_map) {
		boolean first_parameter = true;
		for ( VariableElement param : method.getParameters() ) {
			if ( param.getAnnotation(Result.class) != null || (param.getAnnotation(Helper.class) != null && !param.getAnnotation(Helper.class).passToNative()) ) {
				continue;
			}

			final Constant constant_annotation = param.getAnnotation(Constant.class);
			if ( constant_annotation == null || !constant_annotation.isNative() ) {
				first_parameter = printMethodCallArgument(writer, method, param, typeinfos_instance, mode, first_parameter, type_map);
			}
		}
		if ( Utils.getNIOBufferType(Utils.getMethodReturnType(method)) != null ) {
			if ( method.getAnnotation(CachedResult.class) != null && method.getAnnotation(CachedResult.class).isRange() ) {
				first_parameter = false;
				Utils.printExtraCallArguments(writer, method, "");
			} else {
				AutoSize auto_size_annotation = method.getAnnotation(AutoSize.class);
				if ( auto_size_annotation == null || !auto_size_annotation.isNative() ) {
					if ( !first_parameter ) {
						writer.print(", ");
					}
					first_parameter = false;

					String result_size_expression;
					if ( mode == Mode.CACHEDRESULT ) {
						result_size_expression = Utils.CACHED_BUFFER_LENGTH_NAME;
					} else if ( auto_size_annotation == null ) {
						result_size_expression = Utils.RESULT_SIZE_NAME;
					} else {
						result_size_expression = auto_size_annotation.value();
					}

					Utils.printExtraCallArguments(writer, method, result_size_expression);
				}
			}
		}
		return first_parameter;
	}

	private static void printParameterCaching(PrintWriter writer, TypeElement interface_decl, ExecutableElement method, Mode mode, boolean context_specific) {
		for ( VariableElement param : method.getParameters() ) {
			Class java_type = Utils.getJavaType(param.asType());
			CachedReference cachedReference = param.getAnnotation(CachedReference.class);
			if ( Buffer.class.isAssignableFrom(java_type)
			     && cachedReference != null
			     && (mode != Mode.BUFFEROBJECT || param.getAnnotation(BufferObject.class) == null)
			     && param.getAnnotation(Result.class) == null ) {
				writer.print("\t\tif ( LWJGLUtil.CHECKS ) StateTracker.");
				if ( context_specific ) {
					writer.print("getReferences(caps).");
				} else {
					writer.print("getTracker().");
				}
				if ( cachedReference.name().length() > 0 ) {
					writer.print(cachedReference.name());
				} else {
					writer.print(Utils.getReferenceName(interface_decl, method, param));
				}
				if ( cachedReference.index().length() > 0 ) {
					writer.print("[" + cachedReference.index() + "]");
				}
				writer.println(" = " + param.getSimpleName() + ";");
			}
		}
	}

	private static void printParameterChecks(PrintWriter writer, ExecutableElement method, Map<VariableElement, TypeInfo> typeinfos, Mode mode, final boolean generate_error_checks) {
		if ( mode == Mode.NORMAL ) {
			final GenerateAutos gen_autos_annotation = method.getAnnotation(GenerateAutos.class);
			if ( gen_autos_annotation != null && gen_autos_annotation.sizeVariables().length > 0 ) {
				// For the auto-generated parameters, declare and init a size variable (that can be reused by @Code)
				for ( final VariableElement param : method.getParameters() ) {
					if ( Arrays.binarySearch(gen_autos_annotation.sizeVariables(), param.getSimpleName().toString()) >= 0 ) {
						final int shifting = getBufferElementSizeExponent(typeinfos.get(param).getType());
						final Check check_annotation = param.getAnnotation(Check.class);

						writer.print("\t\tlong " + param.getSimpleName() + "_size = ");
						if ( check_annotation == null || !check_annotation.canBeNull() ) {
							writer.println(param.getSimpleName() + ".remaining() << " + shifting + ";");
						} else {
							writer.println(param.getSimpleName() + " == null ? 0 : " + param.getSimpleName() + ".remaining() << " + shifting + ";");
						}
					}
				}
			}
		}

		for ( VariableElement param : method.getParameters() ) {
			Class java_type = Utils.getJavaType(param.asType());
			if ( java_type.isArray() || (Utils.isAddressableType(java_type)
			                             && (mode != Mode.BUFFEROBJECT || param.getAnnotation(BufferObject.class) == null)
			                             && (mode != Mode.AUTOS || getAutoTypeParameter(method, param) == null)
			                             && param.getAnnotation(Result.class) == null
			                             && !Utils.isReturnParameter(method, param)) ) {
				String check_value = null;
				boolean can_be_null = false;
				Check check_annotation = param.getAnnotation(Check.class);
				if ( check_annotation != null ) {
					check_value = check_annotation.value();
					can_be_null = check_annotation.canBeNull();
				}
				if ( (Buffer.class.isAssignableFrom(java_type) || PointerBuffer.class.isAssignableFrom(java_type)) && param.getAnnotation(Constant.class) == null ) {
					TypeInfo typeinfo = typeinfos.get(param);
					printParameterCheck(writer, method, param.getSimpleName().toString(), typeinfo.getType().getSimpleName(), check_value, can_be_null, param.getAnnotation(NullTerminated.class), generate_error_checks);
				} else if ( String.class.equals(java_type) ) {
					if ( !can_be_null ) {
						writer.println("\t\tBufferChecks.checkNotNull(" + param.getSimpleName() + ");");
					}
				} else if ( java_type.isArray() ) {
					printArrayParameterCheck(writer, param.getSimpleName().toString(), check_value, can_be_null);
				}
			}
		}
		if ( method.getAnnotation(CachedResult.class) != null ) {
			printParameterCheck(writer, method, Utils.CACHED_BUFFER_NAME, null, null, true, null, generate_error_checks);
		}
	}

	private static void printParameterCheck(PrintWriter writer, ExecutableElement method, String name, String type, String check_value, boolean can_be_null, NullTerminated null_terminated, boolean generate_error_checks) {
		String tabs;
		if ( can_be_null ) {
			writer.print("\t\tif (" + name + " != null)");
			if ( null_terminated != null ) {
				writer.println(" {");
			} else {
				writer.println();
			}
			tabs = "\t\t\t";
		} else {
			tabs = "\t\t";
		}
		writer.print(tabs + "BufferChecks.check");
		if ( check_value != null && check_value.length() > 0 ) {
			writer.print("Buffer");
			if ( "Buffer".equals(type) ) {
				writer.print("Size"); // Check size only, Buffer.isDirect() was added in 1.6, cannot use yet. TODO: Remove?
			}
			writer.print("(" + name + ", " + check_value);
		} else {
			writer.print("Direct(" + name);
		}
		writer.println(");");
		if ( can_be_null && generate_error_checks ) {
			final Check check_annotation = method.getAnnotation(Check.class);
			if ( check_annotation != null && check_annotation.value().equals(name) ) {
				writer.println("\t\telse");
				writer.println("\t\t\t" + name + " = APIUtil.getBufferIntDebug();"); // Use an exclusive buffer here
			}
		}
		if ( null_terminated != null ) {
			writer.print(tabs + "BufferChecks.checkNullTerminated(");
			writer.print(name);
			if ( null_terminated.value().length() > 0 ) {
				writer.print(", ");
				writer.print(null_terminated.value());
			}
			writer.println(");");
			if ( can_be_null ) {
				writer.println("\t\t}");
			}
		}
	}

	private static void printArrayParameterCheck(PrintWriter writer, String name, String check_value, boolean can_be_null) {
		String tabs;
		if ( can_be_null ) {
			writer.println("\t\tif (" + name + " != null)");
			tabs = "\t\t\t";
		} else {
			tabs = "\t\t";
		}

		writer.print(tabs + "BufferChecks.checkArray(" + name);
		if ( check_value != null && check_value.length() > 0 ) {
			writer.print(", " + check_value);
		}
		writer.println(");");
	}

	private static String getResultType(ExecutableElement method, boolean native_stub) {
		if ( native_stub && method.getAnnotation(PointerWrapper.class) != null ) {
			return "long";
		} else if ( !native_stub && method.getAnnotation(GLreturn.class) != null ) {
			return Utils.getMethodReturnType(method, method.getAnnotation(GLreturn.class), false);
		} else {
			return Utils.getJavaType(Utils.getMethodReturnType(method)).getSimpleName();
		}
	}

	private static String getDefaultResultValue(ExecutableElement method) {
		if ( method.getAnnotation(GLreturn.class) != null ) {
			final String type = Utils.getMethodReturnType(method, method.getAnnotation(GLreturn.class), false);
			if ( "boolean".equals(type) ) {
				return "false";
			} else if ( Character.isLowerCase(type.charAt(0)) ) {
				return "0";
			} else {
				return "null";
			}
		} else {
			final Class type = Utils.getJavaType(Utils.getMethodReturnType(method));
			if ( type.isPrimitive() ) {
				if ( type == boolean.class ) {
					return "false";
				} else {
					return "0";
				}
			} else {
				return "null";
			}
		}
	}

}
