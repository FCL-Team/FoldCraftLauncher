/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import java.util.Set;
import java.util.HashSet;

public class ContextCapabilities {
	static final boolean DEBUG = false;
	final APIUtil util = new APIUtil();
	final StateTracker tracker = new StateTracker();

	public final boolean GL_AMD_blend_minmax_factor;
	public final boolean GL_AMD_conservative_depth;
	public final boolean GL_AMD_debug_output;
	public final boolean GL_AMD_depth_clamp_separate;
	public final boolean GL_AMD_draw_buffers_blend;
	public final boolean GL_AMD_interleaved_elements;
	public final boolean GL_AMD_multi_draw_indirect;
	public final boolean GL_AMD_name_gen_delete;
	public final boolean GL_AMD_performance_monitor;
	public final boolean GL_AMD_pinned_memory;
	public final boolean GL_AMD_query_buffer_object;
	public final boolean GL_AMD_sample_positions;
	public final boolean GL_AMD_seamless_cubemap_per_texture;
	public final boolean GL_AMD_shader_atomic_counter_ops;
	public final boolean GL_AMD_shader_stencil_export;
	public final boolean GL_AMD_shader_trinary_minmax;
	public final boolean GL_AMD_sparse_texture;
	public final boolean GL_AMD_stencil_operation_extended;
	public final boolean GL_AMD_texture_texture4;
	public final boolean GL_AMD_transform_feedback3_lines_triangles;
	public final boolean GL_AMD_vertex_shader_layer;
	public final boolean GL_AMD_vertex_shader_tessellator;
	public final boolean GL_AMD_vertex_shader_viewport_index;
	public final boolean GL_APPLE_aux_depth_stencil;
	public final boolean GL_APPLE_client_storage;
	public final boolean GL_APPLE_element_array;
	public final boolean GL_APPLE_fence;
	public final boolean GL_APPLE_float_pixels;
	public final boolean GL_APPLE_flush_buffer_range;
	public final boolean GL_APPLE_object_purgeable;
	public final boolean GL_APPLE_packed_pixels;
	public final boolean GL_APPLE_rgb_422;
	public final boolean GL_APPLE_row_bytes;
	public final boolean GL_APPLE_texture_range;
	public final boolean GL_APPLE_vertex_array_object;
	public final boolean GL_APPLE_vertex_array_range;
	public final boolean GL_APPLE_vertex_program_evaluators;
	public final boolean GL_APPLE_ycbcr_422;
	public final boolean GL_ARB_ES2_compatibility;
	public final boolean GL_ARB_ES3_1_compatibility;
	public final boolean GL_ARB_ES3_compatibility;
	public final boolean GL_ARB_arrays_of_arrays;
	public final boolean GL_ARB_base_instance;
	public final boolean GL_ARB_bindless_texture;
	public final boolean GL_ARB_blend_func_extended;
	public final boolean GL_ARB_buffer_storage;
	public final boolean GL_ARB_cl_event;
	public final boolean GL_ARB_clear_buffer_object;
	public final boolean GL_ARB_clear_texture;
	public final boolean GL_ARB_clip_control;
	public final boolean GL_ARB_color_buffer_float;
	public final boolean GL_ARB_compatibility;
	public final boolean GL_ARB_compressed_texture_pixel_storage;
	public final boolean GL_ARB_compute_shader;
	public final boolean GL_ARB_compute_variable_group_size;
	public final boolean GL_ARB_conditional_render_inverted;
	public final boolean GL_ARB_conservative_depth;
	public final boolean GL_ARB_copy_buffer;
	public final boolean GL_ARB_copy_image;
	public final boolean GL_ARB_cull_distance;
	public final boolean GL_ARB_debug_output;
	public final boolean GL_ARB_depth_buffer_float;
	public final boolean GL_ARB_depth_clamp;
	public final boolean GL_ARB_depth_texture;
	public final boolean GL_ARB_derivative_control;
	public final boolean GL_ARB_direct_state_access;
	public final boolean GL_ARB_draw_buffers;
	public final boolean GL_ARB_draw_buffers_blend;
	public final boolean GL_ARB_draw_elements_base_vertex;
	public final boolean GL_ARB_draw_indirect;
	public final boolean GL_ARB_draw_instanced;
	public final boolean GL_ARB_enhanced_layouts;
	public final boolean GL_ARB_explicit_attrib_location;
	public final boolean GL_ARB_explicit_uniform_location;
	public final boolean GL_ARB_fragment_coord_conventions;
	public final boolean GL_ARB_fragment_layer_viewport;
	public final boolean GL_ARB_fragment_program;
	public final boolean GL_ARB_fragment_program_shadow;
	public final boolean GL_ARB_fragment_shader;
	public final boolean GL_ARB_framebuffer_no_attachments;
	public final boolean GL_ARB_framebuffer_object;
	public final boolean GL_ARB_framebuffer_sRGB;
	public final boolean GL_ARB_geometry_shader4;
	public final boolean GL_ARB_get_program_binary;
	public final boolean GL_ARB_get_texture_sub_image;
	public final boolean GL_ARB_gpu_shader5;
	public final boolean GL_ARB_gpu_shader_fp64;
	public final boolean GL_ARB_half_float_pixel;
	public final boolean GL_ARB_half_float_vertex;
	public final boolean GL_ARB_imaging;
	public final boolean GL_ARB_indirect_parameters;
	public final boolean GL_ARB_instanced_arrays;
	public final boolean GL_ARB_internalformat_query;
	public final boolean GL_ARB_internalformat_query2;
	public final boolean GL_ARB_invalidate_subdata;
	public final boolean GL_ARB_map_buffer_alignment;
	public final boolean GL_ARB_map_buffer_range;
	public final boolean GL_ARB_matrix_palette;
	public final boolean GL_ARB_multi_bind;
	public final boolean GL_ARB_multi_draw_indirect;
	public final boolean GL_ARB_multisample;
	public final boolean GL_ARB_multitexture;
	public final boolean GL_ARB_occlusion_query;
	public final boolean GL_ARB_occlusion_query2;
	public final boolean GL_ARB_pipeline_statistics_query;
	public final boolean GL_ARB_pixel_buffer_object;
	public final boolean GL_ARB_point_parameters;
	public final boolean GL_ARB_point_sprite;
	public final boolean GL_ARB_program_interface_query;
	public final boolean GL_ARB_provoking_vertex;
	public final boolean GL_ARB_query_buffer_object;
	public final boolean GL_ARB_robust_buffer_access_behavior;
	public final boolean GL_ARB_robustness;
	public final boolean GL_ARB_robustness_isolation;
	public final boolean GL_ARB_sample_shading;
	public final boolean GL_ARB_sampler_objects;
	public final boolean GL_ARB_seamless_cube_map;
	public final boolean GL_ARB_seamless_cubemap_per_texture;
	public final boolean GL_ARB_separate_shader_objects;
	public final boolean GL_ARB_shader_atomic_counters;
	public final boolean GL_ARB_shader_bit_encoding;
	public final boolean GL_ARB_shader_draw_parameters;
	public final boolean GL_ARB_shader_group_vote;
	public final boolean GL_ARB_shader_image_load_store;
	public final boolean GL_ARB_shader_image_size;
	public final boolean GL_ARB_shader_objects;
	public final boolean GL_ARB_shader_precision;
	public final boolean GL_ARB_shader_stencil_export;
	public final boolean GL_ARB_shader_storage_buffer_object;
	public final boolean GL_ARB_shader_subroutine;
	public final boolean GL_ARB_shader_texture_image_samples;
	public final boolean GL_ARB_shader_texture_lod;
	public final boolean GL_ARB_shading_language_100;
	public final boolean GL_ARB_shading_language_420pack;
	public final boolean GL_ARB_shading_language_include;
	public final boolean GL_ARB_shading_language_packing;
	public final boolean GL_ARB_shadow;
	public final boolean GL_ARB_shadow_ambient;
	public final boolean GL_ARB_sparse_buffer;
	public final boolean GL_ARB_sparse_texture;
	public final boolean GL_ARB_stencil_texturing;
	public final boolean GL_ARB_sync;
	public final boolean GL_ARB_tessellation_shader;
	public final boolean GL_ARB_texture_barrier;
	public final boolean GL_ARB_texture_border_clamp;
	public final boolean GL_ARB_texture_buffer_object;
	public final boolean GL_ARB_texture_buffer_object_rgb32;
	public final boolean GL_ARB_texture_buffer_range;
	public final boolean GL_ARB_texture_compression;
	public final boolean GL_ARB_texture_compression_bptc;
	public final boolean GL_ARB_texture_compression_rgtc;
	public final boolean GL_ARB_texture_cube_map;
	public final boolean GL_ARB_texture_cube_map_array;
	public final boolean GL_ARB_texture_env_add;
	public final boolean GL_ARB_texture_env_combine;
	public final boolean GL_ARB_texture_env_crossbar;
	public final boolean GL_ARB_texture_env_dot3;
	public final boolean GL_ARB_texture_float;
	public final boolean GL_ARB_texture_gather;
	public final boolean GL_ARB_texture_mirror_clamp_to_edge;
	public final boolean GL_ARB_texture_mirrored_repeat;
	public final boolean GL_ARB_texture_multisample;
	public final boolean GL_ARB_texture_non_power_of_two;
	public final boolean GL_ARB_texture_query_levels;
	public final boolean GL_ARB_texture_query_lod;
	public final boolean GL_ARB_texture_rectangle;
	public final boolean GL_ARB_texture_rg;
	public final boolean GL_ARB_texture_rgb10_a2ui;
	public final boolean GL_ARB_texture_stencil8;
	public final boolean GL_ARB_texture_storage;
	public final boolean GL_ARB_texture_storage_multisample;
	public final boolean GL_ARB_texture_swizzle;
	public final boolean GL_ARB_texture_view;
	public final boolean GL_ARB_timer_query;
	public final boolean GL_ARB_transform_feedback2;
	public final boolean GL_ARB_transform_feedback3;
	public final boolean GL_ARB_transform_feedback_instanced;
	public final boolean GL_ARB_transform_feedback_overflow_query;
	public final boolean GL_ARB_transpose_matrix;
	public final boolean GL_ARB_uniform_buffer_object;
	public final boolean GL_ARB_vertex_array_bgra;
	public final boolean GL_ARB_vertex_array_object;
	public final boolean GL_ARB_vertex_attrib_64bit;
	public final boolean GL_ARB_vertex_attrib_binding;
	public final boolean GL_ARB_vertex_blend;
	public final boolean GL_ARB_vertex_buffer_object;
	public final boolean GL_ARB_vertex_program;
	public final boolean GL_ARB_vertex_shader;
	public final boolean GL_ARB_vertex_type_10f_11f_11f_rev;
	public final boolean GL_ARB_vertex_type_2_10_10_10_rev;
	public final boolean GL_ARB_viewport_array;
	public final boolean GL_ARB_window_pos;
	public final boolean GL_ATI_draw_buffers;
	public final boolean GL_ATI_element_array;
	public final boolean GL_ATI_envmap_bumpmap;
	public final boolean GL_ATI_fragment_shader;
	public final boolean GL_ATI_map_object_buffer;
	public final boolean GL_ATI_meminfo;
	public final boolean GL_ATI_pn_triangles;
	public final boolean GL_ATI_separate_stencil;
	public final boolean GL_ATI_shader_texture_lod;
	public final boolean GL_ATI_text_fragment_shader;
	public final boolean GL_ATI_texture_compression_3dc;
	public final boolean GL_ATI_texture_env_combine3;
	public final boolean GL_ATI_texture_float;
	public final boolean GL_ATI_texture_mirror_once;
	public final boolean GL_ATI_vertex_array_object;
	public final boolean GL_ATI_vertex_attrib_array_object;
	public final boolean GL_ATI_vertex_streams;
	public final boolean GL_EXT_Cg_shader;
	public final boolean GL_EXT_abgr;
	public final boolean GL_EXT_bgra;
	public final boolean GL_EXT_bindable_uniform;
	public final boolean GL_EXT_blend_color;
	public final boolean GL_EXT_blend_equation_separate;
	public final boolean GL_EXT_blend_func_separate;
	public final boolean GL_EXT_blend_minmax;
	public final boolean GL_EXT_blend_subtract;
	public final boolean GL_EXT_compiled_vertex_array;
	public final boolean GL_EXT_depth_bounds_test;
	public final boolean GL_EXT_direct_state_access;
	public final boolean GL_EXT_draw_buffers2;
	public final boolean GL_EXT_draw_instanced;
	public final boolean GL_EXT_draw_range_elements;
	public final boolean GL_EXT_fog_coord;
	public final boolean GL_EXT_framebuffer_blit;
	public final boolean GL_EXT_framebuffer_multisample;
	public final boolean GL_EXT_framebuffer_multisample_blit_scaled;
	public final boolean GL_EXT_framebuffer_object;
	public final boolean GL_EXT_framebuffer_sRGB;
	public final boolean GL_EXT_geometry_shader4;
	public final boolean GL_EXT_gpu_program_parameters;
	public final boolean GL_EXT_gpu_shader4;
	public final boolean GL_EXT_multi_draw_arrays;
	public final boolean GL_EXT_packed_depth_stencil;
	public final boolean GL_EXT_packed_float;
	public final boolean GL_EXT_packed_pixels;
	public final boolean GL_EXT_paletted_texture;
	public final boolean GL_EXT_pixel_buffer_object;
	public final boolean GL_EXT_point_parameters;
	public final boolean GL_EXT_provoking_vertex;
	public final boolean GL_EXT_rescale_normal;
	public final boolean GL_EXT_secondary_color;
	public final boolean GL_EXT_separate_shader_objects;
	public final boolean GL_EXT_separate_specular_color;
	public final boolean GL_EXT_shader_image_load_store;
	public final boolean GL_EXT_shadow_funcs;
	public final boolean GL_EXT_shared_texture_palette;
	public final boolean GL_EXT_stencil_clear_tag;
	public final boolean GL_EXT_stencil_two_side;
	public final boolean GL_EXT_stencil_wrap;
	public final boolean GL_EXT_texture_3d;
	public final boolean GL_EXT_texture_array;
	public final boolean GL_EXT_texture_buffer_object;
	public final boolean GL_EXT_texture_compression_latc;
	public final boolean GL_EXT_texture_compression_rgtc;
	public final boolean GL_EXT_texture_compression_s3tc;
	public final boolean GL_EXT_texture_env_combine;
	public final boolean GL_EXT_texture_env_dot3;
	public final boolean GL_EXT_texture_filter_anisotropic;
	public final boolean GL_EXT_texture_integer;
	public final boolean GL_EXT_texture_lod_bias;
	public final boolean GL_EXT_texture_mirror_clamp;
	public final boolean GL_EXT_texture_rectangle;
	public final boolean GL_EXT_texture_sRGB;
	public final boolean GL_EXT_texture_sRGB_decode;
	public final boolean GL_EXT_texture_shared_exponent;
	public final boolean GL_EXT_texture_snorm;
	public final boolean GL_EXT_texture_swizzle;
	public final boolean GL_EXT_timer_query;
	public final boolean GL_EXT_transform_feedback;
	public final boolean GL_EXT_vertex_array_bgra;
	public final boolean GL_EXT_vertex_attrib_64bit;
	public final boolean GL_EXT_vertex_shader;
	public final boolean GL_EXT_vertex_weighting;
	public final boolean OpenGL11;
	public final boolean OpenGL12;
	public final boolean OpenGL13;
	public final boolean OpenGL14;
	public final boolean OpenGL15;
	public final boolean OpenGL20;
	public final boolean OpenGL21;
	public final boolean OpenGL30;
	public final boolean OpenGL31;
	public final boolean OpenGL32;
	public final boolean OpenGL33;
	public final boolean OpenGL40;
	public final boolean OpenGL41;
	public final boolean OpenGL42;
	public final boolean OpenGL43;
	public final boolean OpenGL44;
	public final boolean OpenGL45;
	public final boolean GL_GREMEDY_frame_terminator;
	public final boolean GL_GREMEDY_string_marker;
	public final boolean GL_HP_occlusion_test;
	public final boolean GL_IBM_rasterpos_clip;
	public final boolean GL_INTEL_map_texture;
	public final boolean GL_KHR_context_flush_control;
	public final boolean GL_KHR_debug;
	public final boolean GL_KHR_robust_buffer_access_behavior;
	public final boolean GL_KHR_robustness;
	public final boolean GL_KHR_texture_compression_astc_ldr;
	public final boolean GL_NVX_gpu_memory_info;
	public final boolean GL_NV_bindless_multi_draw_indirect;
	public final boolean GL_NV_bindless_texture;
	public final boolean GL_NV_blend_equation_advanced;
	public final boolean GL_NV_blend_square;
	public final boolean GL_NV_compute_program5;
	public final boolean GL_NV_conditional_render;
	public final boolean GL_NV_copy_depth_to_color;
	public final boolean GL_NV_copy_image;
	public final boolean GL_NV_deep_texture3D;
	public final boolean GL_NV_depth_buffer_float;
	public final boolean GL_NV_depth_clamp;
	public final boolean GL_NV_draw_texture;
	public final boolean GL_NV_evaluators;
	public final boolean GL_NV_explicit_multisample;
	public final boolean GL_NV_fence;
	public final boolean GL_NV_float_buffer;
	public final boolean GL_NV_fog_distance;
	public final boolean GL_NV_fragment_program;
	public final boolean GL_NV_fragment_program2;
	public final boolean GL_NV_fragment_program4;
	public final boolean GL_NV_fragment_program_option;
	public final boolean GL_NV_framebuffer_multisample_coverage;
	public final boolean GL_NV_geometry_program4;
	public final boolean GL_NV_geometry_shader4;
	public final boolean GL_NV_gpu_program4;
	public final boolean GL_NV_gpu_program5;
	public final boolean GL_NV_gpu_program5_mem_extended;
	public final boolean GL_NV_gpu_shader5;
	public final boolean GL_NV_half_float;
	public final boolean GL_NV_light_max_exponent;
	public final boolean GL_NV_multisample_coverage;
	public final boolean GL_NV_multisample_filter_hint;
	public final boolean GL_NV_occlusion_query;
	public final boolean GL_NV_packed_depth_stencil;
	public final boolean GL_NV_parameter_buffer_object;
	public final boolean GL_NV_parameter_buffer_object2;
	public final boolean GL_NV_path_rendering;
	public final boolean GL_NV_pixel_data_range;
	public final boolean GL_NV_point_sprite;
	public final boolean GL_NV_present_video;
	public final boolean GL_NV_primitive_restart;
	public final boolean GL_NV_register_combiners;
	public final boolean GL_NV_register_combiners2;
	public final boolean GL_NV_shader_atomic_counters;
	public final boolean GL_NV_shader_atomic_float;
	public final boolean GL_NV_shader_buffer_load;
	public final boolean GL_NV_shader_buffer_store;
	public final boolean GL_NV_shader_storage_buffer_object;
	public final boolean GL_NV_tessellation_program5;
	public final boolean GL_NV_texgen_reflection;
	public final boolean GL_NV_texture_barrier;
	public final boolean GL_NV_texture_compression_vtc;
	public final boolean GL_NV_texture_env_combine4;
	public final boolean GL_NV_texture_expand_normal;
	public final boolean GL_NV_texture_multisample;
	public final boolean GL_NV_texture_rectangle;
	public final boolean GL_NV_texture_shader;
	public final boolean GL_NV_texture_shader2;
	public final boolean GL_NV_texture_shader3;
	public final boolean GL_NV_transform_feedback;
	public final boolean GL_NV_transform_feedback2;
	public final boolean GL_NV_vertex_array_range;
	public final boolean GL_NV_vertex_array_range2;
	public final boolean GL_NV_vertex_attrib_integer_64bit;
	public final boolean GL_NV_vertex_buffer_unified_memory;
	public final boolean GL_NV_vertex_program;
	public final boolean GL_NV_vertex_program1_1;
	public final boolean GL_NV_vertex_program2;
	public final boolean GL_NV_vertex_program2_option;
	public final boolean GL_NV_vertex_program3;
	public final boolean GL_NV_vertex_program4;
	public final boolean GL_NV_video_capture;
	public final boolean GL_SGIS_generate_mipmap;
	public final boolean GL_SGIS_texture_lod;
	public final boolean GL_SUN_slice_accum;

	// AMD_debug_output
	long glDebugMessageEnableAMD;
	long glDebugMessageInsertAMD;
	long glDebugMessageCallbackAMD;
	long glGetDebugMessageLogAMD;
	// AMD_draw_buffers_blend
	long glBlendFuncIndexedAMD;
	long glBlendFuncSeparateIndexedAMD;
	long glBlendEquationIndexedAMD;
	long glBlendEquationSeparateIndexedAMD;
	// AMD_interleaved_elements
	long glVertexAttribParameteriAMD;
	// AMD_multi_draw_indirect
	long glMultiDrawArraysIndirectAMD;
	long glMultiDrawElementsIndirectAMD;
	// AMD_name_gen_delete
	long glGenNamesAMD;
	long glDeleteNamesAMD;
	long glIsNameAMD;
	// AMD_performance_monitor
	long glGetPerfMonitorGroupsAMD;
	long glGetPerfMonitorCountersAMD;
	long glGetPerfMonitorGroupStringAMD;
	long glGetPerfMonitorCounterStringAMD;
	long glGetPerfMonitorCounterInfoAMD;
	long glGenPerfMonitorsAMD;
	long glDeletePerfMonitorsAMD;
	long glSelectPerfMonitorCountersAMD;
	long glBeginPerfMonitorAMD;
	long glEndPerfMonitorAMD;
	long glGetPerfMonitorCounterDataAMD;
	// AMD_sample_positions
	long glSetMultisamplefvAMD;
	// AMD_sparse_texture
	long glTexStorageSparseAMD;
	long glTextureStorageSparseAMD;
	// AMD_stencil_operation_extended
	long glStencilOpValueAMD;
	// AMD_vertex_shader_tessellator
	long glTessellationFactorAMD;
	long glTessellationModeAMD;
	// APPLE_element_array
	long glElementPointerAPPLE;
	long glDrawElementArrayAPPLE;
	long glDrawRangeElementArrayAPPLE;
	long glMultiDrawElementArrayAPPLE;
	long glMultiDrawRangeElementArrayAPPLE;
	// APPLE_fence
	long glGenFencesAPPLE;
	long glDeleteFencesAPPLE;
	long glSetFenceAPPLE;
	long glIsFenceAPPLE;
	long glTestFenceAPPLE;
	long glFinishFenceAPPLE;
	long glTestObjectAPPLE;
	long glFinishObjectAPPLE;
	// APPLE_flush_buffer_range
	long glBufferParameteriAPPLE;
	long glFlushMappedBufferRangeAPPLE;
	// APPLE_object_purgeable
	long glObjectPurgeableAPPLE;
	long glObjectUnpurgeableAPPLE;
	long glGetObjectParameterivAPPLE;
	// APPLE_texture_range
	long glTextureRangeAPPLE;
	long glGetTexParameterPointervAPPLE;
	// APPLE_vertex_array_object
	long glBindVertexArrayAPPLE;
	long glDeleteVertexArraysAPPLE;
	long glGenVertexArraysAPPLE;
	long glIsVertexArrayAPPLE;
	// APPLE_vertex_array_range
	long glVertexArrayRangeAPPLE;
	long glFlushVertexArrayRangeAPPLE;
	long glVertexArrayParameteriAPPLE;
	// APPLE_vertex_program_evaluators
	long glEnableVertexAttribAPPLE;
	long glDisableVertexAttribAPPLE;
	long glIsVertexAttribEnabledAPPLE;
	long glMapVertexAttrib1dAPPLE;
	long glMapVertexAttrib1fAPPLE;
	long glMapVertexAttrib2dAPPLE;
	long glMapVertexAttrib2fAPPLE;
	// ARB_bindless_texture
	long glGetTextureHandleARB;
	long glGetTextureSamplerHandleARB;
	long glMakeTextureHandleResidentARB;
	long glMakeTextureHandleNonResidentARB;
	long glGetImageHandleARB;
	long glMakeImageHandleResidentARB;
	long glMakeImageHandleNonResidentARB;
	long glUniformHandleui64ARB;
	long glUniformHandleui64vARB;
	long glProgramUniformHandleui64ARB;
	long glProgramUniformHandleui64vARB;
	long glIsTextureHandleResidentARB;
	long glIsImageHandleResidentARB;
	long glVertexAttribL1ui64ARB;
	long glVertexAttribL1ui64vARB;
	long glGetVertexAttribLui64vARB;
	// ARB_buffer_object
	long glBindBufferARB;
	long glDeleteBuffersARB;
	long glGenBuffersARB;
	long glIsBufferARB;
	long glBufferDataARB;
	long glBufferSubDataARB;
	long glGetBufferSubDataARB;
	long glMapBufferARB;
	long glUnmapBufferARB;
	long glGetBufferParameterivARB;
	long glGetBufferPointervARB;
	// ARB_buffer_storage
	long glNamedBufferStorageEXT;
	// ARB_cl_event
	long glCreateSyncFromCLeventARB;
	// ARB_clear_buffer_object
	long glClearNamedBufferDataEXT;
	long glClearNamedBufferSubDataEXT;
	// ARB_color_buffer_float
	long glClampColorARB;
	// ARB_compute_variable_group_size
	long glDispatchComputeGroupSizeARB;
	// ARB_debug_output
	long glDebugMessageControlARB;
	long glDebugMessageInsertARB;
	long glDebugMessageCallbackARB;
	long glGetDebugMessageLogARB;
	// ARB_draw_buffers
	long glDrawBuffersARB;
	// ARB_draw_buffers_blend
	long glBlendEquationiARB;
	long glBlendEquationSeparateiARB;
	long glBlendFunciARB;
	long glBlendFuncSeparateiARB;
	// ARB_draw_instanced
	long glDrawArraysInstancedARB;
	long glDrawElementsInstancedARB;
	// ARB_framebuffer_no_attachments
	long glNamedFramebufferParameteriEXT;
	long glGetNamedFramebufferParameterivEXT;
	// ARB_geometry_shader4
	long glProgramParameteriARB;
	long glFramebufferTextureARB;
	long glFramebufferTextureLayerARB;
	long glFramebufferTextureFaceARB;
	// ARB_gpu_shader_fp64
	long glProgramUniform1dEXT;
	long glProgramUniform2dEXT;
	long glProgramUniform3dEXT;
	long glProgramUniform4dEXT;
	long glProgramUniform1dvEXT;
	long glProgramUniform2dvEXT;
	long glProgramUniform3dvEXT;
	long glProgramUniform4dvEXT;
	long glProgramUniformMatrix2dvEXT;
	long glProgramUniformMatrix3dvEXT;
	long glProgramUniformMatrix4dvEXT;
	long glProgramUniformMatrix2x3dvEXT;
	long glProgramUniformMatrix2x4dvEXT;
	long glProgramUniformMatrix3x2dvEXT;
	long glProgramUniformMatrix3x4dvEXT;
	long glProgramUniformMatrix4x2dvEXT;
	long glProgramUniformMatrix4x3dvEXT;
	// ARB_imaging
	long glColorTable;
	long glColorSubTable;
	long glColorTableParameteriv;
	long glColorTableParameterfv;
	long glCopyColorSubTable;
	long glCopyColorTable;
	long glGetColorTable;
	long glGetColorTableParameteriv;
	long glGetColorTableParameterfv;
	long glHistogram;
	long glResetHistogram;
	long glGetHistogram;
	long glGetHistogramParameterfv;
	long glGetHistogramParameteriv;
	long glMinmax;
	long glResetMinmax;
	long glGetMinmax;
	long glGetMinmaxParameterfv;
	long glGetMinmaxParameteriv;
	long glConvolutionFilter1D;
	long glConvolutionFilter2D;
	long glConvolutionParameterf;
	long glConvolutionParameterfv;
	long glConvolutionParameteri;
	long glConvolutionParameteriv;
	long glCopyConvolutionFilter1D;
	long glCopyConvolutionFilter2D;
	long glGetConvolutionFilter;
	long glGetConvolutionParameterfv;
	long glGetConvolutionParameteriv;
	long glSeparableFilter2D;
	long glGetSeparableFilter;
	// ARB_indirect_parameters
	long glMultiDrawArraysIndirectCountARB;
	long glMultiDrawElementsIndirectCountARB;
	// ARB_instanced_arrays
	long glVertexAttribDivisorARB;
	// ARB_matrix_palette
	long glCurrentPaletteMatrixARB;
	long glMatrixIndexPointerARB;
	long glMatrixIndexubvARB;
	long glMatrixIndexusvARB;
	long glMatrixIndexuivARB;
	// ARB_multisample
	long glSampleCoverageARB;
	// ARB_multitexture
	long glClientActiveTextureARB;
	long glActiveTextureARB;
	long glMultiTexCoord1fARB;
	long glMultiTexCoord1dARB;
	long glMultiTexCoord1iARB;
	long glMultiTexCoord1sARB;
	long glMultiTexCoord2fARB;
	long glMultiTexCoord2dARB;
	long glMultiTexCoord2iARB;
	long glMultiTexCoord2sARB;
	long glMultiTexCoord3fARB;
	long glMultiTexCoord3dARB;
	long glMultiTexCoord3iARB;
	long glMultiTexCoord3sARB;
	long glMultiTexCoord4fARB;
	long glMultiTexCoord4dARB;
	long glMultiTexCoord4iARB;
	long glMultiTexCoord4sARB;
	// ARB_occlusion_query
	long glGenQueriesARB;
	long glDeleteQueriesARB;
	long glIsQueryARB;
	long glBeginQueryARB;
	long glEndQueryARB;
	long glGetQueryivARB;
	long glGetQueryObjectivARB;
	long glGetQueryObjectuivARB;
	// ARB_point_parameters
	long glPointParameterfARB;
	long glPointParameterfvARB;
	// ARB_program
	long glProgramStringARB;
	long glBindProgramARB;
	long glDeleteProgramsARB;
	long glGenProgramsARB;
	long glProgramEnvParameter4fARB;
	long glProgramEnvParameter4dARB;
	long glProgramEnvParameter4fvARB;
	long glProgramEnvParameter4dvARB;
	long glProgramLocalParameter4fARB;
	long glProgramLocalParameter4dARB;
	long glProgramLocalParameter4fvARB;
	long glProgramLocalParameter4dvARB;
	long glGetProgramEnvParameterfvARB;
	long glGetProgramEnvParameterdvARB;
	long glGetProgramLocalParameterfvARB;
	long glGetProgramLocalParameterdvARB;
	long glGetProgramivARB;
	long glGetProgramStringARB;
	long glIsProgramARB;
	// ARB_robustness
	long glGetGraphicsResetStatusARB;
	long glGetnMapdvARB;
	long glGetnMapfvARB;
	long glGetnMapivARB;
	long glGetnPixelMapfvARB;
	long glGetnPixelMapuivARB;
	long glGetnPixelMapusvARB;
	long glGetnPolygonStippleARB;
	long glGetnTexImageARB;
	long glReadnPixelsARB;
	long glGetnColorTableARB;
	long glGetnConvolutionFilterARB;
	long glGetnSeparableFilterARB;
	long glGetnHistogramARB;
	long glGetnMinmaxARB;
	long glGetnCompressedTexImageARB;
	long glGetnUniformfvARB;
	long glGetnUniformivARB;
	long glGetnUniformuivARB;
	long glGetnUniformdvARB;
	// ARB_sample_shading
	long glMinSampleShadingARB;
	// ARB_shader_objects
	long glDeleteObjectARB;
	long glGetHandleARB;
	long glDetachObjectARB;
	long glCreateShaderObjectARB;
	long glShaderSourceARB;
	long glCompileShaderARB;
	long glCreateProgramObjectARB;
	long glAttachObjectARB;
	long glLinkProgramARB;
	long glUseProgramObjectARB;
	long glValidateProgramARB;
	long glUniform1fARB;
	long glUniform2fARB;
	long glUniform3fARB;
	long glUniform4fARB;
	long glUniform1iARB;
	long glUniform2iARB;
	long glUniform3iARB;
	long glUniform4iARB;
	long glUniform1fvARB;
	long glUniform2fvARB;
	long glUniform3fvARB;
	long glUniform4fvARB;
	long glUniform1ivARB;
	long glUniform2ivARB;
	long glUniform3ivARB;
	long glUniform4ivARB;
	long glUniformMatrix2fvARB;
	long glUniformMatrix3fvARB;
	long glUniformMatrix4fvARB;
	long glGetObjectParameterfvARB;
	long glGetObjectParameterivARB;
	long glGetInfoLogARB;
	long glGetAttachedObjectsARB;
	long glGetUniformLocationARB;
	long glGetActiveUniformARB;
	long glGetUniformfvARB;
	long glGetUniformivARB;
	long glGetShaderSourceARB;
	// ARB_shading_language_include
	long glNamedStringARB;
	long glDeleteNamedStringARB;
	long glCompileShaderIncludeARB;
	long glIsNamedStringARB;
	long glGetNamedStringARB;
	long glGetNamedStringivARB;
	// ARB_sparse_buffer
	long glBufferPageCommitmentARB;
	// ARB_sparse_texture
	long glTexPageCommitmentARB;
	long glTexturePageCommitmentEXT;
	// ARB_texture_buffer_object
	long glTexBufferARB;
	// ARB_texture_buffer_range
	long glTextureBufferRangeEXT;
	// ARB_texture_compression
	long glCompressedTexImage1DARB;
	long glCompressedTexImage2DARB;
	long glCompressedTexImage3DARB;
	long glCompressedTexSubImage1DARB;
	long glCompressedTexSubImage2DARB;
	long glCompressedTexSubImage3DARB;
	long glGetCompressedTexImageARB;
	// ARB_texture_storage
	long glTextureStorage1DEXT;
	long glTextureStorage2DEXT;
	long glTextureStorage3DEXT;
	// ARB_texture_storage_multisample
	long glTextureStorage2DMultisampleEXT;
	long glTextureStorage3DMultisampleEXT;
	// ARB_transpose_matrix
	long glLoadTransposeMatrixfARB;
	long glMultTransposeMatrixfARB;
	// ARB_vertex_attrib_64bit
	long glVertexArrayVertexAttribLOffsetEXT;
	// ARB_vertex_blend
	long glWeightbvARB;
	long glWeightsvARB;
	long glWeightivARB;
	long glWeightfvARB;
	long glWeightdvARB;
	long glWeightubvARB;
	long glWeightusvARB;
	long glWeightuivARB;
	long glWeightPointerARB;
	long glVertexBlendARB;
	// ARB_vertex_shader
	long glVertexAttrib1sARB;
	long glVertexAttrib1fARB;
	long glVertexAttrib1dARB;
	long glVertexAttrib2sARB;
	long glVertexAttrib2fARB;
	long glVertexAttrib2dARB;
	long glVertexAttrib3sARB;
	long glVertexAttrib3fARB;
	long glVertexAttrib3dARB;
	long glVertexAttrib4sARB;
	long glVertexAttrib4fARB;
	long glVertexAttrib4dARB;
	long glVertexAttrib4NubARB;
	long glVertexAttribPointerARB;
	long glEnableVertexAttribArrayARB;
	long glDisableVertexAttribArrayARB;
	long glBindAttribLocationARB;
	long glGetActiveAttribARB;
	long glGetAttribLocationARB;
	long glGetVertexAttribfvARB;
	long glGetVertexAttribdvARB;
	long glGetVertexAttribivARB;
	long glGetVertexAttribPointervARB;
	// ARB_window_pos
	long glWindowPos2fARB;
	long glWindowPos2dARB;
	long glWindowPos2iARB;
	long glWindowPos2sARB;
	long glWindowPos3fARB;
	long glWindowPos3dARB;
	long glWindowPos3iARB;
	long glWindowPos3sARB;
	// ATI_draw_buffers
	long glDrawBuffersATI;
	// ATI_element_array
	long glElementPointerATI;
	long glDrawElementArrayATI;
	long glDrawRangeElementArrayATI;
	// ATI_envmap_bumpmap
	long glTexBumpParameterfvATI;
	long glTexBumpParameterivATI;
	long glGetTexBumpParameterfvATI;
	long glGetTexBumpParameterivATI;
	// ATI_fragment_shader
	long glGenFragmentShadersATI;
	long glBindFragmentShaderATI;
	long glDeleteFragmentShaderATI;
	long glBeginFragmentShaderATI;
	long glEndFragmentShaderATI;
	long glPassTexCoordATI;
	long glSampleMapATI;
	long glColorFragmentOp1ATI;
	long glColorFragmentOp2ATI;
	long glColorFragmentOp3ATI;
	long glAlphaFragmentOp1ATI;
	long glAlphaFragmentOp2ATI;
	long glAlphaFragmentOp3ATI;
	long glSetFragmentShaderConstantATI;
	// ATI_map_object_buffer
	long glMapObjectBufferATI;
	long glUnmapObjectBufferATI;
	// ATI_pn_triangles
	long glPNTrianglesfATI;
	long glPNTrianglesiATI;
	// ATI_separate_stencil
	long glStencilOpSeparateATI;
	long glStencilFuncSeparateATI;
	// ATI_vertex_array_object
	long glNewObjectBufferATI;
	long glIsObjectBufferATI;
	long glUpdateObjectBufferATI;
	long glGetObjectBufferfvATI;
	long glGetObjectBufferivATI;
	long glFreeObjectBufferATI;
	long glArrayObjectATI;
	long glGetArrayObjectfvATI;
	long glGetArrayObjectivATI;
	long glVariantArrayObjectATI;
	long glGetVariantArrayObjectfvATI;
	long glGetVariantArrayObjectivATI;
	// ATI_vertex_attrib_array_object
	long glVertexAttribArrayObjectATI;
	long glGetVertexAttribArrayObjectfvATI;
	long glGetVertexAttribArrayObjectivATI;
	// ATI_vertex_streams
	long glVertexStream2fATI;
	long glVertexStream2dATI;
	long glVertexStream2iATI;
	long glVertexStream2sATI;
	long glVertexStream3fATI;
	long glVertexStream3dATI;
	long glVertexStream3iATI;
	long glVertexStream3sATI;
	long glVertexStream4fATI;
	long glVertexStream4dATI;
	long glVertexStream4iATI;
	long glVertexStream4sATI;
	long glNormalStream3bATI;
	long glNormalStream3fATI;
	long glNormalStream3dATI;
	long glNormalStream3iATI;
	long glNormalStream3sATI;
	long glClientActiveVertexStreamATI;
	long glVertexBlendEnvfATI;
	long glVertexBlendEnviATI;
	// EXT_bindable_uniform
	long glUniformBufferEXT;
	long glGetUniformBufferSizeEXT;
	long glGetUniformOffsetEXT;
	// EXT_blend_color
	long glBlendColorEXT;
	// EXT_blend_equation_separate
	long glBlendEquationSeparateEXT;
	// EXT_blend_func_separate
	long glBlendFuncSeparateEXT;
	// EXT_blend_minmax
	long glBlendEquationEXT;
	// EXT_compiled_vertex_array
	long glLockArraysEXT;
	long glUnlockArraysEXT;
	// EXT_depth_bounds_test
	long glDepthBoundsEXT;
	// EXT_direct_state_access
	long glClientAttribDefaultEXT;
	long glPushClientAttribDefaultEXT;
	long glMatrixLoadfEXT;
	long glMatrixLoaddEXT;
	long glMatrixMultfEXT;
	long glMatrixMultdEXT;
	long glMatrixLoadIdentityEXT;
	long glMatrixRotatefEXT;
	long glMatrixRotatedEXT;
	long glMatrixScalefEXT;
	long glMatrixScaledEXT;
	long glMatrixTranslatefEXT;
	long glMatrixTranslatedEXT;
	long glMatrixOrthoEXT;
	long glMatrixFrustumEXT;
	long glMatrixPushEXT;
	long glMatrixPopEXT;
	long glTextureParameteriEXT;
	long glTextureParameterivEXT;
	long glTextureParameterfEXT;
	long glTextureParameterfvEXT;
	long glTextureImage1DEXT;
	long glTextureImage2DEXT;
	long glTextureSubImage1DEXT;
	long glTextureSubImage2DEXT;
	long glCopyTextureImage1DEXT;
	long glCopyTextureImage2DEXT;
	long glCopyTextureSubImage1DEXT;
	long glCopyTextureSubImage2DEXT;
	long glGetTextureImageEXT;
	long glGetTextureParameterfvEXT;
	long glGetTextureParameterivEXT;
	long glGetTextureLevelParameterfvEXT;
	long glGetTextureLevelParameterivEXT;
	long glTextureImage3DEXT;
	long glTextureSubImage3DEXT;
	long glCopyTextureSubImage3DEXT;
	long glBindMultiTextureEXT;
	long glMultiTexCoordPointerEXT;
	long glMultiTexEnvfEXT;
	long glMultiTexEnvfvEXT;
	long glMultiTexEnviEXT;
	long glMultiTexEnvivEXT;
	long glMultiTexGendEXT;
	long glMultiTexGendvEXT;
	long glMultiTexGenfEXT;
	long glMultiTexGenfvEXT;
	long glMultiTexGeniEXT;
	long glMultiTexGenivEXT;
	long glGetMultiTexEnvfvEXT;
	long glGetMultiTexEnvivEXT;
	long glGetMultiTexGendvEXT;
	long glGetMultiTexGenfvEXT;
	long glGetMultiTexGenivEXT;
	long glMultiTexParameteriEXT;
	long glMultiTexParameterivEXT;
	long glMultiTexParameterfEXT;
	long glMultiTexParameterfvEXT;
	long glMultiTexImage1DEXT;
	long glMultiTexImage2DEXT;
	long glMultiTexSubImage1DEXT;
	long glMultiTexSubImage2DEXT;
	long glCopyMultiTexImage1DEXT;
	long glCopyMultiTexImage2DEXT;
	long glCopyMultiTexSubImage1DEXT;
	long glCopyMultiTexSubImage2DEXT;
	long glGetMultiTexImageEXT;
	long glGetMultiTexParameterfvEXT;
	long glGetMultiTexParameterivEXT;
	long glGetMultiTexLevelParameterfvEXT;
	long glGetMultiTexLevelParameterivEXT;
	long glMultiTexImage3DEXT;
	long glMultiTexSubImage3DEXT;
	long glCopyMultiTexSubImage3DEXT;
	long glEnableClientStateIndexedEXT;
	long glDisableClientStateIndexedEXT;
	long glEnableClientStateiEXT;
	long glDisableClientStateiEXT;
	long glGetFloatIndexedvEXT;
	long glGetDoubleIndexedvEXT;
	long glGetPointerIndexedvEXT;
	long glGetFloati_vEXT;
	long glGetDoublei_vEXT;
	long glGetPointeri_vEXT;
	long glNamedProgramStringEXT;
	long glNamedProgramLocalParameter4dEXT;
	long glNamedProgramLocalParameter4dvEXT;
	long glNamedProgramLocalParameter4fEXT;
	long glNamedProgramLocalParameter4fvEXT;
	long glGetNamedProgramLocalParameterdvEXT;
	long glGetNamedProgramLocalParameterfvEXT;
	long glGetNamedProgramivEXT;
	long glGetNamedProgramStringEXT;
	long glCompressedTextureImage3DEXT;
	long glCompressedTextureImage2DEXT;
	long glCompressedTextureImage1DEXT;
	long glCompressedTextureSubImage3DEXT;
	long glCompressedTextureSubImage2DEXT;
	long glCompressedTextureSubImage1DEXT;
	long glGetCompressedTextureImageEXT;
	long glCompressedMultiTexImage3DEXT;
	long glCompressedMultiTexImage2DEXT;
	long glCompressedMultiTexImage1DEXT;
	long glCompressedMultiTexSubImage3DEXT;
	long glCompressedMultiTexSubImage2DEXT;
	long glCompressedMultiTexSubImage1DEXT;
	long glGetCompressedMultiTexImageEXT;
	long glMatrixLoadTransposefEXT;
	long glMatrixLoadTransposedEXT;
	long glMatrixMultTransposefEXT;
	long glMatrixMultTransposedEXT;
	long glNamedBufferDataEXT;
	long glNamedBufferSubDataEXT;
	long glMapNamedBufferEXT;
	long glUnmapNamedBufferEXT;
	long glGetNamedBufferParameterivEXT;
	long glGetNamedBufferPointervEXT;
	long glGetNamedBufferSubDataEXT;
	long glProgramUniform1fEXT;
	long glProgramUniform2fEXT;
	long glProgramUniform3fEXT;
	long glProgramUniform4fEXT;
	long glProgramUniform1iEXT;
	long glProgramUniform2iEXT;
	long glProgramUniform3iEXT;
	long glProgramUniform4iEXT;
	long glProgramUniform1fvEXT;
	long glProgramUniform2fvEXT;
	long glProgramUniform3fvEXT;
	long glProgramUniform4fvEXT;
	long glProgramUniform1ivEXT;
	long glProgramUniform2ivEXT;
	long glProgramUniform3ivEXT;
	long glProgramUniform4ivEXT;
	long glProgramUniformMatrix2fvEXT;
	long glProgramUniformMatrix3fvEXT;
	long glProgramUniformMatrix4fvEXT;
	long glProgramUniformMatrix2x3fvEXT;
	long glProgramUniformMatrix3x2fvEXT;
	long glProgramUniformMatrix2x4fvEXT;
	long glProgramUniformMatrix4x2fvEXT;
	long glProgramUniformMatrix3x4fvEXT;
	long glProgramUniformMatrix4x3fvEXT;
	long glTextureBufferEXT;
	long glMultiTexBufferEXT;
	long glTextureParameterIivEXT;
	long glTextureParameterIuivEXT;
	long glGetTextureParameterIivEXT;
	long glGetTextureParameterIuivEXT;
	long glMultiTexParameterIivEXT;
	long glMultiTexParameterIuivEXT;
	long glGetMultiTexParameterIivEXT;
	long glGetMultiTexParameterIuivEXT;
	long glProgramUniform1uiEXT;
	long glProgramUniform2uiEXT;
	long glProgramUniform3uiEXT;
	long glProgramUniform4uiEXT;
	long glProgramUniform1uivEXT;
	long glProgramUniform2uivEXT;
	long glProgramUniform3uivEXT;
	long glProgramUniform4uivEXT;
	long glNamedProgramLocalParameters4fvEXT;
	long glNamedProgramLocalParameterI4iEXT;
	long glNamedProgramLocalParameterI4ivEXT;
	long glNamedProgramLocalParametersI4ivEXT;
	long glNamedProgramLocalParameterI4uiEXT;
	long glNamedProgramLocalParameterI4uivEXT;
	long glNamedProgramLocalParametersI4uivEXT;
	long glGetNamedProgramLocalParameterIivEXT;
	long glGetNamedProgramLocalParameterIuivEXT;
	long glNamedRenderbufferStorageEXT;
	long glGetNamedRenderbufferParameterivEXT;
	long glNamedRenderbufferStorageMultisampleEXT;
	long glNamedRenderbufferStorageMultisampleCoverageEXT;
	long glCheckNamedFramebufferStatusEXT;
	long glNamedFramebufferTexture1DEXT;
	long glNamedFramebufferTexture2DEXT;
	long glNamedFramebufferTexture3DEXT;
	long glNamedFramebufferRenderbufferEXT;
	long glGetNamedFramebufferAttachmentParameterivEXT;
	long glGenerateTextureMipmapEXT;
	long glGenerateMultiTexMipmapEXT;
	long glFramebufferDrawBufferEXT;
	long glFramebufferDrawBuffersEXT;
	long glFramebufferReadBufferEXT;
	long glGetFramebufferParameterivEXT;
	long glNamedCopyBufferSubDataEXT;
	long glNamedFramebufferTextureEXT;
	long glNamedFramebufferTextureLayerEXT;
	long glNamedFramebufferTextureFaceEXT;
	long glTextureRenderbufferEXT;
	long glMultiTexRenderbufferEXT;
	long glVertexArrayVertexOffsetEXT;
	long glVertexArrayColorOffsetEXT;
	long glVertexArrayEdgeFlagOffsetEXT;
	long glVertexArrayIndexOffsetEXT;
	long glVertexArrayNormalOffsetEXT;
	long glVertexArrayTexCoordOffsetEXT;
	long glVertexArrayMultiTexCoordOffsetEXT;
	long glVertexArrayFogCoordOffsetEXT;
	long glVertexArraySecondaryColorOffsetEXT;
	long glVertexArrayVertexAttribOffsetEXT;
	long glVertexArrayVertexAttribIOffsetEXT;
	long glEnableVertexArrayEXT;
	long glDisableVertexArrayEXT;
	long glEnableVertexArrayAttribEXT;
	long glDisableVertexArrayAttribEXT;
	long glGetVertexArrayIntegervEXT;
	long glGetVertexArrayPointervEXT;
	long glGetVertexArrayIntegeri_vEXT;
	long glGetVertexArrayPointeri_vEXT;
	long glMapNamedBufferRangeEXT;
	long glFlushMappedNamedBufferRangeEXT;
	// EXT_draw_buffers2
	long glColorMaskIndexedEXT;
	long glGetBooleanIndexedvEXT;
	long glGetIntegerIndexedvEXT;
	long glEnableIndexedEXT;
	long glDisableIndexedEXT;
	long glIsEnabledIndexedEXT;
	// EXT_draw_instanced
	long glDrawArraysInstancedEXT;
	long glDrawElementsInstancedEXT;
	// EXT_draw_range_elements
	long glDrawRangeElementsEXT;
	// EXT_fog_coord
	long glFogCoordfEXT;
	long glFogCoorddEXT;
	long glFogCoordPointerEXT;
	// EXT_framebuffer_blit
	long glBlitFramebufferEXT;
	// EXT_framebuffer_multisample
	long glRenderbufferStorageMultisampleEXT;
	// EXT_framebuffer_object
	long glIsRenderbufferEXT;
	long glBindRenderbufferEXT;
	long glDeleteRenderbuffersEXT;
	long glGenRenderbuffersEXT;
	long glRenderbufferStorageEXT;
	long glGetRenderbufferParameterivEXT;
	long glIsFramebufferEXT;
	long glBindFramebufferEXT;
	long glDeleteFramebuffersEXT;
	long glGenFramebuffersEXT;
	long glCheckFramebufferStatusEXT;
	long glFramebufferTexture1DEXT;
	long glFramebufferTexture2DEXT;
	long glFramebufferTexture3DEXT;
	long glFramebufferRenderbufferEXT;
	long glGetFramebufferAttachmentParameterivEXT;
	long glGenerateMipmapEXT;
	// EXT_geometry_shader4
	long glProgramParameteriEXT;
	long glFramebufferTextureEXT;
	long glFramebufferTextureLayerEXT;
	long glFramebufferTextureFaceEXT;
	// EXT_gpu_program_parameters
	long glProgramEnvParameters4fvEXT;
	long glProgramLocalParameters4fvEXT;
	// EXT_gpu_shader4
	long glVertexAttribI1iEXT;
	long glVertexAttribI2iEXT;
	long glVertexAttribI3iEXT;
	long glVertexAttribI4iEXT;
	long glVertexAttribI1uiEXT;
	long glVertexAttribI2uiEXT;
	long glVertexAttribI3uiEXT;
	long glVertexAttribI4uiEXT;
	long glVertexAttribI1ivEXT;
	long glVertexAttribI2ivEXT;
	long glVertexAttribI3ivEXT;
	long glVertexAttribI4ivEXT;
	long glVertexAttribI1uivEXT;
	long glVertexAttribI2uivEXT;
	long glVertexAttribI3uivEXT;
	long glVertexAttribI4uivEXT;
	long glVertexAttribI4bvEXT;
	long glVertexAttribI4svEXT;
	long glVertexAttribI4ubvEXT;
	long glVertexAttribI4usvEXT;
	long glVertexAttribIPointerEXT;
	long glGetVertexAttribIivEXT;
	long glGetVertexAttribIuivEXT;
	long glUniform1uiEXT;
	long glUniform2uiEXT;
	long glUniform3uiEXT;
	long glUniform4uiEXT;
	long glUniform1uivEXT;
	long glUniform2uivEXT;
	long glUniform3uivEXT;
	long glUniform4uivEXT;
	long glGetUniformuivEXT;
	long glBindFragDataLocationEXT;
	long glGetFragDataLocationEXT;
	// EXT_multi_draw_arrays
	long glMultiDrawArraysEXT;
	// EXT_paletted_texture
	long glColorTableEXT;
	long glColorSubTableEXT;
	long glGetColorTableEXT;
	long glGetColorTableParameterivEXT;
	long glGetColorTableParameterfvEXT;
	// EXT_point_parameters
	long glPointParameterfEXT;
	long glPointParameterfvEXT;
	// EXT_provoking_vertex
	long glProvokingVertexEXT;
	// EXT_secondary_color
	long glSecondaryColor3bEXT;
	long glSecondaryColor3fEXT;
	long glSecondaryColor3dEXT;
	long glSecondaryColor3ubEXT;
	long glSecondaryColorPointerEXT;
	// EXT_separate_shader_objects
	long glUseShaderProgramEXT;
	long glActiveProgramEXT;
	long glCreateShaderProgramEXT;
	// EXT_shader_image_load_store
	long glBindImageTextureEXT;
	long glMemoryBarrierEXT;
	// EXT_stencil_clear_tag
	long glStencilClearTagEXT;
	// EXT_stencil_two_side
	long glActiveStencilFaceEXT;
	// EXT_texture_buffer_object
	long glTexBufferEXT;
	// EXT_texture_integer
	long glClearColorIiEXT;
	long glClearColorIuiEXT;
	long glTexParameterIivEXT;
	long glTexParameterIuivEXT;
	long glGetTexParameterIivEXT;
	long glGetTexParameterIuivEXT;
	// EXT_timer_query
	long glGetQueryObjecti64vEXT;
	long glGetQueryObjectui64vEXT;
	// EXT_transform_feedback
	long glBindBufferRangeEXT;
	long glBindBufferOffsetEXT;
	long glBindBufferBaseEXT;
	long glBeginTransformFeedbackEXT;
	long glEndTransformFeedbackEXT;
	long glTransformFeedbackVaryingsEXT;
	long glGetTransformFeedbackVaryingEXT;
	// EXT_vertex_attrib_64bit
	long glVertexAttribL1dEXT;
	long glVertexAttribL2dEXT;
	long glVertexAttribL3dEXT;
	long glVertexAttribL4dEXT;
	long glVertexAttribL1dvEXT;
	long glVertexAttribL2dvEXT;
	long glVertexAttribL3dvEXT;
	long glVertexAttribL4dvEXT;
	long glVertexAttribLPointerEXT;
	long glGetVertexAttribLdvEXT;
	// EXT_vertex_shader
	long glBeginVertexShaderEXT;
	long glEndVertexShaderEXT;
	long glBindVertexShaderEXT;
	long glGenVertexShadersEXT;
	long glDeleteVertexShaderEXT;
	long glShaderOp1EXT;
	long glShaderOp2EXT;
	long glShaderOp3EXT;
	long glSwizzleEXT;
	long glWriteMaskEXT;
	long glInsertComponentEXT;
	long glExtractComponentEXT;
	long glGenSymbolsEXT;
	long glSetInvariantEXT;
	long glSetLocalConstantEXT;
	long glVariantbvEXT;
	long glVariantsvEXT;
	long glVariantivEXT;
	long glVariantfvEXT;
	long glVariantdvEXT;
	long glVariantubvEXT;
	long glVariantusvEXT;
	long glVariantuivEXT;
	long glVariantPointerEXT;
	long glEnableVariantClientStateEXT;
	long glDisableVariantClientStateEXT;
	long glBindLightParameterEXT;
	long glBindMaterialParameterEXT;
	long glBindTexGenParameterEXT;
	long glBindTextureUnitParameterEXT;
	long glBindParameterEXT;
	long glIsVariantEnabledEXT;
	long glGetVariantBooleanvEXT;
	long glGetVariantIntegervEXT;
	long glGetVariantFloatvEXT;
	long glGetVariantPointervEXT;
	long glGetInvariantBooleanvEXT;
	long glGetInvariantIntegervEXT;
	long glGetInvariantFloatvEXT;
	long glGetLocalConstantBooleanvEXT;
	long glGetLocalConstantIntegervEXT;
	long glGetLocalConstantFloatvEXT;
	// EXT_vertex_weighting
	long glVertexWeightfEXT;
	long glVertexWeightPointerEXT;
	// GL11
	long glAccum;
	long glAlphaFunc;
	long glClearColor;
	long glClearAccum;
	long glClear;
	long glCallLists;
	long glCallList;
	long glBlendFunc;
	long glBitmap;
	long glBindTexture;
	long glPrioritizeTextures;
	long glAreTexturesResident;
	long glBegin;
	long glEnd;
	long glArrayElement;
	long glClearDepth;
	long glDeleteLists;
	long glDeleteTextures;
	long glCullFace;
	long glCopyTexSubImage2D;
	long glCopyTexSubImage1D;
	long glCopyTexImage2D;
	long glCopyTexImage1D;
	long glCopyPixels;
	long glColorPointer;
	long glColorMaterial;
	long glColorMask;
	long glColor3b;
	long glColor3f;
	long glColor3d;
	long glColor3ub;
	long glColor4b;
	long glColor4f;
	long glColor4d;
	long glColor4ub;
	long glClipPlane;
	long glClearStencil;
	long glEvalPoint1;
	long glEvalPoint2;
	long glEvalMesh1;
	long glEvalMesh2;
	long glEvalCoord1f;
	long glEvalCoord1d;
	long glEvalCoord2f;
	long glEvalCoord2d;
	long glEnableClientState;
	long glDisableClientState;
	long glEnable;
	long glDisable;
	long glEdgeFlagPointer;
	long glEdgeFlag;
	long glDrawPixels;
	long glDrawElements;
	long glDrawBuffer;
	long glDrawArrays;
	long glDepthRange;
	long glDepthMask;
	long glDepthFunc;
	long glFeedbackBuffer;
	long glGetPixelMapfv;
	long glGetPixelMapuiv;
	long glGetPixelMapusv;
	long glGetMaterialfv;
	long glGetMaterialiv;
	long glGetMapfv;
	long glGetMapdv;
	long glGetMapiv;
	long glGetLightfv;
	long glGetLightiv;
	long glGetError;
	long glGetClipPlane;
	long glGetBooleanv;
	long glGetDoublev;
	long glGetFloatv;
	long glGetIntegerv;
	long glGenTextures;
	long glGenLists;
	long glFrustum;
	long glFrontFace;
	long glFogf;
	long glFogi;
	long glFogfv;
	long glFogiv;
	long glFlush;
	long glFinish;
	long glGetPointerv;
	long glIsEnabled;
	long glInterleavedArrays;
	long glInitNames;
	long glHint;
	long glGetTexParameterfv;
	long glGetTexParameteriv;
	long glGetTexLevelParameterfv;
	long glGetTexLevelParameteriv;
	long glGetTexImage;
	long glGetTexGeniv;
	long glGetTexGenfv;
	long glGetTexGendv;
	long glGetTexEnviv;
	long glGetTexEnvfv;
	long glGetString;
	long glGetPolygonStipple;
	long glIsList;
	long glMaterialf;
	long glMateriali;
	long glMaterialfv;
	long glMaterialiv;
	long glMapGrid1f;
	long glMapGrid1d;
	long glMapGrid2f;
	long glMapGrid2d;
	long glMap2f;
	long glMap2d;
	long glMap1f;
	long glMap1d;
	long glLogicOp;
	long glLoadName;
	long glLoadMatrixf;
	long glLoadMatrixd;
	long glLoadIdentity;
	long glListBase;
	long glLineWidth;
	long glLineStipple;
	long glLightModelf;
	long glLightModeli;
	long glLightModelfv;
	long glLightModeliv;
	long glLightf;
	long glLighti;
	long glLightfv;
	long glLightiv;
	long glIsTexture;
	long glMatrixMode;
	long glPolygonStipple;
	long glPolygonOffset;
	long glPolygonMode;
	long glPointSize;
	long glPixelZoom;
	long glPixelTransferf;
	long glPixelTransferi;
	long glPixelStoref;
	long glPixelStorei;
	long glPixelMapfv;
	long glPixelMapuiv;
	long glPixelMapusv;
	long glPassThrough;
	long glOrtho;
	long glNormalPointer;
	long glNormal3b;
	long glNormal3f;
	long glNormal3d;
	long glNormal3i;
	long glNewList;
	long glEndList;
	long glMultMatrixf;
	long glMultMatrixd;
	long glShadeModel;
	long glSelectBuffer;
	long glScissor;
	long glScalef;
	long glScaled;
	long glRotatef;
	long glRotated;
	long glRenderMode;
	long glRectf;
	long glRectd;
	long glRecti;
	long glReadPixels;
	long glReadBuffer;
	long glRasterPos2f;
	long glRasterPos2d;
	long glRasterPos2i;
	long glRasterPos3f;
	long glRasterPos3d;
	long glRasterPos3i;
	long glRasterPos4f;
	long glRasterPos4d;
	long glRasterPos4i;
	long glPushName;
	long glPopName;
	long glPushMatrix;
	long glPopMatrix;
	long glPushClientAttrib;
	long glPopClientAttrib;
	long glPushAttrib;
	long glPopAttrib;
	long glStencilFunc;
	long glVertexPointer;
	long glVertex2f;
	long glVertex2d;
	long glVertex2i;
	long glVertex3f;
	long glVertex3d;
	long glVertex3i;
	long glVertex4f;
	long glVertex4d;
	long glVertex4i;
	long glTranslatef;
	long glTranslated;
	long glTexImage1D;
	long glTexImage2D;
	long glTexSubImage1D;
	long glTexSubImage2D;
	long glTexParameterf;
	long glTexParameteri;
	long glTexParameterfv;
	long glTexParameteriv;
	long glTexGenf;
	long glTexGend;
	long glTexGenfv;
	long glTexGendv;
	long glTexGeni;
	long glTexGeniv;
	long glTexEnvf;
	long glTexEnvi;
	long glTexEnvfv;
	long glTexEnviv;
	long glTexCoordPointer;
	long glTexCoord1f;
	long glTexCoord1d;
	long glTexCoord2f;
	long glTexCoord2d;
	long glTexCoord3f;
	long glTexCoord3d;
	long glTexCoord4f;
	long glTexCoord4d;
	long glStencilOp;
	long glStencilMask;
	long glViewport;
	// GL12
	long glDrawRangeElements;
	long glTexImage3D;
	long glTexSubImage3D;
	long glCopyTexSubImage3D;
	// GL13
	long glActiveTexture;
	long glClientActiveTexture;
	long glCompressedTexImage1D;
	long glCompressedTexImage2D;
	long glCompressedTexImage3D;
	long glCompressedTexSubImage1D;
	long glCompressedTexSubImage2D;
	long glCompressedTexSubImage3D;
	long glGetCompressedTexImage;
	long glMultiTexCoord1f;
	long glMultiTexCoord1d;
	long glMultiTexCoord2f;
	long glMultiTexCoord2d;
	long glMultiTexCoord3f;
	long glMultiTexCoord3d;
	long glMultiTexCoord4f;
	long glMultiTexCoord4d;
	long glLoadTransposeMatrixf;
	long glLoadTransposeMatrixd;
	long glMultTransposeMatrixf;
	long glMultTransposeMatrixd;
	long glSampleCoverage;
	// GL14
	long glBlendEquation;
	long glBlendColor;
	long glFogCoordf;
	long glFogCoordd;
	long glFogCoordPointer;
	long glMultiDrawArrays;
	long glPointParameteri;
	long glPointParameterf;
	long glPointParameteriv;
	long glPointParameterfv;
	long glSecondaryColor3b;
	long glSecondaryColor3f;
	long glSecondaryColor3d;
	long glSecondaryColor3ub;
	long glSecondaryColorPointer;
	long glBlendFuncSeparate;
	long glWindowPos2f;
	long glWindowPos2d;
	long glWindowPos2i;
	long glWindowPos3f;
	long glWindowPos3d;
	long glWindowPos3i;
	// GL15
	long glBindBuffer;
	long glDeleteBuffers;
	long glGenBuffers;
	long glIsBuffer;
	long glBufferData;
	long glBufferSubData;
	long glGetBufferSubData;
	long glMapBuffer;
	long glUnmapBuffer;
	long glGetBufferParameteriv;
	long glGetBufferPointerv;
	long glGenQueries;
	long glDeleteQueries;
	long glIsQuery;
	long glBeginQuery;
	long glEndQuery;
	long glGetQueryiv;
	long glGetQueryObjectiv;
	long glGetQueryObjectuiv;
	// GL20
	long glShaderSource;
	long glCreateShader;
	long glIsShader;
	long glCompileShader;
	long glDeleteShader;
	long glCreateProgram;
	long glIsProgram;
	long glAttachShader;
	long glDetachShader;
	long glLinkProgram;
	long glUseProgram;
	long glValidateProgram;
	long glDeleteProgram;
	long glUniform1f;
	long glUniform2f;
	long glUniform3f;
	long glUniform4f;
	long glUniform1i;
	long glUniform2i;
	long glUniform3i;
	long glUniform4i;
	long glUniform1fv;
	long glUniform2fv;
	long glUniform3fv;
	long glUniform4fv;
	long glUniform1iv;
	long glUniform2iv;
	long glUniform3iv;
	long glUniform4iv;
	long glUniformMatrix2fv;
	long glUniformMatrix3fv;
	long glUniformMatrix4fv;
	long glGetShaderiv;
	long glGetProgramiv;
	long glGetShaderInfoLog;
	long glGetProgramInfoLog;
	long glGetAttachedShaders;
	long glGetUniformLocation;
	long glGetActiveUniform;
	long glGetUniformfv;
	long glGetUniformiv;
	long glGetShaderSource;
	long glVertexAttrib1s;
	long glVertexAttrib1f;
	long glVertexAttrib1d;
	long glVertexAttrib2s;
	long glVertexAttrib2f;
	long glVertexAttrib2d;
	long glVertexAttrib3s;
	long glVertexAttrib3f;
	long glVertexAttrib3d;
	long glVertexAttrib4s;
	long glVertexAttrib4f;
	long glVertexAttrib4d;
	long glVertexAttrib4Nub;
	long glVertexAttribPointer;
	long glEnableVertexAttribArray;
	long glDisableVertexAttribArray;
	long glGetVertexAttribfv;
	long glGetVertexAttribdv;
	long glGetVertexAttribiv;
	long glGetVertexAttribPointerv;
	long glBindAttribLocation;
	long glGetActiveAttrib;
	long glGetAttribLocation;
	long glDrawBuffers;
	long glStencilOpSeparate;
	long glStencilFuncSeparate;
	long glStencilMaskSeparate;
	long glBlendEquationSeparate;
	// GL21
	long glUniformMatrix2x3fv;
	long glUniformMatrix3x2fv;
	long glUniformMatrix2x4fv;
	long glUniformMatrix4x2fv;
	long glUniformMatrix3x4fv;
	long glUniformMatrix4x3fv;
	// GL30
	long glGetStringi;
	long glClearBufferfv;
	long glClearBufferiv;
	long glClearBufferuiv;
	long glClearBufferfi;
	long glVertexAttribI1i;
	long glVertexAttribI2i;
	long glVertexAttribI3i;
	long glVertexAttribI4i;
	long glVertexAttribI1ui;
	long glVertexAttribI2ui;
	long glVertexAttribI3ui;
	long glVertexAttribI4ui;
	long glVertexAttribI1iv;
	long glVertexAttribI2iv;
	long glVertexAttribI3iv;
	long glVertexAttribI4iv;
	long glVertexAttribI1uiv;
	long glVertexAttribI2uiv;
	long glVertexAttribI3uiv;
	long glVertexAttribI4uiv;
	long glVertexAttribI4bv;
	long glVertexAttribI4sv;
	long glVertexAttribI4ubv;
	long glVertexAttribI4usv;
	long glVertexAttribIPointer;
	long glGetVertexAttribIiv;
	long glGetVertexAttribIuiv;
	long glUniform1ui;
	long glUniform2ui;
	long glUniform3ui;
	long glUniform4ui;
	long glUniform1uiv;
	long glUniform2uiv;
	long glUniform3uiv;
	long glUniform4uiv;
	long glGetUniformuiv;
	long glBindFragDataLocation;
	long glGetFragDataLocation;
	long glBeginConditionalRender;
	long glEndConditionalRender;
	long glMapBufferRange;
	long glFlushMappedBufferRange;
	long glClampColor;
	long glIsRenderbuffer;
	long glBindRenderbuffer;
	long glDeleteRenderbuffers;
	long glGenRenderbuffers;
	long glRenderbufferStorage;
	long glGetRenderbufferParameteriv;
	long glIsFramebuffer;
	long glBindFramebuffer;
	long glDeleteFramebuffers;
	long glGenFramebuffers;
	long glCheckFramebufferStatus;
	long glFramebufferTexture1D;
	long glFramebufferTexture2D;
	long glFramebufferTexture3D;
	long glFramebufferRenderbuffer;
	long glGetFramebufferAttachmentParameteriv;
	long glGenerateMipmap;
	long glRenderbufferStorageMultisample;
	long glBlitFramebuffer;
	long glTexParameterIiv;
	long glTexParameterIuiv;
	long glGetTexParameterIiv;
	long glGetTexParameterIuiv;
	long glFramebufferTextureLayer;
	long glColorMaski;
	long glGetBooleani_v;
	long glGetIntegeri_v;
	long glEnablei;
	long glDisablei;
	long glIsEnabledi;
	long glBindBufferRange;
	long glBindBufferBase;
	long glBeginTransformFeedback;
	long glEndTransformFeedback;
	long glTransformFeedbackVaryings;
	long glGetTransformFeedbackVarying;
	long glBindVertexArray;
	long glDeleteVertexArrays;
	long glGenVertexArrays;
	long glIsVertexArray;
	// GL31
	long glDrawArraysInstanced;
	long glDrawElementsInstanced;
	long glCopyBufferSubData;
	long glPrimitiveRestartIndex;
	long glTexBuffer;
	long glGetUniformIndices;
	long glGetActiveUniformsiv;
	long glGetActiveUniformName;
	long glGetUniformBlockIndex;
	long glGetActiveUniformBlockiv;
	long glGetActiveUniformBlockName;
	long glUniformBlockBinding;
	// GL32
	long glGetBufferParameteri64v;
	long glDrawElementsBaseVertex;
	long glDrawRangeElementsBaseVertex;
	long glDrawElementsInstancedBaseVertex;
	long glProvokingVertex;
	long glTexImage2DMultisample;
	long glTexImage3DMultisample;
	long glGetMultisamplefv;
	long glSampleMaski;
	long glFramebufferTexture;
	long glFenceSync;
	long glIsSync;
	long glDeleteSync;
	long glClientWaitSync;
	long glWaitSync;
	long glGetInteger64v;
	long glGetInteger64i_v;
	long glGetSynciv;
	// GL33
	long glBindFragDataLocationIndexed;
	long glGetFragDataIndex;
	long glGenSamplers;
	long glDeleteSamplers;
	long glIsSampler;
	long glBindSampler;
	long glSamplerParameteri;
	long glSamplerParameterf;
	long glSamplerParameteriv;
	long glSamplerParameterfv;
	long glSamplerParameterIiv;
	long glSamplerParameterIuiv;
	long glGetSamplerParameteriv;
	long glGetSamplerParameterfv;
	long glGetSamplerParameterIiv;
	long glGetSamplerParameterIuiv;
	long glQueryCounter;
	long glGetQueryObjecti64v;
	long glGetQueryObjectui64v;
	long glVertexAttribDivisor;
	long glVertexP2ui;
	long glVertexP3ui;
	long glVertexP4ui;
	long glVertexP2uiv;
	long glVertexP3uiv;
	long glVertexP4uiv;
	long glTexCoordP1ui;
	long glTexCoordP2ui;
	long glTexCoordP3ui;
	long glTexCoordP4ui;
	long glTexCoordP1uiv;
	long glTexCoordP2uiv;
	long glTexCoordP3uiv;
	long glTexCoordP4uiv;
	long glMultiTexCoordP1ui;
	long glMultiTexCoordP2ui;
	long glMultiTexCoordP3ui;
	long glMultiTexCoordP4ui;
	long glMultiTexCoordP1uiv;
	long glMultiTexCoordP2uiv;
	long glMultiTexCoordP3uiv;
	long glMultiTexCoordP4uiv;
	long glNormalP3ui;
	long glNormalP3uiv;
	long glColorP3ui;
	long glColorP4ui;
	long glColorP3uiv;
	long glColorP4uiv;
	long glSecondaryColorP3ui;
	long glSecondaryColorP3uiv;
	long glVertexAttribP1ui;
	long glVertexAttribP2ui;
	long glVertexAttribP3ui;
	long glVertexAttribP4ui;
	long glVertexAttribP1uiv;
	long glVertexAttribP2uiv;
	long glVertexAttribP3uiv;
	long glVertexAttribP4uiv;
	// GL40
	long glBlendEquationi;
	long glBlendEquationSeparatei;
	long glBlendFunci;
	long glBlendFuncSeparatei;
	long glDrawArraysIndirect;
	long glDrawElementsIndirect;
	long glUniform1d;
	long glUniform2d;
	long glUniform3d;
	long glUniform4d;
	long glUniform1dv;
	long glUniform2dv;
	long glUniform3dv;
	long glUniform4dv;
	long glUniformMatrix2dv;
	long glUniformMatrix3dv;
	long glUniformMatrix4dv;
	long glUniformMatrix2x3dv;
	long glUniformMatrix2x4dv;
	long glUniformMatrix3x2dv;
	long glUniformMatrix3x4dv;
	long glUniformMatrix4x2dv;
	long glUniformMatrix4x3dv;
	long glGetUniformdv;
	long glMinSampleShading;
	long glGetSubroutineUniformLocation;
	long glGetSubroutineIndex;
	long glGetActiveSubroutineUniformiv;
	long glGetActiveSubroutineUniformName;
	long glGetActiveSubroutineName;
	long glUniformSubroutinesuiv;
	long glGetUniformSubroutineuiv;
	long glGetProgramStageiv;
	long glPatchParameteri;
	long glPatchParameterfv;
	long glBindTransformFeedback;
	long glDeleteTransformFeedbacks;
	long glGenTransformFeedbacks;
	long glIsTransformFeedback;
	long glPauseTransformFeedback;
	long glResumeTransformFeedback;
	long glDrawTransformFeedback;
	long glDrawTransformFeedbackStream;
	long glBeginQueryIndexed;
	long glEndQueryIndexed;
	long glGetQueryIndexediv;
	// GL41
	long glReleaseShaderCompiler;
	long glShaderBinary;
	long glGetShaderPrecisionFormat;
	long glDepthRangef;
	long glClearDepthf;
	long glGetProgramBinary;
	long glProgramBinary;
	long glProgramParameteri;
	long glUseProgramStages;
	long glActiveShaderProgram;
	long glCreateShaderProgramv;
	long glBindProgramPipeline;
	long glDeleteProgramPipelines;
	long glGenProgramPipelines;
	long glIsProgramPipeline;
	long glGetProgramPipelineiv;
	long glProgramUniform1i;
	long glProgramUniform2i;
	long glProgramUniform3i;
	long glProgramUniform4i;
	long glProgramUniform1f;
	long glProgramUniform2f;
	long glProgramUniform3f;
	long glProgramUniform4f;
	long glProgramUniform1d;
	long glProgramUniform2d;
	long glProgramUniform3d;
	long glProgramUniform4d;
	long glProgramUniform1iv;
	long glProgramUniform2iv;
	long glProgramUniform3iv;
	long glProgramUniform4iv;
	long glProgramUniform1fv;
	long glProgramUniform2fv;
	long glProgramUniform3fv;
	long glProgramUniform4fv;
	long glProgramUniform1dv;
	long glProgramUniform2dv;
	long glProgramUniform3dv;
	long glProgramUniform4dv;
	long glProgramUniform1ui;
	long glProgramUniform2ui;
	long glProgramUniform3ui;
	long glProgramUniform4ui;
	long glProgramUniform1uiv;
	long glProgramUniform2uiv;
	long glProgramUniform3uiv;
	long glProgramUniform4uiv;
	long glProgramUniformMatrix2fv;
	long glProgramUniformMatrix3fv;
	long glProgramUniformMatrix4fv;
	long glProgramUniformMatrix2dv;
	long glProgramUniformMatrix3dv;
	long glProgramUniformMatrix4dv;
	long glProgramUniformMatrix2x3fv;
	long glProgramUniformMatrix3x2fv;
	long glProgramUniformMatrix2x4fv;
	long glProgramUniformMatrix4x2fv;
	long glProgramUniformMatrix3x4fv;
	long glProgramUniformMatrix4x3fv;
	long glProgramUniformMatrix2x3dv;
	long glProgramUniformMatrix3x2dv;
	long glProgramUniformMatrix2x4dv;
	long glProgramUniformMatrix4x2dv;
	long glProgramUniformMatrix3x4dv;
	long glProgramUniformMatrix4x3dv;
	long glValidateProgramPipeline;
	long glGetProgramPipelineInfoLog;
	long glVertexAttribL1d;
	long glVertexAttribL2d;
	long glVertexAttribL3d;
	long glVertexAttribL4d;
	long glVertexAttribL1dv;
	long glVertexAttribL2dv;
	long glVertexAttribL3dv;
	long glVertexAttribL4dv;
	long glVertexAttribLPointer;
	long glGetVertexAttribLdv;
	long glViewportArrayv;
	long glViewportIndexedf;
	long glViewportIndexedfv;
	long glScissorArrayv;
	long glScissorIndexed;
	long glScissorIndexedv;
	long glDepthRangeArrayv;
	long glDepthRangeIndexed;
	long glGetFloati_v;
	long glGetDoublei_v;
	// GL42
	long glGetActiveAtomicCounterBufferiv;
	long glTexStorage1D;
	long glTexStorage2D;
	long glTexStorage3D;
	long glDrawTransformFeedbackInstanced;
	long glDrawTransformFeedbackStreamInstanced;
	long glDrawArraysInstancedBaseInstance;
	long glDrawElementsInstancedBaseInstance;
	long glDrawElementsInstancedBaseVertexBaseInstance;
	long glBindImageTexture;
	long glMemoryBarrier;
	long glGetInternalformativ;
	// GL43
	long glClearBufferData;
	long glClearBufferSubData;
	long glDispatchCompute;
	long glDispatchComputeIndirect;
	long glCopyImageSubData;
	long glDebugMessageControl;
	long glDebugMessageInsert;
	long glDebugMessageCallback;
	long glGetDebugMessageLog;
	long glPushDebugGroup;
	long glPopDebugGroup;
	long glObjectLabel;
	long glGetObjectLabel;
	long glObjectPtrLabel;
	long glGetObjectPtrLabel;
	long glFramebufferParameteri;
	long glGetFramebufferParameteriv;
	long glGetInternalformati64v;
	long glInvalidateTexSubImage;
	long glInvalidateTexImage;
	long glInvalidateBufferSubData;
	long glInvalidateBufferData;
	long glInvalidateFramebuffer;
	long glInvalidateSubFramebuffer;
	long glMultiDrawArraysIndirect;
	long glMultiDrawElementsIndirect;
	long glGetProgramInterfaceiv;
	long glGetProgramResourceIndex;
	long glGetProgramResourceName;
	long glGetProgramResourceiv;
	long glGetProgramResourceLocation;
	long glGetProgramResourceLocationIndex;
	long glShaderStorageBlockBinding;
	long glTexBufferRange;
	long glTexStorage2DMultisample;
	long glTexStorage3DMultisample;
	long glTextureView;
	long glBindVertexBuffer;
	long glVertexAttribFormat;
	long glVertexAttribIFormat;
	long glVertexAttribLFormat;
	long glVertexAttribBinding;
	long glVertexBindingDivisor;
	// GL44
	long glBufferStorage;
	long glClearTexImage;
	long glClearTexSubImage;
	long glBindBuffersBase;
	long glBindBuffersRange;
	long glBindTextures;
	long glBindSamplers;
	long glBindImageTextures;
	long glBindVertexBuffers;
	// GL45
	long glClipControl;
	long glCreateTransformFeedbacks;
	long glTransformFeedbackBufferBase;
	long glTransformFeedbackBufferRange;
	long glGetTransformFeedbackiv;
	long glGetTransformFeedbacki_v;
	long glGetTransformFeedbacki64_v;
	long glCreateBuffers;
	long glNamedBufferStorage;
	long glNamedBufferData;
	long glNamedBufferSubData;
	long glCopyNamedBufferSubData;
	long glClearNamedBufferData;
	long glClearNamedBufferSubData;
	long glMapNamedBuffer;
	long glMapNamedBufferRange;
	long glUnmapNamedBuffer;
	long glFlushMappedNamedBufferRange;
	long glGetNamedBufferParameteriv;
	long glGetNamedBufferParameteri64v;
	long glGetNamedBufferPointerv;
	long glGetNamedBufferSubData;
	long glCreateFramebuffers;
	long glNamedFramebufferRenderbuffer;
	long glNamedFramebufferParameteri;
	long glNamedFramebufferTexture;
	long glNamedFramebufferTextureLayer;
	long glNamedFramebufferDrawBuffer;
	long glNamedFramebufferDrawBuffers;
	long glNamedFramebufferReadBuffer;
	long glInvalidateNamedFramebufferData;
	long glInvalidateNamedFramebufferSubData;
	long glClearNamedFramebufferiv;
	long glClearNamedFramebufferuiv;
	long glClearNamedFramebufferfv;
	long glClearNamedFramebufferfi;
	long glBlitNamedFramebuffer;
	long glCheckNamedFramebufferStatus;
	long glGetNamedFramebufferParameteriv;
	long glGetNamedFramebufferAttachmentParameteriv;
	long glCreateRenderbuffers;
	long glNamedRenderbufferStorage;
	long glNamedRenderbufferStorageMultisample;
	long glGetNamedRenderbufferParameteriv;
	long glCreateTextures;
	long glTextureBuffer;
	long glTextureBufferRange;
	long glTextureStorage1D;
	long glTextureStorage2D;
	long glTextureStorage3D;
	long glTextureStorage2DMultisample;
	long glTextureStorage3DMultisample;
	long glTextureSubImage1D;
	long glTextureSubImage2D;
	long glTextureSubImage3D;
	long glCompressedTextureSubImage1D;
	long glCompressedTextureSubImage2D;
	long glCompressedTextureSubImage3D;
	long glCopyTextureSubImage1D;
	long glCopyTextureSubImage2D;
	long glCopyTextureSubImage3D;
	long glTextureParameterf;
	long glTextureParameterfv;
	long glTextureParameteri;
	long glTextureParameterIiv;
	long glTextureParameterIuiv;
	long glTextureParameteriv;
	long glGenerateTextureMipmap;
	long glBindTextureUnit;
	long glGetTextureImage;
	long glGetCompressedTextureImage;
	long glGetTextureLevelParameterfv;
	long glGetTextureLevelParameteriv;
	long glGetTextureParameterfv;
	long glGetTextureParameterIiv;
	long glGetTextureParameterIuiv;
	long glGetTextureParameteriv;
	long glCreateVertexArrays;
	long glDisableVertexArrayAttrib;
	long glEnableVertexArrayAttrib;
	long glVertexArrayElementBuffer;
	long glVertexArrayVertexBuffer;
	long glVertexArrayVertexBuffers;
	long glVertexArrayAttribFormat;
	long glVertexArrayAttribIFormat;
	long glVertexArrayAttribLFormat;
	long glVertexArrayAttribBinding;
	long glVertexArrayBindingDivisor;
	long glGetVertexArrayiv;
	long glGetVertexArrayIndexediv;
	long glGetVertexArrayIndexed64iv;
	long glCreateSamplers;
	long glCreateProgramPipelines;
	long glCreateQueries;
	long glMemoryBarrierByRegion;
	long glGetTextureSubImage;
	long glGetCompressedTextureSubImage;
	long glTextureBarrier;
	long glGetGraphicsResetStatus;
	long glReadnPixels;
	long glGetnUniformfv;
	long glGetnUniformiv;
	long glGetnUniformuiv;
	// GREMEDY_frame_terminator
	long glFrameTerminatorGREMEDY;
	// GREMEDY_string_marker
	long glStringMarkerGREMEDY;
	// INTEL_map_texture
	long glMapTexture2DINTEL;
	long glUnmapTexture2DINTEL;
	long glSyncTextureINTEL;
	// NV_bindless_multi_draw_indirect
	long glMultiDrawArraysIndirectBindlessNV;
	long glMultiDrawElementsIndirectBindlessNV;
	// NV_bindless_texture
	long glGetTextureHandleNV;
	long glGetTextureSamplerHandleNV;
	long glMakeTextureHandleResidentNV;
	long glMakeTextureHandleNonResidentNV;
	long glGetImageHandleNV;
	long glMakeImageHandleResidentNV;
	long glMakeImageHandleNonResidentNV;
	long glUniformHandleui64NV;
	long glUniformHandleui64vNV;
	long glProgramUniformHandleui64NV;
	long glProgramUniformHandleui64vNV;
	long glIsTextureHandleResidentNV;
	long glIsImageHandleResidentNV;
	// NV_blend_equation_advanced
	long glBlendParameteriNV;
	long glBlendBarrierNV;
	// NV_conditional_render
	long glBeginConditionalRenderNV;
	long glEndConditionalRenderNV;
	// NV_copy_image
	long glCopyImageSubDataNV;
	// NV_depth_buffer_float
	long glDepthRangedNV;
	long glClearDepthdNV;
	long glDepthBoundsdNV;
	// NV_draw_texture
	long glDrawTextureNV;
	// NV_evaluators
	long glGetMapControlPointsNV;
	long glMapControlPointsNV;
	long glMapParameterfvNV;
	long glMapParameterivNV;
	long glGetMapParameterfvNV;
	long glGetMapParameterivNV;
	long glGetMapAttribParameterfvNV;
	long glGetMapAttribParameterivNV;
	long glEvalMapsNV;
	// NV_explicit_multisample
	long glGetMultisamplefvNV;
	long glSampleMaskIndexedNV;
	long glTexRenderbufferNV;
	// NV_fence
	long glGenFencesNV;
	long glDeleteFencesNV;
	long glSetFenceNV;
	long glTestFenceNV;
	long glFinishFenceNV;
	long glIsFenceNV;
	long glGetFenceivNV;
	// NV_fragment_program
	long glProgramNamedParameter4fNV;
	long glProgramNamedParameter4dNV;
	long glGetProgramNamedParameterfvNV;
	long glGetProgramNamedParameterdvNV;
	// NV_framebuffer_multisample_coverage
	long glRenderbufferStorageMultisampleCoverageNV;
	// NV_geometry_program4
	long glProgramVertexLimitNV;
	// NV_gpu_program4
	long glProgramLocalParameterI4iNV;
	long glProgramLocalParameterI4ivNV;
	long glProgramLocalParametersI4ivNV;
	long glProgramLocalParameterI4uiNV;
	long glProgramLocalParameterI4uivNV;
	long glProgramLocalParametersI4uivNV;
	long glProgramEnvParameterI4iNV;
	long glProgramEnvParameterI4ivNV;
	long glProgramEnvParametersI4ivNV;
	long glProgramEnvParameterI4uiNV;
	long glProgramEnvParameterI4uivNV;
	long glProgramEnvParametersI4uivNV;
	long glGetProgramLocalParameterIivNV;
	long glGetProgramLocalParameterIuivNV;
	long glGetProgramEnvParameterIivNV;
	long glGetProgramEnvParameterIuivNV;
	// NV_gpu_shader5
	long glUniform1i64NV;
	long glUniform2i64NV;
	long glUniform3i64NV;
	long glUniform4i64NV;
	long glUniform1i64vNV;
	long glUniform2i64vNV;
	long glUniform3i64vNV;
	long glUniform4i64vNV;
	long glUniform1ui64NV;
	long glUniform2ui64NV;
	long glUniform3ui64NV;
	long glUniform4ui64NV;
	long glUniform1ui64vNV;
	long glUniform2ui64vNV;
	long glUniform3ui64vNV;
	long glUniform4ui64vNV;
	long glGetUniformi64vNV;
	long glGetUniformui64vNV;
	long glProgramUniform1i64NV;
	long glProgramUniform2i64NV;
	long glProgramUniform3i64NV;
	long glProgramUniform4i64NV;
	long glProgramUniform1i64vNV;
	long glProgramUniform2i64vNV;
	long glProgramUniform3i64vNV;
	long glProgramUniform4i64vNV;
	long glProgramUniform1ui64NV;
	long glProgramUniform2ui64NV;
	long glProgramUniform3ui64NV;
	long glProgramUniform4ui64NV;
	long glProgramUniform1ui64vNV;
	long glProgramUniform2ui64vNV;
	long glProgramUniform3ui64vNV;
	long glProgramUniform4ui64vNV;
	// NV_half_float
	long glVertex2hNV;
	long glVertex3hNV;
	long glVertex4hNV;
	long glNormal3hNV;
	long glColor3hNV;
	long glColor4hNV;
	long glTexCoord1hNV;
	long glTexCoord2hNV;
	long glTexCoord3hNV;
	long glTexCoord4hNV;
	long glMultiTexCoord1hNV;
	long glMultiTexCoord2hNV;
	long glMultiTexCoord3hNV;
	long glMultiTexCoord4hNV;
	long glFogCoordhNV;
	long glSecondaryColor3hNV;
	long glVertexWeighthNV;
	long glVertexAttrib1hNV;
	long glVertexAttrib2hNV;
	long glVertexAttrib3hNV;
	long glVertexAttrib4hNV;
	long glVertexAttribs1hvNV;
	long glVertexAttribs2hvNV;
	long glVertexAttribs3hvNV;
	long glVertexAttribs4hvNV;
	// NV_occlusion_query
	long glGenOcclusionQueriesNV;
	long glDeleteOcclusionQueriesNV;
	long glIsOcclusionQueryNV;
	long glBeginOcclusionQueryNV;
	long glEndOcclusionQueryNV;
	long glGetOcclusionQueryuivNV;
	long glGetOcclusionQueryivNV;
	// NV_parameter_buffer_object
	long glProgramBufferParametersfvNV;
	long glProgramBufferParametersIivNV;
	long glProgramBufferParametersIuivNV;
	// NV_path_rendering
	long glPathCommandsNV;
	long glPathCoordsNV;
	long glPathSubCommandsNV;
	long glPathSubCoordsNV;
	long glPathStringNV;
	long glPathGlyphsNV;
	long glPathGlyphRangeNV;
	long glWeightPathsNV;
	long glCopyPathNV;
	long glInterpolatePathsNV;
	long glTransformPathNV;
	long glPathParameterivNV;
	long glPathParameteriNV;
	long glPathParameterfvNV;
	long glPathParameterfNV;
	long glPathDashArrayNV;
	long glGenPathsNV;
	long glDeletePathsNV;
	long glIsPathNV;
	long glPathStencilFuncNV;
	long glPathStencilDepthOffsetNV;
	long glStencilFillPathNV;
	long glStencilStrokePathNV;
	long glStencilFillPathInstancedNV;
	long glStencilStrokePathInstancedNV;
	long glPathCoverDepthFuncNV;
	long glPathColorGenNV;
	long glPathTexGenNV;
	long glPathFogGenNV;
	long glCoverFillPathNV;
	long glCoverStrokePathNV;
	long glCoverFillPathInstancedNV;
	long glCoverStrokePathInstancedNV;
	long glGetPathParameterivNV;
	long glGetPathParameterfvNV;
	long glGetPathCommandsNV;
	long glGetPathCoordsNV;
	long glGetPathDashArrayNV;
	long glGetPathMetricsNV;
	long glGetPathMetricRangeNV;
	long glGetPathSpacingNV;
	long glGetPathColorGenivNV;
	long glGetPathColorGenfvNV;
	long glGetPathTexGenivNV;
	long glGetPathTexGenfvNV;
	long glIsPointInFillPathNV;
	long glIsPointInStrokePathNV;
	long glGetPathLengthNV;
	long glPointAlongPathNV;
	// NV_pixel_data_range
	long glPixelDataRangeNV;
	long glFlushPixelDataRangeNV;
	// NV_point_sprite
	long glPointParameteriNV;
	long glPointParameterivNV;
	// NV_present_video
	long glPresentFrameKeyedNV;
	long glPresentFrameDualFillNV;
	long glGetVideoivNV;
	long glGetVideouivNV;
	long glGetVideoi64vNV;
	long glGetVideoui64vNV;
	// NV_primitive_restart
	long glPrimitiveRestartNV;
	long glPrimitiveRestartIndexNV;
	// NV_program
	long glLoadProgramNV;
	long glBindProgramNV;
	long glDeleteProgramsNV;
	long glGenProgramsNV;
	long glGetProgramivNV;
	long glGetProgramStringNV;
	long glIsProgramNV;
	long glAreProgramsResidentNV;
	long glRequestResidentProgramsNV;
	// NV_register_combiners
	long glCombinerParameterfNV;
	long glCombinerParameterfvNV;
	long glCombinerParameteriNV;
	long glCombinerParameterivNV;
	long glCombinerInputNV;
	long glCombinerOutputNV;
	long glFinalCombinerInputNV;
	long glGetCombinerInputParameterfvNV;
	long glGetCombinerInputParameterivNV;
	long glGetCombinerOutputParameterfvNV;
	long glGetCombinerOutputParameterivNV;
	long glGetFinalCombinerInputParameterfvNV;
	long glGetFinalCombinerInputParameterivNV;
	// NV_register_combiners2
	long glCombinerStageParameterfvNV;
	long glGetCombinerStageParameterfvNV;
	// NV_shader_buffer_load
	long glMakeBufferResidentNV;
	long glMakeBufferNonResidentNV;
	long glIsBufferResidentNV;
	long glMakeNamedBufferResidentNV;
	long glMakeNamedBufferNonResidentNV;
	long glIsNamedBufferResidentNV;
	long glGetBufferParameterui64vNV;
	long glGetNamedBufferParameterui64vNV;
	long glGetIntegerui64vNV;
	long glUniformui64NV;
	long glUniformui64vNV;
	long glProgramUniformui64NV;
	long glProgramUniformui64vNV;
	// NV_texture_barrier
	long glTextureBarrierNV;
	// NV_texture_multisample
	long glTexImage2DMultisampleCoverageNV;
	long glTexImage3DMultisampleCoverageNV;
	long glTextureImage2DMultisampleNV;
	long glTextureImage3DMultisampleNV;
	long glTextureImage2DMultisampleCoverageNV;
	long glTextureImage3DMultisampleCoverageNV;
	// NV_transform_feedback
	long glBindBufferRangeNV;
	long glBindBufferOffsetNV;
	long glBindBufferBaseNV;
	long glTransformFeedbackAttribsNV;
	long glTransformFeedbackVaryingsNV;
	long glBeginTransformFeedbackNV;
	long glEndTransformFeedbackNV;
	long glGetVaryingLocationNV;
	long glGetActiveVaryingNV;
	long glActiveVaryingNV;
	long glGetTransformFeedbackVaryingNV;
	// NV_transform_feedback2
	long glBindTransformFeedbackNV;
	long glDeleteTransformFeedbacksNV;
	long glGenTransformFeedbacksNV;
	long glIsTransformFeedbackNV;
	long glPauseTransformFeedbackNV;
	long glResumeTransformFeedbackNV;
	long glDrawTransformFeedbackNV;
	// NV_vertex_array_range
	long glVertexArrayRangeNV;
	long glFlushVertexArrayRangeNV;
	long glAllocateMemoryNV;
	long glFreeMemoryNV;
	// NV_vertex_attrib_integer_64bit
	long glVertexAttribL1i64NV;
	long glVertexAttribL2i64NV;
	long glVertexAttribL3i64NV;
	long glVertexAttribL4i64NV;
	long glVertexAttribL1i64vNV;
	long glVertexAttribL2i64vNV;
	long glVertexAttribL3i64vNV;
	long glVertexAttribL4i64vNV;
	long glVertexAttribL1ui64NV;
	long glVertexAttribL2ui64NV;
	long glVertexAttribL3ui64NV;
	long glVertexAttribL4ui64NV;
	long glVertexAttribL1ui64vNV;
	long glVertexAttribL2ui64vNV;
	long glVertexAttribL3ui64vNV;
	long glVertexAttribL4ui64vNV;
	long glGetVertexAttribLi64vNV;
	long glGetVertexAttribLui64vNV;
	long glVertexAttribLFormatNV;
	// NV_vertex_buffer_unified_memory
	long glBufferAddressRangeNV;
	long glVertexFormatNV;
	long glNormalFormatNV;
	long glColorFormatNV;
	long glIndexFormatNV;
	long glTexCoordFormatNV;
	long glEdgeFlagFormatNV;
	long glSecondaryColorFormatNV;
	long glFogCoordFormatNV;
	long glVertexAttribFormatNV;
	long glVertexAttribIFormatNV;
	long glGetIntegerui64i_vNV;
	// NV_vertex_program
	long glExecuteProgramNV;
	long glGetProgramParameterfvNV;
	long glGetProgramParameterdvNV;
	long glGetTrackMatrixivNV;
	long glGetVertexAttribfvNV;
	long glGetVertexAttribdvNV;
	long glGetVertexAttribivNV;
	long glGetVertexAttribPointervNV;
	long glProgramParameter4fNV;
	long glProgramParameter4dNV;
	long glProgramParameters4fvNV;
	long glProgramParameters4dvNV;
	long glTrackMatrixNV;
	long glVertexAttribPointerNV;
	long glVertexAttrib1sNV;
	long glVertexAttrib1fNV;
	long glVertexAttrib1dNV;
	long glVertexAttrib2sNV;
	long glVertexAttrib2fNV;
	long glVertexAttrib2dNV;
	long glVertexAttrib3sNV;
	long glVertexAttrib3fNV;
	long glVertexAttrib3dNV;
	long glVertexAttrib4sNV;
	long glVertexAttrib4fNV;
	long glVertexAttrib4dNV;
	long glVertexAttrib4ubNV;
	long glVertexAttribs1svNV;
	long glVertexAttribs1fvNV;
	long glVertexAttribs1dvNV;
	long glVertexAttribs2svNV;
	long glVertexAttribs2fvNV;
	long glVertexAttribs2dvNV;
	long glVertexAttribs3svNV;
	long glVertexAttribs3fvNV;
	long glVertexAttribs3dvNV;
	long glVertexAttribs4svNV;
	long glVertexAttribs4fvNV;
	long glVertexAttribs4dvNV;
	// NV_video_capture
	long glBeginVideoCaptureNV;
	long glBindVideoCaptureStreamBufferNV;
	long glBindVideoCaptureStreamTextureNV;
	long glEndVideoCaptureNV;
	long glGetVideoCaptureivNV;
	long glGetVideoCaptureStreamivNV;
	long glGetVideoCaptureStreamfvNV;
	long glGetVideoCaptureStreamdvNV;
	long glVideoCaptureNV;
	long glVideoCaptureStreamParameterivNV;
	long glVideoCaptureStreamParameterfvNV;
	long glVideoCaptureStreamParameterdvNV;

	private boolean AMD_debug_output_initNativeFunctionAddresses() {
		return 
			(glDebugMessageEnableAMD = GLContext.getFunctionAddress(new String[] {"glDebugMessageEnableAMD","glDebugMessageEnableAMDX"})) != 0 &
			(glDebugMessageInsertAMD = GLContext.getFunctionAddress(new String[] {"glDebugMessageInsertAMD","glDebugMessageInsertAMDX"})) != 0 &
			(glDebugMessageCallbackAMD = GLContext.getFunctionAddress(new String[] {"glDebugMessageCallbackAMD","glDebugMessageCallbackAMDX"})) != 0 &
			(glGetDebugMessageLogAMD = GLContext.getFunctionAddress(new String[] {"glGetDebugMessageLogAMD","glGetDebugMessageLogAMDX"})) != 0;
	}

	private boolean AMD_draw_buffers_blend_initNativeFunctionAddresses() {
		return 
			(glBlendFuncIndexedAMD = GLContext.getFunctionAddress("glBlendFuncIndexedAMD")) != 0 &
			(glBlendFuncSeparateIndexedAMD = GLContext.getFunctionAddress("glBlendFuncSeparateIndexedAMD")) != 0 &
			(glBlendEquationIndexedAMD = GLContext.getFunctionAddress("glBlendEquationIndexedAMD")) != 0 &
			(glBlendEquationSeparateIndexedAMD = GLContext.getFunctionAddress("glBlendEquationSeparateIndexedAMD")) != 0;
	}

	private boolean AMD_interleaved_elements_initNativeFunctionAddresses() {
		return 
			(glVertexAttribParameteriAMD = GLContext.getFunctionAddress("glVertexAttribParameteriAMD")) != 0;
	}

	private boolean AMD_multi_draw_indirect_initNativeFunctionAddresses() {
		return 
			(glMultiDrawArraysIndirectAMD = GLContext.getFunctionAddress("glMultiDrawArraysIndirectAMD")) != 0 &
			(glMultiDrawElementsIndirectAMD = GLContext.getFunctionAddress("glMultiDrawElementsIndirectAMD")) != 0;
	}

	private boolean AMD_name_gen_delete_initNativeFunctionAddresses() {
		return 
			(glGenNamesAMD = GLContext.getFunctionAddress("glGenNamesAMD")) != 0 &
			(glDeleteNamesAMD = GLContext.getFunctionAddress("glDeleteNamesAMD")) != 0 &
			(glIsNameAMD = GLContext.getFunctionAddress("glIsNameAMD")) != 0;
	}

	private boolean AMD_performance_monitor_initNativeFunctionAddresses() {
		return 
			(glGetPerfMonitorGroupsAMD = GLContext.getFunctionAddress("glGetPerfMonitorGroupsAMD")) != 0 &
			(glGetPerfMonitorCountersAMD = GLContext.getFunctionAddress("glGetPerfMonitorCountersAMD")) != 0 &
			(glGetPerfMonitorGroupStringAMD = GLContext.getFunctionAddress("glGetPerfMonitorGroupStringAMD")) != 0 &
			(glGetPerfMonitorCounterStringAMD = GLContext.getFunctionAddress("glGetPerfMonitorCounterStringAMD")) != 0 &
			(glGetPerfMonitorCounterInfoAMD = GLContext.getFunctionAddress("glGetPerfMonitorCounterInfoAMD")) != 0 &
			(glGenPerfMonitorsAMD = GLContext.getFunctionAddress("glGenPerfMonitorsAMD")) != 0 &
			(glDeletePerfMonitorsAMD = GLContext.getFunctionAddress("glDeletePerfMonitorsAMD")) != 0 &
			(glSelectPerfMonitorCountersAMD = GLContext.getFunctionAddress("glSelectPerfMonitorCountersAMD")) != 0 &
			(glBeginPerfMonitorAMD = GLContext.getFunctionAddress("glBeginPerfMonitorAMD")) != 0 &
			(glEndPerfMonitorAMD = GLContext.getFunctionAddress("glEndPerfMonitorAMD")) != 0 &
			(glGetPerfMonitorCounterDataAMD = GLContext.getFunctionAddress("glGetPerfMonitorCounterDataAMD")) != 0;
	}

	private boolean AMD_sample_positions_initNativeFunctionAddresses() {
		return 
			(glSetMultisamplefvAMD = GLContext.getFunctionAddress("glSetMultisamplefvAMD")) != 0;
	}

	private boolean AMD_sparse_texture_initNativeFunctionAddresses() {
		return 
			(glTexStorageSparseAMD = GLContext.getFunctionAddress("glTexStorageSparseAMD")) != 0 &
			(glTextureStorageSparseAMD = GLContext.getFunctionAddress("glTextureStorageSparseAMD")) != 0;
	}

	private boolean AMD_stencil_operation_extended_initNativeFunctionAddresses() {
		return 
			(glStencilOpValueAMD = GLContext.getFunctionAddress("glStencilOpValueAMD")) != 0;
	}

	private boolean AMD_vertex_shader_tessellator_initNativeFunctionAddresses() {
		return 
			(glTessellationFactorAMD = GLContext.getFunctionAddress("glTessellationFactorAMD")) != 0 &
			(glTessellationModeAMD = GLContext.getFunctionAddress("glTessellationModeAMD")) != 0;
	}

	private boolean APPLE_element_array_initNativeFunctionAddresses() {
		return 
			(glElementPointerAPPLE = GLContext.getFunctionAddress("glElementPointerAPPLE")) != 0 &
			(glDrawElementArrayAPPLE = GLContext.getFunctionAddress("glDrawElementArrayAPPLE")) != 0 &
			(glDrawRangeElementArrayAPPLE = GLContext.getFunctionAddress("glDrawRangeElementArrayAPPLE")) != 0 &
			(glMultiDrawElementArrayAPPLE = GLContext.getFunctionAddress("glMultiDrawElementArrayAPPLE")) != 0 &
			(glMultiDrawRangeElementArrayAPPLE = GLContext.getFunctionAddress("glMultiDrawRangeElementArrayAPPLE")) != 0;
	}

	private boolean APPLE_fence_initNativeFunctionAddresses() {
		return 
			(glGenFencesAPPLE = GLContext.getFunctionAddress("glGenFencesAPPLE")) != 0 &
			(glDeleteFencesAPPLE = GLContext.getFunctionAddress("glDeleteFencesAPPLE")) != 0 &
			(glSetFenceAPPLE = GLContext.getFunctionAddress("glSetFenceAPPLE")) != 0 &
			(glIsFenceAPPLE = GLContext.getFunctionAddress("glIsFenceAPPLE")) != 0 &
			(glTestFenceAPPLE = GLContext.getFunctionAddress("glTestFenceAPPLE")) != 0 &
			(glFinishFenceAPPLE = GLContext.getFunctionAddress("glFinishFenceAPPLE")) != 0 &
			(glTestObjectAPPLE = GLContext.getFunctionAddress("glTestObjectAPPLE")) != 0 &
			(glFinishObjectAPPLE = GLContext.getFunctionAddress("glFinishObjectAPPLE")) != 0;
	}

	private boolean APPLE_flush_buffer_range_initNativeFunctionAddresses() {
		return 
			(glBufferParameteriAPPLE = GLContext.getFunctionAddress("glBufferParameteriAPPLE")) != 0 &
			(glFlushMappedBufferRangeAPPLE = GLContext.getFunctionAddress("glFlushMappedBufferRangeAPPLE")) != 0;
	}

	private boolean APPLE_object_purgeable_initNativeFunctionAddresses() {
		return 
			(glObjectPurgeableAPPLE = GLContext.getFunctionAddress("glObjectPurgeableAPPLE")) != 0 &
			(glObjectUnpurgeableAPPLE = GLContext.getFunctionAddress("glObjectUnpurgeableAPPLE")) != 0 &
			(glGetObjectParameterivAPPLE = GLContext.getFunctionAddress("glGetObjectParameterivAPPLE")) != 0;
	}

	private boolean APPLE_texture_range_initNativeFunctionAddresses() {
		return 
			(glTextureRangeAPPLE = GLContext.getFunctionAddress("glTextureRangeAPPLE")) != 0 &
			(glGetTexParameterPointervAPPLE = GLContext.getFunctionAddress("glGetTexParameterPointervAPPLE")) != 0;
	}

	private boolean APPLE_vertex_array_object_initNativeFunctionAddresses() {
		return 
			(glBindVertexArrayAPPLE = GLContext.getFunctionAddress("glBindVertexArrayAPPLE")) != 0 &
			(glDeleteVertexArraysAPPLE = GLContext.getFunctionAddress("glDeleteVertexArraysAPPLE")) != 0 &
			(glGenVertexArraysAPPLE = GLContext.getFunctionAddress("glGenVertexArraysAPPLE")) != 0 &
			(glIsVertexArrayAPPLE = GLContext.getFunctionAddress("glIsVertexArrayAPPLE")) != 0;
	}

	private boolean APPLE_vertex_array_range_initNativeFunctionAddresses() {
		return 
			(glVertexArrayRangeAPPLE = GLContext.getFunctionAddress("glVertexArrayRangeAPPLE")) != 0 &
			(glFlushVertexArrayRangeAPPLE = GLContext.getFunctionAddress("glFlushVertexArrayRangeAPPLE")) != 0 &
			(glVertexArrayParameteriAPPLE = GLContext.getFunctionAddress("glVertexArrayParameteriAPPLE")) != 0;
	}

	private boolean APPLE_vertex_program_evaluators_initNativeFunctionAddresses() {
		return 
			(glEnableVertexAttribAPPLE = GLContext.getFunctionAddress("glEnableVertexAttribAPPLE")) != 0 &
			(glDisableVertexAttribAPPLE = GLContext.getFunctionAddress("glDisableVertexAttribAPPLE")) != 0 &
			(glIsVertexAttribEnabledAPPLE = GLContext.getFunctionAddress("glIsVertexAttribEnabledAPPLE")) != 0 &
			(glMapVertexAttrib1dAPPLE = GLContext.getFunctionAddress("glMapVertexAttrib1dAPPLE")) != 0 &
			(glMapVertexAttrib1fAPPLE = GLContext.getFunctionAddress("glMapVertexAttrib1fAPPLE")) != 0 &
			(glMapVertexAttrib2dAPPLE = GLContext.getFunctionAddress("glMapVertexAttrib2dAPPLE")) != 0 &
			(glMapVertexAttrib2fAPPLE = GLContext.getFunctionAddress("glMapVertexAttrib2fAPPLE")) != 0;
	}

	private boolean ARB_ES2_compatibility_initNativeFunctionAddresses() {
		return 
			(glReleaseShaderCompiler = GLContext.getFunctionAddress("glReleaseShaderCompiler")) != 0 &
			(glShaderBinary = GLContext.getFunctionAddress("glShaderBinary")) != 0 &
			(glGetShaderPrecisionFormat = GLContext.getFunctionAddress("glGetShaderPrecisionFormat")) != 0 &
			(glDepthRangef = GLContext.getFunctionAddress("glDepthRangef")) != 0 &
			(glClearDepthf = GLContext.getFunctionAddress("glClearDepthf")) != 0;
	}

	private boolean ARB_ES3_1_compatibility_initNativeFunctionAddresses() {
		return 
			(glMemoryBarrierByRegion = GLContext.getFunctionAddress("glMemoryBarrierByRegion")) != 0;
	}

	private boolean ARB_base_instance_initNativeFunctionAddresses() {
		return 
			(glDrawArraysInstancedBaseInstance = GLContext.getFunctionAddress("glDrawArraysInstancedBaseInstance")) != 0 &
			(glDrawElementsInstancedBaseInstance = GLContext.getFunctionAddress("glDrawElementsInstancedBaseInstance")) != 0 &
			(glDrawElementsInstancedBaseVertexBaseInstance = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertexBaseInstance")) != 0;
	}

	private boolean ARB_bindless_texture_initNativeFunctionAddresses() {
		return 
			(glGetTextureHandleARB = GLContext.getFunctionAddress("glGetTextureHandleARB")) != 0 &
			(glGetTextureSamplerHandleARB = GLContext.getFunctionAddress("glGetTextureSamplerHandleARB")) != 0 &
			(glMakeTextureHandleResidentARB = GLContext.getFunctionAddress("glMakeTextureHandleResidentARB")) != 0 &
			(glMakeTextureHandleNonResidentARB = GLContext.getFunctionAddress("glMakeTextureHandleNonResidentARB")) != 0 &
			(glGetImageHandleARB = GLContext.getFunctionAddress("glGetImageHandleARB")) != 0 &
			(glMakeImageHandleResidentARB = GLContext.getFunctionAddress("glMakeImageHandleResidentARB")) != 0 &
			(glMakeImageHandleNonResidentARB = GLContext.getFunctionAddress("glMakeImageHandleNonResidentARB")) != 0 &
			(glUniformHandleui64ARB = GLContext.getFunctionAddress("glUniformHandleui64ARB")) != 0 &
			(glUniformHandleui64vARB = GLContext.getFunctionAddress("glUniformHandleui64vARB")) != 0 &
			(glProgramUniformHandleui64ARB = GLContext.getFunctionAddress("glProgramUniformHandleui64ARB")) != 0 &
			(glProgramUniformHandleui64vARB = GLContext.getFunctionAddress("glProgramUniformHandleui64vARB")) != 0 &
			(glIsTextureHandleResidentARB = GLContext.getFunctionAddress("glIsTextureHandleResidentARB")) != 0 &
			(glIsImageHandleResidentARB = GLContext.getFunctionAddress("glIsImageHandleResidentARB")) != 0 &
			(glVertexAttribL1ui64ARB = GLContext.getFunctionAddress("glVertexAttribL1ui64ARB")) != 0 &
			(glVertexAttribL1ui64vARB = GLContext.getFunctionAddress("glVertexAttribL1ui64vARB")) != 0 &
			(glGetVertexAttribLui64vARB = GLContext.getFunctionAddress("glGetVertexAttribLui64vARB")) != 0;
	}

	private boolean ARB_blend_func_extended_initNativeFunctionAddresses() {
		return 
			(glBindFragDataLocationIndexed = GLContext.getFunctionAddress("glBindFragDataLocationIndexed")) != 0 &
			(glGetFragDataIndex = GLContext.getFunctionAddress("glGetFragDataIndex")) != 0;
	}

	private boolean ARB_buffer_object_initNativeFunctionAddresses() {
		return 
			(glBindBufferARB = GLContext.getFunctionAddress("glBindBufferARB")) != 0 &
			(glDeleteBuffersARB = GLContext.getFunctionAddress("glDeleteBuffersARB")) != 0 &
			(glGenBuffersARB = GLContext.getFunctionAddress("glGenBuffersARB")) != 0 &
			(glIsBufferARB = GLContext.getFunctionAddress("glIsBufferARB")) != 0 &
			(glBufferDataARB = GLContext.getFunctionAddress("glBufferDataARB")) != 0 &
			(glBufferSubDataARB = GLContext.getFunctionAddress("glBufferSubDataARB")) != 0 &
			(glGetBufferSubDataARB = GLContext.getFunctionAddress("glGetBufferSubDataARB")) != 0 &
			(glMapBufferARB = GLContext.getFunctionAddress("glMapBufferARB")) != 0 &
			(glUnmapBufferARB = GLContext.getFunctionAddress("glUnmapBufferARB")) != 0 &
			(glGetBufferParameterivARB = GLContext.getFunctionAddress("glGetBufferParameterivARB")) != 0 &
			(glGetBufferPointervARB = GLContext.getFunctionAddress("glGetBufferPointervARB")) != 0;
	}

	private boolean ARB_buffer_storage_initNativeFunctionAddresses(Set<String> supported_extensions) {
		return 
			(glBufferStorage = GLContext.getFunctionAddress("glBufferStorage")) != 0 &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glNamedBufferStorageEXT = GLContext.getFunctionAddress("glNamedBufferStorageEXT")) != 0);
	}

	private boolean ARB_cl_event_initNativeFunctionAddresses() {
		return 
			(glCreateSyncFromCLeventARB = GLContext.getFunctionAddress("glCreateSyncFromCLeventARB")) != 0;
	}

	private boolean ARB_clear_buffer_object_initNativeFunctionAddresses(Set<String> supported_extensions) {
		return 
			(glClearBufferData = GLContext.getFunctionAddress("glClearBufferData")) != 0 &
			(glClearBufferSubData = GLContext.getFunctionAddress("glClearBufferSubData")) != 0 &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glClearNamedBufferDataEXT = GLContext.getFunctionAddress("glClearNamedBufferDataEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glClearNamedBufferSubDataEXT = GLContext.getFunctionAddress("glClearNamedBufferSubDataEXT")) != 0);
	}

	private boolean ARB_clear_texture_initNativeFunctionAddresses() {
		return 
			(glClearTexImage = GLContext.getFunctionAddress("glClearTexImage")) != 0 &
			(glClearTexSubImage = GLContext.getFunctionAddress("glClearTexSubImage")) != 0;
	}

	private boolean ARB_clip_control_initNativeFunctionAddresses() {
		return 
			(glClipControl = GLContext.getFunctionAddress("glClipControl")) != 0;
	}

	private boolean ARB_color_buffer_float_initNativeFunctionAddresses() {
		return 
			(glClampColorARB = GLContext.getFunctionAddress("glClampColorARB")) != 0;
	}

	private boolean ARB_compute_shader_initNativeFunctionAddresses() {
		return 
			(glDispatchCompute = GLContext.getFunctionAddress("glDispatchCompute")) != 0 &
			(glDispatchComputeIndirect = GLContext.getFunctionAddress("glDispatchComputeIndirect")) != 0;
	}

	private boolean ARB_compute_variable_group_size_initNativeFunctionAddresses() {
		return 
			(glDispatchComputeGroupSizeARB = GLContext.getFunctionAddress("glDispatchComputeGroupSizeARB")) != 0;
	}

	private boolean ARB_copy_buffer_initNativeFunctionAddresses() {
		return 
			(glCopyBufferSubData = GLContext.getFunctionAddress("glCopyBufferSubData")) != 0;
	}

	private boolean ARB_copy_image_initNativeFunctionAddresses() {
		return 
			(glCopyImageSubData = GLContext.getFunctionAddress("glCopyImageSubData")) != 0;
	}

	private boolean ARB_debug_output_initNativeFunctionAddresses() {
		return 
			(glDebugMessageControlARB = GLContext.getFunctionAddress("glDebugMessageControlARB")) != 0 &
			(glDebugMessageInsertARB = GLContext.getFunctionAddress("glDebugMessageInsertARB")) != 0 &
			(glDebugMessageCallbackARB = GLContext.getFunctionAddress("glDebugMessageCallbackARB")) != 0 &
			(glGetDebugMessageLogARB = GLContext.getFunctionAddress("glGetDebugMessageLogARB")) != 0;
	}

	private boolean ARB_direct_state_access_initNativeFunctionAddresses() {
		return 
			(glCreateTransformFeedbacks = GLContext.getFunctionAddress("glCreateTransformFeedbacks")) != 0 &
			(glTransformFeedbackBufferBase = GLContext.getFunctionAddress("glTransformFeedbackBufferBase")) != 0 &
			(glTransformFeedbackBufferRange = GLContext.getFunctionAddress("glTransformFeedbackBufferRange")) != 0 &
			(glGetTransformFeedbackiv = GLContext.getFunctionAddress("glGetTransformFeedbackiv")) != 0 &
			(glGetTransformFeedbacki_v = GLContext.getFunctionAddress("glGetTransformFeedbacki_v")) != 0 &
			(glGetTransformFeedbacki64_v = GLContext.getFunctionAddress("glGetTransformFeedbacki64_v")) != 0 &
			(glCreateBuffers = GLContext.getFunctionAddress("glCreateBuffers")) != 0 &
			(glNamedBufferStorage = GLContext.getFunctionAddress("glNamedBufferStorage")) != 0 &
			(glNamedBufferData = GLContext.getFunctionAddress("glNamedBufferData")) != 0 &
			(glNamedBufferSubData = GLContext.getFunctionAddress("glNamedBufferSubData")) != 0 &
			(glCopyNamedBufferSubData = GLContext.getFunctionAddress("glCopyNamedBufferSubData")) != 0 &
			(glClearNamedBufferData = GLContext.getFunctionAddress("glClearNamedBufferData")) != 0 &
			(glClearNamedBufferSubData = GLContext.getFunctionAddress("glClearNamedBufferSubData")) != 0 &
			(glMapNamedBuffer = GLContext.getFunctionAddress("glMapNamedBuffer")) != 0 &
			(glMapNamedBufferRange = GLContext.getFunctionAddress("glMapNamedBufferRange")) != 0 &
			(glUnmapNamedBuffer = GLContext.getFunctionAddress("glUnmapNamedBuffer")) != 0 &
			(glFlushMappedNamedBufferRange = GLContext.getFunctionAddress("glFlushMappedNamedBufferRange")) != 0 &
			(glGetNamedBufferParameteriv = GLContext.getFunctionAddress("glGetNamedBufferParameteriv")) != 0 &
			(glGetNamedBufferParameteri64v = GLContext.getFunctionAddress("glGetNamedBufferParameteri64v")) != 0 &
			(glGetNamedBufferPointerv = GLContext.getFunctionAddress("glGetNamedBufferPointerv")) != 0 &
			(glGetNamedBufferSubData = GLContext.getFunctionAddress("glGetNamedBufferSubData")) != 0 &
			(glCreateFramebuffers = GLContext.getFunctionAddress("glCreateFramebuffers")) != 0 &
			(glNamedFramebufferRenderbuffer = GLContext.getFunctionAddress("glNamedFramebufferRenderbuffer")) != 0 &
			(glNamedFramebufferParameteri = GLContext.getFunctionAddress("glNamedFramebufferParameteri")) != 0 &
			(glNamedFramebufferTexture = GLContext.getFunctionAddress("glNamedFramebufferTexture")) != 0 &
			(glNamedFramebufferTextureLayer = GLContext.getFunctionAddress("glNamedFramebufferTextureLayer")) != 0 &
			(glNamedFramebufferDrawBuffer = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffer")) != 0 &
			(glNamedFramebufferDrawBuffers = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffers")) != 0 &
			(glNamedFramebufferReadBuffer = GLContext.getFunctionAddress("glNamedFramebufferReadBuffer")) != 0 &
			(glInvalidateNamedFramebufferData = GLContext.getFunctionAddress("glInvalidateNamedFramebufferData")) != 0 &
			(glInvalidateNamedFramebufferSubData = GLContext.getFunctionAddress("glInvalidateNamedFramebufferSubData")) != 0 &
			(glClearNamedFramebufferiv = GLContext.getFunctionAddress("glClearNamedFramebufferiv")) != 0 &
			(glClearNamedFramebufferuiv = GLContext.getFunctionAddress("glClearNamedFramebufferuiv")) != 0 &
			(glClearNamedFramebufferfv = GLContext.getFunctionAddress("glClearNamedFramebufferfv")) != 0 &
			(glClearNamedFramebufferfi = GLContext.getFunctionAddress("glClearNamedFramebufferfi")) != 0 &
			(glBlitNamedFramebuffer = GLContext.getFunctionAddress("glBlitNamedFramebuffer")) != 0 &
			(glCheckNamedFramebufferStatus = GLContext.getFunctionAddress("glCheckNamedFramebufferStatus")) != 0 &
			(glGetNamedFramebufferParameteriv = GLContext.getFunctionAddress("glGetNamedFramebufferParameteriv")) != 0 &
			(glGetNamedFramebufferAttachmentParameteriv = GLContext.getFunctionAddress("glGetNamedFramebufferAttachmentParameteriv")) != 0 &
			(glCreateRenderbuffers = GLContext.getFunctionAddress("glCreateRenderbuffers")) != 0 &
			(glNamedRenderbufferStorage = GLContext.getFunctionAddress("glNamedRenderbufferStorage")) != 0 &
			(glNamedRenderbufferStorageMultisample = GLContext.getFunctionAddress("glNamedRenderbufferStorageMultisample")) != 0 &
			(glGetNamedRenderbufferParameteriv = GLContext.getFunctionAddress("glGetNamedRenderbufferParameteriv")) != 0 &
			(glCreateTextures = GLContext.getFunctionAddress("glCreateTextures")) != 0 &
			(glTextureBuffer = GLContext.getFunctionAddress("glTextureBuffer")) != 0 &
			(glTextureBufferRange = GLContext.getFunctionAddress("glTextureBufferRange")) != 0 &
			(glTextureStorage1D = GLContext.getFunctionAddress("glTextureStorage1D")) != 0 &
			(glTextureStorage2D = GLContext.getFunctionAddress("glTextureStorage2D")) != 0 &
			(glTextureStorage3D = GLContext.getFunctionAddress("glTextureStorage3D")) != 0 &
			(glTextureStorage2DMultisample = GLContext.getFunctionAddress("glTextureStorage2DMultisample")) != 0 &
			(glTextureStorage3DMultisample = GLContext.getFunctionAddress("glTextureStorage3DMultisample")) != 0 &
			(glTextureSubImage1D = GLContext.getFunctionAddress("glTextureSubImage1D")) != 0 &
			(glTextureSubImage2D = GLContext.getFunctionAddress("glTextureSubImage2D")) != 0 &
			(glTextureSubImage3D = GLContext.getFunctionAddress("glTextureSubImage3D")) != 0 &
			(glCompressedTextureSubImage1D = GLContext.getFunctionAddress("glCompressedTextureSubImage1D")) != 0 &
			(glCompressedTextureSubImage2D = GLContext.getFunctionAddress("glCompressedTextureSubImage2D")) != 0 &
			(glCompressedTextureSubImage3D = GLContext.getFunctionAddress("glCompressedTextureSubImage3D")) != 0 &
			(glCopyTextureSubImage1D = GLContext.getFunctionAddress("glCopyTextureSubImage1D")) != 0 &
			(glCopyTextureSubImage2D = GLContext.getFunctionAddress("glCopyTextureSubImage2D")) != 0 &
			(glCopyTextureSubImage3D = GLContext.getFunctionAddress("glCopyTextureSubImage3D")) != 0 &
			(glTextureParameterf = GLContext.getFunctionAddress("glTextureParameterf")) != 0 &
			(glTextureParameterfv = GLContext.getFunctionAddress("glTextureParameterfv")) != 0 &
			(glTextureParameteri = GLContext.getFunctionAddress("glTextureParameteri")) != 0 &
			(glTextureParameterIiv = GLContext.getFunctionAddress("glTextureParameterIiv")) != 0 &
			(glTextureParameterIuiv = GLContext.getFunctionAddress("glTextureParameterIuiv")) != 0 &
			(glTextureParameteriv = GLContext.getFunctionAddress("glTextureParameteriv")) != 0 &
			(glGenerateTextureMipmap = GLContext.getFunctionAddress("glGenerateTextureMipmap")) != 0 &
			(glBindTextureUnit = GLContext.getFunctionAddress("glBindTextureUnit")) != 0 &
			(glGetTextureImage = GLContext.getFunctionAddress("glGetTextureImage")) != 0 &
			(glGetCompressedTextureImage = GLContext.getFunctionAddress("glGetCompressedTextureImage")) != 0 &
			(glGetTextureLevelParameterfv = GLContext.getFunctionAddress("glGetTextureLevelParameterfv")) != 0 &
			(glGetTextureLevelParameteriv = GLContext.getFunctionAddress("glGetTextureLevelParameteriv")) != 0 &
			(glGetTextureParameterfv = GLContext.getFunctionAddress("glGetTextureParameterfv")) != 0 &
			(glGetTextureParameterIiv = GLContext.getFunctionAddress("glGetTextureParameterIiv")) != 0 &
			(glGetTextureParameterIuiv = GLContext.getFunctionAddress("glGetTextureParameterIuiv")) != 0 &
			(glGetTextureParameteriv = GLContext.getFunctionAddress("glGetTextureParameteriv")) != 0 &
			(glCreateVertexArrays = GLContext.getFunctionAddress("glCreateVertexArrays")) != 0 &
			(glDisableVertexArrayAttrib = GLContext.getFunctionAddress("glDisableVertexArrayAttrib")) != 0 &
			(glEnableVertexArrayAttrib = GLContext.getFunctionAddress("glEnableVertexArrayAttrib")) != 0 &
			(glVertexArrayElementBuffer = GLContext.getFunctionAddress("glVertexArrayElementBuffer")) != 0 &
			(glVertexArrayVertexBuffer = GLContext.getFunctionAddress("glVertexArrayVertexBuffer")) != 0 &
			(glVertexArrayVertexBuffers = GLContext.getFunctionAddress("glVertexArrayVertexBuffers")) != 0 &
			(glVertexArrayAttribFormat = GLContext.getFunctionAddress("glVertexArrayAttribFormat")) != 0 &
			(glVertexArrayAttribIFormat = GLContext.getFunctionAddress("glVertexArrayAttribIFormat")) != 0 &
			(glVertexArrayAttribLFormat = GLContext.getFunctionAddress("glVertexArrayAttribLFormat")) != 0 &
			(glVertexArrayAttribBinding = GLContext.getFunctionAddress("glVertexArrayAttribBinding")) != 0 &
			(glVertexArrayBindingDivisor = GLContext.getFunctionAddress("glVertexArrayBindingDivisor")) != 0 &
			(glGetVertexArrayiv = GLContext.getFunctionAddress("glGetVertexArrayiv")) != 0 &
			(glGetVertexArrayIndexediv = GLContext.getFunctionAddress("glGetVertexArrayIndexediv")) != 0 &
			(glGetVertexArrayIndexed64iv = GLContext.getFunctionAddress("glGetVertexArrayIndexed64iv")) != 0 &
			(glCreateSamplers = GLContext.getFunctionAddress("glCreateSamplers")) != 0 &
			(glCreateProgramPipelines = GLContext.getFunctionAddress("glCreateProgramPipelines")) != 0 &
			(glCreateQueries = GLContext.getFunctionAddress("glCreateQueries")) != 0;
	}

	private boolean ARB_draw_buffers_initNativeFunctionAddresses() {
		return 
			(glDrawBuffersARB = GLContext.getFunctionAddress("glDrawBuffersARB")) != 0;
	}

	private boolean ARB_draw_buffers_blend_initNativeFunctionAddresses() {
		return 
			(glBlendEquationiARB = GLContext.getFunctionAddress("glBlendEquationiARB")) != 0 &
			(glBlendEquationSeparateiARB = GLContext.getFunctionAddress("glBlendEquationSeparateiARB")) != 0 &
			(glBlendFunciARB = GLContext.getFunctionAddress("glBlendFunciARB")) != 0 &
			(glBlendFuncSeparateiARB = GLContext.getFunctionAddress("glBlendFuncSeparateiARB")) != 0;
	}

	private boolean ARB_draw_elements_base_vertex_initNativeFunctionAddresses() {
		return 
			(glDrawElementsBaseVertex = GLContext.getFunctionAddress("glDrawElementsBaseVertex")) != 0 &
			(glDrawRangeElementsBaseVertex = GLContext.getFunctionAddress("glDrawRangeElementsBaseVertex")) != 0 &
			(glDrawElementsInstancedBaseVertex = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertex")) != 0;
	}

	private boolean ARB_draw_indirect_initNativeFunctionAddresses() {
		return 
			(glDrawArraysIndirect = GLContext.getFunctionAddress("glDrawArraysIndirect")) != 0 &
			(glDrawElementsIndirect = GLContext.getFunctionAddress("glDrawElementsIndirect")) != 0;
	}

	private boolean ARB_draw_instanced_initNativeFunctionAddresses() {
		return 
			(glDrawArraysInstancedARB = GLContext.getFunctionAddress("glDrawArraysInstancedARB")) != 0 &
			(glDrawElementsInstancedARB = GLContext.getFunctionAddress("glDrawElementsInstancedARB")) != 0;
	}

	private boolean ARB_framebuffer_no_attachments_initNativeFunctionAddresses(Set<String> supported_extensions) {
		return 
			(glFramebufferParameteri = GLContext.getFunctionAddress("glFramebufferParameteri")) != 0 &
			(glGetFramebufferParameteriv = GLContext.getFunctionAddress("glGetFramebufferParameteriv")) != 0 &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glNamedFramebufferParameteriEXT = GLContext.getFunctionAddress("glNamedFramebufferParameteriEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glGetNamedFramebufferParameterivEXT = GLContext.getFunctionAddress("glGetNamedFramebufferParameterivEXT")) != 0);
	}

	private boolean ARB_framebuffer_object_initNativeFunctionAddresses() {
		return 
			(glIsRenderbuffer = GLContext.getFunctionAddress("glIsRenderbuffer")) != 0 &
			(glBindRenderbuffer = GLContext.getFunctionAddress("glBindRenderbuffer")) != 0 &
			(glDeleteRenderbuffers = GLContext.getFunctionAddress("glDeleteRenderbuffers")) != 0 &
			(glGenRenderbuffers = GLContext.getFunctionAddress("glGenRenderbuffers")) != 0 &
			(glRenderbufferStorage = GLContext.getFunctionAddress("glRenderbufferStorage")) != 0 &
			(glRenderbufferStorageMultisample = GLContext.getFunctionAddress("glRenderbufferStorageMultisample")) != 0 &
			(glGetRenderbufferParameteriv = GLContext.getFunctionAddress("glGetRenderbufferParameteriv")) != 0 &
			(glIsFramebuffer = GLContext.getFunctionAddress("glIsFramebuffer")) != 0 &
			(glBindFramebuffer = GLContext.getFunctionAddress("glBindFramebuffer")) != 0 &
			(glDeleteFramebuffers = GLContext.getFunctionAddress("glDeleteFramebuffers")) != 0 &
			(glGenFramebuffers = GLContext.getFunctionAddress("glGenFramebuffers")) != 0 &
			(glCheckFramebufferStatus = GLContext.getFunctionAddress("glCheckFramebufferStatus")) != 0 &
			(glFramebufferTexture1D = GLContext.getFunctionAddress("glFramebufferTexture1D")) != 0 &
			(glFramebufferTexture2D = GLContext.getFunctionAddress("glFramebufferTexture2D")) != 0 &
			(glFramebufferTexture3D = GLContext.getFunctionAddress("glFramebufferTexture3D")) != 0 &
			(glFramebufferTextureLayer = GLContext.getFunctionAddress("glFramebufferTextureLayer")) != 0 &
			(glFramebufferRenderbuffer = GLContext.getFunctionAddress("glFramebufferRenderbuffer")) != 0 &
			(glGetFramebufferAttachmentParameteriv = GLContext.getFunctionAddress("glGetFramebufferAttachmentParameteriv")) != 0 &
			(glBlitFramebuffer = GLContext.getFunctionAddress("glBlitFramebuffer")) != 0 &
			(glGenerateMipmap = GLContext.getFunctionAddress("glGenerateMipmap")) != 0;
	}

	private boolean ARB_geometry_shader4_initNativeFunctionAddresses() {
		return 
			(glProgramParameteriARB = GLContext.getFunctionAddress("glProgramParameteriARB")) != 0 &
			(glFramebufferTextureARB = GLContext.getFunctionAddress("glFramebufferTextureARB")) != 0 &
			(glFramebufferTextureLayerARB = GLContext.getFunctionAddress("glFramebufferTextureLayerARB")) != 0 &
			(glFramebufferTextureFaceARB = GLContext.getFunctionAddress("glFramebufferTextureFaceARB")) != 0;
	}

	private boolean ARB_get_program_binary_initNativeFunctionAddresses() {
		return 
			(glGetProgramBinary = GLContext.getFunctionAddress("glGetProgramBinary")) != 0 &
			(glProgramBinary = GLContext.getFunctionAddress("glProgramBinary")) != 0 &
			(glProgramParameteri = GLContext.getFunctionAddress("glProgramParameteri")) != 0;
	}

	private boolean ARB_get_texture_sub_image_initNativeFunctionAddresses() {
		return 
			(glGetTextureSubImage = GLContext.getFunctionAddress("glGetTextureSubImage")) != 0 &
			(glGetCompressedTextureSubImage = GLContext.getFunctionAddress("glGetCompressedTextureSubImage")) != 0;
	}

	private boolean ARB_gpu_shader_fp64_initNativeFunctionAddresses(Set<String> supported_extensions) {
		return 
			(glUniform1d = GLContext.getFunctionAddress("glUniform1d")) != 0 &
			(glUniform2d = GLContext.getFunctionAddress("glUniform2d")) != 0 &
			(glUniform3d = GLContext.getFunctionAddress("glUniform3d")) != 0 &
			(glUniform4d = GLContext.getFunctionAddress("glUniform4d")) != 0 &
			(glUniform1dv = GLContext.getFunctionAddress("glUniform1dv")) != 0 &
			(glUniform2dv = GLContext.getFunctionAddress("glUniform2dv")) != 0 &
			(glUniform3dv = GLContext.getFunctionAddress("glUniform3dv")) != 0 &
			(glUniform4dv = GLContext.getFunctionAddress("glUniform4dv")) != 0 &
			(glUniformMatrix2dv = GLContext.getFunctionAddress("glUniformMatrix2dv")) != 0 &
			(glUniformMatrix3dv = GLContext.getFunctionAddress("glUniformMatrix3dv")) != 0 &
			(glUniformMatrix4dv = GLContext.getFunctionAddress("glUniformMatrix4dv")) != 0 &
			(glUniformMatrix2x3dv = GLContext.getFunctionAddress("glUniformMatrix2x3dv")) != 0 &
			(glUniformMatrix2x4dv = GLContext.getFunctionAddress("glUniformMatrix2x4dv")) != 0 &
			(glUniformMatrix3x2dv = GLContext.getFunctionAddress("glUniformMatrix3x2dv")) != 0 &
			(glUniformMatrix3x4dv = GLContext.getFunctionAddress("glUniformMatrix3x4dv")) != 0 &
			(glUniformMatrix4x2dv = GLContext.getFunctionAddress("glUniformMatrix4x2dv")) != 0 &
			(glUniformMatrix4x3dv = GLContext.getFunctionAddress("glUniformMatrix4x3dv")) != 0 &
			(glGetUniformdv = GLContext.getFunctionAddress("glGetUniformdv")) != 0 &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform1dEXT = GLContext.getFunctionAddress("glProgramUniform1dEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform2dEXT = GLContext.getFunctionAddress("glProgramUniform2dEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform3dEXT = GLContext.getFunctionAddress("glProgramUniform3dEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform4dEXT = GLContext.getFunctionAddress("glProgramUniform4dEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform1dvEXT = GLContext.getFunctionAddress("glProgramUniform1dvEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform2dvEXT = GLContext.getFunctionAddress("glProgramUniform2dvEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform3dvEXT = GLContext.getFunctionAddress("glProgramUniform3dvEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform4dvEXT = GLContext.getFunctionAddress("glProgramUniform4dvEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniformMatrix2dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2dvEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniformMatrix3dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3dvEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniformMatrix4dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4dvEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniformMatrix2x3dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2x3dvEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniformMatrix2x4dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2x4dvEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniformMatrix3x2dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3x2dvEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniformMatrix3x4dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3x4dvEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniformMatrix4x2dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4x2dvEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniformMatrix4x3dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4x3dvEXT")) != 0);
	}

	private boolean ARB_imaging_initNativeFunctionAddresses(boolean forwardCompatible) {
		return 
			(forwardCompatible || (glColorTable = GLContext.getFunctionAddress("glColorTable")) != 0) &
			(forwardCompatible || (glColorSubTable = GLContext.getFunctionAddress("glColorSubTable")) != 0) &
			(forwardCompatible || (glColorTableParameteriv = GLContext.getFunctionAddress("glColorTableParameteriv")) != 0) &
			(forwardCompatible || (glColorTableParameterfv = GLContext.getFunctionAddress("glColorTableParameterfv")) != 0) &
			(forwardCompatible || (glCopyColorSubTable = GLContext.getFunctionAddress("glCopyColorSubTable")) != 0) &
			(forwardCompatible || (glCopyColorTable = GLContext.getFunctionAddress("glCopyColorTable")) != 0) &
			(forwardCompatible || (glGetColorTable = GLContext.getFunctionAddress("glGetColorTable")) != 0) &
			(forwardCompatible || (glGetColorTableParameteriv = GLContext.getFunctionAddress("glGetColorTableParameteriv")) != 0) &
			(forwardCompatible || (glGetColorTableParameterfv = GLContext.getFunctionAddress("glGetColorTableParameterfv")) != 0) &
			(glBlendEquation = GLContext.getFunctionAddress("glBlendEquation")) != 0 &
			(glBlendColor = GLContext.getFunctionAddress("glBlendColor")) != 0 &
			(forwardCompatible || (glHistogram = GLContext.getFunctionAddress("glHistogram")) != 0) &
			(forwardCompatible || (glResetHistogram = GLContext.getFunctionAddress("glResetHistogram")) != 0) &
			(forwardCompatible || (glGetHistogram = GLContext.getFunctionAddress("glGetHistogram")) != 0) &
			(forwardCompatible || (glGetHistogramParameterfv = GLContext.getFunctionAddress("glGetHistogramParameterfv")) != 0) &
			(forwardCompatible || (glGetHistogramParameteriv = GLContext.getFunctionAddress("glGetHistogramParameteriv")) != 0) &
			(forwardCompatible || (glMinmax = GLContext.getFunctionAddress("glMinmax")) != 0) &
			(forwardCompatible || (glResetMinmax = GLContext.getFunctionAddress("glResetMinmax")) != 0) &
			(forwardCompatible || (glGetMinmax = GLContext.getFunctionAddress("glGetMinmax")) != 0) &
			(forwardCompatible || (glGetMinmaxParameterfv = GLContext.getFunctionAddress("glGetMinmaxParameterfv")) != 0) &
			(forwardCompatible || (glGetMinmaxParameteriv = GLContext.getFunctionAddress("glGetMinmaxParameteriv")) != 0) &
			(forwardCompatible || (glConvolutionFilter1D = GLContext.getFunctionAddress("glConvolutionFilter1D")) != 0) &
			(forwardCompatible || (glConvolutionFilter2D = GLContext.getFunctionAddress("glConvolutionFilter2D")) != 0) &
			(forwardCompatible || (glConvolutionParameterf = GLContext.getFunctionAddress("glConvolutionParameterf")) != 0) &
			(forwardCompatible || (glConvolutionParameterfv = GLContext.getFunctionAddress("glConvolutionParameterfv")) != 0) &
			(forwardCompatible || (glConvolutionParameteri = GLContext.getFunctionAddress("glConvolutionParameteri")) != 0) &
			(forwardCompatible || (glConvolutionParameteriv = GLContext.getFunctionAddress("glConvolutionParameteriv")) != 0) &
			(forwardCompatible || (glCopyConvolutionFilter1D = GLContext.getFunctionAddress("glCopyConvolutionFilter1D")) != 0) &
			(forwardCompatible || (glCopyConvolutionFilter2D = GLContext.getFunctionAddress("glCopyConvolutionFilter2D")) != 0) &
			(forwardCompatible || (glGetConvolutionFilter = GLContext.getFunctionAddress("glGetConvolutionFilter")) != 0) &
			(forwardCompatible || (glGetConvolutionParameterfv = GLContext.getFunctionAddress("glGetConvolutionParameterfv")) != 0) &
			(forwardCompatible || (glGetConvolutionParameteriv = GLContext.getFunctionAddress("glGetConvolutionParameteriv")) != 0) &
			(forwardCompatible || (glSeparableFilter2D = GLContext.getFunctionAddress("glSeparableFilter2D")) != 0) &
			(forwardCompatible || (glGetSeparableFilter = GLContext.getFunctionAddress("glGetSeparableFilter")) != 0);
	}

	private boolean ARB_indirect_parameters_initNativeFunctionAddresses() {
		return 
			(glMultiDrawArraysIndirectCountARB = GLContext.getFunctionAddress("glMultiDrawArraysIndirectCountARB")) != 0 &
			(glMultiDrawElementsIndirectCountARB = GLContext.getFunctionAddress("glMultiDrawElementsIndirectCountARB")) != 0;
	}

	private boolean ARB_instanced_arrays_initNativeFunctionAddresses() {
		return 
			(glVertexAttribDivisorARB = GLContext.getFunctionAddress("glVertexAttribDivisorARB")) != 0;
	}

	private boolean ARB_internalformat_query_initNativeFunctionAddresses() {
		return 
			(glGetInternalformativ = GLContext.getFunctionAddress("glGetInternalformativ")) != 0;
	}

	private boolean ARB_internalformat_query2_initNativeFunctionAddresses() {
		return 
			(glGetInternalformati64v = GLContext.getFunctionAddress("glGetInternalformati64v")) != 0;
	}

	private boolean ARB_invalidate_subdata_initNativeFunctionAddresses() {
		return 
			(glInvalidateTexSubImage = GLContext.getFunctionAddress("glInvalidateTexSubImage")) != 0 &
			(glInvalidateTexImage = GLContext.getFunctionAddress("glInvalidateTexImage")) != 0 &
			(glInvalidateBufferSubData = GLContext.getFunctionAddress("glInvalidateBufferSubData")) != 0 &
			(glInvalidateBufferData = GLContext.getFunctionAddress("glInvalidateBufferData")) != 0 &
			(glInvalidateFramebuffer = GLContext.getFunctionAddress("glInvalidateFramebuffer")) != 0 &
			(glInvalidateSubFramebuffer = GLContext.getFunctionAddress("glInvalidateSubFramebuffer")) != 0;
	}

	private boolean ARB_map_buffer_range_initNativeFunctionAddresses() {
		return 
			(glMapBufferRange = GLContext.getFunctionAddress("glMapBufferRange")) != 0 &
			(glFlushMappedBufferRange = GLContext.getFunctionAddress("glFlushMappedBufferRange")) != 0;
	}

	private boolean ARB_matrix_palette_initNativeFunctionAddresses() {
		return 
			(glCurrentPaletteMatrixARB = GLContext.getFunctionAddress("glCurrentPaletteMatrixARB")) != 0 &
			(glMatrixIndexPointerARB = GLContext.getFunctionAddress("glMatrixIndexPointerARB")) != 0 &
			(glMatrixIndexubvARB = GLContext.getFunctionAddress("glMatrixIndexubvARB")) != 0 &
			(glMatrixIndexusvARB = GLContext.getFunctionAddress("glMatrixIndexusvARB")) != 0 &
			(glMatrixIndexuivARB = GLContext.getFunctionAddress("glMatrixIndexuivARB")) != 0;
	}

	private boolean ARB_multi_bind_initNativeFunctionAddresses() {
		return 
			(glBindBuffersBase = GLContext.getFunctionAddress("glBindBuffersBase")) != 0 &
			(glBindBuffersRange = GLContext.getFunctionAddress("glBindBuffersRange")) != 0 &
			(glBindTextures = GLContext.getFunctionAddress("glBindTextures")) != 0 &
			(glBindSamplers = GLContext.getFunctionAddress("glBindSamplers")) != 0 &
			(glBindImageTextures = GLContext.getFunctionAddress("glBindImageTextures")) != 0 &
			(glBindVertexBuffers = GLContext.getFunctionAddress("glBindVertexBuffers")) != 0;
	}

	private boolean ARB_multi_draw_indirect_initNativeFunctionAddresses() {
		return 
			(glMultiDrawArraysIndirect = GLContext.getFunctionAddress("glMultiDrawArraysIndirect")) != 0 &
			(glMultiDrawElementsIndirect = GLContext.getFunctionAddress("glMultiDrawElementsIndirect")) != 0;
	}

	private boolean ARB_multisample_initNativeFunctionAddresses() {
		return 
			(glSampleCoverageARB = GLContext.getFunctionAddress("glSampleCoverageARB")) != 0;
	}

	private boolean ARB_multitexture_initNativeFunctionAddresses() {
		return 
			(glClientActiveTextureARB = GLContext.getFunctionAddress("glClientActiveTextureARB")) != 0 &
			(glActiveTextureARB = GLContext.getFunctionAddress("glActiveTextureARB")) != 0 &
			(glMultiTexCoord1fARB = GLContext.getFunctionAddress("glMultiTexCoord1fARB")) != 0 &
			(glMultiTexCoord1dARB = GLContext.getFunctionAddress("glMultiTexCoord1dARB")) != 0 &
			(glMultiTexCoord1iARB = GLContext.getFunctionAddress("glMultiTexCoord1iARB")) != 0 &
			(glMultiTexCoord1sARB = GLContext.getFunctionAddress("glMultiTexCoord1sARB")) != 0 &
			(glMultiTexCoord2fARB = GLContext.getFunctionAddress("glMultiTexCoord2fARB")) != 0 &
			(glMultiTexCoord2dARB = GLContext.getFunctionAddress("glMultiTexCoord2dARB")) != 0 &
			(glMultiTexCoord2iARB = GLContext.getFunctionAddress("glMultiTexCoord2iARB")) != 0 &
			(glMultiTexCoord2sARB = GLContext.getFunctionAddress("glMultiTexCoord2sARB")) != 0 &
			(glMultiTexCoord3fARB = GLContext.getFunctionAddress("glMultiTexCoord3fARB")) != 0 &
			(glMultiTexCoord3dARB = GLContext.getFunctionAddress("glMultiTexCoord3dARB")) != 0 &
			(glMultiTexCoord3iARB = GLContext.getFunctionAddress("glMultiTexCoord3iARB")) != 0 &
			(glMultiTexCoord3sARB = GLContext.getFunctionAddress("glMultiTexCoord3sARB")) != 0 &
			(glMultiTexCoord4fARB = GLContext.getFunctionAddress("glMultiTexCoord4fARB")) != 0 &
			(glMultiTexCoord4dARB = GLContext.getFunctionAddress("glMultiTexCoord4dARB")) != 0 &
			(glMultiTexCoord4iARB = GLContext.getFunctionAddress("glMultiTexCoord4iARB")) != 0 &
			(glMultiTexCoord4sARB = GLContext.getFunctionAddress("glMultiTexCoord4sARB")) != 0;
	}

	private boolean ARB_occlusion_query_initNativeFunctionAddresses() {
		return 
			(glGenQueriesARB = GLContext.getFunctionAddress("glGenQueriesARB")) != 0 &
			(glDeleteQueriesARB = GLContext.getFunctionAddress("glDeleteQueriesARB")) != 0 &
			(glIsQueryARB = GLContext.getFunctionAddress("glIsQueryARB")) != 0 &
			(glBeginQueryARB = GLContext.getFunctionAddress("glBeginQueryARB")) != 0 &
			(glEndQueryARB = GLContext.getFunctionAddress("glEndQueryARB")) != 0 &
			(glGetQueryivARB = GLContext.getFunctionAddress("glGetQueryivARB")) != 0 &
			(glGetQueryObjectivARB = GLContext.getFunctionAddress("glGetQueryObjectivARB")) != 0 &
			(glGetQueryObjectuivARB = GLContext.getFunctionAddress("glGetQueryObjectuivARB")) != 0;
	}

	private boolean ARB_point_parameters_initNativeFunctionAddresses() {
		return 
			(glPointParameterfARB = GLContext.getFunctionAddress("glPointParameterfARB")) != 0 &
			(glPointParameterfvARB = GLContext.getFunctionAddress("glPointParameterfvARB")) != 0;
	}

	private boolean ARB_program_initNativeFunctionAddresses() {
		return 
			(glProgramStringARB = GLContext.getFunctionAddress("glProgramStringARB")) != 0 &
			(glBindProgramARB = GLContext.getFunctionAddress("glBindProgramARB")) != 0 &
			(glDeleteProgramsARB = GLContext.getFunctionAddress("glDeleteProgramsARB")) != 0 &
			(glGenProgramsARB = GLContext.getFunctionAddress("glGenProgramsARB")) != 0 &
			(glProgramEnvParameter4fARB = GLContext.getFunctionAddress("glProgramEnvParameter4fARB")) != 0 &
			(glProgramEnvParameter4dARB = GLContext.getFunctionAddress("glProgramEnvParameter4dARB")) != 0 &
			(glProgramEnvParameter4fvARB = GLContext.getFunctionAddress("glProgramEnvParameter4fvARB")) != 0 &
			(glProgramEnvParameter4dvARB = GLContext.getFunctionAddress("glProgramEnvParameter4dvARB")) != 0 &
			(glProgramLocalParameter4fARB = GLContext.getFunctionAddress("glProgramLocalParameter4fARB")) != 0 &
			(glProgramLocalParameter4dARB = GLContext.getFunctionAddress("glProgramLocalParameter4dARB")) != 0 &
			(glProgramLocalParameter4fvARB = GLContext.getFunctionAddress("glProgramLocalParameter4fvARB")) != 0 &
			(glProgramLocalParameter4dvARB = GLContext.getFunctionAddress("glProgramLocalParameter4dvARB")) != 0 &
			(glGetProgramEnvParameterfvARB = GLContext.getFunctionAddress("glGetProgramEnvParameterfvARB")) != 0 &
			(glGetProgramEnvParameterdvARB = GLContext.getFunctionAddress("glGetProgramEnvParameterdvARB")) != 0 &
			(glGetProgramLocalParameterfvARB = GLContext.getFunctionAddress("glGetProgramLocalParameterfvARB")) != 0 &
			(glGetProgramLocalParameterdvARB = GLContext.getFunctionAddress("glGetProgramLocalParameterdvARB")) != 0 &
			(glGetProgramivARB = GLContext.getFunctionAddress("glGetProgramivARB")) != 0 &
			(glGetProgramStringARB = GLContext.getFunctionAddress("glGetProgramStringARB")) != 0 &
			(glIsProgramARB = GLContext.getFunctionAddress("glIsProgramARB")) != 0;
	}

	private boolean ARB_program_interface_query_initNativeFunctionAddresses() {
		return 
			(glGetProgramInterfaceiv = GLContext.getFunctionAddress("glGetProgramInterfaceiv")) != 0 &
			(glGetProgramResourceIndex = GLContext.getFunctionAddress("glGetProgramResourceIndex")) != 0 &
			(glGetProgramResourceName = GLContext.getFunctionAddress("glGetProgramResourceName")) != 0 &
			(glGetProgramResourceiv = GLContext.getFunctionAddress("glGetProgramResourceiv")) != 0 &
			(glGetProgramResourceLocation = GLContext.getFunctionAddress("glGetProgramResourceLocation")) != 0 &
			(glGetProgramResourceLocationIndex = GLContext.getFunctionAddress("glGetProgramResourceLocationIndex")) != 0;
	}

	private boolean ARB_provoking_vertex_initNativeFunctionAddresses() {
		return 
			(glProvokingVertex = GLContext.getFunctionAddress("glProvokingVertex")) != 0;
	}

	private boolean ARB_robustness_initNativeFunctionAddresses(boolean forwardCompatible,Set<String> supported_extensions) {
		return 
			(glGetGraphicsResetStatusARB = GLContext.getFunctionAddress("glGetGraphicsResetStatusARB")) != 0 &
			(forwardCompatible || (glGetnMapdvARB = GLContext.getFunctionAddress("glGetnMapdvARB")) != 0) &
			(forwardCompatible || (glGetnMapfvARB = GLContext.getFunctionAddress("glGetnMapfvARB")) != 0) &
			(forwardCompatible || (glGetnMapivARB = GLContext.getFunctionAddress("glGetnMapivARB")) != 0) &
			(forwardCompatible || (glGetnPixelMapfvARB = GLContext.getFunctionAddress("glGetnPixelMapfvARB")) != 0) &
			(forwardCompatible || (glGetnPixelMapuivARB = GLContext.getFunctionAddress("glGetnPixelMapuivARB")) != 0) &
			(forwardCompatible || (glGetnPixelMapusvARB = GLContext.getFunctionAddress("glGetnPixelMapusvARB")) != 0) &
			(forwardCompatible || (glGetnPolygonStippleARB = GLContext.getFunctionAddress("glGetnPolygonStippleARB")) != 0) &
			(glGetnTexImageARB = GLContext.getFunctionAddress("glGetnTexImageARB")) != 0 &
			(glReadnPixelsARB = GLContext.getFunctionAddress("glReadnPixelsARB")) != 0 &
			(!supported_extensions.contains("GL_ARB_imaging") || (glGetnColorTableARB = GLContext.getFunctionAddress("glGetnColorTableARB")) != 0) &
			(!supported_extensions.contains("GL_ARB_imaging") || (glGetnConvolutionFilterARB = GLContext.getFunctionAddress("glGetnConvolutionFilterARB")) != 0) &
			(!supported_extensions.contains("GL_ARB_imaging") || (glGetnSeparableFilterARB = GLContext.getFunctionAddress("glGetnSeparableFilterARB")) != 0) &
			(!supported_extensions.contains("GL_ARB_imaging") || (glGetnHistogramARB = GLContext.getFunctionAddress("glGetnHistogramARB")) != 0) &
			(!supported_extensions.contains("GL_ARB_imaging") || (glGetnMinmaxARB = GLContext.getFunctionAddress("glGetnMinmaxARB")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glGetnCompressedTexImageARB = GLContext.getFunctionAddress("glGetnCompressedTexImageARB")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glGetnUniformfvARB = GLContext.getFunctionAddress("glGetnUniformfvARB")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glGetnUniformivARB = GLContext.getFunctionAddress("glGetnUniformivARB")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glGetnUniformuivARB = GLContext.getFunctionAddress("glGetnUniformuivARB")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glGetnUniformdvARB = GLContext.getFunctionAddress("glGetnUniformdvARB")) != 0);
	}

	private boolean ARB_sample_shading_initNativeFunctionAddresses() {
		return 
			(glMinSampleShadingARB = GLContext.getFunctionAddress("glMinSampleShadingARB")) != 0;
	}

	private boolean ARB_sampler_objects_initNativeFunctionAddresses() {
		return 
			(glGenSamplers = GLContext.getFunctionAddress("glGenSamplers")) != 0 &
			(glDeleteSamplers = GLContext.getFunctionAddress("glDeleteSamplers")) != 0 &
			(glIsSampler = GLContext.getFunctionAddress("glIsSampler")) != 0 &
			(glBindSampler = GLContext.getFunctionAddress("glBindSampler")) != 0 &
			(glSamplerParameteri = GLContext.getFunctionAddress("glSamplerParameteri")) != 0 &
			(glSamplerParameterf = GLContext.getFunctionAddress("glSamplerParameterf")) != 0 &
			(glSamplerParameteriv = GLContext.getFunctionAddress("glSamplerParameteriv")) != 0 &
			(glSamplerParameterfv = GLContext.getFunctionAddress("glSamplerParameterfv")) != 0 &
			(glSamplerParameterIiv = GLContext.getFunctionAddress("glSamplerParameterIiv")) != 0 &
			(glSamplerParameterIuiv = GLContext.getFunctionAddress("glSamplerParameterIuiv")) != 0 &
			(glGetSamplerParameteriv = GLContext.getFunctionAddress("glGetSamplerParameteriv")) != 0 &
			(glGetSamplerParameterfv = GLContext.getFunctionAddress("glGetSamplerParameterfv")) != 0 &
			(glGetSamplerParameterIiv = GLContext.getFunctionAddress("glGetSamplerParameterIiv")) != 0 &
			(glGetSamplerParameterIuiv = GLContext.getFunctionAddress("glGetSamplerParameterIuiv")) != 0;
	}

	private boolean ARB_separate_shader_objects_initNativeFunctionAddresses() {
		return 
			(glUseProgramStages = GLContext.getFunctionAddress("glUseProgramStages")) != 0 &
			(glActiveShaderProgram = GLContext.getFunctionAddress("glActiveShaderProgram")) != 0 &
			(glCreateShaderProgramv = GLContext.getFunctionAddress("glCreateShaderProgramv")) != 0 &
			(glBindProgramPipeline = GLContext.getFunctionAddress("glBindProgramPipeline")) != 0 &
			(glDeleteProgramPipelines = GLContext.getFunctionAddress("glDeleteProgramPipelines")) != 0 &
			(glGenProgramPipelines = GLContext.getFunctionAddress("glGenProgramPipelines")) != 0 &
			(glIsProgramPipeline = GLContext.getFunctionAddress("glIsProgramPipeline")) != 0 &
			(glProgramParameteri = GLContext.getFunctionAddress("glProgramParameteri")) != 0 &
			(glGetProgramPipelineiv = GLContext.getFunctionAddress("glGetProgramPipelineiv")) != 0 &
			(glProgramUniform1i = GLContext.getFunctionAddress("glProgramUniform1i")) != 0 &
			(glProgramUniform2i = GLContext.getFunctionAddress("glProgramUniform2i")) != 0 &
			(glProgramUniform3i = GLContext.getFunctionAddress("glProgramUniform3i")) != 0 &
			(glProgramUniform4i = GLContext.getFunctionAddress("glProgramUniform4i")) != 0 &
			(glProgramUniform1f = GLContext.getFunctionAddress("glProgramUniform1f")) != 0 &
			(glProgramUniform2f = GLContext.getFunctionAddress("glProgramUniform2f")) != 0 &
			(glProgramUniform3f = GLContext.getFunctionAddress("glProgramUniform3f")) != 0 &
			(glProgramUniform4f = GLContext.getFunctionAddress("glProgramUniform4f")) != 0 &
			(glProgramUniform1d = GLContext.getFunctionAddress("glProgramUniform1d")) != 0 &
			(glProgramUniform2d = GLContext.getFunctionAddress("glProgramUniform2d")) != 0 &
			(glProgramUniform3d = GLContext.getFunctionAddress("glProgramUniform3d")) != 0 &
			(glProgramUniform4d = GLContext.getFunctionAddress("glProgramUniform4d")) != 0 &
			(glProgramUniform1iv = GLContext.getFunctionAddress("glProgramUniform1iv")) != 0 &
			(glProgramUniform2iv = GLContext.getFunctionAddress("glProgramUniform2iv")) != 0 &
			(glProgramUniform3iv = GLContext.getFunctionAddress("glProgramUniform3iv")) != 0 &
			(glProgramUniform4iv = GLContext.getFunctionAddress("glProgramUniform4iv")) != 0 &
			(glProgramUniform1fv = GLContext.getFunctionAddress("glProgramUniform1fv")) != 0 &
			(glProgramUniform2fv = GLContext.getFunctionAddress("glProgramUniform2fv")) != 0 &
			(glProgramUniform3fv = GLContext.getFunctionAddress("glProgramUniform3fv")) != 0 &
			(glProgramUniform4fv = GLContext.getFunctionAddress("glProgramUniform4fv")) != 0 &
			(glProgramUniform1dv = GLContext.getFunctionAddress("glProgramUniform1dv")) != 0 &
			(glProgramUniform2dv = GLContext.getFunctionAddress("glProgramUniform2dv")) != 0 &
			(glProgramUniform3dv = GLContext.getFunctionAddress("glProgramUniform3dv")) != 0 &
			(glProgramUniform4dv = GLContext.getFunctionAddress("glProgramUniform4dv")) != 0 &
			(glProgramUniform1ui = GLContext.getFunctionAddress("glProgramUniform1ui")) != 0 &
			(glProgramUniform2ui = GLContext.getFunctionAddress("glProgramUniform2ui")) != 0 &
			(glProgramUniform3ui = GLContext.getFunctionAddress("glProgramUniform3ui")) != 0 &
			(glProgramUniform4ui = GLContext.getFunctionAddress("glProgramUniform4ui")) != 0 &
			(glProgramUniform1uiv = GLContext.getFunctionAddress("glProgramUniform1uiv")) != 0 &
			(glProgramUniform2uiv = GLContext.getFunctionAddress("glProgramUniform2uiv")) != 0 &
			(glProgramUniform3uiv = GLContext.getFunctionAddress("glProgramUniform3uiv")) != 0 &
			(glProgramUniform4uiv = GLContext.getFunctionAddress("glProgramUniform4uiv")) != 0 &
			(glProgramUniformMatrix2fv = GLContext.getFunctionAddress("glProgramUniformMatrix2fv")) != 0 &
			(glProgramUniformMatrix3fv = GLContext.getFunctionAddress("glProgramUniformMatrix3fv")) != 0 &
			(glProgramUniformMatrix4fv = GLContext.getFunctionAddress("glProgramUniformMatrix4fv")) != 0 &
			(glProgramUniformMatrix2dv = GLContext.getFunctionAddress("glProgramUniformMatrix2dv")) != 0 &
			(glProgramUniformMatrix3dv = GLContext.getFunctionAddress("glProgramUniformMatrix3dv")) != 0 &
			(glProgramUniformMatrix4dv = GLContext.getFunctionAddress("glProgramUniformMatrix4dv")) != 0 &
			(glProgramUniformMatrix2x3fv = GLContext.getFunctionAddress("glProgramUniformMatrix2x3fv")) != 0 &
			(glProgramUniformMatrix3x2fv = GLContext.getFunctionAddress("glProgramUniformMatrix3x2fv")) != 0 &
			(glProgramUniformMatrix2x4fv = GLContext.getFunctionAddress("glProgramUniformMatrix2x4fv")) != 0 &
			(glProgramUniformMatrix4x2fv = GLContext.getFunctionAddress("glProgramUniformMatrix4x2fv")) != 0 &
			(glProgramUniformMatrix3x4fv = GLContext.getFunctionAddress("glProgramUniformMatrix3x4fv")) != 0 &
			(glProgramUniformMatrix4x3fv = GLContext.getFunctionAddress("glProgramUniformMatrix4x3fv")) != 0 &
			(glProgramUniformMatrix2x3dv = GLContext.getFunctionAddress("glProgramUniformMatrix2x3dv")) != 0 &
			(glProgramUniformMatrix3x2dv = GLContext.getFunctionAddress("glProgramUniformMatrix3x2dv")) != 0 &
			(glProgramUniformMatrix2x4dv = GLContext.getFunctionAddress("glProgramUniformMatrix2x4dv")) != 0 &
			(glProgramUniformMatrix4x2dv = GLContext.getFunctionAddress("glProgramUniformMatrix4x2dv")) != 0 &
			(glProgramUniformMatrix3x4dv = GLContext.getFunctionAddress("glProgramUniformMatrix3x4dv")) != 0 &
			(glProgramUniformMatrix4x3dv = GLContext.getFunctionAddress("glProgramUniformMatrix4x3dv")) != 0 &
			(glValidateProgramPipeline = GLContext.getFunctionAddress("glValidateProgramPipeline")) != 0 &
			(glGetProgramPipelineInfoLog = GLContext.getFunctionAddress("glGetProgramPipelineInfoLog")) != 0;
	}

	private boolean ARB_shader_atomic_counters_initNativeFunctionAddresses() {
		return 
			(glGetActiveAtomicCounterBufferiv = GLContext.getFunctionAddress("glGetActiveAtomicCounterBufferiv")) != 0;
	}

	private boolean ARB_shader_image_load_store_initNativeFunctionAddresses() {
		return 
			(glBindImageTexture = GLContext.getFunctionAddress("glBindImageTexture")) != 0 &
			(glMemoryBarrier = GLContext.getFunctionAddress("glMemoryBarrier")) != 0;
	}

	private boolean ARB_shader_objects_initNativeFunctionAddresses() {
		return 
			(glDeleteObjectARB = GLContext.getFunctionAddress("glDeleteObjectARB")) != 0 &
			(glGetHandleARB = GLContext.getFunctionAddress("glGetHandleARB")) != 0 &
			(glDetachObjectARB = GLContext.getFunctionAddress("glDetachObjectARB")) != 0 &
			(glCreateShaderObjectARB = GLContext.getFunctionAddress("glCreateShaderObjectARB")) != 0 &
			(glShaderSourceARB = GLContext.getFunctionAddress("glShaderSourceARB")) != 0 &
			(glCompileShaderARB = GLContext.getFunctionAddress("glCompileShaderARB")) != 0 &
			(glCreateProgramObjectARB = GLContext.getFunctionAddress("glCreateProgramObjectARB")) != 0 &
			(glAttachObjectARB = GLContext.getFunctionAddress("glAttachObjectARB")) != 0 &
			(glLinkProgramARB = GLContext.getFunctionAddress("glLinkProgramARB")) != 0 &
			(glUseProgramObjectARB = GLContext.getFunctionAddress("glUseProgramObjectARB")) != 0 &
			(glValidateProgramARB = GLContext.getFunctionAddress("glValidateProgramARB")) != 0 &
			(glUniform1fARB = GLContext.getFunctionAddress("glUniform1fARB")) != 0 &
			(glUniform2fARB = GLContext.getFunctionAddress("glUniform2fARB")) != 0 &
			(glUniform3fARB = GLContext.getFunctionAddress("glUniform3fARB")) != 0 &
			(glUniform4fARB = GLContext.getFunctionAddress("glUniform4fARB")) != 0 &
			(glUniform1iARB = GLContext.getFunctionAddress("glUniform1iARB")) != 0 &
			(glUniform2iARB = GLContext.getFunctionAddress("glUniform2iARB")) != 0 &
			(glUniform3iARB = GLContext.getFunctionAddress("glUniform3iARB")) != 0 &
			(glUniform4iARB = GLContext.getFunctionAddress("glUniform4iARB")) != 0 &
			(glUniform1fvARB = GLContext.getFunctionAddress("glUniform1fvARB")) != 0 &
			(glUniform2fvARB = GLContext.getFunctionAddress("glUniform2fvARB")) != 0 &
			(glUniform3fvARB = GLContext.getFunctionAddress("glUniform3fvARB")) != 0 &
			(glUniform4fvARB = GLContext.getFunctionAddress("glUniform4fvARB")) != 0 &
			(glUniform1ivARB = GLContext.getFunctionAddress("glUniform1ivARB")) != 0 &
			(glUniform2ivARB = GLContext.getFunctionAddress("glUniform2ivARB")) != 0 &
			(glUniform3ivARB = GLContext.getFunctionAddress("glUniform3ivARB")) != 0 &
			(glUniform4ivARB = GLContext.getFunctionAddress("glUniform4ivARB")) != 0 &
			(glUniformMatrix2fvARB = GLContext.getFunctionAddress("glUniformMatrix2fvARB")) != 0 &
			(glUniformMatrix3fvARB = GLContext.getFunctionAddress("glUniformMatrix3fvARB")) != 0 &
			(glUniformMatrix4fvARB = GLContext.getFunctionAddress("glUniformMatrix4fvARB")) != 0 &
			(glGetObjectParameterfvARB = GLContext.getFunctionAddress("glGetObjectParameterfvARB")) != 0 &
			(glGetObjectParameterivARB = GLContext.getFunctionAddress("glGetObjectParameterivARB")) != 0 &
			(glGetInfoLogARB = GLContext.getFunctionAddress("glGetInfoLogARB")) != 0 &
			(glGetAttachedObjectsARB = GLContext.getFunctionAddress("glGetAttachedObjectsARB")) != 0 &
			(glGetUniformLocationARB = GLContext.getFunctionAddress("glGetUniformLocationARB")) != 0 &
			(glGetActiveUniformARB = GLContext.getFunctionAddress("glGetActiveUniformARB")) != 0 &
			(glGetUniformfvARB = GLContext.getFunctionAddress("glGetUniformfvARB")) != 0 &
			(glGetUniformivARB = GLContext.getFunctionAddress("glGetUniformivARB")) != 0 &
			(glGetShaderSourceARB = GLContext.getFunctionAddress("glGetShaderSourceARB")) != 0;
	}

	private boolean ARB_shader_storage_buffer_object_initNativeFunctionAddresses() {
		return 
			(glShaderStorageBlockBinding = GLContext.getFunctionAddress("glShaderStorageBlockBinding")) != 0;
	}

	private boolean ARB_shader_subroutine_initNativeFunctionAddresses() {
		return 
			(glGetSubroutineUniformLocation = GLContext.getFunctionAddress("glGetSubroutineUniformLocation")) != 0 &
			(glGetSubroutineIndex = GLContext.getFunctionAddress("glGetSubroutineIndex")) != 0 &
			(glGetActiveSubroutineUniformiv = GLContext.getFunctionAddress("glGetActiveSubroutineUniformiv")) != 0 &
			(glGetActiveSubroutineUniformName = GLContext.getFunctionAddress("glGetActiveSubroutineUniformName")) != 0 &
			(glGetActiveSubroutineName = GLContext.getFunctionAddress("glGetActiveSubroutineName")) != 0 &
			(glUniformSubroutinesuiv = GLContext.getFunctionAddress("glUniformSubroutinesuiv")) != 0 &
			(glGetUniformSubroutineuiv = GLContext.getFunctionAddress("glGetUniformSubroutineuiv")) != 0 &
			(glGetProgramStageiv = GLContext.getFunctionAddress("glGetProgramStageiv")) != 0;
	}

	private boolean ARB_shading_language_include_initNativeFunctionAddresses() {
		return 
			(glNamedStringARB = GLContext.getFunctionAddress("glNamedStringARB")) != 0 &
			(glDeleteNamedStringARB = GLContext.getFunctionAddress("glDeleteNamedStringARB")) != 0 &
			(glCompileShaderIncludeARB = GLContext.getFunctionAddress("glCompileShaderIncludeARB")) != 0 &
			(glIsNamedStringARB = GLContext.getFunctionAddress("glIsNamedStringARB")) != 0 &
			(glGetNamedStringARB = GLContext.getFunctionAddress("glGetNamedStringARB")) != 0 &
			(glGetNamedStringivARB = GLContext.getFunctionAddress("glGetNamedStringivARB")) != 0;
	}

	private boolean ARB_sparse_buffer_initNativeFunctionAddresses() {
		return 
			(glBufferPageCommitmentARB = GLContext.getFunctionAddress("glBufferPageCommitmentARB")) != 0;
	}

	private boolean ARB_sparse_texture_initNativeFunctionAddresses(Set<String> supported_extensions) {
		return 
			(glTexPageCommitmentARB = GLContext.getFunctionAddress("glTexPageCommitmentARB")) != 0 &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glTexturePageCommitmentEXT = GLContext.getFunctionAddress("glTexturePageCommitmentEXT")) != 0);
	}

	private boolean ARB_sync_initNativeFunctionAddresses() {
		return 
			(glFenceSync = GLContext.getFunctionAddress("glFenceSync")) != 0 &
			(glIsSync = GLContext.getFunctionAddress("glIsSync")) != 0 &
			(glDeleteSync = GLContext.getFunctionAddress("glDeleteSync")) != 0 &
			(glClientWaitSync = GLContext.getFunctionAddress("glClientWaitSync")) != 0 &
			(glWaitSync = GLContext.getFunctionAddress("glWaitSync")) != 0 &
			(glGetInteger64v = GLContext.getFunctionAddress("glGetInteger64v")) != 0 &
			(glGetSynciv = GLContext.getFunctionAddress("glGetSynciv")) != 0;
	}

	private boolean ARB_tessellation_shader_initNativeFunctionAddresses() {
		return 
			(glPatchParameteri = GLContext.getFunctionAddress("glPatchParameteri")) != 0 &
			(glPatchParameterfv = GLContext.getFunctionAddress("glPatchParameterfv")) != 0;
	}

	private boolean ARB_texture_barrier_initNativeFunctionAddresses() {
		return 
			(glTextureBarrier = GLContext.getFunctionAddress("glTextureBarrier")) != 0;
	}

	private boolean ARB_texture_buffer_object_initNativeFunctionAddresses() {
		return 
			(glTexBufferARB = GLContext.getFunctionAddress("glTexBufferARB")) != 0;
	}

	private boolean ARB_texture_buffer_range_initNativeFunctionAddresses(Set<String> supported_extensions) {
		return 
			(glTexBufferRange = GLContext.getFunctionAddress("glTexBufferRange")) != 0 &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glTextureBufferRangeEXT = GLContext.getFunctionAddress("glTextureBufferRangeEXT")) != 0);
	}

	private boolean ARB_texture_compression_initNativeFunctionAddresses() {
		return 
			(glCompressedTexImage1DARB = GLContext.getFunctionAddress("glCompressedTexImage1DARB")) != 0 &
			(glCompressedTexImage2DARB = GLContext.getFunctionAddress("glCompressedTexImage2DARB")) != 0 &
			(glCompressedTexImage3DARB = GLContext.getFunctionAddress("glCompressedTexImage3DARB")) != 0 &
			(glCompressedTexSubImage1DARB = GLContext.getFunctionAddress("glCompressedTexSubImage1DARB")) != 0 &
			(glCompressedTexSubImage2DARB = GLContext.getFunctionAddress("glCompressedTexSubImage2DARB")) != 0 &
			(glCompressedTexSubImage3DARB = GLContext.getFunctionAddress("glCompressedTexSubImage3DARB")) != 0 &
			(glGetCompressedTexImageARB = GLContext.getFunctionAddress("glGetCompressedTexImageARB")) != 0;
	}

	private boolean ARB_texture_multisample_initNativeFunctionAddresses() {
		return 
			(glTexImage2DMultisample = GLContext.getFunctionAddress("glTexImage2DMultisample")) != 0 &
			(glTexImage3DMultisample = GLContext.getFunctionAddress("glTexImage3DMultisample")) != 0 &
			(glGetMultisamplefv = GLContext.getFunctionAddress("glGetMultisamplefv")) != 0 &
			(glSampleMaski = GLContext.getFunctionAddress("glSampleMaski")) != 0;
	}

	private boolean ARB_texture_storage_initNativeFunctionAddresses(Set<String> supported_extensions) {
		return 
			(glTexStorage1D = GLContext.getFunctionAddress(new String[] {"glTexStorage1D","glTexStorage1DEXT"})) != 0 &
			(glTexStorage2D = GLContext.getFunctionAddress(new String[] {"glTexStorage2D","glTexStorage2DEXT"})) != 0 &
			(glTexStorage3D = GLContext.getFunctionAddress(new String[] {"glTexStorage3D","glTexStorage3DEXT"})) != 0 &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glTextureStorage1DEXT = GLContext.getFunctionAddress(new String[] {"glTextureStorage1DEXT","glTextureStorage1DEXTEXT"})) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glTextureStorage2DEXT = GLContext.getFunctionAddress(new String[] {"glTextureStorage2DEXT","glTextureStorage2DEXTEXT"})) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glTextureStorage3DEXT = GLContext.getFunctionAddress(new String[] {"glTextureStorage3DEXT","glTextureStorage3DEXTEXT"})) != 0);
	}

	private boolean ARB_texture_storage_multisample_initNativeFunctionAddresses(Set<String> supported_extensions) {
		return 
			(glTexStorage2DMultisample = GLContext.getFunctionAddress("glTexStorage2DMultisample")) != 0 &
			(glTexStorage3DMultisample = GLContext.getFunctionAddress("glTexStorage3DMultisample")) != 0 &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glTextureStorage2DMultisampleEXT = GLContext.getFunctionAddress("glTextureStorage2DMultisampleEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glTextureStorage3DMultisampleEXT = GLContext.getFunctionAddress("glTextureStorage3DMultisampleEXT")) != 0);
	}

	private boolean ARB_texture_view_initNativeFunctionAddresses() {
		return 
			(glTextureView = GLContext.getFunctionAddress("glTextureView")) != 0;
	}

	private boolean ARB_timer_query_initNativeFunctionAddresses() {
		return 
			(glQueryCounter = GLContext.getFunctionAddress("glQueryCounter")) != 0 &
			(glGetQueryObjecti64v = GLContext.getFunctionAddress("glGetQueryObjecti64v")) != 0 &
			(glGetQueryObjectui64v = GLContext.getFunctionAddress("glGetQueryObjectui64v")) != 0;
	}

	private boolean ARB_transform_feedback2_initNativeFunctionAddresses() {
		return 
			(glBindTransformFeedback = GLContext.getFunctionAddress("glBindTransformFeedback")) != 0 &
			(glDeleteTransformFeedbacks = GLContext.getFunctionAddress("glDeleteTransformFeedbacks")) != 0 &
			(glGenTransformFeedbacks = GLContext.getFunctionAddress("glGenTransformFeedbacks")) != 0 &
			(glIsTransformFeedback = GLContext.getFunctionAddress("glIsTransformFeedback")) != 0 &
			(glPauseTransformFeedback = GLContext.getFunctionAddress("glPauseTransformFeedback")) != 0 &
			(glResumeTransformFeedback = GLContext.getFunctionAddress("glResumeTransformFeedback")) != 0 &
			(glDrawTransformFeedback = GLContext.getFunctionAddress("glDrawTransformFeedback")) != 0;
	}

	private boolean ARB_transform_feedback3_initNativeFunctionAddresses() {
		return 
			(glDrawTransformFeedbackStream = GLContext.getFunctionAddress("glDrawTransformFeedbackStream")) != 0 &
			(glBeginQueryIndexed = GLContext.getFunctionAddress("glBeginQueryIndexed")) != 0 &
			(glEndQueryIndexed = GLContext.getFunctionAddress("glEndQueryIndexed")) != 0 &
			(glGetQueryIndexediv = GLContext.getFunctionAddress("glGetQueryIndexediv")) != 0;
	}

	private boolean ARB_transform_feedback_instanced_initNativeFunctionAddresses() {
		return 
			(glDrawTransformFeedbackInstanced = GLContext.getFunctionAddress("glDrawTransformFeedbackInstanced")) != 0 &
			(glDrawTransformFeedbackStreamInstanced = GLContext.getFunctionAddress("glDrawTransformFeedbackStreamInstanced")) != 0;
	}

	private boolean ARB_transpose_matrix_initNativeFunctionAddresses() {
		return 
			(glLoadTransposeMatrixfARB = GLContext.getFunctionAddress("glLoadTransposeMatrixfARB")) != 0 &
			(glMultTransposeMatrixfARB = GLContext.getFunctionAddress("glMultTransposeMatrixfARB")) != 0;
	}

	private boolean ARB_uniform_buffer_object_initNativeFunctionAddresses() {
		return 
			(glGetUniformIndices = GLContext.getFunctionAddress("glGetUniformIndices")) != 0 &
			(glGetActiveUniformsiv = GLContext.getFunctionAddress("glGetActiveUniformsiv")) != 0 &
			(glGetActiveUniformName = GLContext.getFunctionAddress("glGetActiveUniformName")) != 0 &
			(glGetUniformBlockIndex = GLContext.getFunctionAddress("glGetUniformBlockIndex")) != 0 &
			(glGetActiveUniformBlockiv = GLContext.getFunctionAddress("glGetActiveUniformBlockiv")) != 0 &
			(glGetActiveUniformBlockName = GLContext.getFunctionAddress("glGetActiveUniformBlockName")) != 0 &
			(glBindBufferRange = GLContext.getFunctionAddress("glBindBufferRange")) != 0 &
			(glBindBufferBase = GLContext.getFunctionAddress("glBindBufferBase")) != 0 &
			(glGetIntegeri_v = GLContext.getFunctionAddress("glGetIntegeri_v")) != 0 &
			(glUniformBlockBinding = GLContext.getFunctionAddress("glUniformBlockBinding")) != 0;
	}

	private boolean ARB_vertex_array_object_initNativeFunctionAddresses() {
		return 
			(glBindVertexArray = GLContext.getFunctionAddress("glBindVertexArray")) != 0 &
			(glDeleteVertexArrays = GLContext.getFunctionAddress("glDeleteVertexArrays")) != 0 &
			(glGenVertexArrays = GLContext.getFunctionAddress("glGenVertexArrays")) != 0 &
			(glIsVertexArray = GLContext.getFunctionAddress("glIsVertexArray")) != 0;
	}

	private boolean ARB_vertex_attrib_64bit_initNativeFunctionAddresses(Set<String> supported_extensions) {
		return 
			(glVertexAttribL1d = GLContext.getFunctionAddress("glVertexAttribL1d")) != 0 &
			(glVertexAttribL2d = GLContext.getFunctionAddress("glVertexAttribL2d")) != 0 &
			(glVertexAttribL3d = GLContext.getFunctionAddress("glVertexAttribL3d")) != 0 &
			(glVertexAttribL4d = GLContext.getFunctionAddress("glVertexAttribL4d")) != 0 &
			(glVertexAttribL1dv = GLContext.getFunctionAddress("glVertexAttribL1dv")) != 0 &
			(glVertexAttribL2dv = GLContext.getFunctionAddress("glVertexAttribL2dv")) != 0 &
			(glVertexAttribL3dv = GLContext.getFunctionAddress("glVertexAttribL3dv")) != 0 &
			(glVertexAttribL4dv = GLContext.getFunctionAddress("glVertexAttribL4dv")) != 0 &
			(glVertexAttribLPointer = GLContext.getFunctionAddress("glVertexAttribLPointer")) != 0 &
			(glGetVertexAttribLdv = GLContext.getFunctionAddress("glGetVertexAttribLdv")) != 0 &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glVertexArrayVertexAttribLOffsetEXT = GLContext.getFunctionAddress("glVertexArrayVertexAttribLOffsetEXT")) != 0);
	}

	private boolean ARB_vertex_attrib_binding_initNativeFunctionAddresses() {
		return 
			(glBindVertexBuffer = GLContext.getFunctionAddress("glBindVertexBuffer")) != 0 &
			(glVertexAttribFormat = GLContext.getFunctionAddress("glVertexAttribFormat")) != 0 &
			(glVertexAttribIFormat = GLContext.getFunctionAddress("glVertexAttribIFormat")) != 0 &
			(glVertexAttribLFormat = GLContext.getFunctionAddress("glVertexAttribLFormat")) != 0 &
			(glVertexAttribBinding = GLContext.getFunctionAddress("glVertexAttribBinding")) != 0 &
			(glVertexBindingDivisor = GLContext.getFunctionAddress("glVertexBindingDivisor")) != 0;
	}

	private boolean ARB_vertex_blend_initNativeFunctionAddresses() {
		return 
			(glWeightbvARB = GLContext.getFunctionAddress("glWeightbvARB")) != 0 &
			(glWeightsvARB = GLContext.getFunctionAddress("glWeightsvARB")) != 0 &
			(glWeightivARB = GLContext.getFunctionAddress("glWeightivARB")) != 0 &
			(glWeightfvARB = GLContext.getFunctionAddress("glWeightfvARB")) != 0 &
			(glWeightdvARB = GLContext.getFunctionAddress("glWeightdvARB")) != 0 &
			(glWeightubvARB = GLContext.getFunctionAddress("glWeightubvARB")) != 0 &
			(glWeightusvARB = GLContext.getFunctionAddress("glWeightusvARB")) != 0 &
			(glWeightuivARB = GLContext.getFunctionAddress("glWeightuivARB")) != 0 &
			(glWeightPointerARB = GLContext.getFunctionAddress("glWeightPointerARB")) != 0 &
			(glVertexBlendARB = GLContext.getFunctionAddress("glVertexBlendARB")) != 0;
	}

	private boolean ARB_vertex_program_initNativeFunctionAddresses() {
		return 
			(glVertexAttrib1sARB = GLContext.getFunctionAddress("glVertexAttrib1sARB")) != 0 &
			(glVertexAttrib1fARB = GLContext.getFunctionAddress("glVertexAttrib1fARB")) != 0 &
			(glVertexAttrib1dARB = GLContext.getFunctionAddress("glVertexAttrib1dARB")) != 0 &
			(glVertexAttrib2sARB = GLContext.getFunctionAddress("glVertexAttrib2sARB")) != 0 &
			(glVertexAttrib2fARB = GLContext.getFunctionAddress("glVertexAttrib2fARB")) != 0 &
			(glVertexAttrib2dARB = GLContext.getFunctionAddress("glVertexAttrib2dARB")) != 0 &
			(glVertexAttrib3sARB = GLContext.getFunctionAddress("glVertexAttrib3sARB")) != 0 &
			(glVertexAttrib3fARB = GLContext.getFunctionAddress("glVertexAttrib3fARB")) != 0 &
			(glVertexAttrib3dARB = GLContext.getFunctionAddress("glVertexAttrib3dARB")) != 0 &
			(glVertexAttrib4sARB = GLContext.getFunctionAddress("glVertexAttrib4sARB")) != 0 &
			(glVertexAttrib4fARB = GLContext.getFunctionAddress("glVertexAttrib4fARB")) != 0 &
			(glVertexAttrib4dARB = GLContext.getFunctionAddress("glVertexAttrib4dARB")) != 0 &
			(glVertexAttrib4NubARB = GLContext.getFunctionAddress("glVertexAttrib4NubARB")) != 0 &
			(glVertexAttribPointerARB = GLContext.getFunctionAddress("glVertexAttribPointerARB")) != 0 &
			(glEnableVertexAttribArrayARB = GLContext.getFunctionAddress("glEnableVertexAttribArrayARB")) != 0 &
			(glDisableVertexAttribArrayARB = GLContext.getFunctionAddress("glDisableVertexAttribArrayARB")) != 0 &
			(glGetVertexAttribfvARB = GLContext.getFunctionAddress("glGetVertexAttribfvARB")) != 0 &
			(glGetVertexAttribdvARB = GLContext.getFunctionAddress("glGetVertexAttribdvARB")) != 0 &
			(glGetVertexAttribivARB = GLContext.getFunctionAddress("glGetVertexAttribivARB")) != 0 &
			(glGetVertexAttribPointervARB = GLContext.getFunctionAddress("glGetVertexAttribPointervARB")) != 0;
	}

	private boolean ARB_vertex_shader_initNativeFunctionAddresses() {
		return 
			(glVertexAttrib1sARB = GLContext.getFunctionAddress("glVertexAttrib1sARB")) != 0 &
			(glVertexAttrib1fARB = GLContext.getFunctionAddress("glVertexAttrib1fARB")) != 0 &
			(glVertexAttrib1dARB = GLContext.getFunctionAddress("glVertexAttrib1dARB")) != 0 &
			(glVertexAttrib2sARB = GLContext.getFunctionAddress("glVertexAttrib2sARB")) != 0 &
			(glVertexAttrib2fARB = GLContext.getFunctionAddress("glVertexAttrib2fARB")) != 0 &
			(glVertexAttrib2dARB = GLContext.getFunctionAddress("glVertexAttrib2dARB")) != 0 &
			(glVertexAttrib3sARB = GLContext.getFunctionAddress("glVertexAttrib3sARB")) != 0 &
			(glVertexAttrib3fARB = GLContext.getFunctionAddress("glVertexAttrib3fARB")) != 0 &
			(glVertexAttrib3dARB = GLContext.getFunctionAddress("glVertexAttrib3dARB")) != 0 &
			(glVertexAttrib4sARB = GLContext.getFunctionAddress("glVertexAttrib4sARB")) != 0 &
			(glVertexAttrib4fARB = GLContext.getFunctionAddress("glVertexAttrib4fARB")) != 0 &
			(glVertexAttrib4dARB = GLContext.getFunctionAddress("glVertexAttrib4dARB")) != 0 &
			(glVertexAttrib4NubARB = GLContext.getFunctionAddress("glVertexAttrib4NubARB")) != 0 &
			(glVertexAttribPointerARB = GLContext.getFunctionAddress("glVertexAttribPointerARB")) != 0 &
			(glEnableVertexAttribArrayARB = GLContext.getFunctionAddress("glEnableVertexAttribArrayARB")) != 0 &
			(glDisableVertexAttribArrayARB = GLContext.getFunctionAddress("glDisableVertexAttribArrayARB")) != 0 &
			(glBindAttribLocationARB = GLContext.getFunctionAddress("glBindAttribLocationARB")) != 0 &
			(glGetActiveAttribARB = GLContext.getFunctionAddress("glGetActiveAttribARB")) != 0 &
			(glGetAttribLocationARB = GLContext.getFunctionAddress("glGetAttribLocationARB")) != 0 &
			(glGetVertexAttribfvARB = GLContext.getFunctionAddress("glGetVertexAttribfvARB")) != 0 &
			(glGetVertexAttribdvARB = GLContext.getFunctionAddress("glGetVertexAttribdvARB")) != 0 &
			(glGetVertexAttribivARB = GLContext.getFunctionAddress("glGetVertexAttribivARB")) != 0 &
			(glGetVertexAttribPointervARB = GLContext.getFunctionAddress("glGetVertexAttribPointervARB")) != 0;
	}

	private boolean ARB_vertex_type_2_10_10_10_rev_initNativeFunctionAddresses() {
		return 
			(glVertexP2ui = GLContext.getFunctionAddress("glVertexP2ui")) != 0 &
			(glVertexP3ui = GLContext.getFunctionAddress("glVertexP3ui")) != 0 &
			(glVertexP4ui = GLContext.getFunctionAddress("glVertexP4ui")) != 0 &
			(glVertexP2uiv = GLContext.getFunctionAddress("glVertexP2uiv")) != 0 &
			(glVertexP3uiv = GLContext.getFunctionAddress("glVertexP3uiv")) != 0 &
			(glVertexP4uiv = GLContext.getFunctionAddress("glVertexP4uiv")) != 0 &
			(glTexCoordP1ui = GLContext.getFunctionAddress("glTexCoordP1ui")) != 0 &
			(glTexCoordP2ui = GLContext.getFunctionAddress("glTexCoordP2ui")) != 0 &
			(glTexCoordP3ui = GLContext.getFunctionAddress("glTexCoordP3ui")) != 0 &
			(glTexCoordP4ui = GLContext.getFunctionAddress("glTexCoordP4ui")) != 0 &
			(glTexCoordP1uiv = GLContext.getFunctionAddress("glTexCoordP1uiv")) != 0 &
			(glTexCoordP2uiv = GLContext.getFunctionAddress("glTexCoordP2uiv")) != 0 &
			(glTexCoordP3uiv = GLContext.getFunctionAddress("glTexCoordP3uiv")) != 0 &
			(glTexCoordP4uiv = GLContext.getFunctionAddress("glTexCoordP4uiv")) != 0 &
			(glMultiTexCoordP1ui = GLContext.getFunctionAddress("glMultiTexCoordP1ui")) != 0 &
			(glMultiTexCoordP2ui = GLContext.getFunctionAddress("glMultiTexCoordP2ui")) != 0 &
			(glMultiTexCoordP3ui = GLContext.getFunctionAddress("glMultiTexCoordP3ui")) != 0 &
			(glMultiTexCoordP4ui = GLContext.getFunctionAddress("glMultiTexCoordP4ui")) != 0 &
			(glMultiTexCoordP1uiv = GLContext.getFunctionAddress("glMultiTexCoordP1uiv")) != 0 &
			(glMultiTexCoordP2uiv = GLContext.getFunctionAddress("glMultiTexCoordP2uiv")) != 0 &
			(glMultiTexCoordP3uiv = GLContext.getFunctionAddress("glMultiTexCoordP3uiv")) != 0 &
			(glMultiTexCoordP4uiv = GLContext.getFunctionAddress("glMultiTexCoordP4uiv")) != 0 &
			(glNormalP3ui = GLContext.getFunctionAddress("glNormalP3ui")) != 0 &
			(glNormalP3uiv = GLContext.getFunctionAddress("glNormalP3uiv")) != 0 &
			(glColorP3ui = GLContext.getFunctionAddress("glColorP3ui")) != 0 &
			(glColorP4ui = GLContext.getFunctionAddress("glColorP4ui")) != 0 &
			(glColorP3uiv = GLContext.getFunctionAddress("glColorP3uiv")) != 0 &
			(glColorP4uiv = GLContext.getFunctionAddress("glColorP4uiv")) != 0 &
			(glSecondaryColorP3ui = GLContext.getFunctionAddress("glSecondaryColorP3ui")) != 0 &
			(glSecondaryColorP3uiv = GLContext.getFunctionAddress("glSecondaryColorP3uiv")) != 0 &
			(glVertexAttribP1ui = GLContext.getFunctionAddress("glVertexAttribP1ui")) != 0 &
			(glVertexAttribP2ui = GLContext.getFunctionAddress("glVertexAttribP2ui")) != 0 &
			(glVertexAttribP3ui = GLContext.getFunctionAddress("glVertexAttribP3ui")) != 0 &
			(glVertexAttribP4ui = GLContext.getFunctionAddress("glVertexAttribP4ui")) != 0 &
			(glVertexAttribP1uiv = GLContext.getFunctionAddress("glVertexAttribP1uiv")) != 0 &
			(glVertexAttribP2uiv = GLContext.getFunctionAddress("glVertexAttribP2uiv")) != 0 &
			(glVertexAttribP3uiv = GLContext.getFunctionAddress("glVertexAttribP3uiv")) != 0 &
			(glVertexAttribP4uiv = GLContext.getFunctionAddress("glVertexAttribP4uiv")) != 0;
	}

	private boolean ARB_viewport_array_initNativeFunctionAddresses() {
		return 
			(glViewportArrayv = GLContext.getFunctionAddress("glViewportArrayv")) != 0 &
			(glViewportIndexedf = GLContext.getFunctionAddress("glViewportIndexedf")) != 0 &
			(glViewportIndexedfv = GLContext.getFunctionAddress("glViewportIndexedfv")) != 0 &
			(glScissorArrayv = GLContext.getFunctionAddress("glScissorArrayv")) != 0 &
			(glScissorIndexed = GLContext.getFunctionAddress("glScissorIndexed")) != 0 &
			(glScissorIndexedv = GLContext.getFunctionAddress("glScissorIndexedv")) != 0 &
			(glDepthRangeArrayv = GLContext.getFunctionAddress("glDepthRangeArrayv")) != 0 &
			(glDepthRangeIndexed = GLContext.getFunctionAddress("glDepthRangeIndexed")) != 0 &
			(glGetFloati_v = GLContext.getFunctionAddress("glGetFloati_v")) != 0 &
			(glGetDoublei_v = GLContext.getFunctionAddress("glGetDoublei_v")) != 0 &
			(glGetIntegerIndexedvEXT = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT")) != 0 &
			(glEnableIndexedEXT = GLContext.getFunctionAddress("glEnableIndexedEXT")) != 0 &
			(glDisableIndexedEXT = GLContext.getFunctionAddress("glDisableIndexedEXT")) != 0 &
			(glIsEnabledIndexedEXT = GLContext.getFunctionAddress("glIsEnabledIndexedEXT")) != 0;
	}

	private boolean ARB_window_pos_initNativeFunctionAddresses(boolean forwardCompatible) {
		return 
			(forwardCompatible || (glWindowPos2fARB = GLContext.getFunctionAddress("glWindowPos2fARB")) != 0) &
			(forwardCompatible || (glWindowPos2dARB = GLContext.getFunctionAddress("glWindowPos2dARB")) != 0) &
			(forwardCompatible || (glWindowPos2iARB = GLContext.getFunctionAddress("glWindowPos2iARB")) != 0) &
			(forwardCompatible || (glWindowPos2sARB = GLContext.getFunctionAddress("glWindowPos2sARB")) != 0) &
			(forwardCompatible || (glWindowPos3fARB = GLContext.getFunctionAddress("glWindowPos3fARB")) != 0) &
			(forwardCompatible || (glWindowPos3dARB = GLContext.getFunctionAddress("glWindowPos3dARB")) != 0) &
			(forwardCompatible || (glWindowPos3iARB = GLContext.getFunctionAddress("glWindowPos3iARB")) != 0) &
			(forwardCompatible || (glWindowPos3sARB = GLContext.getFunctionAddress("glWindowPos3sARB")) != 0);
	}

	private boolean ATI_draw_buffers_initNativeFunctionAddresses() {
		return 
			(glDrawBuffersATI = GLContext.getFunctionAddress("glDrawBuffersATI")) != 0;
	}

	private boolean ATI_element_array_initNativeFunctionAddresses() {
		return 
			(glElementPointerATI = GLContext.getFunctionAddress("glElementPointerATI")) != 0 &
			(glDrawElementArrayATI = GLContext.getFunctionAddress("glDrawElementArrayATI")) != 0 &
			(glDrawRangeElementArrayATI = GLContext.getFunctionAddress("glDrawRangeElementArrayATI")) != 0;
	}

	private boolean ATI_envmap_bumpmap_initNativeFunctionAddresses() {
		return 
			(glTexBumpParameterfvATI = GLContext.getFunctionAddress("glTexBumpParameterfvATI")) != 0 &
			(glTexBumpParameterivATI = GLContext.getFunctionAddress("glTexBumpParameterivATI")) != 0 &
			(glGetTexBumpParameterfvATI = GLContext.getFunctionAddress("glGetTexBumpParameterfvATI")) != 0 &
			(glGetTexBumpParameterivATI = GLContext.getFunctionAddress("glGetTexBumpParameterivATI")) != 0;
	}

	private boolean ATI_fragment_shader_initNativeFunctionAddresses() {
		return 
			(glGenFragmentShadersATI = GLContext.getFunctionAddress("glGenFragmentShadersATI")) != 0 &
			(glBindFragmentShaderATI = GLContext.getFunctionAddress("glBindFragmentShaderATI")) != 0 &
			(glDeleteFragmentShaderATI = GLContext.getFunctionAddress("glDeleteFragmentShaderATI")) != 0 &
			(glBeginFragmentShaderATI = GLContext.getFunctionAddress("glBeginFragmentShaderATI")) != 0 &
			(glEndFragmentShaderATI = GLContext.getFunctionAddress("glEndFragmentShaderATI")) != 0 &
			(glPassTexCoordATI = GLContext.getFunctionAddress("glPassTexCoordATI")) != 0 &
			(glSampleMapATI = GLContext.getFunctionAddress("glSampleMapATI")) != 0 &
			(glColorFragmentOp1ATI = GLContext.getFunctionAddress("glColorFragmentOp1ATI")) != 0 &
			(glColorFragmentOp2ATI = GLContext.getFunctionAddress("glColorFragmentOp2ATI")) != 0 &
			(glColorFragmentOp3ATI = GLContext.getFunctionAddress("glColorFragmentOp3ATI")) != 0 &
			(glAlphaFragmentOp1ATI = GLContext.getFunctionAddress("glAlphaFragmentOp1ATI")) != 0 &
			(glAlphaFragmentOp2ATI = GLContext.getFunctionAddress("glAlphaFragmentOp2ATI")) != 0 &
			(glAlphaFragmentOp3ATI = GLContext.getFunctionAddress("glAlphaFragmentOp3ATI")) != 0 &
			(glSetFragmentShaderConstantATI = GLContext.getFunctionAddress("glSetFragmentShaderConstantATI")) != 0;
	}

	private boolean ATI_map_object_buffer_initNativeFunctionAddresses() {
		return 
			(glMapObjectBufferATI = GLContext.getFunctionAddress("glMapObjectBufferATI")) != 0 &
			(glUnmapObjectBufferATI = GLContext.getFunctionAddress("glUnmapObjectBufferATI")) != 0;
	}

	private boolean ATI_pn_triangles_initNativeFunctionAddresses() {
		return 
			(glPNTrianglesfATI = GLContext.getFunctionAddress("glPNTrianglesfATI")) != 0 &
			(glPNTrianglesiATI = GLContext.getFunctionAddress("glPNTrianglesiATI")) != 0;
	}

	private boolean ATI_separate_stencil_initNativeFunctionAddresses() {
		return 
			(glStencilOpSeparateATI = GLContext.getFunctionAddress("glStencilOpSeparateATI")) != 0 &
			(glStencilFuncSeparateATI = GLContext.getFunctionAddress("glStencilFuncSeparateATI")) != 0;
	}

	private boolean ATI_vertex_array_object_initNativeFunctionAddresses() {
		return 
			(glNewObjectBufferATI = GLContext.getFunctionAddress("glNewObjectBufferATI")) != 0 &
			(glIsObjectBufferATI = GLContext.getFunctionAddress("glIsObjectBufferATI")) != 0 &
			(glUpdateObjectBufferATI = GLContext.getFunctionAddress("glUpdateObjectBufferATI")) != 0 &
			(glGetObjectBufferfvATI = GLContext.getFunctionAddress("glGetObjectBufferfvATI")) != 0 &
			(glGetObjectBufferivATI = GLContext.getFunctionAddress("glGetObjectBufferivATI")) != 0 &
			(glFreeObjectBufferATI = GLContext.getFunctionAddress("glFreeObjectBufferATI")) != 0 &
			(glArrayObjectATI = GLContext.getFunctionAddress("glArrayObjectATI")) != 0 &
			(glGetArrayObjectfvATI = GLContext.getFunctionAddress("glGetArrayObjectfvATI")) != 0 &
			(glGetArrayObjectivATI = GLContext.getFunctionAddress("glGetArrayObjectivATI")) != 0 &
			(glVariantArrayObjectATI = GLContext.getFunctionAddress("glVariantArrayObjectATI")) != 0 &
			(glGetVariantArrayObjectfvATI = GLContext.getFunctionAddress("glGetVariantArrayObjectfvATI")) != 0 &
			(glGetVariantArrayObjectivATI = GLContext.getFunctionAddress("glGetVariantArrayObjectivATI")) != 0;
	}

	private boolean ATI_vertex_attrib_array_object_initNativeFunctionAddresses() {
		return 
			(glVertexAttribArrayObjectATI = GLContext.getFunctionAddress("glVertexAttribArrayObjectATI")) != 0 &
			(glGetVertexAttribArrayObjectfvATI = GLContext.getFunctionAddress("glGetVertexAttribArrayObjectfvATI")) != 0 &
			(glGetVertexAttribArrayObjectivATI = GLContext.getFunctionAddress("glGetVertexAttribArrayObjectivATI")) != 0;
	}

	private boolean ATI_vertex_streams_initNativeFunctionAddresses() {
		return 
			(glVertexStream2fATI = GLContext.getFunctionAddress("glVertexStream2fATI")) != 0 &
			(glVertexStream2dATI = GLContext.getFunctionAddress("glVertexStream2dATI")) != 0 &
			(glVertexStream2iATI = GLContext.getFunctionAddress("glVertexStream2iATI")) != 0 &
			(glVertexStream2sATI = GLContext.getFunctionAddress("glVertexStream2sATI")) != 0 &
			(glVertexStream3fATI = GLContext.getFunctionAddress("glVertexStream3fATI")) != 0 &
			(glVertexStream3dATI = GLContext.getFunctionAddress("glVertexStream3dATI")) != 0 &
			(glVertexStream3iATI = GLContext.getFunctionAddress("glVertexStream3iATI")) != 0 &
			(glVertexStream3sATI = GLContext.getFunctionAddress("glVertexStream3sATI")) != 0 &
			(glVertexStream4fATI = GLContext.getFunctionAddress("glVertexStream4fATI")) != 0 &
			(glVertexStream4dATI = GLContext.getFunctionAddress("glVertexStream4dATI")) != 0 &
			(glVertexStream4iATI = GLContext.getFunctionAddress("glVertexStream4iATI")) != 0 &
			(glVertexStream4sATI = GLContext.getFunctionAddress("glVertexStream4sATI")) != 0 &
			(glNormalStream3bATI = GLContext.getFunctionAddress("glNormalStream3bATI")) != 0 &
			(glNormalStream3fATI = GLContext.getFunctionAddress("glNormalStream3fATI")) != 0 &
			(glNormalStream3dATI = GLContext.getFunctionAddress("glNormalStream3dATI")) != 0 &
			(glNormalStream3iATI = GLContext.getFunctionAddress("glNormalStream3iATI")) != 0 &
			(glNormalStream3sATI = GLContext.getFunctionAddress("glNormalStream3sATI")) != 0 &
			(glClientActiveVertexStreamATI = GLContext.getFunctionAddress("glClientActiveVertexStreamATI")) != 0 &
			(glVertexBlendEnvfATI = GLContext.getFunctionAddress("glVertexBlendEnvfATI")) != 0 &
			(glVertexBlendEnviATI = GLContext.getFunctionAddress("glVertexBlendEnviATI")) != 0;
	}

	private boolean EXT_bindable_uniform_initNativeFunctionAddresses() {
		return 
			(glUniformBufferEXT = GLContext.getFunctionAddress("glUniformBufferEXT")) != 0 &
			(glGetUniformBufferSizeEXT = GLContext.getFunctionAddress("glGetUniformBufferSizeEXT")) != 0 &
			(glGetUniformOffsetEXT = GLContext.getFunctionAddress("glGetUniformOffsetEXT")) != 0;
	}

	private boolean EXT_blend_color_initNativeFunctionAddresses() {
		return 
			(glBlendColorEXT = GLContext.getFunctionAddress("glBlendColorEXT")) != 0;
	}

	private boolean EXT_blend_equation_separate_initNativeFunctionAddresses() {
		return 
			(glBlendEquationSeparateEXT = GLContext.getFunctionAddress("glBlendEquationSeparateEXT")) != 0;
	}

	private boolean EXT_blend_func_separate_initNativeFunctionAddresses() {
		return 
			(glBlendFuncSeparateEXT = GLContext.getFunctionAddress("glBlendFuncSeparateEXT")) != 0;
	}

	private boolean EXT_blend_minmax_initNativeFunctionAddresses() {
		return 
			(glBlendEquationEXT = GLContext.getFunctionAddress("glBlendEquationEXT")) != 0;
	}

	private boolean EXT_compiled_vertex_array_initNativeFunctionAddresses() {
		return 
			(glLockArraysEXT = GLContext.getFunctionAddress("glLockArraysEXT")) != 0 &
			(glUnlockArraysEXT = GLContext.getFunctionAddress("glUnlockArraysEXT")) != 0;
	}

	private boolean EXT_depth_bounds_test_initNativeFunctionAddresses() {
		return 
			(glDepthBoundsEXT = GLContext.getFunctionAddress("glDepthBoundsEXT")) != 0;
	}

	private boolean EXT_direct_state_access_initNativeFunctionAddresses(boolean forwardCompatible,Set<String> supported_extensions) {
		return 
			(forwardCompatible || (glClientAttribDefaultEXT = GLContext.getFunctionAddress("glClientAttribDefaultEXT")) != 0) &
			(forwardCompatible || (glPushClientAttribDefaultEXT = GLContext.getFunctionAddress("glPushClientAttribDefaultEXT")) != 0) &
			(forwardCompatible || (glMatrixLoadfEXT = GLContext.getFunctionAddress("glMatrixLoadfEXT")) != 0) &
			(forwardCompatible || (glMatrixLoaddEXT = GLContext.getFunctionAddress("glMatrixLoaddEXT")) != 0) &
			(forwardCompatible || (glMatrixMultfEXT = GLContext.getFunctionAddress("glMatrixMultfEXT")) != 0) &
			(forwardCompatible || (glMatrixMultdEXT = GLContext.getFunctionAddress("glMatrixMultdEXT")) != 0) &
			(forwardCompatible || (glMatrixLoadIdentityEXT = GLContext.getFunctionAddress("glMatrixLoadIdentityEXT")) != 0) &
			(forwardCompatible || (glMatrixRotatefEXT = GLContext.getFunctionAddress("glMatrixRotatefEXT")) != 0) &
			(forwardCompatible || (glMatrixRotatedEXT = GLContext.getFunctionAddress("glMatrixRotatedEXT")) != 0) &
			(forwardCompatible || (glMatrixScalefEXT = GLContext.getFunctionAddress("glMatrixScalefEXT")) != 0) &
			(forwardCompatible || (glMatrixScaledEXT = GLContext.getFunctionAddress("glMatrixScaledEXT")) != 0) &
			(forwardCompatible || (glMatrixTranslatefEXT = GLContext.getFunctionAddress("glMatrixTranslatefEXT")) != 0) &
			(forwardCompatible || (glMatrixTranslatedEXT = GLContext.getFunctionAddress("glMatrixTranslatedEXT")) != 0) &
			(forwardCompatible || (glMatrixOrthoEXT = GLContext.getFunctionAddress("glMatrixOrthoEXT")) != 0) &
			(forwardCompatible || (glMatrixFrustumEXT = GLContext.getFunctionAddress("glMatrixFrustumEXT")) != 0) &
			(forwardCompatible || (glMatrixPushEXT = GLContext.getFunctionAddress("glMatrixPushEXT")) != 0) &
			(forwardCompatible || (glMatrixPopEXT = GLContext.getFunctionAddress("glMatrixPopEXT")) != 0) &
			(glTextureParameteriEXT = GLContext.getFunctionAddress("glTextureParameteriEXT")) != 0 &
			(glTextureParameterivEXT = GLContext.getFunctionAddress("glTextureParameterivEXT")) != 0 &
			(glTextureParameterfEXT = GLContext.getFunctionAddress("glTextureParameterfEXT")) != 0 &
			(glTextureParameterfvEXT = GLContext.getFunctionAddress("glTextureParameterfvEXT")) != 0 &
			(glTextureImage1DEXT = GLContext.getFunctionAddress("glTextureImage1DEXT")) != 0 &
			(glTextureImage2DEXT = GLContext.getFunctionAddress("glTextureImage2DEXT")) != 0 &
			(glTextureSubImage1DEXT = GLContext.getFunctionAddress("glTextureSubImage1DEXT")) != 0 &
			(glTextureSubImage2DEXT = GLContext.getFunctionAddress("glTextureSubImage2DEXT")) != 0 &
			(glCopyTextureImage1DEXT = GLContext.getFunctionAddress("glCopyTextureImage1DEXT")) != 0 &
			(glCopyTextureImage2DEXT = GLContext.getFunctionAddress("glCopyTextureImage2DEXT")) != 0 &
			(glCopyTextureSubImage1DEXT = GLContext.getFunctionAddress("glCopyTextureSubImage1DEXT")) != 0 &
			(glCopyTextureSubImage2DEXT = GLContext.getFunctionAddress("glCopyTextureSubImage2DEXT")) != 0 &
			(glGetTextureImageEXT = GLContext.getFunctionAddress("glGetTextureImageEXT")) != 0 &
			(glGetTextureParameterfvEXT = GLContext.getFunctionAddress("glGetTextureParameterfvEXT")) != 0 &
			(glGetTextureParameterivEXT = GLContext.getFunctionAddress("glGetTextureParameterivEXT")) != 0 &
			(glGetTextureLevelParameterfvEXT = GLContext.getFunctionAddress("glGetTextureLevelParameterfvEXT")) != 0 &
			(glGetTextureLevelParameterivEXT = GLContext.getFunctionAddress("glGetTextureLevelParameterivEXT")) != 0 &
			(!supported_extensions.contains("OpenGL12") || (glTextureImage3DEXT = GLContext.getFunctionAddress("glTextureImage3DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL12") || (glTextureSubImage3DEXT = GLContext.getFunctionAddress("glTextureSubImage3DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL12") || (glCopyTextureSubImage3DEXT = GLContext.getFunctionAddress("glCopyTextureSubImage3DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glBindMultiTextureEXT = GLContext.getFunctionAddress("glBindMultiTextureEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMultiTexCoordPointerEXT = GLContext.getFunctionAddress("glMultiTexCoordPointerEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMultiTexEnvfEXT = GLContext.getFunctionAddress("glMultiTexEnvfEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMultiTexEnvfvEXT = GLContext.getFunctionAddress("glMultiTexEnvfvEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMultiTexEnviEXT = GLContext.getFunctionAddress("glMultiTexEnviEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMultiTexEnvivEXT = GLContext.getFunctionAddress("glMultiTexEnvivEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMultiTexGendEXT = GLContext.getFunctionAddress("glMultiTexGendEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMultiTexGendvEXT = GLContext.getFunctionAddress("glMultiTexGendvEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMultiTexGenfEXT = GLContext.getFunctionAddress("glMultiTexGenfEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMultiTexGenfvEXT = GLContext.getFunctionAddress("glMultiTexGenfvEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMultiTexGeniEXT = GLContext.getFunctionAddress("glMultiTexGeniEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMultiTexGenivEXT = GLContext.getFunctionAddress("glMultiTexGenivEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glGetMultiTexEnvfvEXT = GLContext.getFunctionAddress("glGetMultiTexEnvfvEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glGetMultiTexEnvivEXT = GLContext.getFunctionAddress("glGetMultiTexEnvivEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glGetMultiTexGendvEXT = GLContext.getFunctionAddress("glGetMultiTexGendvEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glGetMultiTexGenfvEXT = GLContext.getFunctionAddress("glGetMultiTexGenfvEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glGetMultiTexGenivEXT = GLContext.getFunctionAddress("glGetMultiTexGenivEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glMultiTexParameteriEXT = GLContext.getFunctionAddress("glMultiTexParameteriEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glMultiTexParameterivEXT = GLContext.getFunctionAddress("glMultiTexParameterivEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glMultiTexParameterfEXT = GLContext.getFunctionAddress("glMultiTexParameterfEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glMultiTexParameterfvEXT = GLContext.getFunctionAddress("glMultiTexParameterfvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glMultiTexImage1DEXT = GLContext.getFunctionAddress("glMultiTexImage1DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glMultiTexImage2DEXT = GLContext.getFunctionAddress("glMultiTexImage2DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glMultiTexSubImage1DEXT = GLContext.getFunctionAddress("glMultiTexSubImage1DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glMultiTexSubImage2DEXT = GLContext.getFunctionAddress("glMultiTexSubImage2DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCopyMultiTexImage1DEXT = GLContext.getFunctionAddress("glCopyMultiTexImage1DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCopyMultiTexImage2DEXT = GLContext.getFunctionAddress("glCopyMultiTexImage2DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCopyMultiTexSubImage1DEXT = GLContext.getFunctionAddress("glCopyMultiTexSubImage1DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCopyMultiTexSubImage2DEXT = GLContext.getFunctionAddress("glCopyMultiTexSubImage2DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glGetMultiTexImageEXT = GLContext.getFunctionAddress("glGetMultiTexImageEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glGetMultiTexParameterfvEXT = GLContext.getFunctionAddress("glGetMultiTexParameterfvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glGetMultiTexParameterivEXT = GLContext.getFunctionAddress("glGetMultiTexParameterivEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glGetMultiTexLevelParameterfvEXT = GLContext.getFunctionAddress("glGetMultiTexLevelParameterfvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glGetMultiTexLevelParameterivEXT = GLContext.getFunctionAddress("glGetMultiTexLevelParameterivEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glMultiTexImage3DEXT = GLContext.getFunctionAddress("glMultiTexImage3DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glMultiTexSubImage3DEXT = GLContext.getFunctionAddress("glMultiTexSubImage3DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCopyMultiTexSubImage3DEXT = GLContext.getFunctionAddress("glCopyMultiTexSubImage3DEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glEnableClientStateIndexedEXT = GLContext.getFunctionAddress("glEnableClientStateIndexedEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glDisableClientStateIndexedEXT = GLContext.getFunctionAddress("glDisableClientStateIndexedEXT")) != 0) &
			((!supported_extensions.contains("OpenGL30") || (glEnableClientStateiEXT = GLContext.getFunctionAddress("glEnableClientStateiEXT")) != 0) || true) &
			((!supported_extensions.contains("OpenGL30") || (glDisableClientStateiEXT = GLContext.getFunctionAddress("glDisableClientStateiEXT")) != 0) || true) &
			(!supported_extensions.contains("OpenGL13") || (glGetFloatIndexedvEXT = GLContext.getFunctionAddress("glGetFloatIndexedvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glGetDoubleIndexedvEXT = GLContext.getFunctionAddress("glGetDoubleIndexedvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glGetPointerIndexedvEXT = GLContext.getFunctionAddress("glGetPointerIndexedvEXT")) != 0) &
			((!supported_extensions.contains("OpenGL30") || (glGetFloati_vEXT = GLContext.getFunctionAddress("glGetFloati_vEXT")) != 0) || true) &
			((!supported_extensions.contains("OpenGL30") || (glGetDoublei_vEXT = GLContext.getFunctionAddress("glGetDoublei_vEXT")) != 0) || true) &
			((!supported_extensions.contains("OpenGL30") || (glGetPointeri_vEXT = GLContext.getFunctionAddress("glGetPointeri_vEXT")) != 0) || true) &
			(!supported_extensions.contains("OpenGL13") || (glEnableIndexedEXT = GLContext.getFunctionAddress("glEnableIndexedEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glDisableIndexedEXT = GLContext.getFunctionAddress("glDisableIndexedEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glIsEnabledIndexedEXT = GLContext.getFunctionAddress("glIsEnabledIndexedEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glGetIntegerIndexedvEXT = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glGetBooleanIndexedvEXT = GLContext.getFunctionAddress("glGetBooleanIndexedvEXT")) != 0) &
			(!supported_extensions.contains("GL_ARB_vertex_program") || (glNamedProgramStringEXT = GLContext.getFunctionAddress("glNamedProgramStringEXT")) != 0) &
			(!supported_extensions.contains("GL_ARB_vertex_program") || (glNamedProgramLocalParameter4dEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameter4dEXT")) != 0) &
			(!supported_extensions.contains("GL_ARB_vertex_program") || (glNamedProgramLocalParameter4dvEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameter4dvEXT")) != 0) &
			(!supported_extensions.contains("GL_ARB_vertex_program") || (glNamedProgramLocalParameter4fEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameter4fEXT")) != 0) &
			(!supported_extensions.contains("GL_ARB_vertex_program") || (glNamedProgramLocalParameter4fvEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameter4fvEXT")) != 0) &
			(!supported_extensions.contains("GL_ARB_vertex_program") || (glGetNamedProgramLocalParameterdvEXT = GLContext.getFunctionAddress("glGetNamedProgramLocalParameterdvEXT")) != 0) &
			(!supported_extensions.contains("GL_ARB_vertex_program") || (glGetNamedProgramLocalParameterfvEXT = GLContext.getFunctionAddress("glGetNamedProgramLocalParameterfvEXT")) != 0) &
			(!supported_extensions.contains("GL_ARB_vertex_program") || (glGetNamedProgramivEXT = GLContext.getFunctionAddress("glGetNamedProgramivEXT")) != 0) &
			(!supported_extensions.contains("GL_ARB_vertex_program") || (glGetNamedProgramStringEXT = GLContext.getFunctionAddress("glGetNamedProgramStringEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCompressedTextureImage3DEXT = GLContext.getFunctionAddress("glCompressedTextureImage3DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCompressedTextureImage2DEXT = GLContext.getFunctionAddress("glCompressedTextureImage2DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCompressedTextureImage1DEXT = GLContext.getFunctionAddress("glCompressedTextureImage1DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCompressedTextureSubImage3DEXT = GLContext.getFunctionAddress("glCompressedTextureSubImage3DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCompressedTextureSubImage2DEXT = GLContext.getFunctionAddress("glCompressedTextureSubImage2DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCompressedTextureSubImage1DEXT = GLContext.getFunctionAddress("glCompressedTextureSubImage1DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glGetCompressedTextureImageEXT = GLContext.getFunctionAddress("glGetCompressedTextureImageEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCompressedMultiTexImage3DEXT = GLContext.getFunctionAddress("glCompressedMultiTexImage3DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCompressedMultiTexImage2DEXT = GLContext.getFunctionAddress("glCompressedMultiTexImage2DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCompressedMultiTexImage1DEXT = GLContext.getFunctionAddress("glCompressedMultiTexImage1DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCompressedMultiTexSubImage3DEXT = GLContext.getFunctionAddress("glCompressedMultiTexSubImage3DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCompressedMultiTexSubImage2DEXT = GLContext.getFunctionAddress("glCompressedMultiTexSubImage2DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glCompressedMultiTexSubImage1DEXT = GLContext.getFunctionAddress("glCompressedMultiTexSubImage1DEXT")) != 0) &
			(!supported_extensions.contains("OpenGL13") || (glGetCompressedMultiTexImageEXT = GLContext.getFunctionAddress("glGetCompressedMultiTexImageEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMatrixLoadTransposefEXT = GLContext.getFunctionAddress("glMatrixLoadTransposefEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMatrixLoadTransposedEXT = GLContext.getFunctionAddress("glMatrixLoadTransposedEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMatrixMultTransposefEXT = GLContext.getFunctionAddress("glMatrixMultTransposefEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL13") || (glMatrixMultTransposedEXT = GLContext.getFunctionAddress("glMatrixMultTransposedEXT")) != 0) &
			(!supported_extensions.contains("OpenGL15") || (glNamedBufferDataEXT = GLContext.getFunctionAddress("glNamedBufferDataEXT")) != 0) &
			(!supported_extensions.contains("OpenGL15") || (glNamedBufferSubDataEXT = GLContext.getFunctionAddress("glNamedBufferSubDataEXT")) != 0) &
			(!supported_extensions.contains("OpenGL15") || (glMapNamedBufferEXT = GLContext.getFunctionAddress("glMapNamedBufferEXT")) != 0) &
			(!supported_extensions.contains("OpenGL15") || (glUnmapNamedBufferEXT = GLContext.getFunctionAddress("glUnmapNamedBufferEXT")) != 0) &
			(!supported_extensions.contains("OpenGL15") || (glGetNamedBufferParameterivEXT = GLContext.getFunctionAddress("glGetNamedBufferParameterivEXT")) != 0) &
			(!supported_extensions.contains("OpenGL15") || (glGetNamedBufferPointervEXT = GLContext.getFunctionAddress("glGetNamedBufferPointervEXT")) != 0) &
			(!supported_extensions.contains("OpenGL15") || (glGetNamedBufferSubDataEXT = GLContext.getFunctionAddress("glGetNamedBufferSubDataEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform1fEXT = GLContext.getFunctionAddress("glProgramUniform1fEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform2fEXT = GLContext.getFunctionAddress("glProgramUniform2fEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform3fEXT = GLContext.getFunctionAddress("glProgramUniform3fEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform4fEXT = GLContext.getFunctionAddress("glProgramUniform4fEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform1iEXT = GLContext.getFunctionAddress("glProgramUniform1iEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform2iEXT = GLContext.getFunctionAddress("glProgramUniform2iEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform3iEXT = GLContext.getFunctionAddress("glProgramUniform3iEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform4iEXT = GLContext.getFunctionAddress("glProgramUniform4iEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform1fvEXT = GLContext.getFunctionAddress("glProgramUniform1fvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform2fvEXT = GLContext.getFunctionAddress("glProgramUniform2fvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform3fvEXT = GLContext.getFunctionAddress("glProgramUniform3fvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform4fvEXT = GLContext.getFunctionAddress("glProgramUniform4fvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform1ivEXT = GLContext.getFunctionAddress("glProgramUniform1ivEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform2ivEXT = GLContext.getFunctionAddress("glProgramUniform2ivEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform3ivEXT = GLContext.getFunctionAddress("glProgramUniform3ivEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniform4ivEXT = GLContext.getFunctionAddress("glProgramUniform4ivEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniformMatrix2fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2fvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniformMatrix3fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3fvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL20") || (glProgramUniformMatrix4fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4fvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL21") || (glProgramUniformMatrix2x3fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2x3fvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL21") || (glProgramUniformMatrix3x2fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3x2fvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL21") || (glProgramUniformMatrix2x4fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2x4fvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL21") || (glProgramUniformMatrix4x2fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4x2fvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL21") || (glProgramUniformMatrix3x4fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3x4fvEXT")) != 0) &
			(!supported_extensions.contains("OpenGL21") || (glProgramUniformMatrix4x3fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4x3fvEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_texture_buffer_object") || (glTextureBufferEXT = GLContext.getFunctionAddress("glTextureBufferEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_texture_buffer_object") || (glMultiTexBufferEXT = GLContext.getFunctionAddress("glMultiTexBufferEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_texture_integer") || (glTextureParameterIivEXT = GLContext.getFunctionAddress("glTextureParameterIivEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_texture_integer") || (glTextureParameterIuivEXT = GLContext.getFunctionAddress("glTextureParameterIuivEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_texture_integer") || (glGetTextureParameterIivEXT = GLContext.getFunctionAddress("glGetTextureParameterIivEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_texture_integer") || (glGetTextureParameterIuivEXT = GLContext.getFunctionAddress("glGetTextureParameterIuivEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_texture_integer") || (glMultiTexParameterIivEXT = GLContext.getFunctionAddress("glMultiTexParameterIivEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_texture_integer") || (glMultiTexParameterIuivEXT = GLContext.getFunctionAddress("glMultiTexParameterIuivEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_texture_integer") || (glGetMultiTexParameterIivEXT = GLContext.getFunctionAddress("glGetMultiTexParameterIivEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_texture_integer") || (glGetMultiTexParameterIuivEXT = GLContext.getFunctionAddress("glGetMultiTexParameterIuivEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_gpu_shader4") || (glProgramUniform1uiEXT = GLContext.getFunctionAddress("glProgramUniform1uiEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_gpu_shader4") || (glProgramUniform2uiEXT = GLContext.getFunctionAddress("glProgramUniform2uiEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_gpu_shader4") || (glProgramUniform3uiEXT = GLContext.getFunctionAddress("glProgramUniform3uiEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_gpu_shader4") || (glProgramUniform4uiEXT = GLContext.getFunctionAddress("glProgramUniform4uiEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_gpu_shader4") || (glProgramUniform1uivEXT = GLContext.getFunctionAddress("glProgramUniform1uivEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_gpu_shader4") || (glProgramUniform2uivEXT = GLContext.getFunctionAddress("glProgramUniform2uivEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_gpu_shader4") || (glProgramUniform3uivEXT = GLContext.getFunctionAddress("glProgramUniform3uivEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_gpu_shader4") || (glProgramUniform4uivEXT = GLContext.getFunctionAddress("glProgramUniform4uivEXT")) != 0) &
			(!supported_extensions.contains("GL_EXT_gpu_program_parameters") || (glNamedProgramLocalParameters4fvEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameters4fvEXT")) != 0) &
			(!supported_extensions.contains("GL_NV_gpu_program4") || (glNamedProgramLocalParameterI4iEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameterI4iEXT")) != 0) &
			(!supported_extensions.contains("GL_NV_gpu_program4") || (glNamedProgramLocalParameterI4ivEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameterI4ivEXT")) != 0) &
			(!supported_extensions.contains("GL_NV_gpu_program4") || (glNamedProgramLocalParametersI4ivEXT = GLContext.getFunctionAddress("glNamedProgramLocalParametersI4ivEXT")) != 0) &
			(!supported_extensions.contains("GL_NV_gpu_program4") || (glNamedProgramLocalParameterI4uiEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameterI4uiEXT")) != 0) &
			(!supported_extensions.contains("GL_NV_gpu_program4") || (glNamedProgramLocalParameterI4uivEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameterI4uivEXT")) != 0) &
			(!supported_extensions.contains("GL_NV_gpu_program4") || (glNamedProgramLocalParametersI4uivEXT = GLContext.getFunctionAddress("glNamedProgramLocalParametersI4uivEXT")) != 0) &
			(!supported_extensions.contains("GL_NV_gpu_program4") || (glGetNamedProgramLocalParameterIivEXT = GLContext.getFunctionAddress("glGetNamedProgramLocalParameterIivEXT")) != 0) &
			(!supported_extensions.contains("GL_NV_gpu_program4") || (glGetNamedProgramLocalParameterIuivEXT = GLContext.getFunctionAddress("glGetNamedProgramLocalParameterIuivEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) || (glNamedRenderbufferStorageEXT = GLContext.getFunctionAddress("glNamedRenderbufferStorageEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) || (glGetNamedRenderbufferParameterivEXT = GLContext.getFunctionAddress("glGetNamedRenderbufferParameterivEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_multisample")) || (glNamedRenderbufferStorageMultisampleEXT = GLContext.getFunctionAddress("glNamedRenderbufferStorageMultisampleEXT")) != 0) &
			(!supported_extensions.contains("GL_NV_framebuffer_multisample_coverage") || (glNamedRenderbufferStorageMultisampleCoverageEXT = GLContext.getFunctionAddress("glNamedRenderbufferStorageMultisampleCoverageEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) || (glCheckNamedFramebufferStatusEXT = GLContext.getFunctionAddress("glCheckNamedFramebufferStatusEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) || (glNamedFramebufferTexture1DEXT = GLContext.getFunctionAddress("glNamedFramebufferTexture1DEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) || (glNamedFramebufferTexture2DEXT = GLContext.getFunctionAddress("glNamedFramebufferTexture2DEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) || (glNamedFramebufferTexture3DEXT = GLContext.getFunctionAddress("glNamedFramebufferTexture3DEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) || (glNamedFramebufferRenderbufferEXT = GLContext.getFunctionAddress("glNamedFramebufferRenderbufferEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) || (glGetNamedFramebufferAttachmentParameterivEXT = GLContext.getFunctionAddress("glGetNamedFramebufferAttachmentParameterivEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) || (glGenerateTextureMipmapEXT = GLContext.getFunctionAddress("glGenerateTextureMipmapEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) || (glGenerateMultiTexMipmapEXT = GLContext.getFunctionAddress("glGenerateMultiTexMipmapEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) || (glFramebufferDrawBufferEXT = GLContext.getFunctionAddress("glFramebufferDrawBufferEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) || (glFramebufferDrawBuffersEXT = GLContext.getFunctionAddress("glFramebufferDrawBuffersEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) || (glFramebufferReadBufferEXT = GLContext.getFunctionAddress("glFramebufferReadBufferEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) || (glGetFramebufferParameterivEXT = GLContext.getFunctionAddress("glGetFramebufferParameterivEXT")) != 0) &
			(!(false || supported_extensions.contains("OpenGL31") || supported_extensions.contains("GL_ARB_copy_buffer")) || (glNamedCopyBufferSubDataEXT = GLContext.getFunctionAddress("glNamedCopyBufferSubDataEXT")) != 0) &
			(!(false || supported_extensions.contains("GL_EXT_geometry_shader4") || supported_extensions.contains("GL_NV_geometry_program4")) || (glNamedFramebufferTextureEXT = GLContext.getFunctionAddress("glNamedFramebufferTextureEXT")) != 0) &
			(!(false || supported_extensions.contains("GL_EXT_geometry_shader4") || supported_extensions.contains("GL_NV_geometry_program4")) || (glNamedFramebufferTextureLayerEXT = GLContext.getFunctionAddress("glNamedFramebufferTextureLayerEXT")) != 0) &
			(!(false || supported_extensions.contains("GL_EXT_geometry_shader4") || supported_extensions.contains("GL_NV_geometry_program4")) || (glNamedFramebufferTextureFaceEXT = GLContext.getFunctionAddress("glNamedFramebufferTextureFaceEXT")) != 0) &
			(!supported_extensions.contains("GL_NV_explicit_multisample") || (glTextureRenderbufferEXT = GLContext.getFunctionAddress("glTextureRenderbufferEXT")) != 0) &
			(!supported_extensions.contains("GL_NV_explicit_multisample") || (glMultiTexRenderbufferEXT = GLContext.getFunctionAddress("glMultiTexRenderbufferEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL30") || (glVertexArrayVertexOffsetEXT = GLContext.getFunctionAddress("glVertexArrayVertexOffsetEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL30") || (glVertexArrayColorOffsetEXT = GLContext.getFunctionAddress("glVertexArrayColorOffsetEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL30") || (glVertexArrayEdgeFlagOffsetEXT = GLContext.getFunctionAddress("glVertexArrayEdgeFlagOffsetEXT")) != 0) &
			(!supported_extensions.contains("OpenGL30") || (glVertexArrayIndexOffsetEXT = GLContext.getFunctionAddress("glVertexArrayIndexOffsetEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL30") || (glVertexArrayNormalOffsetEXT = GLContext.getFunctionAddress("glVertexArrayNormalOffsetEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL30") || (glVertexArrayTexCoordOffsetEXT = GLContext.getFunctionAddress("glVertexArrayTexCoordOffsetEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL30") || (glVertexArrayMultiTexCoordOffsetEXT = GLContext.getFunctionAddress("glVertexArrayMultiTexCoordOffsetEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL30") || (glVertexArrayFogCoordOffsetEXT = GLContext.getFunctionAddress("glVertexArrayFogCoordOffsetEXT")) != 0) &
			(forwardCompatible || !supported_extensions.contains("OpenGL30") || (glVertexArraySecondaryColorOffsetEXT = GLContext.getFunctionAddress("glVertexArraySecondaryColorOffsetEXT")) != 0) &
			(!supported_extensions.contains("OpenGL30") || (glVertexArrayVertexAttribOffsetEXT = GLContext.getFunctionAddress("glVertexArrayVertexAttribOffsetEXT")) != 0) &
			(!supported_extensions.contains("OpenGL30") || (glVertexArrayVertexAttribIOffsetEXT = GLContext.getFunctionAddress("glVertexArrayVertexAttribIOffsetEXT")) != 0) &
			(!supported_extensions.contains("OpenGL30") || (glEnableVertexArrayEXT = GLContext.getFunctionAddress("glEnableVertexArrayEXT")) != 0) &
			(!supported_extensions.contains("OpenGL30") || (glDisableVertexArrayEXT = GLContext.getFunctionAddress("glDisableVertexArrayEXT")) != 0) &
			(!supported_extensions.contains("OpenGL30") || (glEnableVertexArrayAttribEXT = GLContext.getFunctionAddress("glEnableVertexArrayAttribEXT")) != 0) &
			(!supported_extensions.contains("OpenGL30") || (glDisableVertexArrayAttribEXT = GLContext.getFunctionAddress("glDisableVertexArrayAttribEXT")) != 0) &
			(!supported_extensions.contains("OpenGL30") || (glGetVertexArrayIntegervEXT = GLContext.getFunctionAddress("glGetVertexArrayIntegervEXT")) != 0) &
			(!supported_extensions.contains("OpenGL30") || (glGetVertexArrayPointervEXT = GLContext.getFunctionAddress("glGetVertexArrayPointervEXT")) != 0) &
			(!supported_extensions.contains("OpenGL30") || (glGetVertexArrayIntegeri_vEXT = GLContext.getFunctionAddress("glGetVertexArrayIntegeri_vEXT")) != 0) &
			(!supported_extensions.contains("OpenGL30") || (glGetVertexArrayPointeri_vEXT = GLContext.getFunctionAddress("glGetVertexArrayPointeri_vEXT")) != 0) &
			(!supported_extensions.contains("OpenGL30") || (glMapNamedBufferRangeEXT = GLContext.getFunctionAddress("glMapNamedBufferRangeEXT")) != 0) &
			(!supported_extensions.contains("OpenGL30") || (glFlushMappedNamedBufferRangeEXT = GLContext.getFunctionAddress("glFlushMappedNamedBufferRangeEXT")) != 0);
	}

	private boolean EXT_draw_buffers2_initNativeFunctionAddresses() {
		return 
			(glColorMaskIndexedEXT = GLContext.getFunctionAddress("glColorMaskIndexedEXT")) != 0 &
			(glGetBooleanIndexedvEXT = GLContext.getFunctionAddress("glGetBooleanIndexedvEXT")) != 0 &
			(glGetIntegerIndexedvEXT = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT")) != 0 &
			(glEnableIndexedEXT = GLContext.getFunctionAddress("glEnableIndexedEXT")) != 0 &
			(glDisableIndexedEXT = GLContext.getFunctionAddress("glDisableIndexedEXT")) != 0 &
			(glIsEnabledIndexedEXT = GLContext.getFunctionAddress("glIsEnabledIndexedEXT")) != 0;
	}

	private boolean EXT_draw_instanced_initNativeFunctionAddresses() {
		return 
			(glDrawArraysInstancedEXT = GLContext.getFunctionAddress("glDrawArraysInstancedEXT")) != 0 &
			(glDrawElementsInstancedEXT = GLContext.getFunctionAddress("glDrawElementsInstancedEXT")) != 0;
	}

	private boolean EXT_draw_range_elements_initNativeFunctionAddresses() {
		return 
			(glDrawRangeElementsEXT = GLContext.getFunctionAddress("glDrawRangeElementsEXT")) != 0;
	}

	private boolean EXT_fog_coord_initNativeFunctionAddresses() {
		return 
			(glFogCoordfEXT = GLContext.getFunctionAddress("glFogCoordfEXT")) != 0 &
			(glFogCoorddEXT = GLContext.getFunctionAddress("glFogCoorddEXT")) != 0 &
			(glFogCoordPointerEXT = GLContext.getFunctionAddress("glFogCoordPointerEXT")) != 0;
	}

	private boolean EXT_framebuffer_blit_initNativeFunctionAddresses() {
		return 
			(glBlitFramebufferEXT = GLContext.getFunctionAddress("glBlitFramebufferEXT")) != 0;
	}

	private boolean EXT_framebuffer_multisample_initNativeFunctionAddresses() {
		return 
			(glRenderbufferStorageMultisampleEXT = GLContext.getFunctionAddress("glRenderbufferStorageMultisampleEXT")) != 0;
	}

	private boolean EXT_framebuffer_object_initNativeFunctionAddresses() {
		return 
			(glIsRenderbufferEXT = GLContext.getFunctionAddress("glIsRenderbufferEXT")) != 0 &
			(glBindRenderbufferEXT = GLContext.getFunctionAddress("glBindRenderbufferEXT")) != 0 &
			(glDeleteRenderbuffersEXT = GLContext.getFunctionAddress("glDeleteRenderbuffersEXT")) != 0 &
			(glGenRenderbuffersEXT = GLContext.getFunctionAddress("glGenRenderbuffersEXT")) != 0 &
			(glRenderbufferStorageEXT = GLContext.getFunctionAddress("glRenderbufferStorageEXT")) != 0 &
			(glGetRenderbufferParameterivEXT = GLContext.getFunctionAddress("glGetRenderbufferParameterivEXT")) != 0 &
			(glIsFramebufferEXT = GLContext.getFunctionAddress("glIsFramebufferEXT")) != 0 &
			(glBindFramebufferEXT = GLContext.getFunctionAddress("glBindFramebufferEXT")) != 0 &
			(glDeleteFramebuffersEXT = GLContext.getFunctionAddress("glDeleteFramebuffersEXT")) != 0 &
			(glGenFramebuffersEXT = GLContext.getFunctionAddress("glGenFramebuffersEXT")) != 0 &
			(glCheckFramebufferStatusEXT = GLContext.getFunctionAddress("glCheckFramebufferStatusEXT")) != 0 &
			(glFramebufferTexture1DEXT = GLContext.getFunctionAddress("glFramebufferTexture1DEXT")) != 0 &
			(glFramebufferTexture2DEXT = GLContext.getFunctionAddress("glFramebufferTexture2DEXT")) != 0 &
			(glFramebufferTexture3DEXT = GLContext.getFunctionAddress("glFramebufferTexture3DEXT")) != 0 &
			(glFramebufferRenderbufferEXT = GLContext.getFunctionAddress("glFramebufferRenderbufferEXT")) != 0 &
			(glGetFramebufferAttachmentParameterivEXT = GLContext.getFunctionAddress("glGetFramebufferAttachmentParameterivEXT")) != 0 &
			(glGenerateMipmapEXT = GLContext.getFunctionAddress("glGenerateMipmapEXT")) != 0;
	}

	private boolean EXT_geometry_shader4_initNativeFunctionAddresses() {
		return 
			(glProgramParameteriEXT = GLContext.getFunctionAddress("glProgramParameteriEXT")) != 0 &
			(glFramebufferTextureEXT = GLContext.getFunctionAddress("glFramebufferTextureEXT")) != 0 &
			(glFramebufferTextureLayerEXT = GLContext.getFunctionAddress("glFramebufferTextureLayerEXT")) != 0 &
			(glFramebufferTextureFaceEXT = GLContext.getFunctionAddress("glFramebufferTextureFaceEXT")) != 0;
	}

	private boolean EXT_gpu_program_parameters_initNativeFunctionAddresses() {
		return 
			(glProgramEnvParameters4fvEXT = GLContext.getFunctionAddress("glProgramEnvParameters4fvEXT")) != 0 &
			(glProgramLocalParameters4fvEXT = GLContext.getFunctionAddress("glProgramLocalParameters4fvEXT")) != 0;
	}

	private boolean EXT_gpu_shader4_initNativeFunctionAddresses() {
		return 
			(glVertexAttribI1iEXT = GLContext.getFunctionAddress("glVertexAttribI1iEXT")) != 0 &
			(glVertexAttribI2iEXT = GLContext.getFunctionAddress("glVertexAttribI2iEXT")) != 0 &
			(glVertexAttribI3iEXT = GLContext.getFunctionAddress("glVertexAttribI3iEXT")) != 0 &
			(glVertexAttribI4iEXT = GLContext.getFunctionAddress("glVertexAttribI4iEXT")) != 0 &
			(glVertexAttribI1uiEXT = GLContext.getFunctionAddress("glVertexAttribI1uiEXT")) != 0 &
			(glVertexAttribI2uiEXT = GLContext.getFunctionAddress("glVertexAttribI2uiEXT")) != 0 &
			(glVertexAttribI3uiEXT = GLContext.getFunctionAddress("glVertexAttribI3uiEXT")) != 0 &
			(glVertexAttribI4uiEXT = GLContext.getFunctionAddress("glVertexAttribI4uiEXT")) != 0 &
			(glVertexAttribI1ivEXT = GLContext.getFunctionAddress("glVertexAttribI1ivEXT")) != 0 &
			(glVertexAttribI2ivEXT = GLContext.getFunctionAddress("glVertexAttribI2ivEXT")) != 0 &
			(glVertexAttribI3ivEXT = GLContext.getFunctionAddress("glVertexAttribI3ivEXT")) != 0 &
			(glVertexAttribI4ivEXT = GLContext.getFunctionAddress("glVertexAttribI4ivEXT")) != 0 &
			(glVertexAttribI1uivEXT = GLContext.getFunctionAddress("glVertexAttribI1uivEXT")) != 0 &
			(glVertexAttribI2uivEXT = GLContext.getFunctionAddress("glVertexAttribI2uivEXT")) != 0 &
			(glVertexAttribI3uivEXT = GLContext.getFunctionAddress("glVertexAttribI3uivEXT")) != 0 &
			(glVertexAttribI4uivEXT = GLContext.getFunctionAddress("glVertexAttribI4uivEXT")) != 0 &
			(glVertexAttribI4bvEXT = GLContext.getFunctionAddress("glVertexAttribI4bvEXT")) != 0 &
			(glVertexAttribI4svEXT = GLContext.getFunctionAddress("glVertexAttribI4svEXT")) != 0 &
			(glVertexAttribI4ubvEXT = GLContext.getFunctionAddress("glVertexAttribI4ubvEXT")) != 0 &
			(glVertexAttribI4usvEXT = GLContext.getFunctionAddress("glVertexAttribI4usvEXT")) != 0 &
			(glVertexAttribIPointerEXT = GLContext.getFunctionAddress("glVertexAttribIPointerEXT")) != 0 &
			(glGetVertexAttribIivEXT = GLContext.getFunctionAddress("glGetVertexAttribIivEXT")) != 0 &
			(glGetVertexAttribIuivEXT = GLContext.getFunctionAddress("glGetVertexAttribIuivEXT")) != 0 &
			(glUniform1uiEXT = GLContext.getFunctionAddress("glUniform1uiEXT")) != 0 &
			(glUniform2uiEXT = GLContext.getFunctionAddress("glUniform2uiEXT")) != 0 &
			(glUniform3uiEXT = GLContext.getFunctionAddress("glUniform3uiEXT")) != 0 &
			(glUniform4uiEXT = GLContext.getFunctionAddress("glUniform4uiEXT")) != 0 &
			(glUniform1uivEXT = GLContext.getFunctionAddress("glUniform1uivEXT")) != 0 &
			(glUniform2uivEXT = GLContext.getFunctionAddress("glUniform2uivEXT")) != 0 &
			(glUniform3uivEXT = GLContext.getFunctionAddress("glUniform3uivEXT")) != 0 &
			(glUniform4uivEXT = GLContext.getFunctionAddress("glUniform4uivEXT")) != 0 &
			(glGetUniformuivEXT = GLContext.getFunctionAddress("glGetUniformuivEXT")) != 0 &
			(glBindFragDataLocationEXT = GLContext.getFunctionAddress("glBindFragDataLocationEXT")) != 0 &
			(glGetFragDataLocationEXT = GLContext.getFunctionAddress("glGetFragDataLocationEXT")) != 0;
	}

	private boolean EXT_multi_draw_arrays_initNativeFunctionAddresses() {
		return 
			(glMultiDrawArraysEXT = GLContext.getFunctionAddress("glMultiDrawArraysEXT")) != 0;
	}

	private boolean EXT_paletted_texture_initNativeFunctionAddresses() {
		return 
			(glColorTableEXT = GLContext.getFunctionAddress("glColorTableEXT")) != 0 &
			(glColorSubTableEXT = GLContext.getFunctionAddress("glColorSubTableEXT")) != 0 &
			(glGetColorTableEXT = GLContext.getFunctionAddress("glGetColorTableEXT")) != 0 &
			(glGetColorTableParameterivEXT = GLContext.getFunctionAddress("glGetColorTableParameterivEXT")) != 0 &
			(glGetColorTableParameterfvEXT = GLContext.getFunctionAddress("glGetColorTableParameterfvEXT")) != 0;
	}

	private boolean EXT_point_parameters_initNativeFunctionAddresses() {
		return 
			(glPointParameterfEXT = GLContext.getFunctionAddress("glPointParameterfEXT")) != 0 &
			(glPointParameterfvEXT = GLContext.getFunctionAddress("glPointParameterfvEXT")) != 0;
	}

	private boolean EXT_provoking_vertex_initNativeFunctionAddresses() {
		return 
			(glProvokingVertexEXT = GLContext.getFunctionAddress("glProvokingVertexEXT")) != 0;
	}

	private boolean EXT_secondary_color_initNativeFunctionAddresses() {
		return 
			(glSecondaryColor3bEXT = GLContext.getFunctionAddress("glSecondaryColor3bEXT")) != 0 &
			(glSecondaryColor3fEXT = GLContext.getFunctionAddress("glSecondaryColor3fEXT")) != 0 &
			(glSecondaryColor3dEXT = GLContext.getFunctionAddress("glSecondaryColor3dEXT")) != 0 &
			(glSecondaryColor3ubEXT = GLContext.getFunctionAddress("glSecondaryColor3ubEXT")) != 0 &
			(glSecondaryColorPointerEXT = GLContext.getFunctionAddress("glSecondaryColorPointerEXT")) != 0;
	}

	private boolean EXT_separate_shader_objects_initNativeFunctionAddresses() {
		return 
			(glUseShaderProgramEXT = GLContext.getFunctionAddress("glUseShaderProgramEXT")) != 0 &
			(glActiveProgramEXT = GLContext.getFunctionAddress("glActiveProgramEXT")) != 0 &
			(glCreateShaderProgramEXT = GLContext.getFunctionAddress("glCreateShaderProgramEXT")) != 0;
	}

	private boolean EXT_shader_image_load_store_initNativeFunctionAddresses() {
		return 
			(glBindImageTextureEXT = GLContext.getFunctionAddress("glBindImageTextureEXT")) != 0 &
			(glMemoryBarrierEXT = GLContext.getFunctionAddress("glMemoryBarrierEXT")) != 0;
	}

	private boolean EXT_stencil_clear_tag_initNativeFunctionAddresses() {
		return 
			(glStencilClearTagEXT = GLContext.getFunctionAddress("glStencilClearTagEXT")) != 0;
	}

	private boolean EXT_stencil_two_side_initNativeFunctionAddresses() {
		return 
			(glActiveStencilFaceEXT = GLContext.getFunctionAddress("glActiveStencilFaceEXT")) != 0;
	}

	private boolean EXT_texture_array_initNativeFunctionAddresses() {
		return 
			(glFramebufferTextureLayerEXT = GLContext.getFunctionAddress("glFramebufferTextureLayerEXT")) != 0;
	}

	private boolean EXT_texture_buffer_object_initNativeFunctionAddresses() {
		return 
			(glTexBufferEXT = GLContext.getFunctionAddress("glTexBufferEXT")) != 0;
	}

	private boolean EXT_texture_integer_initNativeFunctionAddresses() {
		return 
			(glClearColorIiEXT = GLContext.getFunctionAddress("glClearColorIiEXT")) != 0 &
			(glClearColorIuiEXT = GLContext.getFunctionAddress("glClearColorIuiEXT")) != 0 &
			(glTexParameterIivEXT = GLContext.getFunctionAddress("glTexParameterIivEXT")) != 0 &
			(glTexParameterIuivEXT = GLContext.getFunctionAddress("glTexParameterIuivEXT")) != 0 &
			(glGetTexParameterIivEXT = GLContext.getFunctionAddress("glGetTexParameterIivEXT")) != 0 &
			(glGetTexParameterIuivEXT = GLContext.getFunctionAddress("glGetTexParameterIuivEXT")) != 0;
	}

	private boolean EXT_timer_query_initNativeFunctionAddresses() {
		return 
			(glGetQueryObjecti64vEXT = GLContext.getFunctionAddress("glGetQueryObjecti64vEXT")) != 0 &
			(glGetQueryObjectui64vEXT = GLContext.getFunctionAddress("glGetQueryObjectui64vEXT")) != 0;
	}

	private boolean EXT_transform_feedback_initNativeFunctionAddresses() {
		return 
			(glBindBufferRangeEXT = GLContext.getFunctionAddress("glBindBufferRangeEXT")) != 0 &
			(glBindBufferOffsetEXT = GLContext.getFunctionAddress("glBindBufferOffsetEXT")) != 0 &
			(glBindBufferBaseEXT = GLContext.getFunctionAddress("glBindBufferBaseEXT")) != 0 &
			(glBeginTransformFeedbackEXT = GLContext.getFunctionAddress("glBeginTransformFeedbackEXT")) != 0 &
			(glEndTransformFeedbackEXT = GLContext.getFunctionAddress("glEndTransformFeedbackEXT")) != 0 &
			(glTransformFeedbackVaryingsEXT = GLContext.getFunctionAddress("glTransformFeedbackVaryingsEXT")) != 0 &
			(glGetTransformFeedbackVaryingEXT = GLContext.getFunctionAddress("glGetTransformFeedbackVaryingEXT")) != 0;
	}

	private boolean EXT_vertex_attrib_64bit_initNativeFunctionAddresses(Set<String> supported_extensions) {
		return 
			(glVertexAttribL1dEXT = GLContext.getFunctionAddress("glVertexAttribL1dEXT")) != 0 &
			(glVertexAttribL2dEXT = GLContext.getFunctionAddress("glVertexAttribL2dEXT")) != 0 &
			(glVertexAttribL3dEXT = GLContext.getFunctionAddress("glVertexAttribL3dEXT")) != 0 &
			(glVertexAttribL4dEXT = GLContext.getFunctionAddress("glVertexAttribL4dEXT")) != 0 &
			(glVertexAttribL1dvEXT = GLContext.getFunctionAddress("glVertexAttribL1dvEXT")) != 0 &
			(glVertexAttribL2dvEXT = GLContext.getFunctionAddress("glVertexAttribL2dvEXT")) != 0 &
			(glVertexAttribL3dvEXT = GLContext.getFunctionAddress("glVertexAttribL3dvEXT")) != 0 &
			(glVertexAttribL4dvEXT = GLContext.getFunctionAddress("glVertexAttribL4dvEXT")) != 0 &
			(glVertexAttribLPointerEXT = GLContext.getFunctionAddress("glVertexAttribLPointerEXT")) != 0 &
			(glGetVertexAttribLdvEXT = GLContext.getFunctionAddress("glGetVertexAttribLdvEXT")) != 0 &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glVertexArrayVertexAttribLOffsetEXT = GLContext.getFunctionAddress("glVertexArrayVertexAttribLOffsetEXT")) != 0);
	}

	private boolean EXT_vertex_shader_initNativeFunctionAddresses() {
		return 
			(glBeginVertexShaderEXT = GLContext.getFunctionAddress("glBeginVertexShaderEXT")) != 0 &
			(glEndVertexShaderEXT = GLContext.getFunctionAddress("glEndVertexShaderEXT")) != 0 &
			(glBindVertexShaderEXT = GLContext.getFunctionAddress("glBindVertexShaderEXT")) != 0 &
			(glGenVertexShadersEXT = GLContext.getFunctionAddress("glGenVertexShadersEXT")) != 0 &
			(glDeleteVertexShaderEXT = GLContext.getFunctionAddress("glDeleteVertexShaderEXT")) != 0 &
			(glShaderOp1EXT = GLContext.getFunctionAddress("glShaderOp1EXT")) != 0 &
			(glShaderOp2EXT = GLContext.getFunctionAddress("glShaderOp2EXT")) != 0 &
			(glShaderOp3EXT = GLContext.getFunctionAddress("glShaderOp3EXT")) != 0 &
			(glSwizzleEXT = GLContext.getFunctionAddress("glSwizzleEXT")) != 0 &
			(glWriteMaskEXT = GLContext.getFunctionAddress("glWriteMaskEXT")) != 0 &
			(glInsertComponentEXT = GLContext.getFunctionAddress("glInsertComponentEXT")) != 0 &
			(glExtractComponentEXT = GLContext.getFunctionAddress("glExtractComponentEXT")) != 0 &
			(glGenSymbolsEXT = GLContext.getFunctionAddress("glGenSymbolsEXT")) != 0 &
			(glSetInvariantEXT = GLContext.getFunctionAddress("glSetInvariantEXT")) != 0 &
			(glSetLocalConstantEXT = GLContext.getFunctionAddress("glSetLocalConstantEXT")) != 0 &
			(glVariantbvEXT = GLContext.getFunctionAddress("glVariantbvEXT")) != 0 &
			(glVariantsvEXT = GLContext.getFunctionAddress("glVariantsvEXT")) != 0 &
			(glVariantivEXT = GLContext.getFunctionAddress("glVariantivEXT")) != 0 &
			(glVariantfvEXT = GLContext.getFunctionAddress("glVariantfvEXT")) != 0 &
			(glVariantdvEXT = GLContext.getFunctionAddress("glVariantdvEXT")) != 0 &
			(glVariantubvEXT = GLContext.getFunctionAddress("glVariantubvEXT")) != 0 &
			(glVariantusvEXT = GLContext.getFunctionAddress("glVariantusvEXT")) != 0 &
			(glVariantuivEXT = GLContext.getFunctionAddress("glVariantuivEXT")) != 0 &
			(glVariantPointerEXT = GLContext.getFunctionAddress("glVariantPointerEXT")) != 0 &
			(glEnableVariantClientStateEXT = GLContext.getFunctionAddress("glEnableVariantClientStateEXT")) != 0 &
			(glDisableVariantClientStateEXT = GLContext.getFunctionAddress("glDisableVariantClientStateEXT")) != 0 &
			(glBindLightParameterEXT = GLContext.getFunctionAddress("glBindLightParameterEXT")) != 0 &
			(glBindMaterialParameterEXT = GLContext.getFunctionAddress("glBindMaterialParameterEXT")) != 0 &
			(glBindTexGenParameterEXT = GLContext.getFunctionAddress("glBindTexGenParameterEXT")) != 0 &
			(glBindTextureUnitParameterEXT = GLContext.getFunctionAddress("glBindTextureUnitParameterEXT")) != 0 &
			(glBindParameterEXT = GLContext.getFunctionAddress("glBindParameterEXT")) != 0 &
			(glIsVariantEnabledEXT = GLContext.getFunctionAddress("glIsVariantEnabledEXT")) != 0 &
			(glGetVariantBooleanvEXT = GLContext.getFunctionAddress("glGetVariantBooleanvEXT")) != 0 &
			(glGetVariantIntegervEXT = GLContext.getFunctionAddress("glGetVariantIntegervEXT")) != 0 &
			(glGetVariantFloatvEXT = GLContext.getFunctionAddress("glGetVariantFloatvEXT")) != 0 &
			(glGetVariantPointervEXT = GLContext.getFunctionAddress("glGetVariantPointervEXT")) != 0 &
			(glGetInvariantBooleanvEXT = GLContext.getFunctionAddress("glGetInvariantBooleanvEXT")) != 0 &
			(glGetInvariantIntegervEXT = GLContext.getFunctionAddress("glGetInvariantIntegervEXT")) != 0 &
			(glGetInvariantFloatvEXT = GLContext.getFunctionAddress("glGetInvariantFloatvEXT")) != 0 &
			(glGetLocalConstantBooleanvEXT = GLContext.getFunctionAddress("glGetLocalConstantBooleanvEXT")) != 0 &
			(glGetLocalConstantIntegervEXT = GLContext.getFunctionAddress("glGetLocalConstantIntegervEXT")) != 0 &
			(glGetLocalConstantFloatvEXT = GLContext.getFunctionAddress("glGetLocalConstantFloatvEXT")) != 0;
	}

	private boolean EXT_vertex_weighting_initNativeFunctionAddresses() {
		return 
			(glVertexWeightfEXT = GLContext.getFunctionAddress("glVertexWeightfEXT")) != 0 &
			(glVertexWeightPointerEXT = GLContext.getFunctionAddress("glVertexWeightPointerEXT")) != 0;
	}

	private boolean GL11_initNativeFunctionAddresses(boolean forwardCompatible) {
		return 
			(forwardCompatible || (glAccum = GLContext.getFunctionAddress("glAccum")) != 0) &
			(forwardCompatible || (glAlphaFunc = GLContext.getFunctionAddress("glAlphaFunc")) != 0) &
			(glClearColor = GLContext.getFunctionAddress("glClearColor")) != 0 &
			(forwardCompatible || (glClearAccum = GLContext.getFunctionAddress("glClearAccum")) != 0) &
			(glClear = GLContext.getFunctionAddress("glClear")) != 0 &
			(forwardCompatible || (glCallLists = GLContext.getFunctionAddress("glCallLists")) != 0) &
			(forwardCompatible || (glCallList = GLContext.getFunctionAddress("glCallList")) != 0) &
			(glBlendFunc = GLContext.getFunctionAddress("glBlendFunc")) != 0 &
			(forwardCompatible || (glBitmap = GLContext.getFunctionAddress("glBitmap")) != 0) &
			(glBindTexture = GLContext.getFunctionAddress("glBindTexture")) != 0 &
			(forwardCompatible || (glPrioritizeTextures = GLContext.getFunctionAddress("glPrioritizeTextures")) != 0) &
			(forwardCompatible || (glAreTexturesResident = GLContext.getFunctionAddress("glAreTexturesResident")) != 0) &
			(forwardCompatible || (glBegin = GLContext.getFunctionAddress("glBegin")) != 0) &
			(forwardCompatible || (glEnd = GLContext.getFunctionAddress("glEnd")) != 0) &
			(glArrayElement = GLContext.getFunctionAddress("glArrayElement")) != 0 &
			(glClearDepth = GLContext.getFunctionAddress("glClearDepth")) != 0 &
			(forwardCompatible || (glDeleteLists = GLContext.getFunctionAddress("glDeleteLists")) != 0) &
			(glDeleteTextures = GLContext.getFunctionAddress("glDeleteTextures")) != 0 &
			(glCullFace = GLContext.getFunctionAddress("glCullFace")) != 0 &
			(glCopyTexSubImage2D = GLContext.getFunctionAddress("glCopyTexSubImage2D")) != 0 &
			(glCopyTexSubImage1D = GLContext.getFunctionAddress("glCopyTexSubImage1D")) != 0 &
			(glCopyTexImage2D = GLContext.getFunctionAddress("glCopyTexImage2D")) != 0 &
			(glCopyTexImage1D = GLContext.getFunctionAddress("glCopyTexImage1D")) != 0 &
			(glCopyPixels = GLContext.getFunctionAddress("glCopyPixels")) != 0 &
			(forwardCompatible || (glColorPointer = GLContext.getFunctionAddress("glColorPointer")) != 0) &
			(forwardCompatible || (glColorMaterial = GLContext.getFunctionAddress("glColorMaterial")) != 0) &
			(glColorMask = GLContext.getFunctionAddress("glColorMask")) != 0 &
			(forwardCompatible || (glColor3b = GLContext.getFunctionAddress("glColor3b")) != 0) &
			(forwardCompatible || (glColor3f = GLContext.getFunctionAddress("glColor3f")) != 0) &
			(forwardCompatible || (glColor3d = GLContext.getFunctionAddress("glColor3d")) != 0) &
			(forwardCompatible || (glColor3ub = GLContext.getFunctionAddress("glColor3ub")) != 0) &
			(forwardCompatible || (glColor4b = GLContext.getFunctionAddress("glColor4b")) != 0) &
			(forwardCompatible || (glColor4f = GLContext.getFunctionAddress("glColor4f")) != 0) &
			(forwardCompatible || (glColor4d = GLContext.getFunctionAddress("glColor4d")) != 0) &
			(forwardCompatible || (glColor4ub = GLContext.getFunctionAddress("glColor4ub")) != 0) &
			(glClipPlane = GLContext.getFunctionAddress("glClipPlane")) != 0 &
			(glClearStencil = GLContext.getFunctionAddress("glClearStencil")) != 0 &
			(forwardCompatible || (glEvalPoint1 = GLContext.getFunctionAddress("glEvalPoint1")) != 0) &
			(forwardCompatible || (glEvalPoint2 = GLContext.getFunctionAddress("glEvalPoint2")) != 0) &
			(forwardCompatible || (glEvalMesh1 = GLContext.getFunctionAddress("glEvalMesh1")) != 0) &
			(forwardCompatible || (glEvalMesh2 = GLContext.getFunctionAddress("glEvalMesh2")) != 0) &
			(forwardCompatible || (glEvalCoord1f = GLContext.getFunctionAddress("glEvalCoord1f")) != 0) &
			(forwardCompatible || (glEvalCoord1d = GLContext.getFunctionAddress("glEvalCoord1d")) != 0) &
			(forwardCompatible || (glEvalCoord2f = GLContext.getFunctionAddress("glEvalCoord2f")) != 0) &
			(forwardCompatible || (glEvalCoord2d = GLContext.getFunctionAddress("glEvalCoord2d")) != 0) &
			(forwardCompatible || (glEnableClientState = GLContext.getFunctionAddress("glEnableClientState")) != 0) &
			(forwardCompatible || (glDisableClientState = GLContext.getFunctionAddress("glDisableClientState")) != 0) &
			(glEnable = GLContext.getFunctionAddress("glEnable")) != 0 &
			(glDisable = GLContext.getFunctionAddress("glDisable")) != 0 &
			(forwardCompatible || (glEdgeFlagPointer = GLContext.getFunctionAddress("glEdgeFlagPointer")) != 0) &
			(forwardCompatible || (glEdgeFlag = GLContext.getFunctionAddress("glEdgeFlag")) != 0) &
			(forwardCompatible || (glDrawPixels = GLContext.getFunctionAddress("glDrawPixels")) != 0) &
			(glDrawElements = GLContext.getFunctionAddress("glDrawElements")) != 0 &
			(glDrawBuffer = GLContext.getFunctionAddress("glDrawBuffer")) != 0 &
			(glDrawArrays = GLContext.getFunctionAddress("glDrawArrays")) != 0 &
			(glDepthRange = GLContext.getFunctionAddress("glDepthRange")) != 0 &
			(glDepthMask = GLContext.getFunctionAddress("glDepthMask")) != 0 &
			(glDepthFunc = GLContext.getFunctionAddress("glDepthFunc")) != 0 &
			(forwardCompatible || (glFeedbackBuffer = GLContext.getFunctionAddress("glFeedbackBuffer")) != 0) &
			(forwardCompatible || (glGetPixelMapfv = GLContext.getFunctionAddress("glGetPixelMapfv")) != 0) &
			(forwardCompatible || (glGetPixelMapuiv = GLContext.getFunctionAddress("glGetPixelMapuiv")) != 0) &
			(forwardCompatible || (glGetPixelMapusv = GLContext.getFunctionAddress("glGetPixelMapusv")) != 0) &
			(forwardCompatible || (glGetMaterialfv = GLContext.getFunctionAddress("glGetMaterialfv")) != 0) &
			(forwardCompatible || (glGetMaterialiv = GLContext.getFunctionAddress("glGetMaterialiv")) != 0) &
			(forwardCompatible || (glGetMapfv = GLContext.getFunctionAddress("glGetMapfv")) != 0) &
			(forwardCompatible || (glGetMapdv = GLContext.getFunctionAddress("glGetMapdv")) != 0) &
			(forwardCompatible || (glGetMapiv = GLContext.getFunctionAddress("glGetMapiv")) != 0) &
			(forwardCompatible || (glGetLightfv = GLContext.getFunctionAddress("glGetLightfv")) != 0) &
			(forwardCompatible || (glGetLightiv = GLContext.getFunctionAddress("glGetLightiv")) != 0) &
			(glGetError = GLContext.getFunctionAddress("glGetError")) != 0 &
			(glGetClipPlane = GLContext.getFunctionAddress("glGetClipPlane")) != 0 &
			(glGetBooleanv = GLContext.getFunctionAddress("glGetBooleanv")) != 0 &
			(glGetDoublev = GLContext.getFunctionAddress("glGetDoublev")) != 0 &
			(glGetFloatv = GLContext.getFunctionAddress("glGetFloatv")) != 0 &
			(glGetIntegerv = GLContext.getFunctionAddress("glGetIntegerv")) != 0 &
			(glGenTextures = GLContext.getFunctionAddress("glGenTextures")) != 0 &
			(forwardCompatible || (glGenLists = GLContext.getFunctionAddress("glGenLists")) != 0) &
			(forwardCompatible || (glFrustum = GLContext.getFunctionAddress("glFrustum")) != 0) &
			(glFrontFace = GLContext.getFunctionAddress("glFrontFace")) != 0 &
			(forwardCompatible || (glFogf = GLContext.getFunctionAddress("glFogf")) != 0) &
			(forwardCompatible || (glFogi = GLContext.getFunctionAddress("glFogi")) != 0) &
			(forwardCompatible || (glFogfv = GLContext.getFunctionAddress("glFogfv")) != 0) &
			(forwardCompatible || (glFogiv = GLContext.getFunctionAddress("glFogiv")) != 0) &
			(glFlush = GLContext.getFunctionAddress("glFlush")) != 0 &
			(glFinish = GLContext.getFunctionAddress("glFinish")) != 0 &
			(glGetPointerv = GLContext.getFunctionAddress("glGetPointerv")) != 0 &
			(glIsEnabled = GLContext.getFunctionAddress("glIsEnabled")) != 0 &
			(glInterleavedArrays = GLContext.getFunctionAddress("glInterleavedArrays")) != 0 &
			(forwardCompatible || (glInitNames = GLContext.getFunctionAddress("glInitNames")) != 0) &
			(glHint = GLContext.getFunctionAddress("glHint")) != 0 &
			(glGetTexParameterfv = GLContext.getFunctionAddress("glGetTexParameterfv")) != 0 &
			(glGetTexParameteriv = GLContext.getFunctionAddress("glGetTexParameteriv")) != 0 &
			(glGetTexLevelParameterfv = GLContext.getFunctionAddress("glGetTexLevelParameterfv")) != 0 &
			(glGetTexLevelParameteriv = GLContext.getFunctionAddress("glGetTexLevelParameteriv")) != 0 &
			(glGetTexImage = GLContext.getFunctionAddress("glGetTexImage")) != 0 &
			(forwardCompatible || (glGetTexGeniv = GLContext.getFunctionAddress("glGetTexGeniv")) != 0) &
			(forwardCompatible || (glGetTexGenfv = GLContext.getFunctionAddress("glGetTexGenfv")) != 0) &
			(forwardCompatible || (glGetTexGendv = GLContext.getFunctionAddress("glGetTexGendv")) != 0) &
			(glGetTexEnviv = GLContext.getFunctionAddress("glGetTexEnviv")) != 0 &
			(glGetTexEnvfv = GLContext.getFunctionAddress("glGetTexEnvfv")) != 0 &
			(glGetString = GLContext.getFunctionAddress("glGetString")) != 0 &
			(forwardCompatible || (glGetPolygonStipple = GLContext.getFunctionAddress("glGetPolygonStipple")) != 0) &
			(forwardCompatible || (glIsList = GLContext.getFunctionAddress("glIsList")) != 0) &
			(forwardCompatible || (glMaterialf = GLContext.getFunctionAddress("glMaterialf")) != 0) &
			(forwardCompatible || (glMateriali = GLContext.getFunctionAddress("glMateriali")) != 0) &
			(forwardCompatible || (glMaterialfv = GLContext.getFunctionAddress("glMaterialfv")) != 0) &
			(forwardCompatible || (glMaterialiv = GLContext.getFunctionAddress("glMaterialiv")) != 0) &
			(forwardCompatible || (glMapGrid1f = GLContext.getFunctionAddress("glMapGrid1f")) != 0) &
			(forwardCompatible || (glMapGrid1d = GLContext.getFunctionAddress("glMapGrid1d")) != 0) &
			(forwardCompatible || (glMapGrid2f = GLContext.getFunctionAddress("glMapGrid2f")) != 0) &
			(forwardCompatible || (glMapGrid2d = GLContext.getFunctionAddress("glMapGrid2d")) != 0) &
			(forwardCompatible || (glMap2f = GLContext.getFunctionAddress("glMap2f")) != 0) &
			(forwardCompatible || (glMap2d = GLContext.getFunctionAddress("glMap2d")) != 0) &
			(forwardCompatible || (glMap1f = GLContext.getFunctionAddress("glMap1f")) != 0) &
			(forwardCompatible || (glMap1d = GLContext.getFunctionAddress("glMap1d")) != 0) &
			(glLogicOp = GLContext.getFunctionAddress("glLogicOp")) != 0 &
			(forwardCompatible || (glLoadName = GLContext.getFunctionAddress("glLoadName")) != 0) &
			(forwardCompatible || (glLoadMatrixf = GLContext.getFunctionAddress("glLoadMatrixf")) != 0) &
			(forwardCompatible || (glLoadMatrixd = GLContext.getFunctionAddress("glLoadMatrixd")) != 0) &
			(forwardCompatible || (glLoadIdentity = GLContext.getFunctionAddress("glLoadIdentity")) != 0) &
			(forwardCompatible || (glListBase = GLContext.getFunctionAddress("glListBase")) != 0) &
			(glLineWidth = GLContext.getFunctionAddress("glLineWidth")) != 0 &
			(forwardCompatible || (glLineStipple = GLContext.getFunctionAddress("glLineStipple")) != 0) &
			(forwardCompatible || (glLightModelf = GLContext.getFunctionAddress("glLightModelf")) != 0) &
			(forwardCompatible || (glLightModeli = GLContext.getFunctionAddress("glLightModeli")) != 0) &
			(forwardCompatible || (glLightModelfv = GLContext.getFunctionAddress("glLightModelfv")) != 0) &
			(forwardCompatible || (glLightModeliv = GLContext.getFunctionAddress("glLightModeliv")) != 0) &
			(forwardCompatible || (glLightf = GLContext.getFunctionAddress("glLightf")) != 0) &
			(forwardCompatible || (glLighti = GLContext.getFunctionAddress("glLighti")) != 0) &
			(forwardCompatible || (glLightfv = GLContext.getFunctionAddress("glLightfv")) != 0) &
			(forwardCompatible || (glLightiv = GLContext.getFunctionAddress("glLightiv")) != 0) &
			(glIsTexture = GLContext.getFunctionAddress("glIsTexture")) != 0 &
			(forwardCompatible || (glMatrixMode = GLContext.getFunctionAddress("glMatrixMode")) != 0) &
			(forwardCompatible || (glPolygonStipple = GLContext.getFunctionAddress("glPolygonStipple")) != 0) &
			(glPolygonOffset = GLContext.getFunctionAddress("glPolygonOffset")) != 0 &
			(glPolygonMode = GLContext.getFunctionAddress("glPolygonMode")) != 0 &
			(glPointSize = GLContext.getFunctionAddress("glPointSize")) != 0 &
			(forwardCompatible || (glPixelZoom = GLContext.getFunctionAddress("glPixelZoom")) != 0) &
			(forwardCompatible || (glPixelTransferf = GLContext.getFunctionAddress("glPixelTransferf")) != 0) &
			(forwardCompatible || (glPixelTransferi = GLContext.getFunctionAddress("glPixelTransferi")) != 0) &
			(glPixelStoref = GLContext.getFunctionAddress("glPixelStoref")) != 0 &
			(glPixelStorei = GLContext.getFunctionAddress("glPixelStorei")) != 0 &
			(forwardCompatible || (glPixelMapfv = GLContext.getFunctionAddress("glPixelMapfv")) != 0) &
			(forwardCompatible || (glPixelMapuiv = GLContext.getFunctionAddress("glPixelMapuiv")) != 0) &
			(forwardCompatible || (glPixelMapusv = GLContext.getFunctionAddress("glPixelMapusv")) != 0) &
			(forwardCompatible || (glPassThrough = GLContext.getFunctionAddress("glPassThrough")) != 0) &
			(forwardCompatible || (glOrtho = GLContext.getFunctionAddress("glOrtho")) != 0) &
			(forwardCompatible || (glNormalPointer = GLContext.getFunctionAddress("glNormalPointer")) != 0) &
			(forwardCompatible || (glNormal3b = GLContext.getFunctionAddress("glNormal3b")) != 0) &
			(forwardCompatible || (glNormal3f = GLContext.getFunctionAddress("glNormal3f")) != 0) &
			(forwardCompatible || (glNormal3d = GLContext.getFunctionAddress("glNormal3d")) != 0) &
			(forwardCompatible || (glNormal3i = GLContext.getFunctionAddress("glNormal3i")) != 0) &
			(forwardCompatible || (glNewList = GLContext.getFunctionAddress("glNewList")) != 0) &
			(forwardCompatible || (glEndList = GLContext.getFunctionAddress("glEndList")) != 0) &
			(forwardCompatible || (glMultMatrixf = GLContext.getFunctionAddress("glMultMatrixf")) != 0) &
			(forwardCompatible || (glMultMatrixd = GLContext.getFunctionAddress("glMultMatrixd")) != 0) &
			(glShadeModel = GLContext.getFunctionAddress("glShadeModel")) != 0 &
			(forwardCompatible || (glSelectBuffer = GLContext.getFunctionAddress("glSelectBuffer")) != 0) &
			(glScissor = GLContext.getFunctionAddress("glScissor")) != 0 &
			(forwardCompatible || (glScalef = GLContext.getFunctionAddress("glScalef")) != 0) &
			(forwardCompatible || (glScaled = GLContext.getFunctionAddress("glScaled")) != 0) &
			(forwardCompatible || (glRotatef = GLContext.getFunctionAddress("glRotatef")) != 0) &
			(forwardCompatible || (glRotated = GLContext.getFunctionAddress("glRotated")) != 0) &
			(forwardCompatible || (glRenderMode = GLContext.getFunctionAddress("glRenderMode")) != 0) &
			(forwardCompatible || (glRectf = GLContext.getFunctionAddress("glRectf")) != 0) &
			(forwardCompatible || (glRectd = GLContext.getFunctionAddress("glRectd")) != 0) &
			(forwardCompatible || (glRecti = GLContext.getFunctionAddress("glRecti")) != 0) &
			(glReadPixels = GLContext.getFunctionAddress("glReadPixels")) != 0 &
			(glReadBuffer = GLContext.getFunctionAddress("glReadBuffer")) != 0 &
			(forwardCompatible || (glRasterPos2f = GLContext.getFunctionAddress("glRasterPos2f")) != 0) &
			(forwardCompatible || (glRasterPos2d = GLContext.getFunctionAddress("glRasterPos2d")) != 0) &
			(forwardCompatible || (glRasterPos2i = GLContext.getFunctionAddress("glRasterPos2i")) != 0) &
			(forwardCompatible || (glRasterPos3f = GLContext.getFunctionAddress("glRasterPos3f")) != 0) &
			(forwardCompatible || (glRasterPos3d = GLContext.getFunctionAddress("glRasterPos3d")) != 0) &
			(forwardCompatible || (glRasterPos3i = GLContext.getFunctionAddress("glRasterPos3i")) != 0) &
			(forwardCompatible || (glRasterPos4f = GLContext.getFunctionAddress("glRasterPos4f")) != 0) &
			(forwardCompatible || (glRasterPos4d = GLContext.getFunctionAddress("glRasterPos4d")) != 0) &
			(forwardCompatible || (glRasterPos4i = GLContext.getFunctionAddress("glRasterPos4i")) != 0) &
			(forwardCompatible || (glPushName = GLContext.getFunctionAddress("glPushName")) != 0) &
			(forwardCompatible || (glPopName = GLContext.getFunctionAddress("glPopName")) != 0) &
			(forwardCompatible || (glPushMatrix = GLContext.getFunctionAddress("glPushMatrix")) != 0) &
			(forwardCompatible || (glPopMatrix = GLContext.getFunctionAddress("glPopMatrix")) != 0) &
			(forwardCompatible || (glPushClientAttrib = GLContext.getFunctionAddress("glPushClientAttrib")) != 0) &
			(forwardCompatible || (glPopClientAttrib = GLContext.getFunctionAddress("glPopClientAttrib")) != 0) &
			(forwardCompatible || (glPushAttrib = GLContext.getFunctionAddress("glPushAttrib")) != 0) &
			(forwardCompatible || (glPopAttrib = GLContext.getFunctionAddress("glPopAttrib")) != 0) &
			(glStencilFunc = GLContext.getFunctionAddress("glStencilFunc")) != 0 &
			(forwardCompatible || (glVertexPointer = GLContext.getFunctionAddress("glVertexPointer")) != 0) &
			(forwardCompatible || (glVertex2f = GLContext.getFunctionAddress("glVertex2f")) != 0) &
			(forwardCompatible || (glVertex2d = GLContext.getFunctionAddress("glVertex2d")) != 0) &
			(forwardCompatible || (glVertex2i = GLContext.getFunctionAddress("glVertex2i")) != 0) &
			(forwardCompatible || (glVertex3f = GLContext.getFunctionAddress("glVertex3f")) != 0) &
			(forwardCompatible || (glVertex3d = GLContext.getFunctionAddress("glVertex3d")) != 0) &
			(forwardCompatible || (glVertex3i = GLContext.getFunctionAddress("glVertex3i")) != 0) &
			(forwardCompatible || (glVertex4f = GLContext.getFunctionAddress("glVertex4f")) != 0) &
			(forwardCompatible || (glVertex4d = GLContext.getFunctionAddress("glVertex4d")) != 0) &
			(forwardCompatible || (glVertex4i = GLContext.getFunctionAddress("glVertex4i")) != 0) &
			(forwardCompatible || (glTranslatef = GLContext.getFunctionAddress("glTranslatef")) != 0) &
			(forwardCompatible || (glTranslated = GLContext.getFunctionAddress("glTranslated")) != 0) &
			(glTexImage1D = GLContext.getFunctionAddress("glTexImage1D")) != 0 &
			(glTexImage2D = GLContext.getFunctionAddress("glTexImage2D")) != 0 &
			(glTexSubImage1D = GLContext.getFunctionAddress("glTexSubImage1D")) != 0 &
			(glTexSubImage2D = GLContext.getFunctionAddress("glTexSubImage2D")) != 0 &
			(glTexParameterf = GLContext.getFunctionAddress("glTexParameterf")) != 0 &
			(glTexParameteri = GLContext.getFunctionAddress("glTexParameteri")) != 0 &
			(glTexParameterfv = GLContext.getFunctionAddress("glTexParameterfv")) != 0 &
			(glTexParameteriv = GLContext.getFunctionAddress("glTexParameteriv")) != 0 &
			(forwardCompatible || (glTexGenf = GLContext.getFunctionAddress("glTexGenf")) != 0) &
			(forwardCompatible || (glTexGend = GLContext.getFunctionAddress("glTexGend")) != 0) &
			(forwardCompatible || (glTexGenfv = GLContext.getFunctionAddress("glTexGenfv")) != 0) &
			(forwardCompatible || (glTexGendv = GLContext.getFunctionAddress("glTexGendv")) != 0) &
			(forwardCompatible || (glTexGeni = GLContext.getFunctionAddress("glTexGeni")) != 0) &
			(forwardCompatible || (glTexGeniv = GLContext.getFunctionAddress("glTexGeniv")) != 0) &
			(glTexEnvf = GLContext.getFunctionAddress("glTexEnvf")) != 0 &
			(glTexEnvi = GLContext.getFunctionAddress("glTexEnvi")) != 0 &
			(glTexEnvfv = GLContext.getFunctionAddress("glTexEnvfv")) != 0 &
			(glTexEnviv = GLContext.getFunctionAddress("glTexEnviv")) != 0 &
			(forwardCompatible || (glTexCoordPointer = GLContext.getFunctionAddress("glTexCoordPointer")) != 0) &
			(forwardCompatible || (glTexCoord1f = GLContext.getFunctionAddress("glTexCoord1f")) != 0) &
			(forwardCompatible || (glTexCoord1d = GLContext.getFunctionAddress("glTexCoord1d")) != 0) &
			(forwardCompatible || (glTexCoord2f = GLContext.getFunctionAddress("glTexCoord2f")) != 0) &
			(forwardCompatible || (glTexCoord2d = GLContext.getFunctionAddress("glTexCoord2d")) != 0) &
			(forwardCompatible || (glTexCoord3f = GLContext.getFunctionAddress("glTexCoord3f")) != 0) &
			(forwardCompatible || (glTexCoord3d = GLContext.getFunctionAddress("glTexCoord3d")) != 0) &
			(forwardCompatible || (glTexCoord4f = GLContext.getFunctionAddress("glTexCoord4f")) != 0) &
			(forwardCompatible || (glTexCoord4d = GLContext.getFunctionAddress("glTexCoord4d")) != 0) &
			(glStencilOp = GLContext.getFunctionAddress("glStencilOp")) != 0 &
			(glStencilMask = GLContext.getFunctionAddress("glStencilMask")) != 0 &
			(glViewport = GLContext.getFunctionAddress("glViewport")) != 0;
	}

	private boolean GL12_initNativeFunctionAddresses() {
		return 
			(glDrawRangeElements = GLContext.getFunctionAddress("glDrawRangeElements")) != 0 &
			(glTexImage3D = GLContext.getFunctionAddress("glTexImage3D")) != 0 &
			(glTexSubImage3D = GLContext.getFunctionAddress("glTexSubImage3D")) != 0 &
			(glCopyTexSubImage3D = GLContext.getFunctionAddress("glCopyTexSubImage3D")) != 0;
	}

	private boolean GL13_initNativeFunctionAddresses(boolean forwardCompatible) {
		return 
			(glActiveTexture = GLContext.getFunctionAddress("glActiveTexture")) != 0 &
			(forwardCompatible || (glClientActiveTexture = GLContext.getFunctionAddress("glClientActiveTexture")) != 0) &
			(glCompressedTexImage1D = GLContext.getFunctionAddress("glCompressedTexImage1D")) != 0 &
			(glCompressedTexImage2D = GLContext.getFunctionAddress("glCompressedTexImage2D")) != 0 &
			(glCompressedTexImage3D = GLContext.getFunctionAddress("glCompressedTexImage3D")) != 0 &
			(glCompressedTexSubImage1D = GLContext.getFunctionAddress("glCompressedTexSubImage1D")) != 0 &
			(glCompressedTexSubImage2D = GLContext.getFunctionAddress("glCompressedTexSubImage2D")) != 0 &
			(glCompressedTexSubImage3D = GLContext.getFunctionAddress("glCompressedTexSubImage3D")) != 0 &
			(glGetCompressedTexImage = GLContext.getFunctionAddress("glGetCompressedTexImage")) != 0 &
			(forwardCompatible || (glMultiTexCoord1f = GLContext.getFunctionAddress("glMultiTexCoord1f")) != 0) &
			(forwardCompatible || (glMultiTexCoord1d = GLContext.getFunctionAddress("glMultiTexCoord1d")) != 0) &
			(forwardCompatible || (glMultiTexCoord2f = GLContext.getFunctionAddress("glMultiTexCoord2f")) != 0) &
			(forwardCompatible || (glMultiTexCoord2d = GLContext.getFunctionAddress("glMultiTexCoord2d")) != 0) &
			(forwardCompatible || (glMultiTexCoord3f = GLContext.getFunctionAddress("glMultiTexCoord3f")) != 0) &
			(forwardCompatible || (glMultiTexCoord3d = GLContext.getFunctionAddress("glMultiTexCoord3d")) != 0) &
			(forwardCompatible || (glMultiTexCoord4f = GLContext.getFunctionAddress("glMultiTexCoord4f")) != 0) &
			(forwardCompatible || (glMultiTexCoord4d = GLContext.getFunctionAddress("glMultiTexCoord4d")) != 0) &
			(forwardCompatible || (glLoadTransposeMatrixf = GLContext.getFunctionAddress("glLoadTransposeMatrixf")) != 0) &
			(forwardCompatible || (glLoadTransposeMatrixd = GLContext.getFunctionAddress("glLoadTransposeMatrixd")) != 0) &
			(forwardCompatible || (glMultTransposeMatrixf = GLContext.getFunctionAddress("glMultTransposeMatrixf")) != 0) &
			(forwardCompatible || (glMultTransposeMatrixd = GLContext.getFunctionAddress("glMultTransposeMatrixd")) != 0) &
			(glSampleCoverage = GLContext.getFunctionAddress("glSampleCoverage")) != 0;
	}

	private boolean GL14_initNativeFunctionAddresses(boolean forwardCompatible) {
		return 
			(glBlendEquation = GLContext.getFunctionAddress("glBlendEquation")) != 0 &
			(glBlendColor = GLContext.getFunctionAddress("glBlendColor")) != 0 &
			(forwardCompatible || (glFogCoordf = GLContext.getFunctionAddress("glFogCoordf")) != 0) &
			(forwardCompatible || (glFogCoordd = GLContext.getFunctionAddress("glFogCoordd")) != 0) &
			(forwardCompatible || (glFogCoordPointer = GLContext.getFunctionAddress("glFogCoordPointer")) != 0) &
			(glMultiDrawArrays = GLContext.getFunctionAddress("glMultiDrawArrays")) != 0 &
			(glPointParameteri = GLContext.getFunctionAddress("glPointParameteri")) != 0 &
			(glPointParameterf = GLContext.getFunctionAddress("glPointParameterf")) != 0 &
			(glPointParameteriv = GLContext.getFunctionAddress("glPointParameteriv")) != 0 &
			(glPointParameterfv = GLContext.getFunctionAddress("glPointParameterfv")) != 0 &
			(forwardCompatible || (glSecondaryColor3b = GLContext.getFunctionAddress("glSecondaryColor3b")) != 0) &
			(forwardCompatible || (glSecondaryColor3f = GLContext.getFunctionAddress("glSecondaryColor3f")) != 0) &
			(forwardCompatible || (glSecondaryColor3d = GLContext.getFunctionAddress("glSecondaryColor3d")) != 0) &
			(forwardCompatible || (glSecondaryColor3ub = GLContext.getFunctionAddress("glSecondaryColor3ub")) != 0) &
			(forwardCompatible || (glSecondaryColorPointer = GLContext.getFunctionAddress("glSecondaryColorPointer")) != 0) &
			(glBlendFuncSeparate = GLContext.getFunctionAddress("glBlendFuncSeparate")) != 0 &
			(forwardCompatible || (glWindowPos2f = GLContext.getFunctionAddress("glWindowPos2f")) != 0) &
			(forwardCompatible || (glWindowPos2d = GLContext.getFunctionAddress("glWindowPos2d")) != 0) &
			(forwardCompatible || (glWindowPos2i = GLContext.getFunctionAddress("glWindowPos2i")) != 0) &
			(forwardCompatible || (glWindowPos3f = GLContext.getFunctionAddress("glWindowPos3f")) != 0) &
			(forwardCompatible || (glWindowPos3d = GLContext.getFunctionAddress("glWindowPos3d")) != 0) &
			(forwardCompatible || (glWindowPos3i = GLContext.getFunctionAddress("glWindowPos3i")) != 0);
	}

	private boolean GL15_initNativeFunctionAddresses() {
		return 
			(glBindBuffer = GLContext.getFunctionAddress("glBindBuffer")) != 0 &
			(glDeleteBuffers = GLContext.getFunctionAddress("glDeleteBuffers")) != 0 &
			(glGenBuffers = GLContext.getFunctionAddress("glGenBuffers")) != 0 &
			(glIsBuffer = GLContext.getFunctionAddress("glIsBuffer")) != 0 &
			(glBufferData = GLContext.getFunctionAddress("glBufferData")) != 0 &
			(glBufferSubData = GLContext.getFunctionAddress("glBufferSubData")) != 0 &
			(glGetBufferSubData = GLContext.getFunctionAddress("glGetBufferSubData")) != 0 &
			(glMapBuffer = GLContext.getFunctionAddress("glMapBuffer")) != 0 &
			(glUnmapBuffer = GLContext.getFunctionAddress("glUnmapBuffer")) != 0 &
			(glGetBufferParameteriv = GLContext.getFunctionAddress("glGetBufferParameteriv")) != 0 &
			(glGetBufferPointerv = GLContext.getFunctionAddress("glGetBufferPointerv")) != 0 &
			(glGenQueries = GLContext.getFunctionAddress("glGenQueries")) != 0 &
			(glDeleteQueries = GLContext.getFunctionAddress("glDeleteQueries")) != 0 &
			(glIsQuery = GLContext.getFunctionAddress("glIsQuery")) != 0 &
			(glBeginQuery = GLContext.getFunctionAddress("glBeginQuery")) != 0 &
			(glEndQuery = GLContext.getFunctionAddress("glEndQuery")) != 0 &
			(glGetQueryiv = GLContext.getFunctionAddress("glGetQueryiv")) != 0 &
			(glGetQueryObjectiv = GLContext.getFunctionAddress("glGetQueryObjectiv")) != 0 &
			(glGetQueryObjectuiv = GLContext.getFunctionAddress("glGetQueryObjectuiv")) != 0;
	}

	private boolean GL20_initNativeFunctionAddresses() {
		return 
			(glShaderSource = GLContext.getFunctionAddress("glShaderSource")) != 0 &
			(glCreateShader = GLContext.getFunctionAddress("glCreateShader")) != 0 &
			(glIsShader = GLContext.getFunctionAddress("glIsShader")) != 0 &
			(glCompileShader = GLContext.getFunctionAddress("glCompileShader")) != 0 &
			(glDeleteShader = GLContext.getFunctionAddress("glDeleteShader")) != 0 &
			(glCreateProgram = GLContext.getFunctionAddress("glCreateProgram")) != 0 &
			(glIsProgram = GLContext.getFunctionAddress("glIsProgram")) != 0 &
			(glAttachShader = GLContext.getFunctionAddress("glAttachShader")) != 0 &
			(glDetachShader = GLContext.getFunctionAddress("glDetachShader")) != 0 &
			(glLinkProgram = GLContext.getFunctionAddress("glLinkProgram")) != 0 &
			(glUseProgram = GLContext.getFunctionAddress("glUseProgram")) != 0 &
			(glValidateProgram = GLContext.getFunctionAddress("glValidateProgram")) != 0 &
			(glDeleteProgram = GLContext.getFunctionAddress("glDeleteProgram")) != 0 &
			(glUniform1f = GLContext.getFunctionAddress("glUniform1f")) != 0 &
			(glUniform2f = GLContext.getFunctionAddress("glUniform2f")) != 0 &
			(glUniform3f = GLContext.getFunctionAddress("glUniform3f")) != 0 &
			(glUniform4f = GLContext.getFunctionAddress("glUniform4f")) != 0 &
			(glUniform1i = GLContext.getFunctionAddress("glUniform1i")) != 0 &
			(glUniform2i = GLContext.getFunctionAddress("glUniform2i")) != 0 &
			(glUniform3i = GLContext.getFunctionAddress("glUniform3i")) != 0 &
			(glUniform4i = GLContext.getFunctionAddress("glUniform4i")) != 0 &
			(glUniform1fv = GLContext.getFunctionAddress("glUniform1fv")) != 0 &
			(glUniform2fv = GLContext.getFunctionAddress("glUniform2fv")) != 0 &
			(glUniform3fv = GLContext.getFunctionAddress("glUniform3fv")) != 0 &
			(glUniform4fv = GLContext.getFunctionAddress("glUniform4fv")) != 0 &
			(glUniform1iv = GLContext.getFunctionAddress("glUniform1iv")) != 0 &
			(glUniform2iv = GLContext.getFunctionAddress("glUniform2iv")) != 0 &
			(glUniform3iv = GLContext.getFunctionAddress("glUniform3iv")) != 0 &
			(glUniform4iv = GLContext.getFunctionAddress("glUniform4iv")) != 0 &
			(glUniformMatrix2fv = GLContext.getFunctionAddress("glUniformMatrix2fv")) != 0 &
			(glUniformMatrix3fv = GLContext.getFunctionAddress("glUniformMatrix3fv")) != 0 &
			(glUniformMatrix4fv = GLContext.getFunctionAddress("glUniformMatrix4fv")) != 0 &
			(glGetShaderiv = GLContext.getFunctionAddress("glGetShaderiv")) != 0 &
			(glGetProgramiv = GLContext.getFunctionAddress("glGetProgramiv")) != 0 &
			(glGetShaderInfoLog = GLContext.getFunctionAddress("glGetShaderInfoLog")) != 0 &
			(glGetProgramInfoLog = GLContext.getFunctionAddress("glGetProgramInfoLog")) != 0 &
			(glGetAttachedShaders = GLContext.getFunctionAddress("glGetAttachedShaders")) != 0 &
			(glGetUniformLocation = GLContext.getFunctionAddress("glGetUniformLocation")) != 0 &
			(glGetActiveUniform = GLContext.getFunctionAddress("glGetActiveUniform")) != 0 &
			(glGetUniformfv = GLContext.getFunctionAddress("glGetUniformfv")) != 0 &
			(glGetUniformiv = GLContext.getFunctionAddress("glGetUniformiv")) != 0 &
			(glGetShaderSource = GLContext.getFunctionAddress("glGetShaderSource")) != 0 &
			(glVertexAttrib1s = GLContext.getFunctionAddress("glVertexAttrib1s")) != 0 &
			(glVertexAttrib1f = GLContext.getFunctionAddress("glVertexAttrib1f")) != 0 &
			(glVertexAttrib1d = GLContext.getFunctionAddress("glVertexAttrib1d")) != 0 &
			(glVertexAttrib2s = GLContext.getFunctionAddress("glVertexAttrib2s")) != 0 &
			(glVertexAttrib2f = GLContext.getFunctionAddress("glVertexAttrib2f")) != 0 &
			(glVertexAttrib2d = GLContext.getFunctionAddress("glVertexAttrib2d")) != 0 &
			(glVertexAttrib3s = GLContext.getFunctionAddress("glVertexAttrib3s")) != 0 &
			(glVertexAttrib3f = GLContext.getFunctionAddress("glVertexAttrib3f")) != 0 &
			(glVertexAttrib3d = GLContext.getFunctionAddress("glVertexAttrib3d")) != 0 &
			(glVertexAttrib4s = GLContext.getFunctionAddress("glVertexAttrib4s")) != 0 &
			(glVertexAttrib4f = GLContext.getFunctionAddress("glVertexAttrib4f")) != 0 &
			(glVertexAttrib4d = GLContext.getFunctionAddress("glVertexAttrib4d")) != 0 &
			(glVertexAttrib4Nub = GLContext.getFunctionAddress("glVertexAttrib4Nub")) != 0 &
			(glVertexAttribPointer = GLContext.getFunctionAddress("glVertexAttribPointer")) != 0 &
			(glEnableVertexAttribArray = GLContext.getFunctionAddress("glEnableVertexAttribArray")) != 0 &
			(glDisableVertexAttribArray = GLContext.getFunctionAddress("glDisableVertexAttribArray")) != 0 &
			(glGetVertexAttribfv = GLContext.getFunctionAddress("glGetVertexAttribfv")) != 0 &
			(glGetVertexAttribdv = GLContext.getFunctionAddress("glGetVertexAttribdv")) != 0 &
			(glGetVertexAttribiv = GLContext.getFunctionAddress("glGetVertexAttribiv")) != 0 &
			(glGetVertexAttribPointerv = GLContext.getFunctionAddress("glGetVertexAttribPointerv")) != 0 &
			(glBindAttribLocation = GLContext.getFunctionAddress("glBindAttribLocation")) != 0 &
			(glGetActiveAttrib = GLContext.getFunctionAddress("glGetActiveAttrib")) != 0 &
			(glGetAttribLocation = GLContext.getFunctionAddress("glGetAttribLocation")) != 0 &
			(glDrawBuffers = GLContext.getFunctionAddress("glDrawBuffers")) != 0 &
			(glStencilOpSeparate = GLContext.getFunctionAddress("glStencilOpSeparate")) != 0 &
			(glStencilFuncSeparate = GLContext.getFunctionAddress("glStencilFuncSeparate")) != 0 &
			(glStencilMaskSeparate = GLContext.getFunctionAddress("glStencilMaskSeparate")) != 0 &
			(glBlendEquationSeparate = GLContext.getFunctionAddress("glBlendEquationSeparate")) != 0;
	}

	private boolean GL21_initNativeFunctionAddresses() {
		return 
			(glUniformMatrix2x3fv = GLContext.getFunctionAddress("glUniformMatrix2x3fv")) != 0 &
			(glUniformMatrix3x2fv = GLContext.getFunctionAddress("glUniformMatrix3x2fv")) != 0 &
			(glUniformMatrix2x4fv = GLContext.getFunctionAddress("glUniformMatrix2x4fv")) != 0 &
			(glUniformMatrix4x2fv = GLContext.getFunctionAddress("glUniformMatrix4x2fv")) != 0 &
			(glUniformMatrix3x4fv = GLContext.getFunctionAddress("glUniformMatrix3x4fv")) != 0 &
			(glUniformMatrix4x3fv = GLContext.getFunctionAddress("glUniformMatrix4x3fv")) != 0;
	}

	private boolean GL30_initNativeFunctionAddresses() {
		return 
			(glGetStringi = GLContext.getFunctionAddress("glGetStringi")) != 0 &
			(glClearBufferfv = GLContext.getFunctionAddress("glClearBufferfv")) != 0 &
			(glClearBufferiv = GLContext.getFunctionAddress("glClearBufferiv")) != 0 &
			(glClearBufferuiv = GLContext.getFunctionAddress("glClearBufferuiv")) != 0 &
			(glClearBufferfi = GLContext.getFunctionAddress("glClearBufferfi")) != 0 &
			(glVertexAttribI1i = GLContext.getFunctionAddress("glVertexAttribI1i")) != 0 &
			(glVertexAttribI2i = GLContext.getFunctionAddress("glVertexAttribI2i")) != 0 &
			(glVertexAttribI3i = GLContext.getFunctionAddress("glVertexAttribI3i")) != 0 &
			(glVertexAttribI4i = GLContext.getFunctionAddress("glVertexAttribI4i")) != 0 &
			(glVertexAttribI1ui = GLContext.getFunctionAddress("glVertexAttribI1ui")) != 0 &
			(glVertexAttribI2ui = GLContext.getFunctionAddress("glVertexAttribI2ui")) != 0 &
			(glVertexAttribI3ui = GLContext.getFunctionAddress("glVertexAttribI3ui")) != 0 &
			(glVertexAttribI4ui = GLContext.getFunctionAddress("glVertexAttribI4ui")) != 0 &
			(glVertexAttribI1iv = GLContext.getFunctionAddress("glVertexAttribI1iv")) != 0 &
			(glVertexAttribI2iv = GLContext.getFunctionAddress("glVertexAttribI2iv")) != 0 &
			(glVertexAttribI3iv = GLContext.getFunctionAddress("glVertexAttribI3iv")) != 0 &
			(glVertexAttribI4iv = GLContext.getFunctionAddress("glVertexAttribI4iv")) != 0 &
			(glVertexAttribI1uiv = GLContext.getFunctionAddress("glVertexAttribI1uiv")) != 0 &
			(glVertexAttribI2uiv = GLContext.getFunctionAddress("glVertexAttribI2uiv")) != 0 &
			(glVertexAttribI3uiv = GLContext.getFunctionAddress("glVertexAttribI3uiv")) != 0 &
			(glVertexAttribI4uiv = GLContext.getFunctionAddress("glVertexAttribI4uiv")) != 0 &
			(glVertexAttribI4bv = GLContext.getFunctionAddress("glVertexAttribI4bv")) != 0 &
			(glVertexAttribI4sv = GLContext.getFunctionAddress("glVertexAttribI4sv")) != 0 &
			(glVertexAttribI4ubv = GLContext.getFunctionAddress("glVertexAttribI4ubv")) != 0 &
			(glVertexAttribI4usv = GLContext.getFunctionAddress("glVertexAttribI4usv")) != 0 &
			(glVertexAttribIPointer = GLContext.getFunctionAddress("glVertexAttribIPointer")) != 0 &
			(glGetVertexAttribIiv = GLContext.getFunctionAddress("glGetVertexAttribIiv")) != 0 &
			(glGetVertexAttribIuiv = GLContext.getFunctionAddress("glGetVertexAttribIuiv")) != 0 &
			(glUniform1ui = GLContext.getFunctionAddress("glUniform1ui")) != 0 &
			(glUniform2ui = GLContext.getFunctionAddress("glUniform2ui")) != 0 &
			(glUniform3ui = GLContext.getFunctionAddress("glUniform3ui")) != 0 &
			(glUniform4ui = GLContext.getFunctionAddress("glUniform4ui")) != 0 &
			(glUniform1uiv = GLContext.getFunctionAddress("glUniform1uiv")) != 0 &
			(glUniform2uiv = GLContext.getFunctionAddress("glUniform2uiv")) != 0 &
			(glUniform3uiv = GLContext.getFunctionAddress("glUniform3uiv")) != 0 &
			(glUniform4uiv = GLContext.getFunctionAddress("glUniform4uiv")) != 0 &
			(glGetUniformuiv = GLContext.getFunctionAddress("glGetUniformuiv")) != 0 &
			(glBindFragDataLocation = GLContext.getFunctionAddress("glBindFragDataLocation")) != 0 &
			(glGetFragDataLocation = GLContext.getFunctionAddress("glGetFragDataLocation")) != 0 &
			(glBeginConditionalRender = GLContext.getFunctionAddress("glBeginConditionalRender")) != 0 &
			(glEndConditionalRender = GLContext.getFunctionAddress("glEndConditionalRender")) != 0 &
			(glMapBufferRange = GLContext.getFunctionAddress("glMapBufferRange")) != 0 &
			(glFlushMappedBufferRange = GLContext.getFunctionAddress("glFlushMappedBufferRange")) != 0 &
			(glClampColor = GLContext.getFunctionAddress("glClampColor")) != 0 &
			(glIsRenderbuffer = GLContext.getFunctionAddress("glIsRenderbuffer")) != 0 &
			(glBindRenderbuffer = GLContext.getFunctionAddress("glBindRenderbuffer")) != 0 &
			(glDeleteRenderbuffers = GLContext.getFunctionAddress("glDeleteRenderbuffers")) != 0 &
			(glGenRenderbuffers = GLContext.getFunctionAddress("glGenRenderbuffers")) != 0 &
			(glRenderbufferStorage = GLContext.getFunctionAddress("glRenderbufferStorage")) != 0 &
			(glGetRenderbufferParameteriv = GLContext.getFunctionAddress("glGetRenderbufferParameteriv")) != 0 &
			(glIsFramebuffer = GLContext.getFunctionAddress("glIsFramebuffer")) != 0 &
			(glBindFramebuffer = GLContext.getFunctionAddress("glBindFramebuffer")) != 0 &
			(glDeleteFramebuffers = GLContext.getFunctionAddress("glDeleteFramebuffers")) != 0 &
			(glGenFramebuffers = GLContext.getFunctionAddress("glGenFramebuffers")) != 0 &
			(glCheckFramebufferStatus = GLContext.getFunctionAddress("glCheckFramebufferStatus")) != 0 &
			(glFramebufferTexture1D = GLContext.getFunctionAddress("glFramebufferTexture1D")) != 0 &
			(glFramebufferTexture2D = GLContext.getFunctionAddress("glFramebufferTexture2D")) != 0 &
			(glFramebufferTexture3D = GLContext.getFunctionAddress("glFramebufferTexture3D")) != 0 &
			(glFramebufferRenderbuffer = GLContext.getFunctionAddress("glFramebufferRenderbuffer")) != 0 &
			(glGetFramebufferAttachmentParameteriv = GLContext.getFunctionAddress("glGetFramebufferAttachmentParameteriv")) != 0 &
			(glGenerateMipmap = GLContext.getFunctionAddress("glGenerateMipmap")) != 0 &
			(glRenderbufferStorageMultisample = GLContext.getFunctionAddress("glRenderbufferStorageMultisample")) != 0 &
			(glBlitFramebuffer = GLContext.getFunctionAddress("glBlitFramebuffer")) != 0 &
			(glTexParameterIiv = GLContext.getFunctionAddress("glTexParameterIiv")) != 0 &
			(glTexParameterIuiv = GLContext.getFunctionAddress("glTexParameterIuiv")) != 0 &
			(glGetTexParameterIiv = GLContext.getFunctionAddress("glGetTexParameterIiv")) != 0 &
			(glGetTexParameterIuiv = GLContext.getFunctionAddress("glGetTexParameterIuiv")) != 0 &
			(glFramebufferTextureLayer = GLContext.getFunctionAddress("glFramebufferTextureLayer")) != 0 &
			(glColorMaski = GLContext.getFunctionAddress("glColorMaski")) != 0 &
			(glGetBooleani_v = GLContext.getFunctionAddress("glGetBooleani_v")) != 0 &
			(glGetIntegeri_v = GLContext.getFunctionAddress("glGetIntegeri_v")) != 0 &
			(glEnablei = GLContext.getFunctionAddress("glEnablei")) != 0 &
			(glDisablei = GLContext.getFunctionAddress("glDisablei")) != 0 &
			(glIsEnabledi = GLContext.getFunctionAddress("glIsEnabledi")) != 0 &
			(glBindBufferRange = GLContext.getFunctionAddress("glBindBufferRange")) != 0 &
			(glBindBufferBase = GLContext.getFunctionAddress("glBindBufferBase")) != 0 &
			(glBeginTransformFeedback = GLContext.getFunctionAddress("glBeginTransformFeedback")) != 0 &
			(glEndTransformFeedback = GLContext.getFunctionAddress("glEndTransformFeedback")) != 0 &
			(glTransformFeedbackVaryings = GLContext.getFunctionAddress("glTransformFeedbackVaryings")) != 0 &
			(glGetTransformFeedbackVarying = GLContext.getFunctionAddress("glGetTransformFeedbackVarying")) != 0 &
			(glBindVertexArray = GLContext.getFunctionAddress("glBindVertexArray")) != 0 &
			(glDeleteVertexArrays = GLContext.getFunctionAddress("glDeleteVertexArrays")) != 0 &
			(glGenVertexArrays = GLContext.getFunctionAddress("glGenVertexArrays")) != 0 &
			(glIsVertexArray = GLContext.getFunctionAddress("glIsVertexArray")) != 0;
	}

	private boolean GL31_initNativeFunctionAddresses() {
		return 
			(glDrawArraysInstanced = GLContext.getFunctionAddress("glDrawArraysInstanced")) != 0 &
			(glDrawElementsInstanced = GLContext.getFunctionAddress("glDrawElementsInstanced")) != 0 &
			(glCopyBufferSubData = GLContext.getFunctionAddress("glCopyBufferSubData")) != 0 &
			(glPrimitiveRestartIndex = GLContext.getFunctionAddress("glPrimitiveRestartIndex")) != 0 &
			(glTexBuffer = GLContext.getFunctionAddress("glTexBuffer")) != 0 &
			(glGetUniformIndices = GLContext.getFunctionAddress("glGetUniformIndices")) != 0 &
			(glGetActiveUniformsiv = GLContext.getFunctionAddress("glGetActiveUniformsiv")) != 0 &
			(glGetActiveUniformName = GLContext.getFunctionAddress("glGetActiveUniformName")) != 0 &
			(glGetUniformBlockIndex = GLContext.getFunctionAddress("glGetUniformBlockIndex")) != 0 &
			(glGetActiveUniformBlockiv = GLContext.getFunctionAddress("glGetActiveUniformBlockiv")) != 0 &
			(glGetActiveUniformBlockName = GLContext.getFunctionAddress("glGetActiveUniformBlockName")) != 0 &
			(glUniformBlockBinding = GLContext.getFunctionAddress("glUniformBlockBinding")) != 0;
	}

	private boolean GL32_initNativeFunctionAddresses() {
		return 
			(glGetBufferParameteri64v = GLContext.getFunctionAddress("glGetBufferParameteri64v")) != 0 &
			(glDrawElementsBaseVertex = GLContext.getFunctionAddress("glDrawElementsBaseVertex")) != 0 &
			(glDrawRangeElementsBaseVertex = GLContext.getFunctionAddress("glDrawRangeElementsBaseVertex")) != 0 &
			(glDrawElementsInstancedBaseVertex = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertex")) != 0 &
			(glProvokingVertex = GLContext.getFunctionAddress("glProvokingVertex")) != 0 &
			(glTexImage2DMultisample = GLContext.getFunctionAddress("glTexImage2DMultisample")) != 0 &
			(glTexImage3DMultisample = GLContext.getFunctionAddress("glTexImage3DMultisample")) != 0 &
			(glGetMultisamplefv = GLContext.getFunctionAddress("glGetMultisamplefv")) != 0 &
			(glSampleMaski = GLContext.getFunctionAddress("glSampleMaski")) != 0 &
			(glFramebufferTexture = GLContext.getFunctionAddress("glFramebufferTexture")) != 0 &
			(glFenceSync = GLContext.getFunctionAddress("glFenceSync")) != 0 &
			(glIsSync = GLContext.getFunctionAddress("glIsSync")) != 0 &
			(glDeleteSync = GLContext.getFunctionAddress("glDeleteSync")) != 0 &
			(glClientWaitSync = GLContext.getFunctionAddress("glClientWaitSync")) != 0 &
			(glWaitSync = GLContext.getFunctionAddress("glWaitSync")) != 0 &
			(glGetInteger64v = GLContext.getFunctionAddress("glGetInteger64v")) != 0 &
			(glGetInteger64i_v = GLContext.getFunctionAddress("glGetInteger64i_v")) != 0 &
			(glGetSynciv = GLContext.getFunctionAddress("glGetSynciv")) != 0;
	}

	private boolean GL33_initNativeFunctionAddresses(boolean forwardCompatible) {
		return 
			(glBindFragDataLocationIndexed = GLContext.getFunctionAddress("glBindFragDataLocationIndexed")) != 0 &
			(glGetFragDataIndex = GLContext.getFunctionAddress("glGetFragDataIndex")) != 0 &
			(glGenSamplers = GLContext.getFunctionAddress("glGenSamplers")) != 0 &
			(glDeleteSamplers = GLContext.getFunctionAddress("glDeleteSamplers")) != 0 &
			(glIsSampler = GLContext.getFunctionAddress("glIsSampler")) != 0 &
			(glBindSampler = GLContext.getFunctionAddress("glBindSampler")) != 0 &
			(glSamplerParameteri = GLContext.getFunctionAddress("glSamplerParameteri")) != 0 &
			(glSamplerParameterf = GLContext.getFunctionAddress("glSamplerParameterf")) != 0 &
			(glSamplerParameteriv = GLContext.getFunctionAddress("glSamplerParameteriv")) != 0 &
			(glSamplerParameterfv = GLContext.getFunctionAddress("glSamplerParameterfv")) != 0 &
			(glSamplerParameterIiv = GLContext.getFunctionAddress("glSamplerParameterIiv")) != 0 &
			(glSamplerParameterIuiv = GLContext.getFunctionAddress("glSamplerParameterIuiv")) != 0 &
			(glGetSamplerParameteriv = GLContext.getFunctionAddress("glGetSamplerParameteriv")) != 0 &
			(glGetSamplerParameterfv = GLContext.getFunctionAddress("glGetSamplerParameterfv")) != 0 &
			(glGetSamplerParameterIiv = GLContext.getFunctionAddress("glGetSamplerParameterIiv")) != 0 &
			(glGetSamplerParameterIuiv = GLContext.getFunctionAddress("glGetSamplerParameterIuiv")) != 0 &
			(glQueryCounter = GLContext.getFunctionAddress("glQueryCounter")) != 0 &
			(glGetQueryObjecti64v = GLContext.getFunctionAddress("glGetQueryObjecti64v")) != 0 &
			(glGetQueryObjectui64v = GLContext.getFunctionAddress("glGetQueryObjectui64v")) != 0 &
			(glVertexAttribDivisor = GLContext.getFunctionAddress("glVertexAttribDivisor")) != 0 &
			(forwardCompatible || (glVertexP2ui = GLContext.getFunctionAddress("glVertexP2ui")) != 0) &
			(forwardCompatible || (glVertexP3ui = GLContext.getFunctionAddress("glVertexP3ui")) != 0) &
			(forwardCompatible || (glVertexP4ui = GLContext.getFunctionAddress("glVertexP4ui")) != 0) &
			(forwardCompatible || (glVertexP2uiv = GLContext.getFunctionAddress("glVertexP2uiv")) != 0) &
			(forwardCompatible || (glVertexP3uiv = GLContext.getFunctionAddress("glVertexP3uiv")) != 0) &
			(forwardCompatible || (glVertexP4uiv = GLContext.getFunctionAddress("glVertexP4uiv")) != 0) &
			(forwardCompatible || (glTexCoordP1ui = GLContext.getFunctionAddress("glTexCoordP1ui")) != 0) &
			(forwardCompatible || (glTexCoordP2ui = GLContext.getFunctionAddress("glTexCoordP2ui")) != 0) &
			(forwardCompatible || (glTexCoordP3ui = GLContext.getFunctionAddress("glTexCoordP3ui")) != 0) &
			(forwardCompatible || (glTexCoordP4ui = GLContext.getFunctionAddress("glTexCoordP4ui")) != 0) &
			(forwardCompatible || (glTexCoordP1uiv = GLContext.getFunctionAddress("glTexCoordP1uiv")) != 0) &
			(forwardCompatible || (glTexCoordP2uiv = GLContext.getFunctionAddress("glTexCoordP2uiv")) != 0) &
			(forwardCompatible || (glTexCoordP3uiv = GLContext.getFunctionAddress("glTexCoordP3uiv")) != 0) &
			(forwardCompatible || (glTexCoordP4uiv = GLContext.getFunctionAddress("glTexCoordP4uiv")) != 0) &
			(forwardCompatible || (glMultiTexCoordP1ui = GLContext.getFunctionAddress("glMultiTexCoordP1ui")) != 0) &
			(forwardCompatible || (glMultiTexCoordP2ui = GLContext.getFunctionAddress("glMultiTexCoordP2ui")) != 0) &
			(forwardCompatible || (glMultiTexCoordP3ui = GLContext.getFunctionAddress("glMultiTexCoordP3ui")) != 0) &
			(forwardCompatible || (glMultiTexCoordP4ui = GLContext.getFunctionAddress("glMultiTexCoordP4ui")) != 0) &
			(forwardCompatible || (glMultiTexCoordP1uiv = GLContext.getFunctionAddress("glMultiTexCoordP1uiv")) != 0) &
			(forwardCompatible || (glMultiTexCoordP2uiv = GLContext.getFunctionAddress("glMultiTexCoordP2uiv")) != 0) &
			(forwardCompatible || (glMultiTexCoordP3uiv = GLContext.getFunctionAddress("glMultiTexCoordP3uiv")) != 0) &
			(forwardCompatible || (glMultiTexCoordP4uiv = GLContext.getFunctionAddress("glMultiTexCoordP4uiv")) != 0) &
			(forwardCompatible || (glNormalP3ui = GLContext.getFunctionAddress("glNormalP3ui")) != 0) &
			(forwardCompatible || (glNormalP3uiv = GLContext.getFunctionAddress("glNormalP3uiv")) != 0) &
			(forwardCompatible || (glColorP3ui = GLContext.getFunctionAddress("glColorP3ui")) != 0) &
			(forwardCompatible || (glColorP4ui = GLContext.getFunctionAddress("glColorP4ui")) != 0) &
			(forwardCompatible || (glColorP3uiv = GLContext.getFunctionAddress("glColorP3uiv")) != 0) &
			(forwardCompatible || (glColorP4uiv = GLContext.getFunctionAddress("glColorP4uiv")) != 0) &
			(forwardCompatible || (glSecondaryColorP3ui = GLContext.getFunctionAddress("glSecondaryColorP3ui")) != 0) &
			(forwardCompatible || (glSecondaryColorP3uiv = GLContext.getFunctionAddress("glSecondaryColorP3uiv")) != 0) &
			(forwardCompatible || (glVertexAttribP1ui = GLContext.getFunctionAddress("glVertexAttribP1ui")) != 0) &
			(forwardCompatible || (glVertexAttribP2ui = GLContext.getFunctionAddress("glVertexAttribP2ui")) != 0) &
			(forwardCompatible || (glVertexAttribP3ui = GLContext.getFunctionAddress("glVertexAttribP3ui")) != 0) &
			(forwardCompatible || (glVertexAttribP4ui = GLContext.getFunctionAddress("glVertexAttribP4ui")) != 0) &
			(forwardCompatible || (glVertexAttribP1uiv = GLContext.getFunctionAddress("glVertexAttribP1uiv")) != 0) &
			(forwardCompatible || (glVertexAttribP2uiv = GLContext.getFunctionAddress("glVertexAttribP2uiv")) != 0) &
			(forwardCompatible || (glVertexAttribP3uiv = GLContext.getFunctionAddress("glVertexAttribP3uiv")) != 0) &
			(forwardCompatible || (glVertexAttribP4uiv = GLContext.getFunctionAddress("glVertexAttribP4uiv")) != 0);
	}

	private boolean GL40_initNativeFunctionAddresses() {
		return 
			(glBlendEquationi = GLContext.getFunctionAddress("glBlendEquationi")) != 0 &
			(glBlendEquationSeparatei = GLContext.getFunctionAddress("glBlendEquationSeparatei")) != 0 &
			(glBlendFunci = GLContext.getFunctionAddress("glBlendFunci")) != 0 &
			(glBlendFuncSeparatei = GLContext.getFunctionAddress("glBlendFuncSeparatei")) != 0 &
			(glDrawArraysIndirect = GLContext.getFunctionAddress("glDrawArraysIndirect")) != 0 &
			(glDrawElementsIndirect = GLContext.getFunctionAddress("glDrawElementsIndirect")) != 0 &
			(glUniform1d = GLContext.getFunctionAddress("glUniform1d")) != 0 &
			(glUniform2d = GLContext.getFunctionAddress("glUniform2d")) != 0 &
			(glUniform3d = GLContext.getFunctionAddress("glUniform3d")) != 0 &
			(glUniform4d = GLContext.getFunctionAddress("glUniform4d")) != 0 &
			(glUniform1dv = GLContext.getFunctionAddress("glUniform1dv")) != 0 &
			(glUniform2dv = GLContext.getFunctionAddress("glUniform2dv")) != 0 &
			(glUniform3dv = GLContext.getFunctionAddress("glUniform3dv")) != 0 &
			(glUniform4dv = GLContext.getFunctionAddress("glUniform4dv")) != 0 &
			(glUniformMatrix2dv = GLContext.getFunctionAddress("glUniformMatrix2dv")) != 0 &
			(glUniformMatrix3dv = GLContext.getFunctionAddress("glUniformMatrix3dv")) != 0 &
			(glUniformMatrix4dv = GLContext.getFunctionAddress("glUniformMatrix4dv")) != 0 &
			(glUniformMatrix2x3dv = GLContext.getFunctionAddress("glUniformMatrix2x3dv")) != 0 &
			(glUniformMatrix2x4dv = GLContext.getFunctionAddress("glUniformMatrix2x4dv")) != 0 &
			(glUniformMatrix3x2dv = GLContext.getFunctionAddress("glUniformMatrix3x2dv")) != 0 &
			(glUniformMatrix3x4dv = GLContext.getFunctionAddress("glUniformMatrix3x4dv")) != 0 &
			(glUniformMatrix4x2dv = GLContext.getFunctionAddress("glUniformMatrix4x2dv")) != 0 &
			(glUniformMatrix4x3dv = GLContext.getFunctionAddress("glUniformMatrix4x3dv")) != 0 &
			(glGetUniformdv = GLContext.getFunctionAddress("glGetUniformdv")) != 0 &
			(glMinSampleShading = GLContext.getFunctionAddress("glMinSampleShading")) != 0 &
			(glGetSubroutineUniformLocation = GLContext.getFunctionAddress("glGetSubroutineUniformLocation")) != 0 &
			(glGetSubroutineIndex = GLContext.getFunctionAddress("glGetSubroutineIndex")) != 0 &
			(glGetActiveSubroutineUniformiv = GLContext.getFunctionAddress("glGetActiveSubroutineUniformiv")) != 0 &
			(glGetActiveSubroutineUniformName = GLContext.getFunctionAddress("glGetActiveSubroutineUniformName")) != 0 &
			(glGetActiveSubroutineName = GLContext.getFunctionAddress("glGetActiveSubroutineName")) != 0 &
			(glUniformSubroutinesuiv = GLContext.getFunctionAddress("glUniformSubroutinesuiv")) != 0 &
			(glGetUniformSubroutineuiv = GLContext.getFunctionAddress("glGetUniformSubroutineuiv")) != 0 &
			(glGetProgramStageiv = GLContext.getFunctionAddress("glGetProgramStageiv")) != 0 &
			(glPatchParameteri = GLContext.getFunctionAddress("glPatchParameteri")) != 0 &
			(glPatchParameterfv = GLContext.getFunctionAddress("glPatchParameterfv")) != 0 &
			(glBindTransformFeedback = GLContext.getFunctionAddress("glBindTransformFeedback")) != 0 &
			(glDeleteTransformFeedbacks = GLContext.getFunctionAddress("glDeleteTransformFeedbacks")) != 0 &
			(glGenTransformFeedbacks = GLContext.getFunctionAddress("glGenTransformFeedbacks")) != 0 &
			(glIsTransformFeedback = GLContext.getFunctionAddress("glIsTransformFeedback")) != 0 &
			(glPauseTransformFeedback = GLContext.getFunctionAddress("glPauseTransformFeedback")) != 0 &
			(glResumeTransformFeedback = GLContext.getFunctionAddress("glResumeTransformFeedback")) != 0 &
			(glDrawTransformFeedback = GLContext.getFunctionAddress("glDrawTransformFeedback")) != 0 &
			(glDrawTransformFeedbackStream = GLContext.getFunctionAddress("glDrawTransformFeedbackStream")) != 0 &
			(glBeginQueryIndexed = GLContext.getFunctionAddress("glBeginQueryIndexed")) != 0 &
			(glEndQueryIndexed = GLContext.getFunctionAddress("glEndQueryIndexed")) != 0 &
			(glGetQueryIndexediv = GLContext.getFunctionAddress("glGetQueryIndexediv")) != 0;
	}

	private boolean GL41_initNativeFunctionAddresses() {
		return 
			(glReleaseShaderCompiler = GLContext.getFunctionAddress("glReleaseShaderCompiler")) != 0 &
			(glShaderBinary = GLContext.getFunctionAddress("glShaderBinary")) != 0 &
			(glGetShaderPrecisionFormat = GLContext.getFunctionAddress("glGetShaderPrecisionFormat")) != 0 &
			(glDepthRangef = GLContext.getFunctionAddress("glDepthRangef")) != 0 &
			(glClearDepthf = GLContext.getFunctionAddress("glClearDepthf")) != 0 &
			(glGetProgramBinary = GLContext.getFunctionAddress("glGetProgramBinary")) != 0 &
			(glProgramBinary = GLContext.getFunctionAddress("glProgramBinary")) != 0 &
			(glProgramParameteri = GLContext.getFunctionAddress("glProgramParameteri")) != 0 &
			(glUseProgramStages = GLContext.getFunctionAddress("glUseProgramStages")) != 0 &
			(glActiveShaderProgram = GLContext.getFunctionAddress("glActiveShaderProgram")) != 0 &
			(glCreateShaderProgramv = GLContext.getFunctionAddress("glCreateShaderProgramv")) != 0 &
			(glBindProgramPipeline = GLContext.getFunctionAddress("glBindProgramPipeline")) != 0 &
			(glDeleteProgramPipelines = GLContext.getFunctionAddress("glDeleteProgramPipelines")) != 0 &
			(glGenProgramPipelines = GLContext.getFunctionAddress("glGenProgramPipelines")) != 0 &
			(glIsProgramPipeline = GLContext.getFunctionAddress("glIsProgramPipeline")) != 0 &
			(glGetProgramPipelineiv = GLContext.getFunctionAddress("glGetProgramPipelineiv")) != 0 &
			(glProgramUniform1i = GLContext.getFunctionAddress("glProgramUniform1i")) != 0 &
			(glProgramUniform2i = GLContext.getFunctionAddress("glProgramUniform2i")) != 0 &
			(glProgramUniform3i = GLContext.getFunctionAddress("glProgramUniform3i")) != 0 &
			(glProgramUniform4i = GLContext.getFunctionAddress("glProgramUniform4i")) != 0 &
			(glProgramUniform1f = GLContext.getFunctionAddress("glProgramUniform1f")) != 0 &
			(glProgramUniform2f = GLContext.getFunctionAddress("glProgramUniform2f")) != 0 &
			(glProgramUniform3f = GLContext.getFunctionAddress("glProgramUniform3f")) != 0 &
			(glProgramUniform4f = GLContext.getFunctionAddress("glProgramUniform4f")) != 0 &
			(glProgramUniform1d = GLContext.getFunctionAddress("glProgramUniform1d")) != 0 &
			(glProgramUniform2d = GLContext.getFunctionAddress("glProgramUniform2d")) != 0 &
			(glProgramUniform3d = GLContext.getFunctionAddress("glProgramUniform3d")) != 0 &
			(glProgramUniform4d = GLContext.getFunctionAddress("glProgramUniform4d")) != 0 &
			(glProgramUniform1iv = GLContext.getFunctionAddress("glProgramUniform1iv")) != 0 &
			(glProgramUniform2iv = GLContext.getFunctionAddress("glProgramUniform2iv")) != 0 &
			(glProgramUniform3iv = GLContext.getFunctionAddress("glProgramUniform3iv")) != 0 &
			(glProgramUniform4iv = GLContext.getFunctionAddress("glProgramUniform4iv")) != 0 &
			(glProgramUniform1fv = GLContext.getFunctionAddress("glProgramUniform1fv")) != 0 &
			(glProgramUniform2fv = GLContext.getFunctionAddress("glProgramUniform2fv")) != 0 &
			(glProgramUniform3fv = GLContext.getFunctionAddress("glProgramUniform3fv")) != 0 &
			(glProgramUniform4fv = GLContext.getFunctionAddress("glProgramUniform4fv")) != 0 &
			(glProgramUniform1dv = GLContext.getFunctionAddress("glProgramUniform1dv")) != 0 &
			(glProgramUniform2dv = GLContext.getFunctionAddress("glProgramUniform2dv")) != 0 &
			(glProgramUniform3dv = GLContext.getFunctionAddress("glProgramUniform3dv")) != 0 &
			(glProgramUniform4dv = GLContext.getFunctionAddress("glProgramUniform4dv")) != 0 &
			(glProgramUniform1ui = GLContext.getFunctionAddress("glProgramUniform1ui")) != 0 &
			(glProgramUniform2ui = GLContext.getFunctionAddress("glProgramUniform2ui")) != 0 &
			(glProgramUniform3ui = GLContext.getFunctionAddress("glProgramUniform3ui")) != 0 &
			(glProgramUniform4ui = GLContext.getFunctionAddress("glProgramUniform4ui")) != 0 &
			(glProgramUniform1uiv = GLContext.getFunctionAddress("glProgramUniform1uiv")) != 0 &
			(glProgramUniform2uiv = GLContext.getFunctionAddress("glProgramUniform2uiv")) != 0 &
			(glProgramUniform3uiv = GLContext.getFunctionAddress("glProgramUniform3uiv")) != 0 &
			(glProgramUniform4uiv = GLContext.getFunctionAddress("glProgramUniform4uiv")) != 0 &
			(glProgramUniformMatrix2fv = GLContext.getFunctionAddress("glProgramUniformMatrix2fv")) != 0 &
			(glProgramUniformMatrix3fv = GLContext.getFunctionAddress("glProgramUniformMatrix3fv")) != 0 &
			(glProgramUniformMatrix4fv = GLContext.getFunctionAddress("glProgramUniformMatrix4fv")) != 0 &
			(glProgramUniformMatrix2dv = GLContext.getFunctionAddress("glProgramUniformMatrix2dv")) != 0 &
			(glProgramUniformMatrix3dv = GLContext.getFunctionAddress("glProgramUniformMatrix3dv")) != 0 &
			(glProgramUniformMatrix4dv = GLContext.getFunctionAddress("glProgramUniformMatrix4dv")) != 0 &
			(glProgramUniformMatrix2x3fv = GLContext.getFunctionAddress("glProgramUniformMatrix2x3fv")) != 0 &
			(glProgramUniformMatrix3x2fv = GLContext.getFunctionAddress("glProgramUniformMatrix3x2fv")) != 0 &
			(glProgramUniformMatrix2x4fv = GLContext.getFunctionAddress("glProgramUniformMatrix2x4fv")) != 0 &
			(glProgramUniformMatrix4x2fv = GLContext.getFunctionAddress("glProgramUniformMatrix4x2fv")) != 0 &
			(glProgramUniformMatrix3x4fv = GLContext.getFunctionAddress("glProgramUniformMatrix3x4fv")) != 0 &
			(glProgramUniformMatrix4x3fv = GLContext.getFunctionAddress("glProgramUniformMatrix4x3fv")) != 0 &
			(glProgramUniformMatrix2x3dv = GLContext.getFunctionAddress("glProgramUniformMatrix2x3dv")) != 0 &
			(glProgramUniformMatrix3x2dv = GLContext.getFunctionAddress("glProgramUniformMatrix3x2dv")) != 0 &
			(glProgramUniformMatrix2x4dv = GLContext.getFunctionAddress("glProgramUniformMatrix2x4dv")) != 0 &
			(glProgramUniformMatrix4x2dv = GLContext.getFunctionAddress("glProgramUniformMatrix4x2dv")) != 0 &
			(glProgramUniformMatrix3x4dv = GLContext.getFunctionAddress("glProgramUniformMatrix3x4dv")) != 0 &
			(glProgramUniformMatrix4x3dv = GLContext.getFunctionAddress("glProgramUniformMatrix4x3dv")) != 0 &
			(glValidateProgramPipeline = GLContext.getFunctionAddress("glValidateProgramPipeline")) != 0 &
			(glGetProgramPipelineInfoLog = GLContext.getFunctionAddress("glGetProgramPipelineInfoLog")) != 0 &
			(glVertexAttribL1d = GLContext.getFunctionAddress("glVertexAttribL1d")) != 0 &
			(glVertexAttribL2d = GLContext.getFunctionAddress("glVertexAttribL2d")) != 0 &
			(glVertexAttribL3d = GLContext.getFunctionAddress("glVertexAttribL3d")) != 0 &
			(glVertexAttribL4d = GLContext.getFunctionAddress("glVertexAttribL4d")) != 0 &
			(glVertexAttribL1dv = GLContext.getFunctionAddress("glVertexAttribL1dv")) != 0 &
			(glVertexAttribL2dv = GLContext.getFunctionAddress("glVertexAttribL2dv")) != 0 &
			(glVertexAttribL3dv = GLContext.getFunctionAddress("glVertexAttribL3dv")) != 0 &
			(glVertexAttribL4dv = GLContext.getFunctionAddress("glVertexAttribL4dv")) != 0 &
			(glVertexAttribLPointer = GLContext.getFunctionAddress("glVertexAttribLPointer")) != 0 &
			(glGetVertexAttribLdv = GLContext.getFunctionAddress("glGetVertexAttribLdv")) != 0 &
			(glViewportArrayv = GLContext.getFunctionAddress("glViewportArrayv")) != 0 &
			(glViewportIndexedf = GLContext.getFunctionAddress("glViewportIndexedf")) != 0 &
			(glViewportIndexedfv = GLContext.getFunctionAddress("glViewportIndexedfv")) != 0 &
			(glScissorArrayv = GLContext.getFunctionAddress("glScissorArrayv")) != 0 &
			(glScissorIndexed = GLContext.getFunctionAddress("glScissorIndexed")) != 0 &
			(glScissorIndexedv = GLContext.getFunctionAddress("glScissorIndexedv")) != 0 &
			(glDepthRangeArrayv = GLContext.getFunctionAddress("glDepthRangeArrayv")) != 0 &
			(glDepthRangeIndexed = GLContext.getFunctionAddress("glDepthRangeIndexed")) != 0 &
			(glGetFloati_v = GLContext.getFunctionAddress("glGetFloati_v")) != 0 &
			(glGetDoublei_v = GLContext.getFunctionAddress("glGetDoublei_v")) != 0;
	}

	private boolean GL42_initNativeFunctionAddresses() {
		return 
			(glGetActiveAtomicCounterBufferiv = GLContext.getFunctionAddress("glGetActiveAtomicCounterBufferiv")) != 0 &
			(glTexStorage1D = GLContext.getFunctionAddress("glTexStorage1D")) != 0 &
			(glTexStorage2D = GLContext.getFunctionAddress("glTexStorage2D")) != 0 &
			(glTexStorage3D = GLContext.getFunctionAddress("glTexStorage3D")) != 0 &
			(glDrawTransformFeedbackInstanced = GLContext.getFunctionAddress("glDrawTransformFeedbackInstanced")) != 0 &
			(glDrawTransformFeedbackStreamInstanced = GLContext.getFunctionAddress("glDrawTransformFeedbackStreamInstanced")) != 0 &
			(glDrawArraysInstancedBaseInstance = GLContext.getFunctionAddress("glDrawArraysInstancedBaseInstance")) != 0 &
			(glDrawElementsInstancedBaseInstance = GLContext.getFunctionAddress("glDrawElementsInstancedBaseInstance")) != 0 &
			(glDrawElementsInstancedBaseVertexBaseInstance = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertexBaseInstance")) != 0 &
			(glBindImageTexture = GLContext.getFunctionAddress("glBindImageTexture")) != 0 &
			(glMemoryBarrier = GLContext.getFunctionAddress("glMemoryBarrier")) != 0 &
			(glGetInternalformativ = GLContext.getFunctionAddress("glGetInternalformativ")) != 0;
	}

	private boolean GL43_initNativeFunctionAddresses() {
		return 
			(glClearBufferData = GLContext.getFunctionAddress("glClearBufferData")) != 0 &
			(glClearBufferSubData = GLContext.getFunctionAddress("glClearBufferSubData")) != 0 &
			(glDispatchCompute = GLContext.getFunctionAddress("glDispatchCompute")) != 0 &
			(glDispatchComputeIndirect = GLContext.getFunctionAddress("glDispatchComputeIndirect")) != 0 &
			(glCopyImageSubData = GLContext.getFunctionAddress("glCopyImageSubData")) != 0 &
			(glDebugMessageControl = GLContext.getFunctionAddress("glDebugMessageControl")) != 0 &
			(glDebugMessageInsert = GLContext.getFunctionAddress("glDebugMessageInsert")) != 0 &
			(glDebugMessageCallback = GLContext.getFunctionAddress("glDebugMessageCallback")) != 0 &
			(glGetDebugMessageLog = GLContext.getFunctionAddress("glGetDebugMessageLog")) != 0 &
			(glPushDebugGroup = GLContext.getFunctionAddress("glPushDebugGroup")) != 0 &
			(glPopDebugGroup = GLContext.getFunctionAddress("glPopDebugGroup")) != 0 &
			(glObjectLabel = GLContext.getFunctionAddress("glObjectLabel")) != 0 &
			(glGetObjectLabel = GLContext.getFunctionAddress("glGetObjectLabel")) != 0 &
			(glObjectPtrLabel = GLContext.getFunctionAddress("glObjectPtrLabel")) != 0 &
			(glGetObjectPtrLabel = GLContext.getFunctionAddress("glGetObjectPtrLabel")) != 0 &
			(glFramebufferParameteri = GLContext.getFunctionAddress("glFramebufferParameteri")) != 0 &
			(glGetFramebufferParameteriv = GLContext.getFunctionAddress("glGetFramebufferParameteriv")) != 0 &
			(glGetInternalformati64v = GLContext.getFunctionAddress("glGetInternalformati64v")) != 0 &
			(glInvalidateTexSubImage = GLContext.getFunctionAddress("glInvalidateTexSubImage")) != 0 &
			(glInvalidateTexImage = GLContext.getFunctionAddress("glInvalidateTexImage")) != 0 &
			(glInvalidateBufferSubData = GLContext.getFunctionAddress("glInvalidateBufferSubData")) != 0 &
			(glInvalidateBufferData = GLContext.getFunctionAddress("glInvalidateBufferData")) != 0 &
			(glInvalidateFramebuffer = GLContext.getFunctionAddress("glInvalidateFramebuffer")) != 0 &
			(glInvalidateSubFramebuffer = GLContext.getFunctionAddress("glInvalidateSubFramebuffer")) != 0 &
			(glMultiDrawArraysIndirect = GLContext.getFunctionAddress("glMultiDrawArraysIndirect")) != 0 &
			(glMultiDrawElementsIndirect = GLContext.getFunctionAddress("glMultiDrawElementsIndirect")) != 0 &
			(glGetProgramInterfaceiv = GLContext.getFunctionAddress("glGetProgramInterfaceiv")) != 0 &
			(glGetProgramResourceIndex = GLContext.getFunctionAddress("glGetProgramResourceIndex")) != 0 &
			(glGetProgramResourceName = GLContext.getFunctionAddress("glGetProgramResourceName")) != 0 &
			(glGetProgramResourceiv = GLContext.getFunctionAddress("glGetProgramResourceiv")) != 0 &
			(glGetProgramResourceLocation = GLContext.getFunctionAddress("glGetProgramResourceLocation")) != 0 &
			(glGetProgramResourceLocationIndex = GLContext.getFunctionAddress("glGetProgramResourceLocationIndex")) != 0 &
			(glShaderStorageBlockBinding = GLContext.getFunctionAddress("glShaderStorageBlockBinding")) != 0 &
			(glTexBufferRange = GLContext.getFunctionAddress("glTexBufferRange")) != 0 &
			(glTexStorage2DMultisample = GLContext.getFunctionAddress("glTexStorage2DMultisample")) != 0 &
			(glTexStorage3DMultisample = GLContext.getFunctionAddress("glTexStorage3DMultisample")) != 0 &
			(glTextureView = GLContext.getFunctionAddress("glTextureView")) != 0 &
			(glBindVertexBuffer = GLContext.getFunctionAddress("glBindVertexBuffer")) != 0 &
			(glVertexAttribFormat = GLContext.getFunctionAddress("glVertexAttribFormat")) != 0 &
			(glVertexAttribIFormat = GLContext.getFunctionAddress("glVertexAttribIFormat")) != 0 &
			(glVertexAttribLFormat = GLContext.getFunctionAddress("glVertexAttribLFormat")) != 0 &
			(glVertexAttribBinding = GLContext.getFunctionAddress("glVertexAttribBinding")) != 0 &
			(glVertexBindingDivisor = GLContext.getFunctionAddress("glVertexBindingDivisor")) != 0;
	}

	private boolean GL44_initNativeFunctionAddresses() {
		return 
			(glBufferStorage = GLContext.getFunctionAddress("glBufferStorage")) != 0 &
			(glClearTexImage = GLContext.getFunctionAddress("glClearTexImage")) != 0 &
			(glClearTexSubImage = GLContext.getFunctionAddress("glClearTexSubImage")) != 0 &
			(glBindBuffersBase = GLContext.getFunctionAddress("glBindBuffersBase")) != 0 &
			(glBindBuffersRange = GLContext.getFunctionAddress("glBindBuffersRange")) != 0 &
			(glBindTextures = GLContext.getFunctionAddress("glBindTextures")) != 0 &
			(glBindSamplers = GLContext.getFunctionAddress("glBindSamplers")) != 0 &
			(glBindImageTextures = GLContext.getFunctionAddress("glBindImageTextures")) != 0 &
			(glBindVertexBuffers = GLContext.getFunctionAddress("glBindVertexBuffers")) != 0;
	}

	private boolean GL45_initNativeFunctionAddresses() {
		return 
			(glClipControl = GLContext.getFunctionAddress("glClipControl")) != 0 &
			(glCreateTransformFeedbacks = GLContext.getFunctionAddress("glCreateTransformFeedbacks")) != 0 &
			(glTransformFeedbackBufferBase = GLContext.getFunctionAddress("glTransformFeedbackBufferBase")) != 0 &
			(glTransformFeedbackBufferRange = GLContext.getFunctionAddress("glTransformFeedbackBufferRange")) != 0 &
			(glGetTransformFeedbackiv = GLContext.getFunctionAddress("glGetTransformFeedbackiv")) != 0 &
			(glGetTransformFeedbacki_v = GLContext.getFunctionAddress("glGetTransformFeedbacki_v")) != 0 &
			(glGetTransformFeedbacki64_v = GLContext.getFunctionAddress("glGetTransformFeedbacki64_v")) != 0 &
			(glCreateBuffers = GLContext.getFunctionAddress("glCreateBuffers")) != 0 &
			(glNamedBufferStorage = GLContext.getFunctionAddress("glNamedBufferStorage")) != 0 &
			(glNamedBufferData = GLContext.getFunctionAddress("glNamedBufferData")) != 0 &
			(glNamedBufferSubData = GLContext.getFunctionAddress("glNamedBufferSubData")) != 0 &
			(glCopyNamedBufferSubData = GLContext.getFunctionAddress("glCopyNamedBufferSubData")) != 0 &
			(glClearNamedBufferData = GLContext.getFunctionAddress("glClearNamedBufferData")) != 0 &
			(glClearNamedBufferSubData = GLContext.getFunctionAddress("glClearNamedBufferSubData")) != 0 &
			(glMapNamedBuffer = GLContext.getFunctionAddress("glMapNamedBuffer")) != 0 &
			(glMapNamedBufferRange = GLContext.getFunctionAddress("glMapNamedBufferRange")) != 0 &
			(glUnmapNamedBuffer = GLContext.getFunctionAddress("glUnmapNamedBuffer")) != 0 &
			(glFlushMappedNamedBufferRange = GLContext.getFunctionAddress("glFlushMappedNamedBufferRange")) != 0 &
			(glGetNamedBufferParameteriv = GLContext.getFunctionAddress("glGetNamedBufferParameteriv")) != 0 &
			(glGetNamedBufferParameteri64v = GLContext.getFunctionAddress("glGetNamedBufferParameteri64v")) != 0 &
			(glGetNamedBufferPointerv = GLContext.getFunctionAddress("glGetNamedBufferPointerv")) != 0 &
			(glGetNamedBufferSubData = GLContext.getFunctionAddress("glGetNamedBufferSubData")) != 0 &
			(glCreateFramebuffers = GLContext.getFunctionAddress("glCreateFramebuffers")) != 0 &
			(glNamedFramebufferRenderbuffer = GLContext.getFunctionAddress("glNamedFramebufferRenderbuffer")) != 0 &
			(glNamedFramebufferParameteri = GLContext.getFunctionAddress("glNamedFramebufferParameteri")) != 0 &
			(glNamedFramebufferTexture = GLContext.getFunctionAddress("glNamedFramebufferTexture")) != 0 &
			(glNamedFramebufferTextureLayer = GLContext.getFunctionAddress("glNamedFramebufferTextureLayer")) != 0 &
			(glNamedFramebufferDrawBuffer = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffer")) != 0 &
			(glNamedFramebufferDrawBuffers = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffers")) != 0 &
			(glNamedFramebufferReadBuffer = GLContext.getFunctionAddress("glNamedFramebufferReadBuffer")) != 0 &
			(glInvalidateNamedFramebufferData = GLContext.getFunctionAddress("glInvalidateNamedFramebufferData")) != 0 &
			(glInvalidateNamedFramebufferSubData = GLContext.getFunctionAddress("glInvalidateNamedFramebufferSubData")) != 0 &
			(glClearNamedFramebufferiv = GLContext.getFunctionAddress("glClearNamedFramebufferiv")) != 0 &
			(glClearNamedFramebufferuiv = GLContext.getFunctionAddress("glClearNamedFramebufferuiv")) != 0 &
			(glClearNamedFramebufferfv = GLContext.getFunctionAddress("glClearNamedFramebufferfv")) != 0 &
			(glClearNamedFramebufferfi = GLContext.getFunctionAddress("glClearNamedFramebufferfi")) != 0 &
			(glBlitNamedFramebuffer = GLContext.getFunctionAddress("glBlitNamedFramebuffer")) != 0 &
			(glCheckNamedFramebufferStatus = GLContext.getFunctionAddress("glCheckNamedFramebufferStatus")) != 0 &
			(glGetNamedFramebufferParameteriv = GLContext.getFunctionAddress("glGetNamedFramebufferParameteriv")) != 0 &
			(glGetNamedFramebufferAttachmentParameteriv = GLContext.getFunctionAddress("glGetNamedFramebufferAttachmentParameteriv")) != 0 &
			(glCreateRenderbuffers = GLContext.getFunctionAddress("glCreateRenderbuffers")) != 0 &
			(glNamedRenderbufferStorage = GLContext.getFunctionAddress("glNamedRenderbufferStorage")) != 0 &
			(glNamedRenderbufferStorageMultisample = GLContext.getFunctionAddress("glNamedRenderbufferStorageMultisample")) != 0 &
			(glGetNamedRenderbufferParameteriv = GLContext.getFunctionAddress("glGetNamedRenderbufferParameteriv")) != 0 &
			(glCreateTextures = GLContext.getFunctionAddress("glCreateTextures")) != 0 &
			(glTextureBuffer = GLContext.getFunctionAddress("glTextureBuffer")) != 0 &
			(glTextureBufferRange = GLContext.getFunctionAddress("glTextureBufferRange")) != 0 &
			(glTextureStorage1D = GLContext.getFunctionAddress("glTextureStorage1D")) != 0 &
			(glTextureStorage2D = GLContext.getFunctionAddress("glTextureStorage2D")) != 0 &
			(glTextureStorage3D = GLContext.getFunctionAddress("glTextureStorage3D")) != 0 &
			(glTextureStorage2DMultisample = GLContext.getFunctionAddress("glTextureStorage2DMultisample")) != 0 &
			(glTextureStorage3DMultisample = GLContext.getFunctionAddress("glTextureStorage3DMultisample")) != 0 &
			(glTextureSubImage1D = GLContext.getFunctionAddress("glTextureSubImage1D")) != 0 &
			(glTextureSubImage2D = GLContext.getFunctionAddress("glTextureSubImage2D")) != 0 &
			(glTextureSubImage3D = GLContext.getFunctionAddress("glTextureSubImage3D")) != 0 &
			(glCompressedTextureSubImage1D = GLContext.getFunctionAddress("glCompressedTextureSubImage1D")) != 0 &
			(glCompressedTextureSubImage2D = GLContext.getFunctionAddress("glCompressedTextureSubImage2D")) != 0 &
			(glCompressedTextureSubImage3D = GLContext.getFunctionAddress("glCompressedTextureSubImage3D")) != 0 &
			(glCopyTextureSubImage1D = GLContext.getFunctionAddress("glCopyTextureSubImage1D")) != 0 &
			(glCopyTextureSubImage2D = GLContext.getFunctionAddress("glCopyTextureSubImage2D")) != 0 &
			(glCopyTextureSubImage3D = GLContext.getFunctionAddress("glCopyTextureSubImage3D")) != 0 &
			(glTextureParameterf = GLContext.getFunctionAddress("glTextureParameterf")) != 0 &
			(glTextureParameterfv = GLContext.getFunctionAddress("glTextureParameterfv")) != 0 &
			(glTextureParameteri = GLContext.getFunctionAddress("glTextureParameteri")) != 0 &
			(glTextureParameterIiv = GLContext.getFunctionAddress("glTextureParameterIiv")) != 0 &
			(glTextureParameterIuiv = GLContext.getFunctionAddress("glTextureParameterIuiv")) != 0 &
			(glTextureParameteriv = GLContext.getFunctionAddress("glTextureParameteriv")) != 0 &
			(glGenerateTextureMipmap = GLContext.getFunctionAddress("glGenerateTextureMipmap")) != 0 &
			(glBindTextureUnit = GLContext.getFunctionAddress("glBindTextureUnit")) != 0 &
			(glGetTextureImage = GLContext.getFunctionAddress("glGetTextureImage")) != 0 &
			(glGetCompressedTextureImage = GLContext.getFunctionAddress("glGetCompressedTextureImage")) != 0 &
			(glGetTextureLevelParameterfv = GLContext.getFunctionAddress("glGetTextureLevelParameterfv")) != 0 &
			(glGetTextureLevelParameteriv = GLContext.getFunctionAddress("glGetTextureLevelParameteriv")) != 0 &
			(glGetTextureParameterfv = GLContext.getFunctionAddress("glGetTextureParameterfv")) != 0 &
			(glGetTextureParameterIiv = GLContext.getFunctionAddress("glGetTextureParameterIiv")) != 0 &
			(glGetTextureParameterIuiv = GLContext.getFunctionAddress("glGetTextureParameterIuiv")) != 0 &
			(glGetTextureParameteriv = GLContext.getFunctionAddress("glGetTextureParameteriv")) != 0 &
			(glCreateVertexArrays = GLContext.getFunctionAddress("glCreateVertexArrays")) != 0 &
			(glDisableVertexArrayAttrib = GLContext.getFunctionAddress("glDisableVertexArrayAttrib")) != 0 &
			(glEnableVertexArrayAttrib = GLContext.getFunctionAddress("glEnableVertexArrayAttrib")) != 0 &
			(glVertexArrayElementBuffer = GLContext.getFunctionAddress("glVertexArrayElementBuffer")) != 0 &
			(glVertexArrayVertexBuffer = GLContext.getFunctionAddress("glVertexArrayVertexBuffer")) != 0 &
			(glVertexArrayVertexBuffers = GLContext.getFunctionAddress("glVertexArrayVertexBuffers")) != 0 &
			(glVertexArrayAttribFormat = GLContext.getFunctionAddress("glVertexArrayAttribFormat")) != 0 &
			(glVertexArrayAttribIFormat = GLContext.getFunctionAddress("glVertexArrayAttribIFormat")) != 0 &
			(glVertexArrayAttribLFormat = GLContext.getFunctionAddress("glVertexArrayAttribLFormat")) != 0 &
			(glVertexArrayAttribBinding = GLContext.getFunctionAddress("glVertexArrayAttribBinding")) != 0 &
			(glVertexArrayBindingDivisor = GLContext.getFunctionAddress("glVertexArrayBindingDivisor")) != 0 &
			(glGetVertexArrayiv = GLContext.getFunctionAddress("glGetVertexArrayiv")) != 0 &
			(glGetVertexArrayIndexediv = GLContext.getFunctionAddress("glGetVertexArrayIndexediv")) != 0 &
			(glGetVertexArrayIndexed64iv = GLContext.getFunctionAddress("glGetVertexArrayIndexed64iv")) != 0 &
			(glCreateSamplers = GLContext.getFunctionAddress("glCreateSamplers")) != 0 &
			(glCreateProgramPipelines = GLContext.getFunctionAddress("glCreateProgramPipelines")) != 0 &
			(glCreateQueries = GLContext.getFunctionAddress("glCreateQueries")) != 0 &
			(glMemoryBarrierByRegion = GLContext.getFunctionAddress("glMemoryBarrierByRegion")) != 0 &
			(glGetTextureSubImage = GLContext.getFunctionAddress("glGetTextureSubImage")) != 0 &
			(glGetCompressedTextureSubImage = GLContext.getFunctionAddress("glGetCompressedTextureSubImage")) != 0 &
			(glTextureBarrier = GLContext.getFunctionAddress("glTextureBarrier")) != 0 &
			(glGetGraphicsResetStatus = GLContext.getFunctionAddress("glGetGraphicsResetStatus")) != 0 &
			(glReadnPixels = GLContext.getFunctionAddress("glReadnPixels")) != 0 &
			(glGetnUniformfv = GLContext.getFunctionAddress("glGetnUniformfv")) != 0 &
			(glGetnUniformiv = GLContext.getFunctionAddress("glGetnUniformiv")) != 0 &
			(glGetnUniformuiv = GLContext.getFunctionAddress("glGetnUniformuiv")) != 0;
	}

	private boolean GREMEDY_frame_terminator_initNativeFunctionAddresses() {
		return 
			(glFrameTerminatorGREMEDY = GLContext.getFunctionAddress("glFrameTerminatorGREMEDY")) != 0;
	}

	private boolean GREMEDY_string_marker_initNativeFunctionAddresses() {
		return 
			(glStringMarkerGREMEDY = GLContext.getFunctionAddress("glStringMarkerGREMEDY")) != 0;
	}

	private boolean INTEL_map_texture_initNativeFunctionAddresses() {
		return 
			(glMapTexture2DINTEL = GLContext.getFunctionAddress("glMapTexture2DINTEL")) != 0 &
			(glUnmapTexture2DINTEL = GLContext.getFunctionAddress("glUnmapTexture2DINTEL")) != 0 &
			(glSyncTextureINTEL = GLContext.getFunctionAddress("glSyncTextureINTEL")) != 0;
	}

	private boolean KHR_debug_initNativeFunctionAddresses() {
		return 
			(glDebugMessageControl = GLContext.getFunctionAddress("glDebugMessageControl")) != 0 &
			(glDebugMessageInsert = GLContext.getFunctionAddress("glDebugMessageInsert")) != 0 &
			(glDebugMessageCallback = GLContext.getFunctionAddress("glDebugMessageCallback")) != 0 &
			(glGetDebugMessageLog = GLContext.getFunctionAddress("glGetDebugMessageLog")) != 0 &
			(glPushDebugGroup = GLContext.getFunctionAddress("glPushDebugGroup")) != 0 &
			(glPopDebugGroup = GLContext.getFunctionAddress("glPopDebugGroup")) != 0 &
			(glObjectLabel = GLContext.getFunctionAddress("glObjectLabel")) != 0 &
			(glGetObjectLabel = GLContext.getFunctionAddress("glGetObjectLabel")) != 0 &
			(glObjectPtrLabel = GLContext.getFunctionAddress("glObjectPtrLabel")) != 0 &
			(glGetObjectPtrLabel = GLContext.getFunctionAddress("glGetObjectPtrLabel")) != 0;
	}

	private boolean KHR_robustness_initNativeFunctionAddresses() {
		return 
			(glGetGraphicsResetStatus = GLContext.getFunctionAddress("glGetGraphicsResetStatus")) != 0 &
			(glReadnPixels = GLContext.getFunctionAddress("glReadnPixels")) != 0 &
			(glGetnUniformfv = GLContext.getFunctionAddress("glGetnUniformfv")) != 0 &
			(glGetnUniformiv = GLContext.getFunctionAddress("glGetnUniformiv")) != 0 &
			(glGetnUniformuiv = GLContext.getFunctionAddress("glGetnUniformuiv")) != 0;
	}

	private boolean NV_bindless_multi_draw_indirect_initNativeFunctionAddresses() {
		return 
			(glMultiDrawArraysIndirectBindlessNV = GLContext.getFunctionAddress("glMultiDrawArraysIndirectBindlessNV")) != 0 &
			(glMultiDrawElementsIndirectBindlessNV = GLContext.getFunctionAddress("glMultiDrawElementsIndirectBindlessNV")) != 0;
	}

	private boolean NV_bindless_texture_initNativeFunctionAddresses() {
		return 
			(glGetTextureHandleNV = GLContext.getFunctionAddress("glGetTextureHandleNV")) != 0 &
			(glGetTextureSamplerHandleNV = GLContext.getFunctionAddress("glGetTextureSamplerHandleNV")) != 0 &
			(glMakeTextureHandleResidentNV = GLContext.getFunctionAddress("glMakeTextureHandleResidentNV")) != 0 &
			(glMakeTextureHandleNonResidentNV = GLContext.getFunctionAddress("glMakeTextureHandleNonResidentNV")) != 0 &
			(glGetImageHandleNV = GLContext.getFunctionAddress("glGetImageHandleNV")) != 0 &
			(glMakeImageHandleResidentNV = GLContext.getFunctionAddress("glMakeImageHandleResidentNV")) != 0 &
			(glMakeImageHandleNonResidentNV = GLContext.getFunctionAddress("glMakeImageHandleNonResidentNV")) != 0 &
			(glUniformHandleui64NV = GLContext.getFunctionAddress("glUniformHandleui64NV")) != 0 &
			(glUniformHandleui64vNV = GLContext.getFunctionAddress("glUniformHandleui64vNV")) != 0 &
			(glProgramUniformHandleui64NV = GLContext.getFunctionAddress("glProgramUniformHandleui64NV")) != 0 &
			(glProgramUniformHandleui64vNV = GLContext.getFunctionAddress("glProgramUniformHandleui64vNV")) != 0 &
			(glIsTextureHandleResidentNV = GLContext.getFunctionAddress("glIsTextureHandleResidentNV")) != 0 &
			(glIsImageHandleResidentNV = GLContext.getFunctionAddress("glIsImageHandleResidentNV")) != 0;
	}

	private boolean NV_blend_equation_advanced_initNativeFunctionAddresses() {
		return 
			(glBlendParameteriNV = GLContext.getFunctionAddress("glBlendParameteriNV")) != 0 &
			(glBlendBarrierNV = GLContext.getFunctionAddress("glBlendBarrierNV")) != 0;
	}

	private boolean NV_conditional_render_initNativeFunctionAddresses() {
		return 
			(glBeginConditionalRenderNV = GLContext.getFunctionAddress("glBeginConditionalRenderNV")) != 0 &
			(glEndConditionalRenderNV = GLContext.getFunctionAddress("glEndConditionalRenderNV")) != 0;
	}

	private boolean NV_copy_image_initNativeFunctionAddresses() {
		return 
			(glCopyImageSubDataNV = GLContext.getFunctionAddress("glCopyImageSubDataNV")) != 0;
	}

	private boolean NV_depth_buffer_float_initNativeFunctionAddresses() {
		return 
			(glDepthRangedNV = GLContext.getFunctionAddress("glDepthRangedNV")) != 0 &
			(glClearDepthdNV = GLContext.getFunctionAddress("glClearDepthdNV")) != 0 &
			(glDepthBoundsdNV = GLContext.getFunctionAddress("glDepthBoundsdNV")) != 0;
	}

	private boolean NV_draw_texture_initNativeFunctionAddresses() {
		return 
			(glDrawTextureNV = GLContext.getFunctionAddress("glDrawTextureNV")) != 0;
	}

	private boolean NV_evaluators_initNativeFunctionAddresses() {
		return 
			(glGetMapControlPointsNV = GLContext.getFunctionAddress("glGetMapControlPointsNV")) != 0 &
			(glMapControlPointsNV = GLContext.getFunctionAddress("glMapControlPointsNV")) != 0 &
			(glMapParameterfvNV = GLContext.getFunctionAddress("glMapParameterfvNV")) != 0 &
			(glMapParameterivNV = GLContext.getFunctionAddress("glMapParameterivNV")) != 0 &
			(glGetMapParameterfvNV = GLContext.getFunctionAddress("glGetMapParameterfvNV")) != 0 &
			(glGetMapParameterivNV = GLContext.getFunctionAddress("glGetMapParameterivNV")) != 0 &
			(glGetMapAttribParameterfvNV = GLContext.getFunctionAddress("glGetMapAttribParameterfvNV")) != 0 &
			(glGetMapAttribParameterivNV = GLContext.getFunctionAddress("glGetMapAttribParameterivNV")) != 0 &
			(glEvalMapsNV = GLContext.getFunctionAddress("glEvalMapsNV")) != 0;
	}

	private boolean NV_explicit_multisample_initNativeFunctionAddresses() {
		return 
			(glGetBooleanIndexedvEXT = GLContext.getFunctionAddress("glGetBooleanIndexedvEXT")) != 0 &
			(glGetIntegerIndexedvEXT = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT")) != 0 &
			(glGetMultisamplefvNV = GLContext.getFunctionAddress("glGetMultisamplefvNV")) != 0 &
			(glSampleMaskIndexedNV = GLContext.getFunctionAddress("glSampleMaskIndexedNV")) != 0 &
			(glTexRenderbufferNV = GLContext.getFunctionAddress("glTexRenderbufferNV")) != 0;
	}

	private boolean NV_fence_initNativeFunctionAddresses() {
		return 
			(glGenFencesNV = GLContext.getFunctionAddress("glGenFencesNV")) != 0 &
			(glDeleteFencesNV = GLContext.getFunctionAddress("glDeleteFencesNV")) != 0 &
			(glSetFenceNV = GLContext.getFunctionAddress("glSetFenceNV")) != 0 &
			(glTestFenceNV = GLContext.getFunctionAddress("glTestFenceNV")) != 0 &
			(glFinishFenceNV = GLContext.getFunctionAddress("glFinishFenceNV")) != 0 &
			(glIsFenceNV = GLContext.getFunctionAddress("glIsFenceNV")) != 0 &
			(glGetFenceivNV = GLContext.getFunctionAddress("glGetFenceivNV")) != 0;
	}

	private boolean NV_fragment_program_initNativeFunctionAddresses() {
		return 
			(glProgramNamedParameter4fNV = GLContext.getFunctionAddress("glProgramNamedParameter4fNV")) != 0 &
			(glProgramNamedParameter4dNV = GLContext.getFunctionAddress("glProgramNamedParameter4dNV")) != 0 &
			(glGetProgramNamedParameterfvNV = GLContext.getFunctionAddress("glGetProgramNamedParameterfvNV")) != 0 &
			(glGetProgramNamedParameterdvNV = GLContext.getFunctionAddress("glGetProgramNamedParameterdvNV")) != 0;
	}

	private boolean NV_framebuffer_multisample_coverage_initNativeFunctionAddresses() {
		return 
			(glRenderbufferStorageMultisampleCoverageNV = GLContext.getFunctionAddress("glRenderbufferStorageMultisampleCoverageNV")) != 0;
	}

	private boolean NV_geometry_program4_initNativeFunctionAddresses() {
		return 
			(glProgramVertexLimitNV = GLContext.getFunctionAddress("glProgramVertexLimitNV")) != 0 &
			(glFramebufferTextureEXT = GLContext.getFunctionAddress("glFramebufferTextureEXT")) != 0 &
			(glFramebufferTextureLayerEXT = GLContext.getFunctionAddress("glFramebufferTextureLayerEXT")) != 0 &
			(glFramebufferTextureFaceEXT = GLContext.getFunctionAddress("glFramebufferTextureFaceEXT")) != 0;
	}

	private boolean NV_gpu_program4_initNativeFunctionAddresses() {
		return 
			(glProgramLocalParameterI4iNV = GLContext.getFunctionAddress("glProgramLocalParameterI4iNV")) != 0 &
			(glProgramLocalParameterI4ivNV = GLContext.getFunctionAddress("glProgramLocalParameterI4ivNV")) != 0 &
			(glProgramLocalParametersI4ivNV = GLContext.getFunctionAddress("glProgramLocalParametersI4ivNV")) != 0 &
			(glProgramLocalParameterI4uiNV = GLContext.getFunctionAddress("glProgramLocalParameterI4uiNV")) != 0 &
			(glProgramLocalParameterI4uivNV = GLContext.getFunctionAddress("glProgramLocalParameterI4uivNV")) != 0 &
			(glProgramLocalParametersI4uivNV = GLContext.getFunctionAddress("glProgramLocalParametersI4uivNV")) != 0 &
			(glProgramEnvParameterI4iNV = GLContext.getFunctionAddress("glProgramEnvParameterI4iNV")) != 0 &
			(glProgramEnvParameterI4ivNV = GLContext.getFunctionAddress("glProgramEnvParameterI4ivNV")) != 0 &
			(glProgramEnvParametersI4ivNV = GLContext.getFunctionAddress("glProgramEnvParametersI4ivNV")) != 0 &
			(glProgramEnvParameterI4uiNV = GLContext.getFunctionAddress("glProgramEnvParameterI4uiNV")) != 0 &
			(glProgramEnvParameterI4uivNV = GLContext.getFunctionAddress("glProgramEnvParameterI4uivNV")) != 0 &
			(glProgramEnvParametersI4uivNV = GLContext.getFunctionAddress("glProgramEnvParametersI4uivNV")) != 0 &
			(glGetProgramLocalParameterIivNV = GLContext.getFunctionAddress("glGetProgramLocalParameterIivNV")) != 0 &
			(glGetProgramLocalParameterIuivNV = GLContext.getFunctionAddress("glGetProgramLocalParameterIuivNV")) != 0 &
			(glGetProgramEnvParameterIivNV = GLContext.getFunctionAddress("glGetProgramEnvParameterIivNV")) != 0 &
			(glGetProgramEnvParameterIuivNV = GLContext.getFunctionAddress("glGetProgramEnvParameterIuivNV")) != 0;
	}

	private boolean NV_gpu_shader5_initNativeFunctionAddresses(Set<String> supported_extensions) {
		return 
			(glUniform1i64NV = GLContext.getFunctionAddress("glUniform1i64NV")) != 0 &
			(glUniform2i64NV = GLContext.getFunctionAddress("glUniform2i64NV")) != 0 &
			(glUniform3i64NV = GLContext.getFunctionAddress("glUniform3i64NV")) != 0 &
			(glUniform4i64NV = GLContext.getFunctionAddress("glUniform4i64NV")) != 0 &
			(glUniform1i64vNV = GLContext.getFunctionAddress("glUniform1i64vNV")) != 0 &
			(glUniform2i64vNV = GLContext.getFunctionAddress("glUniform2i64vNV")) != 0 &
			(glUniform3i64vNV = GLContext.getFunctionAddress("glUniform3i64vNV")) != 0 &
			(glUniform4i64vNV = GLContext.getFunctionAddress("glUniform4i64vNV")) != 0 &
			(glUniform1ui64NV = GLContext.getFunctionAddress("glUniform1ui64NV")) != 0 &
			(glUniform2ui64NV = GLContext.getFunctionAddress("glUniform2ui64NV")) != 0 &
			(glUniform3ui64NV = GLContext.getFunctionAddress("glUniform3ui64NV")) != 0 &
			(glUniform4ui64NV = GLContext.getFunctionAddress("glUniform4ui64NV")) != 0 &
			(glUniform1ui64vNV = GLContext.getFunctionAddress("glUniform1ui64vNV")) != 0 &
			(glUniform2ui64vNV = GLContext.getFunctionAddress("glUniform2ui64vNV")) != 0 &
			(glUniform3ui64vNV = GLContext.getFunctionAddress("glUniform3ui64vNV")) != 0 &
			(glUniform4ui64vNV = GLContext.getFunctionAddress("glUniform4ui64vNV")) != 0 &
			(glGetUniformi64vNV = GLContext.getFunctionAddress("glGetUniformi64vNV")) != 0 &
			(glGetUniformui64vNV = GLContext.getFunctionAddress("glGetUniformui64vNV")) != 0 &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform1i64NV = GLContext.getFunctionAddress("glProgramUniform1i64NV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform2i64NV = GLContext.getFunctionAddress("glProgramUniform2i64NV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform3i64NV = GLContext.getFunctionAddress("glProgramUniform3i64NV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform4i64NV = GLContext.getFunctionAddress("glProgramUniform4i64NV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform1i64vNV = GLContext.getFunctionAddress("glProgramUniform1i64vNV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform2i64vNV = GLContext.getFunctionAddress("glProgramUniform2i64vNV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform3i64vNV = GLContext.getFunctionAddress("glProgramUniform3i64vNV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform4i64vNV = GLContext.getFunctionAddress("glProgramUniform4i64vNV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform1ui64NV = GLContext.getFunctionAddress("glProgramUniform1ui64NV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform2ui64NV = GLContext.getFunctionAddress("glProgramUniform2ui64NV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform3ui64NV = GLContext.getFunctionAddress("glProgramUniform3ui64NV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform4ui64NV = GLContext.getFunctionAddress("glProgramUniform4ui64NV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform1ui64vNV = GLContext.getFunctionAddress("glProgramUniform1ui64vNV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform2ui64vNV = GLContext.getFunctionAddress("glProgramUniform2ui64vNV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform3ui64vNV = GLContext.getFunctionAddress("glProgramUniform3ui64vNV")) != 0) &
			(!supported_extensions.contains("GL_EXT_direct_state_access") || (glProgramUniform4ui64vNV = GLContext.getFunctionAddress("glProgramUniform4ui64vNV")) != 0);
	}

	private boolean NV_half_float_initNativeFunctionAddresses(Set<String> supported_extensions) {
		return 
			(glVertex2hNV = GLContext.getFunctionAddress("glVertex2hNV")) != 0 &
			(glVertex3hNV = GLContext.getFunctionAddress("glVertex3hNV")) != 0 &
			(glVertex4hNV = GLContext.getFunctionAddress("glVertex4hNV")) != 0 &
			(glNormal3hNV = GLContext.getFunctionAddress("glNormal3hNV")) != 0 &
			(glColor3hNV = GLContext.getFunctionAddress("glColor3hNV")) != 0 &
			(glColor4hNV = GLContext.getFunctionAddress("glColor4hNV")) != 0 &
			(glTexCoord1hNV = GLContext.getFunctionAddress("glTexCoord1hNV")) != 0 &
			(glTexCoord2hNV = GLContext.getFunctionAddress("glTexCoord2hNV")) != 0 &
			(glTexCoord3hNV = GLContext.getFunctionAddress("glTexCoord3hNV")) != 0 &
			(glTexCoord4hNV = GLContext.getFunctionAddress("glTexCoord4hNV")) != 0 &
			(glMultiTexCoord1hNV = GLContext.getFunctionAddress("glMultiTexCoord1hNV")) != 0 &
			(glMultiTexCoord2hNV = GLContext.getFunctionAddress("glMultiTexCoord2hNV")) != 0 &
			(glMultiTexCoord3hNV = GLContext.getFunctionAddress("glMultiTexCoord3hNV")) != 0 &
			(glMultiTexCoord4hNV = GLContext.getFunctionAddress("glMultiTexCoord4hNV")) != 0 &
			(!supported_extensions.contains("GL_EXT_fog_coord") || (glFogCoordhNV = GLContext.getFunctionAddress("glFogCoordhNV")) != 0) &
			(!supported_extensions.contains("GL_EXT_secondary_color") || (glSecondaryColor3hNV = GLContext.getFunctionAddress("glSecondaryColor3hNV")) != 0) &
			(!supported_extensions.contains("GL_EXT_vertex_weighting") || (glVertexWeighthNV = GLContext.getFunctionAddress("glVertexWeighthNV")) != 0) &
			(!supported_extensions.contains("GL_NV_vertex_program") || (glVertexAttrib1hNV = GLContext.getFunctionAddress("glVertexAttrib1hNV")) != 0) &
			(!supported_extensions.contains("GL_NV_vertex_program") || (glVertexAttrib2hNV = GLContext.getFunctionAddress("glVertexAttrib2hNV")) != 0) &
			(!supported_extensions.contains("GL_NV_vertex_program") || (glVertexAttrib3hNV = GLContext.getFunctionAddress("glVertexAttrib3hNV")) != 0) &
			(!supported_extensions.contains("GL_NV_vertex_program") || (glVertexAttrib4hNV = GLContext.getFunctionAddress("glVertexAttrib4hNV")) != 0) &
			(!supported_extensions.contains("GL_NV_vertex_program") || (glVertexAttribs1hvNV = GLContext.getFunctionAddress("glVertexAttribs1hvNV")) != 0) &
			(!supported_extensions.contains("GL_NV_vertex_program") || (glVertexAttribs2hvNV = GLContext.getFunctionAddress("glVertexAttribs2hvNV")) != 0) &
			(!supported_extensions.contains("GL_NV_vertex_program") || (glVertexAttribs3hvNV = GLContext.getFunctionAddress("glVertexAttribs3hvNV")) != 0) &
			(!supported_extensions.contains("GL_NV_vertex_program") || (glVertexAttribs4hvNV = GLContext.getFunctionAddress("glVertexAttribs4hvNV")) != 0);
	}

	private boolean NV_occlusion_query_initNativeFunctionAddresses() {
		return 
			(glGenOcclusionQueriesNV = GLContext.getFunctionAddress("glGenOcclusionQueriesNV")) != 0 &
			(glDeleteOcclusionQueriesNV = GLContext.getFunctionAddress("glDeleteOcclusionQueriesNV")) != 0 &
			(glIsOcclusionQueryNV = GLContext.getFunctionAddress("glIsOcclusionQueryNV")) != 0 &
			(glBeginOcclusionQueryNV = GLContext.getFunctionAddress("glBeginOcclusionQueryNV")) != 0 &
			(glEndOcclusionQueryNV = GLContext.getFunctionAddress("glEndOcclusionQueryNV")) != 0 &
			(glGetOcclusionQueryuivNV = GLContext.getFunctionAddress("glGetOcclusionQueryuivNV")) != 0 &
			(glGetOcclusionQueryivNV = GLContext.getFunctionAddress("glGetOcclusionQueryivNV")) != 0;
	}

	private boolean NV_parameter_buffer_object_initNativeFunctionAddresses() {
		return 
			(glProgramBufferParametersfvNV = GLContext.getFunctionAddress("glProgramBufferParametersfvNV")) != 0 &
			(glProgramBufferParametersIivNV = GLContext.getFunctionAddress("glProgramBufferParametersIivNV")) != 0 &
			(glProgramBufferParametersIuivNV = GLContext.getFunctionAddress("glProgramBufferParametersIuivNV")) != 0;
	}

	private boolean NV_path_rendering_initNativeFunctionAddresses() {
		return 
			(glPathCommandsNV = GLContext.getFunctionAddress("glPathCommandsNV")) != 0 &
			(glPathCoordsNV = GLContext.getFunctionAddress("glPathCoordsNV")) != 0 &
			(glPathSubCommandsNV = GLContext.getFunctionAddress("glPathSubCommandsNV")) != 0 &
			(glPathSubCoordsNV = GLContext.getFunctionAddress("glPathSubCoordsNV")) != 0 &
			(glPathStringNV = GLContext.getFunctionAddress("glPathStringNV")) != 0 &
			(glPathGlyphsNV = GLContext.getFunctionAddress("glPathGlyphsNV")) != 0 &
			(glPathGlyphRangeNV = GLContext.getFunctionAddress("glPathGlyphRangeNV")) != 0 &
			(glWeightPathsNV = GLContext.getFunctionAddress("glWeightPathsNV")) != 0 &
			(glCopyPathNV = GLContext.getFunctionAddress("glCopyPathNV")) != 0 &
			(glInterpolatePathsNV = GLContext.getFunctionAddress("glInterpolatePathsNV")) != 0 &
			(glTransformPathNV = GLContext.getFunctionAddress("glTransformPathNV")) != 0 &
			(glPathParameterivNV = GLContext.getFunctionAddress("glPathParameterivNV")) != 0 &
			(glPathParameteriNV = GLContext.getFunctionAddress("glPathParameteriNV")) != 0 &
			(glPathParameterfvNV = GLContext.getFunctionAddress("glPathParameterfvNV")) != 0 &
			(glPathParameterfNV = GLContext.getFunctionAddress("glPathParameterfNV")) != 0 &
			(glPathDashArrayNV = GLContext.getFunctionAddress("glPathDashArrayNV")) != 0 &
			(glGenPathsNV = GLContext.getFunctionAddress("glGenPathsNV")) != 0 &
			(glDeletePathsNV = GLContext.getFunctionAddress("glDeletePathsNV")) != 0 &
			(glIsPathNV = GLContext.getFunctionAddress("glIsPathNV")) != 0 &
			(glPathStencilFuncNV = GLContext.getFunctionAddress("glPathStencilFuncNV")) != 0 &
			(glPathStencilDepthOffsetNV = GLContext.getFunctionAddress("glPathStencilDepthOffsetNV")) != 0 &
			(glStencilFillPathNV = GLContext.getFunctionAddress("glStencilFillPathNV")) != 0 &
			(glStencilStrokePathNV = GLContext.getFunctionAddress("glStencilStrokePathNV")) != 0 &
			(glStencilFillPathInstancedNV = GLContext.getFunctionAddress("glStencilFillPathInstancedNV")) != 0 &
			(glStencilStrokePathInstancedNV = GLContext.getFunctionAddress("glStencilStrokePathInstancedNV")) != 0 &
			(glPathCoverDepthFuncNV = GLContext.getFunctionAddress("glPathCoverDepthFuncNV")) != 0 &
			(glPathColorGenNV = GLContext.getFunctionAddress("glPathColorGenNV")) != 0 &
			(glPathTexGenNV = GLContext.getFunctionAddress("glPathTexGenNV")) != 0 &
			(glPathFogGenNV = GLContext.getFunctionAddress("glPathFogGenNV")) != 0 &
			(glCoverFillPathNV = GLContext.getFunctionAddress("glCoverFillPathNV")) != 0 &
			(glCoverStrokePathNV = GLContext.getFunctionAddress("glCoverStrokePathNV")) != 0 &
			(glCoverFillPathInstancedNV = GLContext.getFunctionAddress("glCoverFillPathInstancedNV")) != 0 &
			(glCoverStrokePathInstancedNV = GLContext.getFunctionAddress("glCoverStrokePathInstancedNV")) != 0 &
			(glGetPathParameterivNV = GLContext.getFunctionAddress("glGetPathParameterivNV")) != 0 &
			(glGetPathParameterfvNV = GLContext.getFunctionAddress("glGetPathParameterfvNV")) != 0 &
			(glGetPathCommandsNV = GLContext.getFunctionAddress("glGetPathCommandsNV")) != 0 &
			(glGetPathCoordsNV = GLContext.getFunctionAddress("glGetPathCoordsNV")) != 0 &
			(glGetPathDashArrayNV = GLContext.getFunctionAddress("glGetPathDashArrayNV")) != 0 &
			(glGetPathMetricsNV = GLContext.getFunctionAddress("glGetPathMetricsNV")) != 0 &
			(glGetPathMetricRangeNV = GLContext.getFunctionAddress("glGetPathMetricRangeNV")) != 0 &
			(glGetPathSpacingNV = GLContext.getFunctionAddress("glGetPathSpacingNV")) != 0 &
			(glGetPathColorGenivNV = GLContext.getFunctionAddress("glGetPathColorGenivNV")) != 0 &
			(glGetPathColorGenfvNV = GLContext.getFunctionAddress("glGetPathColorGenfvNV")) != 0 &
			(glGetPathTexGenivNV = GLContext.getFunctionAddress("glGetPathTexGenivNV")) != 0 &
			(glGetPathTexGenfvNV = GLContext.getFunctionAddress("glGetPathTexGenfvNV")) != 0 &
			(glIsPointInFillPathNV = GLContext.getFunctionAddress("glIsPointInFillPathNV")) != 0 &
			(glIsPointInStrokePathNV = GLContext.getFunctionAddress("glIsPointInStrokePathNV")) != 0 &
			(glGetPathLengthNV = GLContext.getFunctionAddress("glGetPathLengthNV")) != 0 &
			(glPointAlongPathNV = GLContext.getFunctionAddress("glPointAlongPathNV")) != 0;
	}

	private boolean NV_pixel_data_range_initNativeFunctionAddresses() {
		return 
			(glPixelDataRangeNV = GLContext.getFunctionAddress("glPixelDataRangeNV")) != 0 &
			(glFlushPixelDataRangeNV = GLContext.getFunctionAddress("glFlushPixelDataRangeNV")) != 0;
	}

	private boolean NV_point_sprite_initNativeFunctionAddresses() {
		return 
			(glPointParameteriNV = GLContext.getFunctionAddress("glPointParameteriNV")) != 0 &
			(glPointParameterivNV = GLContext.getFunctionAddress("glPointParameterivNV")) != 0;
	}

	private boolean NV_present_video_initNativeFunctionAddresses() {
		return 
			(glPresentFrameKeyedNV = GLContext.getFunctionAddress("glPresentFrameKeyedNV")) != 0 &
			(glPresentFrameDualFillNV = GLContext.getFunctionAddress("glPresentFrameDualFillNV")) != 0 &
			(glGetVideoivNV = GLContext.getFunctionAddress("glGetVideoivNV")) != 0 &
			(glGetVideouivNV = GLContext.getFunctionAddress("glGetVideouivNV")) != 0 &
			(glGetVideoi64vNV = GLContext.getFunctionAddress("glGetVideoi64vNV")) != 0 &
			(glGetVideoui64vNV = GLContext.getFunctionAddress("glGetVideoui64vNV")) != 0;
	}

	private boolean NV_primitive_restart_initNativeFunctionAddresses() {
		return 
			(glPrimitiveRestartNV = GLContext.getFunctionAddress("glPrimitiveRestartNV")) != 0 &
			(glPrimitiveRestartIndexNV = GLContext.getFunctionAddress("glPrimitiveRestartIndexNV")) != 0;
	}

	private boolean NV_program_initNativeFunctionAddresses() {
		return 
			(glLoadProgramNV = GLContext.getFunctionAddress("glLoadProgramNV")) != 0 &
			(glBindProgramNV = GLContext.getFunctionAddress("glBindProgramNV")) != 0 &
			(glDeleteProgramsNV = GLContext.getFunctionAddress("glDeleteProgramsNV")) != 0 &
			(glGenProgramsNV = GLContext.getFunctionAddress("glGenProgramsNV")) != 0 &
			(glGetProgramivNV = GLContext.getFunctionAddress("glGetProgramivNV")) != 0 &
			(glGetProgramStringNV = GLContext.getFunctionAddress("glGetProgramStringNV")) != 0 &
			(glIsProgramNV = GLContext.getFunctionAddress("glIsProgramNV")) != 0 &
			(glAreProgramsResidentNV = GLContext.getFunctionAddress("glAreProgramsResidentNV")) != 0 &
			(glRequestResidentProgramsNV = GLContext.getFunctionAddress("glRequestResidentProgramsNV")) != 0;
	}

	private boolean NV_register_combiners_initNativeFunctionAddresses() {
		return 
			(glCombinerParameterfNV = GLContext.getFunctionAddress("glCombinerParameterfNV")) != 0 &
			(glCombinerParameterfvNV = GLContext.getFunctionAddress("glCombinerParameterfvNV")) != 0 &
			(glCombinerParameteriNV = GLContext.getFunctionAddress("glCombinerParameteriNV")) != 0 &
			(glCombinerParameterivNV = GLContext.getFunctionAddress("glCombinerParameterivNV")) != 0 &
			(glCombinerInputNV = GLContext.getFunctionAddress("glCombinerInputNV")) != 0 &
			(glCombinerOutputNV = GLContext.getFunctionAddress("glCombinerOutputNV")) != 0 &
			(glFinalCombinerInputNV = GLContext.getFunctionAddress("glFinalCombinerInputNV")) != 0 &
			(glGetCombinerInputParameterfvNV = GLContext.getFunctionAddress("glGetCombinerInputParameterfvNV")) != 0 &
			(glGetCombinerInputParameterivNV = GLContext.getFunctionAddress("glGetCombinerInputParameterivNV")) != 0 &
			(glGetCombinerOutputParameterfvNV = GLContext.getFunctionAddress("glGetCombinerOutputParameterfvNV")) != 0 &
			(glGetCombinerOutputParameterivNV = GLContext.getFunctionAddress("glGetCombinerOutputParameterivNV")) != 0 &
			(glGetFinalCombinerInputParameterfvNV = GLContext.getFunctionAddress("glGetFinalCombinerInputParameterfvNV")) != 0 &
			(glGetFinalCombinerInputParameterivNV = GLContext.getFunctionAddress("glGetFinalCombinerInputParameterivNV")) != 0;
	}

	private boolean NV_register_combiners2_initNativeFunctionAddresses() {
		return 
			(glCombinerStageParameterfvNV = GLContext.getFunctionAddress("glCombinerStageParameterfvNV")) != 0 &
			(glGetCombinerStageParameterfvNV = GLContext.getFunctionAddress("glGetCombinerStageParameterfvNV")) != 0;
	}

	private boolean NV_shader_buffer_load_initNativeFunctionAddresses() {
		return 
			(glMakeBufferResidentNV = GLContext.getFunctionAddress("glMakeBufferResidentNV")) != 0 &
			(glMakeBufferNonResidentNV = GLContext.getFunctionAddress("glMakeBufferNonResidentNV")) != 0 &
			(glIsBufferResidentNV = GLContext.getFunctionAddress("glIsBufferResidentNV")) != 0 &
			(glMakeNamedBufferResidentNV = GLContext.getFunctionAddress("glMakeNamedBufferResidentNV")) != 0 &
			(glMakeNamedBufferNonResidentNV = GLContext.getFunctionAddress("glMakeNamedBufferNonResidentNV")) != 0 &
			(glIsNamedBufferResidentNV = GLContext.getFunctionAddress("glIsNamedBufferResidentNV")) != 0 &
			(glGetBufferParameterui64vNV = GLContext.getFunctionAddress("glGetBufferParameterui64vNV")) != 0 &
			(glGetNamedBufferParameterui64vNV = GLContext.getFunctionAddress("glGetNamedBufferParameterui64vNV")) != 0 &
			(glGetIntegerui64vNV = GLContext.getFunctionAddress("glGetIntegerui64vNV")) != 0 &
			(glUniformui64NV = GLContext.getFunctionAddress("glUniformui64NV")) != 0 &
			(glUniformui64vNV = GLContext.getFunctionAddress("glUniformui64vNV")) != 0 &
			(glGetUniformui64vNV = GLContext.getFunctionAddress("glGetUniformui64vNV")) != 0 &
			(glProgramUniformui64NV = GLContext.getFunctionAddress("glProgramUniformui64NV")) != 0 &
			(glProgramUniformui64vNV = GLContext.getFunctionAddress("glProgramUniformui64vNV")) != 0;
	}

	private boolean NV_texture_barrier_initNativeFunctionAddresses() {
		return 
			(glTextureBarrierNV = GLContext.getFunctionAddress("glTextureBarrierNV")) != 0;
	}

	private boolean NV_texture_multisample_initNativeFunctionAddresses() {
		return 
			(glTexImage2DMultisampleCoverageNV = GLContext.getFunctionAddress("glTexImage2DMultisampleCoverageNV")) != 0 &
			(glTexImage3DMultisampleCoverageNV = GLContext.getFunctionAddress("glTexImage3DMultisampleCoverageNV")) != 0 &
			(glTextureImage2DMultisampleNV = GLContext.getFunctionAddress("glTextureImage2DMultisampleNV")) != 0 &
			(glTextureImage3DMultisampleNV = GLContext.getFunctionAddress("glTextureImage3DMultisampleNV")) != 0 &
			(glTextureImage2DMultisampleCoverageNV = GLContext.getFunctionAddress("glTextureImage2DMultisampleCoverageNV")) != 0 &
			(glTextureImage3DMultisampleCoverageNV = GLContext.getFunctionAddress("glTextureImage3DMultisampleCoverageNV")) != 0;
	}

	private boolean NV_transform_feedback_initNativeFunctionAddresses() {
		return 
			(glBindBufferRangeNV = GLContext.getFunctionAddress("glBindBufferRangeNV")) != 0 &
			(glBindBufferOffsetNV = GLContext.getFunctionAddress("glBindBufferOffsetNV")) != 0 &
			(glBindBufferBaseNV = GLContext.getFunctionAddress("glBindBufferBaseNV")) != 0 &
			(glTransformFeedbackAttribsNV = GLContext.getFunctionAddress("glTransformFeedbackAttribsNV")) != 0 &
			(glTransformFeedbackVaryingsNV = GLContext.getFunctionAddress("glTransformFeedbackVaryingsNV")) != 0 &
			(glBeginTransformFeedbackNV = GLContext.getFunctionAddress("glBeginTransformFeedbackNV")) != 0 &
			(glEndTransformFeedbackNV = GLContext.getFunctionAddress("glEndTransformFeedbackNV")) != 0 &
			(glGetVaryingLocationNV = GLContext.getFunctionAddress("glGetVaryingLocationNV")) != 0 &
			(glGetActiveVaryingNV = GLContext.getFunctionAddress("glGetActiveVaryingNV")) != 0 &
			(glActiveVaryingNV = GLContext.getFunctionAddress("glActiveVaryingNV")) != 0 &
			(glGetTransformFeedbackVaryingNV = GLContext.getFunctionAddress("glGetTransformFeedbackVaryingNV")) != 0;
	}

	private boolean NV_transform_feedback2_initNativeFunctionAddresses() {
		return 
			(glBindTransformFeedbackNV = GLContext.getFunctionAddress("glBindTransformFeedbackNV")) != 0 &
			(glDeleteTransformFeedbacksNV = GLContext.getFunctionAddress("glDeleteTransformFeedbacksNV")) != 0 &
			(glGenTransformFeedbacksNV = GLContext.getFunctionAddress("glGenTransformFeedbacksNV")) != 0 &
			(glIsTransformFeedbackNV = GLContext.getFunctionAddress("glIsTransformFeedbackNV")) != 0 &
			(glPauseTransformFeedbackNV = GLContext.getFunctionAddress("glPauseTransformFeedbackNV")) != 0 &
			(glResumeTransformFeedbackNV = GLContext.getFunctionAddress("glResumeTransformFeedbackNV")) != 0 &
			(glDrawTransformFeedbackNV = GLContext.getFunctionAddress("glDrawTransformFeedbackNV")) != 0;
	}

	private boolean NV_vertex_array_range_initNativeFunctionAddresses() {
		return 
			(glVertexArrayRangeNV = GLContext.getFunctionAddress("glVertexArrayRangeNV")) != 0 &
			(glFlushVertexArrayRangeNV = GLContext.getFunctionAddress("glFlushVertexArrayRangeNV")) != 0 &
			(glAllocateMemoryNV = GLContext.getPlatformSpecificFunctionAddress("gl", new String[]{"Windows", "Linux"}, new String[]{"wgl", "glX"}, "glAllocateMemoryNV")) != 0 &
			(glFreeMemoryNV = GLContext.getPlatformSpecificFunctionAddress("gl", new String[]{"Windows", "Linux"}, new String[]{"wgl", "glX"}, "glFreeMemoryNV")) != 0;
	}

	private boolean NV_vertex_attrib_integer_64bit_initNativeFunctionAddresses(Set<String> supported_extensions) {
		return 
			(glVertexAttribL1i64NV = GLContext.getFunctionAddress("glVertexAttribL1i64NV")) != 0 &
			(glVertexAttribL2i64NV = GLContext.getFunctionAddress("glVertexAttribL2i64NV")) != 0 &
			(glVertexAttribL3i64NV = GLContext.getFunctionAddress("glVertexAttribL3i64NV")) != 0 &
			(glVertexAttribL4i64NV = GLContext.getFunctionAddress("glVertexAttribL4i64NV")) != 0 &
			(glVertexAttribL1i64vNV = GLContext.getFunctionAddress("glVertexAttribL1i64vNV")) != 0 &
			(glVertexAttribL2i64vNV = GLContext.getFunctionAddress("glVertexAttribL2i64vNV")) != 0 &
			(glVertexAttribL3i64vNV = GLContext.getFunctionAddress("glVertexAttribL3i64vNV")) != 0 &
			(glVertexAttribL4i64vNV = GLContext.getFunctionAddress("glVertexAttribL4i64vNV")) != 0 &
			(glVertexAttribL1ui64NV = GLContext.getFunctionAddress("glVertexAttribL1ui64NV")) != 0 &
			(glVertexAttribL2ui64NV = GLContext.getFunctionAddress("glVertexAttribL2ui64NV")) != 0 &
			(glVertexAttribL3ui64NV = GLContext.getFunctionAddress("glVertexAttribL3ui64NV")) != 0 &
			(glVertexAttribL4ui64NV = GLContext.getFunctionAddress("glVertexAttribL4ui64NV")) != 0 &
			(glVertexAttribL1ui64vNV = GLContext.getFunctionAddress("glVertexAttribL1ui64vNV")) != 0 &
			(glVertexAttribL2ui64vNV = GLContext.getFunctionAddress("glVertexAttribL2ui64vNV")) != 0 &
			(glVertexAttribL3ui64vNV = GLContext.getFunctionAddress("glVertexAttribL3ui64vNV")) != 0 &
			(glVertexAttribL4ui64vNV = GLContext.getFunctionAddress("glVertexAttribL4ui64vNV")) != 0 &
			(glGetVertexAttribLi64vNV = GLContext.getFunctionAddress("glGetVertexAttribLi64vNV")) != 0 &
			(glGetVertexAttribLui64vNV = GLContext.getFunctionAddress("glGetVertexAttribLui64vNV")) != 0 &
			(!supported_extensions.contains("GL_NV_vertex_buffer_unified_memory") || (glVertexAttribLFormatNV = GLContext.getFunctionAddress("glVertexAttribLFormatNV")) != 0);
	}

	private boolean NV_vertex_buffer_unified_memory_initNativeFunctionAddresses() {
		return 
			(glBufferAddressRangeNV = GLContext.getFunctionAddress("glBufferAddressRangeNV")) != 0 &
			(glVertexFormatNV = GLContext.getFunctionAddress("glVertexFormatNV")) != 0 &
			(glNormalFormatNV = GLContext.getFunctionAddress("glNormalFormatNV")) != 0 &
			(glColorFormatNV = GLContext.getFunctionAddress("glColorFormatNV")) != 0 &
			(glIndexFormatNV = GLContext.getFunctionAddress("glIndexFormatNV")) != 0 &
			(glTexCoordFormatNV = GLContext.getFunctionAddress("glTexCoordFormatNV")) != 0 &
			(glEdgeFlagFormatNV = GLContext.getFunctionAddress("glEdgeFlagFormatNV")) != 0 &
			(glSecondaryColorFormatNV = GLContext.getFunctionAddress("glSecondaryColorFormatNV")) != 0 &
			(glFogCoordFormatNV = GLContext.getFunctionAddress("glFogCoordFormatNV")) != 0 &
			(glVertexAttribFormatNV = GLContext.getFunctionAddress("glVertexAttribFormatNV")) != 0 &
			(glVertexAttribIFormatNV = GLContext.getFunctionAddress("glVertexAttribIFormatNV")) != 0 &
			(glGetIntegerui64i_vNV = GLContext.getFunctionAddress("glGetIntegerui64i_vNV")) != 0;
	}

	private boolean NV_vertex_program_initNativeFunctionAddresses() {
		return 
			(glExecuteProgramNV = GLContext.getFunctionAddress("glExecuteProgramNV")) != 0 &
			(glGetProgramParameterfvNV = GLContext.getFunctionAddress("glGetProgramParameterfvNV")) != 0 &
			(glGetProgramParameterdvNV = GLContext.getFunctionAddress("glGetProgramParameterdvNV")) != 0 &
			(glGetTrackMatrixivNV = GLContext.getFunctionAddress("glGetTrackMatrixivNV")) != 0 &
			(glGetVertexAttribfvNV = GLContext.getFunctionAddress("glGetVertexAttribfvNV")) != 0 &
			(glGetVertexAttribdvNV = GLContext.getFunctionAddress("glGetVertexAttribdvNV")) != 0 &
			(glGetVertexAttribivNV = GLContext.getFunctionAddress("glGetVertexAttribivNV")) != 0 &
			(glGetVertexAttribPointervNV = GLContext.getFunctionAddress("glGetVertexAttribPointervNV")) != 0 &
			(glProgramParameter4fNV = GLContext.getFunctionAddress("glProgramParameter4fNV")) != 0 &
			(glProgramParameter4dNV = GLContext.getFunctionAddress("glProgramParameter4dNV")) != 0 &
			(glProgramParameters4fvNV = GLContext.getFunctionAddress("glProgramParameters4fvNV")) != 0 &
			(glProgramParameters4dvNV = GLContext.getFunctionAddress("glProgramParameters4dvNV")) != 0 &
			(glTrackMatrixNV = GLContext.getFunctionAddress("glTrackMatrixNV")) != 0 &
			(glVertexAttribPointerNV = GLContext.getFunctionAddress("glVertexAttribPointerNV")) != 0 &
			(glVertexAttrib1sNV = GLContext.getFunctionAddress("glVertexAttrib1sNV")) != 0 &
			(glVertexAttrib1fNV = GLContext.getFunctionAddress("glVertexAttrib1fNV")) != 0 &
			(glVertexAttrib1dNV = GLContext.getFunctionAddress("glVertexAttrib1dNV")) != 0 &
			(glVertexAttrib2sNV = GLContext.getFunctionAddress("glVertexAttrib2sNV")) != 0 &
			(glVertexAttrib2fNV = GLContext.getFunctionAddress("glVertexAttrib2fNV")) != 0 &
			(glVertexAttrib2dNV = GLContext.getFunctionAddress("glVertexAttrib2dNV")) != 0 &
			(glVertexAttrib3sNV = GLContext.getFunctionAddress("glVertexAttrib3sNV")) != 0 &
			(glVertexAttrib3fNV = GLContext.getFunctionAddress("glVertexAttrib3fNV")) != 0 &
			(glVertexAttrib3dNV = GLContext.getFunctionAddress("glVertexAttrib3dNV")) != 0 &
			(glVertexAttrib4sNV = GLContext.getFunctionAddress("glVertexAttrib4sNV")) != 0 &
			(glVertexAttrib4fNV = GLContext.getFunctionAddress("glVertexAttrib4fNV")) != 0 &
			(glVertexAttrib4dNV = GLContext.getFunctionAddress("glVertexAttrib4dNV")) != 0 &
			(glVertexAttrib4ubNV = GLContext.getFunctionAddress("glVertexAttrib4ubNV")) != 0 &
			(glVertexAttribs1svNV = GLContext.getFunctionAddress("glVertexAttribs1svNV")) != 0 &
			(glVertexAttribs1fvNV = GLContext.getFunctionAddress("glVertexAttribs1fvNV")) != 0 &
			(glVertexAttribs1dvNV = GLContext.getFunctionAddress("glVertexAttribs1dvNV")) != 0 &
			(glVertexAttribs2svNV = GLContext.getFunctionAddress("glVertexAttribs2svNV")) != 0 &
			(glVertexAttribs2fvNV = GLContext.getFunctionAddress("glVertexAttribs2fvNV")) != 0 &
			(glVertexAttribs2dvNV = GLContext.getFunctionAddress("glVertexAttribs2dvNV")) != 0 &
			(glVertexAttribs3svNV = GLContext.getFunctionAddress("glVertexAttribs3svNV")) != 0 &
			(glVertexAttribs3fvNV = GLContext.getFunctionAddress("glVertexAttribs3fvNV")) != 0 &
			(glVertexAttribs3dvNV = GLContext.getFunctionAddress("glVertexAttribs3dvNV")) != 0 &
			(glVertexAttribs4svNV = GLContext.getFunctionAddress("glVertexAttribs4svNV")) != 0 &
			(glVertexAttribs4fvNV = GLContext.getFunctionAddress("glVertexAttribs4fvNV")) != 0 &
			(glVertexAttribs4dvNV = GLContext.getFunctionAddress("glVertexAttribs4dvNV")) != 0;
	}

	private boolean NV_video_capture_initNativeFunctionAddresses() {
		return 
			(glBeginVideoCaptureNV = GLContext.getFunctionAddress("glBeginVideoCaptureNV")) != 0 &
			(glBindVideoCaptureStreamBufferNV = GLContext.getFunctionAddress("glBindVideoCaptureStreamBufferNV")) != 0 &
			(glBindVideoCaptureStreamTextureNV = GLContext.getFunctionAddress("glBindVideoCaptureStreamTextureNV")) != 0 &
			(glEndVideoCaptureNV = GLContext.getFunctionAddress("glEndVideoCaptureNV")) != 0 &
			(glGetVideoCaptureivNV = GLContext.getFunctionAddress("glGetVideoCaptureivNV")) != 0 &
			(glGetVideoCaptureStreamivNV = GLContext.getFunctionAddress("glGetVideoCaptureStreamivNV")) != 0 &
			(glGetVideoCaptureStreamfvNV = GLContext.getFunctionAddress("glGetVideoCaptureStreamfvNV")) != 0 &
			(glGetVideoCaptureStreamdvNV = GLContext.getFunctionAddress("glGetVideoCaptureStreamdvNV")) != 0 &
			(glVideoCaptureNV = GLContext.getFunctionAddress("glVideoCaptureNV")) != 0 &
			(glVideoCaptureStreamParameterivNV = GLContext.getFunctionAddress("glVideoCaptureStreamParameterivNV")) != 0 &
			(glVideoCaptureStreamParameterfvNV = GLContext.getFunctionAddress("glVideoCaptureStreamParameterfvNV")) != 0 &
			(glVideoCaptureStreamParameterdvNV = GLContext.getFunctionAddress("glVideoCaptureStreamParameterdvNV")) != 0;
	}


	private static void remove(Set supported_extensions, String extension) {
		LWJGLUtil.log(extension + " was reported as available but an entry point is missing");
		supported_extensions.remove(extension);
	}

	private Set<String> initAllStubs(boolean forwardCompatible) throws LWJGLException {
		this.glGetError = GLContext.getFunctionAddress("glGetError");
		this.glGetString = GLContext.getFunctionAddress("glGetString");
		this.glGetIntegerv = GLContext.getFunctionAddress("glGetIntegerv");
		this.glGetStringi = GLContext.getFunctionAddress("glGetStringi");
		this.glAccum = GLContext.getFunctionAddress("glAccum");
		this.glAlphaFunc = GLContext.getFunctionAddress("glAlphaFunc");
		this.glClearColor = GLContext.getFunctionAddress("glClearColor");
		this.glClearAccum = GLContext.getFunctionAddress("glClearAccum");
		this.glClear = GLContext.getFunctionAddress("glClear");
		this.glCallLists = GLContext.getFunctionAddress("glCallLists");
		this.glCallList = GLContext.getFunctionAddress("glCallList");
		this.glBlendFunc = GLContext.getFunctionAddress("glBlendFunc");
		this.glBitmap = GLContext.getFunctionAddress("glBitmap");
		this.glBindTexture = GLContext.getFunctionAddress("glBindTexture");
		this.glPrioritizeTextures = GLContext.getFunctionAddress("glPrioritizeTextures");
		this.glAreTexturesResident = GLContext.getFunctionAddress("glAreTexturesResident");
		this.glBegin = GLContext.getFunctionAddress("glBegin");
		this.glEnd = GLContext.getFunctionAddress("glEnd");
		this.glArrayElement = GLContext.getFunctionAddress("glArrayElement");
		this.glClearDepth = GLContext.getFunctionAddress("glClearDepth");
		this.glDeleteLists = GLContext.getFunctionAddress("glDeleteLists");
		this.glDeleteTextures = GLContext.getFunctionAddress("glDeleteTextures");
		this.glCullFace = GLContext.getFunctionAddress("glCullFace");
		this.glCopyTexSubImage2D = GLContext.getFunctionAddress("glCopyTexSubImage2D");
		this.glCopyTexSubImage1D = GLContext.getFunctionAddress("glCopyTexSubImage1D");
		this.glCopyTexImage2D = GLContext.getFunctionAddress("glCopyTexImage2D");
		this.glCopyTexImage1D = GLContext.getFunctionAddress("glCopyTexImage1D");
		this.glCopyPixels = GLContext.getFunctionAddress("glCopyPixels");
		this.glColorPointer = GLContext.getFunctionAddress("glColorPointer");
		this.glColorMaterial = GLContext.getFunctionAddress("glColorMaterial");
		this.glColorMask = GLContext.getFunctionAddress("glColorMask");
		this.glColor3b = GLContext.getFunctionAddress("glColor3b");
		this.glColor3f = GLContext.getFunctionAddress("glColor3f");
		this.glColor3d = GLContext.getFunctionAddress("glColor3d");
		this.glColor3ub = GLContext.getFunctionAddress("glColor3ub");
		this.glColor4b = GLContext.getFunctionAddress("glColor4b");
		this.glColor4f = GLContext.getFunctionAddress("glColor4f");
		this.glColor4d = GLContext.getFunctionAddress("glColor4d");
		this.glColor4ub = GLContext.getFunctionAddress("glColor4ub");
		this.glClipPlane = GLContext.getFunctionAddress("glClipPlane");
		this.glClearStencil = GLContext.getFunctionAddress("glClearStencil");
		this.glEvalPoint1 = GLContext.getFunctionAddress("glEvalPoint1");
		this.glEvalPoint2 = GLContext.getFunctionAddress("glEvalPoint2");
		this.glEvalMesh1 = GLContext.getFunctionAddress("glEvalMesh1");
		this.glEvalMesh2 = GLContext.getFunctionAddress("glEvalMesh2");
		this.glEvalCoord1f = GLContext.getFunctionAddress("glEvalCoord1f");
		this.glEvalCoord1d = GLContext.getFunctionAddress("glEvalCoord1d");
		this.glEvalCoord2f = GLContext.getFunctionAddress("glEvalCoord2f");
		this.glEvalCoord2d = GLContext.getFunctionAddress("glEvalCoord2d");
		this.glEnableClientState = GLContext.getFunctionAddress("glEnableClientState");
		this.glDisableClientState = GLContext.getFunctionAddress("glDisableClientState");
		this.glEnable = GLContext.getFunctionAddress("glEnable");
		this.glDisable = GLContext.getFunctionAddress("glDisable");
		this.glEdgeFlagPointer = GLContext.getFunctionAddress("glEdgeFlagPointer");
		this.glEdgeFlag = GLContext.getFunctionAddress("glEdgeFlag");
		this.glDrawPixels = GLContext.getFunctionAddress("glDrawPixels");
		this.glDrawElements = GLContext.getFunctionAddress("glDrawElements");
		this.glDrawBuffer = GLContext.getFunctionAddress("glDrawBuffer");
		this.glDrawArrays = GLContext.getFunctionAddress("glDrawArrays");
		this.glDepthRange = GLContext.getFunctionAddress("glDepthRange");
		this.glDepthMask = GLContext.getFunctionAddress("glDepthMask");
		this.glDepthFunc = GLContext.getFunctionAddress("glDepthFunc");
		this.glFeedbackBuffer = GLContext.getFunctionAddress("glFeedbackBuffer");
		this.glGetPixelMapfv = GLContext.getFunctionAddress("glGetPixelMapfv");
		this.glGetPixelMapuiv = GLContext.getFunctionAddress("glGetPixelMapuiv");
		this.glGetPixelMapusv = GLContext.getFunctionAddress("glGetPixelMapusv");
		this.glGetMaterialfv = GLContext.getFunctionAddress("glGetMaterialfv");
		this.glGetMaterialiv = GLContext.getFunctionAddress("glGetMaterialiv");
		this.glGetMapfv = GLContext.getFunctionAddress("glGetMapfv");
		this.glGetMapdv = GLContext.getFunctionAddress("glGetMapdv");
		this.glGetMapiv = GLContext.getFunctionAddress("glGetMapiv");
		this.glGetLightfv = GLContext.getFunctionAddress("glGetLightfv");
		this.glGetLightiv = GLContext.getFunctionAddress("glGetLightiv");
		this.glGetError = GLContext.getFunctionAddress("glGetError");
		this.glGetClipPlane = GLContext.getFunctionAddress("glGetClipPlane");
		this.glGetBooleanv = GLContext.getFunctionAddress("glGetBooleanv");
		this.glGetDoublev = GLContext.getFunctionAddress("glGetDoublev");
		this.glGetFloatv = GLContext.getFunctionAddress("glGetFloatv");
		this.glGetIntegerv = GLContext.getFunctionAddress("glGetIntegerv");
		this.glGenTextures = GLContext.getFunctionAddress("glGenTextures");
		this.glGenLists = GLContext.getFunctionAddress("glGenLists");
		this.glFrustum = GLContext.getFunctionAddress("glFrustum");
		this.glFrontFace = GLContext.getFunctionAddress("glFrontFace");
		this.glFogf = GLContext.getFunctionAddress("glFogf");
		this.glFogi = GLContext.getFunctionAddress("glFogi");
		this.glFogfv = GLContext.getFunctionAddress("glFogfv");
		this.glFogiv = GLContext.getFunctionAddress("glFogiv");
		this.glFlush = GLContext.getFunctionAddress("glFlush");
		this.glFinish = GLContext.getFunctionAddress("glFinish");
		this.glGetPointerv = GLContext.getFunctionAddress("glGetPointerv");
		this.glIsEnabled = GLContext.getFunctionAddress("glIsEnabled");
		this.glInterleavedArrays = GLContext.getFunctionAddress("glInterleavedArrays");
		this.glInitNames = GLContext.getFunctionAddress("glInitNames");
		this.glHint = GLContext.getFunctionAddress("glHint");
		this.glGetTexParameterfv = GLContext.getFunctionAddress("glGetTexParameterfv");
		this.glGetTexParameteriv = GLContext.getFunctionAddress("glGetTexParameteriv");
		this.glGetTexLevelParameterfv = GLContext.getFunctionAddress("glGetTexLevelParameterfv");
		this.glGetTexLevelParameteriv = GLContext.getFunctionAddress("glGetTexLevelParameteriv");
		this.glGetTexImage = GLContext.getFunctionAddress("glGetTexImage");
		this.glGetTexGeniv = GLContext.getFunctionAddress("glGetTexGeniv");
		this.glGetTexGenfv = GLContext.getFunctionAddress("glGetTexGenfv");
		this.glGetTexGendv = GLContext.getFunctionAddress("glGetTexGendv");
		this.glGetTexEnviv = GLContext.getFunctionAddress("glGetTexEnviv");
		this.glGetTexEnvfv = GLContext.getFunctionAddress("glGetTexEnvfv");
		this.glGetString = GLContext.getFunctionAddress("glGetString");
		this.glGetPolygonStipple = GLContext.getFunctionAddress("glGetPolygonStipple");
		this.glIsList = GLContext.getFunctionAddress("glIsList");
		this.glMaterialf = GLContext.getFunctionAddress("glMaterialf");
		this.glMateriali = GLContext.getFunctionAddress("glMateriali");
		this.glMaterialfv = GLContext.getFunctionAddress("glMaterialfv");
		this.glMaterialiv = GLContext.getFunctionAddress("glMaterialiv");
		this.glMapGrid1f = GLContext.getFunctionAddress("glMapGrid1f");
		this.glMapGrid1d = GLContext.getFunctionAddress("glMapGrid1d");
		this.glMapGrid2f = GLContext.getFunctionAddress("glMapGrid2f");
		this.glMapGrid2d = GLContext.getFunctionAddress("glMapGrid2d");
		this.glMap2f = GLContext.getFunctionAddress("glMap2f");
		this.glMap2d = GLContext.getFunctionAddress("glMap2d");
		this.glMap1f = GLContext.getFunctionAddress("glMap1f");
		this.glMap1d = GLContext.getFunctionAddress("glMap1d");
		this.glLogicOp = GLContext.getFunctionAddress("glLogicOp");
		this.glLoadName = GLContext.getFunctionAddress("glLoadName");
		this.glLoadMatrixf = GLContext.getFunctionAddress("glLoadMatrixf");
		this.glLoadMatrixd = GLContext.getFunctionAddress("glLoadMatrixd");
		this.glLoadIdentity = GLContext.getFunctionAddress("glLoadIdentity");
		this.glListBase = GLContext.getFunctionAddress("glListBase");
		this.glLineWidth = GLContext.getFunctionAddress("glLineWidth");
		this.glLineStipple = GLContext.getFunctionAddress("glLineStipple");
		this.glLightModelf = GLContext.getFunctionAddress("glLightModelf");
		this.glLightModeli = GLContext.getFunctionAddress("glLightModeli");
		this.glLightModelfv = GLContext.getFunctionAddress("glLightModelfv");
		this.glLightModeliv = GLContext.getFunctionAddress("glLightModeliv");
		this.glLightf = GLContext.getFunctionAddress("glLightf");
		this.glLighti = GLContext.getFunctionAddress("glLighti");
		this.glLightfv = GLContext.getFunctionAddress("glLightfv");
		this.glLightiv = GLContext.getFunctionAddress("glLightiv");
		this.glIsTexture = GLContext.getFunctionAddress("glIsTexture");
		this.glMatrixMode = GLContext.getFunctionAddress("glMatrixMode");
		this.glPolygonStipple = GLContext.getFunctionAddress("glPolygonStipple");
		this.glPolygonOffset = GLContext.getFunctionAddress("glPolygonOffset");
		this.glPolygonMode = GLContext.getFunctionAddress("glPolygonMode");
		this.glPointSize = GLContext.getFunctionAddress("glPointSize");
		this.glPixelZoom = GLContext.getFunctionAddress("glPixelZoom");
		this.glPixelTransferf = GLContext.getFunctionAddress("glPixelTransferf");
		this.glPixelTransferi = GLContext.getFunctionAddress("glPixelTransferi");
		this.glPixelStoref = GLContext.getFunctionAddress("glPixelStoref");
		this.glPixelStorei = GLContext.getFunctionAddress("glPixelStorei");
		this.glPixelMapfv = GLContext.getFunctionAddress("glPixelMapfv");
		this.glPixelMapuiv = GLContext.getFunctionAddress("glPixelMapuiv");
		this.glPixelMapusv = GLContext.getFunctionAddress("glPixelMapusv");
		this.glPassThrough = GLContext.getFunctionAddress("glPassThrough");
		this.glOrtho = GLContext.getFunctionAddress("glOrtho");
		this.glNormalPointer = GLContext.getFunctionAddress("glNormalPointer");
		this.glNormal3b = GLContext.getFunctionAddress("glNormal3b");
		this.glNormal3f = GLContext.getFunctionAddress("glNormal3f");
		this.glNormal3d = GLContext.getFunctionAddress("glNormal3d");
		this.glNormal3i = GLContext.getFunctionAddress("glNormal3i");
		this.glNewList = GLContext.getFunctionAddress("glNewList");
		this.glEndList = GLContext.getFunctionAddress("glEndList");
		this.glMultMatrixf = GLContext.getFunctionAddress("glMultMatrixf");
		this.glMultMatrixd = GLContext.getFunctionAddress("glMultMatrixd");
		this.glShadeModel = GLContext.getFunctionAddress("glShadeModel");
		this.glSelectBuffer = GLContext.getFunctionAddress("glSelectBuffer");
		this.glScissor = GLContext.getFunctionAddress("glScissor");
		this.glScalef = GLContext.getFunctionAddress("glScalef");
		this.glScaled = GLContext.getFunctionAddress("glScaled");
		this.glRotatef = GLContext.getFunctionAddress("glRotatef");
		this.glRotated = GLContext.getFunctionAddress("glRotated");
		this.glRenderMode = GLContext.getFunctionAddress("glRenderMode");
		this.glRectf = GLContext.getFunctionAddress("glRectf");
		this.glRectd = GLContext.getFunctionAddress("glRectd");
		this.glRecti = GLContext.getFunctionAddress("glRecti");
		this.glReadPixels = GLContext.getFunctionAddress("glReadPixels");
		this.glReadBuffer = GLContext.getFunctionAddress("glReadBuffer");
		this.glRasterPos2f = GLContext.getFunctionAddress("glRasterPos2f");
		this.glRasterPos2d = GLContext.getFunctionAddress("glRasterPos2d");
		this.glRasterPos2i = GLContext.getFunctionAddress("glRasterPos2i");
		this.glRasterPos3f = GLContext.getFunctionAddress("glRasterPos3f");
		this.glRasterPos3d = GLContext.getFunctionAddress("glRasterPos3d");
		this.glRasterPos3i = GLContext.getFunctionAddress("glRasterPos3i");
		this.glRasterPos4f = GLContext.getFunctionAddress("glRasterPos4f");
		this.glRasterPos4d = GLContext.getFunctionAddress("glRasterPos4d");
		this.glRasterPos4i = GLContext.getFunctionAddress("glRasterPos4i");
		this.glPushName = GLContext.getFunctionAddress("glPushName");
		this.glPopName = GLContext.getFunctionAddress("glPopName");
		this.glPushMatrix = GLContext.getFunctionAddress("glPushMatrix");
		this.glPopMatrix = GLContext.getFunctionAddress("glPopMatrix");
		this.glPushClientAttrib = GLContext.getFunctionAddress("glPushClientAttrib");
		this.glPopClientAttrib = GLContext.getFunctionAddress("glPopClientAttrib");
		this.glPushAttrib = GLContext.getFunctionAddress("glPushAttrib");
		this.glPopAttrib = GLContext.getFunctionAddress("glPopAttrib");
		this.glStencilFunc = GLContext.getFunctionAddress("glStencilFunc");
		this.glVertexPointer = GLContext.getFunctionAddress("glVertexPointer");
		this.glVertex2f = GLContext.getFunctionAddress("glVertex2f");
		this.glVertex2d = GLContext.getFunctionAddress("glVertex2d");
		this.glVertex2i = GLContext.getFunctionAddress("glVertex2i");
		this.glVertex3f = GLContext.getFunctionAddress("glVertex3f");
		this.glVertex3d = GLContext.getFunctionAddress("glVertex3d");
		this.glVertex3i = GLContext.getFunctionAddress("glVertex3i");
		this.glVertex4f = GLContext.getFunctionAddress("glVertex4f");
		this.glVertex4d = GLContext.getFunctionAddress("glVertex4d");
		this.glVertex4i = GLContext.getFunctionAddress("glVertex4i");
		this.glTranslatef = GLContext.getFunctionAddress("glTranslatef");
		this.glTranslated = GLContext.getFunctionAddress("glTranslated");
		this.glTexImage1D = GLContext.getFunctionAddress("glTexImage1D");
		this.glTexImage2D = GLContext.getFunctionAddress("glTexImage2D");
		this.glTexSubImage1D = GLContext.getFunctionAddress("glTexSubImage1D");
		this.glTexSubImage2D = GLContext.getFunctionAddress("glTexSubImage2D");
		this.glTexParameterf = GLContext.getFunctionAddress("glTexParameterf");
		this.glTexParameteri = GLContext.getFunctionAddress("glTexParameteri");
		this.glTexParameterfv = GLContext.getFunctionAddress("glTexParameterfv");
		this.glTexParameteriv = GLContext.getFunctionAddress("glTexParameteriv");
		this.glTexGenf = GLContext.getFunctionAddress("glTexGenf");
		this.glTexGend = GLContext.getFunctionAddress("glTexGend");
		this.glTexGenfv = GLContext.getFunctionAddress("glTexGenfv");
		this.glTexGendv = GLContext.getFunctionAddress("glTexGendv");
		this.glTexGeni = GLContext.getFunctionAddress("glTexGeni");
		this.glTexGeniv = GLContext.getFunctionAddress("glTexGeniv");
		this.glTexEnvf = GLContext.getFunctionAddress("glTexEnvf");
		this.glTexEnvi = GLContext.getFunctionAddress("glTexEnvi");
		this.glTexEnvfv = GLContext.getFunctionAddress("glTexEnvfv");
		this.glTexEnviv = GLContext.getFunctionAddress("glTexEnviv");
		this.glTexCoordPointer = GLContext.getFunctionAddress("glTexCoordPointer");
		this.glTexCoord1f = GLContext.getFunctionAddress("glTexCoord1f");
		this.glTexCoord1d = GLContext.getFunctionAddress("glTexCoord1d");
		this.glTexCoord2f = GLContext.getFunctionAddress("glTexCoord2f");
		this.glTexCoord2d = GLContext.getFunctionAddress("glTexCoord2d");
		this.glTexCoord3f = GLContext.getFunctionAddress("glTexCoord3f");
		this.glTexCoord3d = GLContext.getFunctionAddress("glTexCoord3d");
		this.glTexCoord4f = GLContext.getFunctionAddress("glTexCoord4f");
		this.glTexCoord4d = GLContext.getFunctionAddress("glTexCoord4d");
		this.glStencilOp = GLContext.getFunctionAddress("glStencilOp");
		this.glStencilMask = GLContext.getFunctionAddress("glStencilMask");
		this.glViewport = GLContext.getFunctionAddress("glViewport");
		this.glActiveTexture = GLContext.getFunctionAddress("glActiveTexture");
		this.glClientActiveTexture = GLContext.getFunctionAddress("glClientActiveTexture");
		this.glCompressedTexImage1D = GLContext.getFunctionAddress("glCompressedTexImage1D");
		this.glCompressedTexImage2D = GLContext.getFunctionAddress("glCompressedTexImage2D");
		this.glCompressedTexImage3D = GLContext.getFunctionAddress("glCompressedTexImage3D");
		this.glCompressedTexSubImage1D = GLContext.getFunctionAddress("glCompressedTexSubImage1D");
		this.glCompressedTexSubImage2D = GLContext.getFunctionAddress("glCompressedTexSubImage2D");
		this.glCompressedTexSubImage3D = GLContext.getFunctionAddress("glCompressedTexSubImage3D");
		this.glGetCompressedTexImage = GLContext.getFunctionAddress("glGetCompressedTexImage");
		this.glMultiTexCoord1f = GLContext.getFunctionAddress("glMultiTexCoord1f");
		this.glMultiTexCoord1d = GLContext.getFunctionAddress("glMultiTexCoord1d");
		this.glMultiTexCoord2f = GLContext.getFunctionAddress("glMultiTexCoord2f");
		this.glMultiTexCoord2d = GLContext.getFunctionAddress("glMultiTexCoord2d");
		this.glMultiTexCoord3f = GLContext.getFunctionAddress("glMultiTexCoord3f");
		this.glMultiTexCoord3d = GLContext.getFunctionAddress("glMultiTexCoord3d");
		this.glMultiTexCoord4f = GLContext.getFunctionAddress("glMultiTexCoord4f");
		this.glMultiTexCoord4d = GLContext.getFunctionAddress("glMultiTexCoord4d");
		this.glLoadTransposeMatrixf = GLContext.getFunctionAddress("glLoadTransposeMatrixf");
		this.glLoadTransposeMatrixd = GLContext.getFunctionAddress("glLoadTransposeMatrixd");
		this.glMultTransposeMatrixf = GLContext.getFunctionAddress("glMultTransposeMatrixf");
		this.glMultTransposeMatrixd = GLContext.getFunctionAddress("glMultTransposeMatrixd");
		this.glSampleCoverage = GLContext.getFunctionAddress("glSampleCoverage");
		GLContext.setCapabilities(this);
		Set<String> supported_extensions = new HashSet<String>(256);
		int profileMask = GLContext.getSupportedExtensions(supported_extensions);
		if ( supported_extensions.contains("OpenGL31") && !(supported_extensions.contains("GL_ARB_compatibility") || (profileMask & GL32.GL_CONTEXT_COMPATIBILITY_PROFILE_BIT) != 0) )
			forwardCompatible = true;
		if (!GL11_initNativeFunctionAddresses(forwardCompatible))
			throw new LWJGLException("GL11 not supported");
		if (supported_extensions.contains("GL_ARB_fragment_program"))
			supported_extensions.add("GL_ARB_program");
		if (supported_extensions.contains("GL_ARB_pixel_buffer_object"))
			supported_extensions.add("GL_ARB_buffer_object");
		if (supported_extensions.contains("GL_ARB_vertex_buffer_object"))
			supported_extensions.add("GL_ARB_buffer_object");
		if (supported_extensions.contains("GL_ARB_vertex_program"))
			supported_extensions.add("GL_ARB_program");
		if (supported_extensions.contains("GL_EXT_pixel_buffer_object"))
			supported_extensions.add("GL_ARB_buffer_object");
		if (supported_extensions.contains("GL_NV_fragment_program"))
			supported_extensions.add("GL_NV_program");
		if (supported_extensions.contains("GL_NV_vertex_program"))
			supported_extensions.add("GL_NV_program");
		if ((supported_extensions.contains("GL_AMD_debug_output") || supported_extensions.contains("GL_AMDX_debug_output")) && !AMD_debug_output_initNativeFunctionAddresses()) {
			remove(supported_extensions, "GL_AMDX_debug_output");
			remove(supported_extensions, "GL_AMD_debug_output");
		}
		if (supported_extensions.contains("GL_AMD_draw_buffers_blend") && !AMD_draw_buffers_blend_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_AMD_draw_buffers_blend");
		if (supported_extensions.contains("GL_AMD_interleaved_elements") && !AMD_interleaved_elements_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_AMD_interleaved_elements");
		if (supported_extensions.contains("GL_AMD_multi_draw_indirect") && !AMD_multi_draw_indirect_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_AMD_multi_draw_indirect");
		if (supported_extensions.contains("GL_AMD_name_gen_delete") && !AMD_name_gen_delete_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_AMD_name_gen_delete");
		if (supported_extensions.contains("GL_AMD_performance_monitor") && !AMD_performance_monitor_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_AMD_performance_monitor");
		if (supported_extensions.contains("GL_AMD_sample_positions") && !AMD_sample_positions_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_AMD_sample_positions");
		if (supported_extensions.contains("GL_AMD_sparse_texture") && !AMD_sparse_texture_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_AMD_sparse_texture");
		if (supported_extensions.contains("GL_AMD_stencil_operation_extended") && !AMD_stencil_operation_extended_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_AMD_stencil_operation_extended");
		if (supported_extensions.contains("GL_AMD_vertex_shader_tessellator") && !AMD_vertex_shader_tessellator_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_AMD_vertex_shader_tessellator");
		if (supported_extensions.contains("GL_APPLE_element_array") && !APPLE_element_array_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_APPLE_element_array");
		if (supported_extensions.contains("GL_APPLE_fence") && !APPLE_fence_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_APPLE_fence");
		if (supported_extensions.contains("GL_APPLE_flush_buffer_range") && !APPLE_flush_buffer_range_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_APPLE_flush_buffer_range");
		if (supported_extensions.contains("GL_APPLE_object_purgeable") && !APPLE_object_purgeable_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_APPLE_object_purgeable");
		if (supported_extensions.contains("GL_APPLE_texture_range") && !APPLE_texture_range_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_APPLE_texture_range");
		if (supported_extensions.contains("GL_APPLE_vertex_array_object") && !APPLE_vertex_array_object_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_APPLE_vertex_array_object");
		if (supported_extensions.contains("GL_APPLE_vertex_array_range") && !APPLE_vertex_array_range_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_APPLE_vertex_array_range");
		if (supported_extensions.contains("GL_APPLE_vertex_program_evaluators") && !APPLE_vertex_program_evaluators_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_APPLE_vertex_program_evaluators");
		if (supported_extensions.contains("GL_ARB_ES2_compatibility") && !ARB_ES2_compatibility_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_ES2_compatibility");
		if (supported_extensions.contains("GL_ARB_ES3_1_compatibility") && !ARB_ES3_1_compatibility_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_ES3_1_compatibility");
		if (supported_extensions.contains("GL_ARB_base_instance") && !ARB_base_instance_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_base_instance");
		if (supported_extensions.contains("GL_ARB_bindless_texture") && !ARB_bindless_texture_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_bindless_texture");
		if (supported_extensions.contains("GL_ARB_blend_func_extended") && !ARB_blend_func_extended_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_blend_func_extended");
		if (supported_extensions.contains("GL_ARB_buffer_object") && !ARB_buffer_object_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_buffer_object");
		if (supported_extensions.contains("GL_ARB_buffer_storage") && !ARB_buffer_storage_initNativeFunctionAddresses(supported_extensions))
			remove(supported_extensions, "GL_ARB_buffer_storage");
		if (supported_extensions.contains("GL_ARB_cl_event") && !ARB_cl_event_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_cl_event");
		if (supported_extensions.contains("GL_ARB_clear_buffer_object") && !ARB_clear_buffer_object_initNativeFunctionAddresses(supported_extensions))
			remove(supported_extensions, "GL_ARB_clear_buffer_object");
		if (supported_extensions.contains("GL_ARB_clear_texture") && !ARB_clear_texture_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_clear_texture");
		if (supported_extensions.contains("GL_ARB_clip_control") && !ARB_clip_control_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_clip_control");
		if (supported_extensions.contains("GL_ARB_color_buffer_float") && !ARB_color_buffer_float_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_color_buffer_float");
		if (supported_extensions.contains("GL_ARB_compute_shader") && !ARB_compute_shader_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_compute_shader");
		if (supported_extensions.contains("GL_ARB_compute_variable_group_size") && !ARB_compute_variable_group_size_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_compute_variable_group_size");
		if (supported_extensions.contains("GL_ARB_copy_buffer") && !ARB_copy_buffer_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_copy_buffer");
		if (supported_extensions.contains("GL_ARB_copy_image") && !ARB_copy_image_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_copy_image");
		if (supported_extensions.contains("GL_ARB_debug_output") && !ARB_debug_output_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_debug_output");
		if (supported_extensions.contains("GL_ARB_direct_state_access") && !ARB_direct_state_access_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_direct_state_access");
		if (supported_extensions.contains("GL_ARB_draw_buffers") && !ARB_draw_buffers_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_draw_buffers");
		if (supported_extensions.contains("GL_ARB_draw_buffers_blend") && !ARB_draw_buffers_blend_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_draw_buffers_blend");
		if (supported_extensions.contains("GL_ARB_draw_elements_base_vertex") && !ARB_draw_elements_base_vertex_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_draw_elements_base_vertex");
		if (supported_extensions.contains("GL_ARB_draw_indirect") && !ARB_draw_indirect_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_draw_indirect");
		if (supported_extensions.contains("GL_ARB_draw_instanced") && !ARB_draw_instanced_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_draw_instanced");
		if (supported_extensions.contains("GL_ARB_framebuffer_no_attachments") && !ARB_framebuffer_no_attachments_initNativeFunctionAddresses(supported_extensions))
			remove(supported_extensions, "GL_ARB_framebuffer_no_attachments");
		if (supported_extensions.contains("GL_ARB_framebuffer_object") && !ARB_framebuffer_object_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_framebuffer_object");
		if (supported_extensions.contains("GL_ARB_geometry_shader4") && !ARB_geometry_shader4_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_geometry_shader4");
		if (supported_extensions.contains("GL_ARB_get_program_binary") && !ARB_get_program_binary_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_get_program_binary");
		if (supported_extensions.contains("GL_ARB_get_texture_sub_image") && !ARB_get_texture_sub_image_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_get_texture_sub_image");
		if (supported_extensions.contains("GL_ARB_gpu_shader_fp64") && !ARB_gpu_shader_fp64_initNativeFunctionAddresses(supported_extensions))
			remove(supported_extensions, "GL_ARB_gpu_shader_fp64");
		if (supported_extensions.contains("GL_ARB_imaging") && !ARB_imaging_initNativeFunctionAddresses(forwardCompatible))
			remove(supported_extensions, "GL_ARB_imaging");
		if (supported_extensions.contains("GL_ARB_indirect_parameters") && !ARB_indirect_parameters_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_indirect_parameters");
		if (supported_extensions.contains("GL_ARB_instanced_arrays") && !ARB_instanced_arrays_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_instanced_arrays");
		if (supported_extensions.contains("GL_ARB_internalformat_query") && !ARB_internalformat_query_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_internalformat_query");
		if (supported_extensions.contains("GL_ARB_internalformat_query2") && !ARB_internalformat_query2_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_internalformat_query2");
		if (supported_extensions.contains("GL_ARB_invalidate_subdata") && !ARB_invalidate_subdata_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_invalidate_subdata");
		if (supported_extensions.contains("GL_ARB_map_buffer_range") && !ARB_map_buffer_range_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_map_buffer_range");
		if (supported_extensions.contains("GL_ARB_matrix_palette") && !ARB_matrix_palette_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_matrix_palette");
		if (supported_extensions.contains("GL_ARB_multi_bind") && !ARB_multi_bind_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_multi_bind");
		if (supported_extensions.contains("GL_ARB_multi_draw_indirect") && !ARB_multi_draw_indirect_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_multi_draw_indirect");
		if (supported_extensions.contains("GL_ARB_multisample") && !ARB_multisample_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_multisample");
		if (supported_extensions.contains("GL_ARB_multitexture") && !ARB_multitexture_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_multitexture");
		if (supported_extensions.contains("GL_ARB_occlusion_query") && !ARB_occlusion_query_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_occlusion_query");
		if (supported_extensions.contains("GL_ARB_point_parameters") && !ARB_point_parameters_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_point_parameters");
		if (supported_extensions.contains("GL_ARB_program") && !ARB_program_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_program");
		if (supported_extensions.contains("GL_ARB_program_interface_query") && !ARB_program_interface_query_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_program_interface_query");
		if (supported_extensions.contains("GL_ARB_provoking_vertex") && !ARB_provoking_vertex_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_provoking_vertex");
		if (supported_extensions.contains("GL_ARB_robustness") && !ARB_robustness_initNativeFunctionAddresses(forwardCompatible,supported_extensions))
			remove(supported_extensions, "GL_ARB_robustness");
		if (supported_extensions.contains("GL_ARB_sample_shading") && !ARB_sample_shading_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_sample_shading");
		if (supported_extensions.contains("GL_ARB_sampler_objects") && !ARB_sampler_objects_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_sampler_objects");
		if (supported_extensions.contains("GL_ARB_separate_shader_objects") && !ARB_separate_shader_objects_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_separate_shader_objects");
		if (supported_extensions.contains("GL_ARB_shader_atomic_counters") && !ARB_shader_atomic_counters_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_shader_atomic_counters");
		if (supported_extensions.contains("GL_ARB_shader_image_load_store") && !ARB_shader_image_load_store_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_shader_image_load_store");
		if (supported_extensions.contains("GL_ARB_shader_objects") && !ARB_shader_objects_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_shader_objects");
		if (supported_extensions.contains("GL_ARB_shader_storage_buffer_object") && !ARB_shader_storage_buffer_object_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_shader_storage_buffer_object");
		if (supported_extensions.contains("GL_ARB_shader_subroutine") && !ARB_shader_subroutine_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_shader_subroutine");
		if (supported_extensions.contains("GL_ARB_shading_language_include") && !ARB_shading_language_include_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_shading_language_include");
		if (supported_extensions.contains("GL_ARB_sparse_buffer") && !ARB_sparse_buffer_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_sparse_buffer");
		if (supported_extensions.contains("GL_ARB_sparse_texture") && !ARB_sparse_texture_initNativeFunctionAddresses(supported_extensions))
			remove(supported_extensions, "GL_ARB_sparse_texture");
		if (supported_extensions.contains("GL_ARB_sync") && !ARB_sync_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_sync");
		if (supported_extensions.contains("GL_ARB_tessellation_shader") && !ARB_tessellation_shader_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_tessellation_shader");
		if (supported_extensions.contains("GL_ARB_texture_barrier") && !ARB_texture_barrier_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_texture_barrier");
		if (supported_extensions.contains("GL_ARB_texture_buffer_object") && !ARB_texture_buffer_object_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_texture_buffer_object");
		if (supported_extensions.contains("GL_ARB_texture_buffer_range") && !ARB_texture_buffer_range_initNativeFunctionAddresses(supported_extensions))
			remove(supported_extensions, "GL_ARB_texture_buffer_range");
		if (supported_extensions.contains("GL_ARB_texture_compression") && !ARB_texture_compression_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_texture_compression");
		if (supported_extensions.contains("GL_ARB_texture_multisample") && !ARB_texture_multisample_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_texture_multisample");
		if ((supported_extensions.contains("GL_ARB_texture_storage") || supported_extensions.contains("GL_EXT_texture_storage")) && !ARB_texture_storage_initNativeFunctionAddresses(supported_extensions)) {
			remove(supported_extensions, "GL_EXT_texture_storage");
			remove(supported_extensions, "GL_ARB_texture_storage");
		}
		if (supported_extensions.contains("GL_ARB_texture_storage_multisample") && !ARB_texture_storage_multisample_initNativeFunctionAddresses(supported_extensions))
			remove(supported_extensions, "GL_ARB_texture_storage_multisample");
		if (supported_extensions.contains("GL_ARB_texture_view") && !ARB_texture_view_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_texture_view");
		if (supported_extensions.contains("GL_ARB_timer_query") && !ARB_timer_query_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_timer_query");
		if (supported_extensions.contains("GL_ARB_transform_feedback2") && !ARB_transform_feedback2_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_transform_feedback2");
		if (supported_extensions.contains("GL_ARB_transform_feedback3") && !ARB_transform_feedback3_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_transform_feedback3");
		if (supported_extensions.contains("GL_ARB_transform_feedback_instanced") && !ARB_transform_feedback_instanced_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_transform_feedback_instanced");
		if (supported_extensions.contains("GL_ARB_transpose_matrix") && !ARB_transpose_matrix_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_transpose_matrix");
		if (supported_extensions.contains("GL_ARB_uniform_buffer_object") && !ARB_uniform_buffer_object_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_uniform_buffer_object");
		if (supported_extensions.contains("GL_ARB_vertex_array_object") && !ARB_vertex_array_object_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_vertex_array_object");
		if (supported_extensions.contains("GL_ARB_vertex_attrib_64bit") && !ARB_vertex_attrib_64bit_initNativeFunctionAddresses(supported_extensions))
			remove(supported_extensions, "GL_ARB_vertex_attrib_64bit");
		if (supported_extensions.contains("GL_ARB_vertex_attrib_binding") && !ARB_vertex_attrib_binding_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_vertex_attrib_binding");
		if (supported_extensions.contains("GL_ARB_vertex_blend") && !ARB_vertex_blend_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_vertex_blend");
		if (supported_extensions.contains("GL_ARB_vertex_program") && !ARB_vertex_program_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_vertex_program");
		if (supported_extensions.contains("GL_ARB_vertex_shader") && !ARB_vertex_shader_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_vertex_shader");
		if (supported_extensions.contains("GL_ARB_vertex_type_2_10_10_10_rev") && !ARB_vertex_type_2_10_10_10_rev_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_vertex_type_2_10_10_10_rev");
		if (supported_extensions.contains("GL_ARB_viewport_array") && !ARB_viewport_array_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ARB_viewport_array");
		if (supported_extensions.contains("GL_ARB_window_pos") && !ARB_window_pos_initNativeFunctionAddresses(forwardCompatible))
			remove(supported_extensions, "GL_ARB_window_pos");
		if (supported_extensions.contains("GL_ATI_draw_buffers") && !ATI_draw_buffers_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ATI_draw_buffers");
		if (supported_extensions.contains("GL_ATI_element_array") && !ATI_element_array_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ATI_element_array");
		if (supported_extensions.contains("GL_ATI_envmap_bumpmap") && !ATI_envmap_bumpmap_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ATI_envmap_bumpmap");
		if (supported_extensions.contains("GL_ATI_fragment_shader") && !ATI_fragment_shader_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ATI_fragment_shader");
		if (supported_extensions.contains("GL_ATI_map_object_buffer") && !ATI_map_object_buffer_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ATI_map_object_buffer");
		if (supported_extensions.contains("GL_ATI_pn_triangles") && !ATI_pn_triangles_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ATI_pn_triangles");
		if (supported_extensions.contains("GL_ATI_separate_stencil") && !ATI_separate_stencil_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ATI_separate_stencil");
		if (supported_extensions.contains("GL_ATI_vertex_array_object") && !ATI_vertex_array_object_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ATI_vertex_array_object");
		if (supported_extensions.contains("GL_ATI_vertex_attrib_array_object") && !ATI_vertex_attrib_array_object_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ATI_vertex_attrib_array_object");
		if (supported_extensions.contains("GL_ATI_vertex_streams") && !ATI_vertex_streams_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_ATI_vertex_streams");
		if (supported_extensions.contains("GL_EXT_bindable_uniform") && !EXT_bindable_uniform_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_bindable_uniform");
		if (supported_extensions.contains("GL_EXT_blend_color") && !EXT_blend_color_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_blend_color");
		if (supported_extensions.contains("GL_EXT_blend_equation_separate") && !EXT_blend_equation_separate_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_blend_equation_separate");
		if (supported_extensions.contains("GL_EXT_blend_func_separate") && !EXT_blend_func_separate_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_blend_func_separate");
		if (supported_extensions.contains("GL_EXT_blend_minmax") && !EXT_blend_minmax_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_blend_minmax");
		if (supported_extensions.contains("GL_EXT_compiled_vertex_array") && !EXT_compiled_vertex_array_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_compiled_vertex_array");
		if (supported_extensions.contains("GL_EXT_depth_bounds_test") && !EXT_depth_bounds_test_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_depth_bounds_test");
		supported_extensions.add("GL_EXT_direct_state_access");
		if (supported_extensions.contains("GL_EXT_direct_state_access") && !EXT_direct_state_access_initNativeFunctionAddresses(forwardCompatible,supported_extensions))
			remove(supported_extensions, "GL_EXT_direct_state_access");
		if (supported_extensions.contains("GL_EXT_draw_buffers2") && !EXT_draw_buffers2_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_draw_buffers2");
		if (supported_extensions.contains("GL_EXT_draw_instanced") && !EXT_draw_instanced_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_draw_instanced");
		if (supported_extensions.contains("GL_EXT_draw_range_elements") && !EXT_draw_range_elements_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_draw_range_elements");
		if (supported_extensions.contains("GL_EXT_fog_coord") && !EXT_fog_coord_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_fog_coord");
		if (supported_extensions.contains("GL_EXT_framebuffer_blit") && !EXT_framebuffer_blit_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_framebuffer_blit");
		if (supported_extensions.contains("GL_EXT_framebuffer_multisample") && !EXT_framebuffer_multisample_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_framebuffer_multisample");
		if (supported_extensions.contains("GL_EXT_framebuffer_object") && !EXT_framebuffer_object_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_framebuffer_object");
		if (supported_extensions.contains("GL_EXT_geometry_shader4") && !EXT_geometry_shader4_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_geometry_shader4");
		if (supported_extensions.contains("GL_EXT_gpu_program_parameters") && !EXT_gpu_program_parameters_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_gpu_program_parameters");
		if (supported_extensions.contains("GL_EXT_gpu_shader4") && !EXT_gpu_shader4_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_gpu_shader4");
		if (supported_extensions.contains("GL_EXT_multi_draw_arrays") && !EXT_multi_draw_arrays_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_multi_draw_arrays");
		if (supported_extensions.contains("GL_EXT_paletted_texture") && !EXT_paletted_texture_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_paletted_texture");
		if (supported_extensions.contains("GL_EXT_point_parameters") && !EXT_point_parameters_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_point_parameters");
		if (supported_extensions.contains("GL_EXT_provoking_vertex") && !EXT_provoking_vertex_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_provoking_vertex");
		if (supported_extensions.contains("GL_EXT_secondary_color") && !EXT_secondary_color_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_secondary_color");
		if (supported_extensions.contains("GL_EXT_separate_shader_objects") && !EXT_separate_shader_objects_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_separate_shader_objects");
		if (supported_extensions.contains("GL_EXT_shader_image_load_store") && !EXT_shader_image_load_store_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_shader_image_load_store");
		if (supported_extensions.contains("GL_EXT_stencil_clear_tag") && !EXT_stencil_clear_tag_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_stencil_clear_tag");
		if (supported_extensions.contains("GL_EXT_stencil_two_side") && !EXT_stencil_two_side_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_stencil_two_side");
		if (supported_extensions.contains("GL_EXT_texture_array") && !EXT_texture_array_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_texture_array");
		if (supported_extensions.contains("GL_EXT_texture_buffer_object") && !EXT_texture_buffer_object_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_texture_buffer_object");
		if (supported_extensions.contains("GL_EXT_texture_integer") && !EXT_texture_integer_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_texture_integer");
		if (supported_extensions.contains("GL_EXT_timer_query") && !EXT_timer_query_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_timer_query");
		if (supported_extensions.contains("GL_EXT_transform_feedback") && !EXT_transform_feedback_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_transform_feedback");
		if (supported_extensions.contains("GL_EXT_vertex_attrib_64bit") && !EXT_vertex_attrib_64bit_initNativeFunctionAddresses(supported_extensions))
			remove(supported_extensions, "GL_EXT_vertex_attrib_64bit");
		if (supported_extensions.contains("GL_EXT_vertex_shader") && !EXT_vertex_shader_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_vertex_shader");
		if (supported_extensions.contains("GL_EXT_vertex_weighting") && !EXT_vertex_weighting_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_EXT_vertex_weighting");
		if (supported_extensions.contains("OpenGL12") && !GL12_initNativeFunctionAddresses())
			remove(supported_extensions, "OpenGL12");
		if (supported_extensions.contains("OpenGL13") && !GL13_initNativeFunctionAddresses(forwardCompatible))
			remove(supported_extensions, "OpenGL13");
		if (supported_extensions.contains("OpenGL14") && !GL14_initNativeFunctionAddresses(forwardCompatible))
			remove(supported_extensions, "OpenGL14");
		if (supported_extensions.contains("OpenGL15") && !GL15_initNativeFunctionAddresses())
			remove(supported_extensions, "OpenGL15");
		if (supported_extensions.contains("OpenGL20") && !GL20_initNativeFunctionAddresses())
			remove(supported_extensions, "OpenGL20");
		if (supported_extensions.contains("OpenGL21") && !GL21_initNativeFunctionAddresses())
			remove(supported_extensions, "OpenGL21");
		if (supported_extensions.contains("OpenGL30") && !GL30_initNativeFunctionAddresses())
			remove(supported_extensions, "OpenGL30");
		if (supported_extensions.contains("OpenGL31") && !GL31_initNativeFunctionAddresses())
			remove(supported_extensions, "OpenGL31");
		if (supported_extensions.contains("OpenGL32") && !GL32_initNativeFunctionAddresses())
			remove(supported_extensions, "OpenGL32");
		if (supported_extensions.contains("OpenGL33") && !GL33_initNativeFunctionAddresses(forwardCompatible))
			remove(supported_extensions, "OpenGL33");
		if (supported_extensions.contains("OpenGL40") && !GL40_initNativeFunctionAddresses())
			remove(supported_extensions, "OpenGL40");
		if (supported_extensions.contains("OpenGL41") && !GL41_initNativeFunctionAddresses())
			remove(supported_extensions, "OpenGL41");
		if (supported_extensions.contains("OpenGL42") && !GL42_initNativeFunctionAddresses())
			remove(supported_extensions, "OpenGL42");
		if (supported_extensions.contains("OpenGL43") && !GL43_initNativeFunctionAddresses())
			remove(supported_extensions, "OpenGL43");
		if (supported_extensions.contains("OpenGL44") && !GL44_initNativeFunctionAddresses())
			remove(supported_extensions, "OpenGL44");
		if (supported_extensions.contains("OpenGL45") && !GL45_initNativeFunctionAddresses())
			remove(supported_extensions, "OpenGL45");
		if (supported_extensions.contains("GL_GREMEDY_frame_terminator") && !GREMEDY_frame_terminator_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_GREMEDY_frame_terminator");
		if (supported_extensions.contains("GL_GREMEDY_string_marker") && !GREMEDY_string_marker_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_GREMEDY_string_marker");
		if (supported_extensions.contains("GL_INTEL_map_texture") && !INTEL_map_texture_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_INTEL_map_texture");
		if (supported_extensions.contains("GL_KHR_debug") && !KHR_debug_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_KHR_debug");
		if (supported_extensions.contains("GL_KHR_robustness") && !KHR_robustness_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_KHR_robustness");
		if (supported_extensions.contains("GL_NV_bindless_multi_draw_indirect") && !NV_bindless_multi_draw_indirect_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_bindless_multi_draw_indirect");
		if (supported_extensions.contains("GL_NV_bindless_texture") && !NV_bindless_texture_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_bindless_texture");
		if (supported_extensions.contains("GL_NV_blend_equation_advanced") && !NV_blend_equation_advanced_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_blend_equation_advanced");
		if (supported_extensions.contains("GL_NV_conditional_render") && !NV_conditional_render_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_conditional_render");
		if (supported_extensions.contains("GL_NV_copy_image") && !NV_copy_image_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_copy_image");
		if (supported_extensions.contains("GL_NV_depth_buffer_float") && !NV_depth_buffer_float_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_depth_buffer_float");
		if (supported_extensions.contains("GL_NV_draw_texture") && !NV_draw_texture_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_draw_texture");
		if (supported_extensions.contains("GL_NV_evaluators") && !NV_evaluators_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_evaluators");
		if (supported_extensions.contains("GL_NV_explicit_multisample") && !NV_explicit_multisample_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_explicit_multisample");
		if (supported_extensions.contains("GL_NV_fence") && !NV_fence_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_fence");
		if (supported_extensions.contains("GL_NV_fragment_program") && !NV_fragment_program_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_fragment_program");
		if (supported_extensions.contains("GL_NV_framebuffer_multisample_coverage") && !NV_framebuffer_multisample_coverage_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_framebuffer_multisample_coverage");
		if (supported_extensions.contains("GL_NV_geometry_program4") && !NV_geometry_program4_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_geometry_program4");
		if (supported_extensions.contains("GL_NV_gpu_program4") && !NV_gpu_program4_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_gpu_program4");
		if (supported_extensions.contains("GL_NV_gpu_shader5") && !NV_gpu_shader5_initNativeFunctionAddresses(supported_extensions))
			remove(supported_extensions, "GL_NV_gpu_shader5");
		if (supported_extensions.contains("GL_NV_half_float") && !NV_half_float_initNativeFunctionAddresses(supported_extensions))
			remove(supported_extensions, "GL_NV_half_float");
		if (supported_extensions.contains("GL_NV_occlusion_query") && !NV_occlusion_query_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_occlusion_query");
		if (supported_extensions.contains("GL_NV_parameter_buffer_object") && !NV_parameter_buffer_object_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_parameter_buffer_object");
		if (supported_extensions.contains("GL_NV_path_rendering") && !NV_path_rendering_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_path_rendering");
		if (supported_extensions.contains("GL_NV_pixel_data_range") && !NV_pixel_data_range_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_pixel_data_range");
		if (supported_extensions.contains("GL_NV_point_sprite") && !NV_point_sprite_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_point_sprite");
		if (supported_extensions.contains("GL_NV_present_video") && !NV_present_video_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_present_video");
		supported_extensions.add("GL_NV_primitive_restart");
		if (supported_extensions.contains("GL_NV_primitive_restart") && !NV_primitive_restart_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_primitive_restart");
		if (supported_extensions.contains("GL_NV_program") && !NV_program_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_program");
		if (supported_extensions.contains("GL_NV_register_combiners") && !NV_register_combiners_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_register_combiners");
		if (supported_extensions.contains("GL_NV_register_combiners2") && !NV_register_combiners2_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_register_combiners2");
		if (supported_extensions.contains("GL_NV_shader_buffer_load") && !NV_shader_buffer_load_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_shader_buffer_load");
		if (supported_extensions.contains("GL_NV_texture_barrier") && !NV_texture_barrier_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_texture_barrier");
		if (supported_extensions.contains("GL_NV_texture_multisample") && !NV_texture_multisample_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_texture_multisample");
		if (supported_extensions.contains("GL_NV_transform_feedback") && !NV_transform_feedback_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_transform_feedback");
		if (supported_extensions.contains("GL_NV_transform_feedback2") && !NV_transform_feedback2_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_transform_feedback2");
		if (supported_extensions.contains("GL_NV_vertex_array_range") && !NV_vertex_array_range_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_vertex_array_range");
		if (supported_extensions.contains("GL_NV_vertex_attrib_integer_64bit") && !NV_vertex_attrib_integer_64bit_initNativeFunctionAddresses(supported_extensions))
			remove(supported_extensions, "GL_NV_vertex_attrib_integer_64bit");
		if (supported_extensions.contains("GL_NV_vertex_buffer_unified_memory") && !NV_vertex_buffer_unified_memory_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_vertex_buffer_unified_memory");
		if (supported_extensions.contains("GL_NV_vertex_program") && !NV_vertex_program_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_vertex_program");
		if (supported_extensions.contains("GL_NV_video_capture") && !NV_video_capture_initNativeFunctionAddresses())
			remove(supported_extensions, "GL_NV_video_capture");
		return supported_extensions;
	}

	static void unloadAllStubs() {
	}

	ContextCapabilities(boolean forwardCompatible) throws LWJGLException {
		Set<String> supported_extensions = initAllStubs(forwardCompatible);
		this.GL_AMD_blend_minmax_factor = supported_extensions.contains("GL_AMD_blend_minmax_factor");
		this.GL_AMD_conservative_depth = supported_extensions.contains("GL_AMD_conservative_depth");
		this.GL_AMD_debug_output = supported_extensions.contains("GL_AMD_debug_output")
			|| supported_extensions.contains("GL_AMDX_debug_output");
		this.GL_AMD_depth_clamp_separate = supported_extensions.contains("GL_AMD_depth_clamp_separate");
		this.GL_AMD_draw_buffers_blend = supported_extensions.contains("GL_AMD_draw_buffers_blend");
		this.GL_AMD_interleaved_elements = supported_extensions.contains("GL_AMD_interleaved_elements");
		this.GL_AMD_multi_draw_indirect = supported_extensions.contains("GL_AMD_multi_draw_indirect");
		this.GL_AMD_name_gen_delete = supported_extensions.contains("GL_AMD_name_gen_delete");
		this.GL_AMD_performance_monitor = supported_extensions.contains("GL_AMD_performance_monitor");
		this.GL_AMD_pinned_memory = supported_extensions.contains("GL_AMD_pinned_memory");
		this.GL_AMD_query_buffer_object = supported_extensions.contains("GL_AMD_query_buffer_object");
		this.GL_AMD_sample_positions = supported_extensions.contains("GL_AMD_sample_positions");
		this.GL_AMD_seamless_cubemap_per_texture = supported_extensions.contains("GL_AMD_seamless_cubemap_per_texture");
		this.GL_AMD_shader_atomic_counter_ops = supported_extensions.contains("GL_AMD_shader_atomic_counter_ops");
		this.GL_AMD_shader_stencil_export = supported_extensions.contains("GL_AMD_shader_stencil_export");
		this.GL_AMD_shader_trinary_minmax = supported_extensions.contains("GL_AMD_shader_trinary_minmax");
		this.GL_AMD_sparse_texture = supported_extensions.contains("GL_AMD_sparse_texture");
		this.GL_AMD_stencil_operation_extended = supported_extensions.contains("GL_AMD_stencil_operation_extended");
		this.GL_AMD_texture_texture4 = supported_extensions.contains("GL_AMD_texture_texture4");
		this.GL_AMD_transform_feedback3_lines_triangles = supported_extensions.contains("GL_AMD_transform_feedback3_lines_triangles");
		this.GL_AMD_vertex_shader_layer = supported_extensions.contains("GL_AMD_vertex_shader_layer");
		this.GL_AMD_vertex_shader_tessellator = supported_extensions.contains("GL_AMD_vertex_shader_tessellator");
		this.GL_AMD_vertex_shader_viewport_index = supported_extensions.contains("GL_AMD_vertex_shader_viewport_index");
		this.GL_APPLE_aux_depth_stencil = supported_extensions.contains("GL_APPLE_aux_depth_stencil");
		this.GL_APPLE_client_storage = supported_extensions.contains("GL_APPLE_client_storage");
		this.GL_APPLE_element_array = supported_extensions.contains("GL_APPLE_element_array");
		this.GL_APPLE_fence = supported_extensions.contains("GL_APPLE_fence");
		this.GL_APPLE_float_pixels = supported_extensions.contains("GL_APPLE_float_pixels");
		this.GL_APPLE_flush_buffer_range = supported_extensions.contains("GL_APPLE_flush_buffer_range");
		this.GL_APPLE_object_purgeable = supported_extensions.contains("GL_APPLE_object_purgeable");
		this.GL_APPLE_packed_pixels = supported_extensions.contains("GL_APPLE_packed_pixels");
		this.GL_APPLE_rgb_422 = supported_extensions.contains("GL_APPLE_rgb_422");
		this.GL_APPLE_row_bytes = supported_extensions.contains("GL_APPLE_row_bytes");
		this.GL_APPLE_texture_range = supported_extensions.contains("GL_APPLE_texture_range");
		this.GL_APPLE_vertex_array_object = supported_extensions.contains("GL_APPLE_vertex_array_object");
		this.GL_APPLE_vertex_array_range = supported_extensions.contains("GL_APPLE_vertex_array_range");
		this.GL_APPLE_vertex_program_evaluators = supported_extensions.contains("GL_APPLE_vertex_program_evaluators");
		this.GL_APPLE_ycbcr_422 = supported_extensions.contains("GL_APPLE_ycbcr_422");
		this.GL_ARB_ES2_compatibility = supported_extensions.contains("GL_ARB_ES2_compatibility");
		this.GL_ARB_ES3_1_compatibility = supported_extensions.contains("GL_ARB_ES3_1_compatibility");
		this.GL_ARB_ES3_compatibility = supported_extensions.contains("GL_ARB_ES3_compatibility");
		this.GL_ARB_arrays_of_arrays = supported_extensions.contains("GL_ARB_arrays_of_arrays");
		this.GL_ARB_base_instance = supported_extensions.contains("GL_ARB_base_instance");
		this.GL_ARB_bindless_texture = supported_extensions.contains("GL_ARB_bindless_texture");
		this.GL_ARB_blend_func_extended = supported_extensions.contains("GL_ARB_blend_func_extended");
		this.GL_ARB_buffer_storage = supported_extensions.contains("GL_ARB_buffer_storage");
		this.GL_ARB_cl_event = supported_extensions.contains("GL_ARB_cl_event");
		this.GL_ARB_clear_buffer_object = supported_extensions.contains("GL_ARB_clear_buffer_object");
		this.GL_ARB_clear_texture = supported_extensions.contains("GL_ARB_clear_texture");
		this.GL_ARB_clip_control = supported_extensions.contains("GL_ARB_clip_control");
		this.GL_ARB_color_buffer_float = supported_extensions.contains("GL_ARB_color_buffer_float");
		this.GL_ARB_compatibility = supported_extensions.contains("GL_ARB_compatibility");
		this.GL_ARB_compressed_texture_pixel_storage = supported_extensions.contains("GL_ARB_compressed_texture_pixel_storage");
		this.GL_ARB_compute_shader = supported_extensions.contains("GL_ARB_compute_shader");
		this.GL_ARB_compute_variable_group_size = supported_extensions.contains("GL_ARB_compute_variable_group_size");
		this.GL_ARB_conditional_render_inverted = supported_extensions.contains("GL_ARB_conditional_render_inverted");
		this.GL_ARB_conservative_depth = supported_extensions.contains("GL_ARB_conservative_depth");
		this.GL_ARB_copy_buffer = supported_extensions.contains("GL_ARB_copy_buffer");
		this.GL_ARB_copy_image = supported_extensions.contains("GL_ARB_copy_image");
		this.GL_ARB_cull_distance = supported_extensions.contains("GL_ARB_cull_distance");
		this.GL_ARB_debug_output = supported_extensions.contains("GL_ARB_debug_output");
		this.GL_ARB_depth_buffer_float = supported_extensions.contains("GL_ARB_depth_buffer_float");
		this.GL_ARB_depth_clamp = supported_extensions.contains("GL_ARB_depth_clamp");
		this.GL_ARB_depth_texture = supported_extensions.contains("GL_ARB_depth_texture");
		this.GL_ARB_derivative_control = supported_extensions.contains("GL_ARB_derivative_control");
		this.GL_ARB_direct_state_access = supported_extensions.contains("GL_ARB_direct_state_access");
		this.GL_ARB_draw_buffers = supported_extensions.contains("GL_ARB_draw_buffers");
		this.GL_ARB_draw_buffers_blend = supported_extensions.contains("GL_ARB_draw_buffers_blend");
		this.GL_ARB_draw_elements_base_vertex = supported_extensions.contains("GL_ARB_draw_elements_base_vertex");
		this.GL_ARB_draw_indirect = supported_extensions.contains("GL_ARB_draw_indirect");
		this.GL_ARB_draw_instanced = supported_extensions.contains("GL_ARB_draw_instanced");
		this.GL_ARB_enhanced_layouts = supported_extensions.contains("GL_ARB_enhanced_layouts");
		this.GL_ARB_explicit_attrib_location = supported_extensions.contains("GL_ARB_explicit_attrib_location");
		this.GL_ARB_explicit_uniform_location = supported_extensions.contains("GL_ARB_explicit_uniform_location");
		this.GL_ARB_fragment_coord_conventions = supported_extensions.contains("GL_ARB_fragment_coord_conventions");
		this.GL_ARB_fragment_layer_viewport = supported_extensions.contains("GL_ARB_fragment_layer_viewport");
		this.GL_ARB_fragment_program = supported_extensions.contains("GL_ARB_fragment_program")
			&& supported_extensions.contains("GL_ARB_program");
		this.GL_ARB_fragment_program_shadow = supported_extensions.contains("GL_ARB_fragment_program_shadow");
		this.GL_ARB_fragment_shader = supported_extensions.contains("GL_ARB_fragment_shader");
		this.GL_ARB_framebuffer_no_attachments = supported_extensions.contains("GL_ARB_framebuffer_no_attachments");
		this.GL_ARB_framebuffer_object = supported_extensions.contains("GL_ARB_framebuffer_object");
		this.GL_ARB_framebuffer_sRGB = supported_extensions.contains("GL_ARB_framebuffer_sRGB");
		this.GL_ARB_geometry_shader4 = supported_extensions.contains("GL_ARB_geometry_shader4");
		this.GL_ARB_get_program_binary = supported_extensions.contains("GL_ARB_get_program_binary");
		this.GL_ARB_get_texture_sub_image = supported_extensions.contains("GL_ARB_get_texture_sub_image");
		this.GL_ARB_gpu_shader5 = supported_extensions.contains("GL_ARB_gpu_shader5");
		this.GL_ARB_gpu_shader_fp64 = supported_extensions.contains("GL_ARB_gpu_shader_fp64");
		this.GL_ARB_half_float_pixel = supported_extensions.contains("GL_ARB_half_float_pixel");
		this.GL_ARB_half_float_vertex = supported_extensions.contains("GL_ARB_half_float_vertex");
		this.GL_ARB_imaging = supported_extensions.contains("GL_ARB_imaging");
		this.GL_ARB_indirect_parameters = supported_extensions.contains("GL_ARB_indirect_parameters");
		this.GL_ARB_instanced_arrays = supported_extensions.contains("GL_ARB_instanced_arrays");
		this.GL_ARB_internalformat_query = supported_extensions.contains("GL_ARB_internalformat_query");
		this.GL_ARB_internalformat_query2 = supported_extensions.contains("GL_ARB_internalformat_query2");
		this.GL_ARB_invalidate_subdata = supported_extensions.contains("GL_ARB_invalidate_subdata");
		this.GL_ARB_map_buffer_alignment = supported_extensions.contains("GL_ARB_map_buffer_alignment");
		this.GL_ARB_map_buffer_range = supported_extensions.contains("GL_ARB_map_buffer_range");
		this.GL_ARB_matrix_palette = supported_extensions.contains("GL_ARB_matrix_palette");
		this.GL_ARB_multi_bind = supported_extensions.contains("GL_ARB_multi_bind");
		this.GL_ARB_multi_draw_indirect = supported_extensions.contains("GL_ARB_multi_draw_indirect");
		this.GL_ARB_multisample = supported_extensions.contains("GL_ARB_multisample");
		this.GL_ARB_multitexture = supported_extensions.contains("GL_ARB_multitexture");
		this.GL_ARB_occlusion_query = supported_extensions.contains("GL_ARB_occlusion_query");
		this.GL_ARB_occlusion_query2 = supported_extensions.contains("GL_ARB_occlusion_query2");
		this.GL_ARB_pipeline_statistics_query = supported_extensions.contains("GL_ARB_pipeline_statistics_query");
		this.GL_ARB_pixel_buffer_object = supported_extensions.contains("GL_ARB_pixel_buffer_object")
			&& supported_extensions.contains("GL_ARB_buffer_object");
		this.GL_ARB_point_parameters = supported_extensions.contains("GL_ARB_point_parameters");
		this.GL_ARB_point_sprite = supported_extensions.contains("GL_ARB_point_sprite");
		this.GL_ARB_program_interface_query = supported_extensions.contains("GL_ARB_program_interface_query");
		this.GL_ARB_provoking_vertex = supported_extensions.contains("GL_ARB_provoking_vertex");
		this.GL_ARB_query_buffer_object = supported_extensions.contains("GL_ARB_query_buffer_object");
		this.GL_ARB_robust_buffer_access_behavior = supported_extensions.contains("GL_ARB_robust_buffer_access_behavior");
		this.GL_ARB_robustness = supported_extensions.contains("GL_ARB_robustness");
		this.GL_ARB_robustness_isolation = supported_extensions.contains("GL_ARB_robustness_isolation");
		this.GL_ARB_sample_shading = supported_extensions.contains("GL_ARB_sample_shading");
		this.GL_ARB_sampler_objects = supported_extensions.contains("GL_ARB_sampler_objects");
		this.GL_ARB_seamless_cube_map = supported_extensions.contains("GL_ARB_seamless_cube_map");
		this.GL_ARB_seamless_cubemap_per_texture = supported_extensions.contains("GL_ARB_seamless_cubemap_per_texture");
		this.GL_ARB_separate_shader_objects = supported_extensions.contains("GL_ARB_separate_shader_objects");
		this.GL_ARB_shader_atomic_counters = supported_extensions.contains("GL_ARB_shader_atomic_counters");
		this.GL_ARB_shader_bit_encoding = supported_extensions.contains("GL_ARB_shader_bit_encoding");
		this.GL_ARB_shader_draw_parameters = supported_extensions.contains("GL_ARB_shader_draw_parameters");
		this.GL_ARB_shader_group_vote = supported_extensions.contains("GL_ARB_shader_group_vote");
		this.GL_ARB_shader_image_load_store = supported_extensions.contains("GL_ARB_shader_image_load_store");
		this.GL_ARB_shader_image_size = supported_extensions.contains("GL_ARB_shader_image_size");
		this.GL_ARB_shader_objects = supported_extensions.contains("GL_ARB_shader_objects");
		this.GL_ARB_shader_precision = supported_extensions.contains("GL_ARB_shader_precision");
		this.GL_ARB_shader_stencil_export = supported_extensions.contains("GL_ARB_shader_stencil_export");
		this.GL_ARB_shader_storage_buffer_object = supported_extensions.contains("GL_ARB_shader_storage_buffer_object");
		this.GL_ARB_shader_subroutine = supported_extensions.contains("GL_ARB_shader_subroutine");
		this.GL_ARB_shader_texture_image_samples = supported_extensions.contains("GL_ARB_shader_texture_image_samples");
		this.GL_ARB_shader_texture_lod = supported_extensions.contains("GL_ARB_shader_texture_lod");
		this.GL_ARB_shading_language_100 = supported_extensions.contains("GL_ARB_shading_language_100");
		this.GL_ARB_shading_language_420pack = supported_extensions.contains("GL_ARB_shading_language_420pack");
		this.GL_ARB_shading_language_include = supported_extensions.contains("GL_ARB_shading_language_include");
		this.GL_ARB_shading_language_packing = supported_extensions.contains("GL_ARB_shading_language_packing");
		this.GL_ARB_shadow = supported_extensions.contains("GL_ARB_shadow");
		this.GL_ARB_shadow_ambient = supported_extensions.contains("GL_ARB_shadow_ambient");
		this.GL_ARB_sparse_buffer = supported_extensions.contains("GL_ARB_sparse_buffer");
		this.GL_ARB_sparse_texture = supported_extensions.contains("GL_ARB_sparse_texture");
		this.GL_ARB_stencil_texturing = supported_extensions.contains("GL_ARB_stencil_texturing");
		this.GL_ARB_sync = supported_extensions.contains("GL_ARB_sync");
		this.GL_ARB_tessellation_shader = supported_extensions.contains("GL_ARB_tessellation_shader");
		this.GL_ARB_texture_barrier = supported_extensions.contains("GL_ARB_texture_barrier");
		this.GL_ARB_texture_border_clamp = supported_extensions.contains("GL_ARB_texture_border_clamp");
		this.GL_ARB_texture_buffer_object = supported_extensions.contains("GL_ARB_texture_buffer_object");
		this.GL_ARB_texture_buffer_object_rgb32 = supported_extensions.contains("GL_ARB_texture_buffer_object_rgb32")
			|| supported_extensions.contains("GL_EXT_texture_buffer_object_rgb32");
		this.GL_ARB_texture_buffer_range = supported_extensions.contains("GL_ARB_texture_buffer_range");
		this.GL_ARB_texture_compression = supported_extensions.contains("GL_ARB_texture_compression");
		this.GL_ARB_texture_compression_bptc = supported_extensions.contains("GL_ARB_texture_compression_bptc")
			|| supported_extensions.contains("GL_EXT_texture_compression_bptc");
		this.GL_ARB_texture_compression_rgtc = supported_extensions.contains("GL_ARB_texture_compression_rgtc");
		this.GL_ARB_texture_cube_map = supported_extensions.contains("GL_ARB_texture_cube_map");
		this.GL_ARB_texture_cube_map_array = supported_extensions.contains("GL_ARB_texture_cube_map_array");
		this.GL_ARB_texture_env_add = supported_extensions.contains("GL_ARB_texture_env_add");
		this.GL_ARB_texture_env_combine = supported_extensions.contains("GL_ARB_texture_env_combine");
		this.GL_ARB_texture_env_crossbar = supported_extensions.contains("GL_ARB_texture_env_crossbar");
		this.GL_ARB_texture_env_dot3 = supported_extensions.contains("GL_ARB_texture_env_dot3");
		this.GL_ARB_texture_float = supported_extensions.contains("GL_ARB_texture_float");
		this.GL_ARB_texture_gather = supported_extensions.contains("GL_ARB_texture_gather");
		this.GL_ARB_texture_mirror_clamp_to_edge = supported_extensions.contains("GL_ARB_texture_mirror_clamp_to_edge");
		this.GL_ARB_texture_mirrored_repeat = supported_extensions.contains("GL_ARB_texture_mirrored_repeat");
		this.GL_ARB_texture_multisample = supported_extensions.contains("GL_ARB_texture_multisample");
		this.GL_ARB_texture_non_power_of_two = supported_extensions.contains("GL_ARB_texture_non_power_of_two");
		this.GL_ARB_texture_query_levels = supported_extensions.contains("GL_ARB_texture_query_levels");
		this.GL_ARB_texture_query_lod = supported_extensions.contains("GL_ARB_texture_query_lod");
		this.GL_ARB_texture_rectangle = supported_extensions.contains("GL_ARB_texture_rectangle");
		this.GL_ARB_texture_rg = supported_extensions.contains("GL_ARB_texture_rg");
		this.GL_ARB_texture_rgb10_a2ui = supported_extensions.contains("GL_ARB_texture_rgb10_a2ui");
		this.GL_ARB_texture_stencil8 = supported_extensions.contains("GL_ARB_texture_stencil8");
		this.GL_ARB_texture_storage = supported_extensions.contains("GL_ARB_texture_storage")
			|| supported_extensions.contains("GL_EXT_texture_storage");
		this.GL_ARB_texture_storage_multisample = supported_extensions.contains("GL_ARB_texture_storage_multisample");
		this.GL_ARB_texture_swizzle = supported_extensions.contains("GL_ARB_texture_swizzle");
		this.GL_ARB_texture_view = supported_extensions.contains("GL_ARB_texture_view");
		this.GL_ARB_timer_query = supported_extensions.contains("GL_ARB_timer_query");
		this.GL_ARB_transform_feedback2 = supported_extensions.contains("GL_ARB_transform_feedback2");
		this.GL_ARB_transform_feedback3 = supported_extensions.contains("GL_ARB_transform_feedback3");
		this.GL_ARB_transform_feedback_instanced = supported_extensions.contains("GL_ARB_transform_feedback_instanced");
		this.GL_ARB_transform_feedback_overflow_query = supported_extensions.contains("GL_ARB_transform_feedback_overflow_query");
		this.GL_ARB_transpose_matrix = supported_extensions.contains("GL_ARB_transpose_matrix");
		this.GL_ARB_uniform_buffer_object = supported_extensions.contains("GL_ARB_uniform_buffer_object");
		this.GL_ARB_vertex_array_bgra = supported_extensions.contains("GL_ARB_vertex_array_bgra");
		this.GL_ARB_vertex_array_object = supported_extensions.contains("GL_ARB_vertex_array_object");
		this.GL_ARB_vertex_attrib_64bit = supported_extensions.contains("GL_ARB_vertex_attrib_64bit");
		this.GL_ARB_vertex_attrib_binding = supported_extensions.contains("GL_ARB_vertex_attrib_binding");
		this.GL_ARB_vertex_blend = supported_extensions.contains("GL_ARB_vertex_blend");
		this.GL_ARB_vertex_buffer_object = supported_extensions.contains("GL_ARB_vertex_buffer_object")
			&& supported_extensions.contains("GL_ARB_buffer_object");
		this.GL_ARB_vertex_program = supported_extensions.contains("GL_ARB_vertex_program")
			&& supported_extensions.contains("GL_ARB_program");
		this.GL_ARB_vertex_shader = supported_extensions.contains("GL_ARB_vertex_shader");
		this.GL_ARB_vertex_type_10f_11f_11f_rev = supported_extensions.contains("GL_ARB_vertex_type_10f_11f_11f_rev");
		this.GL_ARB_vertex_type_2_10_10_10_rev = supported_extensions.contains("GL_ARB_vertex_type_2_10_10_10_rev");
		this.GL_ARB_viewport_array = supported_extensions.contains("GL_ARB_viewport_array");
		this.GL_ARB_window_pos = supported_extensions.contains("GL_ARB_window_pos");
		this.GL_ATI_draw_buffers = supported_extensions.contains("GL_ATI_draw_buffers");
		this.GL_ATI_element_array = supported_extensions.contains("GL_ATI_element_array");
		this.GL_ATI_envmap_bumpmap = supported_extensions.contains("GL_ATI_envmap_bumpmap");
		this.GL_ATI_fragment_shader = supported_extensions.contains("GL_ATI_fragment_shader");
		this.GL_ATI_map_object_buffer = supported_extensions.contains("GL_ATI_map_object_buffer");
		this.GL_ATI_meminfo = supported_extensions.contains("GL_ATI_meminfo");
		this.GL_ATI_pn_triangles = supported_extensions.contains("GL_ATI_pn_triangles");
		this.GL_ATI_separate_stencil = supported_extensions.contains("GL_ATI_separate_stencil");
		this.GL_ATI_shader_texture_lod = supported_extensions.contains("GL_ATI_shader_texture_lod");
		this.GL_ATI_text_fragment_shader = supported_extensions.contains("GL_ATI_text_fragment_shader");
		this.GL_ATI_texture_compression_3dc = supported_extensions.contains("GL_ATI_texture_compression_3dc");
		this.GL_ATI_texture_env_combine3 = supported_extensions.contains("GL_ATI_texture_env_combine3");
		this.GL_ATI_texture_float = supported_extensions.contains("GL_ATI_texture_float");
		this.GL_ATI_texture_mirror_once = supported_extensions.contains("GL_ATI_texture_mirror_once");
		this.GL_ATI_vertex_array_object = supported_extensions.contains("GL_ATI_vertex_array_object");
		this.GL_ATI_vertex_attrib_array_object = supported_extensions.contains("GL_ATI_vertex_attrib_array_object");
		this.GL_ATI_vertex_streams = supported_extensions.contains("GL_ATI_vertex_streams");
		this.GL_EXT_Cg_shader = supported_extensions.contains("GL_EXT_Cg_shader");
		this.GL_EXT_abgr = supported_extensions.contains("GL_EXT_abgr");
		this.GL_EXT_bgra = supported_extensions.contains("GL_EXT_bgra");
		this.GL_EXT_bindable_uniform = supported_extensions.contains("GL_EXT_bindable_uniform");
		this.GL_EXT_blend_color = supported_extensions.contains("GL_EXT_blend_color");
		this.GL_EXT_blend_equation_separate = supported_extensions.contains("GL_EXT_blend_equation_separate");
		this.GL_EXT_blend_func_separate = supported_extensions.contains("GL_EXT_blend_func_separate");
		this.GL_EXT_blend_minmax = supported_extensions.contains("GL_EXT_blend_minmax");
		this.GL_EXT_blend_subtract = supported_extensions.contains("GL_EXT_blend_subtract");
		this.GL_EXT_compiled_vertex_array = supported_extensions.contains("GL_EXT_compiled_vertex_array");
		this.GL_EXT_depth_bounds_test = supported_extensions.contains("GL_EXT_depth_bounds_test");
		this.GL_EXT_direct_state_access = supported_extensions.contains("GL_EXT_direct_state_access");
		this.GL_EXT_draw_buffers2 = supported_extensions.contains("GL_EXT_draw_buffers2");
		this.GL_EXT_draw_instanced = supported_extensions.contains("GL_EXT_draw_instanced");
		this.GL_EXT_draw_range_elements = supported_extensions.contains("GL_EXT_draw_range_elements");
		this.GL_EXT_fog_coord = supported_extensions.contains("GL_EXT_fog_coord");
		this.GL_EXT_framebuffer_blit = supported_extensions.contains("GL_EXT_framebuffer_blit");
		this.GL_EXT_framebuffer_multisample = supported_extensions.contains("GL_EXT_framebuffer_multisample");
		this.GL_EXT_framebuffer_multisample_blit_scaled = supported_extensions.contains("GL_EXT_framebuffer_multisample_blit_scaled");
		this.GL_EXT_framebuffer_object = supported_extensions.contains("GL_EXT_framebuffer_object");
		this.GL_EXT_framebuffer_sRGB = supported_extensions.contains("GL_EXT_framebuffer_sRGB");
		this.GL_EXT_geometry_shader4 = supported_extensions.contains("GL_EXT_geometry_shader4");
		this.GL_EXT_gpu_program_parameters = supported_extensions.contains("GL_EXT_gpu_program_parameters");
		this.GL_EXT_gpu_shader4 = supported_extensions.contains("GL_EXT_gpu_shader4");
		this.GL_EXT_multi_draw_arrays = supported_extensions.contains("GL_EXT_multi_draw_arrays");
		this.GL_EXT_packed_depth_stencil = supported_extensions.contains("GL_EXT_packed_depth_stencil");
		this.GL_EXT_packed_float = supported_extensions.contains("GL_EXT_packed_float");
		this.GL_EXT_packed_pixels = supported_extensions.contains("GL_EXT_packed_pixels");
		this.GL_EXT_paletted_texture = supported_extensions.contains("GL_EXT_paletted_texture");
		this.GL_EXT_pixel_buffer_object = supported_extensions.contains("GL_EXT_pixel_buffer_object")
			&& supported_extensions.contains("GL_ARB_buffer_object");
		this.GL_EXT_point_parameters = supported_extensions.contains("GL_EXT_point_parameters");
		this.GL_EXT_provoking_vertex = supported_extensions.contains("GL_EXT_provoking_vertex");
		this.GL_EXT_rescale_normal = supported_extensions.contains("GL_EXT_rescale_normal");
		this.GL_EXT_secondary_color = supported_extensions.contains("GL_EXT_secondary_color");
		this.GL_EXT_separate_shader_objects = supported_extensions.contains("GL_EXT_separate_shader_objects");
		this.GL_EXT_separate_specular_color = supported_extensions.contains("GL_EXT_separate_specular_color");
		this.GL_EXT_shader_image_load_store = supported_extensions.contains("GL_EXT_shader_image_load_store");
		this.GL_EXT_shadow_funcs = supported_extensions.contains("GL_EXT_shadow_funcs");
		this.GL_EXT_shared_texture_palette = supported_extensions.contains("GL_EXT_shared_texture_palette");
		this.GL_EXT_stencil_clear_tag = supported_extensions.contains("GL_EXT_stencil_clear_tag");
		this.GL_EXT_stencil_two_side = supported_extensions.contains("GL_EXT_stencil_two_side");
		this.GL_EXT_stencil_wrap = supported_extensions.contains("GL_EXT_stencil_wrap");
		this.GL_EXT_texture_3d = supported_extensions.contains("GL_EXT_texture_3d");
		this.GL_EXT_texture_array = supported_extensions.contains("GL_EXT_texture_array");
		this.GL_EXT_texture_buffer_object = supported_extensions.contains("GL_EXT_texture_buffer_object");
		this.GL_EXT_texture_compression_latc = supported_extensions.contains("GL_EXT_texture_compression_latc");
		this.GL_EXT_texture_compression_rgtc = supported_extensions.contains("GL_EXT_texture_compression_rgtc");
		this.GL_EXT_texture_compression_s3tc = supported_extensions.contains("GL_EXT_texture_compression_s3tc");
		this.GL_EXT_texture_env_combine = supported_extensions.contains("GL_EXT_texture_env_combine");
		this.GL_EXT_texture_env_dot3 = supported_extensions.contains("GL_EXT_texture_env_dot3");
		this.GL_EXT_texture_filter_anisotropic = supported_extensions.contains("GL_EXT_texture_filter_anisotropic");
		this.GL_EXT_texture_integer = supported_extensions.contains("GL_EXT_texture_integer");
		this.GL_EXT_texture_lod_bias = supported_extensions.contains("GL_EXT_texture_lod_bias");
		this.GL_EXT_texture_mirror_clamp = supported_extensions.contains("GL_EXT_texture_mirror_clamp");
		this.GL_EXT_texture_rectangle = supported_extensions.contains("GL_EXT_texture_rectangle");
		this.GL_EXT_texture_sRGB = supported_extensions.contains("GL_EXT_texture_sRGB");
		this.GL_EXT_texture_sRGB_decode = supported_extensions.contains("GL_EXT_texture_sRGB_decode");
		this.GL_EXT_texture_shared_exponent = supported_extensions.contains("GL_EXT_texture_shared_exponent");
		this.GL_EXT_texture_snorm = supported_extensions.contains("GL_EXT_texture_snorm");
		this.GL_EXT_texture_swizzle = supported_extensions.contains("GL_EXT_texture_swizzle");
		this.GL_EXT_timer_query = supported_extensions.contains("GL_EXT_timer_query");
		this.GL_EXT_transform_feedback = supported_extensions.contains("GL_EXT_transform_feedback");
		this.GL_EXT_vertex_array_bgra = supported_extensions.contains("GL_EXT_vertex_array_bgra");
		this.GL_EXT_vertex_attrib_64bit = supported_extensions.contains("GL_EXT_vertex_attrib_64bit");
		this.GL_EXT_vertex_shader = supported_extensions.contains("GL_EXT_vertex_shader");
		this.GL_EXT_vertex_weighting = supported_extensions.contains("GL_EXT_vertex_weighting");
		this.OpenGL11 = supported_extensions.contains("OpenGL11");
		this.OpenGL12 = supported_extensions.contains("OpenGL12");
		this.OpenGL13 = supported_extensions.contains("OpenGL13");
		this.OpenGL14 = supported_extensions.contains("OpenGL14");
		this.OpenGL15 = supported_extensions.contains("OpenGL15");
		this.OpenGL20 = supported_extensions.contains("OpenGL20");
		this.OpenGL21 = supported_extensions.contains("OpenGL21");
		this.OpenGL30 = supported_extensions.contains("OpenGL30");
		this.OpenGL31 = supported_extensions.contains("OpenGL31");
		this.OpenGL32 = supported_extensions.contains("OpenGL32");
		this.OpenGL33 = supported_extensions.contains("OpenGL33");
		this.OpenGL40 = supported_extensions.contains("OpenGL40");
		this.OpenGL41 = supported_extensions.contains("OpenGL41");
		this.OpenGL42 = supported_extensions.contains("OpenGL42");
		this.OpenGL43 = supported_extensions.contains("OpenGL43");
		this.OpenGL44 = supported_extensions.contains("OpenGL44");
		this.OpenGL45 = supported_extensions.contains("OpenGL45");
		this.GL_GREMEDY_frame_terminator = supported_extensions.contains("GL_GREMEDY_frame_terminator");
		this.GL_GREMEDY_string_marker = supported_extensions.contains("GL_GREMEDY_string_marker");
		this.GL_HP_occlusion_test = supported_extensions.contains("GL_HP_occlusion_test");
		this.GL_IBM_rasterpos_clip = supported_extensions.contains("GL_IBM_rasterpos_clip");
		this.GL_INTEL_map_texture = supported_extensions.contains("GL_INTEL_map_texture");
		this.GL_KHR_context_flush_control = supported_extensions.contains("GL_KHR_context_flush_control");
		this.GL_KHR_debug = supported_extensions.contains("GL_KHR_debug");
		this.GL_KHR_robust_buffer_access_behavior = supported_extensions.contains("GL_KHR_robust_buffer_access_behavior");
		this.GL_KHR_robustness = supported_extensions.contains("GL_KHR_robustness");
		this.GL_KHR_texture_compression_astc_ldr = supported_extensions.contains("GL_KHR_texture_compression_astc_ldr");
		this.GL_NVX_gpu_memory_info = supported_extensions.contains("GL_NVX_gpu_memory_info");
		this.GL_NV_bindless_multi_draw_indirect = supported_extensions.contains("GL_NV_bindless_multi_draw_indirect");
		this.GL_NV_bindless_texture = supported_extensions.contains("GL_NV_bindless_texture");
		this.GL_NV_blend_equation_advanced = supported_extensions.contains("GL_NV_blend_equation_advanced");
		this.GL_NV_blend_square = supported_extensions.contains("GL_NV_blend_square");
		this.GL_NV_compute_program5 = supported_extensions.contains("GL_NV_compute_program5");
		this.GL_NV_conditional_render = supported_extensions.contains("GL_NV_conditional_render");
		this.GL_NV_copy_depth_to_color = supported_extensions.contains("GL_NV_copy_depth_to_color");
		this.GL_NV_copy_image = supported_extensions.contains("GL_NV_copy_image");
		this.GL_NV_deep_texture3D = supported_extensions.contains("GL_NV_deep_texture3D");
		this.GL_NV_depth_buffer_float = supported_extensions.contains("GL_NV_depth_buffer_float");
		this.GL_NV_depth_clamp = supported_extensions.contains("GL_NV_depth_clamp");
		this.GL_NV_draw_texture = supported_extensions.contains("GL_NV_draw_texture");
		this.GL_NV_evaluators = supported_extensions.contains("GL_NV_evaluators");
		this.GL_NV_explicit_multisample = supported_extensions.contains("GL_NV_explicit_multisample");
		this.GL_NV_fence = supported_extensions.contains("GL_NV_fence");
		this.GL_NV_float_buffer = supported_extensions.contains("GL_NV_float_buffer");
		this.GL_NV_fog_distance = supported_extensions.contains("GL_NV_fog_distance");
		this.GL_NV_fragment_program = supported_extensions.contains("GL_NV_fragment_program")
			&& supported_extensions.contains("GL_NV_program");
		this.GL_NV_fragment_program2 = supported_extensions.contains("GL_NV_fragment_program2");
		this.GL_NV_fragment_program4 = supported_extensions.contains("GL_NV_fragment_program4");
		this.GL_NV_fragment_program_option = supported_extensions.contains("GL_NV_fragment_program_option");
		this.GL_NV_framebuffer_multisample_coverage = supported_extensions.contains("GL_NV_framebuffer_multisample_coverage");
		this.GL_NV_geometry_program4 = supported_extensions.contains("GL_NV_geometry_program4");
		this.GL_NV_geometry_shader4 = supported_extensions.contains("GL_NV_geometry_shader4");
		this.GL_NV_gpu_program4 = supported_extensions.contains("GL_NV_gpu_program4");
		this.GL_NV_gpu_program5 = supported_extensions.contains("GL_NV_gpu_program5");
		this.GL_NV_gpu_program5_mem_extended = supported_extensions.contains("GL_NV_gpu_program5_mem_extended");
		this.GL_NV_gpu_shader5 = supported_extensions.contains("GL_NV_gpu_shader5");
		this.GL_NV_half_float = supported_extensions.contains("GL_NV_half_float");
		this.GL_NV_light_max_exponent = supported_extensions.contains("GL_NV_light_max_exponent");
		this.GL_NV_multisample_coverage = supported_extensions.contains("GL_NV_multisample_coverage");
		this.GL_NV_multisample_filter_hint = supported_extensions.contains("GL_NV_multisample_filter_hint");
		this.GL_NV_occlusion_query = supported_extensions.contains("GL_NV_occlusion_query");
		this.GL_NV_packed_depth_stencil = supported_extensions.contains("GL_NV_packed_depth_stencil");
		this.GL_NV_parameter_buffer_object = supported_extensions.contains("GL_NV_parameter_buffer_object");
		this.GL_NV_parameter_buffer_object2 = supported_extensions.contains("GL_NV_parameter_buffer_object2");
		this.GL_NV_path_rendering = supported_extensions.contains("GL_NV_path_rendering");
		this.GL_NV_pixel_data_range = supported_extensions.contains("GL_NV_pixel_data_range");
		this.GL_NV_point_sprite = supported_extensions.contains("GL_NV_point_sprite");
		this.GL_NV_present_video = supported_extensions.contains("GL_NV_present_video");
		this.GL_NV_primitive_restart = supported_extensions.contains("GL_NV_primitive_restart");
		this.GL_NV_register_combiners = supported_extensions.contains("GL_NV_register_combiners");
		this.GL_NV_register_combiners2 = supported_extensions.contains("GL_NV_register_combiners2");
		this.GL_NV_shader_atomic_counters = supported_extensions.contains("GL_NV_shader_atomic_counters");
		this.GL_NV_shader_atomic_float = supported_extensions.contains("GL_NV_shader_atomic_float");
		this.GL_NV_shader_buffer_load = supported_extensions.contains("GL_NV_shader_buffer_load");
		this.GL_NV_shader_buffer_store = supported_extensions.contains("GL_NV_shader_buffer_store");
		this.GL_NV_shader_storage_buffer_object = supported_extensions.contains("GL_NV_shader_storage_buffer_object");
		this.GL_NV_tessellation_program5 = supported_extensions.contains("GL_NV_tessellation_program5");
		this.GL_NV_texgen_reflection = supported_extensions.contains("GL_NV_texgen_reflection");
		this.GL_NV_texture_barrier = supported_extensions.contains("GL_NV_texture_barrier");
		this.GL_NV_texture_compression_vtc = supported_extensions.contains("GL_NV_texture_compression_vtc");
		this.GL_NV_texture_env_combine4 = supported_extensions.contains("GL_NV_texture_env_combine4");
		this.GL_NV_texture_expand_normal = supported_extensions.contains("GL_NV_texture_expand_normal");
		this.GL_NV_texture_multisample = supported_extensions.contains("GL_NV_texture_multisample");
		this.GL_NV_texture_rectangle = supported_extensions.contains("GL_NV_texture_rectangle");
		this.GL_NV_texture_shader = supported_extensions.contains("GL_NV_texture_shader");
		this.GL_NV_texture_shader2 = supported_extensions.contains("GL_NV_texture_shader2");
		this.GL_NV_texture_shader3 = supported_extensions.contains("GL_NV_texture_shader3");
		this.GL_NV_transform_feedback = supported_extensions.contains("GL_NV_transform_feedback");
		this.GL_NV_transform_feedback2 = supported_extensions.contains("GL_NV_transform_feedback2");
		this.GL_NV_vertex_array_range = supported_extensions.contains("GL_NV_vertex_array_range");
		this.GL_NV_vertex_array_range2 = supported_extensions.contains("GL_NV_vertex_array_range2");
		this.GL_NV_vertex_attrib_integer_64bit = supported_extensions.contains("GL_NV_vertex_attrib_integer_64bit");
		this.GL_NV_vertex_buffer_unified_memory = supported_extensions.contains("GL_NV_vertex_buffer_unified_memory");
		this.GL_NV_vertex_program = supported_extensions.contains("GL_NV_vertex_program")
			&& supported_extensions.contains("GL_NV_program");
		this.GL_NV_vertex_program1_1 = supported_extensions.contains("GL_NV_vertex_program1_1");
		this.GL_NV_vertex_program2 = supported_extensions.contains("GL_NV_vertex_program2");
		this.GL_NV_vertex_program2_option = supported_extensions.contains("GL_NV_vertex_program2_option");
		this.GL_NV_vertex_program3 = supported_extensions.contains("GL_NV_vertex_program3");
		this.GL_NV_vertex_program4 = supported_extensions.contains("GL_NV_vertex_program4");
		this.GL_NV_video_capture = supported_extensions.contains("GL_NV_video_capture");
		this.GL_SGIS_generate_mipmap = supported_extensions.contains("GL_SGIS_generate_mipmap");
		this.GL_SGIS_texture_lod = supported_extensions.contains("GL_SGIS_texture_lod");
		this.GL_SUN_slice_accum = supported_extensions.contains("GL_SUN_slice_accum");
		tracker.init();
	}
}
