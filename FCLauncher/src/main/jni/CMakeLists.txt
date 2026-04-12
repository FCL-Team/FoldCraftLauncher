cmake_minimum_required(VERSION 3.18.1)

project("fcl")

# 设置C标准
set(CMAKE_C_STANDARD 11)
set(CMAKE_CXX_STANDARD 11)

# 查找ByteHook库
find_package(bytehook REQUIRED CONFIG)

# 设置全局包含目录
include_directories(
    ${CMAKE_CURRENT_SOURCE_DIR}
)

# 检查架构
if(${ANDROID_ABI} STREQUAL "arm64-v8a")
    add_definitions(-DADRENO_POSSIBLE)
endif()

# 模块：fcl
add_library(
    fcl
    SHARED
    fcl/fcl_bridge.c
    fcl/fcl_loader.c
)

target_include_directories(
    fcl
    PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}/fcl/include
    ${CMAKE_CURRENT_SOURCE_DIR}
)

target_link_libraries(
    fcl
    PRIVATE
    bytehook::bytehook
    log
    dl
    android
)

# 模块：driver_helper
add_library(
    driver_helper
    SHARED
    driver_helper/driver_helper.c
    driver_helper/nsbypass.c
)

target_compile_options(
    driver_helper
    PRIVATE
    -rdynamic
)

target_link_libraries(
    driver_helper
    PRIVATE
    log
    dl
    android
)

# 仅在arm64-v8a架构下链接EGL和GLESv2
if(${ANDROID_ABI} STREQUAL "arm64-v8a")
    target_link_libraries(
        driver_helper
        PRIVATE
        EGL
        GLESv2
    )
endif()

# 模块：linkerhook
add_library(
    linkerhook
    SHARED
    linkerhook/linkerhook.cpp
    linkerhook/linkerns.c
)

target_link_options(
    linkerhook
    PRIVATE
    -z global
)

# 模块：awt_headless - 占位库
add_library(
    awt_headless
    SHARED
    # 空库，仅作为占位
    ${CMAKE_CURRENT_SOURCE_DIR}/awt_headless/placeholder.c
)

# 为占位库添加必要的链接选项
target_link_libraries(
    awt_headless
    PRIVATE
    log
)

# 模块：awt_xawt
add_library(
    awt_xawt
    SHARED
    awt_xawt/xawt_fake.c
)

target_include_directories(
    awt_xawt
    PUBLIC
    ${CMAKE_CURRENT_SOURCE_DIR}/awt_xawt
)

target_link_libraries(
    awt_xawt
    PRIVATE
    awt_headless
)

# 模块：pojavexec_awt
add_library(
    pojavexec_awt
    SHARED
    awt/awt_bridge.c
)

target_link_libraries(
    pojavexec_awt
    PRIVATE
    fcl
)

# 模块：pojavexec
add_library(
    pojavexec
    SHARED
    bigcoreaffinity.c
    egl_bridge.c
    ctxbridges/loader_dlopen.c
    ctxbridges/gl_bridge.c
    ctxbridges/osm_bridge.c
    ctxbridges/egl_loader.c
    ctxbridges/osmesa_loader.c
    ctxbridges/swap_interval_no_egl.c
    environ/environ.c
    jvm_hooks/emui_iterator_fix_hook.c
    jvm_hooks/java_exec_hooks.c
    jvm_hooks/lwjgl_dlopen_hook.c
    input_bridge_v3.c
    virgl/virgl.c
    jre_launcher.c
    utils.c
)

target_include_directories(
    pojavexec
    PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${CMAKE_CURRENT_SOURCE_DIR}/ctxbridges
    ${CMAKE_CURRENT_SOURCE_DIR}/environ
    ${CMAKE_CURRENT_SOURCE_DIR}/virgl
)

target_link_libraries(
    pojavexec
    PRIVATE
    fcl
    driver_helper
    log
    dl
    android
)

# 仅在arm64-v8a架构下链接EGL和GLESv2
if(${ANDROID_ABI} STREQUAL "arm64-v8a")
    target_link_libraries(
        pojavexec
        PRIVATE
        EGL
        GLESv2
    )
endif()
