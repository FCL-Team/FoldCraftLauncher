cmake_minimum_required(VERSION 3.18.1)

project("fcl")

set(CMAKE_C_STANDARD 11)
set(CMAKE_CXX_STANDARD 11)

find_package(bytehook REQUIRED CONFIG)

include_directories(
    ${CMAKE_CURRENT_SOURCE_DIR}
)

if(${ANDROID_ABI} STREQUAL "arm64-v8a")
    add_definitions(-DADRENO_POSSIBLE)
endif()

add_library(
    fcl
    SHARED
    fcl/fcl_bridge.c
    fcl/fcl_event.c
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

if(${ANDROID_ABI} STREQUAL "arm64-v8a")
    target_link_libraries(
        driver_helper
        PRIVATE
        EGL
        GLESv2
    )
endif()

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

add_library(
    awt_headless
    SHARED
    awt_headless/placeholder.c
)

target_link_libraries(
    awt_headless
    PRIVATE
    log
)

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
    input_bridge_v3.c
    virgl/virgl.c
    jre_launcher.c
    lwjgl_dlopen_hook.c
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

if(${ANDROID_ABI} STREQUAL "arm64-v8a")
    target_link_libraries(
        pojavexec
        PRIVATE
        EGL
        GLESv2
    )
endif()

add_library(
    alsa_stub
    SHARED
    alsa_stub/alsa_stub.c
)

target_link_options(
    alsa_stub
    PRIVATE
    -Wl,-soname,libasound.so.2
)

target_link_libraries(
    alsa_stub
    PRIVATE
    log
    dl
)

add_library(
    jsound
    SHARED
    jsound/jsound.c
)

target_link_libraries(
    jsound
    PRIVATE
    OpenSLES
    log
    dl
    android
)