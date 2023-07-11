/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBFragmentProgram extends ARBProgram {

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled, by the
	 *  &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev,
	 *  and by the &lt;target&gt; parameter of ProgramStringARB, BindProgramARB,
	 *  ProgramEnvParameter4[df][v]ARB, ProgramLocalParameter4[df][v]ARB,
	 *  GetProgramEnvParameter[df]vARB, GetProgramLocalParameter[df]vARB,
	 *  GetProgramivARB and GetProgramStringARB.
	 */
	public static final int GL_FRAGMENT_PROGRAM_ARB = 0x8804;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetProgramivARB:
	 */
	public static final int GL_PROGRAM_ALU_INSTRUCTIONS_ARB = 0x8805,
		GL_PROGRAM_TEX_INSTRUCTIONS_ARB = 0x8806,
		GL_PROGRAM_TEX_INDIRECTIONS_ARB = 0x8807,
		GL_PROGRAM_NATIVE_ALU_INSTRUCTIONS_ARB = 0x8808,
		GL_PROGRAM_NATIVE_TEX_INSTRUCTIONS_ARB = 0x8809,
		GL_PROGRAM_NATIVE_TEX_INDIRECTIONS_ARB = 0x880A,
		GL_MAX_PROGRAM_ALU_INSTRUCTIONS_ARB = 0x880B,
		GL_MAX_PROGRAM_TEX_INSTRUCTIONS_ARB = 0x880C,
		GL_MAX_PROGRAM_TEX_INDIRECTIONS_ARB = 0x880D,
		GL_MAX_PROGRAM_NATIVE_ALU_INSTRUCTIONS_ARB = 0x880E,
		GL_MAX_PROGRAM_NATIVE_TEX_INSTRUCTIONS_ARB = 0x880F,
		GL_MAX_PROGRAM_NATIVE_TEX_INDIRECTIONS_ARB = 0x8810;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_MAX_TEXTURE_COORDS_ARB = 0x8871,
		GL_MAX_TEXTURE_IMAGE_UNITS_ARB = 0x8872;

	private ARBFragmentProgram() {}
}
