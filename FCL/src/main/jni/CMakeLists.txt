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
if (${ANDROID_ABI} STREQUAL "arm64-v8a")
    add_definitions(-DADRENO_POSSIBLE)
endif ()

# 模块：androidnsbypass - Android linker 命名空间绕过工具库
# （vendor 自 Amethyst-Android 的 androidnsbypass 子模块 616dc33，MIT 协议）
add_library(
        androidnsbypass
        SHARED
        androidnsbypass/android_linker_ns.cpp
        androidnsbypass/elf_soname_patcher.c
        androidnsbypass/nsbypass.c
        androidnsbypass/nsbypass_dlfcn.c
        androidnsbypass/utils.c
)

set_target_properties(
        androidnsbypass
        PROPERTIES
        CXX_STANDARD 17
        CXX_STANDARD_REQUIRED ON
)

target_include_directories(
        androidnsbypass
        PUBLIC
        ${CMAKE_CURRENT_SOURCE_DIR}/androidnsbypass/include
)

target_link_libraries(
        androidnsbypass
        PRIVATE
        log
)

# 模块：fcl
add_library(
        fcl
        SHARED
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

# 模块：linkerhook - 拦截 libvulkan 加载器以加载 Turnip 驱动
add_library(
        linkerhook
        SHARED
        driver_helper/internal_android_dlopen_hook/turnip/hook.c
)

target_link_libraries(
        linkerhook
        PRIVATE
        androidnsbypass
)

# 符号全局化，确保 libvulkan 加载器优先解析到 hook 的 android_dlopen_ext
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
        awt_bridge.c
)

# 模块：pojavexec
add_library(
    pojavexec
    SHARED
    egl_bridge.c
    ctxbridges/loader_dlopen.c
    ctxbridges/gl_bridge.c
    ctxbridges/osm_bridge.c
    ctxbridges/egl_loader.c
    ctxbridges/osmesa_loader.c
    ctxbridges/swap_interval_no_egl.c
    environ/environ.c
    input_bridge_v3.c
    virgl/virgl.c
    jre_launcher.c
    utils.c
    jvm_hooks/java_exec_hooks.c
    jvm_hooks/emui_iterator_fix_hook.c
    jvm_hooks/lwjgl_dlopen_hook.c
    native_hooks/exit_hook.c
    native_hooks/chmod_hook.c
    native_hooks/sdl_hook.c
    native_hooks/sdl_dlopen_hook.c
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
        androidnsbypass
        log
        dl
        android
        bytehook::bytehook
)

# 仅在arm64-v8a架构下链接EGL和GLESv2
if (${ANDROID_ABI} STREQUAL "arm64-v8a")
    target_link_libraries(
            pojavexec
            PRIVATE
            EGL
            GLESv2
    )
endif ()

# 模块：glxshim - 为 LWJGL 3.3.5 之前的版本提供 glXGetProcAddress 符号
add_library(
        glxshim
        SHARED
        driver_helper/glxshim/glxshim.c
)

target_link_libraries(
        glxshim
        PRIVATE
        pojavexec
)
