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
 * A TypeVisitor that generates the native typedefs.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$
 * $Id$
 */

import java.io.PrintWriter;
import java.util.Collection;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class TypedefsGenerator {
	private static void generateNativeTypedefs(TypeMap type_map, PrintWriter writer, ExecutableElement method) {
		TypeMirror return_type = method.getReturnType();
		writer.print("typedef ");
		writer.print(type_map.getTypedefPostfix());
		NativeTypeTranslator translator = new NativeTypeTranslator(type_map, method);
		return_type.accept(translator, null);
		writer.print(translator.getSignature());
		writer.print(" (");
		writer.print(type_map.getFunctionPrefix());
		writer.print(" *" + Utils.getTypedefName(method) + ") (");
		generateNativeTypedefsParameters(type_map, writer, method.getParameters());
		writer.println(");");
	}

	private static void generateNativeTypedefsParameters(TypeMap type_map, PrintWriter writer, Collection<? extends VariableElement> params) {
		if ( params.size() > 0 ) {
			boolean first = true;
			for ( VariableElement param : params ) {
				if ( param.getAnnotation(Helper.class) != null )
					continue;

				if ( first )
					first = false;
				else
					writer.print(", ");

				generateNativeTypedefsParameter(type_map, writer, param);
			}
		}
	}

	private static void generateNativeTypedefsParameter(TypeMap type_map, PrintWriter writer, VariableElement param) {
		NativeTypeTranslator translator = new NativeTypeTranslator(type_map, param);
		param.asType().accept(translator, null);
		writer.print(translator.getSignature());
		if ( param.getAnnotation(Result.class) != null || param.getAnnotation(Indirect.class) != null || param.getAnnotation(PointerArray.class) != null )
			writer.print("*");
		writer.print(" " + param.getSimpleName());
	}

	public static void generateNativeTypedefs(TypeMap type_map, PrintWriter writer, Collection<? extends ExecutableElement> methods) {
		for ( ExecutableElement method : methods ) {
			if ( method.getAnnotation(Alternate.class) == null && method.getAnnotation(Reuse.class) == null )
				generateNativeTypedefs(type_map, writer, method);
		}
	}

}
