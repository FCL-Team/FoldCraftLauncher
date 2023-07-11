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
import org.lwjgl.util.generator.Extension;
import org.lwjgl.util.generator.Private;
import org.lwjgl.util.generator.Utils;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

/**
 * CL platform/device capabilities generator.
 *
 * @author Spasi
 */
public class CLPDCapabilitiesGenerator {

	// TODO: Add future versions here
	private static final int[][] CL_VERSIONS = {
		{ 1, 2 },  // OpenCL 1
	};

	static void generateClassPrologue(final PrintWriter writer, final String name) {
		writer.println("public class " + name + " {");
		writer.println();
		writer.println("\tpublic final int majorVersion;");
		writer.println("\tpublic final int minorVersion;");
		writer.println();
		for ( int major = 1; major <= CL_VERSIONS.length; major++ ) {
			for ( final int minor : CL_VERSIONS[major - 1] )
				writer.println("\tpublic final boolean OpenCL" + Integer.toString(major) + Integer.toString(minor) + ";");
		}
		writer.println();
	}

	static void generateExtensions(final PrintWriter writer, final TypeElement d) {
		writer.print("\t");

		if ( d.getAnnotation(Private.class) == null )
			writer.print("public ");

		writer.println("final boolean " + CLGeneratorProcessor.getExtensionName(d.getSimpleName().toString()) + ";");
	}

	static void generateConstructor(ProcessingEnvironment env, final PrintWriter writer, final Set<? extends TypeElement> templates,
		final Class<? extends Annotation> capsType, final String capsName,
		final Class<? extends PointerWrapper> objectType, final String objectName) {
		writer.println("\tpublic " + capsName + "(final " + objectType.getSimpleName() + ' ' + objectName + ") {");

		writer.println("\t\tfinal String extensionList = " + objectName + ".getInfoString(CL10.CL_" + objectName.toUpperCase() + "_EXTENSIONS);\n" +
		               "\t\tfinal String version = " + objectName + ".getInfoString(CL10.CL_" + objectName.toUpperCase() + "_VERSION);\n" +
		               "\t\tif ( !version.startsWith(\"OpenCL \") )\n" +
		               "\t\t\tthrow new RuntimeException(\"Invalid OpenCL version string: \" + version);\n\n" +
		               "\t\ttry {\n" +
		               "\t\t\tfinal StringTokenizer tokenizer = new StringTokenizer(version.substring(7), \". \");\n" +
		               "\n" +
		               "\t\t\tmajorVersion = Integer.parseInt(tokenizer.nextToken());\n" +
		               "\t\t\tminorVersion = Integer.parseInt(tokenizer.nextToken());\n");

		for ( int major = 1; major <= CL_VERSIONS.length; major++ ) {
			for ( final int minor : CL_VERSIONS[major - 1] )
				writer.println("\t\t\tOpenCL" + Integer.toString(major) + Integer.toString(minor) + " = " + major + " < majorVersion || (" + major + " == majorVersion && " + minor + " <= minorVersion);");
		}

		writer.println("\t\t} catch (RuntimeException e) {\n" +
		               "\t\t\tthrow new RuntimeException(\"The major and/or minor OpenCL version \\\"\" + version + \"\\\" is malformed: \" + e.getMessage());\n" +
		               "\t\t}\n");

		writer.println("\t\tfinal Set<String> extensions = APIUtil.getExtensions(extensionList);");

		for ( final TypeElement t : templates ) {
			if ( t.getAnnotation(capsType) == null )
				continue;

			final String extName = CLGeneratorProcessor.getExtensionName(t.getSimpleName().toString());

			String nativeName = extName.toLowerCase();
			Extension ext = t.getAnnotation(Extension.class);
			if ( ext != null && !ext.nativeName().isEmpty() )
				nativeName = ext.nativeName();

			writer.print("\t\t" + extName + " = extensions.contains(\"" + nativeName + "\")");
			if ( !Utils.getMethods(t).isEmpty() )
				writer.print(" && CLCapabilities." + extName);
			writer.println(";");
		}

		writer.println("\t}\n");
	}

	public static void generateGetters(final PrintWriter writer) {
		writer.println("\tpublic int getMajorVersion() {");
		writer.println("\t\treturn majorVersion;");
		writer.println("\t}\n");

		writer.println("\tpublic int getMinorVersion() {");
		writer.println("\t\treturn minorVersion;");
		writer.println("\t}\n");
	}

	public static void generateToString(final PrintWriter writer, final Set<? extends TypeElement> templates, final Class<? extends Annotation> capsType) {
		writer.println("\tpublic String toString() {");
		writer.println("\t\tfinal StringBuilder buf = new StringBuilder();\n");

		writer.println("\t\tbuf.append(\"OpenCL \").append(majorVersion).append('.').append(minorVersion);");
		writer.println();
		writer.println("\t\tbuf.append(\" - Extensions: \");");
		for ( final TypeElement t : templates ) {
			if ( t.getAnnotation(capsType) == null )
				continue;

			writer.println("\t\tif ( " + CLGeneratorProcessor.getExtensionName(t.getSimpleName().toString()) + " ) buf.append(\"" + CLGeneratorProcessor.getExtensionName(t.getSimpleName().toString()).toLowerCase() + " \");");
		}

		writer.println("\n\t\treturn buf.toString();");
		writer.println("\t}\n");
	}

}