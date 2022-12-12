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
                           glfw/null_joystick.c \
                           glfw/egl_context.c \
                           glfw/osmesa_context.c \
                           glfw/posix_thread.c \
                           glfw/posix_time.c
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/fcl/include \
                           $(LOCAL_PATH)/glfw/include
LOCAL_CFLAGS            := -Wall -fuse-ld=gold -Werror=implicit-function-declaration
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