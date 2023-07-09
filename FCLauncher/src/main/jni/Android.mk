LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE            := angle_gles2
LOCAL_SRC_FILES         := tinywrapper/angle-gles/$(TARGET_ARCH_ABI)/libGLESv2_angle.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := tinywrapper
LOCAL_SHARED_LIBRARIES  := angle_gles2
LOCAL_SRC_FILES         := tinywrapper/main.c tinywrapper/string_utils.c
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/tinywrapper
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := xhook
LOCAL_SRC_FILES         := xhook/xhook.c \
                           xhook/xh_core.c \
                           xhook/xh_elf.c \
                           xhook/xh_jni.c \
                           xhook/xh_log.c \
                           xhook/xh_util.c \
                           xhook/xh_version.c
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/xhook/include
LOCAL_CFLAGS            := -Wall -Wextra -Werror -fvisibility=hidden
LOCAL_CONLYFLAGS        := -std=c11
LOCAL_LDLIBS            := -llog
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := byopen
LOCAL_SRC_FILES         := byopen/byopen_android.c
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/byopen
LOCAL_CFLAGS            := -Wall
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := fcl
LOCAL_SHARED_LIBRARIES  := xhook \
						   byopen
LOCAL_SRC_FILES         := fcl/fcl_bridge.c \
                           fcl/fcl_event.c \
                           fcl/fcl_loader.c
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/xhook/include \
                           $(LOCAL_PATH)/fcl/include
LOCAL_LDLIBS            := -llog -ldl -landroid
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := glfw
LOCAL_SHARED_LIBRARIES  := fcl \
						   byopen
LOCAL_SRC_FILES         := glfw/context.c \
                           glfw/init.c \
                           glfw/input.c \
                           glfw/monitor.c \
                           glfw/vulkan.c \
                           glfw/window.c \
                           glfw/fcl_init.c \
                           glfw/fcl_monitor.c \
                           glfw/fcl_window.c \
                           glfw/egl_context.c \
                           glfw/osmesa_context.c \
                           glfw/posix_thread.c \
                           glfw/posix_time.c
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/fcl/include \
                           $(LOCAL_PATH)/glfw/include
LOCAL_CFLAGS            := -Wall
LOCAL_LDLIBS            := -llog -ldl -landroid
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := awt_headless
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := awt_xawt
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/awt_xawt
LOCAL_SHARED_LIBRARIES  := awt_headless
LOCAL_SRC_FILES         := awt_xawt/xawt_fake.c
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := fcl_awt
LOCAL_SRC_FILES         := awt/awt_bridge.c
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := lwjgl2
LOCAL_SHARED_LIBRARIES  := fcl
LOCAL_SRC_FILES         := lwjgl2/common/common_tools.c \
                           lwjgl2/common/extal.c \
                           lwjgl2/common/extcl.c \
                           lwjgl2/common/opengl/extgl.c \
                           lwjgl2/common/opengl/org_lwjgl_opengl_CallbackUtil.c \
                           lwjgl2/common/opengl/org_lwjgl_opengl_GLContext.c \
                           lwjgl2/common/opengl/org_lwjgl_opengl_NVPresentVideoUtil.c \
                           lwjgl2/common/opengl/org_lwjgl_opengl_NVVideoCaptureUtil.c \
                           lwjgl2/common/org_lwjgl_BufferUtils.c \
                           lwjgl2/common/org_lwjgl_LWJGLUtil.c \
                           lwjgl2/common/org_lwjgl_openal_AL.c \
                           lwjgl2/common/org_lwjgl_openal_ALC10.c \
                           lwjgl2/common/org_lwjgl_openal_ALC11.c \
                           lwjgl2/common/org_lwjgl_opencl_CallbackUtil.c \
                           lwjgl2/common/org_lwjgl_opencl_CL.c \
                           lwjgl2/common/org_lwjgl_opengl_AWTSurfaceLock.c \
                           lwjgl2/fcl/fcl_al.c \
                           lwjgl2/fcl/fcl_cl.c \
                           lwjgl2/fcl/opengl/context.c \
                           lwjgl2/fcl/opengl/display.c \
                           lwjgl2/fcl/opengl/EGL.c \
                           lwjgl2/fcl/opengl/extgl_egl.c \
                           lwjgl2/fcl/opengl/org_lwjgl_opengl_Display.c \
                           lwjgl2/fcl/opengl/org_lwjgl_opengl_FCLContextImplementation.c \
                           lwjgl2/fcl/opengl/org_lwjgl_opengl_Pbuffer.c \
                           lwjgl2/fcl/opengl/org_lwjgl_opengl_FCLInjector.c \
                           lwjgl2/fcl/org_lwjgl_input_Cursor.c \
                           lwjgl2/fcl/org_lwjgl_opengl_Display.c \
                           lwjgl2/fcl/org_lwjgl_opengl_FCLEvent.c \
                           lwjgl2/fcl/org_lwjgl_opengl_FCLKeyboard.c \
                           lwjgl2/fcl/org_lwjgl_opengl_FCLMouse.c \
                           lwjgl2/fcl/org_lwjgl_opengl_FCLPeerInfo.c \
                           lwjgl2/generated/openal/org_lwjgl_openal_AL10.c \
                           lwjgl2/generated/openal/org_lwjgl_openal_AL11.c \
                           lwjgl2/generated/openal/org_lwjgl_openal_EFX10.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_APPLEContextLoggingFunctions.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_APPLEGLSharing.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_APPLESetMemObjectDestructor.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_CL10.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_CL10GL.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_CL11.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_CL12.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_CL12GL.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_EXTDeviceFission.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_EXTMigrateMemobject.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_KHRGLEvent.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_KHRGLSharing.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_KHRICD.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_KHRSubgroups.c \
                           lwjgl2/generated/opencl/org_lwjgl_opencl_KHRTerminateContext.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_AMDDebugOutput.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_AMDDrawBuffersBlend.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_AMDInterleavedElements.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_AMDMultiDrawIndirect.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_AMDNameGenDelete.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_AMDPerformanceMonitor.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_AMDSamplePositions.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_AMDSparseTexture.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_AMDStencilOperationExtended.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_AMDVertexShaderTessellator.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_APPLEElementArray.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_APPLEFence.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_APPLEFlushBufferRange.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_APPLEObjectPurgeable.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_APPLETextureRange.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_APPLEVertexArrayObject.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_APPLEVertexArrayRange.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_APPLEVertexProgramEvaluators.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBBindlessTexture.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBBufferObject.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBBufferStorage.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBClearBufferObject.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBCLEvent.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBColorBufferFloat.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBComputeVariableGroupSize.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBDebugOutput.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBDrawBuffers.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBDrawBuffersBlend.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBDrawInstanced.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBFramebufferNoAttachments.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBGeometryShader4.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBGpuShaderFp64.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBImaging.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBIndirectParameters.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBInstancedArrays.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBMatrixPalette.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBMultisample.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBMultitexture.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBOcclusionQuery.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBPointParameters.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBProgram.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBRobustness.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBSampleShading.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBShaderObjects.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBShadingLanguageInclude.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBSparseBuffer.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBSparseTexture.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBTextureBufferObject.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBTextureBufferRange.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBTextureCompression.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBTextureStorage.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBTextureStorageMultisample.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBTransposeMatrix.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBVertexAttrib64bit.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBVertexBlend.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBVertexShader.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ARBWindowPos.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ATIDrawBuffers.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ATIElementArray.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ATIEnvmapBumpmap.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ATIFragmentShader.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ATIMapObjectBuffer.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ATIPnTriangles.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ATISeparateStencil.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ATIVertexArrayObject.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ATIVertexAttribArrayObject.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_ATIVertexStreams.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTBindableUniform.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTBlendColor.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTBlendEquationSeparate.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTBlendFuncSeparate.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTBlendMinmax.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTCompiledVertexArray.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTDepthBoundsTest.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTDirectStateAccess.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTDrawBuffers2.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTDrawInstanced.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTDrawRangeElements.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTFogCoord.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTFramebufferBlit.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTFramebufferMultisample.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTFramebufferObject.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTGeometryShader4.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTGpuProgramParameters.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTGpuShader4.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTMultiDrawArrays.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTPalettedTexture.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTPointParameters.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTProvokingVertex.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTSecondaryColor.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTSeparateShaderObjects.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTShaderImageLoadStore.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTStencilClearTag.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTStencilTwoSide.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTTextureBufferObject.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTTextureInteger.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTTimerQuery.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTTransformFeedback.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTVertexAttrib64bit.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTVertexShader.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_EXTVertexWeighting.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL11.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL12.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL13.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL14.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL15.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL20.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL21.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL30.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL31.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL32.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL33.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL40.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL41.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL42.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL43.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL44.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GL45.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GREMEDYFrameTerminator.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_GREMEDYStringMarker.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_INTELMapTexture.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVBindlessMultiDrawIndirect.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVBindlessTexture.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVBlendEquationAdvanced.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVConditionalRender.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVCopyImage.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVDepthBufferFloat.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVDrawTexture.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVEvaluators.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVExplicitMultisample.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVFence.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVFragmentProgram.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVFramebufferMultisampleCoverage.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVGeometryProgram4.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVGpuProgram4.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVGpuShader5.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVHalfFloat.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVOcclusionQuery.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVParameterBufferObject.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVPathRendering.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVPixelDataRange.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVPointSprite.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVPresentVideo.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVPrimitiveRestart.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVProgram.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVRegisterCombiners.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVRegisterCombiners2.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVShaderBufferLoad.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVTextureBarrier.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVTextureMultisample.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVTransformFeedback.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVTransformFeedback2.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVVertexArrayRange.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVVertexAttribInteger64bit.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVVertexBufferUnifiedMemory.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVVertexProgram.c \
                           lwjgl2/generated/opengl/org_lwjgl_opengl_NVVideoCapture.c

LOCAL_C_INCLUDES        := $(LOCAL_PATH)/lwjgl2/common \
						   $(LOCAL_PATH)/lwjgl2/common/opengl \
						   $(LOCAL_PATH)/lwjgl2/common/CL \
						   $(LOCAL_PATH)/lwjgl2/common/EGL \
						   $(LOCAL_PATH)/lwjgl2/common/GLES2 \
						   $(LOCAL_PATH)/lwjgl2/common/KHR \
						   $(LOCAL_PATH)/lwjgl2/common/OpenCL \
						   $(LOCAL_PATH)/lwjgl2/fcl \
						   $(LOCAL_PATH)/lwjgl2/fcl/opengl \
						   $(LOCAL_PATH)/fcl/include

LOCAL_CFLAGS            := -O2 -Wall -c -fPIC -std=c99 -Wunused -DPLATFORM_FCL
LOCAL_LDLIBS            := -lm -landroid
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := dyncall
LOCAL_SRC_FILES         := lwjgl3/dyncall/$(TARGET_ARCH_ABI)/libdyncall_s.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := dyncallback
LOCAL_SRC_FILES         := lwjgl3/dyncall/$(TARGET_ARCH_ABI)/libdyncallback_s.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := dynload
LOCAL_SRC_FILES         := lwjgl3/dyncall/$(TARGET_ARCH_ABI)/libdynload_s.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := lwjgl
LOCAL_STATIC_LIBRARIES  := dyncall \
						   dyncallback \
						   dynload
LOCAL_SHARED_LIBRARIES  := byopen \
						   fcl
LOCAL_SRC_FILES         := lwjgl3/fcl_hook.c \
						   lwjgl3/common_tools.c \
                           lwjgl3/org_lwjgl_opengl_AMDDebugOutput.c \
                           lwjgl3/org_lwjgl_opengl_AMDDrawBuffersBlend.c \
                           lwjgl3/org_lwjgl_opengl_AMDFramebufferMultisampleAdvanced.c \
                           lwjgl3/org_lwjgl_opengl_AMDInterleavedElements.c \
                           lwjgl3/org_lwjgl_opengl_AMDOcclusionQueryEvent.c \
                           lwjgl3/org_lwjgl_opengl_AMDPerformanceMonitor.c \
                           lwjgl3/org_lwjgl_opengl_AMDSamplePositions.c \
                           lwjgl3/org_lwjgl_opengl_AMDSparseTexture.c \
                           lwjgl3/org_lwjgl_opengl_AMDStencilOperationExtended.c \
                           lwjgl3/org_lwjgl_opengl_AMDVertexShaderTessellator.c \
                           lwjgl3/org_lwjgl_opengl_ARBBindlessTexture.c \
                           lwjgl3/org_lwjgl_opengl_ARBBufferStorage.c \
                           lwjgl3/org_lwjgl_opengl_ARBClearBufferObject.c \
                           lwjgl3/org_lwjgl_opengl_ARBColorBufferFloat.c \
                           lwjgl3/org_lwjgl_opengl_ARBComputeVariableGroupSize.c \
                           lwjgl3/org_lwjgl_opengl_ARBDebugOutput.c \
                           lwjgl3/org_lwjgl_opengl_ARBDrawBuffers.c \
                           lwjgl3/org_lwjgl_opengl_ARBDrawBuffersBlend.c \
                           lwjgl3/org_lwjgl_opengl_ARBDrawInstanced.c \
                           lwjgl3/org_lwjgl_opengl_ARBES32Compatibility.c \
                           lwjgl3/org_lwjgl_opengl_ARBFramebufferNoAttachments.c \
                           lwjgl3/org_lwjgl_opengl_ARBGeometryShader4.c \
                           lwjgl3/org_lwjgl_opengl_ARBGLSPIRV.c \
                           lwjgl3/org_lwjgl_opengl_ARBGPUShaderFP64.c \
                           lwjgl3/org_lwjgl_opengl_ARBGPUShaderInt64.c \
                           lwjgl3/org_lwjgl_opengl_ARBImaging.c \
                           lwjgl3/org_lwjgl_opengl_ARBIndirectParameters.c \
                           lwjgl3/org_lwjgl_opengl_ARBInstancedArrays.c \
                           lwjgl3/org_lwjgl_opengl_ARBMatrixPalette.c \
                           lwjgl3/org_lwjgl_opengl_ARBMultisample.c \
                           lwjgl3/org_lwjgl_opengl_ARBMultitexture.c \
                           lwjgl3/org_lwjgl_opengl_ARBOcclusionQuery.c \
                           lwjgl3/org_lwjgl_opengl_ARBParallelShaderCompile.c \
                           lwjgl3/org_lwjgl_opengl_ARBPointParameters.c \
                           lwjgl3/org_lwjgl_opengl_ARBRobustness.c \
                           lwjgl3/org_lwjgl_opengl_ARBSampleLocations.c \
                           lwjgl3/org_lwjgl_opengl_ARBSampleShading.c \
                           lwjgl3/org_lwjgl_opengl_ARBShaderObjects.c \
                           lwjgl3/org_lwjgl_opengl_ARBShadingLanguageInclude.c \
                           lwjgl3/org_lwjgl_opengl_ARBSparseBuffer.c \
                           lwjgl3/org_lwjgl_opengl_ARBSparseTexture.c \
                           lwjgl3/org_lwjgl_opengl_ARBTextureBufferObject.c \
                           lwjgl3/org_lwjgl_opengl_ARBTextureBufferRange.c \
                           lwjgl3/org_lwjgl_opengl_ARBTextureCompression.c \
                           lwjgl3/org_lwjgl_opengl_ARBTextureStorage.c \
                           lwjgl3/org_lwjgl_opengl_ARBTextureStorageMultisample.c \
                           lwjgl3/org_lwjgl_opengl_ARBTransposeMatrix.c \
                           lwjgl3/org_lwjgl_opengl_ARBVertexAttrib64Bit.c \
                           lwjgl3/org_lwjgl_opengl_ARBVertexAttribBinding.c \
                           lwjgl3/org_lwjgl_opengl_ARBVertexBlend.c \
                           lwjgl3/org_lwjgl_opengl_ARBVertexBufferObject.c \
                           lwjgl3/org_lwjgl_opengl_ARBVertexProgram.c \
                           lwjgl3/org_lwjgl_opengl_ARBVertexShader.c \
                           lwjgl3/org_lwjgl_opengl_ARBWindowPos.c \
                           lwjgl3/org_lwjgl_opengl_EXTBindableUniform.c \
                           lwjgl3/org_lwjgl_opengl_EXTBlendColor.c \
                           lwjgl3/org_lwjgl_opengl_EXTBlendEquationSeparate.c \
                           lwjgl3/org_lwjgl_opengl_EXTBlendFuncSeparate.c \
                           lwjgl3/org_lwjgl_opengl_EXTBlendMinmax.c \
                           lwjgl3/org_lwjgl_opengl_EXTCompiledVertexArray.c \
                           lwjgl3/org_lwjgl_opengl_EXTDebugLabel.c \
                           lwjgl3/org_lwjgl_opengl_EXTDebugMarker.c \
                           lwjgl3/org_lwjgl_opengl_EXTDepthBoundsTest.c \
                           lwjgl3/org_lwjgl_opengl_EXTDirectStateAccess.c \
                           lwjgl3/org_lwjgl_opengl_EXTDrawBuffers2.c \
                           lwjgl3/org_lwjgl_opengl_EXTDrawInstanced.c \
                           lwjgl3/org_lwjgl_opengl_EXTEGLImageStorage.c \
                           lwjgl3/org_lwjgl_opengl_EXTExternalBuffer.c \
                           lwjgl3/org_lwjgl_opengl_EXTFramebufferBlit.c \
                           lwjgl3/org_lwjgl_opengl_EXTFramebufferMultisample.c \
                           lwjgl3/org_lwjgl_opengl_EXTFramebufferObject.c \
                           lwjgl3/org_lwjgl_opengl_EXTGeometryShader4.c \
                           lwjgl3/org_lwjgl_opengl_EXTGPUProgramParameters.c \
                           lwjgl3/org_lwjgl_opengl_EXTGPUShader4.c \
                           lwjgl3/org_lwjgl_opengl_EXTMemoryObject.c \
                           lwjgl3/org_lwjgl_opengl_EXTMemoryObjectFD.c \
                           lwjgl3/org_lwjgl_opengl_EXTMemoryObjectWin32.c \
                           lwjgl3/org_lwjgl_opengl_EXTPointParameters.c \
                           lwjgl3/org_lwjgl_opengl_EXTPolygonOffsetClamp.c \
                           lwjgl3/org_lwjgl_opengl_EXTProvokingVertex.c \
                           lwjgl3/org_lwjgl_opengl_EXTRasterMultisample.c \
                           lwjgl3/org_lwjgl_opengl_EXTSecondaryColor.c \
                           lwjgl3/org_lwjgl_opengl_EXTSemaphore.c \
                           lwjgl3/org_lwjgl_opengl_EXTSemaphoreFD.c \
                           lwjgl3/org_lwjgl_opengl_EXTSemaphoreWin32.c \
                           lwjgl3/org_lwjgl_opengl_EXTSeparateShaderObjects.c \
                           lwjgl3/org_lwjgl_opengl_EXTShaderFramebufferFetchNonCoherent.c \
                           lwjgl3/org_lwjgl_opengl_EXTShaderImageLoadStore.c \
                           lwjgl3/org_lwjgl_opengl_EXTStencilClearTag.c \
                           lwjgl3/org_lwjgl_opengl_EXTStencilTwoSide.c \
                           lwjgl3/org_lwjgl_opengl_EXTTextureArray.c \
                           lwjgl3/org_lwjgl_opengl_EXTTextureBufferObject.c \
                           lwjgl3/org_lwjgl_opengl_EXTTextureInteger.c \
                           lwjgl3/org_lwjgl_opengl_EXTTimerQuery.c \
                           lwjgl3/org_lwjgl_opengl_EXTTransformFeedback.c \
                           lwjgl3/org_lwjgl_opengl_EXTVertexAttrib64bit.c \
                           lwjgl3/org_lwjgl_opengl_EXTWin32KeyedMutex.c \
                           lwjgl3/org_lwjgl_opengl_EXTWindowRectangles.c \
                           lwjgl3/org_lwjgl_opengl_EXTX11SyncObject.c \
                           lwjgl3/org_lwjgl_opengl_GL11.c \
                           lwjgl3/org_lwjgl_opengl_GL11C.c \
                           lwjgl3/org_lwjgl_opengl_GL12C.c \
                           lwjgl3/org_lwjgl_opengl_GL13.c \
                           lwjgl3/org_lwjgl_opengl_GL13C.c \
                           lwjgl3/org_lwjgl_opengl_GL14.c \
                           lwjgl3/org_lwjgl_opengl_GL14C.c \
                           lwjgl3/org_lwjgl_opengl_GL15C.c \
                           lwjgl3/org_lwjgl_opengl_GL20C.c \
                           lwjgl3/org_lwjgl_opengl_GL21C.c \
                           lwjgl3/org_lwjgl_opengl_GL30C.c \
                           lwjgl3/org_lwjgl_opengl_GL31C.c \
                           lwjgl3/org_lwjgl_opengl_GL32C.c \
                           lwjgl3/org_lwjgl_opengl_GL33.c \
                           lwjgl3/org_lwjgl_opengl_GL33C.c \
                           lwjgl3/org_lwjgl_opengl_GL40C.c \
                           lwjgl3/org_lwjgl_opengl_GL41C.c \
                           lwjgl3/org_lwjgl_opengl_GL42C.c \
                           lwjgl3/org_lwjgl_opengl_GL43C.c \
                           lwjgl3/org_lwjgl_opengl_GL44C.c \
                           lwjgl3/org_lwjgl_opengl_GL45.c \
                           lwjgl3/org_lwjgl_opengl_GL45C.c \
                           lwjgl3/org_lwjgl_opengl_GL46C.c \
                           lwjgl3/org_lwjgl_opengl_GREMEDYFrameTerminator.c \
                           lwjgl3/org_lwjgl_opengl_GREMEDYStringMarker.c \
                           lwjgl3/org_lwjgl_opengl_INTELFramebufferCMAA.c \
                           lwjgl3/org_lwjgl_opengl_INTELMapTexture.c \
                           lwjgl3/org_lwjgl_opengl_INTELPerformanceQuery.c \
                           lwjgl3/org_lwjgl_opengl_KHRBlendEquationAdvanced.c \
                           lwjgl3/org_lwjgl_opengl_KHRParallelShaderCompile.c \
                           lwjgl3/org_lwjgl_opengl_NVAlphaToCoverageDitherControl.c \
                           lwjgl3/org_lwjgl_opengl_NVBindlessMultiDrawIndirect.c \
                           lwjgl3/org_lwjgl_opengl_NVBindlessMultiDrawIndirectCount.c \
                           lwjgl3/org_lwjgl_opengl_NVBindlessTexture.c \
                           lwjgl3/org_lwjgl_opengl_NVBlendEquationAdvanced.c \
                           lwjgl3/org_lwjgl_opengl_NVClipSpaceWScaling.c \
                           lwjgl3/org_lwjgl_opengl_NVCommandList.c \
                           lwjgl3/org_lwjgl_opengl_NVConditionalRender.c \
                           lwjgl3/org_lwjgl_opengl_NVConservativeRaster.c \
                           lwjgl3/org_lwjgl_opengl_NVConservativeRasterDilate.c \
                           lwjgl3/org_lwjgl_opengl_NVConservativeRasterPreSnapTriangles.c \
                           lwjgl3/org_lwjgl_opengl_NVCopyImage.c \
                           lwjgl3/org_lwjgl_opengl_NVDepthBufferFloat.c \
                           lwjgl3/org_lwjgl_opengl_NVDrawTexture.c \
                           lwjgl3/org_lwjgl_opengl_NVDrawVulkanImage.c \
                           lwjgl3/org_lwjgl_opengl_NVExplicitMultisample.c \
                           lwjgl3/org_lwjgl_opengl_NVFence.c \
                           lwjgl3/org_lwjgl_opengl_NVFragmentCoverageToColor.c \
                           lwjgl3/org_lwjgl_opengl_NVFramebufferMixedSamples.c \
                           lwjgl3/org_lwjgl_opengl_NVFramebufferMultisampleCoverage.c \
                           lwjgl3/org_lwjgl_opengl_NVGPUMulticast.c \
                           lwjgl3/org_lwjgl_opengl_NVGPUShader5.c \
                           lwjgl3/org_lwjgl_opengl_NVHalfFloat.c \
                           lwjgl3/org_lwjgl_opengl_NVInternalformatSampleQuery.c \
                           lwjgl3/org_lwjgl_opengl_NVMemoryAttachment.c \
                           lwjgl3/org_lwjgl_opengl_NVMeshShader.c \
                           lwjgl3/org_lwjgl_opengl_NVPathRendering.c \
                           lwjgl3/org_lwjgl_opengl_NVPixelDataRange.c \
                           lwjgl3/org_lwjgl_opengl_NVPointSprite.c \
                           lwjgl3/org_lwjgl_opengl_NVPrimitiveRestart.c \
                           lwjgl3/org_lwjgl_opengl_NVQueryResource.c \
                           lwjgl3/org_lwjgl_opengl_NVQueryResourceTag.c \
                           lwjgl3/org_lwjgl_opengl_NVSampleLocations.c \
                           lwjgl3/org_lwjgl_opengl_NVScissorExclusive.c \
                           lwjgl3/org_lwjgl_opengl_NVShaderBufferLoad.c \
                           lwjgl3/org_lwjgl_opengl_NVShadingRateImage.c \
                           lwjgl3/org_lwjgl_opengl_NVTextureBarrier.c \
                           lwjgl3/org_lwjgl_opengl_NVTextureMultisample.c \
                           lwjgl3/org_lwjgl_opengl_NVTransformFeedback.c \
                           lwjgl3/org_lwjgl_opengl_NVTransformFeedback2.c \
                           lwjgl3/org_lwjgl_opengl_NVVertexArrayRange.c \
                           lwjgl3/org_lwjgl_opengl_NVVertexAttribInteger64bit.c \
                           lwjgl3/org_lwjgl_opengl_NVVertexBufferUnifiedMemory.c \
                           lwjgl3/org_lwjgl_opengl_NVViewportSwizzle.c \
                           lwjgl3/org_lwjgl_opengl_NVXConditionalRender.c \
                           lwjgl3/org_lwjgl_opengl_NVXGpuMulticast2.c \
                           lwjgl3/org_lwjgl_opengl_NVXProgressFence.c \
                           lwjgl3/org_lwjgl_opengl_OVRMultiview.c \
                           lwjgl3/org_lwjgl_stb_LibSTB.c \
                           lwjgl3/org_lwjgl_stb_STBDXT.c \
                           lwjgl3/org_lwjgl_stb_STBEasyFont.c \
                           lwjgl3/org_lwjgl_stb_STBImage.c \
                           lwjgl3/org_lwjgl_stb_STBImageResize.c \
                           lwjgl3/org_lwjgl_stb_STBImageWrite.c \
                           lwjgl3/org_lwjgl_stb_STBPerlin.c \
                           lwjgl3/org_lwjgl_stb_STBRectPack.c \
                           lwjgl3/org_lwjgl_stb_STBTruetype.c \
                           lwjgl3/org_lwjgl_stb_STBTTFontinfo.c \
                           lwjgl3/org_lwjgl_stb_STBVorbis.c \
                           lwjgl3/org_lwjgl_system_Callback.c \
                           lwjgl3/org_lwjgl_system_dyncall_DynCall.c \
                           lwjgl3/org_lwjgl_system_dyncall_DynCallback.c \
                           lwjgl3/org_lwjgl_system_dyncall_DynLoad.c \
                           lwjgl3/org_lwjgl_system_fcl_DynamicLinkLoader.c \
                           lwjgl3/org_lwjgl_system_JNI.c \
                           lwjgl3/org_lwjgl_system_jni_JNINativeInterface.c \
                           lwjgl3/org_lwjgl_system_libc_LibCErrno.c \
                           lwjgl3/org_lwjgl_system_libc_LibCLocale.c \
                           lwjgl3/org_lwjgl_system_libc_LibCStdio.c \
                           lwjgl3/org_lwjgl_system_libc_LibCStdlib.c \
                           lwjgl3/org_lwjgl_system_libc_LibCString.c \
                           lwjgl3/org_lwjgl_system_MemoryAccessJNI.c \
                           lwjgl3/org_lwjgl_system_MemoryUtil.c \
                           lwjgl3/org_lwjgl_system_ThreadLocalUtil.c \
                           lwjgl3/org_lwjgl_util_tinyfd_TinyFileDialogs.c \
                           lwjgl3/tinyfiledialogs.c

LOCAL_CFLAGS            := -O2 -Wall -c -fPIC -std=c99 -Wunused -DLWJGL_FCL -Wunused-value

include $(BUILD_SHARED_LIBRARY)