LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE            := fcl
LOCAL_SHARED_LIBRARIES  := bytehook
LOCAL_SRC_FILES         := fcl/fcl_bridge.c \
                           fcl/fcl_event.c \
                           fcl/fcl_loader.c
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/fcl/include
LOCAL_LDLIBS            := -llog -ldl -landroid
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_LDLIBS := -ldl -llog -landroid
LOCAL_MODULE := driver_helper
LOCAL_SRC_FILES := \
    driver_helper/driver_helper.c \
    driver_helper/nsbypass.c
LOCAL_CFLAGS += -rdynamic
ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
LOCAL_CFLAGS += -DADRENO_POSSIBLE
LOCAL_LDLIBS += -lEGL -lGLESv2
endif
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := linkerhook
LOCAL_SRC_FILES := \
    linkerhook/linkerhook.cpp \
    linkerhook/linkerns.c
LOCAL_LDFLAGS := -z global
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
LOCAL_MODULE            := pojavexec_awt
LOCAL_SHARED_LIBRARIES  := fcl
LOCAL_SRC_FILES         := awt/awt_bridge.c
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
# Link GLESv2 for test
LOCAL_LDLIBS := -ldl -llog -landroid
# -lGLESv2
LOCAL_MODULE := pojavexec
LOCAL_SHARED_LIBRARIES  := fcl driver_helper
# LOCAL_CFLAGS += -DDEBUG
# -DGLES_TEST
LOCAL_SRC_FILES := \
    bigcoreaffinity.c \
    egl_bridge.c \
    ctxbridges/loader_dlopen.c \
    ctxbridges/gl_bridge.c \
    ctxbridges/osm_bridge.c \
    ctxbridges/egl_loader.c \
    ctxbridges/osmesa_loader.c \
    ctxbridges/swap_interval_no_egl.c \
    environ/environ.c \
    input_bridge_v3.c \
    virgl/virgl.c \
    jre_launcher.c \
    lwjgl_dlopen_hook.c
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/
ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
LOCAL_CFLAGS += -DADRENO_POSSIBLE
LOCAL_LDLIBS += -lEGL -lGLESv2
endif
include $(BUILD_SHARED_LIBRARY)

$(call import-module,prefab/bytehook)