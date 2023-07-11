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
 * This class generates the functions in the native source files.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$
 * $Id$
 */

import org.lwjgl.PointerBuffer;

import java.io.PrintWriter;
import java.nio.Buffer;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class NativeMethodStubsGenerator {
	private static final String BUFFER_ADDRESS_POSTFIX  = "_address";
	public static final  String BUFFER_POSITION_POSTFIX = "_position";
	private static final String STRING_LIST_NAME        = "_str";
	private static final String POINTER_LIST_NAME       = "_ptr";

	public static void generateNativeMethodStubs(ProcessingEnvironment env, TypeMap type_map, PrintWriter writer, TypeElement d, boolean generate_error_checks, boolean context_specific) {
		for ( ExecutableElement method : Utils.getMethods(d) ) {
			Alternate alt_annotation = method.getAnnotation(Alternate.class);
			if ( (alt_annotation != null && (!alt_annotation.nativeAlt() || alt_annotation.skipNative())) || method.getAnnotation(Reuse.class) != null )
				continue;
			generateMethodStub(env, type_map, writer, Utils.getQualifiedClassName(d), method, Mode.NORMAL, generate_error_checks, context_specific);
			if ( Utils.hasMethodBufferObjectParameter(method) )
				generateMethodStub(env, type_map, writer, Utils.getQualifiedClassName(d), method, Mode.BUFFEROBJECT, generate_error_checks, context_specific);
		}
	}

	private static void generateParameters(PrintWriter writer, List<? extends VariableElement> params, Mode mode) {
		for ( VariableElement param : params ) {
			if ( param.getAnnotation(Result.class) != null || (param.getAnnotation(Helper.class) != null && !param.getAnnotation(Helper.class).passToNative()) )
				continue;
			final Constant constant_annotation = param.getAnnotation(Constant.class);
			if ( constant_annotation == null || !constant_annotation.isNative() )
				generateParameter(writer, param, mode);
		}
	}

	private static void generateParameter(PrintWriter writer, VariableElement param, Mode mode) {
		writer.print(", ");
		if ( mode == Mode.BUFFEROBJECT && param.getAnnotation(BufferObject.class) != null ) {
			writer.print("jlong " + param.getSimpleName() + Utils.BUFFER_OBJECT_PARAMETER_POSTFIX);
		} else if ( param.getAnnotation(PointerWrapper.class) != null ) {
			writer.print("jlong " + param.getSimpleName());
		} else {
			JNITypeTranslator translator = new JNITypeTranslator();
			param.asType().accept(translator, null);
			writer.print(translator.getSignature() + " " + param.getSimpleName());
		}
	}

	private static void generateMethodStub(ProcessingEnvironment env, TypeMap type_map, PrintWriter writer, String interface_name, ExecutableElement method, Mode mode, boolean generate_error_checks, boolean context_specific) {
		if ( !context_specific && method.getAnnotation(Alternate.class) == null )
			writer.print("static ");
		else
			writer.print("JNIEXPORT ");

		final TypeMirror result_type = Utils.getMethodReturnType(method);
		final CachedResult cached_result_annotation = method.getAnnotation(CachedResult.class);
		final AutoSize auto_size_annotation = method.getAnnotation(AutoSize.class);

		if ( method.getAnnotation(PointerWrapper.class) != null ) {
			writer.print("jlong");
		} else {
			JNITypeTranslator translator = new JNITypeTranslator();
			result_type.accept(translator, null);
			writer.print(translator.getReturnSignature());
		}
		writer.print(" JNICALL ");

		writer.print(Utils.getQualifiedNativeMethodName(interface_name, method, generate_error_checks, context_specific));
		if ( mode == Mode.BUFFEROBJECT )
			writer.print(Utils.BUFFER_OBJECT_METHOD_POSTFIX);
		writer.print("(JNIEnv *env, jclass clazz");
		generateParameters(writer, method.getParameters(), mode);
		if ( Utils.getNIOBufferType(result_type) != null ) {
			if ( (cached_result_annotation == null || !cached_result_annotation.isRange()) && (auto_size_annotation == null || !auto_size_annotation.isNative()) )
				writer.print(", jlong " + Utils.RESULT_SIZE_NAME);
			if ( cached_result_annotation != null )
				writer.print(", jobject " + Utils.CACHED_BUFFER_NAME);
		}
		if ( context_specific ) {
			writer.print(", jlong " + Utils.FUNCTION_POINTER_VAR_NAME);
		}
		writer.println(") {");

		generateBufferParameterAddresses(type_map, writer, method, mode);
		Alternate alt_annotation = method.getAnnotation(Alternate.class);
		if ( context_specific ) {
			String typedef_name = Utils.getTypedefName(method);
			writer.print("\t" + typedef_name + " " + (alt_annotation == null ? method.getSimpleName() : alt_annotation.value()));
			writer.print(" = (" + typedef_name + ")((intptr_t)");
			writer.println(Utils.FUNCTION_POINTER_VAR_NAME + ");");
		}

		final Code code_annotation = method.getAnnotation(Code.class);

		final boolean hasResult = !result_type.equals(env.getTypeUtils().getNoType(TypeKind.VOID));
		final boolean resultPreDeclare = hasResult && (hasPointerArrayInits(method.getParameters()) || (code_annotation != null && (code_annotation.nativeAfterVars().length() > 0 || code_annotation.nativeBeforeCall().length() > 0)));
		if ( resultPreDeclare )
			printResultParam(type_map, writer, method, result_type, true);

		if ( code_annotation != null && code_annotation.nativeAfterVars().length() > 0 )
			writer.println(code_annotation.nativeAfterVars());

		generatePointerArrayInits(type_map, writer, method.getParameters());

		if ( code_annotation != null && code_annotation.nativeBeforeCall().length() > 0 )
			writer.println(code_annotation.nativeBeforeCall());

		writer.print("\t");
		if ( resultPreDeclare )
			writer.print(Utils.RESULT_VAR_NAME + " = ");
		else if ( hasResult )
			printResultParam(type_map, writer, method, result_type, false);
		writer.print((alt_annotation == null ? method.getSimpleName() : alt_annotation.value()) + "(");
		generateCallParameters(writer, type_map, method.getParameters());
		writer.print(")");
		writer.println(";");

		if ( code_annotation != null && code_annotation.nativeAfterCall().length() > 0 )
			writer.println(code_annotation.nativeAfterCall());

		generateStringDeallocations(writer, method.getParameters());
		if ( !result_type.equals(env.getTypeUtils().getNoType(TypeKind.VOID)) ) {
			writer.print("\treturn ");
			Class java_result_type = Utils.getJavaType(result_type);
			if ( Buffer.class.isAssignableFrom(java_result_type) ) {
				if ( cached_result_annotation != null )
					writer.print("safeNewBufferCached(env, ");
				else
					writer.print("safeNewBuffer(env, ");
			} else if ( String.class.equals(java_result_type) ) {
				writer.print("NewStringNativeUnsigned(env, ");
			} else if ( method.getAnnotation(PointerWrapper.class) != null ) {
				writer.print("(intptr_t)");
			}
			writer.print(Utils.RESULT_VAR_NAME);
			if ( Buffer.class.isAssignableFrom(java_result_type) ) {
				final String size_parameter_name;
				if ( auto_size_annotation != null && (auto_size_annotation.isNative() || (cached_result_annotation != null && cached_result_annotation.isRange())) )
					size_parameter_name = auto_size_annotation.value();
				else
					size_parameter_name = Utils.RESULT_SIZE_NAME;

				writer.print(", ");
				Utils.printExtraCallArguments(writer, method, size_parameter_name);
			}
			if ( Buffer.class.isAssignableFrom(java_result_type) ||
			     String.class.equals(java_result_type) )
				writer.print(")");
			writer.println(";");
		}
		writer.println("}");
		writer.println();
	}

	private static void printResultParam(final TypeMap type_map, final PrintWriter writer, final ExecutableElement method, final TypeMirror result_type, final boolean preDeclare) {
		final VariableElement result_param = Utils.getResultParameter(method);
		final Element return_declaration = result_param == null ? method : result_param;
		final NativeTypeTranslator result_translator = new NativeTypeTranslator(type_map, return_declaration);
		result_type.accept(result_translator, null);
		if ( preDeclare )
			writer.print("\t");
		writer.print(result_translator.getSignature() + " " + Utils.RESULT_VAR_NAME);
		if ( preDeclare )
			writer.println(";");
		else
			writer.print(result_param == null ? " = " : ";\n\t");
	}

	private static void generateCallParameters(PrintWriter writer, TypeMap type_map, List<? extends VariableElement> params) {
		if ( params.size() > 0 ) {
			boolean first = true;
			for ( VariableElement param : params ) {
				if ( param.getAnnotation(Helper.class) != null )
					continue;

				if ( first )
					first = false;
				else
					writer.print(", ");

				generateCallParameter(writer, type_map, param);
			}
		}
	}

	private static void generateCallParameter(PrintWriter writer, TypeMap type_map, VariableElement param) {
		if ( param.getAnnotation(Helper.class) != null )
			return;

		final Constant constant_annotation = param.getAnnotation(Constant.class);
		if ( constant_annotation != null && constant_annotation.isNative() ) {
			writer.print(constant_annotation.value());
			return;
		}

		boolean is_indirect = param.getAnnotation(Indirect.class) != null;
		if ( is_indirect || param.getAnnotation(PointerArray.class) != null ) {
			writer.print("(");
			final NativeTypeTranslator translator = new NativeTypeTranslator(type_map, param);
			param.asType().accept(translator, null);
			writer.print(translator.getSignature());
			writer.print("*)");
		}
		if ( param.getAnnotation(PointerWrapper.class) != null )
			writer.print("(" + param.getAnnotation(PointerWrapper.class).value() + ")(intptr_t)");
		if ( param.getAnnotation(Result.class) != null || is_indirect )
			writer.print("&");

		if ( param.getAnnotation(Result.class) != null ) {
			writer.print(Utils.RESULT_VAR_NAME);
		} else {
			writer.print(param.getSimpleName());
			if ( param.getAnnotation(PointerArray.class) != null )
				writer.print(getPointerArrayName(Utils.getJavaType(param.asType())));
			else if ( Utils.isAddressableType(param.asType()) )
				writer.print(BUFFER_ADDRESS_POSTFIX);
		}
	}

	private static void generateStringDeallocations(PrintWriter writer, List<? extends VariableElement> params) {
		for ( VariableElement param : params ) {
			final Class java_type = Utils.getJavaType(param.asType());
			if ( java_type.equals(String.class) && param.getAnnotation(Result.class) == null )
				writer.println("\tfree(" + param.getSimpleName() + BUFFER_ADDRESS_POSTFIX + ");");
			else if ( param.getAnnotation(PointerArray.class) != null ) // Free the string array mem
				writer.println("\tfree(" + param.getSimpleName() + getPointerArrayName(java_type) + ");");
		}
	}

	private static void generateBufferParameterAddresses(TypeMap type_map, PrintWriter writer, ExecutableElement method, Mode mode) {
		strLoopDeclared = false;
		ptrLoopDeclared = false;
		for ( VariableElement param : method.getParameters() ) {
			final Constant constant_annotation = param.getAnnotation(Constant.class);
			if ( param.getAnnotation(Result.class) == null && (constant_annotation == null || !constant_annotation.isNative()) && Utils.isAddressableType(param.asType()) )
				generateBufferParameterAddress(type_map, writer, param, mode);
		}
	}

	private static boolean strLoopDeclared;
	private static boolean ptrLoopDeclared;

	private static void generateBufferParameterAddress(TypeMap type_map, PrintWriter writer, VariableElement param, Mode mode) {
		final Check check_annotation = param.getAnnotation(Check.class);
		final PointerArray array_annotation = param.getAnnotation(PointerArray.class);
		final Class java_type = Utils.getJavaType(param.asType());

		final NativeTypeTranslator translator = new NativeTypeTranslator(type_map, param);
		param.asType().accept(translator, null);
		final String native_type = translator.getSignature();

		if ( !java_type.isArray() || CharSequence.class.isAssignableFrom(java_type.getComponentType()) ) {
			writer.print("\t" + native_type + param.getSimpleName());
			writer.print(BUFFER_ADDRESS_POSTFIX + " = (");
			writer.print(native_type);
			writer.print(")(intptr_t)");

			if ( mode == Mode.BUFFEROBJECT && param.getAnnotation(BufferObject.class) != null ) {
				writer.print("offsetToPointer(" + param.getSimpleName() + Utils.BUFFER_OBJECT_PARAMETER_POSTFIX + ")");
			} else {
				if ( Buffer.class.isAssignableFrom(java_type) || java_type.equals(CharSequence.class) || java_type.equals(CharSequence[].class) || PointerBuffer.class.isAssignableFrom(java_type) ) {
					writer.print(param.getSimpleName());
				} else if ( java_type.equals(String.class) ) {
					writer.print("GetStringNativeChars(env, " + param.getSimpleName() + ")");
				} else if ( array_annotation == null )
					throw new RuntimeException("Illegal type " + java_type);
			}
			writer.println(";");
		}

		if ( array_annotation != null ) {
			final String n = getPointerArrayName(java_type);
			final String arrayType;
			if ( POINTER_LIST_NAME.equals(n) ) {
				if ( n.equals(param.getSimpleName().toString()) )
					throw new RuntimeException("The name '" + n + "' is not valid for object array arguments annotated with PointerArray");

				arrayType = translator.getSignature(true) + (org.lwjgl.PointerWrapper.class.isAssignableFrom(java_type.getComponentType()) ? " " : "");

				// Declare loop counters and allocate object array
				if ( !ptrLoopDeclared ) {
					writer.println("\tint " + n + "_i;");
					writer.println("\tjobject " + n + "_object;");
					ptrLoopDeclared = true;
				}
			} else {
				if ( n.equals(param.getSimpleName().toString()) )
					throw new RuntimeException("The name '" + n + "' is not valid for arguments annotated with PointerArray");

				arrayType = translator.getSignature(true);

				// Declare loop counters and allocate string array
				if ( !strLoopDeclared ) {
					writer.println("\tint " + n + "_i;");
					writer.println("\t" + arrayType + n + "_address;");
					strLoopDeclared = true;
				}
			}

			writer.print("\t" + arrayType + "*" + param.getSimpleName() + n + " = ");
			if ( check_annotation != null && check_annotation.canBeNull() )
				writer.print(array_annotation.value() + " == 0 ? NULL : ");
			writer.println("(" + arrayType + "*) malloc(" + array_annotation.value() + " * sizeof(" + arrayType + "));");
		}
	}

	private static String getPointerArrayName(final Class java_type) {
		final Class<?> component_type = java_type.getComponentType();
		if ( component_type != null && (Buffer.class.isAssignableFrom(component_type) || org.lwjgl.PointerWrapper.class.isAssignableFrom(component_type)) )
			return POINTER_LIST_NAME;
		else
			return STRING_LIST_NAME;
	}

	private static boolean hasPointerArrayInits(List<? extends VariableElement> params) {
		for ( VariableElement param : params ) {
			PointerArray pointerArray_annotation = param.getAnnotation(PointerArray.class);
			if ( pointerArray_annotation != null )
				return true;
		}
		return false;
	}

	private static void generatePointerArrayInits(TypeMap type_map, PrintWriter writer, List<? extends VariableElement> params) {
		for ( VariableElement param : params ) {
			PointerArray pointerArray_annotation = param.getAnnotation(PointerArray.class);
			if ( pointerArray_annotation != null ) {
				final Class java_type = Utils.getJavaType(param.asType());
				final Class<?> component_type = java_type.isArray() ? java_type.getComponentType() : null;
				final NativeTypeTranslator translator = new NativeTypeTranslator(type_map, param);
				param.asType().accept(translator, null);

				final String n = getPointerArrayName(java_type);
				if ( POINTER_LIST_NAME.equals(n) ) {
					// Init vars
					writer.println("\t" + n + "_i = 0;");
					// Fill pointer array with the buffer pointers
					writer.println("\twhile ( " + n + "_i < " + pointerArray_annotation.value() + " ) {");
					writer.println("\t\t" + n + "_object = (*env)->GetObjectArrayElement(env, " + param.getSimpleName() + ", " + n + "_i);");
					writer.print("\t\t" + param.getSimpleName() + n + "[" + n + "_i++] = (" + translator.getSignature(true) + ")");
					if ( Buffer.class.isAssignableFrom(component_type) )
						writer.println("(*env)->GetDirectBufferAddress(env, " + n + "_object);");
					else
						writer.println("(intptr_t)getPointerWrapperAddress(env, " + n + "_object);");
					writer.println("\t}");
				} else {
					final String lengths = pointerArray_annotation.lengths();

					// Init vars
					writer.println("\t" + n + "_i = 0;");
					writer.println("\t" + n + "_address = (" + translator.getSignature(true) + ")" + param.getSimpleName() + BUFFER_ADDRESS_POSTFIX + ";");
					// Fill string array with the string pointers
					writer.println("\twhile ( " + n + "_i < " + pointerArray_annotation.value() + " ) {");
					if ( lengths.length() == 0 ) {
						writer.println("\t\t" + param.getSimpleName() + n + "[" + n + "_i++] = " + n + "_address;");
						writer.println("\t\t" + n + "_address += strlen((const char *)" + n + "_address) + 1;");
					} else {
						writer.println("\t\t" + param.getSimpleName() + n + "[" + n + "_i] = " + n + "_address;");
						writer.println("\t\t" + n + "_address += " + lengths + BUFFER_ADDRESS_POSTFIX + "[" + n + "_i++];");
					}
					writer.println("\t}");
				}
			}
		}
	}

}
