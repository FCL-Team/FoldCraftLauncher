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
LOCAL_MODULE            := fcl
LOCAL_SHARED_LIBRARIES  := xhook
LOCAL_SRC_FILES         := fcl/fcl_bridge.c \
                           fcl/fcl_event.c \
                           fcl/fcl_loader.c
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/xhook/include \
                           $(LOCAL_PATH)/fcl/include
LOCAL_LDLIBS            := -llog -ldl -landroid
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := glfw
LOCAL_SHARED_LIBRARIES  := fcl
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
                           glfw/platform.c \
                           glfw/posix_thread.c \
                           glfw/posix_time.c \
                           glfw/driver_helper.c \
                           driver_helper/nsbypass.c
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/fcl/include \
                           $(LOCAL_PATH)/glfw/include
LOCAL_CFLAGS            := -Wall
LOCAL_LDLIBS            := -llog -ldl -landroid
ifeq ($(TARGET_ARCH_ABI), arm64-v8a)
LOCAL_CFLAGS            += -DADRENO_POSSIBLE
LOCAL_LDLIBS            += -lEGL -lGLESv2
endif
include $(BUILD_SHARED_LIBRARY)

#ifeq ($(TARGET_ARCH_ABI), arm64-v8a)
include $(CLEAR_VARS)
LOCAL_MODULE            := linkerhook
LOCAL_SRC_FILES         := driver_helper/hook.c
LOCAL_LDFLAGS           := -z global
include $(BUILD_SHARED_LIBRARY)
#endif

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
LOCAL_SHARED_LIBRARIES  := fcl
LOCAL_SRC_FILES         := awt/awt_bridge.c
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := libffi
LOCAL_SRC_FILES         := lwjgl/libffi/$(TARGET_ARCH_ABI)/libffi.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := lwjgl
LOCAL_STATIC_LIBRARIES  := libffi
LOCAL_SHARED_LIBRARIES  := fcl
LOCAL_SRC_FILES         := lwjgl/fcl_hook.c \
						   lwjgl/common_tools.c \
                           lwjgl/org_lwjgl_system_Callback.c \
                           lwjgl/org_lwjgl_system_linux_DynamicLinkLoader.c \
                           lwjgl/org_lwjgl_system_JNI.c \
                           lwjgl/org_lwjgl_system_jni_JNINativeInterface.c \
                           lwjgl/org_lwjgl_system_libc_LibCErrno.c \
                           lwjgl/org_lwjgl_system_libc_LibCLocale.c \
                           lwjgl/org_lwjgl_system_libc_LibCStdio.c \
                           lwjgl/org_lwjgl_system_libc_LibCStdlib.c \
                           lwjgl/org_lwjgl_system_libc_LibCString.c \
                           lwjgl/org_lwjgl_system_libffi_FFICIF.c \
                           lwjgl/org_lwjgl_system_libffi_FFIClosure.c \
                           lwjgl/org_lwjgl_system_libffi_LibFFI.c \
                           lwjgl/org_lwjgl_system_MemoryAccessJNI.c \
                           lwjgl/org_lwjgl_system_MemoryUtil.c \
                           lwjgl/org_lwjgl_system_SharedLibraryUtil.c \
                           lwjgl/org_lwjgl_system_ThreadLocalUtil.c
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/lwjgl/libffi/$(TARGET_ARCH_ABI)
LOCAL_CFLAGS            := -O2 -Wall -c -fPIC -std=c99 -Wunused -DLWJGL_FCL -Wunused-value
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := lwjgl_tinyfd
LOCAL_SRC_FILES         := lwjgl/tinyfd/org_lwjgl_util_tinyfd_TinyFileDialogs.c \
                           lwjgl/tinyfd/tinyfiledialogs.c
LOCAL_CFLAGS            := -O2 -Wall -c -fPIC -std=c99 -Wunused -DLWJGL_FCL -Wunused-value
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := lwjgl_nanovg
LOCAL_SRC_FILES         := lwjgl/nanovg/org_lwjgl_nanovg_Blendish.c \
                           lwjgl/nanovg/org_lwjgl_nanovg_LibNanoVG.c \
                           lwjgl/nanovg/org_lwjgl_nanovg_NanoSVG.c \
                           lwjgl/nanovg/org_lwjgl_nanovg_NanoVG.c \
                           lwjgl/nanovg/org_lwjgl_nanovg_NanoVGGL2.c \
                           lwjgl/nanovg/org_lwjgl_nanovg_NanoVGGL3.c \
                           lwjgl/nanovg/org_lwjgl_nanovg_NanoVGGLES2.c \
                           lwjgl/nanovg/org_lwjgl_nanovg_NanoVGGLES3.c \
                           lwjgl/nanovg/org_lwjgl_nanovg_OUI.c
LOCAL_CFLAGS            := -O2 -Wall -c -fPIC -std=c99 -Wunused -DLWJGL_FCL -Wunused-value
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := lwjgl_stb
LOCAL_SRC_FILES         := lwjgl/stb/org_lwjgl_stb_LibSTB.c \
                           lwjgl/stb/org_lwjgl_stb_STBDXT.c \
                           lwjgl/stb/org_lwjgl_stb_STBEasyFont.c \
                           lwjgl/stb/org_lwjgl_stb_STBImage.c \
                           lwjgl/stb/org_lwjgl_stb_STBImageResize.c \
                           lwjgl/stb/org_lwjgl_stb_STBImageWrite.c \
                           lwjgl/stb/org_lwjgl_stb_STBPerlin.c \
                           lwjgl/stb/org_lwjgl_stb_STBRectPack.c \
                           lwjgl/stb/org_lwjgl_stb_STBTruetype.c \
                           lwjgl/stb/org_lwjgl_stb_STBTTFontinfo.c \
                           lwjgl/stb/org_lwjgl_stb_STBVorbis.c
LOCAL_CFLAGS            := -O2 -Wall -c -fPIC -std=c99 -Wunused -DLWJGL_FCL -Wunused-value
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := lwjgl_opengl
LOCAL_SRC_FILES         := lwjgl/opengl/org_lwjgl_opengl_AMDDebugOutput.c \
                           lwjgl/opengl/org_lwjgl_opengl_AMDDrawBuffersBlend.c \
                           lwjgl/opengl/org_lwjgl_opengl_AMDFramebufferMultisampleAdvanced.c \
                           lwjgl/opengl/org_lwjgl_opengl_AMDInterleavedElements.c \
                           lwjgl/opengl/org_lwjgl_opengl_AMDOcclusionQueryEvent.c \
                           lwjgl/opengl/org_lwjgl_opengl_AMDPerformanceMonitor.c \
                           lwjgl/opengl/org_lwjgl_opengl_AMDSamplePositions.c \
                           lwjgl/opengl/org_lwjgl_opengl_AMDSparseTexture.c \
                           lwjgl/opengl/org_lwjgl_opengl_AMDStencilOperationExtended.c \
                           lwjgl/opengl/org_lwjgl_opengl_AMDVertexShaderTessellator.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBBindlessTexture.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBBufferStorage.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBClearBufferObject.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBCLEvent.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBColorBufferFloat.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBComputeVariableGroupSize.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBDebugOutput.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBDrawBuffers.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBDrawBuffersBlend.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBDrawInstanced.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBES32Compatibility.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBFramebufferNoAttachments.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBGeometryShader4.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBGLSPIRV.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBGPUShaderFP64.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBGPUShaderInt64.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBImaging.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBIndirectParameters.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBInstancedArrays.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBMatrixPalette.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBMultisample.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBMultitexture.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBOcclusionQuery.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBParallelShaderCompile.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBPointParameters.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBRobustness.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBSampleLocations.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBSampleShading.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBShaderObjects.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBShadingLanguageInclude.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBSparseBuffer.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBSparseTexture.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBTextureBufferObject.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBTextureBufferRange.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBTextureCompression.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBTextureStorage.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBTextureStorageMultisample.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBTransposeMatrix.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBVertexAttrib64Bit.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBVertexAttribBinding.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBVertexBlend.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBVertexBufferObject.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBVertexProgram.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBVertexShader.c \
                           lwjgl/opengl/org_lwjgl_opengl_ARBWindowPos.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTBindableUniform.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTBlendColor.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTBlendEquationSeparate.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTBlendFuncSeparate.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTBlendMinmax.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTCompiledVertexArray.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTDebugLabel.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTDebugMarker.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTDepthBoundsTest.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTDirectStateAccess.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTDrawBuffers2.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTDrawInstanced.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTEGLImageStorage.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTExternalBuffer.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTFramebufferBlit.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTFramebufferBlitLayers.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTFramebufferMultisample.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTFramebufferObject.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTGeometryShader4.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTGPUProgramParameters.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTGPUShader4.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTMemoryObject.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTMemoryObjectFD.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTMemoryObjectWin32.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTPointParameters.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTPolygonOffsetClamp.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTProvokingVertex.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTRasterMultisample.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTSecondaryColor.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTSemaphore.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTSemaphoreFD.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTSemaphoreWin32.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTSeparateShaderObjects.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTShaderFramebufferFetchNonCoherent.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTShaderImageLoadStore.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTStencilClearTag.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTStencilTwoSide.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTTextureArray.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTTextureBufferObject.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTTextureInteger.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTTextureStorage.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTTimerQuery.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTTransformFeedback.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTVertexAttrib64bit.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTWin32KeyedMutex.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTWindowRectangles.c \
                           lwjgl/opengl/org_lwjgl_opengl_EXTX11SyncObject.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL11.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL11C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL12C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL13.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL13C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL14.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL14C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL15C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL20C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL21C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL30C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL31C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL32C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL33.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL33C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL40C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL41C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL42C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL43C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL44C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL45.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL45C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GL46C.c \
                           lwjgl/opengl/org_lwjgl_opengl_GREMEDYFrameTerminator.c \
                           lwjgl/opengl/org_lwjgl_opengl_GREMEDYStringMarker.c \
                           lwjgl/opengl/org_lwjgl_opengl_INTELFramebufferCMAA.c \
                           lwjgl/opengl/org_lwjgl_opengl_INTELMapTexture.c \
                           lwjgl/opengl/org_lwjgl_opengl_INTELPerformanceQuery.c \
                           lwjgl/opengl/org_lwjgl_opengl_KHRBlendEquationAdvanced.c \
                           lwjgl/opengl/org_lwjgl_opengl_KHRParallelShaderCompile.c \
                           lwjgl/opengl/org_lwjgl_opengl_MESAFramebufferFlipY.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVAlphaToCoverageDitherControl.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVBindlessMultiDrawIndirect.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVBindlessMultiDrawIndirectCount.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVBindlessTexture.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVBlendEquationAdvanced.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVClipSpaceWScaling.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVCommandList.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVConditionalRender.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVConservativeRaster.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVConservativeRasterDilate.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVConservativeRasterPreSnapTriangles.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVCopyImage.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVDepthBufferFloat.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVDrawTexture.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVDrawVulkanImage.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVExplicitMultisample.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVFence.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVFragmentCoverageToColor.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVFramebufferMixedSamples.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVFramebufferMultisampleCoverage.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVGPUMulticast.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVGPUShader5.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVHalfFloat.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVInternalformatSampleQuery.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVMemoryAttachment.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVMemoryObjectSparse.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVMeshShader.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVPathRendering.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVPixelDataRange.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVPointSprite.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVPrimitiveRestart.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVQueryResource.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVQueryResourceTag.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVSampleLocations.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVScissorExclusive.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVShaderBufferLoad.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVShadingRateImage.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVTextureBarrier.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVTextureMultisample.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVTimelineSemaphore.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVTransformFeedback.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVTransformFeedback2.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVVertexArrayRange.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVVertexAttribInteger64bit.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVVertexBufferUnifiedMemory.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVViewportSwizzle.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVXConditionalRender.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVXGpuMulticast2.c \
                           lwjgl/opengl/org_lwjgl_opengl_NVXProgressFence.c \
                           lwjgl/opengl/org_lwjgl_opengl_OVRMultiview.c
LOCAL_CFLAGS            := -O2 -Wall -c -fPIC -std=c99 -Wunused -DLWJGL_FCL -Wunused-value
include $(BUILD_SHARED_LIBRARY)